/*

 * Utilities.java

 *

 * Created on November 11, 2002, 5:31 PM

 */



package neuragenix.common;

import neuragenix.dao.DBSchema;

import neuragenix.dao.DBMSTypes;

import neuragenix.dao.DatabaseSchema;

import java.util.Hashtable;

import java.util.Enumeration;

import java.text.SimpleDateFormat;

import java.util.Locale;

import java.util.Date;

import java.text.NumberFormat;

import java.util.GregorianCalendar;

import java.sql.Timestamp;

import org.jasig.portal.services.LogService;

import org.jasig.portal.PropertiesManager;
import org.jasig.portal.ChannelRuntimeData;

import java.util.regex.*;
import java.util.StringTokenizer;

import bsh.*;

/** This is a common class to store utilities

 * based static methods

 * @author Hayden Molnar

 */

public class Utilities {

    /** Creates a new instance of utilFormValidate */

    public Utilities() 
    {
        
    }

    

    /** This method checks to see if the value passed to it is a

     * valid integer.

     * @return Returns true or false depending whether

     * the value passed to it was an integer

     * @param strValue Value to check

     */    

    public static boolean validateIntValue(String strValue)

    {

        try

        {

            Integer.parseInt(strValue.trim());

        }

        catch(NumberFormatException nfe)

        {

            return false;

        }

        catch(Exception e)

        {

            return false;

        }

        return true;

    }

    /** This method checks to see if the value passed to it is a

     * valid date.
     * 
     *  Modified by Phan Le so that it can accept variable length dates as long as they have separators ('-' or '/') within.
     *  Eg: 04-03-2002 and 04/03/02 also accepted. 

     * @return Returns true or false depending whether

     * the value passed to it was a date

     * @param strValue Value to check

     */    

      public static boolean validateDateValue(String strValue)

    {

        try

        {     
           
           //System.out.println("VALUE "+strValue);  
           // first  replace all - with / if possible so that we can use / separator
           strValue = strValue.replace('/','-');     
           //System.out.println("VALUE "+strValue); 
           
           //now tokenizer it using / as the separator 
           StringTokenizer tok = new StringTokenizer(strValue,"-");
           
           // if cant get date, month and year in 3 tokens then false
           if (tok.countTokens()!= 3) return false;
           
           int date = Integer.parseInt(tok.nextToken());
            // Get the month
            int month = Integer.parseInt(tok.nextToken());
            // Get the year
            int year = Integer.parseInt(tok.nextToken());

            
            //System.out.println(strValue);
            

            // normal checks for the date

            if (date > 31 || date < 1) return false;

            if (month > 12 || month < 1) return false;

            if ((month == 4 || month == 6 || month == 9 || month == 11) &&

                date > 30) return false;

            

            if (month == 2 && date > 29) return false;
            
            //convert the year
            if (year>=0 && year <=9) year+= 2000; // year from 2001 to 2009
            
            if (year>=11 && year <=99) year+= 1900; //year from 1910 to 1999
            
            // now year should be in 4 digits code otherwise reject it

            if (year < 1900 || year > 2075)        // due to MSSQL short date limitation (NP)
            {
                return false;
            }
            // checks leap year
            if (month == 2 && date > 28) {

                GregorianCalendar calendar = new GregorianCalendar();

                if (!calendar.isLeapYear(year))

                    return false;

            }
            
           

        }

        catch(NumberFormatException nfe)

        {

            return false;

        }

        catch(Exception e)

        {

            return false;

        }

        return true;

    }

    
    
    
/*   public static boolean validateDateValue(String strValue)

    {

        try

        {     

            if (strValue.length() != 10)

                return false;

            
             //get  the Day
            int date = Integer.parseInt(strValue.substring(0,2));
            // Get the month
            int month = Integer.parseInt(strValue.substring(3,5));
            // Get the year
            int year = Integer.parseInt(strValue.substring(6));

            

            // normal checks for the date

            if (date > 31 || date < 1) return false;

            if (month > 12 || month < 1) return false;

            if ((month == 4 || month == 6 || month == 9 || month == 11) &&

                date > 30) return false;

            

            if (month == 2 && date > 29) return false;

            

            // checks leap year

            if (month == 2 && date > 28) {

                GregorianCalendar calendar = new GregorianCalendar();

                if (!calendar.isLeapYear(year))

                    return false;

            }
            
            if (year < 1900 || year > 2075)        // due to MSSQL short date limitation (NP)
            {
                return false;
            }

        }

        catch(NumberFormatException nfe)

        {

            return false;

        }

        catch(Exception e)

        {

            return false;

        }

        return true;

    }

        
 */
    public boolean isNumeric(String s){

        String validChars = "0123456789";
        boolean isNumber = true;
        for (int i = 0; i < s.length() && isNumber; i++){
            char c = s.charAt(i);
            if (validChars.indexOf(c) == -1){
                return false;
            }
        }
        return true;
    }
    
    
    

