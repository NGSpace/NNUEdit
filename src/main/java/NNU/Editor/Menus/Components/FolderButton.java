package NNU.Editor.Menus.Components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

import NNU.Editor.App;

public class FolderButton extends JButton {
	
	private App app;

	public FolderButton(App app) {
		this.app=app;
	}
	
	private static final long serialVersionUID = 1696619060167421515L;

	@Override
	protected void paintComponent(Graphics gra) {
		super.paintComponent(gra);
		paintButton(gra,0,0,false);
	}
	
	private void paintButton(Graphics gra, int x, int y,boolean shaded) {
		Graphics2D g = (Graphics2D)gra;
		if (shaded)
			g.drawString("rr",x, y);
		int w = getWidth();
		int he = getHeight();

		int h = he - 5;
		g.setColor(App.MenuBG);
		g.fillOval(x-(w/2), y, w, h);
		App.adjustAntialias(g,false);
	    g.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, .3f));
	    if (!shaded)
	    	paintButton((Graphics2D) g.create(0, 0, w, h),w + 10,h,true);
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g.setColor(getForeground().darker());
		g.setStroke(new BasicStroke(3));
		g.drawOval(x+-(w/2), y, w, h-1);
		g.setColor(getForeground());
		if (app.isFolderOpen()) {
			g.drawLine(x+1, y+he/2-2, x+w-40, y+20);
			g.drawLine(x+1, y+he/2-2, x+w-40, y+15+33);
		} else {
			g.drawLine(x+w-40, y+he/2 - 2, x+6, y+18);
			g.drawLine(x+w-40, y+he/2 - 2, x+6, y+15+33);
		}
	}

}
