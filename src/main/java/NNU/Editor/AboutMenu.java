package NNU.Editor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class AboutMenu extends JFrame {

	public AboutMenu() {
		setSize(300, 350);
		this.setLocation(App.screenSize.width/2-this.getSize().width/2,
				App.screenSize.height/2-this.getSize().height/2);
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("NNUEdit");
		lblNewLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 264, 66);
		getContentPane().add(lblNewLabel);
		
		JLabel lblByNgspace = new JLabel("<html>By "
				+ "<em><strong>"
				+ "<span color=\"red\">NGS</span>"
				+ "pace</strong></em>\r\n"
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
		getContentPane().add(lblGithubPage);
		setVisible(true);
	}
}
