/*
 * TrayManager.java
 *
 * Created on 9 June 2005
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  sparappat
 */

import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.bio.utilities.*;
import neuragenix.bio.biospecimen.*;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Enumeration;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

public class TrayManager {
    
    private AuthToken authToken = null;
    
    private NGXRuntimeProperties rp = null;
    
    private LockRequest lockRequest = null;
    
    private ChannelRuntimeData runtimeData = null;
    
    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    // pages & activities
    private final String INVENTORY_VIEW = "inventory_view";
    
    private static final String CHANGE_TRAY = "change_tray";

    private static final String CHANGE_TRAY_REPORT = "change_tray_report";
    
    private final String VIEW_TRAY = "view_tray";  
    
    // Private variables
    private int intNumCells = 0;
    
    private int intCurrentTrayID = 0;
    
    private String strCellList;
    
    private Vector vtMapCellDetails = new Vector ();
    
    private Hashtable htMapCellID = new Hashtable ();
    
    private Hashtable htNewCellBioID = new Hashtable ();
        
    private int intNewCellID[];
    
    //Keep track of previous XML, stylesheet and error message
    private String strPreviousXML;
    
    private String strPreviousStylesheet;
    
    private String strPreviousErrorMsg;
    
    /** Creates a new instance of TrayManager */
    public TrayManager(AuthToken authToken, LockRequest lockRequest) 
    {
        this.authToken = authToken;
        this.lockRequest = lockRequest;
    }
    
    public TrayManager(NGXRuntimeProperties rp, LockRequest lockRequest)
    {
        this.rp = rp;
        this.authToken = rp.getAuthToken();
        this.lockRequest = lockRequest;
    }
   
    public NGXRuntimeProperties processRuntimeData(ChannelRuntimeData rd)
    {
        Vector formFields = DatabaseSchema.getFormFields("cinventory_get_display");
        this.runtimeData = rd;
        
        if (rp == null)
        {
            rp = new NGXRuntimeProperties();
            rp.setAuthToken(authToken);
        }

        // Clear the XML        
        rp.clearXML();

        if ((runtimeData.getParameter("selectCells") != null) && !runtimeData.getParameter("selectCells").equals(""))
        {
            htMapCellID.clear();
            vtMapCellDetails.clear();   
            htNewCellBioID.clear();

            // Build the inventory list for the selected cells
            // That is - for the number of cells that was selected for relocation
            // build the list of SITE, TANK, BOX and TRAY that the biospecimen can be
            // relocated to
            buildSelectedCellsInventoryXML();

        } 
        else if ((runtimeData.getParameter("moveCells") != null) && !runtimeData.getParameter("moveCells").equals(""))
        {
            // build the summary of biospecimens to moved, the tray moving from
            // and the new tray that the biosepecimens are being relocated to
            buildRelocationSummaryXML ();
        }
        else if (runtimeData.getParameter("confirmMoveCells") != null)
        {            
            boolean blPassed = updateCellLocations ();       
            if (blPassed == true)
            {    
                runtimeData.setParameter("TRAY_intTrayID", Integer.toString(intCurrentTrayID));
                rp.addXML("<TRAY_intTrayID>" + (runtimeData.getParameter("TRAY_intTrayID") + "</TRAY_intTrayID>"));
                rp.setStylesheet(VIEW_TRAY);

                // Log the history of the biospecimens that were relocated
                InventoryHistory.addHistory(vtMapCellDetails, authToken);
                htMapCellID.clear();
                vtMapCellDetails.clear(); 
                htNewCellBioID.clear();
            }
            else
            {
                rp.addXML(strPreviousXML);
                rp.addXML("<blLockError>true</blLockError>");
                rp.setStylesheet(strPreviousStylesheet);                        
                rp.setErrorMessage(strPreviousErrorMsg); 
            }    
        }   
        else if (runtimeData.getParameter("Cancel") != null)
        {
            htMapCellID.clear();
            vtMapCellDetails.clear(); 
            htNewCellBioID.clear();
            runtimeData.setParameter("TRAY_intTrayID", Integer.toString(intCurrentTrayID));
            rp.addXML("<TRAY_intTrayID>" + (runtimeData.getParameter("TRAY_intTrayID") + "</TRAY_intTrayID>"));
            rp.setStylesheet(VIEW_TRAY);
        }
        else if (runtimeData.getParameter("Back") != null)
        {
            // On hitting back button from the change_tray_report page
            // rebuild the old tray, the cells that were selected
            // and the SITE, TANK, BOX, TRAY information for the destination location
            rp.addXML(getInventoryListforRelocation(intNumCells));
            rp.addXML(buildCellXMLFile(intCurrentTrayID));
            rp.addXML(QueryChannel.buildFormLabelXMLFile(formFields));
            rp.addXML("<numCellstoMove>" + intNumCells + "</numCellstoMove>");
            rp.addXML("<cellList>" + strCellList + "</cellList>");
            rp.setStylesheet(CHANGE_TRAY);        
        }    
        else
        {
            htMapCellID.clear();
            vtMapCellDetails.clear();
            htNewCellBioID.clear();

            intCurrentTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayID"));        
            rp.addXML(buildCellXMLFile(intCurrentTrayID));
            rp.addXML(QueryChannel.buildFormLabelXMLFile(formFields));
            rp.addXML("<TRAY_intTrayID>" + (runtimeData.getParameter("TRAY_intTrayID") + "</TRAY_intTrayID>"));
            rp.setStylesheet(CHANGE_TRAY);        
        }    

        // Keep track of the previous data
        strPreviousXML = rp.getXML();
        strPreviousStylesheet = rp.getStylesheet();
        strPreviousErrorMsg = rp.getErrorMessage();

        return rp;
    }

