package NNU.Editor;

import static java.lang.System.out;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import NNU.Editor.Menus.Components.FolderPopup;
import NNU.Editor.Menus.Components.IconButton;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Window;

public class FolderPanel extends JPanel {
	
	private static final long serialVersionUID = -936840551668141819L;
	private String FolderPath = "\000";
	public static final Map<String, Icon> icons = new HashMap<String,Icon>();
	public static final String FOLDER = "folder";
	public final JScrollPane FilesPanel;
	public final App app;
	public final JTree tree;
	public static final int IMAGESIZE = 45;
	public static final int PADDING = 3;
	public static final String[] IconsNames = new String[] {
			 "ui/refresh", "ui/newfolder", "ui/folder", "ui/close", "ui/newfile", "ui/file","ui/shell"
			
			, "cpp", "javascript", "java", "html", "css", "c",
	};
	public DefaultMutableTreeNode selectedNode() {
		return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	}
	
	public void setFolderPath(String path) {
		
		FolderPath = Utils.tuneFileDialogResult(path)==null ? "\000" : path;
		refresh();
	}

	public String getFolderPath() {
		return FolderPath;
	}
	
	@Override public int getWidth() {
		return 300;
	}
	
	public int MenuBarSize() {
		return 30;
	}
	
	static {
		icons.put("file",Utils.ResizeIcon(UIManager.getIcon("FileView.fileIcon"),IMAGESIZE,IMAGESIZE));
		for (String i : IconsNames) {
			String[] p = i.split("/");
			String name = p[p.length-1];
			icons.put(name, Utils.ResizeIcon(Utils.ReadImageIcon(i + ".png"),IMAGESIZE,IMAGESIZE));
		}
		icons.put("bat",icons.get("shell"));
		icons.put("unix",icons.get("shell"));
	}

