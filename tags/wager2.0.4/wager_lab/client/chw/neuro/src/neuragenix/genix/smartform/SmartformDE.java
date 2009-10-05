/*
 * SmartformDE.java
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
import org.jasig.portal.ChannelRuntimeData;
import java.util.*;

public class SmartformDE implements ISmartformDE
{
    protected String strName = null;
    protected String strResult = null;
    protected String strType = null;
    protected String strID = null;
    protected String strOrder = null;
    protected String strDENo = null;
    protected boolean blMandatory = false;
    protected Hashtable attributes = new Hashtable();
    
    /** Creates a new instance of SmartformDE */
    public SmartformDE() 
    {
        
    }
    public String getName()
    {
        return this.strName;
    }
    public void setName(String str_Name)
    {
        this.strName = str_Name;
    }
    public String getResult()
    {
        return this.strResult;
    }
    public void setResult(String str_Result)
    {
        this.strResult = str_Result;
    }
    public String getType()
    {
        return this.strType;
    }
    public void setType(String str_type)
    {
        this.strType = str_type;
    }
    public String getID()
    {
        return this.strID;
    }
    public void setID(String str_ID)
    {
        this.strID = str_ID;
    }
    public String getOrder()
    {
        return this.strOrder;
    }
    public void setOrder(String str_Order)
    {
        this.strOrder = str_Order;
    }
    public String getDENo()
    {
        return this.strDENo;
    }
    public void setDENo(String str_DENo)
    {
        this.strDENo = str_DENo;
    }
    public boolean getManadatory()
    {
        return this.blMandatory;
    }
    public void setMandatory(boolean bl_Mandatory)
    {
        this.blMandatory = bl_Mandatory;
    }
    public String getOther()
    {
    	return "";
    }
    public void setOther(String strOrder)
    {
    
    }
    /**getAttribute,setAttribute, getParameter use the same hashtable but the last method cast the key and values to Strings */
    public Object getAttribute(Object key){
        return this.attributes.get(key);
    }
    /**getAttribute,setAttribute, getParameter use the same hashtable but the last method cast the key and values to Strings */
    public void setAttribute(String key, Object value){
        this.attributes.put(key, value);
    }
    /**
     * getAttribute,setAttribute, getParameter use the same hashtable but the last method cast the key and values to Strings
     * @return the attribue casted to a String
     * @param name the same as the attribute key
     */
    public String getParameter(String name){
        if (this.attributes.get(name) != null)
            return (String)this.attributes.get(name);
        else
            return null;                
    }
    
    public String toXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
            if(this.strID != null && this.strID.length() > 0)
            {
                strbfrXML.append("<SmartformDE>");
                strbfrXML.append("<strID>" + this.strName + "</strID>");
                if(this.strName != null && this.strName.length() > 0)
                {
                    strbfrXML.append("<strName>" + this.strName + "</strName>");
                }
                if(this.strResult != null && this.strResult.length() > 0)
                {
                    strbfrXML.append("<strResult>" + this.strResult + "</strResult>");
                }
                if(this.strType != null && this.strType.length() > 0)
                {
                    strbfrXML.append("<strType>" + this.strType + "</strType>");
                }
                if(this.strOrder != null && this.strOrder.length() > 0)
                {
                    strbfrXML.append("<strOrder>" + this.strOrder + "</strOrder>");
                }
                if(this.strDENo != null && this.strDENo.length() > 0)
                {
                    strbfrXML.append("<strDENo>" + this.strDENo + "</strDENo>");
                }
                if(this.blMandatory)
                {
                    strbfrXML.append("<blMandatory>" + this.blMandatory + "</blMandatory>");
                }
                strbfrXML.append("</SmartformDE>");
            }
        return strbfrXML.toString();
    }
    
    public ChannelRuntimeData toRuntimeData(ChannelRuntimeData rd)
    {
        return rd;
    }
    /** Checks if 2 object are equal. If the object passed is a String then it compare it to the DE name.
    * If the object passed is a a SmartformDE then it compares again only 2 fields the name of the DE and ther result value*/
    public boolean equals(Object obj) {
        if (obj instanceof String)
            return ((String)obj).equals(strName);
        else if (obj instanceof ISmartformDE){
            ISmartformDE de = (ISmartformDE)obj;            
            return  de.getName().equals(this.strName) && de.getResult().equals(this.strResult);
        }
        else return false;
    }
    
}
