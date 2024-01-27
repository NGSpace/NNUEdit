package io.github.ngspace.nnuedit.window.abstractions;

public interface Editor extends Savable {
	
	public String DEFAULT = "Default/Editor";
	public String getFilePath();
	public void setFilePath(String value);
	public boolean isOpen(String path);
	public void openFind();
	public void escape();
	public void openfile(boolean load, boolean save);
}
