package NNU.Editor.Windows;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import NNU.Editor.App;
import NNU.Editor.Tab;
import NNU.Editor.Utils.ValueNotFoundException;

public interface Window {
	
	public static final int HEIGHT = 100;

	public boolean isSaved();
	public boolean Save(boolean ask);
	public String getTitle();
	public JScrollPane getScrollPane();
	public JComponent getComponent();
	public Tab getTab();
	public App getApp();
	public boolean closeEvent(String Reason);
	public void refresh() throws ValueNotFoundException, IOException;
	
}
