package io.github.ngspace.nnuedit.utils.testing.test_types;

import io.github.ngspace.nnuedit.utils.testing.TestResult;

public interface BoolTest<T extends Exception> extends Test<T> {

	@Override public default TestResult runTest() throws T {return new TestResult(run());}
	public boolean run() throws T;

}
