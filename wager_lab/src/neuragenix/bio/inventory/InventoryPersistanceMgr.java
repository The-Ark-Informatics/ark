/*
 * InventoryFactory.java
 *
 * Created on 28 June 2005, 10:19
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
import java.util.Vector;
import java.util.Enumeration;
import java.sql.ResultSet;
import java.sql.SQLException;

import neuragenix.security.AuthToken;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.DALQuery;
import neuragenix.dao.exception.DAOException;
/**
 * This class is the one that queries the Database to give the required data
 * regarding the inventory
 */
public class InventoryPersistanceMgr 
{
    
    /** Creates a new instance of InventoryFactory */
    public InventoryPersistanceMgr() 
    {
    }
    
    public Vector buildInventoryListSitenTanks(String strSecurity,AuthToken authToken)  throws DAOException,SQLException,Exception
    {
        Vector vtSites = this.getSites(strSecurity,authToken);
        for(Enumeration eSites = vtSites.elements();eSites.hasMoreElements();)
        {
            Site stObj = (Site)eSites.nextElement();
            Vector vtTanks = this.getTanks(strSecurity,authToken,stObj);
            stObj.setTanks(vtTanks);
        }
        return vtSites;
    }
    public boolean checkObject(String domain,String strIDName,String strID) throws DAOException,SQLException,Exception
    {
        boolean exists = false;
        DALQuery qrchkObj = new DALQuery();
        qrchkObj.setDomain(domain, null, null, null); 
        qrchkObj.setField(strIDName,null);
        qrchkObj.setWhere(null, 0, domain + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        qrchkObj.setWhere("AND", 0, strIDName , "=", strID, 0, DALQuery.WHERE_HAS_VALUE);
        ResultSet rschkObj = qrchkObj.executeSelect();
        if(rschkObj.next())
        {
            exists = true;
        }
        return exists;
    }
    public Vector buildInventoryListSiteTanksnBoxes(String strSecurity,AuthToken authToken) throws DAOException,SQLException,Exception
    {
        Vector vtSites = this.buildInventoryListSitenTanks(strSecurity,authToken);
        for(Enumeration eSites = vtSites.elements();eSites.hasMoreElements();)
        {
            Site stObj = (Site)eSites.nextElement();
            Vector vtTanks = stObj.getTanks();
            for(Enumeration eTanks = vtTanks.elements();eTanks.hasMoreElements();)
            {
                Tank tnkObj = (Tank)eTanks.nextElement();                
                Vector vtBoxes = this.getBoxes(strSecurity,authToken,tnkObj);
                tnkObj.setBoxes(vtBoxes);
            }
        }
        return vtSites;
    }
    public Vector getEntities(String domain,String strParentID) throws DAOException,SQLException,Exception
    {
        Vector vtEntities = new Vector();
        DALQuery qryEntities = new DALQuery();
        qryEntities.setDomain(domain, null, null, null);    
        ResultSet rsEntities = null;
            if(domain.equalsIgnoreCase("SITE"))
            {
                qryEntities.setField("SITE_strSiteName",null);
                qryEntities.setField("SITE_intSiteID",null);
                qryEntities.setWhere(null, 0, "SITE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                rsEntities = qryEntities.executeSelect();
                while(rsEntities.next())
                {
                    Site strObj = new Site();
                    strObj.setSiteName(rsEntities.getString("SITE_strSiteName"));
                    strObj.setSiteID(rsEntities.getString("SITE_intSiteID"));
                    vtEntities.add(strObj);                
                }
            }
            else if(domain.equalsIgnoreCase("TANK"))
            {
                qryEntities.setField("TANK_strTankName",null);
                qryEntities.setField("TANK_intSiteID",null);
                qryEntities.setField("TANK_intTankID",null);
                qryEntities.setWhere(null, 0, "TANK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qryEntities.setWhere("AND", 0, "TANK_intSiteID", "=", strParentID, 0, DALQuery.WHERE_HAS_VALUE);
                rsEntities = qryEntities.executeSelect();
                while(rsEntities.next())
                {
                    Tank strObj = new Tank();
                    strObj.setTankName(rsEntities.getString("TANK_strTankName"));
                    strObj.setParentID(rsEntities.getString("TANK_intSiteID"));
                    strObj.setTankID(rsEntities.getString("TANK_intTankID"));
                    vtEntities.add(strObj);                
                }
            }
            else if(domain.equalsIgnoreCase("BOX"))
            {
                qryEntities.setField("BOX_strBoxName",null);
                qryEntities.setField("BOX_intTankID",null);
                qryEntities.setField("BOX_intBoxID",null);
                qryEntities.setWhere(null, 0, "BOX_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qryEntities.setWhere("AND", 0, "BOX_intTankID", "=", strParentID, 0, DALQuery.WHERE_HAS_VALUE);
                rsEntities = qryEntities.executeSelect();
                while(rsEntities.next())
                {
                    Box strObj = new Box();
                    strObj.setBoxName(rsEntities.getString("BOX_strBoxName"));
                    strObj.setBoxID(rsEntities.getString("BOX_intBoxID"));
                    strObj.setParentID(rsEntities.getString("BOX_intTankID"));
                    vtEntities.add(strObj);                
                }               
            }
            else if(domain.equalsIgnoreCase("TRAY"))
            {
                qryEntities.setField("TRAY_strTrayName",null);
                qryEntities.setField("TRAY_intBoxID",null);
                qryEntities.setField("TRAY_intTrayID",null);
                qryEntities.setWhere(null, 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qryEntities.setWhere("AND", 0, "TRAY_intBoxID", "=", strParentID, 0, DALQuery.WHERE_HAS_VALUE);
                rsEntities = qryEntities.executeSelect();
                while(rsEntities.next())
                {
                    Tray strObj = new Tray();
                    strObj.setTrayName(rsEntities.getString("TRAY_strTrayName"));
                    strObj.setTrayID(rsEntities.getString("TRAY_intTrayID"));
                    strObj.setParentID(rsEntities.getString("TRAY_intBoxID"));
                    vtEntities.add(strObj);                
                }               
            }
            rsEntities.close();
            return vtEntities;
    }
    public Vector getSites(String strSecurity,AuthToken authToken) throws DAOException,SQLException,Exception
    {
        //get all the sites and close the resultSet
        Vector vtSites = new Vector();
        DALSecurityQuery qrySites = new DALSecurityQuery(strSecurity, authToken);
        qrySites.setDomain("SITE", null, null, null);    
        qrySites.setField("SITE_intSiteID",null);
        qrySites.setField("SITE_strSiteName",null);
        qrySites.setWhere(null, 0, "SITE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        ResultSet rsSites = qrySites.executeSelect();
        while(rsSites.next())
        {
            Site stObj = new Site(rsSites.getString("SITE_strSiteName"),rsSites.getString("SITE_intSiteID"));
            vtSites.add(stObj);
        }
        rsSites.close();
        return vtSites;
    }
    public Vector getTanks(String strSecurity,AuthToken authToken,Site stObj) throws DAOException,SQLException,Exception
    {
        Vector vtTanks = new Vector();
        DALSecurityQuery qryTank = new DALSecurityQuery(strSecurity, authToken);
        qryTank.setDomain("TANK", null, null, null);    
        qryTank.setField("TANK_intTankID",null);
        qryTank.setField("TANK_strTankName",null);
        qryTank.setWhere(null, 0, "TANK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        if(stObj != null)
        {
            qryTank.setWhere("AND", 0, "TANK_intSiteID", "=", stObj.getSiteID(), 0, DALQuery.WHERE_HAS_VALUE);	
        }
        ResultSet rsTank = qryTank.executeSelect();
        while(rsTank.next())
        {
            Tank tnkObj = new Tank(rsTank.getString("TANK_strTankName"),rsTank.getString("TANK_intTankID"));
            vtTanks.add(tnkObj);
        }
        rsTank.close();
        return vtTanks;
    }
    public Vector getBoxes(String strSecurity,AuthToken authToken,Tank tnkObj) throws DAOException,SQLException,Exception
    {
        Vector vtBoxes = new Vector();
        DALSecurityQuery qryBox = new DALSecurityQuery(strSecurity, authToken);
        qryBox.setDomain("BOX", null, null, null);    
        qryBox.setField("BOX_intBoxID",null);
        qryBox.setField("BOX_strBoxName",null);
        qryBox.setWhere(null, 0, "BOX_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        if(tnkObj != null)
        {
            qryBox.setWhere("AND", 0, "BOX_intTankID", "=", tnkObj.getTankID(), 0, DALQuery.WHERE_HAS_VALUE);	
        }
        ResultSet rsBox = qryBox.executeSelect();
        while(rsBox.next())
        {
            Box bxObj = new Box(rsBox.getString("BOX_strBoxName"),rsBox.getString("BOX_intBoxID"));
            vtBoxes.add(bxObj);
        }
        rsBox.close();
        return vtBoxes;
    }
    public Vector getTrays(String strSecurity,AuthToken authToken,Box bxObj) throws DAOException,SQLException,Exception
    {
        Vector vtTrays = new Vector();
        DALSecurityQuery qryTray = new DALSecurityQuery(strSecurity, authToken);
        qryTray.setDomain("TRAY", null, null, null);    
        qryTray.setField("TRAY_intTrayID",null);
        qryTray.setField("TRAY_strTrayName",null);
        qryTray.setWhere(null, 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        if(bxObj != null)
        {
            qryTray.setWhere("AND", 0, "TRAY_intBoxID", "=", bxObj.getBoxID(), 0, DALQuery.WHERE_HAS_VALUE);	
        }
        ResultSet rsTray = qryTray.executeSelect();
        while(rsTray.next())
        {
            Tray tryObj = new Tray(rsTray.getString("TRAY_strTrayName"),rsTray.getString("TRAY_intTrayID"));
            vtTrays.add(tryObj);
        }
        rsTray.close();
        return vtTrays;
    }
    /**
     * This method queries the database and get the details required to list the
     * inventory which is the name and ID's of the sites,Tanks,Boxes and Trays
     * @param strSecurity the permission that needs to be checked before performing this action
     * @param authToken The authorization token which holds all the permissions for
     * this user/session
     * @throws DAOException this is the Exception that is thrown by the DALQuery
     * @throws SQLException this is the Exception that is thrown by the DALQuery
     * @throws Exception this is the Exception that is thrown by the DALQuery
     * @return returns a Vector of Site objects indicating the Sites that are
     * available in this inventory
     */    
    public Vector buildInventoryList(String strSecurity,AuthToken authToken) throws DAOException,SQLException,Exception
    {
        Vector vtSites = this.buildInventoryListSiteTanksnBoxes(strSecurity, authToken);
        // for each Site get all the tanks and close the resultSet
        for(Enumeration eSites = vtSites.elements();eSites.hasMoreElements();)
        {
            Site stObj = (Site)eSites.nextElement();
            Vector vtTanks = stObj.getTanks();
            for(Enumeration eTanks = vtTanks.elements();eTanks.hasMoreElements();)
            {
                Tank tnkObj = (Tank)eTanks.nextElement();
                Vector vtBoxes = tnkObj.getBoxes();
                for(Enumeration eBoxes = vtBoxes.elements();eBoxes.hasMoreElements();)
                {
                    Box bxObj = (Box)eBoxes.nextElement();
                    Vector vtTrays = this.getTrays(strSecurity,authToken,bxObj);
                    bxObj.setTrays(vtTrays);
                }
            }
        }
        return vtSites;
    }
    
}
