package io.github.ngspace.nnuedit.asset_manager;

import static io.github.ngspace.nnuedit.folder_management.FolderPanel.ROW_HEIGHT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.utils.FileIO;
import io.github.ngspace.nnuedit.utils.ImageUtils;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.utils.settings.Settings;

public class AssetManager {private AssetManager() {}
	
	/* Moved this here because Java hates me and cleaner code */
	static Map<? extends CharSequence, Object> strings = new StringTable.StringTableMap("en").getMap();
	public static Map<String, Icon> icons = new HashMap<String,Icon>();
	public static Map<String, ImageIcon> imgs = new HashMap<String, ImageIcon>();
	public static Map<String, Object> fileext = new Settings(Utils.getAssetAsStream("FileExt.properties")).getMap();
	
	
	
	public static void addDefaultIcons() throws IOException {
		
		/**
		 * Some time ago I was fucking around on a different project and
		 * I found out you can read how many files a package has by reading the folder
		 * so you don't have to hardcode everyfile.
		 */
		
		String[] IconNames = Utils.concatArrays(Utils.getResourcesName("io.github.ngspace.nnuedit.Assets.Icons"),
				Utils.getResourcesName("io.github.ngspace.nnuedit.Assets.Icons.ui", "ui/"));
	    
		for (String i : IconNames) {
			String[] p = i.split("/");
			String name = p[p.length-1];
			icons.put(name, ImageUtils.resizeIcon(ImageUtils.readIconAsset(i + ".png"),ROW_HEIGHT,ROW_HEIGHT));
		}
		
		if (icons.get("file")==null)
			icons.put("file",ImageUtils.resizeIcon(UIManager.getIcon("FileView.fileIcon"),ROW_HEIGHT,ROW_HEIGHT));
		
		if (icons.get("folder")==null)
			icons.put
				("folder",ImageUtils.resizeIcon(UIManager.getIcon("FileView.directoryIcon"),ROW_HEIGHT,ROW_HEIGHT));
		
		icons.put("unix",icons.get("shell"));
		icons.put("bat",icons.get("shell"));
	}
	
	
	
	public static void putIcon(String name, Icon icon) {
		icons.put(name, icon);
		dispatchAssetLoaded(assetLoadedListeners,icons,icons.get(name));
	}
	
	
	
	public static Icon getIcon(String name) {
		Icon c = icons.get(name);
		if (c==null) putIcon(name, ImageUtils.readIconAsset(name + ".png"));
		return icons.get(name);
	}
	
	public static Icon getIcon(String name, int width, int height) {
		return ImageUtils.resizeIcon(getIcon(name),width,height);
	}
	
	
	public static Icon getIconOfFile(File f, int width, int height) {
		return ImageUtils.resizeIcon(getIconOfFile(f),width,height);
	}
	
	public static Icon getIconOfFile(File f) {
		if (f.isDirectory()) return AssetManager.getIcon("folder");
		
        String[] arr = f.getAbsolutePath().split("[.]");
        String ext = arr[arr.length-1];
		Icon ic;
		if ("img".equals(FileIO.getFileType(f.getPath()))) {
			if (Main.settings.getBoolean("folder.imgpreview"))
				try {
					ImageIcon i = imgs.get(f.getAbsolutePath());
					if (i==null) {
						imgs.put(f.getAbsolutePath(), new ImageIcon
								(ImageUtils.resizeImage(ImageUtils.readImageFromFile(f),ROW_HEIGHT,ROW_HEIGHT)));
						i = imgs.get(f.getAbsolutePath());
						dispatchAssetLoaded(assetLoadedListeners,imgs,i);
					}
					return i;
				} catch (Exception e) {e.printStackTrace();}
			return AssetManager.getIcon("imageicon");
		}
		if ((ic = AssetManager.getIcon(String.valueOf(fileext.get(ext))))==ImageUtils.getMissingIcon())
			return AssetManager.getIcon("file");
		return ic;
	}
	
	
	
	public static void clearImageCache() {imgs.clear();dispatchClearCache(imageListeners);}
	public static void clearIconsCache() {icons.clear();dispatchClearCache(iconsListeners);}
	
	
	
	public static List<ClearCacheListener> iconsListeners = new ArrayList<ClearCacheListener>();
	public static List<ClearCacheListener> imageListeners = new ArrayList<ClearCacheListener>();
	public static List<AssetLoadedListener> assetLoadedListeners = new ArrayList<AssetLoadedListener>();
	
	public static boolean addImageCacheListener(ClearCacheListener e) {return imageListeners.add(e);}
	public static boolean addIconsCacheListener(ClearCacheListener e) {return iconsListeners.add(e);}
	public static boolean addAssetLoadedListener(AssetLoadedListener e) {return assetLoadedListeners.add(e);}
	
	private static void dispatchClearCache(List<ClearCacheListener> ls) {
		for (ClearCacheListener ccl : ls)
			ccl.clearCache();
	}
	private static void dispatchAssetLoaded(List<AssetLoadedListener> ls, Map<?,?> cache, Object asset) {
		for (AssetLoadedListener ccl : ls)
			ccl.loadedAsset(cache, asset);
	}
}