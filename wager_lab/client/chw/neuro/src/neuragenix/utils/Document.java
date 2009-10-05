/*
 * Document.java
 *
 * Created on August 29, 2005, 3:20 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.utils;

/**
 *
 * @author renny
 */

public class Document 
{
    String strExternalName = null;
    String strFileName = null;
    String strID = null;
    /** Creates a new instance of Document */
    public Document() 
    {
    }
    public Document(String str_ID)
    {
        this();
        this.strID = str_ID;
    }
    public String getExternalName()
    {
        return this.strExternalName;
    }
    public void setExternalName(String str_ExternalName)
    {
        this.strExternalName = str_ExternalName;
    }
    public String getFileName()
    {
        return this.strFileName;
    }
    public void setFileName(String str_FileName)
    {
        this.strFileName = str_FileName;
    }
    public String getID()
    {
        return this.strID;
    }
    public void setID(String str_ID)
    {
        this.strID = str_ID;
    }
    public String toXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
        if(this.strID != null && this.strID.length() > 0)
        {
            strbfrXML.append("<Document>");
            strbfrXML.append("<ID>" + this.strID + "</ID>");
            if(this.strExternalName != null && this.strExternalName.length() > 0)
            {
                strbfrXML.append("<ExternalName>" + this.strExternalName + "</ExternalName>");
            }
            if(this.strFileName != null && this.strFileName.length() > 0)
            {
                strbfrXML.append("<FileName>" + this.strFileName+ "</FileName>");
            }
            strbfrXML.append("</Document>");
        }
        return strbfrXML.toString();
    }
}
