package io.github.ngspace.nnuedit.menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import io.github.ngspace.nnuedit.asset_manager.StringTable;
import io.github.ngspace.nnuedit.utils.user_io.UserMessager;

public class FindMenu extends JPanel implements DocumentListener {
	
	private static final long serialVersionUID = -3066669034146785706L;
	protected EditorTextArea txtArea;
	protected boolean CaseSensitivity = false;
	protected JTextField txtFind;
	protected String str = "";
	
	int off = 0;
	private JTextField ReplaceTxt;

	public FindMenu(EditorTextArea txtArea) {
		super();
		this.txtArea = txtArea;
		setLayout(null);
		setBounds(0,0,551,92);
		
		txtFind = new JTextField();
		txtFind.setText("");
		txtFind.setFont(new Font("Courier New", Font.BOLD, 18));
		txtFind.setBounds(10, 11, 269, 30);
		txtFind.setColumns(10);
		txtFind.getDocument().addDocumentListener(this);
		add(txtFind);
		
		JButton btnNewButton = new JButton(StringTable.get("find.findnext"));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				findNext(txtFind.getText());
			}
		});
		btnNewButton.setBounds(289, 11, 99, 30);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(btnNewButton);
		
		ReplaceTxt = new JTextField();
		ReplaceTxt.setText("");
		ReplaceTxt.setFont(new Font("Courier New", Font.BOLD, 18));
		ReplaceTxt.setColumns(10);
		ReplaceTxt.setBounds(10, 52, 269, 30);
		add(ReplaceTxt);
		
		
		/* Replace Next*/
		JButton btnReplaceNext = new JButton(StringTable.get("find.replacenext"));
		btnReplaceNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				replaceNext(txtFind.getText(),ReplaceTxt.getText());
			}
		});
		btnReplaceNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnReplaceNext.setBounds(289, 52, 99, 30);
		add(btnReplaceNext);


		/* Replace All */
		JButton btnReplaceAll = new JButton(StringTable.get("find.replaceall"));
		btnReplaceAll.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				txtArea.setText(txtArea.getText().replace(txtFind.getText(), ReplaceTxt.getText()));
			}
		});
		btnReplaceAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnReplaceAll.setBounds(398, 52, 99, 30);
		add(btnReplaceAll);

		
		/* Change case sensitivity */
		JCheckBox chckbxNewCheckBox = new JCheckBox("Aa");
		chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				CaseSensitivity = !CaseSensitivity;
			}
		});//UserMessager.input("", "input.newfile");
		chckbxNewCheckBox.setToolTipText(StringTable.get("find.case"));
		chckbxNewCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		chckbxNewCheckBox.setBounds(500, 52, 38, 30);
		chckbxNewCheckBox.setSelected(CaseSensitivity);
		add(chckbxNewCheckBox);
		
		
		/* Close menu */
		JButton XButton = new XButton();
		XButton.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				txtArea.escape();
			}
		});
		XButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		XButton.setBounds(507, 11, 30, 30);
		add(XButton);
		this.setVisible(true);

        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
        		Collections.emptySet());
		this.setFocusTraversalKeysEnabled(false);

		txtFind.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyChar() == KeyEvent.VK_TAB) {
					toggle();
				} else if (event.getKeyChar() == KeyEvent.VK_ENTER) {
					enterKey();
				}
			}
	    });
		ReplaceTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyChar() == KeyEvent.VK_TAB) {
					toggle();
				} else if (event.getKeyChar() == KeyEvent.VK_ENTER) {
					enterKey();
				}
			}
	    });
		
        this.requestFocus();
	}
	
	public Component getFocusedComponent() {
		return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
	}

	public void enterKey() {
		if (getFocusedComponent()==txtFind) {
			findNext(txtFind.getText());
		} else {replaceNext(txtFind.getText(), ReplaceTxt.getText());}
	}
	
	public void toggle() {
		if (getFocusedComponent()==txtFind) {
			ReplaceTxt.requestFocus();
		} else {txtFind.requestFocus();}
	}
	
	public void findAll(String searchReq) {
		new Thread(() -> {
    	Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter
    		(new Color(80,80,0));

    	String txt = CaseSensitivity ? txtArea.getText() : txtArea.getText().toLowerCase();
    	String search = CaseSensitivity ? searchReq : searchReq.toLowerCase();

		txtArea.getHighlighter().removeAllHighlights();
		if ("".equals(search)) return;
		
		int offset = indexOf(txt,search);
		int length = search.length();

		while (offset != -1) {
		    try {
		    	txtArea.getHighlighter().addHighlight(offset, offset + length, painter);
				offset = indexOf(txt,search, offset+1);
			} catch (BadLocationException e1) {e1.printStackTrace();}
		}
		}).start();
	}
	
	public void findNext(String searchReq) {
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
				UserMessager.showErrorDialogTB("editor.err.cantfind.title","editor.err.cantfind",searchReq);
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
	
	public void replaceNext(String searchReq, String Replace) {
		new Thread(() -> {
    	String txt = CaseSensitivity ? txtArea.getText() : txtArea.getText().toLowerCase();
    	String search = CaseSensitivity ? searchReq : searchReq.toLowerCase();
    	
    	off=txtArea.getCaretPosition();
		off = indexOf(txt,search,off);
		if (off==-1) {
			off = 0;
			off = indexOf(txt,search,off);
			if (off==-1) {
				UserMessager.showErrorDialogTB("editor.err.cantfind.title","editor.err.cantfind",searchReq);
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

	@Override public void insertUpdate(DocumentEvent e)  {findAll(txtFind.getText());}

	@Override public void removeUpdate(DocumentEvent e)  {findAll(txtFind.getText());}

	@Override public void changedUpdate(DocumentEvent e) {findAll(txtFind.getText());}
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