    public static boolean validateDateValueForIntegration(String strValue)

    {

        try

        {     

            int index1 = strValue.indexOf("/");

            if (index1 < 0)

                return false;

            

            int index2 = strValue.indexOf("/", index1 + 1);

            if (index2 < 0)

                return false;

            

            int date = Integer.parseInt(strValue.substring(0,index1));

            int month = Integer.parseInt(strValue.substring(index1 + 1,index2));

            int year = Integer.parseInt(strValue.substring(index2 + 1));

            

            // normal checks for the date

            if (date > 31 || date < 1) return false;

            if (month > 12 || month < 1) return false;

            if ((month == 4 || month == 6 || month == 9 || month == 11) &&

                date > 30) return false;

            

            if (month == 2 && date > 29) return false;

            

            // checks leap year

            if (month == 2 && date > 28) {

                GregorianCalendar calendar = new GregorianCalendar();

                if (!calendar.isLeapYear(year))

                    return false;

            }

        }

        catch(NumberFormatException nfe)

        {

            return false;

        }

        catch(Exception e)

        {

            return false;

        }

        return true;

    }

    

    public static String formDateForDB(String strValue)

    {

        if (strValue.length() == 10)

            return strValue;

        

        int index1 = strValue.indexOf("/");

        int index2 = strValue.indexOf("/", index1 + 1);

        

        String day = strValue.substring(0, index1 + 1);;

        if (index1 < 2)

            day = "0" + day;

        

        String month = strValue.substring(index1 + 1, index2 + 1);;

        if (index2 - index1 < 3)

            month = "0" + month;

        

        String year = strValue.substring(index2 + 1);

        

        return day + month + year;

    }

    /** This method checks whether a the field

     * given is required according to the

     * schema

     * @return Returns true or false depending on whether

     * the field passed to it is required

     * @param strValue The field to be checked

     */    

    public static boolean requiredField(String strValue)

    {

        

        boolean blRequiredField = false;

        try

            {

                Hashtable hashDBRequiredFields = DBSchema.getDBRequiredFields();

                               

                if(hashDBRequiredFields.containsKey(strValue))

                {

                    blRequiredField = true;

                }

            }

            catch(Exception e)

            {

                LogService.instance().log(LogService.ERROR, "Unknown error while trying to validate whether a field is required - " + e.toString());

            }

            return blRequiredField;

    }

    

    public static String convertDateForDisplay(String strADateToConvert)

