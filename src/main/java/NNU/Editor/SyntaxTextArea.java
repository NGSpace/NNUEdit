package NNU.Editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.ConfigurableCaret;

import NNU.Editor.Menus.FindMenu;
import NNU.Editor.Menus.Components.FolderButton;
import NNU.Editor.Menus.Components.LineTextArea;
import NNU.Editor.Menus.Components.NGSScrollPane;
import NNU.Editor.Utils.SmartGraphics2D;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Utils.ValueNotFoundException;
import NNU.Editor.Windows.Window;

public class SyntaxTextArea extends RSyntaxTextArea implements DocumentListener, FolderButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4557978198642746316L;
	
	private String FilePath = "\000";
	private static String tx = "text/";
	private static String syn = "syntax";
    public LineTextArea lta;
    private boolean saved = true;
    public final Window window;
	public final App app;

	public JMenuItem jpmln = new JMenuItem("1");

    public void setFilePath(String fp) {
        String[] arr = fp.split("[.]");
        String ext = arr[arr.length-1];
		if (arr.length>1&&App.fileext.get(ext.toLowerCase().trim())!=null)
			setSyntaxEditingStyle(tx + ((String) App.fileext.get(ext)).toLowerCase().trim());
		else
			try {
				setSyntaxEditingStyle(tx + app.stng.get(syn).toLowerCase().trim());
			} catch (ValueNotFoundException e) {e.printStackTrace();}
    	FilePath = fp;
    	app.redraw();
    	//App.fileext
    }
    public String getFilePath() {
    	return FilePath;
    }

	public SyntaxTextArea(App app, Window w) {
		
		window = w;
		this.app = app;
		
	    /*CompletionProvider provider = createCompletionProvider();
	    AutoCompletion ac = new AutoCompletion(provider);
	    ac.install(this);*/
	      
		
        try {
			setSyntaxEditingStyle(tx + app.stng.get(syn).toLowerCase().trim());
			AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
			atmf.putMapping("text/myLanguage", "NNU.Editor.TestTokenizer");
			//setSyntaxEditingStyle("text/myLanguage");
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
        this.getDocument().addDocumentListener(this);
        this.setFont(app.getFont());
        this.setBackground(new Color(10,10,12));
        this.setForeground(Color.LIGHT_GRAY);
        this.setCurrentLineHighlightColor(new Color(44,44,44));
        this.setSelectionColor(new Color(99, 128, 176));
        this.setSelectedTextColor(new Color(255,255,255));
        
        this.setOpaque(true);
        this.setBorder(null);
        this.setLayout(null);
        
        JMenuItem FIND = new JMenuItem("Find and Replace");

        FIND.addActionListener(e -> openFind());
        FIND.setMnemonic(KeyEvent.VK_F);
        FIND.setToolTipText("Find and Replace");
        
        //this.getPopupMenu().addSeparator();
		JPopupMenu jpm = this.getPopupMenu();
        jpm.add(FIND,9);
        jpm.addSeparator();
        jpm.add(jpmln);
        setBorder(new EmptyBorder(0,getBuffer(),0,0));
        
        ((ConfigurableCaret) this.getCaret()).setBlinkRate(0);
        
        JScrollPane sp = window.getScrollPane();
        sp.getHorizontalScrollBar().addAdjustmentListener(e -> Resize());
        sp.getVerticalScrollBar().addAdjustmentListener(e -> Resize());
        

        // Create the drag and drop listener
        DropTargetListener myDragDropListener = new DropTargetAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void drop(DropTargetDropEvent dtde) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY);
	            List<File> droppedFiles = null;
				try {
					droppedFiles = (List<File>)
							dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
				} catch (UnsupportedFlavorException | IOException e) {e.printStackTrace();return;}
	            for (File file : droppedFiles) {
	                app.openFile(file.getAbsolutePath());
	            }
			}
        };

        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
        		Collections.emptySet());
        
        registerKeyboardAction(e -> app.getRootPane().getActionForKeyStroke
    			(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.CTRL_DOWN_MASK)).actionPerformed(e)
		,KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.CTRL_DOWN_MASK),
    	JComponent.WHEN_FOCUSED);
        
        // Connect the label with a drag and drop listener
        new DropTarget(this, myDragDropListener);
        
        initjl(this,sp);
        
        app.redraw();
	}
	
	@Override public JPopupMenu getPopupMenu() {
		int caretpos = getCaretPosition();
		String row = "";
		try {
			int i = getLineOfOffset(caretpos);
			if(i!=-1) row = i + 1 + "";
		} catch (BadLocationException e) {row = "Unknown line";}
		jpmln.setText(row);
		return super.getPopupMenu();
	}
	
	public void Resize() {
        JScrollPane sp = window.getScrollPane();
		Point position = sp.getViewport().getViewPosition();
		JScrollBar vsb = sp.getVerticalScrollBar();
    	if (fm!=null) {
        	fm.setLocation((int) (position.getX()+(sp.getWidth()-fm.getWidth())
        			-(vsb.isVisible()?vsb.getWidth():0)),
        			(int) position.getY());
    	}
    	repaint();
	}

	@Override
	public void paintComponent(Graphics gra) {
		try {
			NGSScrollPane sp1 = window.getScrollPane();
	        //sp1.repaint();
			Color c = gra.getColor();
			gra.setColor(getBackground());
			gra.fillRect(0, 0, getWidth(), getHeight());
			gra.setColor(c);
			TextUI ui = getUI();
			if (ui != null) {
				Graphics2D g = (Graphics2D) gra.create(0, 0, getWidth(), getHeight());
				if (app.stng.getBoolean("textantialias"))
					g.setRenderingHint(
				        RenderingHints.KEY_TEXT_ANTIALIASING,
				        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				SmartGraphics2D.SuccessfulDrawCalls = 0;
				ui.update(new SmartGraphics2D(g, getVisibleRect()), this);
				//System.out.println(SmartGraphics2D.SuccessfulDrawCalls);
				gra.setColor(app.contentpane.getBackground());
				((Graphics2D)gra).setStroke(new BasicStroke(10));
				gra.drawLine(0, 0, 0, getHeight());
			}
			this.paintFB(gra, sp1);
			sp1.paintLines(gra);
		} catch (ValueNotFoundException e) {e.printStackTrace();}
	}
	
	public FindMenu fm;
	
	public void openFind() {
		if (fm==null) {
	    	fm = new FindMenu(this);
	    	this.add(fm);
	    	Resize();
		}
		fm.getComponents()[0].requestFocus();
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
					setFilePath(res);
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
	
	public boolean megaSave(boolean ask) {
		int result = 0;
		if (ask)
			result = JOptionPane.showConfirmDialog((Component) null,
				"Do you want to save?","alert", JOptionPane.YES_NO_CANCEL_OPTION);
    	if (result==JOptionPane.CANCEL_OPTION)
    		return false;
    	if (result==JOptionPane.YES_OPTION&&"\000".equals(this.getFilePath())) {
    		this.openfile(false, true);
    	}
    	if (result==JOptionPane.NO_OPTION)
    		return true;
		if ("\000".equals(this.getFilePath())) return false;
		setSaved(true);
		return Utils.save(this.getFilePath(), this.getText());
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
        String[] arr = FilePath.split("[.]");
        String ext = arr[arr.length-1];
		if (arr.length>1&&App.fileext.get(ext.toLowerCase().trim())!=null)
			setSyntaxEditingStyle(tx + ((String) App.fileext.get(ext)).toLowerCase().trim());
		else {
			setSyntaxEditingStyle(tx + app.stng.get(syn).toLowerCase().trim()); 
		}
	}
	public boolean isSaved() {
		return saved;
	}
	public void setSaved(boolean saved) {
		this.saved = saved;
		window.getTab().repaint();
	}
 
    public void insertUpdate(DocumentEvent e) {
        updateLog();
        int pos = e.getOffset();
        try {
			if (this.getText(pos, 1).equals("{")) {
				setText(FilePath);
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    public void removeUpdate(DocumentEvent e) {
        updateLog();
    }
    public void changedUpdate(DocumentEvent e) {
    }

    public void updateLog() {
    	setSaved(false);
    	window.getTab().repaint();
    	getHighlighter().removeAllHighlights();
    	window.getScrollPane().countLines(this);
    }
	public final JButton jl = new JButton(">") {
		private static final long serialVersionUID = 3394518635747541418L;

		@Override public void paint(Graphics g) {app.paintJB();}
	};
	
	@Override
	public JButton getFolderButton() {
		return jl;
	}

	@Override
	public App getApp() {
		return app;
	}
	@Override public int hashCode() {
		return this.getFilePath().hashCode();
	}
	@Override public boolean equals(Object obj) {
		if (!(obj instanceof String)) return super.equals(obj);
		return FilePath.equals(obj);
	}
	
	public boolean equals(String obj) {
		return FilePath.equals(obj);
	}
	public LineTextArea getLineNumberArea() {
		if (lta==null) {
			lta = new LineTextArea(this);
		}
		return lta;
	}

	   /**
	    * Create a simple provider that adds some Java-related completions.
	    */
	   private CompletionProvider createCompletionProvider() {

	      // A DefaultCompletionProvider is the simplest concrete implementation
	      // of CompletionProvider. This provider has no understanding of
	      // language semantics. It simply checks the text entered up to the
	      // caret position for a match against known completions. This is all
	      // that is needed in the majority of cases.
	      DefaultCompletionProvider provider = new DefaultCompletionProvider();

	      // Add completions for all Java keywords. A BasicCompletion is just
	      // a straightforward word completion.
	      provider.addCompletion(new BasicCompletion(provider, "abstract"));
	      provider.addCompletion(new BasicCompletion(provider, "assert"));
	      provider.addCompletion(new BasicCompletion(provider, "break"));
	      provider.addCompletion(new BasicCompletion(provider, "case"));
	      // ... etc ...
	      provider.addCompletion(new BasicCompletion(provider, "transient"));
	      provider.addCompletion(new BasicCompletion(provider, "try"));
	      provider.addCompletion(new BasicCompletion(provider, "void"));
	      provider.addCompletion(new BasicCompletion(provider, "volatile"));
	      provider.addCompletion(new BasicCompletion(provider, "while"));
	      
	      String prnln = "System.out.println(";
	      
	      // Add a couple of "shorthand" completions. These completions don't
	      // require the input text to be the same thing as the replacement text.
	      provider.addCompletion(new ShorthandCompletion(provider, "sysout",
	    		  prnln, prnln));
	      provider.addCompletion(new ShorthandCompletion(provider, "sout",
	    		  prnln, prnln));
	      provider.addCompletion(new ShorthandCompletion(provider, "syserr",
	            "System.err.println(", "System.err.println("));
	      

	      return provider;

	   }
}
