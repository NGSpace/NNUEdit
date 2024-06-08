package io.github.ngspace.nnuedit.menu.prefrences.options;

public class HeadlessHeader extends Header {
	public HeadlessHeader(String name) {
		this(name, 10, true);
	}
	public HeadlessHeader(String name, int size, boolean underline) {
		super(name);
		this.size = size;
		this.underline = underline;
	}
}
