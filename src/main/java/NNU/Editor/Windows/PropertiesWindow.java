package NNU.Editor.Windows;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.Menus.PropertiesMenu;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Windows.Interfaces.Editor;
import NNU.Editor.Windows.Interfaces.EditorWindow;

public class PropertiesWindow implements EditorWindow {

	protected PropertiesMenu menu;
	protected NGSScrollPane sp;
	protected Tab tab;
	protected App app;
	
	public PropertiesWindow(App app, File file) {
		this.sp = new NGSScrollPane(app);
        this.tab = new Tab(app, this);
		this.menu = new PropertiesMenu(app, this, file);
		sp.setViewportView(menu);
        this.app = app;
        
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
	@Override public boolean isSaved() {return menu.isSaved();}
	@Override public boolean Save(boolean ask) {return menu.Save(ask);}
	@Override
	public String getTitle() {
		return ("".equals(menu.getFilePath()) ? "Unknown" : new File(menu.getFilePath()).getName());
	}
	@Override public NGSScrollPane getScrollPane() {return sp;}
	@Override public JComponent getComponent() {return menu;}
	@Override public Tab getTab() {return tab;}
	@Override public App getApp() {return app;}
	@Override public boolean closeEvent(Object... Reason) {return Save(!isSaved());}
	@Override public void refresh() throws IOException {}
	@Override public void resize() {sp.revalidate();menu.resize();}
	@Override public void delete() {}
	@Override public Editor getEditor() {return menu;}

}
