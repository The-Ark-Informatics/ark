/*
 * DefaultBiospecimenIDGenerator.java
 *
 * Created on 20 April 2005, 13:25
 */

package neuragenix.bio.utilities;

import java.util.Vector;
import neuragenix.security.AuthToken;
import org.jasig.portal.PropertiesManager;
import neuragenix.dao.*;
/**
 *
 * @author  dmurley
 */
public class DefaultBiospecimenIDGenerator implements IBiospecimenIDGenerator 
{
    public static final String SYSTEM_TIMEZONE = PropertiesManager.getProperty("neuragenix.genix.timezone");
    public static final String SYSTEM_LANGUAGE = PropertiesManager.getProperty("neuragenix.genix.languageCode");
    public static final String SYSTEM_COUNTRY = PropertiesManager.getProperty("neuragenix.genix.countryCode");
    
    
    /** Creates a new instance of DefaultBiospecimenIDGenerator */
    public DefaultBiospecimenIDGenerator() 
    {
        
    }
    
    public String getBiospecimenID(int intSequenceKey, DALSecurityQuery query, AuthToken authToken) 
    {
        return intSequenceKey + "";
    }

    public String getBiospecimenID(int intSequenceKey, DALQuery query, AuthToken authToken) 
    {
        return intSequenceKey + "";
    }
    
    
    public String getBiospecimenIDPrefix() 
    {
        String strHospitalSite = "";
        String Year = "";
        
        try
        {
            // Define the Timezone
            java.util.TimeZone tz = java.util.TimeZone.getTimeZone(SYSTEM_TIMEZONE);
            // Define the locality information
            java.util.Locale loc = new java.util.Locale(SYSTEM_LANGUAGE, SYSTEM_COUNTRY);
            // Setup the calender
            java.util.Calendar theCalendar = java.util.Calendar.getInstance(tz,loc);
            // set the year
            Year = new String(Integer.toString(theCalendar.get(java.util.Calendar.YEAR)).substring(2));

            strHospitalSite = PropertiesManager.getProperty("neuragenix.bio.HospitalSite");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return Year + strHospitalSite;
    }
    
    
}
