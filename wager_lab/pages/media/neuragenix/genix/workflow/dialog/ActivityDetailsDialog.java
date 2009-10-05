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
import java.awt.Dimension;

// javax packages
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ButtonGroup;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

// jGraph
import org.jgraph.JGraph;
import org.jgraph.graph.*;

// neuragenix packages
import neuragenix.genix.workflow.*;
import media.neuragenix.genix.workflow.DesignPanel;
import media.neuragenix.genix.workflow.org.jgraph.pad.*;

public class ActivityDetailsDialog extends JDialog
{
    // Required field symbol color
    private static final Color REQUIRED_FIELD_COLOR = Color.red;
    
    // Human dialog size
    private static final int HUMAN_DIALOG_WIDTH = 956;
    private static final int HUMAN_DIALOG_HEIGHT = 700;
    
    // System dialog size
    private static final int SYSTEM_DIALOG_WIDTH = 956;
    private static final int SYSTEM_DIALOG_HEIGHT = 725;
    
    // Sub workflow dialog size
    private static final int SUBWORKFLOW_DIALOG_WIDTH = 906;
    private static final int SUBWORKFLOW_DIALOG_HEIGHT = 500;
    
    private static int DIALOG_WIDTH;
    private static int DIALOG_HEIGHT;
    
    private JTextField tfID, tfName, tfTriggerValue, tfFinishedValue, tfFunctionKey, tfFunctionTimeout, tfRetryCounter, 
                    tfRetryDelayValue, tfCompletedTask, tfParamName, tfPerformer, tfTriggerOther, tfFinishOther, 
                    tfRecurringTriggerValue, tfRecurringFinishValue;
    private JTextArea taDescription, taInstruction, taParamDesc;
    private JComboBox cbPriority, cbReassignable, cbIfFail, cbSignoff, cbType, cbRetryFail, cbTriggerUnit,
                    cbFinishedUnit, cbRetryDelayUnit, cbIntervalUnit, cbHour, cbMinute, cbAMPM, cbFunctionKey,
                    cbParamDataType, cbSubWorkflow, cbMultiAction, cbRecurringTriggerUnit, cbRecurringFinishUnit;
    private JButton btOK, btCancel, btAdd, btRemove, btRemoveAll, btParamUpdate, btOrgChart, btTriggerOrgChart, btFinishOrgChart;
    private JRadioButton rbFiveWorkingDays, rbSevenWorkingDays, rbTriggerRecurringYes, rbTriggerRecurringNo,
                    rbFinishRecurringYes, rbFinishRecurringNo;
    private JCheckBox ckbTriggerPerformer, ckbFinishPerformer, ckbTriggerOther, ckbFinishOther;
    
    private JSeparator sepTriggerDivider;
    
    private JList ltParams;
    private DefaultListModel paramListModel;
    
    private DefaultGraphCell cell;
    private JGraph graph;
    private String type;
    private DesignPanel designPanel;
    private Point point;
    private OrgChartUser orgchartPerformer, orgchartTriggerSendTo, orgchartFinishSendTo;
    
    private String strPrevID = "";
    
    private boolean isUpdatingList = false;
    