    {
        if (DatabaseSchema.getDBMSType() == DBMSTypes.SQLSERVER)
        {
            return SQLServerUtilities.convertDateForDisplay(strADateToConvert);
        }

        
        
        // hh: check the validation of the string
        if (strADateToConvert == null || strADateToConvert.length() < 10)
            return "";

        if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE){
           java.util.Date dateObj = convertStringToDate(strADateToConvert , "MM/dd/yyyy"); 
           SimpleDateFormat dfFormatter = new SimpleDateFormat("dd/MM/yyyy");
           
           if (dateObj != null) 
              return dfFormatter.format(dateObj);
        }
        // Reverse the date and replace - for / as seperators
        return strADateToConvert.substring(8) + "/" + strADateToConvert.substring(5,7) + "/" + strADateToConvert.substring(0,4);

    }

    public static String convertDate(Date dtDateObject, String format)
    {
       SimpleDateFormat dfFormatter = new SimpleDateFormat(format);
       return dfFormatter.format(dtDateObject);
    }
    
    
    public static String convertDateForDAL(Date dtDateObject)
    {
       if (dtDateObject == null)
          return null;
       
       return convertDate(dtDateObject, "dd/MM/yyyy");
    }
    
    public static String convertDateForDB(String strADateToConvert)

    {
        
        // hh: check the validation of the string
        if (strADateToConvert == null || strADateToConvert.length() < 10)
            return "";


     //   System.out.println(strADateToConvert.substring(6) + "-" + strADateToConvert.substring(3,5) + "-" + strADateToConvert.substring(0,2));

        // Reverse the date and replace - for / as seperators

        
        // check if this actually has a time value at the end, and if so, strip it.
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]", strADateToConvert))
        {
            return strADateToConvert.substring(0, 10);
        }
        
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", strADateToConvert))
        {
            return strADateToConvert;
        }
        


        return strADateToConvert.substring(6) + "-" + strADateToConvert.substring(3,5) + "-" + strADateToConvert.substring(0,2);

        

       

    }

    

    public static String convertTimeForDB(String strATimeToConvert)

    {
        

        // hh: check the validation of the string
        if (strATimeToConvert == null || strATimeToConvert.length() < 8)
            return "";
        
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]", strATimeToConvert))
        {
            strATimeToConvert = convertTimeForDisplay(strATimeToConvert);
        }
        
        
        String strHour = strATimeToConvert.substring(0, 2);
        String strMinute = strATimeToConvert.substring(3, 5);
        String strAMPM = strATimeToConvert.substring(6);
        
        try {
            int intHour = Integer.parseInt(strHour);
            int intMinute = Integer.parseInt(strMinute);
            
            
            if (strAMPM.equalsIgnoreCase("pm")) {
                strHour = "" + (intHour + 12);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        
        return strHour + ":" + strMinute + ":00";  

    }

    public static String convertDateForDisplay(java.sql.Date dtADate)
    {   
        return convertDateForDisplay(dtADate, "dd/MM/yyyy");
    }
     
      
    public static String convertDateForDisplay(java.sql.Date dtADate, String dateFormat)
    {           
        SimpleDateFormat  sdFormatter;
        sdFormatter = new SimpleDateFormat ( dateFormat );
        return (dtADate == null ? "" : sdFormatter.format ( dtADate ));
    }      


    public static String convertTimeForDisplay(String strATimeToConvert)

    {   
        

        if (DatabaseSchema.getDBMSType() == DBMSTypes.SQLSERVER)
        {
            return SQLServerUtilities.convertTimeForDisplay(strATimeToConvert);
        }
        
        SimpleDateFormat  sdFormatter;

        sdFormatter = new SimpleDateFormat ( "hh:mm a", new Locale ( "en", "US" ) );

        java.util.Date myDate = null;;
         if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE)
        {
            myDate = convertStringToDate(strATimeToConvert, "yyyy/MM/dd HH:mm:ss");
        }
         else 
           myDate = convertStringToDate(strATimeToConvert, "HH:mm:ss");
        
       
        return sdFormatter.format ( myDate );



    }

    

    public static boolean validateTimeValue(String strATimeValue)

    {

        boolean blValueOK = true;

        try

        {

            String strTester = strATimeValue.trim();

       

            if(strTester.length() > 8){ blValueOK = false; }

            else if(strTester.indexOf(':') == -1){ blValueOK = false; }

            else if(!strTester.substring((strTester.length() - 2), strTester.length()).equalsIgnoreCase("PM") && !strTester.substring((strTester.length() - 2), strTester.length()).equalsIgnoreCase("AM")){ blValueOK = false; }

            else if(Integer.parseInt(strTester.substring(0, strTester.indexOf(':'))) > 12){ blValueOK = false; }

            else if(Integer.parseInt(strTester.substring(0, strTester.indexOf(':'))) < 1){ blValueOK = false; }

            else if(Integer.parseInt(strTester.substring((strTester.indexOf(':') + 1), (strTester.indexOf(':') + 3))) > 59){ blValueOK = false; }

            else if(Integer.parseInt(strTester.substring((strTester.indexOf(':') + 1), (strTester.indexOf(':') + 3))) < 0){ blValueOK = false; }

        }

        catch(NumberFormatException nfe)

        {

            blValueOK = false;

        }

        catch(Exception e)

        {

            blValueOK = false;

        }

        return blValueOK;

    }

    public static java.util.Date convertStringToDate(String strAStringDate, String strFormat)

    {      

        return (new java.text.SimpleDateFormat(strFormat)).parse(strAStringDate, new java.text.ParsePosition(0));  

    }

       

    public static boolean validateFloatValue(String strValue)

    {

        boolean blValueOK = true;

        try

        {

            Float intTester = new Float(strValue);

        }

        catch(NumberFormatException nfe)

        {

            blValueOK = false;

        }

        catch(Exception e)

        {

            blValueOK = false;

        }

        return blValueOK;

    }

    public static boolean validateScriptValue(String strScriptValue)

    {

        boolean blValueOK = true;

        try

        {

            Interpreter i = new Interpreter(); 

            i.eval(strScriptValue);

            i = null;

        }

        catch (ParseException pe) {

            blValueOK = false;

        }

        catch(Exception e)

        {

            blValueOK = true;

        }

        return blValueOK;

    }

    

   public static String getDateTimeStampAsString(String aFormat)

	{



		String            dtResult   = new String();



		java.util.Date    today;



		SimpleDateFormat  formatter;



 



		formatter = new SimpleDateFormat (aFormat, new Locale ( "en", "US" ) );

    //	formatter = new SimpleDateFormat (aFormat);

		today = new java.util.Date();



		dtResult = formatter.format ( today );



		dtResult.toString();



		return dtResult;



	}

   

   public static java.sql.Timestamp convertStringToTimestamp(String strValue) {

       String strTemp = "";

       

       // get the year

       strTemp = strValue.substring(27, 31) + "-";

       

       // get the month

       if (strValue.substring(4, 7).equals("Jan"))

           strTemp += "01-";

       else if (strValue.substring(4, 7).equals("Feb"))

           strTemp += "02-";

       else if (strValue.substring(4, 7).equals("Mar"))

           strTemp += "03-";

       else if (strValue.substring(4, 7).equals("Apr"))

           strTemp += "04-";

       else if (strValue.substring(4, 7).equals("May"))

           strTemp += "05-";

       else if (strValue.substring(4, 7).equals("Jun"))

           strTemp += "06-";

       else if (strValue.substring(4, 7).equals("Jul"))

           strTemp += "07-";

       else if (strValue.substring(4, 7).equals("Aug"))

           strTemp += "08-";

       else if (strValue.substring(4, 7).equals("Sep"))

           strTemp += "09-";

       else if (strValue.substring(4, 7).equals("Oct"))

           strTemp += "10-";

       else if (strValue.substring(4, 7).equals("Nov"))

           strTemp += "11-";

       else if (strValue.substring(4, 7).equals("Dec"))

           strTemp += "12-";

       

       // get the day

       strTemp += strValue.substring(8, 10) + " ";

       

       // get the time

       strTemp += strValue.substring(11, 26) + "000";

       

       return java.sql.Timestamp.valueOf(strTemp);

   }

   public static String convertMonths(String strAMonth){
       String strResult = "";
       
       
       if (strAMonth.equals("Jan"))

           strResult = "01";

       else if (strAMonth.equals("Feb"))

           strResult = "02";

       else if (strAMonth.equals("Mar"))

           strResult = "03";

       else if (strAMonth.equals("Apr"))

           strResult = "04";

       else if (strAMonth.equals("May"))

           strResult = "05";

       else if ( strAMonth.equals("Jun") )

           strResult = "06";

       else if  ( strAMonth.equals("Jul") )

           strResult = "07";

       else if  (strAMonth.equals("Aug") )

           strResult = "08";

       else if ( strAMonth.equals("Sep") )

           strResult = "09";

       else if  ( strAMonth.equals("Oct"))

           strResult = "10";

       else if  ( strAMonth.equals("Nov") )

           strResult = "11";

       else if (strAMonth.equals("Dec"))

           strResult = "12";
       
      return strResult; 
       
   }
   

   public static String cleanForXSL(String strDirtyXSL)

  {
      if (strDirtyXSL == null)
          return "";

      strDirtyXSL = strDirtyXSL.replaceAll("&", "&amp;");

      strDirtyXSL = strDirtyXSL.replaceAll("<", "&lt;");

      strDirtyXSL = strDirtyXSL.replaceAll(">", "&gt;");

      strDirtyXSL = strDirtyXSL.replaceAll("'", "&apos;");

      strDirtyXSL = strDirtyXSL.replaceAll("\"", "&quot;");

      

      return strDirtyXSL;

   }

   public static String cleanForXML(String strDirtyXML)

  {

      strDirtyXML = strDirtyXML.replaceAll("&amp;", "&");

      strDirtyXML = strDirtyXML.replaceAll("&lt;", "<");

      strDirtyXML = strDirtyXML.replaceAll("&gt;", ">");

      strDirtyXML = strDirtyXML.replaceAll("&apos;", "'");

      strDirtyXML = strDirtyXML.replaceAll("&quot;", "\"");

      

      return strDirtyXML;

   }

   public static String base64Decode(String base64) {

    int pad = 0;

    for (int i = base64.length() - 1; base64.charAt(i) == '='; i--)

      pad++;

    int length = base64.length()*6/8 - pad;

    byte[] raw = new byte[length];

    int rawIndex = 0;

    for (int i = 0; i < base64.length(); i += 4) {

      int block = (getValue(base64.charAt(i)) << 18) + (getValue(base64.charAt(i + 1)) << 12) + (getValue(base64.charAt(

          i + 2)) << 6) + (getValue(base64.charAt(i + 3)));

      for (int j = 0; j < 3 && rawIndex + j < raw.length; j++)

        raw[rawIndex + j] = (byte)((block >> (8*(2 - j))) & 0xff);

      rawIndex += 3;

    }

    return  new String(raw);

  }

    public static String base64Encode(String strToEncode) {

    byte [] raw = strToEncode.getBytes();

    StringBuffer encoded = new StringBuffer();

    for (int i = 0; i < raw.length; i += 3) {

      encoded.append(encodeBlock(raw, i));

    }

    return encoded.toString();

  }

    private static char[] encodeBlock(byte[] raw, int offset) {

        int block = 0;

        int slack = raw.length - offset - 1;

        int end = (slack >= 2) ? 2 : slack;

        for (int i = 0; i <= end; i++) {

        byte b = raw[offset + i];

        int neuter = (b < 0) ? b + 256 : b;

        block += neuter << (8 * (2 - i));

        }

        char[] base64 = new char[4];

        for (int i = 0; i < 4; i++) {

        int sixbit = (block >>> (6 * (3 - i))) & 0x3f;

        base64[i] = getChar(sixbit);

        }

        if (slack < 1) base64[2] = '=';

        if (slack < 2) base64[3] = '=';

        return base64;

  }

    private static char getChar(int sixBit) {

    if (sixBit >= 0 && sixBit <= 25)

      return (char)('A' + sixBit);

    if (sixBit >= 26 && sixBit <= 51)

      return (char)('a' + (sixBit - 26));

    if (sixBit >= 52 && sixBit <= 61)

      return (char)('0' + (sixBit - 52));

    if (sixBit == 62) return '+';

    if (sixBit == 63) return '/';

    return '?';

  }

    protected static int getValue(char c) {

    if (c >= 'A' && c <= 'Z')

      return  c - 'A';

    if (c >= 'a' && c <= 'z')

      return  c - 'a' + 26;

    if (c >= '0' && c <= '9')

      return  c - '0' + 52;

    if (c == '+')

      return  62;

    if (c == '/')

      return  63;

    if (c == '=')

      return  0;

    return  -1;

  }

    public static int substrReplace ( StringBuffer s1, String s2, String s3 )

	{
               // only replace if there is no null string
               if (s1 == null || s2 == null || s3 == null)
                   return -1;

		//   System.err.println(s1.toString() + "\n" + s2 + "\n" + s3 + "\n\n");

		int  j  = 0;

		int  i  = s1.indexOf ( s2 );

		int  h  = s2.length();



		while (  ( i != -1 ) &&  ( h < s1.length() ) )

		{

			//System.err.println(h + "\t" + i + "\t" + j);

			s1.replace ( i, i + h, s3 );

			i = s1.indexOf ( s2 );

			j++;

		}

		return j;

	}
    
    public static String cleanForExcelFile(String strDirty)
    {
        if (strDirty == null)
          return "";

          strDirty = strDirty.replaceAll("\n", " ");
          strDirty = strDirty.replaceAll("\r", " ");          
          return strDirty;

    }
    
    /** Validate monetary value
     *  @param strValue
     *  @return true if the value is valid
     */
    public static boolean validateMonetaryValue(String strValue)

    {
        
        boolean blValueOK = true;

        try
        {
            if(strValue.indexOf(',') > 0)
            {
                String val1;
                if(strValue.indexOf('.') > 0)
                {
                    val1 = strValue.substring(0,strValue.indexOf('.'));
                }
                else
                {
                    val1 = strValue;
                }
                String val2 = val1.replaceAll(",", "");
                NumberFormat numformat = NumberFormat.getNumberInstance(Locale.US);
                val2 = numformat.format(Double.parseDouble(val2));
                if(!(val1.equals(val2)))
                {
                    blValueOK = false;
                }
                strValue = strValue.replaceAll(",","");
            }
            Float intTester = new Float(strValue);
            
            int indexOfPoint = strValue.indexOf(".");
            
            if (indexOfPoint > 0 && indexOfPoint < strValue.length() - 3)
            {
                blValueOK = false;
            }

        }

        catch(NumberFormatException nfe)

        {
            blValueOK = false;

        }

        catch(Exception e)

        {

            blValueOK = false;

        }

        return blValueOK;

    }

    public static String convertTimeForQuery(String strATimeToConvert) {
        // hh: check the validation of the string
        if (strATimeToConvert == null || strATimeToConvert.length() < 8)
            return "";
        
        String strATimeToReturn = convertTimeForDB(strATimeToConvert);
        if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
            strATimeToReturn = "TO_DATE('01-Jan-1900 " + strATimeToConvert + "', 'DD-MON-YYYY HH24:MI:SS')";
        }
        
        return strATimeToReturn;
    }
    
    public static String convertDateForQuery(String strADateToConvert) {
        if (strADateToConvert == null || strADateToConvert.length() < 10)
            return "";
        
        String strADateToReturn = convertDateForDB(strADateToConvert);
        if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE) {
            SimpleDateFormat dfFormatter = new SimpleDateFormat("dd-MMM-yyyy");
            java.sql.Date dtObject = java.sql.Date.valueOf(strADateToReturn);
            strADateToReturn = dfFormatter.format(dtObject);
        }
        
        return strADateToReturn;
    }
    
    /** Return the difference between 2 dates
     *  @param strDate1
     *  @param strDate2
     *  @param strFormat
     */
    public static long getDateDifference(String strDate1, String strDate2, String strFormat) {
        
        java.util.Date dtDate1 = convertStringToDate(strDate1, strFormat);
        java.util.Date dtDate2 = convertStringToDate(strDate2, strFormat);
        
        return (Math.round((dtDate1.getTime() - dtDate2.getTime()) / 86400000.0 + 0.5));
    }    
    
	public static String convertDurationForDB(String strADurationToConvert)
	{
		//System.err.println("strADurationToConvert = " + strADurationToConvert + "<<<");

        if (strADurationToConvert == null)
            return "";

		String[] strDayHourMinute = strADurationToConvert.split("/");

		String strDay = strDayHourMinute[0];
		String strHour = strDayHourMinute[1];
		String strMinute = strDayHourMinute[2];

		//System.err.println("strDay = >>>" + strDay + "<<<");
		//System.err.println("strHour = >>>" + strHour + "<<<");
		//System.err.println("strMinute = >>>" + strMinute + "<<<");

		int intDay = (new Integer(strDay)).intValue() * 1440;
		int intHour = (new Integer(strHour)).intValue() * 60;
		int intMinute = (new Integer(strMinute)).intValue();

		//System.err.println("intDay = >>>" + intDay + "<<<");
		//System.err.println("intHour = >>>" + intHour + "<<<");
		//System.err.println("intMinute = >>>" + intMinute + "<<<");
		
		int intTotalInMinutes = intDay + intHour + intMinute;

		//System.err.println("intTotalInMinutes = >>>" + intTotalInMinutes + "<<<");

        return (new Integer(intTotalInMinutes)).toString(); 
	}
    
        
        /** Use this function to debug
         */
        public static void printRuntimeData(ChannelRuntimeData runtimeData){
        
        System.out.println("Runtime Data : ");
        Enumeration e = runtimeData.keys();
        while (e.hasMoreElements()) {
            String keyValue = (String) e.nextElement();
            System.out.println(keyValue + ":\t\t" + runtimeData.getParameter(keyValue));
        }
        //System.out.println("----------------");

    }
        
        
       public static int[] shiftArray(int[] availableCells)
        {
            int newArray[] = new int[availableCells.length];
            for (int i = 1; i < availableCells.length; i++)
            {
                newArray[i - 1] = availableCells[i];
            }
            
            return newArray;
            
        }
           public static String cleanForSQLStatement(String strDirty)
    {
        if (strDirty == null)
          return "";

          strDirty = strDirty.replaceAll("'", "''");     
          return strDirty;
    } 
}

