package io.github.ngspace.nnuedit.menu.prefrences.options;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class AOption {
	
	public static String FT = "Consolas";
	public static final int TEXT_SPACING = 15;
	public static Font f = new Font(FT, Font.BOLD, 24);
	
	public String name;
	public String key;
	public JPanel panel = new JPanel(null,true);
	public AOption(String name, String key) {
		panel.setOpaque(false);
		this.name = name;
		this.key = key;
	}
	public abstract JComponent getComponentWidth(int width);
	public abstract AOption create(String name, String key);
}
