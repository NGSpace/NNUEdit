package NNU.Editor;

import static NNU.Editor.Utils.EditorName;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class PreferencesMenu extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5027581198213070489L;
	
	protected JPanel contentpane = new JPanel() {
		public void paintComponent(Graphics g){
		    super.paintComponent(g);

		    g.setColor(App.MenuBG);
		    g.fillRect(0, 0, getWidth(), getHeight());
		}
	};
	public Settings stng;
	public App app;
	
	
	/**
	 * Creates and opens the prefrences menu
	 * @param stng the settings linked to the menu
	 * @param app the app linked to the menu
	 */
	public PreferencesMenu(Settings stng, App app) {
		this.stng = stng;
		this.app = app;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(683, 542));
		this.setLocationRelativeTo(app);
		this.setBackground(App.MenuBG);
		this.setForeground(App.MenuFG);
		this.setTitle(EditorName + "'s Prefrences");
		contentpane.setOpaque(true);
		contentpane.setBackground(App.MenuBG);
		contentpane.setForeground(App.MenuFG);
		contentpane.setLayout(null);
	    JScrollPane scrollPane = new JScrollPane(contentpane,
	    		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
	    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.setOpaque(true);
		getContentPane().add(scrollPane);
		refresh();
		this.setVisible(true);
	}
	
	public void refresh() {
		try {
			stng.refresh();
		} catch (Exception e) {e.printStackTrace();}
		contentpane.removeAll();
		try {
			InitFontSettings();
			InitSyntaxSettings();
		} catch (Exception e) {e.printStackTrace();}
		contentpane.revalidate();
		contentpane.repaint();
	}
	
	
	
	// FontSettings
	
	protected void InitFontSettings() {
		JLabel jlb = new JLabel();
		jlb.setFont(new Font("Tahoma", Font.BOLD, 26));
		jlb.setHorizontalAlignment(SwingConstants.CENTER);
		jlb.setText("Choose Font:");
		jlb.setSize(178, 93);
		jlb.setLocation(10,50);
		jlb.setForeground(App.MenuFG);
		contentpane.add(jlb);
		
		JLabel lblfont = new JLabel();
		Font f = app.getFont();
        String  strStyle;

        if (f.isBold()) {
            strStyle = f.isItalic() ? "bolditalic" : "bold";
        } else {
            strStyle = f.isItalic() ? "italic" : "plain";
        }
		lblfont.setText(f.getFamily() + " " + strStyle +  " " + f.getSize());
		lblfont.setHorizontalAlignment(SwingConstants.CENTER);
		lblfont.setForeground(App.MenuFG);
		lblfont.setFont(new Font("Monospaced", Font.BOLD, 18));
		lblfont.setBounds(205, 79, 252, 41);
		contentpane.add(lblfont);
		
		JButton btnNewButton = new JButton("Browse");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {try {
				ClickFontSettings();} catch (Exception e1) {e1.printStackTrace();}}});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(491, 79, 134, 41);
		btnNewButton.setBackground(App.MenuBG);
		btnNewButton.setOpaque(true);
		contentpane.add(btnNewButton);
	}
	
	protected void ClickFontSettings() throws ValueNotFoundException {
		JFontChooser jfc = new JFontChooser();
		jfc.setSelectedFont(app.getFont());
		jfc.showDialog(this);
		Font f = jfc.getSelectedFont();
		stng.set("fontsize", f.getSize());
		stng.set("fontfamily", f.getFamily());
		stng.set("fontstyle", f.getStyle());
		refresh();
	}
	
	
	
	// SyntaxSettings
	
	protected Map<String, String> values = new HashMap<String,String>();
	
	protected void InitSyntaxSettings() throws Exception {
		JLabel jlb = new JLabel();
		jlb.setFont(new Font("Tahoma", Font.BOLD, 24));
		jlb.setHorizontalAlignment(SwingConstants.CENTER);
		jlb.setText("Choose Langauge:");
		jlb.setSize(234, 93);
		jlb.setLocation(10,209);
		jlb.setForeground(App.MenuFG);
		contentpane.add(jlb);
		Field[] fields = SyntaxConstants.class.getDeclaredFields();
		for(Field f : fields){
			  f.setAccessible(true);
			  Object objectValue = "";
			  Object value = f.get(objectValue);
			  values.put(f.getName().replace("SYNTAX_STYLE_", "").toLowerCase().replace('_', ' '),
					  ((String) value).replace("text/", ""));
		}
	    Vector<String> items = new Vector<>();
        for (Map.Entry<String,String> entry : values.entrySet()) {
            items.add(entry.getKey());
			//  System.out.println(entry.getKey() + "    " + entry.getValue());
        }
        JComboBox<String> jls = new JComboBox<String>(items);
        // To change the arrow button's background        
        jls.setRenderer(new CustomComboBoxRenderer());
        jls.setUI(new BasicComboBoxUI() {
            @Override protected JButton createArrowButton()
            {
                return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, Color.GRAY, null);
            }
        });
        jls.setBackground(App.MenuBG);
        //jls.setForeground(app.MenuFG);
        jls.setSelectedItem(getKey(stng.get("syntax")));
        jls.addItemListener(this::ClickSyntaxSettings);
        jls.setOpaque(true);
		jls.setSize(234, 41);
		jls.setLocation(391,236);
		contentpane.add(jls);
	}
	
	protected void ClickSyntaxSettings(ItemEvent e) {
		stng.set("syntax", values.get(e.getItem()));
		refresh();
	}
	
	protected String getKey(String value) {
		for(Entry<String, String> entry: values.entrySet()) {
			// if give value is equal to value from entry
			// print the corresponding key
			if(entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
class CustomComboBoxRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent
		(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel lbl = (JLabel)super.getListCellRendererComponent
				(list, value, index, isSelected,  cellHasFocus);
		lbl.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
		lbl.setBackground(App.MenuBG);
		return lbl;
	}
}
