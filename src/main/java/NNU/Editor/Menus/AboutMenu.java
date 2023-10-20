package NNU.Editor.Menus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import NNU.Editor.App;
import NNU.Editor.Main;
import NNU.Editor.Menus.Components.FolderButton;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Window;

public class AboutMenu extends JPanel implements FolderButton {

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
		
		JLabel lblNewLabel = new JLabel(Utils.EDITORNAME);
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 264, 66);
		add(lblNewLabel);
        this.setBackground(new Color(10,10,12));
        this.setForeground(Color.LIGHT_GRAY);

		String hex = "#"+Integer.toHexString(Color.LIGHT_GRAY.getRGB()).substring(2);
		
		JLabel lblByNgspace = new JLabel("<html><span color=\"" + hex + "\">By "
				+ "<em><strong>"
				+ "<span color=\"red\">NGS</span>"
				+ "pace</strong></em>\r\n</span>"
				+ "</html>");
		lblByNgspace.setHorizontalAlignment(SwingConstants.CENTER);
		lblByNgspace.setFont(new Font("Monospaced", Font.BOLD, 36));
		lblByNgspace.setBounds(10, 79, 264, 37);
		add(lblByNgspace);

		JLabel lblGithubPage = new JLabel(
				"<html><a href=\"https://github.com/NGSpace/NNUEdit\">Github page</a></html>");
		lblGithubPage.setHorizontalAlignment(SwingConstants.CENTER);
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
		JLabel lblVersion = new JLabel("<html><span color=\"" + hex + "\">By "
				+ "<em><strong>"
				+ "Version: " + Main.Version + "</strong></em>\r\n</span>"
				+ "</html>");
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setFont(new Font("Courier New", Font.BOLD, 24));
		lblVersion.setBounds(10, 187, 264, 25);
		add(lblVersion);
        
        initjl(this,window.getScrollPane());
    	//resizeButton(window.getScrollPane());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.paintFB(g, window.getScrollPane());
		g.setColor(app.contentpane.getBackground());
		((Graphics2D)g).setStroke(new BasicStroke(10));
		g.drawLine(0, 0, 0, getHeight());
	}
	public final JButton jl = new JButton(">") {
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
