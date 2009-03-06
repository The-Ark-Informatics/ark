/*
 * TransitionComponent.java
 *
 * Created on July 4, 2003, 11:28 AM
 */

package media.neuragenix.bio.workflow;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;
/**
 *
 * @author  laptops'n'memory
 */
public class TransitionComponent extends JComponent {
    static final int[] X_POINTS = {0, -15, -15};
    static final int[] Y_POINTS = {0, -6, 6};
    static final int OUTBOUNDS = 7;
    
    static final Color SELECTED_COLOR = Color.RED;
    static final Color UNSELECTED_COLOR = Color.BLACK;
    
    private ActivityComponent fromActivity, toActivity;
    private int x1, x2, y1, y2, x_topleft, y_topleft, width, height;
    private double theta;
    
    /** Creates a new instance of TransitionComponent */
    public TransitionComponent(ActivityComponent from, ActivityComponent to) {
        fromActivity = from;
        toActivity = to;
    }
    
    public void setLocation() {
        int x1_from = fromActivity.getX();
        int x2_from = x1_from + fromActivity.getWidth()/2;
        int x3_from = x1_from + fromActivity.getWidth();
        int x4_from = x2_from;
        int y2_from = fromActivity.getY();
        int y1_from = y2_from + fromActivity.getHeight()/2;
        int y3_from = y1_from;
        int y4_from = y2_from + fromActivity.getHeight();
        
        int x1_to = toActivity.getX();
        int x2_to = x1_to + toActivity.getWidth()/2;
        int x3_to = x1_to + toActivity.getWidth();
        int x4_to = x2_to;
        int y2_to = toActivity.getY();
        int y1_to = y2_to + toActivity.getHeight()/2;
        int y3_to = y1_to;
        int y4_to = y2_to + toActivity.getHeight();
        
        if (x3_from < x1_to) {
            if (y3_from <= y1_to) {
                x1 = 0; y1 = 0; x2 = x1_to - x3_from; y2 = y1_to - y3_from;
                setBounds(x3_from - OUTBOUNDS, y3_from - OUTBOUNDS, x2 + 2*OUTBOUNDS, y2 + 2*OUTBOUNDS);
            }
            else {
                x1 = 0; y1 = y3_from - y1_to; x2 = x1_to - x3_from; y2 = 0;
                setBounds(x3_from - OUTBOUNDS, y1_to - OUTBOUNDS, x2 + 2*OUTBOUNDS, y1 + 2*OUTBOUNDS);
            }
            return;
        }
        
        if (x1_from > x3_to) {
            if (y1_from >= y3_to) {
                x1 = x1_from - x3_to; y1 = y1_from - y3_to; x2 = 0; y2 = 0;
                setBounds(x3_to - OUTBOUNDS, y3_to - OUTBOUNDS, x1 + 2*OUTBOUNDS, y1 + 2*OUTBOUNDS);
            }
            else {
                x1 = x1_from - x3_to; y1 = 0; x2 = 0; y2 = y3_to - y1_from;
                setBounds(x3_to - OUTBOUNDS, y1_from - OUTBOUNDS, x1 + 2*OUTBOUNDS, y2 + 2*OUTBOUNDS);
            }
            return;
        }
        
        if (y4_from <= y2_to) {
            if (x4_from >= x2_to) {
                x1 = x4_from - x2_to; y1 = 0; x2 = 0; y2 = y2_to - y4_from;
                setBounds(x2_to - OUTBOUNDS, y4_from - OUTBOUNDS, x1 + 2*OUTBOUNDS, y2 + 2*OUTBOUNDS);
            }
            else {
                x1 = 0; y1 = 0; x2 = x2_to - x4_from; y2 = y2_to - y4_from;
                setBounds(x4_from - OUTBOUNDS, y4_from - OUTBOUNDS, x2 + 2*OUTBOUNDS, y2 + 2*OUTBOUNDS);
            }
            return;
        }
        
        if (y4_to <= y2_from) {
            if (x4_to >= x2_from) {
                x1 = 0; y1 = y2_from - y4_to; x2 = x4_to - x2_from; y2 = 0;
                setBounds(x2_from - OUTBOUNDS, y4_to - OUTBOUNDS, x2 + 2*OUTBOUNDS, y1 + 2*OUTBOUNDS);
            }
            else {
                x1 = x2_from - x4_to; y1 = y2_from - y4_to; x2 = 0; y2 = 0;
                setBounds(x4_to - OUTBOUNDS, y4_to - OUTBOUNDS, x1 + 2*OUTBOUNDS, y1 + 2*OUTBOUNDS);
            }
            return;
        }
        
        setBounds(0,0,0,0);
    }
    
    private void calculateTheta() {
        if (x1 == x2) {
            if (y1 <= y2)
                theta = Math.PI/2;
            else
                theta = -Math.PI/2;
        }
        else {
            if (x1 <= x2)
                theta = Math.atan(((double) (y2 - y1))/(x2 - x1));
            else
                theta = Math.atan(((double) (y2 - y1))/(x2 - x1)) + Math.PI;
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //setLocation();
        calculateTheta();
        
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(UNSELECTED_COLOR);
        g2D.translate(OUTBOUNDS, OUTBOUNDS);
        g2D.drawLine(x1, y1, x2, y2);
        
        g2D.translate(x2, y2);
        g2D.rotate(theta);
        g2D.fillPolygon(X_POINTS, Y_POINTS, 3);
    }
}