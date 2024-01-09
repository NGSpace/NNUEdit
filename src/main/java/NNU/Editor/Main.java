package NNU.Editor;

import static java.lang.System.out;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
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

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;

import com.formdev.flatlaf.FlatDarkLaf;

import NNU.Editor.Menus.Components.NodeIcon;
import NNU.Editor.Utils.FancyPrint;
import NNU.Editor.Utils.UserMessager;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.PreferencesWindow;

public class Main {
	
	public static final String SYSTEM = /* Remember to change the dependencies! */ "universal";
	public static final String Version;
	public static String shouldEvenTry = "maybe";
	static {
		Version = 'v' + getVersion();
		
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("sun.java2d.uiScale", "1.0");
		
		//System.setProperty("sun.java2d.opengl", "true"); /* Do it yourself if you want to */
		
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("BRO RUNNING THIS ON A HEADLESS JVM/MACHINE XD");
			shouldEvenTry = "no"; /* Ik I could've just System.exit() I just wanted to make this joke xD */
		} else {
			shouldEvenTry = "worth a shot!";
	        System.setOut(new FancyPrint(System.out));
	        out.println("Version of NNUEdit : " + Version);
		}
		
        //System.setOut(new BSPS(out)); /* Shall never be used again :D */
	}
	
	/**
     * the entry point of this weird ass code editor
     * @param args which file to open at startup (leave empty for no selected file)
	 * @throws IOException 
     */
	public static void main(String[] args) {
		if (shouldEvenTry.equals("no")) {giveUpInstantly();}
		
		Thread.setDefaultUncaughtExceptionHandler((t,e) -> {e.printStackTrace();});
		Instant start = Instant.now();
        try {
        	out.println("Attempting to load flatlaf.");
        	FlatDarkLaf.setup();
    		UIManager.put("ScrollBar.buttonArrowColor", Color.white);
    		UIManager.put("ScrollBar.thumb", Color.black);
    		UIManager.put("ScrollBar.showButtons", true);
    		UIManager.put("ScrollBar.width", 22);
    		UIManager.put("ScrollBar.thumbArc", 22);
    		UIManager.put("ScrollBar.trackArc", 22);
    		UIManager.put("ScrollBar.thumbInsets", new Insets(4, 4, 4, 4));
    		UIManager.put("ScrollBar.trackInsets", new Insets(4, 4, 4, 4));
            UIManager.put("Tree.showDefaultIcons ", true);
        	UIManager.put("Tree.collapsedIcon", new IconUIResource(new NodeIcon('+')));
        	UIManager.put("Tree.expandedIcon",  new IconUIResource(new NodeIcon('-')));
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
        
        //Must be called first of all Swing code as this sets the look and feel to FlatDark.
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
		Instant end2 = Instant.now();
		Duration timeElapsed2 = Duration.between(start, end2);
		out.println("Milliseconds took to load ui: " + timeElapsed2.toMillis());
		
		final StringBuilder starttext = new StringBuilder();
		if (args.length>0) {
			starttext.append(args[0]);
			if (!new File(args[0]).exists()) {
				System.err.println("File or Folder Doesn't exist: " + args[0]);
				//System.exit(1);
			}
		}
		
        SwingUtilities.invokeLater(() -> {
			try {
				Thread.setDefaultUncaughtExceptionHandler((t,e) -> {e.printStackTrace();});
				App app = new App(starttext.toString());
				Instant end = Instant.now();
				Duration timeElapsed = Duration.between(end2, end);
				out.println("Total milliseconds took to start: " + timeElapsed.toMillis());
		        
		        if (App.stng.getBoolean("system.checkversion"))
			        try {
						URL url = new URI(App.stng.get("system.updatecheckurl")).toURL();
				        
				        String NEWEST = new BufferedReader(new InputStreamReader(url.openStream(), 
				                 StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
				        //NEWEST = "v1.3.0";
				        out.println("Newest release of NNUEdit : " + NEWEST);
				        String digitRegex = "[^\\d]";
				        String ne = NEWEST.replaceAll(digitRegex, "");
				        String ve = Version.replaceAll(digitRegex, "");
				        if (!"".equals(ne) && (!"".equals(ve)) && Utils.parseInt(ne)> Utils.parseInt(ve))
				        	UserMessager.showWarningDialogTB
				        		("editor.newversionpopup.title", "editor.newversionpopup");
			        } catch (Exception e) {
						System.out.println("Could not check for new version due to:");
						e.printStackTrace();
					}
				app.setVisible(true);
				new Thread(() ->{
					try {
						Thread.sleep(3000);
						PreferencesWindow.prefwin = 2;
					} catch (InterruptedException e) {e.printStackTrace();}
				}).start();
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
		            if (version == null) {
		                version = aPackage.getSpecificationVersion();
		            }
		        }
		    }
	    } catch (Exception e) {}
	    if (version == null) {
	        // we could not compute the version so use a blank
	        version = "1.2.0";
	    }
	    return version;
	}
	private static void giveUpInstantly() {
		System.exit(12);
	}
}
