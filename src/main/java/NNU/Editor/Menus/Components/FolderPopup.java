package NNU.Editor.Menus.Components;

import static java.lang.System.out;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

import NNU.Editor.App;
import NNU.Editor.FolderPanel;
import NNU.Editor.SyntaxTextArea;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Window;

public class FolderPopup extends JPopupMenu {
	
	private static final long serialVersionUID = 5845511693212284567L;
	FolderPanel fp;
	App app;
	
	public FolderPopup(FolderPanel fp, App app) {
		this.fp = fp;
		this.app = app;
	}
	
	public void show(Component invoker, int x, int y, String text, TreePath node) {
		
		File f = new File(text);
		this.removeAll();
		
		JMenuItem DELETE = new JMenuItem("Delete");
		DELETE.addActionListener(e -> {
			try {
				int res = JOptionPane.showConfirmDialog
						(app.getRootPane(), "Are you sure you want to delete?",
								"Confirm Deletion",JOptionPane.YES_NO_OPTION);
				if (res==JOptionPane.YES_OPTION) {
					
					out.println("Deleting " + f.toPath().toAbsolutePath());
					
					Utils.delete(f.toPath());
				}
				fp.refresh();
			} catch (IOException e1) {e1.printStackTrace();}
        });
		
		JMenuItem RENAME = new JMenuItem("Rename");
		RENAME.addActionListener(e -> {
			String res = JOptionPane.showInputDialog
					(app.getRootPane(), "What would like to name the file?", f.getName());
			if (res!=null) {
				
				out.println("Deleting " + f.toPath().toAbsolutePath());
				
				try {
					rename(f,res);
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, 
							"I was unable to rename the selected file or folder",
							"Unable to rename Object",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			fp.refresh();
        });
		
		JMenuItem REFRESH = new JMenuItem("Refresh");
		REFRESH.addActionListener(e -> fp.refresh());
		
		JMenuItem OPENINEXPLORER = new JMenuItem("Open in explorer");
		OPENINEXPLORER.addActionListener(e -> {
			try {
				Desktop.getDesktop().open(f.getParentFile());
			} catch (IOException e1) {e1.printStackTrace();}
		});
		
		if (f.isDirectory()) {
			JMenuItem NewFile = new JMenuItem("New File");
			NewFile.addActionListener(e -> {
				try {
					String path =
							JOptionPane.showInputDialog(app.getRootPane(), "Enter name of new File:");
					if (path!=null) {
						out.println("Creating file " + f.getAbsolutePath() + File.separatorChar + path);
						File fi = new File(f.getAbsolutePath() + File.separatorChar + path);
						boolean res = fi.createNewFile();
						out.println(res);
						if (!res) {
							JOptionPane.showMessageDialog(app.getRootPane(),
								fi.exists() ? "A file or folder with the same name already exists" :
									"Unable to create file", "Error while creating file",
									JOptionPane.ERROR_MESSAGE);
						}
						fp.tree.expandPath(node);
					} else {
						out.println("Canceled File Creation");
					}
					fp.refresh();
				} catch (IOException e1) {e1.printStackTrace();}
	        });
			JMenuItem NewFolder = new JMenuItem("New Folder");
			NewFolder.addActionListener(e -> {
				String path =
						JOptionPane.showInputDialog(app.getRootPane(), "Enter name of new Folder:");
				if (path!=null) {
					out.println("Creating Folder " + f.getAbsolutePath() + File.separatorChar + path);
					File fi = new File(f.getAbsolutePath() + File.separatorChar + path);
					boolean res = fi.mkdirs();
					out.println(res);
					if (!res) {
						JOptionPane.showMessageDialog(app.getRootPane(),
							fi.exists() ? "A file or folder with the same name already exists" :
								"Unable to create folder", "Error while creating folder",
								JOptionPane.ERROR_MESSAGE);
					}
					fp.tree.expandPath(node);
				} else {
					out.println("Canceled Folder Creation");
				}
				fp.refresh();
	        });
			add(NewFile);
			add(NewFolder);
			this.addSeparator();
		}
		
		add(RENAME);
		add(DELETE);

		this.addSeparator();
		add(REFRESH);
		this.addSeparator();
		add(OPENINEXPLORER);
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
				if (i.getComponent() instanceof SyntaxTextArea
						&&Objects.equals(((SyntaxTextArea)i.getComponent()).getFilePath(), name)) {
					((SyntaxTextArea)i.getComponent()).setFilePath(newFile);
					i.getTab().repaint();
				}
			}
		}
		return res;
	}
}