	public FolderPanel(App app) {
		//initIcons();

		
		/*Path dir = new File(FolderPath).toPath();
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
		    WatchKey key = dir.register(watcher,
		    		StandardWatchEventKinds.ENTRY_CREATE,
		    		StandardWatchEventKinds.ENTRY_DELETE,
		    		StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException x) {
		    System.err.println(x);
		}*/
		
        this.app = app;
		//setBounds(0,MenuBarSize()+TabSize(),300,getHeight()-MenuBarSize()-TabSize());
		this.setDoubleBuffered(true);
		
		tree = new JTree() {
			
			private static final long serialVersionUID = -1909873281110302190L;
			int RoundedBox = 20;
			@Override public void paint(Graphics gra) {
	    		Graphics2D g = (Graphics2D) gra;
	    		g.setColor(getBackground());
	    		g.fillRect(0, 0, getWidth(), getHeight());
				if (tree.getModel()==null) {

		    		g.setFont(app.getTipFont());
		    		g.setColor(App.MenuFG);
		    		
		    		g.setRenderingHint(
		    		        RenderingHints.KEY_TEXT_ANTIALIASING,
		    		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		    		g.setRenderingHint(
		    		        RenderingHints.KEY_ANTIALIASING,
		    		        RenderingHints.VALUE_ANTIALIAS_ON);

		    		String str = "Click here to";
		    		int strwidth = g.getFontMetrics().stringWidth(str);
		    		int x = getWidth() / 2 - (strwidth / 2);
		    		
		    		int y = ((getHeight() - g.getFontMetrics().getHeight()) / 2)
		    				- g.getFontMetrics().getAscent()/4;
		    		g.drawString(str, x, y);

		    		String str2 = "open a folder";
		    		int strwidth2 = g.getFontMetrics().stringWidth(str2);
		    		int x2 = getWidth() / 2 - (strwidth2 / 2);
		    		
		    		int y2 = ((getHeight() - g.getFontMetrics().getHeight()) / 2)
		    				+ g.getFontMetrics().getAscent() + g.getFontMetrics().getAscent()/8;
		    		g.drawString(str2, x2, y2);
		    		
		    		g.setStroke(new BasicStroke(2));
		    		g.drawRoundRect(Math.min(x, x2), y-g.getFontMetrics().getAscent(),
		    				Math.max(strwidth2, strwidth)
		    				, 100,
		    				RoundedBox, RoundedBox);
		    		return;
				}
				super.paint(gra);
			}
		};
		tree.addKeyListener(new KeyAdapter( ) {
			@Override public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_TAB) {
					tree.setSelectionPath(new TreePath(tree.getModel().getRoot()));
				}
				if (selectedNode()==null) return;
				String userobj = selectedNode().getUserObject().toString();
				if (e.getKeyCode()==KeyEvent.VK_ENTER||e.getKeyCode()==KeyEvent.VK_SPACE) {
					if (!new File(userobj).isDirectory()) {
						app.openFile(userobj);
						return;
					}
					if (!tree.isExpanded(tree.getSelectionPath()))
						tree.expandPath(tree.getSelectionPath());
					else
						tree.collapsePath(tree.getSelectionPath());
				}
				File f = new File(userobj);
				switch (e.getKeyCode()) {
					case KeyEvent.VK_DELETE:
						int res = JOptionPane.showConfirmDialog
								(app.getRootPane(), "Are you sure you want to delete?",
										"Confirm Deletion",JOptionPane.YES_NO_OPTION);
						if (res==JOptionPane.YES_OPTION) {
							
							out.println("Deleting " + f.toPath().toAbsolutePath());
							
							try {
								Utils.delete(f.toPath());
							} catch (IOException e1) {e1.printStackTrace();}
						}
						refresh();
						break;
					case KeyEvent.VK_F2:
						String val = JOptionPane.showInputDialog 
								(app.getRootPane(), "What would like to name the file?", f.getName());
						if (val!=null) { 
							out.println("Deleting " + f.toPath().toAbsolutePath());
							try {
								rename(f,val);
							} catch (Exception e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, 
										"I was unable to rename the selected file or folder",
										"Unable to rename Object", JOptionPane.ERROR_MESSAGE);
							}
						}
						refresh();
						break;
					default: break;
				}
			}
		});
		tree.registerKeyboardAction(e -> newFile()
    	,KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK),
    	JComponent.WHEN_FOCUSED);
        tree.setDragEnabled(true);
        tree.setTransferHandler(new TransferHandler() {
			private static final long serialVersionUID = 7811105014278307258L;
        	/* I give up :( */
        });
		/* Update: I came crawling back and fixed the issue in less than 30 minutes */
        tree.setTransferHandler(new TreeTransferHandler()); 
        tree.setDropMode(DropMode.ON_OR_INSERT);
        
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		//tree.setPaintSelection(true);
		tree.putClientProperty("Tree.collapsedIcon", new IconUIResource(new NodeIcon('+')));
		
		tree.setBounds(30, 0, 646, 723);
		tree.setFont(new Font("Tahoma", Font.BOLD, 20));
		tree.setRootVisible(true);
		tree.setCellRenderer(new Renderer());
		
		tree.addMouseMotionListener(new MouseMotionAdapter() {
		    @Override
		    public void mouseMoved(MouseEvent e) {
		        int x = (int) e.getPoint().getX();
		        int y = (int) e.getPoint().getY();
		        TreePath path = tree.getPathForLocation(x, y);
		        if (path == null) {
		            tree.setCursor(Cursor.getDefaultCursor());
		        } else {
		            tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		        }
		    }
		});
		
		FolderPopup jp = new FolderPopup(this,app);
		
		tree.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("\000".equals(getFolderPath())) {
					setFolderPath(Utils.openFolderDialog());
					refresh();
				}
			    if (SwingUtilities.isRightMouseButton(e)) {
			        int row = tree.getClosestRowForLocation(e.getX(), e.getY());
			        TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
			        tree.setSelectionRow(row);
				    if(row==-1) //When user clicks on the "empty surface"
				    	tree.setSelectionRow(0);
			        jp.show(e.getComponent(), e.getX(), e.getY(),
			        		selectedNode().getUserObject().toString(), tp);
			    }
			}
			@Override public void mousePressed(MouseEvent e) {
			    int row=tree.getRowForLocation(e.getX(),e.getY());
			    if(row==-1) //When user clicks on the "empty surface"
			        tree.clearSelection();
				if (selectedNode()==null) return;
				if(e.getClickCount() == 2) {
					File f = new File(selectedNode().getUserObject().toString());
	                if (!f.isDirectory()) {
	            		/* Get Text Editor */
		    	        app.openFile(f.getAbsolutePath());
	                }
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("\000".equals(getFolderPath()))
					setFolderPath(Utils.openFolderDialog());
			}
		});
		
		//tree.
		FilesPanel = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		FilesPanel.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("\000".equals(getFolderPath()))
					setFolderPath(Utils.openFolderDialog());
			}
		});
		
		FilesPanel.setBounds(0, 56, 646, 723);
		FilesPanel.setDoubleBuffered(true);
		FilesPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		//FilesPanel.setLayout(null);
		setLayout(null);
		
		setBackground(App.MenuBG.darker().darker());
		FilesPanel.setBackground(getBackground());
		tree.setBackground(getBackground());
		
		add(FilesPanel);
		JButton FileCreate = new IconButton(icons.get("newfile"));
		FileCreate.setBounds(0,0,30,30);
		FileCreate.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				newFile();
				refresh();
			}
		});
		add(FileCreate);

		JButton Refresh = new IconButton(icons.get("refresh"));
		Refresh.setBounds(getWidth()-90,0,30,30);
		Refresh.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				refresh();
			}
		});
		add(Refresh);
		
		JButton FolderCreate = new IconButton(icons.get("newfolder"));
		FolderCreate.setBounds(30,0,30,30);
		FolderCreate.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if ("\000".equals(getFolderPath())) {
					JOptionPane.showMessageDialog(app.getRootPane(),
							"You need to selecte a folder first",
							"Error while creating a new folder",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String path =
						JOptionPane.showInputDialog(app.getRootPane(), "Enter name of new folder:");
				if (path!=null) {
					out.println("Creating folder " + getFolderPath() + File.separatorChar + path);
					File f = new File(getFolderPath() + File.separatorChar + path);
					boolean res = f.mkdirs();
					out.println(res);
					if (!res) {
						JOptionPane.showMessageDialog(app.getRootPane(),
							f.exists() ? "A file or folder with the same name already exists" :
								"Unable to create folder", "Error while creating folder",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					out.println("Canceled File Creation");
				}
				refresh();
			}
		});
		add(FolderCreate);
		JButton CloseFolder = new IconButton(icons.get("close"));
		CloseFolder.setBounds(getWidth()-30,0,30,30);
		CloseFolder.setToolTipText("Close Current Folder");
		CloseFolder.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				setFolderPath(null);
				refresh();
			}
		});
		add(CloseFolder);
		JButton OpenFolder = new IconButton(icons.get(FOLDER));
		OpenFolder.setBounds(getWidth()-60,0,30,30);
		OpenFolder.setToolTipText("Open Folder");
		OpenFolder.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				String temp = Utils.openFolderDialog();

				setFolderPath(Utils.tuneFileDialogResult(temp)==null ? getFolderPath() : temp);
				refresh();
			}
		});
		add(OpenFolder);
		this.setOpaque(false);
	}
	public boolean rename(File f, String newName) throws Exception {
		String name = f.getAbsolutePath();
		String newFile = f.getParent() + File.separatorChar + newName;
		boolean res = f.renameTo(new File(f.getParent() + File.separatorChar + newName));
		if (!res)
			throw new Exception("Unable to rename Object");
		else {
			for (Window i : app.Windows) {
				if (i.getComponent() instanceof SyntaxTextArea
						&&Objects.equals(((SyntaxTextArea)i.getComponent()).getFilePath(), name)) {
					((SyntaxTextArea)i.getComponent()).setFilePath(newFile);
				}
			}
		}
		return res;
	}
	
	protected void newFile() {
		try {
			if ("\000".equals(getFolderPath())) {
				JOptionPane.showMessageDialog(app.getRootPane(),
						"You need to selecte a folder first",
						"Error while creating file",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String path =
					JOptionPane.showInputDialog(app.getRootPane(), "Enter name of new File:");
			if (path!=null) {
				out.println("Creating file " + getFolderPath() + File.separatorChar + path);
				File f = new File(getFolderPath() + File.separatorChar + path);
				boolean res = f.createNewFile();
				out.println(res);
				if (!res) {
					JOptionPane.showMessageDialog(app.getRootPane(),
						f.exists() ? "A file or folder with the same name already exists" :
							"Unable to create file", "Error while creating file",
							JOptionPane.ERROR_MESSAGE);
				} else {
					app.openFile(getFolderPath() + File.separatorChar + path);
				}
			} else {
				out.println("Canceled File Creation");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void refresh() {
		if ("\000".equals(getFolderPath())) {
			tree.setModel(null);
			return;
		}
        String state = getExpansionState();
		tree.setModel(new DefaultTreeModel(readFileList(getFolderPath())));
		setExpansionState(state);
	}
	
	public static DefaultMutableTreeNode readFileList(String FPath) {
		if (FPath==null||"\000".equals(FPath)) return null;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(FPath);
		
		File folder = new File(FPath);
		
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				DefaultMutableTreeNode fi = readFileList(f.getAbsolutePath());
				root.add(fi);
			}
		}
		for (File f : folder.listFiles()) {
			if (!f.isDirectory()) {
				DefaultMutableTreeNode fi = new DefaultMutableTreeNode(f.getAbsolutePath());
				root.add(fi);
				fi.setAllowsChildren(false);
			}
		}
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
	        if(s.contains(tp.toString() )){
	            tree.expandRow(i);
	        }   
	    }
	}
	
	public static Icon getIcon(File f) {
		if (f.isDirectory())
			return icons.get(FOLDER);
        String[] arr = f.getAbsolutePath().split("[.]");
        String ext = arr[arr.length-1];
		Icon ic;
		if ((ic = icons.get(App.fileext.get(ext)))==null) {
			return icons.get("file");
		}
		return ic;
	}
    
    @Override public void paintComponents(Graphics g) {
    	g.setColor(getBackground());
    	g.fillRect(0, 500, getWidth(), getHeight());
    	//paintComponents(g);
    }

	public static class Renderer extends DefaultTreeCellRenderer {
		
		private static final long serialVersionUID = -467043583904458260L;
		
		//private static final String SPAN_FORMAT = "<span style='color:%s;'>%s</span>";
	    Border border = BorderFactory.createLineBorder(Color.black);
	
		public Renderer() {}
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
				boolean expanded, boolean leaf, int row, boolean hasFocus) {
		    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		    //this.setBorder(border);
		    this.setBorder(new Utils.CustomBorder(PADDING, PADDING, PADDING, PADDING));
		    String file = value.toString();
	        this.setText(Utils.getFileName(file));
	        this.setBackgroundSelectionColor(new Color(50,50,50));//(30,95,190).darker().darker()
	        this.setForeground(new Color(164,164,164));
	        
	        if (!Utils.getFileName(file).startsWith("."))
	        	this.setIcon(FolderPanel.getIcon(new File(file)));
	        else {
	        	this.setIcon(Utils.getTransparency(0.5f,FolderPanel.getIcon(new File(file))));
	        	this.setForeground(getForeground().darker().darker().darker());
		        this.setBackgroundSelectionColor(getBackgroundSelectionColor().darker().darker());
	        	
	        }
	        //this.setUI(new FLLABELUI(true));
	        
	        return this;
		    
		}
	}

	public boolean contains(String file, boolean root) {
		if (!root) return false;
		/*if (tree.getModel()==null) return false;
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tree.getModel().getRoot();
	    File[] fList = new File(dmtn.toString()).listFiles();
		for (File tr : fList) {
			if (file.equals(tr.getName()))
				return true;
		}*/
		return new File(getFolderPath() + File.separatorChar + file).exists();
	}
}

