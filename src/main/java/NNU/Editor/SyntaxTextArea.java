package NNU.Editor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;

public class SyntaxTextArea extends RSyntaxTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4557978198642746316L;
	
	public final App app;
    public String FilePath = "\000";

	public SyntaxTextArea(App app) {
		super();
		this.app = app;
        try {
			setSyntaxEditingStyle("text/" + app.stng.get("syntax").toLowerCase().trim());
		} catch (ValueNotFoundException e) {e.printStackTrace();}
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
	
	public void openfile(boolean load) {
		openfile(load, false);
	}
	
	/**
	 * Sets the current file of the textArea
	 * @param load whether to load the file into the TextArea's text bar or not.
	 * @param save whether to display a save button or open button (this option does not actually save)
	 */
	public void openfile(boolean load, boolean save) {
		String res = "\000";
		while ("\000".equals(res)) {
			res = Utils.openFileDialog(save);
	        if(res != null&&!"/".equals(res.trim()) && !res.trim().isEmpty()) {
	            try {
					File f = new File(res);
					if (!f.exists() && !f.createNewFile())
							throw new Exception("Can't create file");
					if ("\000".equals(res)) {
						JOptionPane.showMessageDialog(this,
							    "file is '\000'.? ",
							    "Error reading file",
							    JOptionPane.ERROR_MESSAGE);
						continue;
					}
					FilePath = res;
					if (load) {
						setText(App.read(res));
				        setCaretPosition(0);
						revalidate();
						repaint();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this,
						    "Unable to read file due to error: " + e.getMessage(),
						    "Error reading file",
						    JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
	        } else {
	        	return;
	        }
		}
	}
	
	/**
	 * Refreshes the TextArea from the settings in the app object<br>
	 * given during initalization of the textArea
	 * @throws ValueNotFoundException
	 * @throws IOException
	 */
	public void refresh() throws ValueNotFoundException, IOException {
		
		/* I was unable to get this to work so I'll try to fix it in the next release.*/
        InputStream inpstr = App.class.getResourceAsStream("/NNU/Editor/Style.xml");
        Theme theme = Theme.load(inpstr);
        
        /* Refresh the font and language*/
		this.setFont(app.getFont());
		setSyntaxEditingStyle("text/" + app.stng.get("syntax").toLowerCase().trim());
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
