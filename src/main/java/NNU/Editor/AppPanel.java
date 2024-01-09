package NNU.Editor;

import static NNU.Editor.AssetManagement.StringTable.getStringRaw;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class AppPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 890330040427900164L;
	
	public static int RoundedBox = 20;
	
	private boolean painting = false;
	
	public final App app;
	
	public AppPanel(App app) {
		this.app = app;
	}
	
	@Override public void paint(Graphics gra) {
		super.paint(gra);
		if (painting) return;
		painting = true;
		if (app.getSelectedWindow()==null||!app.getSelectedWindow().getScrollPane().isVisible()) {
    		Graphics2D g = (Graphics2D) gra;
    		g.setFont(app.getTipFont());
    		g.setColor(App.MenuFG);
    		
    		App.adjustAntialias(g,true);
    		
    		String string = getStringRaw("app.clickhere");
    		String[] strs = string.split("%",2);
    		
    		if (strs.length!=2) strs = new String[] {"STR "," MISSING"};
    		
    		string = strs[0] + "CTRL + O" + strs[1];
    		int keywidth = g.getFontMetrics().stringWidth(strs[0]);
    		int ctrlwidth = g.getFontMetrics().stringWidth("CTRL + 0 ");
    		int totalwidth = g.getFontMetrics().stringWidth(string);

    		int x = getWidth() / 2 - (totalwidth / 2) + 
    				(app.isFolderOpen() ? app.Folder.getWidth() : 0)/2;
    		
    		int y = ((getHeight() - g.getFontMetrics().getHeight()) / 2)
    				+ g.getFontMetrics().getAscent();
    		int strheight = (int) g.getFontMetrics().getLineMetrics(string, g).getHeight();
    		g.drawString(string, x, y);
    		
    		g.drawRoundRect(x+keywidth-5, y-strheight+5, ctrlwidth+10, strheight+5, RoundedBox, RoundedBox);
    		/*String key = "CTRL + O";
    		String str = "Open a file with " + key;
    		int keywidth = g.getFontMetrics().stringWidth(key);
    		int strwidth = g.getFontMetrics().stringWidth(str);
    		int x = getWidth() / 2 - (strwidth / 2) + 
    				(app.isFolderOpen() ? app.Folder.getWidth() : 0)/2;
    		
    		int y = ((getHeight() - g.getFontMetrics().getHeight()) / 2)
    				+ g.getFontMetrics().getAscent();
    		int h = g.getFontMetrics().getHeight() / 2 + g.getFontMetrics().getAscent();
    		g.drawString(str, x, y);
    		g.drawString(click, x, y+h);
    		g.setStroke(new BasicStroke(2));
    		g.drawRoundRect(x+strwidth-keywidth, y-40, keywidth, 50, RoundedBox, RoundedBox);*/
    		
		}painting = false;
	}
}
