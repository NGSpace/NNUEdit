package io.github.ngspace.nnuedit.menu.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JCheckBox;

/**
 * https://github.com/DJ-Raven/raven-project/tree/main
 */
public class JCheckBoxCustom extends JCheckBox {

	private static final long serialVersionUID = 1L;
	private static final int BORDER = 4;

    public JCheckBoxCustom() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);
        setBackground(new Color(69, 124, 235));
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int ly = (getHeight() - 16) / 2;
        if (isSelected()) {
            if (isEnabled()) {
                g2.setColor(Color.white);
            } else {
                g2.setColor(Color.GRAY);
            }
            g2.fillRoundRect(1, ly, 16, 16, BORDER, BORDER);
            //  Draw Check icon
            int[] px = {4, 8, 14, 12, 8, 6};
            int[] py = {ly + 8, ly + 14, ly + 5, ly + 3, ly + 10, ly + 6};
            g2.setColor(Color.RED);
            g2.fillPolygon(px, py, px.length);
        } else {
            g2.setColor(Color.GRAY);
            g2.fillRoundRect(1, ly, 16, 16, BORDER, BORDER);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(2, ly + 1, 14, 14, BORDER, BORDER);
        }
        g2.dispose();
    }
}