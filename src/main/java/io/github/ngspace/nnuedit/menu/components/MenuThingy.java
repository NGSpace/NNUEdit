package io.github.ngspace.nnuedit.menu.components;

import static io.github.ngspace.nnuedit.App.MenuBG;
import static io.github.ngspace.nnuedit.App.MenuFG;
import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;
import static io.github.ngspace.nnuedit.utils.Utils.EDITORNAME;
import static java.lang.System.out;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.window.AboutWindow;
import io.github.ngspace.nnuedit.window.PreferencesWindow;
import io.github.ngspace.nnuedit.window.TextEditorWindow;
/**
 * the menubar
 */
public class MenuThingy extends JMenuBar {
	
	private static final long serialVersionUID = -6781221982811029019L;
	protected final App app;
	
	public MenuThingy(App app) {super();this.app = app;this.setOpaque(true);initComps();}

	protected void initComps() {
		
		/* File */
		
        Menu FILE = new Menu(get("menubar.file"));
        FILE.setMnemonic(KeyEvent.VK_F);
        FILE.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        FILE.setToolTipText(get("menubar.file.tooltip"));
        
        MenuItem SAVE = new MenuItem(get("menubar.save"));
        SAVE.addActionListener(e -> app.SaveAll(false));
        SAVE.setMnemonic(KeyEvent.VK_S);
        SAVE.setToolTipText(get("menubar.save.tooltip"));
        
        MenuItem NEW = new MenuItem(get("menubar.neweditor"));
        NEW.addActionListener(e -> {
        	TextEditorWindow txtwin = new TextEditorWindow(app, "");
        	app.addWindow(txtwin);
        });
        NEW.setMnemonic(KeyEvent.VK_N);
        NEW.setToolTipText(get("menubar.neweditor.tooltip"));
        
        MenuItem OPEN = new MenuItem(get("menubar.openfile"));
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
        OPEN.setToolTipText(get("menubar.openfile.tooltip",EDITORNAME));
        
        MenuItem OPENFOLDER = new MenuItem(get("menubar.openfolder"));
        OPENFOLDER.addActionListener(e -> app.openFolderAndDialog());
        OPENFOLDER.setMnemonic(KeyEvent.VK_E);
        OPENFOLDER.setToolTipText(get("menubar.openfolder.tooltip",EDITORNAME));
        
        MenuItem CLOSE = new MenuItem(get("menubar.close"));
        CLOSE.addActionListener(e -> app.closeSelectedWindow());
        CLOSE.setMnemonic(KeyEvent.VK_C);
        CLOSE.setToolTipText(get("menubar.close.tooltip"));
        
        MenuItem EXIT = new MenuItem(get("menubar.exit"));
        EXIT.addActionListener(e -> app.close());
        EXIT.setMnemonic(KeyEvent.VK_C);
        EXIT.setToolTipText(get("menubar.exit.tooltip",EDITORNAME));
        
        FILE.add(SAVE);
        FILE.add(NEW);
        FILE.add(OPEN);
        FILE.add(OPENFOLDER);
        FILE.addSeparator();
        FILE.add(CLOSE);
        FILE.add(EXIT);
        add(FILE);
        
        
		/* Help */
        Menu HELP = new Menu(get("menubar.help"));
        HELP.setMnemonic(KeyEvent.VK_H);
        HELP.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        HELP.setToolTipText(get("menubar.help.tooltip"));
        MenuItem PREFRENCES = new MenuItem(get("menubar.options"));
        PREFRENCES.addActionListener(e -> {app.addWindow(new PreferencesWindow(app));app.redraw();});
        PREFRENCES.setMnemonic(KeyEvent.VK_P);
        PREFRENCES.setToolTipText(get("menubar.options.tooltip",EDITORNAME));
        MenuItem CREDITS = new MenuItem(get("menubar.about"));
        CREDITS.addActionListener(e -> app.addWindow(new AboutWindow(app)));
        CREDITS.setMnemonic(KeyEvent.VK_A);
        CREDITS.setToolTipText(get("menubar.about.tooltip",EDITORNAME));
        
        
        HELP.add(PREFRENCES);
        HELP.add(CREDITS);
        add(HELP);
        
        
		/* RUN */
        Menu RUN = new Menu(get("menubar.run"));
        RUN.setMnemonic(KeyEvent.VK_R);
        RUN.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        RUN.setToolTipText(get("menubar.run.tooltip"));
        MenuItem RUNPROJ = new MenuItem(get("menubar.runproj"));
        RUNPROJ.addActionListener(e -> app.RunApp());
        RUNPROJ.setMnemonic(KeyEvent.VK_R);
        RUNPROJ.setToolTipText(get("menubar.runproj.tooltip"));
        MenuItem RUNFILE = new MenuItem(get("menubar.runfile"));
        RUNFILE.addActionListener(e -> app.RunFile());
        RUNFILE.setMnemonic(KeyEvent.VK_F);
        RUNFILE.setToolTipText(get("menubar.runfile.tooltip"));
        
        
        RUN.add(RUNPROJ);
        RUN.add(RUNFILE);
        add(RUN);
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

	public boolean contains(Component comp) {
		for (Component c : this.getComponents()) if (c==comp) return true;
		return false;
	}
}

class MenuItem extends JMenuItem {
	private static final long serialVersionUID = 9131978348527133180L;
	public MenuItem(String str) {super(str);setOpaque(true);}
}
class Menu extends JMenu {
	private static final long serialVersionUID = -1789396700301437504L;
	public Menu(String str) {super(str);setOpaque(true);setBackground(MenuBG);setForeground(MenuFG);}
}