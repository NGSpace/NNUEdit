package io.github.ngspace.nnuedit.menu.prefrences.options;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import io.github.ngspace.nnuedit.asset_manager.StringTable;
import io.github.ngspace.nnuedit.menu.components.SmartJLabel;

public abstract class WidgetOption extends AOption {
	protected WidgetOption(String name, String key) {super(name, key);}
	@Override public JComponent getComponentWidth(int width) {
		panel = new JPanel(null);
		panel.setOpaque(false);
		panel.setSize(width, 45);
		SmartJLabel jlb = new SmartJLabel(StringTable.get(name));
		jlb.setFont(f);
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setBounds(0,0,width/2 - TEXT_SPACING, panel.getHeight());
		jlb.setForeground(panel.getForeground());
		panel.add(jlb);
		JComponent widget = getWidget(width);
		widget.setLocation(width/2 + TEXT_SPACING, (45-widget.getHeight())/2);
		panel.add(widget);
		return panel;
	}
	public abstract JComponent getWidget(int width);
}