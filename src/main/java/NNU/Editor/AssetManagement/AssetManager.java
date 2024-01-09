package NNU.Editor.AssetManagement;

import static NNU.Editor.FolderManager.FolderPanel.IMAGESIZE;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.UIManager;

import NNU.Editor.Utils.Utils;

public class AssetManager implements StringTable {
	
	/* Moved this here because Java hates me and clean code */
	static Map<String, Object> strings = new StringTable.table("en").map;
	
	public static final Map<String, Icon> icons = new HashMap<String,Icon>();
	
	public static final String[] IconsNames = new String[] {
			 "ui/refresh", "ui/newfolder", "ui/folder", "ui/close", "ui/newfile", "ui/file",
			 "ui/shell", "ui/imageicon"
			 
			 , "cpp", "javascript", "java", "html", "css", "c", "typescript", "python", "xml",
	};
	
	static {
		icons.put("file",Utils.ResizeIcon(UIManager.getIcon("FileView.fileIcon"),IMAGESIZE,IMAGESIZE));
		for (String i : IconsNames) {
			String[] p = i.split("/");
			String name = p[p.length-1];
			icons.put(name, Utils.ResizeIcon(Utils.ReadImageIcon(i + ".png"),IMAGESIZE,IMAGESIZE));
		}
		icons.put("unix",icons.get("shell"));
		icons.put("bat",icons.get("shell"));
	}
	public static Icon getIcon(String name) {
		Icon c = icons.get(name);
		if (c==null) {
			icons.put(name, Utils.ReadImageIcon(name + ".png"));c=icons.get(name);
		}
		return c;
	}
	
}
