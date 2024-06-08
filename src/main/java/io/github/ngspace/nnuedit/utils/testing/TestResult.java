package io.github.ngspace.nnuedit.utils.testing;

public class TestResult {
	public int succesful = 0;
	public int attempted = 0;
	public TestResult(int succesful, int attempted) {
		this.succesful = succesful;
		this.attempted = attempted;
	}
	public TestResult(boolean succesful) {
		this.succesful = succesful?1:0;
		this.attempted = 1;
	}
	@Override public String toString() {
		return (isSuccesful() ? "Passed" : "Failed") + ", " + succesful + '/' + attempted;
	}
	public boolean isSuccesful() {
		return succesful>=attempted;
	}
}
