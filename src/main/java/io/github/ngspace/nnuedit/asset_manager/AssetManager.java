package io.github.ngspace.nnuedit.asset_manager;

import static io.github.ngspace.nnuedit.folder_management.FolderPanel.ROW_HEIGHT;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.utils.settings.Settings;

public class AssetManager implements StringTable {
	
	/* Moved this here because Java hates me and cleaner code */
	static Map<? extends CharSequence, Object> strings = new StringTable.table("en").getMap();
	private static Map<String, Icon> icons = new HashMap<String,Icon>();
	private static HashMap<String, ImageIcon> imgs = new HashMap<String, ImageIcon>();
	public static Map<String, Object> fileext = new Settings(Utils.getAssetAsStream("FileExt.properties")).getMap();
	
	static {AddDefaultIcons();}
	
	public static void AddDefaultIcons() {
		String[] IconsNames = new String[] {
				 "ui/refresh", "ui/newfolder", "ui/folder", "ui/close", "ui/newfile", "ui/file",
				 "ui/shell", "ui/imageicon", "ui/NNUEdit72x72"
				 
				 , "cpp", "javascript", "java", "html", "css", "c", "typescript", "python", "xml",
		};
		
		icons.put("file",Utils.ResizeIcon(UIManager.getIcon("FileView.fileIcon"),ROW_HEIGHT,ROW_HEIGHT));
		for (String i : IconsNames) {
			String[] p = i.split("/");
			String name = p[p.length-1];
			icons.put(name, Utils.ResizeIcon(Utils.ReadImageIcon(i + ".png"),ROW_HEIGHT,ROW_HEIGHT));
		}
		icons.put("unix",icons.get("shell"));
		icons.put("bat",icons.get("shell"));
	}
	public static void putIcon(String name, Icon icon) {
		icons.put(name, icon);
		dispatchAssetLoaded(assetLoadedListeners,icons,icons.get(name));
	}
	public static Icon getIcon(String name) {
		Icon c = icons.get(name);
		if (c==null) {
			putIcon(name, Utils.ReadImageIcon(name + ".png"));c=icons.get(name);
		}
		return c;
	}
	public static Icon getIcon(String name, int width, int height) {
		return Utils.ResizeIcon(getIcon(name),width,height);
	}
	public static Icon getIconOfFile(File f, int width, int height) {
		return Utils.ResizeIcon(getIconOfFile(f),width,height);
	}
	public static Icon getIconOfFile(File f) {
		if (f.isDirectory())
			return AssetManager.getIcon("folder");
        String[] arr = f.getAbsolutePath().split("[.]");
        String ext = arr[arr.length-1];
		Icon ic;
		if ("img".equals(Utils.getFileType(f.getPath()))) {
			if (Main.settings.getBoolean("folder.imgpreview"))
				try {
					ImageIcon i = imgs.get(f.getAbsolutePath());
					if (i==null) {
						imgs.put(f.getAbsolutePath(), new ImageIcon
								(Utils.ResizeImage(Utils.readImageFromFile(f),ROW_HEIGHT,ROW_HEIGHT)));
						i = imgs.get(f.getAbsolutePath());
						dispatchAssetLoaded(assetLoadedListeners,imgs,i);
					}
					return i;
				} catch (Exception e) {e.printStackTrace();}
			return AssetManager.getIcon("imageicon");
		}
		if ((ic = AssetManager.getIcon(String.valueOf(fileext.get(ext))))==Utils.getMissingIcon()) {
			return AssetManager.getIcon("file");
		}
		return ic;
	}
	public static void clearImageCache() {
		imgs.clear();
		dispatchClearCache(imageListeners);
	}
	public static void clearIconsCache() {
		icons.clear();
		dispatchClearCache(iconsListeners);
	}
	public static List<ClearCacheListener> iconsListeners = new ArrayList<ClearCacheListener>();
	public static List<ClearCacheListener> imageListeners = new ArrayList<ClearCacheListener>();
	public static List<AssetLoadedListener> assetLoadedListeners = new ArrayList<AssetLoadedListener>();
	public static boolean addImageCacheListener(ClearCacheListener e) {
		return imageListeners.add(e);
	}
	public static boolean addIconsCacheListener(ClearCacheListener e) {
		return iconsListeners.add(e);
	}
	public static boolean addAssetLoadedListener(AssetLoadedListener e) {
		return assetLoadedListeners.add(e);
	}
	public static Map<String, Icon> getIcons() {
		return icons;
	}
	public static void setIcons(Map<String, Icon> icons) {
		AssetManager.icons = icons;
	}
	public static HashMap<String, ImageIcon> getImgs() {
		return imgs;
	}
	public static void setImgs(HashMap<String, ImageIcon> imgs) {
		AssetManager.imgs = imgs;
	}
	private static void dispatchClearCache(List<ClearCacheListener> ls) {
		for (ClearCacheListener ccl : ls)
			ccl.clearCache();
	}
	public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
	    try {
	        return clazz.cast(o);
	    } catch(ClassCastException e) {
	        return null;
	    }
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void dispatchAssetLoaded(List<AssetLoadedListener> ls, Map cache, Object asset) {
		for (AssetLoadedListener ccl : ls)
			ccl.LoadedAsset(cache, asset);
	}
}
