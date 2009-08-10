/*
 * Site.java
 *
 * Created on 28 June 2005, 10:08
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
import java.util.Vector;
/**
 * This is a Site EntityBean that has trays within it
 */
public class Site 
{
    String strSiteName = "";
    String strSiteID = "";
    Vector<Tank> vtTank = null;
    String strContact = null;
    String strAddress = null;
    String strPhone = null;
    /** Creates a new instance of Site */
    public Site() 
    {
        vtTank = new Vector<Tank>();
    }
    /**
     * This is a constructor that creates a Site object with a name and ID
     * @param strName Name of the Site
     * @param strID ID is the Key of this Site in the Site Table
     */    
    public Site(String strName,String strID)
    {
        this();
        this.strSiteName = strName;
        this.strSiteID = strID;
    }
    /**
     * Sets the name of the site to this name
     * @param strName The name of the site
     */    
    public void setSiteName(String strName)
    {
        this.strSiteName = strName;
    }
    /**
     * used to get the site name of this object
     * @return returns the site name of this object
     */    
    public String getSiteName()
    {
        return this.strSiteName;
    }
    /**
     * sets the siteID which is the Key in the Database table for this object
     * @param strID the key of this object in the database
     */    
    public void setSiteID(String strID)
    {
        this.strSiteID = strID;
    }
    /**
     * gets the key of this object as in the Database
     * @return a the key of this object in the database
     */    
    public String getSiteID()
    {
        return this.strSiteID;
    }
    public void setContact(String str_contact)
    {
        this.strContact = str_contact;
    }
    public String getContact()
    {
        return this.strContact;
    }
    public void setAddress(String str_address)
    {
        this.strAddress = str_address;
    }
    public String getAddress()
    {
        return this.strAddress;
    }
    public void setPhone(String str_phone)
    {
        this.strPhone = str_phone;
    }
    public String getPhone()
    {
        return this.strPhone;
    }
    /**
     * add a Tank object to the Vector of tanks already associated with
     * this Site
     * @param tkObj the tank object to be added to this site
     */    
    public void addTank(Tank tkObj)
    {
        this.vtTank.add(tkObj);
    }
    
    public void setTanks(Vector<Tank> vtTanks)
    {
        this.vtTank = vtTanks;
    }
    /**
     * gets the Vector of Tanks associated with this SIte
     * @return Vector of Tanks
     */    
    public Vector<Tank> getTanks()
    {
        return this.vtTank;
    }
}
