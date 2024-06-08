package io.github.ngspace.nnuedit.utils.settings;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.utils.user_io.UserMessager;

public class Settings {
	
	protected File file = null;
	protected Map<String,Object> map = new HashMap<String,Object>();
	protected boolean printGetError = true;
	
	public void setPrintMissingKey(boolean printGetError) {this.printGetError = printGetError;}
	public boolean shouldPrintMissingKey() {return printGetError;}
	public Map<String, Object> getMap() {return map;}
	public void setMap(Map<String, Object> map) {this.map = map;}
	
	public Map<String,Object> defaults;
	
	/**
	 * The settings manager
	 * @param FilePath path to read from
	 * @param app the app linked to this
	 * @throws Exception
	 */
	public Settings(File file, Map<String,Object> defaults) {
		this.file = file;
		this.defaults = defaults;
		try {
			if (!file.exists()&&(createfile(file.getAbsolutePath()))) {
				map = new HashMap<String,Object>(defaults);
				save();
			}
			process(new FileInputStream(file));
		} catch (Exception e) {
			map = new HashMap<String,Object>(defaults);
			e.printStackTrace();
			try {
				save();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public Settings(InputStream ins) {
		try {
			process(ins);
		} catch (ReadException e) {
			System.out.println("Corrupted properties file");
			UserMessager.showErrorDialogTB("err.corruptedsettings.title","err.corruptedsettings",e.line);
			Main.crash(1);
		} catch (IOException e) {
			System.out.println("Corrupted properties file");
			UserMessager.showErrorDialogTB("err.corruptedsettings.title","generic.error", e.getMessage());
			Main.crash(1);
		}
	}
	protected Settings() {}
	protected boolean createfile(String FilePath) throws IOException {
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
	public void processSave(File file) {
		if (file==null) return;
		try {
			process(new FileInputStream(file));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void process(File file) throws IOException {
		process(new FileInputStream(file));
	}
	
	protected void process(InputStream ins) throws IOException {
		Scanner myReader = new Scanner(ins);
		ArrayList<String> lines = new ArrayList<String>();
		while (myReader.hasNextLine())
			lines.add(myReader.nextLine());
	    myReader.close();
	    finalizelist(lines.toArray(new String[lines.size()]));
	}
	protected void finalizelist(String[] ls) throws IOException {
		if (map==null) map = new HashMap<String,Object>();
		for (int i = 0;i<ls.length;i++) {
			try {
				if (ls[i].trim().isEmpty()) continue;
				if (ls[i].charAt(0)=='#') continue;
				String[] kAV = ls[i].split("=", 2);
				int ln = kAV[1].length();
				while (kAV[1].charAt(ln-1)=='\\') {
					i++;
					kAV[1]=kAV[1].substring(0, ln - 1) + ls[i];
					ln = kAV[1].length();
				}
				map.put(kAV[0], kAV[1]);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ReadException(i);
			}
			
		}
	}
	AtomicBoolean b = new AtomicBoolean(false);
	public void save() throws IOException {
		if (file==null&&!b.get()) return;
		b.set(true);
		try (FileWriter fw = new FileWriter(file)) {
			for (Entry<String, Object> entry : map.entrySet()) fw.write(entry.getKey() + "=" + entry.getValue() + "\n");
		} catch (Exception e) {
			b.set(false);
			throw e;
		}
	}
	/* Getters */
	public String get(String key) {
		Object r = map.get(key);
		String res = r!=null ? r.toString():null;
		if (r==null) {
			res = (String) defaults.get(key);
			if (res!=null) {
				map.put(key, res);
				try {
					save();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		/* Was a bit too spammy so I had to disable it. */
		if (res==null&&printGetError) {
			System.err.println("The key " + key + 
					" does not exist in either user settings or defaults");
			for (Map.Entry<String, Object> entry : map.entrySet()) {
			    System.err.println(entry.getKey()+" : "+entry.getValue());
			}
		}
		return res;
	}
	
	public boolean has(String key) {return get(key)!=null;}
	public boolean getBoolean(String key) {return Boolean.TRUE.equals(Boolean.valueOf(get(key)));}
	public int getInt(String string) {return Integer.parseInt(get(string).trim());}
	public double getDouble(String string) {return Double.parseDouble(get(string).trim());}
	
	public Font getFont(String str) {
		return new Font(get(str+".family"), Utils.safeParseInt(get(str+".style")),Utils.safeParseInt(get(str+".size")));
	}
	public Color getColor(String key) {
		return new Color(getInt(key+".r"),getInt(key+".g"),getInt(key+".b"),getInt(key+".a"));
	}
	
	/* Set without triggering event listeners */
	
	protected void setNoEvent(String key,Object value) {
		map.put(key, value);
		try {
			save();
		} catch (IOException e) {e.printStackTrace();}
	}
	protected void setNoEvent(String key,Font f) {
		setNoEvent(key+".family", f.getFamily());
		setNoEvent(key+".size"  , f.getSize  ());
		setNoEvent(key+".style" , f.getStyle ());
	}
	protected void setNoEvent(String key,Color f) {
		setNoEvent(key+".r", f.getRed  ());
		setNoEvent(key+".g", f.getGreen());
		setNoEvent(key+".b", f.getBlue ());
		setNoEvent(key+".a", f.getAlpha());
	}
	
	/* Set WITH triggering event listeners */
	
	public void set(String key,Object value) {
		Object oldval = null;
		try {oldval = get(key);}      catch (Exception e) {e.printStackTrace();}
		setNoEvent(key, value);
		dispatchChangedValueEvents(key, value, oldval);
	}
	public void set(String key, Font value) {
		Object oldval = null;
		try {oldval = getFont(key);}  catch (Exception e) {e.printStackTrace();}
		setNoEvent(key, value);
		dispatchChangedValueEvents(key, value, oldval);
	}
	public void set(String key,Color value) {
		Object oldval = null;
		try {oldval = getColor(key);} catch (Exception e) {e.printStackTrace();}
		setNoEvent(key, value);
		dispatchChangedValueEvents(key, value, oldval);
	}
	
	/* Event Listeners */
	
	protected List<ChangedSettingsListener> changeListers = new ArrayList<ChangedSettingsListener>();
	
	public void addValueChangeListener(ChangedSettingsListener listener) {changeListers.add(listener);}
	public void addRefreshListener(RefreshListener listener) {changeListers.add(listener);}
	public void dispatchChangedValueEvents(String key, Object value, Object oldval) {
		for (ChangedSettingsListener lis : changeListers) lis.changedValue(key, value, oldval, this);
	}
	public File getFile() {return file;}
	public void setFile(File file) {this.file = file;}
	public static Map<String, Object> getMap(InputStream is) {return new Settings(is).getMap();}
}
