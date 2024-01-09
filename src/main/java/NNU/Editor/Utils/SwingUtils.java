package NNU.Editor.Utils;

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
}
