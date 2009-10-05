/*
 * QuantityAllocator.java
 *
 * Created on 26 February 2005, 14:24
 *
 *
 * Purpose : This class integrates with the Intelligent Search class.
 * It allows a user to search through the biospecimens for available quantities and
 * then enter a transaction into each of these specimens for the quantity selected
 *
 *
 *
 */

package neuragenix.bio.biospecimen;

import neuragenix.dao.*;
import neuragenix.security.*;
import neuragenix.common.*;
import neuragenix.genix.isearch.IntelligentSearch;
import org.jasig.portal.ChannelStaticData;   
import org.jasig.portal.ChannelRuntimeData; 
import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.StringBuffer;
import java.sql.*;


/**
 *
 * @author  dmurley
 */
public class QuantityAllocator {
  
    /**
     * Mode for the module to run in -- Change to allocated
     */    
    public static final int CHANGE_TO_ALLOCATED = 1;
    /**
     * Mode to run in - Delivered
     */    
    public static final int CHANGE_TO_DELIVERED = 2;
    
    
    /**
     * Stylesheet for allocation mode selection
     */    
    public static final String XSL_DISPLAY_ALLOCMODE_SELECTION = "bp_quantity_allocmode";
    /**
     * Stylesheet for confirmation and addtional details
     */    
    public static final String XSL_DISPLAY_ALLOC_CONFIRMATION = "bp_quantity_confirmAlloc";
    /**
     * Final stylesheet
     */    
    public static final String XSL_DISPLAY_ALLOC_FINISH = "bp_quantity_finishAlloc";
    
    private int intAllocationType = -1;
    
    private IntelligentSearch iSearch = null;
    
    private Hashtable htDataFields = null;
    
    private AuthToken authToken;
    
    /**
     * Failure code
     */    
    public static final int ALLOCATION_FAILED = -1;
    /**
     * Success Code
     */    
    public static final int ALLOCATION_SUCCESS = 0;
    
    /**
     * Default constructor -- not to be used
     */
    public QuantityAllocator() 
    {
        
    }
    
    
    // required to run isearch correctly
    /**
     * Constructor for QuantityAllocator
     * @param sd ChannelStaticData -- Passed through to the search module
     */    
    public QuantityAllocator(ChannelStaticData sd)
    {
        iSearch = new IntelligentSearch(sd);
        iSearch.setFinishButtonVisible(true, "Complete Allocation &gt;");
        iSearch.setPickerMode(true);
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
    }
    
    public void setAllocationType(int intAllocationType)
    {
        this.intAllocationType = intAllocationType;
    }
    
    /*
     *  setISearchCritera
     *
     *  -- Automatically sets readonly/forced criteria for the isearch object
     *  -- which is very noice! (yes , pronounced with the 'o')
     */
    public void setISearchCriteria()
    {
        switch (intAllocationType)
        {
            case CHANGE_TO_ALLOCATED:
                // lock the biospecimen domain
                iSearch.setForcedDomain("BIOSPECIMEN");
                
                // lock biospecimen ID and collected fields
                iSearch.setForcedField("BIOSPECIMEN", "BIOSPECIMEN_strBiospecimenID");
                
                // quantity collected
                iSearch.setForcedField("BIOSPECIMEN", "BIOSPECIMEN_flNumberCollected");
                
                
                // TODO: Need a function here to automatically expand the domains
                // TODO: Locked fields should lose thier checkbox, replace with a padlock icon
                // TODO: Locked domains should have "(locked)" or something next to them, or a notice or such
                // TODO: Locked criteria should have "edit" and "delete" replaced with padlock icons
                
            break;
                
            case CHANGE_TO_DELIVERED:
                iSearch.setForcedDomain("BIOSPECIMEN");
                iSearch.setForcedDomain("BIOSPECIMEN_TRANSACTIONS");
                iSearch.setForcedField("BIOSPECIMEN", "BIOSPECIMEN_strBiospecimenID");
                iSearch.setForcedField("BIOSPECIMEN_TRANSACTIONS", "BIOSPECIMEN_TRANSACTIONS_intBioTransactionID");
                
                //TODO: need a proper way of locking in criteria... mine isnt going to work at this stage
                
            break;
                
        }
            
       
    }
    public String getISearchStylesheet()
    {
        return iSearch.getStylesheet();
    }
    
