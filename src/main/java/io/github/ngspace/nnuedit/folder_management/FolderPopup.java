package io.github.ngspace.nnuedit.folder_management;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;
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

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.utils.UserMessager;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.window.abstractions.Editor;
import io.github.ngspace.nnuedit.window.abstractions.Window;
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
		
		JMenuItem DELETE = new JMenuItem(get("folder.ctx.delete"));
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
		
		JMenuItem RENAME = new JMenuItem(get("folder.ctx.rename"));
		RENAME.addActionListener(e -> {
			Object res = UserMessager.inputTB(f.getName(), "input.newfile");
			if (res!=null) {
				
				out.println("Deleting " + f.toPath().toAbsolutePath());
				
				try {
					rename(f,res.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
					UserMessager.showErrorDialogTB("err.renamefile.title", "err.renamefile", e1.getMessage());
				}
			}
			fp.refresh(true);
        });
		
		JMenuItem REFRESH = new JMenuItem(get("folder.ctx.refresh"));
		REFRESH.addActionListener(e -> fp.refresh(true));
		
		JMenuItem OPENINEXPLORER = new JMenuItem(get("folder.ctx.openinexp"));
		OPENINEXPLORER.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(f.getParentFile().toURI());
			} catch (IOException e1) {e1.printStackTrace();}
		});
		
		if (text.length==1&&f.isDirectory()) {
			JMenuItem NewFile = new JMenuItem(get("folder.ctx.newfile"));
			NewFile.addActionListener(e -> {
				try {
					Object path = UserMessager.inputTB("", "input.newfile");
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
			JMenuItem NewFolder = new JMenuItem(get("folder.ctx.newfolder"));
			NewFolder.addActionListener(e -> {
				Object path =UserMessager.inputTB("", "input.newfolder");
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
		}
		if(text.length==1) {
			add(RENAME);
			add(DELETE);
	
			this.addSeparator();
			add(REFRESH);
			this.addSeparator();
			add(OPENINEXPLORER);
			if (!f.isDirectory()) {
				JMenuItem OpenInEditor = new JMenuItem(get("folder.ctx.openwitheditor"));
				OpenInEditor.addActionListener(e -> app.openDefaultTextEditor(f));
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
