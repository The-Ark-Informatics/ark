/*
 * SQLServerUtilities.java
 *
 * Created on 11 November 2004, 10:44
 */



/**
 *
 * @author  dmurley
 */

package neuragenix.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SQLServerUtilities {
    
    /** Creates a new instance of SQLServerUtilities */
    public SQLServerUtilities() {
    
    }
    
    public static String convertDateForDisplay(String strADateToConvert)
    {
        // hh: check the validation of the string
        if (strADateToConvert == null || strADateToConvert.length() < 10)
            return "";

        // Reverse the date and replace - for / as seperators
        // String returnValue = strADateToConvert.substring(8) + "/" + strADateToConvert.substring(5,7) + "/" + strADateToConvert.substring(0,4);
        
        //String returnValue = strADateToConvert.substring(8) + "/" + strADateToConvert.substring(5,7) + "/" + strADateToConvert.substring(0,4);
        //String returnValue = strADateToConvert.substring(0, 4) + "/" + strADateToConvert.substring(5,7) + "/" + strADateToConvert.substring(8,10);
        String returnValue = strADateToConvert.substring(8,10) + "/" + strADateToConvert.substring(5,7) + "/" + strADateToConvert.substring(0,4);

        return returnValue;

       

    }

    public static String convertTimeForDisplay (String strATimeToConvert)
    {
               
        SimpleDateFormat  sdFormatter;
        System.out.println("[SQLServerUtil ] " + strATimeToConvert);
        sdFormatter = new SimpleDateFormat ( "hh:mm a", new Locale ( "en", "US" ) );

        java.util.Date myDate = convertStringToDate(strATimeToConvert, "HH:mm:ss");
        
        // TODO: this is a temp fix
        /*
        if (myDate == null ){
           return "";
        }
        */
        
        return sdFormatter.format ( myDate );
    }
    
    public static java.util.Date convertStringToDate(String strAStringDate, String strFormat)

    {      
        // Bug for SQL server
        // TODO: confirm which date format returned by SQLServer
        if (strAStringDate.length() >= 10 ){
            // if format is MM:DD:YY hh:mm:ss
            return (new java.text.SimpleDateFormat(strFormat)).parse(strAStringDate, new java.text.ParsePosition(10));
        }
        else
            return (new java.text.SimpleDateFormat(strFormat)).parse(strAStringDate, new java.text.ParsePosition(1));

    }
    
}
