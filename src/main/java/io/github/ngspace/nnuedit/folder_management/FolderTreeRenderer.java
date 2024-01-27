package io.github.ngspace.nnuedit.folder_management;

import static io.github.ngspace.nnuedit.folder_management.FolderPanel.ROW_HEIGHT;

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

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.asset_manager.AssetManager;
import io.github.ngspace.nnuedit.utils.SmartGraphics2D;
import io.github.ngspace.nnuedit.utils.Utils;

public class FolderTreeRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = -467043583904458260L;
	
    Border border = BorderFactory.createLineBorder(Color.black);
    String[] exceptions = {".nnuproject"};

	public FolderTreeRenderer(FolderPanel folder) {}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
	    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    String file = value.toString();
	    String n = Utils.getFileName(file);
        this.setText(n);
        this.setToolTipText(n);
        this.setBackgroundSelectionColor(new Color(50,50,50));
        this.setForeground(new Color(164,164,164));
        
        
        if (!Utils.getFileName(file).startsWith(".")||contains(exceptions,Utils.getFileName(file)))
        	this.setIcon(AssetManager.getIconOfFile(new File(file)));
        else {
        	this.setIcon(Utils.getTransparency(.5f,AssetManager.getIconOfFile(new File(file),ROW_HEIGHT,ROW_HEIGHT)));
        	this.setForeground(getForeground().darker().darker().darker());
	        this.setBackgroundSelectionColor(getBackgroundSelectionColor().darker().darker());
        }return this;
	}
	
	@Override public void paintComponent(Graphics g) {
		Graphics2D gra = (Graphics2D) g;
		App.adjustAntialias(gra,true);
		
		Graphics2D gr = new SmartGraphics2D((Graphics2D) g,new Rectangle(100,100)) {
			@Override public void setRenderingHint(Key hintKey, Object hintValue) {
				if (hintKey!=RenderingHints.KEY_ANTIALIASING&&hintKey!=RenderingHints.KEY_TEXT_ANTIALIASING)
					super.setRenderingHint(hintKey, hintValue);
			}
		};
        ui.update(gr, this);
	}

	private boolean contains(Object[] arr, String fileName) {
		for(Object s : arr) if(fileName.equals(s)) return true;
		return false;
	}
}