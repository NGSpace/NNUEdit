package io.github.ngspace.nnuedit;

import static io.github.ngspace.nnuedit.utils.Utils.EDITORNAME;
import static io.github.ngspace.nnuedit.utils.Utils.EFFECTIVE_EDITORNAME;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import static java.awt.GraphicsEnvironment.isHeadless;
import static java.lang.System.err;
import static java.lang.System.out;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;

import com.formdev.flatlaf.FlatDarkLaf;

import io.github.ngspace.nnuedit.asset_manager.StringTable;
import io.github.ngspace.nnuedit.asset_manager.extensions.ExtensionManager;
import io.github.ngspace.nnuedit.menu.components.NodeIcon;
import io.github.ngspace.nnuedit.utils.FancyPrint;
import io.github.ngspace.nnuedit.utils.UserMessager;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.utils.settings.Settings;

public class Main {
	
	public static Instant start;
	
	public static final String SYSTEM = /* Remember to change the dependencies and version! */ "universal";
	public static final String Version;
	public static String shouldEvenTry = "maybe";
	public static Settings settings;
	public static Settings theme;
	public static final String Location = null;
	public static Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static String getLocation() {return Location;}
	static {
		start = Instant.now();
		Utils.EFFECTIVE_EDITORNAME.chars();
		Version = 'v' + getVersion();
		
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("sun.java2d.uiScale", "1.0");
		
		//System.setProperty("sun.java2d.opengl", "true"); /* Do it yourself if you want to */
		
		if (isHeadless()) {
			out.println("BRO RUNNING THIS ON A HEADLESS JVM/MACHINE XD");
			shouldEvenTry = "no"; /* Ik I could've just System.exit() I just wanted to make this joke xD */
		} else {
			shouldEvenTry = "worth a shot!";
			FancyPrint stream = new FancyPrint(out);
	        System.setOut(stream);
	        System.setErr(stream);
	        out.println("Version of NNUEdit : " + Version);
		}
	    try {
	    	getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, 
	    		Utils.getAssetAsStream("Consolas.ttf")));
	    } catch(FontFormatException e){} catch (IOException e) {}
		
	    ImageIO.setUseCache(false);
	    
        try {
        	out.println("Attempting to load flatlaf.");
        	FlatDarkLaf.setup();
    		UIManager.put("ScrollBar.buttonArrowColor", new Color(0,0,0,0));
    		UIManager.put("ScrollBar.thumb", new Color(140,140,140,255));
    		UIManager.put("ScrollBar.background", new Color(20,20,20,255));
    		UIManager.put("ScrollBar.showButtons", false);
    		UIManager.put("ScrollBar.width", 14);
    		UIManager.put("ScrollBar.thumbArc", 22);
    		UIManager.put("ScrollBar.trackArc", 22);
    		final int i = 2;
    		UIManager.put("ScrollBar.thumbInsets", new Insets(i,i,i,i));
    		UIManager.put("ScrollBar.trackInsets", new Insets(i,i,i,i));
    		
            UIManager.put("Tree.showDefaultIcons ", true);
        	UIManager.put("Tree.collapsedIcon", new IconUIResource(new NodeIcon(false)));
        	UIManager.put("Tree.expandedIcon",  new IconUIResource(new NodeIcon(true)));
        	UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 18));
        	UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 16));
        } catch (Exception e){
			try {
	        	out.println("Unable to load flatlaf, loading system look and feel.");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {
	        	out.println("Unable to load system look and feel, using default.");
				ex.printStackTrace();
			}
			e.printStackTrace();
        }
        
        //Toolkit.getDefaultToolkit().setDynamicLayout(false);
		out.println("Milliseconds took to load ui: " + Duration.between(start, Instant.now()).toMillis());
	}
	
	/**
     * the entry point of this weird ass code editor
     * @param args which file to open at startup (leave empty for no selected file)
	 * @throws IOException 
     */
	public static void main(String[] args) {
		if (shouldEvenTry.equals("no")) {giveUpInstantly();}
		
		Thread.setDefaultUncaughtExceptionHandler((t,e) -> {e.printStackTrace();});
    	
		try {
			String str = Utils.getConfigFolderPath();
			
			File f = new File(str + EFFECTIVE_EDITORNAME + ".properties");
	    	settings = new Settings(f, new Settings(Utils.getAssetAsStream("NNUEdit.properties")).getMap());
	    	theme = settings;
	    	
	    	
	    	if ("ask".equals(settings.get("system.language"))) settings.set("system.language",UserMessager.comboInput
	    			("Select language","Select language",Utils.getLangMap(),Utils.getSelectedLang()));
	    	
	    	StringTable.loadLang(Utils.getSelectedLang());
	    	
	    	EDITORNAME = StringTable.get("system.name");
	    	
	    	Main.settings.addValueChangeListener((key, newVal, oldval, stng)-> {
	    		if ("system.language".equals(key)) {
	    	    	StringTable.loadLang(String.valueOf(newVal));
	    	    	EDITORNAME = StringTable.get("system.name");
	    	    	if (App.getInstance()!=null) App.getInstance().setTitle(EDITORNAME);
	    	    	UserMessager.showWarningDialogTB("system.restart.title", "system.restart", EDITORNAME);
	    		}
	    	});
		} catch (Exception e) {e.printStackTrace();/* There is no point in trying :P. */giveUpInstantly();}
		
		/* Load Extensions */
		try {
			ExtensionManager.LoadExtensions();
		} catch (Exception e) {
			e.printStackTrace();
			UserMessager.showErrorDialogTB("err.extensionloading.title", "err.extensionloading", e.getMessage());
			System.exit(0);
		}
		
		final StringBuilder starttext = new StringBuilder();
		if (args.length>0) {
			starttext.append(args[0]);
			if (!new File(args[0]).exists()&&!(args[0].isEmpty())) 
				{err.println("File or Folder Doesn't exist: " + args[0]);starttext.setLength(0);}
		}
		
        SwingUtilities.invokeLater(() -> {
			try {
				Thread.setDefaultUncaughtExceptionHandler((t,e) -> {e.printStackTrace();});
				App app = new App(starttext.toString());
				ExtensionManager.startApp(app);
				out.println("Total milliseconds took to start: "+Duration.between(start, Instant.now()).toMillis());
		        
		        if (settings.getBoolean("system.checkversion"))
			        try {
						URL url = new URI(settings.get("system.updatecheckurl")).toURL();
				        
				        String NEWEST = new BufferedReader(new InputStreamReader(url.openStream(), 
				                 StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
				        out.println("Newest release of NNUEdit : " + NEWEST);
				        String digitRegex = "[^\\d]";
				        String ne = NEWEST.replaceAll(digitRegex, "");
				        String ve = Version.replaceAll(digitRegex, "");
				        if (!"".equals(ne) && (!"".equals(ve)) && Utils.parseInt(ne)> Utils.parseInt(ve))
				        	UserMessager.showWarningDialogTB
				        		("system.newversionpopup.title", "system.newversionpopup");
			        } catch (Exception e) {
						System.out.println("Could not check for new version due to:");
						e.printStackTrace();
					}
				app.setBounds(0, 0, scrSize.width/2, scrSize.height/2);
				app.setLocationRelativeTo(null);
				app.setVisible(true);
				
			} catch (Exception e) {e.printStackTrace();}
		});
    }
	public static synchronized String getVersion() {
	    String version = null;
	    // try to load from maven properties first
	    try {
	        Properties p = new Properties();
	        InputStream is = Main.class.getResourceAsStream("/META-INF/maven/NNU/NNUEdit/pom.properties");
	        if (is != null) {
	            p.load(is);
	            version = p.getProperty("version", "");
	        }
	    } catch (Exception e) {}
	    try {
	    	// fallback to using Java API
		    if (version == null) {
		        Package aPackage = Main.class.getPackage();
		        if (aPackage != null) {
		            version = aPackage.getImplementationVersion();
		            if (version == null)
		                version = aPackage.getSpecificationVersion();
		        }
		    }
	    } catch (Exception e) {}
	    if (version == null) {
	        // we could not compute the version so use a blank
	        version = "1.4.2";
	    }
	    return version;
	}
	private static void giveUpInstantly() {System.exit(1);}
}
