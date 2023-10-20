package NNU.Editor.Utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

public class SwingUtils {
	private SwingUtils() {}
	public static void animate(JComponent component, Point newPoint, int frames, int interval) {
		
	    Rectangle compBounds = component.getBounds();

	    Point oldPoint = new Point(compBounds.x, compBounds.y);
	    Point animFrame = new Point((newPoint.x - oldPoint.x) / frames,
	                                (newPoint.y - oldPoint.y) / frames);
	    if (animFrame.x==0) animFrame.setLocation(1, animFrame.y);
	    if (animFrame.y==0) animFrame.setLocation(animFrame.x, 1);

	    new Timer(interval, new ActionListener() {
	        int currentFrame = 0;
	        public void actionPerformed(ActionEvent e) {
	            component.setBounds(oldPoint.x + (animFrame.x * currentFrame),
	                                oldPoint.y + (animFrame.y * currentFrame),
	                                compBounds.width, compBounds.height);

	            if (currentFrame != frames)
	                currentFrame++;
	            else
	                ((Timer)e.getSource()).stop();
	        }
	    }).start();
	}
	public static void animateSize(JComponent component, Dimension Dim, int frames, int interval) {
	    Rectangle oldDim = component.getBounds();

	    double wdifperframe = (Dim.width - oldDim.width) / (double) frames;
	    
	    new Timer(interval, new ActionListener() {
	        int currentFrame = 0;
	        public void actionPerformed(ActionEvent e) {
	        	System.out.println(wdifperframe);
	            component.setSize(
	            		(int)(component.getWidth() + (wdifperframe < 1 ? 1 : wdifperframe)),
                        component.getHeight());

	            if (currentFrame != frames)
	                currentFrame++;
	            else
	                ((Timer)e.getSource()).stop();
	        }
	    }).start();
	}
}
