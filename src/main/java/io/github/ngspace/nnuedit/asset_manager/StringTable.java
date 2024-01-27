package io.github.ngspace.nnuedit.asset_manager;

import static io.github.ngspace.nnuedit.asset_manager.AssetManager.strings;
import static java.lang.System.out;

import java.util.Map;

import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.utils.settings.ReadException;
import io.github.ngspace.nnuedit.utils.settings.Settings;
public interface StringTable {
	static class table extends Settings {
		public table(String type) {
			String lang = type;
			try {
				if (Utils.getAssetAsStream("Lang/Lang_"+lang+".properties")==null) {
					out.println("\"Lang_"+lang+".properties\" does not exist.");
					lang = "en";
				}
			} catch (Exception e) {}
			
			try {process(Utils.getAssetAsStream("Lang/Lang_"+lang+".properties"));
			} catch (ReadException e) {e.printStackTrace();}
		}
	}
	/**
	 * Load a language from <code>/Assets/Lang/</code>
	 * @param lang - the initals of the language to use
	 */
	public static void loadLang(String lang) {strings = new table(lang).getMap();}
	/**
	 * Load a language from a map
	 * @param map - the map to use as the language
	 */
	public static void loadLangMap(Map<? extends CharSequence, Object> map) {strings = map;}
	/**
	 * Returns the value from a HashMap of preloaded strings from the Lang.properties file
	 * @apiNote The current list of strings is very much lacking!
	 * 
	 * @param key
	 * @return the value from the Lang.properties file
	 * 
	 */
	public static String getRaw(String key) {
		if (strings==null) return key;
		Object obj = strings.get(key.trim());
		if (obj==null) return String.valueOf(key);
		return String.valueOf(obj);
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
	public static String get(String key) {
		return getRaw(key).replace("\\n", "\r\n");
	}
	/**
	 * 
	 * Returns getString(key) but replaces all extra arguments with corresponding arguements.
	 * 
	 * @param key
	 * @return the value from the Lang.properties file
	 * 
	 */
	public static String get(String key, Object... args) {
		char[] str = get(key).toCharArray();
		StringBuilder finalstr = new StringBuilder();
		boolean esc = false;
		for (int i = 0; i<str.length;i++) {
			char c = str[i];
			if (esc) {
				if (c=='%') {finalstr.append('%');continue;}
				try {
					if (c-48>args.length)
						finalstr.append("%"+c);
					finalstr.append(args[Utils.parseInt(c)]);
				} catch (Exception e) {return "Invalid string arg " + c;}
				esc = false;
				continue;
			}
			if (c=='%') {esc = true;continue;}
			finalstr.append(c);
		}
		return finalstr.toString().replace("\\n", "\r\n");
	}
}
