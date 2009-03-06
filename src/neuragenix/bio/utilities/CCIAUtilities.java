/*
 * CCIAUtilities.java
 *
 * Created on 24 May 2005, 18:29
 */

package neuragenix.bio.utilities;


import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.utils.*;
import org.jasig.portal.ChannelRuntimeData;
import java.util.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import org.jasig.portal.services.LogService;

/**
 * @author  navin
 * Implementation of methods are defined in the /client-bluefire/ccia folder
 * Under neuragenix/bio/utilities/CCIAUtilities.java
 *
 */
public class CCIAUtilities
{
    
    /** Default Constructor CCIAUtilities  - does nothing */
    public CCIAUtilities()
    {}
    
    /** converts Dates in the hshtable to dd-mm-yyyy format and returns the hashtable **/
    public static Hashtable convertToAusDateFormat (Hashtable hshResults)
    {
        return hshResults;
    }
    
    /** updates the runtimeData for a new CCIA field ClinicalAge **/
    public static void calculateClinicalAge (Vector vtSaveBiospecimenFields, ChannelRuntimeData runtimeData)
    {}
}
