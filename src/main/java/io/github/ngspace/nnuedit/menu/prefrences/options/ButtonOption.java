package io.github.ngspace.nnuedit.menu.prefrences.options;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComponent;

import io.github.ngspace.nnuedit.asset_manager.StringTable;

public class ButtonOption extends WidgetOption {
	Consumer<JComponent> onclick;
	public ButtonOption(String name, String text, Consumer<JComponent> onclick) {super(name, text);this.onclick=onclick;}
	@Override public JComponent getWidget(int width) {
		JButton btn = new JButton(StringTable.get(key));
		btn.setSize(100, 45);
		btn.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {onclick.accept(btn);}
		});
		return btn;
	}
}