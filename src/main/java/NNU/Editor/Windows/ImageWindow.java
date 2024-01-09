package NNU.Editor.Windows;

import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.Menus.ImageMenu;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Windows.Interfaces.Window;

public class ImageWindow implements Window {

	protected final NGSScrollPane sp;
	protected final String title;
	protected ImageMenu menu;
	protected final Tab tab;
	protected final App app;
	
	public ImageWindow(App app, File f) {
		this.title = f.getName();
		this.sp = new NGSScrollPane(app);
		try {
			this.menu = new ImageMenu(this, app, f);
		} catch (Exception e) {e.printStackTrace();}
		sp.setViewportView(menu);
        this.tab = new Tab(app, this);
        tab.setIcon(app.Folder.getIcon1(f));
        
        this.app = app;
        
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
		return title;
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
