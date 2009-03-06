/*
 * Tray.java
 *
 * Created on 28 June 2005, 10:09
 */

package neuragenix.bio.inventory;

/**
 *
 * @author  renny
 */
public class Tray 
{
    String strTrayName = "";
    String strTrayID = "";
    String strParentID = "";
    String strCols = "";
    String strRows = "";
    String strColsType = "";
    String strRowType = "";
    /** Creates a new instance of Tray */
    public Tray() 
    {
    }
    public Tray(String strName,String strID)
    {
        this();
        this.strTrayName = strName;
        this.strTrayID = strID;
    }
    public Tray(String strName,String strID,String strParent)
    {
        this(strName,strID);
        this.strParentID = strParent;
    }
    public void setTrayName(String strName)
    {
        this.strTrayName = strName;
    }
    public String getTrayName()
    {
        return this.strTrayName;
    }
    public void setTrayID(String strID)
    {
        this.strTrayID = strID;
    }
    public String getTrayID()
    {
        return this.strTrayID;
    }
    public void setParentID(String strID)
    {
        this.strParentID = strID;
    }
    public String getParentID()
    {
        return this.strParentID;
    }
    public void setColsRows(String strrow,String strcol,String strrowtype,String strcoltype)
    {
        this.strRows = strrow;
        this.strCols = strcol;
        this.strRowType = strrowtype;
        this.strColsType = strcoltype;
    }
    public String getRows()
    {
        return this.strRows;
    }
    public String getCols()
    {
        return this.strCols;
    }
    public String getRowType()
    {
        return this.strRowType;
    }
    public String getColType()
    {
        return this.strColsType;
    }
}
