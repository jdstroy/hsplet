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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;

/**
 *
 * @author jdstroy
 */
public class LoopMusicEventListener extends AbstractMusicEventListener {

    @Override
    public void onMusicStop(IMusicClip oldClip, IMusicClip newClip, SequencerMultiplexer sequencer) {
        try {
            if (oldClip == newClip) {
                newClip.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(LoopMusicEventListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(LoopMusicEventListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}