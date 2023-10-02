package NNU.Editor.Utils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * the bullshit class I made to find out where I forgot that one System.out.println("test");
 */
public class BSPS extends PrintStream {
	public BSPS(OutputStream out) {
		super(out);
	}

	@Override public void println() {Thread.dumpStack();}
	@Override public void println(Object shit) {Thread.dumpStack();}
	@Override public void println(String shit) {Thread.dumpStack();}
	@Override public void println(boolean shit) {Thread.dumpStack();}
	@Override public void println(int shit) {Thread.dumpStack();}
	@Override public void println(char shit) {Thread.dumpStack();}
	@Override public void println(long shit) {Thread.dumpStack();}
	@Override public void println(float shit) {Thread.dumpStack();}
	@Override public void println(double shit) {Thread.dumpStack();}
	@Override public void println(char[] shit) {Thread.dumpStack();}
	
	@Override public void print(Object shit) {Thread.dumpStack();}
	@Override public void print(String shit) {Thread.dumpStack();}
	@Override public void print(boolean shit) {Thread.dumpStack();}
	@Override public void print(int shit) {Thread.dumpStack();}
	@Override public void print(char shit) {Thread.dumpStack();}
	@Override public void print(long shit) {Thread.dumpStack();}
	@Override public void print(float shit) {Thread.dumpStack();}
	@Override public void print(double shit) {Thread.dumpStack();}
	@Override public void print(char[] shit) {Thread.dumpStack();}
	

    @Override public PrintStream printf(String format, Object ... args) 
    {Thread.dumpStack();return null;}
    @Override public PrintStream printf(Locale l, String format, Object ... args) 
    {Thread.dumpStack();return null;}
	
}
