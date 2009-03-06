/**
 * StudyAllocation.java
 * Copyright (C) 2004 Neuragenix Pty Ltd
 *
 *
 * Class for managing searches based on allocations of Biospecimens to particular studies
 *
 * Created on 25 November 2004, 17:35
 *
 * @author : Daniel Murley dmurley@neuragenix.com
 *
 *
 */

package neuragenix.bio.study;

import neuragenix.dao.*;
import neuragenix.common.*;

import java.util.*;
import java.io.*;
import java.sql.*;
import java.lang.StringBuffer;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;

/**
 *
 * @author  dmurley
 */
public class StudyAllocation {
    
    
    Hashtable htMarkedForUpdate = new Hashtable();
    private int intStudyKey = -1;
    
    /** Creates a new instance of StudyAllocation */
    public StudyAllocation() {
    }
    
    public StudyAllocation(String strStudyKey) {
        intStudyKey = Integer.parseInt(strStudyKey);
    }
    
    public StudyAllocation(int intStudyKey) {
        this.intStudyKey = intStudyKey;
    }
    
    public void finalize() {
        // release all the locks
    }
    
    public int getCurrentStudy() {
        return intStudyKey;
    }
    
    public boolean deleteSearch(int intAllocationKey) {
        DALQuery qryDeleteSearch = new DALQuery();
        boolean blQueryResult = false;
        
        try {
            qryDeleteSearch.setDomain("STUDY_ALLOCATION", null, null, null);
            qryDeleteSearch.setField("STUDY_ALLOCATION_intDeleted", "-1");
            qryDeleteSearch.setWhere(null, 0, "STUDY_ALLOCATION_intAllocationKey", "=", intAllocationKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            blQueryResult = qryDeleteSearch.executeUpdate();
        }
        catch (Exception e) {
            
            LogService.instance().log(LogService.ERROR, "[StudyAllocation-Delete (Critical)] Unable to update database\n" + e.getMessage(), e);
            System.err.println("[StudyAllocation-Delete (Critical)] Unable to update database - See log for details");
            return false;
        }
        
        return blQueryResult;
    }
    
    
    // needs to input the id of the search we wish to modify, update the search list
    public String addNewSearch(ChannelRuntimeData rd, boolean update) {
        DALQuery qryAddSearch = new DALQuery();
        boolean blQueryResult = false;
        Vector vtFormFields = DatabaseSchema.getFormFields("cstudy_addedit_allocation_search");
        String strErrors = null;
        
        
        
        
        try {
            
            
            
            qryAddSearch.setDomain("STUDY_ALLOCATION", null, null, null);
            
            rd.remove("STUDY_ALLOCATION_intStudyKey");
            rd.setParameter("STUDY_ALLOCATION_intStudyKey", intStudyKey + "");
            
            if ((strErrors = QueryChannel.validateData(vtFormFields, rd)) != null){
                System.out.println("Invalid Data when saving a new search "+ strErrors);
                return strErrors;
            }
            
            if ((strErrors = QueryChannel.checkRequiredFields(vtFormFields, rd)) != null){
                System.out.println("Required Fields must be filled in :" + strErrors);
                return strErrors;
            }
            qryAddSearch.setFields(vtFormFields, rd);
            
            
            
            if (update == true) {
                String allocKey = rd.getParameter("STUDY_ALLOCATION_intAllocationKey");
                if (allocKey != null) {
                    if (allocKey.equals("")) {
                        LogService.instance().log(LogService.ERROR, "[StudyAllocation-Update] Unable to update database - No Allocation Key in runtime data");
                        System.err.println("[StudyAllocation-Update] Unable to update database - No allocation key in runtime data");
                        
                        
                        return "[StudyAllocation-Update] Unable to update database - No allocation key in runtime data";
                    }
                    qryAddSearch.setWhere(null, 0, "STUDY_ALLOCATION_intAllocationKey", "=", allocKey, 0, DALQuery.WHERE_HAS_VALUE);
                    blQueryResult = qryAddSearch.executeUpdate();
                    if (blQueryResult == true) {
                        unmarkUpdate(Integer.parseInt(allocKey));
                    }
                }
                else {
                    LogService.instance().log(LogService.ERROR, "[StudyAllocation-Update] Unable to update database - No Allocation Key in runtime data");
                    System.err.println("[StudyAllocation-Update] Unable to update database - No allocation key in runtime data");
                    return "[StudyAllocation-Update] Unable to update database - No allocation key in runtime data";
                    
                }
            }
            else {
                blQueryResult = qryAddSearch.executeInsert();
            }
            if (blQueryResult == false)
                return "[Study Allocation] Errors when executing insert";
            else return null;
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "[StudyAllocation (Critical)] Unknown error has occurred" + e.getMessage(), e);
            System.err.println("[StudyAllocation (Critical)] Unknown error has occurred - See portal log for details");
            return "[StudyAllocation (Critical)] Unknown error has occurred - See portal log for details";
        }
        
        
        
    }
    
    
    
    
    // needs to input the id of the search we wish to modify update the search list
    
