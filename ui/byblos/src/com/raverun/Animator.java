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

/**
 * Components that wants to be animated notifies the animation thread here about
 * that intent. Current limitation is that only one component in a screen can be
 * animated.
 * 
 * @author Gavin Bong gavin.emploi@gmail.com
 * @author Marcel Ruff mr@marcelruff.info
 */
public class Animator implements Runnable {

    private Thread m_animator;
    private int delay = 200;

    private Animatable m_element;
    private int m_h, m_x, m_y;
    private int m_w;

    /**
     * Sets the animation refresh delay in milli seconds
     * 
     * @param delay
     */
    public void setDelay( int delay )
    {
        this.delay = delay;
    }

    /**
     * Called by the Animation Thread at a fixed interval Threadsafe? YES
     */
    public synchronized void tick()
    {
        while( m_element == null )
        {
            try
            {
                wait();
            } 
            catch( InterruptedException e )
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
        if( m_element != null && m_element.needAutoReset() )
            m_element.reset();

        this.m_x = x;
        this.m_y = y;
        this.m_h = h;
        this.m_w = w;
        this.m_element = element;
        notify();
    }

    public synchronized void startAnimatorThread()
    {
        if( m_animator == null )
        {
            m_animator = new Thread( this );
            m_animator.start();
        } 
        else
        {
            if( !m_animator.isAlive() )
                m_animator.start();
        }
    }

    public synchronized void stopAnimatorThread()
    {
        m_animator = null;
        setElement( Animatable.NOOP_TOKEN, 0, 0, 0, 0 );
        // should call m_animator.interrupt() but not supported in CLDC 1.0 / 1.1
    }

    public void run()
    {
        Thread currentThread = Thread.currentThread();

        long tickStartTime = 0L;
        long tickEndTime = 0L;
        int tickDuration = 0;
        
        while( currentThread == m_animator )
        {
            tickStartTime = System.currentTimeMillis();
            tick(); // blocks until notify()
            tickEndTime = System.currentTimeMillis();
            
            tickDuration = (int) (tickEndTime - tickStartTime);
            if( tickDuration < delay )
            {
                try
                {
                    Thread.sleep( delay - tickDuration );
                } 
                catch( InterruptedException ignored )
                {
                }
            }
        }
    }

}
