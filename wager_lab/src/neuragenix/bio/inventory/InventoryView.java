/*
 * InventoryView.java
 *
 * Created on 28 June 2005, 10:20
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
import java.util.Vector;
import java.util.Enumeration;
import neuragenix.common.*;
/**
 * This is the View in the MVC pattern and creates all the different XML's
 * for the views
 */
public class InventoryView 
{
    private boolean blSite = true;
    private boolean blTank = true;
    private boolean blBox = true;
    private boolean blTray = true;
    /** Creates a new instance of InventoryView */
    public InventoryView() 
    {
    }
    public String buildListSite(Vector vtSites)
    {
        StringBuffer strbfrXML = new StringBuffer();
        for(Enumeration eSites=vtSites.elements();eSites.hasMoreElements();)
        {
            Site stObj = (Site)eSites.nextElement();
            strbfrXML.append(this.buildSiteXML(stObj));
        }
        return strbfrXML.toString();
    }
    public String buildInvListSitenTanks(Vector vtSites)
    {
        this.blBox = false;
        StringBuffer strbfrXML = new StringBuffer("<InventoryList>"+this.buildListSite(vtSites)+"</InventoryList>");
        this.blBox = true;
        return strbfrXML.toString();
    }
    public String buildInvListSiteTanksnBoxes(Vector vtSites)
    {
	this.blTray = false;
        StringBuffer strbfrXML = new StringBuffer("<InventoryList>"+this.buildListSite(vtSites)+"</InventoryList>");
        this.blTray = true;
        return strbfrXML.toString();
    }
    public String buildSiteXML(Site stObj)
    {
        StringBuffer strbfrXML = new StringBuffer();
        strbfrXML.append("<SITE>");
        if(stObj.getSiteID().length() > 0)
        {
            strbfrXML.append("<SITE_intSiteID>" + stObj.getSiteID() + "</SITE_intSiteID>");
        }
        if(stObj.getSiteName().length() > 0)
        {
            strbfrXML.append("<SITE_strSiteName>" + Utilities.cleanForXSL(stObj.getSiteName()) + "</SITE_strSiteName>");
        }
        if(stObj.getContact() != null)
        {
            strbfrXML.append("<SITE_strSiteContact>" + Utilities.cleanForXSL(stObj.getContact()) + "</SITE_strSiteContact>");
        }
        if(stObj.getAddress() != null)
        {
            strbfrXML.append("<SITE_strSiteAddress>" + Utilities.cleanForXSL(stObj.getAddress()) + "</SITE_strSiteAddress>");
        }
        if(stObj.getPhone() != null)
        {
            strbfrXML.append("<SITE_strSitePhone>" + Utilities.cleanForXSL(stObj.getPhone()) + "</SITE_strSitePhone>");
        }
        if(this.blTank)
        {
            Vector vtTanks = stObj.getTanks();
            for(Enumeration eTanks=vtTanks.elements();eTanks.hasMoreElements();)
            {
                Tank tnkObj =  (Tank)eTanks.nextElement();
                strbfrXML.append(this.buildTankXML(tnkObj));
            }
        }
        strbfrXML.append("</SITE>");
        return strbfrXML.toString();
    }
    public String buildTankXML(Tank tnkObj)
    {
        StringBuffer strbfrXML = new StringBuffer();
        strbfrXML.append("<TANK>");
        strbfrXML.append("<TANK_intTankID>" + tnkObj.getTankID() + "</TANK_intTankID>");
        if(tnkObj.getTankName().length() > 0 )
        {
            strbfrXML.append("<TANK_strTankName>" + Utilities.cleanForXSL(tnkObj.getTankName()) + "</TANK_strTankName>");
        }
        if(tnkObj.getStatus() != null)
        {
            strbfrXML.append("<TANK_strStatus>" + Utilities.cleanForXSL(tnkObj.getStatus()) + "</TANK_strStatus>");
        }
        if(tnkObj.getCommissionDate() != null)
        {
            String[] strDate = this.splitDates(tnkObj.getCommissionDate());
            strbfrXML.append("<TANK_dtCommissionDate_Day>" + strDate[0] + "</TANK_dtCommissionDate_Day>");
            strbfrXML.append("<TANK_dtCommissionDate_Month>" + strDate[1] + "</TANK_dtCommissionDate_Month>");
            strbfrXML.append("<TANK_dtCommissionDate_Year>" + strDate[2] + "</TANK_dtCommissionDate_Year>");
        }
        if(tnkObj.getServiceDate() != null)
        {
            String[] strDate = this.splitDates(tnkObj.getServiceDate());
            strbfrXML.append("<TANK_dtLastServiceDate_Day>" + strDate[0] + "</TANK_dtLastServiceDate_Day>");
            strbfrXML.append("<TANK_dtLastServiceDate_Month>" + strDate[1] + "</TANK_dtLastServiceDate_Month>");
            strbfrXML.append("<TANK_dtLastServiceDate_Year>" + strDate[2] + "</TANK_dtLastServiceDate_Year>");
        }
        if(tnkObj.getServiceNotes() != null)
        {
            strbfrXML.append("<TANK_strLastServiceNote>" + Utilities.cleanForXSL(tnkObj.getServiceNotes()) + "</TANK_strLastServiceNote>");
        }
        if(tnkObj.getDecommissionDate() != null)
        {
            String[] strDate = this.splitDates(tnkObj.getDecommissionDate());
            strbfrXML.append("<TANK_dtDecommissionDate_Day>" + strDate[0] + "</TANK_dtDecommissionDate_Day>");
            strbfrXML.append("<TANK_dtDecommissionDate_Month>" + strDate[1] + "</TANK_dtDecommissionDate_Month>");
            strbfrXML.append("<TANK_dtDecommissionDate_Year>" + strDate[2] + "</TANK_dtDecommissionDate_Year>");
        }
        if(tnkObj.getLocation() != null)
        {
            strbfrXML.append("<TANK_strTankLocation>" + Utilities.cleanForXSL(tnkObj.getLocation()) + "</TANK_strTankLocation>");
        }
        if(tnkObj.getDescription() != null)
        {
            strbfrXML.append("<TANK_strTankDescription>" + Utilities.cleanForXSL(tnkObj.getDescription()) + "</TANK_strTankDescription>");
        }
        if(this.blBox)
        {
            Vector vtBoxes = tnkObj.getBoxes();
            for(Enumeration eBoxes=vtBoxes.elements();eBoxes.hasMoreElements();)
            {
                Box bxObj = (Box)eBoxes.nextElement();
                strbfrXML.append(this.buildBoxXML(bxObj));
            }
        }
        strbfrXML.append("</TANK>");
        return strbfrXML.toString();
    }
    public String buildBoxXML(Box bxObj)
    {
        StringBuffer strbfrXML = new StringBuffer();
        strbfrXML.append("<BOX>");
        strbfrXML.append("<BOX_intBoxID>" + bxObj.getBoxID() + "</BOX_intBoxID>");
        if(bxObj.getBoxName().length() > 0)
        {
            strbfrXML.append("<BOX_strBoxName>" + Utilities.cleanForXSL(bxObj.getBoxName()) + "</BOX_strBoxName>");
        }
        if(bxObj.getDescription().length() > 0)
        {
            strbfrXML.append("<BOX_strBoxDesc>" + Utilities.cleanForXSL(bxObj.getDescription()) + "</BOX_strBoxDesc>");
        }
        if(this.blTray)
        {
            Vector vtTrays = bxObj.getTrays();
            for(Enumeration eTrays=vtTrays.elements();eTrays.hasMoreElements();)
            {
                Tray tryObj = (Tray)eTrays.nextElement();
                strbfrXML.append(this.buildTrayXML(tryObj));
            }
        }
        strbfrXML.append("</BOX>");
        return strbfrXML.toString();
    }
    public String buildTrayXML(Tray tryObj)
    {
        StringBuffer strbfrXML = new StringBuffer();
        strbfrXML.append("<TRAY>");
            strbfrXML.append("<TRAY_intTrayID>"+ tryObj.getTrayID() +"</TRAY_intTrayID>");
            if(tryObj.getTrayName().length() > 0)
            {
            	strbfrXML.append("<TRAY_strTrayName>"+ Utilities.cleanForXSL(tryObj.getTrayName())+"</TRAY_strTrayName>");
            }
            if(tryObj.getRows().length() > 0)
            {
                strbfrXML.append("<TRAY_intNoOfRows>"+tryObj.getRows() +"</TRAY_intNoOfRows>");
                strbfrXML.append("<TRAY_strRowNoType>"+ tryObj.getRowType() +"</TRAY_strRowNoType>");
            }
            if(tryObj.getCols().length() > 0)
            {
                strbfrXML.append("<TRAY_intNoOfCols>"+ tryObj.getCols() +"</TRAY_intNoOfCols>");
                strbfrXML.append("<TRAY_strColNoType>"+ tryObj.getColType() +"</TRAY_strColNoType>");
            }
            
        strbfrXML.append("</TRAY>");
        return strbfrXML.toString();
    }
    /**
     * This method is called when a list of all names of the sites,Tanks,Boxes and Trays
     * are required to be displayed
     * @param vtSites accepts a vector of Sites
     * @return returns an XML required for the display
     */    
    public String buildInventoryListXML(Vector vtSites)
    {
        StringBuffer strbfrXML = new StringBuffer("<InventoryList>");
        for(Enumeration eSites=vtSites.elements();eSites.hasMoreElements();)
        {
            Site stObj = (Site)eSites.nextElement();
            strbfrXML.append(this.buildSiteXML(stObj));
        }
        strbfrXML.append("</InventoryList>");
        return strbfrXML.toString();
    }
    private String[] splitDates(String strDate)
    {
        return strDate.split("/");
    }
}
