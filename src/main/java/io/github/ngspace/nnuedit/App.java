package io.github.ngspace.nnuedit;

import static io.github.ngspace.nnuedit.Main.settings;
import static io.github.ngspace.nnuedit.Main.theme;
import static io.github.ngspace.nnuedit.utils.Utils.EDITORNAME;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.lang.System.out;

import java.awt.AWTEvent;
import java.awt.Color;
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
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import io.github.ngspace.nnuedit.asset_manager.AssetManager;
import io.github.ngspace.nnuedit.asset_manager.extensions.ExtensionManager;
import io.github.ngspace.nnuedit.folder_management.FolderPanel;
import io.github.ngspace.nnuedit.menu.EditorTextArea;
import io.github.ngspace.nnuedit.menu.components.FolderButton;
import io.github.ngspace.nnuedit.menu.components.MenuThingy;
import io.github.ngspace.nnuedit.menu.components.Tab;
import io.github.ngspace.nnuedit.runner.IRunner;
import io.github.ngspace.nnuedit.utils.SwingUtils;
import io.github.ngspace.nnuedit.utils.UserMessager;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.utils.registry.Registries;
import io.github.ngspace.nnuedit.utils.settings.RefreshListener;
import io.github.ngspace.nnuedit.window.AboutWindow;
import io.github.ngspace.nnuedit.window.PreferencesWindow;
import io.github.ngspace.nnuedit.window.TextEditorWindow;
import io.github.ngspace.nnuedit.window.abstractions.Editor;
import io.github.ngspace.nnuedit.window.abstractions.Savable;
import io.github.ngspace.nnuedit.window.abstractions.Window;

/**
 * the editor
 */
public class App extends JFrame implements WindowListener, AWTEventListener {
	private static final long serialVersionUID = 5770603365260133811L;
	
	public static final String PROJ = "project";
    public static Color MenuBG = new Color(19, 19, 26);
    public static Color MenuFG = new Color(240, 240, 240);
	private Window SelectedWindow;
	public List<Window> Windows = new ArrayList<Window>(10);
	public JPanel contentpane;
	public FolderPanel Folder;
	public MenuThingy ToolBar;
	public JButton FolderButton;

	private static int MNBS = 30;
	private static int TBS = 50;
	protected boolean isbusy = false;
	AtomicBoolean boolean1 = new AtomicBoolean();
	AtomicBoolean boolean2 = new AtomicBoolean();
	int intreval = 3;
	int frames = 10;
	
	public static final int MINIMUM_DISTANCE = 50;
	
	private static App instance;
	
	/**
	 * the app
	 * @param filepath the path to read from upon startup (leave empty for none)
	 * @throws IOException
	 */
	public App(String filepath) throws IOException {
		instance = this;
		
		setIgnoreRepaint(false);
		
    	try {
			App.MenuBG = theme.getColor("bgcolor");
		} catch (Exception e) {e.printStackTrace();}
		
		ExtensionManager.preStartApp(this);
    	
		setOpacity(1);
    	
    	String startfolder = "";
    	File startfile = null;
    	
    	if (!"".equals(filepath)) {
    		if (!new File(filepath).isDirectory()) startfile = new File(filepath);
    		else startfolder = new File(filepath).getAbsolutePath();
    	}
    	
    	setIconImage(Utils.ResizeIcon(AssetManager.getIcon("NNUEdit72x72"),64,64).getImage());
        contentpane = new AppPanel(this); 
    	FolderButton = new FolderButton(this);
        
        Folder = new FolderPanel(this);
		
		try {if (Main.settings.getBoolean("folder.openlast"))
				if (new File(Main.settings.get("folder.last")).exists())
					Folder.setFolderPath(Main.settings.get("folder.last"));
		} catch (Exception e) {e.printStackTrace();}
		
        if (startfolder!="") Folder.setFolderPath(startfolder);
        
        setContentPane(contentpane);
    	try {setDefaultLookAndFeelDecorated(true);
    		getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.white);
    		getRootPane().putClientProperty("JRootPane.menuBarEmbedded", false);
    	} catch (Exception e) {System.err.println("UI Exception caught: " + e.getLocalizedMessage());}
        
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
        contentpane.add(Folder);
		contentpane.add(FolderButton);
        
