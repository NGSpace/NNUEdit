package io.github.ngspace.nnuedit.utils;

import static java.lang.System.out;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.asset_manager.StringTable;
import io.github.ngspace.nnuedit.utils.settings.Settings;
import net.sf.image4j.codec.ico.ICODecoder;

public class Utils {
	/**
	 * the name of the editor bcz maybe I will want to change it in the future (again).
	 */
	public static final String EFFECTIVE_EDITORNAME = "NNUEdit";//StringTable.getString("editor.name");
	public static String EDITORNAME = StringTable.get("editor.name");
	public static ImageIcon missingIcon = null;
	private static Instant s = Instant.now();
	public static ImageIcon getMissingIcon() {
		if (missingIcon==null) {
			try {missingIcon = new ImageIcon(ImageIO.read(Utils.getAssetAsStream("Icons/ui/missingicon.png")));
			} catch (Exception e) {e.printStackTrace();}
		}
		return missingIcon;
	}
	
	private Utils() {}

	/**
	 * returns the location of the jar
	 * @return the location of the jar
	 * @throws UnsupportedEncodingException
	 */
	public static String getProgramPath() throws UnsupportedEncodingException {
		if (String.valueOf(Main.getLocation())!="null") return Main.Location;
		URL url = App.class.getProtectionDomain().getCodeSource().getLocation();
		String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
		String fi = new File(jarPath).getParentFile().getPath();
		System.out.println(Duration.between(s, Instant.now()).toMillis());
		return fi;
	}

	/**
	 * returns the location of the jar
	 * @return the location of the jar
	 * @throws UnsupportedEncodingException
	 */
	public static String getConfigFolderPath() throws UnsupportedEncodingException {
		return Utils.getProgramPath() + "/" + EFFECTIVE_EDITORNAME + "/";
	}
	
	/**
	 * opens the file dialog
	 * @param save whether to show a save button or not
	 * @return the selected file (returns null otherwise)
	 */
	public static String openFileDialog(boolean save) {
		String res = null;
		if (!"universal".equals(Main.SYSTEM)) {
			Display display = new Display ();
	        Shell shell = new Shell (display);
	        
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
			try {res = chooser.getSelectedFile().getCanonicalPath();
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
			try {res = chooser.getSelectedFile().getCanonicalPath();
			} catch (IOException e) {e.printStackTrace();}
		}
		return res;
	}
	
	public static String tuneFileDialogResult(String res) {
		if (res != null&&!"/".equals(res.trim()) && !res.trim().isEmpty()) return res;
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
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.setComposite(AlphaComposite.Src);
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
	}
	
	public static ImageIcon ResizeIcon(Icon c, int Width, int Height) {
		Image newimg = Utils.iconToImage(c).getScaledInstance(Width, Height,Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}
	public static Image ResizeImage(Image c, int Width, int Height) {
		if (c.getWidth(null)==Width&&c.getHeight(null)==Height) return c;
		return c.getScaledInstance(Width, Height, Image.SCALE_SMOOTH);
	}

	public static ImageIcon ReadImageIcon(String name) {
		try {
			InputStream is;
			if ((is = Utils.getAssetAsStream("Icons/" + name))==null)
				return getMissingIcon();
			return new ImageIcon(ImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
			return getMissingIcon();
		}
	}
	
	public static ImageIcon ReadImageIcon(InputStream is) {
		try {
			if (is==null)
				return getMissingIcon();
			return new ImageIcon(ImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
			return getMissingIcon();
		}
	}

	public static Icon getTransparency(float transparency, Icon icon) {
		int w = icon.getIconWidth(), h = icon.getIconHeight();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transparency));
		icon.paintIcon(null, g, 0, 0); g.dispose();
		return new ImageIcon(image);
	}
	

	public static void delete(Path path) throws IOException {
		if (path.toFile().isDirectory())
	        Files.walk(path)
	            .sorted(Comparator.reverseOrder())
	            .forEach(e -> {
	                try {System.out.println("Deleting: " + e);Files.delete(e);
	                } catch (IOException ex) {ex.printStackTrace();}
	            });
		else Files.delete(path);
	}
	
	public static int parseInt(String str) {try {return Integer.parseInt(str);} catch (Exception e) {return 0;}}
	public static int parseInt(char c) {return parseInt(String.valueOf(c));}
	public static HashMap<String, String> filetypes = new HashMap<String, String>();
	public static String[] imagetypes = {"png","jpg", "jpeg","bmp","wbmp","gif","tiff","tif","webp","ico"}; static {
		for (String fex : imagetypes) filetypes.put(fex, "img");
		filetypes.put("mp3","audio");
		filetypes.put("mp4","video");
	}

	public static String getFileType(String path) {
		if (path==null) return "";
		String res = filetypes.get(getFileExt(path).toLowerCase().trim());
		if (res==null) return "";
		return res;
	}

	public static String getFileExt(String path) {
		if (path==null) return "";
        String[] f = path.replace('\\', '/').split("/");
        String[] fi = f[f.length-1].split("[.]");
		return fi[fi.length-1];
	}
	public static String getFileExt(File path) {return getFileExt(path.getAbsolutePath());}
	public static String valueOf(Object val) {return val==null?"":val.toString();}
	public static BufferedImage readImageFromFile(File f) throws Exception {
		switch (getFileExt(f.getAbsolutePath()).toLowerCase()) {
			case "ico": return ICODecoder.read(f).get(0);
			default:
				ImageReader ir = getReader(getFileExt(f.getAbsolutePath()));
				ImageReadParam defaultReadParam = ir.getDefaultReadParam();
				ir.setInput(ImageIO.createImageInputStream(new FileInputStream(f)), true);
				return ir.read(0, defaultReadParam);
		}
	}
	public static ImageReader getReader(String type) {
		final Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(type);
		if (iterator.hasNext()) return iterator.next();
		return null;
	}
	public static Color newColorWithAlpha(Color color, int alpha) {
	    return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
	public static <T> T getKeyByValue(Map<T, ? extends Object> c, Object icon) {
	    for (Entry<T, ? extends Object> entry : c.entrySet())
	        if (Objects.equals(icon, entry.getValue()))
	            return entry.getKey();
	    return null;
	}

	public static Icon getTransparency(double d, Icon iconOfFile) {return getTransparency((float) d, iconOfFile);}

	public static InputStream getResourceAsStream(String string) {
		return Utils.class.getResourceAsStream("/io/github/ngspace/nnuedit"+string);
	}
	public static InputStream getAssetAsStream(String string) {return getResourceAsStream("/Assets/"+string);}

	public static Map<String, Object> getLangMap() {
		Map<String, Object> m = getCensoredLangMap();
		m.put("Gen Z", "lmao");
		return m;
	}
	public static Map<String, Object> getCensoredLangMap() {
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("English", "en");
		return m;
	}
	public static String getSelectedLang() {
		Settings settings = Main.settings;
		if (getAssetAsStream("Lang/Lang_"+settings.get("system.language")+".properties")!=null)
			return settings.get("system.language");
		return System.getProperty("user.language.format");
	}
	public static String getUserLang() {return System.getProperty("user.language.format");}
}
