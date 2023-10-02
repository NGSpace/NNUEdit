package NNU.Editor.Utils;

import static java.lang.System.getProperty;
import static java.lang.System.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import NNU.Editor.App;

public class Utils {
	
	/**
	 * the name of the editor bcz maybe I will want to change it in the future (again).
	 */
	public static final String EDITORNAME = "NNUEdit";
	
	private Utils() {}

	/**
	 * returns the location of the jar
	 * @return the location of the jar
	 * @throws UnsupportedEncodingException
	 */
	public static String getProgramPath() throws UnsupportedEncodingException {
		URL url = App.class.getProtectionDomain().getCodeSource().getLocation();
		String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
		return new File(jarPath).getParentFile().getPath();
	}
	
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
	
	public static String tuneFileDialogResult(String res) {
		if (res != null&&!"/".equals(res.trim()) && !res.trim().isEmpty())
			return res;
		return null;
	}
	
	/**
	 * reads a file and returns it's contents in String form.
	 * @param path The path to file to read from.
	 * @return the contents of said file.
	 */
	public static String read(String path) {
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			StringBuilder strb = new StringBuilder();
			while (myReader.hasNextLine()) {
				strb.append(myReader.nextLine());
				strb.append('\n');
			}
			myReader.close();
			return strb.toString();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return "\000";
	}

}
