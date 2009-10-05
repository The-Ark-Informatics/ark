/*
 * InventoryTranslator.java
 *
 * Created on 11 July 2005, 09:35
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
import org.jasig.portal.ChannelRuntimeData;

public class InventoryTranslator {
    
    /** Creates a new instance of InventoryTranslator */
    public InventoryTranslator() 
    {
    }
    public Site getSiteObject(ChannelRuntimeData rd)
    {
        Site stObj =null;
        if(rd.getParameter("SITE_intSiteID") != null || rd.getParameter("SITE_strSiteName") != null)
        {
             stObj = new Site();
             if(rd.getParameter("SITE_intSiteID") != null)
             {
                stObj.setSiteID(rd.getParameter("SITE_intSiteID"));
             }
             if(rd.getParameter("SITE_strSiteName") != null)
             {
                stObj.setSiteName(rd.getParameter("SITE_strSiteName"));
             }
             if(rd.getParameter("SITE_strSiteContact") != null)
             {
                stObj.setContact(rd.getParameter("SITE_strSiteContact"));
             }
             if(rd.getParameter("SITE_strSiteAddress") != null)
             {
                stObj.setAddress(rd.getParameter("SITE_strSiteAddress"));
             }
             if(rd.getParameter("SITE_strSitePhone") != null)
             {
                stObj.setPhone(rd.getParameter("SITE_strSitePhone"));
             }
            Tank tnkObj = this.getTankObj(rd);
            if(tnkObj != null)
            {
                stObj.addTank(tnkObj);
            }
        }
        return stObj;
    }
    public Tank getTankObj(ChannelRuntimeData rd)
    {
        Tank tnkObj = null;
        if(rd.getParameter("TANK_intTankID") != null || rd.getParameter("TANK_strTankName") != null)
        {
            tnkObj = new Tank();
            if(rd.getParameter("TANK_intTankID") != null)
            {
                tnkObj.setTankID(rd.getParameter("TANK_intTankID"));
            }
            if(rd.getParameter("TANK_strTankName") != null)
            {
                tnkObj.setTankName(rd.getParameter("TANK_strTankName"));
            }
            if(rd.getParameter("TANK_strStatus") != null)
            {
                tnkObj.setStatus(rd.getParameter("TANK_strStatus"));
            }
            if(rd.getParameter("TANK_dtCommissionDate") != null)
            {
                tnkObj.setCommissionDate(rd.getParameter("TANK_dtCommissionDate"));
            }
            if(rd.getParameter("TANK_dtLastServiceDate") != null)
            {
                tnkObj.setServiceDate(rd.getParameter("TANK_dtLastServiceDate"));
            }
            if(rd.getParameter("TANK_strTankLocation") != null)
            {
                tnkObj.setLocation(rd.getParameter("TANK_strTankLocation"));
            }
            if(rd.getParameter("TANK_strTankDescription") != null)
            {
                tnkObj.setDescription(rd.getParameter("TANK_strTankDescription"));
            }
            if(rd.getParameter("TANK_strLastServiceNote") != null)
            {
                tnkObj.setServiceNotes(rd.getParameter("TANK_strLastServiceNote"));
            }
            if(rd.getParameter("TANK_dtDecommissionDate") != null)
            {
                tnkObj.setDecommissionDate(rd.getParameter("TANK_dtDecommissionDate"));
            }
            Box bxObj = this.getBoxObj(rd);
            if(bxObj != null)
            {
                tnkObj.addBox(bxObj);
            }
        }
        return tnkObj;
    }
    public Box getBoxObj(ChannelRuntimeData rd)
    {
        Box bxObj = null;
        if(rd.getParameter("BOX_intBoxID") != null || rd.getParameter("BOX_strBoxName") != null)
        {
            bxObj = new Box();
            if(rd.getParameter("BOX_intBoxID") != null)
            {
                bxObj.setBoxID(rd.getParameter("BOX_intBoxID"));
            }
            if(rd.getParameter("BOX_strBoxName") != null)
            {
		bxObj.setBoxName(rd.getParameter("BOX_strBoxName"));
            }
            if(rd.getParameter("BOX_strBoxDescription") != null)
            {
                bxObj.setDescription(rd.getParameter("BOX_strBoxDescription"));
            }
            Tray tryObj = this.getTrayObj(rd);
            if(tryObj != null)
            {
                bxObj.addTray(tryObj);
            }
        }
        return bxObj;
    }
    public Tray getTrayObj(ChannelRuntimeData rd)
    {
        Tray tryObj = null;
        if(rd.getParameter("TRAY_intTrayID") != null || rd.getParameter("TRAY_strTrayName") != null)
        {
            tryObj = new Tray();
            if(rd.getParameter("TRAY_intTrayID") != null)
            {
                tryObj.setTrayID(rd.getParameter("TRAY_intTrayID"));
            }
            if(rd.getParameter("TRAY_strTrayName") != null)
            {
                tryObj.setTrayName(rd.getParameter("TRAY_strTrayName"));
            }
            if(rd.getParameter("TRAY_intNoOfRow") != null && rd.getParameter("TRAY_intNoOfCol") != null)
            {
                tryObj.setColsRows(rd.getParameter("TRAY_intNoOfRow"), rd.getParameter("TRAY_intNoOfCol"),rd.getParameter("TRAY_strRowNoType"), rd.getParameter("TRAY_strColNoType"));
            }
        }
        return tryObj;
    }
    
}
