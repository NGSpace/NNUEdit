package NNU.Editor;

import java.io.IOException;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import static java.lang.System.*;

public class Utils {
	
	/**
	 * the name of the editor bcz maybe I will want to change it in the future (again).
	 */
	public static final String EditorName = "NNUEdit";
	
	private Utils() {}
	
	/**
	 * opens the file dialog
	 * @param save whether to show a save button or not
	 * @return the selected file (returns null otherwise)
	 */
	public static String openFileDialog(boolean save) {
		String res = null;
		if (getProperty("os.name").contains("Windows")) {
			out.println("Windows machine detected. opening windows file dialog.");
	        Display display = new Display ();
	        Shell shell = new Shell (display);
	        // Don't show the shell.
	        // shell.open();
	        FileDialog dialog = new FileDialog (shell, save ? SWT.SAVE : SWT.OPEN);
	        dialog.open();
	        res = dialog.getFilterPath() + "/" + dialog.getFileName();
	        shell.close();
	        display.close();
		} else {
			out.println("Not windows machine detected. opening default file dialog.");
	        JFileChooser chooser = new JFileChooser();
	        chooser.setDialogTitle("Specify a file to " + (save ? "Save" : "Open"));  
	        int returnVal = save ? chooser.showSaveDialog(null) : chooser.showOpenDialog(null);
	        if (returnVal!=0) return null;
			try {
				res = chooser.getSelectedFile().getCanonicalPath();
			} catch (IOException e) {e.printStackTrace();}
		}
		return res;
	}

}
