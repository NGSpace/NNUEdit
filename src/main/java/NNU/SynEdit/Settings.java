package NNU.SynEdit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Settings {
	
	public File file;
	public Map<String,Object> map = new HashMap<String,Object>();
	public static final Map<String,Object> defaults = 
			new Settings(App.class.getResourceAsStream("/NNU/SynEdit/SynEdit.properties")).map;

	public Settings(String FilePath) throws Exception {
		file = new File(FilePath);
		if (!file.exists()) {
			if (makefile(FilePath)) {
				map = new HashMap<String,Object>(defaults);
				save();
			}
		}
		try {
			read(new FileInputStream(file));
		} catch (FileNotFoundException e) {
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
	public Settings(InputStream ins) {
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

	protected void read(InputStream ins)  {
		Scanner myReader = new Scanner(ins);
		StringBuilder strb = new StringBuilder();
		while (myReader.hasNextLine())
			strb.append(myReader.nextLine() + "\n");
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
	protected String get(String key) throws ValueNotFoundException {
		String res = null;
		res = (String) map.get(key);
		if (res==null) {
			res = (String) defaults.get(key);
			map.put(key, res);
			try {
				save();
			} catch (IOException e) {e.printStackTrace();}
		}
		if (res==null)
			throw new ValueNotFoundException("The key " + key + 
					" does not exist in either local settings or defaults");
		return res;
		
	}
	
}
