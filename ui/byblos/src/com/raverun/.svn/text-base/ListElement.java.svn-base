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

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

/**
 * Models each choice in the List
 *
 * @author Gavin Bong gavin.emploi@gmail.com
 * @author Marcel Ruff mr@marcelruff.info
 */
public class ListElement implements Animatable 
{
    public static final int ICON_RESOLUTION = 16;
    public static final int LEFT_MARGIN = 2;
    public static final int VERTICAL_MARGIN = 2;
    public static final String DOTDOT = "..";
    
    private final ListContainer m_container;
    
    /**
     * Width of a string containing two periods
     */
    private final int ELIPSIS_WIDTH;
        
    /**
     * The actual string to be displayed. Has an added space character at the end.
     */
    private String m_label;
    
    public final String m_labelBase;                
    
    private String m_toBeDisplayed;
    
   /**
    * Associated icon (which can be null)
    */
    public final Image m_icon;
    
    /**
     * Whether there's a need to display the full label
     */
    public final boolean m_isTooLong;
    
    /**
     * width in pixels that can fit one line on the screen
     */
    public final int MAX_WIDTH;
    
    private boolean hasValidIcon = false; 
    
// animation counters
    private int start = 0;
    private boolean isAnimating = false;    
        
    public ListElement(ListContainer container, String label)
    {
        this( container, label, null );    
    }
    
    public ListElement(ListContainer container, String label, Image icon) 
    {
        m_container = container;
        setLabel( label );
        m_icon = icon;
        
        if (icon != null)
        {
            if ( m_icon.getWidth() == ListElement.ICON_RESOLUTION && m_icon.getHeight() == ListElement.ICON_RESOLUTION )
                hasValidIcon = true;
        }
        
        Font font = m_container.getSelectedFont();        

        ELIPSIS_WIDTH = calcElipsisWidth( font );       
        
        if (hasValidIcon)
        {
            MAX_WIDTH = ( m_container.HIGHLIGHT_WIDTH-2*m_container.HIGHLIGHT_YOFFSET)-LEFT_MARGIN-(2*ELIPSIS_WIDTH)-getSpaceForIcon();            
        }
        else
        {
            MAX_WIDTH = ( m_container.HIGHLIGHT_WIDTH-2*m_container.HIGHLIGHT_YOFFSET)-LEFT_MARGIN-(2*ELIPSIS_WIDTH);
        }
            
        StringBuffer baseLabel = new StringBuffer();
        m_isTooLong = !(canFit( font, baseLabel ));        
        m_labelBase = ( m_isTooLong ) ? baseLabel.toString() : DOTDOT; // assign a dummy string DOTDOT        
        ListContainer._(m_label.trim() + ".isTooLong=" + String.valueOf(m_isTooLong) + " |m_labelBase=" + m_labelBase);
    }
    
    /**
     * Thread safe? yes
     * Modifies the string to be displayed
     *
     * Assumption: <code>m_isTooLong</code> is true
     */
    public void tick()
    {        
        synchronized (m_container.m_shared)
        {
            String s = toDisplayable();
//            m_container._("\t=> (" + s + ")");
            m_toBeDisplayed = s;
            isAnimating = true;
        }
    }
    
    public void refresh( final int x, final int y, final int w, final int h )
    {
        m_container.updateList(
            new Runnable() {
                public void run() {
                    m_container.repaint( x, y, w, h );        
                    //m_container.repaint();
                    m_container.serviceRepaints();
                    Thread.yield();
                }
            }
        );
    }
    
    /**
     * sideeffect: increments the start pointer by one (wrap around)
     */
    private final String toDisplayable()
    {
        StringBuffer buf = new StringBuffer();        
        int p = start;
        
        Font font = m_container.getSelectedFont();
        
        for( int width=0; width < MAX_WIDTH; p=(p+1)%m_label.length() )
        {
            buf.append( m_label.charAt(p) );            
            width = font.stringWidth( buf.toString() );        
        }
        
        start = (start+1) % m_label.length();
        
        return buf.toString();
    }
    
