package NNU.Editor.Menus;

import static NNU.Editor.AssetManagement.StringTable.getString;

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
import java.util.concurrent.atomic.AtomicBoolean;

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
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import NNU.Editor.App;
import NNU.Editor.Settings;
import NNU.Editor.Menus.Components.SmartJLabel;
import NNU.Editor.Windows.Interfaces.Window;
public class PreferencesMenu extends JPanel {
	
	public static final String FT = "Tahoma";
	public static final int TEXT_SPACING = 15;
	public static final int VARIABLE_SPACING = 30;
	public static Font f = new Font(FT, Font.BOLD, 24);
	
	private static final long serialVersionUID = 5027581198213070489L;
	//public Jthis this = this;
	public final Settings stng;
	public App app;
	public Window window;
	public int Y = 0;
	
	@Override public Font getFont() {return f;}
	
	/**
	 * Creates and opens the prefrences menu
	 * @param stng the settings linked to the menu
	 * @param app the app linked to the menu
	 */
	public PreferencesMenu(Settings stng, App app, Window window) {
		this.stng = stng;
		this.app = app;
		this.window = window;
		setOpaque(true);
		setBackground(new Color(10,10,12));
		setForeground(Color.white.darker());
		
        setBorder(new EmptyBorder(0,App.getBuffer(),0,0));
		setLayout(null);
		
		//setMinimumSize(new Dimension(1000, 1005));
		
		refresh();
		app.repaint();
	}
	
