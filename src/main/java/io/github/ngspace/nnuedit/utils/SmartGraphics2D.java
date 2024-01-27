package io.github.ngspace.nnuedit.utils;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class SmartGraphics2D extends Graphics2D {

	private Graphics2D g;
	
	private Rectangle r = new Rectangle(1000,1000);

	public SmartGraphics2D(Graphics2D g, Rectangle r) {
		this.g = g;
		this.r = r;
	}

	@Override
	public void draw(Shape s) {
		g.draw(s);
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return g.drawImage(img, xform, obs);
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		if (isInside(x, y))
			g.drawImage(img, op, x, y);
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		g.drawRenderedImage(img, xform);
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		g.drawRenderableImage(img,xform);
	}

	@Override
	public void drawString(String str, int x, int y) {
		if (isInside(x, y, g.getFontMetrics().stringWidth(str), (int) g.getFont().getSize2D()))
			g.drawString(str,x, y);
	}

	@Override
	public void drawString(String str, float x, float y) {
		if (isInside(x, y, g.getFontMetrics().stringWidth(str), (int) g.getFont().getSize2D()))
			g.drawString(str,x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		if (isInside(x, y))
			g.drawString(iterator,x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		if (isInside((int)x, (int)y))
			g.drawString(iterator,x, y);
	}

	@Override
	public void drawGlyphVector(GlyphVector gr, float x, float y) {
		if (isInside((int)x, (int)y))
			g.drawGlyphVector(gr,x, y);
	}

	@Override
	public void fill(Shape s) {
		g.fill(s);
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return g.hit(rect, s, onStroke);
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return g.getDeviceConfiguration();
	}

	@Override
	public void setComposite(Composite comp) {
		g.setComposite(comp);
	}

	@Override
	public void setPaint(Paint paint) {
		g.setPaint(paint);
	}

	@Override
	public void setStroke(Stroke s) {
		g.setStroke(s);
	}

	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		g.setRenderingHint(hintKey, hintValue);
	}

	@Override
	public Object getRenderingHint(Key hintKey) {
		return g.getRenderingHint(hintKey);
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		g.setRenderingHints(hints);
	}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		g.addRenderingHints(hints);
	}

	@Override
	public RenderingHints getRenderingHints() {
		return g.getRenderingHints();
	}

	@Override
	public void translate(int x, int y) {
		g.translate(x, y);
	}

	@Override
	public void translate(double tx, double ty) {
		g.translate(tx, ty);
	}

	@Override
	public void rotate(double theta) {
		g.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		g.rotate(theta, x, y);
	}

	@Override
	public void scale(double sx, double sy) {
		g.scale(sx, sy);
	}

	@Override
	public void shear(double shx, double shy) {
		g.shear(shx, shy);
	}

	@Override
	public void transform(AffineTransform Tx) {
		g.transform(Tx);
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		g.setTransform(Tx);
	}

	@Override
	public AffineTransform getTransform() {
		return g.getTransform();
	}

	@Override
	public Paint getPaint() {
		return g.getPaint();
	}

	@Override
	public Composite getComposite() {
		return g.getComposite();
	}

	@Override
	public void setBackground(Color color) {
		g.setBackground(color);
	}

	@Override
	public Color getBackground() {
		return g.getBackground();
	}

	@Override
	public Stroke getStroke() {
		return g.getStroke();
	}

	@Override
	public void clip(Shape s) {
		g.clip(s);
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return g.getFontRenderContext();
	}

	@Override
	public Graphics create() {
		return g.create();
	}

	@Override
	public Color getColor() {
		return g.getColor();
	}

	@Override
	public void setColor(Color c) {
		g.setColor(c);
	}

	@Override
	public void setPaintMode() {
		g.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		g.setXORMode(c1);
	}

	@Override
	public Font getFont() {
		return g.getFont();
	}

	@Override
	public void setFont(Font font) {
		g.setFont(font);
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return g.getFontMetrics(f);
	}

	@Override
	public Rectangle getClipBounds() {
		return g.getClipBounds();
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		if (isInside(x, y, width, height))
			g.clipRect(x, y, width, height);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		if (isInside(x, y, width, height))
			g.setClip(x, y, width, height);
	}

	@Override
	public Shape getClip() {
		return g.getClip();
	}

	@Override
	public void setClip(Shape clip) {
		g.setClip(clip);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		if (isInside(dx, dy, width, height))
			g.copyArea(x, y, width, height,dx,dy);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		if (isInside(x1, y1))
			g.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		if (isInside(x, y, width, height))
			g.fillRect(x, y, width, height);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		if (isInside(x, y, width, height))
			g.clearRect(x, y, width, height);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		if (isInside(x, y, width, height))
			g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		if (isInside(x, y, width, height))
			g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {}

	@Override
	public void fillOval(int x, int y, int width, int height) {}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		if (isInside(x, y))
			return g.drawImage(img, x, y, observer);
		else
			return true;
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		if (isInside(x, y, width, height))
			return g.drawImage(img, x, y, width, height, observer);
		else
			return true;
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		if (isInside(x, y))
			return g.drawImage(img, x, y, bgcolor, observer);
		else
			return true;
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		if (isInside(x, y, width, height))
			return g.drawImage(img, x, y, width, height, bgcolor, observer);
		else
			return true;
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
		return g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2,sy2, bgcolor, observer);
	}

	@Override
	public void dispose() {
		g.dispose();
	}
	
	public boolean isInside(int x, int y) {
		return isInside(x,y,1,1);
	}
	
	public static int SuccessfulDrawCalls = 0;

	private boolean isInside(int x, int y, int Width, int height) {
		boolean res = r.intersects(new Rectangle(x,y,Width,height));
		if (res) SuccessfulDrawCalls++; /* Success is measurable :) */
		return res;
	}

	private boolean isInside(float x, float y, int stringWidth, int size2d) {
		return isInside((int) x, (int) y, stringWidth, size2d);
	}

}
