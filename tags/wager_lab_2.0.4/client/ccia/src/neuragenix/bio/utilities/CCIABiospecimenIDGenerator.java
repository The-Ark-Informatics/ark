/*
 * CCIABiospecimenIDGenerator.java
 *
 * Created on 20 April 2005, 13:25
 */

package neuragenix.bio.utilities;

import java.util.Vector;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Enumeration;
import java.sql.ResultSet;
import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;
/**
 *
 * @author  sparappat
 */
public class CCIABiospecimenIDGenerator implements IBiospecimenIDGenerator 
{
    
    private static Hashtable hashUsedBioIDs = new Hashtable ();
    private static Vector vtUsedBioIDs = new Vector ();
    public static final int MAX_CACHE = 1000;
    public static final int FLUSH_SIZE = 500;
    
    /** Creates a new instance of CCIABiospecimenIDGenerator */
    public CCIABiospecimenIDGenerator() 
    {
        
    }
    
    public String getBiospecimenID(int intSequenceKey, DALSecurityQuery query, AuthToken authToken) 
    {
      if (query == null)
         query = new DALSecurityQuery();
        
        return getBiospecimenID(intSequenceKey, query.getQuery(), authToken);
    }
    
    public String getBiospecimenID(int intSequenceKey, DALQuery query, AuthToken authToken) 
    {
        if (query == null)
         query = new DALQuery();

        
        String strNewBiospecimenID = "";
        String strParentKey = getParentKey(intSequenceKey, query);
        

        
        if (!strParentKey.equals("-1"))
        {
            strNewBiospecimenID =  generateSubSpecimenID (intSequenceKey, strParentKey,  query);  
        }
        else
        {    
            strNewBiospecimenID =  getNewBiospecimenID (intSequenceKey);        
        }
            
        // once the vtUsedBioIDs contains more than 1000 IDs
        // flush out the first 500
        if (vtUsedBioIDs.size() > MAX_CACHE)
        {
            flushhashUsedBioIDs();            
        }     
            
        return strNewBiospecimenID; 
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
    
    private static synchronized String getNewBiospecimenID(int intBiospecimenKey)
    {
        
        try
        {
            DALQuery query = new DALQuery();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setMaxField ("BIOSPECIMEN_intBiospecimenID");
            query.setWhere (null, 0, "BIOSPECIMEN_strBiospecimenID", "", "IS NOT NULL", 0, DALQuery.WHERE_HAS_NULL_VALUE);
            query.setWhere ("AND", 0, "BIOSPECIMEN_intParentID", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
            //System.out.println ("QUERY" + query.convertSelectQueryToString());
            // Don't check for intDeleted
            ResultSet rs = query.executeSelect();

            if (rs.next())
            {
                String strMaxKey = rs.getString("MAX_BSPC_intBiospecID");
                //System.out.println("strMaxKey" + strMaxKey);                
                query.reset();
                query.clearDomains();
                query.clearFields();
                query.clearWhere();
                query.setDomain("BIOSPECIMEN", null, null, null);
                query.setField ("BIOSPECIMEN_strBiospecimenID", null);
                query.setWhere (null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strMaxKey, 0, DALQuery.WHERE_HAS_VALUE);
                rs = query.executeSelect();

                if (rs.next())
                {
                    //System.out.println("BIOSPECIMEN_strBiospecimenID" + rs.getString("BIOSPECIMEN_strBiospecimenID")); 
                    String strLatestBiospecimenId = rs.getString("BIOSPECIMEN_strBiospecimenID");
                    int intLatestBiospecimenId = Integer.parseInt(strLatestBiospecimenId);
                    intLatestBiospecimenId++;
                    // Make sure the ID has not been used before
                    while (hashUsedBioIDs.containsKey(Integer.toString(intLatestBiospecimenId)))
                    {
                        intLatestBiospecimenId++;
                    }

                    // close the resultset
                    rs.close();

                    //System.out.println ("intLatestBiospecimenId" + intLatestBiospecimenId);                    
                    // Put the newly selected ID into the hashtable
                    hashUsedBioIDs.put(Integer.toString(intLatestBiospecimenId), Integer.toString(intLatestBiospecimenId));
                    vtUsedBioIDs.add(Integer.toString(intLatestBiospecimenId));
                    return Integer.toString(intLatestBiospecimenId);       
                }    
            }  
        }
        catch (Exception e)
        {
            // If anything goes wrong the Biospecimen ID will be set equal to the newly 
            // inserted Biospecimen key
            LogService.instance().log(LogService.ERROR, "Error with generating the Biospecimen ID for Biospecimen Key: " + intBiospecimenKey + e.toString(), e);
            e.printStackTrace();
            // Put the newly selected ID into the hashtable
            hashUsedBioIDs.put(Integer.toString(intBiospecimenKey), Integer.toString(intBiospecimenKey));
            vtUsedBioIDs.add(Integer.toString(intBiospecimenKey));
            return intBiospecimenKey + "";
        }
        
        return intBiospecimenKey + "";
        
    }
    
    
    // Checks if we are generating an ID for a parent or sub bio-specimen    
    private String getParentKey (int intBiospecimenKey, DALQuery query)    
    {
        String strParentKey = "-1";
        
        try
        {
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_intParentID", null);
            query.setWhere (null, 0, "BIOSPECIMEN_intBiospecimenID", "=", Integer.toString(intBiospecimenKey), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();
            
            if (rs.first())
            {
                
                //System.out.println ("BIOSPECIMEN_intParentID" + rs.getString("BIOSPECIMEN_intParentID"));
                if (rs.getString("BIOSPECIMEN_intParentID") != null)
                {    
                    strParentKey = rs.getString("BIOSPECIMEN_intParentID");
                }
                // reset the query
                query.reset();

            }
            
            return strParentKey;
        
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Error with generating the Biospecimen ID for Biospecimen Key: " + intBiospecimenKey + e.toString(), e);
            e.printStackTrace();
            return "-1";
        }
        
    }
    
    // clear the oldest 500 entries from hashUsedBioIDs
    private synchronized void flushhashUsedBioIDs ()
    {

        if (vtUsedBioIDs.size() > MAX_CACHE)
        {
            // remove the oldest 500 IDs from the hashUsedBioIDs hashtable
            for (int i = 0; i<FLUSH_SIZE; i++)
            {
                String strIDToRemove = vtUsedBioIDs.get(i).toString();
                //System.out.println ("strIDToRemove" + strIDToRemove);                
                hashUsedBioIDs.remove(strIDToRemove);
            }    

            // remove the oldest 500 IDs from the Vector
            for (int i = 0; i<FLUSH_SIZE; i++)
            {
                //System.out.println ("going to remove " + vtUsedBioIDs.get(0).toString());                
                vtUsedBioIDs.remove(0);
            }            
        }    
    
    }
    
    // generates the sub-specimen ID
    private static synchronized String generateSubSpecimenID (int intBiospecimenKey, String strParentKey, DALQuery query)
    {    
        try
        {
            String strParentID = "";
            int intNumSubBiospecimens = 0;
            // The prefix of the generated sub-specimen
            String strSubPrefix = "";
            
            // get the ID corresponding to the parent key
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField ("BIOSPECIMEN_strBiospecimenID", null);
            query.setWhere (null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strParentKey, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();
            
            if (rs.next())
            {
                if (rs.getString("BIOSPECIMEN_strBiospecimenID") != null)
                {    
                    strParentID = rs.getString("BIOSPECIMEN_strBiospecimenID");
                }
            }    
            
            
            strSubPrefix = strParentID + "-s-"; 
            
            // get all the sub-samples for this parent key
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField ("BIOSPECIMEN_strBiospecimenID", null);
            query.setField ("BIOSPECIMEN_strParentID", null);
            query.setWhere (null, 0, "BIOSPECIMEN_intParentID", "=", strParentKey, 0, DALQuery.WHERE_HAS_VALUE);
            // Don't check for intDeleted because the next number is generated
            // based on number of sub-specimens already existing fot the parent
            rs = query.executeSelect();

            while (rs.next())
            {

                //System.out.println ("strParentKey" + strParentKey);
                //System.out.println ("intBiospecimenKey" + intBiospecimenKey);
                //System.out.println ("BIOSPECIMEN_strBiospecimenID" + rs.getString("BIOSPECIMEN_strBiospecimenID"));

                // If we already have sub-specimens of the format of the strSubPrefix
                // these are manually added sub-specimens (those not generated by vial calculation)
                // manually added sub-specimens are of the format XXXX-s-X where X are numbers    
                // Increment the intNumSubBiospecimens
                if (rs.getString("BIOSPECIMEN_strBiospecimenID").indexOf(strSubPrefix) != -1)
                {
                    intNumSubBiospecimens++;
                }    
                
            }

            // reset the query
            query.reset();

            if (intNumSubBiospecimens > 0)
            {
                // return the next sub-specimen ID number
                String strNewBioID = strSubPrefix + Integer.toString(intNumSubBiospecimens + 1);

                // Make sure the ID has not been used before
                while (hashUsedBioIDs.containsKey(strNewBioID))
                {
                    intNumSubBiospecimens = intNumSubBiospecimens + 1;
                    strNewBioID = strSubPrefix + Integer.toString(intNumSubBiospecimens);
                }
                    
                // Put the newly selected ID into the hashtable                
                hashUsedBioIDs.put (strNewBioID, strNewBioID);
                vtUsedBioIDs.add(strNewBioID);
                return strNewBioID;

            }    
            else
            {
                int intSubSpecimen = 1;
                // return the next sub-specimen ID number
                String strNewBioID = strSubPrefix + intSubSpecimen;

                // Make sure the ID has not been used before
                while (hashUsedBioIDs.containsKey(strNewBioID))
                {
                    intSubSpecimen = intSubSpecimen + 1;
                    strNewBioID = strSubPrefix + Integer.toString(intSubSpecimen);
                }

                // Put the newly selected ID into the hashtable                
                hashUsedBioIDs.put (strNewBioID, strNewBioID);
                vtUsedBioIDs.add(strNewBioID);
                // generate the first sub-specimen ID number
                return strNewBioID;
            }    
        
        }
        catch (Exception e)
        {
            // If anything goes wrong the Biospecimen ID will be set equal to the newly 
            // inserted Biospecimen key
            LogService.instance().log(LogService.ERROR, "Error with generating the Biospecimen ID for Biospecimen Key: " + intBiospecimenKey + e.toString(), e);
            e.printStackTrace();
            // Put the newly selected ID into the hashtable                
            hashUsedBioIDs.put (Integer.toString(intBiospecimenKey), Integer.toString(intBiospecimenKey));
            vtUsedBioIDs.add(Integer.toString(intBiospecimenKey));
            
            return intBiospecimenKey + "";        
        
        }
    
    }
        
}
