package io.github.ngspace.nnuedit.menu.prefrences.tabs;

import javax.swing.JComponent;
import javax.swing.JPanel;

import io.github.ngspace.nnuedit.asset_manager.StringTable;
import io.github.ngspace.nnuedit.menu.prefrences.PreferencesMenu;
import io.github.ngspace.nnuedit.menu.prefrences.options.AOption;
import io.github.ngspace.nnuedit.menu.prefrences.options.Header;

public class PreferenceTab extends APreferenceTab {
	public Header h;
	public PreferenceTab(Header h) {this.h = h;}

	@Override public JComponent getComponent(int w) {
		JPanel options_panel = new JPanel(null, true);
		options_panel.setOpaque(false);
		int Y = 0;
		for (AOption pref : h.components) {
			JComponent jc = pref.getComponentWidth(w);
			jc.setBounds(0, Y, w, jc.getHeight());
			options_panel.add(jc);
			Y+=jc.getHeight()+30;
		}
		options_panel.setBounds(0, PreferencesMenu.BUTTONS_HEIGHT, w, Y-30);
		return options_panel;
	}
	@Override public String getName() {return StringTable.get(h.name);}
}