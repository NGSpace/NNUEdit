package NNU.Editor.Windows.Interfaces;

public interface EditorWindow extends Window {

	public default boolean isEditor() {return true;}
	public Editor getEditor();
}
