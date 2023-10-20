package NNU.Editor.Menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import NNU.Editor.App;
import NNU.Editor.Menus.Components.FolderButton;
import NNU.Editor.Windows.Window;

public class GitMenu extends JPanel implements FolderButton {

	private static final long serialVersionUID = 6893937793741839803L;
	public final Window window;
	public final App app;

	public GitMenu(Window window, App app) {
		this.app = app;
		this.window = window;
		setSize(300, 350);
		setLocation(App.screenSize.width/2-this.getSize().width/2,
				App.screenSize.height/2-this.getSize().height/2);
		setLayout(null);
		
		JTextPane red = new JTextPane();
		red.setBounds(100,100,100,100);
		add(red);
		JButton jb = new JButton();
		jb.setBounds(200,200,200,200);
		jb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				App.MenuBG = new Color(Integer.valueOf(red.getText()), 100, 100);
				for (Component c : app.getComponents()) {
					c.setBackground(App.MenuBG);
				}
			}
		});
		add(jb);
		
		//g.add().addFilepattern(".").call();
		//g.commit().setMessage("Test").call();
		//g.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("username", "")).call();
        this.setBackground(new Color(10,10,12));
        this.setForeground(Color.LIGHT_GRAY);
        
        initjl(this,window.getScrollPane());
    	//resizeButton(window.getScrollPane());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.paintFB(g, window.getScrollPane());
	}
	public final JButton jl = new JButton() {
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