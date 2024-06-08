package io.github.ngspace.nnuedit.window.abstractions;

public interface EditorWindow extends Window {
	@Override public default boolean isEditor() {return true;}
	@Override public Editor getEditor();
}
