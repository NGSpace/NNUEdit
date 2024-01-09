package NNU.Editor.Menus.Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import NNU.Editor.App;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Interfaces.Window;

public class Tab extends JButton implements MouseListener, MouseMotionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1985094871969942L;
	public boolean showX = false;
	public boolean highlightX = false;
	protected final App app;
	public static int padding = 3;
	protected final Window window;
	
	public Rectangle rect;
	
	public Tab(App app, Window window) {
		super();
		this.app = app;
		this.window = window;
		this.setOpaque(true);
		this.setBackground(App.MenuBG);
		this.setLayout(null);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	@Override
	public Font getFont() {
		try {if (app!=null) return App.stng.getFont("tab.font");} catch (Exception e) {e.printStackTrace();}
		return new Font("Arial", Font.BOLD, 20);
	}
	@Override public void setIcon(Icon i) {super.setIcon(Utils.ResizeIcon(i, 40, 40));}

	@Override
	public void paint(Graphics gra) {
		rect = new Rectangle(getWidth()-50, 10, 30, getHeight()-20);
		
		/* Draw background */
		
		Graphics2D g = (Graphics2D) gra;
		g.setColor(app.contentpane.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(app.getSelectedWindow().getTab()==this ?
				window.getComponent().getBackground() : App.MenuBG);
		App.adjustAntialias(g,false);
		g.fillRoundRect(padding, 0, getWidth() - padding * 2, getHeight()*2, 30, 30);
		g.setColor(App.MenuBG.darker().darker());
		
		/* Draw title */
		String str = window.getTitle();

		g.setFont(getFont());
		
		g.setColor(App.MenuFG);
		
		int strwidth = g.getFontMetrics().stringWidth(str);
		int x = getWidth() / 2 - (strwidth / 2);
		int y = ((getHeight() - g.getFontMetrics().getHeight()) / 2) + g.getFontMetrics().getAscent();
		x = x<50&&getIcon()!=null?50:x;
		g.drawString(str, x, y);
		
		
		/* Draw X button */
		if (showX) {
			if (!highlightX) {
				g.setColor(App.MenuFG.brighter());
				g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			} else {
				//g.setColor(new Color(0,140,255,150));
				//g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 3, 3);
				g.setColor(Color.red.brighter());
				g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			}
			g.drawLine(getWidth()-50, 10, getWidth()-20, getHeight()-10);
			g.drawLine(getWidth()-20 , 10, getWidth()-50, getHeight()-10);
		} else if (!window.isSaved()) {
			g.setColor(App.MenuFG.brighter());
			g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.fillOval(getWidth()-50+10, getHeight()/2-5, 10, 10);
		}
		if (this.getIcon()!=null) {
			//Width 50
			getIcon().paintIcon(this, g, 10, 5);
		}
	}
	public Font scaleFont(String text, Rectangle rect, Graphics g, Font font) {
		float fontSize = 20;//font.getSize2D();
		Font font1 = g.getFont().deriveFont(fontSize);
		int width = g.getFontMetrics(font1).stringWidth(text);
		fontSize = (float) ((rect.width / (double) width ) * fontSize);
		return g.getFont().deriveFont(fontSize);
	}
	
	public boolean isOnX(int x, int y) {
		return rect.intersects(new Rectangle(x,y,1,1));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (MouseEvent.BUTTON2==e.getButton()||isOnX(e.getX(),e.getY())) {
			app.closeWindow(window);
		} else {
			app.setSelectedWindow(this.window);
		}
	}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {if (!showX) {showX = true;repaint();}}//{}
	@Override public void mouseExited(MouseEvent e) {if (showX) {showX = false;repaint();}}
	@Override public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (rect==null) return;
		if (rect.intersects(new Rectangle(e.getX(),e.getY(),1,1))) {
			if (!highlightX) {
				highlightX = true;
				repaint();
			}
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			if (highlightX) {
				highlightX = false;
				repaint();
			}
			this.setCursor(Cursor.getDefaultCursor());
		}
	}
}
