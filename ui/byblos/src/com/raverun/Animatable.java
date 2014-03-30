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
 * Component that needs animation should implement this.
 * 
 * @author Gavin Bong gavin.emploi@gmail.com
 * @author Marcel Ruff mr@marcelruff.info
 */
public interface Animatable {

    /**
     * A dummy implementation of {@code Animatable} which does nothing.
     */
    public static final Animatable NOOP_TOKEN = new Animatable() {
        public void reset() {}
        public void refresh( final int x, final int y, final int w, final int h ) {}
        public void tick() {}
        public boolean needAutoReset() {
            return false;
        }
    };

    /**
     * Called by Animator thread after an animation tick.
     */
    void refresh( final int x, final int y, final int w, final int h );

    /**
     * Called by Animator thread for a new animation tick.
     */
    void tick();

    /**
     * Called by Animator when the animated element changes
     */
    void reset();

    /**
     * @return return true if you want to be called with reset()
     */
    boolean needAutoReset();
}