    public void reset()
    {
        start = 0;
        isAnimating = false;
    }
    
    /**
     * postcondition: if the return value is false, then paramter <code>buf</code> contains the label modified with elipsis at the end
     *
     * @return true when <code>m_label</code> fits snugly onto one line, false otherwise
     */
    private boolean canFit(Font font, StringBuffer buf)
    {
        int start = 0;
        int end = 0;

        String source = m_label.trim();
        
        int srcLength = source.length();

        for( int width=0; width < MAX_WIDTH && end < srcLength; end++ )
            width = font.substringWidth( source, start, end - start );

        if( source.charAt( start ) == ' ' )
            start++;

        String s = source.substring( start, end );
        if ( s.length() < source.length() )
        {
            // it is impossible that the screen width of the device cannot accomodate at least 3 characters.
            // Based on this supposition, we will do little checking.
            buf.append( s.substring( 0, s.length()-2 ) );
            buf.append( DOTDOT ); 
        }
        return ( s.length() == source.length() );   
    }    
        
    public void paint(Graphics g, int x, int y, boolean isSelected) {
        //ListContainer._("rendering (" + m_label + ")");
                
        String toBeDisplayed = "";
        
        if (isAnimating)
            toBeDisplayed = m_toBeDisplayed;
        else
            toBeDisplayed = (m_isTooLong) ? m_labelBase: m_label.trim();
                
        if (isSelected)
        {
            int rect_x = x - m_container.HIGHLIGHT_XOFFSET;
            int rect_y = y - m_container.HIGHLIGHT_YOFFSET;
            int rect_h = g.getFont().getHeight() + 2*(m_container.HIGHLIGHT_YOFFSET);
//            m_container._("\t\trect_x=" + rect_x + " |rect_y=" + rect_y + " |rect_h=" + rect_h + " |font_h=" + g.getFont().getHeight());

            g.setColor( m_container.m_colors[ ListContainer.HLITE_BACK ] );
            g.fillRoundRect( rect_x,
                             rect_y,
                             m_container.HIGHLIGHT_WIDTH,
                             rect_h,
                             7, 7 );
            g.setColor(  m_container.m_colors[ ListContainer.HLITE_LINE ] );
            g.drawRoundRect( rect_x,
                             rect_y,
                             m_container.HIGHLIGHT_WIDTH,
                             rect_h,
                             7, 7 );
            g.setColor( m_container.m_colors[ ListContainer.FONT ] );
            g.drawString( toBeDisplayed, (hasValidIcon) ? x + LEFT_MARGIN + getSpaceForIcon() : x + LEFT_MARGIN, y, Graphics.TOP | Graphics.LEFT);

            if (!isAnimating)
                m_container.m_shared.setElement( ((m_isTooLong) ? this : null), rect_x, rect_y, rect_h, m_container.HIGHLIGHT_WIDTH );
        }
        else
        {
            g.setColor( m_container.m_colors[ m_container.FONT ] );
            g.drawString( toBeDisplayed, (hasValidIcon) ? x + LEFT_MARGIN + getSpaceForIcon() : x + LEFT_MARGIN, y, Graphics.TOP | Graphics.LEFT);
        }

        if (hasValidIcon)
            g.drawImage( m_icon, x + LEFT_MARGIN, y-1, Graphics.TOP | Graphics.LEFT );
    }

    private int calcElipsisWidth(Font font) {
        return font.stringWidth( DOTDOT );
    }
    
    private final int getSpaceForIcon()
    {
        return ListElement.ICON_RESOLUTION + 2;
    }
    
    public boolean needAutoReset() 
    { 
        return true;
    }
    
    public void setLabel(String label) 
    {
        m_label = ( (label==null) ? "" : label ) + "     ";
    }

    public String getLabel() 
    {
        return m_label.trim();
    }

}