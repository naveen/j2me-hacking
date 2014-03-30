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

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

/**
 * @author Gavin Bong gavin.emploi@gmail.com
 * @author Marcel Ruff mr@marcelruff.info
 */
public class MidletHandler implements CommandListener, UiController
{
    public MainMidlet mMidlet;
    private Display mDisplay;

    ListContainer mMainMenuUI;
    ListContainer m_menuWithIcons;    
    ListContainer m_menuNoIcons;    
    
    public static final Command mExitCommand = new Command("Exit", Command.EXIT, 10);
    public static final Command mBackCommand = new Command("Back", Command.BACK, 8 );
     
    private Animator m_shared = new Animator();
    
    private ListElement leNoIcons;
    private ListElement leWithIcons;    
    
    public MidletHandler( MainMidlet midlet )
    {
        mMidlet = midlet;
        mDisplay = Display.getDisplay( midlet );        
    }
    
    public void init() 
    {
        initMainMenuUI();
        initMenuWithIcons();
        initMenuNoIcons();        
        mDisplay.setCurrent( mMainMenuUI );
        m_shared.startAnimatorThread();
    }    
    
    public void initMenuWithIcons()
    {
        if (m_menuWithIcons == null)
        {
            m_menuWithIcons = new ListContainer( this, "Select city to bomb", m_shared );    
            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Kiryat Shmona", getImage(0)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Taumatawhaka tangihangakoauauotamate aturipukakapikimaunga horonukupo kaiwhenuakita natahu", getImage(1)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Kota Kinabalu", getImage(2)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Malmo", getImage(3)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Krungthepmahanakornamornratanakosinmahintarayutthaya", getImage(4)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Haifa", getImage(5)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Beirut", getImage(6)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "El Pueblo de Nuestra Se\u00F1ora la Reina de los Angeles de Porci\u00FAncula", getImage(7)) );            
            m_menuWithIcons.append(new ListElement(m_menuWithIcons, "Seattle", getImage(8)) );
            m_menuWithIcons.setOriginator( mMainMenuUI );
            m_menuWithIcons.addCommand( mBackCommand );            
            m_menuWithIcons.init();        
        }
    }

    public void initMainMenuUI()
    {
        if (mMainMenuUI == null)
        {
            mMainMenuUI = new ListContainer( this, "List demo", m_shared );   
            leNoIcons = new ListElement( mMainMenuUI, "No icons" );
            leWithIcons = new ListElement( mMainMenuUI, "With icons" );
            mMainMenuUI.append( leNoIcons );
            mMainMenuUI.append( leWithIcons );
            mMainMenuUI.addCommand( mExitCommand );             
            mMainMenuUI.init();
        }
    }
    
    public void initMenuNoIcons()
    {
        if (m_menuNoIcons == null)
        {
            int[] THEME = new int[] { 0x00FFFFFF, 0x00330066, 0x00FFFF00, 0x00336633, 0x00FFFF00 };
            
            m_menuNoIcons = new ListContainer( this, "Random stuff", m_shared, THEME );   
            
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "One"));
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "Two"));
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "About"));
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "Pirates of the Carribean: Dead Man's chest"));        
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "An American Haunting"));        
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "Garfield: A tail of two kittens"));        
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "pneumonoultramicroscopicsilicovolcanoconiosis"));
            m_menuNoIcons.append(new ListElement(m_menuNoIcons, "0123456789012345678901234567890123456789"));
            m_menuNoIcons.setOriginator( mMainMenuUI );
            m_menuNoIcons.addCommand( mBackCommand );
            m_menuNoIcons.init();
        }
    }

    public void handle( ListElement selected )
    {
        if (selected == leNoIcons )
        {
            mDisplay.setCurrent( m_menuNoIcons );
        }
        else if (selected == leWithIcons )
        {
            mDisplay.setCurrent( m_menuWithIcons );
        }
    }    
    
    
    public void commandAction(Command command, Displayable displayable)
    {
        if( command == MidletHandler.mExitCommand )
        {
            exitRequested();
        }
        else if( command == MidletHandler.mBackCommand )
        {            
            Displayable previous = ((Navigable)displayable).getOriginator();
            mDisplay.setCurrent( previous );
        }        
    }

    public void stopAnimatorThread()
    {
        m_shared.stopAnimatorThread();
    }
    
    public void exitRequested()
    {
        mMidlet.exit();
    }
    
    public final void runSerially(Runnable runnable)
    {
        mDisplay.callSerially( runnable );
    }    
    
    String[] images = new String[] { "emoticon_evilgrin", "emoticon_grin", "emoticon_happy", "emoticon_smile", "emoticon_surprised", "emoticon_tongue", "emoticon_unhappy", "emoticon_waii", "emoticon_wink" };
    
    public final Image getImage(int index)
    {
        Image image = null;
        if (index <0 || index>8)
            return null;
        
        try
        {
            image = Image.createImage( "/" + images[index] + ".png" );
            return image;
        }
        catch ( IOException e )
        {
            ListContainer._(e.getMessage());
            return null;
        }         
    }

}
