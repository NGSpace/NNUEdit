package io.github.ngspace.nnuedit.window.windowfactorys;

import java.io.File;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.window.abstractions.Window;

@FunctionalInterface
public abstract interface IWindowFactory {
	
	public abstract Window CreateWindowFromFile(App app, File file);

}
