package NNU.Editor.FolderManager;

import static NNU.Editor.AssetManagement.StringTable.getString;
import static java.lang.System.out;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

import NNU.Editor.App;
import NNU.Editor.Utils.UserMessager;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Interfaces.Editor;
import NNU.Editor.Windows.Interfaces.Window;
public class FolderPopup extends JPopupMenu {
	
	private static final long serialVersionUID = 5845511693212284567L;
	FolderPanel fp;
	App app;
	
	public FolderPopup(FolderPanel fp, App app) {
		this.fp = fp;
		this.app = app;
	}
	
	public void show(Component invoker, int x, int y, int[] text, TreePath[] node) {
		/**
		 * Do not trust this variable please! it is most likely null!
		 */
		File f = text.length==1?new File(fp.nodeFromRow(text[0]).getUserObject().toString()):null;
		this.removeAll();
		
		JMenuItem DELETE = new JMenuItem(getString("folder.ctx.delete"));
		DELETE.addActionListener(e -> {
			try {
				int res = UserMessager.confirmTB("confirm.delete", "confirm.delete");
				if (res==UserMessager.YES_OPTION) {
					for (int p : text) {
						out.println("Deleting " + fp.nodeFromRow(p).getUserObject().toString());
						
						Utils.delete(Paths.get(fp.nodeFromRow(p).getUserObject().toString()));
					}
					fp.tree.setSelectionRow(-1);
				}
				fp.refresh(true);
			} catch (Exception e1) {e1.printStackTrace();}
        });
		
		JMenuItem RENAME = new JMenuItem(getString("folder.ctx.rename"));
		RENAME.addActionListener(e -> {
			String res = UserMessager.inputTB(f.getName(), "input.filename");
			if (res!=null) {
				
				out.println("Deleting " + f.toPath().toAbsolutePath());
				
				try {
					rename(f,res);
				} catch (Exception e1) {
					e1.printStackTrace();
					UserMessager.showErrorDialogTB("err.renamefile.title", "err.renamefile", e1.getMessage());
				}
			}
			fp.refresh(true);
        });
		
		JMenuItem REFRESH = new JMenuItem(getString("folder.ctx.refresh"));
		REFRESH.addActionListener(e -> fp.refresh(true));
		
		JMenuItem OPENINEXPLORER = new JMenuItem(getString("folder.ctx.openinexp"));
		OPENINEXPLORER.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(f.getParentFile().toURI());
			} catch (IOException e1) {e1.printStackTrace();}
		});
		
		if (text.length==1&&f.isDirectory()) {
			JMenuItem NewFile = new JMenuItem(getString("folder.ctx.newfile"));
			NewFile.addActionListener(e -> {
				try {
					String path = UserMessager.inputTB("", "input.newfile");
					if (path!=null) {
						out.println("Creating file " + f.getAbsolutePath() + File.separatorChar + path);
						File fi = new File(f.getAbsolutePath() + File.separatorChar + path);
						boolean res = fi.createNewFile();
						out.println(res);
						if (!res) {
							UserMessager.showErrorDialogTB("folder.err.foldercreate.title",
									f.exists() ? "err.fileexists" : "folder.err.foldercreate");
						}
						fp.tree.expandPath(node[0]);
					} else {
						out.println("Canceled File Creation");
					}
					fp.refresh(true);
				} catch (IOException e1) {e1.printStackTrace();}
	        });
			JMenuItem NewFolder = new JMenuItem(getString("folder.ctx.newfolder"));
			NewFolder.addActionListener(e -> {
				String path =UserMessager.inputTB("", "input.newfolder");
				if (path!=null) {
					out.println("Creating Folder " + f.getAbsolutePath() + File.separatorChar + path);
					File fi = new File(f.getAbsolutePath() + File.separatorChar + path);
					boolean res = fi.mkdirs();
					out.println(res);
					if (!res) {
						UserMessager.showErrorDialogTB("folder.err.foldercreate.title",
								fi.exists() ? "err.fileexists" :
									"folder.err.foldercreate");
					}
					fp.tree.expandPath(node[0]);
				} else {
					out.println("Canceled Folder Creation");
				}
				fp.refresh(true);
	        });
			add(NewFile);
			add(NewFolder);
			this.addSeparator();
		} else {
		}
		if(text.length==1) {
			add(RENAME);
			add(DELETE);
	
			this.addSeparator();
			add(REFRESH);
			this.addSeparator();
			add(OPENINEXPLORER);
			if (!f.isDirectory()) {
				JMenuItem OpenInEditor = new JMenuItem(getString("folder.ctx.openwitheditor"));
				OpenInEditor.addActionListener(e -> app.openEditor(f.getAbsolutePath()));
				add(OpenInEditor);
			}
		} else {
			add(DELETE);
			add(REFRESH);
		}
		super.show(invoker,x,y);
    }
	public boolean rename(File f, String newName) throws Exception {
		String name = f.getAbsolutePath();
		String newFile = f.getParent() + File.separatorChar + newName;
		boolean res = f.renameTo(new File(f.getParent() + File.separatorChar + newName));
		if (!res)
			throw new Exception("Unable to rename Object");
		else {
			for (Window i : app.Windows) {
				if (i.getComponent() instanceof Editor
						&&Objects.equals(((Editor)i.getComponent()).getFilePath(), name)) {
					((Editor)i.getComponent()).setFilePath(newFile);
					i.getTab().repaint();
				}
			}
		}
		return res;
	}
}
