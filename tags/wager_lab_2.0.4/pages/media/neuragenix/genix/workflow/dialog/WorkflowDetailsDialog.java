/**
 * WorkflowDetailsDialog.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 11/02/2004
 */

package media.neuragenix.genix.workflow.dialog;

/**
 * This class creates dialog to capture workflow details.
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;

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
import org.jgraph.graph.*;

// neuragenix packages
import neuragenix.genix.workflow.*;
import media.neuragenix.genix.workflow.DesignPanel;

public class WorkflowDetailsDialog extends JDialog
{
    private static final int DIALOG_WIDTH = 350;
    private static final int DIALOG_HEIGHT = 320;
    
    
    private JTextField tfID, tfName, tfPackage;
    private JTextArea taDescription;
    private JComboBox cbPackage;
    private JButton btOK, btCancel;
    private boolean isAnUpdate = false;
    
    String currentProcessName, currentProcessId, currentPackageName, currentPackageId = "";
    
    private DesignPanel design_panel;
    
    
    /** Creates a new instance of WorkflowDetailsDialog */
    public WorkflowDetailsDialog(DesignPanel design_panel)
    {
        super((JFrame) null, "Save workflow", true);
        getContentPane().setLayout(new BorderLayout());
        this.design_panel = design_panel;
        
        getContentPane().add(createCentralPanel());
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
    }
    
    /** To allow for a Save As Operation, this
     * constructor will enable checking so that the new name
     * for the workflow isn't the same as the current
     * one
     * @param design_panel Reference to the designer being used
     * @param isAnUpdate boolean to switch on ID and Naming validation against
     * workflow in the specified designer
     * @param currentProcessName Name of the current workflow process in the designer
     * @param currentProcessId Id of the current workflow process in the designer
     * @param currentPackageName Name of the package with which this workflow resides
     * @param currentPackageId ID of the current package with which this workflow resides
     */    
    public WorkflowDetailsDialog(DesignPanel design_panel, 
                                 boolean isAnUpdate,
                                 String currentProcessName,
                                 String currentProcessId,
                                 String currentPackageName,
                                 String currentPackageId)
    {
    
        super((JFrame) null, "Save workflow as...", true);
        
        this.isAnUpdate = isAnUpdate;
        
        this.currentProcessName = currentProcessName;
        this.currentProcessId = currentProcessId;
        this.currentPackageName = currentPackageName;
        this.currentPackageId = currentPackageId;
        
        
        
        getContentPane().setLayout(new BorderLayout());
        this.design_panel = design_panel;
        
        getContentPane().add(createCentralPanel());
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
    
    }
    
    
    
    
    private JPanel createCentralPanel()
    {
        int row_no = 0;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        // row 1
        JLabel label = new JLabel("ID");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfID = new JTextField();
        tfID.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                       DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfID);
        
        
        // row 2
        row_no++;
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfName = new JTextField();
        tfName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfName);
        
        
        // row 3
        row_no++;
        label = new JLabel("Package");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfPackage = new JTextField();
        tfPackage.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfPackage);
        
        /*cbPackage = new JComboBox();
        Vector vtPackages = design_panel.getWFDesigner().getPackages();
        if (vtPackages != null)
        {
            for (int i=0; i < vtPackages.size(); i++)
                cbPackage.addItem(vtPackages.get(i));
        }
        cbPackage.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbPackage);
        */
     
        // row 4
        row_no++;
        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        taDescription = new JTextArea();
        taDescription.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        JScrollPane areaScrollPane = new JScrollPane(taDescription);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        
        panel.add(areaScrollPane);
        
        
        // last row
        row_no += 4;
        btOK = new JButton("OK");
        btOK.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isAnUpdate == true)
                {
                    boolean isValid = true;
                    String errorMessage = "There were the following problems that prevented saving : \n";
                    // check that the name that is about to be used isn't the same as the current one
                    if (tfName.getText().equals(currentProcessName) || tfName.getText().equals(""))
                    {
                        isValid = false;
                        errorMessage = errorMessage + " - Name of the workflow must be unique \n";
                    }
                    if (tfID.getText().equals(currentProcessId) || tfID.getText().equals(""))
                    {
                        isValid = false;
                        errorMessage = errorMessage + " - ID of the workflow must be unique \n";
                    }
                    
                    if (tfPackage.getText().equals(""))
                    {
                        isValid = false;
                        errorMessage = errorMessage + " - A package name must be entered \n";
                    }
                    if (isValid == false)
                    {
                        JOptionPane.showMessageDialog(null, errorMessage, "Unable to Save", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                else
                {
                    boolean isValid = true;
                    String errorMessage = "There were the following problems that prevented saving : \n";
                    // check that the name that is about to be used isn't the same as the current one
                    if (tfName.getText().equals(""))
                    {
                        isValid = false;
                        errorMessage = errorMessage + " - Name of the workflow must be entered and be unique ";
                    }
                    if (tfID.getText().equals(""))
                    {
                        isValid = false;
                        errorMessage = errorMessage + " - ID of the workflow must be entered and be unique ";
                    }
                    if (tfPackage.getText().equals(""))
                    {
                        isValid = false;
                        errorMessage = errorMessage + " - A package name must be entered \n";
                    }
                    if (isValid == false)
                    {
                        JOptionPane.showMessageDialog(null, errorMessage, "Unable to Save", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
               
                }
                
                String packageName = tfPackage.getText();
                String packageID = packageName;
                String workflowName = tfName.getText();
                String workflowID = tfID.getText();
                design_panel.saveWorkflow(packageID, packageName, workflowID, workflowName);

                WorkflowDetailsDialog.this.dispose();
               
            }
        });
        panel.add(btOK);
        
        btCancel = new JButton("Cancel");
        btCancel.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WorkflowDetailsDialog.this.dispose();
            }
        });
        panel.add(btCancel);
   
        /*
         * Multiple workflows per template need to be supported to
         * make the code below work
         *
        if (isAnUpdate == true)
        {
            tfPackage.setText(currentPackageName);
        }
        
        */
        
        
        
        
        
        return panel;
    }
    
}
