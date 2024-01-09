package NNU.Editor.Utils;

import static java.lang.System.out;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import NNU.Editor.App;
import NNU.Editor.Main;
import NNU.Editor.AssetManagement.StringTable;
import net.sf.image4j.codec.ico.ICODecoder;

public class Utils {
	/**
	 * the name of the editor bcz maybe I will want to change it in the future (again).
	 */
	public static final String EFFECTIVE_EDITORNAME = "NNUEdit";//StringTable.getString("editor.name");
	public static String EDITORNAME = StringTable.getString("editor.name");
	
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
		if (!"universal".equals(Main.SYSTEM)) {
			//out.println("Windows machine detected. opening windows file dialog.");
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
			out.println("Universal system detected! Using universal file chooser");
	        JFileChooser chooser = new JFileChooser();
	        chooser.setDialogTitle("Specify a file to " + (save ? "Save" : "Open"));  
	        int returnVal = save ? chooser.showSaveDialog(null) : chooser.showOpenDialog(null);
	        if (returnVal!=0) return null;
			try {
				res = chooser.getSelectedFile().getCanonicalPath();
			} catch (IOException e1) {e1.printStackTrace();}
		}
		return res;
	}
	
	/**
	 * opens the file dialog
	 * @param save whether to show a save button or not
	 * @return the selected file (returns null otherwise)
	 */
	public static String openFolderDialog() {
		String res = "";
		if (!"universal".equals(Main.SYSTEM)) {
		    Display display = new Display();
		    Shell shell = new Shell(display);
		    //shell.open();
		    DirectoryDialog dialog = new DirectoryDialog(shell);
		    dialog.setFilterPath("c:\\"); // Windows specific
		    res = dialog.open();
		    display.dispose();
		} else {
			out.println("Opening folder dialog.");
	        JFileChooser chooser = new JFileChooser();
	        chooser.setDialogTitle("Specify a folder to open");  
	        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        int returnVal = chooser.showOpenDialog(null);
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
	
	public static String getEncoding() {
		String s = null;//System.getProperty("sun.jnu.encoding");
		return s==null?"Cp852":s;
	}
	
	/**
	 * reads a file and returns it's contents in String form.
	 * @param path The path to file to read from.
	 * @return the contents of said file.
	 */
	public static String read(String path) {
		try {
			File myObj = new File(path);
			Scanner scan = new Scanner(myObj, getEncoding());
			StringBuilder strb = new StringBuilder();
			while (scan.hasNextLine()) {
				strb.append(scan.nextLine() + '\n');
			}
			scan.close();
			return strb.toString();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * saves text to the path specified
	 * @param path the path
	 * @param text the text
	 * @return whether the operation was successful
	 */
	public static boolean save(String path, String text) {
		try {
			FileWriter fw = new FileWriter(path);
			fw.write(text);
		    fw.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getFileName(String file) {
		return new File(file).getName();
	}
	
	public static Image iconToImage(Icon icon) {
		if (icon instanceof ImageIcon) {
			return ((ImageIcon)icon).getImage();
		} else {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			BufferedImage image = new BufferedImage(w, h,
		            BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.setComposite(AlphaComposite.Src);
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
	}
	
	public static Icon ResizeIcon(Icon c, int Width, int Height) {
		Image newimg = Utils.iconToImage(c).getScaledInstance(Width, Height,
			Image.SCALE_SMOOTH); // scale it the smooth way
		return new ImageIcon(newimg);
	}
	public static Image ResizeImage(Image c, int Width, int Height) {
		return c.getScaledInstance(Width, Height, Image.SCALE_SMOOTH);
	}
	
	public static ImageIcon ReadImageIcon(String name) {
		return new ImageIcon(App.class.getResource("/NNU/Editor/Assets/Icons/" + name));
	}

	public static Icon getTransparency(float transparency, Icon icon) {
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		BufferedImage image = new BufferedImage(w, h,
	            BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transparency));
		icon.paintIcon(null, g, 0, 0);
		g.dispose();
		return new ImageIcon(image);
	}
	

	public static void delete(Path path) throws IOException {
		if (path.toFile().isDirectory()) {
	        Files.walk(path) // Traverse the file tree in depth-first order
	            .sorted(Comparator.reverseOrder())
	            .forEach(e -> {
	                try {
	                    System.out.println("Deleting: " + e);
	                    Files.delete(e);  //delete each file or directory
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                }
	            });
		} else {
			Files.delete(path);
		}
	}
	
	public static int parseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}
	public static int parseInt(char c) {
		return parseInt(String.valueOf(c));
	}
	static HashMap<String, String> filetypes = new HashMap<String, String>();
	
	static {
        String[] imagetypes = {"png","jpg", "jpeg","bmp","wbmp","gif","tiff","tif","webp","ico"};
		for (String fex : imagetypes)
			filetypes.put(fex, "img");
		filetypes.put("properties", "prop");
		filetypes.put("mp3","audio");
		filetypes.put("mp4","video");
	}

	public static String getFileType(String path) {
		if (path==null) return "";
		String res = filetypes.get(getFileExt(path).toLowerCase().trim());
		if (res==null)
			return "";
		return res;
	}

	public static String getFileExt(String path) {
		if (path==null) return "";
        String[] f = path.replace('\\', '/').split("/");
        String[] fi = f[f.length-1].split("[.]");
		return fi[fi.length-1];
	}

	public static String valueOf(Object val) {
		return val==null?"":val.toString();
	}
	
	public static BufferedImage readImageFromFile(File f) throws Exception {
		switch (getFileExt(f.getAbsolutePath()).toLowerCase()) {
			case "ico":
				return ICODecoder.read(f).get(0);
			default:
				return ImageIO.read(f);
		}
	}
}
