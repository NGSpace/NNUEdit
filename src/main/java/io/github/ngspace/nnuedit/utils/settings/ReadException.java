package io.github.ngspace.nnuedit.utils.settings;


public class ReadException extends Exception {
	private static final long serialVersionUID = 6607957682950815858L;
	public int line;
	protected ReadException(int line) {
		super();
		this.line=line;
	}
}
