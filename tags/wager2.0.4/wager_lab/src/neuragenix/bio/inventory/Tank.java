/*
 * Tank.java
 *
 * Created on 28 June 2005, 10:08
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
import java.util.Vector;
public class Tank 
{
    String strTankName = "";
    String strTankID = "";
    String strParentID = "";
    String strStatus=null;
    String strCommissionDate = null;
    String strServiceDate = null;
    String strServiceNotes = null;
    String strDecommissionDate = null;
    String strLocation = null;
    String strDescription = null;
    Vector<Box> vtBox = null;
    /** Creates a new instance of Tank */
    public Tank() 
    {
        vtBox = new Vector<Box>();
    }
    public Tank(String strName,String strID)
    {
        this();
        this.strTankName = strName;
        this.strTankID = strID;
    }
    public Tank(String strName,String strID,String strParent)
    {
        this(strName,strID);
        this.strParentID = strParent;
    }
    
    public void setTankName(String strName)
    {
        this.strTankName = strName;
    }
    public String getTankName()
    {
        return this.strTankName;
    }
    public void setTankID(String strID)
    {
        this.strTankID = strID;
    }
    public String getTankID()
    {
        return this.strTankID;
    }
    public void setParentID(String strID)
    {
        this.strParentID = strID;
    }
    public String getParentID()
    {
        return this.strParentID;
    }
    public void addBox(Box bxObj)
    {
        this.vtBox.add(bxObj);
    }
    public void setBoxes(Vector<Box> vtBoxes)
    {
        this.vtBox = vtBoxes;
    }
    public Vector<Box> getBoxes()
    {
        return this.vtBox;
    }
    public void setStatus(String str_Status)
    {
        this.strStatus = str_Status;
    }
    public String getStatus()
    {
        return this.strStatus;
    }
    public void setCommissionDate(String str_CommissionDate)
    {
        this.strCommissionDate = str_CommissionDate;
    }
    public String getCommissionDate()
    {
        return this.strCommissionDate;
    }
    public void setServiceDate(String str_ServiceDate)
    {
        this.strServiceDate = str_ServiceDate;
    }
    public String getServiceDate()
    {
        return this.strServiceDate;
    }
    public void setServiceNotes(String str_ServiceNotes)
    {
        this.strServiceNotes = str_ServiceNotes;
    }
    public String getServiceNotes()
    {
        return this.strServiceNotes;
    }
    public void setDecommissionDate(String str_DecommissionDate)
    {
        this.strDecommissionDate = str_DecommissionDate;
    }
    public String getDecommissionDate()
    {
        return this.strDecommissionDate;
    }
    public void setLocation(String str_Location)
    {
        this.strLocation = str_Location;
    }
    public String getLocation()
    {
        return this.strLocation;
    }
    public void setDescription(String str_Description)
    {
        this.strDescription = str_Description;
    }
    public String getDescription()
    {
        return this.strDescription;
    }
}