    // flags a row in the internal data set that we'll be updating
    public boolean markForUpdate(int intAllocationKey) {
        // lock record for updating
        // if unable to get lock, inform user.
        
        htMarkedForUpdate.put(String.valueOf(intAllocationKey), String.valueOf(intAllocationKey));
        return true;
    }
    
    public boolean unmarkUpdate(int intAllocationKey) {
        // remove locks
        htMarkedForUpdate.remove(String.valueOf(intAllocationKey));
        return true;
        
        
    }
    
    public boolean unmarkAll() {
        htMarkedForUpdate = new Hashtable();
        return true;
    }
    
    
    
    public boolean cancelUpdate() {
        // unlock record
        return false;
    }
    
    public String buildAddNewSearch(ChannelRuntimeData rd) {
        StringBuffer sbAdd = new StringBuffer();
        try {
            
            if (rd == null) {
                sbAdd.append(QueryChannel.buildLOVXMLFile("STUDY_ALLOCATION_strBiospecimenType", null));
                sbAdd.append(QueryChannel.buildLOVXMLFile("STUDY_ALLOCATION_strRequestedMeasure", null));
                sbAdd.append("<FIRSTADD>true</FIRSTADD>");
            }
            else {
                sbAdd.append(QueryChannel.buildLOVXMLFile("STUDY_ALLOCATION_strBiospecimenType", rd.getParameter("STUDY_ALLOCATION_strBiospecimenType")));
                sbAdd.append(updateLOVFromEdit(rd.getParameter("STUDY_ALLOCATION_strBiospecimenType"), ""));
                sbAdd.append(QueryChannel.buildLOVXMLFile("STUDY_ALLOCATION_strRequestedMeasure", rd.getParameter("STUDY_ALLOCATION_strRequestedMeasure")));
                sbAdd.append("<STUDY_ALLOCATION_intRequestedQuantity>" + rd.getParameter("STUDY_ALLOCATION_intRequestedQuantity") + "</STUDY_ALLOCATION_intRequestedQuantity>");
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return sbAdd.toString();
        
        
    }
    
    public String updateLOVFromEdit(String strParentValue, String strValue) {
        StringBuffer sbAdd = new StringBuffer();
        //System.out.println("strParentValue = " + strParentValue + "\tstrValue = " + strValue);
        try {
            sbAdd.append(QueryChannel.buildLOVXMLFromParent("STUDY_ALLOCATION_strBiospecimenSubType", strValue, strParentValue));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sbAdd.toString();
        
    }
    
    public String buildUpdateLOV(Hashtable fields) {
        StringBuffer sbAdd = new StringBuffer();
        Enumeration fieldNames = fields.keys();
        String fieldName;
        try {
            
            while (fieldNames.hasMoreElements()) {
                fieldName = (String) fieldNames.nextElement();
                
                sbAdd.append(QueryChannel.buildLOVXMLFile(fieldName, (String) fields.get(fieldName)));
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return sbAdd.toString();
        
        
    }
    
    
    public String buildSavedSearchList(int intStudyKey, int runtimeDataKey, ChannelRuntimeData rd) {
        
        StringBuffer sbSearchList = new StringBuffer();
        
        DALQuery qrySearchList = new DALQuery();
        DALQuery qryAllocatedList;
        
        
        ResultSet rsSearchList;
        ResultSet rsAllocatedList;
        
        Vector vtFormFields = DatabaseSchema.getFormFields("cstudy_view_allocation");
        Vector vtAllocationFields = DatabaseSchema.getFormFields("cstudy_search_biospecimen_transaction");
        Vector vtBiospecimenTypes = DatabaseSchema.getFormFields("cstudy_view_biospecimen_type");
        
        try {
            
            qrySearchList.setDomain("STUDY_ALLOCATION", null, null, null);
            
            qrySearchList.setFields(vtFormFields, null);
            
            qrySearchList.setWhere( null, 0, "STUDY_ALLOCATION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            qrySearchList.setWhere("AND", 0, "STUDY_ALLOCATION_intStudyKey", "=", Integer.toString(intStudyKey), 0, DALQuery.WHERE_HAS_VALUE);
            
            rsSearchList = qrySearchList.executeSelect();
            
            String strStudyKey;
            String strBiospecimenType;
            String strBiospecimenSubType;
            String strUnit;
            String strAllocationKey;
            
            
            while (rsSearchList.next()) {
                // get the study key
                strStudyKey = rsSearchList.getString("STUDY_ALLOCATION_intStudyKey");
                strBiospecimenType = rsSearchList.getString("STUDY_ALLOCATION_strBiospecimenType");
                strBiospecimenSubType = rsSearchList.getString("STUDY_ALLOCATION_strBiospecimenSubType");
                strUnit = rsSearchList.getString("STUDY_ALLOCATION_strRequestedMeasure");
                strAllocationKey = rsSearchList.getString("STUDY_ALLOCATION_intAllocationKey");
                
                // out the details of this search to this point
                sbSearchList.append("<SavedAllocationSearch>");
                
                int intUpdateKey = -2;
                if (htMarkedForUpdate.get(strAllocationKey) != null) {
                    Hashtable fieldsForLOV = new Hashtable();
                    sbSearchList.append("<editMode>true</editMode>");
                    
                    intUpdateKey = Integer.parseInt((String) htMarkedForUpdate.get(strAllocationKey));
                    
                    if (intUpdateKey == runtimeDataKey) {
                        
                        fieldsForLOV.put("STUDY_ALLOCATION_strBiospecimenType", rd.getParameter("STUDY_ALLOCATION_strBiospecimenType"));
                        fieldsForLOV.put("STUDY_ALLOCATION_strRequestedMeasure", rd.getParameter("STUDY_ALLOCATION_strRequestedMeasure"));
                        sbSearchList.append("<STUDY_ALLOCATION_LOVFields>");
                        sbSearchList.append(buildUpdateLOV(fieldsForLOV));
                        sbSearchList.append(updateLOVFromEdit(rd.getParameter("STUDY_ALLOCATION_strBiospecimenType"), ""));
                        sbSearchList.append("</STUDY_ALLOCATION_LOVFields>");
                    }
                    else {
                        
                        fieldsForLOV.put("STUDY_ALLOCATION_strBiospecimenType", strBiospecimenType);
                        fieldsForLOV.put("STUDY_ALLOCATION_strRequestedMeasure", strUnit);
                        sbSearchList.append("<STUDY_ALLOCATION_LOVFields>");
                        sbSearchList.append(buildUpdateLOV(fieldsForLOV));
                        sbSearchList.append(updateLOVFromEdit(strBiospecimenType, strBiospecimenSubType));
                        sbSearchList.append("</STUDY_ALLOCATION_LOVFields>");
                    }
                    
                }
                
                if (intUpdateKey == runtimeDataKey) {
                    sbSearchList.append("<STUDY_ALLOCATION_intAllocationKey>" + rd.getParameter("STUDY_ALLOCATION_intAllocationKey") + "</STUDY_ALLOCATION_intAllocationKey>");
                    sbSearchList.append("<STUDY_ALLOCATION_intRequestedQuantity>" + rd.getParameter("STUDY_ALLOCATION_intRequestedQuantity") + "</STUDY_ALLOCATION_intRequestedQuantity>");
                    sbSearchList.append("</SavedAllocationSearch>");
                }
                else {
                    
                    sbSearchList.append(QueryChannel.buildViewXMLForCurrentRecord(rsSearchList, vtFormFields));
                    
                    if (strStudyKey != null) {
                        // start a new query for biotransactions that will take a sum of the quantity fields
                        String strQueryChannelReturn = "";
                        String strAllocatedValue;
                        
                        qryAllocatedList = new DALQuery();
                        qryAllocatedList.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                        qryAllocatedList.setDomain("BIOSPECIMEN", "BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "INNER JOIN");
                        qryAllocatedList.setField("BIOSPECIMEN_TRANSACTIONS_strUnit", null);
                        
                        // transaction should only be for fields where the Biospecimen type and subtype match up
                        qryAllocatedList.setWhere( null, 0, "BIOSPECIMEN_TRANSACTIONS_intStudyKey", "=", strStudyKey, 0, DALQuery.WHERE_HAS_VALUE );
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_strSampleType", "=", strBiospecimenType, 0, DALQuery.WHERE_HAS_VALUE);
                       
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_strUnit", "=", strUnit, 0, DALQuery.WHERE_HAS_VALUE);
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_strStatus", "=", "Allocated", 0, DALQuery.WHERE_HAS_VALUE);
                        //qryAllocatedList.setWhere("OR", 0, "BIOSPECIMEN_TRANSACTIONS_strStatus", "=", "Delivered", 1, DALQuery.WHERE_HAS_VALUE);
                        
                        qryAllocatedList.setSumField("BIOSPECIMEN_TRANSACTIONS_flQuantity");
                        qryAllocatedList.setGroupBy("BIOSPECIMEN_strSampleType");
                        //qryAllocatedList.setGroupBy("BIOSPECIMEN_strSampleSubType");
                        qryAllocatedList.setGroupBy("BIOSPECIMEN_TRANSACTIONS_strUnit"); // TODO: Dont think i actually need this
                        
                        
                        qryAllocatedList.setCaseSensitive(false);
                        //System.out.println("[Study Location] Query 1 is: " + qryAllocatedList.convertSelectQueryToString());
                        rsAllocatedList = qryAllocatedList.executeSelect();
                        
                        
                        
                        if (rsAllocatedList.first() == false) {
                            strAllocatedValue = "0";
                        }
                        else {
                            strAllocatedValue = rsAllocatedList.getString("SUM_BSTN_flQuantity");
                        }
                        
                        sbSearchList.append("<AllocatedQuantity><SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity>" + strAllocatedValue + "</SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity></AllocatedQuantity>");
                        sbSearchList.append("<STUDY_ALLOCATION_strRequestedMeasure>");
                        sbSearchList.append(strUnit);
                        sbSearchList.append("</STUDY_ALLOCATION_strRequestedMeasure>");
                        
                        rsAllocatedList.close();
                        qryAllocatedList = new DALQuery();
                        qryAllocatedList.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                        qryAllocatedList.setDomain("BIOSPECIMEN", "BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "INNER JOIN");
                        qryAllocatedList.setField("BIOSPECIMEN_TRANSACTIONS_strUnit", null);
                        
                        // transaction should only be for fields where the Biospecimen type and subtype match up
                        qryAllocatedList.setWhere( null, 0, "BIOSPECIMEN_TRANSACTIONS_intStudyKey", "=", strStudyKey, 0, DALQuery.WHERE_HAS_VALUE );
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_strSampleType", "=", strBiospecimenType, 0, DALQuery.WHERE_HAS_VALUE);
                        
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_strUnit", "=", strUnit, 0, DALQuery.WHERE_HAS_VALUE);
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                        //qryAllocatedList.setWhere("AND", 1, "BIOSPECIMEN_TRANSACTIONS_strStatus", "=", "Allocated", 0, DALQuery.WHERE_HAS_VALUE);
                        qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_strStatus", "=", "Delivered",0, DALQuery.WHERE_HAS_VALUE);
                        
                        qryAllocatedList.setSumField("BIOSPECIMEN_TRANSACTIONS_flQuantity");
                        qryAllocatedList.setGroupBy("BIOSPECIMEN_strSampleType");
                        //qryAllocatedList.setGroupBy("BIOSPECIMEN_strSampleSubType");
                        qryAllocatedList.setGroupBy("BIOSPECIMEN_TRANSACTIONS_strUnit"); // TODO: Dont think i actually need this
                        
                        
                        qryAllocatedList.setCaseSensitive(false);
                        //System.out.println("[Study Location] Query 2 is: " + qryAllocatedList.convertSelectQueryToString());
                        rsAllocatedList = qryAllocatedList.executeSelect();
                        
                        
                        //String strAllocatedValue;
                        if (rsAllocatedList.first() == false) {
                            strAllocatedValue = "0";
                        }
                        else {
                            strAllocatedValue = rsAllocatedList.getString("SUM_BSTN_flQuantity");
                        }
                        /*
                        sbSearchList.append("<AllocatedQuantity><SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity>" + strAllocatedValue + "</SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity></AllocatedQuantity>");
                        sbSearchList.append("<STUDY_ALLOCATION_strRequestedMeasure>");
                        sbSearchList.append(strUnit);
                        sbSearchList.append("</STUDY_ALLOCATION_strRequestedMeasure>");
                        */
                        // output data into xml
                        
                        //qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_dtDeliveryDate", ">", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        //qryAllocatedList.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_dtDeliveryDate", "IS NOT NULL", "IS NOT NULL", 0, DALQuery.WHERE_HAS_NULL_VALUE);
                        
                        rsAllocatedList.close();
                        //System.out.println("[Study Location] Query is: " + qryAllocatedList.convertSelectQueryToString());
                        qryAllocatedList.setCaseSensitive(false);
                        //System.out.println("[Study Location] Query 2 is: " + qryAllocatedList.convertSelectQueryToString());
                        rsAllocatedList = qryAllocatedList.executeSelect();
                        rsAllocatedList.first();
                        
                        if (rsAllocatedList.first() == false) {
                            strAllocatedValue = "0";
                        }
                        else {
                            strAllocatedValue = rsAllocatedList.getString("SUM_BSTN_flQuantity");
                        }
                        rsAllocatedList.close();
                        
                        // output data into xml
                                        
                        sbSearchList.append("<DeliveredQuantity><SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity>" + strAllocatedValue + "</SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity></DeliveredQuantity>");
                        
                        
                        
                        sbSearchList.append("</SavedAllocationSearch>");
                        
                        // store this value, and rerun the query this time looking for anything that has a delivery date
                        
                    }
                    else {
                        LogService.instance().log(LogService.ERROR, "[StudyAllocation (Non Critical)] Invalid Study key passed to build search list");
                        System.err.println("[StudyAllocation (Non Critical)] Invalid Study key passed to build search");
                    }
                }
            }
            rsSearchList.close();
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "[StudyAllocation (Critical)] Unknown Error has occured" + e.getMessage(), e);
            System.err.println("[StudyAllocation (Critical)] Unknown error has occurred - See portal log for details");
        }
        
        // Search for saved searches
        
        // calculate allocated units value ( do this later)
        
        
        
        
        // calculate delivered units value ( do this later)
        
        return sbSearchList.toString();
    }
    
    
}
