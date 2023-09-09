package NNU.SynEdit;

import static NNU.SynEdit.App.MenuBG;
import static NNU.SynEdit.App.MenuFG;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
        JMenu FILE = new Menu("File");
        FILE.setMnemonic(KeyEvent.VK_F);
        FILE.setToolTipText("It's fucking file settings genius");
        JMenuItem SAVE = new MenuItem("Save");
        SAVE.setMnemonic(KeyEvent.VK_S);
        FILE.add(SAVE);
        JMenuItem OPEN = new MenuItem("Open");
        OPEN.addActionListener(e -> app.openfile(true));
        OPEN.setMnemonic(KeyEvent.VK_O);
        FILE.add(OPEN);
        JMenuItem CLOSE = new MenuItem("Close");
        CLOSE.addActionListener(e -> app.close());
        CLOSE.setMnemonic(KeyEvent.VK_O);
        FILE.add(CLOSE);
        add(FILE);
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