package io.github.ngspace.nnuedit.utils.testing.test_types;

import io.github.ngspace.nnuedit.utils.testing.TestResult;

public interface ExceptionTest<T extends Exception> extends Test<Exception> {
	@Override public default TestResult runTest() throws Exception {
//		Thread t = new Thread(() -> {
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		});
//		t.start();
//		while (!t.isInterrupted()) Thread.sleep(1000);
		return run();
	}
	public TestResult run() throws T;
	public TestResult fail() throws T;
}
