/*
 * ISmartformEntity.java
 *
 * Created on August 24, 2005, 12:18 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.smartform;

import java.util.List;
/**
 *
 * @author renny
 */
public interface ISmartformEntity 
{
    public String getSmartformParticipantKey();
    public void setSmartformParticipantKey(String strSFPKey);
    public String getSmartformKey();
    public void setSmartformKey(String strSK);
    public String getDomain();
    public void setDomain(String strdomain);
    public String getParticipantKey();
    public void setParticipantKey(String strPK);
    public void clearSFParticipantKey ();    
    public List getDE();
    public void setDE(List lstDE);
    /** get a Data element by name, null if not found*/
    public ISmartformDE getDE(String name);
    public void replaceDE(List lstDE);
    public String getSmartformName();
    public void setSmartformName(String strSmartformName);
    public void addDE(ISmartformDE DEObj);
    public String toXML();
}