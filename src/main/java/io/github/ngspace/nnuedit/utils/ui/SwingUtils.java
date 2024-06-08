package io.github.ngspace.nnuedit.utils.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

public class SwingUtils {private SwingUtils() {}
    public static void animate(JComponent component, Point newPoint, int frames, int interval, Runnable finish) {
	    Rectangle compBounds = component.getBounds();
	    
		Point oldPos = new Point(compBounds.x, compBounds.y);
		Point animFrame = new Point((newPoint.x - oldPos.x) / frames, (newPoint.y - oldPos.y) / frames);
		
	    new Timer(interval, new ActionListener() {
	        int frame = 0;
	        public void actionPerformed(ActionEvent e) {
	        	if (component.isEnabled())
	        		component.setLocation(oldPos.x + (animFrame.x * frame), oldPos.y + (animFrame.y * frame));
	            if (frame==frames) {
	            	finish.run();
	                ((Timer)e.getSource()).stop();
	                return;
	            }
	        	frame++;
	        }
	    }).start();
	}
}