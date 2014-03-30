/*
   Copyright 2006-2007 Gavin Bong

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.raverun;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Re-implementation of MIDP's List component
 *
 * @author Gavin Bong gavin.emploi@gmail.com
 * @author Marcel Ruff mr@marcelruff.info
 */
public class ListContainer extends Canvas implements CommandListener, Navigable
{
    private UiController m_uiController;    

    // TODO put this into the resource file
    public final static Command SELECT_COMMAND = new Command("Select", Command.SCREEN, 1);

    public final int canvasWidth;
    public final int canvasHeight;
    
    //START: ListElement related dimensions
    public final int HIGHLIGHT_XOFFSET;
    public final int HIGHLIGHT_WIDTH;
    public final int HIGHLIGHT_YOFFSET;
    //  END: ListElement related dimensions
    
    private Vector m_elements = new Vector();

    private int m_firstVisibleLine;
    private int m_lastVisibleLine;

    private int numOfPages;
    
    private int m_maxVisibleLines;

    private int XOFFSET;

    private int YOFFSET;

    private int currentSelectedElement = 0;
    
    // Vertical padding between each ListElement
    private static final int LINE_PADDING = 2;

    private boolean m_init;

    public final Animator m_shared;

    private Displayable m_originator;

    /**
     * Dimensions for the arrows.
     *
     * The 3 vertices of the triangle are stored from left to right
     */
    private final int[][] m_arrows_coordinates = new int[6][2];

    /**
     * Color scheme. Default colors are:
     * <ul>
     * <li>Font             RGB(255, 255, 255) = 0x00FFFFFF
     * <li>Background       RGB(102,   0,  51) = 0x00660033
     * <li>Arrow            RGB( 51, 255,   0) = 0x00FFFF00
     * <li>Highlight back   RGB(255, 102,   0) = 0x00FF6600
     * <li>Highlight border RGB(  0,   0,   0) = 0x00000000
     * </ul>
     */
    public final int[] m_colors;

    public static final int FONT = 0;
    public static final int BACK = 1;
    public static final int ARROW = 2;
    public static final int HLITE_BACK = 3;
    public static final int HLITE_LINE = 4;

    public static final int[] DEFAULT_THEME = new int[] { 0x00FFFFFF, 0x00660033, 0x00FFFF00, 0x00FF6600, 0x00000000 };
    
    public ListContainer( UiController uiController, String title, Animator animator ) 
    {
        this( uiController, title, animator, DEFAULT_THEME );
    }
 
    /**
     * @param uiController an instance of a UIController, the main handler for the MIDlet
     * @param animator an instance of {@code Animator}
     * @param colors an array of colors. Must contain 5 members. 
     * @see #m_colors
     */
    public ListContainer( UiController uiController, String title, Animator animator, int[] colors )
    {
        setTitle( title );        
        m_uiController = uiController;
        
        canvasWidth = getWidth();
        canvasHeight = getHeight();
        XOFFSET = CommonUtils.normalize( canvasWidth, 5 );
        YOFFSET = CommonUtils.normalize( canvasHeight, 8 );

        HIGHLIGHT_XOFFSET = CommonUtils.sizing( canvasHeight );
        HIGHLIGHT_YOFFSET = HIGHLIGHT_XOFFSET;
        HIGHLIGHT_WIDTH = canvasWidth-CommonUtils.normalize( canvasWidth, 9 );

        m_shared = animator;
        m_colors = colors;

        initArrows();
        addCommand( SELECT_COMMAND );
        setCommandListener(this);        
    }

