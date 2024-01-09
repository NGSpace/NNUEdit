package NNU.Editor.Menus.Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JLabel;

import NNU.Editor.Settings;

public class SmartJLabel extends JLabel {
	
	private static final long serialVersionUID = 4289244094063883606L;
	
	protected final Settings set;
	
	protected boolean forceantialias = false;
	protected boolean underlineenabled = false;
	protected boolean underlinerounded = true;
	protected Color underlinecolor = getForeground();
	protected int underlinethickness = 3;
	protected boolean backoverriden = false;
	
	@Override public void setBackground(Color c) {
		backoverriden = true;
		super.setBackground(c);
	}
	
	public SmartJLabel(Settings stg) {set = stg;}

	@Override
	public void paint(Graphics gra) {
		if (getText()!=null) {
	        
	        Graphics2D g = (Graphics2D) gra;
			g.setFont(getFont());
			
			if (backoverriden && !isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			
			Stroke s = g.getStroke();
			
			if (set.getBoolean("editor.antialias")||forceantialias) {
				g.setRenderingHint(
			        RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}
			
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			FontMetrics fm = g.getFontMetrics();
			String[] lines = getText().split("\r\n|\r|\n");
			float LineHeight = fm.getAscent() + fm.getDescent() + (float) fm.getLeading();
			
			for (int i = lines.length-1;i>-1;i--) {
				
				/**
				 * Java 8, can't use  "int x = switch (...) {...};" :P
				 */
				int x = (getWidth() - g.getFontMetrics().stringWidth(lines[i])) / 2 -2;
				
				/**
				 * Can't be fucked to support the other 2... and Vertical Alignment...
				 */
				if(this.getHorizontalAlignment()==RIGHT) {
					x = (getWidth() - g.getFontMetrics().stringWidth(lines[i]));
				} else if (getHorizontalAlignment()==LEFT) {x = 0;}
				
				double y = (getHeight()/2d - LineHeight/1.5) + LineHeight/1.5 * i -
						(LineHeight/1.5 * countLines(getText()))/2 + LineHeight/4;

				if (getUnderlineEnabled()) {
					g.setColor(getUnderlineColor());
					g.setStroke(new BasicStroke(getUnderlineThickness(), 
							getUnderlineRounded()?1:0, getUnderlineRounded()?1:0));
					
					int underscoreY = (int) (y + LineHeight) + getUnderlineThickness() *2;
					
					g.drawLine(x, underscoreY, x + g.getFontMetrics().stringWidth(lines[i]), underscoreY);
				}
				g.setColor(getForeground());
				g.setStroke(s);
				drawString(g, lines[i], x, (int) y);
			}
			
			paintBorder(g);

		}
	}
	
	protected void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }
	
	protected int countLines(String str){
		String[] lines = str.split("\r\n|\r|\n");
		return lines.length;
	}
	
	public void setAntiAliasing(boolean antialias) {
		this.forceantialias = antialias;
	}
	
	public boolean getAntiAliasing() {
		return forceantialias;
	}
	
	public void setUnderlineEnabled(boolean underline) {
		this.underlineenabled = underline;
	}
	
	public boolean getUnderlineEnabled() {
		return underlineenabled;
	}
	
	public void setUnderlineRounded(boolean underline) {
		this.underlinerounded = underline;
	}
	
	public boolean getUnderlineRounded() {
		return underlinerounded;
	}
	
	public void setUnderlineColor(Color color) {
		this.underlinecolor = color;
	}
	
	public Color getUnderlineColor() {
		return underlinecolor;
	}
	
	public void setUnderlineThickness(int Thickness) {
		this.underlinethickness = Thickness;
	}
	
	public int getUnderlineThickness() {
		return underlinethickness;
	}
}
