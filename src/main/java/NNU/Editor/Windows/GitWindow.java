package NNU.Editor.Windows;

import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.Menus.GitMenu;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Utils.ValueNotFoundException;

public class GitWindow implements Window {

	protected GitMenu menu;
	protected NGSScrollPane sp;
	protected Tab tab;
	protected App app;
	
	public GitWindow(App app) {
		this.sp = new NGSScrollPane(app);
		try {
			this.menu = new GitMenu(this, app);
		} catch (Exception e) {e.printStackTrace();}
		sp.setViewportView(menu);
        this.tab = new Tab(app, this);
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
		return "Git";
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
	public void resize() {menu.resizeButton(sp);}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
