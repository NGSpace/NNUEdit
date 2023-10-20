package NNU.Editor;

import static java.lang.System.*;
import static java.lang.System.setOut;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import com.formdev.flatlaf.FlatDarkLaf;

import NNU.Editor.Utils.BSPS;
import NNU.Editor.Windows.PreferencesWindow;

public class Main {
	
	public static String Version = "Unknown";
	
	/**
     * the entry point of this weird ass code editor
     * @param args which file to open at startup (leave empty for no selected file)
	 * @throws IOException 
     */
	public static void main(String[] args) {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
	        Model model = reader.read(new FileReader("pom.xml"));
	        Version = model.getVersion();
		} catch (Exception e) {
			e.printStackTrace();
			err.println("Unable to read jar Version");
		}
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
        
        //setOut(new BSPS(out));
		
		final StringBuilder starttext = new StringBuilder();
		if (args.length>0) {
			starttext.append(args[0]);
			if (!new File(args[0]).exists()) {
				System.err.println("File or Folder Doesn't exist: " + args[0]);
				System.exit(1);
			}
		}
		
        SwingUtilities.invokeLater(() -> {
			try {
				App app = new App(starttext.toString());
				app.setVisible(true);
				Instant end = Instant.now();
				Duration timeElapsed = Duration.between(start, end);
				out.println("Milliseconds took to start: " + timeElapsed.toMillis());
				new Thread(() ->{
					try {
						Thread.sleep(3000);
						PreferencesWindow.prefwin = 2;
					} catch (InterruptedException e) {e.printStackTrace();}
				}).start();
			} catch (Exception e) {e.printStackTrace();}
		});
    }
}

class NodeIcon implements Icon {
	private static final int SIZE = 20;

    private char type;

    public NodeIcon(char type) {
        this.type = type;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(UIManager.getColor("Tree.background"));
        g.fillRect(x, y, SIZE - 1, SIZE - 1);

        g.setColor(UIManager.getColor("Tree.hash").darker());
        g.drawRect(x, y, SIZE - 1, SIZE - 1);

        g.setColor(UIManager.getColor("Tree.foreground"));
        
        g.drawLine(x + 2, y + SIZE / 2, x + SIZE - 3, y + SIZE / 2);
        if (type == '+') {
            g.drawLine(x + SIZE / 2, y + 2, x + SIZE / 2, y + SIZE - 3);
        }
    }

    public int getIconWidth() {
        return SIZE;
    }

    public int getIconHeight() {
        return SIZE;
    }
}
