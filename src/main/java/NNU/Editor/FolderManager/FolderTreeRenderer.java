package NNU.Editor.FolderManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeCellRenderer;

import NNU.Editor.App;
import NNU.Editor.Utils.SmartGraphics2D;
import NNU.Editor.Utils.Utils;

public class FolderTreeRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = -467043583904458260L;
	
	//private static final String SPAN_FORMAT = "<span style='color:%s;'>%s</span>";
    Border border = BorderFactory.createLineBorder(Color.black);
    String[] exceptions = {".nnuproject"};

	private FolderPanel folder;

	public FolderTreeRenderer(FolderPanel folder) {this.folder=folder;}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
	    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    //this.setBorder(border);
	    //this.setBorder(new Utils.CustomBorder(PADDING, PADDING, PADDING, PADDING));
	    String file = value.toString();
	    String n = Utils.getFileName(file);
        this.setText(n);
        this.setToolTipText(n);
        this.setBackgroundSelectionColor(new Color(50,50,50));
        this.setForeground(new Color(164,164,164));
        
        
        if (!Utils.getFileName(file).startsWith(".")||contains(exceptions,Utils.getFileName(file)))
        	this.setIcon(folder.getIcon1(new File(file)));
        else {
        	this.setIcon(Utils.getTransparency(0.5f,folder.getIcon1(new File(file))));
        	this.setForeground(getForeground().darker().darker().darker());
	        this.setBackgroundSelectionColor(getBackgroundSelectionColor().darker().darker());
        }
        return this;
	}
	
	@Override public void paintComponent(Graphics g) {
		Graphics2D gra = (Graphics2D) g;
		App.adjustAntialias(gra,true);
		//gra.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, gra);
		
		Graphics2D gr = new SmartGraphics2D((Graphics2D) g,new Rectangle(100,100)) {
			@Override public void setRenderingHint(Key hintKey, Object hintValue) {
				if (hintKey!=RenderingHints.KEY_ANTIALIASING&&hintKey!=RenderingHints.KEY_TEXT_ANTIALIASING) {
					super.setRenderingHint(hintKey, hintValue);
				}
			}
		};
        ui.update(gr, this);
	}

	private boolean contains(Object[] arr, String fileName) {
		for (Object s : arr)
			if (fileName.equals(s))
				return true;
		return false;
	}
}