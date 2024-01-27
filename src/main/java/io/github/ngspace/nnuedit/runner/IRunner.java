package io.github.ngspace.nnuedit.runner;

import java.io.File;

import io.github.ngspace.nnuedit.App;

public interface IRunner {
	public boolean canRun(App app);
	public void Run(App app);
	public boolean canRunFile(File f, App app);
	public void RunFile(File f, App app) throws Exception;
	
	public default boolean ValidFileExt(String[] fileext, String s) {
		if (s==null) return false;
		for (String fex : fileext)
			if (s.endsWith(fex))
				return true;
		return false;
	}
	public default String containsFiles(String[] starterfiles, App app) {
		
		for (String fex : starterfiles)
			if (app.Folder.contains(fex.toLowerCase(), true))
				return fex;
		return null;
	}
	public default String slashify(String ts) {
		return ts.replace('\\', '/');
	}
}
