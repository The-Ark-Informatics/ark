/**
 * QueryChannel.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 15/09/2003
 *
 * Last changed by Shendon Ewans 5 March 04
 */

package neuragenix.common;

/**
 *
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.ResultSet;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.StringTokenizer;
// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

// neuragenix packages
import neuragenix.dao.DBField;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DBMSTypes;
import neuragenix.dao.DALQuery;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.utils.Password;
import neuragenix.common.*;

import neuragenix.security.AuthToken;

import neuragenix.common.Utilities;

import java.util.Date;
import java.text.*;

public class QueryChannel
{
   
   /** Creates a new instance of QueryChannel */
   public QueryChannel()
   {
   }
   
   /** Long's Modification, adding method to save upload files */
   
   public static String saveUploadFile( String strFieldName, ChannelRuntimeData runtimeData, String location ) throws Exception
   {
      
      String result = null;
      
      try
      {
         
         org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter(strFieldName);
         
         if(fileToSave == null || fileToSave.getInputStream().available() > PropertiesManager.getPropertyAsInt("org.jasig.portal.PortalSessionManager.File_upload_max_size")
         || fileToSave.getInputStream().available() < 1 )
         {
            // since null = error
            //result = "The file size is over the maximum allowable or no file selected.";
            
         }
         else
         {
            
            // if user enter dummy value
            if( fileToSave.getInputStream().available() < 1 )
               return null;
            
            String strFileName = fileToSave.getName();
            String tag = null;
            int intDotPos = strFileName.lastIndexOf( '.' );
            
            if( intDotPos != -1 )
            {
               
               tag = strFileName.substring( intDotPos );
               
            }
            
            Calendar calCurrent = Calendar.getInstance();
            calCurrent.getInstance();
            
            // Build the new file name with enryption!
            strFileName = Password.getEncrypt(calCurrent.getTime().toString());
            
            // Replace any characters that will stuff up the save!
            strFileName = strFileName.replace((char)47,'n'); // random character
            strFileName = strFileName.replace((char)92,'n'); // random character
            strFileName = strFileName.replace((char)43,'n'); // random character
            strFileName = strFileName.replace((char)63,'n'); // random character
            strFileName = strFileName.replace((char)64,'n'); // random character
            strFileName = strFileName.replace((char)38,'n'); // random character
            strFileName = strFileName.replace((char)58,'n'); // random character
            strFileName = strFileName.replace((char)59,'n'); // random character
            strFileName = strFileName.replace((char)61,'n'); // random character
            strFileName = strFileName.replace((char)44,'n'); // random character
            strFileName = strFileName.replace((char)35,'x'); // random character
            strFileName = strFileName.replace('.','n'); // random character
            
            strFileName = strFileName + tag;
            
            strFileName = strFileName.substring(5);
            
            File fileOnServer = new File( location, strFileName );
            
            if( fileOnServer.exists() )
            {
               
               while( fileOnServer.exists() )
               {
                  
                  calCurrent.getInstance();
                  
                  // Build the new file name with enryption!
                  strFileName = Password.getEncrypt(calCurrent.getTime().toString());
                  
                  // Replace any characters that will stuff up the save!
                  strFileName = strFileName.replace((char)47,'n'); // random character
                  strFileName = strFileName.replace((char)92,'n'); // random character
                  strFileName = strFileName.replace((char)43,'n'); // random character
                  strFileName = strFileName.replace((char)63,'n'); // random character
                  strFileName = strFileName.replace((char)64,'n'); // random character
                  strFileName = strFileName.replace((char)38,'n'); // random character
                  strFileName = strFileName.replace((char)58,'n'); // random character
                  strFileName = strFileName.replace((char)59,'n'); // random character
                  strFileName = strFileName.replace((char)61,'n'); // random character
                  strFileName = strFileName.replace((char)44,'n'); // random character
                  strFileName = strFileName.replace((char)35,'x'); // random character
                  strFileName = strFileName.replace('.','n'); // random character
                  
                  strFileName = strFileName + tag;
                  
                  strFileName = strFileName.substring(5);
                  
                  fileOnServer = new File( location, strFileName );
                  
               }
            }
            
            result = strFileName;
            
            byte[] byteFromFile = new byte[fileToSave.getInputStream().available()];
            
            fileToSave.getInputStream().read(byteFromFile);
            
            FileOutputStream fos = new FileOutputStream( fileOnServer ) ;
            
            fos.write(byteFromFile);
            
            fos.close( ) ;
            
            
         }
      }
      catch( Exception e )
      {
         
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
         
      }
      
      return result;
      
   }
  /*
   *Field Level security
   This is to remove the data with ## while doing an update
   */
   public static Vector prepareFormfieldsForFieldSecurity(Vector formfields,AuthToken authToken) throws Exception
   {
      Vector vtFormFields = (Vector) formfields.clone();
      
      try
      {
         Enumeration eformfields= formfields.elements();
         while (eformfields.hasMoreElements())
         {
            String key = (String)eformfields.nextElement();
            if(authToken.hasDenyActivity(key))
            {
               vtFormFields.remove(key);
            }
         }
      }
      catch(Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      
      return vtFormFields;
   }
   
   public static Vector prepareFormfieldsForFieldSecurity(Vector formfields,ChannelRuntimeData runtimeData)
   {
      
      Vector vtFormFields = (Vector) formfields.clone();
      
      for(Enumeration eformfields=formfields.elements(); eformfields.hasMoreElements();)
      {
         String key = (String)eformfields.nextElement();
         if(runtimeData.getParameter(key) != null && runtimeData.getParameter(key).startsWith("#"))
         {
            vtFormFields.remove(key);
         }
      }
      
      return vtFormFields;
   }
   
   public static String uploadFile( String strFieldName, ChannelRuntimeData runtimeData, String location,String newName ) throws Exception
   {
      String result = null;
      try
      {
         org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter( strFieldName );
         String strFileName = "" ;
         if(newName != null)
         {
            strFileName  = newName;
         }
         else
         {
            strFileName = fileToSave.getName();
         }
         
         File fileOnServer = new File( location, strFileName );
         if(fileOnServer.exists())
         {
            fileOnServer.delete();
         }
         result = strFileName;
         
         byte[] byteFromFile = new byte[fileToSave.getInputStream().available()];
         
         fileToSave.getInputStream().read(byteFromFile);
         
         FileOutputStream fos = new FileOutputStream( fileOnServer ) ;
         
         fos.write(byteFromFile);
         
         fos.close( ) ;
         
      }
      catch( Exception e )
      {
         
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
         
      }
      
      return result;
      
   }
   
   public static String checkFileSize(String strfilename,ChannelRuntimeData runtimeData)
   {
      String error = null;
      try
      {
         org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter(strfilename);
         if(fileToSave == null || fileToSave.getInputStream().available() > PropertiesManager.getPropertyAsInt("org.jasig.portal.PortalSessionManager.File_upload_max_size")
         || fileToSave.getInputStream().available() < 1 )
         {
            error = "The file size is over the maximum allowable or of 0kb size or no file selected.";
            
         }
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
      return error;
   }
   
   public static String checkFileType(String strfilename,ChannelRuntimeData runtimeData,String type)
   {
      String error = null;
      org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter(strfilename);
      String filename = fileToSave.getName();
      int indx = filename.lastIndexOf('.');
      String extension = filename.substring(indx+1);
      if(!extension.equals(type))
      {
         error =  "This file type is not allowed. The only file type allowed is of the extension "+ type;
      }
      return error;
   }
   
   public static Hashtable makeHashTableFromRuntimeData(Vector vtFields, ChannelRuntimeData rdData)
   {
      Hashtable htReturn = new Hashtable();
      
      if (vtFields == null || rdData == null)
         return null;
      
      for (int i = 0; i < vtFields.size(); i++)
      {
         htReturn.put((String) vtFields.get(i), rdData.getParameter((String) vtFields.get(i)));
      }
      
      if (htReturn.size() == 0)
         return null;
      else
         return htReturn;
      
      
   }
   
   
   /**
    *
    *
    */
   public static void updateDateValuesInRuntimeData(Vector vtFields, ChannelRuntimeData runtimeData)
   {
      if (vtFields == null || runtimeData == null)
         return;
      
      String strFieldName;
      DBField field;
      
      
      for (int i=0; i<vtFields.size(); i++)
      {
         strFieldName = (String) vtFields.get(i);
         field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         
         if (field.getDataType() == DBMSTypes.DATE_TYPE )
         {
            //System.out.println("Checking" + strFieldName);
            // make sure it does not overwrite stuff already in there
            // i.e. in Vial Calculation
            if (runtimeData.getParameter(strFieldName) == null)
               runtimeData.setParameter(strFieldName, makeDateFromForm(strFieldName, runtimeData));
            
         }
      }
      
      
   }
   
   public static void updateTimeValuesInRuntimeData(Vector vtFields, ChannelRuntimeData runtimeData)
   {
      if (vtFields == null || runtimeData == null)
         return;
      
      String strFieldName;
      DBField field;
      
      
      for (int i=0; i<vtFields.size(); i++)
      {
         strFieldName = (String) vtFields.get(i);
         field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         
         if (field.getDataType() == DBMSTypes.TIME_TYPE )
         {
            
            if (runtimeData.getParameter(strFieldName) == null )
               runtimeData.setParameter(strFieldName, makeTimeFromForm(strFieldName, runtimeData));
            
         }
      }
      
      
   }
   
   /** Method to build a date from form by concatinating dd/mm/yy
    */
   
   public static String makeDateFromForm( String strField, ChannelRuntimeData rdData )
   {
      String strResult = null;
      try
      {
         
         // Ensure that the runtime data is not null or is not empty
         if (    (rdData.getParameter(strField + "_Day") != null) && (rdData.getParameter(strField + "_Day").length() != 0) &&
                 (rdData.getParameter(strField + "_Month") != null) && (rdData.getParameter(strField + "_Month").length() != 0) &&
                 (rdData.getParameter(strField + "_Year") != null) && (rdData.getParameter(strField + "_Year").length() != 0)
                 )
         {
            strResult = rdData.getParameter( strField + "_Day") + "/" + rdData.getParameter( strField + "_Month") + "/"
                    + rdData.getParameter( strField + "_Year");
            // too short date format
            //if (strResult.length() != 10)
            //    strResult = "";
            
            if(!strResult.startsWith("#"))
            {
               strResult = makeCorrectDateFormat(strResult);
            }
         }
         
         
         
      }
      catch(Exception e)
      {
         
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         
      }
      return strResult;
   }
   
   
   /** Method to build a correct date format from a length-varied but verified date string
    * Helper of makeDataFromForm
    */
   public static String makeCorrectDateFormat(String strRawDate)
   {
      String strResult = null;
      //if ( strRawDate == null) return null;
      try
      {
         
         // first  replace all - with / if possible so that we can use / separator
         strRawDate = strRawDate.replace('-','/');
         
         
         //now tokenizer it using / as the separator
         StringTokenizer tok = new StringTokenizer(strRawDate,"/");
         
         // if cant get date, month and year in 3 tokens then false
         if (tok.countTokens()!= 3) return "";
         
         int date = Integer.parseInt(tok.nextToken());
         // Get the month
         int month = Integer.parseInt(tok.nextToken());
         // Get the year
         int year = Integer.parseInt(tok.nextToken());
         
         
         //convert the year
         if (year>=0 && year <=9) year+= 2000; // year from 2001 to 2009
         if (year>=11 && year <=99) year+= 1900; //year from 1910 to 1999
         
         //now combine them together and return the correct format
         if (date <= 9)
            strResult = "0"+ Integer.toString(date);
         else
            strResult = Integer.toString(date);
         
         if ( month <= 9 )
            strResult += "/0" + Integer.toString(month);
         else strResult += "/" + Integer.toString(month);
         
         
         strResult += "/" + Integer.toString(year);
         
         
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         strResult = "Invalid Date";
         //throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      return strResult;
   }
   
   
   
   
   
   /** Method to build a time from form by concatinating hh/mm/am
    */
   
   public static String makeTimeFromForm( String strField, ChannelRuntimeData rdData )
   {
      String strResult = null;
      try
      {
         
         strResult = rdData.getParameter( strField + "_Hour") + ":" + rdData.getParameter( strField + "_Minute") + " " + rdData.getParameter( strField + "_AMPM");
         
      }
      catch(Exception e)
      {
         
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         
      }
      return strResult;
   }
   
   
   /** Return a vector keeping result from a query
    */
   public static Vector lookupRecord(ResultSet rsResultSet, Vector vtFields) throws Exception
   {
      Vector vtResult = new Vector(10, 10);
      int intNoOfFields = vtFields.size();
      
      try
      {
         while (rsResultSet.next())
         {
            Hashtable hashTemp = new Hashtable(10);
            
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
               
               if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
               {
                  SimpleDateFormat dfFormatter = new SimpleDateFormat("dd/MM/yyyy");
                  java.sql.Date dtObject = rsResultSet.getDate(strFieldName);
                  
                  if (dtObject != null)
                  {
                     hashTemp.put(strFieldName, dfFormatter.format(dtObject));
                  }
               }
               else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
               {
                  SimpleDateFormat dfFormatter = new SimpleDateFormat("hh:mm a");
                  java.sql.Time tmObject = rsResultSet.getTime(strFieldName);
                  
                  if (tmObject != null)
                  {
                     hashTemp.put(strFieldName, dfFormatter.format(tmObject));
                  }
               }
               else if (currentField.getDataType() == DBMSTypes.CLOB_TYPE)
               {
                  if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE)
                  {
                     oracle.sql.CLOB clObject = (oracle.sql.CLOB) rsResultSet.getClob(strFieldName);
                     String strClobContent = null;
                     if (clObject != null)
                     {
                        strClobContent = clObject.getSubString(1, DatabaseSchema.getMaxClobSize());
                     }
                     
                     if (strClobContent != null)
                     {
                        hashTemp.put(strFieldName, strClobContent);
                     }
                  }
                  else
                  {
                     String strClobContent = rsResultSet.getString(strFieldName);
                     
                     if (strClobContent != null)
                     {
                        hashTemp.put(strFieldName, strClobContent);
                     }
                  }
               }
               else
               {
                  String strData = null;
                  try
                  {
                     strData = rsResultSet.getString(strFieldName);
                  }
                  catch (Exception e)
                  {
                     //System.out.println("FILED NAME FAILDED " + strFieldName);
                     e.printStackTrace(System.err);
                  }
                  
                  if (strData != null)
                  {
                     hashTemp.put(strFieldName, strData);
                  }
               }
            }
            
            vtResult.add(hashTemp);
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel -  "+ e.toString(), e);
         
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return vtResult;
   }
   
   
   /** Build XML string for form fields label
    */
   public static String buildFormLabelXMLFile(Vector vtFields)
   {
      String strXML = "";
      
      if ( vtFields == null )
      {
         // System.out.println("found vtFields null");
      }
      
      int intNoOfFields = vtFields.size();
      
      for (int i=0; i < intNoOfFields; i++)
      {
         String strFieldName = (String) vtFields.get(i);
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         
         
         strXML += "<" + strFieldName + "Display";
         
         if (field.isRequired())
            strXML += " required=\"true\"";
         
         strXML +=  ">" + field.getLabelInForm() + "</" + strFieldName + "Display>";
         if (field != null)
         {
            strXML += "<" + strFieldName + "Display";
            
            if (field.isRequired())
               strXML += " required=\"true\"";
            
            strXML +=  ">" + field.getLabelInForm() + "</" + strFieldName + "Display>";
            
         }
         else
         {
            System.err.println("[QueryChannel :: Warning] Unable to locate schema information for field '" + strFieldName + "'");
         }
         
      }
      
      return strXML;
   }
   
   /** Build XML string for column fields label
    */
   public static String buildColumnLabelXMLFile(Vector vtFields)
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      for (int i=0; i < intNoOfFields; i++)
      {
         String strFieldName = (String) vtFields.get(i);
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         strXML += "<" + strFieldName + "Display";
         
         if (field.isRequired())
            strXML += " required=\"true\"";
         
         strXML +=  ">" + field.getLabelInColumn() + "</" + strFieldName + "Display>";
         
         //strXML += "<" + strFieldName + "Display>" +	field.getLabelInColumn() + "</" + strFieldName + "Display>";
      }
      
      return strXML;
   }
   
   
   /** Build XML string for add new object form
    */
   public static String buildAddFormXMLFile(Vector vtFields)
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      try
      {
         for (int i=0; i < intNoOfFields; i++)
         {
            String strFieldName = (String) vtFields.get(i);
            DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            //System.err.println("Field name: "+ strFieldName );
            // if the field is a dropdown field
            if (field.getLOVType() != null)
               strXML += buildLOVXMLFile(strFieldName, null);
            // if the field is a date type field
            else if (field.getDataType() == DBMSTypes.DATE_TYPE)
            {
               if (field.getDefaultDate() == null)
                  strXML += buildDateXMLFile(field, strFieldName, null);
               else
                  strXML += buildDateXMLFile(field, strFieldName, field.getDefaultDate());
            }
            // if the field is a time type field
            else if (field.getDataType() == DBMSTypes.TIME_TYPE)
               strXML += buildTimeDropDownXMLFile(strFieldName, null);
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         
      }
      
      return strXML;
   }
   
   //rennypv...overloaded to introduce field level security
   
    /*public static String buildAddFormXMLFile(Vector vtFields,AuthToken authToken) throws Exception {
        String strXML = "";
        int intNoOfFields = vtFields.size();
     
        try {
            for (int i=0; i < intNoOfFields; i++) {
                String strFieldName = (String) vtFields.get(i);
                DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
     
                //System.err.println("Field name: "+ strFieldName );
                // if the field is a dropdown field
                if (field.getLOVType() != null)
                    strXML += buildLOVXMLFile(strFieldName, null);
                // if the field is a date type field
                else if (field.getDataType() == DBMSTypes.DATE_TYPE)
                {
                    if (field.getDefaultDate() == null)
                        strXML += buildDateXMLFile(field, strFieldName, null);
                    else
                        strXML += buildDateXMLFile(field, strFieldName, field.getDefaultDate());
                }
                // if the field is a time type field
                else if (field.getDataType() == DBMSTypes.TIME_TYPE)
                    strXML += buildTimeDropDownXMLFile(strFieldName, null);
            }
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
            throw new Exception("Unknown error in QueryChannel - " + e.toString());
        }
     
        return strXML;
    }*/
   
   public static String buildViewXMLFile(Vector vtFields, ChannelRuntimeData rdData ) throws Exception
   {
	  return buildViewXMLFile( vtFields, rdData, -1);
	   
   }
   
   
   /** Build XML string for view form
    */
   public static String buildViewXMLFile(Vector vtFields, ChannelRuntimeData rdData, int intStudyKey) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      try
      {
         for (int i=0; i < intNoOfFields; i++)
         {
            String strFieldName = (String) vtFields.get(i);
            DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            // if the field is a dropdown field
            if (field.getLOVType() != null)
            	
               strXML += buildLOVXMLFile(strFieldName, rdData.getParameter(strFieldName), intStudyKey);
            // if the field is a date type field
            else if (field.getDataType() == DBMSTypes.DATE_TYPE)
            {   // if date is null
               if (rdData.getParameter(strFieldName) == null)
               {
                  if (field.getDefaultDate() == null)
                     strXML += buildDateXMLFile(field, strFieldName, null);
                  else
                     strXML += buildDateXMLFile(field, strFieldName, field.getDefaultDate());
               }
               else
               {
                  strXML += buildDateXMLFile(field, strFieldName, rdData.getParameter(strFieldName));
               }
            }
            // if the field is a time type field
            else if (field.getDataType() == DBMSTypes.TIME_TYPE)
               strXML += buildTimeDropDownXMLFile(strFieldName, rdData.getParameter(strFieldName));
            // normal field
            else if (rdData.getParameter(strFieldName) != null)
               strXML += "<" + strFieldName + ">" + Utilities.cleanForXSL(rdData.getParameter(strFieldName)) + "</" + strFieldName + ">";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   /** Build XML string for view form after updating
    */
   public static String buildViewAfterUpdateXMLFile(Vector vtFields, ChannelRuntimeData rdData, String strPrimaryKeyField) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      if (strPrimaryKeyField != null)
         strXML += "<" + strPrimaryKeyField + ">" + rdData.getParameter(strPrimaryKeyField) + "</" + strPrimaryKeyField + ">";
      
      try
      {
         for (int i=0; i < intNoOfFields; i++)
         {
            String strFieldName = (String) vtFields.get(i);
            DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            // if the field is a dropdown field
            if (field.getLOVType() != null)
               strXML += buildLOVXMLFile(strFieldName, rdData.getParameter(strFieldName));
            // if the field is a date type field
            else if (field.getDataType() == DBMSTypes.DATE_TYPE)
            {
               if (rdData.getParameter(strFieldName) == null)
               {
                  if (field.getDefaultDate() == null)
                     strXML += buildDateDropDownXMLFile(strFieldName, null);
                  else
                     strXML += buildDateDropDownXMLFile(strFieldName, field.getDefaultDate());
               }
               else
               {
                  strXML += buildDateDropDownXMLFile(strFieldName, rdData.getParameter(strFieldName));
               }
            }
            // if the field is a time type field
            else if (field.getDataType() == DBMSTypes.TIME_TYPE)
               strXML += buildTimeDropDownXMLFile(strFieldName, rdData.getParameter(strFieldName));
            // normal field
            else if (rdData.getParameter(strFieldName) != null)
               strXML += "<" + strFieldName + ">" + Utilities.cleanForXSL(rdData.getParameter(strFieldName)) + "</" + strFieldName + ">";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   public static String buildXMLFromRuntimeData(Vector vtParameters, ChannelRuntimeData rdData)
   {
      StringBuffer strXML = new StringBuffer();
      
      for (int i = 0; i < vtParameters.size(); i++)
      {
         String strParameterName = (String) vtParameters.get(i);
         String strRDValue = (String) rdData.getParameter(strParameterName);
         
         if (strParameterName != null)
         {
            strXML.append("<" + "runtimeData" + " name=\"" + strParameterName + "\"" + ">");
            if (strRDValue != null)
               strXML.append(strRDValue);
            strXML.append("</" + "runtimeData" + ">");
         }
         
      }
      
      return strXML.toString();
      
   }
   
   
   
   
   
   
   public static String buildViewAfterAddXMLFile(Vector vtFields, ChannelRuntimeData rdData, String strPrimaryKeyField, int intKeyValue) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      if (strPrimaryKeyField != null)
         strXML += "<" + strPrimaryKeyField + ">" + intKeyValue + "</" + strPrimaryKeyField + ">";
      
      try
      {
         for (int i=0; i < intNoOfFields; i++)
         {
            String strFieldName = (String) vtFields.get(i);
            DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            // if the field is a dropdown field
            if (field.getLOVType() != null)
               strXML += buildLOVXMLFile(strFieldName, rdData.getParameter(strFieldName));
            // if the field is a date type field
            else if (field.getDataType() == DBMSTypes.DATE_TYPE)
            {
               if (rdData.getParameter(strFieldName) == null)
               {
                  if (field.getDefaultDate() == null)
                     strXML += buildDateDropDownXMLFile(strFieldName, null);
                  else
                     strXML += buildDateDropDownXMLFile(strFieldName, field.getDefaultDate());
               }
               else
               {
                  strXML += buildDateDropDownXMLFile(strFieldName, rdData.getParameter(strFieldName));
               }
            }
            // if the field is a time type field
            else if (field.getDataType() == DBMSTypes.TIME_TYPE)
               strXML += buildTimeDropDownXMLFile(strFieldName, rdData.getParameter(strFieldName));
            // normal field
            else if (rdData.getParameter(strFieldName) != null)
               strXML += "<" + strFieldName + ">" + Utilities.cleanForXSL(rdData.getParameter(strFieldName)) + "</" + strFieldName + ">";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   /** Build XML string for view form based on the object key
    */
   public static String buildViewFromKeyXMLFile(Vector vtFields, String strPrimaryKeyField, int intKeyValue) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      String strDomainName = ((DBField) DatabaseSchema.getFields().get(strPrimaryKeyField)).getDomain();
      //ChannelRuntimeData rdData = new ChannelRuntimeData();
      
      try
      {
         DALQuery query = new DALQuery();
         query.setDomain(strDomainName, null, null, null);
         query.setFields(vtFields, null);
         query.setWhere(null, 0, strPrimaryKeyField, "=", new Integer(intKeyValue).toString(), 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rsResultSet = query.executeSelect();
         
         while (rsResultSet.next())
         {
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
               
               String data = rsResultSet.getString(strFieldName);
               
               // if the field is a dropdown field
               if (field.getLOVType() != null)
                  strXML += buildLOVXMLFile(strFieldName, data);
               // if the field is a date type field
               else if (field.getDataType() == DBMSTypes.DATE_TYPE)
               {
                  //java.sql.Date dtObject = rsResultSet.getDate(strFieldName);
                  
                  if (data != null)
                  {
                     strXML += buildDateXMLFile(field, strFieldName, Utilities.convertDateForDisplay(data));
                     //SimpleDateFormat dfFormatter = new SimpleDateFormat("dd/MM/yyyy");
                     //strXML+=(buildDateDropDownXMLFile(strFieldName, dfFormatter.format(dtObject)));
                     //System.out.println("Date is "+ dfFormatter.format(dtObject));
                  }
                  else
                  {
                     /*
                     if (field.getDefaultDate() == null)
                        strXML += buildDateXMLFile(field, strFieldName, null);
                     else
                        strXML += buildDateXMLFile(field, strFieldName, field.getDefaultDate());
                      */  
                      //it should not show the default date
                      strXML += buildDateXMLFile(field, strFieldName, "");
                  }
               }
               // if the field is a time type field
               else if (field.getDataType() == DBMSTypes.TIME_TYPE)
               {
                  if (data != null)
                     strXML += buildTimeDropDownXMLFile(strFieldName, Utilities.convertTimeForDisplay(data));
                  else
                     strXML += buildTimeDropDownXMLFile(strFieldName, null);
               }
               else if (field.getDataType() == DBMSTypes.CLOB_TYPE)
               {
                  if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE)
                  {
                     oracle.sql.CLOB clObject = (oracle.sql.CLOB) rsResultSet.getClob(strFieldName);
                     String strClobContent = null;
                     if (clObject != null)
                     {
                        strClobContent = clObject.getSubString(1, DatabaseSchema.getMaxClobSize());
                     }
                     if (strClobContent != null)
                     {
                        strXML += ("<" + strFieldName + ">" + Utilities.cleanForXSL(strClobContent) + "</" + strFieldName + ">");
                     }
                  }
                  else
                  {
                     String strClobContent = rsResultSet.getString(strFieldName);
                     if (strClobContent != null)
                     {
                        strXML += ("<" + strFieldName + ">" + Utilities.cleanForXSL(strClobContent) + "</" + strFieldName + ">");
                     }
                  }
               }
               // normal field
               else if (data != null)
               {
                  strXML += "<" + strFieldName + ">" + Utilities.cleanForXSL(data) + "</" + strFieldName + ">";
               }
               
            }
         }
         rsResultSet.close();
         rsResultSet = null;
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      if (strPrimaryKeyField != null)
         strXML += "<" + strPrimaryKeyField + ">" + intKeyValue + "</" + strPrimaryKeyField + ">";
      
      return strXML;
   }
   
   
   
   /** Build XML string for view form based on the object key
    */
   public static String buildViewFromKeyXMLFile(Vector vtFields, String strPrimaryKeyField, String intKeyValue) throws Exception
   {
      StringBuffer strXML = new StringBuffer();
      int intNoOfFields = vtFields.size();
      String strDomainName = ((DBField) DatabaseSchema.getFields().get(strPrimaryKeyField)).getDomain();
      //ChannelRuntimeData rdData = new ChannelRuntimeData();
      
      try
      {
         DALQuery query = new DALQuery();
         query.setDomain(strDomainName, null, null, null);
         query.setFields(vtFields, null);
         query.setWhere(null, 0, strPrimaryKeyField, "=", intKeyValue, 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rsResultSet = query.executeSelect();
         
         while (rsResultSet.next())
         {
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
               
               // if the field is a dropdown field
               if (field.getLOVType() != null)
               {
                  String data = rsResultSet.getString(strFieldName);
                  strXML.append(buildLOVXMLFile(strFieldName, data));
               }
               // if the field is a date type field
               else if (field.getDataType() == DBMSTypes.DATE_TYPE)
               {
                  java.sql.Date dtObject = rsResultSet.getDate(strFieldName);
                  
                  if (dtObject != null)
                  {
                     SimpleDateFormat dfFormatter = new SimpleDateFormat("dd/MM/yyyy");
                     strXML.append(buildDateXMLFile(field, strFieldName, dfFormatter.format(dtObject)));
                  }
                  else 
                      strXML.append(buildDateXMLFile(field, strFieldName, ""));
                      /* if (field.getDefaultDate() == null)
                  {
                     strXML.append(buildDateDropDownXMLFile(strFieldName, null));
                  }
                  else
                  {
                     strXML.append(buildDateDropDownXMLFile(strFieldName, field.getDefaultDate()));
                  }*/
               }
               // if the field is a time type field
               else if (field.getDataType() == DBMSTypes.TIME_TYPE)
               {
                  java.sql.Time tmObject = rsResultSet.getTime(strFieldName);
                  
                  if (tmObject != null)
                  {
                     SimpleDateFormat dfFormatter = new SimpleDateFormat("hh:mm a");
                     strXML.append(buildTimeDropDownXMLFile(strFieldName, dfFormatter.format(tmObject)));
                  }
                  else
                  {
                     strXML.append(buildTimeDropDownXMLFile(strFieldName, null));
                  }
               }
               else if (field.getDataType() == DBMSTypes.CLOB_TYPE)
               {
                  if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE)
                  {
                     oracle.sql.CLOB clObject = (oracle.sql.CLOB) rsResultSet.getClob(strFieldName);
                     String strClobContent = null;
                     if (clObject != null)
                     {
                        strClobContent = clObject.getSubString(1, DatabaseSchema.getMaxClobSize());
                     }
                     if (strClobContent != null)
                     {
                        strXML.append("<" + strFieldName + ">" + Utilities.cleanForXSL(strClobContent) + "</" + strFieldName + ">");
                     }
                  }
                  else
                  {
                     String strClobContent = rsResultSet.getString(strFieldName);
                     if (strClobContent != null)
                     {
                        strXML.append("<" + strFieldName + ">" + Utilities.cleanForXSL(strClobContent) + "</" + strFieldName + ">");
                     }
                  }
               }
               // normal field
               else
               {
                  String data = rsResultSet.getString(strFieldName);
                  
                  if (data != null)
                  {
                     strXML.append("<" + strFieldName + ">" + Utilities.cleanForXSL(data) + "</" + strFieldName + ">");
                  }
               }
            }
         }
         // close the resultset
         rsResultSet.close();
         rsResultSet = null;
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      if (strPrimaryKeyField != null)
         strXML.append("<" + strPrimaryKeyField + ">" + intKeyValue + "</" + strPrimaryKeyField + ">");
      
      return strXML.toString();
   }
   
   //rennypv----overloaded to include field level security
   
   public static String buildViewFromKeyXMLFile(Vector vtFields, String strPrimaryKeyField, String intKeyValue,AuthToken authToken) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      String strDomainName = ((DBField) DatabaseSchema.getFields().get(strPrimaryKeyField)).getDomain();
      //ChannelRuntimeData rdData = new ChannelRuntimeData();
      
      if (strPrimaryKeyField == null)
         LogService.instance().log(LogService.ERROR, "[QueryChannel : buildViewFromKeyXMLFile] No primary key field specified");
      
      if (intKeyValue == null)
         LogService.instance().log(LogService.ERROR, "[QueryChannel : buildViewFromKeyXMLFile] No key value specified");
      
      try
      {
         
         
         DALQuery query = new DALQuery();
         query.setDomain(strDomainName, null, null, null);
         query.setFields(vtFields, null);
         query.setWhere(null, 0, strPrimaryKeyField, "=", intKeyValue, 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rsResultSet = query.executeSelect();
         
         while (rsResultSet.next())
         {
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
               
               String data = rsResultSet.getString(i + 1);
               //if field is not to be viewed by the user/group
               if(authToken.hasDenyActivity(strFieldName))
               {
                  // strXML += "<" + strFieldName + ">######</" + strFieldName + ">";
                  data = "######";
               }
               // if the field is a dropdown field
               if (field.getLOVType() != null)
                  strXML += buildLOVXMLFile(strFieldName, data);
               // if the field is a date type field
               else if (field.getDataType() == DBMSTypes.DATE_TYPE)
               {
                  if (data != null)
                  {
                     if(data.startsWith("##"))
                     {
                        strXML += buildDateXMLFile(field, strFieldName, data.substring(0,2));
                     }
                     else
                     {
                        strXML += buildDateXMLFile(field, strFieldName, Utilities.convertDateForDisplay(data));
                     }
                  }
                  else
                  /*{
                     if (field.getDefaultDate() == null)
                        strXML += buildDateXMLFile(field, strFieldName, null);
                     else
                        strXML += buildDateXMLFile(field, strFieldName, field.getDefaultDate());
                  }*/
                      //should show the actual database result
                      strXML += buildDateXMLFile(field, strFieldName, "");
               }
               // if the field is a time type field
               else if (field.getDataType() == DBMSTypes.TIME_TYPE)
               {
                  if (data != null)
                     strXML += buildTimeDropDownXMLFile(strFieldName, Utilities.convertTimeForDisplay(data));
                  else
                     strXML += buildTimeDropDownXMLFile(strFieldName, null);
               }
               else if (field.getDataType() == DBMSTypes.CLOB_TYPE)
               {
                  if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE)
                  {
                     oracle.sql.CLOB clObject = (oracle.sql.CLOB) rsResultSet.getClob(strFieldName);
                     String strClobContent = null;
                     if (clObject != null)
                     {
                        strClobContent = clObject.getSubString(1, DatabaseSchema.getMaxClobSize());
                     }
                     if (strClobContent != null)
                     {
                        strXML+=("<" + strFieldName + ">" + Utilities.cleanForXSL(strClobContent) + "</" + strFieldName + ">");
                     }
                  }
                  else
                  {
                     String strClobContent = rsResultSet.getString(strFieldName);
                     if (strClobContent != null)
                     {
                        strXML+=("<" + strFieldName + ">" + Utilities.cleanForXSL(strClobContent) + "</" + strFieldName + ">");
                     }
                  }
               }
               // normal field
               else if (data != null)
               {
                  strXML += "<" + strFieldName + ">" + Utilities.cleanForXSL(data) + "</" + strFieldName + ">";
               }
               
            }
         }
         rsResultSet.close();
         rsResultSet = null;
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         e.printStackTrace();
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      if (strPrimaryKeyField != null)
         strXML += "<" + strPrimaryKeyField + ">" + intKeyValue + "</" + strPrimaryKeyField + ">";
      
      return strXML;
   }
   
   //end rennypv
   
   public static String buildLOVXMLFile(String strFieldName, String strValue)  throws Exception {
	   return  buildLOVXMLFile( strFieldName,  strValue, -1);
	   
   }
   
   /** Build XML string for lov fields
    */
   public static String buildAJAXLOVXMLFile(String strFieldName, String strValue, int intStudyKey,String parentValue) throws Exception
   {
      
      String strXML = "";
      boolean blIsInList = false;
      
      try
      {
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         String strLOVType = field.getLOVType();
         DALQuery query = new DALQuery();
         if (parentValue == null) {
    
    
         
            query.setDomain("LOV", null, null, null);
            Vector vtFields = DatabaseSchema.getFormFields("view_listofvalues");
            query.setFields(vtFields, null);
            query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0, DALQuery.WHERE_HAS_VALUE);
            if (intStudyKey != -1){
            	query.setWhere("AND", 1, "LOV_intStudyID", "=", ""+intStudyKey, 0, DALQuery.WHERE_HAS_VALUE);
            	query.setWhere("OR", 0, "LOV_intStudyID", "=", "0", 1, DALQuery.WHERE_HAS_VALUE);
            }
            query.setWhere("AND", 0, "LOV_strLOVParentValue", "IS NULL","IS NULL", 0, DALQuery.WHERE_HAS_NULL_VALUE);
            query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("LOV_intLOVSortOrder", "ASC");
    }
    else
    {
 
          query.setDomain("LOV", null, null, null);
          Vector vtFields = DatabaseSchema.getFormFields("view_listofvalues");
          query.setFields(vtFields, null);
          query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0, DALQuery.WHERE_HAS_VALUE);
          query.setWhere("AND", 0, "LOV_strLOVParentValue", "=", parentValue, 0, DALQuery.WHERE_HAS_VALUE);
          query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
          query.setOrderBy("LOV_intLOVSortOrder", "ASC");
    
    }
            ResultSet rsResultSet = query.executeSelect();
            while (rsResultSet.next())
            {
               String strTempValue = rsResultSet.getString("LOV_strLOVValue");
               
               if (strTempValue == null)
               {
               }
               strXML += "<" + "lov"+ " selected=\"";
               
               
               if (strValue == null)
                  strXML += "0";
               //if (strValue == null && rsResultSet.isFirst())
               //    strXML += "1";
               else if(strValue.equals(strTempValue))
               {
                  strXML += "1";
                  blIsInList = true;
               }
               else
                  strXML += "0";
               
               strXML += "\">"+ "<name>"+Utilities.cleanForXSL(strTempValue)+"</name>";
               strXML += "</lov>";
            }
            
            
            rsResultSet.close();
            rsResultSet = null;
            
          
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   
   /** Build XML string for lov fields
    */
   public static String buildLOVXMLFile(String strFieldName, String strValue, int intStudyKey) throws Exception
   {
      
      String strXML = "";
      boolean blIsInList = false;
      
      try
      {
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         String strLOVType = field.getLOVType();
         if(strLOVType != null && strValue != null && strValue.startsWith("#"))
         {
            strXML += "<" + strFieldName + " selected=\"1\">";
            strXML += strValue + "</" + strFieldName + ">";
         }
         else if (strLOVType != null)
         {
            DALQuery query = new DALQuery();
            query.setDomain("LOV", null, null, null);
            Vector vtFields = DatabaseSchema.getFormFields("view_listofvalues");
            query.setFields(vtFields, null);
            query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0, DALQuery.WHERE_HAS_VALUE);
            if (intStudyKey != -1){
            	query.setWhere("AND", 1, "LOV_intStudyID", "=", ""+intStudyKey, 0, DALQuery.WHERE_HAS_VALUE);
            	query.setWhere("OR", 0, "LOV_intStudyID", "=", "0", 1, DALQuery.WHERE_HAS_VALUE);
            }
            query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("LOV_intLOVSortOrder", "ASC");
            
            ResultSet rsResultSet = query.executeSelect();
            while (rsResultSet.next())
            {
               String strTempValue = rsResultSet.getString("LOV_strLOVValue");
               
               if (strTempValue == null)
               {
               }
               strXML += "<" + strFieldName + " selected=\"";
               
               
               if (strValue == null)
                  strXML += "0";
               //if (strValue == null && rsResultSet.isFirst())
               //    strXML += "1";
               else if(strValue.equals(strTempValue))
               {
                  strXML += "1";
                  blIsInList = true;
               }
               else
                  strXML += "0";
               
               strXML += "\">" + Utilities.cleanForXSL(strTempValue);
               strXML += "</" + strFieldName + ">";
               
            }
            
            
            rsResultSet.close();
            rsResultSet = null;
            
            if (blIsInList == false && strValue != null)
            {
               
               strXML += "<" + strFieldName + " selected=\"1\">"+ Utilities.cleanForXSL(strValue) + " </" + strFieldName + ">";
            }
            
            
            if (strValue == null)
               strXML += "<" + strFieldName + "_Selected></" + strFieldName + "_Selected>";
            else
               strXML += "<" + strFieldName + "_Selected>" + Utilities.cleanForXSL(strValue) + "</" + strFieldName + "_Selected>";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   /**
    *
    * @param strFieldName name of the field we want to build XML for
    * @param strValue current value of that field
    * @param strParentValue Parent value
    * @return
    * @throws Exception
    */
   public static String buildLOVXMLFromParent(String strFieldName, String strValue, String strParentValue ) throws Exception
   {
      String strXML = "";
      
      boolean blIsInList = false;
      try
      {
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         String strLOVType = field.getLOVType();
         if (strParentValue == null){
            return ""; 
         }
         if (strLOVType != null)
         {
            DALQuery query = new DALQuery();
            query.setDomain("LOV", null, null, null);
            Vector vtFields = DatabaseSchema.getFormFields("view_listofvalues");
            query.setFields(vtFields, null);
            query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "LOV_strLOVParentValue", "=", strParentValue, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("LOV_intLOVSortOrder", "ASC");
           
            
            
           
            ResultSet rsResultSet = query.executeSelect();
            
            
            while (rsResultSet.next())
            {
               String strTempValue = rsResultSet.getString("LOV_strLOVValue");
               
               strXML += "<" + strFieldName + " selected=\"";
               
               if (strValue == null)
                  strXML += "0";
               //if (strValue == null && rsResultSet.isFirst())
               //    strXML += "1";
               else if(strTempValue.equals(strValue))
               {
                  strXML += "1";
                  blIsInList = true;
               }
               else
                  strXML += "0";
               
               strXML += "\">" + Utilities.cleanForXSL(strTempValue);
               strXML += "</" + strFieldName + ">";
            }
            
            rsResultSet.close();
            rsResultSet = null;
            
            if (blIsInList == false && strValue != null && strValue.length()>0)
            {
               
               strXML += "<" + strFieldName + " selected=\"1\">"+ Utilities.cleanForXSL(strValue) + " </" + strFieldName + ">";
            }
            
            
            if (strValue == null)
               strXML += "<" + strFieldName + "_Selected></" + strFieldName + "_Selected>";
            else
               strXML += "<" + strFieldName + "_Selected>" + Utilities.cleanForXSL(strValue) + "</" + strFieldName + "_Selected>";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         e.printStackTrace();
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   
   public static String buildLOVXMLFromParentWithDescription(String strFieldName, String strValue, String strParentValue ) throws Exception
   {
      String strXML = "";
      //System.out.println("[Query Channel] "+ strFieldName +"\tParentValue="+ strParentValue);
      try
      {
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         String strLOVType = field.getLOVType();
         
         if (strLOVType != null)
         {
            DALQuery query = new DALQuery();
            query.setDomain("LOV", null, null, null);
            Vector vtFields = DatabaseSchema.getFormFields("view_listofvalues");
            query.setFields(vtFields, null);
            query.setWhere(null, 0, "LOV_strLOVParentType", "=", strLOVType, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "LOV_strLOVParentValue", "=", strParentValue, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("LOV_intLOVSortOrder", "ASC");
            
            ResultSet rsResultSet = query.executeSelect();
            
            //System.out.println("[Query Channel] Query" + query.convertSelectQueryToString());
            while (rsResultSet.next())
            {
               String strTempValue = rsResultSet.getString("LOV_strLOVValue");
               
               strXML += "<" + strFieldName + " selected=\"";
               
               if (strValue == null)
                  strXML += "0";
               //if (strValue == null && rsResultSet.isFirst())
               //    strXML += "1";
               else if(strTempValue.equals(strValue))
               {
                  strXML += "1";
               }
               else
                  strXML += "0";
               
               
               strXML += "description=\"" + rsResultSet.getString("LOV_strDescription");
               
               
               strXML += "\">" + Utilities.cleanForXSL(strTempValue);
               strXML += "</" + strFieldName + ">";
            }
            
            rsResultSet.close();
            rsResultSet = null;
            
            if (strValue == null)
               strXML += "<" + strFieldName + "_Selected></" + strFieldName + "_Selected>";
            else
               strXML += "<" + strFieldName + "_Selected>" + Utilities.cleanForXSL(strValue) + "</" + strFieldName + "_Selected>";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   
   
   
   /** Build XML string for search result with date type
    */
   public static String buildSearchXMLFileWithDateType(String strSearchTag, ResultSet rsResultSet,
           Vector vtFields) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      try
      {
         while (rsResultSet.next())
         {
            strXML += "<" + strSearchTag + ">";
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               
               if(strFieldName.endsWith("strType"))
               {
                  if(rsResultSet.getString(strFieldName) != null)
                  {
                     if(rsResultSet.getString(strFieldName).equals("Date"))
                     {
                        strXML += buildDateDropDownXMLFile("Field", null);
                     }
                  }
               }
               
               strXML += "<" + strFieldName + ">";
               
               if (rsResultSet.getString(strFieldName) != null)
               {
                  DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                  if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                     strXML += Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName));
                  else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                     strXML += Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName));
                  else
                     strXML += Utilities.cleanForXSL(rsResultSet.getString(strFieldName));
               }
               strXML += "</" + strFieldName + ">";
            }
            
            strXML += "</" + strSearchTag + ">";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - buildSearchXMLFileWithDateType" + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   /** Build XML string for search result with date type
    */
   public static String buildSearchXMLFileWithDateTypeSet(String strSearchTag, ResultSet rsResultSet, Vector vtFields,  Hashtable hs) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      try
      {
         while (rsResultSet.next())
         {
            strXML += "<" + strSearchTag + ">";
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               
               if(strFieldName.endsWith("strType"))
               {
                  if(rsResultSet.getString(strFieldName) != null)
                  {
                     if(rsResultSet.getString(strFieldName).equals("Date"))
                     {
                        int intStringLength = strFieldName.length();
                        // 7 is the length of strType
                        String strName = "strName";
                        int intLastIndex = intStringLength - strName.length();
                        String strTempName = strFieldName.substring(0,intLastIndex).concat("strName");
                        //System.err.println("strTempName = >>>" + strTempName + "<<<");
                        
                        
                        //System.out.println("rsesultsetval:"+rsResultSet.getString(strTempName));
                        String strVal = (String) hs.get( rsResultSet.getString(strTempName));
                        //System.out.println ("strVal: "+strVal);
                        if(strVal != null)
                        {
                           //System.out.println(rsResultSet.getString(strTempName) + ":" + Utilities.convertDateForDisplay(strVal));
                           strXML += buildDateDropDownXMLFile("Field", Utilities.convertDateForDisplay(strVal));
                        }
                        else
                        {
                           //System.out.println("runtime is null");
                           strXML += buildDateDropDownXMLFile("Field", null);
                        }
                        
                        //System.out.println(rsResultSet.getString(strFieldName) + ":" + Utilities.convertDateForDisplay("2004-05-18"));
                        //strXML += buildDateDropDownXMLFile("Field", Utilities.convertDateForDisplay("2004-05-18"));
                     }
                  }
               }
               
               strXML += "<" + strFieldName + ">";
               
               if (rsResultSet.getString(strFieldName) != null)
               {
                  DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                  if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                     strXML += Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName));
                  else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                     strXML += Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName));
                  else
                     strXML += Utilities.cleanForXSL(rsResultSet.getString(strFieldName));
               }
               strXML += "</" + strFieldName + ">";
            }
            
            strXML += "</" + strSearchTag + ">";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - buildSearchXMLFileWithDateType" + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   /** Build XML string for lov fields
    */
   public static String buildSearchLOVXMLFile(String strFieldName, String strValue) throws Exception
   {
      String strXML = "";
      
      
      try
      {
         
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         String strLOVType = field.getLOVType();
         
         if (strLOVType != null)
         {
            
            strXML += "<LOV_values selected=\"";
            if (strValue == null)
               strXML += "1";
            else
               strXML += "0";
            strXML += "\"></LOV_values>";
            
            DALQuery query = new DALQuery();
            query.setDomain("LOV", null, null, null);
            Vector vtFields = DatabaseSchema.getFormFields("view_listofvalues");
            query.setFields(vtFields, null);
            query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("LOV_intLOVSortOrder", "ASC");
            ResultSet rsResultSet = query.executeSelect();
            while (rsResultSet.next())
            {
               String strTempValue = rsResultSet.getString("LOV_strLOVValue");
               
               
               strXML += "<LOV_values selected=\"";
               
               if(strValue != null && strTempValue != null && strTempValue.equals(strValue))
               {
                  strXML += "1";
               }
               else
               {
                  strXML += "0";
               }
               
               strXML += "\">" + Utilities.cleanForXSL(strTempValue == null ? "" : strTempValue) + "</LOV_values>";
            }
            rsResultSet.close();
            rsResultSet = null;
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   /**
    * Build XML string for search result with Aggregation Functions
    *
    *
    */
   public static String buildSearchXMLFile(String strSearchTag, ResultSet rsResultSet, Vector vtFields, String aField, String aggFunc) throws Exception
   {
      String strXML = "";
      int intNoOfFields = vtFields.size();
      
      try
      {
         while (rsResultSet.next())
         {
            strXML += "<" + strSearchTag + ">";
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               strXML += "<" + strFieldName + ">";
               
               if (rsResultSet.getString(strFieldName) != null)
               {
                  DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                  if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                     strXML += Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName));
                  else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                     strXML += Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName));
                  else
                     strXML += Utilities.cleanForXSL(rsResultSet.getString(strFieldName));
               }
               strXML += "</" + strFieldName + ">";
            }
            
            if( rsResultSet.getString( aggFunc + "_" + aField ) != null )
               strXML += Utilities.cleanForXSL(rsResultSet.getString(aggFunc + "_" + aField));
            
            strXML += "</" + strSearchTag + ">";
         }
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   /** Build XML string for dropdown fields
    */
   public static String buildDropdownFieldXMLFile(String strFieldName, String strValue) throws Exception
   {
      String strXML = "";
      
      try
      {
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         String strDomainName = field.getDomain();
         
         strXML += "<LOV_values selected=\"";
         if (strValue == null)
            strXML += "1";
         else
            strXML += "0";
         strXML += "\"></LOV_values>";
         
         DALQuery query = new DALQuery();
         query.setDomain(strDomainName, null, null, null);
         query.setField(strFieldName, null);
         query.setWhere(null, 0, strDomainName + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         
         if (strFieldName.indexOf("strQuestion") > -1)
            query.setWhere("AND", 0, "SURVEY QUESTIONS_intQuestionType", "<", "100", 0, DALQuery.WHERE_HAS_VALUE);
         
         ResultSet rsResultSet = query.executeSelect();
         while (rsResultSet.next())
         {
            String strTempValue = rsResultSet.getString(strFieldName);
            
            strXML += "<LOV_values selected=\"";
            
            if(strValue != null && strTempValue.equals(strValue))
            {
               strXML += "1";
            }
            else
               strXML += "0";
            
            strXML += "\">" + Utilities.cleanForXSL(strTempValue) + "</LOV_values>";
         }
         rsResultSet.close();
         rsResultSet = null;
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   /** Build XML string for search result
    */
   public static String buildSearchXMLFile(String strSearchTag, ResultSet rsResultSet, Vector vtFields) throws Exception
   {
      StringBuffer strXML = new StringBuffer();
      int intNoOfFields = vtFields.size();
      String strTempData = null;
      
      
      try
      {
         while (rsResultSet.next())
         {
            //strXML += "<" + strSearchTag + ">";
            strXML.append("<" + strSearchTag + ">");
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               //strXML += "<" + strFieldName + ">";
               strXML.append("<" + strFieldName + ">");
               
               strTempData = rsResultSet.getString(strFieldName);
               if (strTempData != null)
               {
                  DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                  if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                     //strXML += Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName));
                     strXML.append(Utilities.convertDateForDisplay(strTempData));
                  else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                     //strXML += Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName));
                     strXML.append(Utilities.convertTimeForDisplay(strTempData));
                  else
                     //strXML += Utilities.cleanForXSL(rsResultSet.getString(strFieldName));
                     strXML.append(Utilities.cleanForXSL(strTempData));
               }
               //strXML += "</" + strFieldName + ">";
               strXML.append("</" + strFieldName + ">");
            }
            
            //strXML += "</" + strSearchTag + ">";
            strXML.append("</" + strSearchTag + ">");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML.toString();
   }
   /** Build XML string from resultset without search tags for the current item in the resultSet
    */
   public static String buildViewXMLForCurrentRecord(ResultSet rsResultSet, Vector vtFields) throws Exception
   {
      StringBuffer strXML = new StringBuffer();
      int intNoOfFields = vtFields.size();
      String strTempData = null;
      
      
      try
      {
         // while (rsResultSet.next()) {
         for (int i=0; i < intNoOfFields; i++)
         {
            String strFieldName = (String) vtFields.get(i);
            //strXML += "<" + strFieldName + ">";
            DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            strXML.append("<" + strFieldName + ">");
            strTempData = rsResultSet.getString(strFieldName);
            
            if (strTempData != null)
            {
               DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
               if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                  strXML.append(Utilities.convertDateForDisplay(strTempData));
               else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                  //strXML += Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName));
                  strXML.append(Utilities.convertTimeForDisplay(strTempData));
               else
                  //strXML += Utilities.cleanForXSL(rsResultSet.getString(strFieldName));
                  strXML.append(Utilities.cleanForXSL(strTempData));
            }
            //strXML += "</" + strFieldName + ">";
            strXML.append("</" + strFieldName + ">");
            //   }
            }
            //strXML += "</" + strSearchTag + ">";
            
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML.toString();
   }
   
   
   
   
   
   
   
   
   
   
   
   
   /** Build XML string and lock records for search result
    */
   public static String buildSearchXMLFileAndLockRecord(String strSearchTag, ResultSet rsResultSet, Vector vtFields, String strDomain, String strPrimaryKey, LockRequest lockRequest, int intLockType) throws Exception
   {
      String strXML = "";
      String strPrimaryKeyData = "";
      
      int intNoOfFields = vtFields.size();
      
      try
      {
         while (rsResultSet.next())
         {
            // add lock record
            strPrimaryKeyData = rsResultSet.getString(strPrimaryKey);
            lockRequest.addLock(strDomain, strPrimaryKeyData, intLockType);
            
            
            strXML += "<" + strSearchTag + ">";
            
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               
               strXML += "<" + strFieldName + ">";
               
               String strFieldNameResult = "";
               if (strFieldName.equals(strPrimaryKey))
                  strFieldNameResult = strPrimaryKeyData;
               else
                  strFieldNameResult = rsResultSet.getString(strFieldName);
               
               if (strFieldNameResult != null)
               {
                  DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                  if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                     strXML += Utilities.convertDateForDisplay(strFieldNameResult);
                  else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                     strXML += Utilities.convertTimeForDisplay(strFieldNameResult);
                  else
                     strXML += Utilities.cleanForXSL(strFieldNameResult);
               }
               strXML += "</" + strFieldName + ">";
            }
            
            strXML += "</" + strSearchTag + ">";
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   
   
   //rennypv overloaded to introduce field level security
   
   public static String buildSearchXMLFileAndLockRecord(String strSearchTag, ResultSet rsResultSet, Vector vtFields, String strDomain, String strPrimaryKey, LockRequest lockRequest, int intLockType,AuthToken authToken) throws Exception
   {
      String strXML = "";
      String strPrimaryKeyData = "";
      
      int intNoOfFields = vtFields.size();
      
      try
      {
         while (rsResultSet.next())
         {
            
            // add lock record
            strPrimaryKeyData = rsResultSet.getString(strPrimaryKey);
            lockRequest.addLock(strDomain, strPrimaryKeyData, intLockType);
            
            
            strXML += "<" + strSearchTag + ">";
            
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               
               strXML += "<" + strFieldName + ">";
               
               String strFieldNameResult = "";
               if (strFieldName.equals(strPrimaryKey))
                  strFieldNameResult = strPrimaryKeyData;
               else
                  strFieldNameResult = rsResultSet.getString(strFieldName);
               if(authToken.hasDenyActivity(strFieldName))
               {
                  strXML += "######";
               }
               else if (strFieldNameResult != null)
               {
                  DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                  if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                     strXML += Utilities.convertDateForDisplay(strFieldNameResult);
                  else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                     strXML += Utilities.convertTimeForDisplay(strFieldNameResult);
                  else
                     strXML += Utilities.cleanForXSL(strFieldNameResult);
               }
               strXML += "</" + strFieldName + ">";
            }
            
            strXML += "</" + strSearchTag + ">";
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML;
   }
   //end rennypv
   /** Return the newest value of a primary key field
    *  @deprecated Not thread safe
    */
   public static int getNewestKeyAsInt(String strFieldName) throws Exception
   {
      int intResult = -1;
      DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
      String strDomain = field.getDomain();
      
      try
      {
         DALQuery query = new DALQuery();
         query.setDomain(strDomain, null, null, null);
         query.setMaxField(strFieldName);
         
         ResultSet rsResultSet = query.executeSelect();
         
         if (rsResultSet.next())
            intResult = rsResultSet.getInt(1);
         rsResultSet.close();
         
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return intResult;
   }
   
   /** Return the newest value of a primary key field
    */
   public static int getNewestKeyAsInt(DALQuery query) throws Exception
   {
      return query.getInsertedRecordKey();
   }
   
   /** Return the newest value of a primary key field
    */
   public static int getNewestKeyAsInt(DALSecurityQuery query) throws Exception
   {
      return query.getInsertedRecordKey();
   }
   
   /** Return the newest value of a primary key field
    * @deprecated Not thread safe
    */
   public static String getNewestKeyAsString(String strFieldName) throws Exception
   {
      String strResult = "";
      DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
      String strDomain = field.getDomain();
      
      Exception legacyFunction = new Exception();
      System.err.println("[QueryChannel] Warning - Legacy Function Called");
      legacyFunction.printStackTrace(System.err);
      
      try
      {
         DALQuery query = new DALQuery();
         query.setDomain(strDomain, null, null, null);
         query.setMaxField(strFieldName);
         
         ResultSet rsResultSet = query.executeSelect();
         
         if (rsResultSet.next())
            strResult = rsResultSet.getString(1);
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strResult;
   }
   
   /** Return the newest value of a primary key field
    */
   public static String getNewestKeyAsString(DALQuery query) throws Exception
   {
      return String.valueOf(query.getInsertedRecordKey());
   }
   
   /** Return the newest value of a primary key field
    */
   public static String getNewestKeyAsString(DALSecurityQuery query) throws Exception
   {
      return String.valueOf(query.getInsertedRecordKey());
   }
   
   
   /** Build XML string for date, regardless of type text or dropdown
    */
   public static String buildDateXMLFile(DBField field, String strFieldName, String strDate)
   {
      String strXML="";
      if (field == null) return strXML;
      
      if (field.getDisplayType().equalsIgnoreCase("dropdown"))
      {
         strXML += buildDateDropDownXMLFile(strFieldName, strDate);
      }
      else if (field.getDisplayType().equalsIgnoreCase("text"))
      {
         strXML += buildDateTextXMLFile(strFieldName, strDate);
      }
      return strXML;
      
   }
   /** Build XML for date if the input type is of text boxes
    */
   public static String buildDateTextXMLFile(String strFieldName, String strDate)
   {
      String strXML = "";
      int day, month;
      String strday ="";
      String strmonth="";
      String year;
      
      //System.out.println("StrDate is: "+ strDate);
      
      if (strDate != null)
      {
         
         
         strXML += "<" + strFieldName + " display_type=\"text\"" + ">" + strDate + "</" + strFieldName + ">";
         if (strDate.length() == 0)
         {
            day = 0;
            month = 0;
            year = "";
         }
         else if(strDate.startsWith("#"))
         {
            strday = "##";
            strmonth = "##";
            day = 0;
            month=0;
            year = "##";
         }
         else
         {
            try
            {
               day = Integer.parseInt(strDate.substring(0,2));
               month = Integer.parseInt(strDate.substring(3,5));
               year = strDate.substring(6);
            }
            catch (NumberFormatException nfe)
            {
               LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel:buildDateTextXMLFile - " + nfe.toString(), nfe);
               day = 0;
               month = 0;
               year = "";
            }
         }
      }
      else
      {
         strXML += "<" + strFieldName + " display_type=\"text\"" + "></" + strFieldName + ">";
         //System.out.println(strXML);
         java.util.Date today = new java.util.Date();
         GregorianCalendar calendar = new GregorianCalendar();
         calendar.setTime(today);
         day = calendar.get(Calendar.DATE);
         month = calendar.get(Calendar.MONTH) + 1;
         year = new Integer(calendar.get(Calendar.YEAR)).toString();
      }
      
      
      //System.out.println(strXML);
      
      if (day<=9 && day>0)
         strXML += "<" + strFieldName + "_Day>0" + day + "</" + strFieldName + "_Day>";
      else if (day >9)
         strXML += "<" + strFieldName + "_Day>" + day + "</" + strFieldName + "_Day>";
      else if(strday != null && strday.length() > 0)
         strXML += "<" + strFieldName + "_Day>" + strday + "</" + strFieldName + "_Day>";
      else strXML += "<" + strFieldName + "_Day>"  + "</" + strFieldName + "_Day>";
      
      if (month<=9 && month>0)
         strXML += "<" + strFieldName + "_Month>0" + month + "</" + strFieldName + "_Month>";
      else if (month > 9)
         strXML += "<" + strFieldName + "_Month>" + month + "</" + strFieldName + "_Month>";
      else if(strmonth != null && strmonth.length() > 0)
         strXML += "<" + strFieldName + "_Month>" + strmonth + "</" + strFieldName + "_Month>";
      else strXML += "<" + strFieldName + "_Month>" + "</" + strFieldName + "_Month>";
      
      
      strXML += "<" + strFieldName + "_Year>" + year + "</" + strFieldName + "_Year>";
      return strXML;
      
   }
   
   /** Build XML string for date type fields
    */
   public static String buildDateDropDownXMLFile(String strFieldName, String strDate)
   {
      String strXML = "";
      int day, month;
      String year;
      //strXML += "<display_type>dropdown</display_type>";
      if(strDate != null && strDate.startsWith("#"))
      {
         strXML += "<" + strFieldName + " display_type=\"dropdown\"" +">" + strDate + "</" + strFieldName + ">";
         strXML += "<" + strFieldName + "_Day selected=\"1\">";
         strXML += strDate + "</" + strFieldName + "_Day>";
         strXML += "<" + strFieldName + "_Month selected=\"1\">";
         strXML += strDate + "</" + strFieldName + "_Month>";
         strXML += "<" + strFieldName + "_Year>" + strDate + "</" + strFieldName + "_Year>";
      }
      else
      {
         if (strDate != null)
         {
            
            strXML += "<" + strFieldName + " display_type=\"dropdown\"" +">" + strDate + "</" + strFieldName + ">";
            if (strDate.length() == 0)
            {
               day = 0;
               month = 0;
               year = "";
            }
            else
            {
               day = Integer.parseInt(strDate.substring(0,2));
               month = Integer.parseInt(strDate.substring(3,5));
               year = strDate.substring(6);
            }
         }
         else
         {
            strXML += "<" + strFieldName + " display_type=\"dropdown\"" + "></" + strFieldName + ">";
            java.util.Date today = new java.util.Date();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            day = calendar.get(Calendar.DATE);
            month = calendar.get(Calendar.MONTH) + 1;
            year = new Integer(calendar.get(Calendar.YEAR)).toString();
         }
         
         
         //  Build up the Day */
         for (int i=1; i < 10; i++)
         {
            strXML += "<" + strFieldName + "_Day selected=\"";
            if (i == day)
               strXML += "1\">";
            else
               strXML += "0\">";
            // leading zero is needed
            strXML += "0" + i + "</" + strFieldName + "_Day>";
         }
         
         for (int i=10; i < 32; i++)
         {
            strXML += "<" + strFieldName + "_Day selected=\"";
            if (i == day)
               strXML += "1\">";
            else
               strXML += "0\">";
            
            strXML += i + "</" + strFieldName + "_Day>";
         }
         
         //Build up the Month
         for (int i=1; i < 10; i++)
         {
            strXML += "<" + strFieldName + "_Month selected=\"";
            if (i == month)
               strXML += "1\">";
            else
               strXML += "0\">";
            // leading zeros is needed
            strXML +="0" + i + "</" + strFieldName + "_Month>";
         }
         
         for (int i=10; i < 13; i++)
         {
            strXML += "<" + strFieldName + "_Month selected=\"";
            if (i == month)
               strXML += "1\">";
            else
               strXML += "0\">";
            
            strXML += i + "</" + strFieldName + "_Month>";
         }
         // year
         strXML += "<" + strFieldName + "_Year>" + year + "</" + strFieldName + "_Year>";
      }
      //System.out.println("Build XML from Drop box:" + strXML);
      return strXML;
   }
   
   
   /** Build XML string for time type fields
    */
   public static String buildTimeDropDownXMLFile(String strFieldName, String strTime)
   {
      StringBuffer strXML = new StringBuffer();
      
      if (strTime == null)
      {
         strXML.append("<" + strFieldName + "></" + strFieldName + ">");
         for (int i=1; i < 10; i++)
            strXML.append("<" + strFieldName + "_Hour selected=\"0\">0" + i + "</" + strFieldName + "_Hour>");
         
         for (int i=10; i < 13; i++)
            strXML.append("<" + strFieldName + "_Hour selected=\"0\">" + i + "</" + strFieldName + "_Hour>");
         
         for (int i=0; i < 10; i++)
            strXML.append("<" + strFieldName + "_Minute selected=\"0\">0" + i + "</" + strFieldName + "_Minute>");
         
         for (int i=10; i < 60; i++)
            strXML.append("<" + strFieldName + "_Minute selected=\"0\">" + i + "</" + strFieldName + "_Minute>");
         
         strXML.append("<" + strFieldName + "_AMPM selected=\"0\">AM</" + strFieldName + "_AMPM>");
         strXML.append("<" + strFieldName + "_AMPM selected=\"0\">PM</" + strFieldName + "_AMPM>");
      }
      else
      {
         strXML.append("<" + strFieldName + ">" + strTime + "</" + strFieldName + ">");
         int intIndexOfColon = strTime.indexOf(":");
         int hour = Integer.parseInt(strTime.substring(0,intIndexOfColon));
         int minute = Integer.parseInt(strTime.substring(intIndexOfColon + 1, intIndexOfColon + 3));
         String strAMPM = strTime.substring(intIndexOfColon + 4);
         
         for (int i=0; i < 10; i++)
         {
            if (i == hour)
               strXML.append("<" + strFieldName + "_Hour selected=\"1\">0" + i + "</" + strFieldName + "_Hour>");
            else
               strXML.append("<" + strFieldName + "_Hour selected=\"0\">0" + i + "</" + strFieldName + "_Hour>");
         }
         
         for (int i=10; i < 13; i++)
         {
            if (i == hour)
               strXML.append("<" + strFieldName + "_Hour selected=\"1\">" + i + "</" + strFieldName + "_Hour>");
            else
               strXML.append("<" + strFieldName + "_Hour selected=\"0\">" + i + "</" + strFieldName + "_Hour>");
         }
         
         for (int i=0; i < 10; i++)
         {
            if (i == minute)
               strXML.append("<" + strFieldName + "_Minute selected=\"1\">0" + i + "</" + strFieldName + "_Minute>");
            else
               strXML.append("<" + strFieldName + "_Minute selected=\"0\">0" + i + "</" + strFieldName + "_Minute>");
         }
         
         for (int i=10; i < 60; i++)
         {
            if (i == minute)
               strXML.append("<" + strFieldName + "_Minute selected=\"1\">" + i + "</" + strFieldName + "_Minute>");
            else
               strXML.append("<" + strFieldName + "_Minute selected=\"0\">" + i + "</" + strFieldName + "_Minute>");
         }
         
         if (strAMPM.equals("AM"))
         {
            strXML.append("<" + strFieldName + "_AMPM selected=\"1\">AM</" + strFieldName + "_AMPM>");
            strXML.append("<" + strFieldName + "_AMPM selected=\"0\">PM</" + strFieldName + "_AMPM>");
         }
         else
         {
            strXML.append("<" + strFieldName + "_AMPM selected=\"0\">AM</" + strFieldName + "_AMPM>");
            strXML.append("<" + strFieldName + "_AMPM selected=\"1\">PM</" + strFieldName + "_AMPM>");
         }
      }
      
      return strXML.toString();
   }
   
   /**
    *    Checks to ensure required fields have values
    */
   public static String validateFields(Vector vtFields, ChannelRuntimeData rdData)
   {
      String strErr = null;
      int intNoOfFields = vtFields.size();
      
      for (int i=0; i < intNoOfFields; i++)
      {
         String strFieldName = (String) vtFields.get(i);
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         // if the field is a required field
         if (field.isRequired() == true && (rdData.getParameter(strFieldName) == null || rdData.getParameter(strFieldName).equals("")))
         {
            strErr = "Please fill in the required field - " + field.getLabelInForm() + ".";
            break;
         }
      }
      return strErr;
   }
   
   /** Check data validation. If all data is OK return null, otherwise return
    *  an error message.
    */
   public static String validateData(Vector vtFields, ChannelRuntimeData rd)
   {
      
      //System.err.println("Performing validation...");
      
      String strResult = "Please enter a valid data for the field: ";
      //hashUniqueFields = new Hashtable(50);
      //hashUniqueFields = (DBField) DatabaseSchema.getUniqueFields();
      
      for (int i=0; i < vtFields.size(); i++)
      {
         String strFieldName = (String) vtFields.get(i);
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         //System.err.println(strFieldName);
         
         if ( (rd.getParameter(strFieldName) != null) && (rd.getParameter(strFieldName).trim().length() > 0))
         {
            // Validate unique field
            //System.err.println(strFieldName + " "  +field.isUnique());
            
            
                /* Can't check for uniqueness here because you don't know
                 * whether it is an insert (i.e no existing record) or
                 * update (i.e one exisiting record).
                 * New method was created.
                 */
            
            
            // Validate data type of field
            switch (field.getDataType())
            {
               case DBMSTypes.INT_TYPE:
                  if (!Utilities.validateIntValue(rd.getParameter(strFieldName)))
                     return strResult + field.getLabelInForm();
                  
                  break;
                  
               case DBMSTypes.FLOAT_TYPE:
                  if (!Utilities.validateFloatValue(rd.getParameter(strFieldName)))
                     return strResult + field.getLabelInForm();
                  
                  break;
                  
               case DBMSTypes.DOUBLE_TYPE:
                  if (!Utilities.validateFloatValue(rd.getParameter(strFieldName)))
                     return strResult + field.getLabelInForm();
                  
                  break;
                  
               case DBMSTypes.DATE_TYPE:
                  if (!Utilities.validateDateValue(rd.getParameter(strFieldName)))
                     return strResult + field.getLabelInForm();
                  
                  break;
                  
               case DBMSTypes.TIME_TYPE:
                  if (!Utilities.validateTimeValue(rd.getParameter(strFieldName)))
                     return strResult + field.getLabelInForm();
                  
                  break;
            } // switch
            //Anton: validate against the patter specified in the schema
            if (field.getValidationPattern()!=null)
            {
               if (!rd.getParameter(strFieldName).matches(field.getValidationPattern()))
               {
                  return strResult + field.getLabelInForm() ;
               }
            }//if - patter validation
         } // if
      } // for
      return null;
   } // method
   
   /** Check data for required fields. If all required fields have data return null, otherwise return
    *  an error message.
    */
   public static String checkRequiredFields(Vector vtFields, ChannelRuntimeData rd)
   {
      String strResult = "Please check the following required fields: ";
      boolean flag = false;
      for (int i=0; i < vtFields.size(); i++)
      {
         String strFieldName = (String) vtFields.get(i);
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         if (field.isRequired())
         {
            if (rd.getParameter(strFieldName) == null)
            {
               strResult += field.getLabelInForm() + ", ";
               flag=true;
            }
            else if (rd.getParameter(strFieldName).trim().equals(""))
            {
               strResult += field.getLabelInForm() + ", ";
               flag=true;
            }
            
         }
         
         // Is this supposed to be commented Huy ?????
         
         //if (field.isRequired() && (rd.getParameter(strFieldName) == null || rd.getParameter(strFieldName).trim().equals("")))
         //    strResult += field.getLabelInForm() + ", ";
      }
      
      if (!flag)
         return null;
      
      return strResult.substring(0, strResult.length() - 2);
   }
   /** Check for duplicates when insert
    */
   public static String checkDuplicates(Vector vtFields, ChannelRuntimeData rd)
   {
      
      
      String strResult = null;
      
      for (int i=0; i < vtFields.size(); i++)
      {
         String strFieldName = (String) vtFields.get(i);
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         
         //System.err.println(strFieldName);
         
         if (rd.getParameter(strFieldName) != null)
         {
            
            // Include all deleted records in validation
            boolean blIncludeDeleted = false;
            
            if (field.isUnique())
            {
               try
               {
                  DALQuery query = new DALQuery();
                  query.setDomain(field.getDomain(), null, null, null);
                  query.setFields(vtFields, null);
                  query.setWhere(null, 0, strFieldName, "=", rd.getParameter(strFieldName), 0, DALQuery.WHERE_HAS_VALUE);
                  query.setWhere("AND", 0, field.getDomain() + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                  
                  ResultSet rsResultSet = query.executeSelect();
                  //System.err.println("Validation Check for " + rd.getParameter(strFieldName));
                  
                  // first record and is an insert action
                  //if (blIsInsert) {
                  if (rsResultSet.next())
                  {
                     
                     //System.err.println("strResult=" + strResult);
                     if (strResult == null)
                     {
                        strResult = "Duplicate value detected for field(s) " +field.getLabelInForm() + " ";
                     }
                     else
                        strResult = strResult + "," +field.getLabelInForm();
                     
                  }
                  
                  rsResultSet.close();
                  rsResultSet = null;
                  //} // Can't detect unique fields for updates because you could update another record.
                        /*
                        else {
                              if ( !rsResultSet.next() ) {   // second row test for updates
                                if (strResult == null)
                                    strResult = "Duplicate value detected for field(s) " +field.getLabelInForm();
                                else
                                    strResult = strResult + "," +field.getLabelInForm();
                                System.err.println("UPDATE"  + strResult);
                                return  strResult;
                              }
                        } */
               }
               catch (Exception e)
               {
                  LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
                  //throw new Exception("Unknown error in QueryChannel - " + e.toString());
               }
            }
            //System.err.println("Passed dup test");
            
         } // if
      } // for
      
      //System.err.println("INSERT"  + strResult);
      return  strResult;
      
      
   }
   
   /** Check for duplicates when update
    */
   public static String checkDuplicatesWhenUpdate(Vector vtFields, ChannelRuntimeData rd, String primaryField, String primaryValue )
   {
      
      
      String strResult = null;
      
      for (int i=0; i < vtFields.size(); i++)
      {
         String strFieldName = (String) vtFields.get(i);
         DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
         
         
         //System.err.println(strFieldName);
         
         if (rd.getParameter(strFieldName) != null)
         {
            
            // Include all deleted records in validation
            boolean blIncludeDeleted = false;
            
            if (field.isUnique())
            {
               try
               {
                  DALQuery query = new DALQuery();
                  query.setDomain(field.getDomain(), null, null, null);
                  query.setFields(vtFields, null);
                  query.setWhere(null, 0, strFieldName, "=", rd.getParameter(strFieldName), 0, DALQuery.WHERE_HAS_VALUE);
                  query.setWhere("AND", 0, field.getDomain() + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                  query.setWhere("AND", 0, primaryField, "<>", primaryValue, 0, DALQuery.WHERE_HAS_VALUE);
                  
                  
                  ResultSet rsResultSet = query.executeSelect();
                  //System.err.println("Validation Check for " + rd.getParameter(strFieldName));
                  
                  // first record and is an insert action
                  //if (blIsInsert) {
                  if (rsResultSet.next())
                  {
                     
                     //System.err.println("strResult=" + strResult);
                     if (strResult == null)
                     {
                        strResult = "Duplicate value detected for field(s) " +field.getLabelInForm() + " ";
                     }
                     else
                        strResult = strResult + "," +field.getLabelInForm();
                     
                  }
                  
                  rsResultSet.close();
                  rsResultSet = null;
                  //} // Can't detect unique fields for updates because you could update another record.
                        /*
                        else {
                              if ( !rsResultSet.next() ) {   // second row test for updates
                                if (strResult == null)
                                    strResult = "Duplicate value detected for field(s) " +field.getLabelInForm();
                                else
                                    strResult = strResult + "," +field.getLabelInForm();
                                System.err.println("UPDATE"  + strResult);
                                return  strResult;
                              }
                        } */
               }
               catch (Exception e)
               {
                  LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
                  //throw new Exception("Unknown error in QueryChannel - " + e.toString());
               }
            }
            //System.err.println("Passed dup test");
            
         } // if
      } // for
      
      //System.err.println("INSERT"  + strResult);
      return  strResult;
      
      
   }
   
   
   /** Check if the date passed is greater than todays date
    *  @param field name of the date to be checked
    *  @param runtimeData
    *  returns true if date is greater than todays date
    *  returns false if date is not greater than todays date
    *  returns null if date is blank
    */
   public static String checkDate(String strDateFieldName, ChannelRuntimeData runtimeData)
   {
      String strFlag = "false";
      
      // if date is blank
      if(runtimeData.getParameter(strDateFieldName) == null || runtimeData.getParameter(strDateFieldName).equals(""))
      {
         strFlag = null;
         
      }
      else
      {
         
         Calendar dtDob = Calendar.getInstance();
         Calendar dtToday = Calendar.getInstance();
         
         dtDob.set( Integer.parseInt(runtimeData.getParameter(strDateFieldName+"_Year")), Integer.parseInt(runtimeData.getParameter(strDateFieldName+"_Month")), Integer.parseInt(runtimeData.getParameter(strDateFieldName+"_Day")));
         // set todays date
         dtToday.set( Integer.parseInt(Utilities.getDateTimeStampAsString("yyyy")), Integer.parseInt(Utilities.getDateTimeStampAsString("MM")), Integer.parseInt(Utilities.getDateTimeStampAsString("dd")));
         
         if(dtDob.after(dtToday))
         {
            strFlag = "true";
         }
         else
         {
            strFlag = "false";
         }
      }
      
      return strFlag;
   }
   
   
   public static String validateDates(String strDateStartFieldName, String strDateEndFieldName,ChannelRuntimeData runtimeData)
   {
      String strFlag = "true";
      
      // if date is blank
      if(runtimeData.getParameter(strDateStartFieldName) == null || runtimeData.getParameter(strDateEndFieldName) == null || runtimeData.getParameter(strDateStartFieldName).equals("") || runtimeData.getParameter(strDateEndFieldName).equals(""))
      {
         strFlag = "true";
         
      }
      else
      {
         if (!Utilities.validateDateValue(runtimeData.getParameter(strDateStartFieldName))
         || !Utilities.validateDateValue(runtimeData.getParameter(strDateEndFieldName)))
         {
            strFlag = "false";
            return strFlag;
         }
         // correct the format into standard dd/MM/yyyy
         try
         {
            
            String strDateStart = makeCorrectDateFormat(runtimeData.getParameter(strDateStartFieldName));
            String strDateEnd = makeCorrectDateFormat(runtimeData.getParameter(strDateEndFieldName));
            
            //System.out.println("strDateStart: " + strDateStart);
            //System.out.println("strDateEnd: " +strDateEnd);
            
            // Set the standard date format back to runtimeData
            
            runtimeData.setParameter(strDateStartFieldName,strDateStart);
            runtimeData.setParameter(strDateEndFieldName,strDateEnd);
            
            // make date objects to compares
            Date startDate = Utilities.convertStringToDate(strDateStart,"dd/MM/yyyy");
            Date EndDate = Utilities.convertStringToDate(strDateEnd,"dd/MM/yyyy");
            
            if(startDate.after(EndDate))
            {
               strFlag = "false";
            }
            else
            {
               strFlag = "true";
            }
         }
         
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      return strFlag;
   }
   
   
   public static Hashtable convertCurrentRecordToHashtable(Vector vtFields, ResultSet rs)
   {
      Hashtable htReturnData = new Hashtable();
      DBField field = null;
      if (rs == null)
         return null;
      try
      {
         for (int i = 0; i < vtFields.size(); i++)
         {
            String strFieldName = (String) vtFields.get(i);
            String strValue = rs.getString(strFieldName);
            field = (DBField) DatabaseSchema.getFields().get(strFieldName);
            
            
            if (strValue != null)
            {
               
               if (field.getDataType() == DBMSTypes.DATE_TYPE )
               {
                  
                  htReturnData.put(strFieldName, Utilities.convertDateForDisplay(strValue));
                  
               }
               else htReturnData.put(strFieldName, strValue);
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return htReturnData;
      
   }
   
   /** Return the max value of a primary key field
    */
   public static String getMaxKeyAsString(String strFieldName) throws Exception
   {
      String strResult = "";
      DBField field = (DBField) DatabaseSchema.getFields().get(strFieldName);
      String strDomain = field.getDomain();
      
      try
      {
         DALQuery query = new DALQuery();
         query.setDomain(strDomain, null, null, null);
         query.setMaxField(strFieldName);
         
         ResultSet rsResultSet = query.executeSelect();
         
         if (rsResultSet.next())
            strResult = rsResultSet.getString(1);
      }
      catch (Exception e)
      {
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strResult;
   }
   
   
   public static String getFirstItemInLOVs(String strFieldName, String strParentValue)
   {
      String result = null;
      
      //	try to get the default sample value
      DBField field = (DBField)DatabaseSchema.getFields().get(strFieldName);
      
      String strLOVType = field.getLOVType();
      // get the first LOV of type strLOVType
      if (strLOVType != null)
      {
         DALSecurityQuery query = new DALSecurityQuery();
         try
         {
            query.setDomain("LOV", null, null,null);
            query.setField("LOV_strLOVValue", null);
            query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0,  DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "LOV_intDeleted", "=", 0+"", 0,  DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "LOV_intLOVSortOrder", "=", 1+"", 0,  DALQuery.WHERE_HAS_VALUE);
            if (strParentValue != null && strParentValue.length() > 0)
               query.setWhere("AND", 0, "LOV_strLOVParentValue", "=",strParentValue, 0,  DALQuery.WHERE_HAS_VALUE);
            
            ResultSet rs = query.executeSelect();
            if (rs.first())
            {
               return rs.getString("LOV_strLOVValue");
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      return null;
   }
   
   
   public static String buildSearchXMLFile(String strSearchTag, ResultSet rsResultSet, Vector vtFields, String strSelectedValue) throws Exception
   {
      StringBuffer strXML = new StringBuffer();
      int intNoOfFields = vtFields.size();
      String strTempData = null;
      
      
      try
      {
         while (rsResultSet.next())
         {
            //strXML += "<" + strSearchTag + ">";
            //strTempData = rsResultSet.getString(strFieldName);
            strXML.append("<" + strSearchTag + ">");
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               //strXML += "<" + strFieldName + ">";
               strXML.append("<" + strFieldName + ">");
               strTempData = rsResultSet.getString(strFieldName);
               
               
               
               
               if (strTempData != null)
               {
                  DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                  if (currentField.getDataType() == DBMSTypes.DATE_TYPE)
                     //strXML += Utilities.convertDateForDisplay(rsResultSet.getString(strFieldName));
                     strXML.append(Utilities.convertDateForDisplay(strTempData));
                  else if (currentField.getDataType() == DBMSTypes.TIME_TYPE)
                     //strXML += Utilities.convertTimeForDisplay(rsResultSet.getString(strFieldName));
                     strXML.append(Utilities.convertTimeForDisplay(strTempData));
                  else
                     //strXML += Utilities.cleanForXSL(rsResultSet.getString(strFieldName));
                     strXML.append(Utilities.cleanForXSL(strTempData));
               }
               //strXML += "</" + strFieldName + ">";
               strXML.append("</" + strFieldName + ">");
               if (strTempData.equals(strSelectedValue))
               {
                  strXML.append("<selected>true</selected>");
               }
            }
            
            //strXML += "</" + strSearchTag + ">";
            strXML.append("</" + strSearchTag + ">");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         LogService.instance().log(LogService.ERROR, "Unknown error in QueryChannel - " + e.toString(), e);
         throw new Exception("Unknown error in QueryChannel - " + e.toString());
      }
      
      return strXML.toString();
   }
}


