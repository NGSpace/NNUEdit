package NNU.Editor.Windows.Interfaces;

import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;

public interface Window extends Savable {
	
	public static final int HEIGHT = 100;

	public String getTitle();
	public NGSScrollPane getScrollPane();
	public JComponent getComponent();
	public void delete();
	public Tab getTab();
	public App getApp();
	public boolean closeEvent(Object... Reason);
	public default void lostFocus(Window newWindow) {}
	public default void gainedFocus(Window prevWindow) {}
	public void refresh() throws IOException;
	public void resize();
	

	public default boolean isEditor() {return false;}
	public default Editor getEditor() {return null;}
}
