package NNU.Editor.Runners;

import java.awt.Desktop;
import java.io.File;

import NNU.Editor.App;
import NNU.Editor.Utils.UserMessager;
import NNU.Editor.Windows.Interfaces.Editor;
import NNU.Editor.Windows.Interfaces.Window;

public class HTMLRunner implements IRunner {
	
	String[] FileExtentions = {"html"};
	String[] StarterFiles = {"index.html","index.htm","index.htmx"};

	public HTMLRunner() {}

	@Override
	public boolean canRun(App app) {
		Window selwin = app.getSelectedWindow();
		//if (selwin==null) return false;
		return (selwin!=null&&(selwin.getComponent() instanceof Editor
				&&ValidFileExt(FileExtentions,((Editor)selwin.getComponent()).getFilePath())))
				||containsFiles(StarterFiles,app)!=null;
	}

	@Override
	public void Run(App app) {
		try {
			Window w = app.getSelectedWindow();
			
			Editor tx = w != null ? ((Editor)w.getComponent()) : null;
			String filetoopen = tx != null ? tx.getFilePath() : null;
			if (!ValidFileExt(FileExtentions, filetoopen))
				filetoopen =
					app.Folder.getFolderPath() + File.separatorChar + containsFiles(StarterFiles,app);
			RunFile(new File(filetoopen), app);
		} catch (Exception e) {
			e.printStackTrace();
			UserMessager.showErrorDialogTB("runner.html.failedopenbrowser.title",
					"runner.html.failedopenbrowser", e.getMessage());
		}
	}

	@Override
	public void RunFile(File f, App app) throws Exception {
		Desktop.getDesktop().browse(f.toURI());
	}

	@Override
	public boolean canRunFile(File f, App app) {
		return ValidFileExt(FileExtentions,(f.getPath()));
	}

}
