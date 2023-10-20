package NNU.Editor.Menus.Components;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JButton;

import NNU.Editor.Utils.Utils;

public class IconButton extends JButton {
	
	
	private static final long serialVersionUID = 7446379822311811240L;
	
	protected BufferedImage image;
	public boolean plus = false;
	public boolean borderleft = false;
	public boolean bordertop = false;
	public boolean borderbottom = false;
	public int bordersize = 1;

	public IconButton(Icon icon) {
		this(icon,true);
	}
	public IconButton(Icon icon,boolean plus) {
		super(icon);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.plus = plus;
		if (icon==null) {
			System.out.println(icon);
		}
	}
	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon);
		image = Utils.toBufferedImage(Utils.iconToImage(icon));
	}
	@Override
	public void paint(Graphics gra) {
		Graphics2D g = (Graphics2D) gra;
		g.drawImage(image, 0, 0, null);
		g.setStroke(new BasicStroke(bordersize));
	}
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		image = Utils.toBufferedImage(Utils.ResizeImage(image,width,height));
	}

}
