/**
 * Inventory.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 27/08/2003
 */

/**
 * Inventory channel
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

/**
 *  Updated 2 March, 2005 - Daniel Murley (dmurley@neuragenix.com)
 *   Moved to centralised schema.
 */

/**
 *  Updated 4 May, 2005 - Daniel Murley (dmurley@neuragenix.com)
 *   Changed code to close its resultsets.
 */

// java packages
package neuragenix.bio.inventory;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;
import java.sql.*;

import javax.swing.tree.DefaultMutableTreeNode;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

// uPortal packages
import org.jasig.portal.IChannel;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XSLT;
import org.xml.sax.ContentHandler;
import org.jasig.portal.security.*;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

// neuragenix packages
import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.bio.biospecimen.InventoryDetails;
import neuragenix.bio.utilities.*;

import neuragenix.common.*;




public class CInventory implements IChannel
{
    //Directory to store data for export
    private static final String EXPORT_DIRECTORY = PropertiesManager.getProperty("neuragenix.bio.search.ExportFileLocation");
    private static final String ALLOW_INVENTORY_DUPLICATES = PropertiesManager.getProperty("neuragenix.bio.inventory.AllowDuplicates");
    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    // pages & acticities
    private final String INVENTORY_VIEW = "inventory_view";
    private final String INVENTORY_INSERT = "inventory_insert";
    private final String INVENTORY_UPDATE = "inventory_update";
    private final String INVENTORY_DELETE = "inventory_delete";
    private final String INVENTORY_CHANGE_LOCATION = "inventory_change_location";
    private final String SECURITY = "security";
    private final String ADD_SITE = "add_site";
    private final String ADD_TANK = "add_tank";
    private final String ADD_BOX = "add_box";
    private final String ADD_TRAY = "add_tray";
    private final String VIEW_SITE = "view_site";
    private final String VIEW_TANK = "view_tank";
    private final String VIEW_BOX = "view_box";
    private final String VIEW_TRAY = "view_tray";
    private final String CHANGE_TRAY = "change_tray";
    private final String INVENTORY_VIEW_HISTORY = "inventory_view_history";
    private final String VIEW_HISTORY = "view_history";
    private final String BATCH_ALLOCATE = "batch_allocate";
    private final String OBJ_DELETED = "obj_deleted";
    private final String VIEW_TANK_ERROR = "view_tank_error";
    
    // channel's runtime & static data
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    private TrayManager mgrTray = null;
    
    private AuthToken authToken ;
    private String strSessionUniqueID;
    private String strActivity;
    private String strTarget;
    private String strCurrent;
    private String strXML = "";
    private String strPreviousXML = "";
    private String strStylesheet;
    private String strPreviousStylesheet;
    private boolean firstTime = false;
    private Hashtable hashExpanded = new Hashtable(50);
    private String strCurrentNode = "";
    private String strErrorMessage = "";
    private String strSource = "";
    private String intCurrentPatientID = "";
    private String intCurrentBiospecimenID = "";
    private String intCurrentCellID = "";
    private String strInitialBiospecSampleType = "";
    private String strBackButton = null;
    private int intSortType = 3; // 1 alphabetically; 2 availbility ; 3 date created						
    private String strCurrentID=""; //currently looked item ID either site, tank, box or tray. They type is determined by strCurrent
    private String  strCurrentFieldString = ""; 	
    /** Lock request object to handle record locking
     */
    private LockRequest lockRequest = null;
    
    private InventoryManager invmgr = null;
    
    /** Contructs the Inventory Channel
     */
    public CInventory()
    {
        this.strStylesheet = INVENTORY_VIEW;
    }
	
    /**
     *  Returns channel runtime properties.
     *  Satisfies implementation of Channel Interface.
     *
     *  @return handle to runtime properties
     */
    public ChannelRuntimeProperties getRuntimeProperties()
    {
        return new ChannelRuntimeProperties();
    }

    /**
     *  Process layout-level events coming from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param PortalEvent ev a portal layout event
     */
    public void receiveEvent(PortalEvent ev)
    {
        // If the user logout, destroy lock request object
        if (ev.getEventNumber() == PortalEvent.SESSION_DONE)
            clearLockRequest();
    }

