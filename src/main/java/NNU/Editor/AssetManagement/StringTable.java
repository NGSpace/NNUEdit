package NNU.Editor.AssetManagement;

import static NNU.Editor.AssetManagement.AssetManager.strings;

import NNU.Editor.App;
import NNU.Editor.Settings;
import NNU.Editor.Utils.Utils;

public interface StringTable {
	public static class table extends Settings {
		public table(String type) {
			String lang = type;
			try {
				if (App.class.getResource("/NNU/Editor/Assets/Lang/Lang_"+lang+".properties")==null) {
					System.out.println("\"Lang_"+lang+".properties\" does not exist.");
					lang = "en";
				}
			} catch (Exception e) {}
			
			try {
				read(App.class.getResourceAsStream("/NNU/Editor/Assets/Lang/Lang_"+lang+".properties"));
			} catch (ReadException e) {e.printStackTrace();}
		}
	}
	public static void loadLang(String string) {strings = new table(string).map;}
	/**
	 * 
	 * Returns the value from a HashMap of preloaded strings from the Lang.properties file
	 * @apiNote The current list of strings is very much lacking!
	 * 
	 * @param key
	 * @return the value from the Lang.properties file
	 * 
	 */
	public static String getStringRaw(String key) {
		if (strings==null) return key;
		Object obj = strings.get(key.trim());
		if (obj==null) return key==null?"String not found":key;
		return obj.toString();
	}
	
	/**
	 * 
	 * Returns a processed version of getStringRaw
	 * @apiNote The current list of strings is very much lacking!
	 * 
	 * @param key
	 * @return the value from the Lang.properties file
	 * 
	 */
	public static String getString(String key) {
		return getStringRaw(key).replace("\\n", "\r\n");
	}
	/**
	 * 
	 * Returns getString(key) but replaces all extra arguments with corresponding arguements.
	 * 
	 * @param key
	 * @return the value from the Lang.properties file
	 * 
	 */
	public static String getString(String key, Object... args) {
		char[] str = getString(key).toCharArray();
		StringBuilder finalstr = new StringBuilder();
		boolean esc = false;
		for (int i = 0; i<str.length;i++) {
			char c = str[i];
			if (esc) {
				try {
					if (Utils.parseInt(c)>args.length)
						throw new Exception();
					finalstr.append(args[Utils.parseInt(c)]);
				} catch (Exception e) {finalstr.append("%" + c);}
				esc = false;
				continue;
			}
			if (c=='%') {esc = true;continue;}
			finalstr.append(c);
		}
		return finalstr.toString().replace("\\n", "\r\n");
	}
}
