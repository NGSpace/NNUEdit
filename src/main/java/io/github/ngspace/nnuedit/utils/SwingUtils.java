package io.github.ngspace.nnuedit.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

public class SwingUtils {

    public static void animate(JComponent component, Point newPoint, int frames, int interval, Runnable finish) {
	    Rectangle compBounds = component.getBounds();
	    
		Point oldPoint = new Point(compBounds.x, compBounds.y);
		Point animFrame = new Point((newPoint.x - oldPoint.x) / frames, (newPoint.y - oldPoint.y) / frames);
		
	    new Timer(interval, new ActionListener() {
	        int currentFrame = 0;
	        public void actionPerformed(ActionEvent e) {
	            component.setBounds(oldPoint.x + (animFrame.x * currentFrame),
	                                oldPoint.y + (animFrame.y * currentFrame),
	                                compBounds.width, compBounds.height);

	            if (currentFrame != frames)
	                currentFrame++;
	            else {
	            	finish.run();
	                ((Timer)e.getSource()).stop();
	            }
	        }
	    }).start();
	}
    public static void animater(JComponent component, int sizeChange, int frames, int interval, boolean goinside) {
    	animater(component, sizeChange, frames, interval, goinside, ()->{});
    }
    
    @Broken
    public static void animater( JComponent component, int sizeChange, int frames, int interval,
    		boolean goinside, Runnable finish) {
	    Rectangle compBounds = component.getBounds();
	    
		Point pos = new Point(compBounds.x, compBounds.y);
		double perframe = (sizeChange/frames) ;
		
	    new Timer(interval, new ActionListener() {
	        int currentFrame = 0;
	        public void actionPerformed(ActionEvent e) {
	            component.setBounds((int) (pos.x - perframe),
	                                (int) (pos.y - perframe),
	                                compBounds.width, compBounds.height);

	            if (currentFrame != frames)
	                currentFrame++;
	            else {
	            	finish.run();
	                ((Timer)e.getSource()).stop();
	            }
	        }
	    }).start();
	}
}
