/**
 * DatabaseSchema.java
 * Copyright (C) 2005 Neuragenix Pty Ltd
 *
 * $Id $
 */
 
package neuragenix.dao;

/**
 * Class to specify a database schema
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

import java.util.Vector;
import java.util.Hashtable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
 
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import org.jasig.portal.PropertiesManager;

public class DatabaseSchema
{
    
    /** Indicate the DBMS type. Default is Postgres
     */
    private static int intDBMSType = DBMSTypes.POSTGRES;
    
    /** Keep domains
     */
    private static Hashtable hashDomains = new Hashtable(20);
    
    /** Keep domain's short name
     */
    private static Hashtable hashShortDomainNames = new Hashtable(20);
    
    /** Keep sequences
     */
    private static Hashtable hashSequences = new Hashtable(20);
    
    /** Keep primary keys
     */
    private static Hashtable hashPrimaryKeys = new Hashtable(20);
    
    /** Keep domains have DELETED fields
     */
    private static Hashtable hashDeletedFieldDomains = new Hashtable(20);
    
    /** Keep domains appears in search channel
     */
    private static Hashtable hashSearchDomains = new Hashtable(10);
    
    /** Keep activities that need to search on domains
     */
    private static Hashtable hashSearchDomainActivities = new Hashtable(10);
    
    /** Keep fields
     */
    private static Hashtable hashFields = new Hashtable(200);
    
    
    /** Keep fields indexed by the external name
     */
    private static Hashtable hashFieldsByExternalName = new Hashtable(200);
    
    
    /** Keep short fields
     */
    private static Hashtable hashShortFields = new Hashtable(200);
   
    /** Unique fields
     */
    private static Hashtable hashUniqueFields = new Hashtable(50);
   
    /** Keep formfields
     */
    private static Hashtable<String,Vector<String>> hashFormFields = new Hashtable(50);

    /** Keep lookup objects
     */
    private static Hashtable hashLookupObjects = new Hashtable(20);
    
    /** Indicate if the files was loaded
     */
    private static Hashtable hashLoaded = new Hashtable(10);
    
    /** Indicate the maximum size of CLOB object
     */
    private static int intMaxClobSize = 1000000;
    
   private  static int intClobTypeThreshold = 4000;
                                
    
    
    // load core domains & formfields
    static 
    {
        // get DBMS type
        if (PropertiesManager.getProperty("neuragenix.dao.DBMSType") != null) {
            String strDBMSName = PropertiesManager.getProperty("neuragenix.dao.DBMSType");
            
            if (strDBMSName.equalsIgnoreCase("oracle")) {
                intDBMSType = DBMSTypes.ORACLE;
            }
            else if (strDBMSName.equalsIgnoreCase("sqlserver")) {
                intDBMSType = DBMSTypes.SQLSERVER;
            }
        }
        
        try
        {
            intClobTypeThreshold = PropertiesManager.getPropertyAsInt("neuragenix.dao.ClobThreshold");
        }
        catch (Exception ex)
        {
            System.err.println("[Database Schema] Could not find properties neuragenix.dao.ClobThreshold. Proceeded with default value of 4000");
            intClobTypeThreshold = 4000;
        }
        

         // get max CLOB size
        if (PropertiesManager.getProperty("neuragenix.dao.MaxClobSize") != null) {
            String strMaxClobSize = PropertiesManager.getProperty("neuragenix.dao.MaxClobSize");
            
            try {
                intMaxClobSize = Integer.parseInt(strMaxClobSize);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        
        InputStream isDBSchemaManifest = DatabaseSchema.class.getResourceAsStream("DBSchemaManifest.mf");
        
        if (isDBSchemaManifest == null)
        {
            System.err.println ("[DatabaseSchema - FATAL] - Unable to read DBSchemaManifest - System will not be able to continue loading");
        }
        
        BufferedReader brDBSchemaManifest = new BufferedReader(new InputStreamReader(isDBSchemaManifest));
        
        try {
            String strDBSchemaName = brDBSchemaManifest.readLine();
            while (strDBSchemaName != null) {
                InputStream file = DatabaseSchema.class.getResourceAsStream(strDBSchemaName);
                loadDomains(file, strDBSchemaName);
                strDBSchemaName = brDBSchemaManifest.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                brDBSchemaManifest.close();
                brDBSchemaManifest = null;
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        
        /*InputStream file = DatabaseSchema.class.getResourceAsStream("CoreDBSchema.xml");
        loadDomains(file, "CoreDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CAlertDBSchema.xml");
        loadDomains(file, "CAlertDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CCaseAssignmentDBSchema.xml");
        loadDomains(file, "CCaseAssignmentDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CCaseAutoDocDBSchema.xml");
        loadDomains(file, "CCaseAutoDocDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CCaseDBSchema.xml");
        loadDomains(file, "CCaseDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CContentManagerDBSchema.xml");
        loadDomains(file, "CContentManagerDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CDomainWorkflowDBSchema.xml");
        loadDomains(file, "CDomainWorkflowDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("COrgChartDBSchema.xml");
        loadDomains(file, "COrgChartDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("COrgTreeDBSchema.xml");
        loadDomains(file, "COrgTreeDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CRelatedPartiesDBSchema.xml");
        loadDomains(file, "CRelatedPartiesDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CReportDBSchema.xml");
        loadDomains(file, "CReportDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CSmartformAdminDBSchema.xml");
        loadDomains(file, "CSmartformAdminDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CSmartformDBSchema.xml");
        loadDomains(file, "CSmartformDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CTemplateDBSchema.xml");
        loadDomains(file, "CTemplateDBSchema.xml");

        file = DatabaseSchema.class.getResourceAsStream("CUserAdminDBSchema.xml");
        loadDomains(file, "CUserAdminDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CUserGroupDataDBSchema.xml");
        loadDomains(file, "CUserGroupDataDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CWorkflowDBSchema.xml");
        loadDomains(file, "CWorkflowDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("CWorkspaceDBSchema.xml");
        loadDomains(file, "CWorkspaceDBSchema.xml");
        
        file = DatabaseSchema.class.getResourceAsStream("uPortalDBSchema.xml");
        loadDomains(file, "uPortalDBSchema.xml");*/
        
    }
    
    /** Creates a new instance of DatabaseSchema 
     */
    public DatabaseSchema() 
    {
    }
    /** Return the unique field flag
     */
    public static Hashtable getUniqueFields()
    {
        return hashUniqueFields;
    }
    
    
    /** Return the DBMS type of the database
     */
    public static int getDBMSType()
    {
        return intDBMSType;
    }
    
    /** Return a hashtable that contains domains of the database
     */
    public static Hashtable getDomains()
    {
        return hashDomains;
    }
    
    /** Return a hashtable that contains domains for seach channel
     */
    public static Hashtable getSearchDomains()
    {
        return hashSearchDomains;
    }
    
    /** Return a hashtable that contains fields of the database
     */
    public static Hashtable getFields()
    {
        return hashFields;
    }
    
    public static Hashtable getFieldsByExternalName()
    {
       return hashFieldsByExternalName;
    }
    
    /** Return a hashtable that contains short fields of the database
     */
    public static Hashtable getShortFields()
    {
        return hashShortFields;
    }
    
    /** Return a vector that represent a formfields
     */
    @SuppressWarnings("unchecked")
	public static Vector<String> getFormFields(String strFormFieldName)
    { 
        Vector<String> vtReturnData = (Vector<String>) hashFormFields.get(strFormFieldName);
        if (vtReturnData == null) System.err.println ("[Database Schema] - Invalid Form Fields Request : " + strFormFieldName);
        //just return a copy of formfields only not reference to the original
        return (Vector<String>) vtReturnData.clone();
    }
    
    /** Return true if the domain has DELETED field
     */
    public static boolean isDomainHasDeletedField(String strDomainName)
    {
        return hashDeletedFieldDomains.containsKey(strDomainName);
    }
    
    /** Return activity required to search the domain
     */
    public static String getActivityRequired(String strDomainName)
    {
        return (String) hashSearchDomainActivities.get(strDomainName);
    }
    
    /** Return sequence associated with a domain
     *  @param strDomainName: a domain name
     *  @return : a sequence
     */
    public static String getSequence(String strDomainName)
    {
        return (String) hashSequences.get(strDomainName);
    }
    
    /** Return primary key associated with a domain
     *  @param strDomainName: a domain name
     *  @return : a key
     */
    
    public static String getPrimaryKey(String strDomainName) {
        return (String) hashPrimaryKeys.get(strDomainName);
    }
    
    /** Return short domain name associated with a domain
     *  @param strDomainName: a domain name
     *  @return : a short domain name
     */
    public static String getShortDomainName(String strDomainName)
    {
        return (String) hashShortDomainNames.get(strDomainName);
    }
    
    /** Load the database schema
     */
    public static void loadDomains(InputStream file, String strFileName)
    {
        
        if (file == null)
        {
            System.err.println ("[Database Schema] : Warning - Attempted to load an empty stream or non-existant file");
            System.err.println ("[Database Schema] : File name was '" + strFileName + "'");
            return;
        }
        
        if (!hashLoaded.containsKey(strFileName))
        {
            // create a document factory instance
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document;
            
            try
            {
                // parse the XML file
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(file);

                // add domains
                NodeList domainNodes = document.getElementsByTagName("domain");
                for (int index=0; index < domainNodes.getLength(); index++)
                {
                    Node currentDomain = domainNodes.item(index); //get current domain
                    NamedNodeMap domainAttributes = currentDomain.getAttributes();
                    String strIDomainName = domainAttributes.getNamedItem("iname").getNodeValue();
                    String strShortDomainName = strIDomainName;
                    if (domainAttributes.getNamedItem("sname") != null) {
                        strShortDomainName = domainAttributes.getNamedItem("sname").getNodeValue();
                    }
                    
                    String strEDomainName = DBMSTypes.getDomain(domainAttributes.getNamedItem("ename").getNodeValue());
                    String strSequenceName = null;
                    String strPrimaryKeyName = null;
                    
                    if (domainAttributes.getNamedItem("sequence") != null) {
                        strSequenceName = domainAttributes.getNamedItem("sequence").getNodeValue();
                    }
                    
                    if (domainAttributes.getNamedItem("primarykey") != null) {
                        strPrimaryKeyName = domainAttributes.getNamedItem("primarykey").getNodeValue();    
                    }
                    
                    if (domainAttributes.getNamedItem("deletedfield") != null)
                        if (!hashDeletedFieldDomains.containsKey(strIDomainName))
                            hashDeletedFieldDomains.put(strIDomainName, "");
                    
                    if (!hashDomains.containsKey(strIDomainName))
                    {
                        hashDomains.put(strIDomainName, strEDomainName);

                        if (strSequenceName != null) {
                            hashSequences.put(strIDomainName, strSequenceName);
                        }

                        if (strPrimaryKeyName != null) {
                            hashPrimaryKeys.put(strIDomainName, strPrimaryKeyName);
                        }
                        
                        NodeList fieldNodes = currentDomain.getChildNodes();
                        // add fields for this domain
                        for (int index1=0; index1 < fieldNodes.getLength(); index1++)
                        {
                            Node currentField = fieldNodes.item(index1); // get a field the domain
                            
                            if (currentField.getNodeType() == Node.ELEMENT_NODE)
                            {
                                NamedNodeMap fieldAttributes = currentField.getAttributes();

                                String strFieldName = fieldAttributes.getNamedItem("iname").getNodeValue();
                                String strShortFieldName = strFieldName;
                                if (fieldAttributes.getNamedItem("sname") != null) {
                                    strShortFieldName = fieldAttributes.getNamedItem("sname").getNodeValue();
                                }
                                //String strShortFieldName = fieldAttributes.getNamedItem("sname").getNodeValue();
                                String strInternalName = DBMSTypes.getField(strIDomainName + "_" + strFieldName);
                                String strShortInternalName = strShortDomainName + "_" + strShortFieldName;
                                String strEName = fieldAttributes.getNamedItem("ename").getNodeValue();
                                if (intDBMSType == DBMSTypes.ORACLE) {
                                    strEName = strEName.toUpperCase();
                                }
                                
                                String strExternalName = strEDomainName + "." + DBMSTypes.getField(strEName);
                                String strNameForUpdate = DBMSTypes.getField(strEName);
                                
                                
                               
                                
                                String strLabelName = fieldAttributes.getNamedItem("lname").getNodeValue();
                                String strLabelInColumnName = fieldAttributes.getNamedItem("cname").getNodeValue();
                                String strDataType = fieldAttributes.getNamedItem("type").getNodeValue();
                                int intLength = Integer.parseInt(fieldAttributes.getNamedItem("length").getNodeValue());
                                boolean blUnique = Boolean.valueOf(fieldAttributes.getNamedItem("unique").getNodeValue()).booleanValue();
                                boolean blRequired = Boolean.valueOf(fieldAttributes.getNamedItem("required").getNodeValue()).booleanValue();
                                int intDataType = 0;

                                if (strDataType.equalsIgnoreCase("integer"))
                                    intDataType = DBMSTypes.INT_TYPE;
                                else if (strDataType.equalsIgnoreCase("float"))
                                    intDataType = DBMSTypes.FLOAT_TYPE;
                                else if (strDataType.equalsIgnoreCase("double"))
                                    intDataType = DBMSTypes.DOUBLE_TYPE;
                                else if (strDataType.equalsIgnoreCase("string"))
                                    intDataType = DBMSTypes.STR_TYPE;
                                else if (strDataType.equalsIgnoreCase("date"))
                                    intDataType = DBMSTypes.DATE_TYPE;
                                else if (strDataType.equalsIgnoreCase("time"))
                                    intDataType = DBMSTypes.TIME_TYPE;
                                else if (strDataType.equalsIgnoreCase("duration"))
                                    intDataType = DBMSTypes.DURATION_TYPE;
                                else if (strDataType.equalsIgnoreCase("clob"))
                                    intDataType = DBMSTypes.CLOB_TYPE;
                                
                               
                                
                                
                                if (intDBMSType == DBMSTypes.ORACLE && intLength >= intClobTypeThreshold  && intDataType == DBMSTypes.STR_TYPE)
                                {
                                    intDataType = DBMSTypes.CLOB_TYPE;
                                }
                                else if (intDBMSType != DBMSTypes.ORACLE && intDataType ==  DBMSTypes.CLOB_TYPE)
                                {
                                    intDataType = DBMSTypes.STR_TYPE;
                                }
                                DBField field = new DBField(strIDomainName, strNameForUpdate, strInternalName, strShortInternalName, strExternalName, strLabelName, strLabelInColumnName, intDataType, intLength, blUnique, blRequired);
                                
                                
                                // get the type of date input box
                                if (fieldAttributes.getNamedItem("display_type") != null){
                                    field.setDisplayType(fieldAttributes.getNamedItem("display_type").getNodeValue());
                                    //System.out.println("Display type " +field.getDisplayType());
                                }
                                if (fieldAttributes.getNamedItem("lov_type") != null)
                                    field.setLOVType(fieldAttributes.getNamedItem("lov_type").getNodeValue());
                                
                                // get default date
                                if (fieldAttributes.getNamedItem("default_date") != null)
                                    field.setDefaultDate(fieldAttributes.getNamedItem("default_date").getNodeValue());
                                
                                if (fieldAttributes.getNamedItem("val_pattern") != null)
                                    field.setValidationPattern(fieldAttributes.getNamedItem("val_pattern").getNodeValue());
                                
                                // add the field
                                hashFields.put(strIDomainName + "_" + strFieldName, field);
                                hashShortFields.put(strShortDomainName + "_" + strShortFieldName, field);
                                hashFieldsByExternalName.put(strIDomainName + "_" + (field.getNameForUpdate().substring(1, field.getNameForUpdate().length() -1)), field);
                                
                                // Add unqiue fields to the table
                                if ( blUnique ) hashUniqueFields.put(strIDomainName, strFieldName);
                            }
                        }
                    }
                    // if the domain was loaded, add only new fields
                    else
                    {
                        NodeList fieldNodes = currentDomain.getChildNodes();
                        // add fields for this domain
                        for (int index1=0; index1 < fieldNodes.getLength(); index1++)
                        {
                            Node currentField = fieldNodes.item(index1);
                            
                            if (currentField.getNodeType() == Node.ELEMENT_NODE)
                            {
                                NamedNodeMap fieldAttributes = currentField.getAttributes();

                                String strFieldName = fieldAttributes.getNamedItem("iname").getNodeValue();
                                String strInternalName = DBMSTypes.getField(strIDomainName + "_" + strFieldName);
                                String strExternalName = strEDomainName + "." + DBMSTypes.getField(fieldAttributes.getNamedItem("ename").getNodeValue());
                                String strNameForUpdate = DBMSTypes.getField(fieldAttributes.getNamedItem("ename").getNodeValue());
                                String strLabelName = fieldAttributes.getNamedItem("lname").getNodeValue();
                                String strLabelInColumnName = fieldAttributes.getNamedItem("cname").getNodeValue();
                                String strDataType = fieldAttributes.getNamedItem("type").getNodeValue();
                                int intLength = Integer.parseInt(fieldAttributes.getNamedItem("length").getNodeValue());
                                boolean blUnique = Boolean.valueOf(fieldAttributes.getNamedItem("unique").getNodeValue()).booleanValue();
                                boolean blRequired = Boolean.valueOf(fieldAttributes.getNamedItem("required").getNodeValue()).booleanValue();
                                int intDataType = 0;

                                if (strDataType.equalsIgnoreCase("integer"))
                                    intDataType = DBMSTypes.INT_TYPE;
                                else if (strDataType.equalsIgnoreCase("float"))
                                    intDataType = DBMSTypes.FLOAT_TYPE;
                                else if (strDataType.equalsIgnoreCase("double"))
                                    intDataType = DBMSTypes.DOUBLE_TYPE;
                                else if (strDataType.equalsIgnoreCase("string"))
                                    intDataType = DBMSTypes.STR_TYPE;
                                else if (strDataType.equalsIgnoreCase("date"))
                                    intDataType = DBMSTypes.DATE_TYPE;
                                else if (strDataType.equalsIgnoreCase("time"))
                                    intDataType = DBMSTypes.TIME_TYPE;
                                else if (strDataType.equalsIgnoreCase("duration"))
                                    intDataType = DBMSTypes.DURATION_TYPE;
                                else if (strDataType.equalsIgnoreCase("clob"))
                                    intDataType = DBMSTypes.CLOB_TYPE;

                                DBField field = new DBField(strIDomainName, strNameForUpdate, strInternalName, strExternalName, strLabelName, strLabelInColumnName, intDataType, intLength, blUnique, blRequired);
                                //System.out.println("About the to get display Type");
                                // get the type of date input box 
                                if (fieldAttributes.getNamedItem("display_type") != null){
                                    field.setDisplayType(fieldAttributes.getNamedItem("display_type").getNodeValue());
                                    //System.out.println("Display type " +field.getDisplayType());
                                }
                                
                                
                                if (fieldAttributes.getNamedItem("lov_type") != null)
                                    field.setLOVType(fieldAttributes.getNamedItem("lov_type").getNodeValue());
                                
                                // get default date
                                if (fieldAttributes.getNamedItem("default_date") != null)
                                    field.setDefaultDate(fieldAttributes.getNamedItem("default_date").getNodeValue());
                                
                                if (fieldAttributes.getNamedItem("val_pattern") != null)
                                    field.setValidationPattern(fieldAttributes.getNamedItem("val_pattern").getNodeValue());
                                
                                // add the field
                                if (!hashFields.containsKey(strIDomainName + "_" + strFieldName))
                                    hashFields.put(strIDomainName + "_" + strFieldName, field);
                                
                                // Add unqiue fields to the table
                                if ( blUnique ) hashUniqueFields.put(strIDomainName, strFieldName);
                            }
                        }
                    }
                }
                
                // add formfields
                NodeList formfieldNodes = document.getElementsByTagName("formfield");
                for (int index=0; index < formfieldNodes.getLength(); index++)
                {
                    Node currentFormField = formfieldNodes.item(index);
                    NamedNodeMap formfieldAttributes = currentFormField.getAttributes();
                    String formfieldName = formfieldAttributes.getNamedItem("name").getNodeValue();
                    NodeList fieldNodes = currentFormField.getChildNodes();

                    Vector vtFormFields = new Vector(10, 5);
                    for (int index1=0; index1 < fieldNodes.getLength(); index1++)
                    {
                        Node currentField = fieldNodes.item(index1);
                        if (currentField.getNodeType() == Node.ELEMENT_NODE)
                        {
                            NamedNodeMap fieldAttributes = currentField.getAttributes();
                            vtFormFields.add(fieldAttributes.getNamedItem("name").getNodeValue());
                        }
                    }
                    hashFormFields.put(formfieldName, vtFormFields);
                }
                
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                try {
                    file.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            hashLoaded.put(strFileName, "loaded");
        }
    }
    
    /** Load the database schema for search channel
     */
    public static void loadSearchDomains(InputStream file, String strFileName)
    {
        if (!hashLoaded.containsKey(strFileName))
        {
            // create a document factory instance
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document;

            try
            {
                // parse the XML file
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(file);

                // add domains
                NodeList domainNodes = document.getElementsByTagName("domain");
                for (int index=0; index < domainNodes.getLength(); index++)
                {
                    Node currentDomain = domainNodes.item(index);
                    NamedNodeMap domainAttributes = currentDomain.getAttributes();
                    String strIDomainName = domainAttributes.getNamedItem("iname").getNodeValue();
                    String strEDomainName = DBMSTypes.getDomain(domainAttributes.getNamedItem("ename").getNodeValue());
                    String strShortDomainName = strIDomainName;
                    if (domainAttributes.getNamedItem("sname") != null) {
                        strShortDomainName = domainAttributes.getNamedItem("sname").getNodeValue();
                    }
                    
                    // check if the domains has deleted field
                    if (domainAttributes.getNamedItem("deletedfield") != null)
                        if (!hashDeletedFieldDomains.containsKey(strIDomainName))
                            hashDeletedFieldDomains.put(strIDomainName, "");
                    
                    // activity to search on this domain
                    if (domainAttributes.getNamedItem("activity") != null)
                        if (!hashSearchDomainActivities.containsKey(strIDomainName))
                            hashSearchDomainActivities.put(strIDomainName, domainAttributes.getNamedItem("activity").getNodeValue());
                    
                    // add to search domains list
                    hashSearchDomains.put(strIDomainName, new Hashtable(5));
                    
                    if (!hashDomains.containsKey(strIDomainName))
                    {
                        hashDomains.put(strIDomainName, strEDomainName);

                        NodeList fieldNodes = currentDomain.getChildNodes();
                        // add fields for this domain
                        for (int index1=0; index1 < fieldNodes.getLength(); index1++)
                        {
                            Node currentField = fieldNodes.item(index1);
                            
                            if (currentField.getNodeType() == Node.ELEMENT_NODE)
                            {
                                NamedNodeMap fieldAttributes = currentField.getAttributes();

                                String strFieldName = fieldAttributes.getNamedItem("iname").getNodeValue();
                                String strShortFieldName = strFieldName;
                                if (fieldAttributes.getNamedItem("sname") != null) {
                                    strShortFieldName = fieldAttributes.getNamedItem("sname").getNodeValue();
                                }
                                //String strShortFieldName = fieldAttributes.getNamedItem("sname").getNodeValue();
                                String strInternalName = DBMSTypes.getField(strIDomainName + "_" + strFieldName);
                                String strShortInternalName = strShortDomainName + "_" + strShortFieldName;
                                
                                //String strFieldName = fieldAttributes.getNamedItem("iname").getNodeValue();
                                //String strInternalName = DBMSTypes.getField(strIDomainName + "_" + strFieldName);
                                String strExternalName = strEDomainName + "." + DBMSTypes.getField(fieldAttributes.getNamedItem("ename").getNodeValue());
                                String strNameForUpdate = DBMSTypes.getField(fieldAttributes.getNamedItem("ename").getNodeValue());
                                if (intDBMSType == DBMSTypes.ORACLE) {
                                    strNameForUpdate = strNameForUpdate.toUpperCase();
                                }
                                
                                String strLabelName = fieldAttributes.getNamedItem("lname").getNodeValue();
                                String strLabelInColumnName = fieldAttributes.getNamedItem("cname").getNodeValue();
                                String strDataType = fieldAttributes.getNamedItem("type").getNodeValue();
                                int intLength = Integer.parseInt(fieldAttributes.getNamedItem("length").getNodeValue());

                                boolean blUnique = Boolean.valueOf(fieldAttributes.getNamedItem("unique").getNodeValue()).booleanValue();
                                boolean blRequired = Boolean.valueOf(fieldAttributes.getNamedItem("required").getNodeValue()).booleanValue();

                                int intDataType = 0;

                                if (strDataType.equalsIgnoreCase("integer"))
                                    intDataType = DBMSTypes.INT_TYPE;
                                else if (strDataType.equalsIgnoreCase("float"))
                                    intDataType = DBMSTypes.FLOAT_TYPE;
                                else if (strDataType.equalsIgnoreCase("double"))
                                    intDataType = DBMSTypes.DOUBLE_TYPE;
                                else if (strDataType.equalsIgnoreCase("string"))
                                    intDataType = DBMSTypes.STR_TYPE;
                                else if (strDataType.equalsIgnoreCase("date"))
                                    intDataType = DBMSTypes.DATE_TYPE;
                                else if (strDataType.equalsIgnoreCase("time"))
                                    intDataType = DBMSTypes.TIME_TYPE;
                                else if (strDataType.equalsIgnoreCase("duration"))
                                    intDataType = DBMSTypes.DURATION_TYPE;
                                                   
                                DBField field = new DBField(strIDomainName, strNameForUpdate, strInternalName, strShortInternalName, strExternalName, strLabelName, strLabelInColumnName, intDataType, intLength, blUnique, blRequired);
                                if (fieldAttributes.getNamedItem("lov_type") != null)
                                    field.setLOVType(fieldAttributes.getNamedItem("lov_type").getNodeValue());
                                
                                if (fieldAttributes.getNamedItem("dropdown") != null)
                                    field.setDropdown(true);
                                
                                if (fieldAttributes.getNamedItem("val_pattern") != null)
                                    field.setValidationPattern(fieldAttributes.getNamedItem("val_pattern").getNodeValue());
                                
                                // add the field
                                hashFields.put(strIDomainName + "_" + strFieldName, field);
                            }
                        }
                    }
                }
                
                // add formfields
                NodeList formfieldNodes = document.getElementsByTagName("formfield");
                for (int index=0; index < formfieldNodes.getLength(); index++)
                {
                    Node currentFormField = formfieldNodes.item(index);
                    NamedNodeMap formfieldAttributes = currentFormField.getAttributes();
                    String formfieldName = formfieldAttributes.getNamedItem("name").getNodeValue();
                    NodeList fieldNodes = currentFormField.getChildNodes();

                    Vector vtFormFields = new Vector(10, 5);
                    for (int index1=0; index1 < fieldNodes.getLength(); index1++)
                    {
                        Node currentField = fieldNodes.item(index1);
                        if (currentField.getNodeType() == Node.ELEMENT_NODE)
                        {
                            NamedNodeMap fieldAttributes = currentField.getAttributes();
                            vtFormFields.add(fieldAttributes.getNamedItem("name").getNodeValue());
                        }
                    }
                    hashFormFields.put(formfieldName, vtFormFields);
                }
                
                // add join conditions
                NodeList joinNodes = document.getElementsByTagName("join");
                for (int index=0; index < joinNodes.getLength(); index++)
                {
                    Node currentJoin = joinNodes.item(index);
                    NamedNodeMap joinAttributes = currentJoin.getAttributes();
                    String strFirstDomain = joinAttributes.getNamedItem("first_domain").getNodeValue();
                    String strSecondDomain = joinAttributes.getNamedItem("second_domain").getNodeValue();
                    String strType = joinAttributes.getNamedItem("type").getNodeValue();
                    String strFirstField = joinAttributes.getNamedItem("first_field").getNodeValue();
                    String strSecondField = joinAttributes.getNamedItem("second_field").getNodeValue();
                                        
                    Hashtable hashTemp = (Hashtable) hashSearchDomains.get(strFirstDomain);
                    DBJoin joinObject = new DBJoin(strType, strFirstField, strSecondField);
                    hashTemp.put(strSecondDomain, joinObject);
                    hashSearchDomains.put(strFirstDomain, hashTemp);
                    
                    hashTemp = (Hashtable) hashSearchDomains.get(strSecondDomain);
                    joinObject = new DBJoin(strType, strSecondField, strFirstField);
                    hashTemp.put(strFirstDomain, joinObject);
                    hashSearchDomains.put(strSecondDomain, hashTemp);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                try {
                    file.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            hashLoaded.put(strFileName, "loaded");
        }
    }
    
    /** Load the lookup objects schema
     */
    public static void loadLookupObjects(InputStream file, String strFileName)
    {
        if (!hashLoaded.containsKey(strFileName))
        {
            // create a document factory instance
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document;

            try
            {
                // parse the XML file
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(file);

                // add domains
                NodeList lookupObjNodes = document.getElementsByTagName("lookup_object");
                for (int index=0; index < lookupObjNodes.getLength(); index++)
                {
                    Node currentLookupObj = lookupObjNodes.item(index);
                    NamedNodeMap lookupObjAttributes = currentLookupObj.getAttributes();
                    String strLookupObjName = lookupObjAttributes.getNamedItem("name").getNodeValue();
                    String strPrimaryField = lookupObjAttributes.getNamedItem("primaryfield").getNodeValue();
                    
                    // new lookup object
                    if (!hashLookupObjects.containsKey(strLookupObjName))
                    {
                        Object[] lookupObject = new Object[5];
                        Vector vtLookupObjDomains = new Vector(3);
                        Vector vtJoins = new Vector(2);
                        Vector vtFields = new Vector(10, 2);
                        Vector vtDisplayFields = new Vector(2);
                        
                        NodeList domainNodes = currentLookupObj.getChildNodes();
                        
                        // add domains for this lookup object
                        for (int index1=0; index1 < domainNodes.getLength(); index1++)
                        {
                            Node currentNode = domainNodes.item(index1);
                            
                            // node is a domain
                            if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("domain"))
                            {
                                NamedNodeMap domainAttributes = currentNode.getAttributes();
                                vtLookupObjDomains.add(domainAttributes.getNamedItem("name").getNodeValue());
                            }
                            // node is a join
                            else if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("join"))
                            {
                                NamedNodeMap joinAttributes = currentNode.getAttributes();
                                String strType = joinAttributes.getNamedItem("type").getNodeValue();
                                String strFirstField = joinAttributes.getNamedItem("first_field").getNodeValue();
                                String strSecondField = joinAttributes.getNamedItem("second_field").getNodeValue();
                                
                                vtJoins.add(new DBJoin(strType, strFirstField, strSecondField));
                            }
                            // node is a field
                            else if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("fieldname"))
                            {
                                NamedNodeMap fieldAttributes = currentNode.getAttributes();
                                String strFieldName = fieldAttributes.getNamedItem("name").getNodeValue();
                                String strDisplayName = fieldAttributes.getNamedItem("dname").getNodeValue();
                                boolean blInWhere = Boolean.valueOf(fieldAttributes.getNamedItem("inwhere").getNodeValue()).booleanValue();
                                boolean blInFieldList = Boolean.valueOf(fieldAttributes.getNamedItem("infieldlist").getNodeValue()).booleanValue();
                                vtFields.add(new LookupObjectField(strFieldName, strDisplayName, blInWhere, blInFieldList));
                            }
                            // node is a display field
                            else if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("displayfield"))
                            {
                                NamedNodeMap fieldAttributes = currentNode.getAttributes();
                                vtDisplayFields.add(fieldAttributes.getNamedItem("name").getNodeValue());
                            }
                            
                        } // nested for
                        
                        lookupObject[0] = strPrimaryField;
                        lookupObject[1] = vtLookupObjDomains;
                        lookupObject[2] = vtJoins;
                        lookupObject[3] = vtFields;
                        lookupObject[4] = vtDisplayFields;
                        hashLookupObjects.put(strLookupObjName, lookupObject);
                    } // if
                }// for
                
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                try {
                    file.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            hashLoaded.put(strFileName, "loaded");
        }
    }
    
    /** Return a hashtable that contains lookup objects
     */
    public static Hashtable getLookupObjects()
    {
        return hashLookupObjects;
    }
    
    /** Return the primary field of the lookup object
     */
    public static String getLookupObjectPrimaryField(String strLookupObjName)
    {
        return (String) ((Object[]) hashLookupObjects.get(strLookupObjName))[0];
    }
    
    /** Return a list contains all the domains of the lookup object
     */
    public static Vector getLookupObjectDomains(String strLookupObjName)
    {
        return (Vector) ((Object[]) hashLookupObjects.get(strLookupObjName))[1];
    }
    
    /** Return a list contains all the joins of the lookup object
     */
    public static Vector getLookupObjectJoins(String strLookupObjName)
    {
        return (Vector) ((Object[]) hashLookupObjects.get(strLookupObjName))[2];
    }
    
    /** Return a list contains all the field of the lookup object
     */
    public static Vector getLookupObjectFields(String strLookupObjName)
    {
        return (Vector) ((Object[]) hashLookupObjects.get(strLookupObjName))[3];
    }
    
    /** Return a list contains all the displayed field of the lookup object
     */
    public static Vector getLookupObjectDisplayFields(String strLookupObjName)
    {
        return (Vector) ((Object[]) hashLookupObjects.get(strLookupObjName))[4];
    }
    
    
    /** Get Max CLOB size
     *  @return max clob size
     */
    public static int getMaxClobSize() {
        return intMaxClobSize;
    }
}
