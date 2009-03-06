/*

 * SmartformBatchGenerator.java

 * Description: Perform batch generation of smartform results

 * Created on August 22, 2005, 15:10
 
 * Author: Seena Parappat

 */



package neuragenix.genix.smartform;



// sun packages
import java.util.Vector;

import java.util.Enumeration;
 
import java.util.Hashtable;

import java.sql.*;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;


// neuragenix packages
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.security.AuthToken;


/**

 *

 * @author  Seena Parappat

 */

public class SmartformBatchGenerator 
{   

    public SmartformBatchGenerator () 
    {
        // constructor
    } 

    /**
     * <p>Does ...</p>
     * 
     * 
     * @param info 
     */
    public static boolean doGeneration(SmartformBatchGenerationInfo info) 
    {
        if (info.getGenerationMode() == SmartformBatchGenerationInfo.GEN_MODE_SINGLEPARTICIPANT)
        {
            Vector vtStandaloneData = info.getStandaloneFieldData();
            Vector  vtParticipants = info.getParticipants();
            Vector  vtSFEntities = new Vector ();
            ISmartformManager sfMng = SmartformFactory.getSmartformManagerInstance();
            
            // For each smartform to generate, create a smartform entity and
            // insert the cloneable and standalone field data
            for (int i=0; i<info.getNumbertoClone(); i++)
            {    
                Hashtable hashCloneData = info.getCloneFieldData();
                Hashtable hashStandaloneData = (Hashtable) vtStandaloneData.get(i); 
                
                // Get a smartform entity object based on the smartform key
                ISmartformEntity SFData = sfMng.getSmartformEntity(info.getSmartformTemplate());
                SFData.setParticipantKey(vtParticipants.get(0).toString());
                SFData.setSmartformKey(Integer.toString(info.getSmartformTemplate()));
                SFData.setDomain(info.getDomain());

                Vector vtDE = (Vector) SFData.getDE();
                
                // For each dataelement, update the cloneable and standalone data
                for (int j=0; j<vtDE.size(); j++)
                {
                    ISmartformDE DE = (ISmartformDE) vtDE.get(j);
                    String strDEID = DE.getID();
                    if (hashCloneData.containsKey(strDEID))
                    {
                        if (hashCloneData.get(strDEID) != null && !hashCloneData.get(strDEID).equals("") )
                        {    
                            DE.setResult(hashCloneData.get(strDEID).toString());
                        }    
                    }
                    else if (hashStandaloneData.containsKey(strDEID))
                    {
                        if (hashStandaloneData.get(strDEID) != null && !hashStandaloneData.get(strDEID).equals("") )
                        {    
                            DE.setResult(hashStandaloneData.get(strDEID).toString());
                        }    
                    }                        
                }    
                vtSFEntities.add(SFData);                
            }
            // save the smartform results to the database
            sfMng.saveSmartformResults(vtSFEntities, info.getAuthToken());
            
            return true;
        }    
        else if (info.getGenerationMode() == SmartformBatchGenerationInfo.GEN_MODE_MULTIPARTICIPANT)
        {
            Vector  vtParticipants = info.getParticipants();
            Vector  vtSFEntities = new Vector ();
            ISmartformEntity SFData = (ISmartformEntity) info.getSmartformFieldData();
            ISmartformManager sfMng = SmartformFactory.getSmartformManagerInstance ();                
            
            if (SFData == null)
            {
                SFData = SmartformFactory.getSmartformEntityInstance();
            }    
            
            for (int i=0; i<vtParticipants.size(); i++)
            {                
                SFData.clearSFParticipantKey();
                SFData.setParticipantKey(vtParticipants.get(i).toString());
                SFData.setSmartformKey(Integer.toString(info.getSmartformTemplate()));
                SFData.setDomain(info.getDomain());
                vtSFEntities.clear();
                vtSFEntities.add(SFData);
                // Save each result
                sfMng.saveSmartformResults(vtSFEntities, info.getAuthToken());
            }
            
            return true;            
        }    
        
        return false;

    } 


}
