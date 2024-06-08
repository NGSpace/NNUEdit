package io.github.ngspace.nnuedit.menu.components;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.UIManager;

public class NodeIcon implements Icon {
	private static final int SIZE = 20;
	private static BufferedImage img = new BufferedImage(SIZE, SIZE, 2);
    private boolean expanded;
	
	static {
    	int[] xs = {8,SIZE-6,8};
    	int[] ys = {4,SIZE/2,SIZE-5};
    	Graphics2D g = img.createGraphics();
        g.setColor(UIManager.getColor("Tree.foreground"));
    	g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND ));
    	g.drawPolyline(xs, ys, 3);
	}
	
	
    public NodeIcon(boolean expanded) {this.expanded = expanded;}
    
    
    @Override public void paintIcon(Component c, Graphics gra, int x, int y) {
        Graphics2D g = (Graphics2D) gra.create(x, y, SIZE, SIZE);
        if (!expanded) g.rotate(Math.PI / 2d, SIZE / 2d, SIZE / 2d);
    	g.drawImage(img, 0, 0, null);
    }
    
    
    @Override public int getIconWidth() {return SIZE;}
    @Override public int getIconHeight() {return getIconWidth();}
}