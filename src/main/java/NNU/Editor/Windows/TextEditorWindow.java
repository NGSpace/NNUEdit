package NNU.Editor.Windows;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import NNU.Editor.App;
import NNU.Editor.SyntaxTextArea;
import NNU.Editor.Tab;
import NNU.Editor.Utils.ValueNotFoundException;

public class TextEditorWindow implements Window  {

	protected JScrollPane sp;
	protected SyntaxTextArea textArea;
	protected Tab tab;
	protected final App app;

	public TextEditorWindow(App app, String Text) {
        
		this.app = app;
		
		textArea = new SyntaxTextArea(app,this);
        sp = new JScrollPane(textArea);
        tab = new Tab(app, this);
        
        sp.setBounds(0, App.MenuBarSize(), app.getWidth(),
        		app.getHeight() - App.MenuBarSize() - App.TabSize());
        
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(25,0));
        sp.getHorizontalScrollBar().setLocation(sp.getWidth() - 25, 0);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(25,100));
        sp.getVerticalScrollBar().setLocation(sp.getWidth() - 25, 0);
        
        sp.setOpaque(true);

        app.contentpane.add(getScrollPane());
        app.contentpane.add(getTab());
        app.Windows.add(this);
        textArea.setOpaque(true);
        textArea.setText(Text);
        textArea.setSaved(true);
        textArea.setCaretPosition(0);
        app.redraw();
	}

	@Override
	public boolean isSaved() {
		return textArea.isSaved();
	}

	@Override
	public String getTitle() {
		return ("\000".equals(textArea.FilePath) ? "Unknown" : 
			new File(textArea.FilePath).getName()) + (textArea.isSaved() ? "" : "*");
	}

	@Override
	public JScrollPane getScrollPane() {
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
		boolean res = textArea.megaSave(ask);
		getTab().repaint();
		return res;
	}

	@Override
	public void refresh() throws ValueNotFoundException, IOException {
		textArea.refresh();
		tab.setFont(textArea.getFont());
		tab.setText(getTitle());
	}

	@Override
	public App getApp() {
		return app;
	}

	@Override
	public boolean closeEvent(String Reason) {
		
		return Save("\000".equals(textArea.FilePath));
	}

}
