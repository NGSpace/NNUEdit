package NNU.Editor.Menus.Components;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JButton;

import NNU.Editor.Utils.Utils;

public class IconButton extends JButton {
	
	private static final long serialVersionUID = 7446379822311811240L;
	
	protected Image image;
	
	public IconButton(Icon icon) {
		super(icon);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setBorderPainted(false);
	}
	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon);
		image = Utils.iconToImage(icon);
	}
	@Override
	public void paintComponent(Graphics gra) {
		Graphics2D g = (Graphics2D) gra;
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setIcon(getIcon());
	}

}
