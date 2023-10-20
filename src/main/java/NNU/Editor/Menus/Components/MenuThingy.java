package NNU.Editor.Menus.Components;

import static NNU.Editor.App.MenuBG;
import static NNU.Editor.App.MenuFG;
import static NNU.Editor.Utils.Utils.EDITORNAME;
import static java.lang.System.out;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import NNU.Editor.App;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.AboutWindow;
import NNU.Editor.Windows.PreferencesWindow;

/**
 * the menubar
 */
public class MenuThingy extends JMenuBar {
	
	private static final long serialVersionUID = -6781221982811029019L;
	protected final App app;
	
	public MenuThingy(App app) {
		super();
		this.app = app;
		this.setOpaque(true);
		//this.setBackground(App.MenuBG);
		initComps();
	}

	protected void initComps() {
		
		/* File */
		
        Menu FILE = new Menu("File");
        FILE.setMnemonic(KeyEvent.VK_F);
        FILE.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        FILE.setToolTipText("File");
        
        MenuItem SAVE = new MenuItem("Save");
        SAVE.addActionListener(e -> app.megaSave(false));
        SAVE.setMnemonic(KeyEvent.VK_S);
        SAVE.setToolTipText("Save");
        FILE.add(SAVE);
        
        MenuItem NEW = new MenuItem("New");
        NEW.addActionListener(e -> app.megaSave(false));
        NEW.setMnemonic(KeyEvent.VK_N);
        NEW.setToolTipText("New empty editor");
        FILE.add(NEW);
        
        MenuItem OPEN = new MenuItem("Open");
        OPEN.addActionListener(e -> {

    		out.println("inputing file");
    		String file = Utils.tuneFileDialogResult(Utils.openFileDialog(false));
    		out.println("inputing file complete");
    		if (file==null) return;
    		out.println("reading file : " + file);
    		out.println("Started timing editor loading");
        	long start = System.nanoTime();
        	
        	app.openFile(file);
        	
            long time1 = System.nanoTime() - start;
            long time2 = System.nanoTime();
            
            long time3 = System.nanoTime() - time2;
            out.println("Time took to read and load the window : " + (time1)
            		+ " to set window : " + (time3));
        });
        OPEN.setMnemonic(KeyEvent.VK_O);
        OPEN.setToolTipText("Open a file in " + Utils.EDITORNAME);
        FILE.add(OPEN);
        
        MenuItem OPENFOLDER = new MenuItem("Open a folder");
        OPENFOLDER.addActionListener(e -> app.openFolderAndDialog());
        OPENFOLDER.setMnemonic(KeyEvent.VK_E);
        OPENFOLDER.setToolTipText("Open a folder in " + Utils.EDITORNAME);
        FILE.add(OPENFOLDER);
        
        FILE.addSeparator();
        
        MenuItem CLOSE = new MenuItem("Close");
        CLOSE.addActionListener(e -> app.closeSelectedWindow());
        CLOSE.setMnemonic(KeyEvent.VK_C);
        CLOSE.setToolTipText("Close the selected window ");
        FILE.add(CLOSE);
        
        MenuItem EXIT = new MenuItem("Exit");
        EXIT.addActionListener(e -> {
			try {
				app.close();
			} catch (InterruptedException e1) {e1.printStackTrace();}
		});
        EXIT.setMnemonic(KeyEvent.VK_C);
        EXIT.setToolTipText("Exit " + Utils.EDITORNAME);
        FILE.add(EXIT);
        add(FILE);
        
        

		/* Help */
        Menu HELP = new Menu("Help");
        HELP.setMnemonic(KeyEvent.VK_H);
        HELP.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        HELP.setToolTipText("Help");
        MenuItem PREFRENCES = new MenuItem("Prefrences");
        PREFRENCES.addActionListener(e -> app.setSelectedWindow(new PreferencesWindow(app)));
        PREFRENCES.setMnemonic(KeyEvent.VK_P);
        PREFRENCES.setToolTipText("Edit " + EDITORNAME + "'s prefrences");
        HELP.add(PREFRENCES);
        MenuItem CREDITS = new MenuItem("About");
        CREDITS.addActionListener(e -> app.setSelectedWindow(new AboutWindow(app)));
        CREDITS.setMnemonic(KeyEvent.VK_P);
        CREDITS.setToolTipText("About " + EDITORNAME);
        HELP.add(CREDITS);
        add(HELP);
	}
	
    @Override
	public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(App.MenuBG);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponents(g);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
    }
}

class MenuItem extends JMenuItem {
	private static final long serialVersionUID = 9131978348527133180L;

	public MenuItem(String str) {
		super(str);
        setOpaque(true);
        //setBackground(MenuBG);
        //setForeground(MenuFG);
	}
}
class Menu extends JMenu {
	private static final long serialVersionUID = -1789396700301437504L;

	public Menu(String str) {
		super(str);
        setOpaque(true);
        setBackground(MenuBG);
        setForeground(MenuFG);
	}
}