    /**
     * @postcondition: the array <code>m_arrows_coordinates</code> is filled with the coordinates
     *                 of vertices for both the UP and DOWN arrow
     */
    private final void initArrows()
    {        
        int xMiddle = canvasWidth/2;
        int yMiddle = canvasHeight/2;
        
        // Down arrow
        m_arrows_coordinates[0][0] = xMiddle-(2*HIGHLIGHT_XOFFSET);
        m_arrows_coordinates[0][1] = canvasHeight-(2*HIGHLIGHT_XOFFSET);
        m_arrows_coordinates[1][0] = xMiddle;
        m_arrows_coordinates[1][1] = canvasHeight;
        m_arrows_coordinates[2][0] = xMiddle+(2*HIGHLIGHT_XOFFSET);
        m_arrows_coordinates[2][1] = m_arrows_coordinates[0][1];
        
        // Up arrow
        m_arrows_coordinates[3][0] = xMiddle-(2*HIGHLIGHT_XOFFSET);
        m_arrows_coordinates[3][1] = 2*HIGHLIGHT_XOFFSET;          
        m_arrows_coordinates[4][0] = xMiddle;
        m_arrows_coordinates[4][1] = 0;
        m_arrows_coordinates[5][0] = xMiddle+(2*HIGHLIGHT_XOFFSET);
        m_arrows_coordinates[5][1] = m_arrows_coordinates[3][1];      
    } 

    /**
     * This method must be called whenever you modify the collection of ListElements in the container
     * 
     * Thread safe? no 
     *
     * Sets up the Viewport
     */
    public void init()
    {
        m_firstVisibleLine = 0;        
        m_lastVisibleLine = (getNumberOfLines() == 0) ? 0 : calcMaximumVisibleLines() - 1;                        
        
        m_init = true;
    }    

    public void resetList()
    {
        if (m_elements != null)
            m_elements.removeAllElements();
        else
            m_elements = new Vector();

        resetSelectedElement();
        resetInitFlag();
    }

    private final void resetInitFlag()
    {
        m_init = false;        
    }

    public final void resetSelectedElement()
    {
        currentSelectedElement = 0;
    }

    /**
     * Gets the number of elements in the List.
     */
    public int size()
    {
        if (m_elements == null)
            return 0;
        return m_elements.size();
    }
    
    /**
     * Thread safe? no
     */
    public void viewportUp()
    {
        m_firstVisibleLine--;
        m_lastVisibleLine--;
//      _("\tviewport UP: firstVisibleLine=" + m_firstVisibleLine);
//      _("\tviewport UP:  lastVisibleLine=" + m_lastVisibleLine);
    }

    /**
     * Thread safe? no
     */    
    public void viewportDown()
    {
        m_firstVisibleLine++;
        m_lastVisibleLine++;        
//      _("\tviewport DOWN: firstVisibleLine=" + m_firstVisibleLine);
//      _("\tviewport DOWN:  lastVisibleLine=" + m_lastVisibleLine);
    }

    public void keyPressed(int keyCode) 
    {
        _("keyCode = " + keyCode);
        switch( getGameAction(keyCode) )
        {    
            case FIRE:
                _("Fire");
                commandAction( SELECT_COMMAND, this );
                break;
            case UP:
                _("Going: UP");
                moveUp();
                repaint();
                break;
            case DOWN:
                _("Going: DOWN");
                moveDown();
                repaint();
                break;
            default:
                break;
        }
    }
    
    private void moveUp() 
    {
        synchronized (m_shared)
        {
            if (currentSelectedElement > 0)
            {
                currentSelectedElement--;
            }

            if ( (currentSelectedElement < m_firstVisibleLine) && m_firstVisibleLine > 0)
            {
                viewportUp();
            }
        }
    }    
    
    private void moveDown() {
        int numberOfLines = getNumberOfLines();        
        
        synchronized (m_shared)
        {        
            if (numberOfLines > 1)
            {
                if (currentSelectedElement < numberOfLines-1)
                {
//                  _("\tpointer = " + currentSelectedElement);
                    currentSelectedElement++;
                }
            }        

            if ( m_firstVisibleLine == (numberOfLines-m_maxVisibleLines+1) )
            {
                return;
            }

            if (currentSelectedElement > m_lastVisibleLine)
            {
                viewportDown();
            }
        }
    }    

    private final boolean canMoveDown()
    {
        return ( (getNumberOfLines() > calcMaximumVisibleLines()) &&  
                 (m_lastVisibleLine != getNumberOfLines()-1) );
    }

