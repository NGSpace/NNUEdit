package io.github.ngspace.nnuedit.window.abstractions;

public interface EditorWindow extends Window {

	public default boolean isEditor() {return true;}
	public Editor getEditor();
}