class TreeTransferHandler extends TransferHandler {
	private static final long serialVersionUID = -1572114826273900103L;
	DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[2];
    DefaultMutableTreeNode[] nodesToRemove;

    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                              ";class=\"" +
                javax.swing.tree.DefaultMutableTreeNode[].class.getName() +
                              "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
            //flavors[1] = new DataFlavor("application/x-java-file-list;representationclass=java.util.List");
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }
        /*System.out.println();
        System.out.println(Arrays.toString(support.getDataFlavors()));
        System.out.println((support.getDataFlavors())[0]);
        System.out.println(flavors[1]);
        System.out.println(nodesFlavor);
        try {
			System.out.println(support.isDataFlavorSupported(new DataFlavor("application/x-java-file-list;representationclass=java.io.InputStream")));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        support.setShowDropLocation(true);
        if(!support.isDataFlavorSupported(nodesFlavor)&&!support.isDataFlavorSupported(flavors[1])) {
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for(int i = 0; i < selRows.length; i++) {
            if(selRows[i] == dropRow) {
                return false;
            }
        }
        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        int action = support.getDropAction();
        if(action == MOVE)
        	return haveCompleteNode(tree);
        
        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode target = (DefaultMutableTreeNode)dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode)path.getLastPathComponent();
        return !(firstNode.getChildCount() > 0 && target.getLevel() < firstNode.getLevel());
    }

    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first = (DefaultMutableTreeNode)path.getLastPathComponent();
        int childCount = first.getChildCount();
        // first has children and no children are selected.
        if(childCount > 0 && selRows.length == 1)
            return false;
        // first may have children.
        for(int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            DefaultMutableTreeNode next = (DefaultMutableTreeNode)path.getLastPathComponent();
            if(first.isNodeChild(next) && (childCount > selRows.length-1)) {
            	// Not all children of first are selected.
            	return false;
            }
        }
        return true;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();
        if(paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<DefaultMutableTreeNode> copies =
                new ArrayList<DefaultMutableTreeNode>();
            List<DefaultMutableTreeNode> toRemove =
                new ArrayList<DefaultMutableTreeNode>();
            DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            for(int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next =
                    (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if(next.getLevel() < node.getLevel()) {
                    break;
                } else if(next.getLevel() > node.getLevel()) {  // child node
                    copy.add(copy(next));
                    // node already contains child
                } else {                                        // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            DefaultMutableTreeNode[] nodes =
                copies.toArray(new DefaultMutableTreeNode[copies.size()]);
            nodesToRemove =
                toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    /** Defensive copy used in createTransferable. */
    private DefaultMutableTreeNode copy(TreeNode node) {
        return new DefaultMutableTreeNode(node);
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if((action & MOVE) == MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Remove nodes saved in nodesToRemove in createTransferable.
            for(int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if(!canImport(support)) {
            return false;
        }
        // Extract transfer data.
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        // Get drop location info.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent =
            (DefaultMutableTreeNode)dest.getLastPathComponent();
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure for drop mode.
        int index = childIndex;    // DropMode.INSERT
        if(childIndex == -1) {     // DropMode.ON
            index = parent.getChildCount();
        }
        if (!new File(parent.toString()).isDirectory()) return false;
        // Add data to model.
        for(int i = 0; i < nodes.length; i++) {
        	
        	File f = new File(nodes[i].toString());
        	if (!f.renameTo(new File(parent.toString() + 
        			File.separatorChar + Utils.getFileName(nodes[i].toString())))) {
        		JOptionPane.showMessageDialog(null, "Unable to move file", "Error while moving file(s)",
        				JOptionPane.ERROR_MESSAGE);
        		return false;
        	}
        }
        for(int i = 0; i < nodes.length; i++) {
        	model.insertNodeInto(nodes[i], parent, index++);
        }
        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {
        DefaultMutableTreeNode[] nodes;

        public NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
         }

        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}