package NNU.Editor;

import static NNU.Editor.Utils.Utils.EDITORNAME;
import static NNU.Editor.Utils.Utils.EFFECTIVE_EDITORNAME;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import NNU.Editor.AssetManagement.StringTable;
import NNU.Editor.FolderManager.FolderPanel;
import NNU.Editor.Menus.Components.FolderButton;
import NNU.Editor.Menus.Components.MenuThingy;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Runners.HTMLRunner;
import NNU.Editor.Runners.IRunner;
import NNU.Editor.Runners.ProjectRunner;
import NNU.Editor.Runners.PythonRunner;
import NNU.Editor.Runners.ShellRunner;
import NNU.Editor.Utils.SwingUtils;
import NNU.Editor.Utils.UserMessager;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.ImageWindow;
import NNU.Editor.Windows.PreferencesWindow;
import NNU.Editor.Windows.PropertiesWindow;
import NNU.Editor.Windows.TextEditorWindow;
import NNU.Editor.Windows.Interfaces.Editor;
import NNU.Editor.Windows.Interfaces.Savable;
import NNU.Editor.Windows.Interfaces.Window;

/**
 * the editor
 */
public class App extends JFrame {
	private static final long serialVersionUID = 5770603365260133811L;
	
	public static final String PROJ = "project";
	
	public static Map<String, IRunner> runners = new HashMap<String,IRunner>(); static {
		runners.put(PROJ, new ProjectRunner());
		runners.put("python", new PythonRunner());
		runners.put("shell", new ShellRunner());
		runners.put("html", new HTMLRunner());
	}
	
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final String TITLEBARBG = "JRootPane.titleBarBackground";
    public static Color MenuBG = new Color(19, 19, 26);
    public static Color MenuFG = new Color(240, 240, 240);
	private Window SelectedWindow;
	public List<Window> Windows = new ArrayList<Window>();
	public JPanel contentpane;
	public FolderPanel Folder;
	public MenuThingy ToolBar;
	public JButton FolderButton;
	public static Settings stng;
	public final Settings theme;
	public static final Map<String, Object> fileext = new Settings
			(App.class.getResourceAsStream("/NNU/Editor/FileExt.properties")).map;

	private static final int MNBS = 30;
	private static final int TBS = 50;
	protected boolean isbusy = false;
	
	public boolean isBusy() {return isbusy;}
	
	public static int MenuBarSize() {
		return MNBS;
	}
	
	public static int TabSize() {
		return TBS;
	}
	
	public static int FolderSize() {
		return TBS;
	}
	
	@Override
	public Font getFont() {
		try {
			return stng.getFont("editor.font");
		} catch (Exception e) {
			e.printStackTrace();
			return new Font(Font.MONOSPACED, Font.BOLD, 40);
		}
	}
	
	public Font getTipFont() {
		return new Font("Arial", Font.BOLD, 40);
	}
	
	static {
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.
				getDefaultInstance();
		
		/* I didn't manage to get this to work in time so I disabled it as of now */
		atmf.putMapping("text/bf", "NNU.Editor.Tokenizers.TestTokenizer");
		atmf.putMapping("text/test", "NNU.Editor.Tokenizers.TestTokenizer");
	}
	
	public Point getLocationRelativeTo() {
	    int x = getX() - MouseInfo.getPointerInfo().getLocation().x;
	    int y = getY() - MouseInfo.getPointerInfo().getLocation().y;
	    return new Point(Math.abs(x), Math.abs(y));
	}
	
	public static final int MINIMUM_DISTANCE = 50;
	AtomicBoolean boolean1 = new AtomicBoolean();
	AtomicBoolean boolean2 = new AtomicBoolean();
	
