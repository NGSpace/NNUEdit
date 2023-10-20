package NNU.Editor.Menus.Components;

import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import NNU.Editor.App;
import NNU.Editor.Utils.ValueNotFoundException;

public class NGSScrollPane extends JScrollPane {

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
		setBorder(new EmptyBorder(0,0,0,0));
		this.setDoubleBuffered(false);
		getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		getVerticalScrollBar().addAdjustmentListener(e ->repaint());
	}

	@Override public void paintComponents(Graphics g) {
		if (isVisible()) {
			for (Component i :this.getComponents())
				if (i.isVisible()) {
					i.paintAll(g.create(i.getX(), i.getY(), i.getWidth(), i.getHeight()));
				}
		}
	}
	
	int i = 0;
	@Override public void paint(Graphics g) {
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paint(g);
		//paintComponents(g);
		//paintLines(getViewport().getGraphics());
		/*if (this.getViewport().getView() instanceof FolderButton) {
			FolderButton folderButton = ((FolderButton)getViewport().getView());
			Point position = getViewport().getViewPosition();
			JButton jl = folderButton.getFolderButton();
			folderButton.resizeButton(this);
			folderButton.paintFolderButton((Graphics2D) g.create((int)position.getX(),
			(int) (getHeight()/2d-jl.getHeight()),
			jl.getWidth(), jl.getHeight()),jl.getWidth(),jl.getHeight());
		}((FolderButton)getViewport().getView()).paintFolderButton(null, WIDTH, HEIGHT);*/
		//g.setColor(app.contentpane.getBackground());
		//((Graphics2D)g).setStroke(new BasicStroke(10));
		//g.drawLine(0, 0, 0, getHeight());
		g.dispose();
	}
	
	public void paintLines(Graphics g) {
		try {
			//new Exception("Stack Dump: paintLines").printStackTrace();
			if (getViewport().getView() instanceof JTextComponent 
					&& app.stng.getBoolean("numberlines")) {
		    	//Instant start = Instant.now();
				if (app.stng.getBoolean("textantialias"))
					((Graphics2D)g).setRenderingHint(
				        RenderingHints.KEY_TEXT_ANTIALIASING,
				        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				JTextComponent i = (JTextComponent) getViewport().getView();
				g.setColor(getForeground());
				g.setFont(i.getFont());
				long y = -10;
				long x;
				Point position = getViewport().getViewPosition();
				FontMetrics gfm = g.getFontMetrics();
				int newlines = "".equals(i.getText()) ? 1 : Number;
				Rectangle r = getViewport().getViewRect();
				//int ir = 0;
				int start = r.y/g.getFontMetrics().getHeight();
				y+=g.getFontMetrics().getHeight()*start;
				for (int j = start;j<newlines;j++) {
					String s = String.valueOf(j+1);
					x = gfm.stringWidth(s) - (long) position.x + (getVerticalScrollBar().isVisible() ?
							getVerticalScrollBar().getWidth() : 0);
					y+=g.getFontMetrics().getHeight();
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
		} catch (ValueNotFoundException e) {e.printStackTrace();}
	}
	
    public void countLines(JTextComponent textArea) {
        /*AttributedString text = new AttributedString(textArea.getText());
        FontRenderContext frc = textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();
        AttributedCharacterIterator charIt = text.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
        float formatWidth = textArea.getSize().width;
        lineMeasurer.setPosition(charIt.getBeginIndex());

        int noLines = 0;
        while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
            lineMeasurer.nextLayout(formatWidth);
            noLines++;
        }

        Element map = textArea.getDocument().getDefaultRootElement();
        int lineEnter =  map.getElementCount();
        //int lineEnter = textArea.getLineCount();
        int countLine = noLines + lineEnter;

        Number = countLine-1;*/
        Document d = textArea.getDocument();
        try {
			Number = d.getText(0, d.getLength()).split("\r\n|\r|\n",-1).length;
		} catch (BadLocationException e) {e.printStackTrace();}
    }

}
