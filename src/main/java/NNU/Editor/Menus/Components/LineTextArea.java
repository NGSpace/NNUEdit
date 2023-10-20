package NNU.Editor.Menus.Components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import NNU.Editor.App;

public class LineTextArea extends JComponent {
	
	private static final long serialVersionUID = -7517531442764228733L;
	private final JTextComponent tx;

	public LineTextArea(JTextComponent textArea) {
		this.tx = textArea;
		//this.add(tx);
	}
	
	@Override public void paintComponent(Graphics g) {
		//super.paint(g);
		g.setColor(App.MenuBG);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(0, 0, 100, getHeight());
		g.setColor(Color.black);
		g.setFont(tx.getFont());
		int y = -10;
		String[] newlines = tx.getText().split("\n");
		for (int i = 0;i<newlines.length;i++) {
			y+=g.getFontMetrics().getHeight();
			g.drawString((i+1)+"", 0, y);
		}
		tx.paint(g.create(100, 0, getWidth()-100, getHeight()));
	}

}
