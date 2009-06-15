/**
 * SmartformUtilities.java
 * Copyright ? 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 07/04/2004
 */

package neuragenix.genix.smartform;

/**
 * 
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Vector;
import java.util.Hashtable;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

// neuragenix packages
import neuragenix.dao.*;
import neuragenix.utils.Password;
import neuragenix.common.*;

import neuragenix.common.Utilities;

// beanshell scripting package
import bsh.Interpreter;
import bsh.EvalError;
import bsh.ParseException;

public class SmartformUtilities
{      
    // Whether to display the original Biospecimen ID that the smartform was associated with
    // when building the BioAnalysis Table
    public static boolean BIOANALYSIS_BIO_ID = false;
    
    
    /** Creates a new instance of SmartformUtilities */
    public SmartformUtilities()
    { 
    } 
    
    
    static
    {        
        try
        {
            BIOANALYSIS_BIO_ID = PropertiesManager.getPropertyAsBoolean("neuragenix.genix.smartform.BioAnalysisBioID");
        }
        catch(Exception e)
        {
            System.out.println ("[SmartformUtilities] Property nuragenix.genix.smartform.BioAnalysisBioID not present, default to false.");
        }        
    }    

    /**
     * Build the options XML string
     * @param strDataElementID: primary key of the data element
     * @param strValue: current option value
     * @return XML string for building the dropdown list for options
     */
    public static String buildDataElementOptions(String strDataElementID, String strValue)
    {
        StringBuffer strResult = new StringBuffer();
        
        try
        {
            // building the query to get all options for this data element
            DALQuery query = new DALQuery();
            query.setDomain("DATAELEMENTS", null, null, null);
            query.setDomain("DATAELEMENTSTOOPTIONS", "DATAELEMENTS_intDataElementID", "DATAELEMENTSTOOPTIONS_intDataElementID", "INNER JOIN");
            query.setDomain("OPTIONPOOL", "DATAELEMENTSTOOPTIONS_intOptionPoolID", "OPTIONPOOL_intOptionPoolID", "INNER JOIN");
            query.setDomain("OPTIONS", "OPTIONPOOL_intOptionPoolID", "OPTIONS_intOptionPoolID", "INNER JOIN");
            query.setField("OPTIONS_strOptionLabel", null);
            query.setField("OPTIONS_strOptionValue", null);
            query.setField("OPTIONS_intOptionOrder", null);
            query.setWhere(null, 0, "OPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("OPTIONS_intOptionOrder", "ASC");
            ResultSet rsResult = query.executeSelect();
            
            // building the XML string to present the options
            while (rsResult.next())
            {
                strResult.append("<option selected=\"");
                if (strValue != null && strValue.trim().equals(rsResult.getString("OPTIONS_strOptionValue").trim()))
                    strResult.append("1\">");
                else
                    strResult.append("0\">");
                strResult.append("<optionlabel>" + Utilities.cleanForXSL(rsResult.getString("OPTIONS_strOptionLabel")) + "</optionlabel>");
                strResult.append("<optionvalue>" + Utilities.cleanForXSL(rsResult.getString("OPTIONS_strOptionValue")) + "</optionvalue>");
                strResult.append("</option>");
            }
            rsResult.close();
            
            
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return strResult.toString();
    }
    
    /**
     * Build the date XML string
     * @param strDate: current date value
     * @return XML string for building the dropdown list for date
     */
    public static String buildDataElementDate(String strDate)
    {
        StringBuffer strResult = new StringBuffer();
        int day, month;
        String year;
        
        if (strDate == null || strDate.trim().length() == 0)
        {
            day = 0; month = 0; year = "";
        }
        else
        {
            day = Integer.parseInt(strDate.substring(0,2));
            month = Integer.parseInt(strDate.substring(3,5));
            year = strDate.substring(6);
        }
        
        if (day == 0)
        {
            strResult.append("<Day selected=\"1\"></Day>");
        }
        else
        {
            strResult.append("<Day selected=\"0\"></Day>");
        }
        
        for (int i=1; i < 10; i++)
        {
            if (i == day)
                strResult.append("<Day selected=\"1\">0" + i + "</Day>");
            else
                strResult.append("<Day selected=\"0\">0" + i + "</Day>");
        }

        for (int i=10; i < 32; i++)
        {
            if (i == day)
                strResult.append("<Day selected=\"1\">" + i + "</Day>");
            else
                strResult.append("<Day selected=\"0\">" + i + "</Day>");
        }
        
        if (month == 0)
        {
            strResult.append("<Month selected=\"1\"></Month>");
        }
        else
        {
            strResult.append("<Month selected=\"0\"></Month>");
        }
        
        for (int i=1; i < 10; i++)
        {
            if (i == month)
                strResult.append("<Month selected=\"1\">0" + i + "</Month>");
            else
                strResult.append("<Month selected=\"0\">0" + i + "</Month>");
        }

        for (int i=10; i < 13; i++)
        {
            if (i == month)
                strResult.append("<Month selected=\"1\">" + i + "</Month>");
            else
                strResult.append("<Month selected=\"0\">" + i + "</Month>");
        }
        
        strResult.append("<Year>" + year + "</Year>");
        
        return strResult.toString();
    }
    
    /**
     * Build the options XML string
     * @param strDataElementID: primary key of the data element
     * @param strValue: current option value
     * @return XML string for building the dropdown list for options
     */
    public static String buildSystemLookupObject(String strDataElementID, String strValue, ChannelRuntimeData runtimeData)
    {
        StringBuffer strResult = new StringBuffer();
        try
        {            
            Vector vtSmartformsDataelements = DatabaseSchema.getFormFields("csmartform_smartforms_dataelements");
            Vector vtLookupField = DatabaseSchema.getFormFields("csmartform_addEditDataElements_LookupField");
            
            DALQuery query = new DALQuery(); 
            query.setDomain("DATAELEMENTS", null, null, null);
            query.setField("DATAELEMENTS_strDataElementLookupType", null);
            query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            //System.out.println("strValue in buildSystemLookupObject:"+strValue);
            
            if (rsResult.next())
            {
                // get the lookup object name
                String strDataElementLookupType = rsResult.getString("DATAELEMENTS_strDataElementLookupType");
                // get lookup object primary key field name
                String strLookupObjectPrimaryField = DatabaseSchema.getLookupObjectPrimaryField(strDataElementLookupType);
                // get lookup object related domains
                Vector vtDomains = DatabaseSchema.getLookupObjectDomains(strDataElementLookupType);
                // get lookup object related join objects
                Vector vtJoins = DatabaseSchema.getLookupObjectJoins(strDataElementLookupType);
                // get lookup object displayed fields
                Vector vtDisplayFields = DatabaseSchema.getLookupObjectDisplayFields(strDataElementLookupType);
                
                // getting the fields needed to display under the dropdown
                query = new DALQuery();
                query.setDomain("SYSTEMLOOKUPFIELD", null, null, null);
                query.setFields(vtLookupField, null);
                //query.setField("SYSTEMLOOKUPFIELD_strInternalName", null);
                //query.setField("SYSTEMLOOKUPFIELD_strFieldOrder", null);
                query.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                // Additional Info Start
                query.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_strInDropdown", "=","0", 0, DALQuery.WHERE_HAS_VALUE);
                // Additional Info End
                query.setOrderBy("SYSTEMLOOKUPFIELD_strFieldOrder", "ASC");
                ResultSet rsFields = query.executeSelect();
                
                Vector vtFields = new Vector(5, 2);
                while (rsFields.next())
                    vtFields.add(rsFields.getString("SYSTEMLOOKUPFIELD_strInternalName"));
                
                rsFields.close();
                
                // build a hashtble to store all the fields
                Hashtable hashFields = new Hashtable(10);
                hashFields.put(strLookupObjectPrimaryField, "needed");
                for (int i=0; i < vtFields.size(); i++)
                    hashFields.put(vtFields.get(i), "needed");
                for (int i=0; i < vtDisplayFields.size(); i++)
                    hashFields.put(vtDisplayFields.get(i), "needed");
                
                // building the query
                DALQuery lookupQuery = new DALQuery();
                lookupQuery.setDomain((String) vtDomains.get(0), null, null, null);
                lookupQuery.setWhere(null, 0, vtDomains.get(0) + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                
                for (int i=1; i < vtDomains.size(); i++)
                {
                    DBJoin joinObj = (DBJoin) vtJoins.get(i-1);
                    lookupQuery.setDomain((String) vtDomains.get(i), joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                    lookupQuery.setWhere("AND", 0, vtDomains.get(i) + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                }
                
                // set fields
                Enumeration key_enum = hashFields.keys();
                while (key_enum.hasMoreElements())
                    lookupQuery.setField((String) key_enum.nextElement(), null);
                    
                // set where conditions
                query = new DALQuery();
                query.setDomain("SYSTEMLOOKUPWHERE", null, null, null);
                query.setField("SYSTEMLOOKUPWHERE_strConnector", null);
                query.setField("SYSTEMLOOKUPWHERE_strInternalName", null);
                query.setField("SYSTEMLOOKUPWHERE_strOperator", null);
                query.setField("SYSTEMLOOKUPWHERE_strValue", null);
                query.setField("SYSTEMLOOKUPWHERE_strOrder", null);
                query.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SYSTEMLOOKUPWHERE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("SYSTEMLOOKUPWHERE_strOrder", "ASC");
                ResultSet rsWheres = query.executeSelect();
                
                if (rsWheres.next())
                {
                    String strWhereValue = rsWheres.getString("SYSTEMLOOKUPWHERE_strValue");
                    // if value is a parameter name
                    if (strWhereValue.indexOf("<") >= 0 && strWhereValue.indexOf(">") > 0)
                    {
                        String strParameterName = strWhereValue.substring(strWhereValue.indexOf("<") + 1, strWhereValue.indexOf(">"));
                        lookupQuery.setWhere("AND", 0, rsWheres.getString("SYSTEMLOOKUPWHERE_strInternalName"), 
                                             rsWheres.getString("SYSTEMLOOKUPWHERE_strOperator"), runtimeData.getParameter(strParameterName),
                                             0, DALQuery.WHERE_HAS_VALUE);
                    }
                    else
                        lookupQuery.setWhere("AND", 0, rsWheres.getString("SYSTEMLOOKUPWHERE_strInternalName"), 
                                             rsWheres.getString("SYSTEMLOOKUPWHERE_strOperator"), rsWheres.getString("SYSTEMLOOKUPWHERE_strValue"),
                                             0, DALQuery.WHERE_HAS_VALUE);
                    
                    while (rsWheres.next())
                    {
                        strWhereValue = rsWheres.getString("SYSTEMLOOKUPWHERE_strValue");
                        // if value is a parameter name
                        if (strWhereValue.indexOf("<") >= 0 && strWhereValue.indexOf(">") > 0)
                        {
                            String strParameterName = strWhereValue.substring(strWhereValue.indexOf("<") + 1, strWhereValue.indexOf(">"));
                            lookupQuery.setWhere(rsWheres.getString("SYSTEMLOOKUPWHERE_strConnector"), 0, rsWheres.getString("SYSTEMLOOKUPWHERE_strInternalName"), 
                                                 rsWheres.getString("SYSTEMLOOKUPWHERE_strOperator"), runtimeData.getParameter(strParameterName),
                                                 0, DALQuery.WHERE_HAS_VALUE);
                        }
                        else
                            lookupQuery.setWhere(rsWheres.getString("SYSTEMLOOKUPWHERE_strConnector"), 0, rsWheres.getString("SYSTEMLOOKUPWHERE_strInternalName"), 
                                             rsWheres.getString("SYSTEMLOOKUPWHERE_strOperator"), rsWheres.getString("SYSTEMLOOKUPWHERE_strValue"),
                                             0, DALQuery.WHERE_HAS_VALUE);
                    }
                }
                
                rsWheres.close();
                
                //System.err.println(lookupQuery.convertSelectQueryToString());
                ResultSet rsLookupResult = lookupQuery.executeSelect();
                
                // Additional Info Start
                strResult.append(buildLookupAdditionalInfo(strDataElementID, strValue, strDataElementLookupType, strLookupObjectPrimaryField, vtDomains, vtJoins, rsLookupResult, runtimeData));             
                String strDropDownVal = runtimeData.getParameter("strLookupDropdownName");
                //System.out.println("strDropDownVal in systemlookupobj :"+strDropDownVal);
                // Additional Info End
                
                rsLookupResult.beforeFirst();
                
                while (rsLookupResult.next())
                {
                    String strOptionValue = rsLookupResult.getString(strLookupObjectPrimaryField);
                    //System.out.println("strLookupObjectPrimaryField :"+strLookupObjectPrimaryField);
                    
                    String strOptionLabel = "";
                    for (int i=0; i < vtFields.size(); i++)
                        strOptionLabel += rsLookupResult.getString((String) vtFields.get(i)) == null ? "" : rsLookupResult.getString((String) vtFields.get(i)) + " ";
                    
                    String strOptionTooltip = "";
                    for (int i=0; i < vtDisplayFields.size(); i++)
                    {
                        strOptionTooltip += rsLookupResult.getString((String) vtDisplayFields.get(i)) + "&#10;&#13;";
                    }                   
                     
                   
                    // If strValue is passed 
                     if (strDropDownVal != null && strDropDownVal.equals(strOptionValue))
                        strResult.append("<option selected=\"1\">");
                    //else if (strOptionValue.equals(strValue))
                      //  strResult.append("<option selected=\"1\">");                                 
                    else if (strValue == null || strValue.trim().length() == 0) 
                        strResult.append("<option selected=\"0\">");
                    else  
                        strResult.append("<option selected=\"0\">");
                    
                   
                    
                    strResult.append("<optionvalue>" + rsLookupResult.getString(strLookupObjectPrimaryField) + "</optionvalue>");
                    strResult.append("<optionlabel>" + strOptionLabel + "</optionlabel>");
                    strResult.append("<optiontooltip>" + strOptionTooltip + "</optiontooltip>");
                    strResult.append("</option>");
                   //System.out.println("Option value: "+rsLookupResult.getString(strLookupObjectPrimaryField));
                    
                    
                }
                rsLookupResult.close();
            }
            rsResult.close();
        }
        catch(Exception e) 
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return strResult.toString();
    }
    
    /** Build XML string for search result
     */
    public static String buildSearchXMLFile(String strSearchTag, ResultSet rsResultSet, Hashtable htRepeatedDEResults, Vector vtFields, String strSmartformParticipantID, int intStartDataElementOrder, int intEndDataElementOrder, ChannelRuntimeData runtimeData, ISmartformEntity SFEntityObject)
    {
        StringBuffer strResult = new StringBuffer();
        int intNoOfFields = vtFields.size();
        String strResultKey = "";
        Hashtable resultSetDetails = null;
        try
        {
            Hashtable hashSmartformResult;
            Hashtable hashSmartformResultID = getSmartformResultsID(strSmartformParticipantID, intStartDataElementOrder, intEndDataElementOrder);            
            // get the smart form result for this set of data elements
            if (SFEntityObject != null)
            {    
                hashSmartformResult = getSmartFormDataFromSFEntityObject(SFEntityObject);
            }    
            else
            {
                hashSmartformResult = getSmartformResults(strSmartformParticipantID, intStartDataElementOrder, intEndDataElementOrder);            
            }    
            
            while (rsResultSet.next())
            {
                resultSetDetails = new Hashtable();
                String tempData = "";
                for (int i = 1; i < vtFields.size(); i++)
                {
                    tempData = rsResultSet.getString(vtFields.get(i).toString());
                    if (tempData == null) tempData = "";
                    
                    resultSetDetails.put(vtFields.get(i).toString(), tempData);
                }
                                
                
                String strDataElementOrder = (String) resultSetDetails.get("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                String strDataElementKey = (String) resultSetDetails.get("DATAELEMENTS_intDataElementID");
                String strDataElementType = (String) resultSetDetails.get("DATAELEMENTS_intDataElementType");
                
                String strValue = "";
                
                 //System.out.println("ValueFromRuntimeData :"+runtimeData.getParameter("ValueFromRuntimeData"));                
                 //System.out.println("Return :" + runtimeData );
                 if(runtimeData.getParameter("ValueFromRuntimeData") != null){
                    if(runtimeData.getParameter("ValueFromRuntimeData").equals("false")){                  
                        strValue = (String) hashSmartformResult.get("d" + strDataElementOrder);                
                        
                    }else{ // if a required field is blank, Value is got from runtimeData
                        strValue = null;                       
                    }
                    
                    // if hashtable returns blank or if a required field is blank, then obtain value from runtimeData
                    if(strValue == null){
                     if(strDataElementType.equals("DATE")){
                            String strDEOrder1 = "Day" + strDataElementOrder; 
                            String strDEOrder2 = "Month" + strDataElementOrder; 
                            String strDEOrder3 = "Year" + strDataElementOrder; 
                            
                            if(runtimeData.getParameter(strDEOrder1) == null || runtimeData.getParameter(strDEOrder2) == null || runtimeData.getParameter(strDEOrder3) == null                    
                               || runtimeData.getParameter(strDEOrder1).equals("") || runtimeData.getParameter(strDEOrder2).equals("") || runtimeData.getParameter(strDEOrder3).equals("")){                         
                                strValue = "";                        
                            }else{                                
                                strValue = runtimeData.getParameter(strDEOrder3) +"-"+ runtimeData.getParameter(strDEOrder2) +"-"+ runtimeData.getParameter(strDEOrder1);
                                
                            }

                     }else{                             
                        strValue = runtimeData.getParameter("d" + strDataElementOrder);
                     }
                     //System.out.println("strValue :"+strValue);
                         
                   }  
                 }
                //System.out.println("strValue :"+strValue);
                
                strResult.append("<" + strSearchTag + ">");
                for (int i=0; i < intNoOfFields; i++)
                {
                    String strFieldName = (String) vtFields.get(i);
                    strResult.append("<" + strFieldName + ">");
                    
                    if ((String) resultSetDetails.get(strFieldName) != null)
                    {
                        strResult.append(Utilities.cleanForXSL((String) resultSetDetails.get(strFieldName)));
                    }
                    strResult.append("</" + strFieldName + ">");
                }
                
                // DROPDOWN data element
                if (strDataElementType.equals("DROPDOWN"))
                {
                    if (strValue == null || strValue.trim().length() == 0)
                    {
                        // get the default value
                        String strDefaultValue = (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefault");
                        if (strDefaultValue == null || strDefaultValue.trim().length() == 0)
                        {
                            String strDefaultSmartformID = (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultSmartformID");
                            String strDefaultDataElementID = (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultDataElementID");
                            
                            if (strDefaultSmartformID == null || strDefaultSmartformID.trim().length() == 0 ||
                                strDefaultDataElementID == null || strDefaultDataElementID.trim().length() == 0)
                            {
                                strResult.append(buildDataElementOptions((String) resultSetDetails.get("DATAELEMENTS_intDataElementID"), null));
                            }
                            else
                            {
                                strDefaultValue = getSmartFormDataElementValue(strDefaultSmartformID, strDefaultDataElementID);
                                if (strDefaultValue == null || strDefaultValue.trim().length() == 0)
                                    strResult.append(buildDataElementOptions((String) resultSetDetails.get("DATAELEMENTS_intDataElementID"), null));
                                else
                                    strResult.append(buildDataElementOptions((String) resultSetDetails.get("DATAELEMENTS_intDataElementID"), strDefaultValue));
                            }
                        }
                        else
                        {
                            strResult.append(buildDataElementOptions((String) resultSetDetails.get("DATAELEMENTS_intDataElementID"), strDefaultValue));
                        }
                    }
                    else
                    {
                        strResult.append(buildDataElementOptions((String) resultSetDetails.get("DATAELEMENTS_intDataElementID"), strValue));
                    }
                }
                // SYSTEM data element
                else if (strDataElementType.equals("SYSTEM LOOKUP"))
                {
                    strResult.append(buildSystemLookupObject((String) resultSetDetails.get("DATAELEMENTS_intDataElementID"), strValue, runtimeData));
                }
                else if (strDataElementType.equals("DATE"))
                {
                  if(runtimeData.getParameter("ValueFromRuntimeData") == null || runtimeData.getParameter("ValueFromRuntimeData").equals("false"))
                  {                         
                    // if there is no smartform result for this data element
                    if (strValue == null || strValue.trim().length() == 0)
                    {
                        String strDefaultDate = (String) resultSetDetails.get("DATAELEMENTS_dtDataElementDefaultDate");
                        // if there is no default date specified for this data element
                        if (strDefaultDate == null || strDefaultDate.trim().length() == 0)
                        {
                            String strDefaultDateToday = (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefaultTodaysDate");
                            // if the default date is not today
                            if (strDefaultDateToday == null || strDefaultDateToday.trim().length() == 0)
                            {
                                String strDefaultDateOperator = (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator");

                                // there is no default date operator
                                if (strDefaultDateOperator == null || !(strDefaultDateOperator.equals("+") || strDefaultDateOperator.equals("-")))
                                {
                                    String strDefaultSmartformID = (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultSmartformID");
                                    String strDefaultDataElementID = (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultDataElementID");

                                    if (strDefaultSmartformID == null || strDefaultSmartformID.trim().length() == 0 ||
                                        strDefaultDataElementID == null || strDefaultDataElementID.trim().length() == 0)
                                    {
                                        strResult.append(buildDataElementDate(null));
                                    }
                                    else
                                    {
                                        strDefaultDate = getSmartFormDataElementValue(strDefaultSmartformID, strDefaultDataElementID);
                                        if (strDefaultDate == null || strDefaultDate.trim().length() == 0)
                                        {
                                            strResult.append(buildDataElementDate(null));
                                        }
                                        else
                                        {
                                            strResult.append(buildDataElementDate(Utilities.convertDateForDisplay(strDefaultDate)));
                                        }
                                    }
                                }
                                else
                                {
                                    int intDefaultWorkingDaysNumber = ((Integer) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber")).intValue();
                                    String strDefaultDateWorkingDaysOrDays = (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefaultWdOrDaysOption");

                                    if (strDefaultDateOperator.equals("-"))
                                        intDefaultWorkingDaysNumber = -intDefaultWorkingDaysNumber;

                                    GregorianCalendar today = new GregorianCalendar();
                                    today.setTime(new java.util.Date());

                                    if (strDefaultDateWorkingDaysOrDays.equals("Days"))
                                    {
                                        today.add(Calendar.DATE, intDefaultWorkingDaysNumber);
                                        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                                        strResult.append(buildDataElementDate(fm.format(today.getTime())));
                                    }
                                    else if (strDefaultDateWorkingDaysOrDays.equals("Working Days"))
                                    {
                                        if (intDefaultWorkingDaysNumber > 0)
                                        {
                                            int intToday = today.get(Calendar.DAY_OF_WEEK);
                                            int intDayTakeAway = 0;

                                            switch (intToday)
                                            {
                                                case Calendar.SUNDAY: intDayTakeAway = 0; today.add(Calendar.DATE, -2); break;
                                                case Calendar.MONDAY: intDayTakeAway = 4; break;
                                                case Calendar.TUESDAY: intDayTakeAway = 3; break;
                                                case Calendar.WEDNESDAY: intDayTakeAway = 2; break;
                                                case Calendar.THURSDAY: intDayTakeAway = 1; break;
                                                case Calendar.FRIDAY: intDayTakeAway = 0; break;
                                                case Calendar.SATURDAY: intDayTakeAway = 0; today.add(Calendar.DATE, -1); break;
                                            }

                                            if (intDayTakeAway >= intDefaultWorkingDaysNumber)
                                            {
                                                today.add(Calendar.DATE, intDefaultWorkingDaysNumber);
                                                SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                                                strResult.append(buildDataElementDate(fm.format(today.getTime())));
                                            }
                                            else
                                            {
                                                int intNumberOfWeeks = (intDefaultWorkingDaysNumber - intDayTakeAway)/5;
                                                if ((intDefaultWorkingDaysNumber - intDayTakeAway)%5 != 0)
                                                    intNumberOfWeeks++;

                                                today.add(Calendar.DATE, intDefaultWorkingDaysNumber + 2*intNumberOfWeeks);
                                                SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                                                strResult.append(buildDataElementDate(fm.format(today.getTime())));
                                            }
                                        }
                                        else
                                        {
                                            int intToday = today.get(Calendar.DAY_OF_WEEK);
                                            int intDayTakeAway = 0;

                                            switch (intToday)
                                            {
                                                case Calendar.SUNDAY: intDayTakeAway = 0; today.add(Calendar.DATE, 1); break;
                                                case Calendar.MONDAY: intDayTakeAway = 0; break;
                                                case Calendar.TUESDAY: intDayTakeAway = 1; break;
                                                case Calendar.WEDNESDAY: intDayTakeAway = 2; break;
                                                case Calendar.THURSDAY: intDayTakeAway = 3; break;
                                                case Calendar.FRIDAY: intDayTakeAway = 4; break;
                                                case Calendar.SATURDAY: intDayTakeAway = 0; today.add(Calendar.DATE, 2); break;
                                            }

                                            if (intDayTakeAway >= -intDefaultWorkingDaysNumber)
                                            {
                                                today.add(Calendar.DATE, intDefaultWorkingDaysNumber);
                                                SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                                                strResult.append(buildDataElementDate(fm.format(today.getTime())));
                                            }
                                            else
                                            {
                                                int intNumberOfWeeks = (-intDefaultWorkingDaysNumber - intDayTakeAway)/5;
                                                if ((-intDefaultWorkingDaysNumber - intDayTakeAway)%5 != 0)
                                                    intNumberOfWeeks++;

                                                today.add(Calendar.DATE, intDefaultWorkingDaysNumber - 2*intNumberOfWeeks);
                                                SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                                                strResult.append(buildDataElementDate(fm.format(today.getTime())));
                                            }
                                            //strResult.append(buildDataElementDate(null));
                                        }
                                    }
                                    else
                                        strResult.append(buildDataElementDate(null));
                                }
                            }
                            else
                            {
                                SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                                strResult.append(buildDataElementDate(fm.format(new java.util.Date())));
                            }
                        }
                        // default date is specified
                        else
                            strResult.append(buildDataElementDate(Utilities.convertDateForDisplay(strDefaultDate)));
                    }
                    // there is a result for the data element
                    else
                        strResult.append(buildDataElementDate(Utilities.convertDateForDisplay(strValue)));                    
                    
                    }else{ // Value is obtained from runtimeData, even if it is blank since valuefromRuntimeData is true
                        
                        if(strValue == null || strValue.trim().length() == 0){                            
                            strResult.append(buildDataElementDate(null));
                        }else{                            
                            strResult.append(buildDataElementDate(Utilities.convertDateForDisplay(strValue)));  
                        }
                        
                    }

                }
                else if (strDataElementType.equals("TEXT") || strDataElementType.equals("NUMERIC") 
                            || strDataElementType.equals("MONETARY") || strDataElementType.equals("FORM LINK") || strDataElementType.equals("CHECK BOX"))
                {                    
                    if(runtimeData.getParameter("ValueFromRuntimeData") == null || runtimeData.getParameter("ValueFromRuntimeData").equals("false")){
                        // if there is no result for this dataelement
                        if (strValue == null || strValue.trim().length() == 0)
                        {
                            // get the default value
                            String strDefaultValue = (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefault");
                            if (strDefaultValue == null || strDefaultValue.trim().length() == 0)
                            {
                                String strDefaultSmartformID = (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultSmartformID");
                                String strDefaultDataElementID = (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultDataElementID");

                                if (strDefaultSmartformID == null || strDefaultSmartformID.trim().length() == 0 ||
                                    strDefaultDataElementID == null || strDefaultDataElementID.trim().length() == 0)
                                {
                                    strResult.append("<value></value>");
                                }
                                else
                                {
                                    strDefaultValue = getSmartFormDataElementValue(strDefaultSmartformID, strDefaultDataElementID);
                                    if (strDefaultValue == null || strDefaultValue.trim().length() == 0)
                                        strResult.append("<value></value>");
                                    else
                                        strResult.append("<value>" + Utilities.cleanForXSL(strDefaultValue) + "</value>");
                                }
                            }
                            else
                            {
                                strResult.append("<value>" + Utilities.cleanForXSL(strDefaultValue) + "</value>");
                            }
                        }
                        else
                        {
                            strResult.append("<value>" + Utilities.cleanForXSL(strValue) + "</value>");
                        }
                        
                    }else{ // Value is obtained from runtimeData, even if it is blank since valuefromRuntimeData is true
                        
                        strResult.append("<value>" + Utilities.cleanForXSL(strValue) + "</value>");
                    }
                }
                else if (strDataElementType.equals("REPEATABLE TEXT") ) 
                {
                    
                    SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                    strResult.append(buildDataElementDate(fm.format(new java.util.Date())));

                    // get the default value
                    String strDefaultValue = (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefault");
                    if (strDefaultValue == null || strDefaultValue.trim().length() == 0)
                    {
                        strResult.append("<value></value>");
                    }
                    else
                    {
                        strResult.append("<value>" + Utilities.cleanForXSL(strDefaultValue) + "</value>");
                    }
                    
                    Vector vtRepeatedDEResults = (Vector) htRepeatedDEResults.get(strDataElementOrder);
                    if (vtRepeatedDEResults != null && vtRepeatedDEResults.size()>0)
                    {    
                        
                        for (int i=0; i<vtRepeatedDEResults.size(); i++)
                        {
                            String strData = vtRepeatedDEResults.get(i).toString();
                            String strDate = "";
                            if (strData.length()>10 && strData.substring(0,10).matches("[0-9]{2}\\W{1}[0-9]{2}\\W{1}[0-9]{4}"))
                            {
                                strDate = vtRepeatedDEResults.get(i).toString().substring(0,10);
                                strData = strData.substring(10).trim();
                            }    
                                
                            strResult.append("<repeatedDEData>");
                            strResult.append("<index>" + Integer.toString(i) + "</index>");
                            strResult.append("<date>" + strDate + "</date>");
                            strResult.append("<data>" + strData + "</data>");
                            strResult.append("</repeatedDEData>");
                        }
                        
                    }                    
                    
                }                
                else if (strDataElementType.equals("SCRIPT"))
                {
                    String strScript = (String) resultSetDetails.get("DATAELEMENTS_strDataElementScript");
                    //System.err.println(strScript);
                    String strScriptResult = getScriptResult(strScript, strSmartformParticipantID);
                    //System.err.println(strScriptResult+"in build search");
                    
                    if (strScriptResult == null || strScriptResult.trim().length() == 0)
                        strResult.append("<value></value>");
                    else
                        strResult.append("<value>" + Utilities.cleanForXSL(strScriptResult) + "</value>");
                    
                        //inserted Script result into database
                        // Add the script result by updating this Dataelement id
                        String strDEId = (String) resultSetDetails.get("DATAELEMENTS_intDataElementID");
                        
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strScriptResult);
                        currentQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDEId, 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.executeUpdate();
                        
                }
                else if (strDataElementType.equals("ATTACHMENT"))
                {     
                    strResultKey = (String) hashSmartformResultID.get(strDataElementKey);  
                                    
                    if (strValue == null || strValue.trim().length() == 0)
                        strResult.append("<value></value>");
                    else
                        strResult.append("<value>" + Utilities.cleanForXSL(strValue) + "</value>");  
                    
                    strResult.append("<strResultKey>"+ strResultKey + "</strResultKey>");                    
                   
                }
                
                strResult.append("</" + strSearchTag + ">");
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return strResult.toString();
    }
    
    /**
     * Get the current smart form result for data elements in a particular order range
     * @return hashtable contains all values
     */
    public static Hashtable getSmartformResults(String strSmartformParticipantID, int intStartDataElementOrder, int intEndDataElementOrder)
    {
        Hashtable hashResult = new Hashtable(10);
        String strDataElementOrder = "";
        String strDataElementResult = "";
        
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMRESULTS", null, null, null);
            query.setDomain("SMARTFORMPARTICIPANTS", "SMARTFORMRESULTS_intSmartformParticipantID", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
            query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setField("SMARTFORMRESULTS_intSmartformResultID", null);
            query.setField("SMARTFORMRESULTS_intDataElementID", null);
            query.setField("SMARTFORMRESULTS_strDataElementResult", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", "SMARTFORMTODATAELEMENTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", new Integer(intStartDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", new Integer(intEndDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            //System.err.println("results: "+query.convertSelectQueryToString());
            ResultSet rsResult = query.executeSelect();
            
            
            
            while (rsResult.next()){
                
                strDataElementResult = rsResult.getString("SMARTFORMRESULTS_strDataElementResult");
                strDataElementOrder = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                
                if (strDataElementResult == null)
                    strDataElementResult = "";
                    
                hashResult.put("d" + strDataElementOrder, strDataElementResult);
                
            }
            
            rsResult.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return hashResult;
        
    }
    
    
    /**
     * Get the current smart form result for data elements in a particular order range
     * and for a particular type
     * @return hashtable contains all values
     */
    public static Hashtable getSmartformResultsForType(String strSmartformParticipantID, int intStartDataElementOrder, int intEndDataElementOrder, String strDEType)
    {
        Hashtable hashResult = new Hashtable(10);
        String strDataElementOrder = "";
        String strDataElementResult = "";
        
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMRESULTS", null, null, null);
            query.setDomain("SMARTFORMPARTICIPANTS", "SMARTFORMRESULTS_intSmartformParticipantID", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
            query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setField("SMARTFORMRESULTS_intSmartformResultID", null);
            query.setField("SMARTFORMRESULTS_intDataElementID", null);
            query.setField("SMARTFORMRESULTS_strDataElementResult", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", "SMARTFORMTODATAELEMENTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", new Integer(intStartDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", new Integer(intEndDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "=", strDEType, 0, DALQuery.WHERE_HAS_VALUE);
            //System.err.println("results: "+query.convertSelectQueryToString());
            ResultSet rsResult = query.executeSelect();
                        
            while (rsResult.next()){
                
                strDataElementResult = rsResult.getString("SMARTFORMRESULTS_strDataElementResult");
                strDataElementOrder = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                
                if (strDataElementResult == null)
                    strDataElementResult = "";
                    
                hashResult.put(strDataElementOrder, strDataElementResult);
                
            }
            
            rsResult.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return hashResult;
        
    }
    
    /**
     * Get the current smart form result for data elements in a particular order range
     * @return hashtable contains all values
     */
    public static Hashtable getSmartformResultsID(String strSmartformParticipantID, int intStartDataElementOrder, int intEndDataElementOrder)
    {        
        Hashtable hashResultKey = new Hashtable(10);
        String strSFResultsID = "";
        String strSFResultsDataElementID = "";
        
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMRESULTS", null, null, null);
            query.setDomain("SMARTFORMPARTICIPANTS", "SMARTFORMRESULTS_intSmartformParticipantID", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
            query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setField("SMARTFORMRESULTS_intSmartformResultID", null);
            query.setField("SMARTFORMRESULTS_intDataElementID", null);
            query.setField("SMARTFORMRESULTS_strDataElementResult", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", "SMARTFORMTODATAELEMENTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", new Integer(intStartDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", new Integer(intEndDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            //System.err.println("resultsId: "+query.convertSelectQueryToString());
            ResultSet rsResult = query.executeSelect();
            
            while (rsResult.next())
            {                
                strSFResultsID = rsResult.getString("SMARTFORMRESULTS_intSmartformResultID");
                strSFResultsDataElementID = rsResult.getString("SMARTFORMRESULTS_intDataElementID");
                
                
                if (strSFResultsDataElementID == null)
                    strSFResultsDataElementID = "";
                
                hashResultKey.put(strSFResultsDataElementID , strSFResultsID);
            }
            
            rsResult.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        
        return hashResultKey;
    }
    
    
    /**
     * Save smart form results
     */
    public static boolean saveSmartformResult(String strSmartformID, String strSmartformParticipantID, String strStartOrder, String strEndOrder, ChannelRuntimeData runtimeData)
    {
        boolean blVal = true;
        try
        {           
            //Mandatory Start
            if(ValidateMandatoryFields(strSmartformID, strStartOrder, strEndOrder, runtimeData ) == true){
            //Mandatory End
                //System.out.println("all mandatory fields r filled");   
                Hashtable hashResult = getSmartformResults(strSmartformParticipantID, Integer.parseInt(strStartOrder), Integer.parseInt(strEndOrder));
                if (hashResult.size() > 0)
                    updateSmartformResult(strSmartformID, strSmartformParticipantID, strStartOrder, strEndOrder, runtimeData);
                else
                    insertSmartformResult(strSmartformID, strSmartformParticipantID, strStartOrder, strEndOrder, runtimeData);

                blVal = true;
            }else{ 
                 //System.out.println("there is mandatory field left blank");   
                 blVal = false;
            }
                
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        return blVal;
    }
    
    
    /**
     * Save smartform result to smartform entity
     */
    public static boolean saveSmartformResulttoSFEntity (String strSmartformID, String strStartOrder, String strEndOrder, ChannelRuntimeData runtimeData, ISmartformEntity sfEntityObj)
    {
        boolean blValidated = false;
        
        try
        {           
            // Check if mandatory fields have been filled
            if(ValidateMandatoryFields(strSmartformID, strStartOrder, strEndOrder, runtimeData ) == true)
            {                
                blValidated = insertSmartformResulttoSFEntity (strSmartformID, strStartOrder, strEndOrder, runtimeData, sfEntityObj);
            }
                
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
            blValidated = false;
        }
        
        return blValidated;
    }
    
    /**
     * Insert smart form results
     */
    public static boolean insertSmartformResulttoSFEntity(String strSmartformID, String strStartOrder, String strEndOrder, ChannelRuntimeData runtimeData, ISmartformEntity sfEntityObj)
    {

        try
        {
            String scriptDEOrder = null;
            String strScript = null;
            String scriptDEID= null;
            String strDEResult = null;
            
            // Vector of Data elements in the smartform entity object
            Vector vtDE = (Vector) sfEntityObj.getDE(); 
            Vector vtDECopy = (Vector) vtDE.clone();
             
            // get all the data elements that are need to update
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORM", null, null, null);
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setField("DATAELEMENTS_intDataElementID", null);
            query.setField("DATAELEMENTS_strDataElementName", null);
            query.setField("DATAELEMENTS_intDataElementType", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setField("DATAELEMENTS_strDataElementScript",null);
            query.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORM_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", strStartOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", strEndOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsDataElements = query.executeSelect();
            
            while (rsDataElements.next())
            {
                String strDataElementID = rsDataElements.getString("DATAELEMENTS_intDataElementID");
                String strDataElementName = rsDataElements.getString("DATAELEMENTS_strDataElementName");
                String strDataElementType = rsDataElements.getString("DATAELEMENTS_intDataElementType");
                String strDataElementOrder = rsDataElements.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                
                if (strDataElementType.equals("TEXT") || strDataElementType.equals("REPEATABLE TEXT") || strDataElementType.equals("DROPDOWN") || strDataElementType.equals("NUMERIC") 
                        || strDataElementType.equals("MONETARY") || strDataElementType.equals("SYSTEM LOOKUP") 
                        || strDataElementType.equals("SCRIPT") || strDataElementType.equals("FORM LINK") || strDataElementType.equals("CHECK BOX"))
                {
                    // To calculate the Script result if type is SCRIPT
                    if(strDataElementType.equals("SCRIPT")){                       
                       
                        scriptDEOrder = strDataElementOrder;
                        strScript = rsDataElements.getString("DATAELEMENTS_strDataElementScript");
                        scriptDEID = strDataElementID;
                        
                    }else{ 
                    
                        strDEResult = runtimeData.getParameter("d" + strDataElementOrder);
                    }
                }
                else if (strDataElementType.equals("DATE"))
                {
                    String strDate = null;
                    if ((runtimeData.getParameter("Day" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Day" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Month" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Month" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Year" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Year" + strDataElementOrder).trim().length() == 0))
                    {
                    }
                    else
                    {
                        strDate = runtimeData.getParameter("Year" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Month" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Day" + strDataElementOrder).trim();
                    
                        strDEResult = strDate;
                    }    
                }
                else if (strDataElementType.equals("ATTACHMENT"))
                {
                    
                    // Location where the attachment will get saved
                    String attachmentLocation = PropertiesManager.getProperty("neuragenix.genix.smartform.AttachmentLocation");     
     
                    //System.err.println("strDataElementOrder :"+strDataElementOrder);                    
                    String strFileName = QueryChannel.saveUploadFile("d" + strDataElementOrder, runtimeData, attachmentLocation);   
                     
                    //System.err.println("strFileName :"+strFileName);             
                                       
                    if( strFileName == null )
                        runtimeData.setParameter( "d" + strDataElementOrder, "" );
                    else{
                        //String strSessionUniqueID = authToken.getSessionUniqueID();
                        //strFileName = strSessionUniqueID + strFileName;
                        runtimeData.setParameter( "d" + strDataElementOrder, strFileName );
                    }
                    
                    strDEResult = strFileName;
                }
                
                boolean found = false;
                // Determine whether the DE already exists in the Smartform Entity object
                for (int i=0; i<vtDECopy.size(); i++)
                {                    
                    ISmartformDE SFDE = (ISmartformDE) vtDE.get(i);
                    if (strDataElementID == SFDE.getID())
                    {
                        SFDE.setResult(strDEResult);
                        found = true;
                    }                    
                } 
                
                // If the DE is not already in the SFEntity Object, add it
                if (found == false)
                {
                   ISmartformDE SFDEnew = new SmartformDE();
                   SFDEnew.setID(strDataElementID);
                   SFDEnew.setName(strDataElementName);
                   SFDEnew.setOrder(strDataElementOrder);
                   SFDEnew.setResult(strDEResult);
                   SFDEnew.setType(strDataElementType);
                   vtDE.add(SFDEnew); 
                }                  
            }
            
            rsDataElements.close();
            
/*            //If it is scipt type, calculate to get the script value and then insert
            if(scriptDEOrder != null){                
                //System.err.println(strScript);
                String strScriptResult = ""; //Seena getScriptResult(strScript, strSmartformParticipantID);
                //System.err.println(strScriptResult);
                runtimeData.setParameter(scriptDEOrder, strScriptResult); 
                
                hashResults.put(scriptDEID, runtimeData.getParameter(scriptDEOrder));
            }
*/                        
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
            return false;
        }
        
        return true;

    }    
    
    /**
     * Update smart form results
     */
    public static void updateSmartformResult(String strSmartformID, String strSmartformParticipantID, String strStartOrder, String strEndOrder, ChannelRuntimeData runtimeData)
    {
        try
        {            
            String scriptDEOrder = null;
            String strScript = null;
            String scriptDEID= null;
            
            // get all the data elements that are need to update
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setField("DATAELEMENTS_intDataElementID", null);
            query.setField("DATAELEMENTS_intDataElementType", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setField("DATAELEMENTS_strDataElementScript",null);
            query.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", strStartOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", strEndOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsDataElements = query.executeSelect();
            
            while (rsDataElements.next())
            {
                String strDataElementID = rsDataElements.getString("DATAELEMENTS_intDataElementID");
                String strDataElementType = rsDataElements.getString("DATAELEMENTS_intDataElementType");
                String strDataElementOrder = rsDataElements.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                
                
                if (strDataElementType.equals("TEXT") || strDataElementType.equals("REPEATABLE TEXT") || strDataElementType.equals("DROPDOWN") || strDataElementType.equals("NUMERIC") 
                        || strDataElementType.equals("MONETARY") || strDataElementType.equals("SYSTEM LOOKUP") 
                        || strDataElementType.equals("SCRIPT") || strDataElementType.equals("FORM LINK") || strDataElementType.equals("CHECK BOX"))
                {
                      // To calculate the Script result if type is SCRIPT
                    if(strDataElementType.equals("SCRIPT")){                       
                       
                        scriptDEOrder = strDataElementOrder;
                        strScript = rsDataElements.getString("DATAELEMENTS_strDataElementScript");
                        scriptDEID = strDataElementID;
                        
                    }else{ 
                        DALQuery checkExistQuery = new DALQuery();
                        checkExistQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        checkExistQuery.setField("SMARTFORMRESULTS_strDataElementResult", null);
                        checkExistQuery.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                        checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                        ResultSet rsCheckExist = checkExistQuery.executeSelect();
                        
                        if (rsCheckExist.next()) {
                            DALQuery currentQuery = new DALQuery();
                            currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                            currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", runtimeData.getParameter("d" + strDataElementOrder));
                            currentQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                            currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                            currentQuery.executeUpdate();
                        }
                        else {
                            DALQuery currentQuery = new DALQuery();
                            currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                            currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                            currentQuery.setField("SMARTFORMRESULTS_intDataElementID", strDataElementID);
                            currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", runtimeData.getParameter("d" + strDataElementOrder));
                            currentQuery.executeInsert();
                        }
                        rsCheckExist.close();
                    }
                }
                else if (strDataElementType.equals("REPEATABLE TEXT")) 
                {
                    String strDate = null;
                    if ((runtimeData.getParameter("Day" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Day" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Month" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Month" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Year" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Year" + strDataElementOrder).trim().length() == 0))
                    {
                    }
                    else
                        strDate = runtimeData.getParameter("Year" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Month" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Day" + strDataElementOrder).trim();
                    
                    DALQuery checkExistQuery = new DALQuery();
                    checkExistQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                    checkExistQuery.setField("SMARTFORMRESULTS_strDataElementResult", null);
                    checkExistQuery.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                    checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsCheckExist = checkExistQuery.executeSelect();

                    if (rsCheckExist.next()) {
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strDate);
                        currentQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.executeUpdate();
                    }
                    else {
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                        currentQuery.setField("SMARTFORMRESULTS_intDataElementID", strDataElementID);
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strDate);
                        currentQuery.executeInsert();
                    }
                    rsCheckExist.close();
                }                
                else if (strDataElementType.equals("DATE")) 
                {
                    String strDate = null;
                    if ((runtimeData.getParameter("Day" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Day" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Month" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Month" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Year" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Year" + strDataElementOrder).trim().length() == 0))
                    {
                    }
                    else
                        strDate = runtimeData.getParameter("Year" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Month" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Day" + strDataElementOrder).trim();
                    
                    DALQuery checkExistQuery = new DALQuery();
                    checkExistQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                    checkExistQuery.setField("SMARTFORMRESULTS_strDataElementResult", null);
                    checkExistQuery.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                    checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsCheckExist = checkExistQuery.executeSelect();

                    if (rsCheckExist.next()) {
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strDate);
                        currentQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.executeUpdate();
                    }
                    else {
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                        currentQuery.setField("SMARTFORMRESULTS_intDataElementID", strDataElementID);
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strDate);
                        currentQuery.executeInsert();
                    }
                    rsCheckExist.close();
                }
                // Attachment
                else if (strDataElementType.equals("ATTACHMENT"))
                {
                    //System.out.println("attachHidden"+strDataElementOrder+ ":"+runtimeData.getParameter("attachHidden"+strDataElementOrder));                    
                    
                    // Location where the attachment will get saved
                    String attachmentLocation = PropertiesManager.getProperty("neuragenix.genix.smartform.AttachmentLocation");     

                    //System.err.println("strDataElementOrder :"+strDataElementOrder);
                    
                    String strFileName = QueryChannel.saveUploadFile("d" + strDataElementOrder, runtimeData, attachmentLocation);   
                      
                    
                    //System.err.println("strFileName :"+strFileName);

                    // if attach text bix is empty / returns null
                    if( strFileName == null ){
                        
                        // if there was an attachment already, update with that value
                        if(runtimeData.getParameter("attachHidden"+ "d" + strDataElementOrder) != null){    
                            
                            //System.out.println("attach hidden is not null");
                            strFileName = runtimeData.getParameter("attachHidden"+ "d" + strDataElementOrder);
                            
                        }else{// if not set it to blank
                            
                            //System.out.println("attach hidden is null");
                            strFileName = "";                            
                        }

                    }
                    
                    runtimeData.setParameter( "d" + strDataElementOrder, strFileName );
                    runtimeData.setParameter( "attachHidden"+ "d" + strDataElementOrder, strFileName );
                    //System.out.println("strDataElementOrder :"+runtimeData.getParameter(strDataElementOrder));   
                    
                    DALQuery checkExistQuery = new DALQuery();
                    checkExistQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                    checkExistQuery.setField("SMARTFORMRESULTS_strDataElementResult", null);
                    checkExistQuery.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                    checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsCheckExist = checkExistQuery.executeSelect();

                    if (rsCheckExist.next()) {
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                        currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE); 
                        currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                    
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strFileName); 
                        currentQuery.executeUpdate();
                    }
                    else {
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                        currentQuery.setField("SMARTFORMRESULTS_intDataElementID", strDataElementID);
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strFileName);
                        currentQuery.executeInsert();
                    }
                    
                    rsCheckExist.close();
                    
                    //System.err.println("updated");
                    
                }// end of else if
            }// End of while
            
            rsDataElements.close();
            
            //If it is scipt type, calculate to get the script value and then update
             if(scriptDEOrder != null){                
                //System.err.println(strScript);
                String strScriptResult = getScriptResult(strScript, strSmartformParticipantID);
                //System.err.println(strScriptResult);
                runtimeData.setParameter(scriptDEOrder, strScriptResult); 
                
                DALQuery checkExistQuery = new DALQuery();
                checkExistQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                checkExistQuery.setField("SMARTFORMRESULTS_strDataElementResult", null);
                checkExistQuery.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                checkExistQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", scriptDEID, 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsCheckExist = checkExistQuery.executeSelect();

                if (rsCheckExist.next()) {
                    //update script
                    DALQuery currentQuery = new DALQuery();
                    currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                    currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strScriptResult);
                    currentQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                    currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", scriptDEID, 0, DALQuery.WHERE_HAS_VALUE);
                    currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    currentQuery.executeUpdate();
                }
                else {
                    DALQuery currentQuery = new DALQuery();
                    currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                    currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                    currentQuery.setField("SMARTFORMRESULTS_intDataElementID", scriptDEID);
                    currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strScriptResult);
                    currentQuery.executeInsert();
                }
                
                rsCheckExist.close();
               
            }
            
        }
        catch (Exception e) 
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
    }
    
    /**
     * Insert smart form results
     */
    public static void insertSmartformResult(String strSmartformID, String strSmartformParticipantID, String strStartOrder, String strEndOrder, ChannelRuntimeData runtimeData)
    {
        try
        {
            String scriptDEOrder = null;
            String strScript = null;
            String scriptDEID= null;
             
            // get all the data elements that are need to update
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setField("DATAELEMENTS_intDataElementID", null);
            query.setField("DATAELEMENTS_intDataElementType", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setField("DATAELEMENTS_strDataElementScript",null);
            query.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", strStartOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", strEndOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsDataElements = query.executeSelect();
            
            while (rsDataElements.next())
            {
                String strDataElementID = rsDataElements.getString("DATAELEMENTS_intDataElementID");
                String strDataElementType = rsDataElements.getString("DATAELEMENTS_intDataElementType");
                String strDataElementOrder = rsDataElements.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                
                //Anita added type equals SCRIPT
                if (strDataElementType.equals("TEXT") || strDataElementType.equals("REPEATABLE TEXT") || strDataElementType.equals("DROPDOWN") || strDataElementType.equals("NUMERIC") 
                        || strDataElementType.equals("MONETARY") || strDataElementType.equals("SYSTEM LOOKUP") 
                        || strDataElementType.equals("SCRIPT") || strDataElementType.equals("FORM LINK") || strDataElementType.equals("CHECK BOX"))
                {
                    // To calculate the Script result if type is SCRIPT
                    if(strDataElementType.equals("SCRIPT")){                       
                       
                        scriptDEOrder = strDataElementOrder;
                        strScript = rsDataElements.getString("DATAELEMENTS_strDataElementScript");
                        scriptDEID = strDataElementID;
                        
                    }else{ 
                    
                        DALQuery currentQuery = new DALQuery();
                        currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                        currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                        currentQuery.setField("SMARTFORMRESULTS_intDataElementID", strDataElementID);
                        currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", runtimeData.getParameter("d" + strDataElementOrder));
                        currentQuery.executeInsert();
                    }
                }
                else if (strDataElementType.equals("DATE"))
                {
                    String strDate = null;
                    if ((runtimeData.getParameter("Day" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Day" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Month" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Month" + strDataElementOrder).trim().length() == 0) &&
                        (runtimeData.getParameter("Year" + strDataElementOrder) == null ||
                        runtimeData.getParameter("Year" + strDataElementOrder).trim().length() == 0))
                    {
                    }
                    else
                        strDate = runtimeData.getParameter("Year" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Month" + strDataElementOrder).trim() + "-" +
                                  runtimeData.getParameter("Day" + strDataElementOrder).trim();
                    
                    DALQuery currentQuery = new DALQuery();
                    currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                    currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                    currentQuery.setField("SMARTFORMRESULTS_intDataElementID", strDataElementID);
                    currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strDate);
                    currentQuery.executeInsert();
                }
                else if (strDataElementType.equals("ATTACHMENT"))
                {
                    
                    // Location where the attachment will get saved
                    String attachmentLocation = PropertiesManager.getProperty("neuragenix.genix.smartform.AttachmentLocation");     
     
                    //System.err.println("strDataElementOrder :"+strDataElementOrder);                    
                    String strFileName = QueryChannel.saveUploadFile("d" + strDataElementOrder, runtimeData, attachmentLocation);   
                     
                    //System.err.println("strFileName :"+strFileName);             
                                       
                    if( strFileName == null )
                        runtimeData.setParameter( "d" + strDataElementOrder, "" );
                    else{
                        //String strSessionUniqueID = authToken.getSessionUniqueID();
                        //strFileName = strSessionUniqueID + strFileName;
                        runtimeData.setParameter( "d" + strDataElementOrder, strFileName );
                    }
                    
                    DALQuery currentQuery = new DALQuery();
                    currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                    currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                    currentQuery.setField("SMARTFORMRESULTS_intDataElementID", strDataElementID);
                    currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", strFileName); 
                    currentQuery.executeInsert();
                    //System.err.println("inserted");
                }
            }
            
            rsDataElements.close();
            
            //If it is scipt type, calculate to get the script value and then insert
            if(scriptDEOrder != null){                
                //System.err.println(strScript);
                String strScriptResult = getScriptResult(strScript, strSmartformParticipantID);
                //System.err.println(strScriptResult);
                runtimeData.setParameter(scriptDEOrder, strScriptResult); 
                
                //insert script
                DALQuery currentQuery = new DALQuery();
                currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
                currentQuery.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSmartformParticipantID);
                currentQuery.setField("SMARTFORMRESULTS_intDataElementID", scriptDEID);
                currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", runtimeData.getParameter(scriptDEOrder));
                currentQuery.executeInsert();                
            }
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
    }
    
    /**
     * Validate smartform data
     * @param channel runtime data
     * @param smartform key
     * @param start data element order
     * @param end data element order
     * @return a string to indicate if there is any invalid data
     */
    public static String validateSmartformData(ChannelRuntimeData rd, String strSmartformID, String strStartOrder, String strEndOrder)
    {
        String strResult = "";
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORM", null, null, null);
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORM_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", strStartOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", strEndOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementNo", null);
            query.setField("DATAELEMENTS_intDataElementIntMax", null);
            query.setField("DATAELEMENTS_intDataElementIntMin", null);
            query.setField("DATAELEMENTS_dtDataElementDateMax", null);
            query.setField("DATAELEMENTS_dtDataElementDateMin", null);
            query.setField("DATAELEMENTS_intDataElementType", null);
            ResultSet rsResult = query.executeSelect();
            
            while (rsResult.next())
            {
                String strDataElementOrder = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                String strDataElementType = rsResult.getString("DATAELEMENTS_intDataElementType");
                
                // data type is NUMERIC
                if (strDataElementType.equals("NUMERIC"))
                {
                    if (rd.getParameter("d" + strDataElementOrder) != null && rd.getParameter("d" + strDataElementOrder).trim().length() > 0)
                    {
                        String strValue = rd.getParameter("d" + strDataElementOrder).trim();
                        if (!Utilities.validateFloatValue(strValue))
                            strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                        else
                        {
                            double dbValue = Double.parseDouble(strValue);
                            
                            // data max and min specified
                            if (rsResult.getString("DATAELEMENTS_intDataElementIntMax") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMax").trim().length() > 0 &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMin") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMin").trim().length() > 0)
                            {
                                //double dbMax = rsResult.getDouble("DATAELEMENTS_intDataElementIntMax");
                                double dbMax = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMax"));
                                double dbMin = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMin"));
                                //double dbMin = rsResult.getDouble("DATAELEMENTS_intDataElementIntMin");
                                
                                if (dbValue > dbMax || dbValue < dbMin)
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                            // data max is specified
                            else if (rsResult.getString("DATAELEMENTS_intDataElementIntMax") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMax").trim().length() > 0)
                            {
                                //double dbMax = rsResult.getDouble("DATAELEMENTS_intDataElementIntMax");
                                double dbMax = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMax"));
                                
                                if (dbValue > dbMax)
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                            // data min is specified
                            else if (rsResult.getString("DATAELEMENTS_intDataElementIntMin") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMin").trim().length() > 0)
                            {
                                //double dbMin = rsResult.getDouble("DATAELEMENTS_intDataElementIntMin");
                                double dbMin = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMin"));
                                
                                if (dbValue < dbMin)
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                        }
                    }
                }
                // data type is MONETARY
                else if (strDataElementType.equals("MONETARY"))
                {
                    if (rd.getParameter("d" + strDataElementOrder) != null && rd.getParameter("d" + strDataElementOrder).trim().length() > 0)
                    {
                        String strValue = rd.getParameter("d" + strDataElementOrder).trim();
                        String valuetoTest = strValue;
                        if(valuetoTest.indexOf(',') > 0)
                        {
                            valuetoTest = valuetoTest.replaceAll(",", "");
                        }
                        if (!Utilities.validateMonetaryValue(strValue))
                            strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                        else
                        {
                            double dbValue = Double.parseDouble(valuetoTest);
                            
                            // data max and min specified
                            if (rsResult.getString("DATAELEMENTS_intDataElementIntMax") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMax").trim().length() > 0 &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMin") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMin").trim().length() > 0)
                            {
                                //double dbMax = rsResult.getDouble("DATAELEMENTS_intDataElementIntMax");
                                //double dbMin = rsResult.getDouble("DATAELEMENTS_intDataElementIntMin");
                                double dbMax = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMax").replaceAll(",",""));
                                double dbMin = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMin").replaceAll(",",""));
                                
                                if (dbValue > dbMax || dbValue < dbMin)
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                            // data max is specified
                            else if (rsResult.getString("DATAELEMENTS_intDataElementIntMax") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMax").trim().length() > 0)
                            {
                                //double dbMax = rsResult.getDouble("DATAELEMENTS_intDataElementIntMax");
                                double dbMax = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMax").replaceAll(",",""));
                                if (dbValue > dbMax)
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                            // data min is specified
                            else if (rsResult.getString("DATAELEMENTS_intDataElementIntMin") != null &&
                                rsResult.getString("DATAELEMENTS_intDataElementIntMin").trim().length() > 0)
                            {
                                double dbMin = Double.parseDouble(rsResult.getString("DATAELEMENTS_intDataElementIntMin").replaceAll(",",""));
                                
                                if (dbValue < dbMin)
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                        }
                    }
                }
                // data type is DATE
                else if (strDataElementType.equals("DATE"))
                {
                    if ((rd.getParameter("Day" + strDataElementOrder) != null && rd.getParameter("Day" + strDataElementOrder).trim().length() > 0) ||
                        (rd.getParameter("Month" + strDataElementOrder) != null && rd.getParameter("Month" + strDataElementOrder).trim().length() > 0) ||
                        (rd.getParameter("Year" + strDataElementOrder) != null && rd.getParameter("Year" + strDataElementOrder).trim().length() > 0))
                    {
                        String strValue = rd.getParameter("Day" + strDataElementOrder).trim() + "/" + rd.getParameter("Month" + strDataElementOrder).trim() + "/" + rd.getParameter("Year" + strDataElementOrder).trim();
                        
                        if (!Utilities.validateDateValue(strValue))
                            strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                        else
                        {
                            Calendar cdValue = Calendar.getInstance();
                            cdValue.setTime(Utilities.convertStringToDate(strValue, "dd/MM/yyyy"));
                            
                            // data max and min specified
                            if (rsResult.getDate("DATAELEMENTS_dtDataElementDateMax") != null &&
                                rsResult.getDate("DATAELEMENTS_dtDataElementDateMin") != null)
                            {
                                
                                Calendar cdMax = Calendar.getInstance();
                                cdMax.setTime(rsResult.getDate("DATAELEMENTS_dtDataElementDateMax"));
                                Calendar cdMin = Calendar.getInstance();
                                cdMin.setTime(rsResult.getDate("DATAELEMENTS_dtDataElementDateMin"));
                                
                                if (cdValue.before(cdMin) || cdValue.after(cdMax))
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                            // data max specified
                            else if (rsResult.getDate("DATAELEMENTS_dtDataElementDateMax") != null)
                            {
                                Calendar cdMax = Calendar.getInstance();
                                cdMax.setTime(rsResult.getDate("DATAELEMENTS_dtDataElementDateMax"));
                                
                                if (cdValue.after(cdMax))
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                            // data min specified
                            else if (rsResult.getString("DATAELEMENTS_dtDataElementDateMin") != null)
                            {
                                Calendar cdMin = Calendar.getInstance();
                                cdMin.setTime(rsResult.getDate("DATAELEMENTS_dtDataElementDateMin"));
                                
                                if (cdValue.before(cdMin))
                                    strResult += rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo") + ", ";
                            }
                        }
                    }
                }
            }
            
            rsResult.close();
            
            if (!strResult.equals(""))
                strResult = "The data element number: " + strResult + "have incorrect values or out of valid range. Please check it again.";
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return strResult;
    }
    
    /**
     * Builds list of data for bioanalysis smartform list
     */    
    protected static Vector buildBioanalysisTable(ResultSet rs)
    {
        
        int lastKey = -1;
        int rowCount = 0;
        int first = 0;
        int colourFound = 0;
        int numCols = 4;
        
        if (BIOANALYSIS_BIO_ID == true)
        {
            numCols = 5;
        }
        
        
        if (rs == null)
           return null;
        
        
        Vector returnVector = new Vector();
        Hashtable tempTable = new Hashtable();
        try {
            // rs.first();
            // while (!rs.isAfterLast())
            while (rs.next())
            {
                int currentKey = rs.getInt("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
                if (lastKey != currentKey)
                {
                    
                    
                    lastKey = currentKey;
                    rowCount = 0;
                    
                    String tempString = "";

                    if (first != 0)
                    {
                       if (colourFound == 0)
                       {
                           
                           tempTable.put("SMARTFORM_rowBackgroundColour", "RED");
                       }
                       returnVector.add(tempTable);
                       tempTable = new Hashtable();
                    } 
                    colourFound = 0;
                    first++;
                    
                    tempTable.put("SMARTFORM_strSmartformName", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORM_strSmartformName"))));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_intSmartformID", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_intSmartformID"))));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_strSmartformStatus", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_strSmartformStatus"))));
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_intParticipantID", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_intParticipantID"))));
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_intSmartformParticipantID", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID") )));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_strUserNote", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_strUserNote") )));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_strAddedBy",Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_strAddedBy") )));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_dtDateAdded", Utilities.cleanForXSL(Utilities.convertDateForDisplay(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_dtDateAdded") ))));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_strLastUpdatedBy", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_strLastUpdatedBy"))));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_strSmartformStatus", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_strSmartformStatus") )));
                    
                    
                    tempTable.put("SMARTFORMPARTICIPANTS_intCurrentPage", Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMPARTICIPANTS_intCurrentPage") )));

                }

                if (rowCount <= numCols)
                {
                    
                    String strDENo = rs.getString("SMARTFORMTODATAELEMENTS_intDataElementNo");
                    if(strDENo != null && strDENo.length() > 0)
                    {
                        rowCount++;
                        int intDENo = new Integer(strDENo).intValue();
                        if(intDENo <= numCols)
                        {
    /*                        tempTable.put("DATAELEMENTS_intDataelementOrder" + rowCount, Utilities.cleanForXSL(getNullFreeString(rs.getString("DATAELEMENTS_intDataElementOrder"))));


                            tempTable.put ("SMARTFORMRESULTS_strDataElementResult" + rowCount, Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMRESULTS_strDataElementResult"))));


                            tempTable.put ("DATAELEMENTS_strDataElementName" + rowCount, Utilities.cleanForXSL(getNullFreeString(rs.getString("DATAELEMENTS_strDataElementName"))));*/
                            tempTable.put("DATAELEMENTS_intDataelementOrder" + intDENo, Utilities.cleanForXSL(getNullFreeString(rs.getString("DATAELEMENTS_intDataElementOrder"))));


                            tempTable.put ("SMARTFORMRESULTS_strDataElementResult" + intDENo, Utilities.cleanForXSL(getNullFreeString(rs.getString("SMARTFORMRESULTS_strDataElementResult"))));


                            tempTable.put ("DATAELEMENTS_strDataElementName" + intDENo, Utilities.cleanForXSL(getNullFreeString(rs.getString("DATAELEMENTS_strDataElementName"))));
                        }
                    }
                }

                if (colourFound == 0)
                {
                   
                    if (rs.getString("DATAELEMENTS_strDataElementName") != null)
                    {
                        
                        
                        if (rs.getString("DATAELEMENTS_strDataElementName").equalsIgnoreCase("Normal"))
                        {
                            if (rs.getString("SMARTFORMRESULTS_strDataElementResult") != null)
                            {
                                
                                if (rs.getString("SMARTFORMRESULTS_strDataElementResult").equalsIgnoreCase("Normal"))
                                {
                                    tempTable.put("SMARTFORM_rowBackgroundColour", "BLUE");
                                    colourFound++;
                                }
                                else
                                {
                                    tempTable.put("SMARTFORM_rowBackgroundColour", "YELLOW");
                                    colourFound++;
                                    
                                }
                            }
                            
                        }
                    }
                }

                // rs.next();
            }
            if (colourFound == 0)
            {
                  
                  tempTable.put("SMARTFORM_rowBackgroundColour", "RED");
            }
            returnVector.add(tempTable); // add the last one!
       
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Smartform Channel - SQLException : " + e.toString(), e);
            return null;
        }
        return returnVector;
    }

    
    /** This method builds the script answer based on a script provided and a set of results provided
     * @param the script
     * @param result set
     */

    private static String getScriptResult(String strScript, String strSmartformParticipantID)
    {
        String strScriptResult = new String();
        boolean blHasData = false;
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMRESULTS", null, null, null);
            query.setDomain("SMARTFORMPARTICIPANTS", "SMARTFORMRESULTS_intSmartformParticipantID", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
            query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN");
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setField("SMARTFORMRESULTS_strDataElementResult", null);
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setField("DATAELEMENTS_intDataElementType", null);
            query.setField("DATAELEMENTS_intDataElementID", null);
            query.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", "SMARTFORMTODATAELEMENTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            
            ResultSet rsResult = query.executeSelect();
            
            // Get the scripting object
            Interpreter my_scripting_object = new Interpreter(); 
            
            // Loop through
            while(rsResult.next())
            {
                blHasData = true;
                
                // Add the question answers to the script object (get the answer depending on the type)
                if (rsResult.getString("DATAELEMENTS_intDataElementType").equals("TEXT") ||
                    rsResult.getString("DATAELEMENTS_intDataElementType").equals("DROPDOWN") || rsResult.getString("DATAELEMENTS_intDataElementType").equals("CHECK BOX"))
                {
                    if (rsResult.getString("SMARTFORMRESULTS_strDataElementResult") != null)
                        my_scripting_object.set("DID" + rsResult.getString("DATAELEMENTS_intDataElementID"), rsResult.getString("SMARTFORMRESULTS_strDataElementResult"));
                    else
                        my_scripting_object.set("DID" + rsResult.getString("DATAELEMENTS_intDataElementID"),  "");
                }
                else if( rsResult.getString("DATAELEMENTS_intDataElementType").equals("NUMERIC") ||
                         rsResult.getString("DATAELEMENTS_intDataElementType").equals("MONETARY"))
                {       
                    if (rsResult.getString("SMARTFORMRESULTS_strDataElementResult") != null)
                        my_scripting_object.set("DID" + rsResult.getString("DATAELEMENTS_intDataElementID"), rsResult.getDouble("SMARTFORMRESULTS_strDataElementResult"));
                    else
                        my_scripting_object.set("DID" + rsResult.getString("DATAELEMENTS_intDataElementID"),  0);
                }
                else if (rsResult.getString("DATAELEMENTS_intDataElementType").equals("DATE"))
                {
                    if (rsResult.getString("SMARTFORMRESULTS_strDataElementResult") != null)
                        my_scripting_object.set("DID" + rsResult.getString("DATAELEMENTS_intDataElementID"), rsResult.getDate("SMARTFORMRESULTS_strDataElementResult"));
                    else
                        my_scripting_object.set("DID" + rsResult.getString("DATAELEMENTS_intDataElementID"),  null);
                }
            }
            
            rsResult.close();
            
            // run the script
            if ((blHasData) && (strScript != null))
            {
                strScriptResult = my_scripting_object.eval(strScript).toString();
            }
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return strScriptResult;
    }
    
     /**
     * Detect and remove nulls from strings
     */    
    protected static String getNullFreeString(String valueToCheck)
    {
        if (valueToCheck == null)
            return "";
        else if (valueToCheck.equals("null"))
            return "";
        else
            return valueToCheck;
        
    }
    
    /**
     * Creates XML from key value pairs in a hash table.
     *
     * Used by Bioanalysis
     */    
    protected static String buildSearchXMLFromTable(String tag, Hashtable htValues)
    {
     
        String returnValue = "";
        String tempKey = "";
        Enumeration enumValues = htValues.elements();
        Enumeration enumKeys = htValues.keys();
        
        returnValue += "<" + tag + ">";
        
        while (enumKeys.hasMoreElements() && enumValues.hasMoreElements())
        {
            tempKey = String.valueOf(enumKeys.nextElement());
            returnValue += "<" + tempKey + ">";
            returnValue += String.valueOf(enumValues.nextElement());
            returnValue += "</" + tempKey + ">";
        }
        
        returnValue += "</" + tag + ">";

        
        
        return returnValue;
        
    }
    
    
    
    private static String getSmartFormDataElementValue(String strSmartformID, String strSmartformDataElementID)
    {
        String strDataElementValue = null;
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMRESULTS", null, null, null);
            query.setDomain("SMARTFORMPARTICIPANTS", "SMARTFORMRESULTS_intSmartformParticipantID", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
            query.setField("SMARTFORMRESULTS_strDataElementResult", null);
            query.setField("SMARTFORMRESULTS_intSmartformParticipantID", null);
            query.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strSmartformDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SMARTFORMRESULTS_intSmartformParticipantID", "DESC");
            ResultSet rsResult = query.executeSelect();
            
            if (rsResult.next())
                strDataElementValue = rsResult.getString("SMARTFORMRESULTS_strDataElementResult");
            
            rsResult.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return strDataElementValue;
    }

    /**
     * Builds a hashtable consisting of the dataelement results in a SmartformEntity object
     * This is used in Batch Analysis SF generation
     * @param sfEntityObject: The SmartformEntity object containing the dataelement results
     * @return Hashtable - consisting of the dataelement results
     */    
    private static Hashtable getSmartFormDataFromSFEntityObject(ISmartformEntity sfEntityObject) 
    {
        Hashtable hashResult = new Hashtable(10);
        
        // Vector of Data elements in the smartform entity object
        Vector vtDE = (Vector) sfEntityObject.getDE(); 
        
        for (int i=0; i<vtDE.size(); i++)
        {
            ISmartformDE DE = (ISmartformDE) vtDE.get(i);
            if (DE.getResult() != null)
            {   
                hashResult.put("d" + DE.getOrder(), DE.getResult());            
            }    
        }    
        
        return hashResult;
    }
    
    
    /**
     * Build the options XML string
     * @param strDataElementID: primary key of the data element
     * @param strValue: current option value
     * @return XML string for building the dropdown list for options
     */
    public static String buildLookupAdditionalInfo(String strDataElementID, String strValue, String strDataElementLookupType, String strLookupObjectPrimaryField,Vector vtDomains, Vector vtJoins, ResultSet rsLookupResult, ChannelRuntimeData runtimeData)
    {
        StringBuffer strResult = new StringBuffer();
        
        try
        {
            //System.out.println("strDataElementID:"+strDataElementID);
            //System.out.println("strValue in buildLookupAdditionalInfo:"+strValue);
            //System.out.println("strDataElementLookupType:"+strDataElementLookupType);
            //System.out.println("strLookupObjectPrimaryField:"+strLookupObjectPrimaryField);
            //System.out.println("vtDomains:"+vtDomains.toString());
            //System.out.println("vtJoins:"+vtJoins.toString());
            //System.out.println("rsLookupResult"+);
            //System.out.println("runtimeData"+runtimeData.toString());
            
            
            Vector vtSmartformsDataelements = DatabaseSchema.getFormFields("csmartform_smartforms_dataelements");
            Vector vtLookupField = DatabaseSchema.getFormFields("csmartform_addEditDataElements_LookupField");
            
            // getting the fields needed to display under the dropdown, as additonal details
            DALQuery queryInAddnl = new DALQuery();
            queryInAddnl.setDomain("SYSTEMLOOKUPFIELD", null, null, null); 
            queryInAddnl.setFields(vtLookupField, null);            
            queryInAddnl.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE); 
            queryInAddnl.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE); 
            // Additional Info Start
            queryInAddnl.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_strInDisplayAddnl", "=","0", 0, DALQuery.WHERE_HAS_VALUE); 
            // Additional Info End
            
            ResultSet rsAddnlFields = queryInAddnl.executeSelect(); 

            Vector vtInAddnlFields = new Vector(5, 2);
            while (rsAddnlFields.next())
                vtInAddnlFields.add(rsAddnlFields.getString("SYSTEMLOOKUPFIELD_strInternalName"));

            rsAddnlFields.close();
            
            // build a hashtble to store all the fields
            Hashtable hashFieldsAddnl = new Hashtable(10);
            hashFieldsAddnl.put(strLookupObjectPrimaryField, "needed");
            for (int i=0; i < vtInAddnlFields.size(); i++)
                hashFieldsAddnl.put(vtInAddnlFields.get(i), "needed");                                       


            // Get the dropdown name i.e dataelement order in SFToDEs table
            DALQuery queryOrder = new DALQuery();
            queryOrder.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
            queryOrder.setFields(vtSmartformsDataelements, null);                     
            queryOrder.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
            queryOrder.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);
            queryOrder.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultOrder = queryOrder.executeSelect();
            String strLookupDropdownName = "";
            if(rsResultOrder.next()){   
                strLookupDropdownName = rsResultOrder.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                //System.out.println("Order :"+rsResultOrder.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder"));
            }

            rsResultOrder.close();
            
            // To get the value of the dropdown            
            String strDropdownVal = "";
            
                if(runtimeData.getParameter(strLookupDropdownName) != null){
                    strDropdownVal = runtimeData.getParameter(strLookupDropdownName);

                }else{// get the first dropdowns key value

                    strDropdownVal = "";
                    /*
                    if(rsLookupResult != null){
                        rsLookupResult.beforeFirst();
                        if(rsLookupResult.next()){
                            strDropdownVal = rsLookupResult.getString(strLookupObjectPrimaryField);
                        }
                    }
                     */
                }
            
            
            if(runtimeData.getParameter("lookupReload") == null || runtimeData.getParameter("lookupReload").equals("")){
                //System.out.println("===================================db value assigned");
                strDropdownVal = strValue;
            }    
            
            
            runtimeData.setParameter("strLookupDropdownName",strDropdownVal);
            //System.out.println("runtimevalue of dropdown in buildaddnlinfo is: "+runtimeData.getParameter("strLookupDropdownName"));
            
            // To fetch the additional information for this value selected in the dropdown
            // building the query with domains, joins and fields
            DALQuery queryDetails = new DALQuery();
            queryDetails.setDomain((String) vtDomains.get(0), null, null, null);
            queryDetails.setWhere(null, 0, vtDomains.get(0) + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);


            for (int i=1; i < vtDomains.size(); i++)
            {
                DBJoin joinObj = (DBJoin) vtJoins.get(i-1);
                queryDetails.setDomain((String) vtDomains.get(i), joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                queryDetails.setWhere("AND", 0, vtDomains.get(i) + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            }                    

            // set fields
            Enumeration enumFields = hashFieldsAddnl.keys();
            while (enumFields.hasMoreElements())
                queryDetails.setField((String) enumFields.nextElement(), null);

            if(strDropdownVal != null && strDropdownVal.equals("")){
                strDropdownVal = null;
            }
            queryDetails.setWhere("AND", 0, strLookupObjectPrimaryField, "=", strDropdownVal, 0, DALQuery.WHERE_HAS_VALUE);

            //System.out.println(queryDetails.convertSelectQueryToString());
            ResultSet rsAddnlInfo = queryDetails.executeSelect();
            if(rsAddnlInfo.next()){

                String strAddnlFieldLabel = "";
                String strAddnlFieldValue = "";

                for (int i=0; i < vtInAddnlFields.size(); i++){
                
                    if(rsAddnlInfo.getString((String) vtInAddnlFields.get(i)) != null){          

                        String strFieldIntName = (String) vtInAddnlFields.get(i);
                        DBField dbFieldObj = (DBField) DatabaseSchema.getFields().get(strFieldIntName);

                        strAddnlFieldLabel = dbFieldObj.getLabelInColumn();
                        strAddnlFieldValue = rsAddnlInfo.getString((String) vtInAddnlFields.get(i));

                        //System.out.println("Addnl detail Label: "+strAddnlFieldLabel);
                        //System.out.println("Addnl val:"+strAddnlFieldValue); 

                        strResult.append("<DisplayAddnlInfo><AddnlInfoLabel>"+ strAddnlFieldLabel +"</AddnlInfoLabel><AddnlInfoValue>"+ strAddnlFieldValue +"</AddnlInfoValue></DisplayAddnlInfo>");

                    }// End of if
                }// End of For

            }// End of rs.next                    
                    
            rsAddnlInfo.close();
            
            
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
        
        return strResult.toString();
    }
    
    /**
     * Save smart form results
     */
    public static boolean ValidateMandatoryFields(String strSmartformID, String strStartOrder, String strEndOrder, ChannelRuntimeData runtimeData)
    {
        boolean blVar = true;
        try
        {       
            Vector vtFormFields = DatabaseSchema.getFormFields("csmartform_SmartformtoDataElements");
            
            // Get all the dataelements for this Smartform ID between the start and end order
            DALQuery query = new DALQuery();
            query.setDomain("DATAELEMENTS", null, null, null);
            query.setDomain("SMARTFORMTODATAELEMENTS","DATAELEMENTS_intDataElementID","SMARTFORMTODATAELEMENTS_intDataElementID","INNER JOIN");
            query.setFields(vtFormFields, null);
            query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", strStartOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", strEndOrder, 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");
            //System.err.println("QUERY :"+query.convertSelectQueryToString());
            ResultSet rsQuery = query.executeSelect();
            
            String strDEOrder = "";
            String strRequired = "";
            String strDEKey = "";
            
            while(rsQuery.next()){
                
                strDEOrder = rsQuery.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                strRequired = rsQuery.getString("DATAELEMENTS_intMandatory");
                strDEKey = rsQuery.getString("DATAELEMENTS_intDataElementID");
                //System.err.println("strDEOrder :"+strDEOrder+"its val is :"+runtimeData.getParameter(strDEOrder));
                //System.err.println("strRequired :"+strRequired);
                
                if(rsQuery.getString("DATAELEMENTS_intDataElementType").equals("DATE")){
                    if(strRequired != null && strRequired.equals("-1")){
                      String strDEOrder1 = "Day"+strDEOrder;
                      String strDEOrder2 = "Month"+strDEOrder;
                      String strDEOrder3 = "Year"+strDEOrder;
                      
                      if(runtimeData.getParameter(strDEOrder1) == null || runtimeData.getParameter(strDEOrder2) == null || runtimeData.getParameter(strDEOrder3).equals("")){                        
                            //System.err.println("this strDEOrder is date is blank :"+strDEOrder1+"its val is :"+runtimeData.getParameter(strDEOrder));
                            blVar = false;
                            runtimeData.setParameter(strDEOrder,"");
                            break;                        
                        }else{
                            String strDate = runtimeData.getParameter(strDEOrder1) +"-"+ runtimeData.getParameter(strDEOrder2) +"-"+ runtimeData.getParameter(strDEOrder3);
                            runtimeData.setParameter(strDEOrder,strDate);   
                        }
                    }
                }else{ // if it is not Date
                    if(strRequired != null && strRequired.equals("-1")){
                        if(runtimeData.getParameter(strDEOrder) != null){ 
                            if(runtimeData.getParameter(strDEOrder).equals("")){                        
                                //System.err.println("this strDEOrder is blank :"+strDEOrder+"its val is :"+runtimeData.getParameter(strDEOrder));
                                blVar = false;
                                runtimeData.setParameter(strDEOrder,"");
                                break;                        
                            }
                        }
                    }
                
                }    
                
            }
            rsQuery.close();
           
        }
        catch (Exception e) 
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in SmartformUtilities - " + e.toString(), e);
        }
         return blVar ;
    }
    
} 