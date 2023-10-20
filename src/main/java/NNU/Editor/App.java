package NNU.Editor;

import static NNU.Editor.Utils.Utils.EDITORNAME;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import NNU.Editor.Menus.Components.FolderButton;
import NNU.Editor.Menus.Components.MenuThingy;
import NNU.Editor.Menus.Components.Tab;
import NNU.Editor.Runners.GitRunner;
import NNU.Editor.Runners.HTMLRunner;
import NNU.Editor.Runners.IRunner;
import NNU.Editor.Runners.ShellRunner;
import NNU.Editor.Utils.Unfinnished;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Utils.ValueNotFoundException;
import NNU.Editor.Windows.GitWindow;
import NNU.Editor.Windows.PreferencesWindow;
import NNU.Editor.Windows.TextEditorWindow;
import NNU.Editor.Windows.Window;

/**
 * the editor
 */
public class App extends JFrame {
	private static final long serialVersionUID = 5770603365260133811L;
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static Color MenuBG = new Color(19, 19, 26);
    public static final Color MenuFG = new Color(240, 240, 240);
    public static final String TITLEBARBG = "JRootPane.titleBarBackground";
	private Window SelectedWindow;
	public List<Window> Windows = new ArrayList<Window>();
	public JPanel contentpane;
	public FolderPanel Folder;
	public MenuThingy ToolBar;
	/**
	 * The settings
	 */
	public final Settings stng;
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
    	
