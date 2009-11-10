/*

 * BaseChannel.java

 *

 * Created on November 14, 2002, 10:09 AM

 */



package neuragenix.common;

// A channel needs these eight classes no matter what:



import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.ChannelRuntimeProperties;

import org.jasig.portal.PortalEvent;

import org.jasig.portal.PortalException;

import org.jasig.portal.security.*;

import org.jasig.portal.services.LogService;

import neuragenix.common.Utilities;

import neuragenix.dao.DBSchema;

import java.util.Hashtable;

import java.util.Vector;

import java.util.Enumeration;

import neuragenix.dao.*;

import neuragenix.security.*;

import java.sql.*;

import java.io.File;

import java.io.InputStream;

import java.io.FileOutputStream;

import neuragenix.common.exception.*;

import org.jasig.portal.PropertiesManager;

import neuragenix.utils.Password;

import java.util.Calendar;

import neuragenix.utils.WebServiceAccessor;



/**

 * This channel is the base channel for all Neuragenix channels.

 *

 * It main purpose is:

 * 1. To seperate the Database activities from the channels

 * 2. To reuse common methods to all channels

 * 3. To ensure consistency in Neuragenix channel production

 *

 * To use BaseChannel there are some typical activities that are usually required

 * to be preformed by all channels using it:

 * 1. Set the Domains you are dealing with (setDomain)

 * 2. To give the BaseChannel the form fields you are dealing with (setFormFields). This

 * contains a hashTable that contains a list of the internal field name and it's extranal description

 * 3. Set an where conditions (setWhere)

 * 4. Set an OrderBy conditions (setOrderBy)

 * 5. To give the BaseChannel the runtimeData (web form data) from the channel so it can manipulate the

 * data for database activities

 * 6. Run specific methods depending on what you want to do (addRecord, updateRecord)

 * 7. Once all operations are complete you should get the formatted XML for the XSL (getXMLFile)

 *

 * @author Hayden Molnar

 */

public class BaseChannel {



    /** Token used for security

     */    

    protected AuthToken authToken;

    /** Used for security permissions

     */    

    protected String strActivityRequested;

    /** Holds the form fields the channel is manipulating

     */    

    protected Hashtable hashFormFields;

    /** The string used to hold the Error String

     */    

    protected String strLastError;

    /** The string used to hold the XML File format

     */    

    protected String strXML;

    /** The runtimeData used to get the form data

     */    

    protected ChannelRuntimeData runtimeData;

    /** Contains the DBSchema field types for each

     * field for the system

     *

     */    

    protected Hashtable hashDBFieldTypes;

    /** Holds the drop down form fields to build

     */    

    protected Hashtable hashFormFieldDropDown;

    private Hashtable hashSearchFieldOperators;

    private Hashtable hashValidateFields;

    /** Holds the fields to reduce in size

     */    

    protected Hashtable hashCutFields;

    private Hashtable hashMaxFields = new Hashtable(5, 2);

    /** Holds the form fields the channel wants to hide

     */    

    protected Hashtable hashHideFormFields = new Hashtable();

    /** Holds the domains

     */    

    protected Vector vecDomains = new Vector(3, 2);

    /** Holds the where fields for the qhere condition

     */    

    protected Vector vecWhereFields = new Vector(5, 2);  

    /** Holds the where values for the where fields

     */    

    protected Vector vecWhereFieldValue = new Vector(5, 2);    

    /** Holds the where operators for the where condition

     */    

    protected Vector vecWhereOperator = new Vector(5, 2);  

    /** Holds the where connectors for the where condition for multiple where

     * statements

     */    

    protected Vector vecWhereConnector = new Vector(5, 2);

    private Vector vecErrorInvalidData = new Vector(5, 2);

    protected Vector vecOpenWhereBracket = new Vector(5,2);

    

    protected Vector vecCloseWhereBracket = new Vector(5,2);

    

    protected Vector vecWhereBracketConnector = new Vector(5,2);

    /** Holds the order by fields for the order by condition

     */    

    protected Vector vecOrderByFields = new Vector(5, 2);

    /** Holds the order by domains for the order by condition

     */    

    protected Vector vecOrderByFieldDirection = new Vector(5, 2);

    private boolean blRequiredOK = true;

    private boolean blValidFieldsOK = true;

    private boolean blDropDownsBuild = false;

    private boolean blAddRecordOK = true;

    /** Flag to indicate whether the record updated ok

     */    

    protected boolean blUpdateRecordOK = true;

    /** Flag to indicate whether the update failed because of timestamps

     */    

    protected boolean blUpdateTimestampFailed = false;

    /** Flag to indicate whether the record search was ok

     */    

    protected boolean blSearchRecordOK = true;

    private boolean blDuplicateKeyFound = false;

    private boolean blDeleteRecord = false;

    private boolean blValidDataOK = true;

    private boolean blFileSizeOK = true;

    private int intInsertedRecordId;

    /** Holds the record count for a search

     */    

    protected int intRecordCount = 0;

    private String strDirectory = new String();

    private String strFileName = new String();

 //   private String strTableLOVField = new String();

    /** Flag to indicate whether to use the delete column for a domain

     */    

    protected boolean blDeletedColumn = true;

    /** Holds the search tag name

     */    

    protected String strSearchResultTag = "searchResult";

    /** Flag to turn off case insensitivity

     */    

    protected boolean blTurnLowerCaseOff = false;

   // private String strTempCutString;

//    private String strDomain;

    //private final  String INT_TYPE = "INT";

    //public final static String DATE_TYPE = "DATE";

    

    // paging

    private int intStartRecord;

    private int intRecordPerPage;

    private int intRecordSetSize;

    

    // enhanced DAL

    /** Flag to indicate whether to use the default joins or explicit defined joins

     */

    private boolean blUseDefaultJoin = true;

    

    // variables to keep explicit defined joins

    private Vector vecJoinDomainNames = new Vector(5,2);

    private Vector vecJoinTypes = new Vector(5,2);

    private Vector vecFirstJoinFieldNames = new Vector(5,2);

    private Vector vecSecondJoinFieldNames = new Vector(5,2);

    

    // Enhanced DAL

    private Vector vecBaseChannels = new Vector(3,2);

    private Vector vecBaseChannelConnectors = new Vector(3,2);

    

    private Vector vecFromBaseChannels = new Vector(3,2);

    private Vector vecFromBaseChannelAlias = new Vector(3,2);

    

    private Vector vecWhereBaseChannels = new Vector(3,2);

    private Vector vecWhereBaseChannelFields = new Vector(3,2);

    private Vector vecWhereBaseChannelOperators = new Vector(3,2);

    private Vector vecWhereBaseChannelConnectors = new Vector(3,2);

    

    private Vector vecWhereStatements = new Vector(3,2);

    private Vector vecWhereStatConnectors = new Vector(3,2);

    

    /** Creates a new instance of BaseChannel */

    public BaseChannel() 

    {

        try

        {

            hashDBFieldTypes = DBSchema.getDBFieldTypes();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Base Channel - " + e.toString());

        }

	strActivityRequested = DBSchema.ACTIVITY_LOV_ACCESS;

	authToken = new NullAuthToken() ;

    }

    /** Creates a new instance of BaseChannel */

    public BaseChannel(AuthToken authTemp) 

    {

        try

        {

            hashDBFieldTypes = DBSchema.getDBFieldTypes();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Base Channel - " + e.toString());

        }

        authToken = authTemp;

    }

    /** Creates a new instance of BaseChannel */

    public BaseChannel(String strActivity, AuthToken authTemp) 

    {

        try

        {

            hashDBFieldTypes = DBSchema.getDBFieldTypes();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Base Channel - " + e.toString());

        }

	strActivityRequested = strActivity ;

        authToken = authTemp; 

    }

    /** Assign AuthToken */

    public void setAuthToken(AuthToken authTemp)

    {

        authToken = authTemp;

    }

    /** Assign a Role */

    public void setActivity(String strActivity)

    {

	strActivityRequested = strActivity ;

    }

    /** Allows another channel to set the directory where a file is to be saved

     * @param strADirectory The directory to save the file too.

     */    

    public void setDirectory(String strADirectory)

    {

        strDirectory = strADirectory;

    } 

    /** This method return the maximum values in a domain

     * for the fields specified for the getMax() method

     * @return A hashTable full of maximum values for the fields specified

     */    

    public Hashtable getMaxFields()

    {

        return hashMaxFields;

    }

    /** Allows the default "<searchTag>"

     * to be changed

     * @param aStrSearchResultTag The new name for the default "<searchResult>"

     */    

    public void setSearchResultTag(String aStrSearchResultTag)

    {

        this.strSearchResultTag = aStrSearchResultTag;

    }

    /** Allows another channel to set a domain

     * @param strADomain A domain to add

     */    

    public void setDomain(String strADomain)

    {

        vecDomains.add(strADomain);

    }

    /** Returns the record count of a query run

     * @return returns the record count for a search

     */    

    public int getRecordCount()

    {

        return intRecordCount;

    }

    /** Allows another channel to reset the record count for subsequent  queries

     */    

    public void resetRecordCount()

    {

        intRecordCount = 0;

    }

    /** Allows another channel to set the where values of a query

     * @param strWhereConnector The connector to use

     * @param strAFieldName The field to add

     * @param strOperator The operator to use

     * @param strAValue The value to check for

     */    

    public void setWhere(String strWhereConnector, String strAFieldName, String strOperator, String strAValue)

    {

        vecWhereFields.add(strAFieldName);

        vecWhereOperator.add(strOperator);

        vecWhereFieldValue.add(strAValue);

        vecWhereConnector.add(strWhereConnector);

    }

    /** Allows an object to open a bracket to group where conditions

     */    

     public void setOpenWhereBracket()

    {

        vecOpenWhereBracket.add(new Integer(vecWhereFields.size()));

    }

     /** Allows an object to close a bracket to group where conditions

      * @param strAWhereConnector Takes a valid bracket connector

      */     

    public void setCloseWhereBracket(String strAWhereConnector)

    {

        vecCloseWhereBracket.add(new Integer(vecWhereFields.size() -1));

 //       vecCloseWhereBracket.add(vecOpenWhereBracket.lastElement());

        vecWhereBracketConnector.add(strAWhereConnector);

    }

    /** Allows another channel to clear the where components already set

     */    

     public void clearWhere()

    {

        vecWhereFields.clear();

        vecWhereOperator.clear();

        vecWhereFieldValue.clear();

        vecWhereConnector.clear();

    }

     /** Allows another channel to set the orderby requirements for a query

      * @param strOrderByField The field to order by

      * @param strOrderDirection The direction of the order by

      */     

    public void setOrderBy(String strOrderByField, String strOrderDirection)

