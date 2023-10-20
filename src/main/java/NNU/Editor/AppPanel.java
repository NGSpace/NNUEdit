package NNU.Editor;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import NNU.Editor.Utils.ValueNotFoundException;

public class AppPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 890330040427900164L;
	
	public static int RoundedBox = 20;
	
	private boolean painting = false;
	
	public final App app;
	
	public AppPanel(App app) {
		this.app = app;
	}
	
	@Override public void paint(Graphics gra) {
		if (painting) return;
		painting = true;super.paint(gra);
		if (app.getSelectedWindow()==null||!app.getSelectedWindow().getScrollPane().isVisible()) {
    		Graphics2D g = (Graphics2D) gra;
    		g.setFont(app.getTipFont());
    		g.setColor(App.MenuFG);
    		
    		try {
				if (app.stng.getBoolean("textantialias")) {
					g.setRenderingHint(
					        RenderingHints.KEY_TEXT_ANTIALIASING,
					        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		    		g.setRenderingHint(
		    		        RenderingHints.KEY_ANTIALIASING,
		    		        RenderingHints.VALUE_ANTIALIAS_ON);
				}
			} catch (ValueNotFoundException e) {
				e.printStackTrace();
			}
    		
    		String str = "You can open a new window by pressing CTRL + O";
    		int strwidth = g.getFontMetrics().stringWidth(str);
    		int x = getWidth() / 2 - (strwidth / 2) + 
    				(app.isFolderOpen() ? app.Folder.getWidth() : 0)/2;
    		
    		int y = ((getHeight() - g.getFontMetrics().getHeight()) / 2)
    				+ g.getFontMetrics().getAscent();
    		g.drawString(str, x, y);
    		g.setStroke(new BasicStroke(2));
    		g.drawRoundRect(x+strwidth-187, y-40, 194, 50, RoundedBox, RoundedBox);
		}painting = false;
	}
	
	@Override public void paintComponents(Graphics g) {
		for (Component i : this.getComponents())
			i.paint(g.create(i.getX(), i.getY(), i.getWidth(), i.getHeight()));
		
	}
}
