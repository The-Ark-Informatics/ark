/**
 * TransitionDetailsDialog.java
 * Copyright ? 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 11/02/2004
 */

package media.neuragenix.genix.workflow.dialog;

/**
 * This class creates dialog to capture workflow transition
 * details.
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;

// javax packages
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

// jGraph
import org.jgraph.JGraph;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;

import media.neuragenix.genix.workflow.org.jgraph.pad.*;

// neuragenix packages
import neuragenix.genix.workflow.*;

public class TransitionDetailsDialog extends JDialog
{
    private static final int DIALOG_WIDTH = 350;
    private static final int DIALOG_HEIGHT = 350;
    
    
    private JTextField tfID, tfName;
    private JComboBox cbType;
    private JTextArea taCondition;
    private JButton btOK, btCancel;
    
    private DefaultEdge cell;
    private JGraph graph;
    
    
    /** Creates a new instance of TransitionDetailsDialog */
    public TransitionDetailsDialog(Object cell, Object graph)
    {
        super((JFrame) null, "Transition details", true);
        getContentPane().setLayout(new BorderLayout());
        this.cell = (DefaultEdge) cell;
        this.graph = (JGraph) graph;
        
        getContentPane().add(createCentralPanel());
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
    }
    
    
    /**
     * Create central panel for the dialog
     */
    private JPanel createCentralPanel()
    {
        Transition transition = null;
        if (cell != null && cell.getUserObject() != null) {
            if (cell.getUserObject() instanceof Transition)
            {
                transition = (Transition) cell.getUserObject();
            }
        }
        
        int row_no = 0;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        // row 1
        JLabel label = new JLabel("ID");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfID = new JTextField(transition == null ? media.neuragenix.genix.workflow.DesignPanel.getNextGenId() : transition.getId());
        tfID.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                       DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        tfID.setBackground(java.awt.Color.lightGray);
        tfID.setToolTipText("This is a system generated ID");    
        tfID.setEnabled(false);
        
        panel.add(tfID);
        
        
        // row 2
        row_no++;
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfName = new JTextField(transition == null ? "" : transition.getName());
        tfName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfName);
        
     
        // row 3
        row_no++;
        label = new JLabel("Type");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        cbType = new JComboBox(DialogConstants.TRANSITION_TYPES);
        cbType.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                         DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbType);
        
        if (transition != null)
            cbType.setSelectedItem(transition.getType());
        
        // row 4
        row_no++;
        label = new JLabel("Condition");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        taCondition = new JTextArea(transition == null ? "" : transition.getCondition());
        taCondition.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.MED_TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        JScrollPane areaScrollPane = new JScrollPane(taCondition);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.MED_TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        
        panel.add(areaScrollPane);
        
        
        // last row
        row_no += 4;
        btOK = new JButton("OK");
        btOK.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tfID.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(null, "A value must be entered for the ID before the details can be saved", "Details Missing", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    doSaveTransition();
                }
                //TransitionDetailsDialog.this.dispose();
            }
        });
        panel.add(btOK);
        
        btCancel = new JButton("Cancel");
        btCancel.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TransitionDetailsDialog.this.dispose();
            }
        });
        panel.add(btCancel);
        
        return panel;
    }
    
    
    private void doSaveTransition()
    {
        Transition transition = null;     
        String strXCoordinator = "";
        String strYCoordinator = "";
        
        // getting source and target activities
        DefaultPort from_port = (DefaultPort) cell.getSource();
        DefaultPort to_port = (DefaultPort) cell.getTarget();
        DefaultGraphCell from_cell = (DefaultGraphCell) graph.getModel().getParent(from_port);
        DefaultGraphCell to_cell = (DefaultGraphCell) graph.getModel().getParent(to_port);
        
        // changing cell attributes
        Hashtable cell_attrs = (Hashtable) cell.getAttributes();
        ArrayList points = (ArrayList) cell_attrs.get("points");
        for (int i=0; i < points.size(); i++)
        {
            Point point = (Point) points.get(i);
            strXCoordinator += point.x + " ";
            strYCoordinator += point.y + " ";
        }
        
        // create transition
        /* 
         * There is no need to check if connected to an EndCell, 
         * as every new EndCell created will now have an attribute
         * associated to the cell.
         *
        if (to_cell instanceof EndCell)
        {
                      
            Activity from_activity = (Activity) from_cell.getUserObject();
                 
            // create transition object
            transition = new Transition(tfID.getText(),
                                         from_activity.getId(),
                                          "",
                                         tfName.getText(),
                                         taCondition.getText(),
                                         (String) cbType.getSelectedItem(),
                                         strXCoordinator.trim(), 
                                         strYCoordinator.trim());
            
        }
        else
        {
        */
            Activity from_activity = (Activity) from_cell.getUserObject();
            Activity to_activity = (Activity) to_cell.getUserObject();
        
            // create transition object
            transition = new Transition(tfID.getText(),
                                         from_activity.getId(),
                                         to_activity.getId(),
                                         tfName.getText(),
                                         taCondition.getText(),
                                         (String) cbType.getSelectedItem(),
                                         strXCoordinator.trim(), 
                                         strYCoordinator.trim());
        //}
        
        // set user object
        cell.setUserObject(transition);        
        cell_attrs.put("value", transition);
        
        //Enumeration enum = cell_attrs.keys();
        //while (enum.hasMoreElements())
            //System.err.println(enum.nextElement());
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(cell, cell_attrs);
        // Insert the Vertex and its Attributes (can also use model)
        graph.getGraphLayoutCache().edit(attributes, null, null, null);
        
        
        // close the dialog
        TransitionDetailsDialog.this.dispose();
    }
}
