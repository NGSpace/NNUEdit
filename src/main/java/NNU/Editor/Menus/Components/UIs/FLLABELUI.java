package NNU.Editor.Menus.Components.UIs;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.formdev.flatlaf.ui.FlatLabelUI;

public class FLLABELUI extends FlatLabelUI {

	public FLLABELUI(boolean shared) {
		super(shared);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void paint( Graphics g, JComponent c ) {

        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        icon.paintIcon(c, g, 0, 0);
        
        paintEnabledText(label, g, text, icon.getIconWidth(), 0);
	}

}
