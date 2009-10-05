/*
 * SmartformManagerXML.java
 *
 * Created on August 25, 2005, 10:19 PM
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
import java.util.Vector;
public class SmartformManagerXML {
    
    /** Creates a new instance of SmartformManagerXML */
    public SmartformManagerXML() {
    }
    public String getSmartformListXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
        return strbfrXML.toString();
    }
    
    public String getSmartformXML(Vector vtSmartformE)
    {
        StringBuffer strbfrXML = new StringBuffer();
        strbfrXML.append("<body_smartforms>");
        for(java.util.Enumeration eSE = vtSmartformE.elements();eSE.hasMoreElements();)
        {
            ISmartformEntity se = (ISmartformEntity)eSE.nextElement();
            strbfrXML.append(se.toXML());
        }
        strbfrXML.append("</body_smartforms>");
        return strbfrXML.toString();
    }
    
}
