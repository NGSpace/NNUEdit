package io.github.ngspace.nnuedit.menu.prefrences.tabs;

import javax.swing.JComponent;

public abstract class APreferenceTab {
	public abstract JComponent getComponent(int width);
	public abstract String getName();
}
