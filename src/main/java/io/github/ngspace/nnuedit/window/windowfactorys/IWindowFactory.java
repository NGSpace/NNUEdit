package io.github.ngspace.nnuedit.window.windowfactorys;

import java.io.File;
import java.io.IOException;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.window.abstractions.Window;

@FunctionalInterface
public abstract interface IWindowFactory {
	public abstract Window createWindowFromFile(App app, File file) throws IOException;
}