    public String doISearch(ChannelRuntimeData rd)
    {
        iSearch.processRuntimeData(rd);
        return iSearch.getCurrentXML();
    }
    
    public void setISearchFlags(ChannelRuntimeData rd)
    {
        iSearch.updateFlags(rd);
    }
    
    public String getSelectedAllocationXML()
    {
        StringBuffer strXML = new StringBuffer();
        Hashtable htFlags = iSearch.getFlaggedData("BIOSPECIMEN_strBiospecimenID");
        
        Enumeration e = htFlags.keys();
        
        while(e.hasMoreElements())
        {
            strXML.append("<AllocationData>");
            strXML.append("<BiospecimenID>");
            strXML.append(e.nextElement());
            strXML.append("</BiospecimenID>");
            strXML.append("</AllocationData>");
        }
        
        return strXML.toString();        
    }
    
    public int getAllocationType()
    {
        return intAllocationType;
    }
    
    public int doAllocation()
    {
        // TODO :: Support transactions in DALSecQuery
        // DALSecurityQuery query = new DALSecurityQuery("quantity_allocation", authToken);
        
        DALQuery query = new DALQuery();
        query.setManualCommit(true);
        
        Hashtable htFlagHandle = null;
        Enumeration e = null;
        
        // if the mode is allocation, data will be the study ID
        
        // if the mode is delivery, data will be the delivery date
        
        try
        {
            switch (getAllocationType())
            {
                case CHANGE_TO_ALLOCATED:
                    // TODO: LOCK THE TABLE

                    // Start transaction mode
                    // execute updates on all the biospecimens we're interested in
                    // to move thier available allocation to removed
                    // then place a new entry in the biotransactions

                    htFlagHandle = iSearch.getFlaggedData("BIOSPECIMEN_strBiospecimenID");
                    e = htFlagHandle.keys();
                    String strBiospecimenKey = null;
                    ResultSet rs = null;
                    // get the current available and removed
                    String strOwner = authToken.getUserIdentifier();
                    String strReason = "Batch Allocation from User : " + authToken.getUserIdentifier() + " - " + Utilities.getDateTimeStampAsString("dd/MM/yy");
                    
                    Hashtable htBiospecimenKeys = new Hashtable();
                    
                    Enumeration e2 = htFlagHandle.keys();
                    
                    while (e2.hasMoreElements())
                    {
                        strBiospecimenKey = (String) e2.nextElement();
                        int internalKey = getBiospecimenKey(strBiospecimenKey);
                        
                        htBiospecimenKeys.put(strBiospecimenKey, internalKey + "");
                    }
                    
                    
                    while (e.hasMoreElements())
                    {
                        strBiospecimenKey = (String) e.nextElement();
                        int internalKey = Integer.parseInt( (String) htBiospecimenKeys.get(strBiospecimenKey));
                        
                        query.reset();
                        query.setDomain("BIOSPECIMEN", null, null, null);
                        query.setField("BIOSPECIMEN_flNumberCollected", null);
                        query.setField("BIOSPECIMEN_flNumberRemoved", null);
                        query.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND", 0, "BIOSPECIMEN_strBiospecimenID", "=", strBiospecimenKey, 0, DALQuery.WHERE_HAS_VALUE);
                        rs = query.executeSelect();
                        if (rs.first())
                        {
                            int intAvailableQuantity = rs.getInt("BIOSPECIMEN_flNumberCollected");
                            int intRemovedQuantity = rs.getInt("BIOSPECIMEN_flNumberRemoved");

                            intRemovedQuantity = intRemovedQuantity + intAvailableQuantity;

                            query.clearFields();
                            query.setField("BIOSPECIMEN_flNumberCollected", "0");
                            query.setField("BIOSPECIMEN_flNumberRemoved", intRemovedQuantity + "");
                            query.executeUpdate();
                            
                            query.reset();
                            query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                            query.setField("BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", internalKey + "");
                            query.setField("BIOSPECIMEN_TRANSACTIONS_flQuantity", intAvailableQuantity + "");
                            query.setField("BIOSPECIMEN_TRANSACTIONS_strReason", strReason);
                            query.setField("BIOSPECIMEN_TRANSACTIONS_strOwner", strOwner);
                            query.setField("BIOSPECIMEN_TRANSACTIONS_strRecordedBy", strOwner);
                            query.setField("BIOSPECIMEN_TRANSACTIONS_strStatus", "Allocated");
                            query.setField("BIOSPECIMEN_TRANSACTIONS_dtTransactionDate", Utilities.getDateTimeStampAsString("dd/MM/yyyy"));

                            Enumeration enumData = htDataFields.keys();
                            while (enumData.hasMoreElements())
                            {
                                String strDataField = (String) enumData.nextElement();
                                String strDataValue = (String) htDataFields.get(strDataField);
                                query.setField(strDataField, strDataValue);
                            }

                            boolean result = query.executeInsert();
                            
                            if (result == false)
                            {
                                query.cancelTransaction();
                                return ALLOCATION_FAILED;
                            }
                            
                        }
                        rs.close(); // shutdown the resultset
                        
                    }
                    
                    // reached this point == success

                    // TODO : Attach a key to declare its all finished
                    
                    query.commitTransaction();
                    return ALLOCATION_SUCCESS;

                // break;


                case CHANGE_TO_DELIVERED:
                    // TODO: LOCK THE TABLE
                    
                    htFlagHandle = iSearch.getFlaggedData("BIOSPECIMEN_TRANSACTIONS_intBioTransactionID");
                    e = htFlagHandle.keys();
                    // cycle through all the biotransaction ids and mark as completed
                   
                    
                    query.reset();
                    query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                    query.setField("BIOSPECIMEN_TRANSACTIONS_strStatus", "Delivered");
                    
                    Enumeration enumData = htDataFields.keys();
                    while (enumData.hasMoreElements())
                    {
                        String strDataField = (String) enumData.nextElement();
                        String strDataValue = (String) htDataFields.get(strDataField);
                        
                        query.setField(strDataField, strDataValue);
                    }
                    
                    
                    
                    // enumeration here
                    while (e.hasMoreElements())
                    {
                        String strTransactionID = (String) e.nextElement();
                        query.clearWhere();
                        query.setWhere(null, 0, "BIOSPECIMEN_TRANSACTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_intBioTransactionID", "=", strTransactionID, 0, DALQuery.WHERE_HAS_VALUE);
                        boolean result = query.executeUpdate();
                        if (result == false)
                        {
                            query.cancelTransaction();
                            return ALLOCATION_FAILED;
                        }
                    
                    }
                    
                    // reach this point -- everything is good... commit the transaction
                    
                    query.commitTransaction();
                    return ALLOCATION_SUCCESS;

            }
        }
        
        catch (Exception ex)
        {
            System.err.println ("There was an error doing the allocation");
            ex.printStackTrace(System.err);
        }
        return ALLOCATION_FAILED;
    }
    
    public void setDataFields(Hashtable fields)
    {
        this.htDataFields = fields;
    }
    
    
    public int getBiospecimenKey(String strBiospecimenID)
    {
        DALQuery query = new DALQuery();
        ResultSet rs = null;
        try
        {
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_intBiospecimenID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_strBiospecimenID", "=", strBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            int intReturnValue = -1;
            if (rs.first())
            {
                intReturnValue = rs.getInt("BIOSPECIMEN_intBiospecimenID");
            }
            rs.close();
            return intReturnValue;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            return -1;
        }
    }
    
}
