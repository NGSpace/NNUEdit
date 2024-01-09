package NNU.Editor.Menus.Components.UIs;


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

import NNU.Editor.Settings;
import NNU.Editor.Menus.Components.SmartJLabel;
import NNU.Editor.Utils.Utils;

public class ColorRenderer extends SmartJLabel implements TableCellRenderer {
    
	private static final long serialVersionUID = -7404205703146143870L;
	
	Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;
    
    public static final int strokeWidth = 3;

    public ColorRenderer(Settings stng, boolean isBordered) {
    	super(stng);
    	this.isBordered = isBordered;
        //setOpaque(true); //MUST do this for background to show up. // No we don't, shut up stoopid.
    }

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
        setText(Utils.valueOf(value));
        setFont(set.getFont("editor.prop.font"));
        setBorder(new EmptyBorder(0, 0, 0, 0) {
			private static final long serialVersionUID = 155872278271851179L;

			@Override
        	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        		((Graphics2D) g).setStroke(new BasicStroke(strokeWidth));
        		g.setColor(Color.black);
        		g.drawLine(0, height, width, height);
        		g.drawLine(1, 0, 1, height);
        	}
        });
        setHorizontalAlignment(isBordered ? SwingConstants.LEFT : SwingConstants.CENTER);
        setBackground(isSelected ? (new Color(80,80,80)) : UIManager.getColor("Panel.background").darker());
        return this;
    }
    
    public boolean contains(int[] l, int v) {
        for (int i : l)
            if (i == v)
                return true;
        return false;
    }
}