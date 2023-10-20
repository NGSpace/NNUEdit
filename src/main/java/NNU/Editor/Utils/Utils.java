package NNU.Editor.Utils;

import static java.lang.System.getProperty;
import static java.lang.System.out;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
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
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.border.EmptyBorder;

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
	
	/**
	 * opens the file dialog
	 * @param save whether to show a save button or not
	 * @return the selected file (returns null otherwise)
	 */
	public static String openFolderDialog() {
		String res = "\000";
		/*if (false) {
			out.println("Windows machine detected. opening swt folder dialog.");
	        Display display = new Display ();
	        Shell shell = new Shell (display);
	        // Don't show the shell.
	        // shell.open();
	        DirectoryDialog dialog = new DirectoryDialog(shell);
	        
	        res = dialog.open();
	        shell.close();
	        display.close();
		} else {*/
		out.println("Opening folder dialog.");
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Specify a folder to open");  
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal!=0) return null;
		try {
			res = chooser.getSelectedFile().getCanonicalPath();
		} catch (IOException e) {e.printStackTrace();}
		//}
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
			//InputStream is = new FileInputStream(myObj);
			Scanner scan = new Scanner(myObj, "Cp852");
			StringBuilder strb = new StringBuilder();
			/*int bit = is.read();
			while ((bit = is.read())!=-1) {
				strb.append((char)bit);
			}
			is.close();*/
			while (scan.hasNextLine()) {
				strb.append(scan.nextLine() + '\n');
			}
			scan.close();
			return strb.toString();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return "\000";
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
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
	    		BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public static Icon ResizeIcon(Icon c, int Width, int Height) {
		Image newimg = Utils.iconToImage(c).getScaledInstance(Width, Height,
			Image.SCALE_SMOOTH); // scale it the smooth way
		return new ImageIcon(newimg);
	}
	public static Image ResizeImage(Image c, int Width, int Height) {
		return c.getScaledInstance(Width, Height, Image.SCALE_SMOOTH);
	}
	public static class CustomBorder extends EmptyBorder {
		
		private static final long serialVersionUID = -2759389573930313650L;
	
		public CustomBorder(int top, int left, int bottom, int right) {
			super(top, left, bottom, right);
		}
	
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			((Graphics2D)g).setStroke(new BasicStroke(3));
				g.drawLine(0, height, width, height);
		}
		
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
	        Files
	            .walk(path) // Traverse the file tree in depth-first order
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
			//if (!path.toFile().delete())return;
			Files.delete(path);
		}
	}
}