    /**
     *  Receive static channel data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelStaticData sd static channel data
     */
    public void setStaticData(ChannelStaticData sd)
    {
        firstTime = true;
        this.staticData = sd;
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
        
        // channel's ids
        Context globalIDContext = null;
        try
        {
            // Get the context that holds the global IDs for this user
            globalIDContext = (Context)staticData.getJNDIContext().lookup("/channel-ids");
        }
        catch (NotContextException nce)
        {
            LogService.log(LogService.ERROR, "Could not find subcontext /channel-ids in JNDI");
        }
        catch (NamingException e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        try
        {
            strSessionUniqueID = authToken.getSessionUniqueID();
            SessionManager.addSession(strSessionUniqueID);
            SessionManager.addChannelID(strSessionUniqueID, "CBiospecimen", (String) globalIDContext.lookup("CBiospecimen"));
            SessionManager.addChannelID(strSessionUniqueID, "CDownload",
                                                (String) globalIDContext.lookup("CDownload"));
        } 
        catch (NotContextException nce)
        {
            LogService.log(LogService.ERROR, "Could not find channel ID for fname=Biospecimen");
        } 
        catch (NamingException e) 
        {
            LogService.log(LogService.ERROR, e);
        }
    }

    /**
     *  Receive channel runtime data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelRuntimeData rd handle to channel runtime data
     */
    public void setRuntimeData(ChannelRuntimeData rd)
    {
        try
        {
            this.runtimeData = rd;
            String strTempError = new String(this.strErrorMessage);
            strErrorMessage = "";
           
	   if(this.invmgr == null)
           {
                invmgr = new InventoryManager(this.authToken);
           }
	    //get the sort type
	    if (runtimeData.getParameter("sort_type") != null ){
	    	intSortType = Integer.parseInt(runtimeData.getParameter("sort_type"));
	 	}
            // Get the current page the users is on
            if(runtimeData.getParameter("current") != null)
            {
                firstTime = false;
                strCurrent = runtimeData.getParameter("current");
		//System.out.println(strCurrent);
            }
            else if (firstTime)
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                firstTime = false;
                strCurrent = INVENTORY_VIEW;
                strSource = "inventory";
            }
            else if (runtimeData.getParameter("source") == null || runtimeData.getParameter("source").equals("allocate"))
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                strCurrent = INVENTORY_VIEW;
                strSource = "inventory";
            }
            
            if (runtimeData.getParameter("source") != null)
            {
                strSource = runtimeData.getParameter("source");
                intCurrentPatientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
                intCurrentBiospecimenID = runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
                strInitialBiospecSampleType = runtimeData.getParameter("BIOSPECIMEN_strInitialBiospecSampleType");

                if (strSource.equals("view") || strSource.equals("change"))
                {
                    intCurrentCellID = runtimeData.getParameter("CELL_intCellID");
                    DALSecurityQuery query = new DALSecurityQuery("inventory_view", authToken);
                    query.setDomain("CELL", null, null, null);
                    query.setField("CELL_intTrayID", null);
                    query.setWhere(null, 0, "CELL_intCellID", "=", intCurrentCellID, 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsResultSet = query.executeSelect();
                    
                    if (rsResultSet.next())
                        runtimeData.setParameter("TRAY_intTrayID", rsResultSet.getString(1));
                    
                    rsResultSet.close();
                }
            }  
		System.err.println("strCurrent = " + strCurrent); 
           	 if (runtimeData.getParameter("current")== null) {
			//Take a look at current_node - see what we have
			if (strCurrentNode != null && !strCurrentNode.equals("")) {
				String current_mode = strCurrentNode.substring(0,strCurrentNode.indexOf("_"));
				String mode_number = strCurrentNode.substring(strCurrentNode.indexOf("_")+1);
				System.err.println("Got mode: " + current_mode + ", number: "+mode_number);
				if (current_mode.equals("SITE")) {
				strCurrent = VIEW_SITE;
				runtimeData.setParameter("SITE_intSiteID",mode_number);	
				}
				else if (current_mode.equals("TANK")) {
				strCurrent = VIEW_TANK;
				runtimeData.setParameter("TANK_intTankID",mode_number);	
				}
				else if (current_mode.equals("BOX")) {
				strCurrent = VIEW_BOX;	
				runtimeData.setParameter("BOX_intBoxID",mode_number);	
				}
				else if (current_mode.equals("TRAY")) {
				strCurrent = VIEW_TRAY;
				runtimeData.setParameter("TRAY_intTrayID",mode_number);	
				}
			}
		} 
            if (strCurrent != null)
            {
                if (runtimeData.getParameter("target") != null)
                {
                    String key = runtimeData.getParameter("target") + "_" + runtimeData.getParameter("id");
		    
		    if (runtimeData.getParameter("target").equals("SITE")){
		   	strCurrentFieldString = "SITE_strSiteID";
                    }
                    
		    else if (runtimeData.getParameter("target").equals("TANK")){
                        strCurrentFieldString = "TANK_strTankID";
                    }
                    else if (runtimeData.getParameter("target").equals("BOX")){
                        strCurrentFieldString = "BOX_strBoxID";
                    }
                    else strCurrentFieldString = "TRAY_strTrayID";
                    
                    strCurrentID = runtimeData.getParameter("id");
                    
					
		    
                    updateHashExpanded(key, false);
                    //putting back the error message if any.....since this is just an expanding of the tree
                    if(strTempError.length() > 0)
                    {
                        this.strErrorMessage = strTempError;
                    }
                    //return the target back to the server
                    strXML = strPreviousXML + "<target>"+runtimeData.getParameter("target") +"</target>" ;
                }
                else 
                {
                    
                    if (strCurrent.equals(INVENTORY_VIEW))
                    
                    {
                        // if the user doesn't have permission to view inventory
                        // display unauthorization message
                        if (!authToken.hasActivity(INVENTORY_VIEW))
                        {
                            strXML  = buildSecurityErrorXMLFile("view inventory");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        doViewInventory();
                    }
                    else if (strCurrent.equals(ADD_SITE))
                    {
                        // if the user doesn't have permission to insert inventory
                        // display unauthorization message
                        if (!authToken.hasActivity(INVENTORY_INSERT))
                        {
                            strXML  = buildSecurityErrorXMLFile("insert inventory element");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        // user wants to save new site
                        if (runtimeData.getParameter("save") != null)
                            doAddSite();
                        // display add site form
                        else
                            displayAddSiteForm();
                    }
                    else if (strCurrent.equals(VIEW_SITE))
                    {
                        
                        // user wants to update this site
                        if (runtimeData.getParameter("save") != null)
                        {
                            // if the user doesn't have permission to update inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_UPDATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("update inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                        
                            doUpdateSite();
                        }
                        // user wants to delete this site
                        else if (runtimeData.getParameter("delete") != null)
                        {
                            // if the user doesn't have permission to delete inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_DELETE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            doDeleteSite();
                        }
                        // display site details
                        else 
                        {
                            // if the user doesn't have permission to view inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_VIEW))
                            {
                                strXML  = buildSecurityErrorXMLFile("view inventory");
                                strStylesheet = SECURITY;
                                return;
                            }
                            displayViewSiteForm();
                        }
                    }
                    else if (strCurrent.equals(ADD_TANK))
                    {
                        // if the user doesn't have permission to insert inventory
                        // display unauthorization message
                        if (!authToken.hasActivity(INVENTORY_INSERT))
                        {
                            strXML  = buildSecurityErrorXMLFile("insert inventory element");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        // user wants to save new site
                        if (runtimeData.getParameter("save") != null)
                            doAddTank();
                        // display add tank form
                        else
                            displayAddTankForm();
                    }
                    else if (strCurrent.equals(VIEW_TANK))
                    {
                        // user wants to update this tank
                        if (runtimeData.getParameter("save") != null)
                        {
                            // if the user doesn't have permission to update inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_UPDATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("update inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            doUpdateTank();
                        }
                        // user wants to delete this tank
                        else if (runtimeData.getParameter("delete") != null)
                        {
                            // if the user doesn't have permission to delete inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_DELETE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            doDeleteTank();
                        }
                        // display tank details
                        else
                        {
                            // if the user doesn't have permission to view inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_VIEW))
                            {
                                strXML  = buildSecurityErrorXMLFile("view inventory");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            displayViewTankForm();
                        }
                    }
                    else if (strCurrent.equals(ADD_BOX))
                    {
                        // if the user doesn't have permission to insert inventory
                        // display unauthorization message
                        if (!authToken.hasActivity(INVENTORY_INSERT))
                        {
                            strXML  = buildSecurityErrorXMLFile("insert inventory element");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        // user wants to save new box
                        if (runtimeData.getParameter("save") != null)
                            doAddBox();
                        // display add box form
                        else
                            displayAddBoxForm();
                    }
                    else if (strCurrent.equals(VIEW_BOX))
                    {
                        // user wants to update this box
                        if (runtimeData.getParameter("save") != null)
                        {
                            // if the user doesn't have permission to update inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_UPDATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("update inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            doUpdateBox();
                        }
                        // user wants to delete this box
                        else if (runtimeData.getParameter("delete") != null)
                        {
                            // if the user doesn't have permission to delete inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_DELETE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            doDeleteBox();
                        }
                        // display box details
                        else
                        {
                            // if the user doesn't have permission to view inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_VIEW))
                            {
                                strXML  = buildSecurityErrorXMLFile("view inventory");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            displayViewBoxForm();
                        }
                    }
                    else if (strCurrent.equals(ADD_TRAY))
                    {
                        // if the user doesn't have permission to insert inventory
                        // display unauthorization message
                        if (!authToken.hasActivity(INVENTORY_INSERT))
                        {
                            strXML  = buildSecurityErrorXMLFile("insert inventory element");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        // user wants to save new tray
                        if (runtimeData.getParameter("save") != null)
                            doAddTray();
                        // display add tray form
                        else
                            displayAddTrayForm();
                    }
                    else if (strCurrent.equals(VIEW_TRAY))
                    {
                        // user wants to update this tray
                        if (runtimeData.getParameter("save") != null)
                        {
                            // if the user doesn't have permission to update inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_UPDATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("update inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            doUpdateTray();
                        }
                        // user wants to delete this tray
                        else if (runtimeData.getParameter("delete") != null)
                        {
                            // if the user doesn't have permission to delete inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_DELETE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete inventory element");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            doDeleteTray();
                        }
                        // user to change the cell locations
                        else if (runtimeData.getParameter("change_cells") != null)
                        {
                            // if the user doesn't have permission to change the inventory
                            // cell locations
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_CHANGE_LOCATION))
                            {
                                strXML  = buildSecurityErrorXMLFile("change biospecimen locations");
                                strStylesheet = SECURITY;
                                return;
                            }                            
                            if (mgrTray == null)
                               mgrTray = new TrayManager(authToken, lockRequest);
                            NGXRuntimeProperties rp = mgrTray.processRuntimeData(runtimeData);
                            
                            strXML = rp.getXML();
                            strStylesheet = rp.getStylesheet();
                        }
                        // display tray details
                        else
                        {
                            // if the user doesn't have permission to view inventory
                            // display unauthorization message
                            if (!authToken.hasActivity(INVENTORY_VIEW))
                            {
                                strXML  = buildSecurityErrorXMLFile("view inventory");
                                strStylesheet = SECURITY;
                                return;
                            }
                            
                            displayViewTrayForm();
                        }
                    }
                    else if (strCurrent.equals(CHANGE_TRAY))
                    {
                        NGXRuntimeProperties rp = mgrTray.processRuntimeData(runtimeData);
                        
                        strErrorMessage = rp.getErrorMessage(true);
                        strStylesheet = rp.getStylesheet();
                        if (strStylesheet.equals(VIEW_TRAY))
                        {
                            displayViewTrayForm();                        
                        }
                        else
                        {
                            strXML = rp.getXML();
                        }    
                    }  
		    else if (strCurrent.equals(BATCH_ALLOCATE)) {
			InventoryDetails id;
			int intCellID = -1;
			if (runtimeData.getParameter("CELL_intCellID") != null)
				intCellID =  Integer.parseInt(runtimeData.getParameter("CELL_intCellID"));
			String strBiospecimenID = runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID");
			int intBiospecimenID = BiospecimenUtilities.getBiospecimenKey(strBiospecimenID);
			//Check to see if biospecimen has already been allocated.
			String loc_result[] = InventoryUtilities.getBiospecimenLocation("biospecimen", intBiospecimenID, authToken); 
			if (intBiospecimenID != -1 && intCellID != -1 && loc_result[7].equals("-1")) {
				InventoryUtilities.saveBiospecimenLocation(intBiospecimenID, intCellID, authToken);
			}
			else
				System.err.println("Biospecimen " + intBiospecimenID + " already allocated");
			int intTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayID"));
			id = new InventoryDetails(-1,-1,-1,intTrayID);
			
			int v_cells[] = id.getAvailableCells(1);
			if (v_cells == null) 
				System.err.println("BATCH ALLOCATION - no cells left in this tray - tray_id = " + intTrayID);
			else
				intCellID = v_cells[0];
				
			displayBatchAllocateForm(authToken,intCellID,intTrayID);

	            } 
                    else if (strCurrent.equals(VIEW_HISTORY))
                    {
                        // if the user doesn't have permission to insert inventory
                        // TODO display unauthorization message
                        if (!authToken.hasActivity(INVENTORY_VIEW_HISTORY))
                        {
                            strXML  = buildSecurityErrorXMLFile("view inventory history");
                            strStylesheet = SECURITY;
                            return;
                        }

                        // user wants to view inventory history
                        displayHistoryForm(authToken);
                    }
                    
                    strPreviousXML = strXML;
                    strPreviousStylesheet = strStylesheet;
                }
            }   
            else
            {
                strXML = strPreviousXML;
                strStylesheet = strPreviousStylesheet;
            }
            
            strXML += "<strErrorMessage>" + strErrorMessage + "</strErrorMessage>";
            strXML += "<strSource>" + strSource + "</strSource>";
            
            if (strSource != null && strSource.equals("inventory") && 
                runtimeData.getParameter("vial_calc") != null)
            {
                strXML += "<blBackToVialCalc>true</blBackToVialCalc>";
            }
            else
            {
                strXML += "<blBackToVialCalc>false</blBackToVialCalc>";
            }
            
            strXML += "<intCurrentPatientID>" + intCurrentPatientID + "</intCurrentPatientID>";
            strXML += "<intCurrentBiospecimenID>" + intCurrentBiospecimenID + "</intCurrentBiospecimenID>";
            strXML += "<intCurrentCellID>" + intCurrentCellID + "</intCurrentCellID>";
            strXML += "<strInitialBiospecSampleType>" + strInitialBiospecSampleType+ "</strInitialBiospecSampleType>";
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><inventory>" + 
                     buildXMLFileForInventoryTree(buildInventoryTree()) +
                     strXML + buildXMLInventoryAdmin() + "</inventory>";
            
          
            
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    private void doViewInventory()
    {
        strStylesheet = INVENTORY_VIEW;
        // Navin: Need to define the inventory titles which will be used
        strXML = QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles"));
        // strXML = "";
 
    }
    
    private String buildXMLInventoryAdmin()
    {
        boolean adminRequired = false;
        String theXML = "";
        try 
        {
            if (authToken.hasActivity("inventory_addSite"))
            {
                theXML += "<intInventoryAddSite>1</intInventoryAddSite>";
                adminRequired = true;
            }

            if (authToken.hasActivity("inventory_addTank"))
            {
                theXML += "<intInventoryAddTank>1</intInventoryAddTank>";
                adminRequired = true;
            }

            if (authToken.hasActivity("inventory_addBox"))
            {
                theXML += "<intInventoryAddBox>1</intInventoryAddBox>";
                adminRequired = true;
            }

            if (authToken.hasActivity("inventory_addTray"))
            {
                theXML += "<intInventoryAddTray>1</intInventoryAddTray>";
                adminRequired = true;
            }
            if (authToken.hasActivity(INVENTORY_VIEW_HISTORY))
            {
                theXML += "<intInventoryHistory>1</intInventoryHistory>";
                adminRequired = true;
            }            
            if (adminRequired == true)
                theXML += "<intAdminSection>1</intAdminSection>";
            //System.out.println ("theXML = " + theXML);            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
        
        return theXML;
    }
    
    
    private DefaultMutableTreeNode buildInventoryTree()
    {
        DefaultMutableTreeNode inventoryTree = new DefaultMutableTreeNode("root");
        try
        {
            // unlock all records
//rennypv            clearLockRequest();
            
            // create new lock request
//rennypv            lockRequest = new LockRequest(authToken);
            
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
            
            // get SITES
            query.setDomain("SITE", null, null, null);
            Vector vtFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_site");
            query.setFields(vtFormFields, null);
            query.setOrderBy("SITE_intSiteID", "ASC");
            ResultSet rsResultSet = query.executeSelect();
            Vector vtResult = QueryChannel.lookupRecord(rsResultSet, vtFormFields);
            
            rsResultSet.close();
            
            for (int i=0; i < vtResult.size(); i++)
            {
                inventoryTree.add(new DefaultMutableTreeNode(vtResult.get(i)));
                String intSiteID = ((Hashtable) vtResult.get(i)).get("SITE_intSiteID").toString();
                
/*                if (strCurrentNode.equals("SITE_" + intSiteID))
                    lockRequest.addLock("SITE", intSiteID, LockRecord.READ_WRITE);
                else
                    lockRequest.addLock("SITE", intSiteID, LockRecord.READ_ONLY);*/
            }
            
            // get TANKS
            query.reset();
            query.setDomain("TANK", null, null, null);
            vtFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tank");
            query.setFields(vtFormFields, null);
            query.setOrderBy("TANK_intTankID", "ASC");
            rsResultSet = query.executeSelect();
            vtResult = QueryChannel.lookupRecord(rsResultSet, vtFormFields);
            rsResultSet.close();
            
            for (int i=0; i < vtResult.size(); i++)
            {
                Hashtable hashNewNode = (Hashtable) vtResult.get(i);
                Enumeration enum = inventoryTree.breadthFirstEnumeration();
                enum.nextElement();// skip the root
                
                
                while (enum.hasMoreElements())
                {
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) enum.nextElement();
                    Hashtable hashCurrentObject = (Hashtable) currentNode.getUserObject();

                    // new node is the parent of a tree node
                    if (hashCurrentObject.get("SITE_intSiteID") != null)
                    {
                        if (hashCurrentObject.get("SITE_intSiteID").toString().equals(
                            hashNewNode.get("TANK_intSiteID").toString()))
                        {
                            currentNode.add(new DefaultMutableTreeNode(hashNewNode));
                            
/*                            if (hashExpanded.containsKey("SITE_" + hashNewNode.get("TANK_intSiteID").toString()))
                            {
                                if (strCurrentNode.equals("TANK_" + hashNewNode.get("TANK_intTankID").toString()))
                                    lockRequest.addLock("TANK", hashNewNode.get("TANK_intTankID").toString(), LockRecord.READ_WRITE);
                                else
                                    lockRequest.addLock("TANK", hashNewNode.get("TANK_intTankID").toString(), LockRecord.READ_ONLY);
                            }*/
                            break;
                        }
                    }
                }
            }
            
            // get BOXES
            query.reset();
            query.setDomain("BOX", null, null, null);
            vtFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_box");
            query.setFields(vtFormFields, null);
            query.setOrderBy("BOX_intBoxID", "ASC");
            rsResultSet = query.executeSelect();
            vtResult = QueryChannel.lookupRecord(rsResultSet, vtFormFields);
            
            rsResultSet.close();
            
            for (int i=0; i < vtResult.size(); i++)
            {
                Hashtable hashNewNode = (Hashtable) vtResult.get(i);
                Enumeration enum = inventoryTree.breadthFirstEnumeration();
                enum.nextElement();// skip the root
                
                
                while (enum.hasMoreElements())
                {
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) enum.nextElement();
                    Hashtable hashCurrentObject = (Hashtable) currentNode.getUserObject();

                    // new node is the parent of a tree node
                    if (hashCurrentObject.get("TANK_intTankID") != null)
                    {
                        if (hashCurrentObject.get("TANK_intTankID").toString().equals(
                            hashNewNode.get("BOX_intTankID").toString()))
                        {
                            currentNode.add(new DefaultMutableTreeNode(hashNewNode));
                            
/*                            if (hashExpanded.containsKey("TANK_" + hashNewNode.get("BOX_intTankID").toString()))
                            {
                                if (strCurrentNode.equals("BOX_" + hashNewNode.get("BOX_intBoxID").toString()))
                                    lockRequest.addLock("BOX", hashNewNode.get("BOX_intBoxID").toString(), LockRecord.READ_WRITE);
                                else
                                    lockRequest.addLock("BOX", hashNewNode.get("BOX_intBoxID").toString(), LockRecord.READ_ONLY);
                            }*/
                            break;
                        }
                    }
                }
            }
            
            // get TRAYS
            query.reset();
            query.setDomain("TRAY", null, null, null);
            vtFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tray");
            query.setFields(vtFormFields, null);
            query.setOrderBy("TRAY_intTrayID", "ASC");
            rsResultSet = query.executeSelect();
            vtResult = QueryChannel.lookupRecord(rsResultSet, vtFormFields);
            
            rsResultSet.close();
            
            for (int i=0; i < vtResult.size(); i++)
            {
                Hashtable hashNewNode = (Hashtable) vtResult.get(i);
                Enumeration enum = inventoryTree.breadthFirstEnumeration();
                enum.nextElement();// skip the root
                
                
                while (enum.hasMoreElements())
                {
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) enum.nextElement();
                    Hashtable hashCurrentObject = (Hashtable) currentNode.getUserObject();

                    // new node is the parent of a tree node
                    if (hashCurrentObject.get("BOX_intBoxID") != null)
                    {
                        if (hashCurrentObject.get("BOX_intBoxID").toString().equals(
                            hashNewNode.get("TRAY_intBoxID").toString()))
                        {
                            currentNode.add(new DefaultMutableTreeNode(hashNewNode));
                            
/*                            if (hashExpanded.containsKey("BOX_" + hashNewNode.get("TRAY_intBoxID").toString()))
                            {
                                if (strCurrentNode.equals("TRAY_" + hashNewNode.get("TRAY_intTrayID").toString()))
                                    lockRequest.addLock("TRAY", hashNewNode.get("TRAY_intTrayID").toString(), LockRecord.READ_WRITE);
                                else
                                    lockRequest.addLock("TRAY", hashNewNode.get("TRAY_intTrayID").toString(), LockRecord.READ_ONLY);
                            }*/
                            break;
                        }
                    }
                }
            }
            
//            lockRequest.lockDelayWrite();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
        
        return inventoryTree;
    }
    
    public String buildXMLFileForInventoryTree(DefaultMutableTreeNode root)
    {
        String strResult = "";
        
	strResult += "<sort_type>" + intSortType + "</sort_type>";       
        strResult += "<current_view>" + strCurrent + "</current_view>";

	
	if (!root.isLeaf())
        {
            for (int i=0; i < root.getChildCount(); i++)
                strResult += getNodeXML((DefaultMutableTreeNode) root.getChildAt(i));
        }
        
	strResult += "<current_ID>" + strCurrentID + "</current_ID>";
	strResult += "<current_field_string>" + strCurrentFieldString +"</current_field_string>";
	return strResult;
    }
    
    public String getNodeXML(DefaultMutableTreeNode node)
    {
        String strResult = "";
        Hashtable hashTemp = (Hashtable) node.getUserObject();
        
	//add sort type
        switch (node.getLevel())
        {
            // a site node
            case 1:
                strResult += "<SITE_intSiteID>" + hashTemp.get("SITE_intSiteID") + "</SITE_intSiteID>";
                strResult += "<SITE_strSiteName>" + Utilities.cleanForXSL((String) hashTemp.get("SITE_strSiteName")) + "</SITE_strSiteName>";
                strResult += "<strType>site</strType>";
                
                if (hashExpanded.containsKey("SITE_" + hashTemp.get("SITE_intSiteID")))
                    strResult += "<site_expanded>true</site_expanded>";
                else
                    strResult += "<site_expanded>false</site_expanded>";
                
                if (strCurrentNode.equals("SITE_" + (String) hashTemp.get("SITE_intSiteID"))){
                    strResult += "<current_node>true</current_node>";
		    strCurrentID = hashTemp.get("SITE_intSiteID").toString();
		    strCurrentFieldString = "SITE_intSiteID";
		 }
                else
                    strResult += "<current_node>false</current_node>";
                    
                for (int i=0; i < node.getChildCount(); i++)
                    strResult += getNodeXML((DefaultMutableTreeNode) node.getChildAt(i));
                
                strResult = "<site>" + strResult + "</site>";
                break;
                
            // a tank node    
            case 2:
                int capacity = Integer.parseInt((String) hashTemp.get("TANK_intTankCapacity"));
                int available = Integer.parseInt((String) hashTemp.get("TANK_intTankAvailable"));
                
                if (hashTemp.get("TANK_strTimeStamp")!= null){
                   String strTime = hashTemp.get("TANK_strTimeStamp").toString();
                   
                   //convert string to sortable format by XSL
                    strResult += "<timestamp>" +  strTime.substring(4,6) +"/"+
                    Utilities.convertMonths(strTime.substring(0,3)) + "/" +
                    strTime.substring(7,11) + " " + strTime.substring(12,14) + ":"+
                    strTime.substring(15)+ "</timestamp>";
                    //System.out.println(strResult);
                    
                }
                int usage = calInventoryUsage(capacity, available);
                strResult += "<TANK_intTankID>" + hashTemp.get("TANK_intTankID") + "</TANK_intTankID>";
                strResult += "<TANK_strTankName>" + Utilities.cleanForXSL((String)hashTemp.get("TANK_strTankName")) + "</TANK_strTankName>";
                strResult += "<usage>" + usage + "</usage>";
                strResult += "<availbility>" + available + "</availbility>"; 

                if (hashExpanded.containsKey("TANK_" + hashTemp.get("TANK_intTankID")))
                    strResult += "<tank_expanded>true</tank_expanded>";
                else
                    strResult += "<tank_expanded>false</tank_expanded>";
                
                if (strCurrentNode.equals("TANK_" + (String) hashTemp.get("TANK_intTankID"))){
                    strResult += "<current_node>true</current_node>";
		    strCurrentID =  hashTemp.get("TANK_intTankID").toString();
		    strCurrentFieldString = "TANK_intTankID";
		}
                else
                    strResult += "<current_node>false</current_node>";
                
                for (int i=0; i < node.getChildCount(); i++)
                    strResult += getNodeXML((DefaultMutableTreeNode) node.getChildAt(i));
                
                strResult = "<tank>" + strResult + "</tank>";
                break;
                
            // a box node    
            case 3:
                capacity = Integer.parseInt((String) hashTemp.get("BOX_intBoxCapacity"));
                available = Integer.parseInt((String) hashTemp.get("BOX_intBoxAvailable"));
                
                if (hashTemp.get("BOX_strTimeStamp")!= null){
                   String strTime = hashTemp.get("BOX_strTimeStamp").toString();
                   
                   //convert string to timestamp
                     strResult += "<timestamp>" +  strTime.substring(4,6) +"/"+
                    Utilities.convertMonths(strTime.substring(0,3)) + "/" +
                    strTime.substring(7,11) + " " + strTime.substring(12,14) + ":"+
                    strTime.substring(15)+ "</timestamp>";
                    //System.out.println("BOX "+ strResult); 
                
                }
                usage = calInventoryUsage(capacity, available);
                strResult += "<BOX_intBoxID>" + hashTemp.get("BOX_intBoxID") + "</BOX_intBoxID>";
                strResult += "<BOX_strBoxName>" + Utilities.cleanForXSL((String) hashTemp.get("BOX_strBoxName")) + "</BOX_strBoxName>";
                strResult += "<usage>" + usage + "</usage>";
                
                strResult += "<availbility>" + available + "</availbility>"; 
                if (hashExpanded.containsKey("BOX_" + hashTemp.get("BOX_intBoxID")))
                    strResult += "<box_expanded>true</box_expanded>";
                else
                    strResult += "<box_expanded>false</box_expanded>";
                
                if (strCurrentNode.equals("BOX_" + (String) hashTemp.get("BOX_intBoxID"))){
                    strResult += "<current_node>true</current_node>";
		    strCurrentID = hashTemp.get("BOX_intBoxID").toString();
		    strCurrentFieldString = "BOX_intBoxID";
		 }
                else
                    strResult += "<current_node>false</current_node>";
                    
                for (int i=0; i < node.getChildCount(); i++)
                    strResult += getNodeXML((DefaultMutableTreeNode) node.getChildAt(i));
               
                strResult = "<box>" + strResult + "</box>";
                break;
                
            // a tray node    
            case 4:
                capacity = Integer.parseInt((String) hashTemp.get("TRAY_intTrayCapacity"));
                available = Integer.parseInt((String) hashTemp.get("TRAY_intTrayAvailable"));
                if (hashTemp.get("TRAY_strTimeStamp")!= null){
                    
                    String strTime = hashTemp.get("TRAY_strTimeStamp").toString();
                    strResult += "<timestamp>" +  strTime.substring(4,6) +"/"+
                    Utilities.convertMonths(strTime.substring(0,3)) + "/" +
                    strTime.substring(7,11) + " " + strTime.substring(12,14) + ":"+
                    strTime.substring(15)+ "</timestamp>";
                    //System.out.println("TRAY" + strResult);
                }
                
                usage = calInventoryUsage(capacity, available);
                strResult += "<TRAY_intTrayID>" + hashTemp.get("TRAY_intTrayID") + "</TRAY_intTrayID>";
                strResult += "<TRAY_strTrayName>" + Utilities.cleanForXSL((String) hashTemp.get("TRAY_strTrayName")) + "</TRAY_strTrayName>";
                strResult += "<usage>" + usage + "</usage>";
                
                strResult += "<availbility>" + available   + "</availbility>"; 
                if (strCurrentNode.equals("TRAY_" + (String) hashTemp.get("TRAY_intTrayID"))){
                    strResult += "<current_node>true</current_node>";
		    strCurrentID =  hashTemp.get("TRAY_intTrayID").toString();
		    strCurrentFieldString = "TRAY_intTrayID";
		 }
                else
                    strResult += "<current_node>false</current_node>";;
                    
                strResult = "<tray>" + strResult + "</tray>";
               
        }
        
	
        return strResult;
    }
    
    /** display add new SITE form
     */
    private void displayAddSiteForm()
    {
        strStylesheet = ADD_SITE;
        Vector vtAddSiteFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_site");
        
        try
        {
            strXML = QueryChannel.buildFormLabelXMLFile(vtAddSiteFormFields) +
                     QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                     QueryChannel.buildAddFormXMLFile(vtAddSiteFormFields);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** add new SITE
     */
    private void doAddSite()
    {
        Vector vtAddSiteFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_site");
        
        try
        {
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddSiteFormFields, runtimeData);
            String strCheckDuplicates = "";
            if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
            {
                strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_SITE_CHECK,runtimeData.getParameter("SITE_strSiteName"),runtimeData.getParameter("SITE_intSiteID"),null);
            }
            // if all required fields values are supplied. Do insert
            if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
            {
                strStylesheet = VIEW_SITE;
                DALSecurityQuery query = new DALSecurityQuery(INVENTORY_INSERT, authToken);

                // add new site
                query.setDomain("SITE", null, null, null);    
                query.setFields(vtAddSiteFormFields, runtimeData);
                query.executeInsert();

                int intCurrentSiteID = QueryChannel.getNewestKeyAsInt(query);
                String strCurrentSiteID = QueryChannel.getNewestKeyAsString(query);
                strCurrentNode = "SITE_" + intCurrentSiteID;
                this.setLocking("SITE", strCurrentSiteID);
                strXML = QueryChannel.buildFormLabelXMLFile(vtAddSiteFormFields) +
                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                         QueryChannel.buildViewAfterAddXMLFile(vtAddSiteFormFields, runtimeData, "SITE_intSiteID", intCurrentSiteID);
            }
            else
            {
                strStylesheet = ADD_SITE;
                displayAddSiteForm();
                if(strCheckRequiredFields != null)
                {
                    strErrorMessage = strCheckRequiredFields;
                }
                else if(strCheckDuplicates.length() != 0)
                {
                    strErrorMessage = strCheckDuplicates;
                }
                Site stObj = this.invmgr.createSite(this.runtimeData);
                if(stObj != null)
                {
                    strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                } 
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
       
    /** display view SITE form
     */
    private void displayViewSiteForm()
    {
        strStylesheet = VIEW_SITE;
        this.clearLockRequest();
        this.lockRequest = new LockRequest(this.authToken);
        Vector vtViewSiteFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_site");

	int intSiteID = Integer.parseInt(runtimeData.getParameter("SITE_intSiteID"));
	
	
        updateHashExpanded("SITE_" + runtimeData.getParameter("SITE_intSiteID"), true);
        strCurrentNode = "SITE_" + runtimeData.getParameter("SITE_intSiteID");
        String strObjExistsError = "";
        try
        {
            if((strObjExistsError = this.invmgr.objectExists("SITE",runtimeData.getParameter("SITE_intSiteID"))).length() == 0)
            {
                this.lockRequest.addLock("SITE", runtimeData.getParameter("SITE_intSiteID"), LockRecord.READ_WRITE);
                this.lockRequest.lockDelayWrite();
                strXML = QueryChannel.buildFormLabelXMLFile(vtViewSiteFormFields) +
                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                         QueryChannel.buildViewFromKeyXMLFile(vtViewSiteFormFields, "SITE_intSiteID", intSiteID);
            }
            else
            {
                this.strStylesheet = this.OBJ_DELETED;
                this.strXML = new String();
                this.strXML = QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles"));
                this.strErrorMessage = strObjExistsError;
            }
//NAVIN
// LogService.instance().log("strXML = " + strXML);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** update a SITE
     */
    private void doUpdateSite()
    {
        strStylesheet = VIEW_SITE;
        Vector vtUpdateSiteFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_site");
        
        try
        {
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtUpdateSiteFormFields, runtimeData);
            String strCheckDuplicates = "";
            if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
            {
                strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_SITE_CHECK,runtimeData.getParameter("SITE_strSiteName"),runtimeData.getParameter("SITE_intSiteID"),null);
            }
            // if all required fields values are supplied. Do insert
            if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
            {
                if (lockRequest.isValid() && lockRequest.lockWrites())
                {

                    DALSecurityQuery query = new DALSecurityQuery(INVENTORY_UPDATE, authToken);

                    // add new site
                    query.setDomain("SITE", null, null, null);    
                    query.setFields(vtUpdateSiteFormFields, runtimeData);
                    query.setWhere(null, 0, "SITE_intSiteID", "=", runtimeData.getParameter("SITE_intSiteID"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();

                    // set the lock to READ_LOCK
                    lockRequest.unlockWrites();

                    strXML = QueryChannel.buildFormLabelXMLFile(vtUpdateSiteFormFields) +
                             QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                             QueryChannel.buildViewAfterUpdateXMLFile(vtUpdateSiteFormFields, runtimeData, "SITE_intSiteID") +
                             "<blLockError>false</blLockError>";
                }
                else
                    strXML = QueryChannel.buildFormLabelXMLFile(vtUpdateSiteFormFields) +
                             QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                             QueryChannel.buildViewAfterUpdateXMLFile(vtUpdateSiteFormFields, runtimeData, "SITE_intSiteID") +
                             "<blLockError>true</blLockError>";
            }
            else
            {
                strXML = QueryChannel.buildFormLabelXMLFile(vtUpdateSiteFormFields) +
                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                         QueryChannel.buildViewAfterUpdateXMLFile(vtUpdateSiteFormFields, runtimeData, "SITE_intSiteID") +
                         "<blLockError>false</blLockError>";
                if(strCheckRequiredFields != null)
                {
                    strErrorMessage = strCheckRequiredFields;             
                }
                else if(strCheckDuplicates.length() != 0)
                {
                    strErrorMessage = strCheckDuplicates;
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Delete the current site
     */
    private void doDeleteSite()
    {
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_DELETE, authToken);
            query.setDomain("TANK", null, null, null);
            query.setField("TANK_intSiteID", null);
            query.setWhere(null, 0, "TANK_intSiteID", "=", runtimeData.getParameter("SITE_intSiteID"), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            // can not delete an unempty site
            if (rsResult.next())
            {
                strErrorMessage = "This site contains tanks. You can not delete it.";
                rsResult.close();
                displayViewSiteForm();
            }
            else
            {
                rsResult.close();
                // if we can lock this site for delete. Delete the site
                if (lockRequest.isValid() && lockRequest.lockWrites())
                {
                    query.reset();
                    query.setDomain("SITE", null, null, null);
                    query.setWhere(null, 0, "SITE_intSiteID", "=", runtimeData.getParameter("SITE_intSiteID"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeDelete();
                    lockRequest.unlockWrites();
                    
                    doViewInventory();
                }
                // if not, send lock-error message
                else
                {
                    displayViewSiteForm();
                    strXML += "<blLockError>true</blLockError>";
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    
    /** display add new TANK form
     */
    private void displayAddTankForm()
    {
        strStylesheet = ADD_TANK;
        Vector vtAddTankFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_tank");
        Vector vtListSiteFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_site");
        
        try
        {
            // build Site dropdown list
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
            query.setDomain("SITE", null, null, null);
            query.setFields(vtListSiteFormFields, null);
            query.setOrderBy("SITE_strSiteName", "ASC");
            ResultSet rsSiteList = query.executeSelect();
            
            strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) + 
                     QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                     QueryChannel.buildFormLabelXMLFile(vtAddTankFormFields) +
                     QueryChannel.buildAddFormXMLFile(vtAddTankFormFields);
            
            rsSiteList.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** add new TANK form
     */
    private void doAddTank()
    {
        Vector vtAddTankFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_tank");
        Vector vtViewTankFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tank");
        Vector vtListSiteFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_site");
        int intCurrentTankID = 0;
        String strObjExistsError="";
        try
        {
            this.setLocking("SITE", runtimeData.getParameter("TANK_intSiteID"));
            if((strObjExistsError = this.invmgr.objectExists("SITE",runtimeData.getParameter("TANK_intSiteID"))).length() == 0)
            {
                String dtCommissionDate = runtimeData.getParameter("TANK_dtCommissionDate_Day") + "/" +
                                                  runtimeData.getParameter("TANK_dtCommissionDate_Month") + "/" +
                                                  runtimeData.getParameter("TANK_dtCommissionDate_Year");

                String dtLastServiceDate = runtimeData.getParameter("TANK_dtLastServiceDate_Day") + "/" +
                                                  runtimeData.getParameter("TANK_dtLastServiceDate_Month") + "/" +
                                                  runtimeData.getParameter("TANK_dtLastServiceDate_Year");
                runtimeData.setParameter("TANK_dtCommissionDate", dtCommissionDate);
                runtimeData.setParameter("TANK_dtLastServiceDate", dtLastServiceDate);

                if(this.lockRequest.isValid() && this.lockRequest.lockWrites())
                {
                        DALSecurityQuery query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                        query.setDomain("SITE", null, null, null);
                        query.setFields(vtListSiteFormFields, null);
                        query.setOrderBy("SITE_strSiteName", "ASC");
                        ResultSet rsSiteList = query.executeSelect();

                        String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddTankFormFields, runtimeData);
                        String strCheckDuplicates = "";
                        if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
                        {
                            strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_TANK_CHECK, runtimeData.getParameter("TANK_strTankName"),runtimeData.getParameter("TANK_intTankID"),runtimeData.getParameter("TANK_intSiteID"));
                        }
                        // if all required fields values are supplied. Do insert
                        if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
                        {
                            // checks data validation
                            String strValidation = QueryChannel.validateData(vtAddTankFormFields, runtimeData);
                            // if all data is OK, insert new tank
                            if (strValidation == null)
                            {
                                strStylesheet = VIEW_TANK;

                                // add new tank
                                DALSecurityQuery qryAddTank = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                qryAddTank.setDomain("TANK", null, null, null);    
                                qryAddTank.setFields(vtAddTankFormFields, runtimeData);
                                qryAddTank.executeInsert();

                                intCurrentTankID = QueryChannel.getNewestKeyAsInt(qryAddTank);
                                String strCurrentTankID = QueryChannel.getNewestKeyAsString(qryAddTank);
                                strCurrentNode = "TANK_" + intCurrentTankID;
                                updateHashExpanded("SITE_" + runtimeData.getParameter("TANK_intSiteID"), true);
                                setLocking("TANK",strCurrentTankID);
                                strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                         QueryChannel.buildFormLabelXMLFile(vtViewTankFormFields) +
                                         QueryChannel.buildViewFromKeyXMLFile(vtViewTankFormFields, "TANK_intTankID", intCurrentTankID);

                            }
                            // if there is an error on data, display error message
                            else
                            {
                                strStylesheet = ADD_TANK;
                                strErrorMessage = strValidation;

                                strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                         QueryChannel.buildFormLabelXMLFile(vtAddTankFormFields) +
                                         QueryChannel.buildViewXMLFile(vtAddTankFormFields, runtimeData);
                                this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                                Site stObj = this.invmgr.createSite(this.runtimeData);
                                if(stObj != null)
                                {
                                    strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                                } 
                            }
                        }
                        // if required fields values are not supplied. Display error message
                        else
                        {
                            strStylesheet = ADD_TANK;
                            if(strCheckRequiredFields != null)
                            {
                                strErrorMessage = strCheckRequiredFields;
                            }
                            else if(strCheckDuplicates.length() != 0)
                            {
                                strErrorMessage = strCheckDuplicates;
                            }
                            strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                                     QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                     QueryChannel.buildFormLabelXMLFile(vtAddTankFormFields) +
                                     QueryChannel.buildViewXMLFile(vtAddTankFormFields, runtimeData);
                                this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                                 Site stObj = this.invmgr.createSite(this.runtimeData);
                                if(stObj != null)
                                {
                                    strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                                } 
                        }
                        rsSiteList.close();
                        this.lockRequest.unlockWrites();                    
                }
                else
                {
                    strErrorMessage = "There is a write lock on the object to which this is being added. Please try again later";
                    this.displayAddTankForm();
                    this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                     Site stObj = this.invmgr.createSite(this.runtimeData);
                    if(stObj != null)
                    {
                        strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                    } 
                }
            }
            else
            {
                strErrorMessage = strObjExistsError;
                this.displayAddTankForm();
                this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                 Site stObj = this.invmgr.createSite(this.runtimeData);
                if(stObj != null)
                {
                    strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                } 
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Display tank details
     */
    private void displayViewTankForm()
    {
        strStylesheet = VIEW_TANK;
        this.clearLockRequest();
        this.lockRequest = new LockRequest(this.authToken);
        Vector vtViewTankFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tank");
        Vector vtListSiteFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_site");
		
	int intTankID = Integer.parseInt(runtimeData.getParameter("TANK_intTankID"));
        updateHashExpanded("TANK_" + runtimeData.getParameter("TANK_intTankID"), true);
        strCurrentNode = "TANK_" + runtimeData.getParameter("TANK_intTankID");
        String strObjExistsError = "";
        try
        {
            if((strObjExistsError = this.invmgr.objectExists("TANK",runtimeData.getParameter("TANK_intTankID"))).length() == 0)
            {
                lockRequest.addLock("TANK", runtimeData.getParameter("TANK_intTankID"), LockRecord.READ_WRITE);
                lockRequest.lockDelayWrite();
                DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
                query.setDomain("SITE", null, null, null);
                query.setFields(vtListSiteFormFields, null);
                query.setOrderBy("SITE_strSiteName", "ASC");
                ResultSet rsSiteList = query.executeSelect();

                strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                         QueryChannel.buildFormLabelXMLFile(vtViewTankFormFields) +
                         QueryChannel.buildViewFromKeyXMLFile(vtViewTankFormFields, "TANK_intTankID", intTankID);

                rsSiteList.close();
            }
            else
            {
                this.strStylesheet = this.OBJ_DELETED;
                this.strXML = new String();
                this.strXML = QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles"));
                this.strErrorMessage = strObjExistsError;
            }
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Update tank details
     */
    private void doUpdateTank()
    {
        strStylesheet = VIEW_TANK;
        Vector vtUpdateTankFormFields = DatabaseSchema.getFormFields("cinventory_update_inventory_tank");
        Vector vtViewTankFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tank");
        Vector vtListSiteFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_site");
        int intCurrentTankID = Integer.parseInt(runtimeData.getParameter("TANK_intTankID"));
        String strObjExistsError = "";
        try
        {
            String dtCommissionDate = runtimeData.getParameter("TANK_dtCommissionDate_Day") + "/" +
                                      runtimeData.getParameter("TANK_dtCommissionDate_Month") + "/" +
                                      runtimeData.getParameter("TANK_dtCommissionDate_Year");

            String dtDecommissionDate = runtimeData.getParameter("TANK_dtDecommissionDate_Day") + "/" +
                                      runtimeData.getParameter("TANK_dtDecommissionDate_Month") + "/" +
                                      runtimeData.getParameter("TANK_dtDecommissionDate_Year");

            String dtLastServiceDate = runtimeData.getParameter("TANK_dtLastServiceDate_Day") + "/" +
                                      runtimeData.getParameter("TANK_dtLastServiceDate_Month") + "/" +
                                      runtimeData.getParameter("TANK_dtLastServiceDate_Year");

            runtimeData.setParameter("TANK_dtCommissionDate", dtCommissionDate);
            runtimeData.setParameter("TANK_dtDecommissionDate", dtDecommissionDate);
            runtimeData.setParameter("TANK_dtLastServiceDate", dtLastServiceDate);
            if((strObjExistsError = this.invmgr.objectExists("SITE",runtimeData.getParameter("TANK_intSiteID"))).length() == 0)
            {
                    this.lockRequest.addLock("SITE", runtimeData.getParameter("TANK_intSiteID"),LockRecord.READ_WRITE);
                    this.lockRequest.lockDelayWrite();
                    String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtUpdateTankFormFields, runtimeData);
                    String strCheckDuplicates = "";
                    if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
                    {
                        strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_TANK_CHECK, runtimeData.getParameter("TANK_strTankName"),runtimeData.getParameter("TANK_intTankID"),runtimeData.getParameter("TANK_intSiteID"));
                    }
                    // if all required fields values are supplied. Do insert
                    if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
                    {
                        // checks data validation
                        String strValidation = QueryChannel.validateData(vtUpdateTankFormFields, runtimeData);
                        // if all data is OK, insert new tank
                        if (strValidation == null)
                        {
                            // we can lock the record for update
                            if (lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                DALSecurityQuery query = new DALSecurityQuery(INVENTORY_DELETE, authToken);

                                // update Tank
                                query.setDomain("TANK", null, null, null);    
                                query.setFields(vtUpdateTankFormFields, runtimeData);
                                query.setWhere(null, 0, "TANK_intTankID", "=", runtimeData.getParameter("TANK_intTankID"), 0, DALQuery.WHERE_HAS_VALUE);
                                query.executeUpdate();

                                // set the lock to READ_LOCK
                                lockRequest.unlockWrites();

                                query.reset();
                                query.setDomain("SITE", null, null, null);
                                query.setFields(vtListSiteFormFields, null);
                                query.setOrderBy("SITE_strSiteName", "ASC");
                                ResultSet rsSiteList = query.executeSelect();
                                this.setLocking("TANK", runtimeData.getParameter("TANK_intTankID"));
                                strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                         QueryChannel.buildFormLabelXMLFile(vtViewTankFormFields) +
                                         QueryChannel.buildViewFromKeyXMLFile(vtViewTankFormFields, "TANK_intTankID", intCurrentTankID) +
                                         "<blLockError>false</blLockError>";

                                rsSiteList.close();
                            }
                            // we can not lock the record since it is being viewed by others
                            else
                            {
                                this.strStylesheet = this.VIEW_TANK_ERROR;
                                DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);

                                //query.reset();
                                query.setDomain("SITE", null, null, null);
                                query.setFields(vtListSiteFormFields, null);
                                query.setOrderBy("SITE_strSiteName", "ASC");
                                ResultSet rsSiteList = query.executeSelect();
                                this.setLocking("TANK", runtimeData.getParameter("TANK_intTankID"));
                                strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                         QueryChannel.buildFormLabelXMLFile(vtViewTankFormFields) +
                                         QueryChannel.buildViewFromKeyXMLFile(vtViewTankFormFields, "TANK_intTankID", intCurrentTankID) +
                                         "<blLockError>true</blLockError>";
                                this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                                 Site stObj = this.invmgr.createSite(this.runtimeData);
                                if(stObj != null)
                                {
                                    strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                                } 
                                rsSiteList.close();
                            }
                        }
                        else
                        {
                            this.strStylesheet = this.VIEW_TANK_ERROR;
                            strErrorMessage = strValidation;
                            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);

                            //query.reset();
                            query.setDomain("SITE", null, null, null);
                            query.setFields(vtListSiteFormFields, null);
                            query.setOrderBy("SITE_strSiteName", "ASC");
                            ResultSet rsSiteList = query.executeSelect();
                            this.setLocking("TANK", runtimeData.getParameter("TANK_intTankID"));
                            strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                                     QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                     QueryChannel.buildFormLabelXMLFile(vtViewTankFormFields) +
                                     QueryChannel.buildViewFromKeyXMLFile(vtViewTankFormFields, "TANK_intTankID", intCurrentTankID) +
                                     "<blLockError>false</blLockError>";
                            this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                            Site stObj = this.invmgr.createSite(this.runtimeData);
                            if(stObj != null)
                            {
                                strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                            } 
                            rsSiteList.close();
                        }
                    }
                    else
                    {
                        this.strStylesheet = this.VIEW_TANK_ERROR;
                        if(strCheckRequiredFields != null)
                        {
                            strErrorMessage = strCheckRequiredFields;
                        }
                        else if(strCheckDuplicates.length() != 0)
                        {
                            strErrorMessage = strCheckDuplicates;
                        }
                        this.setLocking("TANK", runtimeData.getParameter("TANK_intTankID"));
                        DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);

                        //query.reset();
                        query.setDomain("SITE", null, null, null);
                        query.setFields(vtListSiteFormFields, null);
                        query.setOrderBy("SITE_strSiteName", "ASC");
                        ResultSet rsSiteList = query.executeSelect();

                        strXML = QueryChannel.buildSearchXMLFile("search_site", rsSiteList, vtListSiteFormFields) +
                                 QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                 QueryChannel.buildFormLabelXMLFile(vtViewTankFormFields) +
                                 QueryChannel.buildViewFromKeyXMLFile(vtViewTankFormFields, "TANK_intTankID", intCurrentTankID) +
                                 "<blLockError>false</blLockError>";
                        this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                         Site stObj = this.invmgr.createSite(this.runtimeData);
                        if(stObj != null)
                        {
                            strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                        } 
                        rsSiteList.close();
                    }
            }
            else
             {
                 this.strStylesheet = this.VIEW_TANK_ERROR;
                strErrorMessage = strObjExistsError;
                this.displayViewTankForm();
                this.runtimeData.setParameter("SITE_intSiteID",runtimeData.getParameter("TANK_intSiteID"));
                Site stObj = this.invmgr.createSite(this.runtimeData);
                if(stObj != null)
                {
                    strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                } 
             }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Delete the current Tank
     */
    private void doDeleteTank()
    {
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_DELETE, authToken);
            query.setDomain("BOX", null, null, null);
            query.setField("BOX_intTankID", null);
            query.setWhere(null, 0, "BOX_intTankID", "=", runtimeData.getParameter("TANK_intTankID"), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            // can not delete an unempty tank
            if (rsResult.next())
            {
                strErrorMessage = "This tank contains boxes. You can not delete it.";
                displayViewTankForm();
            }
            else
            {
                // if we can lock this tank for delete. Delete the tank
                if (lockRequest.isValid() && lockRequest.lockWrites())
                {
                    query.reset();
                    query.setDomain("TANK", null, null, null);
                    query.setWhere(null, 0, "TANK_intTankID", "=", runtimeData.getParameter("TANK_intTankID"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeDelete();
                    lockRequest.unlockWrites();
                    
                    doViewInventory();
                }
                // if not, send lock-error message
                else
                {
                    displayViewTankForm();
                    strXML += "<blLockError>true</blLockError>";
                }
            }
            rsResult.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Display add box form
     */
    public void displayAddBoxForm()
    {
        strStylesheet = ADD_BOX;
        Vector vtAddBoxFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_box");
        Vector vtListTankFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_tank");
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
            query.setDomain("TANK", null, null, null);
            query.setFields(vtListTankFormFields, null);
            query.setOrderBy("TANK_strTankName", "ASC");
            ResultSet rsTankList = query.executeSelect();
            
            strXML = QueryChannel.buildSearchXMLFile("search_tank", rsTankList, vtListTankFormFields) +
                     QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                     QueryChannel.buildFormLabelXMLFile(vtAddBoxFormFields) +
                     QueryChannel.buildAddFormXMLFile(vtAddBoxFormFields);
            
            rsTankList.close();
            strXML += invmgr.getInventoryDetails(InventoryManager.TYPE_LIST_SITE_N_TANKS_NAMES, INVENTORY_VIEW, authToken);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Add new box
     */
    public void doAddBox()
    {
        
        Vector vtAddBoxFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_box");
        Vector vtViewBoxFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_box");
        Vector vtListTankFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_tank");
        
        try
        { 
            this.setLocking("TANK", runtimeData.getParameter("BOX_intTankID"));
            String strObjExistsError = "";
            if((strObjExistsError = this.invmgr.objectExists("TANK",runtimeData.getParameter("BOX_intTankID"))).length() == 0)
            {
                    if(this.lockRequest.isValid() && this.lockRequest.lockWrites())
                    {
                        String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddBoxFormFields, runtimeData);
                        String strCheckDuplicates = "";
                        if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
                        {
                            strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_BOX_CHECK, runtimeData.getParameter("BOX_strBoxName"),runtimeData.getParameter("BOX_intBoxID"),runtimeData.getParameter("BOX_intTankID"));
                        }
                        // if all required fields values are supplied. Do insert
                        if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
                        {
                            strStylesheet = VIEW_BOX;

                            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_INSERT, authToken);

                            // add new box
                            query.setDomain("BOX", null, null, null);    
                            query.setFields(vtAddBoxFormFields, runtimeData);
                            query.executeInsert();

                            int intCurrentBoxID = QueryChannel.getNewestKeyAsInt(query);
                            String strCurrentBoxID = QueryChannel.getNewestKeyAsString(query);
                            strCurrentNode = "BOX_" + intCurrentBoxID;
                            updateHashExpanded("TANK_" + runtimeData.getParameter("BOX_intTankID"), true);

                            query.reset();
                            query.setDomain("TANK", null, null, null);  
                            query.setField("TANK_intSiteID", null);
                            query.setWhere(null, 0, "TANK_intTankID", "=", runtimeData.getParameter("BOX_intTankID"), 0, DALQuery.WHERE_HAS_VALUE);
                            ResultSet rsThisTank = query.executeSelect();
                            if (rsThisTank.next())
                                updateHashExpanded("SITE_" + rsThisTank.getString(1), true);

                            rsThisTank.close();
                            this.lockRequest.unlockWrites();                    
                            // build Tank dropdown list
                            query.reset();
                            query.setDomain("TANK", null, null, null);
                            query.setFields(vtListTankFormFields, null);
                            query.setOrderBy("TANK_strTankName", "ASC");
                            ResultSet rsTankList = query.executeSelect();
                            this.setLocking("BOX", strCurrentBoxID);
                            strXML = QueryChannel.buildSearchXMLFile("search_tank", rsTankList, vtListTankFormFields) +
                                     QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                     QueryChannel.buildFormLabelXMLFile(vtViewBoxFormFields) +
                                     QueryChannel.buildViewFromKeyXMLFile(vtViewBoxFormFields, "BOX_intBoxID", intCurrentBoxID);

                            rsTankList.close();
                            strXML += invmgr.getInventoryDetails(InventoryManager.TYPE_LIST_SITE_N_TANKS_NAMES, INVENTORY_VIEW, authToken);
                        }
                        else
                        {
                            if(strCheckRequiredFields != null)
                            {
                                strErrorMessage = strCheckRequiredFields;
                            }
                            else if(strCheckDuplicates.length() != 0)
                            {
                                strErrorMessage = strCheckDuplicates;
                            }
                            displayAddBoxForm();
                            
                            runtimeData.setParameter("TANK_intTankID",runtimeData.getParameter("BOX_intTankID"));
                            Site stObj = this.invmgr.createSite(this.runtimeData);
                            if(stObj != null)
                            {
                                strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                            }
                            this.lockRequest.unlockWrites();
                        }
                }
                else
                {
                    strErrorMessage = "There is a write lock on the object to which this is being added. Please try again later";
                    displayAddBoxForm();
                    runtimeData.setParameter("TANK_intTankID",runtimeData.getParameter("BOX_intTankID"));
                    Site stObj = this.invmgr.createSite(this.runtimeData);
                    if(stObj != null)
                    {
                        strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                    }
                }
            }
            else
            {
                strErrorMessage = strObjExistsError;
                displayAddBoxForm();
                runtimeData.setParameter("TANK_intTankID",runtimeData.getParameter("BOX_intTankID"));
                Site stObj = this.invmgr.createSite(this.runtimeData);
                if(stObj != null)
                {
                    strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Update box details
     */
    public void doUpdateBox()
    {
        strStylesheet = VIEW_BOX;
        Vector vtUpdateBoxFormFields = DatabaseSchema.getFormFields("cinventory_update_inventory_box");
        Vector vtViewBoxFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_box");
        Vector vtListTankFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_tank");
        int intCurrentBoxID = Integer.parseInt(runtimeData.getParameter("BOX_intBoxID"));
        int intNewTankID = Integer.parseInt(runtimeData.getParameter("BOX_intTankID"));
        int intOldTankID = intNewTankID;
        String strObjExistsError = "";
        try
        {
             if((strObjExistsError = this.invmgr.objectExists("TANK",runtimeData.getParameter("BOX_intTankID"))).length() == 0)
            {
                this.lockRequest.addLock("TANK", runtimeData.getParameter("BOX_intTankID"),LockRecord.READ_WRITE);
                this.lockRequest.lockDelayWrite();
                String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtUpdateBoxFormFields, runtimeData);
                String strCheckDuplicates = "";
                if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
                {
                    strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_BOX_CHECK, runtimeData.getParameter("BOX_strBoxName"),runtimeData.getParameter("BOX_intBoxID"),runtimeData.getParameter("BOX_intTankID"));
                }
                // if all required fields values are supplied. Do insert
                if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
                {
                    if (lockRequest.isValid() && lockRequest.lockWrites())
                    {
                        DALSecurityQuery query = new DALSecurityQuery(INVENTORY_UPDATE, authToken);

                        // get the old tank ID
                        query.setDomain("BOX", null, null, null);
                        query.setField("BOX_intTankID", null);
                        query.setWhere(null, 0, "BOX_intBoxID", "=", runtimeData.getParameter("BOX_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
                        ResultSet rsThisBox = query.executeSelect();
                        if (rsThisBox.next())
                            intOldTankID = rsThisBox.getInt(1);

                        rsThisBox.close();

                        // update Box
                        query.reset();
                        query.setDomain("BOX", null, null, null);    
                        query.setFields(vtUpdateBoxFormFields, runtimeData);
                        query.setWhere(null, 0, "BOX_intBoxID", "=", runtimeData.getParameter("BOX_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
                        query.executeUpdate();

                        // set the lock to READ_LOCK
                        lockRequest.unlockWrites();

                        // update Tank capacity
                        if (intNewTankID != intOldTankID)
                        {
                            query.reset();
                            query.setDomain("BOX", null, null, null); 
                            query.setSumField("BOX_intBoxCapacity");
                            query.setSumField("BOX_intBoxAvailable");
                            query.setWhere(null, 0, "BOX_intTankID", "=", new Integer(intNewTankID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                            ResultSet rsResultSet = query.executeSelect();

                            if (rsResultSet.next())
                            {
                                String strTankCapacity = rsResultSet.getString("SUM_BOX_intBoxCapacity");
                                String strTankAvailable = rsResultSet.getString("SUM_BOX_intBoxAvailable");

                                query.reset();
                                query.setDomain("TANK", null, null, null);
                                query.setField("TANK_intTankCapacity", strTankCapacity);
                                query.setField("TANK_intTankAvailable", strTankAvailable);
                                query.setWhere(null, 0, "TANK_intTankID", "=", new Integer(intNewTankID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                                query.executeUpdate();
                            }

                            rsResultSet.close();

                            query.reset();
                            query.setDomain("BOX", null, null, null); 
                            query.setSumField("BOX_intBoxCapacity");
                            query.setSumField("BOX_intBoxAvailable");
                            query.setWhere(null, 0, "BOX_intTankID", "=", new Integer(intOldTankID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                            rsResultSet = query.executeSelect();

                            if (rsResultSet.next())
                            {
                                String strTankCapacity = rsResultSet.getString("SUM_BOX_intBoxCapacity");
                                String strTankAvailable = rsResultSet.getString("SUM_BOX_intBoxAvailable");

                                if (strTankCapacity != null && strTankAvailable != null)
                                {
                                    query.reset();
                                    query.setDomain("TANK", null, null, null);
                                    query.setField("TANK_intTankCapacity", strTankCapacity);
                                    query.setField("TANK_intTankAvailable", strTankAvailable);
                                    query.setWhere(null, 0, "TANK_intTankID", "=", new Integer(intOldTankID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.executeUpdate();
                                }
                                else
                                {
                                    query.reset();
                                    query.setDomain("TANK", null, null, null);
                                    query.setField("TANK_intTankCapacity", "0");
                                    query.setField("TANK_intTankAvailable", "0");
                                    query.setWhere(null, 0, "TANK_intTankID", "=", new Integer(intOldTankID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.executeUpdate();
                                }
                            }
                            rsResultSet.close();
                        }


                        // build Tank dropdown list
                        query.reset();
                        query.setDomain("TANK", null, null, null);
                        query.setFields(vtListTankFormFields, null);
                        query.setOrderBy("TANK_strTankName", "ASC");
                        ResultSet rsTankList = query.executeSelect();

                        this.setLocking("TANK", runtimeData.getParameter("BOX_intBoxID"));
                        strXML = QueryChannel.buildSearchXMLFile("search_tank", rsTankList, vtListTankFormFields) +
                                 QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                 QueryChannel.buildFormLabelXMLFile(vtViewBoxFormFields) +
                                 QueryChannel.buildViewFromKeyXMLFile(vtViewBoxFormFields, "BOX_intBoxID", intCurrentBoxID) +
                                 "<blLockError>false</blLockError>";
                        rsTankList.close();
                    }
                    else
                    {
                        DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
                        query.setDomain("TANK", null, null, null);
                        query.setFields(vtListTankFormFields, null);
                        query.setOrderBy("TANK_strTankName", "ASC");
                        ResultSet rsTankList = query.executeSelect();
                        this.setLocking("BOX", runtimeData.getParameter("BOX_intBoxID"));
                        strXML = QueryChannel.buildSearchXMLFile("search_tank", rsTankList, vtListTankFormFields) +
                                 QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                 QueryChannel.buildFormLabelXMLFile(vtViewBoxFormFields) +
                                 QueryChannel.buildViewFromKeyXMLFile(vtViewBoxFormFields, "BOX_intBoxID", intCurrentBoxID) +
                                 "<blLockError>true</blLockError>";
                        runtimeData.setParameter("TANK_intTankID",runtimeData.getParameter("BOX_intTankID"));
                        Site stObj = this.invmgr.createSite(this.runtimeData);
                        if(stObj != null)
                        {
                            strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                        }

                        rsTankList.close();
                    }
                }
                else
                {
                    if(strCheckRequiredFields != null)
                    {
                        strErrorMessage = strCheckRequiredFields;
                    }
                    else if(strCheckDuplicates.length() != 0)
                    {
                        strErrorMessage = strCheckDuplicates;
                    }
                    this.setLocking("BOX", runtimeData.getParameter("BOX_intBoxID"));
                    DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
                    query.setDomain("TANK", null, null, null);
                    query.setFields(vtListTankFormFields, null);
                    query.setOrderBy("TANK_strTankName", "ASC");
                    ResultSet rsTankList = query.executeSelect();

                    strXML = QueryChannel.buildSearchXMLFile("search_tank", rsTankList, vtListTankFormFields) +
                             QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                             QueryChannel.buildFormLabelXMLFile(vtViewBoxFormFields) +
                             QueryChannel.buildViewFromKeyXMLFile(vtViewBoxFormFields, "BOX_intBoxID", intCurrentBoxID) +
                             "<blLockError>false</blLockError>";
                     runtimeData.setParameter("TANK_intTankID",runtimeData.getParameter("BOX_intTankID"));
                    Site stObj = this.invmgr.createSite(this.runtimeData);
                    if(stObj != null)
                    {
                        strXML += "<Selected>" + this.invmgr.getSiteXML(stObj)+"</Selected>";
                    }    
                    rsTankList.close();

                }
                //rennypv---added with the OO concept introduced and when the dropdowns were introduced for the site and tank
                strXML += invmgr.getInventoryDetails(InventoryManager.TYPE_LIST_SITE_N_TANKS_NAMES, INVENTORY_VIEW, authToken);
             }
             else
             {
                strErrorMessage = strObjExistsError;
                this.displayViewBoxForm();
             }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Delete the current box
     */
    private void doDeleteBox()
    {
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_DELETE, authToken);
            query.setDomain("TRAY", null, null, null);
            query.setField("TRAY_intBoxID", null);
            query.setWhere(null, 0, "TRAY_intBoxID", "=", runtimeData.getParameter("BOX_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            // can not delete an unempty box
            if (rsResult.next())
            {
                strErrorMessage = "This box contains trays. You can not delete it.";
                displayViewBoxForm();
            }
            else
            {
                // if we can lock this box for delete. Delete the box
                if (lockRequest.isValid() && lockRequest.lockWrites())
                {
                    query.reset();
                    query.setDomain("BOX", null, null, null);
                    query.setWhere(null, 0, "BOX_intBoxID", "=", runtimeData.getParameter("BOX_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeDelete();
                    lockRequest.unlockWrites();
                    
                    doViewInventory();
                }
                // if not, send lock-error message
                else
                {
                    displayViewBoxForm();
                    strXML += "<blLockError>true</blLockError>";
                }
            }
            
            rsResult.close();
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Display box details
     */
    public void displayViewBoxForm()
    {
        strStylesheet = VIEW_BOX;
        this.clearLockRequest();
        this.lockRequest = new LockRequest(this.authToken);
        Vector vtViewBoxFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_box");
        Vector vtListTankFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_tank");
        int intCurrentBoxID = Integer.parseInt(runtimeData.getParameter("BOX_intBoxID"));
        updateHashExpanded("BOX_" + runtimeData.getParameter("BOX_intBoxID"), true);
        strCurrentNode = "BOX_" + runtimeData.getParameter("BOX_intBoxID");
        String strObjExistsError = "";
        try
        {
             if((strObjExistsError = this.invmgr.objectExists("BOX",runtimeData.getParameter("BOX_intBoxID"))).length() == 0)
            {
                this.lockRequest.addLock("BOX", runtimeData.getParameter("BOX_intBoxID"), LockRecord.READ_WRITE);
                this.lockRequest.lockDelayWrite();
                DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
                query.setDomain("TANK", null, null, null);
                query.setFields(vtListTankFormFields, null);
                query.setOrderBy("TANK_strTankName", "ASC");
                ResultSet rsTankList = query.executeSelect();

                strXML = QueryChannel.buildSearchXMLFile("search_tank", rsTankList, vtListTankFormFields) +
                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                         QueryChannel.buildFormLabelXMLFile(vtViewBoxFormFields) +
                         QueryChannel.buildViewFromKeyXMLFile(vtViewBoxFormFields, "BOX_intBoxID", intCurrentBoxID);

                rsTankList.close();
                //rennypv---added with the OO concept introduced and when the dropdowns were introduced for the site and tank
                strXML += invmgr.getInventoryDetails(InventoryManager.TYPE_LIST_SITE_N_TANKS_NAMES, INVENTORY_VIEW, authToken);
             }
             else
            {
                this.strStylesheet = this.OBJ_DELETED;
                this.strXML = new String();
                this.strXML = QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles"));
                this.strErrorMessage = strObjExistsError;
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Add new tray
     */
    private void doAddTray()
    {

        Vector vtAddTrayFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_tray");
        Vector vtViewTrayFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tray");
        Vector vtListBoxFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_box");
        Vector vtAddCellFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_cell");
        String strObjExistsError = "";
        try
        { 
            this.setLocking("BOX", runtimeData.getParameter("TRAY_intBoxID"));
            if((strObjExistsError = this.invmgr.objectExists("BOX",runtimeData.getParameter("TRAY_intBoxID"))).length() == 0)
            {
                    if(this.lockRequest.isValid() && this.lockRequest.lockWrites())
                    {
                        String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddTrayFormFields, runtimeData);
                        String strCheckDuplicates = "";
                        if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
                        {
                            strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_TRAY_CHECK, runtimeData.getParameter("TRAY_strTrayName"),runtimeData.getParameter("TRAY_intTrayID"),runtimeData.getParameter("TRAY_intBoxID"));
                        }
                        // if all required fields values are supplied. Do insert
                        if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
                        {
                            // checks data validation
                            String strValidation = QueryChannel.validateData(vtAddTrayFormFields, runtimeData);
                            // if all data is OK, insert new tank
                            if (strValidation == null)
                            {
                                strStylesheet = VIEW_TRAY;
                                int intNoOfRow = Integer.parseInt(runtimeData.getParameter("TRAY_intNoOfRow"));
                                int intNoOfCol = Integer.parseInt(runtimeData.getParameter("TRAY_intNoOfCol"));

                                // check data is in valid range
                                if (intNoOfRow > 0 && intNoOfRow < 27 && intNoOfCol >0 && intNoOfCol < 27)
                                {
                                    runtimeData.setParameter("TRAY_intTrayCapacity", new Integer(intNoOfRow * intNoOfCol).toString());
                                    runtimeData.setParameter("TRAY_intTrayAvailable", new Integer(intNoOfRow * intNoOfCol).toString());

                                    DALSecurityQuery query = new DALSecurityQuery(INVENTORY_INSERT, authToken);

                                    // add new tray
                                    query.setDomain("TRAY", null, null, null);    
                                    query.setFields(vtAddTrayFormFields, runtimeData);
                                    query.executeInsert();           
                                    int intCurrentTrayID = QueryChannel.getNewestKeyAsInt(query);
                                    String strCurrentTrayID = QueryChannel.getNewestKeyAsString(query);
                                    strCurrentNode = "TRAY_" + intCurrentTrayID;
                                    updateHashExpanded("BOX_" + runtimeData.getParameter("TRAY_intBoxID"), true);

                                    query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                    query.setDomain("BOX", null, null, null);
                                    query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "LEFT JOIN");
                                    query.setField("TANK_intTankID", null);
                                    query.setField("TANK_intSiteID", null);
                                    query.setWhere(null, 0, "BOX_intBoxID", "=", runtimeData.getParameter("TRAY_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    ResultSet rsThisBox = query.executeSelect();
                                    if (rsThisBox.next())
                                    {
                                        updateHashExpanded("TANK_" + rsThisBox.getString(1), true);
                                        updateHashExpanded("SITE_" + rsThisBox.getString(2), true);
                                    }

                                    rsThisBox.close();

                                    // add cells for this tray
                                    ChannelRuntimeData[] rdMultiData = new ChannelRuntimeData[intNoOfRow*intNoOfCol];
                                    int intCounter = 0;
                                    for (int i=1; i < intNoOfRow + 1; i++)
                                    {
                                        for (int j=1; j < intNoOfCol + 1; j++)
                                        {
                                            ChannelRuntimeData rdData = new ChannelRuntimeData();
                                            rdData.setParameter("CELL_intTrayID", new Integer(intCurrentTrayID).toString());
                                            rdData.setParameter("CELL_intRowNo", new Integer(i).toString());
                                            rdData.setParameter("CELL_intColNo", new Integer(j).toString());
                                            rdMultiData[intCounter] = rdData;
                                            intCounter++;
                                        }
                                    }
                                    query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                    query.setDomain("CELL", null, null, null);
                                    query.setMultiInsert(vtAddCellFormFields, rdMultiData);
                                    query.executeMultiInsert();


                                    // update box capacity
                                    query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                    query.setDomain("TRAY", null, null, null);
                                    query.setSumField("TRAY_intTrayCapacity");
                                    query.setSumField("TRAY_intTrayAvailable");
                                    query.setWhere(null, 0, "TRAY_intBoxID", "=", runtimeData.getParameter("TRAY_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    ResultSet rsResultSet = query.executeSelect();

                                    if (rsResultSet.next())
                                    {
                                        String strBoxCapacity = rsResultSet.getString("SUM_TRAY_intTrayCapacity");
                                        String strBoxAvailable = rsResultSet.getString("SUM_TRAY_intTrayAvail");

                                        query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                        query.setDomain("BOX", null, null, null);
                                        query.setField("BOX_intBoxCapacity", strBoxCapacity);
                                        query.setField("BOX_intBoxAvailable", strBoxAvailable);
                                        query.setWhere(null, 0, "BOX_intBoxID", "=", runtimeData.getParameter("TRAY_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
                                        query.executeUpdate();
                                    }
                                    rsResultSet.close();

                                    // update tank capacity
                                    query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                    query.setDomain("BOX", null, null, null);
                                    query.setField("BOX_intTankID", null);
                                    query.setWhere(null, 0, "BOX_intBoxID", "=", runtimeData.getParameter("TRAY_intBoxID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    rsResultSet = query.executeSelect();

                                    if (rsResultSet.next())
                                    {
                                        String strTankID = rsResultSet.getString("BOX_intTankID");

                                        query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                        query.setDomain("BOX", null, null, null);
                                        query.setSumField("BOX_intBoxCapacity");
                                        query.setSumField("BOX_intBoxAvailable");
                                        query.setWhere(null, 0, "BOX_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                                        ResultSet rsResultSet1 = query.executeSelect();

                                        if (rsResultSet1.next())
                                        {
                                            String strTankCapacity = rsResultSet1.getString("SUM_BOX_intBoxCapacity");
                                            String strTankAvailable = rsResultSet1.getString("SUM_BOX_intBoxAvailable");

                                            query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                            query.setDomain("TANK", null, null, null);
                                            query.setField("TANK_intTankCapacity", strTankCapacity);
                                            query.setField("TANK_intTankAvailable", strTankAvailable);
                                            query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                                            query.executeUpdate();
                                        }
                                        rsResultSet1.close();
                                    }
                                    rsResultSet.close();

                                    query = new DALSecurityQuery(INVENTORY_INSERT, authToken);
                                    query.setDomain("BOX", null, null, null);
                                    query.setFields(vtListBoxFormFields, null);
                                    query.setOrderBy("BOX_strBoxName", "ASC");
                                    ResultSet rsBoxList = query.executeSelect();
                                    this.setLocking("TRAY",strCurrentTrayID);
                                    strXML = QueryChannel.buildSearchXMLFile("search_box", rsBoxList, vtListBoxFormFields) +
                                             QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                                             QueryChannel.buildFormLabelXMLFile(vtViewTrayFormFields) +
                                             QueryChannel.buildViewFromKeyXMLFile(vtViewTrayFormFields, "TRAY_intTrayID", intCurrentTrayID) +
                                             buildCellXMLFile(intCurrentTrayID);

                                    rsBoxList.close();
                                    strXML += invmgr.getInventoryDetails(InventoryManager.TYPE_LIST_SITE_TANK_N_BOX_NAMES, INVENTORY_VIEW, authToken);
                                }
                                else
                                {
                                    strErrorMessage = "Number of rows and columns must be between 1 and 26.";
                                    displayAddTrayForm();
                                    runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                                    Site stObj = invmgr.createSite(this.runtimeData);
                                    if(stObj != null)
                                    {
                                        strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                                    }
                                }
                        }
                        else
                        {
                            strErrorMessage = strValidation;
                            displayAddTrayForm();
                            runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                            Site stObj = invmgr.createSite(this.runtimeData);
                            if(stObj != null)
                            {
                                strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                            }
                        }
                    }
                    else
                    {
                        if(strCheckRequiredFields != null)
                        {
                            strErrorMessage = strCheckRequiredFields;
                        }
                        else if(strCheckDuplicates.length() != 0)
                        {
                            strErrorMessage = strCheckDuplicates;
                        }
                        displayAddTrayForm();
                        runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                        Site stObj = invmgr.createSite(this.runtimeData);
                        if(stObj != null)
                        {
                            strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                        }
                    }
                    this.lockRequest.unlockWrites();                    
                }
                else
                {
                    strErrorMessage = "There is a write lock on the object to which this is being added. Please try again later";
                    displayAddTrayForm();
                    runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                    Site stObj = invmgr.createSite(this.runtimeData);
                    if(stObj != null)
                    {
                        strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                    }
                }
            }
            else
            {
                strErrorMessage = strObjExistsError;
                displayAddTrayForm();
                runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                Site stObj = invmgr.createSite(this.runtimeData);
                if(stObj != null)
                {
                    strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Display add tray form
     */
    private void displayAddTrayForm()
    {
        strStylesheet = ADD_TRAY;
        Vector vtAddTrayFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_tray");
        Vector vtListBoxFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_box");
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
            query.setDomain("BOX", null, null, null);
            query.setFields(vtListBoxFormFields, null);
            query.setOrderBy("BOX_strBoxName", "ASC");
            ResultSet rsBoxList = query.executeSelect();
            
            strXML = QueryChannel.buildSearchXMLFile("search_box", rsBoxList, vtListBoxFormFields) +
                     QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                     QueryChannel.buildFormLabelXMLFile(vtAddTrayFormFields) +
                     QueryChannel.buildAddFormXMLFile(vtAddTrayFormFields);
            
            rsBoxList.close();
            strXML += invmgr.getInventoryDetails(InventoryManager.TYPE_LIST_SITE_TANK_N_BOX_NAMES, INVENTORY_VIEW, authToken);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Update tray details
     */
    private void doUpdateTray()
    {
        Vector vtAddTrayFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_tray");
        Vector vtUpdateTrayFormFields = DatabaseSchema.getFormFields("cinventory_update_inventory_tray");
        Vector vtViewTrayFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tray");
        Vector vtListBoxFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_box");
        Vector vtAddCellFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_cell");
        int intCurrentTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayID"));
        String strObjExistsError = "";
        try
        {
            if((strObjExistsError = this.invmgr.objectExists("BOX",runtimeData.getParameter("TRAY_intBoxID"))).length() == 0)
            {
                this.lockRequest.addLock("BOX", runtimeData.getParameter("TRAY_intBoxID"),LockRecord.READ_WRITE);
                this.lockRequest.lockDelayWrite();
                String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddTrayFormFields, runtimeData);
                String strCheckDuplicates = "";
                if(ALLOW_INVENTORY_DUPLICATES.equalsIgnoreCase("false"))
                {
                    strCheckDuplicates += this.invmgr.checkIfDuplicateName(InventoryManager.DUPLICATE_TRAY_CHECK, runtimeData.getParameter("TRAY_strTrayName"),runtimeData.getParameter("TRAY_intTrayID"),runtimeData.getParameter("TRAY_intBoxID"));
                }
                // if all required fields values are supplied. Do insert
                if (strCheckRequiredFields == null && strCheckDuplicates.length() == 0)
                {
                    // checks data validation
                    String strValidation = QueryChannel.validateData(vtAddTrayFormFields, runtimeData);
                    // if all data is OK, insert new tank
                    if (strValidation == null)
                    {

                        int intNewBoxID = Integer.parseInt(runtimeData.getParameter("TRAY_intBoxID"));
                        int intOldBoxID = intNewBoxID;
                        int intOldCapacity = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayCapacity"));
                        int intOldAvailable = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayAvailable"));
                        int intNewNoOfRow = Integer.parseInt(runtimeData.getParameter("TRAY_intNoOfRow"));
                        int intNewNoOfCol = Integer.parseInt(runtimeData.getParameter("TRAY_intNoOfCol"));
                        int intOldNoOfRow = intNewNoOfRow;
                        int intOldNoOfCol = intNewNoOfCol;
                        strCurrentNode = "TRAY_" + runtimeData.getParameter("TRAY_intTrayID");

                        // check data is in valid range
                        if (intNewNoOfRow > 0 && intNewNoOfRow < 27 && intNewNoOfCol >0 && intNewNoOfCol < 27)
                        {
                            if (lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                // get old tray data
                                DALSecurityQuery query = new DALSecurityQuery(INVENTORY_UPDATE, authToken);
                                query.setDomain("TRAY", null, null, null);
                                query.setFields(vtViewTrayFormFields, null);
                                query.setWhere(null, 0, "TRAY_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
                                ResultSet rsThisTray = query.executeSelect();

                                if (rsThisTray.next())
                                {
                                    intOldBoxID = rsThisTray.getInt("TRAY_intBoxID");
                                    intOldNoOfRow = rsThisTray.getInt("TRAY_intNoOfRow");
                                    intOldNoOfCol = rsThisTray.getInt("TRAY_intNoOfCol");
                                }

                                rsThisTray.close();

                                // Can reduce tray's size only if the tray is empty
                                if ((intNewNoOfRow < intOldNoOfRow || intNewNoOfCol < intOldNoOfCol) &&
                                     intOldAvailable < intOldCapacity)
                                {
                                    // set the lock to READ_LOCK
                                    lockRequest.unlockWrites();

                                    strErrorMessage = "This tray contains biospecimen. You can not reduce its size.";
                                    displayViewTrayForm();
                                    return;
                                }
                                else if (intNewNoOfRow != intOldNoOfRow || intNewNoOfCol != intOldNoOfCol)
                                {
                                    int intNewCapacity = intNewNoOfRow * intNewNoOfCol;
                                    int intNewAvailable = intNewCapacity - intOldCapacity + intOldAvailable;
                                    runtimeData.setParameter("TRAY_intTrayCapacity", new Integer(intNewCapacity).toString());
                                    runtimeData.setParameter("TRAY_intTrayAvailable", new Integer(intNewAvailable).toString());

                                    // update tray
                                    query.reset();
                                    query.setDomain("TRAY", null, null, null);
                                    query.setFields(vtAddTrayFormFields, runtimeData);
                                    query.setWhere(null, 0, "TRAY_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.executeUpdate();
                                }
                                else
                                {
                                    // update tray
                                    query.reset();
                                    query.setDomain("TRAY", null, null, null);
                                    query.setFields(vtUpdateTrayFormFields, runtimeData);
                                    query.setWhere(null, 0, "TRAY_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.executeUpdate();
                                }

                                // set the lock to READ_LOCK
                                lockRequest.unlockWrites();

                                // create new cells & delete cells
                                if (intNewNoOfRow <= intOldNoOfRow && intNewNoOfCol <= intOldNoOfCol)
                                {
                                    query.reset();
                                    query.setDomain("CELL", null, null, null);
                                    query.setWhere(null,1,  "CELL_intRowNo", ">", new Integer(intNewNoOfRow).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.setWhere("OR", 0, "CELL_intColNo", ">", new Integer(intNewNoOfCol).toString(), 1, DALQuery.WHERE_HAS_VALUE);
                                    query.setWhere("AND", 0, "CELL_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.executeDelete();
                                }
                                else if (intNewNoOfRow <= intOldNoOfRow)
                                {
                                    query.reset();
                                    query.setDomain("CELL", null, null, null);
                                    query.setWhere(null,0,  "CELL_intRowNo", ">", new Integer(intNewNoOfRow).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.setWhere("AND", 0, "CELL_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.executeDelete();

                                    // add cells for this tray
                                    ChannelRuntimeData[] rdMultiData = new ChannelRuntimeData[intNewNoOfRow * (intNewNoOfCol - intOldNoOfCol)];
                                    int intCounter = 0;
                                    for (int i=1; i < intNewNoOfRow + 1; i++)
                                        for (int j=intOldNoOfCol + 1; j < intNewNoOfCol + 1; j++)
                                        {
                                            ChannelRuntimeData rdData = new ChannelRuntimeData();
                                            rdData.setParameter("CELL_intTrayID", new Integer(intCurrentTrayID).toString());
                                            rdData.setParameter("CELL_intRowNo", new Integer(i).toString());
                                            rdData.setParameter("CELL_intColNo", new Integer(j).toString());
                                            rdMultiData[intCounter] = rdData;
                                            intCounter++;
                                        }
                                    query.reset();
                                    query.setDomain("CELL", null, null, null);
                                    query.setMultiInsert(vtAddCellFormFields, rdMultiData);
                                    query.executeMultiInsert();
                                }
                                else if (intNewNoOfCol <= intOldNoOfCol)
                                {
                                    query.reset();
                                    query.setDomain("CELL", null, null, null);
                                    query.setWhere(null,0,  "CELL_intColNo", ">", new Integer(intNewNoOfCol).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.setWhere("AND", 0, "CELL_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
                                    query.executeDelete();

                                    // add cells for this tray
                                    ChannelRuntimeData[] rdMultiData = new ChannelRuntimeData[intNewNoOfCol * (intNewNoOfRow - intOldNoOfRow)];
                                    int intCounter = 0;
                                    for (int i=intOldNoOfRow + 1; i < intNewNoOfRow + 1; i++)
                                        for (int j=1; j < intNewNoOfCol + 1; j++)
                                        {
                                            ChannelRuntimeData rdData = new ChannelRuntimeData();
                                            rdData.setParameter("CELL_intTrayID", new Integer(intCurrentTrayID).toString());
                                            rdData.setParameter("CELL_intRowNo", new Integer(i).toString());
                                            rdData.setParameter("CELL_intColNo", new Integer(j).toString());
                                            rdMultiData[intCounter] = rdData;
                                            intCounter++;
                                        }
                                    query.reset();
                                    query.setDomain("CELL", null, null, null);
                                    query.setMultiInsert(vtAddCellFormFields, rdMultiData);
                                    query.executeMultiInsert();
                                }
                                else
                                {
                                    // add cells for this tray
                                    ChannelRuntimeData[] rdMultiData = new ChannelRuntimeData[intNewNoOfCol * (intNewNoOfRow - intOldNoOfRow)];
                                    int intCounter = 0;
                                    for (int i=intOldNoOfRow + 1; i < intNewNoOfRow + 1; i++)
                                        for (int j=1; j < intNewNoOfCol + 1; j++)
                                        {
                                            ChannelRuntimeData rdData = new ChannelRuntimeData();
                                            rdData.setParameter("CELL_intTrayID", new Integer(intCurrentTrayID).toString());
                                            rdData.setParameter("CELL_intRowNo", new Integer(i).toString());
                                            rdData.setParameter("CELL_intColNo", new Integer(j).toString());
                                            rdMultiData[intCounter] = rdData;
                                            intCounter++;
                                        }
                                    query.reset();
                                    query.setDomain("CELL", null, null, null);
                                    query.setMultiInsert(vtAddCellFormFields, rdMultiData);
                                    query.executeMultiInsert();

                                    // add cells for this tray
                                    rdMultiData = new ChannelRuntimeData[intOldNoOfRow * (intNewNoOfCol - intOldNoOfCol)];
                                    intCounter = 0;
                                    for (int i=1; i < intOldNoOfRow + 1; i++)
                                        for (int j=intOldNoOfCol + 1; j < intNewNoOfCol + 1; j++)
                                        {
                                            ChannelRuntimeData rdData = new ChannelRuntimeData();
                                            rdData.setParameter("CELL_intTrayID", new Integer(intCurrentTrayID).toString());
                                            rdData.setParameter("CELL_intRowNo", new Integer(i).toString());
                                            rdData.setParameter("CELL_intColNo", new Integer(j).toString());
                                            rdMultiData[intCounter] = rdData;
                                            intCounter++;
                                        }
                                    query.reset();
                                    query.setDomain("CELL", null, null, null);
                                    query.setMultiInsert(vtAddCellFormFields, rdMultiData);
                                    query.executeMultiInsert();
                                }

                                // update BOXES capacity & TANKS capacity
                                if (intNewBoxID != intOldBoxID || intNewNoOfRow != intOldNoOfRow || intNewNoOfCol != intOldNoOfCol)
                                    doUpdateCapacityForTrayUpdate(intNewBoxID, intOldBoxID);

                                displayViewTrayForm();
                                strXML += "<blLockError>false</blLockError>";
                            }
                            else
                            {
                                displayViewTrayForm();
                                runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                                Site stObj = invmgr.createSite(this.runtimeData);
                                if(stObj != null)
                                {
                                    strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                                }
                                strXML += "<blLockError>true</blLockError>";
                            }
                        }
                        else
                        {
                            strErrorMessage = "Number of rows and columns must be between 1 and 26.";
                            displayViewTrayForm();
                            runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                            Site stObj = invmgr.createSite(this.runtimeData);
                            if(stObj != null)
                            {
                                strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                            }
                        }
                    }
                    else
                    {
                        strErrorMessage = strValidation;
                        displayViewTrayForm();
                        runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                        Site stObj = invmgr.createSite(this.runtimeData);
                        if(stObj != null)
                        {
                            strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                        }
                    }
                }
                else
                {
                    if(strCheckRequiredFields != null)
                    {
                        strErrorMessage = strCheckRequiredFields;
                    }
                    else if(strCheckDuplicates.length() != 0)
                    {
                        strErrorMessage = strCheckDuplicates;
                    }
                    displayViewTrayForm();
                    runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                    Site stObj = invmgr.createSite(this.runtimeData);
                    if(stObj != null)
                    {
                        strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                    }
                }
            }
            else
            {
                strErrorMessage = strObjExistsError;
                displayViewTrayForm();
                runtimeData.setParameter("BOX_intBoxID",runtimeData.getParameter("TRAY_intBoxID"));
                Site stObj = invmgr.createSite(this.runtimeData);
                if(stObj != null)
                {
                    strXML += "<Selected>"+invmgr.getSiteXML(stObj)+"</Selected>";
                }
            }
       }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    /** Delete current tray
     */
    private void doDeleteTray()
    {
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_DELETE, authToken);
            query.setDomain("TRAY", null, null, null);
            query.setField("TRAY_intTrayCapacity", null);
            query.setField("TRAY_intTrayAvailable", null);
            query.setField("TRAY_intBoxID", null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();

            if (rsResult.next())
            {
                int intBoxID = rsResult.getInt("TRAY_intBoxID");
                
                // can delete the tray only if it is empty
                if (rsResult.getInt("TRAY_intTrayCapacity") == rsResult.getInt("TRAY_intTrayAvailable"))
                {
                    // if we can lock this tray for delete. Delete the tray
                    if (lockRequest.isValid() && lockRequest.lockWrites())
                    {
                        query.executeDelete();
                        lockRequest.unlockWrites();
                        doUpdateCapacityForTrayUpdate(intBoxID, intBoxID);
                        doViewInventory();
                    }
                    // if not, send lock-error message
                    else
                    {
                        displayViewTrayForm();
                        strXML += "<blLockError>true</blLockError>";
                    }   
                }
                // can not delete the tray because it's not empty
                else
                {
                    strErrorMessage = "This tray contains biospecimen. You can not delete it.";
                    displayViewTrayForm();
                }
            }
            
            rsResult.close();
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    private void doUpdateCapacityForTrayUpdate(int intNewBoxID, int intOldBoxID)
    {
        try
        {
            // update BOXES capacity
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_UPDATE, authToken);
            query.setDomain("TRAY", null, null, null);
            query.setSumField("TRAY_intTrayCapacity");
            query.setSumField("TRAY_intTrayAvailable");
            query.setWhere(null, 0, "TRAY_intBoxID", "=", new Integer(intNewBoxID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();
            
            if (rsResultSet.next())
            {
                String strBoxCapacity = rsResultSet.getString("SUM_TRAY_intTrayCapacity");
                String strBoxAvailable = rsResultSet.getString("SUM_TRAY_intTrayAvail");
                
                query.reset();
                query.setDomain("BOX", null, null, null);
                query.setField("BOX_intBoxCapacity", strBoxCapacity);
                query.setField("BOX_intBoxAvailable", strBoxAvailable);
                query.setWhere(null, 0, "BOX_intBoxID", "=", new Integer(intNewBoxID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.executeUpdate();
            }
            
            rsResultSet.close();
            
            query.reset();
            query.setDomain("TRAY", null, null, null);
            query.setSumField("TRAY_intTrayCapacity");
            query.setSumField("TRAY_intTrayAvailable");
            query.setWhere(null, 0, "TRAY_intBoxID", "=", new Integer(intOldBoxID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            rsResultSet = query.executeSelect();
            
            if (rsResultSet.next())
            {
                String strBoxCapacity = rsResultSet.getString("SUM_TRAY_intTrayCapacity");
                String strBoxAvailable = rsResultSet.getString("SUM_TRAY_intTrayAvail");
                
                if (strBoxCapacity != null && strBoxAvailable != null)
                {
                    query.reset();
                    query.setDomain("BOX", null, null, null);
                    query.setField("BOX_intBoxCapacity", strBoxCapacity);
                    query.setField("BOX_intBoxAvailable", strBoxAvailable);
                    query.setWhere(null, 0, "BOX_intBoxID", "=", new Integer(intOldBoxID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                }
                else
                {
                    query.reset();
                    query.setDomain("BOX", null, null, null);
                    query.setField("BOX_intBoxCapacity", "0");
                    query.setField("BOX_intBoxAvailable", "0");
                    query.setWhere(null, 0, "BOX_intBoxID", "=", new Integer(intOldBoxID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                }
            }
            
            rsResultSet.close();
            
            // update TANKS capacity
            query.reset();
            query.setDomain("BOX", null, null, null);
            query.setField("BOX_intTankID", null);
            query.setWhere(null, 0, "BOX_intBoxID", "=", new Integer(intNewBoxID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            rsResultSet = query.executeSelect();
            
            if (rsResultSet.next())
            {
                String strTankID = rsResultSet.getString("BOX_intTankID");
                
                query.reset();
                query.setDomain("BOX", null, null, null);
                query.setSumField("BOX_intBoxCapacity");
                query.setSumField("BOX_intBoxAvailable");
                query.setWhere(null, 0, "BOX_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResultSet1 = query.executeSelect();
                
                if (rsResultSet1.next())
                {
                    String strTankCapacity = rsResultSet1.getString("SUM_BOX_intBoxCapacity");
                    String strTankAvailable = rsResultSet1.getString("SUM_BOX_intBoxAvailable");

                    query.reset();
                    query.setDomain("TANK", null, null, null);
                    query.setField("TANK_intTankCapacity", strTankCapacity);
                    query.setField("TANK_intTankAvailable", strTankAvailable);
                    query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                }
            }
            
            rsResultSet.close();
            
            query.reset();
            query.setDomain("BOX", null, null, null);
            query.setField("BOX_intTankID", null);
            query.setWhere(null, 0, "BOX_intBoxID", "=", new Integer(intOldBoxID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
            rsResultSet = query.executeSelect();
            
            if (rsResultSet.next())
            {
                String strTankID = rsResultSet.getString("BOX_intTankID");
                
                query.reset();
                query.setDomain("BOX", null, null, null);
                query.setSumField("BOX_intBoxCapacity");
                query.setSumField("BOX_intBoxAvailable");
                query.setWhere(null, 0, "BOX_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResultSet1 = query.executeSelect();
                
                if (rsResultSet1.next())
                {
                    String strTankCapacity = rsResultSet1.getString("SUM_BOX_intBoxCapacity");
                    String strTankAvailable = rsResultSet1.getString("SUM_BOX_intBoxAvailable");

                    query.reset();
                    query.setDomain("TANK", null, null, null);
                    query.setField("TANK_intTankCapacity", strTankCapacity);
                    query.setField("TANK_intTankAvailable", strTankAvailable);
                    query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                }
                
                rsResultSet1.close();
            }
            
            rsResultSet.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    private void displayViewTrayForm()
    {
        strStylesheet = VIEW_TRAY;
        clearLockRequest();
        lockRequest = new LockRequest(this.authToken);
        Vector vtViewTrayFormFields = DatabaseSchema.getFormFields("cinventory_view_inventory_tray");
        Vector vtListBoxFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_box");
        int intCurrentTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayID"));
        strCurrentNode = "TRAY_" + runtimeData.getParameter("TRAY_intTrayID");
        String strTrayID = runtimeData.getParameter("TRAY_intTrayID");
        String strObjExistsError = "";
        try
        {
            if((strObjExistsError = this.invmgr.objectExists("TRAY",strTrayID)).length() == 0)
            {
                this.lockRequest.addLock("TRAY", strTrayID, LockRecord.READ_WRITE);
                this.lockRequest.lockDelayWrite();
                DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);

                query.reset();
                query.setDomain("TRAY", null, null, null);
                query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "LEFT JOIN");
                query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "LEFT JOIN");
                query.setField("TRAY_intBoxID", null);
                query.setField("TANK_intTankID", null);
                query.setField("TANK_intSiteID", null);
                query.setWhere(null, 0, "TRAY_intTrayID", "=", runtimeData.getParameter("TRAY_intTrayID"), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsThisBox = query.executeSelect();
                if (rsThisBox.next())
                {
                    updateHashExpanded("BOX_" + rsThisBox.getString(1), true);
                    updateHashExpanded("TANK_" + rsThisBox.getString(2), true);
                    updateHashExpanded("SITE_" + rsThisBox.getString(3), true);
                }

                rsThisBox.close();

                query.reset();
                query.setDomain("BOX", null, null, null);
                query.setFields(vtListBoxFormFields, null);
                query.setOrderBy("BOX_strBoxName", "ASC");
                ResultSet rsBoxList = query.executeSelect();

                strXML = QueryChannel.buildSearchXMLFile("search_box", rsBoxList, vtListBoxFormFields) +
                         QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles")) +
                         QueryChannel.buildFormLabelXMLFile(vtViewTrayFormFields) +
                         QueryChannel.buildViewFromKeyXMLFile(vtViewTrayFormFields, "TRAY_intTrayID", intCurrentTrayID) +
                         buildCellXMLFile(intCurrentTrayID);
		strXML = strXML + "<currentTray>" + runtimeData.getParameter("TRAY_intTrayID") + "</currentTray>";
                rsBoxList.close();

                if(runtimeData.getParameter("strOrigin") != null)
                {   //guesing where we were, very shitty
                    // -- To the person who wrote the above comment, swearing is not nice.
                    String strPrev = runtimeData.getParameter("strOrigin") +"";

                    if (strPrev.equalsIgnoreCase("view_biospecimen")){
                        strBackButton = "module=core&action="+runtimeData.getParameter("strOrigin")+"&strOrigin="+runtimeData.getParameter("strOrigin")
                                +"&BIOSPECIMEN_intBiospecimenID="+runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
                        strBackButton = Utilities.cleanForXSL(strBackButton);
                        strXML += "<strBackButton>"+strBackButton+ "</strBackButton>";
                     }
                    else if (strPrev.equalsIgnoreCase("biospecimen_search"))
                    {
                        strBackButton = "module=biospecimen_search&action=redo_last_search";
                        strBackButton = Utilities.cleanForXSL(strBackButton);
                        strXML += "<strBackButton>"+strBackButton+ "</strBackButton>";
                    }

                }
                else if(runtimeData.getParameter("strCurrent") != null)
                {   
                        strBackButton = "current="+runtimeData.getParameter("strCurrent")+"&back=back";
                        strBackButton = Utilities.cleanForXSL(strBackButton);
                        strXML += "<strBackButton>"+strBackButton+ "</strBackButton>";
                }
                buildTrayReportFile(runtimeData.getParameter("TRAY_intTrayID"));
                strXML += invmgr.getInventoryDetails(InventoryManager.TYPE_LIST_SITE_TANK_N_BOX_NAMES, INVENTORY_VIEW, authToken);
            }
            else
            {
                this.strStylesheet = this.OBJ_DELETED;
                this.strXML = new String();
                this.strXML = QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cinventory_view_titles"));
                this.strErrorMessage = strObjExistsError;
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }
    
    private void buildTrayReportFile(String strTrayID)
    {
        try
        {
            String strFileName = authToken.getSessionUniqueID() + "trayreport.csv";
            BufferedWriter reportFile = new BufferedWriter(new FileWriter(new File(EXPORT_DIRECTORY + "/" + strFileName)));
            
            DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
            query.setDomain("TRAY", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("cinventory_view_inventory_tray"), null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            rsResult.next();
            String strRowType = rsResult.getString("TRAY_strRowNoType");
            String strColType = rsResult.getString("TRAY_strColNoType");
            
            rsResult.close();
            query.reset();
            query.setDomain("CELL", null, null, null);
            query.setDomain("TRAY", "CELL_intTrayID", "TRAY_intTrayID", "INNER JOIN");
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
            query.setDomain("BIOSPECIMEN", "CELL_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "INNER JOIN");
            
            query.setFields(DatabaseSchema.getFormFields("cinventory_build_inventory_tray_report"), null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", strTrayID, 0, DALQuery.WHERE_HAS_VALUE);
            rsResult = query.executeSelect();
            
            if (rsResult.next())
            {
                String strRowNo = "";
                String strColNo = "";
                
                String line = "ROW,COL,TRAY,BOX,TANK,SITE,BIOSPECIMEN ID,TYPE,SUB TYPE,COLLECTED AMOUNT,REMOVE AMOUNT \n";
                reportFile.write(line);
                int intRowNo = rsResult.getInt("CELL_intRowNo");
                int intColNo = rsResult.getInt("CELL_intColNo");
                
                if (strRowType.equals("Alphabet"))
                    strRowNo = ALPHABET.substring(intRowNo-1, intRowNo);
                else
                    strRowNo = intRowNo + "";
                
                if (strColType.equals("Alphabet"))
                    strColNo = ALPHABET.substring(intColNo-1, intColNo);
                else
                    strColNo = intColNo + "";
                    
                line = strRowNo + "," +
                      strColNo + "," +
                      rsResult.getString("TRAY_strTrayName") + "," +
                      rsResult.getString("BOX_strBoxName") + "," +
                      rsResult.getString("TANK_strTankName") + "," +
                      rsResult.getString("SITE_strSiteName") + "," +
                      rsResult.getString("BIOSPECIMEN_strBiospecimenID") + "," +
                      rsResult.getString("BIOSPECIMEN_strSampleType") + "," +
                      rsResult.getString("BIOSPECIMEN_strSampleSubType") + "," +
                      rsResult.getString("BIOSPECIMEN_flNumberCollected") + "," +
                      rsResult.getString("BIOSPECIMEN_flNumberRemoved") + "\n";
                
                reportFile.write(line);
                
                while (rsResult.next())
                {
                    intRowNo = rsResult.getInt("CELL_intRowNo");
                    intColNo = rsResult.getInt("CELL_intColNo");

                    if (strRowType.equals("Alphabet"))
                        strRowNo = ALPHABET.substring(intRowNo-1, intRowNo);
                    else
                        strRowNo = intRowNo + "";

                    if (strColType.equals("Alphabet"))
                        strColNo = ALPHABET.substring(intColNo-1, intColNo);
                    else
                        strColNo = intColNo + "";
                    
                    line = strRowNo + "," +
                          strColNo + "," +
                          rsResult.getString("TRAY_strTrayName") + "," +
                          rsResult.getString("BOX_strBoxName") + "," +
                          rsResult.getString("TANK_strTankName") + "," +
                          rsResult.getString("SITE_strSiteName") + "," +
                          rsResult.getString("BIOSPECIMEN_strBiospecimenID") + "," +
                          rsResult.getString("BIOSPECIMEN_strSampleType") + "," +
                          rsResult.getString("BIOSPECIMEN_strSampleSubType") + "," +
                          rsResult.getString("BIOSPECIMEN_flNumberCollected") + "," +
                          rsResult.getString("BIOSPECIMEN_flNumberRemoved") + "\n";
                    
                    reportFile.write(line);
                }
            }
            rsResult.close();
            strXML += "<strReportName>" + strFileName + "</strReportName>";
            // close the output stream
            reportFile.close();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
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
                            
                            strXML += "<label>-1</label>";
                            strXML += "<CELL_intCellID>" + cellID + "</CELL_intCellID>";
                            strXML += "<CELL_intBiospecimenID>" + biospecimenID + "</CELL_intBiospecimenID>";
                            strXML += "<CELL_intPatientID>" + patientID + "</CELL_intPatientID>";
                            
                            if (biospecimenID == -1)
                            {
                                strXML += "<CELL_info>Cell ID: " + cellID + "\\n";
                                
                                if (strRowType.equals("Alphabet"))
                                    strXML += "Row: " + ALPHABET.substring(i-1, i) + "\\n";
                                else
                                    strXML += "Row: " + i + "\\n";
                                
                                if (strColType.equals("Alphabet"))
                                    strXML += "Col: " + ALPHABET.substring(j-1, j) + "\\n";
                                else
                                    strXML += "Col: " + j + "\\n";
                                
                                strXML += "Status: Empty</CELL_info>";
                            }
                            else
                            {
                                String strBiospecimenID = rsResultSet.getString("BIOSPECIMEN_strBiospecimenID");
                                String strSampleType = rsResultSet.getString("BIOSPECIMEN_strSampleType");
                                String strSampleSubType = rsResultSet.getString("BIOSPECIMEN_strSampleSubType");
                                
                                strXML += "<CELL_info>Cell ID: " + cellID + "\\n";
                                
                                if (strRowType.equals("Alphabet"))
                                    strXML += "Row: " + ALPHABET.substring(i-1, i) + "\\n";
                                else
                                    strXML += "Row: " + i + "\\n";
                                
                                if (strColType.equals("Alphabet"))
                                    strXML += "Col: " + ALPHABET.substring(j-1, j) + "\\n";
                                else
                                    strXML += "Col: " + j + "\\n";
                                
                                strXML += "Status: Full\\n";
                                strXML += "Biospecimen ID: " + strBiospecimenID + "\\n";
                                strXML += "Type: " + (strSampleType == null ? "N/A" : strSampleType) + "\\n";
                                strXML += "Subtype: " + (strSampleSubType == null ? "N/A" : strSampleSubType) + "\\n</CELL_info>";
                              
                            }
                        }
                            
                        strXML += "</Col>";
                    }
                    strXML += "</Row>";
                }
            }
            
            rsResultSet.next();
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
        
        return strXML;
    }
    
    /** Calculate the inventory usage
     */
    private int calInventoryUsage(int capacity, int available)
    {
        if (capacity == available)
            return 0;
        if (available == 0)
            return 3;
        
        double usage = (capacity - available)/(1.0 * capacity);
        if (usage > 0.66)
            return 2;
        
        return 1;
    }
    
 private void updateHashExpanded(String key, boolean fromLink)
    {   
        if (!hashExpanded.containsKey(key))
            hashExpanded.put(key, "expanded");
        else if (!fromLink)
            hashExpanded.remove(key);
    }
    
    /**
     * Clear the lock request
     */
    private void clearLockRequest()
    {
        try
        {
            if (lockRequest != null && lockRequest.isValid())
                lockRequest.unlock();
        }
        catch (Exception e)
        {
            LogService.instance().log(
                LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
        finally
        {
            lockRequest = null;
        }
    }
    
    /** Output channel content to the portal
     *  @param out a sax document handler
     */
    public void renderXML(ContentHandler out) throws PortalException
    {
    	
        // Create a new XSLT styling engine
        XSLT xslt = new XSLT(this);

        //System.err.println("strXML: " + strXML);
        // pass the result xml to the styling engine.
        xslt.setXML(strXML);
        




        // specify the stylesheet selector
        xslt.setXSL("CInventory.ssl", strStylesheet, runtimeData.getBrowserInfo());

        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());

        org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL());

        upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CBiospecimen"));
        xslt.setStylesheetParameter("biospecimenChannelURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter( "biospecimenTabOrder", SessionManager.getTabOrder( authToken, "CBiospecimen") );
        
        upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( true ));        
        
        upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CDownload"));
        xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter("nodeId", SessionManager.getChannelID(strSessionUniqueID, "CDownload")); 
        // set the output Handler for the output.
        xslt.setTarget(out);

        // do the deed
        xslt.transform();
    }
    
    /**
     *  Print out message if user doesn't have the autority to do a specific activity
     */
    private String buildSecurityErrorXMLFile(String aFunctionalArea)
    {
        String strTempErrorXML  = "<?xml version=\"1.0\" encoding=\"utf-8\"?><securityerror>"
                + "<errorstring>No Permission to " + aFunctionalArea + "</errorstring>"
                + "<errortext>The " + staticData.getPerson().getFullName() 
                + " is not authorised to " + aFunctionalArea + "</errortext>"
                + "<errordata></errordata>"
                + "</securityerror>";
        return strTempErrorXML;      
    }
    private void setLocking(String domain,String strKey) throws UnsupportedLockException
    {
        this.clearLockRequest();
        this.lockRequest = new LockRequest(this.authToken);
        this.lockRequest.addLock(domain, strKey, LockRecord.READ_WRITE);
        this.lockRequest.lockDelayWrite();
    }

    private void displayBatchAllocateForm(AuthToken authToken,int intCellID, int intTrayID) {
	try
        {
		strStylesheet = BATCH_ALLOCATE;
	int intCurrentTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayID")); 
	  DALSecurityQuery query = new DALSecurityQuery(INVENTORY_VIEW, authToken);
            query.setDomain("CELL", null, null, null);
	    query.setDomain("TRAY", "CELL_intTrayID", "TRAY_intTrayID", "INNER JOIN");
            query.setField("CELL_intRowNo", null);
            query.setField("CELL_intColNo", null);
            query.setField("TRAY_strRowNoType", null);
	    query.setField("TRAY_strColNoType", null);
            query.setWhere(null, 0, "CELL_intCellID", "=", ""+intCellID, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
		String strRowType;
		String strColType;
		String strRowNo="";
		String strColNo="";
            if (rsResult.next())
            {
                int intRowNo = rsResult.getInt("CELL_intRowNo");
                int intColNo = rsResult.getInt("CELL_intColNo");
		strRowType = rsResult.getString("TRAY_strRowNoType");
		strColType = rsResult.getString("TRAY_strColNoType");
	    
		 if (strRowType.equals("Alphabet"))
                    strRowNo = ALPHABET.substring(intRowNo-1, intRowNo);
                else
                    strRowNo = intRowNo + "";

                if (strColType.equals("Alphabet"))
                    strColNo = ALPHABET.substring(intColNo-1, intColNo);
                else
                    strColNo = intColNo + "";
	
	}	
			
		strXML = buildCellXMLFile(intCurrentTrayID);
		strXML += "<intCellID>"+intCellID+"</intCellID>";
		strXML += "<TRAY_intTrayID>"+intTrayID+"</TRAY_intTrayID>";
		strXML += "<CELL_strRow>"+strRowNo+"</CELL_strRow>";
		strXML += "<CELL_strCol>"+strColNo+"</CELL_strCol>";
		System.err.println(strXML);
	}
	catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }

    }
    	
    private void displayHistoryForm(AuthToken authToken)
    {
        Vector formFields = DatabaseSchema.getFormFields("cinventory_get_display");
        
        try
        {
            strStylesheet = VIEW_HISTORY;
            strXML = InventoryHistory.buildHistory(authToken).toString();
            strXML += (QueryChannel.buildFormLabelXMLFile(formFields));
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
        }
    }	
}
