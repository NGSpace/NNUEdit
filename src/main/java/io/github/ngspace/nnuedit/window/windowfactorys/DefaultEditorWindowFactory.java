package io.github.ngspace.nnuedit.window.windowfactorys;

import java.io.File;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.window.TextEditorWindow;
import io.github.ngspace.nnuedit.window.abstractions.Window;

public class DefaultEditorWindowFactory implements IWindowFactory {
	@Override public Window CreateWindowFromFile(App app, File file) {
		return new TextEditorWindow(app,file);
	}
}
