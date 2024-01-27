package io.github.ngspace.nnuedit.runner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.utils.UserMessager;
import io.github.ngspace.nnuedit.utils.registry.Registries;

public class ProjectRunner implements IRunner {
	
	public static final String PROJECTFILE = ".nnuproject";

	@Override
	public boolean canRun(App app) {
		return app.Folder.contains(PROJECTFILE.toLowerCase(), true);
	}

	@Override
	public void Run(App app) {
		String runnertype = "Unknown";
		try {
			IRunner resRunner = null;
			File resFile = null;
			
			File f = new File(app.Folder.getFolderPath() + File.separatorChar + PROJECTFILE);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(f);
			doc.getDocumentElement().normalize();
			Node type = doc.getElementsByTagName("project").item(0);
		    NodeList nodeList = type.getChildNodes();
		    int n = nodeList.getLength();
		    Node current;
		    for (int i=0; i<n; i++) {
		        current = nodeList.item(i);
		        switch (current.getNodeName()) {
					case "file":
						String resf = current.getTextContent();
						Path p = Paths.get(resf);
						if (!p.isAbsolute()) resf = app.Folder.getFolderPath() + File.separatorChar + resf;
						resFile = new File(resf);
						break;
					case "type":
						runnertype = current.getTextContent();
						resRunner = Registries.Runners.get(current.getTextContent());
						break;
					default:
						break;
				}
		    }
		    if (resRunner==null) {
				UserMessager.showErrorDialogTB("runner.project.error.title",
						"runner.project.unknowntype",runnertype);
				return;
		    }
		    resRunner.RunFile(resFile, app);
		} catch (Exception e) {
			e.printStackTrace();
			UserMessager.showErrorDialogTB("runner.project.error.title",
					"runner.project.genericerror",e.getLocalizedMessage());
		}
	}

	@Override
	public void RunFile(File f, App app) throws Exception {
		throw new ProjectCannotRunItselfException("Don't try to outsmart me: project can't run project");
	}

	@Override
	public boolean canRunFile(File f, App app) {
		return false;
	}
}

class ProjectCannotRunItselfException extends Exception {
	public ProjectCannotRunItselfException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -5345004481419592535L;
}
