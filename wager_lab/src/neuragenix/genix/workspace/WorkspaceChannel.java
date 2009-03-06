/**
 * WorkspaceChannel.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 26/04/2004
 */
  
package neuragenix.genix.workspace;

/**
 * 
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Vector;
import java.util.Hashtable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.ResultSet;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

// neuragenix packages
import neuragenix.dao.DBField;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DBMSTypes;
import neuragenix.dao.DALQuery;
import neuragenix.utils.Password;
import neuragenix.common.*;
import neuragenix.common.Utilities;
import neuragenix.genix.workflow.WorkflowManager;

public class WorkspaceChannel
{
    private static final String START_TAG = "<";
    private static final String END_TAG = "/>";
    private static final String START_LINK_TAG = "<a href=\"";
    private static final String END_LINK_TAG = "\">";
    private static final String END_LINK_TAG_2 = "</a>";
    private static final String CASE_URL = "{<CaseURL/>}?";
    private static final String SMARTFORM_URL = "{<SmartformURL/>}?";
    
    /** Creates a new instance of WorkspaceChannel */
    public WorkspaceChannel()
    { 
    } 

    /** Build XML string for a specific task
     * @param vector formfields
     * @param task's key
     * @param system user name
     * @return XML string
     */
    public static String buildViewTaskFromKey(boolean isFromWorkspace, Vector vtFields, String strCurrent, String strSource, String strTaskKey, String strSystemUser) throws Exception
    {
        StringBuffer strResult = new StringBuffer();
        int intNoOfFields = vtFields.size();
        
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("WORKFLOW_TASK", null, null, null);
            query.setFields(vtFields, null);
            query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();
            
            while (rsResultSet.next())
            {
                String intWorkflowInstanceKey = rsResultSet.getString("WORKFLOW_TASK_intWorkflowInstanceKey");
                for (int i=0; i < intNoOfFields; i++)
                {
                    String strFieldName = (String) vtFields.get(i);
                    DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
                    
                    String data = rsResultSet.getString(i + 1);
                    
                    if (strFieldName.equals("WORKFLOW_TASK_strAction"))
                    {
                        strResult.append(buildTaskActionDropdown(strTaskKey, data));
                    }
                    else if (strFieldName.equals("WORKFLOW_TASK_strPriority"))
                    {
                        if (data.equals("1"))
                            strResult.append("<WORKFLOW_TASK_strPriority>Low</WORKFLOW_TASK_strPriority>");
                        else if (data.equals("2"))
                            strResult.append("<WORKFLOW_TASK_strPriority>Medium</WORKFLOW_TASK_strPriority>");
                        else if (data.equals("3"))
                            strResult.append("<WORKFLOW_TASK_strPriority>High</WORKFLOW_TASK_strPriority>");
                    }
                    else if (strFieldName.equals("WORKFLOW_TASK_strPerformerType"))
                    {
                        if (data.equals("Group"))
                        {
                            String strGroupKey = rsResultSet.getString("WORKFLOW_TASK_intPerformer");
                            strResult.append(buildUserListDropdown(strGroupKey, strSystemUser));
                        }
                        else
                        {
                            String strUserKey = rsResultSet.getString("WORKFLOW_TASK_intPerformer");
                            strResult.append("<OrgUser><WORKFLOW_TASK_intPerformer>" + strUserKey + "</WORKFLOW_TASK_intPerformer></OrgUser>");
                            strResult.append("<multi_user>false</multi_user>");
                        }
                    }
                    else if (strFieldName.equals("WORKFLOW_TASK_strName"))
                    {
                        String strName = replaceRuntimeData(data, intWorkflowInstanceKey);
                        strResult.append("<WORKFLOW_TASK_strName>" + Utilities.cleanForXSL(strName) + "</WORKFLOW_TASK_strName>");
                    }
                    else if (strFieldName.equals("WORKFLOW_TASK_strInstruction"))
                    {
                        String strInstruction = buildLinkXMLFile(isFromWorkspace, strCurrent, strSource, strTaskKey, replaceRuntimeData(data, intWorkflowInstanceKey));
                        strResult.append(strInstruction);
                    }
                    else
                    {
                        // if the field is a dropdown field
                        if (field.getLOVType() != null)
                            strResult.append(QueryChannel.buildLOVXMLFile(strFieldName, data));
                        // if the field is a date type field
                        else if (field.getDataType() == DBMSTypes.DATE_TYPE)
                        {
                            if (data != null)
                                strResult.append(QueryChannel.buildDateDropDownXMLFile(strFieldName, Utilities.convertDateForDisplay(data)));
                            else
                                strResult.append(QueryChannel.buildDateDropDownXMLFile(strFieldName, null));
                        }
                        // if the field is a time type field
                        else if (field.getDataType() == DBMSTypes.TIME_TYPE)
                        {
                            if (data != null)
                                strResult.append(QueryChannel.buildTimeDropDownXMLFile(strFieldName, Utilities.convertTimeForDisplay(data)));
                            else
                                strResult.append(QueryChannel.buildTimeDropDownXMLFile(strFieldName, null));
                        }
                        // normal field
                        else if (data != null)
                        {
                            strResult.append("<" + strFieldName + ">" + Utilities.cleanForXSL(data) + "</" + strFieldName + ">");
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkspaceChannel - " + e.toString(), e);
            throw new Exception("Unknown error in QueryChannel - " + e.toString());
        }
        
        
        return strResult.toString();
    }
    
    /** Build the task's actions dropdown
     *  @param task key
     *  @param current selected action
     *  @return a XML format string to build the dropdown list
     */
    public static String buildTaskActionDropdown(String strTaskKey, String strCurrentAction) throws Exception
    {
        StringBuffer strResult = new StringBuffer();
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("WORKFLOW_TASK_TRANSITION", null, null, null);
            query.setField("WORKFLOW_TASK_TRANSITION_strName", null);
            query.setDistinctField("WORKFLOW_TASK_TRANSITION_strName");
            query.setWhere(null, 0, "WORKFLOW_TASK_TRANSITION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_intOriginTaskKey", "=", strTaskKey, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "WORKFLOW_TASK_TRANSITION_strName", "<>", "", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            if (rsResult.next())
            {
                strResult.append("<hasAction>true</hasAction>");
                strResult.append("<WORKFLOW_TASK_strAction selected=\"0\"></WORKFLOW_TASK_strAction>");
                String strAction = rsResult.getString("WORKFLOW_TASK_TRANSITION_strName");
                
                if (strCurrentAction != null && strAction.equals(strCurrentAction))
                    strResult.append("<WORKFLOW_TASK_strAction selected=\"1\">" + Utilities.cleanForXSL(strAction) + "</WORKFLOW_TASK_strAction>");
                else
                    strResult.append("<WORKFLOW_TASK_strAction selected=\"0\">" + Utilities.cleanForXSL(strAction) + "</WORKFLOW_TASK_strAction>");
                
                while (rsResult.next())
                {
                    strAction = rsResult.getString("WORKFLOW_TASK_TRANSITION_strName");
                    if (strCurrentAction != null && strAction.equals(strCurrentAction))
                        strResult.append("<WORKFLOW_TASK_strAction selected=\"1\">" + Utilities.cleanForXSL(strAction) + "</WORKFLOW_TASK_strAction>");
                    else
                        strResult.append("<WORKFLOW_TASK_strAction selected=\"0\">" + Utilities.cleanForXSL(strAction) + "</WORKFLOW_TASK_strAction>");
                }
            }
            else
            {     
                strResult.append("<hasAction>false</hasAction>");
            }
            
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in WorkspaceChannel - " + e.toString(), e);
            throw new Exception("Unknown error in WorkspaceChannel - " + e.toString());
        }
        
        return strResult.toString();
    }
   
    /** Build XML String for users
     * @param group key
     * @param system user name
     */
    private static String buildUserListDropdown(String strGroupKey, String strSystemUser)
    {
        StringBuffer strResult = new StringBuffer();
        boolean isMultiUsers = false;
        try
        {
            // getting all users, user groups related to the current system users
            DALQuery query = new DALQuery();
            query.setDomain("ORGUSER", null, null, null);
            query.setDomain("ORGUSERGROUP", "ORGUSER_intOrgUserKey", "ORGUSERGROUP_intOrgUserKey", "INNER JOIN");
            query.setField("ORGUSER_intOrgUserKey", null);
            query.setField("ORGUSER_strFirstName", null);
            query.setField("ORGUSER_strLastName", null);
            query.setField("ORGUSERGROUP_intOrgGroupKey", null);
            query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "ORGUSER_strSystemUser", "=", strSystemUser, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "ORGUSERGROUP_intOrgGroupKey", "=", strGroupKey, 0, DALQuery.WHERE_HAS_VALUE);
            //System.err.println(query.convertSelectQueryToString());
            ResultSet rsResult = query.executeSelect();
            if (rsResult.next())
            {
                strResult.append("<OrgUser>");
                strResult.append("<WORKFLOW_TASK_intPerformer>" + rsResult.getString("ORGUSER_intOrgUserKey") + "</WORKFLOW_TASK_intPerformer>");
                strResult.append("<WORKFLOW_TASK_strPerformer>" + rsResult.getString("ORGUSER_strFirstName") + " " + rsResult.getString("ORGUSER_strLastName") + "</WORKFLOW_TASK_strPerformer>");
                strResult.append("</OrgUser>");
                
                while (rsResult.next())
                {
                    if (!isMultiUsers)
                        isMultiUsers = true;
                        
                    strResult.append("<OrgUser>");
                    strResult.append("<WORKFLOW_TASK_intPerformer>" + rsResult.getString("ORGUSER_intOrgUserKey") + "</WORKFLOW_TASK_intPerformer>");
                    strResult.append("<WORKFLOW_TASK_strPerformer>" + rsResult.getString("ORGUSER_strFirstName") + " " + rsResult.getString("ORGUSER_strLastName") + "</WORKFLOW_TASK_strPerformer>");
                    strResult.append("</OrgUser>");
                }
                
                strResult.append("<multi_user>" + isMultiUsers + "</multi_user>");
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);
        }
        
        return strResult.toString();
    }
    
    /** Replace parameter tags with the current values
     * @param original string
     * @param workflow instance key
     * @return replaced string
     */
    private static String replaceRuntimeData(String strStringToBeReplaced, String intWorkflowInstanceKey)
    {
        if (strStringToBeReplaced == null)
            return "";
        
        StringBuffer strResult = new StringBuffer(strStringToBeReplaced);
        
        try
        {
            // get all parameters for this workflow instance
            Vector vecWorkflowRuntimeDataFormFields = DatabaseSchema.getFormFields("workflow_runtimedata_details");

            DALQuery query = new DALQuery();
            query.setDomain("WORKFLOW_RUNTIMEDATA", null, null, null);
            query.setFields(vecWorkflowRuntimeDataFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_RUNTIMEDATA_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "WORKFLOW_RUNTIMEDATA_intWorkflowInstanceKey", "=", intWorkflowInstanceKey, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsParameters = query.executeSelect();
            
            
            while (rsParameters.next())
            {
                String strTagName = START_TAG + rsParameters.getString("WORKFLOW_RUNTIMEDATA_strParameterName") + END_TAG;
                String strTagValue = rsParameters.getString("WORKFLOW_RUNTIMEDATA_strParameterValue");
                Utilities.substrReplace(strResult, strTagName, strTagValue);
            }
            
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);
        }
        //System.err.println("Instruction: " + strResult.toString());
        return strResult.toString();
    }
    
    /** Build XML string for search result
     */
    public static String buildSearchXMLFileForTask(String strSearchTag, ResultSet rsResultSet, Vector vtFields) throws Exception
    {
        StringBuffer strResult = new StringBuffer();
        int intNoOfFields = vtFields.size();
        
        try
        {
            while (rsResultSet.next())
            {
                String intWorkflowInstanceKey = rsResultSet.getString("WORKFLOW_TASK_intWorkflowInstanceKey");
                strResult.append("<" + strSearchTag + ">");
                for (int i=0; i < intNoOfFields; i++)
                {
                    String strFieldName = (String) vtFields.get(i);
                    strResult.append("<" + strFieldName + ">");
                    
                    if (rsResultSet.getString(strFieldName) != null)
                    {
                        DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                        if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                            strResult.append(Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName)));
                        else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                            strResult.append(Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName)));
                        else if (strFieldName.equals("WORKFLOW_TASK_strName"))
                            strResult.append(Utilities.cleanForXSL(replaceRuntimeData(rsResultSet.getString(strFieldName), intWorkflowInstanceKey)));
                        else
                            strResult.append(Utilities.cleanForXSL(rsResultSet.getString(strFieldName)));
                            
                    }
                    strResult.append("</" + strFieldName + ">");
                }
                
                strResult.append("</" + strSearchTag + ">");
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
            throw new Exception("Unknown error in QueryChannel - " + e.toString());
        }
        
        return strResult.toString();
    }
    
    /** Build XML string for search result
     */
    public static String buildSearchXMLFileForTaskHistory(String strSearchTag, ResultSet rsResultSet, Vector vtFields) throws Exception
    {
        StringBuffer strResult = new StringBuffer();
        int intNoOfFields = vtFields.size();
        
        try
        {
            while (rsResultSet.next())
            {
                String intWorkflowInstanceKey = rsResultSet.getString("WORKFLOW_TASK_HISTORY_intWorkflowInstanceKey");
                strResult.append("<" + strSearchTag + ">");
                for (int i=0; i < intNoOfFields; i++)
                {
                    String strFieldName = (String) vtFields.get(i);
                    
                    if (strFieldName.equals("WORKFLOW_TASK_HISTORY_dtDateCompleted")){
                            strResult.append("<WORKFLOW_TASK_dtDateCompleted>");
                            strResult.append(Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName)));
                            strResult.append("</WORKFLOW_TASK_dtDateCompleted>");
                    }
                    
                    if (strFieldName.equals("WORKFLOW_TASK_HISTORY_strTimeCompleted")){
                            strResult.append("<WORKFLOW_TASK_strTimeCompleted>");
                            strResult.append(Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName)));
                            strResult.append("</WORKFLOW_TASK_strTimeCompleted>");
                    }                    
                    
                    
                    strResult.append("<" + strFieldName + ">");
                    
                    if (rsResultSet.getString(strFieldName) != null)
                    {
                        DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                        if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                            strResult.append(Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName)));
                        else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                            strResult.append(Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName)));
                        else if (strFieldName.equals("WORKFLOW_TASK_HISTORY_strName"))
                            strResult.append(Utilities.cleanForXSL(replaceRuntimeData(rsResultSet.getString(strFieldName), intWorkflowInstanceKey)));
                        else
                            strResult.append(Utilities.cleanForXSL(rsResultSet.getString(strFieldName)));
                            
                    }
                    strResult.append("</" + strFieldName + ">");
                }
                
                strResult.append("</" + strSearchTag + ">");
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
            throw new Exception("Unknown error in QueryChannel - " + e.toString());
        }
        
        return strResult.toString();
    }
    
    /** Build XML string for displaying hyper link purpose
     * @param original string
     * @return XML string
     */
    private static String buildLinkXMLFile(boolean isFromWorkspace, String strCurrent, String strSource, String strTaskKey, String strOrigin)
    {
        StringBuffer strResult = new StringBuffer();
        
        int intStartLinkIndex = strOrigin.indexOf(START_LINK_TAG);
        int intEndLinkIndex = -END_LINK_TAG.length();
        int intEndLinkIndex2 = -END_LINK_TAG_2.length();
        //boolean isValidLink = true;
        while (intStartLinkIndex >= 0)
        {
            strResult.append("<data>");
            strResult.append("<type>text</type>");
            strResult.append("<domain></domain>");
            strResult.append("<param></param>");
            strResult.append("<content>" + Utilities.cleanForXSL(strOrigin.substring(intEndLinkIndex2 + END_LINK_TAG_2.length(), intStartLinkIndex)) + "</content>");
            strResult.append("</data>");
            
            // get new end link tag index
            intEndLinkIndex = strOrigin.indexOf(END_LINK_TAG, intStartLinkIndex);
            if (intEndLinkIndex < 0)
            {
                System.err.println("Invalid end link tag.");
                strResult.append("<data>");
                strResult.append("<type>text</type>");
                strResult.append("<domain></domain>");
                strResult.append("<param></param>");
                strResult.append("<content>" + strOrigin + "</content>");
                strResult.append("</data>");
                break;
            }
            
            intEndLinkIndex2 = strOrigin.indexOf(END_LINK_TAG_2, intEndLinkIndex);
            if (intEndLinkIndex2 < 0)
            {
                System.err.println("Invalid end link tag.");
                strResult.append("<data>");
                strResult.append("<type>text</type>");
                strResult.append("<domain></domain>");
                strResult.append("<param></param>");
                strResult.append("<content>" + strOrigin + "</content>");
                strResult.append("</data>");
                break;
            }
            
            String strTheLink = strOrigin.substring(intStartLinkIndex + START_LINK_TAG.length(), intEndLinkIndex);
            String strContent = strOrigin.substring(intEndLinkIndex + END_LINK_TAG.length(), intEndLinkIndex2);
            
            strResult.append("<data>");
            strResult.append("<type>link</type>");
            if (strTheLink.indexOf(CASE_URL) >= 0)
            {
                strResult.append("<domain>CASE</domain>");
                strResult.append("<param>" + Utilities.cleanForXSL(strTheLink.substring(CASE_URL.length())) + "</param>");
                strResult.append("<content>" + Utilities.cleanForXSL(strContent) + "</content>");
                strResult.append("</data>");
            }
            else if (strTheLink.indexOf(SMARTFORM_URL) >= 0)
            {
                strResult.append("<domain>SMARTFORM</domain>");
                strResult.append("<param>" + Utilities.cleanForXSL(strTheLink.substring(SMARTFORM_URL.length()) +
                "&isFromWorkspace=true&origin=" + strCurrent + "&source=" + strSource + "&WORKFLOW_TASK_intTaskKey=" + strTaskKey) + "</param>");
                strResult.append("<content>" + Utilities.cleanForXSL(strContent) + "</content>");
                strResult.append("</data>");
            }
            
            // get new start link tag
            intStartLinkIndex = strOrigin.indexOf(START_LINK_TAG, intEndLinkIndex + END_LINK_TAG_2.length());
        }
        
        if (intEndLinkIndex == -END_LINK_TAG.length())
        {
            strResult.append("<data>");
            strResult.append("<type>text</type>");
            strResult.append("<domain></domain>");
            strResult.append("<param></param>");
            strResult.append("<content>" + strOrigin + "</content>");
            strResult.append("</data>");
        }
        else
        {
            strResult.append("<data>");
            strResult.append("<type>text</type>");
            strResult.append("<domain></domain>");
            strResult.append("<param></param>");
            strResult.append("<content>" + strOrigin.substring(intEndLinkIndex2 + END_LINK_TAG_2.length()) + "</content>");
            strResult.append("</data>");
        }
        //System.err.println(strResult.toString());
        return strResult.toString();
    }
}
    

