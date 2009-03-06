/*
 * DefaultIDGenerator.java
 *
 * Created on 20 April 2005, 13:25
 */

package neuragenix.bio.biospecimen.BiospecimenIDGeneration;

import java.util.Vector;
import neuragenix.security.AuthToken;
import org.jasig.portal.PropertiesManager;
/**
 *
 * @author  dmurley
 */
public class DefaultIDGenerator implements IBiospecimenIDGenerator 
{
    
    
    
    /** Creates a new instance of DefaultIDGenerator */
    public DefaultIDGenerator() 
    {
        
    }
    
    public String getBiospecimenID(int intSequenceKey, AuthToken authToken) 
    {
        return intSequenceKey + "";
    }
    
    public String getBiospecimenIDPrefix() 
    {
        String strHospitalSite = "";
        try
        {
            strHospitalSite = PropertiesManager.getProperty("neuragenix.bio.HospitalSite");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return strHospitalSite;
    }
    
    
}
