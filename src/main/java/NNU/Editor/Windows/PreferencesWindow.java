package NNU.Editor.Windows;

import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.AssetManagement.StringTable;
import NNU.Editor.Menus.PreferencesMenu;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Interfaces.Window;

public class PreferencesWindow implements Window {
	
	public static Integer prefwin = 1;

	protected PreferencesMenu menu;
	protected NGSScrollPane sp;
	protected Tab tab;
	protected App app;
	
	public PreferencesWindow(App app) {
        this.app = app;
		this.sp = new NGSScrollPane(getApp());
		getApp();
		this.menu = new PreferencesMenu(App.stng,getApp(),this);
		sp.setViewportView(menu);
        this.tab = new Tab(getApp(), this);
        sp.getHorizontalScrollBar().setEnabled(true);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setDoubleBuffered(false);
        tab.setIcon(Utils.ReadImageIcon("ui/NNUEdit72x72.png"));
        
		//sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        getApp().contentpane.add(sp);
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
		return StringTable.getString("options.title", Utils.EDITORNAME);
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
	public void refresh() throws IOException {}

	@Override
	public void resize() {
		menu.refresh();
	}

	@Override
	public void delete() {}

}
