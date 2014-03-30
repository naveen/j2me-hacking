/*
   Copyright 2006 Gavin Bong

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

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class MainMidlet extends MIDlet 
{    
    MidletHandler mController;
                
    public MainMidlet() 
    {
    }

    public void startApp() {
        ListContainer._("startApp()");
        if ( Display.getDisplay(this).getCurrent() == null ) 
        {
            if (mController == null)
            {
                mController = new MidletHandler( this );           
                mController.init();            
            }
        }
    }

    public void pauseApp() {
        ListContainer._("pauseApp()");
    }

    public void destroyApp(boolean unconditional) {
        if (mController != null)
            mController.stopAnimatorThread();
    }

    public void exit() {
        destroyApp(true);
        notifyDestroyed();
    }
}
