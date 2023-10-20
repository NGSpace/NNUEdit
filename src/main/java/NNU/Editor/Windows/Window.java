package NNU.Editor.Windows;

import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Utils.ValueNotFoundException;

public interface Window {
	
	public static final int HEIGHT = 100;

	public boolean isSaved();
	public boolean Save(boolean ask);
	public String getTitle();
	public NGSScrollPane getScrollPane();
	public JComponent getComponent();
	public void delete();
	public Tab getTab();
	public App getApp();
	public boolean closeEvent(Object... Reason);
	public default void lostFocus(Window newWindow) {}
	public default void gainedFocus(Window prevWindow) {}
	public void refresh() throws ValueNotFoundException, IOException;
	public void resize();
	
}
