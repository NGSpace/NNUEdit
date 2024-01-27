package io.github.ngspace.nnuedit.utils.registry;

import static io.github.ngspace.nnuedit.menu.EditorTextArea.getSyntaxList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.asset_manager.extensions.ExtensionManager;
import io.github.ngspace.nnuedit.menu.prefrences.options.BooleanOption;
import io.github.ngspace.nnuedit.menu.prefrences.options.ComboBoxOption;
import io.github.ngspace.nnuedit.menu.prefrences.options.FontOption;
import io.github.ngspace.nnuedit.menu.prefrences.options.Header;
import io.github.ngspace.nnuedit.menu.prefrences.options.HeadlessHeader;
import io.github.ngspace.nnuedit.menu.prefrences.tabs.APreferenceTab;
import io.github.ngspace.nnuedit.menu.prefrences.tabs.ExtensionsTab;
import io.github.ngspace.nnuedit.menu.prefrences.tabs.PreferenceTab;
import io.github.ngspace.nnuedit.runner.HTMLRunner;
import io.github.ngspace.nnuedit.runner.IRunner;
import io.github.ngspace.nnuedit.runner.ProjectRunner;
import io.github.ngspace.nnuedit.runner.PythonRunner;
import io.github.ngspace.nnuedit.runner.ShellRunner;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.window.abstractions.Editor;
import io.github.ngspace.nnuedit.window.windowfactorys.DefaultEditorWindowFactory;
import io.github.ngspace.nnuedit.window.windowfactorys.IWindowFactory;
import io.github.ngspace.nnuedit.window.windowfactorys.ImageWindowFactory;
import io.github.ngspace.nnuedit.window.windowfactorys.PropertiesWindowFactory;

public class Registries {
	public static HashMap<String, IWindowFactory> WindowFactories = new HashMap<String, IWindowFactory>(); static {
		WindowFactories.put(Editor.DEFAULT, new DefaultEditorWindowFactory());
		ImageWindowFactory imgw = new ImageWindowFactory();
		for (String s : Utils.imagetypes)
			WindowFactories.put(s, imgw);
		PropertiesWindowFactory propw = new PropertiesWindowFactory();
		WindowFactories.put("properties", propw);
		WindowFactories.put("prop", propw);
	}
	public static Map<String, IRunner> Runners = new HashMap<String,IRunner>(); static {
		Runners.put(App.PROJ, new ProjectRunner());
		Runners.put("python", new PythonRunner());
		Runners.put("shell", new ShellRunner());
		Runners.put("html", new HTMLRunner());
	}
	public static List<APreferenceTab> PreferencesTabs = new ArrayList<APreferenceTab>(); static {FixPrefTabs();}
	public static void FixPrefTabs() {
		PreferencesTabs.clear();
		//Appearance
		Header appearence = new HeadlessHeader("options.header.appearence");
		appearence.addPrefrence(FontOption.build("options.tab.font","tab.font"));
		appearence.addPrefrence(BooleanOption.build("options.antialias","editor.antialias"));
		appearence.addPrefrence(BooleanOption.build("options.preview","folder.imgpreview"));
		
		// Editor Appearance
		appearence.addPrefrence(new Header("options.header.editor.appearence"));
		appearence.addPrefrence(FontOption.build("options.editor.font","editor.font"));
		/* (hopefully) All the bugs got fixed so might as well just remove it. */
//		appearence.addPrefrence(BooleanPreference.build("options.editor.numbers","editor.numberlines"));
		appearence.addPrefrence(ComboBoxOption.build("options.editor.language","editor.syntax",getSyntaxList()));
		
		//Properties Appearance
//		appearence.addPrefrence(new Header("options.header.other.propeditor.appearence"));
		appearence.addPrefrence(FontOption.build("options.prop.font","editor.prop.font"));
//		appearence.addPrefrence(IntOption.build("options.prop.width","editor.prop.tablewidth"));
		
		//System
		Header system = new HeadlessHeader("options.header.system");
		system.addPrefrence(BooleanOption.build("options.system.startup", "system.checkversion"));
		system.addPrefrence(ComboBoxOption.build("options.system.lang", "system.language",Utils.getLangMap()));
		
		if (ExtensionManager.Extensions.size()>0)
		PreferencesTabs.add(new ExtensionsTab());
		PreferencesTabs.add(new PreferenceTab(system));
		PreferencesTabs.add(new PreferenceTab(appearence));
	}
}