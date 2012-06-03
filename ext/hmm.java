
import hsplet.Context;
import hsplet.HSPError;
import hsplet.PEXInfo;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author jdstroy
 */
public class hmm extends FunctionBase {

    private static class DSObject implements DirectMediaObject {

        private String fileName;
        private boolean playing;
        private int fileSeekPosition;
        private boolean playWhenWindowIsActive;

        public DSObject(String fileName, int fileSeekPosition, boolean playWhenWindowIsActive) {
            this.fileName = fileName;
            this.fileSeekPosition = fileSeekPosition;
            this.playWhenWindowIsActive = playWhenWindowIsActive;
            playing = false;
        }

        public boolean isPlaying() {
            return playing;
        }

        public void play(boolean loop) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "play({0}) called but not implemented.", loop);
        }

        public void stop() {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "stop() called but not implemented.");
        }
    }

    private static interface DirectMediaObject {

        public boolean isPlaying();

        public void play(boolean loop);

        public void stop();
    }
    private Context context;

    public hmm(final Context context) {
        this.context = context;
    }
    public static final int DS_OK = 1, HMM_OK = 1, DS_ERR = 0, HMM_ERR = 0;
    private Map<Integer, DirectMediaObject> dsBuffers = new TreeMap<>();

    public void DSINIT(int bmscr_window_id, int a, int b, int c) {
        if (bmscr_window_id < 0 || bmscr_window_id >= context.windows.size() || context.windows.get(bmscr_window_id) == null) {
            context.error(HSPError.InvalidParameterValue, "grotate", "id==" + bmscr_window_id);
        }

        final Bmscr target = context.windows.get(bmscr_window_id);
        /*
         * Looks like we need not do anything at all!
         */

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DSEND() {
        Logger.getLogger(getClass().getName()).log(Level.WARNING,
                "DSEND() called but not implemented.");
        return DS_OK;
    }

    public int DSRELEASE(int bufferIndex) {
        if (dsBuffers.containsKey(new Integer(bufferIndex))) {
            dsBuffers.remove(new Integer(bufferIndex)).stop();
        }
        return DS_OK;
    }

    /**
     * Loads filename into the buffer specified by bufferIndex
     *
     * @param filename The filename to load
     * @param bufferIndex The buffer in which to load filename
     * @param fileSeekPosition Position to seek before playing the buffer
     * @param playWhenWindowIsActive If 0, only plays when this application is
     * active; if 1, always play this buffer
     * @return DS_OK on success, DS_ERR on failure.
     */
    public int DSLOADFNAME(@PEXInfo String filename, int bufferIndex,
            int fileSeekPosition, int playWhenWindowIsActive) {
        dsBuffers.put(new Integer(bufferIndex), new DSObject(filename,
                fileSeekPosition, playWhenWindowIsActive == 0));
        Logger.getLogger(getClass().getName()).log(Level.WARNING,
                "DSLOADFNAME(\"{0}\",{1},{2},{3}) called but not implemented "
                + "completely.",
                new Object[]{
                    filename, bufferIndex, fileSeekPosition,
                    playWhenWindowIsActive
                });
        return DS_OK;
    }

    /**
     * Plays buffer
     *
     * @param buffer The number of the buffer to play, from 0 to 1024
     * @param loop 0 for not looped, 1 for looped
     * @return DS_OK on success, DS_ERR on failure
     */
    public int DSPLAY(int buffer, int loop) {
        dsBuffers.get(new Integer(buffer)).play(loop == 1);
        return DS_OK;
    }

    public int DSSTOP(int buffer) {
        if (dsBuffers.containsKey(new Integer(buffer))) {
            dsBuffers.get(new Integer(buffer)).stop();
        }
        return DS_OK;
    }

    /**
     * DSSETVOLUME p1, p2
     *
     * Argument (from 0 to 1024) the number of buffers: p1
     *
     * (0 to 100) volume: p2 0, the minimum volume At 100, the maximum volume
     *
     * Return value (stat) 1 or DS_OK: success 0 or DS_ERR: failure
     *
     * Function to change the volume of the buffer of p1.
     *
     * @param buffer
     * @param inVolume
     * @return
     */
    public int DSSETVOLUME(int buffer, int inVolume) {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "DSSETVOLUME({0},{1}) called but not implemented.", new Object[]{buffer, inVolume});
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                try {
                    Line port = mixer.getLine(Port.Info.SPEAKER);
                    port.open();
                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                        Control volume = port.getControl(FloatControl.Type.VOLUME);
                        FloatControl fc = (FloatControl) volume;
                        fc.setValue((float) ((((float) inVolume) / 1024.0)
                                * (fc.getMaximum() - fc.getMinimum())
                                + fc.getMinimum()));
                        System.out.println(info);
                        System.out.println("- " + Port.Info.SPEAKER);
                        System.out.println("  - " + volume);
                    }
                    port.close();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(hmm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return DS_OK;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Gets the volume of the master wave channel
     *
     * @return The volume, or 0 for failure.
     */
    public int DSGETMASTERVOLUME() {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "DSGETMASTERVOLUME() called but not implemented.");
        return DS_ERR;
    }

    public int CHECKPLAY(int bufferIndex) {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "CHECKPLAY({0}) called but not implemented.", bufferIndex);
        return dsBuffers.get(new Integer(bufferIndex)).isPlaying() ? DS_OK : DS_ERR;
    }

    public int DMINIT(int bmscr, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DMEND(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DMLOADFNAME(@PEXInfo String filename, int z, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DMPLAY(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DMSTOP(int bmscr, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DIINIT(int bmscr, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DIGETJOYNUM(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int DIGETJOYSTATE(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Turns on bit position in target.
     *
     * @param target
     * @param position
     * @return
     */
    public int HMMBITON(Operand target, int index, int position) {
        target.or(index, Scalar.fromValue(1 << (position)), 0);
        return 1;
    }

    /**
     * Turns off bit position in target.
     *
     * @param target
     * @param position
     * @return
     */
    public int HMMBITOFF(Operand target, int index, int position) {
        target.and(0, Scalar.fromValue(~(1 << (position))), 0);
        return 1;
    }

    /**
     * Returns the value of the bit in position of target.
     *
     * @param target
     * @param position
     * @return
     */
    public int HMMBITCHECK(Operand target, int index, int position) {
        return ((target.toInt(index) >> position) & 1);
    }
}
