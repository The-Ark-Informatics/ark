/*

 * SmartformBatchGenerationInfo.java

 * Descriptiom: Template for all info to be supplied
 * when batch generating smarform results.

 * Created on August 22, 2005, 14:07
 
 * Author: Seena Parappat

 */



package neuragenix.genix.smartform;



// sun packages
import java.util.Vector;

import java.util.Hashtable;

import java.util.Enumeration;
 
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

public class SmartformBatchGenerationInfo 
{   

//Seena    public SmartformDAO smartformDAO;
//Seena    public SmartformBatchGenerator smartformBatchGenerator;
    public static final int GEN_MODE_SINGLEPARTICIPANT = 1;
    public static final int GEN_MODE_MULTIPARTICIPANT = 2;
    private int intGenerationMode;
    private int intSmartformKey;
    private Vector vtParticipants = new Vector();
    private int intNumCloneSFs = 1;
    private Vector vtCloneFields = new Vector();
    private Vector vtStandaloneFields = new Vector();
    private Vector vtSFEntities = new Vector();
    private ISmartformEntity sfEtySFFieldData;
    private String strSFDomain = "";
    private Hashtable hashCloneFieldData = new Hashtable ();
    private Vector vtStandaloneFieldData = new Vector ();
    private AuthToken authToken = null;   
    
    public SmartformBatchGenerationInfo() 
    {
        // constructor
    } 
    
    // Set the Auth Token
    public void setAuthToken (AuthToken authTok) 
    { 
        authToken = authTok;
    }

    // Get the Auth Token
    public AuthToken getAuthToken () 
    { 
        return authToken;
    }

    
    // Set the number of Smartforms to generate
    // Used in Many SF one Particpant generation
    public void setNumbertoClone (int intNumClones) 
    { 
        intNumCloneSFs = intNumClones;
    }

    // Set the number of Smartforms to generate
    // Used in Many SF one Particpant generation
    public int getNumbertoClone () 
    { 
        return intNumCloneSFs;
    }
    
    // Add to the list of fields to be clone
    // in Many SF one Particpant generation
    public void addCloneField(String strDEID) 
    { 
        vtCloneFields.add(strDEID);
    }
    
    // Returns the fields selected to be cloned
    public Vector getCloneFields () 
    { 
        return vtCloneFields;
    }  
    
    // clears the vector containing the fields to be cloned
    public void clearCloneFields () 
    { 
        vtCloneFields.clear();
    }     
    
    public void setCloneFieldData (Hashtable hashCloneData) 
    { 
        this.hashCloneFieldData = (Hashtable) hashCloneData.clone();
    }    
    
    public Hashtable getCloneFieldData () 
    { 
        return hashCloneFieldData;
    }
    

    public void setStandaloneFieldData (Vector vtStandaloneData) 
    { 
        vtStandaloneFieldData = (Vector) vtStandaloneData.clone();
    }    
    
    public Vector getStandaloneFieldData () 
    { 
        return vtStandaloneFieldData;
    }    
    

    // Add to the list of fields that will be unique for each generated smartform
    // Used in Many SF one Particpant generation
    public void addStandaloneField (String strDEID) 
    { 
        vtStandaloneFields.add(strDEID);
    }     
    
    // Returns the fields selected as stand alone fields
    public Vector getStandaloneFields () 
    { 
        return vtStandaloneFields;
    }     
    
    
    // clears the vector containing the standalone fields 
    public void clearStandaloneFields () 
    { 
        vtStandaloneFields.clear();
    }     
    
    /**
     * <p>Does ...</p>
     * 
     * 
     * @param generationMode 
     */
    public void setGenerationMode(int generationMode) 
    {  
        intGenerationMode = generationMode;
    }

    
    /**
     * <p>Does ...</p>
     * 
     * 
     * @param generationMode 
     */
    public int getGenerationMode() 
    {  
        // your code here
        return intGenerationMode;
    }
    
    public void setDomain(String strDomain) 
    {  
        strSFDomain = strDomain;
    }

    
    public String getDomain() 
    {  
        // your code here
        return strSFDomain;
    }

    // Add a participant to the list of participants to generate the smartform for
    public Vector getParticipants () 
    {
        return vtParticipants;        
    } 


    /**
     * <p>Add a participant to the list of participants for which to generate smartform results</p>
     * 
     * 
     * @param String strParticipantKey - partipant key for the new participant 
     */    
    public void addParticipant (String strParticipantKey) 
    {
        vtParticipants.add(strParticipantKey);        
    } 
    
    /**
     * <p>Add a participant to the list of participants for which to generate smartform results</p>
     * 
     * 
     * @param String strParticipantKey - partipant key for the new participant 
     */    
    public void removeParticipant (String strParticipantKey) 
    {
        vtParticipants.remove(strParticipantKey);        
    }    
    
    /**
     * <p>Appends the passed in participant list to that already in the object</p>
     * 
     * 
     * @param participants 
     */
    public void addParticipants(java.util.List participants) 
    {
        // your code here
    } 

    /**
     * <p>Does ...</p>
     * 
     * 
     * @param participantID 
     */
    public void removeParticipant(int participantID) 
    {
        // your code here
    } 

    /**
     * <p>Overwrites the current list of participants with the new one passed in</p>
     * 
     * 
     * @param participants 
     */
    public void setParticipants(java.util.List participants) 
    {
        // your code here
    }

    /**
     * <p>Sets the smartform key for the smartform to be cloned</p>
     * 
     * 
     * @param smartformID 
     */
    public void setSmartformTemplate(int smartformID) 
    { 
        intSmartformKey = smartformID;
    } 


    /**
     * <p>Returns the smartform key selected for batch results generation<./p>
     * 
     * 
     * 
     */
    public int getSmartformTemplate() 
    { 
        return intSmartformKey;
    } 
    
    /**
     * <p>Returns the smartform key selected for batch results generation<./p>
     * 
     * 
     * 
     */
    public void generateSmartformEntities() 
    { 
        
        for (int i=0; i<intNumCloneSFs; i++)
        {
            ISmartformManager sfMng = SmartformFactory.getSmartformManagerInstance();
            // Get a smartform entity object based on the smartform key
            ISmartformEntity SFE = sfMng.getSmartformEntity(intSmartformKey);
            vtSFEntities.add (SFE);
        }    
        
    }     
    
   /**
     * <p>Returns the vector of smartform entities
     * 
     * 
     * 
     */
    public Vector getSmartformEntities () 
    {         
        return vtSFEntities;
    }      
    
    
    /**
     * <p>Set the SF data to be cloned for the one SF to many participants generation.</p>
     * 
     * 
     * @param ISmartformEntity sfEtySmartformDetails - Object containing details to be cloned
     */    
    public void setSmartformFieldData (ISmartformEntity sfEtySmartformDetails)
    {
        this.sfEtySFFieldData  = sfEtySmartformDetails;
    }

    public ISmartformEntity getSmartformFieldData ()
    {
        return sfEtySFFieldData;
    }
}
