package NNU.Editor;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import NNU.Editor.Windows.Window;

public class Tab extends JButton implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1985094871969942L;
	public boolean showX = false;
	protected final App app;
	protected final Window window;
	
	public Tab(App app, Window window) {
		super();
		this.app = app;
		this.window = window;
		this.setOpaque(true);
		this.setBackground(App.MenuBG);
		this.setLayout(null);
		this.addMouseListener(this);
	}
	
	@Override
	public Font getFont() {
		return new Font("Arial", Font.BOLD, 20);
	}

	@Override
	public void paint(Graphics gra) {
		
		/* Draw background */
		
		Graphics2D g = (Graphics2D) gra;
		g.setColor(app.SelectedWindow.getTab()==this ?
				window.getComponent().getBackground() : App.MenuBG.brighter());
		g.setFont(getFont());
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(App.MenuBG.darker().darker());
		g.drawRect(0, 0, getWidth(), getHeight());
		
		/* Draw title */
		
		g.setFont(getFont());
		g.setColor(App.MenuFG);
		
		String str = window.getTitle();
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
	
	public boolean isOnX(int x, int y) {
		return (x>getWidth()-50&&x<getWidth()-20)&&
				(y>10&&y<getHeight()-10);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		e.getX();
		if (app.SelectedWindow!=this.window) app.setSelectedWindow(this.window);
		boolean close = false;
		if (MouseEvent.BUTTON2==e.getButton()) close = true;
		if (isOnX(e.getX(),e.getY())) close = true;
		if (close) app.closeSelectedWindow();
	}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {showX = true;}
	@Override public void mouseExited(MouseEvent e) {showX = false;}
}
