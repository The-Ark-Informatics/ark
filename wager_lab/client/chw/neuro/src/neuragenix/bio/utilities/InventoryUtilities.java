/*
 * InventoryUtilities.java
 *
 * Created on 16 March 2005, 11:13
 */

package neuragenix.bio.utilities;

/**
 *
 * @author  dmurley
 */

import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.security.AuthToken;
import neuragenix.utils.NeuroUtils;

import java.sql.ResultSet;
import org.jasig.portal.services.LogService;
import neuragenix.bio.biospecimen.BiospecimenCore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.jasig.portal.PropertiesManager;

public class InventoryUtilities
{
   //use this keys in a tray  as a place holder for a biospecimen;
   public static final int PLACEHOLDER = -3;
   public static final int SITENAME = 0;
   public static final int TANKNAME = 1;
   public static final int BOXNAME = 2;
   public static final int TRAYNAME = 3;
   public static final int COLUMN_NUMBER = 4;
   public static final int ROW_NUMBER = 5;
   public static final int CELL_ID= 6;
   public static final int TRAY_ID=7;
   
   private static final int LOCATION_RETURN_VALUES = 8;
   
   private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
   
   /** Creates a new instance of InventoryUtilities */
   public InventoryUtilities()
   {
   }
   
   
   public static int getCellKey(int intTrayKey, int intColNo, int intRowNo)
   {
      DALQuery query = new DALQuery();
      
      try
      {
         query.setDomain("TRAY", null, null, null);
         query.setDomain("CELL", "TRAY_intTrayID", "CELL_intTrayID", "INNER JOIN");
         query.setField("CELL_intCellID", null);
         query.setWhere(null, 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TRAY_intTrayID", "=", intTrayKey + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "CELL_intColNo", "=", intColNo + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "CELL_intRowNo", "=", intRowNo + "", 0, DALQuery.WHERE_HAS_VALUE);
         
         ResultSet rs = query.executeSelect();
         
         int intReturnValue = 0;
         if (rs.first())
         {
            intReturnValue = rs.getInt(1);
         }
         
         rs.close();
         rs = null;
         return intReturnValue;
         
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return -1;
      }
   }
   
   
   public static int getTrayKey(String strSiteName, String strTankName, String strBoxName, String strTrayName)
   {
      DALQuery query = new DALQuery();
     
      
      try
      {
         query.setDomain("SITE", null, null, null);
         query.setDomain("TANK", "SITE_intSiteID", "TANK_intSiteID", "INNER JOIN");
         query.setDomain("BOX", "TANK_intTankID", "BOX_intTankID", "INNER JOIN");
         query.setDomain("TRAY", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");

         query.setField ("TRAY_intTrayID", null);
         query.setWhere(null, 0, "SITE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TANK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "BOX_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "SITE_strSiteName", "=", strSiteName, 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TANK_strTankName", "=", strTankName, 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "BOX_strBoxName", "=", strBoxName, 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TRAY_strTrayName", "=", strTrayName, 0, DALQuery.WHERE_HAS_VALUE);

         
         //System.out.println ("Get Tray Key Query : " + query.convertSelectQueryToString());
         ResultSet rs = query.executeSelect();

         int keyValue = -1;
         if (rs.first())
         {
            keyValue = rs.getInt(1);
         }

         if (rs.next())
         {
            System.err.println("[InventoryUtilities] Warning : Identical paths found - can't locate a specific tray");
         }
         rs.close();
         rs = null;
         return keyValue;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return -1;
      }
         
   }
   
   public static String[] getBiospecimenLocation(String strSource, int intCellOrBiospecimenID, AuthToken authToken){
   
   	return getBiospecimenLocation(strSource,intCellOrBiospecimenID, authToken, null);
   
   }
   
  
   
   public static String[] getBiospecimenLocation(String strSource, int intCellOrBiospecimenID, AuthToken authToken, DALSecurityQuery query)
   {
      String[] strReturnValue = new String[LOCATION_RETURN_VALUES];
      
      for (int i = 0; i < strReturnValue.length-1; i++)
      {
         strReturnValue[i] = "N/A";
      }
      
      strReturnValue[strReturnValue.length-1] ="-1";
      
      try
      {  
      	 
         if (query == null){
         	 query = new DALSecurityQuery("inventory_view", authToken);
         }
         
         query.reset();
         if (strSource.equals("inventory"))
         {
            
            query.setDomain("CELL", null, null, null);
            query.setDomain("TRAY", "CELL_intTrayID", "TRAY_intTrayID", "LEFT JOIN");
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "LEFT JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "LEFT JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "LEFT JOIN");
            
            query.setWhere(null, 0, "CELL_intCellID", "=", intCellOrBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
            
            
         }
         else
         { // if source equals biospecimen channel
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setDomain("CELL", "BIOSPECIMEN_intCellID", "CELL_intCellID", "INNER JOIN");
            query.setDomain("TRAY", "CELL_intTrayID", "TRAY_intTrayID", "LEFT JOIN");
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "LEFT JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "LEFT JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "LEFT JOIN");
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intCellOrBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
         }
         
         query.setField("SITE_strSiteName", null);
         query.setField("TANK_strTankName", null);
         query.setField("BOX_strBoxName", null);
         query.setField("TRAY_strTrayName", null);
         query.setField("TRAY_strRowNoType", null);
         query.setField("TRAY_strColNoType", null);
         query.setField("CELL_intCellID",null);
         query.setField("CELL_intRowNo", null);
         query.setField("CELL_intColNo", null);
         query.setField("TRAY_intTrayID",null);
         
         ResultSet rsResultSet = query.executeSelect();
         
         if (rsResultSet.next())
         {
            strReturnValue[SITENAME] = rsResultSet.getString("SITE_strSiteName");
            strReturnValue[TANKNAME] = rsResultSet.getString("TANK_strTankName");
            strReturnValue[BOXNAME] = rsResultSet.getString("BOX_strBoxName");
            strReturnValue[TRAYNAME] = rsResultSet.getString("TRAY_strTrayName");
            strReturnValue[TRAY_ID] = rsResultSet.getString("TRAY_intTrayID");
            String strRowNoType = rsResultSet.getString("TRAY_strRowNoType");
            String strColNoType = rsResultSet.getString("TRAY_strColNoType");
            int intRowNo = rsResultSet.getInt("CELL_intRowNo");
            int intColNo = rsResultSet.getInt("CELL_intColNo");
            if (strRowNoType.equals("Alphabet"))
               strReturnValue[ROW_NUMBER] = ALPHABET.substring(intRowNo-1, intRowNo);
            else
               strReturnValue[ROW_NUMBER] = "" + intRowNo;
            
            if (strColNoType.equals("Alphabet"))
               strReturnValue[COLUMN_NUMBER] = ALPHABET.substring(intColNo-1, intColNo);
            else
               strReturnValue[COLUMN_NUMBER] = "" + intColNo;
            if (rsResultSet.getString("CELL_intCellID") != null)
               strReturnValue[CELL_ID] = rsResultSet.getString("CELL_intCellID");
         }
         rsResultSet.close();
      }
      catch(Exception e)
      {
         
         LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
      }
      return strReturnValue;
   }
   
   /**
    * Unallocate the biospecimen from a cell in the tray
    */
   public static boolean unallocateBiospecimenFromTray(int intBiospecimenID, AuthToken authToken)
   {
      
      //System.out.println ("WARNING!  THIS FUNCTION WILL... WILLL CORRUPT INVENTORY COUNTS.");
      //Exception jk = new Exception();
      //jk.printStackTrace();
      
      try
      {
         
         InventoryUtilities.updateInventoryWhenDeleteAndUnallocateBiospecimen(intBiospecimenID, authToken);
         DALSecurityQuery query = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_UPDATE, authToken);
         query.setDomain("BIOSPECIMEN", null, null, null);
         query.setField("BIOSPECIMEN_intCellID", "-1"); // deassociate the biospecimen from a cell
         query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID+"", 0, DALQuery.WHERE_HAS_VALUE);
         return query.executeUpdate();
         
         
         
      }
      catch(Exception e)
      {
         LogService.instance().log(LogService.ERROR, "CBiospecimen::unallocateBiospecimenFromTray() : Unexpected error ", e);
      }
      return false;
   }
   
   public static String getInventoryList()
   {
      DALQuery query = new DALQuery();
      ResultSet rs;
      
      try
      {
         query.setDomain("SITE", null, null, null);
         query.setDomain("TANK", "SITE_intSiteID", "TANK_intSiteID", "INNER JOIN");
         query.setDomain("BOX", "TANK_intTankID", "BOX_intTankID", "INNER JOIN");
         query.setDomain("TRAY", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
         
         query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_details_for_html"), null);
         query.setWhere(null, 0, "SITE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TANK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "BOX_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         
         rs = query.executeSelect();
         
         String strXML = QueryChannel.buildSearchXMLFile("InventoryLocations", rs, DatabaseSchema.getFormFields("cbiospecimen_view_inventory_details_for_html"));
         
         rs.close();
         return strXML;
         
      }
      catch (Exception e)
      {
         System.err.println("[CBiospecimen : getInventoryList() - Error occured when trying to Inventory details " + e.getMessage());
      }
      return "";
   }
   
   /**
    * Update Inventory by increasing the number of available cell by 1.
    * This is required in situations such as:
    * - Delete biospecimen which was previously allocated in a cell.
    * - Unallocate biospecimen which was previously allocated in a cell.
    */
   public static void updateInventoryWhenDeleteAndUnallocateBiospecimen(int intBiospecimenID, AuthToken authToken)
   {
      
      try
      {
         
         DALSecurityQuery query = new DALSecurityQuery("inventory_update", authToken);
         
         query.setDomain("BIOSPECIMEN", null, null, null);
         query.setField("BIOSPECIMEN_intCellID", null);
         query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rsResultSet = query.executeSelect();
         
         //System.out.println("BiospecimentID " +intBiospecimenID);
         String strInvCellID = "";
         if (rsResultSet.first())            
            strInvCellID = rsResultSet.getString("BIOSPECIMEN_intCellID");
         rsResultSet.close();
         
         // free-up the cell
         query.reset();
         query.setDomain("CELL", null, null, null);
         query.setField("CELL_intPatientID", "-1"); // reset the patient id
         query.setField("CELL_intBiospecimenID", "-1"); // resetthe biospecimen id
         query.setWhere(null, 0, "CELL_intCellID", "=", strInvCellID, 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
         //System.out.println("The Query : " + query.convertSelectQueryToString());
         //System.out.println("Query result : " + query.executeUpdate());
         
         // TRAY UPDATE
         // go up one level and get the tray id
         query.reset();
         query.setDomain("CELL", null, null, null);
         query.setField("CELL_intTrayID", null);
         query.setWhere(null, 0, "CELL_intCellID", "=", strInvCellID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the tray id
         String strTrayID = "-1";
         if (rsResultSet.next())
            strTrayID = rsResultSet.getString("CELL_intTrayID");
         
         rsResultSet.close();
         
         // get the available cell in the tray
         query.reset();
         query.setDomain("TRAY", null, null, null);
         query.setField("TRAY_intTrayAvailable", null);
         query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the available cell in the tray and increase it by 1
         int intTrayAvailable = 0;
         if (rsResultSet.next())
            intTrayAvailable = rsResultSet.getInt("TRAY_intTrayAvailable") + 1;
         
         rsResultSet.close();
         
         // set the increased available cell in the tray back to the DB
         query.reset();
         query.setDomain("TRAY", null, null, null);
         query.setField("TRAY_intTrayAvailable", new Integer(intTrayAvailable).toString());
         query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
         
         // BOX UPDATE
         // go up one level and get the box id
         query.reset();
         query.setDomain("TRAY", null, null, null);
         query.setField("TRAY_intBoxID", null);
         query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the box id
         String strBoxID = "-1";
         if (rsResultSet.next())
            strBoxID = rsResultSet.getString("TRAY_intBoxID");
         
         rsResultSet.close();
         
         // get the available cell in the box
         query.reset();
         query.setDomain("BOX", null, null, null);
         query.setField("BOX_intBoxAvailable", null);
         query.setWhere(null, 0, "BOX_intBoxID", "=", strBoxID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the available cell in the box and increase it by 1
         int intBoxAvailable = 0;
         if (rsResultSet.next())
            intBoxAvailable = rsResultSet.getInt("BOX_intBoxAvailable") + 1;
         
         rsResultSet.close();
         
         // set the increased available cell in the box back to the DB
         query.reset();
         query.setDomain("BOX", null, null, null);
         query.setField("BOX_intBoxAvailable", new Integer(intBoxAvailable).toString());
         query.setWhere(null, 0, "BOX_intBoxID", "=", strBoxID, 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
         
         // TANK UPDATE
         // go up one level and get the tank id
         query.reset();
         query.setDomain("BOX", null, null, null);
         query.setField("BOX_intTankID", null);
         query.setWhere(null, 0, "BOX_intBoxID", "=", strBoxID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the tank id
         String strTankID = "-1";
         if (rsResultSet.next())
            strTankID = rsResultSet.getString("BOX_intTankID");
         
         rsResultSet.close();
         
         // get the available cell in the tank
         query.reset();
         query.setDomain("TANK", null, null, null);
         query.setField("TANK_intTankAvailable", null);
         query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the available cell in the tank and increase it by 1
         int intTankAvailable = 0;
         if (rsResultSet.next())
            intTankAvailable = rsResultSet.getInt("TANK_intTankAvailable") + 1;
         
         rsResultSet.close();
         
         // set the increased available cell in the tank back to the DB
         query.reset();
         query.setDomain("TANK", null, null, null);
         query.setField("TANK_intTankAvailable", new Integer(intTankAvailable).toString());
         query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
      }
      catch(Exception e)
      {
         LogService.instance().log(LogService.ERROR, "CBiospecimen::updateInventoryWhenDeleteAndUnallocateBiospecimen() : Unexpected error ", e);
      }
   }
   
   
   public static boolean saveBiospecimenLocation(int intBiospecimenID, int intNewInvCellID, AuthToken authToken)
   {
      return saveBiospecimenLocation(intBiospecimenID, intNewInvCellID, authToken, null);
   }
   
   
   /**
    * to save the biospec location in a tray
    */
   public static boolean saveBiospecimenLocation(int intBiospecimenID, int intNewInvCellID, AuthToken authToken, DALSecurityQuery dsqQuery)
   {
      DALSecurityQuery query = null;
      
      
      
      try
      {   
          if (dsqQuery == null)
             query = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_UPDATE, authToken);
          else
             query = dsqQuery;
          
          query.reset();
          //only update intBiospecimen if valid biosID
          if (intBiospecimenID != PLACEHOLDER )
          {
              query.setDomain("BIOSPECIMEN", null, null, null);
              query.setField("BIOSPECIMEN_intCellID",  intNewInvCellID + ""); // associate the biospecimen to cell
              query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
              if (query.executeUpdate() == false)
              {
                  System.out.println("Was unable to save inventory data to database");
                  return false;
              }
          }
          updateInventoryWhenAllocateBiospecimen(intBiospecimenID, intNewInvCellID,  authToken,query);
          
          return true;
          
          
      }
      catch(Exception e)
      {
         LogService.instance().log(LogService.ERROR, "InventoryUtilities::saveBiospecLocation() : Unexpected error ", e);
         return false;
      }
      
   }
   
   public static void updateInventoryWhenAllocateBiospecimen(int intBiospecimenID, int intNewInvCellID, AuthToken authToken)
   {
      updateInventoryWhenAllocateBiospecimen(intBiospecimenID, intNewInvCellID, authToken, null);
   }
   /** update the inventory when a biospecimen get allocated
    *
    **/
   public static void updateInventoryWhenAllocateBiospecimen(int intBiospecimenID, int intNewInvCellID, AuthToken authToken, DALSecurityQuery dsqQuery)
   {
      DALSecurityQuery query = null;
      
      
      try
      {
         // DALSecurityQuery query = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_UPDATE, authToken);
         
         if (dsqQuery == null)
         {
            query = new DALSecurityQuery();
         }
         else
         {
            query = dsqQuery;
            query.reset();
         }
         
         // XXX: Why is this query here at all?
         
         query.setDomain("BIOSPECIMEN", null, null, null);
         query.setField("BIOSPECIMEN_intCellID", null);
         query.setField("BIOSPECIMEN_intPatientID", null);
         query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rsResultSet = query.executeSelect();

         String intPatientKey = "";
         if (rsResultSet.next())
         {    
             intPatientKey = rsResultSet.getString("BIOSPECIMEN_intPatientID");
         }
         rsResultSet.close();
         
         // update the cell
         query.reset();
         query.setDomain("CELL", null, null, null);
         //query.setField("CELL_intPatientID", rsResultSet.getString("BIOSPECIMEN_intPatientID")); // set the patient id
         query.setField("CELL_intBiospecimenID", intBiospecimenID+""); // set the biospecimen id
         query.setField("CELL_intPatientID", intPatientKey);
         query.setWhere(null, 0, "CELL_intCellID", "=", intNewInvCellID+"", 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
         
         // TRAY UPDATE
         // go up one level and get the tray id
         query.reset();
         query.setDomain("CELL", null, null, null);
         query.setField("CELL_intTrayID", null);
         query.setWhere(null, 0, "CELL_intCellID", "=", intNewInvCellID+"", 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the tray id
         String strTrayID = "-1";
         if (rsResultSet.next())
            strTrayID = rsResultSet.getString("CELL_intTrayID");
         
         rsResultSet.close();
         
         // get the available cell in the tray
         query.reset();
         query.setDomain("TRAY", null, null, null);
         query.setField("TRAY_intTrayAvailable", null);
         query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the available cell in the tray and decrease it by 1
         int intTrayAvailable = 0;
         if (rsResultSet.next())
            intTrayAvailable = rsResultSet.getInt("TRAY_intTrayAvailable") - 1;
         
         rsResultSet.close();
         
         // set the decreased available cell in the tray back to the DB
         query.reset();
         query.setDomain("TRAY", null, null, null);
         query.setField("TRAY_intTrayAvailable", new Integer(intTrayAvailable).toString());
         query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
         
         // BOX UPDATE
         // go up one level and get the box id
         query.reset();
         query.setDomain("TRAY", null, null, null);
         query.setField("TRAY_intBoxID", null);
         query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the box id
         String strBoxID = "-1";
         if (rsResultSet.next())
            strBoxID = rsResultSet.getString("TRAY_intBoxID");
         
         rsResultSet.close();
         
         // get the available cell in the box
         query.reset();
         query.setDomain("BOX", null, null, null);
         query.setField("BOX_intBoxAvailable", null);
         query.setWhere(null, 0, "BOX_intBoxID", "=", strBoxID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the available cell in the box and decrease it by 1
         int intBoxAvailable = 0;
         if (rsResultSet.next())
            intBoxAvailable = rsResultSet.getInt("BOX_intBoxAvailable") - 1;
         
         rsResultSet.close();
         
         // set the decreased available cell in the box back to the DB
         query.reset();
         query.setDomain("BOX", null, null, null);
         query.setField("BOX_intBoxAvailable", new Integer(intBoxAvailable).toString());
         query.setWhere(null, 0, "BOX_intBoxID", "=", strBoxID, 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
         
         // TANK UPDATE
         // go up one level and get the tank id
         query.reset();
         query.setDomain("BOX", null, null, null);
         query.setField("BOX_intTankID", null);
         query.setWhere(null, 0, "BOX_intBoxID", "=", strBoxID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the tank id
         String strTankID = "-1";
         if (rsResultSet.next())
            strTankID = rsResultSet.getString("BOX_intTankID");
         
         rsResultSet.close();
         
         // get the available cell in the tank
         query.reset();
         query.setDomain("TANK", null, null, null);
         query.setField("TANK_intTankAvailable", null);
         query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
         rsResultSet = query.executeSelect();
         
         // actually get the available cell in the tank and increase it by 1
         int intTankAvailable = 0;
         if (rsResultSet.next())
            intTankAvailable = rsResultSet.getInt("TANK_intTankAvailable") - 1;
         
         rsResultSet.close();
         
         // set the decreased available cell in the tank back to the DB
         query.reset();
         query.setDomain("TANK", null, null, null);
         query.setField("TANK_intTankAvailable", new Integer(intTankAvailable).toString());
         query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
         query.executeUpdate();
      }
      catch(Exception e)
      {
         
         LogService.instance().log(LogService.ERROR, "CBiospecimen::updateInventoryWhenDeleteAndUnallocateBiospecimen() : Unexpected error ", e);
      }
   }
   
   /** Get inventory details of a biospecimen
    */
   
   public static String getInventoryXML(int intBiospecimenKey, AuthToken authToken)
   {
      
      StringBuffer strXML = new StringBuffer();
      if (intBiospecimenKey<0)
      {
         System.err.print("Invalid biospecimen key");
         return null;
      }
      
      String[] strResult = InventoryUtilities.getBiospecimenLocation("biospecimen", intBiospecimenKey,authToken);
      strXML.append("<inventory_info>");
      
      strXML.append("<SITE_strSiteName>" + strResult[InventoryUtilities.SITENAME] + "</SITE_strSiteName>");
      strXML.append("<TANK_strTankName>" + strResult[InventoryUtilities.TANKNAME] + "</TANK_strTankName>");
      strXML.append("<BOX_strBoxName>" + strResult[InventoryUtilities.BOXNAME] + "</BOX_strBoxName>");
      strXML.append("<TRAY_strTrayName>" + strResult[InventoryUtilities.TRAYNAME] + "</TRAY_strTrayName>");
      strXML.append("<TRAY_intRowNumber>"+ strResult[InventoryUtilities.ROW_NUMBER] + "</TRAY_intRowNumber>");
      strXML.append("<TRAY_intColumnNumber>"+ strResult[InventoryUtilities.COLUMN_NUMBER] + "</TRAY_intColumnNumber>");
      strXML.append("<CELL_intCellID>"+ strResult[InventoryUtilities.CELL_ID] + "</CELL_intCellID>");
      strXML.append("<intLocationID>" + getBiospecLocation(intBiospecimenKey+"") + "</intLocationID>");
      //LogService.instance().log(LogService.INFO, ":::::"+strXML);
      // call function to get XML for inventory titles displayed
      strXML.append(getInventoryTitleXML());
        
      strXML.append("</inventory_info>");
      
      return strXML.toString();
      
      
   }
   
    /*
     *  Function which generates the Inventory titles
     *
     */
    public static StringBuffer getInventoryTitleXML ()
    {
        StringBuffer titleXML = new StringBuffer();
	String CLIENT = null;

        try
        {
            try
            {
              CLIENT = PropertiesManager.getProperty("neuragenix.bio.Client");
            }
            catch (Exception e)
            {
              System.err.println("[InvUtilities]: Unable to retreive property - neuragenix.bio.Client");
              e.printStackTrace();
            }
            
            if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("ccia")))
            {
                Vector vtFormFields = DatabaseSchema.getFormFields("cbiospecimen_inventory_titles");
                titleXML.append(QueryChannel.buildFormLabelXMLFile(vtFormFields));            
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "InvUtilities::getInventoryTitleXML] Unexpected error ", e);
            e.printStackTrace();
        }

        return titleXML;

    }   
   
    /** 
     * Build the XML string for displaying, multi purpose
     * Legend: -2: Empty, -1 Highligted, 0: Marked as used, >0 Biospecimen ID that occupying the cell
     */
    
   public static String buildTrayMapXML(int intTrayID, Collection vtCellIDsToHightLight, Collection vtCellIDsToMarkAsUsed, Collection biospecimenIDsToHighlight){
        
    		// to avoid null pointer checking
    		if ( vtCellIDsToHightLight == null ){
    			vtCellIDsToHightLight = new Vector();
    		}
    		if (vtCellIDsToMarkAsUsed == null){
    			vtCellIDsToMarkAsUsed = new Vector();
    		}
    		if ( biospecimenIDsToHighlight == null){
    			biospecimenIDsToHighlight = new ArrayList();
    		}
        StringBuffer strXML =new StringBuffer();
        int intCounter = 0;
        StringBuffer sbLocations = new StringBuffer();
        Hashtable htIDLocations = new Hashtable();
        try {
            DALSecurityQuery query = new DALSecurityQuery();
            query.setDomain("TRAY", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_tray"), null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID+"", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();
            
            if (rsResultSet.next()) {
                String strRowType = rsResultSet.getString("TRAY_strRowNoType");
                String strColType = rsResultSet.getString("TRAY_strColNoType");
                int intNoOfRow = rsResultSet.getInt("TRAY_intNoOfRow");
                int intNoOfCol = rsResultSet.getInt("TRAY_intNoOfCol");
                
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setDomain("BIOSPECIMEN", "CELL_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "LEFT JOIN");
                query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_cell"), null);
                query.setWhere(null, 0, "CELL_intTrayID", "=", new Integer(intTrayID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("CELL_intRowNo", "ASC");
                query.setOrderBy("CELL_intColNo", "ASC");
                int counter = 0;
                rsResultSet = query.executeSelect();
                //for each row
                for (int i=0; i < intNoOfRow + 1; i++) {
                    strXML.append("<Row number=\""+i+"\">");
                    //for each column
                    for (int j=0; j < intNoOfCol + 1; j++) {
                        strXML.append("<Col number=\""+j+"\">");
                        //first cell 
                        if (i == 0 && j == 0) {
                            strXML.append("<label>0</label>");
                        }
                        else if (i == 0) {
                        	  //first rows, label printing
                            if (strColType.equals("Alphabet"))
                                strXML .append("<label>" + ALPHABET.substring(j-1, j) + "</label>");
                            else
                                strXML.append( "<label>" + j + "</label>");
                        }
                        //first column label printing
                        else if (j == 0) {
                            if (strRowType.equals("Alphabet"))
                                strXML.append( "<label>" + ALPHABET.substring(i-1, i) + "</label>");
                            else
                                strXML.append( "<label>" + i + "</label>");
                        }
                        else {
                            rsResultSet.next();
                            //get cell id 
                            int cellID = rsResultSet.getInt("CELL_intCellID");
                            int biospecimenID = rsResultSet.getInt("CELL_intBiospecimenID");
                            int patientID = rsResultSet.getInt("CELL_intPatientID");
                            
                            strXML.append("<label>-1</label>");
                            strXML.append("<CELL_intCellID>" + cellID + "</CELL_intCellID>");
                            strXML.append("<CELL_intPatientID>" + patientID + "</CELL_intPatientID>");
                            if ( vtCellIDsToHightLight.contains(cellID+"") || biospecimenIDsToHighlight.contains(biospecimenID+"")){
                            		counter++;
                            		strXML.append("<CELL_intBiospecimenID>" + -1 + "</CELL_intBiospecimenID>");
                            		sbLocations.append("<biospecimen key=\""+counter+"\">");
                            		sbLocations.append("<highlight_location>");
                            		if (strRowType.equals("Alphabet"))
                                        sbLocations.append( ALPHABET.substring(i-1, i)) ;
                                    else
                                    	sbLocations.append(i) ;
                            		
                            		if (strColType.equals("Alphabet"))
                                        sbLocations.append(" "+ ALPHABET.substring(j-1, j)) ;
                                    else
                                    	sbLocations.append(" "+j) ;
                            		sbLocations.append("</highlight_location>");
                            		//print out user friendly ID (not the internal ID
                                  sbLocations.append("<id>"+BiospecimenUtilities.getUserBiospecimenID(biospecimenID) +"</id>");  	
                                  sbLocations.append("</biospecimen>");
                            		
                            }
                            else if (vtCellIDsToMarkAsUsed.contains(cellID+"")){
                            	//mark as used but the id set to 0
                            	strXML .append("<CELL_intBiospecimenID>" + 0 + "</CELL_intBiospecimenID>");
                            }
                            else if (biospecimenID == -1 ) {
                                
                                //mark as green in the map
                                strXML.append("<CELL_intBiospecimenID>-2</CELL_intBiospecimenID>");

                            }
                            else {
                                String strBiospecimenID = rsResultSet.getString("BIOSPECIMEN_strBiospecimenID");
                                String strSampleType = rsResultSet.getString("BIOSPECIMEN_strSampleType");
                                String strSampleSubType = rsResultSet.getString("BIOSPECIMEN_strSampleSubType");
                                                          
                                strXML.append("<CELL_intBiospecimenID>" + biospecimenID + "</CELL_intBiospecimenID>");
                            }
                            
                        }
                        
                        strXML.append("</Col>");
                    }
                    strXML.append("</Row>");
                    
                }
                
              
                Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
                DALSecurityQuery query1 = new DALSecurityQuery();
                query1.setDomain("TRAY", null, null, null);
                query1.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
                
                query1.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
                query1.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
                query1.setFields(vtAvailableLocationForm, null);
                
                query1.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID+"", 0, DALQuery.WHERE_HAS_VALUE);
                
                ResultSet rsResultSet1 = query1.executeSelect();
                
                if (rsResultSet1.next()) {
                    strXML.append("<strSiteName>" + rsResultSet1.getString("SITE_strSiteName") + "</strSiteName>");
                    strXML.append ("<strTankName>" + rsResultSet1.getString("TANK_strTankName") + "</strTankName>");
                    strXML.append( "<strBoxName>" +  rsResultSet1.getString("BOX_strBoxName") + "</strBoxName>");
                    strXML.append( "<strTrayName>" + rsResultSet1.getString("TRAY_strTrayName") + "</strTrayName>");
                }
                
                rsResultSet1.close();
               
               
               strXML.append(sbLocations);
            }
            rsResultSet.close();
            
        }
        catch (Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in Biospecimen Channel - " + e.toString(), e);
        }
        
        return strXML.toString();
      
    }
    
   private static String getBiospecLocation(String intBiospecimenID) {
	   String strLocation = "N/A";
	   DALQuery query = new DALQuery();
	   try 
	   {  
		   
		   query.setDomain("BIOSPECIMEN", null, null, null);
		   query.setDomain("CELL", "BIOSPECIMEN_intCellID", "CELL_intCellID", "INNER JOIN");
		   query.setDomain("TRAY", "CELL_intTrayID", "TRAY_intTrayID", "LEFT JOIN");
		   query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "LEFT JOIN");
		   query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "LEFT JOIN");
		   query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "LEFT JOIN");
		   query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID , 0, DALQuery.WHERE_HAS_VALUE);
		    		   
		   query.setField("TRAY_intNoOfCol", null);
		   query.setField("TRAY_intNoOfRow", null);
		   
		   query.setField("CELL_intRowNo", null);
		   query.setField("CELL_intColNo", null);	  	   
		   ResultSet rsResultSet = query.executeSelect();		   
		   if (rsResultSet.next()) 
		   {			  			   
			   int intNumberOfRows = rsResultSet.getInt("TRAY_intNoOfCol");
			   int intNumberOfCols = rsResultSet.getInt("TRAY_intNoOfRow");			   
			   int intRowNo = rsResultSet.getInt("CELL_intRowNo");
			   int intColNo = rsResultSet.getInt("CELL_intColNo");			   
			   strLocation = String.valueOf(NeuroUtils.getLocationID(intColNo, intRowNo, intNumberOfRows, intNumberOfCols));
			   			 
		   }
	   }
	   catch(Exception e) {
		   LogService.instance().log(LogService.ERROR, "InventoryUitilities: getBiospecLocation ", e);
	   }
	   return strLocation;
   }
    
}
