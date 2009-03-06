/*
 * ISmartformDE.java
 *
 * Created on August 24, 2005, 12:19 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.smartform;

import org.jasig.portal.ChannelRuntimeData;
/**
 *
 * @author renny
 */
public interface ISmartformDE 
{

    public String getName();
    public void setName(String strName);
    public String getResult();
    public void setResult(String strResult);
    public String getType();
    public void setType(String strtype);
    public String getID();
    public void setID(String strDEID);
    public String getOrder();
    public void setOrder(String strOrder);
    public String getDENo();
    public void setDENo(String strOrder);
    
    /**
     *
     * @deprecated replaced with getAttribute() and getParameter()
     */    
    public String getOther();
    /**
     *
     * @deprecated replaced with setAttribute()
     */    
    public void setOther(String strOrder);
    
    /**getAttribute,setAttribute, getParameter use the same hashtable but the last method cast the key and values to Strings */
    public Object getAttribute(Object key);
    /**getAttribute,setAttribute, getParameter use the same hashtable but the last method cast the key and values to Strings */
    public void setAttribute(String key, Object value);
    /**
     * getAttribute,setAttribute, getParameter use the same hashtable but the last method cast the key and values to Strings
     * @return the attribue casted to a String
     * @param name the same as the attribute key
     */
    public String getParameter(String name);
       
    public boolean getManadatory();
    public void setMandatory(boolean vlmandatory);
    public String toXML();
    public ChannelRuntimeData toRuntimeData(ChannelRuntimeData rd);
}