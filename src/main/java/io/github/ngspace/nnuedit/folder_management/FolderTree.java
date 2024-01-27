package io.github.ngspace.nnuedit.folder_management;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTree;

import io.github.ngspace.nnuedit.App;

public class FolderTree extends JTree {

	public FolderTree() {}
	private static final long serialVersionUID = -1909873281110302190L;
	int RoundedBox = 20;
	@Override public void paint(Graphics gra) {
		Graphics2D g = (Graphics2D) gra;
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		if (getModel()==null) {

    		g.setFont(getFont());
    		g.setColor(App.MenuFG);
    		
    		App.adjustAntialias(g, true);
    		int i = 0;
    		int bx = 100000;
    		int starty = 0;
    		int strheight = 0;
    		String string = get("folder.clickhere");
    		
    		String[] strs = string.split("\n");
    		
    		for (String str : strs) {
	    		int strwidth = g.getFontMetrics().stringWidth(str);
	    		int x = getWidth() / 2 - (strwidth / 2);
	    		strheight = (int) g.getFontMetrics().getLineMetrics(str, g).getHeight();
	    		starty = getHeight()/2 - ((strs.length*strheight)/2);
	    		int y = starty + i * strheight;
	    		g.drawString(str, x, y);
	    		i++;
	    		bx = x<bx?x:bx;
    		}starty-=strheight-5;
    		int w = getWidth()-bx*2+10;
    		int h = strheight*strs.length+10;
    		
    		g.setStroke(new BasicStroke(2));
    		g.drawRoundRect(bx-5, starty, w, h, RoundedBox, RoundedBox);
    		return;
		}
		super.paint(gra);
	}

}
