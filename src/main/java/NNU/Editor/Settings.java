package NNU.Editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JOptionPane;

import NNU.Editor.Utils.ValueNotFoundException;

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
		} catch (IOException e) {
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
		read(ins);
	}
	protected Settings(InputStream ins) {
		this.app = null;
		read(ins);
	}
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
	
	protected void read(InputStream ins)  {
		Scanner myReader = new Scanner(ins);
		StringBuilder strb = new StringBuilder();
		while (myReader.hasNextLine())
			strb.append(myReader.nextLine() + "\n");
		//System.out.println(strb);
	    myReader.close();
	    finalizelist(strb.toString());
	}
	protected void finalizelist(String string) {
		map = new HashMap<String,Object>();
		String[] ls = string.split("\n");
		for (int i = 0;i<ls.length;i++) {
			try {
				if (ls[i].isEmpty()) continue;
				String[] kAV = ls[i].split("=", 2);
				map.put(kAV[0], kAV[1]);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Corrupted settings file! can't open thy program!",
						"Error reading settings file", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}
	public String get(String key) throws ValueNotFoundException {
		String res = null;
		res = (String) map.get(key);
		if (res==null) {
			res = (String) defaults.get(key);
			map.put(key, res);
			try {
				save();
			} catch (IOException e) {e.printStackTrace();}
		}
		if (res==null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
			    System.out.println(entry.getKey()+" : "+entry.getValue());
			}
			throw new ValueNotFoundException("The key " + key + 
					" does not exist in either user settings or defaults");
		}
		return res;
		
	}
	public boolean getBoolean(String key) throws ValueNotFoundException {
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
	public int getInt(String string) throws ValueNotFoundException {
		return Integer.valueOf(get(string));
	}
	
	
}
