/*
 * IDGenerationFactory.java
 *
 * Created on 20 April 2005, 11:46
 */

package neuragenix.bio.utilities;

/**
 *
 *  Class to get the appropriate ID Generation class
 *
 *
 * @author  dmurley
 */

import org.jasig.portal.PropertiesManager;
import java.lang.reflect.*;


public class IDGenerationFactory {
    
    private static String strDefaultBiospecimenIDGenerator = null;
    private static String strDefaultPatientIDGenerator = null;
    private static String strDefaultAdmissionIDGenerator = null;
    private static String strDefaultPlateIDGenerator = null;
    /** Creates a new instance of BiospecimenIDGenerationManager */
    public IDGenerationFactory() 
    {
    }
    
    static
    {
        try
        {
            strDefaultBiospecimenIDGenerator = PropertiesManager.getProperty("neuragenix.bio.Biospecimen.IDGenerationImplementation");
            strDefaultPatientIDGenerator = PropertiesManager.getProperty("neuragenix.bio.patient.IDGenerationImplementation");
        }
        catch (Exception e)
        {
            System.err.println ("[ID Generation Factory - Unable to read property : neuragenix.bio.Biospecimen.IDGenerationImplementation");
            System.err.println ("[ID Generation Factory - Unable to read property : neuragenix.bio.patient.IDGenerationImplementation");
            strDefaultBiospecimenIDGenerator = null;
            strDefaultPatientIDGenerator = null;
            strDefaultAdmissionIDGenerator = null;
        }
    }
    
    public static IBiospecimenIDGenerator getBiospecimenIDGenerationInstance()
    {
        IBiospecimenIDGenerator defaultGenerator = new DefaultBiospecimenIDGenerator();
        
        if (strDefaultBiospecimenIDGenerator == null)
        {
            return defaultGenerator;
        }
        else
        {
            try
            {
                Class c = Class.forName(strDefaultBiospecimenIDGenerator);
                IBiospecimenIDGenerator genDynamicGenerator = (IBiospecimenIDGenerator) c.newInstance();
                return genDynamicGenerator;
            }
            catch (Exception e)
            {
                System.err.println ("[ID Generation Factory] Invalid Implementation specified in properties.  Using Default ");
                e.printStackTrace();
                return defaultGenerator;
            }
            
        }
      
    }
    
    public static IPatientIDGenerator getPatientIDGenerationInstance()
    {
        IPatientIDGenerator defaultGenerator = new DefaultPatientIDGenerator();
        
        if (strDefaultPatientIDGenerator == null)
        {
            return defaultGenerator;
        }
        else
        {
            try
            {
                Class c = Class.forName(strDefaultPatientIDGenerator);
                IPatientIDGenerator genDynamicGenerator = (IPatientIDGenerator) c.newInstance();
                return genDynamicGenerator;
            }
            catch (Exception e)
            {
                System.err.println ("[ID Generation Factory] Invalid Implementation specified in properties.  Using Default ");
                e.printStackTrace();
                return defaultGenerator;
            }
            
        }
      
    }
    public static IAdmissionIDGenerator getAdmissionIDGenerationInstance()
    {
        IAdmissionIDGenerator defaultGenerator = new DefaultAdmissionIDGenerator();
        
        if (strDefaultAdmissionIDGenerator == null)
        {
            return defaultGenerator;
        }
        else
        {
            try
            {
                Class c = Class.forName(strDefaultAdmissionIDGenerator);
                IAdmissionIDGenerator genDynamicGenerator = (IAdmissionIDGenerator) c.newInstance();
                return genDynamicGenerator;
            }
            catch (Exception e)
            {
                System.err.println ("[ID Generation Factory] Invalid Implementation specified in properties.  Using Default ");
                e.printStackTrace();
                return defaultGenerator;
            }
            
        }
      
    }
    public static IInventoryIDGenerator getPlateIDGenerationInstance()
    {
        IInventoryIDGenerator defaultGenerator = new DefaultPlateIDGenerator();
      
            return defaultGenerator;
     
      
      
    }
    
}
