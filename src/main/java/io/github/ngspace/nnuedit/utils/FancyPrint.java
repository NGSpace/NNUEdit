package io.github.ngspace.nnuedit.utils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FancyPrint extends PrintStream implements ConsoleColors {

	public FancyPrint(OutputStream out) {
		super(out,true);
	}

	@Override public void println() {print("\n");}
	@Override public void println(Object obj) {println(String.valueOf(obj));}
	@Override public void println(boolean obj) {println(String.valueOf(obj));}
	@Override public void println(int obj) {println(String.valueOf(obj));}
	@Override public void println(char obj) {println(String.valueOf(obj));}
	@Override public void println(long obj) {println(String.valueOf(obj));}
	@Override public void println(float obj) {println(String.valueOf(obj));}
	@Override public void println(double obj) {println(String.valueOf(obj));}
	@Override public void println(char[] obj) {println(String.valueOf(obj));}

	@Override public void print(Object obj) {print(String.valueOf(obj));}
	@Override public void print(boolean obj) {print(String.valueOf(obj));}
	@Override public void print(int obj) {print(String.valueOf(obj));}
	@Override public void print(char obj) {print(String.valueOf(obj));}
	@Override public void print(long obj) {print(String.valueOf(obj));}
	@Override public void print(float obj) {print(String.valueOf(obj));}
	@Override public void print(double obj) {print(String.valueOf(obj));}
	@Override public void print(char[] obj) {print(String.valueOf(obj));}
	
	@Override public void println(String text) {
		print(text + "\n");
	}
	@Override public void print(String text) {
		try {
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
			int i = 2;
			String caller = stacktrace[i].getClassName();
			int linenum = stacktrace[i].getLineNumber();
			while (caller.equals("io.github.ngspace.nnuedit.utils.FancyPrint")
					||caller.equals("java.lang.Throwable$WrappedPrintStream")
					||caller.equals("java.lang.Throwable")) {
				i++;
				caller = stacktrace[i].getClassName();
				linenum = stacktrace[i].getLineNumber();
			}
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();  
			super.print(BLUE + '[' + dtf.format(now) + "] "
					+ RED_BOLD + getClassName(caller)+":"+linenum+"> " + RESET
					+ WHITE_BRIGHT + text);
		} catch (Throwable e) {
			super.print(text);
		}
		
	}
	
	protected String getClassName(String fullclassname) {
		
		String[] s = fullclassname.split("\\.");
		return s[s.length-1];
	}

}
