package io.github.ngspace.nnuedit.utils.testing.test_types;

import io.github.ngspace.nnuedit.utils.testing.TestResult;
import io.github.ngspace.nnuedit.utils.testing.TestScope;

public interface Test<T extends Exception> {
	public default TestScope getTestScope() {return TestScope.BASIC;}
	public TestResult runTest() throws T;
}
