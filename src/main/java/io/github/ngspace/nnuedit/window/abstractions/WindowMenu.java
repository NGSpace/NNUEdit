package io.github.ngspace.nnuedit.window.abstractions;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public abstract class WindowMenu extends JPanel {

	private static final long serialVersionUID = -8751980129886284869L;
	
	protected Window window;
	protected boolean paintSeperators = true;
	protected WindowMenu() {if (this instanceof Window thiz) {window = thiz;}}
	protected WindowMenu(Window window) {this.window = window;}
	@Override public void paint(Graphics g) {
		super.paint(g);
		if (paintSeperators) window.getScrollPane().paintSeperators((Graphics2D)g);
	}
}
