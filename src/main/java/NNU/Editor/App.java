package NNU.Editor;

import static NNU.Editor.Utils.Utils.EDITORNAME;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

import NNU.Editor.Menus.MenuThingy;
import NNU.Editor.Utils.Unfinnished;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Utils.ValueNotFoundException;
import NNU.Editor.Windows.AboutWindow;
import NNU.Editor.Windows.PrefrencesWindow;
import NNU.Editor.Windows.TextEditorWindow;
import NNU.Editor.Windows.Window;

/**
 * the editor
 */
public class App extends JFrame {
	private static final long serialVersionUID = 5770603365260133811L;
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final Color MenuBG = new Color(24, 24, 33);
    public static final Color MenuFG = new Color(240, 240, 240);
	public Window SelectedWindow;
	public List<Window> Windows = new ArrayList<Window>();
	public JPanel contentpane;
	public MenuThingy ToolBar;
	/**
	 * The settings
	 */
	public final Settings stng;

	private static final int MNBS = 30;
	private static final int TBS = 50;
	
	public static int MenuBarSize() {
		return MNBS;
	}
	
	public static int TabSize() {
		return TBS;
	}
	
	@Override
	public Font getFont() {
		try {
			return new Font(stng.get("fontfamily"),
				Integer.valueOf(stng.get("fontstyle")),
				Integer.valueOf(stng.get("fontsize")));
		} catch (Exception e) {
			e.printStackTrace();
			return new Font(Font.MONOSPACED, Font.BOLD, 40);
		}
	}
	
	public Font getTipFont() {
		return new Font("Arial", Font.BOLD, 40);
	}
	
