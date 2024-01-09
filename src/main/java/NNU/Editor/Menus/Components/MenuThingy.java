package NNU.Editor.Menus.Components;

import static NNU.Editor.App.MenuBG;
import static NNU.Editor.App.MenuFG;
import static NNU.Editor.AssetManagement.StringTable.getString;
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

import NNU.Editor.App;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.AboutWindow;
import NNU.Editor.Windows.PreferencesWindow;
import NNU.Editor.Windows.TextEditorWindow;
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
		initComps();
	}

	protected void initComps() {
		
		/* File */
		
        Menu FILE = new Menu(getString("menubar.file"));
        FILE.setMnemonic(KeyEvent.VK_F);
        FILE.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        FILE.setToolTipText(getString("menubar.file.tooltip"));
        
        MenuItem SAVE = new MenuItem(getString("menubar.save"));
        SAVE.addActionListener(e -> app.SaveAll(false));
        SAVE.setMnemonic(KeyEvent.VK_S);
        SAVE.setToolTipText(getString("menubar.save.tooltip"));
        FILE.add(SAVE);
        
        MenuItem NEW = new MenuItem(getString("menubar.neweditor"));
        NEW.addActionListener(e -> {
        	TextEditorWindow txtwin = new TextEditorWindow(app, "");
        	app.setSelectedWindow(txtwin);
        });
        NEW.setMnemonic(KeyEvent.VK_N);
        NEW.setToolTipText(getString("menubar.neweditor.tooltip"));
        FILE.add(NEW);
        
        MenuItem OPEN = new MenuItem(getString("menubar.openfile"));
        OPEN.addActionListener(e -> {

    		out.println("Inputing file");
    		String file = Utils.tuneFileDialogResult(Utils.openFileDialog(false));
    		out.println("Loading file");
    		if (file==null) return;
        	long start = System.nanoTime();
        	
        	app.openFile(file);
        	
            long time1 = System.nanoTime() - start;

            out.println("Time took to read and load the window : " + (time1));
        });
        OPEN.setMnemonic(KeyEvent.VK_O);
        OPEN.setToolTipText(getString("menubar.openfile.tooltip",EDITORNAME));
        FILE.add(OPEN);
        
        MenuItem OPENFOLDER = new MenuItem(getString("menubar.openfolder"));
        OPENFOLDER.addActionListener(e -> app.openFolderAndDialog());
        OPENFOLDER.setMnemonic(KeyEvent.VK_E);
        OPENFOLDER.setToolTipText(getString("menubar.openfolder.tooltip",EDITORNAME));
        FILE.add(OPENFOLDER);
        
        FILE.addSeparator();
        
        MenuItem CLOSE = new MenuItem(getString("menubar.close"));
        CLOSE.addActionListener(e -> app.closeSelectedWindow());
        CLOSE.setMnemonic(KeyEvent.VK_C);
        CLOSE.setToolTipText(getString("menubar.close.tooltip"));
        FILE.add(CLOSE);
        
        MenuItem EXIT = new MenuItem(getString("menubar.exit"));
        EXIT.addActionListener(e -> app.close());
        EXIT.setMnemonic(KeyEvent.VK_C);
        EXIT.setToolTipText(getString("menubar.exit.tooltip",EDITORNAME));
        FILE.add(EXIT);
        add(FILE);
        
        

		/* Help */
        Menu HELP = new Menu(getString("menubar.help"));
        HELP.setMnemonic(KeyEvent.VK_H);
        HELP.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        HELP.setToolTipText(getString("menubar.help.tooltip"));
        MenuItem PREFRENCES = new MenuItem(getString("menubar.options"));
        PREFRENCES.addActionListener(e -> app.setSelectedWindow(new PreferencesWindow(app)));
        PREFRENCES.setMnemonic(KeyEvent.VK_P);
        PREFRENCES.setToolTipText(getString("menubar.options.tooltip",EDITORNAME));
        HELP.add(PREFRENCES);
        MenuItem CREDITS = new MenuItem(getString("menubar.about"));
        CREDITS.addActionListener(e -> app.setSelectedWindow(new AboutWindow(app)));
        CREDITS.setMnemonic(KeyEvent.VK_A);
        CREDITS.setToolTipText(getString("menubar.about.tooltip",EDITORNAME));
        HELP.add(CREDITS);
        add(HELP);
        
        

		/* RUN */
        Menu RUN = new Menu(getString("menubar.run"));
        RUN.setMnemonic(KeyEvent.VK_R);
        RUN.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        RUN.setToolTipText(getString("menubar.run.tooltip"));
        MenuItem RUNPROJ = new MenuItem(getString("menubar.runproj"));
        RUNPROJ.addActionListener(e -> app.RunApp());
        RUNPROJ.setMnemonic(KeyEvent.VK_R);
        RUNPROJ.setToolTipText(getString("menubar.runproj.tooltip"));
        RUN.add(RUNPROJ);
        MenuItem RUNFILE = new MenuItem(getString("menubar.runfile"));
        RUNFILE.addActionListener(e -> app.RunFile());
        RUNFILE.setMnemonic(KeyEvent.VK_F);
        RUNFILE.setToolTipText(getString("menubar.runfile.tooltip"));
        RUN.add(RUNFILE);
        add(RUN);
        
        

		/* Index */
        Menu INDEX = new Menu(getString("menubar.index"));
        INDEX.setMnemonic(KeyEvent.VK_I);
        INDEX.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        INDEX.setToolTipText(getString("menubar.index.tooltip"));
        MenuItem INFO = new MenuItem(getString("menubar.info"));
        INFO.addActionListener(e -> {
			try {
				app.showInfoPopup();
			} catch (Exception e1) {
				System.out.println();
				e1.printStackTrace();
			}
		});
        INFO.setMnemonic(KeyEvent.VK_I);
        INFO.setToolTipText(getString("menubar.info.tooltip"));
        INDEX.add(INFO);
        add(INDEX);
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