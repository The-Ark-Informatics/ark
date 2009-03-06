/*
 * ActivityJComponent.java
 *
 * Created on July 1, 2003, 10:21 AM
 */

package media.neuragenix.bio.workflow;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.LineBorder;

import media.neuragenix.bio.workflow.border.SelectedBorder;
/**
 *
 * @author  laptops'n'memory
 */
public class ActivityComponent extends JButton {
    public static final int MANUAL = 0;
    public static final int AUTOMATIC = 1;
    
    static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
    static final Color SELECTED_COLOR = Color.RED;
    static final Color UNSELECTED_COLOR = Color.BLACK;
    static final int BORDER_THICKNESS = 2;
    static final LineBorder SELECTED_BORDER = new LineBorder(SELECTED_COLOR, BORDER_THICKNESS);
    static final LineBorder UNSELECTED_BORDER = new LineBorder(UNSELECTED_COLOR, BORDER_THICKNESS);
    static final ImageIcon MANUAL_ICON = new ImageIcon(WorkflowDesigner.class.getResource("images/person.gif"));
    static final ImageIcon AUTO_ICON = new ImageIcon(WorkflowDesigner.class.getResource("images/machine.gif"));
    
    static boolean isControlKeyPressed = false;
    
    private boolean isSelected;
    private int x_offset;
    private int y_offset;
    private int type;
    
    /** Creates a new instance of ActivityJComponent */
    public ActivityComponent(int type) {
        this.type = type;
        
        switch (type)
        {
            case MANUAL: 
                setIcon(MANUAL_ICON);
                break;
            case AUTOMATIC:
                setIcon(AUTO_ICON);
        }
        
        init();
    }
    
    private void init() {
        setText("Activity");
        isSelected = true;
        setBackground(BACKGROUND_COLOR);
        setBorder(SELECTED_BORDER);
        ActivityMouseListener listener = new ActivityMouseListener();
        addMouseMotionListener(listener);
        addMouseListener(listener);
        addKeyListener(new ActivityKeyListener());
    }
    
    public void setSelected(boolean isSelect) {
        isSelected = isSelect;
        setBorder(isSelected ? SELECTED_BORDER : UNSELECTED_BORDER);
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setXOffset(int x_off) {
        x_offset = x_off;
    }
    
    public void setYOffset(int y_off) {
        y_offset = y_off;
    }
    
    class ActivityMouseListener extends MouseInputAdapter {
        int x_differ, y_differ;
        boolean firstTime = true;
        
        public void mouseDragged(MouseEvent e) {
            DesignPanel panel = (DesignPanel) ActivityComponent.this.getParent();
            
            if (firstTime) {
                ActivityComponent.this.setSelected(true);
                x_differ = e.getX();
                y_differ = e.getY();
                firstTime = false;
                
                /*if (!isControlKeyPressed) {
                    panel.unselectedOthers(ActivityComponent.this);
                }*/
                
                return;
            }
            
            int newX = ActivityComponent.this.getX() + e.getX() - x_differ;
            int newY = ActivityComponent.this.getY() + e.getY() - y_differ;
            if (newX < 0) newX = 0;
            if (newY < 0) newY = 0;
            int width = ActivityComponent.this.getWidth();
            int height = ActivityComponent.this.getHeight();
            
            ActivityComponent.this.setBounds(newX, newY, width, height);
            panel.reLocateActivities(ActivityComponent.this);
            panel.reLocateTransition();
            panel.resize();
        }
        
        public void mouseClicked(MouseEvent e) {
            ActivityComponent.this.setSelected(true);
            if (!isControlKeyPressed)
            {
                DesignPanel panel = (DesignPanel) ActivityComponent.this.getParent();
                if (panel.getCurrentAction() == DesignPanel.NEW_TRANSITION)
                {
                    Component[] children = panel.getComponents();
                    Component from = null;
                    
                    for (int i=0; i < children.length; i++)
                    {
                        Component c = children[i];
                        if (c instanceof ActivityComponent && c != ActivityComponent.this &&
                            ((ActivityComponent) c).isSelected())
                        {
                            from = c;
                            break;
                        }
                    }
                    
                    if (from != null) {
                        TransitionComponent newTransition = new TransitionComponent((ActivityComponent) from, ActivityComponent.this);
                        panel.add(newTransition);
                    }
                }
                
                panel.unselectedOthers(ActivityComponent.this);
                panel.reLocateTransition();
            }
        }
        
        public void mouseReleased(MouseEvent e) {
            firstTime = true;
        }
    }
    
    class ActivityKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                isControlKeyPressed = true;
        }
        
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                isControlKeyPressed = false;
        }
    }
}