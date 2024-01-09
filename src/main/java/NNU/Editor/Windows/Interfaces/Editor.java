package NNU.Editor.Windows.Interfaces;

public interface Editor extends Savable {
	public String getFilePath();
	public void setFilePath(String value);
	public boolean isOpen(String path);
	public void openFind();
	public void escape();
}
