package io.github.ngspace.nnuedit.folder_management;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;
import static java.lang.System.out;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.asset_manager.AssetManager;
import io.github.ngspace.nnuedit.menu.components.IconButton;
import io.github.ngspace.nnuedit.utils.UserMessager;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.window.abstractions.Window;

public class FolderPanel extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = -936840551668141819L;
	private String FolderPath = "";
	public final JScrollPane FilesPanel;
	public final App app;
	public final JTree tree;
	public static final int ROW_HEIGHT = 25;
	public static final int PADDING = 3;
	public static final int DRAGBAR_WIDTH = 10;
	JButton CloseFolder;
	JButton OpenFolder;
	JButton Refresh;
	public DefaultMutableTreeNode nodeFromRow(int row) {
		return (DefaultMutableTreeNode) tree.getPathForRow(row).getLastPathComponent();
	}
	
	public void setFolderPath(String path) {
		FolderPath = Utils.tuneFileDialogResult(path)==null ? "" : path;
		try {
			if (Main.settings.getBoolean("folder.openlast")) Main.settings.set("folder.last", path);
			else /* Same */ Main.settings.set("folder.last", "I want to be a Real Folder!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		refresh(true);
	}

	public String getFolderPath() {return FolderPath;}
	public int MenuBarSize() {return 25 * app.getScale();}
	@Override public int getWidth() {
		return Math.min(Math.max(200,Main.settings.getInt("folder.panelwidth")),app.getWidth()/2);
	}

	public FolderPanel(App app) {
        this.app = app;
        
		this.setDoubleBuffered(true);
		
		tree = new FolderTree();
		tree.setFont(app.getTipFont());
		tree.addKeyListener(this);
		tree.registerKeyboardAction(e->newFile(),KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_FOCUSED);
        tree.setDragEnabled(true);
        
        tree.setTransferHandler(new FolderTransferHandler()); 
        tree.setDropMode(DropMode.ON_OR_INSERT);
        
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
		tree.setBounds(30, 0, 646, 723);
		tree.setFont(new Font("Tahoma", Font.BOLD, 20));
		tree.setRootVisible(true);
		tree.setCellRenderer(new FolderTreeRenderer(this));
		
		tree.addMouseMotionListener(new MouseMotionAdapter() {
		    @Override
		    public void mouseMoved(MouseEvent e) {
		        TreePath path = tree.getPathForLocation((int) e.getPoint().getX(), (int) e.getPoint().getY());
		        tree.setCursor(path==null?Cursor.getDefaultCursor():Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    }
		});
		
		FolderPopup jp = new FolderPopup(this,app);
		
		tree.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("".equals(getFolderPath())) {
					setFolderPath(Utils.openFolderDialog());
					refresh(false);
					return;
				}
			    if (SwingUtilities.isRightMouseButton(e)) {
			        int row = tree.getClosestRowForLocation(e.getX(), e.getY());
			        TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
			        tree.addSelectionPath(tp);
			        tree.addSelectionRow(row);
				    if(row==-1) tree.setSelectionRow(0);
				    if (tree.getSelectionCount()>0)
			        jp.show(e.getComponent(), e.getX(), e.getY(), tree.getSelectionRows(), tree.getSelectionPaths());
			    }
			}
			@Override public void mousePressed(MouseEvent e) {
			    int row=tree.getRowForLocation(e.getX(),e.getY());
			    if(row==-1) tree.clearSelection();
			    if (tree.getSelectionCount()<0) return;
				if(e.getClickCount() == 2&&tree.getSelectionCount()>0) {
					File f = new File(nodeFromRow(tree.getSelectionRows()[0]).getUserObject().toString());
	                if (!f.isDirectory())
		    	        app.openFile(f.getAbsolutePath());
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("".equals(getFolderPath())) setFolderPath(Utils.openFolderDialog());
			}
		});
		
		FilesPanel = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		FilesPanel.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("".equals(getFolderPath())) setFolderPath(Utils.openFolderDialog());
			}
		});
		
		FilesPanel.setBounds(0, 56, 646, 723);
		FilesPanel.setDoubleBuffered(true);
		FilesPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(null);
		
		setBackground(App.MenuBG.darker());
		FilesPanel.setBackground(getBackground());
		tree.setBackground(getBackground());
		tree.setRowHeight(ROW_HEIGHT);
		//tree.setBorder(new Utils.TopCustomBorder(0, 0, 0, 0,new Color(78, 78, 78, 150)));
		Color linecolor = new Color(78, 78, 78, 150);
		
		tree.setBorder(new EmptyBorder(0, 0, 0, 0) {
			private static final long serialVersionUID = -2491978365735980533L;

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				((Graphics2D)g).setStroke(new BasicStroke(10,1,1));
				g.setColor(linecolor);
				g.drawLine(0, 0, width, 0);
			}
		});

		initDrag(); //SLAYYYYYYYYYYYYYYYYYYYYYYY
		initButtons();
		add(FilesPanel);
	}
	public void initButtons() {
		JButton FileCreate = new IconButton(AssetManager.getIcon("newfile"));
		FileCreate.setToolTipText(get("folder.bar.newfile"));
		FileCreate.setBounds(0,0,MenuBarSize(),MenuBarSize());
		FileCreate.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				newFile();
				refresh(true);
			}
		});
		add(FileCreate);
		
		JButton FolderCreate = new IconButton(AssetManager.getIcon("newfolder"));
		FolderCreate.setToolTipText(get("folder.bar.newfolder"));
		FolderCreate.setBounds(MenuBarSize(),0,MenuBarSize(),MenuBarSize());
		FolderCreate.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("".equals(getFolderPath())) {
					UserMessager.showErrorDialogTB
						("folder.err.foldercreate.title", "folder.err.foldernotselected");
					return;
				}
				Object path = UserMessager.inputTB("", "input.newfolder");
				if (path!=null) {
					out.println("Creating folder " + getFolderPath() + File.separatorChar + path);
					File f = new File(getFolderPath() + File.separatorChar + path);
					boolean res = f.mkdirs();
					out.println(res);
					if (!res)
						UserMessager.showErrorDialogTB("folder.err.foldercreate.title",
							f.exists() ? "err.fileexists" : "folder.err.foldercreate");
				} else
					out.println("Canceled File Creation");
				refresh(true);
			}
		});
		add(FolderCreate);

		Refresh = new IconButton(AssetManager.getIcon("refresh"));
		Refresh.setToolTipText(get("folder.bar.refreshfolder"));
		add(Refresh);
		
		OpenFolder = new IconButton(AssetManager.getIcon("folder"));
		OpenFolder.setToolTipText(get("folder.bar.openfolder"));
		add(OpenFolder);
		
		CloseFolder = new XButton();
		CloseFolder.setToolTipText(get("folder.bar.closefolder"));
		add(CloseFolder);
		this.setOpaque(true);

		Refresh.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				refresh(true);
			}
		});
		CloseFolder.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				setFolderPath(null);
				refresh(true);
			}
		});
		OpenFolder.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				String temp = Utils.openFolderDialog();

				setFolderPath(Utils.tuneFileDialogResult(temp)==null ? getFolderPath() : temp);
				refresh(true);
			}
		});
	}
	
	@Override public void paint(Graphics g) {
		super.paint(g);
		if (DragBar.isVisible()) {
			Icon ic = DragBar.getIcon();
			for (int i = 0;i<getHeight();i+=ic.getIconHeight()-1)
				ic.paintIcon(DragBar, g, DragBar.getX(), i);
		}
	}

	JButton DragBar;
	int i = 0;
	boolean b = false;
	private void initDrag() {
		
		DragBar = new JButton();
		DragBar.setIcon(Utils.ReadImageIcon("ui/Bar.png"));
		DragBar.setBounds(Main.settings.getInt("folder.panelwidth")-10,0,10,10000);
		DragBar.setBorderPainted(false); 
		DragBar.setBackground(Color.black);
		DragBar.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		DragBar.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				i = getWidth();
				DragBar.setVisible(false);
				repaint();
				b = true;
			}
		});
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener() {
		    public void eventDispatched(AWTEvent ev) {
		    	MouseEvent e = (MouseEvent) ev;
		    	if (e.getID()==MouseEvent.MOUSE_DRAGGED&&b) {
					Main.settings.set("folder.panelwidth", Math.min(Math.max(200,e.getX() + i),app.getWidth()/2));
					DragBar.setVisible(false);
					app.redraw();
					repaint();
		    	}
		    	if (e.getID()==MouseEvent.MOUSE_RELEASED) {
		    		if (b) {
						DragBar.setLocation(getWidth()-DRAGBAR_WIDTH,0);
						DragBar.setVisible(true);
		    		}
		    		b = false;
		    	}
		    }
		}, eventMask);
		add(DragBar);
		repaint();
	}

	public boolean rename(File f, String newName) throws Exception {
		String name = f.getAbsolutePath();
		String newFile = f.getParent() + File.separatorChar + newName;
		boolean res = f.renameTo(new File(f.getParent() + File.separatorChar + newName));
		if (!res) throw new Exception("Unable to rename Object");
		else
			for (Window i : app.Windows)
				if (i.isEditor()&&name.equals(i.getEditor().getFilePath()))
					i.getEditor().setFilePath(newFile);
		return res;
	}
	
	protected void newFile() {
		try {
			if ("".equals(getFolderPath())) {
				UserMessager.showErrorDialogTB("folder.err.filecreate.title", "folder.err.foldernotselected");
				return;
			}
			Object path = UserMessager.inputTB("", "input.newfile");
			if (path!=null) {
				out.println("Creating file " + getFolderPath() + File.separatorChar + path);
				File f = new File(getFolderPath() + File.separatorChar + path);
				boolean res = f.createNewFile();
				if (!res)
					UserMessager.showErrorDialogTB("folder.err.filecreate.title",
							f.exists()?"err.fileexists":"folder.err.filecreate");
				else
					app.openFile(getFolderPath() + File.separatorChar + path);
			} else {
				out.println("Canceled File Creation");
			}
		} catch (IOException e) {
			UserMessager.showErrorDialogTB("folder.err.filecreate.title", "folder.err.filecreate");
			e.printStackTrace();
		}
	}

	public void refresh(boolean t) {
		Refresh.setBounds(getWidth()-MenuBarSize()*3 - DRAGBAR_WIDTH,0,MenuBarSize(),MenuBarSize());
		OpenFolder.setBounds(getWidth()-MenuBarSize()*2 - DRAGBAR_WIDTH,0,MenuBarSize(),MenuBarSize());
		CloseFolder.setBounds(getWidth()-MenuBarSize() - DRAGBAR_WIDTH,0,MenuBarSize(),MenuBarSize());
		
		if (t) AssetManager.clearImageCache();
		
		if ("".equals(getFolderPath())) {tree.setModel(null);return;}

		int[] selection = tree.getSelectionRows();
        String state = getExpansionState();
        TreeNode node = readFileList(getFolderPath());
		tree.setModel(new DefaultTreeModel(node));
		setExpansionState(state);
		tree.addSelectionRows(selection);
	}
	
	public static DefaultMutableTreeNode readFileList(String FPath) {
		if (FPath==null||"".equals(FPath)) return null;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(FPath);
		
		File folder = new File(FPath);
		ArrayList<MutableTreeNode> files = new ArrayList<MutableTreeNode>();
		
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				DefaultMutableTreeNode fi = readFileList(f.getAbsolutePath());
				root.add(fi);
			} else {
				DefaultMutableTreeNode fi = new DefaultMutableTreeNode(f.getAbsolutePath());
				fi.setAllowsChildren(false);
				files.add(fi);
			}
		}
		for (MutableTreeNode tn : files)
			root.add(tn);
		return root;
	}
	public StringBuilder sb;
	
	public String getExpansionState() {
	    sb = new StringBuilder();
	    for(int i =0 ; i < tree.getRowCount(); i++){
	        TreePath tp = tree.getPathForRow(i);
	        if(tree.isExpanded(i)){
	            sb.append(tp.toString());
	            sb.append(",");
	        }
	    }
	    return sb.toString();
	}
	public void setExpansionState(String s){
	    for(int i = 0 ; i<tree.getRowCount(); i++){
	        TreePath tp = tree.getPathForRow(i);
	        if(s.contains(tp.toString()))
	            tree.expandRow(i);
	    }
	}
	public boolean contains(String file, boolean root) {
		if (!root) for (File tr : new File(getFolderPath()).listFiles()) if (file.equals(tr.getName())) return true;
		return new File(getFolderPath() + File.separatorChar + file).exists();
	}
	@Override public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_TAB) tree.setSelectionPath(new TreePath(tree.getModel().getRoot()));
		if (tree.getSelectionCount()<1) return;
		String firstselrowval = nodeFromRow(tree.getSelectionRows()[0]).getUserObject().toString();
		if (e.getKeyCode()==KeyEvent.VK_ENTER||e.getKeyCode()==KeyEvent.VK_SPACE) {
			if (!new File(firstselrowval).isDirectory()) {
				app.openFile(firstselrowval);
				return;
			}
			if (!tree.isExpanded(tree.getSelectionPath())) tree.expandPath(tree.getSelectionPath());
			else tree.collapsePath(tree.getSelectionPath());
		}
		File f = new File(firstselrowval);
		switch (e.getKeyCode()) {
			case KeyEvent.VK_DELETE:
				int res = UserMessager.confirmTB("confirm.delete", "confirm.delete");
				if (res==UserMessager.YES_OPTION) {
					try {
						for (int p : tree.getSelectionRows()) {
							out.println("Deleting " + nodeFromRow(p).getUserObject().toString());
							
							Utils.delete(Paths.get(nodeFromRow(p).getUserObject().toString()));
						}
						tree.setSelectionRow(-1);
					} catch (IOException e1) {e1.printStackTrace();}
				}
				break;
			case KeyEvent.VK_F2:
				if (tree.getSelectionCount()>1) break;
				Object val = UserMessager.inputTB(f.getName(), "input.newfile");
				if (val!=null) { 
					out.println("Deleting " + f.toPath().toAbsolutePath());
					try {
						rename(f,val.toString());
					} catch (Exception e1) {
						e1.printStackTrace();
						UserMessager.showErrorDialogTB
							("err.renamefile.title", "err.renamefile",e1.getLocalizedMessage());
					}
				}
				break;
			default: break;
		}
		refresh(false);
	}
	@Override public void keyTyped(KeyEvent e) {}
	@Override public void keyPressed(KeyEvent e) {}
}