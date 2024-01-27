package io.github.ngspace.nnuedit.menu.prefrences.options;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.menu.JFontChooser;
import io.github.ngspace.nnuedit.menu.components.SmartJLabel;

public class FontOption extends AOption {

	public FontOption(String name, String key) {
		super(name, key);
	}

	@Override
	public JComponent getComponentWidth(int width) {
		panel = new JPanel(null);
		panel.setOpaque(false);
		
		SmartJLabel jlb = new SmartJLabel();
		
		SmartJLabel lblfont = new SmartJLabel();
		lblfont.setSize(252, TEXT_SPACING);
		lblfont.setLocation(width/2- lblfont.getWidth()/2, TEXT_SPACING*2);
		
		Font font = Main.settings.getFont(key);
        String  strStyle;

        if (font.isBold()) {
            strStyle = font.isItalic() ? "bolditalic" : "bold";
        } else {
            strStyle = font.isItalic() ? "italic" : "plain";
        }
		lblfont.setText(font.getFamily() + " " + strStyle +  " " + font.getSize());
		lblfont.setHorizontalAlignment(SwingConstants.CENTER);
		lblfont.setForeground(panel.getForeground());
		lblfont.setFont(new Font("Monospaced", Font.BOLD, 18));
		
		jlb.setFont(f);
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(get(name));
		jlb.setForeground(panel.getForeground());
		
		jlb.setSize(lblfont.getX() - (TEXT_SPACING/2-10), 100);
		jlb.setLocation(0,TEXT_SPACING-20);
		
		JButton btnNewButton = new JButton(get("options.browse"));
		btnNewButton.setSize(104, 55);
		btnNewButton.setLocation(width/2 + lblfont.getWidth()/2 + TEXT_SPACING, TEXT_SPACING);
		btnNewButton.setForeground(panel.getForeground());
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				try {
					JFontChooser jfc = new JFontChooser();
					jfc.setSelectedFont(font);
					jfc.showDialog(null);
					Font f = jfc.getSelectedFont();
					Main.settings.set(key, f);
				} catch (Exception e1) {e1.printStackTrace();}
			}
		});
		btnNewButton.setFont(new Font(FT, Font.BOLD, 14));
		btnNewButton.setBackground(new Color(10,10,12));
		btnNewButton.setOpaque(true);
		
		panel.setSize(width, 70);
		panel.add(jlb);
		panel.add(lblfont);
		panel.add(btnNewButton);
		return panel;
	}

	@Override
	public AOption create(String name, String key) {
		return new FontOption(name, key);
	}
	public static AOption build(String name, String key) {
		return new FontOption(name, key);
	}

}
