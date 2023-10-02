package NNU.Editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.ConfigurableCaret;

import NNU.Editor.Utils.Utils;
import NNU.Editor.Utils.ValueNotFoundException;
import NNU.Editor.Windows.Window;

public class SyntaxTextArea extends RSyntaxTextArea{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4557978198642746316L;
	
	public final App app;
    public String FilePath = "\000";
    private boolean saved = true;
    public final Window window;

	public SyntaxTextArea(App app, Window w) {
		super();
		window = w;
		this.app = app;
        try {
			setSyntaxEditingStyle("text/" + app.stng.get("syntax").toLowerCase().trim());
		} catch (ValueNotFoundException e) {e.printStackTrace();}
        setCodeFoldingEnabled(true);
        setAntiAliasingEnabled(true);
        setMarginLineEnabled(false);
        setPaintTabLines(true);
        setCaretColor(Color.LIGHT_GRAY);
        InputStream inpstr = App.class.getResourceAsStream("/NNU/Editor/Style.xml");
        Theme theme = null;
		try {
			theme = Theme.load(inpstr);
		} catch (IOException e) {e.printStackTrace();}
        theme.apply(this);
        this.getDocument().addDocumentListener(new DocListener(app, this));
        this.setFont(app.getFont());
        this.setBackground(new Color(10,10,12));
        this.setForeground(Color.LIGHT_GRAY);
        this.setCurrentLineHighlightColor(new Color(44,44,44));
        this.setSelectionColor(new Color(99, 128, 176));
        this.setSelectedTextColor(new Color(255,255,255));
        this.setOpaque(true);
        ((ConfigurableCaret) this.getCaret()).setBlinkRate(0);
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
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
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
						setText(Utils.read(res));
				        setCaretPosition(0);
						revalidate();
						app.repaint();
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
	 * saves text to the path specified
	 * @param path the path
	 * @param text the text
	 * @return whether the operation was successful
	 */
	public boolean save(String path, String text) {
		try {
			FileWriter fw = new FileWriter(path);
			fw.write(text);
		    fw.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean megaSave(boolean ask) {
		int result = 0;
		if (ask)
			result = JOptionPane.showConfirmDialog((Component) null,
				"Do you want to save?","alert", JOptionPane.YES_NO_CANCEL_OPTION);
    	if (result==JOptionPane.CANCEL_OPTION)
    		return false;
    	if (result==JOptionPane.YES_OPTION&&"\000".equals(this.FilePath)) {
    		this.openfile(false, true);
    	}
    	if (result==JOptionPane.NO_OPTION)
    		return true;
		if ("\000".equals(this.FilePath)) return false;
		setSaved(true);
		return save(this.FilePath, this.getText());
	}
	
	/**
	 * Refreshes the TextArea from the settings in the app object<br>
	 * given during initalization of the textArea
	 * @throws ValueNotFoundException
	 * @throws IOException
	 */
	public void refresh() throws ValueNotFoundException {
		
		/* I was unable to get this to work so I'll try to fix it in the next release.*/
        //InputStream inpstr = App.class.getResourceAsStream("/NNU/Editor/Style.xml");
        //Theme theme = Theme.load(inpstr);
        
        /* Refresh the font and language*/
		this.setFont(app.getFont());
		this.setSyntaxEditingStyle("text/" + app.stng.get("syntax").toLowerCase().trim());
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
}

class DocListener implements DocumentListener {
    String newline = "\n";
    public final App app;
    public final SyntaxTextArea textArea;
    public DocListener(App app, SyntaxTextArea textarea) {
    	this.app = app;
    	textArea = textarea;
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
    	textArea.setSaved(false);
    	textArea.window.getTab().repaint();
    }
}
