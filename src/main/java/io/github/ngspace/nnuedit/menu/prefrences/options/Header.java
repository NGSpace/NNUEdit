package io.github.ngspace.nnuedit.menu.prefrences.options;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import io.github.ngspace.nnuedit.menu.components.SmartJLabel;
import io.github.ngspace.nnuedit.menu.prefrences.PreferencesMenu;

public class Header extends AOption {
	public List<AOption> components = new ArrayList<AOption>();
	public int size;
	public boolean underline;
	public Header(String name) {super(name, "");size =10;underline = true;components.add(this);}
	@Override public JComponent getComponentWidth(int width) {
		panel = new JPanel(null); panel.setOpaque(false);
		SmartJLabel jlb = new SmartJLabel();
		jlb.setFont(new Font(FT, Font.BOLD, (int)(34f * (size/10f))));
		jlb.setText(get(name));
		jlb.setLocation(0,0);
		jlb.setForeground(PreferencesMenu.selectedColor);
		jlb.setUnderlineEnabled(underline); jlb.setOpaque(false);
		panel.add(jlb);panel.setSize(width, (int) (50 * (size/10f) + 30 * (size/10f)));
		jlb.setSize(width, panel.getHeight());
		return panel;
	}
	public void addPrefrence(AOption factory) {components.add(factory);}
	public List<AOption> getComponents() {return components;}
	public int getSize() {return size;}
	public boolean isUnderline() {return underline;}
	public void setComponents(List<AOption> components) {this.components = components;}
	public void setSize(int size) {this.size = size;}
	public void setUnderline(boolean underline) {this.underline = underline;}
}