package NNU.Editor.Menus.Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import NNU.Editor.App;
import NNU.Editor.EditorTextArea;
import NNU.Editor.Utils.Unfinnished;

public class NGSScrollPane extends JScrollPane implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 3952255173637165838L;
	boolean CTRL_HELD = false;
	private App app;
	public int Number = 0;

	public NGSScrollPane(App app, Container contentPane) {
		super(contentPane);
		this.app=app;
		init();
	}
	
	public NGSScrollPane(App app) {
		super();
		this.app=app;
		init();
	}
	
	boolean temp = true;
	
	public void init() {
        setBorder(null);
        setOpaque(true);
		setDoubleBuffered(true);
		//getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getVerticalScrollBar().addAdjustmentListener(e ->repaint());
        getHorizontalScrollBar().addAdjustmentListener(e ->repaint());
		
		//NGSScrollPane thiz = this;
		
		setViewport(new JViewport() {
			private static final long serialVersionUID = -8588144347690129055L;
			@Override public void setView(Component view) {
				if (view instanceof JTextComponent) {
					JTextComponent obj = (JTextComponent) view;
					//obj.addMouseListener(thiz);
					//obj.addMouseMotionListener(thiz);
					countLines(obj);
				}
				super.setView(view);
			}
		});
	}
	
	@Override public void paint(Graphics g) {
		//g.fillRect(0, 0, getWidth(), getHeight());
		super.paint(g);
		g.dispose();
	}
	
	@Override public void repaint() {revalidate();super.repaint();}
	
	public void paintLines(Graphics g) {
		if (getViewport().getView() instanceof EditorTextArea 
				&& App.stng.getBoolean("editor.numberlines")) {
			//Instant start = Instant.now();
			app.adjustAntialias(g,false);
			EditorTextArea i = (EditorTextArea) getViewport().getView();
			g.setColor(getForeground());
			g.setFont(i.getFont());
			long y = -(g.getFontMetrics().getHeight()/4);
			long x;
			Point position = getViewport().getViewPosition();
			FontMetrics gfm = g.getFontMetrics();
			int newlines = "".equals(i.getText()) ? 1 : Number;
			Rectangle r = getViewport().getViewRect();
			//int ir = 0;
			int start = r.y/g.getFontMetrics().getHeight();
			int offset = 0;
			y+=g.getFontMetrics().getHeight()*start;
			for (int j = start;j+offset<newlines;j++) {
				String s = String.valueOf(j+1+offset);
				x = gfm.stringWidth(s) - (long) position.x + (getVerticalScrollBar().isVisible() ?
						getVerticalScrollBar().getWidth() : 0);
				y+=g.getFontMetrics().getHeight();
				try {
					if (i.getFoldManager().getFoldForLine(j+offset)!=null
							&&i.getFoldManager().getFoldForLine(j+offset).isCollapsed ()
							&&i.getFoldManager().getFoldForLine(j+offset).getEndOffset()
							!=Integer.MAX_VALUE) {
						
						offset+=i.getLineOfOffset(i.getFoldManager().getFoldForLine(j+offset)
								.getEndOffset())-j-offset;
					}
					if (i.getFoldManager().getFoldForLine(j+offset)!=null) {
						g.setColor(Color.red);
					} else {
						g.setColor(getForeground());
					}
				} catch (Exception e) {
					System.err.println(e.getMessage()+" j : "+j+" offset : "+offset );
					break;
				}
				if (y>r.y+getHeight()) break;
				if (y>r.y&&y-getHeight()<r.y) {
					g.drawString(s, (int) (getWidth()-x), (int) y);
				}
				//ir++;
			}
			
			//System.out.println(ir);
			/*Instant end = Instant.now();
			Duration dur = Duration.between(start, end);
			System.out.println("milli" + dur.toMillis());*/
		}
	}
	
    public void countLines(JTextComponent textArea) {
        Document d = textArea.getDocument();
        try {
			Number = d.getText(0, d.getLength()).split("\r\n|\r|\n",-1).length;
		} catch (BadLocationException e) {e.printStackTrace();}
    }

	public void paintSeperators(Graphics2D gra) {
		Point position = getViewport().getViewPosition();
		gra.setColor(app.contentpane.getBackground());
		gra.setStroke(new BasicStroke(10));
		//gra.drawLine(position.x, position.y, position.x, getHeight() + position.y);
		gra.drawRect(position.x, position.y, App.getBuffer() - 5, getHeight());
	}

	@Unfinnished
	@Override public void mouseClicked(MouseEvent e)  {
		/*if (e.getComponent() instanceof RSyntaxTextArea) {
			RSyntaxTextArea comp = (RSyntaxTextArea) e.getComponent();
			FoldManager foldman = comp.getFoldManager();
			Graphics g = comp.getGraphics();
			g.setFont(comp.getFont());
			int y = g.getFontMetrics().getHeight();
			int linenumber = e.getY()/y;
			linenumber -=foldman.getHiddenLineCountAbove(linenumber);
			FontMetrics gfm = g.getFontMetrics();
			int x = gfm.stringWidth(Number+"") + (getVerticalScrollBar().isVisible() ?
					getVerticalScrollBar().getWidth() : 0);
			if (e.getX() + comp.getX()>getWidth()-x &&  (foldman.getFoldForLine(linenumber)!=null)) {
					foldman.getFoldForLine(linenumber).toggleCollapsedState();
			}
		}*/
	}
	@Unfinnished
	@Override public void mouseMoved(MouseEvent e)    {
		/*if (e.getComponent() instanceof RSyntaxTextArea) {
			RSyntaxTextArea comp = (RSyntaxTextArea) e.getComponent();
			FoldManager foldman = comp.getFoldManager();
			
			Graphics g = comp.getGraphics();
			g.setFont(comp.getFont());
			int y = g.getFontMetrics().getHeight();
			int linenumber = e.getY()/y;
			linenumber -=foldman.getHiddenLineCountAbove(linenumber);
			FontMetrics gfm = g.getFontMetrics();
			int x = gfm.stringWidth(linenumber+"") + (getVerticalScrollBar().isVisible() ?
					getVerticalScrollBar().getWidth() : 0);
			if (e.getX() + comp.getX()>getWidth()-x && foldman.getFoldForLine(linenumber)!=null) {
				comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else {
				comp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}*/
	}
	
	

	@Override public void mousePressed(MouseEvent e)  {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e)  {}
	@Override public void mouseExited(MouseEvent e)   {}
	@Override public void mouseDragged(MouseEvent e)  {}

}
