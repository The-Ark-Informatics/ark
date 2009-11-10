/*
 * Box.java
 *
 * Created on 28 June 2005, 10:09
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
import java.util.Vector;
public class Box 
{
    String strBoxName = "";
    String strBoxID = "";
    String strParentID = "";
    String strDesc = "";
    Vector<Tray> vtTray = null;
    /** Creates a new instance of Box */
    public Box() 
    {
        vtTray = new Vector<Tray>();
    }
    public Box(String strName,String strID)
    {
        this();
        this.strBoxName = strName;
        this.strBoxID = strID;
    }
    public Box(String strName,String strID,String strParent)
    {
        this(strName,strID);
        this.strParentID = strParent;
    }
    public void setBoxName(String strName)
    {
        this.strBoxName = strName;
    }
    public String getBoxName()
    {
        return this.strBoxName;
    }
    public void setBoxID(String strID)
    {
        this.strBoxID = strID;
    }
    public String getBoxID()
    {
        return this.strBoxID;
    }
    public void setParentID(String strID)
    {
        this.strParentID = strID;
    }
    public String getParentID()
    {
        return this.strParentID;
    }
    public void setDescription(String strDesc)
    {
        this.strDesc = strDesc;
    }
    public String getDescription()
    {
        return this.strDesc;
    }
    public void addTray(Tray tryObj)
    {
        this.vtTray.add(tryObj);
    }
    public void setTrays(Vector<Tray> vtTrays)
    {
        this.vtTray = vtTrays;
    }
    public Vector<Tray> getTrays()
    {
        return this.vtTray;
    }
}
