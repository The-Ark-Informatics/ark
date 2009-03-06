/*
 * SelectedBorder.java
 *
 * Created on July 3, 2003, 12:22 PM
 */

package media.neuragenix.bio.workflow.border;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
/**
 *
 * @author  laptops'n'memory
 */
public class SelectedBorder implements Border {
    
    int inset_w;
    int inset_h;
    int line_thickness = 2;
    Color color = Color.BLACK;
    
    /** Creates a new instance of SelectedBorder */
    public SelectedBorder() {
        inset_w = 4;
        inset_h = 4;
    }
    
    public Insets getBorderInsets(Component c) {
        return new Insets(inset_h, inset_w, inset_h, inset_w);
    }
    
    public boolean isBorderOpaque() {
        return true;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        int hline_w = (w - 3*inset_w)/2;
        int vline_w = (h - 3*inset_w)/2;
        
        int p1_x = x;
        int p2_x = p1_x + inset_w;
        int p3_x = p2_x + hline_w;
        int p4_x = p3_x + inset_w;
        int p5_x = p1_x + w - inset_w - 1;
        int p6_x = p1_x + w - inset_w + line_thickness;
        int p1_y = y;
        int p2_y = p1_y + inset_h;
        int p3_y = p2_y + vline_w;
        int p4_y = p3_y + inset_h;
        int p5_y = p1_y + h - inset_h - 1;
        int p6_y = p1_y + h - inset_h + line_thickness;
        
        g.setColor(color);
        g.drawRect(p1_x, p1_y, inset_w, inset_h);
        g.drawRect(p3_x, p1_y, inset_w, inset_h);
        g.drawRect(p5_x, p1_y, inset_w, inset_h);
        g.drawRect(p1_x, p3_y, inset_w, inset_h);
        g.drawRect(p5_x, p3_y, inset_w, inset_h);
        g.drawRect(p1_x, p5_y, inset_w, inset_h);
        g.drawRect(p3_x, p5_y, inset_w, inset_h);
        g.drawRect(p5_x, p5_y, inset_w, inset_h);
        
        g.fillRect(p2_x, p1_y, hline_w, line_thickness);
        g.fillRect(p4_x, p1_y, hline_w, line_thickness);
        g.fillRect(p2_x, p6_y, hline_w, line_thickness);
        g.fillRect(p4_x, p6_y, hline_w, line_thickness);
        g.fillRect(p1_x, p2_y, line_thickness, vline_w);
        g.fillRect(p1_x, p4_y, line_thickness, vline_w);
        g.fillRect(p6_x, p2_y, line_thickness, vline_w);
        g.fillRect(p6_x, p4_y, line_thickness, vline_w);
    }
}
