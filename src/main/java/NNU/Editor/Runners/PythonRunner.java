package NNU.Editor.Runners;

import java.io.File;

import NNU.Editor.App;
import NNU.Editor.EditorTextArea;
import NNU.Editor.Utils.UserMessager;
import NNU.Editor.Windows.Interfaces.Window;

public class PythonRunner implements IRunner {

	String[] FileExtentions = {"py","pyi","pyc","pyd","pyo","pyw","pyz"};
	String[] FileNames = {"main", "start", "index", "run", "debug"};
	String[] StarterFiless = new String[FileNames.length*FileExtentions.length];
	
	public String[] getStarterFiles() {return StarterFiless;}

	public PythonRunner() {
		int i = 0;
		for (String fex : FileExtentions) {
			for (String fn : FileNames) {
				getStarterFiles()[i] = fn + '.' + fex;
				i++;
			}
		}
	}

	@Override
	public boolean canRun(App app) {
		Window selwin = app.getSelectedWindow();
		return (selwin!=null&&(selwin.getComponent() instanceof EditorTextArea
				&&ValidFileExt(FileExtentions,((EditorTextArea)selwin.getComponent()).getFilePath())))
				||containsFiles(getStarterFiles(),app)!=null;
	}

	@Override
	public void Run(App app) {
		try {
			Window w = app.getSelectedWindow();
			
			EditorTextArea tx = w != null ? ((EditorTextArea)w.getComponent()) : null;
			String filetoopen = tx != null ? tx.getFilePath() : null;
			if (!ValidFileExt(FileExtentions, filetoopen))
				filetoopen =
					app.Folder.getFolderPath() + File.separatorChar + containsFiles(getStarterFiles(),app);
			String file = app.Folder.getFolderPath() +File.separatorChar+ 
					containsFiles(getStarterFiles(),app);
			file = slashify(file);
			RunFile(new File(file), app);
		} catch (Exception e) {
			e.printStackTrace();
			UserMessager.showErrorDialogTB("runner.shell.err.title","runner.shell.err",e.getLocalizedMessage());
		}
	}
	
	@Override
	public void RunFile(File f, App app) throws Exception {
		/* Can't use Main.SYSTEM because universal might be used on a windows device */
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			ProcessBuilder pb = new ProcessBuilder("cmd","/c", "start cmd /c \"py "+f.getAbsolutePath()+" & pause\"");
			pb.directory(new File(app.Folder.getFolderPath()));
			pb.start();
		} else {
			ProcessBuilder pb = new ProcessBuilder("py","\""+f.getPath() + "\"");
			pb.directory(new File(app.Folder.getFolderPath()));
			pb.start();
		}
	}
	

	@Override
	public boolean canRunFile(File f, App app) {
		return ValidFileExt(FileExtentions,(f.getPath()));
	}

}
