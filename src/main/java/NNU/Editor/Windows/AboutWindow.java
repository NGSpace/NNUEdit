package NNU.Editor.Windows;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import NNU.Editor.App;
import NNU.Editor.Tab;
import NNU.Editor.Menus.AboutMenu;
import NNU.Editor.Menus.PreferencesMenu;
import NNU.Editor.Utils.ValueNotFoundException;

public class AboutWindow implements Window {

	protected AboutMenu menu;
	protected JScrollPane sp;
	protected Tab tab;
	protected App app;
	
	public AboutWindow(App app) {
		this.menu = new AboutMenu();
		this.sp = new JScrollPane(menu.getContentPane());
        this.tab = new Tab(app, this);
        this.app = app;

        menu.setVisible(false);
        getComponent().setBackground(App.MenuBG);
        
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(25,0));
        sp.getHorizontalScrollBar().setLocation(sp.getWidth() - 25, 0);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(25,100));
        sp.getVerticalScrollBar().setLocation(sp.getWidth() - 25, 0);
        
        sp.setOpaque(true);
        
        getApp().contentpane.add(getScrollPane());	
        getApp().contentpane.add(getTab());
        getApp().Windows.add(this);
        getApp().redraw();
	}

	@Override
	public boolean isSaved() {
		return true;
	}

	@Override
	public boolean Save(boolean ask) {
		return true;
	}

	@Override
	public String getTitle() {
		return "About";
	}

	@Override
	public JScrollPane getScrollPane() {
		return sp;
	}

	@Override
	public JComponent getComponent() {
		return (JComponent) menu.getContentPane();
	}

	@Override
	public Tab getTab() {
		return tab;
	}

	@Override
	public App getApp() {
		return app;
	}

	@Override
	public boolean closeEvent(String Reason) {
		return true;
	}

	@Override
	public void refresh() throws ValueNotFoundException, IOException {
		
	}

}
