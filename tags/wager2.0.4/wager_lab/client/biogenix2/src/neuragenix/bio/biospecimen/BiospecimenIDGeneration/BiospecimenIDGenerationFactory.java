/*
 * BiospecimenIDGenerationManager.java
 *
 * Created on 20 April 2005, 11:46
 */

package neuragenix.bio.biospecimen.BiospecimenIDGeneration;

/**
 *
 *  Class to get the appropriate ID Generation class
 *
 *
 * @author  dmurley
 */

import org.jasig.portal.PropertiesManager;
import java.lang.reflect.*;


public class BiospecimenIDGenerationFactory {
    
    private static String strDefaultIDGenerator = null;
    
    
    /** Creates a new instance of BiospecimenIDGenerationManager */
    public BiospecimenIDGenerationFactory() 
    {
    }
    
    static
    {
        try
        {
            strDefaultIDGenerator = PropertiesManager.getProperty("neuragenix.bio.Biospecimen.IDGenerationImplementation");
        }
        catch (Exception e)
        {
            System.err.println ("[Biospecimen ID Generation Factory - Unable to read property : neuragenix.bio.Biospecimen.IDGenerationImplementation");
            strDefaultIDGenerator = null;
        }
    }
    
    public static IBiospecimenIDGenerator getIDGenerationInstance()
    {
        IBiospecimenIDGenerator defaultGenerator = new DefaultIDGenerator();
        
        if (strDefaultIDGenerator == null)
        {
            return defaultGenerator;
        }
        else
        {
            try
            {
                Class c = Class.forName(strDefaultIDGenerator);
                IBiospecimenIDGenerator genDynamicGenerator = (IBiospecimenIDGenerator) c.newInstance();
                return genDynamicGenerator;
            }
            catch (Exception e)
            {
                System.err.println ("[Biospecimen ID Generation Factory] Invalid Implementation specified in properties.  Using Default ");
                e.printStackTrace();
                return defaultGenerator;
            }
            
        }
      
    }
    
    
    
}