    {

        vecOrderByFields.add(strOrderByField);

        vecOrderByFieldDirection.add(strOrderDirection);

    }

    /** Clears the orderby statement in a query object

     */    

    public void clearOrderBy()

    {

        vecOrderByFields.clear();

        vecOrderByFieldDirection.clear();

    }

     /** Allows another channel to clear the domains

      */     

    public void clearDomains()

    {

        if(vecDomains != null)

        {

            vecDomains.clear();

        }

    }

    public boolean getUpdateTimeStampFailed()

    {

        return blUpdateTimestampFailed;

    }

    /** Set the forms fields that the base channel is to deal with.

     * @param hashAFormFields List of form fields the base channel has to deal with

     */    

    public void setFormFields(Hashtable hashAFormFields)

    {

        this.hashFormFields = hashAFormFields;

    }

    /** Allows channels to set the drop downs for the base channel to build

     * @param hashAFormFieldDropDown The list of form fields drop down

     */    

    public void setFormFieldDropDown(Hashtable hashAFormFieldDropDown)

    {

        this.hashFormFieldDropDown = hashAFormFieldDropDown;

    }

    /** Allows channels to set the operators for a search

     * form

     * @param hashASearchFieldOperators Contains the search operators for the search form the

     * channel is using.

     */    

    public void setSearchFieldOperators(Hashtable hashASearchFieldOperators)

    {

        this.hashSearchFieldOperators = hashASearchFieldOperators;

    }

    /** Allows channels to specify any fields that must be validated for form data submission

     * @param hashAValidateFields The first column contains the form field and the second column contais the number

     * of the function is must pass in the ValidateFieldFunction class

     */    

    public void hashValidateFields(Hashtable hashAValidateFields)

    {

        this.hashValidateFields = hashAValidateFields;

    }

    /** Allows a channel to set the amount of text to cut out of

     * the data returned between a tag. For example if you give

     * a field to be cut to 10, all the text returned in that tag will be

     * cut to ten characters.

     * @param hashACutFields Hashtable containing the form field to cut

     * and the size to cut it to

     */    

    public void sethashCutFields(Hashtable hashACutFields)

    {

        this.hashCutFields = hashACutFields;

    }

    /** Allows a channel to add additional

     * content (E.g., extra tags to the XML fiel bing

     * built)

     * @param strAXML The content to add

     */    

    public void setXMLFile(String strAXML)

    {

        strXML += strAXML;

    }

    /** Allows a channel to wipe the current

     * form of the XML file being built.

     */    

    public void setNewXMLFile()

    {

        strXML = ""; 

    }

    /** Adds the last error message to the XML file

     *

     */

     public void setErrorXMLFile()

     {

	strXML += strLastError;

     }

    /** Returns the XML file that has been

     * built by the BaseChannel

     * @return Returns the XML string for the XSL

     */    

    public String getXMLFile()

    {

        return strXML;

    }

    /** This method allows a channel to give the runtimeData from the

     * form (web page) to the BaseChannel. Without this the BaseChannel

     * can not get data from the web page

     * @param rd The runtimeData from the channel

     */    

    public void setRunTimeData(ChannelRuntimeData rd)

    {

        runtimeData = rd;

    }

    /** This method returns whether all the required fields have been

     * filled in

     * @return returns false if all the required fields have not been filled in

     */    

    public boolean getRequiredStatus()

    {

        return blRequiredOK;

    }

    /** This is a flag indicating whether the record was added successfully

     * using the add method function

     * @return returns false if the record was not added correctly

     */    

    public boolean getAddRecordStatus()

    {

        return blAddRecordOK;

    }

    /** Indicates whether the record was updated

     * successfully updated with the updateRecord method

     * @return returns false if the update was not successful

     */    

    public boolean getUpdateRecordStatus()

    {

        return blUpdateRecordOK;

    }

    /** Indicates whether the fields that require

     * validation passed or not

     * @return True means that the fields (passed in as a hashTable)

     * passed the validation.

     */    

    public boolean getValidFieldsStatus()

    {

        return blValidFieldsOK;

    }

    /** This flag incidates whether the data submitted in runtimeData

     * was of the correct type (E.g., a date was in a date format, a int was a int)

     * @return True means that the data was in the correct

     * format for the type of field in the database

     */    

    public boolean getValidDataStatus()

    {

        return blValidDataOK;

    }

    /** This method returns the inserted record Id (auto-increment)

     * for the addRecord method

     * @return returns the inserted record id as an int

     */    

    public int getInsertedRecordId()

    {

        return intInsertedRecordId;

    }

    /** This flad incidates whether there is already a duplicate record in the

     * database for the duplicate fields specified in the DBSchema.class

     * @return True indicates that a duplicate key already exisits

     */    

    public boolean getDuplicateKeyStatus()

    {  

        return blDuplicateKeyFound;

    }

    /** This method sets the flag to delete for the updateRecord.

     * If this is set to true the delete column will be set to -1 (yes)

     * once the updateRecord method is run

     */    

    public void deleteRecord()

    {

        blDeleteRecord = true;

    }

    /** This flag indicates whether the file size was ok for the saveFileToServer

     * method

     * @return True indicates that the file size

     * was ok according to the portal.properties file

     */    

    public boolean getOKFileSize()

    {

        return blFileSizeOK;

    }

    /** Returns the file name given by the saveFileToServer

     * method. This name is the name save on the server and is encrytped

     * @return Returns the encrypted file name of the downlaoded file

     */    

    public String getFileName()

    {

        return strFileName;

    }

    /** This flag tells the BaseChannel that the domain

     * it is dealing with does not have the default "delete" column

     * in the database. Mainly used for uportal tables as all neuragenix

     * tables should contain the delete column

     * @param aDeletedFlag True means the domain has a deleted column. The default is true

     */    

    public void setDeletedColumn(boolean aDeletedFlag) 

    {

        blDeletedColumn = aDeletedFlag;

    }


    /** Set the forms fields that the base channel wants to hide.

     * @param hashAHideFormFields List of form fields the base channel wants to hide

     */    

    public void setHideFormFields(Hashtable hashAHideFormFields)

    {

        this.hashHideFormFields = hashAHideFormFields;

    }


    /** This flag tells BaseChannel that the

     * where search should be case sensitive.

     * The default is case insensitive

     * @param aTurnOffKey True means that all searches should be case sensitive. The default is false

     */    

     public void setCaseSensitive(boolean aTurnOffKey)

    {

        blTurnLowerCaseOff = aTurnOffKey;

     }

     /** This method saves a file to the server based on

      * the runtimeData and form fields given. The form fields must contain the

      * type "File"

      * @throws BaseChannelInvalidDirectory The directory given (in setDirectory) is not a valid

      * directory on the server

      * @throws BaseChannelException Thrown when an unknown error occurs

      * @throws BaseChannelInvalidFieldSupplied Thrown when there is not "FILE" type field supplied in

      * the form fields hashTable

      */     

    public void saveFileToServer() throws BaseChannelInvalidDirectory, BaseChannelException, BaseChannelInvalidFieldSupplied

