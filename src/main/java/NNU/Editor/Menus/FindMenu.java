package NNU.Editor.Menus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import NNU.Editor.SyntaxTextArea;

public class FindMenu extends JPanel {
	
	private static final long serialVersionUID = -3066669034146785706L;
	protected SyntaxTextArea txtArea;
	protected static boolean CaseSensitivity = false;
	protected JTextField txtTest;
	protected String str = "";
	
	int off = 0;
	private JTextField ReplaceTxt;

	public FindMenu(SyntaxTextArea txtArea) {
		super();
		this.txtArea = txtArea;
		setLayout(null);
		setBounds(0,0,551,92);
		
		txtTest = new JTextField();
		txtTest.setText("");
		txtTest.setFont(new Font("Courier New", Font.BOLD, 18));
		txtTest.setBounds(10, 11, 269, 30);
		add(txtTest);
		txtTest.setColumns(10);
		
		JButton btnNewButton = new JButton("Find Next");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FindNext(txtTest.getText());
			}
		});
		btnNewButton.setBounds(289, 11, 99, 30);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(btnNewButton);
		
		JButton btnFindAll = new JButton("Find All");
		btnFindAll.setBounds(398, 11, 99, 30);
		btnFindAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FindAll(txtTest.getText());
			}
		});
		btnFindAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(btnFindAll);
		
		ReplaceTxt = new JTextField();
		ReplaceTxt.setText("");
		ReplaceTxt.setFont(new Font("Courier New", Font.BOLD, 18));
		ReplaceTxt.setColumns(10);
		ReplaceTxt.setBounds(10, 52, 269, 30);
		add(ReplaceTxt);
		
		JButton btnReplaceNext = new JButton("Replace Next");
		btnReplaceNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ReplaceNext(txtTest.getText(),ReplaceTxt.getText());
			}
		});
		btnReplaceNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnReplaceNext.setBounds(289, 52, 99, 30);
		add(btnReplaceNext);


		
		/* Change case sensitivity */
		JButton btnReplaceAll = new JButton("Replace All");
		btnReplaceAll.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				txtArea.setText(txtArea.getText().replace(txtTest.getText(), ReplaceTxt.getText()));
			}
		});
		btnReplaceAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnReplaceAll.setBounds(398, 52, 99, 30);
		add(btnReplaceAll);

		
		/* Change case sensitivity */
		JCheckBox chckbxNewCheckBox = new JCheckBox("Aa");
		chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {CaseSensitivity = !CaseSensitivity;}
		});
		chckbxNewCheckBox.setToolTipText("Case Sensitivity");
		chckbxNewCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		chckbxNewCheckBox.setBounds(500, 52, 38, 30);
		chckbxNewCheckBox.setSelected(CaseSensitivity);
		add(chckbxNewCheckBox);
		
		
		/* Close menu */
		JButton XButton = new XButton();
		XButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtArea.remove(txtArea.fm);
				txtArea.repaint();
			}
		});
		XButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		XButton.setBounds(507, 11, 30, 30);
		add(XButton);
		this.setVisible(true);
	}
	
	public void FindAll(String searchReq) {
		new Thread(() -> {
    	Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter
    		(new Color(80,80,0));

    	String txt = CaseSensitivity ? txtArea.getText() : txtArea.getText().toLowerCase();
    	String search = CaseSensitivity ? searchReq : searchReq.toLowerCase();

		txtArea.getHighlighter().removeAllHighlights();
		
		int offset = indexOf(txt,search);
		int length = search.length();

		while (offset != -1) {
		    try {
		    	txtArea.getHighlighter().addHighlight(offset, offset + length, painter);
		    	//txtArea.select
		    	//txtArea.select(offset, offset + length);
		    	txtArea.setCaretPosition(offset + length);
				offset = indexOf(txt,search, offset+1);
			} catch (BadLocationException e1) {e1.printStackTrace();}
		}
		}).start();
	}
	
	public void FindNext(String searchReq) {
		new Thread(() -> {
    	Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter
    		(new Color(80,80,0));

    	String txt = CaseSensitivity ? txtArea.getText() : txtArea.getText().toLowerCase();
    	String search = CaseSensitivity ? searchReq : searchReq.toLowerCase();

    	off = txtArea.getCaretPosition();
		off = indexOf(txt,search,off);
		if (off==-1) {
			off = 0;
			off = indexOf(txt,search,off);
			if (off==-1) {
				JOptionPane.showMessageDialog(null, "Could not find " + searchReq, 
						"Could not find", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		int length = search.length();
    	try {
			txtArea.getHighlighter().removeAllHighlights();
			txtArea.getHighlighter().addHighlight(off, off + length, painter);

	    	txtArea.select(off, off + length);
	    	txtArea.setCaretPosition(off + length);
	    	off++;
		} catch (BadLocationException e) {e.printStackTrace();
		}
    	
		}).start();
	}
	
	public void ReplaceNext(String searchReq, String Replace) {
		new Thread(() -> {
    	String txt = CaseSensitivity ? txtArea.getText() : txtArea.getText().toLowerCase();
    	String search = CaseSensitivity ? searchReq : searchReq.toLowerCase();
    	
    	off=txtArea.getCaretPosition();
		off = indexOf(txt,search,off);
		if (off==-1) {
			off = 0;
			off = indexOf(txt,search,off);
			if (off==-1) {
				JOptionPane.showMessageDialog(null, "Could not find " + searchReq, 
						"Could not find", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		int length = search.length();
    	txtArea.getHighlighter().removeAllHighlights();

		txtArea.select(off, off + length);
		txtArea.replaceSelection(Replace);
		txtArea.setCaretPosition(off + Replace.length());
		off++;
		}).start();
	}

	protected int indexOf(String txt, String search, int i) {
		return txt.indexOf(search,i);
	}

	protected int indexOf(String txt, String search) {
		return txt.indexOf(search);
	}
}

class XButton extends JButton {

	private static final long serialVersionUID = 7562656481186873433L;

	@Override public void paint(Graphics gra) {
		Graphics2D g = (Graphics2D) gra;
		//g.setColor(App.MenuFG.brighter());
		g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(5, 5, getWidth()-5, getHeight()-5);
		g.drawLine(getWidth()-5 , 5, 5, getHeight()-5);
	}
}
