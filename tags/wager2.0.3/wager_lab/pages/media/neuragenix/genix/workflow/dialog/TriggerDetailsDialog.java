/**
 * TriggerDetailsDialog.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 24/03/2004
 */

package media.neuragenix.genix.workflow.dialog;

/**
 * This class creates dialog to capture workflow trigger
 * details.
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

// javax packages
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;


// jGraph
import org.jgraph.JGraph;
import org.jgraph.graph.*;

// neuragenix packages
import neuragenix.genix.workflow.*;
import media.neuragenix.genix.workflow.DesignPanel;
import media.neuragenix.genix.workflow.org.jgraph.pad.*;

public class TriggerDetailsDialog extends JDialog
{
    
    // Dialog size
    private static final int DIALOG_WIDTH = 800;
    private static int DIALOG_HEIGHT = 600;
    
    private JButton btOK, btCancel, btAdd, btRemove, btRemoveAll, btParamAdd, btParamRemove, btParamRemoveAll, btUpdate, btParamUpdate;
    private JComboBox cbDomains, cbFields, cbConnector1, cbConnector2, cbOperator1, cbOperator2, cbActions,
                    cbParamDataType;
    private JTextArea taParamDesc;
    private JTextField tfValue1, tfValue2, tfParamName;
    private JList ltTriggers, ltParams;
    private DefaultListModel triggerListModel, paramListModel;
    
    private DefaultGraphCell cell;
    private JGraph graph;
    private DesignPanel designPanel;
    
    private Hashtable hashDomainList = new Hashtable(10);
    private Hashtable hashTriggers, hashParams;
    
    // mutex to prevent listener from updating list while the update button is working on it
    private boolean isUpdatingList = false;
    
    /** Creates a new instance of ActivityDetailsDialog */
    public TriggerDetailsDialog(DesignPanel panel, Object cell, Hashtable hashTriggers, Hashtable hashParams)
    {
        super((JFrame) null, "Trigger details", true);
        getContentPane().setLayout(new BorderLayout());
        
        // setting variable
        this.designPanel = panel;
        this.cell = (DefaultGraphCell) cell;
        if (hashTriggers == null)
            hashTriggers = new Hashtable(10);
        if (hashParams == null)
            hashParams = new Hashtable(10);
        this.hashTriggers = hashTriggers;
        this.hashParams = hashParams;
        
        // load domains & fields 
        loadDomains();
        
        // create central panel
        getContentPane().add(createCentralPanel());
        
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
    }
    
    /**
     * Create central panel for the dialog
     */
    private JPanel createCentralPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        // column 1
        JLabel label = new JLabel("Domains list");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        Enumeration enum = hashDomainList.keys();
        Vector vtDomains = new Vector(10, 3);
        while (enum.hasMoreElements())
        {
            vtDomains.add(enum.nextElement());
        }
        cbDomains = new JComboBox(vtDomains);
        cbDomains.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(cbDomains);
        cbDomains.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    //String strDomain = (String) cbDomains.getSelectedItem();
                    //System.err.println(hashDomainList);
                    Vector vtFields = (Vector) hashDomainList.get(cbDomains.getSelectedItem());
                    cbFields.removeAllItems();
                    
                    //System.err.println("Domain: " + cbDomains.getSelectedItem());
                    
                    for (int i=0; i < vtFields.size(); i++)
                    {
                        cbFields.addItem(vtFields.get(i));
                        //System.err.println("Field added: " + vtFields.get(i));
                    }
                }
            }
        );
        
        // get actions
        label = new JLabel("Action");
        label.setBounds(350 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        Vector vtActions = designPanel.getWFDesigner().getActions("GET ACTIONS");
        cbActions = new JComboBox(vtActions);
        cbActions.setBounds(350 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(cbActions);
        Enumeration enumTriggers = hashTriggers.elements();
        if (enumTriggers.hasMoreElements())
        {
            String strAction = ((Trigger) enumTriggers.nextElement()).getAction();
            cbActions.setSelectedItem(strAction);
        }
        
        // column 2
        label = new JLabel("Connector");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH + 10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        label = new JLabel("Domain fields");
        label.setBounds(DialogConstants.TOPLEFT_X + 70, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        label = new JLabel("Operator");
        label.setBounds(DialogConstants.TOPLEFT_X + 280, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH + 10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        label = new JLabel("Value");
        label.setBounds(DialogConstants.TOPLEFT_X + 350, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        label = new JLabel("Connector");
        label.setBounds(DialogConstants.TOPLEFT_X + 460, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH + 10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        label = new JLabel("Operator");
        label.setBounds(DialogConstants.TOPLEFT_X + 530, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH + 10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        label = new JLabel("Value");
        label.setBounds(DialogConstants.TOPLEFT_X + 600, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        // next row
        cbConnector1 = new JComboBox(DialogConstants.CONNECTION_OPTIONS);
        cbConnector1.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + 3*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH + 10, DialogConstants.LABEL_HEIGHT);
        cbConnector1.setSelectedIndex(0);
        panel.add(cbConnector1);
        
        //Vector vtFields = (Vector) hashDomainList.get(vtDomains.get(0));
        Vector vtFirstDomainFields = (Vector) hashDomainList.get(vtDomains.get(0));
        cbFields = new JComboBox();
        cbFields.setBounds(DialogConstants.TOPLEFT_X + 70, DialogConstants.TOPLEFT_Y + 3*DialogConstants.LINE_HEIGHT, DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(cbFields);
        for (int i=0; i < vtFirstDomainFields.size(); i++)
            cbFields.addItem(vtFirstDomainFields.get(i));
        
        cbOperator1 = new JComboBox(DialogConstants.OPERATION_OPTIONS);
        cbOperator1.setBounds(DialogConstants.TOPLEFT_X + 280, DialogConstants.TOPLEFT_Y + 3*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH +10, DialogConstants.LABEL_HEIGHT);
        cbOperator1.setSelectedIndex(0);
        panel.add(cbOperator1);
        
        tfValue1 = new JTextField("");
        tfValue1.setBounds(DialogConstants.TOPLEFT_X + 350, DialogConstants.TOPLEFT_Y + 3*DialogConstants.LINE_HEIGHT, DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(tfValue1);
        
        cbConnector2 = new JComboBox(DialogConstants.CONNECTION_OPTIONS);
        cbConnector2.setBounds(DialogConstants.TOPLEFT_X + 460, DialogConstants.TOPLEFT_Y + 3*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH + 10, DialogConstants.LABEL_HEIGHT);
        cbConnector2.setSelectedIndex(0);
        panel.add(cbConnector2);
        
        cbOperator2 = new JComboBox(DialogConstants.OPERATION_OPTIONS);
        cbOperator2.setBounds(DialogConstants.TOPLEFT_X + 530, DialogConstants.TOPLEFT_Y + 3*DialogConstants.LINE_HEIGHT, DialogConstants.SHORT_BUTTON_WIDTH +10, DialogConstants.LABEL_HEIGHT);
        cbOperator2.setSelectedIndex(0);
        panel.add(cbOperator2);
        
        tfValue2 = new JTextField("");
        tfValue2.setBounds(DialogConstants.TOPLEFT_X + 600, DialogConstants.TOPLEFT_Y + 3*DialogConstants.LINE_HEIGHT, DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(tfValue2);
        
        // buttons
        btAdd = new JButton("Add");

        btAdd.setBounds((DIALOG_WIDTH - 3*DialogConstants.BUTTON_WIDTH)/2, DialogConstants.TOPLEFT_Y + 4*DialogConstants.LINE_HEIGHT, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                isUpdatingList = true;
                    
                    int key = hashTriggers.size() + 1;
                    String strTriggerID = "Trigger" + (key);
                    
                    while (hashTriggers.containsKey(strTriggerID))
                    {
                       strTriggerID = "Trigger" + (++key);
                    }
                    
                    
                    String strDomain = (String) cbDomains.getSelectedItem();
                    String strAction = (String) cbActions.getSelectedItem();
                    TriggerField tgField = (TriggerField) cbFields.getSelectedItem();
                    String strFieldName = tgField.getName();
                    String strType = tgField.getType();
                    String strConnector1 = (String) cbConnector1.getSelectedItem();
                    String strConnector2 = (String) cbConnector2.getSelectedItem();
                    String strOperator1 = (String) cbOperator1.getSelectedItem();
                    String strOperator2 = (String) cbOperator2.getSelectedItem();
                    String strValue1 = (tfValue1.getText() == null ? "" : tfValue1.getText().trim());
                    String strValue2 = (tfValue2.getText() == null ? "" : tfValue2.getText().trim());

                    if (!strOperator1.equals("") && !strValue1.equals(""))
                    {
                        hashTriggers.put(strTriggerID, new Trigger(strTriggerID, strTriggerID, strAction, strDomain, strFieldName, 
                                        strType, strConnector1, strOperator1, strValue1, strConnector2, strOperator2, strValue2));
                        triggerListModel.addElement(new Trigger(strTriggerID, strTriggerID, strAction, strDomain, strFieldName, 
                                        strType, strConnector1, strOperator1, strValue1, strConnector2, strOperator2, strValue2));
                    }
                 isUpdatingList = false;
            }
        });
        panel.add(btAdd);
        
        btRemove = new JButton("Remove");
 
        btRemove.setBounds((DIALOG_WIDTH - 3*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, DialogConstants.TOPLEFT_Y + 4*DialogConstants.LINE_HEIGHT, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Trigger tgCurrent = (Trigger) ltTriggers.getSelectedValue();
                if (tgCurrent != null)
                {
                    isUpdatingList = true;
                    hashTriggers.remove(tgCurrent.getId());
                    triggerListModel.removeElement(tgCurrent);
                    isUpdatingList = false;
                }
            }
        });
        panel.add(btRemove);
        
        btUpdate = new JButton("Update");

        btUpdate.setBounds((DIALOG_WIDTH - 3*DialogConstants.BUTTON_WIDTH)/2 + (DialogConstants.BUTTON_WIDTH * 2), DialogConstants.TOPLEFT_Y + 4*DialogConstants.LINE_HEIGHT, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        
        btUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                
                if (ltTriggers.isSelectionEmpty() == true)
                {
                    JOptionPane.showMessageDialog(null, "You must select a Defined Trigger to update", "No Trigger Selected", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // generate the new item
                Trigger tgCurrent = (Trigger) ltTriggers.getSelectedValue();
                // get original values
                // String strTriggerID = "Trigger" + (hashTriggers.size() + 1);
                // String strDomain = (String) cbDomains.getSelectedItem();
                // String strAction = (String) cbActions.getSelectedItem();
                
                String strTriggerID = tgCurrent.getId();
                String strDomain = tgCurrent.getDomain();
                String strAction = tgCurrent.getAction();
                
                // rebuild the rest of the fields
                TriggerField tgField = (TriggerField) cbFields.getSelectedItem();
                String strFieldName = tgField.getName();
                String strType = tgField.getType();
                String strConnector1 = (String) cbConnector1.getSelectedItem();
                String strConnector2 = (String) cbConnector2.getSelectedItem();
                String strOperator1 = (String) cbOperator1.getSelectedItem();
                String strOperator2 = (String) cbOperator2.getSelectedItem();
                String strValue1 = (tfValue1.getText() == null ? "" : tfValue1.getText().trim());
                String strValue2 = (tfValue2.getText() == null ? "" : tfValue2.getText().trim());
                
                
                
                
                
                // remove the item we're modifying from the list
                
                isUpdatingList = true; // mutex this function from list listener
                if (tgCurrent != null)
                {
                    triggerListModel.removeElement(tgCurrent);
                    hashTriggers.remove(tgCurrent.getId());
                }
                
                
                if (!strOperator1.equals("") && !strValue1.equals(""))
                {
                    hashTriggers.put(strTriggerID, new Trigger(strTriggerID, strTriggerID, strAction, strDomain, strFieldName, 
                                    strType, strConnector1, strOperator1, strValue1, strConnector2, strOperator2, strValue2));
                    triggerListModel.addElement(new Trigger(strTriggerID, strTriggerID, strAction, strDomain, strFieldName, 
                                    strType, strConnector1, strOperator1, strValue1, strConnector2, strOperator2, strValue2));
                }
                
                
                
                
                
                
                isUpdatingList = false;
                
            }
        });
                
        panel.add(btUpdate);   
        
        
        
        label = new JLabel("Defined triggers");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + 5*DialogConstants.LINE_HEIGHT, DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        triggerListModel = new DefaultListModel();
        if (hashTriggers != null)
        {
            enumTriggers = hashTriggers.elements();
            while (enumTriggers.hasMoreElements())
            {
                triggerListModel.addElement(enumTriggers.nextElement());
            }
        }
        
        ltTriggers = new JList(triggerListModel);
        ltTriggers.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + 6*DialogConstants.LINE_HEIGHT, 750, 100);
        ltTriggers.setFont(new Font("Courier", Font.PLAIN, 12));
        
        ltTriggers.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            
            public void valueChanged(javax.swing.event.ListSelectionEvent e) 
            {
         
                // retrieve an exact match of the objects used for the combo
                
                Enumeration enDomainList = hashDomainList.keys();
                Vector vtDomainFields = new Vector(10, 5);
                while (enDomainList.hasMoreElements())
                {
                    Object domainKey = enDomainList.nextElement();
                    Vector vtTemp    = (Vector) hashDomainList.get(domainKey);
                    
                    for (int i=0; i < vtTemp.size(); i++)
                        vtDomainFields.add(vtTemp.get(i));
                }
                
                if (isUpdatingList == false)
                {
                    TriggerField tempField = null;

                    // retrieve the object for the combo

                    boolean isNameFound = false;

                    Trigger trgTemp = (Trigger) ltTriggers.getSelectedValue();
                    
                    for (int i = 0; i < vtDomainFields.size(); i++)
                    {                        
                        if (((TriggerField)vtDomainFields.get(i)).getName().equals(trgTemp.getField()))
                        {
                           tempField = (TriggerField) vtDomainFields.get(i);
                           isNameFound = true;
                           i = vtDomainFields.size();
                        }
                    }

                    if (isNameFound == false)
                    {
                        JOptionPane.showMessageDialog(null, "Warning : Domain Field for \"" + trgTemp.getField() + "\" was not found", "Field Not Found", JOptionPane.WARNING_MESSAGE);
                        System.err.println("Matching Domain List object for " + trgTemp.getField() + " was not found during update procedure");
                    }



                        cbConnector1.setSelectedItem(trgTemp.getConnection1());

                        cbFields.setSelectedItem(tempField);
                        cbOperator1.setSelectedItem(trgTemp.getOperator1());
                        tfValue1.setText(trgTemp.getValue1());
                        cbConnector2.setSelectedItem(trgTemp.getConnection2());
                        cbOperator2.setSelectedItem(trgTemp.getOperator2());
                        tfValue2.setText(trgTemp.getValue2());
                }
                
            }    
        
        
        });
        
        
        
        
        JScrollPane areaScrollPane = new JScrollPane(ltTriggers);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + 6*DialogConstants.LINE_HEIGHT, 750, 100);       
        panel.add(areaScrollPane);
        
        btOK = new JButton("OK");
        btOK.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2, 500, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                designPanel.setTriggers(hashTriggers);
                designPanel.setParameters(hashParams);
                TriggerDetailsDialog.this.dispose();
            }
        });
        panel.add(btOK);

        btCancel = new JButton("Cancel");
        btCancel.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, 500, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TriggerDetailsDialog.this.dispose();
            }
        });
        panel.add(btCancel);
        
        
        // parameter panel
        JPanel parampanel = new JPanel();
        parampanel.setLayout(null);
        parampanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLACK), "Parameters"));
        parampanel.setBounds(0, 320, 790, 160);
        panel.add(parampanel);
        

        // Parameters area
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        parampanel.add(label);

        tfParamName = new JTextField();
        tfParamName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y,
                         DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        parampanel.add(tfParamName);
        
        //panel.setBounds(0, 320, 700, 160);

        // row 2
        
        label = new JLabel("Data type");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        parampanel.add(label);

        cbParamDataType = new JComboBox(DialogConstants.DATA_TYPES);
        cbParamDataType.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.MED_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        parampanel.add(cbParamDataType);

        // row 3

        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        parampanel.add(label);

        taParamDesc = new JTextArea();
        taParamDesc.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT,
                             DialogConstants.MED_TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT/2);
        areaScrollPane = new JScrollPane(taParamDesc);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + 2*DialogConstants.LINE_HEIGHT,
                             DialogConstants.MED_TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT*2/3);
        parampanel.add(areaScrollPane);


        // Column 2
        paramListModel = new DefaultListModel();
        if (hashParams != null)
        {
            Enumeration enumParams = hashParams.elements();
            while (enumParams.hasMoreElements())
            {
                paramListModel.addElement(enumParams.nextElement());
            }
        }
        
        ltParams = new JList(paramListModel);
        ltParams.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.MED_TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        
        ltParams.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            
            public void valueChanged(javax.swing.event.ListSelectionEvent e) 
            {
                if (isUpdatingList == false) // mutex
                {
                    Parameter prTemp = (Parameter) ltParams.getSelectedValue();
                    tfParamName.setText(prTemp.getName());
                    cbParamDataType.setSelectedItem(prTemp.getType());
                    taParamDesc.setText(prTemp.getDescription());
                    
                }
                
        
            
            }
        
            
        });
        
        
        areaScrollPane = new JScrollPane(ltParams);
        areaScrollPane.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y,
                             DialogConstants.MED_TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT + 20);
        parampanel.add(areaScrollPane);


        // add control button
        btParamAdd = new JButton(">");
        btParamAdd.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 0, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btParamAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add a parameter to the param list
                addParameter(tfParamName.getText(), (String) cbParamDataType.getSelectedItem(), taParamDesc.getText());
            }
        });
        parampanel.add(btParamAdd);

        btParamRemove = new JButton("<");
        btParamRemove.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 20, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btParamRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove the selected parameter from the param list
                isUpdatingList = true;
                
                removeParameter(ltParams.getSelectedValue());
                
                isUpdatingList = false;
            }
        });
        parampanel.add(btParamRemove);

        btParamRemoveAll = new JButton("<<");
        btParamRemoveAll.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 45, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btParamRemoveAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove all paramereters
                
                isUpdatingList = true; // mutex list
                
                paramListModel.removeAllElements();
                hashParams.clear();
                
                isUpdatingList = false; 
            }
        });
        parampanel.add(btParamRemoveAll);
        
        btParamUpdate = new JButton ("Update");
        btParamUpdate.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 70, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btParamUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // update parameters
                if (updateParameter(ltParams.getSelectedValue(), tfParamName.getText(),(String) cbParamDataType.getSelectedItem(), taParamDesc.getText()) == false)
                {
                    
                    JOptionPane.showMessageDialog(null, "The Parameter failed to update.  Ensure the parameter name is unique.", "No Update", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
        parampanel.add(btParamUpdate);
        
        
        
        return panel;
    }
    
    /** 
     * Load domains and fields
     */
    private void loadDomains()
    {
        /*Vector vtBiospecimenFields = new Vector(10, 5);
        vtBiospecimenFields.add(new TriggerField("BIOSPECIMEN_strBiospecimenID", "Biospecimen ID", "String"));
        vtBiospecimenFields.add(new TriggerField("BIOSPECIMEN_strSubtype", "Sub type", "String"));
        hashDomainList.put("Biospecimen", vtBiospecimenFields);
        
        Vector vtPatientFields = new Vector(10, 5);
        vtPatientFields.add(new TriggerField("PATIENT_strFirstname", "First name", "String"));
        vtPatientFields.add(new TriggerField("PATIENT_strLastname", "Last name", "String"));
        hashDomainList.put("Patient", vtPatientFields);*/
        /*
        // Case fields
        Vector vtCaseFields = new Vector(10, 5);
        vtCaseFields.add(new TriggerField("CORECASE_intCoreCaseKey", "Case key", "Integer"));
        vtCaseFields.add(new TriggerField("CORECASE_intCaseID", "Case ID", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strCategory", "Category", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strSubCategory", "Sub category", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strOffence", "Offence category", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strCHSID", "CHS ID", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strFileNo", "File no", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strReferenceID", "Reference ID", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strConfidential", "Invest. grading", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strPriority", "Priority", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strCreatedDate", "Created date", "Date"));
        vtCaseFields.add(new TriggerField("CORECASE_strOccurrenceDate", "Occurrence date", "Date"));
        vtCaseFields.add(new TriggerField("CORECASE_strReceivedDate", "Received date", "Date"));
        vtCaseFields.add(new TriggerField("CORECASE_strSource", "Source", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strDetection", "Detection method", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strSummary", "Summary", "String"));
        vtCaseFields.add(new TriggerField("CORECASE_strStatus", "Status", "String"));
        
        vtCaseFields.add(new TriggerField("CORECASE_dblSavings", "Estimated cost savings", "Float"));
        vtCaseFields.add(new TriggerField("CORECASE_dblRecovery", "Recovery", "Float"));
        vtCaseFields.add(new TriggerField("CORECASE_dblLegalCosts", "Legal costs", "Float"));
        vtCaseFields.add(new TriggerField("CORECASE_dblRestitution", "Restitution", "Float"));
        vtCaseFields.add(new TriggerField("CORECASE_dblAwardedCosts", "Awarded costs", "Float"));
        vtCaseFields.add(new TriggerField("CORECASE_dblFines", "Fines", "Float"));
        vtCaseFields.add(new TriggerField("CORECASE_strComments", "Comments", "String"));
        hashDomainList.put("Case", vtCaseFields);
        */
        
        // load from DB from now on
        hashDomainList = designPanel.getWFDesigner().getTriggerTypes();
    }
    
    /** Add a parameter to list
     */
    private void addParameter(String strName, String strType, String strDesc)
    {
        // only add parameter if it has unique name
        if (strName != null && !strName.trim().equals(""))
        {
            boolean isExist = false;
            for (int i=0; i < paramListModel.getSize(); i++)
            {
                Parameter current_param = (Parameter) paramListModel.get(i);
                if (current_param.getName().equals(strName))
                {
                    isExist = true;
                    break;
                }
            }
            
            if (!isExist)
            {
                paramListModel.addElement(new Parameter(strName, strType, strDesc));
                hashParams.put(strName, new Parameter(strName, strType, strDesc));
            }
        }
    }
    
    private boolean updateParameter(Object parPrev, String strName, String strType, String strDesc)
    {
        if (parPrev == null || ltParams.isSelectionEmpty() == true)
        {
            JOptionPane.showMessageDialog(null, "Nothing has been selected to update", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        else
        {
            isUpdatingList = true;

            Parameter parPrevious = (Parameter) parPrev;

            // only add parameter if it has unique name


            // dont check if name unchanged
            if (parPrevious.getName().equals(strName))
            {
                removeParameter(parPrevious);
                paramListModel.addElement(new Parameter(strName, strType, strDesc));
                hashParams.put(strName, new Parameter(strName, strType, strDesc));

                isUpdatingList = false;
                return true;
            }


            // check name is unique, because it has changed
            if (strName != null && !strName.trim().equals(""))
            {
                boolean isExist = false;
                for (int i=0; i < paramListModel.getSize(); i++)
                {
                    Parameter current_param = (Parameter) paramListModel.get(i);
                    if (current_param.getName().equals(strName))
                    {
                        isExist = true;
                        isUpdatingList = false;
                        return false;
                    }
                }

                if (!isExist)
                {
                    removeParameter(parPrevious);
                    paramListModel.addElement(new Parameter(strName, strType, strDesc));
                    hashParams.put(strName, new Parameter(strName, strType, strDesc));
                    // clear the fields to signify the update occurred
                    tfParamName.setText("");
                    cbParamDataType.setSelectedIndex(0);
                    taParamDesc.setText("");
                    isUpdatingList = false;
                    return true;
                }
            }

            return false;
        }
        
    }
    
    
    
    
    /** Remove a parameter from list
     */
    private void removeParameter(Object param)
    {
        paramListModel.removeElement(param);
        hashParams.remove(((Parameter) param).getName());
    }
}