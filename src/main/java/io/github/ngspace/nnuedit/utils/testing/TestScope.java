package io.github.ngspace.nnuedit.utils.testing;

public enum TestScope {
	/**
	 * The test will always execute if debugging is enabled
	 */
	BASIC(1),
	/**
	 * The test will execute if debugging level 2 is enabled
	 */
	DEEP(2),
	/**
	 * The test will execute if debugging level 3 is enabled
	 */
	THOROUGH(3),
	;
	public final int scope;
	private TestScope(int scope) {this.scope = scope;}
}