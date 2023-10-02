package NNU.Editor.Menus;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import NNU.Editor.App;
import NNU.Editor.Utils.Utils;

public class AboutMenu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6893937793741839803L;

	public AboutMenu() {
		setSize(300, 350);
		setLocation(App.screenSize.width/2-this.getSize().width/2,
				App.screenSize.height/2-this.getSize().height/2);
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel(Utils.EDITORNAME);
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 264, 66);
		getContentPane().add(lblNewLabel);

		String hex = "#"+Integer.toHexString(Color.LIGHT_GRAY.getRGB()).substring(2);
		
		JLabel lblByNgspace = new JLabel("<html><span color=\"" + hex + "\">By "
				+ "<em><strong>"
				+ "<span color=\"red\">NGS</span>"
				+ "pace</strong></em>\r\n</span>"
				+ "</html>");
		lblByNgspace.setHorizontalAlignment(SwingConstants.CENTER);
		lblByNgspace.setFont(new Font("Monospaced", Font.BOLD, 36));
		lblByNgspace.setBounds(10, 79, 264, 37);
		getContentPane().add(lblByNgspace);
		
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
                    } catch (URISyntaxException | IOException ex) {
                            //It looks like there's a problem
                    }
            }
        });
		getContentPane().add(lblGithubPage);
		setVisible(false);
	}
}
