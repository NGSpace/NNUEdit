package NNU.Editor.Menus.Components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import NNU.Editor.App;

public interface FolderButton {
	
	public JButton getFolderButton();
	public App getApp();
	
	public static final int MINIMUM_DISTANCE = 50;
	
	public default int getBuffer() {
		return 5;
	}
	
	default void initjl(JComponent comp, JScrollPane sp) {
		
		getFolderButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		getFolderButton().setBounds(-1,0,60,70);
		getFolderButton().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getApp().toggleFolder();
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		comp.addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseDragged(MouseEvent e) {}
			@Override public void mouseMoved(MouseEvent e) {
				Point position = sp.getViewport().getViewPosition();
				getFolderButton().setVisible(e.getX()-position.getX()<MINIMUM_DISTANCE);
			}
		});
		comp.add(getFolderButton());
		resizeButton(sp);
	}
	
	public default void paintFB(Graphics g, JScrollPane sp) {
		JButton jl = getFolderButton();
		if (jl.isVisible()) {
			Point position = sp.getViewport().getViewPosition();
			resizeButton(sp);
			Graphics2D gra = (Graphics2D) g.create((int)position.getX() + getBuffer(),
					(int) (position.getY()+sp.getHeight()/2d-jl.getHeight()),
					jl.getWidth(), jl.getHeight());
			paintFolderButton(gra,jl.getWidth() + getBuffer(),jl.getHeight());
		}
	}
	
	public default void paintFolderButton(Graphics2D g, int w, int he) {
		if (!getFolderButton().isVisible()) return;
		int h = he - 5;
		g.setColor(App.MenuBG);
		g.fillOval(-(w/2), 0, w, h);
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, .3f));
	    ShadedpaintFolderButton((Graphics2D) g.create(0, 0, w, h),w + 10,h);
	    g.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, 1f));
		g.setColor(getFolderButton().getForeground().darker());
		g.setStroke(new BasicStroke(3));
		g.drawOval(-(w/2), 0, w, h-1);
		g.setColor(getFolderButton().getForeground());
		if (getApp().isFolderOpen()) {
			g.drawLine(1, he/2-2, w-40, 20);
			g.drawLine(1, he/2-2, w-40, 15+33);
		} else {
			g.drawLine(w-40, he/2 - 2, 6, 18);
			g.drawLine(w-40, he/2 - 2, 6, 15+33);
		}
	}
	
	default void ShadedpaintFolderButton(Graphics2D g, int w, int h) {
		g.setColor(App.MenuBG.darker().darker());
		g.fillOval(-(w/2), 0, w, h);
		g.setColor(getFolderButton().getForeground());
		g.setRenderingHint(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	}
	public default void resizeButton(JScrollPane sp) {
		resizeButton(sp,0);
	}
	
	public default void resizeButton(JScrollPane sp, int i) {
		Point position = sp.getViewport().getViewPosition();
    	getFolderButton().setLocation((int)position.getX()+i,
			(int) (position.getY()+sp.getHeight()/2d-getFolderButton().getHeight()));
	}
}