        initListeners();
        
        setUndecorated(false);
        setBackground(Utils.newColorWithAlpha(MenuBG,255));
        setTitle(EDITORNAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocation(getWidth()/2,getHeight()/2);
        contentpane.setDoubleBuffered(true);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
        if (startfile!=null)
        addWindow(Registries.WindowFactories.get(Editor.DEFAULT).CreateWindowFromFile(this, startfile),true,false);
    	addWindow(new PreferencesWindow(this),true,false);
        
        redraw();
        revalidate();
        repaint();
    }
	
	public void initListeners() {
		Toolkit.getDefaultToolkit().addAWTEventListener(this,
				AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
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
        new DropTarget(this,DnDConstants.ACTION_COPY, myDragDropListener, true, null);
        
        addWindowStateListener(e -> redraw());
        addWindowListener(this);
        
        addWindowFocusListener(new WindowFocusListener() {
            @Override public void windowLostFocus(WindowEvent e) {
        		getRootPane().putClientProperty("JRootPane.titleBarBackground", new Color(45,45,45));
            }
            @Override public void windowGainedFocus(WindowEvent e) {
        		getRootPane().putClientProperty("JRootPane.titleBarBackground", Color.black);
            }
        });
        
        getRootPane().addComponentListener(new ComponentListener() {
            @Override public void componentResized(ComponentEvent e) {redraw();}
            @Override public void componentMoved(ComponentEvent e)   {redraw();}
			@Override public void componentShown(ComponentEvent e)   {redraw();}
			@Override public void componentHidden(ComponentEvent e)  {redraw();}
        });
        
        getRootPane().setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.emptySet());
		
		Main.settings.addRefreshListener((s)-> {
			isbusy = true;
			revalidate();
			redraw();
			isbusy = false;
		});
	}
	
	/**
	 * There used to be a crash() function that will insta kill your ram and I removed it because I didn't want
	 * Extension creators to abuse it.
	 */
	
	/**
	 * Repaints all Tabs (not the entire Window!)
	 */
	public void repaintTabs() {for (Window i : Windows)i.getTab().repaint();}

