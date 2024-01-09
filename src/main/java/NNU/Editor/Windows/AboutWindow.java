package NNU.Editor.Windows;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.AssetManagement.StringTable;
import NNU.Editor.Menus.AboutMenu;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Interfaces.Window;

public class AboutWindow implements Window {

	protected AboutMenu menu;
	protected NGSScrollPane sp;
	protected Tab tab;
	protected App app;
	
	public AboutWindow(App app) {
		this.sp = new NGSScrollPane(app);
		this.menu = new AboutMenu(this, app);
		sp.setViewportView(menu);
        this.tab = new Tab(app, this);
        this.app = app;
        tab.setIcon(Utils.ReadImageIcon("ui/NNUEdit72x72.png"));
        
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
		return StringTable.getString("about.title", Utils.EDITORNAME);
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
	public void resize() {}

	@Override
	public void delete() {}

}
