package io.github.ngspace.nnuedit.menu.prefrences.options;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.menu.components.CustomComboBoxRenderer;

public class ComboBoxOption extends WidgetOption {
	Map<String, Object> values = new HashMap<String,Object>();
	Set<String> items = null;
	/**
	 * Create a combobox option
	 * @param name - the text displayed besides the combobox
	 * @param key - the key to be changed
	 * @param values2 - String: The name to be displayed. Object: the value of said name.
	 */
	public ComboBoxOption(String name, String key, Map<String, Object> values) {
		super(name, key);
		items = values.keySet();
		this.values = values;
	}
	
	protected String getKey(String value) {
		for(Entry<String, Object> entry: values.entrySet()) if(entry.getValue().equals(value)) return entry.getKey();
		return null;
	}

	@Override public JComponent getWidget(int width) {
        JComboBox<String> jls = new JComboBox<String>(items.toArray(new String[0]));
        jls.setRenderer(new CustomComboBoxRenderer());
        jls.setBackground(new Color(10,10,12));
        jls.setSelectedItem(getKey(Main.settings.get(key)));
        jls.addItemListener(e -> {
        	if (e.getStateChange()!=1) return;
        	Main.settings.set(key, values.get(e.getItem()));
        	jls.getItemListeners()[0] = null;
        });
        jls.setOpaque(true);
		jls.setSize(234, 41);
		jls.setLocation(width/2,26);
		return jls;
	}

}
