/*
 * DesignPanel.java
 *
 * Created on July 3, 2003, 4:22 PM
 */

package media.neuragenix.bio.workflow;

import java.awt.*;

import javax.swing.*;
/**
 *
 * @author  laptops'n'memory
 */
public class DesignPanel extends JPanel {
    public static final int NORMAL = 0;
    public static final int NEW_TRANSITION = 1;
    
    static final Color BACKGROUND_COLOR = Color.WHITE;
    static final LayoutManager LAYOUT = null;
    
    private int currentAction;
    
    
    /** Creates a new instance of DesignPanel */
    public DesignPanel() {
        super();
        init();
    }
    
    private void init() {
        setBackground(BACKGROUND_COLOR);
        setLayout(LAYOUT);
        currentAction = NORMAL;
    }
    
    public void setCurrentAction(int ca) {
        currentAction = ca;
    }
    
    public int getCurrentAction() {
        return currentAction;
    }
    
    public void unselectedOthers(Component current) {
        Component[] children = getComponents();
        
        for (int i=0; i < children.length; i++)
        {
            Component c = children[i];
            if (c instanceof ActivityComponent && c != current)
                ((ActivityComponent) c).setSelected(false);
        }   
    }
    
    public void reLocateActivities(Component current) {
        
    }
    
    public void reLocateTransition() {
        Component[] children = getComponents();
        
        for (int i=0; i < children.length; i++)
        {
            Component c = children[i];
            if (c instanceof TransitionComponent)
                ((TransitionComponent) c).setLocation();
        }   
    }
    
    public void resize() {
        int maxWidth = 0;
        int maxHeight = 0;
        
        Component[] children = getComponents();
        
        for (int i=0; i < children.length; i++)
        {
            Component c = children[i];
            int maxXTemp = c.getX() + c.getWidth();
            int maxYTemp = c.getY() + c.getHeight();
            
            if (maxWidth < maxXTemp)
                maxWidth = maxXTemp;
            if (maxHeight < maxYTemp)
                maxHeight = maxYTemp;
        }
        
        setPreferredSize(new Dimension(maxWidth, maxHeight));
        revalidate();
    }
}
