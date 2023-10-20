package NNU.Editor.Runners;

import java.io.File;
import javax.swing.JOptionPane;

import NNU.Editor.App;
import NNU.Editor.SyntaxTextArea;
import NNU.Editor.Windows.Window;

public class ShellRunner implements IRunner {
	
	String[] FileExtentions = {"bat", "com", "cmd", "sh", "profile", "bash", "zsh"};
	String[] FileNames = {"run", "start", "debug"};
	String[] StarterFiles = new String[FileNames.length*FileExtentions.length];

	public ShellRunner() {
		int i = 0;
		for (String fex : FileExtentions) {
			for (String fn : FileNames) {
				StarterFiles[i] = fn + '.' + fex;
				i++;
			}
		}
	}

	@Override
	public boolean canRun(App app) {
		Window selwin = app.getSelectedWindow();
		//if (selwin==null) return false;
		return (selwin!=null&&(selwin.getComponent() instanceof SyntaxTextArea
				&&ValidFileExt(FileExtentions,((SyntaxTextArea)selwin.getComponent()).getFilePath())))
				||containsFiles(StarterFiles,app)!=null;
	}

	@Override
	public void Run(App app) {
		try {
			Window w = app.getSelectedWindow();
			
			SyntaxTextArea tx = w != null ? ((SyntaxTextArea)w.getComponent()) : null;
			String filetoopen = tx != null ? tx.getFilePath() : null;
			if (!ValidFileExt(FileExtentions, filetoopen))
				filetoopen =
					app.Folder.getFolderPath() + File.separatorChar + containsFiles(StarterFiles,app);
			String command = app.Folder.getFolderPath() +File.separatorChar+ containsFiles(StarterFiles,app);
			command = slashify(command);
			if (!System.getProperty("os.name").toLowerCase().contains("linux")) {
				//Desktop.getDesktop().browse(new URI(command));
				//ProcessBuilder pb = new ProcessBuilder("\"" + command + "\"");
				Runtime.
				   getRuntime().
				   exec("cmd /c \"start cmd /c "+ command + "\"",
					        null, new File(app.Folder.getFolderPath()));
				//Map<String, String> env = pb.environment();
				//env.put("VAR1", "myValue");
				//env.remove("OTHERVAR");
				//env.put("VAR2", env.get("VAR1") + "suffix");
				//pb.directory(new File(app.Folder.FolderPath));
				//Process p = pb.start();
			} else {
				ProcessBuilder pb = new ProcessBuilder(command);
				//Map<String, String> env = pb.environment();
				//env.put("VAR1", "myValue");
				//env.remove("OTHERVAR");
				//env.put("VAR2", env.get("VAR1") + "suffix");
				pb.directory(new File(app.Folder.getFolderPath()));
				pb.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error running shell","Error running shell",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
