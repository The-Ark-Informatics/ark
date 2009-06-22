/*
 * WorkflowDesigner.java
 *
 * Created on June 30, 2003, 11:40 AM
 */

package media.neuragenix.bio.workflow;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import neuragenix.common.*;

/**
 *
 * @author  laptops'n'memory
 */
public class WorkflowDesigner extends JApplet {
    // icons for button
    ImageIcon newIcon = new ImageIcon(WorkflowDesigner.class.getResource("images/new.gif"));
    ImageIcon saveIcon = new ImageIcon(WorkflowDesigner.class.getResource("images/save.gif"));
    ImageIcon deleteIcon = new ImageIcon(WorkflowDesigner.class.getResource("images/delete.gif"));
    ImageIcon personIcon = new ImageIcon(WorkflowDesigner.class.getResource("images/person.gif"));
    ImageIcon machineIcon = new ImageIcon(WorkflowDesigner.class.getResource("images/machine.gif"));
    ImageIcon joinIcon = new ImageIcon(WorkflowDesigner.class.getResource("images/and_join.gif"));
    ImageIcon transitionIcon = new ImageIcon(WorkflowDesigner.class.getResource("images/transition.gif"));
    
    JButton btNew, btSave, btDelete;
    JToggleButton btManual, btAutomatic, btJoin, btTransition;
    
    JLabel lbMessage;
    
    DesignPanel rightPanel;
    
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() {
        createToolbar();
        createMainPanel();
    }
    
    /** Create toolbar for the applet
     *
     */
    private void createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        getContentPane().add(toolbar, BorderLayout.NORTH);
        toolbar.addSeparator(new Dimension(300,0));
        
        // create button for creating a new workflow
        btNew = new JButton(newIcon);
        btNew.setToolTipText("new workflow");
        toolbar.add(btNew);
        btNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rightPanel.removeAll();
                rightPanel.setCurrentAction(DesignPanel.NORMAL);
                rightPanel.repaint();
            }
        });
        
        // create button for saving a workflow
        btSave = new JButton(saveIcon);
        btSave.setToolTipText("save workflow");
        toolbar.add(btSave);
        
        // create button for deleting a workflow
        btDelete = new JButton(deleteIcon);
        btDelete.setToolTipText("delete workflow");
        toolbar.add(btDelete);
        toolbar.addSeparator(new Dimension(50,0));
        
        // create button for creating a new manual activity
        btManual = new JToggleButton(personIcon);
        btManual.setToolTipText("new manual activity");
        btManual.setSelected(true);
        toolbar.add(btManual);
        btManual.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                rightPanel.setCurrentAction(DesignPanel.NORMAL);
                rightPanel.unselectedOthers(null);
                if (e.getClickCount() == 2)
                {
                    //rightPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    ActivityComponent bt = new ActivityComponent(ActivityComponent.MANUAL);
                    rightPanel.add(bt);
                    bt.setBounds(200, 200, 100, 50);
                    rightPanel.unselectedOthers(bt);
                }   
            }
        });
        
        // create button for creating a new automatic activity
        btAutomatic = new JToggleButton(machineIcon);
        btAutomatic.setToolTipText("new automatic activity");
        toolbar.add(btAutomatic);
        btAutomatic.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                rightPanel.setCurrentAction(DesignPanel.NORMAL);
                rightPanel.unselectedOthers(null);
                if (e.getClickCount() == 2)
                {
                    //rightPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    ActivityComponent bt = new ActivityComponent(ActivityComponent.AUTOMATIC);
                    rightPanel.add(bt);
                    bt.setBounds(200, 200, 100, 50);
                    rightPanel.unselectedOthers(bt);
                }   
            }
        });
        
        btJoin = new JToggleButton(joinIcon);
        btJoin.setToolTipText("new join activity");
        toolbar.add(btJoin);
        
        btTransition = new JToggleButton(transitionIcon);
        btTransition.setToolTipText("new transition");
        toolbar.add(btTransition);
        btTransition.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                rightPanel.setCurrentAction(DesignPanel.NEW_TRANSITION);
                rightPanel.unselectedOthers(null);
            }
        });
        
        //lbMessage = new JLabel("Message");
        //toolbar.add(lbMessage);
        
        ButtonGroup btg = new ButtonGroup();
        btg.add(btManual);
        btg.add(btAutomatic);
        btg.add(btJoin);
        btg.add(btTransition);
    }
    
    private void createMainPanel() {
        final JTree workflowTree = createWorkflowList();
        JScrollPane leftScrollPane = new JScrollPane(workflowTree);
        leftScrollPane.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.BLACK), "Workflow list"));
        
        rightPanel = new DesignPanel();
        rightPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                rightPanel.unselectedOthers(null);
                rightPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.BLACK), "Design frame"));
        
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        sp.setDividerLocation(getWidth()/4);
        getContentPane().add(sp);
    }
    
    private JTree createWorkflowList() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Neuragenix Workflows");
        createNodes(root);
        JTree workflowTree = new JTree(root);
        return workflowTree;
    }
    
    private void createNodes(DefaultMutableTreeNode root) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        category = new DefaultMutableTreeNode("Add new patient");
        root.add(category);

        //original Tutorial
        book = new DefaultMutableTreeNode("Fill in new patient form");
        category.add(book);

        //Tutorial Continued
        book = new DefaultMutableTreeNode("Validate patient data");
        category.add(book);

        //JFC Swing Tutorial
        book = new DefaultMutableTreeNode("Displaying error");
        category.add(book);

        //Arnold/Gosling
        book = new DefaultMutableTreeNode("Select patient consented files");
        category.add(book);

        //FAQ
        book = new DefaultMutableTreeNode("Download consented files");
        category.add(book);

        category = new DefaultMutableTreeNode("Patient appointment email alert");
        root.add(category);

        //VM
        book = new DefaultMutableTreeNode("Finding appointments");
        category.add(book);

        //Language Spec
        book = new DefaultMutableTreeNode("Create email alert process");
        category.add(book);

    }

    protected static ImageIcon createImageIcon(String path) {
        return new ImageIcon(WorkflowDesigner.class.getResource(path));
    }
    
}
