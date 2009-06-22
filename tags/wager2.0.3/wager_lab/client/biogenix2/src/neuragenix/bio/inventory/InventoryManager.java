/*
 * InventoryManager.java
 *
 * Created on 28 June 2005, 10:08
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
import java.util.Vector;
import java.sql.SQLException;

import neuragenix.security.AuthToken;
import neuragenix.dao.exception.DAOException;

import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.services.LogService;

public class InventoryManager 
{
    public static final String TYPE_LIST_INVENTORY_NAMES = "List_Inventory_names";
    public static final String TYPE_LIST_SITE_N_TANKS_NAMES = "List_Site_n_tank_names";
    public static final String TYPE_LIST_SITE_TANK_N_BOX_NAMES = "List_Site_Tank_n_Box_names";
    public static final String DUPLICATE_SITE_CHECK = "Duplicate site check";
    public static final String DUPLICATE_TANK_CHECK = "Duplicate tank check";
    public static final String DUPLICATE_BOX_CHECK = "Duplicate box check";
    public static final String DUPLICATE_TRAY_CHECK = "Duplicate tray check";
    private InventoryPersistanceMgr invpersisMgr = null;
    private InventoryView invView = null;
    private InventoryTranslator invTrans= null;
    private AuthToken authToken = null;
    /** Creates a new instance of InventoryManager */
    public InventoryManager() 
    {
        invpersisMgr = new InventoryPersistanceMgr();
        invView = new InventoryView();
        invTrans = new InventoryTranslator();
        
    }
    public InventoryManager(AuthToken authToken)
    {
        this();
        this.authToken = authToken;
    }
    public InventoryPersistanceMgr getInventoryPersistanceMgr()
    {
        return this.invpersisMgr;
    }
    public InventoryView getInventoryView()
    {
        return this.invView;
    }
    public InventoryTranslator getInventoryTranslator()
    {
        return this.invTrans;
    }
    public Site createSite(ChannelRuntimeData rd)
    {
        Site stObj = this.invTrans.getSiteObject(rd);
        return stObj;
    }
    public String objectExists(String domain,String strID)
    {
        String strError = "";
        String strIDName = "";
        if(domain.equalsIgnoreCase("SITE"))
        {
            strIDName = "SITE_intSiteID";
        }
        else if(domain.equalsIgnoreCase("TANK"))
	{
            strIDName = "TANK_intTankID";
        }
        else if(domain.equalsIgnoreCase("BOX"))
	{
            strIDName = "BOX_intBoxID";
        }
        else if(domain.equalsIgnoreCase("TRAY"))
	{
            strIDName = "TRAY_intTrayID";
        }
        boolean exists = false;
        try
        {
            exists = this.invpersisMgr.checkObject(domain,strIDName,strID);
        }
        catch(DAOException daoe)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the check for existance of object in Inventory - " + daoe.toString(), daoe);
        }
        catch(SQLException sqle)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the check for existance of object in Inventory - " + sqle.toString(), sqle);
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the check for existance of object in Inventory - " + e.toString(), e);
        }
        if(!exists)
        {
            strError = "One or more objects related to this action may be deleted by another user";
        }
        return strError;
    }
    public String checkIfDuplicateName(String type,String strname,String strID,String strParentID)
    {
        String strError = "";
        try
        {
            if(type.equalsIgnoreCase(DUPLICATE_SITE_CHECK))
            {
                Vector vtSites = this.invpersisMgr.getEntities("SITE",strParentID);
                for(java.util.Enumeration esites=vtSites.elements();esites.hasMoreElements();)
                {
                    Site strObj = ((Site)esites.nextElement());
                    if(strObj.getSiteName().equalsIgnoreCase(strname) && !strObj.getSiteID().equals(strID))
                    {
                        strError += "This name already exists";
                        return strError;
                    }
                }
            }
            else if(type.equalsIgnoreCase(DUPLICATE_TANK_CHECK))
            {
                Vector vtTanks =  this.invpersisMgr.getEntities("TANK",strParentID);
                for(java.util.Enumeration eTanks=vtTanks.elements();eTanks.hasMoreElements();)
                {
                    Tank strObj = (Tank)eTanks.nextElement();
                    if(strObj.getTankName().equalsIgnoreCase(strname) && !strObj.getTankID().equals(strID))
                    {
                        strError += "This name already exists";
                        return strError;
                    }
                }

            }
            else if(type.equalsIgnoreCase(DUPLICATE_BOX_CHECK))
            {
                Vector vtBox =  this.invpersisMgr.getEntities("BOX",strParentID);
                for(java.util.Enumeration eBox=vtBox.elements();eBox.hasMoreElements();)
                {
                    Box strObj = (Box)eBox.nextElement();
                    if(strObj.getBoxName().equalsIgnoreCase(strname) && !strObj.getBoxID().equals(strID))
                    {
                        strError += "This name already exists";
                        return strError;
                    }
                }
            }
            else if(type.equalsIgnoreCase(DUPLICATE_TRAY_CHECK))
            {
                Vector vtTray =  this.invpersisMgr.getEntities("TRAY",strParentID);
                for(java.util.Enumeration eTray=vtTray.elements();eTray.hasMoreElements();)
                {
                    Tray strObj = (Tray)eTray.nextElement();
                    if(strObj.getTrayName().equalsIgnoreCase(strname) && !strObj.getTrayID().equals(strID))
                    {
                        strError += "This name already exists";
                        return strError;
                    }
                }
            }
        }
        catch(DAOException daoe)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the check for duplicates in Inventory - " + daoe.toString(), daoe);
        }
        catch(SQLException sqle)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the check for duplicates in Inventory - " + sqle.toString(), sqle);
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the check for duplicates in Inventory - " + e.toString(), e);
        }
        return strError;
    }
    private Vector getInventoryList(String strSecurity,AuthToken authToken)
    {
        Vector vtSitesList = null;
        try
        {
            vtSitesList = this.invpersisMgr.buildInventoryList(strSecurity, authToken);
            return vtSitesList;
        }
        catch(DAOException daoe)
        {
            LogService.instance().log(LogService.ERROR, "DAOException when trying to get the list in Inventory - " + daoe.toString(), daoe);
        }
        catch(SQLException sqle)
        {
            LogService.instance().log(LogService.ERROR, "SQLException when trying to get the list in Inventory - " + sqle.toString(), sqle);
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the list in Inventory - " + e.toString(), e);
        }
        return new Vector();
    }
    private Vector getInventoryListSitenTanks(String strSecurity,AuthToken authToken)
    {
        try
        {
            return this.invpersisMgr.buildInventoryListSitenTanks(strSecurity, authToken);
        }
        catch(DAOException daoe)
        {
            LogService.instance().log(LogService.ERROR, "DAOException when trying to get the list in Inventory - " + daoe.toString(), daoe);
        }
        catch(SQLException sqle)
        {
            LogService.instance().log(LogService.ERROR, "SQLException when trying to get the list in Inventory - " + sqle.toString(), sqle);
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the list in Inventory - " + e.toString(), e);
        }
        return new Vector();
    }
    private Vector getInventoryListSiteTanknBoxes(String strSecurity,AuthToken authToken)
    {
        try
        {
            return this.invpersisMgr.buildInventoryListSiteTanksnBoxes(strSecurity, authToken);
        }
        catch(DAOException daoe)
        {
            LogService.instance().log(LogService.ERROR, "DAOException when trying to get the list in Inventory - " + daoe.toString(), daoe);
        }
        catch(SQLException sqle)
        {
            LogService.instance().log(LogService.ERROR, "SQLException when trying to get the list in Inventory - " + sqle.toString(), sqle);
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to get the list in Inventory - " + e.toString(), e);
        }
        return new Vector();
    }
    public String getSiteXML(Site stObj)
    {
        return this.invView.buildSiteXML(stObj);
    }
    private String getInventoryListView(Vector vtSites)
    {
        return this.invView.buildInventoryListXML(vtSites);
    }
    private String getInventorySitenTanksView(Vector vtSites)
    {
        return this.invView.buildInvListSitenTanks(vtSites);
    }
    private String getInventorySiteTanknBoxView(Vector vtSites)
    {
        return this.invView.buildInvListSiteTanksnBoxes(vtSites);
    }
    public String getInventoryDetails(String type,String strSecurity,AuthToken authToken)
    {
        
        Vector vtInvList = null;
        StringBuffer strXML = new StringBuffer();
        try
        {
            if(authToken.hasActivity(strSecurity))
            {
                if(type.equalsIgnoreCase(this.TYPE_LIST_INVENTORY_NAMES))
                {
                    vtInvList = getInventoryList(strSecurity,authToken);
                    strXML.append(getInventoryListView(vtInvList));
                }
                else if(type.equalsIgnoreCase(this.TYPE_LIST_SITE_N_TANKS_NAMES))
                {
                    vtInvList = this.getInventoryListSitenTanks(strSecurity, authToken);
                    strXML.append(this.getInventorySitenTanksView(vtInvList));
                }
                else if(type.equalsIgnoreCase(this.TYPE_LIST_SITE_TANK_N_BOX_NAMES))
                {
                    vtInvList = this.getInventoryListSiteTanknBoxes(strSecurity, authToken);
                    strXML.append(this.getInventorySiteTanknBoxView(vtInvList));
                }
            }
        }
        catch(neuragenix.security.exception.SecurityException see)
        {
            LogService.instance().log(LogService.ERROR, "Unknown Exception when trying to check the permissions - " + see.toString(), see);            
        }
        return strXML.toString();
    }

}
