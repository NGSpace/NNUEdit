package NNU.Editor.Runners;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import NNU.Editor.App;
import NNU.Editor.SyntaxTextArea;
import NNU.Editor.Windows.Window;

public class HTMLRunner implements IRunner {
	
	String[] FileExtentions = {"html"};
	String[] StarterFiles = {"index.html","index.htm"};

	public HTMLRunner() {}

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
			Desktop.getDesktop().browse(new URI("file://" + filetoopen.replace('\\', '/')));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error Opening browser","Error Opening browser",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
