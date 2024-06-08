package io.github.ngspace.nnuedit.menu.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import io.github.ngspace.nnuedit.Main;

public class PropertiesRenderer extends SmartJLabel implements TableCellRenderer {
    
	private static final long serialVersionUID = -7404205703146143870L;

    public static final int STROKE_WIDTH = 3;
	Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;

    public PropertiesRenderer() {super();}

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                            int row, int column) {
        setText(String.valueOf(value));
        setFont(Main.settings.getFont("editor.prop.font"));
        setBorder(new EmptyBorder(0, 10, 0, 0) {
			private static final long serialVersionUID = 155872278271851179L;

			@Override
        	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        		((Graphics2D) g).setStroke(new BasicStroke(STROKE_WIDTH));
        		g.setColor(Color.black);
        		g.drawLine(0, height, width, height);
        		g.drawLine(1, 0, 1, height);
        	}
        });
        
        setHorizontalAlignment(isBordered ? SwingConstants.LEFT : SwingConstants.CENTER);
        setBackground(isSelected ? (new Color(80,80,80)) : UIManager.getColor("Panel.background").darker());
        return this;
    }
}