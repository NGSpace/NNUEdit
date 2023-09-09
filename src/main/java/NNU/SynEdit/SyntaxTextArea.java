package NNU.SynEdit;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class SyntaxTextArea extends RSyntaxTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4557978198642746316L;

	public SyntaxTextArea(int i, int j) {
		super(i,j);
	}

	public SyntaxTextArea(App app) {
		super();
        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        setCodeFoldingEnabled(true);
        setAntiAliasingEnabled(true);
        setMarginLineEnabled(false);
        setPaintTabLines(true);
        setCaretColor(Color.LIGHT_GRAY);
        this.getDocument().addDocumentListener(new DocListener(app));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		TextUI ui = getUI();
		if (ui != null) {
			// Not allowed to modify g, so make a copy.
			Graphics scratchGraphics = g.create();
			try {
				ui.update(scratchGraphics, this);
			} finally {
				scratchGraphics.dispose();
			}
		}
	}

}

class DocListener implements DocumentListener {
    String newline = "\n";
    public final App app;
    public DocListener(App app) {
    	this.app = app;
    }
 
    public void insertUpdate(DocumentEvent e) {
        updateLog();
    }
    public void removeUpdate(DocumentEvent e) {
        updateLog();
    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }

    public void updateLog() {
    	app.saved = false;
    }
}
