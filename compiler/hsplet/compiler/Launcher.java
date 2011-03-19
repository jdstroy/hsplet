package hsplet.compiler;

import java.io.IOException;

import edu.stanford.ejalbert.exception.BrowserLaunchingExecutionException;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class Launcher {

	/**
	 * �e���C���N���X���Ăяo�����߂̃����`���[
	 * @throws IOException 
	 * @throws BrowserLaunchingInitializingException 
	 * @throws BrowserLaunchingExecutionException 
	 * @throws UnsupportedOperatingSystemException 
	 * @throws InterruptedException 
	 * 
	 * @args
	 *            ���s���̈����B
	 *            <ol>
	 *            <li>GuiFrontEnd args... ���[�U�C���^�[�t�F�C�X��\���B</li>
	 *            <li>Compiler args... �R���p�C�����s�B</li>
	 *            <li>Tester args... �e�X�g���s�B</li>
	 *            </ol>
	 */
	public static void main(String[] args) throws IOException, UnsupportedOperatingSystemException,
			BrowserLaunchingExecutionException, BrowserLaunchingInitializingException, InterruptedException {

		if (args.length == 0) {
			args = new String[] { "GuiFrontEnd" };
		}

		String className = args[0];

		String[] subargs = new String[args.length - 1];
		System.arraycopy(args, 1, subargs, 0, args.length - 1);

		if (className.equals("GuiFrontEnd")) {
			GuiFrontEnd.main(subargs);
		} else if (className.equals("Compiler")) {
			Compiler.main(subargs);
		} else if (className.equals("Tester")) {
			Tester.main(subargs);
		}

	}
}
