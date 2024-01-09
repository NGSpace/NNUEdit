package NNU.Editor;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import NNU.Editor.Utils.UserMessager;
import NNU.Editor.Utils.Utils;

public class Settings {
	
	public File file;
	public Map<String,Object> map = new HashMap<String,Object>();
	/**
	 * The default values read from "/NNU/Editor/NNUEdit.properties" in the jar file
	 */
	public static final Map<String,Object> defaults = 
			new Settings(App.class.getResourceAsStream("/NNU/Editor/NNUEdit.properties")).map;
	public final App app;
	
	/**
	 * The settings manager
	 * @param FilePath path to read from
	 * @param app the app linked to this
	 * @throws Exception
	 */
	public Settings(String FilePath, App app) {
		this.app = app;
		try {
			file = new File(FilePath);
			if (!file.exists()&&(makefile(FilePath))) {
				map = new HashMap<String,Object>(defaults);
				save();
			}
			read(new FileInputStream(file));
		} catch (Exception e) {
			map = new HashMap<String,Object>(defaults);
			try {
				save();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	public void save() throws IOException {
		FileWriter fw = new FileWriter(file);
		for (Entry<String, Object> entry : map.entrySet()) {
			fw.write(entry.getKey() + "=" + entry.getValue() + "\n");
		}
		fw.close();
	}
	public Settings(InputStream ins, App app) {
		this.app = app;
		try {
			read(ins);
		} catch (ReadException e) {
			System.out.println("Corrupted properties file");
			UserMessager.showErrorDialogTB("err.corruptedsettings.title","err.corruptedsettings",e.line);
			System.exit(1);
		}
	}
	protected Settings(InputStream ins) {
		this.app = null;
		try {
			read(ins);
		} catch (ReadException e) {
			System.out.println("Corrupted properties file");
			UserMessager.showErrorDialogTB("err.corruptedsettings.title","err.corruptedsettings",e.line);
			System.exit(1);
		}
	}
	protected Settings() {this.app=null;}
	protected boolean makefile(String FilePath) throws IOException {
		try {
			new File(FilePath).getParentFile().mkdirs();
			if (!new File(FilePath).createNewFile())
				return false;
			map = new HashMap<String,Object>(defaults);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void refresh() throws Exception {
		read(new FileInputStream(file));
		app.refreshSettings();
	}
	
	protected void read(InputStream ins) throws ReadException  {
		Scanner myReader = new Scanner(ins);
		ArrayList<String> lines = new ArrayList<String>();
		while (myReader.hasNextLine())
			lines.add(myReader.nextLine());
	    myReader.close();
	    finalizelist(lines.toArray(new String[0]));
	}
	protected void finalizelist(String[] ls) throws ReadException {
		map = new HashMap<String,Object>();
		for (int i = 0;i<ls.length;i++) {
			try {
				if (ls[i].trim().isEmpty()) continue;
				if (ls[i].charAt(0)=='#') continue;
				String[] kAV = ls[i].split("=", 2);
				map.put(kAV[0], kAV[1]);
			} catch (Exception e) {
				throw new ReadException(i);
			}
			
		}
	}
	public String get(String key) {
		String res = null;
		Object r = map.get(key);
		if (r instanceof String)
			res = (String) r;
		else if (r!=null) res = String.valueOf(r);
		if (res==null) {
			res = (String) defaults.get(key);
			map.put(key, res);
			try {
				save();
			} catch (IOException e) {e.printStackTrace();}
		}
		if (res==null) {
			System.err.println("The key " + key + 
					" does not exist in either user settings or defaults");
			for (Map.Entry<String, Object> entry : map.entrySet()) {
			    System.err.println(entry.getKey()+" : "+entry.getValue());
			}
		}
		return res;
		
	}
	public boolean getBoolean(String key) {
		return Boolean.TRUE.equals(Boolean.valueOf(get(key)));
		
	}
	
	protected String getKey(Map<String, Object> map, String value) {
		for(Entry<String, Object> entry: map.entrySet()) {
			// if give value is equal to value from entry
			// print the corresponding key
			if(entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}
	public void set(String key,Object value) {
		map.put(key, value);
		try {
			save();
		} catch (IOException e) {e.printStackTrace();}
	}
	public void set(String key,Font f) {
		set(key+".family", f.getFamily());
		set(key+".size", f.getSize());
		set(key+".style", f.getStyle());
	}
	public void set(String key,Color f) {
		set(key+".r", f.getRed  ());
		set(key+".g", f.getGreen());
		set(key+".b", f.getBlue ());
		set(key+".a", f.getAlpha());
	}
	public int getInt(String string) {
		return Integer.valueOf(get(string).trim().replaceAll("[^\\d]+", ""));
	}
	public Font getFont(String str) {
		return new Font
				(get(str+".family"), Utils.parseInt(get(str+".style")), Utils.parseInt(get(str+".size")));
	}
	public Color getColor(String key) {
		return new Color(getInt(key+".r"),getInt(key+".g"),getInt(key+".b"),getInt(key+".a"));
	}
	
	protected static class ReadException extends Exception{
		private static final long serialVersionUID = 6607957682950815858L;
		public int line;
		protected ReadException(int line) {
			super();
			this.line=line;
		}
	}
}