    private final boolean canMoveUp()
    {
        return ( (m_firstVisibleLine != 0) );
    }
    
    private int calcNumberOfPages( final int pageSize, final int totalLines ) 
    {        
        if (numOfPages == 0)
        {
           numOfPages = (totalLines/pageSize) + (((totalLines % pageSize) != 0) ? 1 : 0);
        }
        return numOfPages;
    }        
    
    public void append(ListElement element) {
        if (m_elements != null)
        {
            m_elements.addElement( element );
            resetInitFlag();
        }
    }

    public int calcIncrementSize() {
        return (getSelectedFont().getHeight() + LINE_PADDING + (2 * ListElement.VERTICAL_MARGIN));
    }

    protected void paint(Graphics g)
    {
        if (!m_init)
        {
            _("ERROR: paint() cannot proceed until init() has been invoked");
            return;
        }

        g.setColor( m_colors[BACK] );
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        if (m_elements.isEmpty())
            return;

        calcMaximumVisibleLines();

        int numberOfLines = getNumberOfLines();

        ListElement anElement = null;
        int elementIndex = 0;        

        g.setFont( getSelectedFont() );                       

        {
            int invisibleLine = m_firstVisibleLine + m_maxVisibleLines;
            int firstInvisibleLine = (numberOfLines < invisibleLine) ? numberOfLines
                    : invisibleLine;
   
            for (int i = m_firstVisibleLine; i < firstInvisibleLine; i++, elementIndex++)
            {
                anElement = (ListElement) m_elements.elementAt(i);
                anElement.paint(g, XOFFSET, YOFFSET + (elementIndex * calcIncrementSize()), ( currentSelectedElement == i ));
            }

            if ( canMoveDown() )
            {
                g.setColor( m_colors[ARROW] );
                g.fillTriangle( m_arrows_coordinates[0][0], m_arrows_coordinates[0][1],
                                m_arrows_coordinates[1][0], m_arrows_coordinates[1][1],
                                m_arrows_coordinates[2][0], m_arrows_coordinates[2][1]
                              );
            }

            if ( canMoveUp() )
            {
                g.setColor( m_colors[ARROW] );
                g.fillTriangle( m_arrows_coordinates[4][0], m_arrows_coordinates[4][1],
                                m_arrows_coordinates[3][0], m_arrows_coordinates[3][1],
                                m_arrows_coordinates[5][0], m_arrows_coordinates[5][1]
                              );
            }
        }
    }

    private boolean isScrollable()
    {
        return getNumberOfLines() > m_maxVisibleLines;
    }    
    
    private int calcMaximumVisibleLines() {
        if (m_maxVisibleLines == 0) {
            m_maxVisibleLines = ((canvasHeight - YOFFSET) / calcIncrementSize());
        }
        return m_maxVisibleLines;
    }

    public Font getSelectedFont()
    {
        return CommonUtils.BOLD_MEDIUM;
    }

    private final int getNumberOfLines() {
        if (m_elements == null || m_elements.isEmpty())
            return 0;
        return m_elements.size();
    }

    /**
     * Debugging method to dump to console
     */
    public static void _(String s) {
        System.out.println(s);
    }

    /**
     * @see javax.microedition.lcdui.List#getSelectedIndex()
     */
    public int getSelectedIndex()
    {
        return currentSelectedElement;
    }
    
    public void commandAction(Command command, Displayable displayable) 
    {
        if (command == SELECT_COMMAND)
        {
            if (getNumberOfLines() > 0)
            {
                ListElement e = (ListElement)m_elements.elementAt( currentSelectedElement );
                _(e.getLabel() + ": selected");
                m_uiController.handle( e );
            }
        }
        else
        {
            m_uiController.commandAction( command, displayable );
        }
    }

    public void updateList( Runnable r )
    {
        m_uiController.runSerially( r );
    }    

    public Displayable getOriginator() 
    {
        return m_originator;
    }

    public void setOriginator(Displayable originator) 
    {
        m_originator = originator;
    }
}
