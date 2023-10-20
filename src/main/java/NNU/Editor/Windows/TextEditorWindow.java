package NNU.Editor.Windows;

import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.SyntaxTextArea;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Utils.ValueNotFoundException;

public class TextEditorWindow implements Window {

	protected SyntaxTextArea textArea;
	protected NGSScrollPane sp;
	protected Tab tab;
	protected App app;

	public TextEditorWindow(App app, String Text) {
        
		this.app = app;

        sp = new NGSScrollPane(app);
        
		textArea = new SyntaxTextArea(app,this);
		sp.setViewportView(textArea);
		
        sp.setBorder(null);
        sp.setOpaque(true);
        
        tab = new Tab(app, this);
        
        sp.setBounds(0, App.MenuBarSize(), app.getWidth(),
        		app.getHeight() - App.MenuBarSize() - App.TabSize());

        app.contentpane.add(getScrollPane());
        app.contentpane.add(getTab());
        app.Windows.add(this);
        textArea.setOpaque(true);
        textArea.setText(Text);
        textArea.setCaretPosition(0);
        textArea.setSaved(true);
        app.redraw();
	}

	@Override
	public boolean isSaved() {
		return textArea.isSaved();
	}

	@Override
	public String getTitle() {
		return ("\000".equals(textArea.getFilePath()) ? "Unknown" : 
			new File(textArea.getFilePath()).getName()) + (textArea.isSaved() ? "" : "*");
	}

	@Override
	public NGSScrollPane getScrollPane() {
		return sp;
	}
	
	@Override
	public JComponent getComponent() {
		return textArea;
	}

	@Override
	public Tab getTab() {
		return tab;
	}

	@Override
	public boolean Save(boolean ask) {
		return textArea.megaSave(ask);
	}

	@Override
	public void refresh() throws ValueNotFoundException, IOException {
		textArea.refresh();
		tab.setText(getTitle());
	}

	@Override
	public App getApp() {
		return app;
	}

	@Override
	public boolean closeEvent(Object... Reason) {
		if (Reason.length>0&&Reason[0]!=null) {
			return Save((boolean) Reason[0]);
		} else {
			return Save("\000".equals(textArea.getFilePath()));
		}
	}

	@Override
	public void resize() {
		textArea.Resize();
		textArea.resizeButton(sp);
	}

	@Override
	public void delete() {
	}

}
