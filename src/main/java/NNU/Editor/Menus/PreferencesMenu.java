package NNU.Editor.Menus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import NNU.Editor.App;
import NNU.Editor.Settings;
import NNU.Editor.Menus.Components.FolderButton;
import NNU.Editor.Utils.ValueNotFoundException;
import NNU.Editor.Windows.Window;

public class PreferencesMenu extends JPanel implements FolderButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5027581198213070489L;
	
	public static final String FT = "Tahoma";
	public static final int TEXT_SPACING = 15;
	public static final int VARIABLE_SPACING = 60;
	public final Settings stng;
	public App app;
	public Window window;
	public int Y = 0;
	
	@Override public Font getFont() {return new Font(FT, Font.BOLD, 24);}
	
	/**
	 * Creates and opens the prefrences menu
	 * @param stng the settings linked to the menu
	 * @param app the app linked to the menu
	 */
	public PreferencesMenu(Settings stng, App app, Window window) {
		this.stng = stng;
		this.app = app;
		this.window = window;
		this.setSize(new Dimension(683, 542));
		setOpaque(true);
		setBackground(new Color(10,10,12));
		setForeground(Color.white.darker());

        setBorder(new EmptyBorder(0,getBuffer(),0,0));
		setLayout(null);
		
		initjl(this,window.getScrollPane());
		
		refresh();
		app.repaint();
	}
	
	public void refresh() {
		try {
			stng.refresh();
		} catch (Exception e) {e.printStackTrace();}
		removeAll();
		Y = 40;
		try {
			InitFontSettings();
			InitSyntaxSettings();
			AddBooleanValue("Text Anti Alias","textantialias");
			AddBooleanValue("Numbered Lines (May cause lag)","numberlines");
			//AddBooleanValue("Auto-Pause Shell after run","shellpause");
			AddIntValue("Tab Font Size", "tabfontsize");
		} catch (Exception e) {e.printStackTrace();}
		add(jl);
		revalidate();
		app.repaint();
	}
	
	public void AddBooleanValue(String name, String key) {
		try {
			AddBooleanCheckbox(name,key, e -> {
				boolean val = ((JCheckBox)e.getSource()).isSelected();
				stng.set(key, val);
				refresh();
			});
		} catch (ValueNotFoundException e) {e.printStackTrace();}
	}
	
	public void AddBooleanCheckbox(String name, String key, ItemListener listener) throws ValueNotFoundException {
		JLabel jlb = new JLabel();
		jlb.setFont(getFont());
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(name);
		jlb.setSize(getWidth()/2 - TEXT_SPACING, 100);
		jlb.setLocation(0,Y);
		jlb.setForeground(getForeground());
		add(jlb);
        JCheckBox jls = new JCheckBox();
        jls.setBackground(new Color(10,10,12));
        //jls.setForeground(app.MenuFG);
        jls.setSelected(Boolean.valueOf(stng.get(key).toLowerCase()));
		//SwingUtils.scaleCheckBoxIcon(jls);
        jls.addItemListener(listener);
        jls.setOpaque(true);
		jls.setSize(getWidth()/2 - TEXT_SPACING, 41);
		jls.setLocation(getWidth()/2 + TEXT_SPACING * 2,Y + 32);
		Y+=60+VARIABLE_SPACING;
		add(jls);
	}
	
	public void AddIntValue(String name, String key) {
		try {
			AddIntField(name,key, new FocusAdapter() {
				
				public void Click(FocusEvent e) {
					Document d = ((JTextComponent)e.getComponent()).getDocument();
					try {
						int value = Integer.parseInt(d.getText(0, d.getLength()));
						stng.set(key, value);
						refresh();
					} catch (NumberFormatException | BadLocationException e1) {e1.printStackTrace();}
				}

				@Override
				public void focusLost(FocusEvent e) {
					Click(e);
				}
			});
		} catch (ValueNotFoundException e) {e.printStackTrace();}
	}
	
	public void AddIntField(String name, String key, FocusListener listener) throws ValueNotFoundException {
		JLabel jlb = new JLabel();
		jlb.setFont(getFont());
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(name);
		jlb.setSize(getWidth()/2 - TEXT_SPACING, 100);
		jlb.setLocation(0,Y);
		jlb.setForeground(getForeground());
		add(jlb);
		JFormattedTextField jls = new JFormattedTextField(NumberFormat.getNumberInstance());
        jls.setBackground(new Color(10,10,12));
        //jls.setForeground(app.MenuFG);
        jls.setValue(stng.getInt(key));
		//SwingUtils.scaleCheckBoxIcon(jls);
        jls.addFocusListener(listener);
        jls.setOpaque(true);
        jls.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    requestFocus();
                }
            }
		});
		jls.setSize(234, 41);
		jls.setLocation(getWidth()/2 + TEXT_SPACING * 2,Y + 32);
		Y+=60+VARIABLE_SPACING;
		add(jls);
	}
	
	
	// FontSettings
	
	protected void InitFontSettings() {
		
		JLabel jlb = new JLabel();
		
		JLabel lblfont = new JLabel();
		lblfont.setSize(252, 41);
		lblfont.setLocation(getWidth()/2- lblfont.getWidth()/2, 79);
		
		Font f = app.getFont();
        String  strStyle;

        if (f.isBold()) {
            strStyle = f.isItalic() ? "bolditalic" : "bold";
        } else {
            strStyle = f.isItalic() ? "italic" : "plain";
        }
		lblfont.setText(f.getFamily() + " " + strStyle +  " " + f.getSize());
		lblfont.setHorizontalAlignment(SwingConstants.CENTER);
		lblfont.setForeground(getForeground());
		lblfont.setFont(new Font("Monospaced", Font.BOLD, 18));
		
		jlb.setFont(new Font(FT, Font.BOLD, 26));
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText("Choose Font:");
		jlb.setForeground(getForeground());
		
		jlb.setSize(180, 100);
		jlb.setLocation(getWidth()/2-jlb.getWidth()/2-lblfont.getWidth() - (TEXT_SPACING/2-10),50);
		
		JButton btnNewButton = new JButton("Browse");
		btnNewButton.setSize(134, 41);
		btnNewButton.setLocation(getWidth()/2 + lblfont.getWidth()/2 + TEXT_SPACING, 79);
		btnNewButton.setForeground(getForeground());
		
		add(jlb);
		add(lblfont);
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
					try {
					JFontChooser jfc = new JFontChooser();
					jfc.setSelectedFont(app.getFont());
					jfc.showDialog(app);
					Font f = jfc.getSelectedFont();
					stng.set("fontsize", f.getSize());
					stng.set("fontfamily", f.getFamily());
					stng.set("fontstyle", f.getStyle());
					refresh();
				} catch (Exception e1) {e1.printStackTrace();}
			}
		});
		btnNewButton.setFont(new Font(FT, Font.BOLD, 14));
		btnNewButton.setBackground(new Color(10,10,12));
		btnNewButton.setOpaque(true);
		add(btnNewButton);
		Y+=60+VARIABLE_SPACING;
	}
	
	
	
	// SyntaxSettings
	
	protected Map<String, String> values = new HashMap<String,String>();
	
	protected void InitSyntaxSettings() throws Exception {
		JLabel jlb = new JLabel();
		jlb.setFont(getFont());
		jlb.setHorizontalAlignment(SwingConstants.CENTER);
		jlb.setText("Choose Langauge:");
		jlb.setForeground(getForeground());
		jlb.setSize(234, 100);
		jlb.setLocation(getWidth()/2 - jlb.getWidth() - TEXT_SPACING, Y);
		add(jlb);
		Field[] fields = SyntaxConstants.class.getDeclaredFields();
		for(Field f : fields){
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
                return new BasicArrowButton(SwingConstants.SOUTH, null, null, Color.GRAY, null);
            }
        });
        jls.setBackground(new Color(10,10,12));
        //jls.setForeground(app.MenuFG);
        jls.setSelectedItem(getSyntaxKey(stng.get("syntax")));
        jls.addItemListener(e -> {
    		stng.set("syntax", values.get(e.getItem()));
    		refresh();
    	});
        jls.setOpaque(true);
		jls.setSize(234, 41);
		jls.setLocation(getWidth()/2 + TEXT_SPACING*2,Y+ 26);
		add(jls);
		Y+=60+VARIABLE_SPACING;
	}
	
	protected String getSyntaxKey(String value) {
		for(Entry<String, String> entry: values.entrySet()) {
			// if give value is equal to value from entry
			// print the corresponding key
			if(entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}


	@Override
	public void paintComponent(Graphics g) {
		//NGSScrollPane sp1 = window.getScrollPane();
        //sp1.repaint();
		super.paintComponent(g);

	    g.setColor(new Color(10,10,12));
	    g.fillRect(getBuffer(), 0, getWidth(), getHeight());
	    
	    paintFB(g, window.getScrollPane());
		g.setColor(app.contentpane.getBackground());
		((Graphics2D)g).setStroke(new BasicStroke(10));
		g.drawLine(0, 0, 0, getHeight());
	}
	public final JButton jl = new JButton(">") {
		private static final long serialVersionUID = 3394518635747541418L;

		@Override public void paint(Graphics g) {}
	};
	@Override
	public JButton getFolderButton() {
		return jl;
	}
	@Override
	public App getApp() {
		return app;
	}
}
class CustomComboBoxRenderer extends DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2702235431578141710L;

	@Override
	public Component getListCellRendererComponent
		(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel lbl = (JLabel)super.getListCellRendererComponent
				(list, value, index, isSelected,  cellHasFocus);
		lbl.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
		lbl.setBackground(new Color(10,10,12));
		return lbl;
	}
}
