/*
 * InventoryHistory.java
 *
 * Created on 13 June 2005, 12:18
 */

package neuragenix.bio.inventory;

import java.sql.ResultSet;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Calendar;
import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.bio.utilities.InventoryUtilities;
import neuragenix.common.*;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

/**
 *
 * @author  navin
 */
public class InventoryHistory 
{
    
    private static final String strBIOSPECIMEN = "CELL_intBiospecimenID";
    private static final String strOLDCELL = "CELL_intOldCellID";
    private static final String strNEWCELL = "CELL_intNewCellID";
   
    // Following static final strings used to define the location of the system
    public static final String SYSTEM_TIMEZONE = PropertiesManager.getProperty("neuragenix.genix.timezone");
    public static final String SYSTEM_LANGUAGE = PropertiesManager.getProperty("neuragenix.genix.languageCode");
    public static final String SYSTEM_COUNTRY = PropertiesManager.getProperty("neuragenix.genix.countryCode");
    
    /** Creates a new instance of InventoryHistory */
    public InventoryHistory() {
    }
    
    public static void addHistory(Vector vtNewHistoryDetails, AuthToken authToken)
    {
        //Simulate a call  - do not do this if getting called from a valid function
        //vtNewHistoryDetails = simulateSetupData();
        
        Hashtable hshRecord = new Hashtable();
        DALQuery qryInsertHistory = new DALQuery ();
        DALQuery qryInsertCellHistory = new DALQuery ();
        // Define the Timezone
        java.util.TimeZone tz = java.util.TimeZone.getTimeZone(SYSTEM_TIMEZONE);
        // Define the locality information
        java.util.Locale loc = new java.util.Locale(SYSTEM_LANGUAGE, SYSTEM_COUNTRY);
        // Setup the calender
        java.util.Calendar rightNow = java.util.Calendar.getInstance(tz,loc);
        String historyDate = new String();
        String historyKey = new String();
        
        try
        {            
            // Define the date based on the current day
            historyDate = String.valueOf(rightNow.get(java.util.Calendar.DAY_OF_MONTH)) + "/" + String.valueOf((rightNow.get(java.util.Calendar.MONTH)+1)) + "/" + String.valueOf(rightNow.get(java.util.Calendar.YEAR));
            //System.out.println ("date= " + QueryChannel.makeCorrectDateFormat(historyDate));
            //System.out.println ("user= " + authToken.getUserIdentifier());
            
            // Add data to history
            qryInsertHistory.reset();
            qryInsertHistory.setDomain("INV_HISTORY", null, null, null);
            qryInsertHistory.setField("INV_HISTORY_dtHistory", QueryChannel.makeCorrectDateFormat(historyDate));
            qryInsertHistory.setField("INV_HISTORY_strUser", authToken.getUserIdentifier().toString());
            
            if (qryInsertHistory.executeInsert()) // if insert succeeds
            {
                // Obtain the historyKey added
                historyKey = QueryChannel.getNewestKeyAsString(qryInsertHistory);
                
                if ((historyKey != null) && (historyKey.length()!=0))
                {
                    
                    // Iterate through the vector
                    for (Enumeration e = vtNewHistoryDetails.elements(); e.hasMoreElements();) 
                    {
                        // Add the inidividual cells to the query
                        qryInsertCellHistory.reset();
                        qryInsertCellHistory.setDomain("INV_CELL_HISTORY", null, null, null);
                        
                        // Obtain the hshRecord from the vector
                        hshRecord.clear();
                        hshRecord = (Hashtable) e.nextElement();
                        
                        // Iterate through the hashtable
                        for (Enumeration f = hshRecord.keys(); f.hasMoreElements();)
                        {
                            String key = (String) f.nextElement();
                            String value = (String) hshRecord.get(key); 
                            if ((key!=null) && (key.equalsIgnoreCase(strBIOSPECIMEN)))
                            {
                                qryInsertCellHistory.setField("INV_CELL_HISTORY_intSampleID", value);
                            }
                            
                            if ((key!=null) && (key.equalsIgnoreCase(strOLDCELL)))
                            {
                                qryInsertCellHistory.setField("INV_CELL_HISTORY_intOldCell", value);
                            }
                            
                            if ((key!=null) && (key.equalsIgnoreCase(strNEWCELL)))
                            {
                                qryInsertCellHistory.setField("INV_CELL_HISTORY_intNewCell", value);
                            }
                            
                            //System.out.println ("\t Key:" + key + "\tValue = " + value);
                        }
                        qryInsertCellHistory.setField("INV_CELL_HISTORY_intHistoryKey", historyKey);
                        
                        //System.out.println ("insert qry = " + qryInsertCellHistory.convertSelectQueryToString());
                        
                        // perform the insert
                        if (!qryInsertCellHistory.executeInsert())
                        {
                            System.out.println ("InventoryHistory:addHistory] Failure to insert cells!!");
                        }
                        /*else
                        {
                            System.out.println ("InventoryHistory:addHistory] Successful insert cells!!");
                        }*/
                    }
                }
            }

        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "InventoryHistory:addHistory] " + e.toString(), e);
            e.printStackTrace();
        }
    }
    
    private static Vector simulateSetupData()
    {
        Vector vtTemp = new Vector();
        
        try
        {
            for (int i=0; i<4; i++)
            {
                Hashtable hshTemp = new Hashtable();
                hshTemp.put(strBIOSPECIMEN, "122");
                hshTemp.put(strOLDCELL, "123");
                hshTemp.put(strNEWCELL, "124");
                
                vtTemp.add(i, hshTemp);
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "InventoryHistory:simulate] " + e.toString(), e);
            e.printStackTrace();
        }
        return vtTemp;
    }
    
    public static StringBuffer buildHistory(AuthToken authToken)
    {
        StringBuffer sbHistory = new StringBuffer();
        DALQuery qryHistory = new DALQuery ();
        String historyKey = new String();
        
        try
        {
            // retrieve history data
            qryHistory.reset();
            qryHistory.setDomain("INV_HISTORY", null, null, null);
            qryHistory.setField("INV_HISTORY_intHistoryKey", null);
            qryHistory.setField("INV_HISTORY_dtHistory", null);
            qryHistory.setField("INV_HISTORY_strUser", null);
            qryHistory.setOrderBy("INV_HISTORY_intHistoryKey", "ASC");
            
            //System.out.println("qryHistory: " + qryHistory.convertSelectQueryToString());
            ResultSet rsOldInvHistory = qryHistory.executeSelect();
            
            // for all the history records
            while (rsOldInvHistory.next())
            {
                historyKey = rsOldInvHistory.getString("INV_HISTORY_intHistoryKey");
                
                sbHistory.append("<history>");
                sbHistory.append("<intHistoryID>").append(historyKey).append("</intHistoryID>");
                sbHistory.append("<dtHistory>").append(rsOldInvHistory.getString("INV_HISTORY_dtHistory")).append("</dtHistory>");
                sbHistory.append("<strUser>").append(rsOldInvHistory.getString("INV_HISTORY_strUser")).append("</strUser>");
                
                // get the cells moved for this history record
                sbHistory.append(getHistoryCellData(historyKey,authToken));
                sbHistory.append("</history>");
            }
            
            rsOldInvHistory.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "InventoryHistory:buildHistory] " + e.toString(), e);
            e.printStackTrace();
        }
        
        return sbHistory;
    }
     
    private static StringBuffer getHistoryCellData(String historyKey, AuthToken authToken)
    {
        DALQuery qryCellHistory = new DALQuery ();
        StringBuffer sbCellHistory = new StringBuffer();
        int cellKey;
        
        try
        {
            qryCellHistory.reset();
            qryCellHistory.setDomain("INV_CELL_HISTORY", null, null, null);
            qryCellHistory.setDomain("BIOSPECIMEN", "INV_CELL_HISTORY_intSampleID", "BIOSPECIMEN_intBiospecimenID", "INNER JOIN");
            qryCellHistory.setField("BIOSPECIMEN_strBiospecimenID", null);
            qryCellHistory.setField("INV_CELL_HISTORY_intOldCell", null);
            qryCellHistory.setField("INV_CELL_HISTORY_intNewCell", null);
            qryCellHistory.setWhere(null,  0, "INV_CELL_HISTORY_intHistoryKey",  "=", historyKey,  0, DALQuery.WHERE_HAS_VALUE);
            
            //System.out.println ("Sub qry: " + qryCellHistory.convertSelectQueryToString());
            ResultSet rsInvCellHistory = qryCellHistory.executeSelect();
            
            while (rsInvCellHistory.next())
            {
                // generate XML for old cells
                sbCellHistory.append("<cell>").append("<strSampleID>");
                sbCellHistory.append(rsInvCellHistory.getString("BIOSPECIMEN_strBiospecimenID"));
                sbCellHistory.append("</strSampleID>").append("<old>");
                cellKey = Integer.parseInt(rsInvCellHistory.getString("INV_CELL_HISTORY_intOldCell"));
                String strOldCells [] = InventoryUtilities.getBiospecimenLocation("inventory", cellKey, authToken);
                
                sbCellHistory.append("<SITE_strSiteName>").append(strOldCells[InventoryUtilities.SITENAME]).append("</SITE_strSiteName>");
                sbCellHistory.append("<TANK_strTankName>").append(strOldCells[InventoryUtilities.TANKNAME]).append("</TANK_strTankName>");
                sbCellHistory.append("<BOX_strBoxName>").append(strOldCells[InventoryUtilities.BOXNAME]).append("</BOX_strBoxName>");
                sbCellHistory.append("<TRAY_strTrayName>").append(strOldCells[InventoryUtilities.TRAYNAME]).append("</TRAY_strTrayName>");
                sbCellHistory.append("<CELL_strCellName>").append(strOldCells[InventoryUtilities.ROW_NUMBER].toUpperCase());
                sbCellHistory.append(strOldCells[InventoryUtilities.COLUMN_NUMBER].toUpperCase()).append("</CELL_strCellName>");
                sbCellHistory.append("</old>");
                
                
                // generate XML for new cells
                sbCellHistory.append("<new>");
                cellKey = Integer.parseInt(rsInvCellHistory.getString("INV_CELL_HISTORY_intNewCell"));
                String strNewCells [] = InventoryUtilities.getBiospecimenLocation("inventory", cellKey, authToken);
                
                sbCellHistory.append("<SITE_strSiteName>").append(strNewCells[InventoryUtilities.SITENAME]).append("</SITE_strSiteName>");
                sbCellHistory.append("<TANK_strTankName>").append(strNewCells[InventoryUtilities.TANKNAME]).append("</TANK_strTankName>");
                sbCellHistory.append("<BOX_strBoxName>").append(strNewCells[InventoryUtilities.BOXNAME]).append("</BOX_strBoxName>");
                sbCellHistory.append("<TRAY_strTrayName>").append(strNewCells[InventoryUtilities.TRAYNAME]).append("</TRAY_strTrayName>");
                sbCellHistory.append("<CELL_strCellName>").append(strNewCells[InventoryUtilities.ROW_NUMBER].toUpperCase());
                sbCellHistory.append(strNewCells[InventoryUtilities.COLUMN_NUMBER].toUpperCase()).append("</CELL_strCellName>");
                sbCellHistory.append("</new>").append("</cell>");
            }
            rsInvCellHistory.close();
                    
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "InventoryHistory:getHistoryCellData] " + e.toString(), e);
            e.printStackTrace();
        }
        
        return sbCellHistory;
    }
}
