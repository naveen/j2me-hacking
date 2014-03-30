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

import javax.microedition.lcdui.Graphics;

/**
 * Components that wants to be animated notifies the animation thread here about that intent.
 * Current limitation is that only one component in a screen can be animated.
 *
 * @author Gavin Bong gavin.emploi@gmail.com
 * @deprecated replaced by {@see Animator}
 */
public class Shared {
    
    Animatable m_element;
    public int m_h, m_x, m_y;
    public int m_w;
    
    /**
     * Called by the Animation Thread at a fixed interval
     *
     * Threadsafe? YES
     */ 
    public synchronized void tick() 
    {
      while( m_element == null ) 
      {    
         try
         {
            wait();
         }
         catch (InterruptedException e) 
         {
         }
      }
      
      m_element.tick();
      m_element.refresh( m_x, m_y, m_w, m_h );
      notify();        
    }

    /**
     * Sets the next component that will be animated
     *
     * Threadsafe? YES
     */    
    public synchronized void setElement( Animatable element, int x, int y, int h, int w )
    {
        if (m_element != null && m_element.needAutoReset())
            m_element.reset();

        this.m_x = x;
        this.m_y = y;
        this.m_h = h;
        this.m_w = w;
        this.m_element = element;
        notify();        
    }
    
    public Shared() 
    {
    }    
}
