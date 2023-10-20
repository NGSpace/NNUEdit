package NNU.Editor.Windows;

import static NNU.Editor.Utils.Utils.EDITORNAME;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JComponent;
import NNU.Editor.App;
import NNU.Editor.Menus.PreferencesMenu;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Utils.ValueNotFoundException;

public class PreferencesWindow implements Window {
	
	public static Integer prefwin = 1;

	protected PreferencesMenu menu;
	protected NGSScrollPane sp;
	protected Tab tab;
	protected App app;
	
	public PreferencesWindow(App app) {
        this.app = app;
		this.sp = new NGSScrollPane(getApp());
		this.menu = new PreferencesMenu(getApp().stng,getApp(),this);
		sp.setViewportView(menu);
        this.tab = new Tab(getApp(), this);
        
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
		return EDITORNAME + "'s Preferences";
	}

	@Override
	public NGSScrollPane getScrollPane() {
		return sp;
	}

	@Override
	public JComponent getComponent() {
		return menu;
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
	public boolean closeEvent(Object... Reason) {
		return true;
	}

	@Override
	public void refresh() throws ValueNotFoundException, IOException {}

	@Override
	public void resize() {menu.resizeButton(sp);menu.refresh();}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
