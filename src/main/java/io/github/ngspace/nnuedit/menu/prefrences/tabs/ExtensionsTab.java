package io.github.ngspace.nnuedit.menu.prefrences.tabs;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JPanel;

import io.github.ngspace.nnuedit.asset_manager.StringTable;
import io.github.ngspace.nnuedit.asset_manager.extensions.ExtensionManager;
import io.github.ngspace.nnuedit.menu.prefrences.options.AOption;

public class ExtensionsTab extends APreferenceTab {
	
	public static int VARIABLE_SPACING = 30;
	public static int EXTENSION_SPACING = 10;
	public static int BUTTON_SPACING = 30;
	public static int BUTTONS_HEIGHT = 100;
	public static final Color selectedColor = new Color(10,100,200);
	public static final Font f = new Font(AOption.FT, Font.BOLD, 40);

	@Override public JComponent getComponent(int width) {
		JPanel options_panel = new JPanel(null, true);
		options_panel.setOpaque(false);
		int Y = 0;
		for (JComponent jc : ExtensionManager.getComponentsOfExtensions(width)) {
			jc.setBounds(0, Y, width, jc.getHeight());
			options_panel.add(jc);
			Y+=jc.getHeight()+EXTENSION_SPACING;
		}
		options_panel.setBounds(0, BUTTONS_HEIGHT, width, Y-VARIABLE_SPACING);
		return options_panel;
	}
	@Override public String getName() {return StringTable.get("options.extensions");}
}