	public void RunApp() {
		if (SaveAll(!isSaved())) {
			if (Registries.Runners.get(PROJ).canRun(this)) {
				Registries.Runners.get(PROJ).Run(this);
				return;
			}
			RunFile();
			try {
				for (Entry<String, IRunner> i : Registries.Runners.entrySet()) {
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
				File f = new File(getSelectedWindow().getEditor().getFilePath());
				for (Entry<String, IRunner> i : Registries.Runners.entrySet()) {
					IRunner runner = i.getValue();
					if (runner.canRunFile(f,this)) {runner.RunFile(f,this);return;}
				}
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	public void openFile(File path) {openFile(path.getAbsolutePath());}
	public void openFile(String path) {
		for (Window i : Windows) {
			if (i.isOpen(path)) {
				setSelectedWindow(i);
				return;
			}
		}
		addWindow(Window.createWindowFromFile(this, new File(path)));
	}
	public void openDefaultTextEditor(File path) {
		for (Window i : Windows) {
			if (i.isOpen(path.getAbsolutePath())) {
				setSelectedWindow(i);
				return;
			}
		}
		addWindow(Registries.WindowFactories.get(Editor.DEFAULT).CreateWindowFromFile(this, path));
	}
	
	public Window addWindow(Window w) {return addWindow(w,true,true);}
	public Window addWindow(Window w, boolean setaswindow) {return addWindow(w,true,true);}
	/**
	 * Adds a window
	 * @param w - the window
	 * @param setaswindow - whether to automatically select the window
	 * @param redraw - PLEASE ONLY USE IF YOU KNOW WHAT YOU ARE DOING.
	 * @return
	 */
	public Window addWindow(Window w, boolean setaswindow, boolean redraw) {
		if (w==null) return null;
		if (Windows.contains(w))
			return w;
		for (Window i : Windows)
			i.getScrollPane().setVisible(false);
        contentpane.add(w.getScrollPane());
        contentpane.add(w.getTab());
        Windows.add(w);
        if (setaswindow) {
        	if (getSelectedWindow()!=null)
        		getSelectedWindow().lostFocus(w);
            setSelectedWindow(w,false);
        } else {
            setSelectedWindow(getSelectedWindow(),false);
            w.getScrollPane().setVisible(false);
        }
        if (redraw) {
        	redraw();
        }
        return w;
	}

	public void setSelectedWindow(Window w) {setSelectedWindow(w,true);}
	public void setSelectedWindow(Window w,boolean redraw) {
		boolean isAdded = false;
		for (Window i : Windows) {
			if (i==w) isAdded = true;
			i.getScrollPane().setVisible(false);
		}
		if (!isAdded)
			addWindow(w);
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
		if (redraw)
			redraw();
	}
	
	public boolean closeSelectedWindow() {
		if (SelectedWindow==null) close();
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
	
	private List<RefreshListener> preRedrawListeners = new ArrayList<RefreshListener>();
	
	public void addPreRefreshListener(RefreshListener listener) {
		preRedrawListeners.add(listener);
	}
	
	private List<RefreshListener> redrawListeners = new ArrayList<RefreshListener>();
	
	public void addRedrawListener(RefreshListener listener) {
		redrawListeners.add(listener);
	}
	
	private static void dispatchRefresh(List<RefreshListener> ls) {
		for (RefreshListener ccl : ls)
			ccl.refresh(settings);
	}
	
	public void redraw() {
		
		dispatchRefresh(preRedrawListeners);
		
		if (ToolBar!=null) ToolBar.setBounds(0, 0, getWidth(), MenuBarSize());
		int i = 0;
		int x = isFolderOpen() ? Folder.getWidth() : -getBuffer()-0;
		int width = contentpane.getWidth();
    	int tabWidth = Math.min((width - x)/(!Windows.isEmpty() ? Windows.size() : 1),
    			325);
        for (Window win : Windows) {
        	win.getScrollPane().setBounds(x, MenuBarSize() + TabSize(), width - x,
        			contentpane.getHeight() - MenuBarSize() - TabSize());
        	win.getTab().setBounds((tabWidth * i) + x + getBuffer() - Tab.padding,
        			MenuBarSize(), tabWidth, TabSize());
        	i++;
        }
        Folder.setBounds(0,MenuBarSize(),Folder.getWidth(), contentpane.getHeight()-MenuBarSize());
        Folder.FilesPanel.setBounds(0,Folder.MenuBarSize(),
        		Folder.getWidth()-FolderPanel.DRAGBAR_WIDTH, Folder.getHeight()-MenuBarSize());
        Folder.refresh(false);
        FolderButton.setLocation((isFolderOpen()?Folder.getWidth()+getBuffer():0),
    			(contentpane.getHeight() - MenuBarSize() - TabSize())/2);
    	
		dispatchRefresh(redrawListeners);
        
        revalidate();
        repaint();
	}

	/**
	 * calls save and asks user where to save if non is selected already.
	 * @return whether the operation was successful
	 */
	public boolean SaveAll(boolean ask) {
		if (getSelectedWindow()==null) return true;
		if (SelectedWindow.isEditor()&&(Windows.size()==1&&"".equals(SelectedWindow.getEditor().getFilePath()))) {
			int result = UserMessager.confirmTB("confirm.save","confirm.save");
	    	if (result==UserMessager.NO_OPTION) return true;
	    	if (result==UserMessager.CANCEL_OPTION) return false;
	    	SelectedWindow.getEditor().openfile(false, true);
		}
		if (Windows.size()!=1) {
			if (ask) {
				int result = UserMessager.confirmTB("confirm.save","confirm.save");
		    	if (result==UserMessager.NO_OPTION) return true;
		    	if (result==UserMessager.CANCEL_OPTION) return false;
		    	for (Window i : Windows)
		    		if (i.getComponent() instanceof EditorTextArea&&
		    				"".equals(((EditorTextArea)i.getComponent()).getFilePath()))
		    	    	((EditorTextArea)i.getComponent()).openfile(false, true);
			}
			boolean res = true;
			for (Window i: Windows) res = i.Save(ask);
			return res;
		}
		if (SelectedWindow.getComponent() instanceof Savable) return SelectedWindow.Save(ask);
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
				{Thread.sleep(100);out.println("Busy, Waiting for the clear.");}
			
			if (saved&&!isBusy()) {
				ExtensionManager.shutdownApp(this);
				System.exit(0);
			}
			int result = !saved ? 
					UserMessager.confirmTB("confirm.exit","confirm.exit") : UserMessager.YES_OPTION;
	    	if (result==UserMessager.NO_OPTION||result==UserMessager.CANCEL_OPTION)
	    		return;
	    	boolean save = !isSaved() ? SaveAll(true) : true;
	    	if (!save) return;
	    	ExtensionManager.shutdownApp(this);
	    	System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isSaved() {
		for (Window i : Windows)
			if (!i.isSaved()) return false;
		return true;	
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
		
		window.delete();
		contentpane.remove(window.getComponent());
		contentpane.remove(window.getTab());
		contentpane.remove(window.getScrollPane());
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

	public void closeWindow(Window window) {
		if (getSelectedWindow()==window) closeSelectedWindow();
		else closeNotSelectedWindow(window);
	}

	public static void adjustAntialias(Graphics2D gra, boolean fancytext) {
		if (getAntialiasing()) {
			if (!(getREALLYPOWERFUL_AntiAliasing()||fancytext))
				gra.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
			else 
				gra.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			gra.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		} else {
			gra.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_OFF);
			gra.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);
		}
	}
	public static boolean getREALLYPOWERFUL_AntiAliasing() {return settings.getBoolean("editor.powerantialias");}
	public static void adjustAntialias(Graphics gra, boolean fancytext) {adjustAntialias((Graphics2D)gra,fancytext);}
	public static int getBuffer() {return 5;}
	public static int MenuBarSize() {return MNBS;}
	public static int TabSize() {return TBS;}
	public static int FolderSize() {return TBS;}
	public static App getInstance() {return instance;}
	public boolean isBusy() {return isbusy;}
	public int getScale() {return 1;}
	public Font getTipFont() {return new Font("Arial", Font.BOLD, 40);}
	public Window getSelectedWindow() {return SelectedWindow;}
	public boolean isFolderOpen() {return Folder.isVisible();}
	public static boolean getAntialiasing() {return settings.getBoolean("editor.antialias");}
	@Override public Font getFont() {
		try {
			return settings.getFont("editor.font");
		} catch (Exception e) {
			e.printStackTrace();
			return new Font(Font.MONOSPACED, Font.BOLD, 40);
		}
	} public Point getLocationRelativeTo() {
	    int x = getX() - MouseInfo.getPointerInfo().getLocation().x;
	    int y = getY() - MouseInfo.getPointerInfo().getLocation().y;
	    return new Point(Math.abs(x), Math.abs(y));
	}
	@Override public void windowClosing(WindowEvent e) {close();}
	@Override public void windowOpened(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {redraw();}
	@Override public void windowDeiconified(WindowEvent e) {redraw();}
	@Override public void windowActivated(WindowEvent e)   {redraw();}
	@Override public void windowDeactivated(WindowEvent e) {redraw();}

	@Override
	public void eventDispatched(AWTEvent ev) {
		if (ev instanceof KeyEvent) {
			KeyEvent e = (KeyEvent) ev;
			if (e.getID()!=KeyEvent.KEY_RELEASED) return;
			if (e.getKeyCode()==KeyEvent.VK_F5) {RunApp();return;}
			if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
		    	if (getSelectedWindow()!=null&&(getSelectedWindow().isEditor()))
		    		getSelectedWindow().getEditor().escape();
		    	return;
		    }
			if (e.isControlDown()) {
				if (e.isShiftDown()) {
					switch (e.getKeyCode()) {
						case KeyEvent.VK_S: {SaveAll(false);repaintTabs();}
					}
					return;
				}
				switch (e.getKeyCode()) {
					case KeyEvent.VK_S: {
			        	if (SelectedWindow!=null&&SelectedWindow.getComponent() instanceof Savable) {
			        		((Savable)SelectedWindow.getComponent()).Save(false);
			        		repaintTabs();
			        	}break;
					}
					case KeyEvent.VK_N: {
						try {
			        		if (settings.getBoolean("system.debugbutton")) {
			        			Registries.WindowFactories.clear();
			        			Registries.WindowFactories.put(Editor.DEFAULT, (a,f) -> new AboutWindow(a));
			        		}
						} catch (Exception e1) {
							e1.printStackTrace();
						}break;
					}
					case KeyEvent.VK_F: {if (getSelectedWindow()!=null && (getSelectedWindow().isEditor()))
				    		getSelectedWindow().getEditor().openFind();break;}
					case KeyEvent.VK_G: {
				    	if (getSelectedWindow()!=null 
				    			&& (getSelectedWindow().getComponent() instanceof EditorTextArea )) {
							EditorTextArea tx = (EditorTextArea) getSelectedWindow().getComponent();
							tx.gotoLineMenu();
				    	}break;
				    }
					case KeyEvent.VK_O: {
		        		String file = Utils.tuneFileDialogResult(Utils.openFileDialog(false));
		        		if (file==null) return;
		                addWindow(new TextEditorWindow(this,file));break;
				    }
					case KeyEvent.VK_W: {closeSelectedWindow();break;}
					case KeyEvent.VK_TAB: {
		        		int loc = Windows.indexOf(SelectedWindow);
		        		setSelectedWindow(Windows.get(loc+1>Windows.size()-1?0:loc+1));
		        		break;
		        	}
					case KeyEvent.VK_P: {addWindow(new PreferencesWindow(this));break;}
					case KeyEvent.VK_T: {addWindow(new TextEditorWindow(this, ""));break;}
					default:
						break;
				}
			}
		}
		/* It's not perfect but it works... */
		if (ev instanceof MouseEvent) {
	    	Point p = getLocationRelativeTo();
	    	if (p.getX()<(isFolderOpen()?Folder.getWidth()+getBuffer():0)+MINIMUM_DISTANCE) {
	    		if (!FolderButton.isVisible()) {
	    			if (boolean1.get()) return;
	    			boolean1.set(true);boolean2.set(true);
		    		FolderButton.setVisible(true);
		        	FolderButton.setLocation((isFolderOpen()?Folder.getWidth()+getBuffer():0)-30,
		        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2);
		        	SwingUtils.animate(FolderButton, new Point(isFolderOpen()?Folder.getWidth()+getBuffer():0,
		        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2), frames, intreval,
		        			()->boolean1.set(false));
	    		}
	    	} else {
    			if (boolean1.get()) return;if (!boolean2.get()) return;
	        	boolean1.set(true);boolean2.set(false);
	    		FolderButton.setVisible(true);
	        	FolderButton.setLocation((isFolderOpen()?Folder.getWidth()+getBuffer():0),
	        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2);
	        	SwingUtils.animate(FolderButton, new Point((isFolderOpen()?Folder.getWidth()+getBuffer():0)-30,
	        			(contentpane.getHeight() - MenuBarSize() - TabSize())/2), frames, intreval,
	        			()->{boolean1.set(false);FolderButton.setVisible(false);});
	    	}
		}
	}
	@SuppressWarnings("deprecation")
	@Override public void reshape(int x, int y, int width, int height) {super.reshape(x, y, width, height);redraw();}
}