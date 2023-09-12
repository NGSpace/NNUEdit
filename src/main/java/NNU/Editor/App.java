package NNU.Editor;

import static NNU.Editor.Utils.EditorName;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.ConfigurableCaret;

/**
 * the editor
 */
public class App extends JFrame {
	private static final long serialVersionUID = 5770603365260133811L;
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int MenuBarSize = 30;
    public static final Color MenuBG = new Color(33, 33, 33);
    public static final Color MenuFG = new Color(240, 240, 240);
	public SyntaxTextArea textArea;
	public boolean saved = true;
	public boolean isbusy = false;
	/**
	 * The settings
	 */
	public final Settings stng;
	
	@Override
	public Font getFont() {
		try {
			return new Font(stng.get("fontfamily"),
				Integer.valueOf(stng.get("fontstyle")),
				Integer.valueOf(stng.get("fontsize")));
		} catch (Exception e) {
			e.printStackTrace();
			return new Font(Font.MONOSPACED, Font.BOLD, 40);
		}
	}
	
	/**
	 * the app
	 * @param filepath the path to read from upon startup (leave empty for none)
	 * @throws IOException
	 */
    public App(String filepath) throws IOException {
    	
    	File stfile = new File(getProgramPath() + "/" + EditorName + "/" + EditorName + ".properties");
    	stng = new Settings(stfile.getAbsolutePath(), this);
    	
    	String starttext = "";
    	
    	if (!"".equals(filepath)) {
    		starttext = read(filepath);
    	}

        setSize((int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2);
        
    	try {
    	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch (Exception e) {
    	    System.err.println("UI Exception caught:"+e);
    	}
    	
        JPanel contentpane = new JPanel(new BorderLayout());

        textArea = new SyntaxTextArea(this);
        
        JScrollPane sp = new JScrollPane(textArea,
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        InputStream inpstr = App.class.getResourceAsStream("/NNU/Editor/Style.xml");
        Theme theme = Theme.load(inpstr);
        theme.apply(textArea);
        textArea.setMargin(new Insets(0, 0, 0, 0));
        textArea.setFont(getFont());
        textArea.setBackground(new Color(10,10,12));
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.setCurrentLineHighlightColor(new Color(44,44,44));
        textArea.setSelectionColor(new Color(99, 128, 176));
        textArea.setSelectedTextColor(new Color(255,255,255));
        ((ConfigurableCaret) textArea.getCaret()).setBlinkRate(0);
        
        //Menu
        MenuThingy menuBar = new MenuThingy(this );
        menuBar.setBackground(MenuBG);
        menuBar.setMinimumSize(new Dimension(getWidth(),100));
        menuBar.setForeground(MenuFG);
        
        //Add Things to contentpane
        contentpane.setLayout(null);
        menuBar.setBounds(0, 0, getWidth(), MenuBarSize);
        contentpane.add(menuBar);
        sp.setBounds(0, MenuBarSize, getWidth(), getHeight() -MenuBarSize);
        contentpane.add(sp);

        contentpane.setSize(this.getSize());
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(25,0));
        sp.getHorizontalScrollBar().setLocation(sp.getWidth() - 25, 0);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(25,100));
        sp.getVerticalScrollBar().setLocation(sp.getWidth() - 25, 0);
        
        getRootPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                menuBar.setBounds(0, 0, getWidth(), MenuBarSize);
                sp.setBounds(0-1, MenuBarSize-3, getWidth() - 13, getHeight() -MenuBarSize - 35);
                repaint();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        
        getRootPane().registerKeyboardAction(e ->
        	megaSave()
        	,KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK),
        	JComponent.WHEN_IN_FOCUSED_WINDOW );
        getRootPane().registerKeyboardAction(e ->
        	textArea.openfile(true)
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        getRootPane().registerKeyboardAction(e ->
        	textArea.openfile(true)
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        getRootPane().registerKeyboardAction(e ->
	    	new PreferencesMenu(stng,this)
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        setUndecorated(false);
        setBackground(Color.black);
        setContentPane(contentpane);
        setTitle(EditorName);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocation(getWidth()/2,getHeight()/2);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        textArea.setText(starttext);
        textArea.setCaretPosition(0);
        
        revalidate();
        repaint();
    }
    
    /**
     * the entry point of this weird ass program
     * @param args which file to open at startup (leave empty for no selected file)
     */
	public static void main(String[] args) {
		
		final StringBuilder starttext = new StringBuilder();
		if (args.length>0) {
			starttext.append(args[0]);
		}
		
        SwingUtilities.invokeLater(() -> {
			try {
				new App(starttext.toString()).setVisible(true);
			} catch (Exception e) {e.printStackTrace();}
		});
    }
	
	/**
	 * calls save and asks user where to save if non is selected already.
	 * @return whether the operation was successful
	 */
	public boolean megaSave() {
		if ("\000".equals(textArea.FilePath)) {
			int result = JOptionPane.showConfirmDialog((Component) null,
					"Do you want to save?","alert", JOptionPane.YES_NO_CANCEL_OPTION);
	    	if (result!=0)
	    		return false;
	    	textArea.openfile(false, true);
		}
		return save(textArea.FilePath, textArea.getText());
	}
	
	/**
	 * saves text to the path specified
	 * @param path the path
	 * @param text the text
	 * @return whether the operation was successful
	 */
	public boolean save(String path, String text) {
		try {
			saved = true;
			FileWriter fw = new FileWriter(path);
			fw.write(text);
		    fw.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * reads a file and returns it's contents in String form.
	 * @param path The path to file to read from.
	 * @return the contents of said file.
	 */
	public static String read(String path) {
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			StringBuilder strb = new StringBuilder();
			while (myReader.hasNextLine()) {
				strb.append(myReader.nextLine());
				strb.append('\n');
			}
			myReader.close();
			return strb.toString();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return "\000";
	}
	
	/**
	 * This does nothing yet.
	 */
	@Unfinnished
	public void ConfirmExit() {
		//Planned
	}
	
	/**
	 * Pull up the close menu and runs System.exit(0); if user agrees.
	 */
	public void close() {
		if (saved&&!isbusy) System.exit(0);
		int result = JOptionPane.showConfirmDialog(null,
				"Do you want to exit?","alert", JOptionPane.YES_NO_OPTION);
    	if (result==1||result==-1)
    		return;
    	megaSave();
    	System.exit(0);
	}
	
	/**
	 * returns the location of the jar
	 * @return the location of the jar
	 * @throws UnsupportedEncodingException
	 */
	public static String getProgramPath() throws UnsupportedEncodingException {
		URL url = App.class.getProtectionDomain().getCodeSource().getLocation();
		String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
		return new File(jarPath).getParentFile().getPath();
	}
	
	/**
	 * Refreshes the settings from the map of stng
	 * @apiNote This rereads changes made to the properties file
	 * @throws ValueNotFoundException
	 * @throws IOException
	 */
	public void refreshSettings() throws ValueNotFoundException, IOException {
		textArea.refresh();
		//this.revalidate();
		//this.repaint();
	}
	
}
