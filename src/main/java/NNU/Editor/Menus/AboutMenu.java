package NNU.Editor.Menus;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import NNU.Editor.App;
import NNU.Editor.Main;
import NNU.Editor.AssetManagement.StringTable;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Interfaces.Window;

public class AboutMenu extends JPanel {

	private static final long serialVersionUID = 6893937793741839803L;
	public final Window window;
	public final App app;

	public AboutMenu(Window window, App app) {
        
		this.app = app;
		this.window = window;
		setSize(300, 350);
		setLocation(App.screenSize.width/2-this.getSize().width/2,
				App.screenSize.height/2-this.getSize().height/2);
		setLayout(null);
        this.setBackground(new Color(10,10,12));
        this.setForeground(Color.LIGHT_GRAY);
		
		JLabel lblNewLabel = new JLabel(Utils.EDITORNAME);
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 264, 66);
		add(lblNewLabel);
		
		JLabel lblByNgspace = new JLabel("NGSpace");
		lblByNgspace.setForeground(Color.red);
		lblByNgspace.setHorizontalAlignment(SwingConstants.CENTER);
		lblByNgspace.setFont(new Font("Monospaced", Font.BOLD, 36));
		lblByNgspace.setBounds(10, 79, 264, 37);
		add(lblByNgspace);

		JLabel lblGithubPage = new JLabel(StringTable.getString("about.github"));
		lblGithubPage.setHorizontalAlignment(SwingConstants.CENTER);
		lblGithubPage.setForeground(Color.blue.brighter());
		lblGithubPage.setFont(new Font("Courier New", Font.BOLD, 24));
		lblGithubPage.setBounds(10, 127, 264, 25);
		lblGithubPage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGithubPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                	Desktop.getDesktop().browse(new URI("https://github.com/NGSpace/NNUEdit"));
                } catch (Exception ex) {
                	ex.printStackTrace();
                }
            }
        });
		add(lblGithubPage);
		JLabel lblVersion = new JLabel(StringTable.getString("about.version",Main.Version));
		lblVersion.setForeground(Color.LIGHT_GRAY);
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setFont(new Font("Courier New", Font.BOLD, 24));
		lblVersion.setBounds(10, 187, 264, 50);
		add(lblVersion);
		JLabel lblSystem = new JLabel(Main.SYSTEM);
		lblSystem.setForeground(Color.LIGHT_GRAY);
		lblSystem.setHorizontalAlignment(SwingConstants.CENTER);
		lblSystem.setFont(new Font("Courier New", Font.BOLD, 24));
		lblSystem.setBounds(10, 237, 264, 50);
		add(lblSystem);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		window.getScrollPane().paintSeperators((Graphics2D)g);
	}
}
