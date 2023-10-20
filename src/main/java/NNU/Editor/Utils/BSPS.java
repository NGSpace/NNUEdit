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

	@Override public void println() {new Exception(String.valueOf("")).printStackTrace();}
	@Override public void println(Object shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(String shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(boolean shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(int shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(char shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(long shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(float shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(double shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void println(char[] shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	
	@Override public void print(Object shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(String shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(boolean shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(int shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(char shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(long shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(float shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(double shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	@Override public void print(char[] shit) {new Exception(String.valueOf(shit)).printStackTrace();}
	

    @Override public PrintStream printf(String format, Object ... args) 
    {new Exception(String.valueOf(args)).printStackTrace();return null;}
    @Override public PrintStream printf(Locale l, String format, Object ... args) 
    {new Exception(String.valueOf(args)).printStackTrace();return null;}
	
}