	/**
	 * the app
	 * @param filepath the path to read from upon startup (leave empty for none)
	 * @throws IOException
	 */
    public App(String filepath) throws IOException {
		this.setOpacity(1);
    	
    	File stfile = new File(Utils.getProgramPath() +
    			"/" + EDITORNAME + "/" + EDITORNAME + ".properties");
    	stng = new Settings(stfile.getAbsolutePath(), this);
    	
    	String starttext = "TextArea1";
    	
    	if (!"".equals(filepath)) {
    		starttext = Utils.read(filepath);
    	}

        setSize((int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2);
        
        
        
    	try {
    	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	    UIManager.put("JFrame.activeTitleBackground", Color.red);
    	    UIDefaults uiDefaults = UIManager.getDefaults();
    	    uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.gray));
    	    uiDefaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.white));
    	    JFrame.setDefaultLookAndFeelDecorated(true);
    	} catch (Exception e) {
    	    System.err.println("UI Exception caught:"+e);
    	}
    	  
        contentpane = new AppPanel(this);

        SelectedWindow = new TextEditorWindow(this,starttext);
        
        //Menu
        ToolBar = new MenuThingy(this );
        ToolBar.setBackground(MenuBG);
        ToolBar.setMinimumSize(new Dimension(getWidth(),100));
        ToolBar.setForeground(MenuFG);
        
        //Add Things to contentpane
        ToolBar.setBounds(0, 0, getWidth(), MenuBarSize());
        contentpane.setLayout(null);
        contentpane.setSize(getSize());
        contentpane.setOpaque(true);
        contentpane.setBackground(MenuBG.darker());
        contentpane.add(ToolBar);
        
        getRootPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redraw();
            }
        });
        
        App frame = this;
        
        addWindowStateListener(e -> {
        	redraw();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
					close();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
            @Override
            public void windowIconified(WindowEvent e) {
                System.out.println("Lost focus");
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
                System.out.println("Gained focus");
            }
        });
        /* Save current window (CTRL + S) */
        getRootPane().registerKeyboardAction(e -> {
	        	if (SelectedWindow!=null&&SelectedWindow.getComponent() instanceof SyntaxTextArea) {
	        		((SyntaxTextArea)SelectedWindow.getComponent()).megaSave(false);
	        		redraw();
	        	}
        	}
        	,KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK),
        	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        /* SaveAll (CTRL + SHIFT + S) */
        getRootPane().registerKeyboardAction(e ->
	    	megaSave(false)
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_S, 
	    			InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        /* Open File (CTRL + O) */
        getRootPane().registerKeyboardAction(e -> {
        		String file = Utils.tuneFileDialogResult(Utils.openFileDialog(false));
        		if (file==null) return;
        		/* Get Text Editor */
        		TextEditorWindow tew = new TextEditorWindow
        				(this,Utils.read(file));
	            ((SyntaxTextArea)tew.getComponent()).FilePath = file;
	            /* Set Selected Window and redraw() */
                this.setSelectedWindow(tew);
                redraw();
        	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );

        /* Close Window (CTRL + W) */
        getRootPane().registerKeyboardAction(e ->
        	closeSelectedWindow()
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        /* Open prefrences menu (CTRL + P) */
        getRootPane().registerKeyboardAction(e ->
        	setSelectedWindow(new PrefrencesWindow(this))
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        /* Test Button (CTRL + T) */
        getRootPane().registerKeyboardAction(e -> {
	        	setSelectedWindow(new AboutWindow(this));
	        }
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        /*((SyntaxTextArea)new TextEditorWindow
        	(this,Utils.read("C:\\Users\\[Insert Username]\\Desktop\\Test.txt"))
        	.getComponent()).FilePath =
        			"C:\\Users\\[Insert Username]\\Desktop\\Test.txt";*/
        
        this.setSelectedWindow(SelectedWindow);
        
        setUndecorated(false);
        setBackground(MenuBG);
        setContentPane(contentpane);
        setTitle(EDITORNAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocation(getWidth()/2,getHeight()/2);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        revalidate();
        repaint();
    }

	@SuppressWarnings("unused")
	private void openFile(String path) {
    	redraw();
    	SelectedWindow.getScrollPane().setVisible(false);
		((SyntaxTextArea) 
				new TextEditorWindow(this, Utils.read(path)).getComponent()).setSaved(false);
		
	}
    
    public boolean megaSaveAll(boolean ask) {
    	return megaSave(ask);
    }

	/**
     * the entry point of this weird ass code editor
     * @param args which file to open at startup (leave empty for no selected file)
     */
	public static void main(String[] args) {
		
		//System.setOut(new BSPS(System.out));
		
		final StringBuilder starttext = new StringBuilder();
		if (args.length>0) {
			starttext.append(args[0]);
		}
		
        SwingUtilities.invokeLater(() -> {
			try {
				new App(starttext.toString()).setVisible(true);
			} catch (Exception e) {e.printStackTrace();}
		});
    }
	
	public void setSelectedWindow(Window w) {
		for (Window i : Windows)
			i.getScrollPane().setVisible(false);
		if (w!=null) {
			w.getScrollPane().setVisible(true);
			SelectedWindow = w;
		}
		redraw();
	}
	
	public boolean closeSelectedWindow() {
		boolean b = SelectedWindow.closeEvent("");
		if (!b) return false;
		contentpane.remove(SelectedWindow.getComponent());
		contentpane.remove(SelectedWindow.getTab());
		contentpane.remove(SelectedWindow.getScrollPane());
		SelectedWindow.getComponent().removeAll();
		SelectedWindow.getScrollPane().removeAll();
		Windows.remove(SelectedWindow);
		SelectedWindow = null;
		if (!Windows.isEmpty())
			setSelectedWindow(Windows.get(0));
		redraw();
		return true;
	}
	
	public void redraw() {
		if (ToolBar!=null) ToolBar.setBounds(0, 3, getWidth(), MenuBarSize());
		int i = 0;
    	int tabWidth = Math.min((getWidth() - 13)/(!Windows.isEmpty() ? Windows.size() : 1),
    			(getWidth()-13)/8);
    	
        for (Window win : Windows) {
        	
        	win.getScrollPane().setBounds( - 1, 
        			MenuBarSize() + TabSize(), getWidth() - 13,
        			getHeight() - MenuBarSize() - TabSize() - 45);
        	win.getTab().setBounds(tabWidth * i , MenuBarSize(), tabWidth, TabSize());
        	i++;
        }
        repaint();
	}

	/**
	 * calls save and asks user where to save if non is selected already.
	 * @return whether the operation was successful
	 */
	public boolean megaSave(boolean ask) {
		
		if (SelectedWindow.getComponent() instanceof SyntaxTextArea &&
				(SelectedWindow instanceof SyntaxTextArea)&&
				(Windows.size()==1&&"\000".equals
				(((SyntaxTextArea)SelectedWindow.getComponent()).FilePath)))
		{
			int result = JOptionPane.showConfirmDialog((Component) null,
					"Do you want to save?","alert", JOptionPane.YES_NO_OPTION);
	    	if (result==JOptionPane.NO_OPTION)
	    		return true;
	    	if (result==JOptionPane.CANCEL_OPTION) {
	    		return false;
	    	}
	    	((SyntaxTextArea)SelectedWindow.getComponent()).openfile(false, true);
		}
		if (Windows.size()!=1) {
			boolean res = true;
			for (Window i: Windows) {
				res = i.Save(ask);
			}
			return res;
		}
		if (SelectedWindow.getComponent() instanceof SyntaxTextArea)
			return SelectedWindow.Save(ask);
		return true;
	}
	
	/**
	 * This does nothing yet.
	 */
	@Unfinnished
	public boolean ConfirmExit() {
		//Planned
		return true;
	}
	
	/**
	 * Pull up the close menu and runs System.exit(0); if user agrees.
	 * @throws InterruptedException 
	 */
	public void close() throws InterruptedException {
		boolean saved = isSaved();
		if (isBusy()) while (isBusy()) 
			{Thread.sleep(100);System.out.println("Busy, Waiting for the clear.");}
		
		if (saved&&!isBusy()) System.exit(0);
		int result = !isSaved() ? JOptionPane.showConfirmDialog(null,
				"Do you want to exit?","alert", JOptionPane.YES_NO_OPTION) : JOptionPane.YES_OPTION;
    	if (result==JOptionPane.NO_OPTION||result==JOptionPane.CANCEL_OPTION)
    		return;
    	boolean save = !isSaved() ? megaSave(true) : true;
    	if (!save) return;
    	System.exit(0);
	}
	
	private boolean isSaved() {
		for (Window i : Windows) {
			if (!i.isSaved()) return false;
		}
		return true;
	}
	
	/**
	 * Refreshes the settings from the map of stng
	 * @apiNote This rereads changes made to the properties file
	 * @throws ValueNotFoundException
	 * @throws IOException
	 */
	public void refreshSettings() throws ValueNotFoundException, IOException {
		isbusy = true;
		for (Window i : Windows) 
			i.refresh();
		this.revalidate();
		redraw();
		isbusy = false;
	}
	
	@Override 
	public void paint(Graphics g) {
		g.setColor(MenuBG);
		g.fillRect(0, 0, getWidth(), getHeight()-13);
		super.paint(g);
	}
	
	protected boolean isbusy = false;
	
	public boolean isBusy() {return isbusy;}
	
}