    /** Creates a new instance of ActivityDetailsDialog */
    public ActivityDetailsDialog(DesignPanel panel, Object cell, String type, Point point)
    {
        super((JFrame) null, "Activity details", true);
        getContentPane().setLayout(new BorderLayout());
        
        this.designPanel = panel;
        this.cell = (DefaultGraphCell) cell;
        //this.graph = (JGraph) graph;
        this.type = type;
        this.point = point;
        
        // create a new HUMAN activity
        if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]))
        {
            DIALOG_WIDTH = HUMAN_DIALOG_WIDTH;
            DIALOG_HEIGHT = HUMAN_DIALOG_HEIGHT;
        }
        // create a new SYSTEM activity
        else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
        {
            DIALOG_WIDTH = SYSTEM_DIALOG_WIDTH;
            DIALOG_HEIGHT = SYSTEM_DIALOG_HEIGHT;
        }
        // create a new SUB-WORKFLOW activity
        else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
        {
            DIALOG_WIDTH = SUBWORKFLOW_DIALOG_WIDTH;
            DIALOG_HEIGHT = SUBWORKFLOW_DIALOG_HEIGHT;
        }
        getContentPane().add(createCentralPanel(type));
        
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
    }
    
    /**
     * Create central panel for the dialog
     */
    private JPanel createCentralPanel(String type)
    {
        // Dialog for human activity
        if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]))
        {
            return createHumanActivityDialog();
        }
        // Dialog for system activity
        else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
        {
            return createSystemActivityDialog();
        }
        // Dialog for sub-workflow activity
        else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
        {
            return createSubWorkflowActivityDialog();
        }
        
        return null;
    }
    
    /** Will validate the data on the current dialog based on the type of dialog
     * displayed.
     * @return Returns a boolean dependent on whether the dialog is valid or not
     */    
    private boolean validateDialog()
    {
       
        
        // Validation of text fields based on dialog displayed
        
        // Note : System Dialog may require validation on the function field
        
        boolean isValid = true;
        String strValidationMessage = "Unable to save action at this time because : \n";
        
        // -- Common fields to validate across all three dialogs
        
        // Id field
          
        if (tfID.getText().trim().equals(""))
        {  
            isValid = false;
            strValidationMessage = strValidationMessage + " - A valid ID has not been assigned for task \n";
        }
        else
        {
            if (isIdValid(tfID.getText(), strPrevID) == false)
            {
                isValid = false;
                strValidationMessage = strValidationMessage + " - The ID you have entered already exists in this workflow\n";
            }
            
        }
        
        // Name field
        if (tfName.getText().trim().equals(""))
        {
            isValid = false;
            strValidationMessage = strValidationMessage + " - A valid Name has not been assigned for task \n";
        }
        
        // Completed Task
        try
        {
            int intTemp = Integer.parseInt(tfCompletedTask.getText());
            if (intTemp <= 0)
            {
                isValid = false;
                strValidationMessage = strValidationMessage + " - Completed Task value must be greater than 0 \n";
            }
            
        }
        catch (NumberFormatException e)
        {
            isValid = false;
            strValidationMessage = strValidationMessage + " - Completed Task must contain only a whole number greater than 0 \n";
            
        }
        
        
        // -- end common fields
        
        
        // Performer -- Human activity Specific
        
        if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0])) 
        {
           if (tfPerformer.getText().trim().equals(""))
           {
               isValid = false;
               strValidationMessage = strValidationMessage + " - A valid Performer has not been assigned for task \n";
               
           }

        }
        
        
        // may need to be removed if parameter section is removed
        if ( (!(taParamDesc.getText().trim().equals(""))) && (tfParamName.getText().trim().equals("")))
        {
            isValid = false;
            strValidationMessage = strValidationMessage + " - A parameter description has been entered, however no name is assigned for the parameter \n";
        }
        
        
        // Validation of Human and System Common Fields
        
        if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]) || (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1])))
        {
            
           if (!tfTriggerValue.getText().equals(""))
           {
                // ensure the value in text field is integer
                
                try 
                {
                    Float fltTempFloat = Float.valueOf((String) tfTriggerValue.getText());
                    // value good
                    if (cbTriggerUnit.getSelectedItem().equals(""))
                    {
                        isValid = false;
                        strValidationMessage = strValidationMessage + " - A time frame (Minutes, Hours, etc) must be selected for the trigger \n";
                    }
                }
                catch (NumberFormatException e)
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - Time period for Trigger must be a decimal value greater than zero \n";
                }
           }
           else
           {
                if (!cbTriggerUnit.getSelectedItem().equals(""))
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - A time period for the trigger is required \n";
                }
                

           }

           if ((ckbTriggerPerformer.isSelected() == true || ckbTriggerOther.isSelected() == true) && tfTriggerValue.getText().equals(""))
           {
              isValid = false;
              strValidationMessage = strValidationMessage + " - A trigger performer has been selected however no time period has been entered for the trigger \n";
               
           }

           if (ckbTriggerPerformer.isSelected() == false && ckbTriggerOther.isSelected() == false && (!tfTriggerValue.getText().equals("")))
           {
               isValid = false;
               
               strValidationMessage = strValidationMessage + " - A performer is required to be selected for the alert to be sent to \n";
           }
           else if (ckbTriggerOther.isSelected() && tfTriggerOther.getText().equals("")) // Requires value in Other tf
           {
               isValid = false;
               
               strValidationMessage = strValidationMessage + " - A performer must be selected from the list for the alert to be sent to \n";
           }
           if ((ckbTriggerOther.isSelected() == false) && (!tfTriggerOther.getText().equals("")))
           {
                isValid = false;
                strValidationMessage = strValidationMessage + " - A performer has been selected for the alert to be sent to, however \"Other\" is not selected \n";
                
           }

           
           
           
            // validate recurring within
            // if recurring alert equals yes, recurring within a must have a value
           
            if (rbTriggerRecurringYes.isSelected())
            {
                // check a value is float
                try
                {
                    Float fltTemp = Float.valueOf(tfRecurringTriggerValue.getText()); 
                    // value is good
                    if (cbRecurringTriggerUnit.getSelectedItem().equals(""))
                    {
                        isValid = false;
                        strValidationMessage = strValidationMessage + " - A time frame (Minutes, Hours, etc) must be selected for the Recurring Trigger\n";
                    }
                    if (tfTriggerValue.getText().equals(""))
                    {
                        isValid = false;
                        strValidationMessage = strValidationMessage + " - A trigger within value has not been set \n";
                        
                    }
                    
                    

                }
                catch (NumberFormatException e)
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - Time period for Recurring Trigger must be a decimal value and not zero \n";  
                }

            }

            else if (!tfRecurringTriggerValue.getText().equals("")) // value is in the recurring alert
            {
                isValid = false;
                strValidationMessage = strValidationMessage + " - Recurring Within field has a value, however Recurring Alert is currently set to No \n";  

                if (cbRecurringTriggerUnit.getSelectedItem().equals("")) // time unit not selected
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - A time frame (Minutes, Hours, etc) must be selected for the Recurring Within field \n";

                }
            }
           
            // -----------------------
            // validate second section of fields (finished within / recurring within )

            if (!tfFinishedValue.getText().equals(""))
            {
                // ensure the value in adjacent text field is integer
           
                try
                {
                   Float fltTempFloat = Float.valueOf(tfFinishedValue.getText().toString());
                   if (cbFinishedUnit.getSelectedItem().equals(""))
                   {
                       isValid = false;
                       strValidationMessage = strValidationMessage + " - A time frame (Minutes, Hours, etc) must be selected for the recurring within trigger \n";
                   }    

                }
                catch (NumberFormatException e)
                {
                    isValid = false;
                    System.err.println("Exception caught for invalid number format");
                    strValidationMessage = strValidationMessage + " - Time period for Trigger must be a decimal value greater than zero \n";    
                }
            }
            else // No value in the field, check to ensure the combo is clear
            {

                if (!cbFinishedUnit.getSelectedItem().equals(""))
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - A time frame (Minutes, Hours, etc) must be selected for the Finished Within field \n";
                }
                
                if (ckbFinishPerformer.isSelected() == true || ckbFinishOther.isSelected() == true)
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - A time frame (Minutes, Hours, etc) must be selected for the Finished Within field \n";
                    
                }

            } 
           
            if ((!tfFinishedValue.getText().equals("")) && ckbFinishPerformer.isSelected() == false && ckbFinishOther.isSelected() == false)
            {
                isValid = false;
                strValidationMessage = strValidationMessage + " - A performer is required to be selected for the alert to be sent to \n";

            }
            if (ckbFinishOther.isSelected() && tfFinishOther.getText().equals("")) // Requires value in Other tf
            {
                isValid = false;
                strValidationMessage = strValidationMessage + " - A performer must be selected from the list for the alert to be sent to \n";
            }
            
            if ((ckbFinishOther.isSelected() == false) && (!tfFinishOther.getText().equals("")))
            {
                isValid = false;
                strValidationMessage = strValidationMessage + " - A performer has been selected for the alert to be sent to, however \"Other\" is not selected \n";
                
            }
            
            
           
            // validate recurring within
            // if recurring alert equals yes, recurring within a must have a value
            if (rbFinishRecurringYes.isSelected())
            {
                // check a value is float
                try
                {
                    Float fltTemp = Float.valueOf(tfRecurringFinishValue.getText());

                    // check time frame
                    if (cbRecurringFinishUnit.getSelectedItem().equals(""))
                    {
                        isValid = false;
                        strValidationMessage = strValidationMessage + " - A time unit (Minutes, Hours, etc) must be selected for the Recurring Trigger (2) \n";
                    }
                    
                    if (tfFinishedValue.getText().equals(""))
                    {
                        isValid = false;
                        strValidationMessage = strValidationMessage + " - A recurring trigger has been entered, however no initial trigger has been set \n";
                    }
                    
                }
                catch (NumberFormatException e)
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - Time period for Recurring Trigger (2) must be a decimal value and not zero \n";      
                }

            }

            else if (!tfRecurringFinishValue.getText().equals("")) // value is in the recurring alert
            {
                isValid = false;
                strValidationMessage = strValidationMessage + " - Recurring Trigger (2) has a value, however Recurring Alert is currently set to no \n";  

                if (cbRecurringFinishUnit.getSelectedItem().equals("")) // time unit not selected
                {
                    isValid = false;
                    strValidationMessage = strValidationMessage + " - A time unit (Minutes, Hours, etc) must be selected for the Recurring Trigger (2) \n";

                }
            
                
                
                
            }
            
           
           
           if ((!cbRecurringFinishUnit.getSelectedItem().equals("")) && tfRecurringFinishValue.getText().equals(""))
           {
                isValid = false;
                strValidationMessage = strValidationMessage + " - Time period for Recurring Within must be a decimal value and not zero \n";
           }
               
               
        }
        
        
        // 
        
        
        
        // -- system specific
        // number of retries must be an integer.
        if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
        {
            if (cbIfFail.getSelectedItem().equals("Retry"))
            {
               
               // check retry delay
               boolean isCorrectDelay = false;
               boolean isCorrectRetryCount = false;
               try
               {
                  
                  float fltTemp = Float.parseFloat(tfRetryDelayValue.getText());
                  isCorrectDelay = true;
                  
                  int intTemp = Integer.parseInt(tfRetryCounter.getText());
                  isCorrectRetryCount = true;
                  
                  if (fltTemp <= 0)
                  {
                     isValid = false;
                     strValidationMessage = strValidationMessage + " - Retry within value must be greater than 0 \n";
                  }
                  
                  if (intTemp <= 0)
                  {
                      isValid = false;
                      strValidationMessage = strValidationMessage + " - Number of retries must be a whole number greater than zero \n";
                  }
                  
                                  
                  // check time unit is set
                  
                  if (cbRetryDelayUnit.getSelectedItem().equals(""))
                  {
                      isValid = false; // unit is not set   
                      strValidationMessage = strValidationMessage + " - The time frame for retries has not been set \n";
                  }    
                  
                  if (cbRetryFail.getSelectedItem().equals(""))
                  {
                      isValid = false; // unit not set
                      strValidationMessage = strValidationMessage + " - The IF RETRY FAIL condition has not been set \n";
                  }
                  
               }
               catch (NumberFormatException e)
               {
                   isValid = false;
                   if (isCorrectDelay == false)
                      strValidationMessage = strValidationMessage + " - Retry within value must be of the format 12.34 \n";
                   else
                      strValidationMessage = strValidationMessage + " - Number of retries must be a whole number greater than zero \n";
                   
               }
                
            }
            
        }    
               
        
        
        if (isValid == false)
        {
            strValidationMessage = strValidationMessage + "\n Please review the above and try again";
            JOptionPane.showMessageDialog(this, strValidationMessage, "Error", JOptionPane.ERROR_MESSAGE);     
        }
        
        return isValid;
        
        
    }
    
    
    // checks the id does not appear in the process
    private boolean isIdValid(String id, String prevID)
    {
       if (id.equals("")) return false;
       
       Object cells[] = designPanel.getGraph().getRoots();
       
       Activity actTemp = null;
       DefaultGraphCell cellTemp = null;
       
       for (int i = 0; i < cells.length; i++)
       {
           if (cells[i] instanceof DefaultGraphCell) 
           {
              cellTemp = (DefaultGraphCell) cells[i];
              if (cellTemp.getUserObject() instanceof Activity)
              {
                  actTemp = (Activity) cellTemp.getUserObject();
                  if (actTemp.getId().equals(id) && (!(actTemp.getId().equals(prevID)))) return false;
              }
           }
           
       }
       
       return true;
        
    }
    
    
    
    /**
     * Save activity details
     */
    private void doSaveActivity()
    {
    
        boolean isNew = false;
        
        if (validateDialog() == false) // Test Dialog
            return;
        
        
        
        if (cell == null)
        {
            isNew = true;
            
            if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]))
                cell = new DefaultGraphCell();
            else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
                cell = new EllipseCell();
            else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
                cell = new RoundedRectangleCell();
            
            designPanel.insert(point, null, cell);
        }
        
        
        
        // get parameters
        Hashtable hashParams = new Hashtable(10);
        for (int i=0; i < paramListModel.size(); i++)
            hashParams.put(((Parameter) paramListModel.get(i)).getName(), paramListModel.get(i));
        
        
        
        // get Performer
        String strPerformer = "";
        
        // performer type
        String strPerformerType = "";
        
        // get no of completed task
        String strCompletedTask = "";
        
        // getting cell attributes
        Hashtable cell_attrs = (Hashtable) cell.getAttributes();
        Enumeration enum = cell_attrs.keys();
        //while (enum.hasMoreElements())
          //  System.err.println(enum.nextElement().toString());
        Rectangle cell_bounds = (Rectangle) cell_attrs.get("bounds");
        
        // create Activity object
        Hashtable hashExtendedAttributes = new Hashtable(10);
        hashExtendedAttributes.put("strInstruction", taInstruction.getText().trim());
        hashExtendedAttributes.put("intCompletedTask", tfCompletedTask.getText().trim());
        Color bdColor = (Color) cell_attrs.get("bordercolor");
        hashExtendedAttributes.put("strBorderColor", bdColor.getRed() + " " + bdColor.getGreen() + " " + bdColor.getBlue());
        Color bgColor = (Color) cell_attrs.get("backgroundColor");
        hashExtendedAttributes.put("strBackgroundColor", bgColor.getRed() + " " + bgColor.getGreen() + " " + bgColor.getBlue());
        hashExtendedAttributes.put("intPosX", new Long(Math.round(cell_bounds.getX())));
        hashExtendedAttributes.put("intPosY", new Long(Math.round(cell_bounds.getY())));
        hashExtendedAttributes.put("intWidth", new Long(Math.round(cell_bounds.getWidth())));
        hashExtendedAttributes.put("intHeight", new Long(Math.round(cell_bounds.getHeight())));
        
        
        // human activity
        if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]))
        {
            hashExtendedAttributes.put("flTriggerValue", tfTriggerValue.getText().trim());
            hashExtendedAttributes.put("strTriggerUnit", cbTriggerUnit.getSelectedItem());
            hashExtendedAttributes.put("flFinishedValue", tfFinishedValue.getText().trim());
            hashExtendedAttributes.put("strFinishedUnit", cbFinishedUnit.getSelectedItem());
            hashExtendedAttributes.put("strType", DialogConstants.ACTIVITY_TYPE_OPTIONS[0]);
            hashExtendedAttributes.put("strReassignable", cbReassignable.getSelectedItem());
            hashExtendedAttributes.put("strSignoff", cbSignoff.getSelectedItem());
            hashExtendedAttributes.put("strMultiAction", cbMultiAction.getSelectedItem());
            hashExtendedAttributes.put("intWorkingDays", rbFiveWorkingDays.isSelected() ? "5" : "7");
            hashExtendedAttributes.put("strSendTriggerAlertToPerformer", ckbTriggerPerformer.isSelected() ? "yes" : "no");
            hashExtendedAttributes.put("strSendFinishAlertToPerformer", ckbFinishPerformer.isSelected() ? "yes" : "no");
            
            if (ckbTriggerOther.isSelected())
            {
                hashExtendedAttributes.put("intSendTriggerAlertToOtherKey", orgchartTriggerSendTo.getKey());
                hashExtendedAttributes.put("strSendTriggerAlertToOtherType", orgchartTriggerSendTo.getType());
            }
            
            hashExtendedAttributes.put("strRecurringTriggerAlert", rbTriggerRecurringYes.isSelected() ? "yes" : "no");
            hashExtendedAttributes.put("flRecurringTriggerAlertValue", tfRecurringTriggerValue.getText().trim());
            hashExtendedAttributes.put("strRecurringTriggerAlertUnit", cbRecurringTriggerUnit.getSelectedItem());
            
            if (ckbFinishOther.isSelected())
            {
                hashExtendedAttributes.put("intSendFinishAlertToOtherKey", orgchartFinishSendTo.getKey());
                hashExtendedAttributes.put("strSendFinishAlertToOtherType", orgchartFinishSendTo.getType());
            }
            
            hashExtendedAttributes.put("strRecurringFinishAlert", rbFinishRecurringYes.isSelected() ? "yes" : "no");
            hashExtendedAttributes.put("flRecurringFinishAlertValue", tfRecurringFinishValue.getText().trim());
            hashExtendedAttributes.put("strRecurringFinishAlertUnit", cbRecurringFinishUnit.getSelectedItem());
            
            strPerformer = orgchartPerformer.getKey();
            strPerformerType = orgchartPerformer.getType();
        }
        // system
        else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
        {
            hashExtendedAttributes.put("flTriggerValue", tfTriggerValue.getText().trim());
            hashExtendedAttributes.put("strTriggerUnit", cbTriggerUnit.getSelectedItem());
            hashExtendedAttributes.put("flFinishedValue", tfFinishedValue.getText().trim());
            hashExtendedAttributes.put("strFinishedUnit", cbFinishedUnit.getSelectedItem());
            hashExtendedAttributes.put("strType", DialogConstants.ACTIVITY_TYPE_OPTIONS[1]);
            hashExtendedAttributes.put("intFunctionKey", ((Function) cbFunctionKey.getSelectedItem()).getKey());
            hashExtendedAttributes.put("strIfFail", cbIfFail.getSelectedItem());
            hashExtendedAttributes.put("intRetryCounter", tfRetryCounter.getText().trim());
            hashExtendedAttributes.put("flRetryDelayValue", tfRetryDelayValue.getText().trim());
            hashExtendedAttributes.put("strRetryDelayUnit", cbRetryDelayUnit.getSelectedItem());
            hashExtendedAttributes.put("strIfFailAfterRetry", cbRetryFail.getSelectedItem());
            hashExtendedAttributes.put("strIntervalUnit", cbIntervalUnit.getSelectedItem());
            hashExtendedAttributes.put("intWorkingDays", rbFiveWorkingDays.isSelected() ? "5" : "7");
            hashExtendedAttributes.put("strSendTriggerAlertToPerformer", ckbTriggerPerformer.isSelected() ? "yes" : "no");
            hashExtendedAttributes.put("strSendFinishAlertToPerformer", ckbFinishPerformer.isSelected() ? "yes" : "no");
            
            if (ckbTriggerOther.isSelected())
            {
                hashExtendedAttributes.put("intSendTriggerAlertToOtherKey", orgchartTriggerSendTo.getKey());
                hashExtendedAttributes.put("strSendTriggerAlertToOtherType", orgchartTriggerSendTo.getType());
            }
            
            hashExtendedAttributes.put("strRecurringTriggerAlert", rbTriggerRecurringYes.isSelected() ? "yes" : "no");
            hashExtendedAttributes.put("flRecurringTriggerAlertValue", tfRecurringTriggerValue.getText().trim());
            hashExtendedAttributes.put("strRecurringTriggerAlertUnit", cbRecurringTriggerUnit.getSelectedItem());
            
            if (ckbFinishOther.isSelected())
            {
                hashExtendedAttributes.put("intSendFinishAlertToOtherKey", orgchartFinishSendTo.getKey());
                hashExtendedAttributes.put("strSendFinishAlertToOtherType", orgchartFinishSendTo.getType());
            }
            
            hashExtendedAttributes.put("strRecurringFinishAlert", rbFinishRecurringYes.isSelected() ? "yes" : "no");
            hashExtendedAttributes.put("flRecurringFinishAlertValue", tfRecurringFinishValue.getText().trim());
            hashExtendedAttributes.put("strRecurringFinishAlertUnit", cbRecurringFinishUnit.getSelectedItem());
            
            String strIntervalValue = "";
            if (!cbHour.getSelectedItem().toString().equals("") && !cbMinute.getSelectedItem().toString().equals(""))
                strIntervalValue = cbHour.getSelectedItem() + ":" + cbMinute.getSelectedItem() + ":" + cbAMPM.getSelectedItem();
            
            hashExtendedAttributes.put("strIntervalValue", strIntervalValue);
        }
        // sub workflow activity
        else if (type.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
        {
            SubWorkflow subWorkflow = (SubWorkflow) cbSubWorkflow.getSelectedItem();
            hashExtendedAttributes.put("strSubWorkflowPackageXPDLId", subWorkflow.getPackageID());
            hashExtendedAttributes.put("strSubWorkflowTemplateXPDLId", subWorkflow.getWorkflowID());
            hashExtendedAttributes.put("strType", DialogConstants.ACTIVITY_TYPE_OPTIONS[4]);
        }
        
        
        Activity activity = new Activity(tfID.getText().trim(),
                                         tfName.getText().trim(), 
                                         taDescription.getText().trim(), 
                                         strPerformer, 
                                         strPerformerType, 
                                         new Integer(cbPriority.getSelectedIndex() + 1).toString(),
                                         hashParams,
                                         hashExtendedAttributes);
        
        // changing value attribute
        cell_attrs.put("value", activity);
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(cell, cell_attrs);
        // Insert the Vertex and its Attributes (can also use model)
        designPanel.getGraph().getGraphLayoutCache().edit(attributes, null, null, null);
        
        // close the dialog
        ActivityDetailsDialog.this.dispose();
    }
    
    /** Create a dialog to edit a human activity
     */
    private JPanel createHumanActivityDialog()
    {
        // get the current activity
        Activity activity = null;
        Hashtable extendedAttrs = null;
        if (cell != null && cell.getUserObject() != null) {
            if (cell.getUserObject() instanceof Activity)
            {
                activity = (Activity) cell.getUserObject();
                extendedAttrs = activity.getExtendedAttributes();
            }
        }

        // build the activity details form
        int row_no = 0;
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // row 1
        JLabel label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y, 10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("ID");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        if (activity != null)
            strPrevID = activity.getId();
        
        tfID = new JTextField(activity == null ? DesignPanel.getNextGenId() : activity.getId());
        
        tfID.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                       DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        // set the ID as a non-editable field
        tfID.setEditable(false);
        tfID.setBackground(java.awt.Color.lightGray);
        tfID.setToolTipText("This is a system generated ID");
        tfID.setEnabled(false);
        
        panel.add(tfID);


        // row 2
        row_no++;
        label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                        10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfName = new JTextField(activity == null ? "" : activity.getName());
        tfName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfName);


        // row 3
        row_no++;        
        label = new JLabel("Priority");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbPriority = new JComboBox(DialogConstants.PRIORITY_OPTIONS);
        if (activity != null)
            cbPriority.setSelectedIndex(Integer.parseInt(activity.getPriority()) - 1);
        cbPriority.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbPriority);


        // row 4
        row_no++;
        label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                        10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("Performer");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfPerformer = new JTextField();
        tfPerformer.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH - 80, DialogConstants.TEXTFIELD_HEIGHT);
        tfPerformer.setEditable(false);
        tfPerformer.setBackground(Color.white);
        panel.add(tfPerformer);
        
        if (activity != null)
        {
            DefaultMutableTreeNode orgchartTree = designPanel.getWFDesigner().getOrgChart();
            Enumeration enum = orgchartTree.breadthFirstEnumeration();
            while (enum.hasMoreElements())
            {
                OrgChartUser user = (OrgChartUser) ((DefaultMutableTreeNode) enum.nextElement()).getUserObject();
                if (activity.getPerformer().equals(user.getKey()) &&
                    activity.getPerformerType().equals(user.getType()))
                {
                   tfPerformer.setText(user.getName());
                   orgchartPerformer = user; 
                   break;
                }
            }
        }

        btOrgChart = new JButton("OrgChart");
        btOrgChart.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.TEXTFIELD_WIDTH - 80, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             80, DialogConstants.TEXTFIELD_HEIGHT);
        btOrgChart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrgChartDialog dialog = new OrgChartDialog(ActivityDetailsDialog.this, designPanel.getWFDesigner().getOrgChart(), 0);
                // show dialog in the center
                Dimension screensize = getToolkit().getScreenSize();
                dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);
                dialog.setVisible(true);
            }
        });
        panel.add(btOrgChart);

        // row 5
        row_no++;
        label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                        10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("Completed tasks");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfCompletedTask = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("intCompletedTask"));
        tfCompletedTask.setHorizontalAlignment(JTextField.RIGHT);
        tfCompletedTask.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfCompletedTask);


        // row 6
        row_no++;
        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taDescription = new JTextArea((activity == null || activity.getDescription() == null) ? "" : activity.getDescription());
        taDescription.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        //taDescription.setDragEnabled(true);
        JScrollPane areaScrollPane = new JScrollPane(taDescription);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);

        panel.add(areaScrollPane);
        
        // row 7
        //row_no++;
        label = new JLabel("Instruction");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 108, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taInstruction = new JTextArea(extendedAttrs == null ? "" : (String) extendedAttrs.get("strInstruction"));
        taInstruction.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 108,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        areaScrollPane = new JScrollPane(taInstruction);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 108,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        panel.add(areaScrollPane);
        
        
        label = new JLabel("Multi-action");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 216, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        cbMultiAction = new JComboBox(DialogConstants.NOYES_OPTIONS);
        if (extendedAttrs != null)
            cbMultiAction.setSelectedItem(extendedAttrs.get("strMultiAction"));
        cbMultiAction.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 216,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbMultiAction);

        // row 1, col 2
        row_no = 0;
        label = new JLabel("Reassignable");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbReassignable = new JComboBox(DialogConstants.NOYES_OPTIONS);
        if (extendedAttrs != null)
            cbReassignable.setSelectedItem(extendedAttrs.get("strReassignable"));
        cbReassignable.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbReassignable);


        // row 2, col 2
        row_no++;
        label = new JLabel("Sign off");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbSignoff = new JComboBox(DialogConstants.NOYES_OPTIONS);
        if (extendedAttrs != null)
            cbSignoff.setSelectedItem(extendedAttrs.get("strSignoff"));
        cbSignoff.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbSignoff);

        
        // row 3, col 2
        row_no++;
        label = new JLabel("Working days");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        rbFiveWorkingDays = new JRadioButton("5 days");
        rbFiveWorkingDays.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbFiveWorkingDays);
        
        if (extendedAttrs == null || extendedAttrs.get("intWorkingDays") == null || 
            ((String) extendedAttrs.get("intWorkingDays")).equals("5"))
            rbFiveWorkingDays.setSelected(true);
        
        rbSevenWorkingDays = new JRadioButton("7 days");
        rbSevenWorkingDays.setBounds(550 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbSevenWorkingDays);
        
        if (extendedAttrs != null && extendedAttrs.get("intWorkingDays") != null &&
            ((String) extendedAttrs.get("intWorkingDays")).equals("7"))
            rbSevenWorkingDays.setSelected(true);
        
        // group the radio button into one button group object
        ButtonGroup btg = new ButtonGroup();
        btg.add(rbFiveWorkingDays);
        btg.add(rbSevenWorkingDays);
        
        // row 4, col 2
        row_no++;
        label = new JLabel("Trigger within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfTriggerValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flTriggerValue"));
        tfTriggerValue.setHorizontalAlignment(JTextField.RIGHT);
        tfTriggerValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfTriggerValue);

        cbTriggerUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbTriggerUnit.setSelectedItem(extendedAttrs.get("strTriggerUnit"));
        
        if (tfTriggerValue.getText().equals(""))
            cbTriggerUnit.setSelectedItem("");
        
        cbTriggerUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbTriggerUnit);


        // row 5, col 2
        row_no++;
        label = new JLabel("Send alert to");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbTriggerPerformer = new JCheckBox("Performer");
        ckbTriggerPerformer.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(ckbTriggerPerformer);
        
        if (extendedAttrs != null && extendedAttrs.get("strSendTriggerAlertToPerformer") != null &&
            ((String) extendedAttrs.get("strSendTriggerAlertToPerformer")).equals("yes"))
            ckbTriggerPerformer.setSelected(true);
        
        // row 6, col 2
        row_no++;
        label = new JLabel("");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbTriggerOther = new JCheckBox("Other");
        ckbTriggerOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        ckbTriggerOther.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ckbTriggerOther.isSelected() == false)
                {
                   tfTriggerOther.setText("");   
                }
            }
        });
        
        
        panel.add(ckbTriggerOther);
        
        tfTriggerOther = new JTextField();
        tfTriggerOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        tfTriggerOther.setEditable(false);
        tfTriggerOther.setBackground(Color.white);
        panel.add(tfTriggerOther);
        
        if (extendedAttrs != null && extendedAttrs.get("intSendTriggerAlertToOtherKey") != null)
        {
            DefaultMutableTreeNode orgchartTree = designPanel.getWFDesigner().getOrgChart();
            Enumeration enum = orgchartTree.breadthFirstEnumeration();
            while (enum.hasMoreElements())
            {
                OrgChartUser user = (OrgChartUser) ((DefaultMutableTreeNode) enum.nextElement()).getUserObject();
                if (extendedAttrs.get("intSendTriggerAlertToOtherKey").equals(user.getKey()) &&
                    extendedAttrs.get("strSendTriggerAlertToOtherType").equals(user.getType()))
                {
                   tfTriggerOther.setText(user.getName());
                   ckbTriggerOther.setSelected(true);
                   orgchartTriggerSendTo = user; 
                   break;
                }
            }
        }
        
        btTriggerOrgChart = new JButton("Org chart");
        btTriggerOrgChart.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + 2*DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.BUTTON_WIDTH + 20, DialogConstants.BUTTON_HEIGHT);
        btTriggerOrgChart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrgChartDialog dialog = new OrgChartDialog(ActivityDetailsDialog.this, designPanel.getWFDesigner().getOrgChart(), 1);
                // show dialog in the center
                Dimension screensize = getToolkit().getScreenSize();
                dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);
                dialog.setVisible(true);
            }
        });
        panel.add(btTriggerOrgChart);
        
        // row 7, col 2
        row_no++;
        label = new JLabel("Recurring alert");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        rbTriggerRecurringNo = new JRadioButton("No");
        rbTriggerRecurringNo.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbTriggerRecurringNo);
        
        if (extendedAttrs == null || extendedAttrs.get("strRecurringTriggerAlert") == null || 
            ((String) extendedAttrs.get("strRecurringTriggerAlert")).equals("no"))
            rbTriggerRecurringNo.setSelected(true);
        
        rbTriggerRecurringYes = new JRadioButton("Yes");
        rbTriggerRecurringYes.setBounds(550 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbTriggerRecurringYes);
        
        if (extendedAttrs != null && extendedAttrs.get("strRecurringTriggerAlert") != null && 
            ((String) extendedAttrs.get("strRecurringTriggerAlert")).equals("yes"))
            rbTriggerRecurringYes.setSelected(true);
        
        // group the radio button into one button group object
        btg = new ButtonGroup();
        btg.add(rbTriggerRecurringYes);
        btg.add(rbTriggerRecurringNo);
        
        // row 8, col 2
        row_no++;
        label = new JLabel("Recurring within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfRecurringTriggerValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flRecurringTriggerAlertValue"));
        tfRecurringTriggerValue.setHorizontalAlignment(JTextField.RIGHT);
        tfRecurringTriggerValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfRecurringTriggerValue);

        sepTriggerDivider = new JSeparator();
        sepTriggerDivider.setBounds(425 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 25,
                             450, DialogConstants.TEXTFIELD_HEIGHT);
        sepTriggerDivider.setForeground(Color.black);
        panel.add(sepTriggerDivider);
        
        cbRecurringTriggerUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbRecurringTriggerUnit.setSelectedItem(extendedAttrs.get("strRecurringTriggerAlertUnit"));
        
        // backwards compatibility
        if (tfRecurringTriggerValue.getText().equals(""))
            cbRecurringTriggerUnit.setSelectedItem("");
        
        
        cbRecurringTriggerUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbRecurringTriggerUnit);
        
        // row 9, col 2
        row_no++;
        label = new JLabel("Finished within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfFinishedValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flFinishedValue"));
        tfFinishedValue.setHorizontalAlignment(JTextField.RIGHT);
        tfFinishedValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfFinishedValue);

        cbFinishedUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbFinishedUnit.setSelectedItem(extendedAttrs.get("strFinishedUnit"));
        cbFinishedUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        if (tfFinishedValue.getText().equals(""))
           cbFinishedUnit.setSelectedItem("");
        
        
        panel.add(cbFinishedUnit);

        // row 10, col 2
        row_no++;
        label = new JLabel("Send alert to");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbFinishPerformer = new JCheckBox("Performer");
        ckbFinishPerformer.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(ckbFinishPerformer);
        
        if (extendedAttrs != null && extendedAttrs.get("strSendFinishAlertToPerformer") != null &&
            ((String) extendedAttrs.get("strSendFinishAlertToPerformer")).equals("yes"))
            ckbFinishPerformer.setSelected(true);
        
        // row 11, col 2
        row_no++;
        label = new JLabel("");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbFinishOther = new JCheckBox("Other");
        ckbFinishOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        ckbFinishOther.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ckbFinishOther.isSelected() == false)
                {
                   tfFinishOther.setText("");   
                }
            }
        });
        
        panel.add(ckbFinishOther);
        
        tfFinishOther = new JTextField();
        tfFinishOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        tfFinishOther.setEditable(false);
        tfFinishOther.setBackground(Color.white);
        panel.add(tfFinishOther);
        
        if (extendedAttrs != null && extendedAttrs.get("intSendFinishAlertToOtherKey") != null)
        {
            DefaultMutableTreeNode orgchartTree = designPanel.getWFDesigner().getOrgChart();
            Enumeration enum = orgchartTree.breadthFirstEnumeration();
            while (enum.hasMoreElements())
            {
                OrgChartUser user = (OrgChartUser) ((DefaultMutableTreeNode) enum.nextElement()).getUserObject();
                if (extendedAttrs.get("intSendFinishAlertToOtherKey").equals(user.getKey()) &&
                    extendedAttrs.get("strSendFinishAlertToOtherType").equals(user.getType()))
                {
                   tfFinishOther.setText(user.getName());
                   ckbFinishOther.setSelected(true);
                   orgchartFinishSendTo = user; 
                   break;
                }
            }
        }
        
        btFinishOrgChart = new JButton("Org chart");
        btFinishOrgChart.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + 2*DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.BUTTON_WIDTH + 20, DialogConstants.BUTTON_HEIGHT);
        btFinishOrgChart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrgChartDialog dialog = new OrgChartDialog(ActivityDetailsDialog.this, designPanel.getWFDesigner().getOrgChart(), 2);
                // show dialog in the center
                Dimension screensize = getToolkit().getScreenSize();
                dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);
                dialog.setVisible(true);
            }
        });
        
        panel.add(btFinishOrgChart);
        
        // row 12, col 2
        row_no++;
        label = new JLabel("Recurring alert");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        rbFinishRecurringNo = new JRadioButton("No");
        rbFinishRecurringNo.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbFinishRecurringNo);
        
        if (extendedAttrs == null || extendedAttrs.get("strRecurringFinishAlert") == null || 
            ((String) extendedAttrs.get("strRecurringFinishAlert")).equals("no"))
            rbFinishRecurringNo.setSelected(true);
        
        rbFinishRecurringYes = new JRadioButton("Yes");
        rbFinishRecurringYes.setBounds(550 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbFinishRecurringYes);
        
        if (extendedAttrs != null && extendedAttrs.get("strRecurringFinishAlert") != null && 
            ((String) extendedAttrs.get("strRecurringFinishAlert")).equals("yes"))
            rbFinishRecurringYes.setSelected(true);
        
        // group the radio button into one button group object
        btg = new ButtonGroup();
        btg.add(rbFinishRecurringYes);
        btg.add(rbFinishRecurringNo);
        
        // row 13, col 2
        row_no++;
        label = new JLabel("Recurring within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfRecurringFinishValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flRecurringFinishAlertValue"));
        tfRecurringFinishValue.setHorizontalAlignment(JTextField.RIGHT);
        tfRecurringFinishValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfRecurringFinishValue);

        cbRecurringFinishUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbRecurringFinishUnit.setSelectedItem(extendedAttrs.get("strRecurringFinishAlertUnit"));
        cbRecurringFinishUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);

        if (tfRecurringFinishValue.getText().equals(""))
           cbRecurringFinishUnit.setSelectedItem("");    
        
        
        
        panel.add(cbRecurringFinishUnit);
        
        

        //
        JPanel main_panel = new JPanel();
        main_panel.setLayout(null);
        panel.setBounds(0, 0, 950, 425);
        main_panel.add(panel);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLACK), "Parameters"));

        // Parameters area
        // row 1
        row_no = 0;
        //label = new JLabel("*");
        //label.setForeground(REQUIRED_FIELD_COLOR);
        //label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y, 10, DialogConstants.LABEL_HEIGHT);
        //panel.add(label);
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfParamName = new JTextField();
        tfParamName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfParamName);
        panel.setBounds(0, 430, 950, 160);

        // row 2
        row_no++;
        label = new JLabel("Data type");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbParamDataType = new JComboBox(DialogConstants.DATA_TYPES);
        cbParamDataType.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbParamDataType);

        // row 3
        row_no++;
        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taParamDesc = new JTextArea();
        taParamDesc.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT/2);
        areaScrollPane = new JScrollPane(taParamDesc);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT*2/3);
        panel.add(areaScrollPane);


        // Column 2
        paramListModel = new DefaultListModel();
        if (activity != null)
        {
            Hashtable hashParams = activity.getParameters();
            Enumeration enum = hashParams.elements();
            while (enum.hasMoreElements())
                paramListModel.addElement(enum.nextElement());
        }
        
        ltParams = new JList(paramListModel);
        ltParams.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.TEXTAREA_WIDTH + 50, DialogConstants.TEXTAREA_HEIGHT);

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
                             DialogConstants.TEXTAREA_WIDTH + 50, DialogConstants.TEXTAREA_HEIGHT + 20);
        panel.add(areaScrollPane);


        // add control button
        btAdd = new JButton(">");
        btAdd.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 0, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add a parameter to the param list
                addParameter(tfParamName.getText(), (String) cbParamDataType.getSelectedItem(), taParamDesc.getText());
            }
        });
        panel.add(btAdd);

        btRemove = new JButton("<");
        btRemove.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 20, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove the selected parameter from the param list
                removeParameter(ltParams.getSelectedValue());
            }
        });
        panel.add(btRemove);

        btRemoveAll = new JButton("<<");
        btRemoveAll.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 45, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btRemoveAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove all paramereters
                paramListModel.removeAllElements();
            }
        });
        panel.add(btRemoveAll);
  
        btParamUpdate = new JButton("Update");
        btParamUpdate.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 70, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btParamUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // update selected parameter
                if (updateParameter(ltParams.getSelectedValue(), tfParamName.getText(),(String) cbParamDataType.getSelectedItem(), taParamDesc.getText()) == false)
                {
                    
                    JOptionPane.showMessageDialog(null, "The Parameter failed to update.  Ensure the parameter name is unique.", "No Update", JOptionPane.ERROR_MESSAGE);
                }
                
                
                
                
            }
        });
        panel.add(btParamUpdate);
        
        main_panel.add(panel);

        // last row
        btOK = new JButton("OK");
        btOK.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2, 605, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSaveActivity();
            }
        });
        main_panel.add(btOK);

        btCancel = new JButton("Cancel");
        btCancel.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, 605, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //OrgChartDialog dialog = new OrgChartDialog(ActivityDetailsDialog.this, designPanel.getWFDesigner().getOrgChart());
                //dialog.setVisible(true);
                ActivityDetailsDialog.this.dispose();
            }
        });
        main_panel.add(btCancel);

        return main_panel;
    }
    
    /** Create a dialog to edit a system activity
     */
    private JPanel createSystemActivityDialog()
    {
        // get the current activity
        Activity activity = null;
        Hashtable extendedAttrs = null;
        if (cell != null && cell.getUserObject() != null) {
            if (cell.getUserObject() instanceof Activity)
            {
                activity = (Activity) cell.getUserObject();
                extendedAttrs = activity.getExtendedAttributes();
            }
        }

        // build the activity details form
        int row_no = 0;
        JPanel panel = new JPanel();
        panel.setLayout(null);

        if (activity != null)
            strPrevID = activity.getId();
        
        // row 1
        JLabel label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y, 10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("ID");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfID = new JTextField(activity == null ? DesignPanel.getNextGenId() : activity.getId());
        tfID.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                       DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        // set the ID as a non-editable field
        tfID.setEditable(false);
        tfID.setBackground(java.awt.Color.lightGray);
        tfID.setToolTipText("This is a system generated ID");
        tfID.setEnabled(false);
        panel.add(tfID);


        // row 2
        row_no++;
        label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfName = new JTextField(activity == null ? "" : activity.getName());
        tfName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfName);


        // row 3
        row_no++;
        label = new JLabel("Priority");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbPriority = new JComboBox(DialogConstants.PRIORITY_OPTIONS);
        if (activity != null)
            cbPriority.setSelectedIndex(Integer.parseInt(activity.getPriority()) - 1);
        cbPriority.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbPriority);


        // row 4
        row_no++;
        label = new JLabel("Function");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        Vector vtFunctions = designPanel.getWFDesigner().getFunctions();
       // if (vtFunctions.size() == 0) JOptionPane.showMessageDialog(null, "Function Vector Empty", "note", JOptionPane.ERROR_MESSAGE);
        
        cbFunctionKey = new JComboBox(vtFunctions);
        cbFunctionKey.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbFunctionKey);
        
        if (extendedAttrs != null && extendedAttrs.get("intFunctionKey") != null && vtFunctions != null)
        {
            for (int i=0; i < vtFunctions.size(); i++)
            {
                Function currentFunction = (Function) vtFunctions.get(i);
                if (currentFunction.getKey().equals(extendedAttrs.get("intFunctionKey")))
                {
                    cbFunctionKey.setSelectedItem(currentFunction);
                    break;
                }
            }
        }


        // row 5
        row_no++;
        label = new JLabel("If fail");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbIfFail = new JComboBox(DialogConstants.IFFAIL_OPTIONS);
        if (extendedAttrs != null)
            cbIfFail.setSelectedItem(extendedAttrs.get("strIfFail"));
        cbIfFail.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        
        cbIfFail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            
                String action = e.getActionCommand();
                                
                if (action.equals("comboBoxChanged"))
                {
                    if (cbIfFail.getSelectedItem().equals("Retry"))
                    {
                        tfRetryDelayValue.setEnabled(true);
                        cbRetryDelayUnit.setEnabled(true);
                        tfRetryCounter.setEnabled(true);
                        cbRetryFail.setEnabled(true);
                    }
                    else
                    {
                        tfRetryDelayValue.setText("");
                        tfRetryDelayValue.setEnabled(false);
                        
                        cbRetryDelayUnit.setSelectedIndex(0);
                        cbRetryDelayUnit.setEnabled(false);
                        
                        tfRetryCounter.setText("");
                        tfRetryCounter.setEnabled(false);
                        
                        cbRetryFail.setSelectedIndex(0);
                        cbRetryFail.setEnabled(false);
                    }
                }
                
                
                
            }
        
        
         });
        
        
        
        
        
        
        
        panel.add(cbIfFail);


        // row 6
        row_no++;
        label = new JLabel("Retry within");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfRetryDelayValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flRetryDelayValue"));
        tfRetryDelayValue.setHorizontalAlignment(JTextField.RIGHT);
        tfRetryDelayValue.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        
        if (cbIfFail.getSelectedItem().equals("Retry"))
            tfRetryDelayValue.setEnabled(true);
        else
            tfRetryDelayValue.setEnabled(false);
        
        panel.add(tfRetryDelayValue);

        cbRetryDelayUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbRetryDelayUnit.setSelectedItem(extendedAttrs.get("strRetryDelayUnit"));
        cbRetryDelayUnit.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);

        if (cbIfFail.getSelectedItem().equals("Retry"))
            cbRetryDelayUnit.setEnabled(true);
        else    
            cbRetryDelayUnit.setEnabled(false);
        
        panel.add(cbRetryDelayUnit);

        // row 7
        row_no++;
        label = new JLabel("No of retry");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfRetryCounter = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("intRetryCounter"));
        tfRetryCounter.setHorizontalAlignment(JTextField.RIGHT);
        tfRetryCounter.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        if (cbIfFail.getSelectedItem().equals("Retry"))
           tfRetryCounter.setEnabled(true);
        else
           tfRetryCounter.setEnabled(false);
        
        panel.add(tfRetryCounter);
        
        // row 8
        row_no++;
        label = new JLabel("If retry fail");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbRetryFail = new JComboBox(DialogConstants.IFRETRYFAIL_OPTIONS);
        if (extendedAttrs != null)
            cbRetryFail.setSelectedItem(extendedAttrs.get("strIfFailAfterRetry"));
        cbRetryFail.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        if (cbIfFail.getSelectedItem().equals("Retry"))
           cbRetryFail.setEnabled(true); 
        else
           cbRetryFail.setEnabled(false);
        panel.add(cbRetryFail);

        // row 9
        row_no++;
        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taDescription = new JTextArea(activity == null ? "" : activity.getDescription());
        taDescription.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        JScrollPane areaScrollPane = new JScrollPane(taDescription);
        //areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);

        panel.add(areaScrollPane);
        
        
        // row 10
        label = new JLabel("Instruction");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 108, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taInstruction = new JTextArea(extendedAttrs == null ? "" : (String) extendedAttrs.get("strInstruction"));
        taInstruction.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 108,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        areaScrollPane = new JScrollPane(taInstruction);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT + 108,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        panel.add(areaScrollPane);



        // row 1, col 2
        row_no = 0;
        label = new JLabel("Working days");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        
        rbFiveWorkingDays = new JRadioButton("5 days");
        rbFiveWorkingDays.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbFiveWorkingDays);
        
        if (extendedAttrs == null || extendedAttrs.get("intWorkingDays") == null || 
            ((String) extendedAttrs.get("intWorkingDays")).equals("5"))
            rbFiveWorkingDays.setSelected(true);
        
        rbSevenWorkingDays = new JRadioButton("7 days");
        rbSevenWorkingDays.setBounds(550 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbSevenWorkingDays);
        
        if (extendedAttrs != null && extendedAttrs.get("intWorkingDays") != null &&
            ((String) extendedAttrs.get("intWorkingDays")).equals("7"))
            rbSevenWorkingDays.setSelected(true);
        
        // group the radio button into one button group object
        ButtonGroup btg = new ButtonGroup();
        btg.add(rbFiveWorkingDays);
        btg.add(rbSevenWorkingDays);
        
        // row 2, col 2
        row_no++;
        label = new JLabel("Trigger within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfTriggerValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flTriggerValue"));
        tfTriggerValue.setHorizontalAlignment(JTextField.RIGHT);
        tfTriggerValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfTriggerValue);

        cbTriggerUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbTriggerUnit.setSelectedItem(extendedAttrs.get("strTriggerUnit"));
        cbTriggerUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        if (tfTriggerValue.getText().equals(""))
            cbTriggerUnit.setSelectedItem("");
        
        panel.add(cbTriggerUnit);

        // row 3, col 2
        row_no++;
        label = new JLabel("Send alert to");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbTriggerPerformer = new JCheckBox("Performer");
        ckbTriggerPerformer.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(ckbTriggerPerformer);
        
        if (extendedAttrs != null && extendedAttrs.get("strSendTriggerAlertToPerformer") != null &&
            ((String) extendedAttrs.get("strSendTriggerAlertToPerformer")).equals("yes"))
            ckbTriggerPerformer.setSelected(true);
        

        // row 4, col 2
        row_no++;
        label = new JLabel("");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbTriggerOther = new JCheckBox("Other");
        ckbTriggerOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(ckbTriggerOther);
        
        tfTriggerOther = new JTextField();
        tfTriggerOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfTriggerOther);
        
        if (extendedAttrs != null && extendedAttrs.get("intSendTriggerAlertToOtherKey") != null)
        {
            DefaultMutableTreeNode orgchartTree = designPanel.getWFDesigner().getOrgChart();
            Enumeration enum = orgchartTree.breadthFirstEnumeration();
            while (enum.hasMoreElements())
            {
                OrgChartUser user = (OrgChartUser) ((DefaultMutableTreeNode) enum.nextElement()).getUserObject();
                if (extendedAttrs.get("intSendTriggerAlertToOtherKey").equals(user.getKey()) &&
                    extendedAttrs.get("strSendTriggerAlertToOtherType").equals(user.getType()))
                {
                   tfTriggerOther.setText(user.getName());
                   ckbTriggerOther.setSelected(true);
                   orgchartTriggerSendTo = user; 
                   break;
                }
            }
        }
        
        btTriggerOrgChart = new JButton("Org chart");
        btTriggerOrgChart.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + 2*DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.BUTTON_WIDTH + 20, DialogConstants.BUTTON_HEIGHT);
        btTriggerOrgChart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrgChartDialog dialog = new OrgChartDialog(ActivityDetailsDialog.this, designPanel.getWFDesigner().getOrgChart(), 1);
                // show dialog in the center
                Dimension screensize = getToolkit().getScreenSize();
                dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);
                dialog.setVisible(true);
            }
        });
        panel.add(btTriggerOrgChart);
        
        // row 5, col 2
        row_no++;
        label = new JLabel("Recurring alert");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        rbTriggerRecurringNo = new JRadioButton("No");
        rbTriggerRecurringNo.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbTriggerRecurringNo);
        
        if (extendedAttrs == null || extendedAttrs.get("strRecurringTriggerAlert") == null || 
            ((String) extendedAttrs.get("strRecurringTriggerAlert")).equals("no"))
            rbTriggerRecurringNo.setSelected(true);
        
        rbTriggerRecurringYes = new JRadioButton("Yes");
        rbTriggerRecurringYes.setBounds(550 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbTriggerRecurringYes);
        
        if (extendedAttrs != null && extendedAttrs.get("strRecurringTriggerAlert") != null && 
            ((String) extendedAttrs.get("strRecurringTriggerAlert")).equals("yes"))
            rbTriggerRecurringYes.setSelected(true);
        
        // group the radio button into one button group object
        btg = new ButtonGroup();
        btg.add(rbTriggerRecurringYes);
        btg.add(rbTriggerRecurringNo);
        
        // row 6, col 2
        row_no++;
        label = new JLabel("Recurring within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfRecurringTriggerValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flRecurringTriggerAlertValue"));
        tfRecurringTriggerValue.setHorizontalAlignment(JTextField.RIGHT);
        tfRecurringTriggerValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfRecurringTriggerValue);

        cbRecurringTriggerUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbRecurringTriggerUnit.setSelectedItem(extendedAttrs.get("strRecurringTriggerAlertUnit"));
        cbRecurringTriggerUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        if (tfRecurringTriggerValue.getText().equals(""))
            cbRecurringTriggerUnit.setSelectedItem("");
        
        
        panel.add(cbRecurringTriggerUnit);
        
        
        // row 7, col 2
        row_no++;
        label = new JLabel("Finished within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfFinishedValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flFinishedValue"));
        tfFinishedValue.setHorizontalAlignment(JTextField.RIGHT);
        tfFinishedValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfFinishedValue);

        cbFinishedUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbFinishedUnit.setSelectedItem(extendedAttrs.get("strFinishedUnit"));
        cbFinishedUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        if (tfFinishedValue.getText().equals(""))
           cbFinishedUnit.setSelectedItem("");
        
        panel.add(cbFinishedUnit);


        // row 8, col 2
        row_no++;
        label = new JLabel("Send alert to");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbFinishPerformer = new JCheckBox("Performer");
        ckbFinishPerformer.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(ckbFinishPerformer);
        
        if (extendedAttrs != null && extendedAttrs.get("strSendFinishAlertToPerformer") != null &&
            ((String) extendedAttrs.get("strSendFinishAlertToPerformer")).equals("yes"))
            ckbFinishPerformer.setSelected(true);
        
        // row 9, col 2
        row_no++;
        label = new JLabel("");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        ckbFinishOther = new JCheckBox("Other");
        ckbFinishOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(ckbFinishOther);
        
        tfFinishOther = new JTextField();
        tfFinishOther.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfFinishOther);
        
        if (extendedAttrs != null && extendedAttrs.get("intSendFinishAlertToOtherKey") != null)
        {
            DefaultMutableTreeNode orgchartTree = designPanel.getWFDesigner().getOrgChart();
            Enumeration enum = orgchartTree.breadthFirstEnumeration();
            while (enum.hasMoreElements())
            {
                OrgChartUser user = (OrgChartUser) ((DefaultMutableTreeNode) enum.nextElement()).getUserObject();
                if (extendedAttrs.get("intSendFinishAlertToOtherKey").equals(user.getKey()) &&
                    extendedAttrs.get("strSendFinishAlertToOtherType").equals(user.getType()))
                {
                   tfFinishOther.setText(user.getName());
                   ckbFinishOther.setSelected(true);
                   orgchartFinishSendTo = user; 
                   break;
                }
            }
        }
        
        btFinishOrgChart = new JButton("Org chart");
        btFinishOrgChart.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + 2*DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.BUTTON_WIDTH + 20, DialogConstants.BUTTON_HEIGHT);
        btFinishOrgChart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrgChartDialog dialog = new OrgChartDialog(ActivityDetailsDialog.this, designPanel.getWFDesigner().getOrgChart(), 2);
                // show dialog in the center
                Dimension screensize = getToolkit().getScreenSize();
                dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);
                dialog.setVisible(true);
            }
        });
        panel.add(btFinishOrgChart);
        
        // row 10, col 2
        row_no++;
        label = new JLabel("Recurring alert");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        rbFinishRecurringNo = new JRadioButton("No");
        rbFinishRecurringNo.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbFinishRecurringNo);
        
        if (extendedAttrs == null || extendedAttrs.get("strRecurringFinishAlert") == null || 
            ((String) extendedAttrs.get("strRecurringFinishAlert")).equals("no"))
            rbFinishRecurringNo.setSelected(true);
        
        rbFinishRecurringYes = new JRadioButton("Yes");
        rbFinishRecurringYes.setBounds(550 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(rbFinishRecurringYes);
        
        if (extendedAttrs != null && extendedAttrs.get("strRecurringFinishAlert") != null && 
            ((String) extendedAttrs.get("strRecurringFinishAlert")).equals("yes"))
            rbFinishRecurringYes.setSelected(true);
        
        // group the radio button into one button group object
        btg = new ButtonGroup();
        btg.add(rbFinishRecurringYes);
        btg.add(rbFinishRecurringNo);
        
        // row 11, col 2
        row_no++;
        label = new JLabel("Recurring within");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        tfRecurringFinishValue = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("flRecurringFinishAlertValue"));
        tfRecurringFinishValue.setHorizontalAlignment(JTextField.RIGHT);
        tfRecurringFinishValue.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfRecurringFinishValue);

        cbRecurringFinishUnit = new JComboBox(DialogConstants.TRIGGER_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbRecurringFinishUnit.setSelectedItem(extendedAttrs.get("strRecurringFinishAlertUnit"));
        cbRecurringFinishUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.SHORT_TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        
        if (tfRecurringFinishValue.getText().equals(""))
            cbRecurringFinishUnit.setSelectedItem("");
        
        
        panel.add(cbRecurringFinishUnit);
        
        
        // row 12, col 2
        row_no++;
        label = new JLabel("Start at");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        
        String strIntervalValue = null;
        if (extendedAttrs != null)
            strIntervalValue = (String) extendedAttrs.get("strIntervalValue");
        
        cbHour = new JComboBox(DialogConstants.HOURS);
        if (strIntervalValue != null && !strIntervalValue.equals(""))
            cbHour.setSelectedItem(strIntervalValue.substring(0, 2));
        cbHour.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             42, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbHour);

        cbMinute = new JComboBox(DialogConstants.MINUTES);
        if (strIntervalValue != null && !strIntervalValue.equals(""))
            cbMinute.setSelectedItem(strIntervalValue.substring(3, 5));
        cbMinute.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + 42, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             42, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbMinute);

        cbAMPM = new JComboBox(DialogConstants.AMPM);
        if (strIntervalValue != null && !strIntervalValue.equals(""))
            cbAMPM.setSelectedItem(strIntervalValue.substring(6));
        cbAMPM.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + 42 + 42, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             46, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbAMPM);

        cbIntervalUnit = new JComboBox(DialogConstants.INTERVAL_PERIOD_OPTIONS);
        if (extendedAttrs != null)
            cbIntervalUnit.setSelectedItem(extendedAttrs.get("strIntervalUnit"));
        cbIntervalUnit.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH + 42 + 42 + 46, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             74, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbIntervalUnit);


        // row 13, col 2
        row_no++;
        label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(450 + DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                        10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("Completed tasks");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfCompletedTask = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("intCompletedTask"));
        tfCompletedTask.setHorizontalAlignment(JTextField.RIGHT);
        tfCompletedTask.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfCompletedTask);


        // row 7, col 2
        
        JPanel main_panel = new JPanel();
        main_panel.setLayout(null);
        panel.setBounds(0, 0, 950, 450);
        main_panel.add(panel);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLACK), "Parameters"));

        // Parameters area
        // row 1
        row_no = 0;
        //label = new JLabel("*");
        //label.setForeground(REQUIRED_FIELD_COLOR);
        //label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y, 10, DialogConstants.LABEL_HEIGHT);
        //panel.add(label);
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfParamName = new JTextField();
        tfParamName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfParamName);
        panel.setBounds(0, 455, 950, 160);

        // row 2
        row_no++;
        label = new JLabel("Data type");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbParamDataType = new JComboBox(DialogConstants.DATA_TYPES);
        cbParamDataType.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbParamDataType);

        // row 3
        row_no++;
        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taParamDesc = new JTextArea();
        taParamDesc.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT/2);
        areaScrollPane = new JScrollPane(taParamDesc);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT*2/3);
        panel.add(areaScrollPane);


        // Column 2
        paramListModel = new DefaultListModel();
        if (activity != null)
        {
            Hashtable hashParams = activity.getParameters();
            Enumeration enum = hashParams.elements();
            while (enum.hasMoreElements())
                paramListModel.addElement(enum.nextElement());
        }
        
        ltParams = new JList(paramListModel);
        ltParams.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.TEXTAREA_WIDTH + 50, DialogConstants.TEXTAREA_HEIGHT);
        
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
                             DialogConstants.TEXTAREA_WIDTH + 50, DialogConstants.TEXTAREA_HEIGHT + 20);
        panel.add(areaScrollPane);


        // add control button
        btAdd = new JButton(">");
        btAdd.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 0, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add a parameter to the param list
                addParameter(tfParamName.getText(), (String) cbParamDataType.getSelectedItem(), taParamDesc.getText());
            }
        });
        panel.add(btAdd);

        btRemove = new JButton("<");
        btRemove.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 20, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove the selected parameter from the param list
                removeParameter(ltParams.getSelectedValue());
            }
        });
        panel.add(btRemove);

        btRemoveAll = new JButton("<<");
        btRemoveAll.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 45, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btRemoveAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove all paramereters
                paramListModel.removeAllElements();
            }
        });
        panel.add(btRemoveAll);

        btParamUpdate = new JButton("Update");
        btParamUpdate.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 70, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btParamUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // update selected parameter
                if (updateParameter(ltParams.getSelectedValue(), tfParamName.getText(),(String) cbParamDataType.getSelectedItem(), taParamDesc.getText()) == false)
                {
                    
                    JOptionPane.showMessageDialog(null, "The Parameter failed to update.  Ensure the parameter name is unique.", "No Update", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
        panel.add(btParamUpdate);
        
        
        
        main_panel.add(panel);


        // last row
        btOK = new JButton("OK");
        btOK.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2, 630, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSaveActivity();
            }
        });
        main_panel.add(btOK);

        btCancel = new JButton("Cancel");
        btCancel.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, 630, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ActivityDetailsDialog.this.dispose();
            }
        });
        main_panel.add(btCancel);
        
        return main_panel;
    }
    
    /** Create a dialog to edit a sub-workflow activity
     */
    private JPanel createSubWorkflowActivityDialog()
    {
        // get the current activity
        Activity activity = null;
        Hashtable extendedAttrs = null;
        if (cell != null && cell.getUserObject() != null) {
            if (cell.getUserObject() instanceof Activity)
            {
                activity = (Activity) cell.getUserObject();
                extendedAttrs = activity.getExtendedAttributes();
            }
        }

        if (activity != null)
            strPrevID = activity.getId();
        
        
        // build the activity details form
        int row_no = 0;
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // row 1
        JLabel label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y, 10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("ID");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfID = new JTextField(activity == null ? DesignPanel.getNextGenId() : activity.getId());
        tfID.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                       DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        // set the ID as a non-editable field
        tfID.setEditable(false);
        tfID.setBackground(java.awt.Color.lightGray);
        tfID.setToolTipText("This is a system generated ID");
        tfID.setEnabled(false);
        panel.add(tfID);

        // row 2
        row_no++;
        label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                        10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfName = new JTextField(activity == null ? "" : activity.getName());
        tfName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfName);


        // row 3
        row_no++;
        label = new JLabel("Priority");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbPriority = new JComboBox(DialogConstants.PRIORITY_OPTIONS);
        if (activity != null)
            cbPriority.setSelectedIndex(Integer.parseInt(activity.getPriority()) - 1);
        cbPriority.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbPriority);

        // row 4
        row_no++;
        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taDescription = new JTextArea(activity == null ? "" : activity.getDescription());
        taDescription.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        JScrollPane areaScrollPane = new JScrollPane(taDescription);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);

        panel.add(areaScrollPane);

        // row 1, col 2
        row_no = 0;
        label = new JLabel("*");
        label.setForeground(REQUIRED_FIELD_COLOR);
        label.setBounds(450 + DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                        10, DialogConstants.LABEL_HEIGHT);
        panel.add(label);
        label = new JLabel("Completed tasks");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfCompletedTask = new JTextField(extendedAttrs == null ? "" : (String) extendedAttrs.get("intCompletedTask"));
        tfCompletedTask.setHorizontalAlignment(JTextField.RIGHT);
        tfCompletedTask.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfCompletedTask);




        // row 2, col 2
        row_no++;
        label = new JLabel("Sub workflow");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        Vector vtSubWorkflows = getSubWorkflows();
        cbSubWorkflow = new JComboBox(vtSubWorkflows);
        
        if (extendedAttrs != null && extendedAttrs.get("strSubWorkflowPackageXPDLId") != null && 
            extendedAttrs.get("strSubWorkflowTemplateXPDLId") != null )
        {
            String strPackageID = (String) extendedAttrs.get("strSubWorkflowPackageXPDLId");
            String strTemplateID = (String) extendedAttrs.get("strSubWorkflowTemplateXPDLId");
            for (int i=0; i < vtSubWorkflows.size(); i++)
            {
                SubWorkflow currentSubWorkflow = (SubWorkflow) vtSubWorkflows.get(i);
                if (strPackageID.equals(currentSubWorkflow.getPackageID()) && strTemplateID.equals(currentSubWorkflow.getWorkflowID()))
                {
                    cbSubWorkflow.setSelectedItem(currentSubWorkflow);
                    break;
                }
            }
        }
        
        
        cbSubWorkflow.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbSubWorkflow);

        
        // row 3, col 2
        row_no++;
        label = new JLabel("Instruction");
        label.setBounds(450 + DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taInstruction = new JTextArea(extendedAttrs == null ? "" : (String) extendedAttrs.get("strInstruction"));
        taInstruction.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        areaScrollPane = new JScrollPane(taInstruction);
        areaScrollPane.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        panel.add(areaScrollPane);


        JPanel main_panel = new JPanel();
        main_panel.setLayout(null);
        panel.setBounds(0, 0, 900, 220);
        main_panel.add(panel);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLACK), "Parameters"));

        // Parameters area
        // row 1
        row_no = 0;
        //label = new JLabel("*");
        //label.setForeground(REQUIRED_FIELD_COLOR);
        //label.setBounds(DialogConstants.TOPLEFT_X - 10, DialogConstants.TOPLEFT_Y, 10, DialogConstants.LABEL_HEIGHT);
        //panel.add(label);
        label = new JLabel("Name");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        tfParamName = new JTextField();
        tfParamName.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(tfParamName);
        panel.setBounds(0, 220, 900, 160);
        //panel.setBounds(0, 320, 700, 160);

        // row 2
        row_no++;
        label = new JLabel("Data type");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        cbParamDataType = new JComboBox(DialogConstants.DATA_TYPES);
        cbParamDataType.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + DialogConstants.LINE_HEIGHT,
                         DialogConstants.TEXTFIELD_WIDTH, DialogConstants.TEXTFIELD_HEIGHT);
        panel.add(cbParamDataType);

        // row 3
        row_no++;
        label = new JLabel("Description");
        label.setBounds(DialogConstants.TOPLEFT_X, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT, 
                        DialogConstants.LABEL_WIDTH, DialogConstants.LABEL_HEIGHT);
        panel.add(label);

        taParamDesc = new JTextArea();
        taParamDesc.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT/2);
        areaScrollPane = new JScrollPane(taParamDesc);
        areaScrollPane.setBounds(DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y + row_no*DialogConstants.LINE_HEIGHT,
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT*2/3);
        panel.add(areaScrollPane);


        // Column 2
        paramListModel = new DefaultListModel();
        if (activity != null)
        {
            Hashtable hashParams = activity.getParameters();
            Enumeration enum = hashParams.elements();
            while (enum.hasMoreElements())
                paramListModel.addElement(enum.nextElement());
        }
        
        ltParams = new JList(paramListModel);
        ltParams.setBounds(450 + DialogConstants.TOPLEFT_X + DialogConstants.LABEL_WIDTH, DialogConstants.TOPLEFT_Y, 
                        DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT);
        
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
                             DialogConstants.TEXTAREA_WIDTH, DialogConstants.TEXTAREA_HEIGHT + 20);
        panel.add(areaScrollPane);


        // add control button
        btAdd = new JButton(">");
        btAdd.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 0, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add a parameter to the param list
                addParameter(tfParamName.getText(), (String) cbParamDataType.getSelectedItem(), taParamDesc.getText());
            }
        });
        panel.add(btAdd);

        btRemove = new JButton("<");
        btRemove.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 20, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove the selected parameter from the param list
                removeParameter(ltParams.getSelectedValue());
            }
        });
        panel.add(btRemove);

        btRemoveAll = new JButton("<<");
        btRemoveAll.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 45, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btRemoveAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove all paramereters
                paramListModel.removeAllElements();
            }
        });
        panel.add(btRemoveAll);

        btParamUpdate = new JButton("Update");
        btParamUpdate.setBounds((DIALOG_WIDTH - DialogConstants.BUTTON_WIDTH)/2 + 50, DialogConstants.TOPLEFT_Y + 70, 
                       DialogConstants.SHORT_BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btParamUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // update selected parameter
                if (updateParameter(ltParams.getSelectedValue(), tfParamName.getText(),(String) cbParamDataType.getSelectedItem(), taParamDesc.getText()) == false)
                {
                    
                    JOptionPane.showMessageDialog(null, "The Parameter failed to update.  Ensure the parameter name is unique.", "No Update", JOptionPane.ERROR_MESSAGE);
                }
                
                
                
                
            }
        });
        panel.add(btParamUpdate);
        
        
        
        
        main_panel.add(panel);

        // last row
        btOK = new JButton("OK");
        btOK.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2, 400, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if(isThereBadLoop() == true)
                {
                    JOptionPane.showMessageDialog(null, "Unable to Save : \nSaving this activity will create an infinite loop between the two workflows", "Error Saving", JOptionPane.ERROR_MESSAGE);
                }
                else
                   doSaveActivity();
            }
            
                
            
            
        });
        main_panel.add(btOK);

        btCancel = new JButton("Cancel");
        btCancel.setBounds((DIALOG_WIDTH - 2*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, 400, 
                       DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ActivityDetailsDialog.this.dispose();
            }
        });
        main_panel.add(btCancel);

        return main_panel;
    }
    
    /** Checks the workflow that has been selected as a subworkflow in the current
     * dialog to see if it calls the currently loaded workflow, which would
     * potentially cause an infinite loop.
     * @return false if no loop is generated, true if it is
     */    
    private boolean isThereBadLoop()
    {
        
        // ensure that saving this activity will not cause
        // an infinate loop
                
        Vector vtSubWF = getSubWorkflows();
        SubWorkflow tempWF = null;
        SubWorkflow selectedWF = null;

        boolean isWorkflowFound = false;

        // check that this is not a direct loopback

        selectedWF = (SubWorkflow) cbSubWorkflow.getSelectedItem();


        if (selectedWF.getWorkflowID().equals(designPanel.getGraphId()))
        {
            return true;
        }




        // find the subworkflow thats being referenced by the dialog
        for (int i = 0; i < vtSubWF.size(); i++)
        {
            tempWF = (SubWorkflow) vtSubWF.get(i);

            if (tempWF.getWorkflowID().equals(selectedWF.getWorkflowID()))  // may need to change to do an ID check instead
            {
                isWorkflowFound = true;
                break;
            }
        }



        if (isWorkflowFound == false)
        {
            return false;
        }

        // got selected workflow
        Hashtable hashActivities = tempWF.getWorkflowProcess().getActivities();
        Enumeration enumActivities = hashActivities.elements();

        Hashtable extendedAttributes = null;


        while (enumActivities.hasMoreElements()) // loop through activities
        {
            Activity tempActivity = (Activity) enumActivities.nextElement();
            extendedAttributes = tempActivity.getExtendedAttributes();

            // check if this activity is a subworkflow
            if (((String) extendedAttributes.get("strType")).equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
            {
                // get the details of the subworkflow
                String tempPackageName = (String) extendedAttributes.get("strSubWorkflowPackageXPDLId");
                String tempTemplateName = (String) extendedAttributes.get("strSubWorkflowTemplateXPDLId");

                if ((designPanel.getGraphPackageId().equals(tempPackageName)) && (designPanel.getGraphId().equals(tempTemplateName)))
                {
                    // Infinite loop found
                    return true;
                }


            }
        }   

        return false;
        
        
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
                paramListModel.addElement(new Parameter(strName, strType, strDesc));
        }
    }
    
    /** Remove a parameter from list
     */
    private void removeParameter(Object param)
    {
        paramListModel.removeElement(param);
    }
    
    /** Set performer
     */
    public void setPerformer(Object obj)
    {
        orgchartPerformer = (OrgChartUser) obj;
        tfPerformer.setText(orgchartPerformer.getName());
        //System.err.println(orgchartuser);
    }
    
    /** Set trigger send to
     */
    public void setTriggerSendTo(Object obj)
    {
        orgchartTriggerSendTo = (OrgChartUser) obj;
        tfTriggerOther.setText(orgchartTriggerSendTo.getName());
        //System.err.println(orgchartuser);
    }
    
    /** Set finish send to
     */
    public void setFinishSendTo(Object obj)
    {
        orgchartFinishSendTo = (OrgChartUser) obj;
        tfFinishOther.setText(orgchartFinishSendTo.getName());
        //System.err.println(orgchartuser);
    }
    
    /** Get all existing sub workflows
     */
    private Vector getSubWorkflows()
    {
        Vector vtSubWorkflows = new Vector(10, 5);
        
        // get all the packages
        Vector vtPackages = designPanel.getWFDesigner().getPackages();
        if (vtPackages != null)
        {
            for (int i=0; i < vtPackages.size(); i++)
            {
                neuragenix.genix.workflow.Package currentPackage = (neuragenix.genix.workflow.Package) vtPackages.get(i);
                Hashtable hashWorkflows = currentPackage.getWorkflowProcesses();
                Enumeration enum = hashWorkflows.elements();
                while (enum.hasMoreElements())
                {
                    vtSubWorkflows.add(new SubWorkflow(currentPackage.getId(), (WorkflowProcess) enum.nextElement()));
                }
            }
        }
        
        return vtSubWorkflows;
    }
    
    
    class SubWorkflow
    {
        String strPackageID;
        WorkflowProcess wfProcess;
        
        SubWorkflow(String strPackageID, WorkflowProcess wfProcess)
        {
            this.strPackageID = strPackageID;
            this.wfProcess = wfProcess;
        }
        
        String getPackageID()
        {
            return strPackageID;
        }
        
        String getWorkflowID()
        {
            return wfProcess.getId();
        }
        
        WorkflowProcess getWorkflowProcess()
        {
            return wfProcess;
        }
        
        
        public String toString()
        {
            return wfProcess.getName();
        }
    }
}