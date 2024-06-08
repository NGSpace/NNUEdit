package io.github.ngspace.nnuedit.menu.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class CustomComboBoxRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = -2702235431578141710L;

	@Override public Component getListCellRendererComponent
		(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel lbl = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected,  cellHasFocus);
		lbl.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
		lbl.setBackground(new Color(10,10,12));
		return lbl;
	}
}