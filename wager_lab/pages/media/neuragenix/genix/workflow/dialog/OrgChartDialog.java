/**
 * ActivityDetailsDialog.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 10/02/2004
 */

package media.neuragenix.genix.workflow.dialog;

/**
 * This class creates dialog to capture workflow activity
 * details.
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import java.util.Iterator;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;

// javax packages
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

// jGraph
import org.jgraph.JGraph;
import org.jgraph.graph.*;

// neuragenix packages
import neuragenix.genix.workflow.*;
import media.neuragenix.genix.workflow.DesignPanel;
import media.neuragenix.genix.workflow.org.jgraph.pad.*;

public class OrgChartDialog extends JDialog
{
    
    JTree orgchartTree;
    DefaultMutableTreeNode orgchartTreeModel;
    int type;
    
    /** Creates a new instance of ActivityDetailsDialog */
    public OrgChartDialog(ActivityDetailsDialog parent, DefaultMutableTreeNode orgchartTreeModel, int intType)
    {
        
        super(parent, "Organisation chart", true);
        this.orgchartTreeModel = orgchartTreeModel;
        type = intType;
        // set layout to Border layout
        getContentPane().setLayout(new BorderLayout());
        
        JScrollPane treeView = new JScrollPane(createCentralPanel());
        getContentPane().add(treeView);
        
        getContentPane().add(createSouthPanel(), BorderLayout.SOUTH);
        
        setSize(500, 300);
        setResizable(false);
    }
    
    /**
     * Create central panel for the dialog
     */
    private JPanel createCentralPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        orgchartTree = new JTree(orgchartTreeModel);
        orgchartTree.setEditable(false);
        orgchartTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        //DefaultTreeModel treeModel = new DefaultTreeModel(orgchartTreeModel);
        //orgchartTree = new JTree(treeModel);

        panel.add(orgchartTree);
        
        return panel;
        
        
        //Where the tree is initialized:

    }
    
    /**
     * Create SOUTH panel
     */
    private JPanel createSouthPanel()
    {
        JPanel panel = new JPanel();
        JButton btOK = new JButton("OK");
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove all paramereters
                if (orgchartTree.getSelectionCount() == 0)
                {
                    JOptionPane.showMessageDialog(null, "A performer has not been selected from the Organisation Chart", "No Performer", JOptionPane.ERROR_MESSAGE);
                }
                
                else
                {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) orgchartTree.getLastSelectedPathComponent();
                    ActivityDetailsDialog parent = (ActivityDetailsDialog) OrgChartDialog.this.getParent();

                    switch (type)
                    {
                        case 0: parent.setPerformer(selectedNode.getUserObject()); break;
                        case 1: parent.setTriggerSendTo(selectedNode.getUserObject()); break;
                        case 2: parent.setFinishSendTo(selectedNode.getUserObject()); break;
                    }
                
                OrgChartDialog.this.dispose();
                //System.err.println(orgchartTree.getLastSelectedPathComponent());
                }
            }
        });
        panel.add(btOK);
        
        JButton btCancel = new JButton("Cancel");
        btCancel.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                OrgChartDialog.this.dispose();
            }
        });
        
        panel.add(btCancel);
        
        
        
        orgchartTree.clearSelection();
        
        return panel;
    }
    
}