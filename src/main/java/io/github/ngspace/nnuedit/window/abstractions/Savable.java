package io.github.ngspace.nnuedit.window.abstractions;

public interface Savable {
	public boolean isSaved();
	public boolean save(boolean ask);
}