    	try {
			MenuBG = new Color(stng.getInt("bgcolor r"),stng.getInt("bgcolor g"),stng.getInt("bgcolor b"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	String starttext = "";
    	String startfolder = "\000";
    	
    	if (!"".equals(filepath)) {
    		if (!new File(filepath).isDirectory())
    			starttext = Utils.read(filepath);
    		else
    			startfolder = new File(filepath).getAbsolutePath();
    	}
    	
    	this.setIconImage(Utils.ResizeImage(Utils.ReadImageIcon("ui/NNUEdit72x72.png").getImage(),64,64));

        setSize((int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2);
        

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
				} catch (UnsupportedFlavorException | IOException e) {e.printStackTrace();}
	            for (File file : droppedFiles) {
	                openFile(file.getAbsolutePath());
	            }
			}
        };

        // Connect the label with a drag and drop listener
        new DropTarget(this, myDragDropListener);
        
        contentpane = new AppPanel(this); 
        
        contentpane.addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseDragged(MouseEvent e) {}
			@Override public void mouseMoved(MouseEvent e) {
				if (SelectedWindow!=null&&SelectedWindow.getComponent() instanceof FolderButton)
					((FolderButton) SelectedWindow.getComponent())
						.getFolderButton().setVisible(e.getX()<FolderButton.MINIMUM_DISTANCE);
			}
		});
        
        Folder = new FolderPanel(this);
        Folder.setBounds(0,0,300,getHeight()-MenuBarSize()-TabSize());
        Folder.setVisible(true);
        Folder.setFolderPath(startfolder);
        contentpane.add(Folder);
        
        setContentPane(contentpane);
    	try {
    		setDefaultLookAndFeelDecorated(true);
    		getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.white);
    		getRootPane().putClientProperty("JRootPane.menuBarEmbedded", false);
    	} catch (Exception e) {
    	    System.err.println("UI Exception caught: " + e);
    	}

        SelectedWindow = new TextEditorWindow(this,starttext);
        ((SyntaxTextArea)SelectedWindow.getComponent()).setFilePath(
        		new File(filepath).isDirectory() || "".equals(filepath) ? "\000" : filepath);
        ((SyntaxTextArea)SelectedWindow.getComponent()).setSaved(true);
        
        //Menu
        ToolBar = new MenuThingy(this);
        ToolBar.setBackground(MenuBG);
        ToolBar.setMinimumSize(new Dimension(getWidth(),100));
        ToolBar.setForeground(MenuFG);
        //this.setJMenuBar(ToolBar);
        
        
        
        //Add Things to contentpane
        ToolBar.setBounds(0, 0, getWidth(), MenuBarSize());
        contentpane.setLayout(null);
        contentpane.setSize(getSize());
        contentpane.setOpaque(true);
        contentpane.setBackground(MenuBG.brighter());
        contentpane.add(ToolBar);
        //contentpane.add(ToolBar);
        
        addWindowStateListener(e -> redraw());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
					close();
				} catch (InterruptedException e1) {e1.printStackTrace();}
            }
        });

        addWindowFocusListener( new WindowFocusListener() {
            @Override public void windowLostFocus(WindowEvent e) {
        		getRootPane().putClientProperty(TITLEBARBG,
        				Color.LIGHT_GRAY.darker().darker().darker().darker());
            }
            @Override public void windowGainedFocus(WindowEvent e) {
        		getRootPane().putClientProperty(TITLEBARBG, Color.black);
            }
        });
        
        initListeners();
        
        setUndecorated(false);
        setBackground(MenuBG);
        setTitle(EDITORNAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocation(getWidth()/2,getHeight()/2);
        this.contentpane.setDoubleBuffered(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        getRootPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	redraw();
            	contentpane.repaint();
            }
            @Override
            public void componentMoved(ComponentEvent e) {
            	redraw();
            	contentpane.repaint();
            }
        });
        
        this.setSelectedWindow(SelectedWindow);
        redraw();
        repaint();
        
    }
	
	public void initListeners() {
		
        /* Save current window (CTRL + S) */
        getRootPane().registerKeyboardAction(e -> {
	        	if (SelectedWindow!=null&&SelectedWindow.getComponent() instanceof SyntaxTextArea) {
	        		((SyntaxTextArea)SelectedWindow.getComponent()).megaSave(false);
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
		    	if (getSelectedWindow()!=null 
		    			&& (getSelectedWindow().getComponent() instanceof SyntaxTextArea )) {
					SyntaxTextArea tx = (SyntaxTextArea) getSelectedWindow().getComponent();
					tx.openFind();
		    	}
	    	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        

        /* Escape (ESCAPE) */
        getRootPane().registerKeyboardAction(e -> {
		    	if (getSelectedWindow()!=null 
		    			&&(getSelectedWindow().getComponent() instanceof SyntaxTextArea)
		    			&&((SyntaxTextArea) getSelectedWindow().getComponent()).fm!=null) {
					SyntaxTextArea tx = (SyntaxTextArea) getSelectedWindow().getComponent();
					tx.remove(tx.fm);
					tx.fm = null;
					tx.repaint();
		    	}
	    	}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
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
	            ((SyntaxTextArea)tew.getComponent()).setFilePath(file);
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
        getRootPane().registerKeyboardAction(e -> 
        	{
				try {
	        		if (stng.getBoolean("testbutton"))
	        			restartApplication();
				} catch (URISyntaxException | IOException | ValueNotFoundException e1) {
					e1.printStackTrace();
				}
			}
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
	}
	
	public static List<IRunner> runners = new ArrayList<IRunner>();
	
	static {
		//runners.add(new GitRunner());
		runners.add(new ShellRunner());
		runners.add(new HTMLRunner());
	}
	
	public void RunApp() {
		if (megaSave(!isSaved()))
			for (IRunner runner : runners) {
				if (runner.canRun(this)) {
					runner.Run(this);
					return;
				}
			}
	}

	public void openFile(String path) {
		for (Window i : Windows) {
			if (i.getComponent()instanceof SyntaxTextArea&&
					((SyntaxTextArea)i.getComponent()).getFilePath().equals(path)) {
				setSelectedWindow(i);
				return;
			}
		}
		Window w = new TextEditorWindow(this, Utils.read(path));
    	SyntaxTextArea sy = ((SyntaxTextArea) 
				w.getComponent());
		sy.setFilePath(path);
		setSelectedWindow(w);
		sy.setSaved(true);
	}
    
    public boolean megaSaveAll(boolean ask) {
    	return megaSave(ask);
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
		boolean b = SelectedWindow.closeEvent(!SelectedWindow.isSaved()
				||(SelectedWindow.getComponent() instanceof SyntaxTextArea
						&&"\000".equals(((SyntaxTextArea)SelectedWindow.getComponent()).getFilePath())));
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
			redraw();
		}
		redraw();
		return true;
	}
	
	public void redraw() {
		if (ToolBar!=null) ToolBar.setBounds(0, 0, getWidth(), MenuBarSize());
		int i = 0;
		int x = isFolderOpen() ? Folder.getWidth() : -5;
		int width = contentpane.getWidth();
    	int tabWidth = Math.min((width - x)/(!Windows.isEmpty() ? Windows.size() : 1),
    			325);
        for (Window win : Windows) {
        	win.getScrollPane().setBounds(x, 
        			MenuBarSize() + TabSize(), width - x,
        			contentpane.getHeight() - MenuBarSize() - TabSize());
        	win.getTab().setBounds((tabWidth * i) + x + 5 - Tab.padding,
        			MenuBarSize(), tabWidth, TabSize());
        	win.getTab().repaint();
        	//win.getComponent().setBackground(MenuBG);
        	win.resize();
        	i++;
        }
        Folder.setBounds(0,MenuBarSize(),Folder.getWidth(), contentpane.getHeight()-MenuBarSize());

        Folder.FilesPanel.setBounds(0,Folder.MenuBarSize(),Folder.getWidth(), Folder.getHeight());
        Folder.tree.setBounds(0,0,Folder.getWidth(), Folder.getHeight()-13);
        Folder.refresh();
        
        repaint();
	}

	/**
	 * calls save and asks user where to save if non is selected already.
	 * @return whether the operation was successful
	 */
	public boolean megaSave(boolean ask) {
		if (getSelectedWindow()==null) return true;
		if (SelectedWindow.getComponent() instanceof SyntaxTextArea &&
				(SelectedWindow instanceof SyntaxTextArea)&&
				(Windows.size()==1&&"\000".equals
				(((SyntaxTextArea)SelectedWindow.getComponent()).getFilePath())))
		{
			int result = JOptionPane.showConfirmDialog((Component) null,
					"Do you want to save?","Are you sure you want to save?", JOptionPane.YES_NO_OPTION);
	    	if (result==JOptionPane.NO_OPTION)
	    		return true;
	    	if (result==JOptionPane.CANCEL_OPTION) {
	    		return false;
	    	}
	    	((SyntaxTextArea)SelectedWindow.getComponent()).openfile(false, true);
		}
		if (Windows.size()!=1) {
			if (ask) {
				int result = JOptionPane.showConfirmDialog((Component) null,
						"Do you want to save?","Are you sure you want to save?",
						JOptionPane.YES_NO_OPTION);
		    	if (result==JOptionPane.NO_OPTION)
		    		return true;
		    	if (result==JOptionPane.CANCEL_OPTION) {
		    		return false;
		    	}
		    	for (Window i : Windows)
		    		if (i.getComponent() instanceof SyntaxTextArea&&
		    				"\000".equals(((SyntaxTextArea)i.getComponent()).getFilePath()))
		    	    	((SyntaxTextArea)i.getComponent()).openfile(false, true);
			}
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
	
	
	public void openFolderAndDialog() {
		Folder.setFolderPath(Utils.openFolderDialog());
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
				"Do you want to exit?","Are you sure you want to exit?",
				JOptionPane.YES_NO_OPTION) : JOptionPane.YES_OPTION;
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
		//redraw();
		isbusy = false;
	}
	
	@Override 
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	public void paintJB() {
		//this.getGraphics().create(jb.getX(), jb.getY(), jb.getWidth(), jb.getHeight());
	}

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
	
	public void restartApplication() throws URISyntaxException, IOException
	{
	  final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
	  final File currentJar = new File
			  (Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());

	  /* is it a jar file? */
	  if(!currentJar.getName().endsWith(".jar"))
	    return;

	  /* Build command: java -jar application.jar */
	  final ArrayList<String> command = new ArrayList<String>();
	  command.add(javaBin);
	  command.add("-jar");
	  command.add(currentJar.getPath());

	  final ProcessBuilder builder = new ProcessBuilder(command);
	  builder.start();
	  System.exit(0);
	}
	
}