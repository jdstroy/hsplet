/*
 * Copyright 2012 John Stroy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hsplet.media.midi;

/**
 * Sequencer playback modes
 * @author jdstroy
 */
public enum SequencerPlaybackMode {

    /**
     * Plays the clip once.  Returns immediately.
     */
    PlaybackOnceAsynchronous(0),
    
    /**
     * Plays the clip repeatedly.  Returns immediately.
     */
    PlaybackRepeatAsynchronous(1),
    /**
     * Plays the clip once.  Blocks until the clip has finished playing.
     */
    PlaybackOnceSynchronous(2);
    private int value;

    SequencerPlaybackMode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
;

}