	/**
	 * the app
	 * @param filepath the path to read from upon startup (leave empty for none)
	 * @throws IOException
	 */
	public App(String filepath) throws IOException {
    	
		this.setOpacity(1);
    	
    	File stfile = new File(Utils.getProgramPath() +
    			"/" + EFFECTIVE_EDITORNAME + "/" + EFFECTIVE_EDITORNAME + ".properties");
    	stng = new Settings(stfile.getAbsolutePath(), this);
    	theme = stng;
    	
    	StringTable.loadLang("en");//System.getProperty("user.language.format"));
    	
    	EDITORNAME = StringTable.getString("editor.name");
    	
    	try {
			MenuBG = theme.getColor("bgcolor");
		} catch (Exception e) {e.printStackTrace();}
    	
    	String starttext = "";
    	String startfolder = "";
    	
    	if (!"".equals(filepath)) {
    		if (!new File(filepath).isDirectory())
    			starttext = Utils.read(filepath);
    		else
    			startfolder = new File(filepath).getAbsolutePath();
    	}
    	
    	this.setIconImage(Utils.ResizeImage(Utils.ReadImageIcon("ui/NNUEdit72x72.png").getImage(),64,64));

        setSize((int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2);
        
        contentpane = new AppPanel(this); 
    	FolderButton = new FolderButton(this);
    	FolderButton.setOpaque(true);
    	FolderButton.setBorderPainted(false); 
    	FolderButton.setContentAreaFilled(false); 
    	FolderButton.setFocusPainted(false); 
    	FolderButton.setOpaque(false);
    	FolderButton.setVisible(false);
    	FolderButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	FolderButton.setBounds(100, 100,60,70);
    	FolderButton.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				toggleFolder();
				if (isFolderOpen()) {
					FolderButton.setVisible(true);
		        	boolean2.set(true);
				} else {
					FolderButton.setVisible(false);
		        	boolean2.set(false);
				}
			}
		});
    	
    	int intreval = 3;
    	int frames = 10;
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener( ev -> {
	    	Point p = getLocationRelativeTo();
	    	if (p.getX()<(isFolderOpen()?Folder.getWidth()+getBuffer():0)+MINIMUM_DISTANCE) {
	    		if (!FolderButton.isVisible()) {
	    			
	    			if (boolean1.get()) return;
	    			boolean1.set(true);
	    			boolean2.set(true);
		    		FolderButton.setVisible(true);
		        	FolderButton.setLocation((isFolderOpen()?Folder.getWidth()+getBuffer():0)-30,
		        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2);
		        	SwingUtils.animate(FolderButton, new Point(isFolderOpen()?Folder.getWidth()+getBuffer():0,
		        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2), frames, intreval,
		        			() ->boolean1.set(false));
	    		}
	    	} else {
    			if (boolean1.get()) return;
    			if (!boolean2.get()) return;
	        	boolean1.set(true);
	        	boolean2.set(false);
	    		FolderButton.setVisible(true);
	        	FolderButton.setLocation((isFolderOpen()?Folder.getWidth()+getBuffer():0),
	        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2);
	        	SwingUtils.animate(FolderButton, new Point((isFolderOpen()?Folder.getWidth()+getBuffer():0)-30,
	        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2), frames, intreval,
	        			() ->{boolean1.set(false);FolderButton.setVisible(false);});
	        	/*Main.animate(jb, new Point((isFolderOpen()?Folder.getWidth()+getBuffer():0)-30,
	        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2), 30, 10,true);*/
	        	//boolean1.set(false);
	    	}
		}, eventMask);
        
        Folder = new FolderPanel(this);
		
        Folder.setBounds(0,0,300,getHeight()-MenuBarSize()-TabSize());
        Folder.setVisible(true);
        Folder.setFolderPath(startfolder);
        contentpane.add(Folder);
		contentpane.add(FolderButton);
        
        setContentPane(contentpane);
    	try {
    		setDefaultLookAndFeelDecorated(true);
    		getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.white);
    		getRootPane().putClientProperty("JRootPane.menuBarEmbedded", false);
    	} catch (Exception e) {
    	    System.err.println("UI Exception caught: " + e);
    	}

        SelectedWindow = new TextEditorWindow(this,starttext);
        ((EditorTextArea)SelectedWindow.getComponent()).setFilePath(
        		new File(filepath).isDirectory() ? "" : filepath);
        ((EditorTextArea)SelectedWindow.getComponent()).setSaved(true);
        
        //Menu
        ToolBar = new MenuThingy(this);
        ToolBar.setBackground(MenuBG);
        ToolBar.setMinimumSize(new Dimension(getWidth(),100));
        ToolBar.setForeground(MenuFG);
        
        
        
        //Add Things to contentpane
        ToolBar.setBounds(0, 0, getWidth(), MenuBarSize());
        contentpane.setLayout(null);
        contentpane.setSize(getSize());
        contentpane.setOpaque(true);
        contentpane.setBackground(MenuBG.brighter());
        contentpane.add(ToolBar);
        //contentpane.add(ToolBar);
        
        initListeners();
        
        setUndecorated(false);
        setBackground(newColorWithAlpha(MenuBG,255));
        setTitle(EDITORNAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocation(getWidth()/2,getHeight()/2);
        this.contentpane.setDoubleBuffered(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        this.setSelectedWindow(SelectedWindow);
        redraw();
        revalidate();
        repaint();
        
    }
	public static Color newColorWithAlpha(Color color, int alpha) {
	    return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
	
	public void initListeners() {
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
				} catch (Exception e) {e.printStackTrace();}
	            for (File file : droppedFiles) {
	            	if (file.isDirectory()) {
	            		UserMessager.showErrorDialogTB("err.importdir.title","err.importdir",file.getName());
	            		continue;
	            	}
	                openFile(file.getAbsolutePath());
	            }
			}
        };

        // Connect the label with a drag and drop listener
        new DropTarget(this,DnDConstants.ACTION_COPY, myDragDropListener, true, null);
        
        addWindowStateListener(e -> redraw());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
				close();
            }
        });

        addWindowFocusListener(new WindowFocusListener() {
            @Override public void windowLostFocus(WindowEvent e) {
        		getRootPane().putClientProperty(TITLEBARBG, new Color(45,45,45));
            }
            @Override public void windowGainedFocus(WindowEvent e) {
        		getRootPane().putClientProperty(TITLEBARBG, Color.black);
            }
        });
        
        getRootPane().addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {redraw();contentpane.repaint();}
            @Override public void componentMoved(ComponentEvent e)   {redraw();contentpane.repaint();}
        });
		
        /* Save current window (CTRL + S) */
        getRootPane().registerKeyboardAction(e -> {
	        	if (SelectedWindow!=null&&SelectedWindow.getComponent() instanceof Savable) {
	        		((Savable)SelectedWindow.getComponent()).Save(false);
	        		repaintTabs();
	        	}
        	}
        	,KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK),
        	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        
        
        /* New (CTRL + N) */
        getRootPane().registerKeyboardAction(e -> {
	    		/* Get Text Editor */
	    		TextEditorWindow tew = new TextEditorWindow(this, "");
	            this.setSelectedWindow(tew);
	    	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        

        /* Find (CTRL + F) */
        getRootPane().registerKeyboardAction(e -> {
		    	if (getSelectedWindow()!=null && (getSelectedWindow().isEditor()))
		    		getSelectedWindow().getEditor().openFind();
		    	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        

        /* Goto Line (CTRL + G) */
        getRootPane().registerKeyboardAction(e -> {
		    	if (getSelectedWindow()!=null 
		    			&& (getSelectedWindow().getComponent() instanceof EditorTextArea )) {
					EditorTextArea tx = (EditorTextArea) getSelectedWindow().getComponent();
					tx.gotoLineMenu();
		    	}
	    	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        

        /* Escape (ESCAPE) */
        getRootPane().registerKeyboardAction(e -> {
		    	if (getSelectedWindow()!=null&&(getSelectedWindow().isEditor())) {
		    		getSelectedWindow().getEditor().escape();
		    	}
	    	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        
        
        /* SaveAll (CTRL + SHIFT + S) */
        getRootPane().registerKeyboardAction(e -> {SaveAll(false);repaintTabs();}
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
        		tew.getEditor().setFilePath(file);
	            /* Set Selected Window and redraw() */
                setSelectedWindow(tew);
        	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );

        
        
        /* Close Window (CTRL + W) */
        getRootPane().registerKeyboardAction(e ->
        	closeSelectedWindow()
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );

        
        getRootPane().setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
        		Collections.emptySet());
        /* Close Window (CTRL + TAB) */
        getRootPane().registerKeyboardAction(e -> {
        		int loc = Windows.indexOf(SelectedWindow);
        		setSelectedWindow(Windows.get(loc+1>Windows.size()-1?0:loc+1));
        	}
    		,KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        addKeyListener(new KeyAdapter() { @Override public void keyPressed(KeyEvent e) {}});
        
        /* Open preferences menu (CTRL + P) */
        getRootPane().registerKeyboardAction(e ->
        	setSelectedWindow(new PreferencesWindow(this))
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        
        
        /* Test Button (F5) */
        getRootPane().registerKeyboardAction(e -> 
        	RunApp()
	    	,KeyStroke.getKeyStroke("F5"),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        
        
        /* Test Button (CTRL + T) */
        getRootPane().registerKeyboardAction(e -> {
				try {
	        		if (stng.getBoolean("system.debugbutton"))
	        			showInfoPopup();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void crash() {
		/* Fuck that RAM REAL fucking hard!!! */
	    Vector v = new Vector();
	    while (true) {
	    	byte b[] = new byte[1048576];
		    v.add(b);
		    Runtime rt = Runtime.getRuntime();
		    System.out.println( "free memory: " + rt.freeMemory() );
	    }
	}

	public void repaintTabs() {
		for (Window i : Windows) {
			i.getTab().repaint();
		}
	}

	public void RunApp() {
		if (SaveAll(!isSaved())) {
			if (runners.get(PROJ).canRun(this)) {
				runners.get(PROJ).Run(this);
				return;
			}
			RunFile();
			try {
				for (Entry<String, IRunner> i : runners.entrySet()) {
					IRunner runner = i.getValue();
					if (runner.canRun(this)) {
						runner.Run(this);
						return;
					}
				}
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public void RunFile() {
		if (getSelectedWindow()!=null&&SaveAll(!isSaved())&&getSelectedWindow().isEditor()) {
			try {
				for (Entry<String, IRunner> i : runners.entrySet()) {
					IRunner runner = i.getValue();
					File f = new File(getSelectedWindow().getEditor().getFilePath());
					if (runner.canRunFile(f,this)) {runner.RunFile(f,this);return;}
				}
			} catch (Exception e) {e.printStackTrace();}
		}
	}

	public void openFile(String path) {
		for (Window i : Windows) {
			if (i.getComponent()instanceof Editor&&
					((Editor)i.getComponent()).isOpen(path)) {
				setSelectedWindow(i);
				return;
			}
		}
		String type = Utils.getFileType(path);
		switch (type) {
			
			case "img":
				setSelectedWindow(new ImageWindow(this, new File(path)));
				break;
			case "prop":
				Window propWindow = new PropertiesWindow(this, new File(path));
				setSelectedWindow(propWindow);
				break;
			default:
				openEditor(path);
				break;
		}
	}
	public void openEditor(String path) {
		for (Window i : Windows) {
			if (i.isEditor()&&i.getEditor().isOpen(path)) {
				setSelectedWindow(i);
				return;
			}
		}
		String s = Utils.read(path);
		//System.out.println(Duration.between(start, Instant.now()).toMillis());
		TextEditorWindow w = new TextEditorWindow(this, s);
		//System.out.println(Duration.between(start, Instant.now()).toMillis());
    	EditorTextArea sy = (EditorTextArea) w.getComponent();
		sy.setFilePath(path);
		setSelectedWindow(w);
		sy.setSaved(true);
	}
	
	public void setSelectedWindow(Window w) {
		for (Window i : Windows)
			i.getScrollPane().setVisible(false);
		if (w!=null) {
			if (SelectedWindow!=null) SelectedWindow.lostFocus(w);
			w.gainedFocus(SelectedWindow);
			w.getScrollPane().setVisible(true);
	        w.getComponent().setBorder(new EmptyBorder(0,5,0,0));
			w.getComponent().requestFocus();
			SelectedWindow = w;
		} else {
			Folder.setVisible(true);
		}
		redraw();
	}
	
	public boolean closeSelectedWindow() {
		if (SelectedWindow==null) System.exit(0);
		boolean b = SelectedWindow.closeEvent(!SelectedWindow.isSaved());
		if (!b) return false;
		
		int loc = Windows.indexOf(SelectedWindow)-1;
		
		SelectedWindow.lostFocus(null);
		
		contentpane.remove(SelectedWindow.getComponent());
		contentpane.remove(SelectedWindow.getTab());
		contentpane.remove(SelectedWindow.getScrollPane());
		SelectedWindow.getComponent().removeAll();
		SelectedWindow.getScrollPane().removeAll();
		Windows.remove(SelectedWindow);
		SelectedWindow.delete();
		SelectedWindow = null;
		if (!Windows.isEmpty()) {
			if (loc>-1)
				setSelectedWindow(Windows.get(loc));
			else
				setSelectedWindow(Windows.get(0));
		} else {
			Folder.setVisible(true);
		}
		redraw();
		return true;
	}
	
	public void redraw() {

    	try {
			MenuBG = theme.getColor("bgcolor");
		} catch (Exception e) {e.printStackTrace();}
    	
		//System.gc();
		if (ToolBar!=null) ToolBar.setBounds(0, 0, getWidth(), MenuBarSize());
		int i = 0;
		int x = isFolderOpen() ? Folder.getWidth() : -getBuffer();
		int width = contentpane.getWidth();
    	int tabWidth = Math.min((width - x)/(!Windows.isEmpty() ? Windows.size() : 1),
    			325);
        for (Window win : Windows) {
        	win.getScrollPane().setBounds(x, 
        			MenuBarSize() + TabSize(), width - x,
        			contentpane.getHeight() - MenuBarSize() - TabSize());
        	win.getTab().setBounds((tabWidth * i) + x + getBuffer() - Tab.padding,
        			MenuBarSize(), tabWidth, TabSize());
        	//win.getTab().repaint();
        	win.resize();
        	i++;
        }
        Folder.setBounds(0,MenuBarSize(),Folder.getWidth(), contentpane.getHeight()-MenuBarSize());
        
        Folder.FilesPanel.setBounds(0,Folder.MenuBarSize(),
        		Folder.getWidth()-FolderPanel.DRAGBAR_WIDTH, Folder.getHeight()-MenuBarSize());
        
        Folder.refresh(false);
        FolderButton.setLocation((isFolderOpen()?Folder.getWidth()+getBuffer():0),
    			(contentpane.getHeight() - MenuBarSize() - TabSize())/2);
        
        revalidate();
        repaint();
	}

	/**
	 * calls save and asks user where to save if non is selected already.
	 * @return whether the operation was successful
	 */
	public boolean SaveAll(boolean ask) {
		if (getSelectedWindow()==null) return true;
		if (SelectedWindow.getComponent() instanceof EditorTextArea &&
				(SelectedWindow instanceof EditorTextArea)&&
				(Windows.size()==1&&"".equals
				(((EditorTextArea)SelectedWindow.getComponent()).getFilePath())))
		{
			int result = UserMessager.confirmTB("confirm.save","confirm.save");
	    	if (result==UserMessager.NO_OPTION)
	    		return true;
	    	if (result==UserMessager.CANCEL_OPTION) {
	    		return false;
	    	}
	    	((EditorTextArea)SelectedWindow.getComponent()).openfile(false, true);
		}
		if (Windows.size()!=1) {
			if (ask) {
				int result = UserMessager.confirmTB("confirm.save","confirm.save");
		    	if (result==UserMessager.NO_OPTION)
		    		return true;
		    	if (result==UserMessager.CANCEL_OPTION) {
		    		return false;
		    	}
		    	for (Window i : Windows)
		    		if (i.getComponent() instanceof EditorTextArea&&
		    				"".equals(((EditorTextArea)i.getComponent()).getFilePath()))
		    	    	((EditorTextArea)i.getComponent()).openfile(false, true);
			}
			boolean res = true;
			for (Window i: Windows) {
				res = i.Save(ask);
			}
			return res;
		}
		if (SelectedWindow.getComponent() instanceof Savable)
			return SelectedWindow.Save(ask);
		return true;
	}
	
	
	public void openFolderAndDialog() {
		String s = Utils.openFolderDialog().trim();
		Folder.setFolderPath(s==null ? Folder.getFolderPath() : s);
	}
	
	/**
	 * Pull up the close menu and runs System.exit(0); if user agrees.
	 * @throws InterruptedException 
	 */
	public void close() {
		try {
			boolean saved = isSaved();
			if (isBusy()) while (isBusy()) 
				{Thread.sleep(100);System.out.println("Busy, Waiting for the clear.");}
			
			if (saved&&!isBusy()) System.exit(0);
			int result = !isSaved() ? 
					UserMessager.confirmTB("confirm.exit","confirm.exit") : UserMessager.YES_OPTION;
	    	if (result==UserMessager.NO_OPTION||result==UserMessager.CANCEL_OPTION)
	    		return;
	    	boolean save = !isSaved() ? SaveAll(true) : true;
	    	if (!save) return;
	    	System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	public void refreshSettings() throws IOException {
		isbusy = true;
		for (Window i : Windows) 
			i.refresh();
		this.revalidate();
		isbusy = false;
	}
	
	
	/**
	 * Close a window that is loaded but not selected
	 * @param window
	 * @return
	 */
	public boolean closeNotSelectedWindow(Window window) {
		if (window==SelectedWindow) return closeSelectedWindow();
		
		boolean b = window.closeEvent(true);
		if (!b) return false;
		
		contentpane.remove(window.getComponent());
		contentpane.remove(window.getTab());
		contentpane.remove(window.getScrollPane());
		window.getComponent().removeAll();
		window.getScrollPane().removeAll();
		Windows.remove(window);
		window = null;
		redraw();
		return true;
	}

	public void toggleFolder() {
		Folder.setVisible(!Folder.isVisible());
		redraw();
	}
	
	public Window getSelectedWindow() {
		return SelectedWindow;
	}

	public boolean isFolderOpen() {return Folder.isVisible();}

	private static boolean getAntialiasing() {return stng.getBoolean("editor.antialias");}

	public void closeWindow(Window window) {
		if (getSelectedWindow()==window)
			closeSelectedWindow();
		else
			closeNotSelectedWindow(window);
	}

	public static int getBuffer() {
		return 5;
	}

	public void showInfoPopup() throws Exception {
		UserMessager.showMessageDialogTB("editor.infomenu.title", "editor.infomenu",
				getLinesInOpenFile(),
				getWordsInDocument(),
				countTotalLines(new File(Folder.getFolderPath()))
			);
	}

	private Object getWordsInDocument() throws Exception {

		if (getSelectedWindow().isEditor()) {
			int lines = 0;
			if (getSelectedWindow().getComponent() instanceof JTextComponent) {
		        Document d = ((JTextComponent)getSelectedWindow().getComponent()).getDocument();
		        try {
		        	lines = d.getText(0, d.getLength()).trim().split(" ",-1).length;
				} catch (BadLocationException e) {e.printStackTrace();}
		        if (d.getText(0, d.getLength()).trim().length()==0) lines = 0;
			} else if (getSelectedWindow().getEditor().getFilePath()!="") {
		        Charset encoding = Charset.defaultCharset();
				InputStream in = new FileInputStream(getSelectedWindow().getEditor().getFilePath());
	            Reader reader = new InputStreamReader(in, encoding);
	            // buffer for efficiency
	            Reader buffer = new BufferedReader(reader);
	            int c;
	            while ((c=buffer.read())!=-1)
	            	if (((char)c)==' ')
	            		lines++;
	            lines++;
				System.out.println(lines);
			} 
			return String.valueOf(lines);
		}
		
		return "No editor selected";
	}

	private int countTotalLines(File file) throws IOException {
		int lines = 0;
		if (file.isDirectory()) {
			if (file.getName().equals(".git")) return 0;
			for (File f : file.listFiles())
				lines += countTotalLines(f);
		} else {
			String type = Utils.getFileType(file.getAbsolutePath());
			if (type=="img"||type=="audio"||type=="video") return 0;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null) lines++;
			reader.close();
		}
		return lines;
	}

	private String getLinesInOpenFile() throws IOException {
		
		if (getSelectedWindow().isEditor()) {
			int lines = 0;
			if (getSelectedWindow().getEditor().getFilePath()!="") {
				BufferedReader reader = new BufferedReader(
						new FileReader(getSelectedWindow().getEditor().getFilePath()));
				while (reader.readLine() != null) lines++;
				reader.close();
			} else if (getSelectedWindow().getComponent() instanceof JTextComponent) {
		        Document d = ((JTextComponent)getSelectedWindow().getComponent()).getDocument();
		        try {
		        	lines = d.getText(0, d.getLength()).split("\r\n|\r|\n",-1).length;
				} catch (BadLocationException e) {e.printStackTrace();}
			}
			return String.valueOf(lines);
		}
		return "No editor selected";
	}

	public static void adjustAntialias(Graphics2D gra, boolean fancytext) {
		if (getAntialiasing()) {
			if (!getREALLYPOWERFUL_AntiAliasing())
				gra.setRenderingHint(KEY_TEXT_ANTIALIASING, 
						fancytext?VALUE_TEXT_ANTIALIAS_LCD_HRGB:VALUE_TEXT_ANTIALIAS_ON);
			else
				gra.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			gra.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		} else {
			gra.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_OFF);
			gra.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);
		}
	}
	public static boolean getREALLYPOWERFUL_AntiAliasing() {
		return stng.getBoolean("editor.powerantialias");
	}

	public void adjustAntialias(Graphics gra, boolean fancytext) {
		adjustAntialias((Graphics2D) gra, fancytext);
	}

	public int getScale() {
		return 1;
	}
	
}