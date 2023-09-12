package NNU.Editor;

import static NNU.Editor.App.MenuBG;
import static NNU.Editor.App.MenuFG;
import static NNU.Editor.Utils.EditorName;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * the menubar
 */
public class MenuThingy extends JMenuBar {
	
	private static final long serialVersionUID = -6781221982811029019L;
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
        OPEN.addActionListener(e -> app.textArea.openfile(true));
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
        HELP.setMnemonic(KeyEvent.VK_H);
        HELP.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        HELP.setToolTipText("Help");
        JMenuItem PREFRENCES = new MenuItem("Prefrences");
        PREFRENCES.addActionListener(e -> new PreferencesMenu(app.stng,app));
        PREFRENCES.setMnemonic(KeyEvent.VK_P);
        PREFRENCES.setToolTipText("Edit " + EditorName + "'s prefrences");
        HELP.add(PREFRENCES);
        JMenuItem CREDITS = new MenuItem("About");
        CREDITS.addActionListener(e -> new AboutMenu());
        CREDITS.setMnemonic(KeyEvent.VK_P);
        CREDITS.setToolTipText("About " + EditorName);
        HELP.add(CREDITS);
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
	private static final long serialVersionUID = 9131978348527133180L;

	public MenuItem(String str) {
		super(str);
        setOpaque(true);
        setBackground(MenuBG);
        setForeground(MenuFG);
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