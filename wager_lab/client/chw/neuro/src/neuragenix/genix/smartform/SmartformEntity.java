/*
 * SmartformEntity.java
 *
 * Created on August 24, 2005, 1:56 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.smartform;

/**
 *
 * @author renny
 */
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;

public class SmartformEntity implements ISmartformEntity
{
    String strSmartfrmName = null;
    String strSmartfrmPK = null;
    String strSmartfrmKey = null;
    String strDomain = null;
    String strParticipantKey = null;
    Vector vtDE = null;
    /** Creates a new instance of SmartformEntity */
    public SmartformEntity() 
    {
        vtDE = new Vector();
    }
    public String getSmartformName()
    {
        return this.strSmartfrmName;
    }
    public void setSmartformName(String str_Name)
    {
        this.strSmartfrmName = str_Name;
    }
    public String getSmartformParticipantKey()
    {
        return this.strSmartfrmPK;
    }
    public void setSmartformParticipantKey(String str_SFPKey)
    {
        this.strSmartfrmPK = str_SFPKey;
    }
    public void clearSFParticipantKey ()
    {
        this.strSmartfrmPK = null;
    }    
    public String getSmartformKey()
    {
        return this.strSmartfrmKey;
    }
    public void setSmartformKey(String str_SK)
    {
        this.strSmartfrmKey = str_SK;
    }
    public String getDomain()
    {
        return this.strDomain;
    }
    public void setDomain(String str_domain)
    {
        this.strDomain = str_domain;
    }
    public String getParticipantKey()
    {
        return this.strParticipantKey;
    }
    public void setParticipantKey(String str_PK)
    {
        this.strParticipantKey = str_PK;
    }
    public List getDE()
    {
        return this.vtDE;
    }
    public void setDE(List lstDE)
    {
        this.vtDE.addAll(lstDE);
    }
    /** get a Data element by name, null if not found*/
    public ISmartformDE getDE(String name){
        Enumeration elements = this.vtDE.elements();
        while (elements.hasMoreElements()){
            ISmartformDE de = (ISmartformDE)elements.nextElement();
            if (de.equals(name))
                return de;
        }
        return null;
    }
    public void replaceDE(List lstDE)
    {
    	this.vtDE = new Vector(lstDE);
    }
    public void addDE(ISmartformDE DEObj)
    {
        this.vtDE.add(DEObj);
    }
    public String toXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
        if(this.strSmartfrmPK != null && this.strSmartfrmPK.length() > 0)
        {
            strbfrXML.append("<Smartform>");
            strbfrXML.append("<strSmartfrmPK>" + this.strSmartfrmPK + "</strSmartfrmPK>");
            if(this.strParticipantKey != null && this.strParticipantKey.length() > 0)
            {
                strbfrXML.append("<strParticipantKey>"+this.strParticipantKey+"</strParticipantKey>");
            }
            if(this.strDomain != null && this.strDomain.length() > 0)
            {
                strbfrXML.append("<strDomain>"+this.strDomain+"</strDomain>");
            }
            if(this.strSmartfrmKey != null && this.strSmartfrmKey.length() > 0)
            {
                strbfrXML.append("<strSmartfrmKey>"+this.strSmartfrmKey+"</strSmartfrmKey>");
            }
            if(this.strSmartfrmName != null && this.strSmartfrmName.length() > 0)
            {
                strbfrXML.append("<strSmartfrmName>"+this.strSmartfrmName+"</strSmartfrmName>");
            }
            if(this.vtDE != null && this.vtDE.size() > 0)
            {
                strbfrXML.append("<DataElements>");
                for(java.util.Enumeration eDE = vtDE.elements();eDE.hasMoreElements();)
                {
                    ISmartformDE smrtfrmDE = (ISmartformDE)eDE.nextElement();
                    strbfrXML.append(smrtfrmDE.toXML());
                }
                strbfrXML.append("</DataElements>");
            }
            strbfrXML.append("</Smartform>");
        }
        return strbfrXML.toString();
    }
}
