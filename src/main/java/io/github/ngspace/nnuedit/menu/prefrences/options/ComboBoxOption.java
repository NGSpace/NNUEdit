package io.github.ngspace.nnuedit.menu.prefrences.options;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.menu.components.CustomComboBoxRenderer;
import io.github.ngspace.nnuedit.menu.components.SmartJLabel;

public class ComboBoxOption extends AOption {
	
	Map<String, Object> values = new HashMap<String,Object>();
	Vector<String> items = new Vector<String>();

	/**
	 * Create a combobox option
	 * @param name - the text displayed besides the combobox
	 * @param key - the key to be changed
	 * @param values2 - String: The name to be displayed. Object: the value of said name.
	 */
	public ComboBoxOption(String name, String key, Map<String, Object> values2) {super(name, key);
		for (String s : values2.keySet())
			items.add(s);
		this.values = values2;
	}

	@Override
	public JComponent getComponentWidth(int width) {
		panel = new JPanel(null);
		panel.setOpaque(false);
		SmartJLabel jlb = new SmartJLabel();
		jlb.setFont(f);
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(get(name));
		jlb.setForeground(panel.getForeground());
		jlb.setSize(width/2, 100);
		jlb.setLocation(width/2 - jlb.getWidth() - TEXT_SPACING, 0);
		panel.add(jlb);
        JComboBox<String> jls = new JComboBox<String>(items);
        
        jls.setRenderer(new CustomComboBoxRenderer());
        
        jls.setBackground(new Color(10,10,12));
        
        jls.setSelectedItem(getKey(Main.settings.get(key)));
        jls.addItemListener(e->{
        	//Make sure it runs only after the value has been selected!
        	if (e.getStateChange()!=1) return;
        	Main.settings.set(key, values.get(e.getItem()));
        	jls.getItemListeners()[0] = null;
        });
        jls.setOpaque(true);
		jls.setSize(234, 41);
		jls.setLocation(width/2,26);
		panel.add(jls);
		panel.setSize(width, 70);
		return panel;
	}
	
	protected String getKey(String value) {
		for(Entry<String, Object> entry: values.entrySet())
			if(entry.getValue().equals(value))
				return entry.getKey();
		return null;
	}

	@Override
	public AOption create(String name, String key) {
		return new ComboBoxOption(name, key, values);
	}
	public static AOption build(String name, String key, Map<String, Object> values) {
		return new ComboBoxOption(name, key, values);
	}

}
