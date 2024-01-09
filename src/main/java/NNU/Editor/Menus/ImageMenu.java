package NNU.Editor.Menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import NNU.Editor.App;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Interfaces.Window;

public class ImageMenu extends JPanel {

	private static final long serialVersionUID = 6893937793741839803L;
	public final Window window;
	public final App app;
	public final BufferedImage img;

	public ImageMenu(Window window, App app, File f) throws Exception {
		this.app = app;
		this.window = window;
		this.img = Utils.readImageFromFile(f);
		setLocation(App.screenSize.width/2-this.getSize().width/2,
				App.screenSize.height/2-this.getSize().height/2);
		setLayout(null);
        setBackground(new Color(10,10,12));
        setForeground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(img.getWidth() + App.getBuffer(),img.getHeight()));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Dimension d = getScaledDimension(new Dimension(img.getWidth(),img.getHeight()),this.getSize());
		g.drawImage(img, 0 + App.getBuffer(), 0,(int) d.getWidth(),(int) d.getHeight(), null);
		
		window.getScrollPane().paintSeperators((Graphics2D)g);
	}
	
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;
	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    } else if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }
	    return new Dimension(new_width, new_height);
	}
}

