package io.github.ngspace.nnuedit.menu.prefrences.options;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.awt.Color;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.menu.components.SmartJLabel;

public class BooleanOption extends AOption {
	public BooleanOption(String name, String key) {super(name, key);}
	@Override public JComponent getComponentWidth(int width) {
		panel = new JPanel(null); JCheckBox jls = new JCheckBox();
		ItemListener listener = e -> Main.settings.set(key, jls.isSelected());
		panel.setOpaque(false); panel.setSize(width, 45);
		SmartJLabel jlb = new SmartJLabel();
		jlb.setFont(f); jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(get(name)); jlb.setSize(width/2 - TEXT_SPACING, panel.getHeight());
		jlb.setLocation(0,0);
		jlb.setForeground(panel.getForeground());
		panel.add(jlb);
        jls.setBackground(new Color(10,10,12));
        jls.setSelected(Boolean.valueOf(Main.settings.get(key).toLowerCase()));
        jls.addItemListener(listener);
		jls.setSize(width/2, panel.getHeight());
		jls.setLocation(width/2, 10);
		jls.setOpaque(false);
		jls.setText(jls.isSelected()?get("options.enabled"):get("options.disabled"));
		jls.setFont(f);
		jls.setVerticalAlignment(SwingConstants.TOP);
		panel.add(jls);
		return panel;
	} @Override public AOption create(String name, String key) {return new BooleanOption(name, key);}
	public static AOption build(String name, String key) {return new BooleanOption(name, key);}
}