    private void buildSelectedCellsInventoryXML ()
    {
        Vector formFields = DatabaseSchema.getFormFields("cinventory_get_display");

        // Get the inventory list based on the number of cells we want to move
        // get all the trays that have enough locations
        if (runtimeData.getParameter("numCellstoMove") != null && !runtimeData.getParameter("numCellstoMove").equals(""))
        {    

            intNumCells = Integer.parseInt(runtimeData.getParameter("numCellstoMove"));
            rp.addXML(getInventoryListforRelocation(intNumCells));
            // get the list of selected cells            
            strCellList = runtimeData.getParameter("cellList");
            // build a string array of the selected cell IDs
            String strCellID[] = strCellList.split(";");

            // Get a tree map of the old cell IDs that
            // can then be sorted in ascending order
            TreeMap trOldCellMap = new TreeMap ();
            for (int i=0; i<strCellID.length; i++)
            {
                trOldCellMap.put(strCellID[i], strCellID[i]);
            }   


            Iterator itr = trOldCellMap.keySet().iterator();
            while (itr.hasNext())
            {
                String strOldCellID = itr.next().toString();  
                // Get a Hashtable of the old cell IDs
                htMapCellID.put (strOldCellID, strOldCellID);
            }                    

        }

        rp.addXML(buildCellXMLFile(intCurrentTrayID));
        rp.addXML(QueryChannel.buildFormLabelXMLFile(formFields));
        rp.addXML("<numCellstoMove>" + intNumCells + "</numCellstoMove>");
        rp.addXML("<cellList>" + strCellList + "</cellList>");
        rp.addXML("<TRAY_intTrayID>" + (runtimeData.getParameter("TRAY_intTrayID") + "</TRAY_intTrayID>"));
        rp.setStylesheet(CHANGE_TRAY);        
    
    }
    
    
    private void buildRelocationSummaryXML ()
    {
        Vector formFields = DatabaseSchema.getFormFields("cinventory_get_display");
        
        try
        {
            int intTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayIDSelected"));

            // Add a lock for the destination tray 
            // Only add the lock if the destination tray is not the same as the current tray
            if (intTrayID != intCurrentTrayID)
            {    
                lockRequest.addLock("TRAY", Integer.toString(intTrayID), LockRecord.READ_WRITE);
            }

            // Get the list of available cell IDs based on the selected tray
            intNewCellID = getAvailableCells (intTrayID, intNumCells);   

            // Get a tree map of the old cell IDs that
            // can then be sorted in ascending order
            TreeMap trOldCellMap = new TreeMap ();
            Enumeration key_enum = htMapCellID.keys();
            while (key_enum.hasMoreElements())
            {
                String strOldCellID = key_enum.nextElement().toString();
                trOldCellMap.put(strOldCellID, strOldCellID);
            }                   

            Iterator itr = trOldCellMap.keySet().iterator();
            int i=0;
            while (itr.hasNext())
            {
                String strOldCellID = itr.next().toString();  
                // Map the new cell ID to the old cell IDs
                htMapCellID.put (strOldCellID,  Integer.toString(intNewCellID[i]));
                i++;
            }                    

            vtMapCellDetails = getCellDetails(htMapCellID);
            rp.addXML("<fromTray>" + buildCellXMLFile(intCurrentTrayID) + "</fromTray>");
            rp.addXML("<toTray>" + buildCellXMLFile(intTrayID) + "</toTray>");
            rp.addXML(QueryChannel.buildFormLabelXMLFile(formFields));
            rp.addXML(buildNewCellLocationReport());
            rp.addXML("<TRAY_intTrayID>" + (runtimeData.getParameter("TRAY_intTrayID") + "</TRAY_intTrayID>"));
            rp.setStylesheet(CHANGE_TRAY_REPORT); 
            rp.setErrorMessage ("Please check that the details below are correct.");
        
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Error in Tray Manager - " + e.toString(), e);         
        }
        
    }
    
    
    // Get the details for each of the cells to be moved
    // each entry in the vector is a Hashtable containing the cell's details (Old Cell ID, New Cell ID,
    // Biospecimen ID, Patient ID
    private Vector getCellDetails (Hashtable htCellIDs)
    {
        Vector vtCellDetails = new Vector(intNumCells);     
        Vector vtFormFields = DatabaseSchema.getFormFields("cinventory_cell_details");
        DALQuery query = new DALQuery();
        
        try
        {

            // Get a tree map of the old cell IDs that
            // can then be sorted in ascending order
            TreeMap trCellMap = new TreeMap ();
            Enumeration key_enum = htCellIDs.keys();
            while (key_enum.hasMoreElements())
            {
                String strOldCellID = key_enum.nextElement().toString();
                trCellMap.put(strOldCellID, strOldCellID);
            }                   

            Iterator itr = trCellMap.keySet().iterator();
            
            while (itr.hasNext())
            {
                Hashtable htCellDetails = new Hashtable (5);
                String strOldCellID = itr.next().toString();
                String strNewCellID = htCellIDs.get(strOldCellID).toString();
                htCellDetails.put ("CELL_intOldCellID", strOldCellID);
                htCellDetails.put ("CELL_intNewCellID", htCellIDs.get(strOldCellID));
            
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setDomain("BIOSPECIMEN", "CELL_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "LEFT JOIN");
                query.setFields(vtFormFields, null);
                query.setWhere(null, 0, "CELL_intCellID", "=", strOldCellID, 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResultSet = query.executeSelect();

                if (rsResultSet.next())
                {
                    for (int j=0; j<vtFormFields.size(); j++)
                    {
                        if (rsResultSet.getString(vtFormFields.get(j).toString()) != null)
                        {
                            htCellDetails.put(vtFormFields.get(j), rsResultSet.getString(vtFormFields.get(j).toString()));                
                        }
                    }    

                }
                
                // Close the resultset
                rsResultSet.close();

                // Get the row and column details of the new cell ID
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setField("CELL_intRowNo", null);
                query.setField("CELL_intColNo", null);
                query.setWhere(null, 0, "CELL_intCellID", "=", strNewCellID, 0, DALQuery.WHERE_HAS_VALUE);
                rsResultSet = query.executeSelect();
                
                if (rsResultSet.next())
                {
                    // Put the new cell ID's row number into the hashtable
                    if (rsResultSet.getString("CELL_intRowNo") != null)
                    {
                        htCellDetails.put("CELL_intNewRowNo", rsResultSet.getString("CELL_intRowNo"));                
                    }

                    // Put the new cell ID's column number into the hashtable
                    if (rsResultSet.getString("CELL_intColNo") != null)
                    {
                        htCellDetails.put("CELL_intNewColNo", rsResultSet.getString("CELL_intColNo"));                
                    }
                
                }
                
                // Close the resultset
                rsResultSet.close();
                
                // Map the new cell ID to the biospecimen it will contain
                htNewCellBioID.put (htCellDetails.get("CELL_intNewCellID").toString(), htCellDetails.get("BIOSPECIMEN_strBiospecimenID").toString());
            
                vtCellDetails.add(htCellDetails);
            }                    
            
        }
        catch (Exception e)
        {
           LogService.instance().log(LogService.ERROR, "Error obtaining cell details - " + e.toString(), e);
        }
    
        return vtCellDetails;
    
    }
    
    
    private String buildCellXMLFile(int intTrayID)
    {
        String strXML = "";
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
            query.setDomain("TRAY", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("cinventory_view_inventory_tray"), null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", new Integer(intTrayID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();
            
            if (rsResultSet.next())
            {
                String strRowType = rsResultSet.getString("TRAY_strRowNoType");
                String strColType = rsResultSet.getString("TRAY_strColNoType");
                int intNoOfRow = rsResultSet.getInt("TRAY_intNoOfRow");
                int intNoOfCol = rsResultSet.getInt("TRAY_intNoOfCol");
                
                rsResultSet.close();
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setDomain("BIOSPECIMEN", "CELL_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "LEFT JOIN");
                query.setFields(DatabaseSchema.getFormFields("cinventory_view_inventory_cell"), null);
                query.setWhere(null, 0, "CELL_intTrayID", "=", new Integer(intTrayID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("CELL_intRowNo", "ASC");
                query.setOrderBy("CELL_intColNo", "ASC");
                
                rsResultSet = query.executeSelect();

                for (int i=0; i < intNoOfRow + 1; i++)
                {
                    strXML += "<Row>";
                    for (int j=0; j < intNoOfCol + 1; j++)
                    {
                        strXML += "<Col>";
                        
                        if (i == 0 && j == 0)
                        {
                            strXML += "<label>0</label>";
                        }
                        else if (i == 0)
                        {
                            if (strColType.equals("Alphabet"))
                                strXML += "<label>" + ALPHABET.substring(j-1, j) + "</label>";
                            else
                                strXML += "<label>" + j + "</label>";
                        }
                        else if (j == 0)
                        {
                            if (strRowType.equals("Alphabet"))
                                strXML += "<label>" + ALPHABET.substring(i-1, i) + "</label>";
                            else
                                strXML += "<label>" + i + "</label>";
                        }
                        else
                        {
                            rsResultSet.next();
                            int cellID = rsResultSet.getInt("CELL_intCellID");
                            int biospecimenID = rsResultSet.getInt("CELL_intBiospecimenID");
                            int patientID = rsResultSet.getInt("CELL_intPatientID");
                            String strCellID = Integer.toString(cellID);
                            
                            strXML += "<label>-1</label>";
                            strXML += "<CELL_intCellID>" + cellID + "</CELL_intCellID>";
                            strXML += "<CELL_intBiospecimenID>" + biospecimenID + "</CELL_intBiospecimenID>";
                            strXML += "<CELL_intPatientID>" + patientID + "</CELL_intPatientID>";
                            
                            if (biospecimenID == -1)
                            {
                                strXML += "<CELL_info>Cell ID: " + cellID + "&#10;&#13;";
                                
                                if (strRowType.equals("Alphabet"))
                                    strXML += "Row: " + ALPHABET.substring(i-1, i) + "&#10;&#13;";
                                else
                                    strXML += "Row: " + i + "&#10;&#13;";
                                
                                if (strColType.equals("Alphabet"))
                                    strXML += "Col: " + ALPHABET.substring(j-1, j) + "&#10;&#13;";
                                else
                                    strXML += "Col: " + j + "&#10;&#13;";
                                
                                
                                if ( htMapCellID.containsValue(strCellID))
                                {
                                    strXML += "Status: New&#10;&#13;";
                                    if (htNewCellBioID.containsKey(strCellID))
                                    {
                                        strXML += "Biospecimen ID: " + htNewCellBioID.get(strCellID).toString() + "&#10;&#13;";
                                    }
                                    strXML += "</CELL_info>";
                                }
                                else
                                {
                                   strXML += "Status: Empty</CELL_info>";
                                }    
                                
                                
                            }
                            else
                            {
                                String strBiospecimenID = rsResultSet.getString("BIOSPECIMEN_strBiospecimenID");
                                String strSampleType = rsResultSet.getString("BIOSPECIMEN_strSampleType");
                                String strSampleSubType = rsResultSet.getString("BIOSPECIMEN_strSampleSubType");
                                
                                strXML += "<CELL_info>Cell ID: " + cellID + "&#10;&#13;";
                                
                                if (strRowType.equals("Alphabet"))
                                    strXML += "Row: " + ALPHABET.substring(i-1, i) + "&#10;&#13;";
                                else
                                    strXML += "Row: " + i + "&#10;&#13;";
                                
                                if (strColType.equals("Alphabet"))
                                    strXML += "Col: " + ALPHABET.substring(j-1, j) + "&#10;&#13;";
                                else
                                    strXML += "Col: " + j + "&#10;&#13;";
                                
                                if ( htMapCellID.containsKey(strCellID))
                                {
                                    strXML += "Status: Moving&#10;&#13;";
                                }
                                else
                                {
                                   strXML += "Status: Full&#10;&#13;";
                                }    
                                                                
                                strXML += "Biospecimen ID: " + strBiospecimenID + "&#10;&#13;";
                                strXML += "Type: " + strSampleType + "&#10;&#13;";
                                strXML += "Subtype: " + strSampleSubType + "&#10;&#13;</CELL_info>";
                            }
                            
                            if ( htMapCellID.containsKey(strCellID))
                            {
                                strXML += "<OldCell>" + "true" + "</OldCell>";
                            }

                            if ( htMapCellID.containsValue(strCellID))
                            {
                                strXML += "<NewCell>" + "true" + "</NewCell>";
                            }
                        
                        }
                            
                        strXML += "</Col>";
                    }
                    strXML += "</Row>";
                }
            }
            
            rsResultSet.close();
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
        
        return strXML;
    }

    
    // unallocate the biospecimens from the old cell ids and allocate them to the
    // new cell IDs
    private boolean updateCellLocations ()
    {
        
        try
        {
           // we can lock the record for update
//           if (lockRequest.isValid() && lockRequest.lockWrites())
           {
               for (int i=0; i<vtMapCellDetails.size(); i++)
               {
                   int intBiospecimenID = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intBiospecimenID").toString());
                   int intAllocCellID = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intNewCellID").toString());
                   //System.out.println ("intBiospecimenID =" + intBiospecimenID);
                   //System.out.println ("intAllocCellID =" + intAllocCellID);

                   // unallocate the old cell ID
                   InventoryUtilities.updateInventoryWhenDeleteAndUnallocateBiospecimen (intBiospecimenID, authToken);
                   // save the biospecimen with the new cell ID location and allocate the biospecimen to the new cell ID
                   InventoryUtilities.saveBiospecimenLocation (intBiospecimenID,intAllocCellID, authToken);           
                }
               
                // set the lock to READ_LOCK
//                lockRequest.unlockWrites(); 
                return true;
           }
//           else
//           {               
//               return false;
//           }    
           
        }    
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
            return false;
        }
        
    }
   
    // Get the inventory list consisting of trays that can satisfy the
    // required number of cells to be relocated
    private String getInventoryListforRelocation(int intNumCells)
    {
       DALQuery query = new DALQuery();
       ResultSet rs;

       String strNumCells = Integer.toString(intNumCells);
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
          query.setWhere("AND", 0, "TRAY_intTrayAvailable", ">=", strNumCells, 0, DALQuery.WHERE_HAS_VALUE);
          rs = query.executeSelect();

          String strXML = QueryChannel.buildSearchXMLFile("InventoryLocations", rs, DatabaseSchema.getFormFields("cbiospecimen_view_inventory_details_for_html"));

          rs.close();
          return strXML;

       }
       catch (Exception e)
       {
          System.err.println("[TrayManager : getInventoryListforRelocation() - Error occured when trying to Inventory details " + e.getMessage());
       }
       return "";
    }

    
    public int[] getAvailableCells(int intTrayID, int intCellCount)
    {
        int availableCells[] = new int[intCellCount];

        
        // hunt down a set of available cells for the amount required.
        DALQuery query = new DALQuery();
        ResultSet rs = null;
        try
        {
            query.setDomain("CELL", null, null, null);
            query.setField("CELL_intCellID", null);
            query.setWhere(null, 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "CELL_intTrayID", "=", intTrayID + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "CELL_intBiospecimenID", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "CELL_intPatientID", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("CELL_intCellID", "ASC");
            rs = query.executeSelect();

            int counter = -1;
            for (counter = 0; (counter < intCellCount) && (rs.next()); counter++)
            {
                availableCells[counter] = rs.getInt("CELL_intCellID");
            }

            if (counter != intCellCount)
            {
                // there was not enough cells... major error
                System.err.println ("[InventoryDetails :: getAvailableCells]  Request [" + intCellCount + "]" + " -- Not enough cells were found in inventory");
                return null;
            }


            rs.close();

            return availableCells;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }


    }
    
    
    private String buildNewCellLocationReport ()
    {
        String strLocationDetails = "";
        
        try
        {
            // Build the old and new site, tank, box and tray names
            int intOldCellID = Integer.parseInt(((Hashtable)vtMapCellDetails.get(0)).get("CELL_intOldCellID").toString());
            int intNewCellID = Integer.parseInt(((Hashtable)vtMapCellDetails.get(0)).get("CELL_intNewCellID").toString());

            String strOldCellDetails [] = InventoryUtilities.getBiospecimenLocation ("inventory", intOldCellID, authToken, null);
            strLocationDetails += "<strOldSiteName>" + strOldCellDetails[InventoryUtilities.SITENAME] + "</strOldSiteName>";
            strLocationDetails += "<strOldTankName>" + strOldCellDetails[InventoryUtilities.TANKNAME] + "</strOldTankName>";
            strLocationDetails += "<strOldBoxName>" + strOldCellDetails[InventoryUtilities.BOXNAME] + "</strOldBoxName>";
            strLocationDetails += "<strOldTrayName>" + strOldCellDetails[InventoryUtilities.TRAYNAME] + "</strOldTrayName>";
            int intOldCellTrayID = Integer.parseInt(strOldCellDetails[InventoryUtilities.TRAY_ID]);
            
            String strNewCellDetails [] = InventoryUtilities.getBiospecimenLocation ("inventory", intNewCellID, authToken, null);
            strLocationDetails += "<strNewSiteName>" + strNewCellDetails[InventoryUtilities.SITENAME] + "</strNewSiteName>";
            strLocationDetails += "<strNewTankName>" + strNewCellDetails[InventoryUtilities.TANKNAME] + "</strNewTankName>";
            strLocationDetails += "<strNewBoxName>" + strNewCellDetails[InventoryUtilities.BOXNAME] + "</strNewBoxName>";
            strLocationDetails += "<strNewTrayName>" + strNewCellDetails[InventoryUtilities.TRAYNAME] + "</strNewTrayName>";
            int intNewCellTrayID = Integer.parseInt(strNewCellDetails[InventoryUtilities.TRAY_ID]);
            
            String strCellMapDetails = "";
            for (int i=0; i<vtMapCellDetails.size(); i++)
            {
               String strBiospecimenID = (((Hashtable)vtMapCellDetails.get(i)).get("BIOSPECIMEN_strBiospecimenID").toString());
               intOldCellID = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intOldCellID").toString());
               int intOldRowNo = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intRowNo").toString());
               int intOldColNo = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intColNo").toString());
               
               intNewCellID = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intNewCellID").toString());
               int intNewRowNo = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intNewRowNo").toString());
               int intNewColNo = Integer.parseInt(((Hashtable)vtMapCellDetails.get(i)).get("CELL_intNewColNo").toString());
               
               strCellMapDetails += "<cellLocationMapping>" + "<strBiospecimenID>" + strBiospecimenID + "</strBiospecimenID>" + "<strOldID>" + intOldCellID + "</strOldID>" +
               "<strOldLocation>" + getCellLocationString(intOldCellTrayID,intOldRowNo, intOldColNo) + "</strOldLocation>" + "<strNewID>" + intNewCellID + "</strNewID>" + 
               "<strNewLocation>" + getCellLocationString(intNewCellTrayID,intNewRowNo, intNewColNo) + "</strNewLocation>" + "</cellLocationMapping>";
            }
            strLocationDetails += strCellMapDetails;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;        
        }        

        
        return strLocationDetails;
    
    }
    
    // Given the tray ID, and the cell row and column number
    // return a string that represents the cell location
    // e.g. A-1
    private String getCellLocationString (int intTrayID, int intRowNo, int intColNo)
    {
    
        DALQuery query = new DALQuery();
        ResultSet rs = null;
        String strRowNoType = "";
        String strColNoType = "";
        String strLocation = "";
        
        try
        {
            // Get the display type for the row and the column
            query.setDomain("TRAY", null, null, null);
            query.setField("TRAY_strRowNoType", null);
            query.setField("TRAY_strColNoType", null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", Integer.toString(intTrayID), 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.next())
            {
                strRowNoType = rs.getString("TRAY_strRowNoType");
                strColNoType = rs.getString("TRAY_strColNoType");            
            }
            
            // Close the resultset
            rs.close();
            
            // Put the row number into the string
            strLocation = Integer.toString(intRowNo) + "-";
            if (strRowNoType.equals("Alphabet"))
            {
               strLocation = ALPHABET.substring(intRowNo-1, intRowNo) + "-";
            } 
            
            // Add the column number to the string
            if (strColNoType.equals("Alphabet"))
            {
               strLocation += ALPHABET.substring(intColNo-1, intColNo);
            }    
            else
            {
              strLocation += Integer.toString(intColNo);            
            }    
           
            return strLocation;  

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    
    }
    
    
}
