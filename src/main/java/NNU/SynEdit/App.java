package NNU.SynEdit;

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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
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
 * Hello world!
 *
 */
public class App extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5770603365260133811L;
    public static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int MenuBarSize = 20;
    public static final Color MenuBG = new Color(33, 33, 33);
    public static final Color MenuFG = new Color(240, 240, 240);
    public static String FilePath = "\000";
	private SyntaxTextArea textArea;
	public boolean saved = true;
	
	@Override
	public Font getFont() {
		return new Font(Font.MONOSPACED, Font.BOLD, 40);
	}

    public App(String str) throws Exception {

        setSize((int)size.getWidth()/2,(int)size.getHeight()/2);

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
        InputStream inpstr = App.class.getResourceAsStream("/NNU/SynEdit/Style.xml");
        Theme theme = Theme.load(inpstr);
        theme.lineNumberColor = Color.white;
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
	    	openfile(true)
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        getRootPane().registerKeyboardAction(e ->
	    	openfile(true)
	    	,KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_DOWN_MASK),
	    	JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        setUndecorated(false);
        setBackground(Color.black);
        setContentPane(contentpane);
        setTitle("SynEdit");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(getWidth()/2,getHeight()/2);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        textArea.setText(str);
        textArea.setCaretPosition(0);
        
        revalidate();
        repaint();
    }

	public static void main(String[] args) {
		
		final StringBuilder starttext = new StringBuilder();
		if (args.length>0) {
			String res = read(args[0]);
			starttext.append("\000".equals(res) ? "" : res);
			if (!"\000".equals(res))
				FilePath = args[0];
		}
		
        // Start all Swing applications on the EDT.
        SwingUtilities.invokeLater(() -> {
			try {
				new App(starttext.toString()).setVisible(true);
			} catch (Exception e) {e.printStackTrace();}
		});
    }
	
	public boolean megaSave() {
		if ("\000".equals(FilePath)) {
			int result = JOptionPane.showConfirmDialog((Component) null,
					"Do you want to save?","alert", JOptionPane.YES_NO_CANCEL_OPTION);
	    	if (result!=0)
	    		return false;
	    	openfile(false);
		}
		return save(FilePath, textArea.getText());
	}
	
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
	
	public void openfile(boolean load) {
		String res = "\000";
		while ("\000".equals(res)) {
	        JFileChooser chooser = new JFileChooser();
	        int returnVal = chooser.showOpenDialog(null);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	            try {
					res = chooser.getSelectedFile().getCanonicalPath();
					Scanner myReader = new Scanner(new File(res));
					if ("\000".equals(res)) {
						JOptionPane.showMessageDialog(this,
							    "file is '\000'.? ",
							    "Error reading file",
							    JOptionPane.ERROR_MESSAGE);
						continue;
					}
					FilePath = res;
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this,
						    "Unable to read file due to error: " + e.getMessage(),
						    "Error reading file",
						    JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
	        } else {
	        	return;
	        }
		}
		if (load) {
			textArea.setText(read(res));
	        textArea.setCaretPosition(0);
			revalidate();
			repaint();
		}
	}
	
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

	public void close() {
		if (saved) System.exit(0);
		int result = JOptionPane.showConfirmDialog((Component) null,
				"Do you want to exit?","alert", JOptionPane.YES_NO_OPTION);
		System.out.println(result);
    	if (result==1||result==-1)
    		return;
    	megaSave();
    	System.exit(0);
	}

}