    {

        try

        { 
//            System.err.println("------------in save file to server----------");
            // First check to see if we have a directory to save to!

            if(strDirectory.length() == 0)

            {

               

                LogService.instance().log(LogService.ERROR, "BaseChannelInvalidDirectory - No directory specified to save the file");

                throw new BaseChannelInvalidDirectory("No directory specified to save the file");

            }

            else

            {

                Enumeration enumInternalFormFields = hashFormFields.keys(); // Get the internal names for the form

                String strTempFormField;

                boolean blFileFieldFound = false;

                while(enumInternalFormFields.hasMoreElements())

                {

                    

                    strTempFormField = enumInternalFormFields.nextElement().toString();
                    
//                    System.err.println("---------formfields--& filetype-----" + strTempFormField +" " + hashDBFieldTypes.get(strTempFormField) +"  : " + DBSchema.FILE_TYPE);

                    if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.FILE_TYPE))

                    {                

                          blFileFieldFound = true;

                          org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter(strTempFormField);
                          
//                          System.err.println("----------file to save & file available---------" + fileToSave + "-------- " + fileToSave.getInputStream().available());

                          if(fileToSave == null || fileToSave.getInputStream().available() < 1 || fileToSave.getInputStream().available() > PropertiesManager.getPropertyAsInt("org.jasig.portal.PortalSessionManager.File_upload_max_size"))

                          {

                                blFileSizeOK = false;

                                strXML += "<strErrorFileToLarge>The file size is over the maximum allowable or 0Kb file attached or no file selected.</strErrorFileToLarge>";

                                                

                          }

                          else

                          {

                              // We are going to encrypt the file name for security purposes

                                strFileName = fileToSave.getName();

                                String strFileEnding = "";

                                // Get the ending type of document

                                int intIndexOfDot = strFileName.lastIndexOf('.');

                                // Make sure we have a dot!

                                if(intIndexOfDot != -1)

                                {

                                    strFileEnding = strFileName.substring(intIndexOfDot);

                                }

                                else{strFileName = "";}

                                

                                // This object allows use to get a unique encrypted name 

                             //   Password my_password_object = new Password();

                                // Encrypt using the date and time stamp to be unique

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

                                strFileName = strFileName + strFileEnding;

                                

                                strFileName = strFileName.substring(5);

           

                                File fileOnDirectory = new File (strDirectory, strFileName) ;

                                if(fileOnDirectory.exists()) // If the file exists keep looping until you get a file name that doesn't exist

                                { 

                                    while(fileOnDirectory.exists())

                                    {

                                       // fileOnDirectory = null;

                                        calCurrent.getInstance();

                                        strFileName = Password.getEncrypt(calCurrent.getTime().toString());

                                        // Replace any characters that will stuff up the save!

                                        strFileName = strFileName.replace((char)47,'x'); // random character

                                        strFileName = strFileName.replace((char)92,'x'); // random character

                                        strFileName = strFileName.replace((char)43,'x'); // random character

                                        strFileName = strFileName.replace((char)63,'x'); // random character

                                        strFileName = strFileName.replace((char)64,'x'); // random character

                                        strFileName = strFileName.replace((char)38,'x'); // random character

                                        strFileName = strFileName.replace((char)58,'x'); // random character

                                        strFileName = strFileName.replace((char)59,'x'); // random character

                                        strFileName = strFileName.replace((char)61,'x'); // random character

                                        strFileName = strFileName.replace((char)44,'x'); // random character

                                        strFileName = strFileName.replace((char)35,'x'); // random character

                                        strFileName = strFileName.replace('.','x'); // random character

                                        strFileName = strFileName + strFileEnding;

                                        strFileName = strFileName.substring(5);

                                        fileOnDirectory = new File (strDirectory, strFileName);

                                    }

                                }

                                

                                byte[] byteFromFile = new byte[fileToSave.getInputStream().available()];

                                  

                                fileToSave.getInputStream().read(byteFromFile);

                                FileOutputStream fos = new FileOutputStream(fileOnDirectory) ;

                                fos.write(byteFromFile);

		                          fos.close( ) ;

                          

                          }

                   

                          break;

                          

                     }             

                }

                if(blFileFieldFound == false)

                {

                    LogService.instance().log(LogService.ERROR, "BaseChannelInvalidFieldSupplied - No file field type supplied while trying to save file to server");

                    throw new BaseChannelInvalidFieldSupplied("No file field type supplied while trying to save file to server");

                }

            }

        }

        catch(java.io.FileNotFoundException fnf)

        {

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidDirectory - Invalid directory specified to save the file - " + fnf.toString(), fnf);

            throw new BaseChannelInvalidDirectory("Invalid directory specified to save the file", fnf);

        }

        catch(Exception e)

        { 

            

            System.err.println("Unknown error while trying to save file to server - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to save file to server - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to save file to server - " + e.toString(), e);

        }

    }
    
    public void encryptFileName(String strdirectory,org.jasig.portal.MultipartDataSource fileToSave)
    {
        this.strDirectory = strdirectory;
        
        try
        {
        
        strFileName = fileToSave.getName();

                                String strFileEnding = "";

                                // Get the ending type of document

                                int intIndexOfDot = strFileName.lastIndexOf('.');

                                // Make sure we have a dot!

                                if(intIndexOfDot != -1)

                                {

                                    strFileEnding = strFileName.substring(intIndexOfDot);

                                }

                                else{strFileName = "";}

                                

                                // This object allows use to get a unique encrypted name 

                             //   Password my_password_object = new Password();

                                // Encrypt using the date and time stamp to be unique

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

                                strFileName = strFileName + strFileEnding;

                                

                                strFileName = strFileName.substring(5);

           

                                File fileOnDirectory = new File (strDirectory, strFileName) ;

                                if(fileOnDirectory.exists()) // If the file exists keep looping until you get a file name that doesn't exist

                                { 

                                    while(fileOnDirectory.exists())

                                    {

                                       // fileOnDirectory = null;

                                        calCurrent.getInstance();

                                        strFileName = Password.getEncrypt(calCurrent.getTime().toString());

                                        // Replace any characters that will stuff up the save!

                                        strFileName = strFileName.replace((char)47,'x'); // random character

                                        strFileName = strFileName.replace((char)92,'x'); // random character

                                        strFileName = strFileName.replace((char)43,'x'); // random character

                                        strFileName = strFileName.replace((char)63,'x'); // random character

                                        strFileName = strFileName.replace((char)64,'x'); // random character

                                        strFileName = strFileName.replace((char)38,'x'); // random character

                                        strFileName = strFileName.replace((char)58,'x'); // random character

                                        strFileName = strFileName.replace((char)59,'x'); // random character

                                        strFileName = strFileName.replace((char)61,'x'); // random character

                                        strFileName = strFileName.replace((char)44,'x'); // random character

                                        strFileName = strFileName.replace((char)35,'x'); // random character

                                        strFileName = strFileName.replace('.','x'); // random character

                                        strFileName = strFileName + strFileEnding;

                                        strFileName = strFileName.substring(5);

                                        fileOnDirectory = new File (strDirectory, strFileName);

                                    }

                                }

                                

                                byte[] byteFromFile = new byte[fileToSave.getInputStream().available()];

                                  

                                fileToSave.getInputStream().read(byteFromFile);

                                FileOutputStream fos = new FileOutputStream(fileOnDirectory) ;

                                fos.write(byteFromFile);

		                          fos.close( ) ;

    }
    catch(Exception e)
    {
            System.err.println("Unknown error while trying to save file to server - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to save file to server - " +  e.toString(), e);
    }
       
        
        
    }

    /** This method builds the XML file that contains

     * the display names for the form fields supplied.

     * No database interaction

     */    

    private String buildDisplayNames(String strAInternalName, String strADisplayName)

    {

        return "<" + strAInternalName + "Display>" + Utilities.cleanForXSL(strADisplayName) + "</" + strAInternalName + "Display>";          

    }

    public String buildListOfValues(String strASelectToGet, String strFormData) throws BaseChannelException

    {

        String strOptionList = "";

       

        

        try

        {         

            // Load a new Query object 

            Query my_query = new Query(Query.SELECT_QUERY, DBSchema.ACTIVITY_LOV_ACCESS, authToken);

            my_query.setDeletedColumn(blDeletedColumn);

            my_query.setCaseSensitive(blTurnLowerCaseOff);

            // Set the domain

            

            my_query.setDomainName("LIST_OF_VALUES");

            my_query.setField("strType", "");

            my_query.setField("strValue", "");

            my_query.setWhere("", "strType", "EQUALS_OPERATOR", hashFormFieldDropDown.get(strASelectToGet).toString());

            my_query.setOrderBy("intSortOrder", DBSchema.ORDERBY_ASENDING);



            boolean blExcuteOK = my_query.execute();

            ResultSet my_resultset = my_query.getResults();

            strFormData.trim(); // Trim the white spaces of the form data 

            String strTempFormValue;

            boolean bFirstRecord = true;

            while(my_resultset.next())

            {

                strTempFormValue = Utilities.cleanForXSL(my_resultset.getString("strValue"));

                

                 // Start to build the XML file

                  strOptionList += "<" + strASelectToGet + " selected=\""; // + my_resultset.getString("strValue");

                  // If their is no form data (the first time someone comes to a page) set the drop down to the first option

                  if (strFormData.length() == 0 && (bFirstRecord == true))

                  {
                      bFirstRecord = false;
                      strOptionList += "1";

                  }

                  // If there is form data set it to selected (1) for the form

                  else if(strTempFormValue.equals(Utilities.cleanForXSL(strFormData)))

                  {

                      strOptionList += "1";

                  }

                  else{strOptionList += "0";}

                  strOptionList += "\">" + Utilities.cleanForXSL(my_resultset.getString("strValue")); // Add the option value to the XML tag

                  strOptionList += "</" + strASelectToGet + ">"; // Close off the tag

            }

                // Return the ResultSet so it can be closed!

                my_query.killResultSet(my_resultset);

                my_query = null;

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build the list of values - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to build the list of values - " + e.toString(), e);

  

        }

        strOptionList += "<" + strASelectToGet + "Selected>" + Utilities.cleanForXSL(strFormData) + "</" + strASelectToGet + "Selected>"; 

     

        return strOptionList;

        

    }

    /** This method adds a record to the database based

     * on the form fields (set in setFormFields)

     * and the runtimeData supplied (set in setRunTimeData)

     * @throws BaseChannelInvalidRuntimeData Thrown when there is no runtimeData given

     * for the field supplied

     * @throws BaseChannelException Thrown when an unknown event occurs

     */    

    public void addRecord() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        try

        {

            Enumeration enumInternalFormFields = hashFormFields.keys(); // Get the internal names for the form

            String strTempFormField;

            // Set the query object with an insert type

            Query my_query = new Query(Query.INSERT_QUERY, strActivityRequested, authToken);

            // Set the domain for the Query object

            my_query.setDomainName(vecDomains.get(0).toString());

            my_query.setDeletedColumn(blDeletedColumn);

            my_query.setCaseSensitive(blTurnLowerCaseOff);

            while(enumInternalFormFields.hasMoreElements())

            {

                strTempFormField = enumInternalFormFields.nextElement().toString();

              

                // Only add records with data!
                
                if(runtimeData.getParameter(strTempFormField).length() > 0)

                {

             

                    // Set the fields to add
                    
                    my_query.setField(strTempFormField, runtimeData.getParameter(strTempFormField).toString());

                }

            }

            

            // Excute the query

            blAddRecordOK = my_query.execute();

          

            if(blAddRecordOK)

            {   

                my_query.saveChanges(); // if ok save the changes

                intInsertedRecordId = my_query.getInsertedRecordId();        

            }

            else{my_query.cancelChanges();} // if there was a problem cancel changes

            my_query = null;

        }



        catch(neuragenix.dao.exception.DAODuplicateKey dk)

        {

            blDuplicateKeyFound = true;

            strXML += "<strErrorDuplicateKey>Duplicate field already exists</strErrorDuplicateKey>";

            

            blAddRecordOK = false;

        }

        catch(java.lang.NullPointerException npe)

        {

            blAddRecordOK = false;

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);



        }

        catch(Exception e)

        {


            e.printStackTrace();
            blAddRecordOK = false;

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to add new record - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to add new record - " + e.toString(), e);

  

        }

   }

    /** This method updates a record to the database based

     * on the form fields (set in setFormFields)

     * and the runtimeData supplied (set in setRunTimeData)

     */    

    public void updateRecord()

    {

        try

        {

            Enumeration enumInternalFormFields = hashFormFields.keys(); // Get the internal names for the form

            String strTempFormField;

            // Set the query object with an insert type

            Query my_query = new Query(Query.UPDATE_QUERY, strActivityRequested, authToken);

            // Set the domain for the Query object

            my_query.setDomainName(vecDomains.get(0).toString());

            my_query.setDeletedColumn(blDeletedColumn);

            my_query.setCaseSensitive(blTurnLowerCaseOff);

            if(blDeleteRecord == true)

            {

                my_query.setField(DBSchema.DELETED_FIELD, "-1");

            }

                             

            my_query.setTimestamp(runtimeData.getParameter(vecDomains.get(0).toString() + "_Timestamp"));



            while(enumInternalFormFields.hasMoreElements())

            {          

                strTempFormField = enumInternalFormFields.nextElement().toString();



                    // Set the fields to add

       //         if(runtimeData.getParameter(strTempFormField).toString().length() > 0)

         //       {

                     my_query.setField(strTempFormField, runtimeData.getParameter(strTempFormField).toString());

           //     }

             //   else

              //  {

                 //    my_query.setField(strTempFormField, null);

               // }



            }

            for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

            {

                    my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

            }

            // Excute the query

            blUpdateRecordOK = my_query.execute();

	    blUpdateTimestampFailed = (my_query.getUpdatedRecordCount() == 0);

		if( blUpdateTimestampFailed )

		{

			blUpdateRecordOK = false;

            		strXML += "<strErrorFailedUpdate>The record was edited by someone else.</strErrorFailedUpdate>";

            		strLastError += "<strErrorFailedUpdate>The record was edited by someone else.</strErrorFailedUpdate>";

		}

			

            if(blUpdateRecordOK)

            {

                my_query.saveChanges(); // if ok save the changes

            }

            else{my_query.cancelChanges();} // if there was a problem cancel changes

            my_query = null;

            // Reset the delete flag to false

            blDeleteRecord = false;

        }

        catch(neuragenix.dao.exception.DAODuplicateKey dk)

        {

            blDuplicateKeyFound = true;

            strXML += "<strErrorDuplicateKey>Duplicate field already exists</strErrorDuplicateKey>";

            blAddRecordOK = false;

        }

        catch(Exception e)

        {

            blUpdateRecordOK = false;

            System.out.println(e.toString());

            LogService.instance().log(LogService.ERROR, "Unknown error while trying to update a record - " + e.toString(), e);

        }

   }

    /** This method builds the display names for the

     * XML. No data is entered into the tags from the

     * the database. Usually used when the page is first hit

     * and no data is required from the database

     * @throws BaseChannelException Thrown when an unknown error

     * occurs

     */    

    public void buildDefaultXMLFile() throws BaseChannelException

    {

        try{

            

            String strTempDisplay = "";

            Enumeration enumInternalFormFields = hashFormFields.keys();

            Enumeration enumDisplayFormFields = hashFormFields.elements();

            while(enumInternalFormFields.hasMoreElements())

            {

                strTempDisplay = enumInternalFormFields.nextElement().toString();

                strXML += "<" + strTempDisplay + "Display>" + Utilities.cleanForXSL(enumDisplayFormFields.nextElement().toString()) + "</" + strTempDisplay + "Display>";          

                if(hashFormFieldDropDown !=null && hashFormFieldDropDown.containsKey(strTempDisplay))

                {

                    strXML +=  buildListOfValues(strTempDisplay, "");

                }

            }

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build default xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "Unknown error while trying to build default xml file - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to build default xml file - " + e.toString(), e);

  

        }

    }

    /** This method checks to see that all the data

     * from the form passes all validation checks, required

     * fields, and the data is of the correct type (e.g., a

     * date field is being save with a date format). It also puts

     * all the data from the form back into the tags (in case of an error), so

     * it can be returned back to the form.

     *

     * This method is usually called before an addRecord or

     * updateRecord to ensure the data is good.

     * @throws BaseChannelInvalidRuntimeData Thrown when no runtimeData (web form data)

     * is supplied for the formFields supplied

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public void buildAddModifyXMLFile() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

       String strField="";

        try

        {

            

            Enumeration enumInternalFormFields = hashFormFields.keys(); // Get the internal form names for the page

            

            String strTempFormField = ""; // A temp variable

            String strTempDisplayName = "";

            Vector vecErrorRequiredFields = new Vector(); // Used to add the name in the error tag

            Vector vecErrorInvalidDataFields = new Vector();// Used to add the name in the error tag

            while(enumInternalFormFields.hasMoreElements())

            { 

               strTempFormField = enumInternalFormFields.nextElement().toString(); // Get the next internal form name

               strTempDisplayName = hashFormFields.get(strTempFormField).toString();

		strField = strTempFormField;

                // If the form has drop down lists build them

                    if(hashFormFieldDropDown != null && hashFormFieldDropDown.containsKey(strTempFormField))

                    {

                        strXML += buildListOfValues(strTempFormField, runtimeData.getParameter(strTempFormField));

                    }

                    else // If it's not a drop down type process has normal

                    {

                        strXML += "<" + strTempFormField + ">";

                        if(runtimeData.getParameter(strTempFormField) != null && runtimeData.getParameter(strTempFormField).length() > 0)

                        {

                            // If it's a date convert it to the correct display format 

                            //*************************************** ?????????????????*********************

                //            if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.DATE_TYPE))

                  //          {

                    //              strXML += Utilities.convertDateForDisplay(runtimeData.getParameter(strTempFormField));

                      //      }else{

                                strXML += Utilities.cleanForXSL(runtimeData.getParameter(strTempFormField));

                          //  }

                        }

                        

                        strXML += "</" + strTempFormField + ">";

                    }

                    // Check the required fields

                    if(Utilities.requiredField(strTempFormField) == true && runtimeData.getParameter(strTempFormField).length() == 0)

                    { 

                        blRequiredOK = false; // If we have a required field not filled in set this to false to tell the calling object

                        vecErrorRequiredFields.add(hashFormFields.get(strTempFormField)); // The the field display name to the error tag

                    }

                //     Check to see if the we have the correct data for the data type

                    if(hashDBFieldTypes.containsKey(strTempFormField))

                    {

                        if(runtimeData.getParameter(strTempFormField).length() > 0)

                        {

                            

                            if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.INT_TYPE))

                            {

                                if(Utilities.validateIntValue(runtimeData.getParameter(strTempFormField)) == false)

                                {

                                    blValidFieldsOK = false;

                                    // !!!!!!!!! CHANGED BUT NOT SURE OF EFFECT _ SUSPECTED BUG

                                    vecErrorInvalidDataFields.add(hashFormFields.get(strTempFormField));

                                }

                                else if(checkValidFields(strTempFormField, runtimeData.getParameter(strTempFormField)) == false)

                                {

                                    blValidDataOK = false;

                                    vecErrorInvalidData.add(hashFormFields.get(strTempFormField));                                

                                }

                                

                             }

                            else if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.DATE_TYPE))

                             {    String strDateValue =  runtimeData.getParameter(strTempFormField);

                                  if(Utilities.validateDateValue(runtimeData.getParameter(strTempFormField)) == false)

                                  {

                                      blValidFieldsOK = false;

                                      vecErrorInvalidDataFields.add(hashFormFields.get(strTempFormField));

                                  }

                                  else {
                                      // ple, added to suppport variable length date format
                                      runtimeData.setParameter(strTempFormField,QueryChannel.makeCorrectDateFormat(strDateValue));
                                      if(checkValidFields(strTempFormField, runtimeData.getParameter(strTempFormField)) == false)
                                          
                                      {
                                          
                                          blValidDataOK = false;
                                          
                                          vecErrorInvalidData.add(hashFormFields.get(strTempFormField));
                                          
                                      }
                                  }

                             }

                            if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.FLOAT_TYPE))

                            {

                                if(Utilities.validateFloatValue(runtimeData.getParameter(strTempFormField)) == false)

                                {

                                    blValidFieldsOK = false;

                                     vecErrorInvalidDataFields.add(hashFormFields.get(strTempFormField));

                                }

                                else if(checkValidFields(strTempFormField, runtimeData.getParameter(strTempFormField)) == false)

                                {

                                    blValidDataOK = false;

                                    vecErrorInvalidData.add(hashFormFields.get(strTempFormField));                                

                                }

                                

                             }

                            

                            else if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.TIME_TYPE))

                            {

                                  if(Utilities.validateTimeValue(runtimeData.getParameter(strTempFormField)) == false)

                                  {

                                      blValidFieldsOK = false;

                                      vecErrorInvalidDataFields.add(hashFormFields.get(strTempFormField));

                                  }

                                else if(checkValidFields(strTempFormField, runtimeData.getParameter(strTempFormField)) == false)

                                  {

                                    blValidDataOK = false;

                                    vecErrorInvalidData.add(hashFormFields.get(strTempFormField));                                

                                  }

                            }

                            else if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.STR_TYPE))

                            {

                                if(checkValidFields(strTempFormField, runtimeData.getParameter(strTempFormField)) == false)

                                  {

                                    blValidDataOK = false;

                                    vecErrorInvalidData.add(hashFormFields.get(strTempFormField));                                

                                  }

                            }

                            else if(hashDBFieldTypes.get(strTempFormField).equals(DBSchema.SCRIPT_TYPE))

                            {

                                if(Utilities.validateScriptValue(runtimeData.getParameter(strTempFormField)) == false)

                                  {

                                      blValidFieldsOK = false;

                                      vecErrorInvalidDataFields.add(hashFormFields.get(strTempFormField));

                                  }

                                else if(checkValidFields(strTempFormField, runtimeData.getParameter(strTempFormField)) == false)

                                  {

                                    blValidDataOK = false;

                                    vecErrorInvalidData.add(hashFormFields.get(strTempFormField));                                

                                  }

                            }

                            

                        }

                    }

                    

                    // ADD THE DISPLAY NAMES TO THE XML FILE

                    strXML += buildDisplayNames(strTempFormField, strTempDisplayName);

                }

            // -------------------------- CHECK THE DATA IS OK! ------------------------------------------------

                // If a required field is not filled in fill the error tag and return them to the add form

                if(blRequiredOK == false)

                {

                    strXML += "<strErrorRequiredFields>Please fill in all required fields for "; 
					strLastError += "<strErrorRequiredFields>Please fill in all required fields for ";

                    for(int intCounter=0; intCounter < vecErrorRequiredFields.size(); intCounter ++)

                    {

                        strXML += Utilities.cleanForXSL(vecErrorRequiredFields.get(intCounter).toString());
						strLastError += Utilities.cleanForXSL(vecErrorRequiredFields.get(intCounter).toString());

                        if (vecErrorRequiredFields.size() > 1 && intCounter < vecErrorRequiredFields.size() - 1)

                        {

                            strXML += ", ";
							strLastError += ", ";

                        }

                    }

                        strXML += "</strErrorRequiredFields>";
						strLastError += "</strErrorRequiredFields>";
                }

                else if(blValidFieldsOK == false)

                {

                    strXML += "<strErrorInvalidDataFields>Please enter a valid input for "; 
					strLastError += "<strErrorInvalidDataFields>Please enter a valid input for ";

                    for(int intCounter=0; intCounter < vecErrorInvalidDataFields.size(); intCounter ++)

                    {

                        strXML += Utilities.cleanForXSL(vecErrorInvalidDataFields.get(intCounter).toString());
						strLastError += Utilities.cleanForXSL(vecErrorInvalidDataFields.get(intCounter).toString());

                        if (vecErrorInvalidDataFields.size() > 1 && intCounter < vecErrorInvalidDataFields.size() - 1)

                        {

                            strXML += ", ";
							strLastError += ", ";

                        }

                    }

                        strXML += "</strErrorInvalidDataFields>";
						strLastError += "</strErrorInvalidDataFields>";

                    

                }

                else if (blValidDataOK == false)

                {

                    strXML += "<strErrorInvalidData>The data entered for  "; 
					strLastError += "<strErrorInvalidData>The data entered for  ";
                    

                    for(int intCounter=0; intCounter < vecErrorInvalidData.size(); intCounter ++)

                    {

                        strXML += Utilities.cleanForXSL(vecErrorInvalidData.get(intCounter).toString());
						strLastError += Utilities.cleanForXSL(vecErrorInvalidData.get(intCounter).toString());

                        if (vecErrorInvalidData.size() > 1 && intCounter < vecErrorInvalidData.size() - 1)

                        {

                            strXML += ", ";
							strLastError += ", ";

                        }

                    }

                        strXML += " is invalid.</strErrorInvalidData>";
						strLastError += " is invalid.</strErrorInvalidData>";

                    

                

                }

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field " + strField + " supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);



        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build add modify xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build add modify xml file - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to build add modify xml file - " + e.toString(), e);

  

        }

    }

    /** This method builds a List of Values from a domain

     * supplied through the setformFields method. This hashTable

     * must contain the internal form name (first row) and the domain

     * in the second (column). This hashTable can contain multiple fields in different

     * domains.

     *

     * Important note - If you have a where statement you can only have one domain

     * in the hashTable

     * @throws BaseChannelInvalidRuntimeData Thrown if no runtimeData is provided for a formField

     * @throws BaseChannelException Thrown when an unknown error occurs

     * @throws BaseChannelInvalidDomainNumber Thrown when the domain supplied in the hashTable is not valid

     */    

    public void buildTableLOVXMLFile() throws BaseChannelInvalidRuntimeData, BaseChannelException, BaseChannelInvalidDomainNumber

    {

        String strTempDisplay = "";

        Enumeration enumInternalFormFields = hashFormFields.keys();

        Enumeration enumDomainFormFields = hashFormFields.elements();

        // Get the results

        try

        {

            

            Vector vecDomainsTemp = new Vector();

            String strTempDomainNameSetup;

            while(enumDomainFormFields.hasMoreElements())

            {

                strTempDomainNameSetup = enumDomainFormFields.nextElement().toString();

                if(vecDomainsTemp.contains(strTempDomainNameSetup) == false)

                {

                     vecDomainsTemp.add(strTempDomainNameSetup);   

                }

            }

                

                if(vecDomainsTemp.size() > 0)

                {

                    

                    String strTempDomainName;

           

                    for(int intCounter = 0; intCounter < vecDomainsTemp.size(); intCounter ++)

                    {

                        Query my_query = new Query(Query.SELECT_QUERY, DBSchema.ACTIVITY_LOV_ACCESS, authToken);

                        strTempDomainName = vecDomainsTemp.get(intCounter).toString();

                        my_query.setDomainName(strTempDomainName);

                        my_query.setDeletedColumn(blDeletedColumn);

                        my_query.setCaseSensitive(blTurnLowerCaseOff);

                        String strTempField;

                        while(enumInternalFormFields.hasMoreElements())

                        {         

                            strTempField = enumInternalFormFields.nextElement().toString();

                            

                            if(hashFormFields.get(strTempField).toString().equals(strTempDomainName))

                            {

                                my_query.setField(strTempField, "");

                            }

                            

                        }

                        // This is added as a short term solution as it does not allowed multiple domain where statements

                        for(int intWhereCounter = 0; intWhereCounter < vecWhereFields.size(); intWhereCounter ++)

                        {

                            my_query.setWhere(vecWhereConnector.get(intWhereCounter).toString(),vecWhereFields.get(intWhereCounter).toString(),  vecWhereOperator.get(intWhereCounter).toString(), vecWhereFieldValue.get(intWhereCounter).toString());

                        }

                        

             

                         blSearchRecordOK = my_query.execute();

                         ResultSet my_resultset = my_query.getResults();

                

                 

                        String strTempInternalFormName;

                        while(my_resultset.next())

                        {

                           

                            strXML += "<LOV_" + strTempDomainName + ">";

			    strXML += "<LOV_" + strTempDomainName + "_Timestamp>" 

				   + my_resultset.getString(strTempDomainName + "_Timestamp") 

				   + "</LOV_" + strTempDomainName + "_Timestamp>";

                            enumInternalFormFields = hashFormFields.keys();

                            while(enumInternalFormFields.hasMoreElements())

                            {

                                strTempInternalFormName = enumInternalFormFields.nextElement().toString();

                       

                          //      if(hashFormFields.get(strTempInternalFormName).toString().equals(strTempDomainName))

                           //     {

                                    

                                    strXML += "<LOV_" + strTempInternalFormName + ">";

                   

                                    if(my_resultset.getString(strTempInternalFormName) != null)

                                    {

                                    // If it's a date convert it to the correct display format 

                                    if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))

                                    {

                                        strXML += Utilities.convertDateForDisplay(my_resultset.getDate(strTempInternalFormName).toString());

                                    }

                                    else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.TIME_TYPE))

                                    {

                                        strXML += Utilities.convertTimeForDisplay(my_resultset.getTime(strTempInternalFormName).toString());

                                    }

                                    else

                                    {

                                        // If the output is to be cut ... cut it!

                                        if(hashCutFields != null && hashCutFields.size() > 0)

                                        {

                                          

                                            if(hashCutFields.containsKey(strTempInternalFormName))

                                            {

                                                // Make sure the length is greater than what you are trying to cut!

                                                if(my_resultset.getString(strTempInternalFormName).length() > ((Integer)hashCutFields.get(strTempInternalFormName)).intValue())

                                                {

                                                    strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName).substring(0, ((Integer)hashCutFields.get(strTempInternalFormName)).intValue()) + " ...");

                                                }

                                                else{strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));}

                                            }

                                            else

                                            {

                                               strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                            }

                                        }

                                        else

                                        {

                                             strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                        }

                                    

                                    }

                                    }

                                    strXML += "</LOV_" + strTempInternalFormName + ">";

                             //   }

                            }

                            strXML += "</LOV_" + strTempDomainName + ">";

                            intRecordCount ++;

                  

                            }

                            my_query.killResultSet(my_resultset);

                    }

            }

            else

            {

                    System.err.println("No domains for build lov");

                    LogService.instance().log(LogService.ERROR, "BaseChannelInvalidDomainNumber - Too many domains for build lov - ");

                    throw new BaseChannelInvalidDomainNumber("No domains for build lov");



            }

            

        

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString(), e);

        }

    }

    /** This method builds an XML file that contains multiple

     * records surrounded by the tag "&lt;searchResult&gt;" based on

     * the formFields provided. You may change the default name

     * "&lt;searchResult&gt;" through the setSearchResultTag method (in case you

     * want to do multiple searches in the one XML file)

     *

     * This method is usually used to get results from a search

     * @throws BaseChannelInvalidRuntimeData Thrown when no runtimeData is provided for the

     * form fields supplied

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public void buildSearchResultsXMLFile() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        String strTempDisplay = "";

        Enumeration enumInternalFormFields = hashFormFields.keys();

        Enumeration enumDisplayFormFields = hashFormFields.elements();

      

        // Get the results

        try

            {

                Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

                my_query.setDeletedColumn(blDeletedColumn);

                my_query.setCaseSensitive(blTurnLowerCaseOff);

                my_query.setUseDefaultJoin(blUseDefaultJoin);

                

                if (!blUseDefaultJoin)

                    for (int intCounter=0; intCounter < vecJoinDomainNames.size(); intCounter++)

                    {

                        my_query.setJoinDomain(vecJoinTypes.get(intCounter), 

                                               vecJoinDomainNames.get(intCounter),

                                               vecFirstJoinFieldNames.get(intCounter),

                                               vecSecondJoinFieldNames.get(intCounter));

                    }

                

                for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

                {

                    my_query.setDomainName(vecDomains.get(intCounter).toString());

                }

                while(enumInternalFormFields.hasMoreElements())

                {         

                    my_query.setField(enumInternalFormFields.nextElement().toString(), "");

                }

                // Old where query

      //          for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

        //        {

          //          my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

            //    }

                // New with "("

                for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

                {

                    if(vecOpenWhereBracket.contains(new Integer(intCounter)))

                    {

                         my_query.setOpenWhereBracket();

                    }

                    my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

                    

                    if(vecCloseWhereBracket.contains(new Integer(intCounter)))

                    {

                       my_query.setCloseWhereBracket(vecWhereBracketConnector.get(vecCloseWhereBracket.indexOf(new Integer(intCounter))).toString());

                 

                    }

                 }

                

                for (int intCounter = 0; intCounter < vecWhereStatements.size(); intCounter++)

                {

                    my_query.setWhereStatement(vecWhereStatConnectors.get(intCounter), vecWhereStatements.get(intCounter));

                }

                

                for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)

                {

                    my_query.setOrderBy(vecOrderByFields.get(intCounter).toString(),  vecOrderByFieldDirection.get(intCounter).toString());

                }

                


                blSearchRecordOK = my_query.execute();

                ResultSet my_resultset = my_query.getResults();

                my_resultset.last();

                intRecordSetSize = my_resultset.getRow();

                

                //paging

                my_query.setStartRecord(intStartRecord);

                my_query.setRecordPerPage(intRecordPerPage);

                

                blSearchRecordOK = my_query.execute();

                my_resultset = my_query.getResults();

                String strTempInternalFormName;

                

                

                while(my_resultset.next())

                {

                    

                    strXML += "<" + strSearchResultTag + ">";

			for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

			{

				strXML += "<" + vecDomains.get(intCounter).toString() + "_Timestamp>" 

					+ my_resultset.getString(vecDomains.get(intCounter).toString() + "_Timestamp") 

					+ "</" + vecDomains.get(intCounter).toString() + "_Timestamp>";

			}

                    enumInternalFormFields = hashFormFields.keys();

                    while(enumInternalFormFields.hasMoreElements())

                    {

                        strTempInternalFormName = enumInternalFormFields.nextElement().toString();

                        strXML += "<" + strTempInternalFormName + ">";

                   

                        if(my_resultset.getString(strTempInternalFormName) != null &&
                                !hashHideFormFields.containsKey(strTempInternalFormName))

                        {

                            // If it's a date convert it to the correct display format 

                            if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))

                            {

                                strXML += Utilities.convertDateForDisplay(my_resultset.getDate(strTempInternalFormName).toString());

                            }

                            // *********************** ??????????????????????????? **********

                            else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.TIME_TYPE))

                            {

                                strXML += Utilities.convertTimeForDisplay(my_resultset.getTime(strTempInternalFormName).toString());

                            }

                            else
                            {
//agus:THIS PART IS COMMENTED FIRST!!! just in case anything wrong happen :)

                                if(hashCutFields != null && hashCutFields.size() > 0)
                                {
                                    if(hashCutFields.containsKey(strTempInternalFormName))
                                    {
                                        // Make sure the length is greater than what you are trying to cut!

                                        if(my_resultset.getString(strTempInternalFormName).length() > ((Integer)hashCutFields.get(strTempInternalFormName)).intValue())
                                        {
                                            strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName).substring(0, ((Integer)hashCutFields.get(strTempInternalFormName)).intValue()) + " ...");
                                        }
                                        else
                                        {
                                            strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));
                                        }
                                     }
                                     else
                                     {
                                         strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));
                                     }
                                  }
                                  else
                                  {
                                      strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));
                                  }
//agus
//strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));
                            }

                        }
			else if(hashHideFormFields.containsKey(strTempInternalFormName))
                        {
                            strXML += Utilities.cleanForXSL((String)hashHideFormFields.get(strTempInternalFormName));
                        }


                        strXML += "</" + strTempInternalFormName + ">";

                        

                    }

                    strXML += "</" + strSearchResultTag + ">";

                    

                    intRecordCount ++;

                  

                }

                

                my_query.killResultSet(my_resultset);

            

        

                enumInternalFormFields = hashFormFields.keys();

                while(enumInternalFormFields.hasMoreElements())

                {

                    strTempDisplay = enumInternalFormFields.nextElement().toString();

                    strXML += "<" + strTempDisplay + "Display>" + Utilities.cleanForXSL(enumDisplayFormFields.nextElement().toString()) + "</" + strTempDisplay + "Display>";          

                    if(hashFormFieldDropDown != null && hashFormFieldDropDown.containsKey(strTempDisplay))

                    {

                        strXML +=  buildListOfValues(strTempDisplay, "");

                    }

                }

                my_query = null;

        }

        catch(java.lang.NullPointerException npe)

        {
            npe.printStackTrace();
            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString(),e);

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString(), e);

        }

    }

    

    

    public void buildSearchResultsXMLFile(int page) throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        String strTempDisplay = "";

        Enumeration enumInternalFormFields = hashFormFields.keys();

        Enumeration enumDisplayFormFields = hashFormFields.elements();

      

        // Get the results

        try

            {

                Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

                my_query.setDeletedColumn(blDeletedColumn);

                my_query.setCaseSensitive(blTurnLowerCaseOff);

                my_query.setUseDefaultJoin(blUseDefaultJoin);

                

                if (!blUseDefaultJoin)

                    for (int intCounter=0; intCounter < vecJoinDomainNames.size(); intCounter++)

                    {

                        my_query.setJoinDomain(vecJoinTypes.get(intCounter), 

                                               vecJoinDomainNames.get(intCounter),

                                               vecFirstJoinFieldNames.get(intCounter),

                                               vecSecondJoinFieldNames.get(intCounter));

                    }

                

                for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

                {

                    my_query.setDomainName(vecDomains.get(intCounter).toString());

                }

                while(enumInternalFormFields.hasMoreElements())

                {         

                    my_query.setField(enumInternalFormFields.nextElement().toString(), "");

                }

                // Old where query

      //          for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

        //        {

          //          my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

            //    }

                // New with "("

                for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

                {

                    if(vecOpenWhereBracket.contains(new Integer(intCounter)))

                    {

                         my_query.setOpenWhereBracket();

                    }

                    my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

                    

                    if(vecCloseWhereBracket.contains(new Integer(intCounter)))

                    {

                       my_query.setCloseWhereBracket(vecWhereBracketConnector.get(vecCloseWhereBracket.indexOf(new Integer(intCounter))).toString());

                 

                    }

                 }

                

                for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)

                {

                    my_query.setOrderBy(vecOrderByFields.get(intCounter).toString(),  vecOrderByFieldDirection.get(intCounter).toString());

                }

                

   

                blSearchRecordOK = my_query.execute();

                ResultSet my_resultset = my_query.getResults();

                String strTempInternalFormName;

                /*

                for (int i=0; i < (page-1)*PAGE_LINES; i++)

                    if (my_resultset.next());

                    else break;*/

                

                //int lineCount = 0;

                while(my_resultset.next())

                {

                    //lineCount++;

                    strXML += "<" + strSearchResultTag + ">";

			for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

			{

				strXML += "<" + vecDomains.get(intCounter).toString() + "_Timestamp>" 

					+ my_resultset.getString(vecDomains.get(intCounter).toString() + "_Timestamp") 

					+ "</" + vecDomains.get(intCounter).toString() + "_Timestamp>";

			}

                    enumInternalFormFields = hashFormFields.keys();

                    while(enumInternalFormFields.hasMoreElements())

                    {

                        strTempInternalFormName = enumInternalFormFields.nextElement().toString();

                        strXML += "<" + strTempInternalFormName + ">";

                   

                        if(my_resultset.getString(strTempInternalFormName) != null)

                        {

                            // If it's a date convert it to the correct display format 

                            if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))

                            {

                                strXML += Utilities.convertDateForDisplay(my_resultset.getDate(strTempInternalFormName).toString());

                            }

                            // *********************** ??????????????????????????? **********

                            else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.TIME_TYPE))

                            {

                                strXML += Utilities.convertTimeForDisplay(my_resultset.getTime(strTempInternalFormName).toString());

                            }

                            else{

                            

                                  if(hashCutFields != null && hashCutFields.size() > 0)

                                  {

                                          

                                       if(hashCutFields.containsKey(strTempInternalFormName))

                                       {

                                         

                                                // Make sure the length is greater than what you are trying to cut!

                                           if(my_resultset.getString(strTempInternalFormName).length() > ((Integer)hashCutFields.get(strTempInternalFormName)).intValue())

                                           {

                                                strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName).substring(0, ((Integer)hashCutFields.get(strTempInternalFormName)).intValue()) + " ...");

                                            }

                                            else{strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));}

                                       }

                                       else

                                       {

                                               strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                       }

                                    }

                                    else

                                    {

                                             strXML += Utilities.cleanForXSL(my_resultset.getString(strTempInternalFormName));

                                    }

                                                                       

                            }

                        }

                        strXML += "</" + strTempInternalFormName + ">";

                        

                    }

                    strXML += "</" + strSearchResultTag + ">";

                    

                    intRecordCount ++;

                  

                }

                

                my_query.killResultSet(my_resultset);

            

        

                enumInternalFormFields = hashFormFields.keys();

                while(enumInternalFormFields.hasMoreElements())

                {

                    strTempDisplay = enumInternalFormFields.nextElement().toString();

                    strXML += "<" + strTempDisplay + "Display>" + Utilities.cleanForXSL(enumDisplayFormFields.nextElement().toString()) + "</" + strTempDisplay + "Display>";          

                    if(hashFormFieldDropDown != null && hashFormFieldDropDown.containsKey(strTempDisplay))

                    {

                        strXML +=  buildListOfValues(strTempDisplay, "");

                    }

                }

                my_query = null;

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString(), e);

        }

    }

    

    /** This method builds an XML file for a single record

     * based on the formFields and setWhere conditions

     *

     * Should be used to view a single record. (E.g. a single patient details)

     * @throws BaseChannelInvalidRuntimeData Thrown when no runtimeData is provided for

     * a formField provided

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public void buildViewXMLFile() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        String strTempDisplay = "";

        Enumeration enumInternalFormFields = hashFormFields.keys();

        Enumeration enumDisplayFormFields = hashFormFields.elements();

        // Get the results

        try

            {

                Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

                my_query.setDeletedColumn(blDeletedColumn);

                my_query.setCaseSensitive(blTurnLowerCaseOff);

                for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

                {

                    my_query.setDomainName(vecDomains.get(intCounter).toString());

                }

                while(enumInternalFormFields.hasMoreElements())

                {         

                    my_query.setField(enumInternalFormFields.nextElement().toString(), "");

                }

                for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

                {

                    if(vecOpenWhereBracket.contains(new Integer(intCounter)))

                    {

                         my_query.setOpenWhereBracket();

                    }

                    my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

                    

                    if(vecCloseWhereBracket.contains(new Integer(intCounter)))

                    {

                       my_query.setCloseWhereBracket(vecWhereBracketConnector.get(vecCloseWhereBracket.indexOf(new Integer(intCounter))).toString());

                 

                    }

                 }

          

                blSearchRecordOK = my_query.execute();

                ResultSet my_resultset = my_query.getResults();

                

                

                String strTempInternalFormName;

               

                while(my_resultset.next())

                {

            //        strXML += "<searchResult>";
                    
                       /*for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

			{

				strXML += "<" + vecDomains.get(intCounter).toString() + "_Timestamp>" 

					+  my_resultset.getString(vecDomains.get(intCounter).toString() + "_Timestamp")

					+ "</" + vecDomains.get(intCounter).toString() + "_Timestamp>";

			}*/

                    enumInternalFormFields = hashFormFields.keys();

                    while(enumInternalFormFields.hasMoreElements())

                    {

                        

                        strTempInternalFormName = enumInternalFormFields.nextElement().toString();
                        String strTempData = "";
                        
                        if ((hashFormFieldDropDown == null) || (hashFormFieldDropDown != null && !hashFormFieldDropDown.containsKey(strTempInternalFormName)))
                        {
                          if(!hashHideFormFields.containsKey(strTempInternalFormName))
                          {
                            // Data is date type
                            if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))
                            {
                                java.sql.Date date = my_resultset.getDate(strTempInternalFormName);
                                if (date != null)
                                {    
                                    strTempData = date.toString();
                                }
                            }    
                            else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))
                            {
                                java.sql.Time time = my_resultset.getTime(strTempInternalFormName);
                                if (time != null)
                                {    
                                    strTempData = time.toString();
                                }
                            }    
                            else
                            {
                                strTempData = my_resultset.getString(strTempInternalFormName);   
                            }    
                          
                          }    
                        
                        }    
    

                        if(hashFormFieldDropDown != null && hashFormFieldDropDown.containsKey(strTempInternalFormName))

                        {

                        // This flag is used to tell the display xml that the drop downs have already been built. Have to use this as if the form has data the drop downs must also have the data.

                        // blDropDownsBuild = true; 

                            if(strTempData != null)

                            {

                                strXML += buildListOfValues(strTempInternalFormName, strTempData);

                            }else{strXML += buildListOfValues(strTempInternalFormName, "");}

                        }

                        else // If it's not a drop down type process has normal

                        {

                       

                            strXML += "<" + strTempInternalFormName + ">";

                            if(strTempData != null && 
				!hashHideFormFields.containsKey(strTempInternalFormName))

                            {

                                // If it's a date convert it to the correct display format 

                                if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))

                                {
                                    strXML += Utilities.convertDateForDisplay(strTempData);

                                }
                                //Anita Start - decomment
                                

                                else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.TIME_TYPE))

                                {

                                    strXML += Utilities.convertTimeForDisplay(strTempData);

                                }                                

                                else{strXML += Utilities.cleanForXSL(strTempData);}

                            }
                            else if(hashHideFormFields.containsKey(strTempInternalFormName))
                            {
				strXML += Utilities.cleanForXSL((String)hashHideFormFields.get(strTempInternalFormName));
                            }

                            strXML += "</" + strTempInternalFormName + ">";
                        }

                        

                    }

                //    strXML += "</searchResult>";

          

                  intRecordCount ++;

                }

                my_query.killResultSet(my_resultset);

                my_query = null;

        

                enumInternalFormFields = hashFormFields.keys();

                while(enumInternalFormFields.hasMoreElements())

                {

                    strTempDisplay = enumInternalFormFields.nextElement().toString();

                    strXML += "<" + strTempDisplay + "Display>" + Utilities.cleanForXSL(enumDisplayFormFields.nextElement().toString()) + "</" + strTempDisplay + "Display>";          

                //    if(hashFormFieldDropDown.containsKey(strTempDisplay))

                  //  {

                    //    strXML +=  buildListOfValues(strTempDisplay, "");

                   // }

                }

        }

        catch(java.lang.NullPointerException npe)

        {
            npe.printStackTrace();
            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);

        }

        catch(Exception e)

        {
            e.printStackTrace();
            System.err.println("Unknown error while trying to build view xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build view xml file - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to build view xml file - " + e.toString(), e);

        }

    }

    /** Sets the where condition for the updateRecord,

     * buildViewXmlFile and buildTableLOVXMLFile

     * and buildSearchResultsXMLFile

     */    

    public void setSearchWhere()

    {

         Enumeration enumInternalFormFields = hashSearchFieldOperators.keys();

         String strTempSearchField;

        int intCounter = 0;

                while(enumInternalFormFields.hasMoreElements())

                {

                    strTempSearchField = enumInternalFormFields.nextElement().toString();
                    if (runtimeData.getParameter(strTempSearchField) != null)
                        if(runtimeData.getParameter(strTempSearchField).length() > 0)

                        {

                            if(intCounter == 0)

                            {

                                setWhere("", strTempSearchField, hashSearchFieldOperators.get(strTempSearchField).toString(), runtimeData.getParameter(strTempSearchField));

                            }else{setWhere(DBSchema.AND_CONNECTOR, strTempSearchField, hashSearchFieldOperators.get(strTempSearchField).toString(), runtimeData.getParameter(strTempSearchField));}



                            intCounter ++;

                        };

                }

        

    }

    

    private boolean checkValidFields(String strAFieldToCheck, String strAFieldValue)

    {

        boolean blTestOK = true;

        // CHECK TO SEE IF THE DATA SUPPLIED IS VALID

//????        // ????????????????????????????!!!!!!!!!!!!!!!!!!!!!!!!??????????????

        if(hashValidateFields != null && hashValidateFields.containsKey(strAFieldToCheck))

        {

            if(strAFieldValue != null)

            {

             //      System.err.println("HASH=" + hashValidateFields.get(strAFieldToCheck).toString());

            //    System.err.println("FIELD TO CHECK =" + strAFieldToCheck);

            //      System.err.println("VALUE TO CHECK =" + strAFieldValue);

               blTestOK = ValidateFieldFunctions.validateFields(strAFieldValue, ((Integer)(hashValidateFields.get(strAFieldToCheck))).intValue());

            }

        }

        return blTestOK;

    }

    

    // This method get the max numbers for the fields provided and fills them into a hashtable

    /** Builds a hashTable (getMaxFields) of the maximum values

     * for a set of formFields

     * @throws BaseChannelInvalidRuntimeData Thrown when no runtimeData is provided for

     * a formField supplied

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public void getMax() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

    

        String strTempDisplay = "";

        int intMaxRecord = 0;

        Enumeration enumInternalFormFields = hashFormFields.keys();

        Enumeration enumDisplayFormFields = hashFormFields.elements();

      

        // Get the results

        try

            {

                Query my_max_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

                my_max_query.setGetMaxRecord(true);

                my_max_query.setDeletedColumn(blDeletedColumn);

                

                for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

                {

                    my_max_query.setDomainName(vecDomains.get(intCounter).toString());

                }

                while(enumInternalFormFields.hasMoreElements())

                {         

                    my_max_query.setField(enumInternalFormFields.nextElement().toString(), "");

                }

                for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

                {

                    my_max_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

                }

                for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)

                {

                    my_max_query.setOrderBy(vecOrderByFields.get(intCounter).toString(),  vecOrderByFieldDirection.get(intCounter).toString());

                }

                

   

                blSearchRecordOK = my_max_query.execute();

                ResultSet my_resultset = my_max_query.getResults();

                

                

                String strTempInternalFormName;

                while(my_resultset.next())

                {

                    enumInternalFormFields = hashFormFields.keys();

                  

                    while(enumInternalFormFields.hasMoreElements())

                    {      

                        

                        strTempInternalFormName = enumInternalFormFields.nextElement().toString();

                        // If there is are records then put in the max, otherwise put in 0.

                        if(my_resultset.getString(strTempInternalFormName) == null)

                        {

                            hashMaxFields.put(strTempInternalFormName, "0");

                        }else{hashMaxFields.put(strTempInternalFormName, my_resultset.getString(strTempInternalFormName));}

                     

                    }

                }

                 

                my_max_query.killResultSet(my_resultset);

                my_max_query = null;

                

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to build search xml file - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to build search xml file - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to build search xml file - " + e.toString(), e);

        }

    }











	

	

    /** This method builds an Vector that contains multiple

     * Hashtables  based on the formFields provided. The Hashtable keys are the same as the

     * Formfields keys, and the values are the corresponding values from the database.

     *

     * This method is usually used to get results from a search, for use within an application.

     * @return Vector containing a hashTable containing

     * the formfield name and the value from the database

     *

     * @throws BaseChannelInvalidRuntimeData Thrown when no runtimeData is provided for a formField supplied

     * @throws BaseChannelException Thrown when an unknown error occurs

     */    

    public Vector lookupRecord() throws BaseChannelInvalidRuntimeData, BaseChannelException

    {

        String strTempDisplay = "";

        Enumeration enumInternalFormFields = hashFormFields.keys();

      

        // Get the results

        try

            {

                Query my_query = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

                my_query.setDeletedColumn(blDeletedColumn);

                my_query.setCaseSensitive(blTurnLowerCaseOff);

                my_query.setUseDefaultJoin(blUseDefaultJoin);

                

                if (!blUseDefaultJoin)

                    for (int intCounter=0; intCounter < vecJoinDomainNames.size(); intCounter++)

                    {

                        my_query.setJoinDomain(vecJoinTypes.get(intCounter), 

                                               vecJoinDomainNames.get(intCounter),

                                               vecFirstJoinFieldNames.get(intCounter),

                                               vecSecondJoinFieldNames.get(intCounter));

                    }

                

                for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

                {

                    my_query.setDomainName(vecDomains.get(intCounter).toString());

                }

                while(enumInternalFormFields.hasMoreElements())

                {         

                    my_query.setField(enumInternalFormFields.nextElement().toString(), "");

                }

                // Old 

                // for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

                // {

                    

                   // my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

                //}

                // New with brackets!

                

                for(int intCounter = 0; intCounter < vecWhereFields.size(); intCounter++)

                {

                    if(vecOpenWhereBracket.contains(new Integer(intCounter)))

                    {

                         my_query.setOpenWhereBracket();

                    }

                    my_query.setWhere(vecWhereConnector.get(intCounter).toString(),vecWhereFields.get(intCounter).toString(),  vecWhereOperator.get(intCounter).toString(), vecWhereFieldValue.get(intCounter).toString());

                    

                    if(vecCloseWhereBracket.contains(new Integer(intCounter)))

                    {

                       my_query.setCloseWhereBracket(vecWhereBracketConnector.get(vecCloseWhereBracket.indexOf(new Integer(intCounter))).toString());

                 

                    }

                }

                

                for (int intCounter = 0; intCounter < vecWhereStatements.size(); intCounter++)

                {

                    my_query.setWhereStatement(vecWhereStatConnectors.get(intCounter), vecWhereStatements.get(intCounter));

                }

                    

                for(int intCounter = 0; intCounter < vecOrderByFields.size(); intCounter++)

                {

                    my_query.setOrderBy(vecOrderByFields.get(intCounter).toString(),  vecOrderByFieldDirection.get(intCounter).toString());

                }

         

                //paging

                my_query.setStartRecord(intStartRecord);

                my_query.setRecordPerPage(intRecordPerPage);

                

                

                for (int i=0; i < vecBaseChannels.size(); i++)

                {

                    BaseChannel bcTemp = (BaseChannel) vecBaseChannels.get(i);

                    

                    Query query_temp = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

                    

                    query_temp.setDeletedColumn(bcTemp.blDeletedColumn);

                    query_temp.setCaseSensitive(bcTemp.blTurnLowerCaseOff);

                    query_temp.setUseDefaultJoin(bcTemp.isUseDefaultJoin());



                    if (!bcTemp.isUseDefaultJoin())

                    {

                        Vector vecJoinDomainNamesTemp = bcTemp.getJoinDomainNames();

                        Vector vecJoinTypesTemp = bcTemp.getJoinTypes();

                        Vector vecFirstJoinFieldNamesTemp = bcTemp.getFirstJoinFieldNames();

                        Vector vecSecondJoinFieldNamesTemp = bcTemp.getSecondJoinFieldNames();

                        for (int intCounter=0; intCounter < vecJoinDomainNamesTemp.size(); intCounter++)

                        {

                            query_temp.setJoinDomain(vecJoinTypesTemp.get(intCounter), 

                                                   vecJoinDomainNamesTemp.get(intCounter),

                                                   vecFirstJoinFieldNamesTemp.get(intCounter),

                                                   vecSecondJoinFieldNamesTemp.get(intCounter));

                        }

                    }



                    Vector vecDomainsTemp = bcTemp.getDomains();

                    for(int intCounter = 0; intCounter < vecDomainsTemp.size(); intCounter++)

                    {

                        query_temp.setDomainName(vecDomainsTemp.get(intCounter).toString());

                    }

                    

                    Enumeration enumTemp = bcTemp.getFormField().keys();

                    while(enumTemp.hasMoreElements())

                    {         

                        query_temp.setField(enumTemp.nextElement().toString(), "");

                    }

                    

                    Vector vecWhereFieldsTemp = bcTemp.getWhereFields();

                    Vector vecOpenWhereBracketTemp = bcTemp.getOpenWhereBracket();

                    Vector vecCloseWhereBracketTemp = bcTemp.getCloseWhereBracket();

                    Vector vecWhereBracketConnectorTemp = bcTemp.getWhereBracketConnector();

                    Vector vecWhereConnectorTemp = bcTemp.getWhereConnector();

                    Vector vecWhereOperatorTemp = bcTemp.getWhereOperator();

                    Vector vecWhereFieldValueTemp = bcTemp.getWhereFieldValue();

                    for(int intCounter = 0; intCounter < vecWhereFieldsTemp.size(); intCounter++)

                    {

                        if(vecOpenWhereBracketTemp.contains(new Integer(intCounter)))

                        {

                             query_temp.setOpenWhereBracket();

                        }

                        query_temp.setWhere(vecWhereConnectorTemp.get(intCounter).toString(),vecWhereFieldsTemp.get(intCounter).toString(), vecWhereOperatorTemp.get(intCounter).toString(), vecWhereFieldValueTemp.get(intCounter).toString());



                        if(vecCloseWhereBracketTemp.contains(new Integer(intCounter)))

                        {

                           query_temp.setCloseWhereBracket(vecWhereBracketConnectorTemp.get(vecCloseWhereBracketTemp.indexOf(new Integer(intCounter))).toString());

                        }

                    }

                    

                    my_query.setQuery(query_temp, vecBaseChannelConnectors.get(i));

                }

              

                for (int i=0; i < vecWhereBaseChannels.size(); i++)

                {

                    BaseChannel bcTemp = (BaseChannel) vecBaseChannels.get(i);

                    

                    Query query_temp = new Query(Query.SELECT_QUERY, strActivityRequested, authToken);

                    

                    query_temp.setDeletedColumn(bcTemp.blDeletedColumn);

                    query_temp.setCaseSensitive(bcTemp.blTurnLowerCaseOff);

                    query_temp.setUseDefaultJoin(bcTemp.isUseDefaultJoin());



                    if (!bcTemp.isUseDefaultJoin())

                    {

                        Vector vecJoinDomainNamesTemp = bcTemp.getJoinDomainNames();

                        Vector vecJoinTypesTemp = bcTemp.getJoinTypes();

                        Vector vecFirstJoinFieldNamesTemp = bcTemp.getFirstJoinFieldNames();

                        Vector vecSecondJoinFieldNamesTemp = bcTemp.getSecondJoinFieldNames();

                        for (int intCounter=0; intCounter < vecJoinDomainNamesTemp.size(); intCounter++)

                        {

                            query_temp.setJoinDomain(vecJoinTypesTemp.get(intCounter), 

                                                   vecJoinDomainNamesTemp.get(intCounter),

                                                   vecFirstJoinFieldNamesTemp.get(intCounter),

                                                   vecSecondJoinFieldNamesTemp.get(intCounter));

                        }

                    }



                    Vector vecDomainsTemp = bcTemp.getDomains();

                    for(int intCounter = 0; intCounter < vecDomainsTemp.size(); intCounter++)

                    {

                        query_temp.setDomainName(vecDomainsTemp.get(intCounter).toString());

                    }

                    

                    Enumeration enumTemp = bcTemp.getFormField().keys();

                    while(enumTemp.hasMoreElements())

                    {         

                        query_temp.setField(enumTemp.nextElement().toString(), "");

                    }

                    

                    Vector vecWhereFieldsTemp = bcTemp.getWhereFields();

                    Vector vecOpenWhereBracketTemp = bcTemp.getOpenWhereBracket();

                    Vector vecCloseWhereBracketTemp = bcTemp.getCloseWhereBracket();

                    Vector vecWhereBracketConnectorTemp = bcTemp.getWhereBracketConnector();

                    Vector vecWhereConnectorTemp = bcTemp.getWhereConnector();

                    Vector vecWhereOperatorTemp = bcTemp.getWhereOperator();

                    Vector vecWhereFieldValueTemp = bcTemp.getWhereFieldValue();

                    for(int intCounter = 0; intCounter < vecWhereFieldsTemp.size(); intCounter++)

                    {

                        if(vecOpenWhereBracketTemp.contains(new Integer(intCounter)))

                        {

                             query_temp.setOpenWhereBracket();

                        }

                        query_temp.setWhere(vecWhereConnectorTemp.get(intCounter).toString(),vecWhereFieldsTemp.get(intCounter).toString(), vecWhereOperatorTemp.get(intCounter).toString(), vecWhereFieldValueTemp.get(intCounter).toString());



                        if(vecCloseWhereBracketTemp.contains(new Integer(intCounter)))

                        {

                           query_temp.setCloseWhereBracket(vecWhereBracketConnectorTemp.get(vecCloseWhereBracketTemp.indexOf(new Integer(intCounter))).toString());

                        }

                    }

                    

                    my_query.setWhereQuery(vecWhereBaseChannelConnectors.get(i), 

                                           vecWhereBaseChannelFields.get(i),

                                           vecWhereBaseChannelOperators.get(i), query_temp);

                }

                

                blSearchRecordOK = my_query.execute();

                ResultSet my_resultset = my_query.getResults();

                           

                String strTempInternalFormName;

		Hashtable hashResults;

		Vector vecResults = new Vector();

     

                while(my_resultset.next())

                {

                 

                    enumInternalFormFields = hashFormFields.keys();

		    hashResults = new Hashtable();

                    for(int intCounter = 0; intCounter < vecDomains.size(); intCounter++)

                    {

                   //         System.err.println(vecDomains.get(intCounter).toString() + "_Timestamp" + ": " + my_resultset.getString(vecDomains.get(intCounter).toString() + "_Timestamp"));
                              
			//	hashResults.put(vecDomains.get(intCounter).toString() + "_Timestamp" , my_resultset.getString(vecDomains.get(intCounter).toString() + "_Timestamp")); 

                    }

                    

                    while(enumInternalFormFields.hasMoreElements())

                    {

                        strTempInternalFormName = enumInternalFormFields.nextElement().toString();

                        {

                            if(my_resultset.getString(strTempInternalFormName) != null)

                            {

                                // If it's a date convert it to the correct display format 

                                if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.DATE_TYPE))

                                {

					hashResults.put(strTempInternalFormName, Utilities.convertDateForDisplay(my_resultset.getDate(strTempInternalFormName).toString()));

                                }

                                else if(hashDBFieldTypes.get(strTempInternalFormName).equals(DBSchema.TIME_TYPE))

                                {

					hashResults.put(strTempInternalFormName, Utilities.convertTimeForDisplay(my_resultset.getTime(strTempInternalFormName).toString()));

                                }

                                else

				{

					hashResults.put(strTempInternalFormName, my_resultset.getString(strTempInternalFormName));

				}

                            }

                        }

                        

                    }

			vecResults.add(hashResults);

			intRecordCount ++;

                }

                my_query.killResultSet(my_resultset);

                my_query = null;

		return vecResults;

        

        }

        catch(java.lang.NullPointerException npe)

        {

            System.err.println("BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " + npe.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelInvalidRuntimeData - No runtimeData provided for field supplied - " +  npe.toString(), npe);

            throw new BaseChannelInvalidRuntimeData("No runtimeData provided for field supplied - " + npe.toString(), npe);

        }

        catch(Exception e)

        {

            System.err.println("Unknown error while trying to do lookupRecord - " + e.toString());

            LogService.instance().log(LogService.ERROR, "BaseChannelException - Unknown error while trying to do lookupRecord - " +  e.toString(), e);

            throw new BaseChannelException("Unknown error while trying to do lookupRecord- " + e.toString(), e);

        }

    }

	

	public String getWebServiceFile(String server, String path, String query)

	{

		WebServiceAccessor wsaTemp = new WebServiceAccessor();

		wsaTemp.get(server, path, query);

		return wsaTemp.getString();

	}

   

	

   // paging

    public void setLimit(int startRecord, int recordPerPage) {

        intStartRecord = startRecord;

        intRecordPerPage = recordPerPage;

    }

    

    public int getRecordSetSize() {

        return intRecordSetSize;

    }



    public void setUseDefaultJoin(boolean blTemp) {

        blUseDefaultJoin = blTemp;

        vecJoinDomainNames.clear();

        vecJoinTypes.clear();

        vecFirstJoinFieldNames.clear();

        vecSecondJoinFieldNames.clear();

    }

    

    public boolean isUseDefaultJoin() {

        return blUseDefaultJoin;

    }

    

    public void setJoinDomain(Object objJoinType, Object objDomain, 

                    Object objField1, Object objField2)

    {

        vecJoinDomainNames.add(objDomain);

        vecJoinTypes.add(objJoinType);

        vecFirstJoinFieldNames.add(objField1);

        vecSecondJoinFieldNames.add(objField2);

    }

    

    public Vector getJoinDomainNames() {

        return vecJoinDomainNames;

    }

    

    public Vector getJoinTypes() {

        return vecJoinTypes;

    }

    

    public Vector getFirstJoinFieldNames() {

        return vecFirstJoinFieldNames;

    }

    

    public Vector getSecondJoinFieldNames() {

        return vecSecondJoinFieldNames;

    }

    

    public Vector getDomains() {

        return vecDomains;

    }

    

    public Hashtable getFormField() {

        return hashFormFields;

    }

    

    public Vector getWhereFields() {

        return vecWhereFields;

    }

    

    public Vector getWhereConnector() {

        return vecWhereConnector;

    }

    

    public Vector getWhereOperator() {

        return vecWhereOperator;

    }

    

    public Vector getWhereBracketConnector() {

        return vecWhereBracketConnector;

    }

    

    public Vector getOpenWhereBracket() {

        return vecOpenWhereBracket;

    }

    

    public Vector getCloseWhereBracket() {

        return vecCloseWhereBracket;

    }

    

    public Vector getWhereFieldValue() {

        return vecWhereFieldValue;

    }

    

    public void setBaseChannel(String connector, BaseChannel bc)

    {

        vecBaseChannelConnectors.add(connector);

        vecBaseChannels.add(bc);

    }

    

    public void setWhereBaseChannel(String connector, String fieldName, String operator, BaseChannel bc)

    {

        vecWhereBaseChannelConnectors.add(connector);

        vecWhereBaseChannelFields.add(fieldName);

        vecWhereBaseChannelOperators.add(operator);

        vecWhereBaseChannels.add(bc);

    }

    

    public Vector getWhereBaseChannels() {

        return vecWhereBaseChannels;

    }

    

    public Vector getWhereBaseChannelConnectors() {

        return vecWhereBaseChannelConnectors;

    }

    

    public Vector getWhereBaseChannelOperators() {

        return vecWhereBaseChannelOperators;

    }

    

    public Vector getWhereBaseChannelFields() {

        return vecWhereBaseChannelFields;

    }

    public void removeField(String fieldName)
    {
        if (hashFormFields.containsKey(fieldName))
            hashFormFields.remove(fieldName);
        
    }
    
    public String getFieldValue(String fieldName)
    {
        
        if (hashFormFields.containsKey(fieldName))
           return (String) hashFormFields.get(fieldName);
        return null;
    }
    
    public void addField(String fieldName, String value)
    {
        
        if (!hashFormFields.containsKey(fieldName))
            hashFormFields.put(fieldName, value);
        
    }
    
    
    
    

    public void setWhereStatements(String conn, String statement) {

        vecWhereStatements.add(statement);

        vecWhereStatConnectors.add(conn);

    }

    

    public void clearWhereStatement() {

        vecWhereStatements.clear();

        vecWhereStatConnectors.clear();

    }

}

