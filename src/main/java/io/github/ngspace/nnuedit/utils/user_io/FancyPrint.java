package io.github.ngspace.nnuedit.utils.user_io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FancyPrint extends java.io.PrintStream {
	public final boolean fancy;
	public final boolean color;
	public final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	public FancyPrint(OutputStream out, boolean fancy, boolean color) {
		super(out,true);
		this.fancy=fancy;
		this.color=color;
	}
	
	
	public static void initOutput(File outputfile, boolean b, boolean c) throws IOException {
		if (!outputfile.exists()&&!(outputfile.getParentFile().mkdirs()&&outputfile.createNewFile()))
			throw new IOException("Failed to create log");
		FancyPrint printer = new FancyPrint(new TeeOutputStream(System.out, new FileOutputStream(outputfile)), b, c);
        System.setOut(printer);
        System.setErr(printer);
	}
	public static void initOutput(boolean b, boolean c) {
		FancyPrint printer = new FancyPrint(System.out, b, c);
        System.setOut(printer);
        System.setErr(printer);
	}
	
	
	
	public static String getClassName(String fullclassname) {
		String[] s = fullclassname.split("\\.");
		return s[s.length-1];
	}

	@Override public void println()            {print  ("\n");}
	@Override public void println(Object obj ) {println(String.valueOf(obj));}
	@Override public void println(boolean obj) {println(String.valueOf(obj));}
	@Override public void println(int obj    ) {println(String.valueOf(obj));}
	@Override public void println(char obj   ) {println(String.valueOf(obj));}
	@Override public void println(long obj   ) {println(String.valueOf(obj));}
	@Override public void println(float obj  ) {println(String.valueOf(obj));}
	@Override public void println(double obj ) {println(String.valueOf(obj));}
	@Override public void println(char[] obj ) {println(String.valueOf(obj));}

	@Override public void print  (Object obj ) {print  (String.valueOf(obj));}
	@Override public void print  (boolean obj) {print  (String.valueOf(obj));}
	@Override public void print  (int obj    ) {print  (String.valueOf(obj));}
	@Override public void print  (char obj   ) {print  (String.valueOf(obj));}
	@Override public void print  (long obj   ) {print  (String.valueOf(obj));}
	@Override public void print  (float obj  ) {print  (String.valueOf(obj));}
	@Override public void print  (double obj ) {print  (String.valueOf(obj));}
	@Override public void print  (char[] obj ) {print  (String.valueOf(obj));}
	
	@Override public void println(String text) {print(text + "\n");}
	@Override public void print(String text) {
		try {
			if (!fancy) super.print(text);
			else super.print(getPrefix(color) + text);
		} catch (Exception e) {super.print(text);}
	}
	private String getPrefix(boolean color) {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		int i = 2;
		String callingClass = stacktrace[i].getClassName();
		int linenum = stacktrace[i].getLineNumber();
		while (callingClass.equals("io.github.ngspace.nnuedit.utils.user_io.FancyPrint")
				||callingClass.startsWith("java.lang")) {
			i++;
			callingClass = stacktrace[i].getClassName();
			linenum = stacktrace[i].getLineNumber();
		}
		
		String time = '[' + dtf.format(LocalDateTime.now()) + "] ";
		String caller = getClassName(callingClass)+":"+linenum+"> ";
		if (!color) return time + caller;
		return ConsoleColors.BLUE+time+ConsoleColors.RED_BOLD+caller+ConsoleColors.RESET+ConsoleColors.WHITE_BRIGHT;
	}
}