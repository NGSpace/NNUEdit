package NNU.Editor.Windows;

import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;

import NNU.Editor.App;
import NNU.Editor.EditorTextArea;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Windows.Interfaces.Editor;
import NNU.Editor.Windows.Interfaces.EditorWindow;

public class TextEditorWindow implements EditorWindow {

	protected EditorTextArea textArea;
	protected NGSScrollPane sp;
	protected Tab tab;
	protected App app;

	public TextEditorWindow(App app, String Text) {
        
		this.app = app;

        sp = new NGSScrollPane(app);
        
		textArea = new EditorTextArea(app,this);
		sp.setViewportView(textArea);
        
        tab = new Tab(app, this);

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
		return ("".equals(textArea.getFilePath()) ? "Unknown" : 
			new File(textArea.getFilePath()).getName());
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
		return textArea.Save(ask);
	}

	@Override
	public void refresh() throws IOException {
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
			return Save("".equals(textArea.getFilePath()));
		}
	}

	@Override
	public void resize() {
		textArea.Resize();
	}

	@Override public void delete() {}

	@Override
	public Editor getEditor() {
		return textArea;
	}

}
