package io.github.ngspace.nnuedit.menu.prefrences.options;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.menu.components.SmartJLabel;

/**
 * There is no reason for this to be deprecated, I just don't want to update it to use the new WidgetOption class...
 * @deprecated
 */
@Deprecated(forRemoval = false, since = "1.4.3")
public class IntOption extends AOption {

	public IntOption(String name, String key) {super(name, key);}

	@Override
	public JComponent getComponentWidth(int width) {
		panel = new JPanel(null);
		panel.setOpaque(false);
		FocusAdapter listener = new FocusAdapter() {
			public void click(FocusEvent e) {
				Document d = ((JTextComponent)e.getComponent()).getDocument();
				try {
					int value = Integer.parseInt
							(d.getText(0, d.getLength()).replaceAll("[^\\d]*", ""));
					Main.settings.set(key, value);
				} catch (NumberFormatException | BadLocationException e1) {e1.printStackTrace();}
			}
			@Override public void focusLost(FocusEvent e) {click(e);}
		};
		SmartJLabel jlb = new SmartJLabel();
		jlb.setFont(f);
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(get(name));
		jlb.setSize(width/2 - TEXT_SPACING, 70);
		jlb.setLocation(0,0);
		jlb.setForeground(panel.getForeground());
		panel.add(jlb);
		JFormattedTextField jls = new JFormattedTextField(NumberFormat.getNumberInstance());
        jls.setBackground(new Color(10,10,12));
        
        try {
            jls.setValue(Main.settings.getInt(key));
		} catch (Exception e) {
            jls.setValue(0);
		}
        
        jls.addFocusListener(listener);
        jls.setOpaque(true);
        jls.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e){
            	if(e.getKeyCode() == KeyEvent.VK_ENTER) {panel.requestFocus();}
            }
		});
		jls.setSize(234, 41);
		panel.setSize(width, 70);
		jls.setLocation(width/2 + TEXT_SPACING * 2, (panel.getHeight()-jls.getHeight())/2);
		panel.add(jls);
		return panel;
	}
}