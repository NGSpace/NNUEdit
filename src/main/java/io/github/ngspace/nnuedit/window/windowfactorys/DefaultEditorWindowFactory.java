package io.github.ngspace.nnuedit.window.windowfactorys;

import java.io.File;
import java.io.IOException;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.window.TextEditorWindow;
import io.github.ngspace.nnuedit.window.abstractions.Window;

public class DefaultEditorWindowFactory implements IWindowFactory {
	@Override public Window createWindowFromFile(App app, File file) throws IOException {
		return new TextEditorWindow(app,file);
	}
}
