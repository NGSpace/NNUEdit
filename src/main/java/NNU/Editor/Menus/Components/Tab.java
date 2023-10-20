package NNU.Editor.Menus.Components;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import NNU.Editor.App;
import NNU.Editor.Utils.ValueNotFoundException;
import NNU.Editor.Windows.Window;

public class Tab extends JButton implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1985094871969942L;
	public boolean showX = false;
	protected final App app;
	public static int padding = 3;
	protected final Window window;
	
	public Tab(App app, Window window) {
		super();
		this.app = app;
		this.window = window;
		this.setOpaque(true);
		this.setBackground(App.MenuBG);
		this.setLayout(null);
		this.addMouseListener(this);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	@Override
	public Font getFont() {
		if (app!=null)
			try {
				return new Font("Arial", Font.BOLD, app.stng.getInt("tabfontsize"));
			} catch (ValueNotFoundException e) {
				e.printStackTrace();
			}
		return new Font("Arial", Font.BOLD, 20);
	}

	@Override
	public void paint(Graphics gra) {
		
		/* Draw background */
		
		Graphics2D g = (Graphics2D) gra;
		g.setColor(app.contentpane.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(app.getSelectedWindow().getTab()==this ?
				window.getComponent().getBackground() : App.MenuBG);
		g.setFont(getFont());
		try {
			if (app.stng.getBoolean("textantialias"))
				g.setRenderingHint(
			        RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			else 
				g.setRenderingHint(
				        RenderingHints.KEY_TEXT_ANTIALIASING,
				        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		} catch (ValueNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g.fillRoundRect(padding, 0, getWidth() - padding * 2, getHeight()*2, 30, 30);
		//g.fill(roundedRectangle);
		g.setColor(App.MenuBG.darker().darker());
		//g.drawRect(0, 0, getWidth(), getHeight());
		
		/* Draw title */
		String str = window.getTitle();
		Rectangle rect = getBounds();

		g.setFont(getFont());
		//g.setFont(scaleFont(str,rect,g,getFont()));
		g.setColor(App.MenuFG);
		
		int strwidth = g.getFontMetrics().stringWidth(str);
		int x = getWidth() / 2 - (strwidth / 2);
		int y = ((getHeight() - g.getFontMetrics().getHeight()) / 2)
				+ g.getFontMetrics().getAscent();
		g.drawString(str, x, y);
		
		/* Draw X button */
		if (showX) {
			g.setColor(App.MenuFG.brighter());
			g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawLine(getWidth()-50, 10, getWidth()-20, getHeight()-10);
			g.drawLine(getWidth()-20 , 10, getWidth()-50, getHeight()-10);
    		//g.setStroke(new BasicStroke(2));
    		//g.drawRoundRect(getWidth()-50-3, 10-3, Math.abs(50-20)+3*2, getHeight()-10-10+3*2, 13, 13);
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
		return (x>getWidth()-50&&x<getWidth()-20)&&
				(y>10&&y<getHeight()-10);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (MouseEvent.BUTTON2==e.getButton()||isOnX(e.getX(),e.getY())) {
			if (app.getSelectedWindow()==this.window)
				app.closeSelectedWindow();
			else
				app.closeNotSelectedWindow(this.window);
		} else {
			app.setSelectedWindow(this.window);
		}
	}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {showX = true;}
	@Override public void mouseExited(MouseEvent e) {showX = false;}
}