	@Override
    public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
    }
    
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		window.getScrollPane().paintSeperators((Graphics2D)g);
	}
	
	AtomicBoolean b = new AtomicBoolean(false);
	
	public void refresh() {
		try {
			
			if (b.get()==true) return;
			stng.refresh();
			removeAll();
			
			this.removeAll();
			this.setLayout(null);
			//int x = app.isFolderOpen() ? app.Folder.getWidth() : 5;//app.contentpane.getWidth() - x - 100
			
			/* Yay pre-set values! (╥﹏╥) (I gave up) */
			this.setPreferredSize(new Dimension(900,Y));
			
			this.setOpaque(true);
			initComponents();
			
			revalidate();
			
			app.repaint();
			b.set(false);
			
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Here you can see all my failures and my inability to add basic features like:
	 *   <li>Resizing the folder view.
	 *   <li>Move the numbers from the right to the left.
	 *   <li>Auto pausing the shell when running projects.<br><br>
	 * 
	 * This is all because of my inability to think into the future.<br>
	 * @throws Exception
	 */
	public void initComponents() throws Exception {
		Y = 40;
		
		
		AddHeader("options.header.appearence"); /* General Appearance */
		
		AddFontValue("options.tab.font", "tab.font");
		AddBooleanValue("options.antialias","editor.antialias");
		
		/**
		 * Can still be enabled in the file (Please don't, you can barely see the difference :P)
		 * and there is litterally 0 visible difference...
		 */
		//AddBooleanValue("options.powerantialias","editor.powerantialias");
		AddBooleanValue("options.preview", "folder.imgpreview");
		
		AddHeader("options.header.editor.appearence",7,false); /* Text Editor Appearance */
		
		AddFontValue("options.editor.font", "editor.font");
		AddBooleanValue("options.editor.numbers","editor.numberlines");
		AddSyntaxValue("options.editor.language","editor.syntax");
		
		AddHeader("options.header.other.editor.appearence",7,false); /* Other Editors Appearance */

		AddFontValue("options.prop.font", "editor.prop.font");
		AddIntValue("options.prop.width", "editor.prop.tablewidth");
		
		

		AddHeader("options.header.system"); /* System */

		AddBooleanValue("options.system.startup", "system.checkversion");
		//AddBooleanValue("options.system.debug", "system.debugbutton"); /* Nu CTRL + T for u */
	}
	
	public void AddHeader(String name) {
		AddHeader(name,10,true);
	}
	
	public void AddHeader(String name, int size, boolean underline) {
		Y+= 10 * (size/10f);
		SmartJLabel jlb = new SmartJLabel(stng);
		jlb.setFont(new Font(FT, Font.BOLD, (int)(34f * (size/10f))));
		jlb.setText(getString(name));
		jlb.setSize(getWidth(), 100);
		jlb.setLocation(0,Y);
		jlb.setForeground(getForeground().brighter());
		jlb.setUnderlineEnabled(underline);
		this.add(jlb);
		Y+=70 * (size/10f) + VARIABLE_SPACING * (size/10f);
	}
	
	public void AddBooleanValue(String name, String key) {
		AddBooleanCheckbox(name,key, e -> {
			boolean val = ((JCheckBox)e.getSource()).isSelected();
			stng.set(key, val);
			refresh();
		});
	}
	
	public void AddBooleanCheckbox(String name, String key, ItemListener listener) {
		SmartJLabel jlb = new SmartJLabel(stng);
		jlb.setFont(getFont());
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(getString(name));
		jlb.setSize(getWidth()/2 - TEXT_SPACING, 100);
		jlb.setLocation(0,Y);
		jlb.setForeground(getForeground());
		this.add(jlb);
        JCheckBox jls = new JCheckBox();
        jls.setBackground(new Color(10,10,12));
        //jls.setForeground(app.MenuFG);
        jls.setSelected(Boolean.valueOf(stng.get(key).toLowerCase()));
		//SwingUtils.scaleCheckBoxIcon(jls);
        jls.addItemListener(listener);
        jls.setOpaque(true);
		jls.setSize(getWidth()/2 - TEXT_SPACING, 41);
		jls.setLocation(getWidth()/2 + TEXT_SPACING * 2,Y + 32);
		jls.setText(jls.isSelected()?getString("options.enabled"):getString("options.disabled"));
		jls.setFont(getFont());
		jls.setVerticalTextPosition(SwingConstants.CENTER);
		jls.setVerticalAlignment(SwingConstants.TOP);
		Y+=60+VARIABLE_SPACING;
		this.add(jls);
	}
	
	public void AddIntValue(String name, String key) {
		AddIntField(name,key, new FocusAdapter() {
			public void Click(FocusEvent e) {
				Document d = ((JTextComponent)e.getComponent()).getDocument();
				try {
					int value = Integer.parseInt
							(d.getText(0, d.getLength()).replaceAll("[^\\d]*", ""));
					stng.set(key, value);
					refresh();
					//app.redraw();
				} catch (NumberFormatException | BadLocationException e1) {e1.printStackTrace();}
			}
			@Override
			public void focusLost(FocusEvent e) {
				Click(e);
			}
		});
	}
	
	public void AddIntField(String name, String key, FocusListener listener) {
		SmartJLabel jlb = new SmartJLabel(stng);
		jlb.setFont(getFont());
		jlb.setHorizontalAlignment(SwingConstants.RIGHT);
		jlb.setText(getString(name));
		jlb.setSize(getWidth()/2 - TEXT_SPACING, 100);
		jlb.setLocation(0,Y);
		jlb.setForeground(getForeground());
		this.add(jlb);
		JFormattedTextField jls = new JFormattedTextField(NumberFormat.getNumberInstance());
        jls.setBackground(new Color(10,10,12));
        
        jls.setValue(stng.getInt(key));
        
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
		this.add(jls);
	}
	
	
	// FontSettings
	
	public void AddFontValue(String name, String key) {
		
		SmartJLabel jlb = new SmartJLabel(stng);
		
		JLabel lblfont = new SmartJLabel(stng);
		lblfont.setSize(252, 100);
		lblfont.setLocation(getWidth()/2- lblfont.getWidth()/2, Y+TEXT_SPACING);
		
		Font f = stng.getFont(key);
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
		jlb.setText(getString(name));
		jlb.setForeground(getForeground());
		
		jlb.setSize(lblfont.getX() - (TEXT_SPACING/2-10), 100);
		jlb.setLocation(0,Y+TEXT_SPACING);
		
		JButton btnNewButton = new JButton(getString("options.browse"));
		btnNewButton.setSize(104, 55);
		btnNewButton.setLocation(getWidth()/2 + lblfont.getWidth()/2 + TEXT_SPACING, Y+TEXT_SPACING + 20);
		btnNewButton.setForeground(getForeground());
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
					try {
					JFontChooser jfc = new JFontChooser();
					jfc.setSelectedFont(f);
					jfc.showDialog(app);
					Font f = jfc.getSelectedFont();
					stng.set(key, f);
					refresh();
				} catch (Exception e1) {e1.printStackTrace();}
			}
		});
		btnNewButton.setFont(new Font(FT, Font.BOLD, 14));
		btnNewButton.setBackground(new Color(10,10,12));
		btnNewButton.setOpaque(true);
		
		this.add(jlb);
		this.add(lblfont);
		this.add(btnNewButton);
		Y+=60+VARIABLE_SPACING;
	}
	
	
	
	// SyntaxSettings
	
	protected static Map<String, String> values = new HashMap<String,String>();
	static Vector<String> items = new Vector<>();
	static {
		try {
			Field[] fields = SyntaxConstants.class.getDeclaredFields();
			for(Field f : fields){
				Object objectValue = "";
				Object value;
				value = f.get(objectValue);
				values.put(f.getName().replace("SYNTAX_STYLE_", "").toLowerCase().replace('_', ' '),
					((String) value).replace("text/", ""));
			}
	
	        for (Map.Entry<String,String> entry : values.entrySet()) {
	            items.add(entry.getKey());
				//  System.out.println(entry.getKey() + "    " + entry.getValue());
	        }
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void AddSyntaxValue(String name, String key) throws Exception {
		SmartJLabel jlb = new SmartJLabel(stng);
		jlb.setFont(getFont());
		jlb.setHorizontalAlignment(SwingConstants.CENTER);
		jlb.setText(getString(name));
		jlb.setForeground(getForeground());
		jlb.setSize(234, 100);
		jlb.setLocation(getWidth()/2 - jlb.getWidth() - TEXT_SPACING, Y);
		this.add(jlb);
        JComboBox<String> jls = new JComboBox<String>(items);
        // To change the arrow button's background
        jls.setRenderer(new CustomComboBoxRenderer());
        
        jls.setBackground(new Color(10,10,12));
        
        jls.setSelectedItem(getSyntaxKey(stng.get(key)));
        jls.addItemListener(e -> {
    		stng.set(key, values.get(e.getItem()));
    		refresh();
    	});
        jls.setOpaque(true);
		jls.setSize(234, 41);
		jls.setLocation(getWidth()/2 + TEXT_SPACING*2,Y+ 26);
		this.add(jls);
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
