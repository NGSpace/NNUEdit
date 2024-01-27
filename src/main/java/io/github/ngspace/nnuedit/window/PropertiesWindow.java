package io.github.ngspace.nnuedit.window;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JComponent;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.menu.PropertiesMenu;
import io.github.ngspace.nnuedit.menu.components.NGSScrollPane;
import io.github.ngspace.nnuedit.menu.components.Tab;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.window.abstractions.Editor;
import io.github.ngspace.nnuedit.window.abstractions.EditorWindow;

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
        app.addRedrawListener((a)->menu.resize());
	}
	@Override public boolean isSaved() {return menu.isSaved();}
	@Override public boolean Save(boolean ask) {return menu.Save(ask);}
	@Override public String getTitle() {
		return ("".equals(menu.getFilePath())?"Unknown":Utils.getFileName(menu.getFilePath()));
	}
	@Override public NGSScrollPane getScrollPane() {return sp;}
	@Override public JComponent getComponent() {return menu;}
	@Override public Tab getTab() {return tab;}
	@Override public App getApp() {return app;}
	@Override public boolean closeEvent(Object... Reason) {return Save(!isSaved());}
	@Override public Editor getEditor() {return menu;}

}
