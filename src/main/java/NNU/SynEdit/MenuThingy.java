package NNU.SynEdit;

import static NNU.SynEdit.App.MenuBG;
import static NNU.SynEdit.App.MenuFG;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class MenuThingy extends JMenuBar {
	
	protected final App app;
	
	public MenuThingy(App app) {
		super();
		this.app = app;
		initComps();
	}

	protected void initComps() {
		
		/* File */
        JMenu FILE = new Menu("File");
        FILE.setMnemonic(KeyEvent.VK_F);
        FILE.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        FILE.setToolTipText("File");
        JMenuItem SAVE = new MenuItem("Save");
        SAVE.addActionListener(e -> app.megaSave());
        SAVE.setMnemonic(KeyEvent.VK_S);
        SAVE.setToolTipText("Save a file");
        FILE.add(SAVE);
        JMenuItem OPEN = new MenuItem("Open");
        OPEN.addActionListener(e -> app.openfile(true));
        OPEN.setMnemonic(KeyEvent.VK_O);
        OPEN.setToolTipText("Open a file in SynEdit");
        FILE.add(OPEN);
        JMenuItem CLOSE = new MenuItem("Close");
        CLOSE.addActionListener(e -> app.close());
        CLOSE.setMnemonic(KeyEvent.VK_C);
        CLOSE.setToolTipText("Close SynEdit");
        FILE.add(CLOSE);
        add(FILE);

		/* Help */
        JMenu HELP = new Menu("Help");
        HELP.setMnemonic(KeyEvent.VK_F);
        HELP.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        HELP.setToolTipText("Help");
        JMenuItem PREFRENCES = new MenuItem("Prefrences");
        PREFRENCES.addActionListener(e -> 
		JOptionPane.showMessageDialog(null,
				"Menu is WIP, please edit the settings directly in the SynEdit.properties file in the"
				+ "SynEdit Directory",
				"WIP", JOptionPane.WARNING_MESSAGE));
        PREFRENCES.setMnemonic(KeyEvent.VK_S);
        PREFRENCES.setToolTipText("Edit SynEdit's prefrences");
        HELP.add(PREFRENCES);
        add(HELP);
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.getBackground());
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}

class MenuItem extends JMenuItem {
	public MenuItem(String str) {
		super(str);
        setOpaque(true);
        setBackground(MenuBG);
        setForeground(MenuFG);
	}
}
class Menu extends JMenu {
	public Menu(String str) {
		super(str);
        setOpaque(true);
        setBackground(MenuBG);
        setForeground(MenuFG);
	}
}