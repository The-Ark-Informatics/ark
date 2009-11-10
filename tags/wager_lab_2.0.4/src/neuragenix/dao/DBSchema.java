/* 
 * Copyright (c) 2002 Neuragenix, Inc. All Rights Reserved.
 * DBSchema.java
 * Created on 10 October 2002, 00:00
 * @author  Hayden et al
 * 
 * Description: 
 * Class to map business domain to db fields acting as a DB layer.
 *
 *
 *
 *
 * Note : Customised for CCV and SQL Server (Dec - 2004)
 *
 *
 */
package neuragenix.dao;
import java.util.Hashtable;
import neuragenix.dao.exception.*;
 
/**
 * This class contains all the Database schema settings for the platformgenix.
 *
 * It is used to:
 * 1. Hold all the table names (as domains), links between tables, internal and database field names,
 * field types, serial keys for the tables, which fields are required fields. </br>
 * 2. Hold static variables used by the dao and channels to use the dao </br>
 * 3. Hold database connectors and operators for the dao
 * </br></br>
 * It will throw an exception if the number of field names do not match the number of
 * field types. So if you add a field name you must add it's type!
 * @author Hayden Molnar
 */
 
public class DBSchema {

    /** Holds the name of the security access for LOV's
     */    
	public static final String ACTIVITY_LOV_ACCESS = "LIST_OF_VALUES_ACCESS";

    private static boolean blLoaded = false;
    private static Hashtable hashDBFields = new Hashtable ( 20 );
    private static Hashtable hashDBRequiredFields = new Hashtable ( 20 );
    private static Hashtable hashDBFieldTypes = new Hashtable ( 20 );
    private static Hashtable hashDBDomains = new Hashtable ( 20 );
    private static Hashtable hashDBJoinTypes = new Hashtable ( 20 );
    private static Hashtable hashDBJoinFields = new Hashtable ( 20 );
    private static Hashtable hashDBOperators = new Hashtable ( 20 );
    private static Hashtable hashDBConnectors = new Hashtable ( 20 );
    private static Hashtable hashDBJoinKeys = new Hashtable (10);
    private static Hashtable hashDBSerialKeys = new Hashtable (10);
    private static Hashtable hashDBNoSerialKeys = new Hashtable (10);
    private static Hashtable hashDBNonDuplicateFields = new Hashtable(10);
    private static Hashtable hashDBGroupSecurity = new Hashtable(10);
    private static Hashtable hashLOVSelector = new Hashtable(10);
    
    private static Hashtable hashFieldLengths = new Hashtable(20);

    
    /** This is the variable used to
     * determine an integer database field
     */    
    public final static String INT_TYPE = "INT";
    /** Holds the type float name
     */    
    public final static String FLOAT_TYPE = "FLOAT";
    /** This is the variable used to
     * determine a string database field
     */    
    public final static String STR_TYPE = "STR";
    
    /** Holds the type date name
     */    
    public final static String DATE_TYPE = "DATE";
    /** Holds the type time name
     */    
    public final static String TIME_TYPE = "TIME";
    /** Holds the type file name
     */    
    public final static String FILE_TYPE = "FILE";
    /** Holds the type script name
     */    
    public final static String SCRIPT_TYPE = "SCRIPT";
 //   public final static String LOF_ETHNICITY = "ETHNICITY";
    /** This is static string used for the order by asending for the dao
     */    
    public final static String ORDERBY_ASENDING = "ASC";
     /** This is static string used for the order by asending for the dao
     */    
    public final static String ORDERBY_DESCENDING = "DESC";
    /** A like operator for building statements using the dao
     */    
    public final static String LIKE_OPERATOR = "LIKE_OPERATOR";
    /** An equals operator for building statements using the dao
     */    
    public final static String EQUALS_OPERATOR = "EQUALS_OPERATOR";
    /** An not equals operator for building statements using the dao
     */    
    public final static String NOT_EQUALS_OPERATOR = "NOT_EQUALS_OPERATOR";
    /** A less than operator for building statements using the dao
     */    
    public final static String LESSTHAN_OPERATOR = "LESSTHAN_OPERATOR";
    /** A less than or equals connector for building statements using the dao
     */    
    public final static String LESSTHAN_AND_EQUAL_OPERATOR = "LESSTHAN_AND_EQUAL_OPERATOR";
    /** An greater than operator for building statements using the dao
     */    
    public final static String GREATERTHAN_OPERATOR = "GREATERTHAN_OPERATOR";
    /** A greater than or equals connector for building statements using the dao
     */    
    public final static String GREATERTHAN_AND_EQUAL_OPERATOR = "GREATERTHAN_AND_EQUAL_OPERATOR";
    /** An "and" connector for building statements using the dao
     */    
    public final static String AND_CONNECTOR = "AND_CONNECTOR"; 
    /** An "or" connector for building statements using the dao
     */    
    public final static String OR_CONNECTOR = "OR_CONNECTOR";
    /** Holds the internal delete field name
     */    
    public final static String DELETED_FIELD = "intDeleted";
    /** Holds the database deleted field name
     */    
    public final static String DELETED_DB_FIELD_NAME = "DELETED";
    
    // enhanced DAL
    public final static String IN_OPERATOR = "IN_OPERATOR";
    public final static String NOT_IN_OPERATOR = "NOT_IN_OPERATOR";
    public final static String UNION_CONNECTOR = "UNION_CONNECTOR";
    public final static String INTERSECT_CONNECTOR = "INTERSECT_CONNECTOR";
    public final static String EXCEPT_CONNECTOR = "EXCEPT_CONNECTOR";
    
    public final static String INNER_JOIN = "INNER_JOIN";
    public final static String LEFT_JOIN = "LEFT_JOIN";
   
    /** Creates a new instance of DBSchema */

    public DBSchema() {
        
    }
    /** Returns a hashtable containing all
     * the allowable fields in the database
     *
     * @return Hashtable
     * @throws Exception If the number of fields in the schema don't make the number of field values
     */    
    public static Hashtable getDBFields() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBFields;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns a hashtable containing all
     * the field types in the database
     * @return Returns a Hashtable containing the allowable types for the fields
     * @throws Exception Throws an exception when an unknown error occurs
     */    
    public static Hashtable getDBFieldTypes() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBFieldTypes;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns a hashtable containing all
     * the allowable domains in the database
     * @throws Exception Throws an exception when an unknown error occurs
     * @return Returns a Hashtable containing the allowable domains for the fields
     */    
    public static Hashtable getDBDomains() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBDomains;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns a hashtable containing all
     * the allowable operators in the database
     * @throws Exception Throws an exception when an unknown error occurs
     * @return Returns a Hashtable containing the allowable operators for the fields
     */    
    public static Hashtable getDBOperators() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBOperators;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns a hashtable containing all
     * the allowable connectors in the database
     * @throws Exception Throws an exception when an unknown error occurs
     * @return Returns a Hashtable containing the allowable connectors for the fields
     */    
        

        
    public static Hashtable getDBConnectors() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBConnectors;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns a hashtable containing all
     * the allowable join types in the database
     * @throws Exception Throws an exception when an unknown error occurs
     * @return Returns a Hashtable containing the allowable join types for the fields
     */    
    public static Hashtable getDBJoinTypes() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBJoinTypes;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns a hashtable containing all
     * the required fields in the database
     * @throws Exception Throws an exception when an unknown error occurs
     * @return Returns a Hashtable containing the required fields for the database
     */    
    public static Hashtable getDBRequiredFields() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBRequiredFields;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns a hashtable containing all
     * the allowable join fields in the database
     * @throws Exception Throws an exception when an unknown error occurs
     * @return Returns a Hashtable containing the join fields for the database
     */    
    public static Hashtable getDBJoinFields() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBJoinFields;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns the fields that the tables join on
     * @throws Exception Thrown when an unknown error occurs
     * @return Returns a Hashtable that contains the join fields between that tables in the database
     */    
    public static Hashtable getDBJoinKeys() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBJoinKeys;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns as list of table that do not have a serial key (as the default is that the
     * table should have a serial key)
     * @throws Exception Thrown when an unknown error occurs
     * @return Returns a hashTable that contains the
     * tables that do not have a serial key
     */    
    public static Hashtable getDBNoSerialKeyDomains() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBNoSerialKeys;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** Returns the serial keys for the domains in the database
     * @throws Exception Thrown when an unknown error occurs
     * @return Returns the serial keys for the domains in the database
     */    
    public static Hashtable getDBSerialKeys() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBSerialKeys;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    /** This method returns those fields that
     * can not contain a duplicate value
     * @throws Exception Thrown when an unknown error occurs
     * @return Returns a hashTable containing the fields in the database that
     * have to be unique. That is, can not contain duplicate values
     *
     */    
    public static Hashtable getNonDuplicateFields() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBNonDuplicateFields;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    public static Hashtable getDBGroupSecurity() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashDBGroupSecurity;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    public static Hashtable getLOVSelectorFields() throws Exception
    {
        try
        {
            // If the class is not loaded, load it to add all the hashtable data.
            if(!blLoaded){LoadDBSchema();}
            return hashLOVSelector;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    
    public static Hashtable getFieldLengths() throws Exception
    {
        try
        {
            if (!blLoaded) {LoadDBSchema();}
            return hashFieldLengths;
        }
        catch(Exception e)
        {
            throw new Exception(e.toString());
        }
    }
    
    private static void LoadDBSchema() throws DAODBSchema
    {
 // ---------------------- DOMAINS THAT HAVE GROUP SECURITY ----------------------
        hashDBGroupSecurity.put("PATIENT","");
        hashDBGroupSecurity.put("PERSON_DIR","");
        hashDBGroupSecurity.put("PROJECT","");
        hashDBGroupSecurity.put("PROFILEAUTH","");
        //hashDBGroupSecurity.put("PROCESS","");
//------------------------ LOAD THE DOMAINS -----------------------------------------
	       
        hashDBDomains.put("PATIENT", "ix_patient");
        //added to intoduce flagging        
        hashDBDomains.put("FLAG","ix_flag");
        hashDBDomains.put("STUDY", "ix_study");
        hashDBDomains.put("SURVEY", "ix_survey");
//added to use smartform with survey        
        hashDBDomains.put("SMARTFORM", "ix_smartform");
        hashDBDomains.put("PATIENT_SMARTFORM", "ix_patient_smartform");
//
        hashDBDomains.put("BIOSPECIMEN", "ix_biospecimen");
        hashDBDomains.put("APPOINTMENTS", "ix_appointments");
        hashDBDomains.put("LIST_OF_VALUES", "ix_listofvalues");
        hashDBDomains.put("CONSENT_CONTACT", "ix_consent");
        hashDBDomains.put("CONSENT_STUDY", "ix_consent_study");
        hashDBDomains.put("PERSON_DIR", "up_person_dir");
        hashDBDomains.put("PERSON_USER", "up_user");
        hashDBDomains.put("USERPROFILE", "ix_user_profile");
        hashDBDomains.put("PROFILE", "ix_profile");
        hashDBDomains.put("ACTIVITY", "ix_activity");
        hashDBDomains.put("PROFILEAUTH", "ix_authorisation");
        //hashDBDomains.put("PERSON_GROUP", "ix_person_group");
        hashDBDomains.put("STUDY_SURVEY", "ix_study_survey");
        hashDBDomains.put("SURVEY_QUESTIONS", "ix_survey_questions");
        //hashDBDomains.put("SURVEY_PATIENTS", "ix_survey_patients");
        hashDBDomains.put("SURVEY_RESULTS", "ix_survey_results");
        hashDBDomains.put("SURVEY_OPTIONS", "ix_survey_question_options");
        hashDBDomains.put("ETHICS_CONSENT", "ix_ethics_consent");

        // Added for questix
        hashDBDomains.put("PERSON_GROUP_MEMBERSHIP", "up_group_membership");
        


      // Added for questix
        hashDBDomains.put("PERSON_GROUP_MEMBERSHIP", "up_group_membership");
        


        
        // --- Microarray        
        hashDBDomains.put("PROJECT", "ix_project");
        hashDBDomains.put("EXPERIMENT", "ix_experiment");
        hashDBDomains.put("EXPERIMENT_ATTACHMENT", "ix_experiment_attachment");
        hashDBDomains.put("DATASET_ATTACHMENT", "ix_dataset_attachment");
        //hashDBDomains.put("RESEARCHER", "ix_researcher");
        //hashDBDomains.put("AUTHOR", "ix_author");
        hashDBDomains.put("DATASET_ATTACHMENT", "ix_dataset_attachment");
        
        // --- Integration
        hashDBDomains.put("TEMPLATE", "ix_template");
        
        // --- Audit logger
        hashDBDomains.put("AUDIT_LOG", "ix_log");
        
        hashDBDomains.put("PROCESS", "ix_process");
        hashDBDomains.put("CONFIGURATION", "ix_configuration");
        

        // Questix
        hashDBDomains.put("QUESTIX_PARTICIPANTS", "ix_questix_participants");   
        hashDBDomains.put("QUESTIX_PARTICIPANTS_RATERS", "ix_questix_participant_to_raters");
        hashDBDomains.put("QUESTIX_EMAIL", "ix_questix_email");
     

        // --- Case and workflow
        hashDBDomains.put("CASE",  "ix_case");
        hashDBDomains.put("TASK",  "ix_task");
        hashDBDomains.put("CASE_TYPE",  "ix_case_type");
        hashDBDomains.put("WORKFLOW",  "ix_workflow");
        hashDBDomains.put("CASE_LOSS",  "ix_case_loss");
        hashDBDomains.put("CASE_DIARY",  "ix_case_diary");
        hashDBDomains.put("CASE_APPOINTMENT",  "ix_case_appointment");
        hashDBDomains.put("CASE_PARTY",  "ix_case_party");
        hashDBDomains.put("CASE_ACCOUNT",  "ix_case_account");
        hashDBDomains.put("CASE_TRANSACTION",  "ix_case_transaction");
        hashDBDomains.put("CASE_LOSS_ALLOC",  "ix_case_loss_allocation");

        hashDBDomains.put("SMARTFORM_PARTICIPANT",  "ix_smartform_participants");
        hashDBDomains.put("SMARTFORM_RESULTS",  "ix_smartform_results");


        // --- Casegenix -- Tables used for importing data
        hashDBDomains.put("CASE",  "ix_core_case");
        hashDBDomains.put("CASE_RELATED_PARTIES",  "ix_related_parties");
        hashDBDomains.put("RELATED_CASE",  "ix_related_case");
        hashDBDomains.put("SMARTFORM_PARTICIPANT",  "ix_smartform_participants");
        hashDBDomains.put("SMARTFORM_RESULTS",  "ix_smartform_results");
        hashDBDomains.put("DATA_ELEMENTS",  "ix_dataelements");
        
        
        // --- Cell inventory

        hashDBDomains.put("INV_SITE", "ix_inv_site");

        hashDBDomains.put("INV_TANK", "ix_inv_tank");

        hashDBDomains.put("INV_BOX", "ix_inv_box");

        hashDBDomains.put("INV_TRAY", "ix_inv_tray");

        hashDBDomains.put("INV_CELL", "ix_inv_cell");

        // Questix
        hashDBDomains.put("QUESTIX_PARTICIPANTS", "ix_questix_participants");   
        hashDBDomains.put("QUESTIX_PARTICIPANTS_RATERS", "ix_questix_participant_to_raters");
        hashDBDomains.put("QUESTIX_EMAIL", "ix_questix_email");


        hashDBDomains.put("BIO_SURVEY_RESULTS", "ix_bio_survey_results");
// ----------------------- LOAD THE DOMAIN JOIN TYPES -------------------------------------
        
    
        hashDBJoinTypes.put("PATIENT-APPOINTMENTS", "LEFT JOIN");
        hashDBJoinTypes.put("CONSENT_CONTACT-CONSENT_STUDY", "LEFT JOIN");
        hashDBJoinTypes.put("CONSENT_STUDY-CONSENT_CONTACT", "LEFT JOIN");
        hashDBJoinTypes.put("PATIENT-CONSENT_CONTACT", "INNER JOIN");
        hashDBJoinTypes.put("CONSENT_CONTACT-PATIENT", "INNER JOIN");
        hashDBJoinTypes.put("SURVEY-APPOINTMENTS", "INNER JOIN");
        hashDBJoinTypes.put("APPOINTMENTS-SURVEY", "INNER JOIN");
        hashDBJoinTypes.put("STUDY_SURVEY-SMARTFORM", "LEFT JOIN");        
        hashDBJoinTypes.put("STUDY_SURVEY-SMARTFORM_PARTICIPANT", "LEFT JOIN");        
        hashDBJoinTypes.put("STUDY-CONSENT_STUDY", "INNER JOIN");
        hashDBJoinTypes.put("CONSENT_STUDY-STUDY", "INNER JOIN");
        hashDBJoinTypes.put("STUDY_SURVEY-SURVEY", "LEFT JOIN");
        hashDBJoinTypes.put("SURVEY-STUDY_SURVEY", "LEFT JOIN");
        hashDBJoinTypes.put("STUDY-STUDY_SURVEY", "INNER JOIN");
        hashDBJoinTypes.put("SURVEY-SURVEY_PATIENTS", "INNER JOIN");
        hashDBJoinTypes.put("SURVEY_PATIENTS-SURVEY", "INNER JOIN");
        hashDBJoinTypes.put("STUDY-SURVEY_PATIENTS", "INNER JOIN");
        hashDBJoinTypes.put("SURVEY_PATIENTS-STUDY", "INNER JOIN");
        hashDBJoinTypes.put("SURVEY_PATIENTS-PATIENT", "INNER JOIN");
        hashDBJoinTypes.put("PATIENT-SURVEY_PATIENTS", "INNER JOIN");
        hashDBJoinTypes.put("CONSENT_CONTACT-BIOSPECIMEN", "LEFT JOIN");
        hashDBJoinTypes.put("SURVEY_RESULTS-SURVEY_QUESTIONS", "INNER JOIN");
        hashDBJoinTypes.put("SURVEY_QUESTIONS-SURVEY_RESULTS", "INNER JOIN");
        hashDBJoinTypes.put("SURVEY_QUESTIONS-BIO_SURVEY_RESULTS", "INNER JOIN");
        hashDBJoinTypes.put("ETHICS_CONSENT-STUDY", "LEFT JOIN");
        hashDBJoinTypes.put("STUDY-ETHICS_CONSENT", "LEFT JOIN");
        hashDBJoinTypes.put("PROFILEAUTH-ACTIVITY", "INNER JOIN");
        hashDBJoinTypes.put("USERPROFILE-PROFILE", "INNER JOIN");
        
        // advanced search
        hashDBJoinTypes.put("PATIENT-SURVEY_RESULTS", "INNER JOIN");
        
           // The link below is a work around for a bug
        hashDBJoinTypes.put("STUDY-PATIENT", "INNER JOIN");
        hashDBJoinTypes.put("PATIENT-BIOSPECIMEN", "LEFT JOIN");
        // --- Microarray 
        hashDBJoinTypes.put("PROJECT-EXPERIMENT", "LEFT JOIN");        

        hashDBJoinTypes.put("EXPERIMENT-EXPERIMENT_ATTACHMENT", "LEFT JOIN");  
        
        // Questix
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS-QUESTIX_PARTICIPANTS_RATERS", "INNER JOIN");
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS_RATERS-QUESTIX_PARTICIPANTS", "INNER JOIN");
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS-SURVEY", "INNER JOIN");
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS_RATERS-SURVEY", "INNER JOIN");
        
        hashDBJoinTypes.put("EXPERIMENT-EXPERIMENT_ATTACHMENT", "LEFT JOIN");           
        
        // case
        hashDBJoinTypes.put("CASE-SURVEY", "INNER JOIN");   
        hashDBJoinTypes.put("PERSON_DIR-TASK", "LEFT JOIN");  
        
        // Questix
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS-QUESTIX_PARTICIPANTS_RATERS", "INNER JOIN");
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS_RATERS-QUESTIX_PARTICIPANTS", "INNER JOIN");
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS-SURVEY", "INNER JOIN");
        hashDBJoinTypes.put("QUESTIX_PARTICIPANTS_RATERS-SURVEY", "INNER JOIN");

        
        // join between STUDY and BIOSPECIMEN
        hashDBJoinTypes.put("BIOSPECIMEN-STUDY", "INNER JOIN");
        
// ------------------------- LOAD THE JOIN ON FIELDS --------------------------------
        
        hashDBJoinFields.put("PATIENT-CONSENT_CONTACT", "ON ix_patient.\"PATIENTKEY\" = ix_consent.\"PATIENTKEY\"");
        hashDBJoinFields.put("CONSENT_CONTACT-PATIENT", "ON ix_patient.\"PATIENTKEY\" = ix_consent.\"PATIENTKEY\"");
        hashDBJoinFields.put("CONSENT_CONTACT-CONSENT_STUDY", "ON ix_consent.\"CONSENTKEY\" = ix_consent_study.\"CONSENTKEY\"");
        hashDBJoinFields.put("CONSENT_STUDY-CONSENT_CONTACT", "ON ix_consent.\"CONSENTKEY\" = ix_consent_study.\"CONSENTKEY\"");
        hashDBJoinFields.put("PATIENT-APPOINTMENTS", "ON ix_patient.\"PATIENTKEY\" = ix_appointments.\"PATIENTKEY\"");
        hashDBJoinFields.put("SURVEY-APPOINTMENTS", "ON ix_survey.\"SURVEYKEY\" = ix_appointments.\"SURVEYKEY\"");
        hashDBJoinFields.put("APPOINTMENTS-SURVEY", "ON ix_survey.\"SURVEYKEY\" = ix_appointments.\"SURVEYKEY\"");
        hashDBJoinFields.put("STUDY_SURVEY-SMARTFORM", "ON ix_smartform.\"SMARTFORMKEY\" = ix_study_survey.\"SURVEYKEY\"");
        hashDBJoinFields.put("STUDY_SURVEY-SMARTFORM_PARTICIPANT", "ON ix_smartform_participants.\"SMARTFORMKEY\" = ix_study_survey.\"SURVEYKEY\"");
        hashDBJoinFields.put("CONSENT_STUDY-STUDY", "ON ix_study.\"STUDYKEY\" = ix_consent_study.\"STUDYKEY\"");
        hashDBJoinFields.put("STUDY-CONSENT_STUDY", "ON ix_study.\"STUDYKEY\" = ix_consent_study.\"STUDYKEY\"");
        hashDBJoinFields.put("SURVEY-STUDY_SURVEY", "ON ix_survey.\"SURVEYKEY\" = ix_study_survey.\"SURVEYKEY\"");
        hashDBJoinFields.put("STUDY_SURVEY-SURVEY", "ON ix_survey.\"SURVEYKEY\" = ix_study_survey.\"SURVEYKEY\"");
        hashDBJoinFields.put("STUDY_SURVEY-STUDY", "ON ix_study.\"STUDYKEY\" = ix_study_survey.\"STUDYKEY\"");
        hashDBJoinFields.put("STUDY-STUDY_SURVEY", "ON ix_study.\"STUDYKEY\" = ix_study_survey.\"STUDYKEY\"");
        //hashDBJoinFields.put("SURVEY-SURVEY_PATIENTS","ON ix_survey.\"SURVEYKEY\" = ix_survey_patients.\"SURVEYKEY\"");
        //hashDBJoinFields.put("SURVEY_PATIENTS-SURVEY","ON ix_survey.\"SURVEYKEY\" = ix_survey_patients.\"SURVEYKEY\"");
        //hashDBJoinFields.put("SURVEY_PATIENTS-STUDY","ON ix_study.\"STUDYKEY\" = ix_survey_patients.\"STUDYKEY\"");
        //hashDBJoinFields.put("STUDY-SURVEY_PATIENTS","ON ix_study.\"STUDYKEY\" = ix_survey_patients.\"STUDYKEY\"");
        hashDBJoinFields.put("SURVEY_QUESTIONS-SURVEY_RESULTS","ON ix_survey_results.\"QUESTIONORDER\" = ix_survey_questions.\"QUESTIONORDER\"");
        hashDBJoinFields.put("SURVEY_QUESTIONS-BIO_SURVEY_RESULTS","ON ix_bio_survey_results.\"QUESTIONORDER\" = ix_survey_questions.\"QUESTIONORDER\"");
        hashDBJoinFields.put("SURVEY_RESULTS-SURVEY_QUESTIONS","ON ix_survey_results.\"QUESTIONORDER\" = ix_survey_questions.\"QUESTIONORDER\"");
        //hashDBJoinFields.put("PATIENT-SURVEY_PATIENTS","ON ix_patient.\"PATIENTKEY\" = ix_survey_patients.\"PATIENTKEY\"");
        //hashDBJoinFields.put("SURVEY_PATIENTS-PATIENT","ON ix_patient.\"PATIENTKEY\" = ix_survey_patients.\"PATIENTKEY\"");
        hashDBJoinFields.put("STUDY-ETHICS_CONSENT", "ON ix_study.\"STUDYKEY\" = ix_ethics_consent.\"STUDYKEY\"");
        hashDBJoinFields.put("ETHICS_CONSENT-STUDY", "ON ix_study.\"STUDYKEY\" = ix_ethics_consent.\"STUDYKEY\"");
        hashDBJoinFields.put("PATIENT-BIOSPECIMEN", "ON ix_patient.\"PATIENTKEY\" = ix_biospecimen.\"PATIENTKEY\"");
        hashDBJoinFields.put("PROFILEAUTH-ACTIVITY", "ON ix_authorisation.\"ACTIVITYKEY\" = ix_activity.\"ACTIVITYKEY\"");
        hashDBJoinFields.put("USERPROFILE-PROFILE", "ON ix_user_profile.\"PROFILEKEY\" = ix_profile.\"PROFILEKEY\"");
        
        
	
          // The link below is a work around for a bug
        //hashDBJoinFields.put("STUDY-PATIENT", "ON ix_patient.\"PATIENTKEY\" = ix_survey_patients.\"PATIENTKEY\"");
        
        // --- Microarray 
        hashDBJoinFields.put("PROJECT-EXPERIMENT", "ON ix_project.\"PROJECTKEY\" = ix_experiment.\"PROJECTKEY\"");
        hashDBJoinFields.put("EXPERIMENT-EXPERIMENT_ATTACHMENT", "ON ix_experiment.\"EXPIERMENTKEY\" = ix_experiment_attachment.\"EXPERIMENTKEY\"");
                
        // Questiz
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS_RATERS-QUESTIX_PARTICIPANTS", "ON ix_questix_participant_to_raters.\"PART_LOGIN_NAME\" = ix_questix_participants.\"PART_LOGIN_NAME\"");
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS-QUESTIX_PARTICIPANTS_RATERS", "ON ix_questix_participant_to_raters.\"PART_LOGIN_NAME\" = ix_questix_participants.\"PART_LOGIN_NAME\"");
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS-SURVEY", "ON ix_survey.\"SURVEYKEY\" = ix_questix_participants.\"QUESTIX_SURVEY_ID\"");
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS_RATERS-SURVEY", "ON ix_questix_participant_to_raters.\"QUESTIX_SURVEY_ID\" = ix_survey.\"SURVEYKEY\"");
        
        hashDBJoinKeys.put("intProcessKey", "ix_process.\"PROCESSKEY\"");   



        // Questiz
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS_RATERS-QUESTIX_PARTICIPANTS", "ON ix_questix_participant_to_raters.\"PART_LOGIN_NAME\" = ix_questix_participants.\"PART_LOGIN_NAME\"");
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS-QUESTIX_PARTICIPANTS_RATERS", "ON ix_questix_participant_to_raters.\"PART_LOGIN_NAME\" = ix_questix_participants.\"PART_LOGIN_NAME\"");
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS-SURVEY", "ON ix_survey.\"SURVEYKEY\" = ix_questix_participants.\"QUESTIX_SURVEY_ID\"");
        hashDBJoinFields.put("QUESTIX_PARTICIPANTS_RATERS-SURVEY", "ON ix_questix_participant_to_raters.\"QUESTIX_SURVEY_ID\" = ix_survey.\"SURVEYKEY\"");

        
        // advanced search
        hashDBJoinFields.put("PATIENT-SURVEY_RESULTS", "ON ix_patient.\"PATIENTKEY\" = ix_survey_results.\"PARTICIPANTID\"");
        
        // case
        hashDBJoinFields.put("CASE-SURVEY", "ON ix_case.\"SURVEYKEY\" = ix_survey.\"SURVEYKEY\"");
        hashDBJoinFields.put("PERSON_DIR-TASK", "ON up_person_dir.\"user_name\" = ix_task.\"user_name\"");
        
        // join between STUDY and BIOSPECIMEN
        hashDBJoinFields.put("BIOSPECIMEN-STUDY", "ON ix_biospecimen.\"STUDYKEY\" = ix_study.\"STUDYKEY\"");
        
// ------------------------ LOAD THE TABLE INNER KEYS --------------------------------------
        
        hashDBJoinKeys.put("intInternalPatientID", "ix_patient.\"PATIENTKEY\"");
        hashDBJoinKeys.put("intConsentID", "ix_consent.\"CONSENTKEY\"");
        hashDBJoinKeys.put("intSurveyID", "ix_survey.\"SURVEYKEY\"");
        hashDBJoinKeys.put("intStudyID", "ix_study.\"STUDYKEY\"");
        hashDBJoinKeys.put("intBiospecimenID", "ix_biospecimen.\"BIOSPECIMENKEY\""); //AF: do we need this? 
        hashDBJoinKeys.put("intQuestionID", "ix_survey_questions.\"QUESTIONKEY\"");
        hashDBJoinKeys.put("intQuestionOrder", "ix_survey_questions.\"QUESTIONORDER\"");
        hashDBJoinKeys.put("intQuestionType", "ix_survey_questions.\"QUESTIONTYPE\"");
        hashDBJoinKeys.put("intActivityID", "ix_activity.\"ACTIVITYKEY\"");
        hashDBJoinKeys.put("intProfileID", "ix_profile.\"PROFILEKEY\"");   
      // --- Microarray               
        hashDBJoinKeys.put("intMaProjectKey", "ix_project.\"PROJECTKEY\"");
        hashDBJoinKeys.put("intMaExperimentProjectKey", "ix_experiment.\"PROJECTKEY\"");
        hashDBJoinKeys.put("intMaExpAttachmentKey", "ix_experiment_attachment.\"EXPERIMENT_ATTACHMENTKEY\"");
        

        // Questix 
        hashDBJoinKeys.put("strPartLoginName", "ix_questix_participants.\"PART_LOGIN_NAME\"");
        hashDBJoinKeys.put("intQuestixSurveyID", "ix_questix_participants.\"QUESTIX_SURVEY_ID\"");
        hashDBJoinKeys.put("intEmailSent", "ix_questix_participants.\"EMAIL_SENT\"");
        


        // --- Inventory

        hashDBJoinKeys.put("intInvSiteID", "ix_inv_site.\"SITEKEY\"");

        hashDBJoinKeys.put("intInvTankID", "ix_inv_tank.\"TANKKEY\"");

        hashDBJoinKeys.put("intInvBoxID", "ix_inv_box.\"BOXKEY\"");

        hashDBJoinKeys.put("intInvTrayID", "ix_inv_tray.\"TRAYKEY\"");

        hashDBJoinKeys.put("intInvCellID", "ix_inv_cell.\"CELLKEY\"");

        // Questix 
        hashDBJoinKeys.put("strPartLoginName", "ix_questix_participants.\"PART_LOGIN_NAME\"");
        hashDBJoinKeys.put("intQuestixSurveyID", "ix_questix_participants.\"QUESTIX_SURVEY_ID\"");
        hashDBJoinKeys.put("intEmailSent", "ix_questix_participants.\"EMAIL_SENT\"");

        
// ----------------------- LOAD THE SERIAL KEYS FOR THE DOMAINS ---------------------------------------
        
        hashDBSerialKeys.put("PATIENT", "ix_patient_patientkey_seq");
        hashDBSerialKeys.put("FLAG", "ix_flag_flagkey_seq");
        hashDBSerialKeys.put("APPOINTMENTS", "ix_appointments_appkey_seq");
        hashDBSerialKeys.put("CONSENT_CONTACT", "ix_consent_consentkey_seq");
        hashDBSerialKeys.put("CONSENT_STUDY", "ix_consent_study_cstudykey_seq");
        hashDBSerialKeys.put("STUDY", "ix_study_studykey_seq");
        hashDBSerialKeys.put("SURVEY", "ix_survey_surveykey_seq");
        hashDBSerialKeys.put("SMARTFORM", "ix_smartform_smartformkey_seq");        
        hashDBSerialKeys.put("PATIENT_SMARTFORM", "ix_patient_smartformkey_seq");        
        hashDBSerialKeys.put("STUDY_SURVEY", "ix_studysurvey_ss_seq");
        hashDBSerialKeys.put("BIOSPECIMEN", "ix_biospecimen_biospeckey_seq");
        hashDBSerialKeys.put("LIST_OF_VALUES", "ix_listofvalues_lovkey_seq");
        hashDBSerialKeys.put("SURVEY_QUESTIONS", "ix_surveyqs_surveyqs_seq");
        hashDBSerialKeys.put("SURVEY_PATIENTS", "ix_surveyp_surveyp_seq");
        hashDBSerialKeys.put("SURVEY_RESULTS", "ix_survey_results_key_seq");
        hashDBSerialKeys.put("BIO_SURVEY_RESULTS", "ix_bio_survey_results_key_seq");
        hashDBSerialKeys.put("SURVEY_OPTIONS", "ix_optionqs_optionqs_seq");
        hashDBSerialKeys.put("ETHICS_CONSENT", "ix_ethics_study_key_seq");
        hashDBSerialKeys.put("USERPROFILE", "ix_userprofilekey_seq");
        hashDBSerialKeys.put("PROFILE", "ix_profile_profilekey_seq");
        hashDBSerialKeys.put("PROFILEAUTH", "ix_authorisation_seq");
        
        // --- Microarray               
        hashDBSerialKeys.put("PROJECT", "ix_project_key_seq");
        hashDBSerialKeys.put("EXPERIMENT", "ix_experiment_key_seq");
        hashDBSerialKeys.put("EXPERIMENT_ATTACHMENT", "ix_experiment_attach_key_seq");


        // Questix
        hashDBSerialKeys.put("QUESTIX_PARTICIPANTS", "ix_participants_seq");   
        hashDBSerialKeys.put("QUESTIX_PARTICIPANTS_RATERS", "ix_part_raters_seq");

        hashDBSerialKeys.put("QUESTIX_EMAIL", "ix_questix_email_seq");

        // --- Integration
        hashDBSerialKeys.put("TEMPLATE", "ix_template_seq");

        hashDBSerialKeys.put("PROCESS", "ix_process_seq");
        hashDBSerialKeys.put("CONFIGURATION", "ix_configuration_seq");

        
        // Questix
        hashDBSerialKeys.put("QUESTIX_PARTICIPANTS", "ix_participants_seq");   
        hashDBSerialKeys.put("QUESTIX_PARTICIPANTS_RATERS", "ix_part_raters_seq");
        

        
        // --- Case
        hashDBSerialKeys.put("CASE", "ix_core_case_seq");
        hashDBSerialKeys.put("CASE_RELATED_PARTIES", "ix_related_parties_seq");
        hashDBSerialKeys.put("RELATED_CASE",  "ix_related_case_seq");
        hashDBSerialKeys.put("SMARTFORM_PARTICIPANT", "ix_smartform_participantskey_seq");
        hashDBSerialKeys.put("SMARTFORM_RESULTS",  "ix_smartform_resultskey_seq");
        hashDBSerialKeys.put("DATA_ELEMENTS",  "ix_smartform_dataelemts_seq");
        
        hashDBSerialKeys.put("SMARTFORM_PARTICIPANT", "ix_smartform_participantskey_seq");
        hashDBSerialKeys.put("SMARTFORM_RESULTS",  "ix_smartform_resultskey_seq");
        
        
// ---------------- LOAD THE TABLES WITHOUT SERIAL KEYS ----------------------------
        
        hashDBNoSerialKeys.put("PERSON_DIR", "");

        hashDBNoSerialKeys.put("PERSON_USER", "");
        hashDBNoSerialKeys.put("PERSON_GROUP_MEMBERSHIP", "");
	

	// Add for questix
        hashDBNoSerialKeys.put("PERSON_USER", "");
        hashDBNoSerialKeys.put("PERSON_GROUP_MEMBERSHIP", "");


// ---------------- LOAD THE NON DUPLICATE FIELDS (Fields who values must be unqiue  ----------------
        hashDBNonDuplicateFields.put("strPatientID", "PATIENT");
        hashDBNonDuplicateFields.put("intInternalPatientID", "PATIENT");
        hashDBNonDuplicateFields.put("intSurveyID", "SURVEY");
        hashDBNonDuplicateFields.put("strBiospecimenID", "BIOSPECIMEN");
   
        
// ------------------------ LOAD THE FIELD NAMES --------------------------------------
        // Patient Table
        hashDBFields.put("intInternalPatientID", "PATIENTKEY");
        hashDBFields.put("strPatientID", "PATIENTID");
        hashDBFields.put("strMedicareNo", "MEDICARENO");
        hashDBFields.put("strOtherID", "OTHERID");
        hashDBFields.put("strHospitalUR", "HOSPITALUR");
        hashDBFields.put("strTitle", "TITLE");
        hashDBFields.put("strSurname", "SURNAME");
        hashDBFields.put("strFirstName", "FIRSTNAME");
        hashDBFields.put("dtDob", "DOB");
        hashDBFields.put("strAddressNumber", "ADDR_NUMBER");
        hashDBFields.put("strAddressLine1", "ADDR_LINE1");
        hashDBFields.put("strAddressSuburb", "ADDR_SUBURB");
        hashDBFields.put("strAddressState", "ADDR_STATE");
        hashDBFields.put("strAddressOtherState", "ADDR_OTHER_STATE");
        hashDBFields.put("strAddressCountry", "ADDR_COUNTRY");
        hashDBFields.put("strAddressOtherCountry", "ADDR_OTHER_COUNTRY");
        hashDBFields.put("intAddressPostCode", "ADDR_POSTCODE");
        hashDBFields.put("intPhoneHome", "TELEPHONE_HOME");
        hashDBFields.put("intPhoneWork", "TELEPHONE_WORK");
        hashDBFields.put("intPhoneMobile", "TELEPHONE_MOBILE");
        hashDBFields.put("strEmail", "EMAIL");
        hashDBFields.put("strStatus", "STATUS");
        hashDBFields.put("strHospital", "HOSPITALNAME");
        hashDBFields.put("intDeleted", "DELETED");
        hashDBFields.put("strSex", "SEX");
        
        //attachments
        
        hashDBFields.put("strAttachmentdomain", "DOMAIN");
        hashDBFields.put("strAttachmentID", "ID");
        hashDBFields.put("strAttachmentBy", "ATTACHMEDBY");
        hashDBFields.put("strAttachmentFilename", "FILE_NAME");
        hashDBFields.put("strAttachmentComments", "COMMENTS");
        hashDBFields.put("ATTACHMENTS_strAttachmentsFileName", "NA");
        
                //Flag table
        hashDBFields.put("strFlagDomain", "DOMAIN");
        hashDBFields.put("intID", "ID");
        hashDBFields.put("struser", "USER");
        
        
        // Appointment Table
        hashDBFields.put("intAppointmentID", "APPKEY");
        hashDBFields.put("intAppPatientID", "PATIENTKEY");
        hashDBFields.put("dtAppDate", "DATE");
        hashDBFields.put("tmAppTime", "TIME");
        hashDBFields.put("strAppNotify", "NOTIFY");
        hashDBFields.put("strSentStamp", "SENT_TIMESTAMP");
        hashDBFields.put("dtAppAlertDate", "ALERT_DATE");
        hashDBFields.put("strAppPurpose", "PURPOSE");
        
        // List of values Table
        hashDBFields.put("intLovKey", "LOVKEY");
        hashDBFields.put("strType", "TYPE");
        hashDBFields.put("strValue", "VALUE");
        hashDBFields.put("strDescription", "DESCRIPTION");
        hashDBFields.put("intSortOrder", "SORTORDER");
        
        // Consent contact
        hashDBFields.put("intConsentID", "CONSENTKEY");
        hashDBFields.put("intFutureStudy", "FUTURE_STUDY");
        hashDBFields.put("intContactOK", "CONTACT_OK");
        
         // Consent study table
        hashDBFields.put("intConsentStudyID", "CSTUDYKEY");
        hashDBFields.put("intConsentApproved", "APPROVED");
        hashDBFields.put("dtConsentDateApproved", "DATE_APPROVED");
        hashDBFields.put("strResearcher", "RESEARCHER");
        hashDBFields.put("strRefDoctor", "REF_DOCTOR");
        hashDBFields.put("strConsentComments", "COMMENTS");
        hashDBFields.put("flConsentFile", "FILE"); // Not add to db dummy!
        hashDBFields.put("strConsentFileName", "FILE_NAME"); 
        hashDBFields.put("intStudyID", "STUDYKEY");
        hashDBFields.put("intStudyApproved", "APPROVED");
        
        // Study biospecimen table
        hashDBFields.put("intStudyBiospecimenID", "STUDYBIOSPECIMENKEY");
        //hashDBFields.put("dtConsentDateApproved", "DATE_APPROVED");
        //hashDBFields.put("strResearcher", "RESEARCHER");
        //hashDBFields.put("strRefDoctor", "REF_DOCTOR");
        //hashDBFields.put("strConsentComments", "COMMENTS");
        
        
        //Survey table
        hashDBFields.put("intSurveyID", "SURVEYKEY");
        hashDBFields.put("strSurveyName" , "SURVEY_NAME");
        hashDBFields.put("strSurveyDesc" , "SURVEY_DESCRIPTION");

        hashDBFields.put("intSmartformKey", "SMARTFORMKEY");
        hashDBFields.put("strSmartformName" , "SMARTFORM_NAME");
        hashDBFields.put("strSmartformDesc" , "SMARTFORM_DESCRIPTION");
        hashDBFields.put("strDomain" , "DOMAIN");
    
        //rennypv------------ix_patient_smartform
/*        hashDBFields.put("intPatientSmartformKey", "PATIENTSMARTFORMKEY");
        hashDBFields.put("intInternalPatientID", "PATIENTKEY");
        hashDBFields.put("intSmartformKey", "SMARTFORMKEY");
        hashDBFields.put("intStudyID", "STUDYKEY");
//        hashDBFields.put("intSmartformKey", "SMARTFORMKEY");*/
        
        hashDBFields.put("strQuestixSurveyStatus" , "STATUS");
        


        hashDBFields.put("strQuestixSurveyStatus" , "STATUS");
        
        // Survey question options
        hashDBFields.put("intOptionID", "OPTIONKEY");
        hashDBFields.put("strOptionLabel", "OPTIONLABEL");
        hashDBFields.put("strOptionValue", "OPTIONVALUE");
        hashDBFields.put("intOptionOrder", "OPTIONORDER");
 
        
        // Study table
        hashDBFields.put("intStudyID", "STUDYKEY");
        hashDBFields.put("strStudyName", "STUDY_NAME");
        hashDBFields.put("strStudyOwner", "STUDY_OWNER");
        hashDBFields.put("strStudyDesc" , "STUDY_DESCRIPTION");
        hashDBFields.put("dtStudyStart", "STUDY_START");
        hashDBFields.put("dtStudyEnd", "STUDY_END");
        hashDBFields.put("intTargetPatientNo", "TARGET_PATIENT_NO");
        hashDBFields.put("intActualPatientNo", "ACTUAL_PATIENT_NO");
        hashDBFields.put("strStudyCode", "STUDYCODE");

        
        // Survey patient table
        hashDBFields.put("intSurveyPatientsID", "SURVEYPATIENTSKEY");
        hashDBFields.put("intSurveyStatus", "SURVEYSTATUS");
        
        //Survey results table
        hashDBFields.put("intSurveyResultsID", "SURVEYRESULTSKEY");
        hashDBFields.put("intParticipantID", "PARTICIPANTID");
        hashDBFields.put("strDataText", "DATATEXT");
        hashDBFields.put("strDataChoices", "DATACHOICES");
        hashDBFields.put("intDataNumeric", "DATANUMERIC");
        hashDBFields.put("dtDataDate", "DATADATE");
        
        // Study Ethics table
        hashDBFields.put("intEthicsStudyID", "ETHICSKEY");
        hashDBFields.put("strEthicsApprovedBy", "ETHICS_APPROVED_BY");
        hashDBFields.put("intEthicsApproved", "ETHICS_APPROVED");
        hashDBFields.put("dtEthicsAppDate", "ETHICS_DATE_APPROVED");
        hashDBFields.put("strEthicsFileName", "ETHICS_FILE_NAME");
        hashDBFields.put("strEthicsComments", "ETHICS_COMMENTS");
        hashDBFields.put("flEthicsFile", "NA"); // Not used in database

        // Person Directoy Table 
        hashDBFields.put("strUsername", "user_name");
        hashDBFields.put("strPassword", "encrptd_pswd");
        hashDBFields.put("strPersonLastName", "last_name");
        hashDBFields.put("strPersonFirstName", "first_name");
        hashDBFields.put("strPersonEmail", "email");
        hashDBFields.put("strPersonProfessionalLevel", "LEVEL");
	
        // Group Table 
        hashDBFields.put("intActivityID", "ACTIVITYKEY");
        hashDBFields.put("strActivityName", "NAME");
        hashDBFields.put("strActivityDesc", "DESCRIPTION");

        // Group Table 
        hashDBFields.put("intProfileID", "PROFILEKEY");
        hashDBFields.put("strProfileName", "NAME");
        hashDBFields.put("strProfileDesc", "DESCRIPTION");

        // Group Table 
        hashDBFields.put("intUserProfileID", "USERPROFILEKEY");
        hashDBFields.put("intForeignProfileID", "PROFILEKEY");

        // Group Table 
        hashDBFields.put("intGenixGroupID", "GROUPKEY");

        // User Table 
        hashDBFields.put("intUserID", "user_id");
        hashDBFields.put("intDefaultLayoutID", "user_dflt_lay_id");
        hashDBFields.put("intDefaultUserID", "user_dflt_usr_id");
        hashDBFields.put("intNextStructID", "next_struct_id");
       
        // Biospecimen 
        hashDBFields.put("intBiospecimenID", "BIOSPECIMENKEY");
        hashDBFields.put("strBiospecimenID", "BIOSPECIMENID");
        hashDBFields.put("intBiospecParentID", "PARENTKEY");
        hashDBFields.put("strBiospecParentID", "PARENTID");
        hashDBFields.put("strBiospecSampleType", "SAMPLETYPE");
        hashDBFields.put("strBiospecSampleSubType", "SAMPLESUBTYPE");
        hashDBFields.put("strBiospecLocation", "LOCATION");
        hashDBFields.put("dtBiospecSampleDate", "SAMPLEDATE");
        hashDBFields.put("strBiospecOtherID", "OTHERID");
        hashDBFields.put("strBiospecGestAt", "GESTAT");
        hashDBFields.put("intBiospecStudyID", "STUDYKEY");
        hashDBFields.put("strBiospecGrade", "GRADE");
        hashDBFields.put("strComments", "COMMENTS"); 
        hashDBFields.put("strEncounter", "ENCOUNTER");
               //rennypv added to include teh inventory details
        hashDBFields.put("intCellID", "CELLKEY");
        
        hashDBFields.put("dtBiospecDateExtracted", "DATEEXTRACTED");
        hashDBFields.put("dtBiospecDateDistributed", "DATEDISTRIBUTED");
        hashDBFields.put("strBiospecSpecies", "SPECIES");
        hashDBFields.put("strCollaborator", "COLLABORATOR");
		
        //Anita Start
        hashDBFields.put("strStoredIn", "STORED_IN");
        hashDBFields.put("tmBiospecSampleTime", "SAMPLE_TIME");
        hashDBFields.put("tmBiospecExtractedTime", "EXTRACTED_TIME");        
        //Anita End
        
        //Biotissue
        hashDBFields.put("strBiospecDescription", "SUBTYPEDESC");
        hashDBFields.put("strBiospecCode", "EXTRATXT2");
        hashDBFields.put("strBiospecTreatment", "EXTRATXT3");
        hashDBFields.put("intBiospecNumCollected", "QTY_COLLECTED");
        hashDBFields.put("intBiospecNumRemoved", "QTY_REMOVED");

        //Biofluid
        hashDBFields.put("intBiospecVolume", "QTY_COLLECTED");
        hashDBFields.put("intBiospecVolRemoved", "QTY_REMOVED");

        //Biofluid - Amniocentesis
        hashDBFields.put("strBiospecIndicAmnio", "SUBTYPEDESC");
        hashDBFields.put("strBiospecKaryotype", "EXTRATXT2");
	
        //Biofluid - Cords Blood
        hashDBFields.put("strBiospecCordArtery", "SUBTYPEDESC");
        hashDBFields.put("strBiospecCordVein", "EXTRATXT2");
        hashDBFields.put("strBiospecArteralPH", "EXTRATXT3");
        hashDBFields.put("strBiospecVenousPH", "EXTRATXT4");
	
	
        // Survey to studys table
        hashDBFields.put("intStudySurveyID", "STUDYSURVEYKEY");
        
                //edited by rennypv
        hashDBFields.put("intStudyID", "STUDYKEY");
        hashDBFields.put("intSurveyID", "SURVEYKEY");

  
        
        // Survey Questions table
        hashDBFields.put("intQuestionID", "QUESTIONKEY");
        hashDBFields.put("intQuestionType", "QUESTIONTYPE");
        hashDBFields.put("strQuestion", "QUESTION");
        hashDBFields.put("intQuestionNo", "QUESTIONNO");
        hashDBFields.put("intQuestionOrder", "QUESTIONORDER");
        hashDBFields.put("intQuestionMaxNumeric", "QUESTIONINTMAX");
        hashDBFields.put("intQuestionMinNumeric", "QUESTIONINTMIN");
        hashDBFields.put("dtQuestionMaxDate", "QUESTIONDATEMAX");
        hashDBFields.put("dtQuestionMinDate", "QUESTIONDATEMIN");
        hashDBFields.put("strQuestionScript", "QUESTIONSCRIPT");
        
         
        // Microarray Directoy Table         
        hashDBFields.put("intMaProjectKey", "PROJECTKEY");
       //hashDBFields.put("strMaProjectID", "PROJECTID");
        hashDBFields.put("strMaProjectName","NAME");
        hashDBFields.put("strMaProjectDescription","DESCRIPTION");
        hashDBFields.put("strMaProjectTitle","TITLE");
        hashDBFields.put("strMaResearcher","RESEARCHER");
        hashDBFields.put("strMaStudyName","STUDY_NAME");                
                               
        // -- Tablename: ix_experiment
        hashDBFields.put("intMaExperimentKey", "EXPERIMENTKEY");
        //hashDBFields.put("strMaExperimentID", "EXPERIMENTID");
        hashDBFields.put("intMaExperimentProjectKey","PROJECTKEY");
        hashDBFields.put("dtMaExperimentDate","EXPERIMENT_DATE");
        hashDBFields.put("strMaExperimentName","NAME");
        hashDBFields.put("strMaExperimentDescription","DESCRIPTION");
        hashDBFields.put("strMaAuthor", "AUTHOR");

        // -- Tablename: ix_experiment_attachment
        hashDBFields.put("intMaExpAttachmentKey", "EXPERIMENT_ATTACHMENTKEY");
        hashDBFields.put("intMaExpAttachment_ExpKey","EXPERIMENTKEY");
        hashDBFields.put("dtMaExpAttachmentCreatedDate","CREATED_DATE");
        hashDBFields.put("strMaExpAttachmentName","NAME");
        hashDBFields.put("strMaExpAttachmentType","TYPE");
        hashDBFields.put("strMaExpAttachmentDescription","DESCRIPTION");
        hashDBFields.put("strMaExpAttachmentFilename","FILENAME");
        hashDBFields.put("flMaExpAttachmentFilename","FILE");
        hashDBFields.put("strMaExpAttachmentData", "DATA");
        
        // -- Table name: ix_template
        hashDBFields.put("intTemplateID", "TEMPLATEKEY");
        hashDBFields.put("strDomainName", "DOMAINNAME");
        hashDBFields.put("strTemplateName", "TEMPLATENAME");
        hashDBFields.put("strQuestionOrders", "QUESTIONORDERS");
        hashDBFields.put("flImportFile", "FILE");
        
        // -- Table name: ix_process
        hashDBFields.put("intProcessKey", "PROCESSKEY");        
        hashDBFields.put("strProcessType", "TYPE");
        hashDBFields.put("strProcessStatus", "STATUS");
        hashDBFields.put("strProcessDescription", "DESCRIPTION");
        hashDBFields.put("strProcessReport", "REPORT");
        
        // -- Table name: ix_process
        hashDBFields.put("intConfigKey", "CONFIGKEY");        
        hashDBFields.put("strConfigName", "NAME");
        hashDBFields.put("strConfigValue", "VALUE");
        

        // Questix Fields
        hashDBFields.put("intPartID", "PARTICIPANTKEY");
        hashDBFields.put("strPartFirstName", "PART_FIRST_NAME");
        hashDBFields.put("strPartLastName", "PART_LAST_NAME");
        hashDBFields.put("strPartInitial", "PART_INITIAL");
        hashDBFields.put("strPartLoginName", "PART_LOGIN_NAME");
        hashDBFields.put("strPartPassword", "PART_PASSWORD");
        hashDBFields.put("strPartEmail", "PART_EMAIL");
        hashDBFields.put("intEmailSent", "EMAIL_SENT");
        hashDBFields.put("intQuestixSurveyID", "QUESTIX_SURVEY_ID");
        
        hashDBFields.put("intPartRaterID", "PARTRATERSKEY");
        hashDBFields.put("strRaterFirstName", "RATER_FIRST_NAME");
        hashDBFields.put("strRaterLastName", "RATER_LAST_NAME");
        hashDBFields.put("strRaterInitial", "RATER_INITIAL");
        hashDBFields.put("strRaterLoginName", "RATER_LOGIN_NAME");
        hashDBFields.put("strRaterPassword", "RATER_PASSWORD");
        hashDBFields.put("strRaterEmail", "RATER_EMAIL");
        hashDBFields.put("strRelationship", "RELATIONSHIP");
        hashDBFields.put("strSurveyStatus", "SURVEY_STATUS");
        hashDBFields.put("intNextQuestionOrder", "SURVEY_NEXT_QUESTION_ORDER");
        hashDBFields.put("intFirstQuestionOrder", "SURVEY_FIRST_QUESTION_ORDER");
        hashDBFields.put("intBackButton", "BACK_BUTTON_ON");
        hashDBFields.put("intQuestixEmailID", "QUESTIXEMAILKEY");
        hashDBFields.put("strEmailSubject", "SUBJECT");
        hashDBFields.put("strEmailText", "EMAIL_TEXT");
        hashDBFields.put("strEmailFromName", "EMAIL_FROM_NAME");
        hashDBFields.put("strEmailFromEmail", "EMAIL_FROM_EMAIL");
        
        
    
        // -- Table name: ix_log
        //hashDBFields.put("intLogKey", "");
        hashDBFields.put("strLogType", "TYPE");
        hashDBFields.put("intLogPriority", "PRIORITY");
        hashDBFields.put("intLogResult", "RESULT");
        hashDBFields.put("strLogShortDesc", "SHORT_DESCRIPTION");
        hashDBFields.put("strLogLongDesc", "LONG_DESCRIPTION");
        hashDBFields.put("strLogUser", "USER");
        
        // case --------------------------------------------------
        
        // -- Table name: ix_case
        hashDBFields.put("intCaseID", "CASEKEY");
        hashDBFields.put("strCaseID", "CASEID");
        hashDBFields.put("intCaseParentID", "PARENTKEY");
        hashDBFields.put("dtCaseCreatedDate", "CREATEDDATE");
        hashDBFields.put("dtCaseOccurranceDate", "OCCURRANCEDATE");
        hashDBFields.put("strCaseInitiator", "INITIATOR");
        hashDBFields.put("strCaseBSB", "BSB");
        hashDBFields.put("strCaseConfidentialLevel", "CONFIDENTIAL_LEVEL");
        hashDBFields.put("strCaseDescription", "DESCRIPTION");       
        hashDBFields.put("intCaseActualLoss", "ACTUALLOSS");
        hashDBFields.put("intCaseActiveExposure", "ACTIVEEXPOSURE");
        hashDBFields.put("intCaseProvisionLoss", "PROVISIONLOSS");
        hashDBFields.put("intCaseInsuredAmount", "INSUREDAMOUNT");
        hashDBFields.put("intCaseRecoveryAmount", "RECOVERYAMOUNT");
        hashDBFields.put("intCaseTotalAllocatedLoss", "TOTALALLOCATEDLOSS");
        hashDBFields.put("strCaseCurrencyCode", "CURRENCYCODE");
        hashDBFields.put("strCaseLossCategory", "LOSSCATEGORY");
        hashDBFields.put("strCaseBaselCategory", "BASELCATEGORY");
        hashDBFields.put("strCaseStatus", "STATUS");
        hashDBFields.put("strCaseBU", "BU");
        hashDBFields.put("strCaseLossNotes", "LOSSNOTES");
        hashDBFields.put("strCaseLossAllocFinalNotes", "LOSSALLNOTES");
        hashDBFields.put("strCaseOutcomes", "OUTCOMES");
        hashDBFields.put("strCaseComplexity", "COMPLEXITY");
        hashDBFields.put("dtCaseClosedDate", "CLOSEDDATE");
        hashDBFields.put("intCaseCreditRisk", "CREDITRISK");
        hashDBFields.put("strCaseOutcomesCategory", "OUTCOMESCATEGORY");
        hashDBFields.put("strCaseCluster", "CLUSTER");
        
        // -- Table name: ix_task
        hashDBFields.put("intTaskID", "TASKKEY");
        hashDBFields.put("strTaskDescription", "DESCRIPTION");
        hashDBFields.put("dtTaskAssignedDate", "ASSIGNEDDATE");
        hashDBFields.put("dtTaskDueDate", "DUEDATE");
        hashDBFields.put("strTaskStatus", "STATUS");
        
        // -- Table name: ix_case_diary
        hashDBFields.put("intCaseDiaryID", "CASEDIARYKEY");
        hashDBFields.put("dtCaseDiaryDate",  "DIARYDATE");
        hashDBFields.put("strCaseDiaryAction", "ACTION");
        hashDBFields.put("strCaseDiaryStatus", "STATUS");
        
        // -- Table name: ix_case_appointment
        hashDBFields.put("intCaseAppointmentID", "CASEAPPKEY");
        hashDBFields.put("dtCaseAppointmentDate",  "APPDATE");
        hashDBFields.put("tmCaseAppointmentTime", "APPTIME");
        hashDBFields.put("strCaseAppointmentPurpose", "PURPOSE");
        hashDBFields.put("dtCaseAppointmentNotifyDate",  "NOTIFYDATE");
        
        // -- Table name: ix_case_party
        hashDBFields.put("intCasePartyID", "CASEPARTYKEY");
        hashDBFields.put("strCasePartyID", "PARTYID");
        hashDBFields.put("strCasePartyType", "TYPE");
        hashDBFields.put("strCasePartyFirstname", "FIRSTNAME");
        hashDBFields.put("strCasePartySurname", "SURNAME");
        hashDBFields.put("strCasePartyPhone", "PHONE");
        hashDBFields.put("strCasePartyAddress", "ADDRESS");
        hashDBFields.put("strCasePartySuburb", "SUBURB");
        hashDBFields.put("strCasePartyState", "STATE");
        hashDBFields.put("strCasePartyCountry", "COUNTRY");
        hashDBFields.put("strCasePartyBSB", "BSB");
        hashDBFields.put("strCasePartyNotes", "NOTES");
        
        // -- Table name: ix_case_account
        hashDBFields.put("intCaseAccountID", "CASEACCOUNTKEY");
        hashDBFields.put("strCaseAccountBSB", "BSB");
        hashDBFields.put("strCaseAccountNo", "ACCOUNTNO");
        hashDBFields.put("strCaseAccountType", "TYPE");
        hashDBFields.put("strCaseAccountName", "NAME");
        hashDBFields.put("strCaseAccountNotes", "NOTES");
        
        // -- Table name: ix_case_transaction
        hashDBFields.put("intCaseTransactionID", "CASETRANSACTIONKEY");
        hashDBFields.put("intCaseTransactionAmount", "AMOUNT");
        hashDBFields.put("strCaseTransactionCurrencyCode", "CURRENCYCODE");
        hashDBFields.put("strCaseTransactionType", "TYPE");
        hashDBFields.put("strCaseTransactionFromBSB", "FROMBSB");
        hashDBFields.put("strCaseTransactionFromAccountNo", "FROMACCNO");
        hashDBFields.put("strCaseTransactionFromAccountName", "FROMACCNAME");
        hashDBFields.put("strCaseTransactionFromAccountType", "FROMACCTYPE");
        hashDBFields.put("strCaseTransactionToBSB", "TOBSB");
        hashDBFields.put("strCaseTransactionToAccountNo", "TOACCNO");
        hashDBFields.put("strCaseTransactionToAccountName", "TOACCNAME");
        hashDBFields.put("strCaseTransactionToAccountType", "TOACCTYPE");
        hashDBFields.put("strCaseTransactionNotes", "NOTES");
        
        // -- Table name: ix_case_loss_allocation
        hashDBFields.put("intCaseLossAllocID", "CASELOSSALLOCATIONKEY");
        hashDBFields.put("strCaseLossAllocBU", "BU");
        hashDBFields.put("intCaseLossAllocAmount", "AMOUNT");
        hashDBFields.put("intCaseLossAllocPercentage", "PERCENTAGE");
        hashDBFields.put("dtCaseLossAllocSubmitDate", "SUBMITDATE");
        hashDBFields.put("strCaseLossAllocNotes", "NOTES");
        hashDBFields.put("strCaseLossAllocCurrencyCode", "CURRENCYCODE");
        hashDBFields.put("strCaseLossAllocStatus", "STATUS");
        
        
        // -- Table name: ix_workflow
        hashDBFields.put("intWorkflowID", "WORKFLOWKEY");
        hashDBFields.put("strWorkflowName", "WORKFLOWNAME");
        hashDBFields.put("strWorkflowURL", "WORKFLOWURL");
        
        // User group table
        hashDBFields.put("intUserGroupID", "group_id");
        hashDBFields.put("strMemberService", "member_service");
        hashDBFields.put("strMemberKey", "member_key");
        hashDBFields.put("strMemberIsGroup", "member_is_group");
        
 //-------------------- CASE IMPORT START ----------------------------///       
        // -- Table name: ix_core_case
        hashDBFields.put("intCaseKey", "CORECASEKEY");
        hashDBFields.put("intCaseID", "CASEID");
        hashDBFields.put("intParentCaseKey", "PARENTCASEKEY");
        hashDBFields.put("strCategory", "CATEGORY");
        hashDBFields.put("strCaseOffence", "OFFENCE");
        hashDBFields.put("strCaseCHSID", "CHSID");
        hashDBFields.put("strCaseFileNo", "FILENO");
        hashDBFields.put("strCaseConfidential", "CONFIDENTIAL");
        hashDBFields.put("strCasePriority", "PRIORITY");
        hashDBFields.put("strCaseReferenceID", "REFERENCEID");
        hashDBFields.put("strCaseSource", "SOURCE");
        hashDBFields.put("strCaseDetection", "DETECTION");
        hashDBFields.put("strCaseSummary", "SUMMARY");
        hashDBFields.put("strCaseStatus", "STATUS");
        hashDBFields.put("strSubCategory", "SUBCATEGORY");
        hashDBFields.put("dtCaseCreatedDate", "CREATEDDATE");
        hashDBFields.put("dtCaseOccurrenceDate", "OCCURRENCEDATE");
        hashDBFields.put("dtCaseReceivedDate", "RECEIVEDDATE");
        hashDBFields.put("flCaseSavings", "SAVINGS");
        hashDBFields.put("flCaseRecovery", "RECOVERY");
        hashDBFields.put("flCaseLegalCost", "LEGAL_COSTS");
        hashDBFields.put("flCaseRestitution", "RESTITUTION");
        hashDBFields.put("flCaseAwardedCost", "AWARDED_COSTS");
        hashDBFields.put("flCaseFines", "FINES");
        hashDBFields.put("strCaseComment", "COMMENTS");
        
        
        // -- Table name: ix_related_parties
        hashDBFields.put("intCasePartyKey", "PARTIESKEY");
        hashDBFields.put("strCasePartyID", "ID");
        hashDBFields.put("strCasePartyType", "TYPE");
        hashDBFields.put("strCasePartyName", "NAME");
        hashDBFields.put("strCasePartyFirstname", "FIRSTNAME");
        hashDBFields.put("strCasePartyLastname", "LASTNAME");
        hashDBFields.put("strCasePartySubtype", "SUBTYPE");
        hashDBFields.put("dtCasePartyDOB", "DOB");
        hashDBFields.put("strCasePartySex", "SEX");
        hashDBFields.put("strCasePartyEmail", "EMAIL");
        hashDBFields.put("strCasePartyTitle", "TITLE");
        //hashDBFields.put("strCasePartyFax", "FAX");
        hashDBFields.put("strCasePartyMobile", "MOBILE");
        hashDBFields.put("strCasePartyPhoneAreaCode", "PHONEAREACODE");
        hashDBFields.put("strCasePartyPhoneNumber", "PHONENUMBER");
        hashDBFields.put("strCasePartyFaxAreaCode", "FAXAREACODE");
        hashDBFields.put("strCasePartyFaxNumber", "FAXNUMBER");
        hashDBFields.put("strCasePartyRelationship", "RELATIONSHIP");
        hashDBFields.put("strCasePartyAddress", "ADDRESS");
        hashDBFields.put("strCasePartyState", "STATE");
        hashDBFields.put("strCasePartyPostcode", "POSTCODE");
        hashDBFields.put("strCasePartyCountry", "COUNTRY");
        hashDBFields.put("strCasePartyDescription", "DESCRIPTION");
        hashDBFields.put("strCasePartySuburb", "SUBURB");
        hashDBFields.put("strCasePartyStreetNumber", "STREETNUMBER");
        
        // -- Table name: ix_related_case
        hashDBFields.put("intRelatedCaseKey", "RELATEDCASEKEY");
        hashDBFields.put("intContactKey", "CONTACTKEY");
        hashDBFields.put("intRCaseKey", "CASEKEY");
        
        // -- Table name: ix_smartform_participants
        hashDBFields.put("intSmartformParticipantKey", "SMARTFORMPARTICIPANTKEY");
        hashDBFields.put("intSmartformKey", "SMARTFORMKEY");
        hashDBFields.put("intParticipantKey", "PARTICIPANTKEY");
        hashDBFields.put("strSmartformParticipantDomain", "DOMAIN");
        hashDBFields.put("strSmartformParticipantStatus", "SMARTFORMSTATUS");
        hashDBFields.put("strSmartformParticipantUserNote", "USERNOTE");
        hashDBFields.put("strSmartformParticipantAddedBy", "ADDEDBY");
        hashDBFields.put("dtAddedDate", "DATEADDED");
        hashDBFields.put("strLastUpdatedBy", "LASTUPDATEDBY");
        hashDBFields.put("dtLastUpdatedDate", "DATELASTUPDATED");
        hashDBFields.put("intCurrentPage", "CURRENTPAGE");
        
        // -- Table name: ix_smartform_results
        hashDBFields.put("intSmartformResultKey", "SMARTFORMRESULTKEY");
        //hashDBFields.put("intSmartformParticipantKey", "SMARTFORMPARTICIPANTKEY");
        hashDBFields.put("intSmartformDataElementKey", "DATAELEMENTKEY");
        hashDBFields.put("strDataElementResult", "DATAELEMENTRESULT");
        
        // -- Table name: ix_dataelements
        hashDBFields.put("intDataElementKey", "DATAELEMENT_KEY");
        hashDBFields.put("strDataElementType", "DATAELEMENT_TYPE");
        
        
//-------------------- CASE IMPORT END ----------------------------///       
        

// ------------------------ LOAD THE FIELD LENGTHS ------------------------------------
        hashFieldLengths.put("strPatientID", new Integer(10));
        hashFieldLengths.put("strMedicareNo", new Integer(11));
        hashFieldLengths.put("strOtherID", new Integer(10));
        hashFieldLengths.put("strHospitalUR", new Integer(10));
        hashFieldLengths.put("strTitle", new Integer(10));
        hashFieldLengths.put("strSurname", new Integer(50));
        hashFieldLengths.put("strFirstName", new Integer(50));
        hashFieldLengths.put("strAddressLine1", new Integer(100));
        hashFieldLengths.put("strAddressSuburb", new Integer(100));
        hashFieldLengths.put("strAddressState", new Integer(100));
        hashFieldLengths.put("strAddressOtherState", new Integer(100));
        hashFieldLengths.put("strAddressCountry", new Integer(100));
        hashFieldLengths.put("strAddressOtherCountry", new Integer(100));
        hashFieldLengths.put("intAddressPostCode", new Integer(10));
        hashFieldLengths.put("intPhoneHome", new Integer(20));
        hashFieldLengths.put("intPhoneWork", new Integer(20));
        hashFieldLengths.put("intPhoneMobile", new Integer(20));

                //-- Table name : ix_flag
        
        hashFieldLengths.put("strFlagDomain", new Integer(20));
        hashFieldLengths.put("intID", new Integer(20));
        hashFieldLengths.put("struser", new Integer(20));
      
        // -- Table name: ix_inv_site

        hashDBFields.put("intInvSiteID", "SITEKEY");

        hashDBFields.put("strInvSiteName", "SITENAME");

        hashDBFields.put("strInvSiteAddress", "ADDRESS");

        hashDBFields.put("strInvSiteContact", "CONTACT");

        hashDBFields.put("strInvSitePhone", "PHONE");

        

        // -- Table name: ix_inv_tank

        hashDBFields.put("intInvTankID", "TANKKEY");

        hashDBFields.put("strInvTankName", "TANKNAME");

        hashDBFields.put("strInvTankDescription", "DESCRIPTION");

        hashDBFields.put("strInvTankLocation", "LOCATION");

        hashDBFields.put("intInvTankCapacity", "CAPACITY");

        hashDBFields.put("intInvTankAvailable", "AVAILABLE");

        hashDBFields.put("dtInvTankCommissionDate", "COMMISSIONDATE");

        hashDBFields.put("dtInvTankDecommissionDate", "DECOMMISSIONDATE");

        hashDBFields.put("dtInvTankLastServiceDate", "LASTSERVICEDATE");

        hashDBFields.put("strInvTankLastServiceNote", "LASTSERVICENOTE");

        hashDBFields.put("strInvTankStatus", "TANKSTATUS");

        

        // -- Table name: ix_inv_box

        hashDBFields.put("intInvBoxID", "BOXKEY");

        hashDBFields.put("strInvBoxName", "BOXNAME");

        hashDBFields.put("intInvBoxCapacity", "CAPACITY");

        hashDBFields.put("intInvBoxAvailable", "AVAILABLE");

        hashDBFields.put("strInvBoxDescription", "DESCRIPTION");

        

        // -- Table name: ix_inv_tray

        hashDBFields.put("intInvTrayID", "TRAYKEY");

        hashDBFields.put("strInvTrayName", "TRAYNAME");

        hashDBFields.put("intInvTrayCapacity", "CAPACITY");

        hashDBFields.put("intInvTrayAvailable", "AVAILABLE");

        hashDBFields.put("intInvTrayNoOfCol", "NOOFCOL");

        hashDBFields.put("intInvTrayNoOfRow", "NOOFROW");

        

        // -- Table name: ix_inv_cell

        hashDBFields.put("intInvCellID", "CELLKEY");

        hashDBFields.put("intInvCellColNo", "COLNO");

        hashDBFields.put("intInvCellRowNo", "ROWNO");

        hashDBFields.put("strInvCellStatus", "STATUS");

        // Questix Fields
        hashDBFields.put("intPartID", "PARTICIPANTKEY");
        hashDBFields.put("strPartFirstName", "PART_FIRST_NAME");
        hashDBFields.put("strPartLastName", "PART_LAST_NAME");
        hashDBFields.put("strPartInitial", "PART_INITIAL");
        hashDBFields.put("strPartLoginName", "PART_LOGIN_NAME");
        hashDBFields.put("strPartPassword", "PART_PASSWORD");
        hashDBFields.put("strPartEmail", "PART_EMAIL");
        hashDBFields.put("intEmailSent", "EMAIL_SENT");
        hashDBFields.put("intQuestixSurveyID", "QUESTIX_SURVEY_ID");
        
        hashDBFields.put("intPartRaterID", "PARTRATERSKEY");
        hashDBFields.put("strRaterFirstName", "RATER_FIRST_NAME");
        hashDBFields.put("strRaterLastName", "RATER_LAST_NAME");
        hashDBFields.put("strRaterInitial", "RATER_INITIAL");
        hashDBFields.put("strRaterLoginName", "RATER_LOGIN_NAME");
        hashDBFields.put("strRaterPassword", "RATER_PASSWORD");
        hashDBFields.put("strRaterEmail", "RATER_EMAIL");
        hashDBFields.put("strRelationship", "RELATIONSHIP");
        hashDBFields.put("strSurveyStatus", "SURVEY_STATUS");
        hashDBFields.put("intNextQuestionOrder", "SURVEY_NEXT_QUESTION_ORDER");
        hashDBFields.put("intFirstQuestionOrder", "SURVEY_FIRST_QUESTION_ORDER");
        hashDBFields.put("intBackButton", "BACK_BUTTON_ON");
        hashDBFields.put("intQuestixEmailID", "QUESTIXEMAILKEY");
        hashDBFields.put("strEmailSubject", "SUBJECT");
        hashDBFields.put("strEmailText", "EMAIL_TEXT");
        hashDBFields.put("strEmailFromName", "EMAIL_FROM_NAME");
        hashDBFields.put("strEmailFromEmail", "EMAIL_FROM_EMAIL");
        
        // User group table -- ADDED for Questix
        hashDBFields.put("intUserGroupID", "group_id");
        hashDBFields.put("strMemberService", "member_service");
        hashDBFields.put("strMemberKey", "member_key");
        hashDBFields.put("strMemberIsGroup", "member_is_group");


        // -- Table name: ix_workflow_template
        hashDBFields.put("strWorkflowTemplateStatus", "WORKFLOW_TEMPLATE_STATUS");
        hashDBFields.put("strWorkflowInstanceStatus", "WORKFLOW_INSTANCE_STATUS");
		
// ------------------------ LOAD THE FIELD LENGTHS ------------------------------------

        hashFieldLengths.put("strPatientID", new Integer(10));

        hashFieldLengths.put("strMedicareNo", new Integer(11));
        
         hashFieldLengths.put("strOtherID", new Integer(10));

        hashFieldLengths.put("strHospitalUR", new Integer(10));

        hashFieldLengths.put("strTitle", new Integer(10));

        hashFieldLengths.put("strSurname", new Integer(50));

        hashFieldLengths.put("strFirstName", new Integer(50));

        hashFieldLengths.put("strAddressLine1", new Integer(100));

        hashFieldLengths.put("strAddressSuburb", new Integer(100));

        hashFieldLengths.put("strAddressState", new Integer(100));

        hashFieldLengths.put("strAddressOtherState", new Integer(100));

        hashFieldLengths.put("strAddressCountry", new Integer(100));

        hashFieldLengths.put("strAddressOtherCountry", new Integer(100));

        hashFieldLengths.put("intAddressPostCode", new Integer(10));

        hashFieldLengths.put("intPhoneHome", new Integer(20));

        hashFieldLengths.put("intPhoneWork", new Integer(20));

        hashFieldLengths.put("intPhoneMobile", new Integer(20));


        hashFieldLengths.put("strEmail", new Integer(100));
        hashFieldLengths.put("strStatus", new Integer(10));
        hashFieldLengths.put("strHospital", new Integer(50));
        hashFieldLengths.put("strSex", new Integer(20));
        
        // Appointment Table
        hashFieldLengths.put("strAppNotify", new Integer(100));
        hashFieldLengths.put("strAppPurpose", new Integer(100));
        
        // List of values Table
        hashFieldLengths.put("strType", new Integer(100));
        hashFieldLengths.put("strValue", new Integer(100));
        hashFieldLengths.put("strDescription", new Integer(100));
        
        //attachments
        hashFieldLengths.put("strAttachmentDomain", new Integer(20));
        hashFieldLengths.put("strAttachmentID", new Integer(20));
        hashFieldLengths.put("strAttachmentBy", new Integer(20));
        hashFieldLengths.put("strAttachmentFilename", new Integer(20));
        hashFieldLengths.put("strAttachmentComments", new Integer(20));
        
        // Consent contact
        
        
         // Consent study table
        hashFieldLengths.put("strResearcher", new Integer(50));
        hashFieldLengths.put("strRefDoctor", new Integer(50));
        hashFieldLengths.put("strConsentComments", new Integer(100));
        hashFieldLengths.put("strConsentFileName", new Integer(50)); 
        
        //Survey table
        hashFieldLengths.put("strSurveyName" , new Integer(100));
        hashFieldLengths.put("strSurveyDesc" , new Integer(100));
        
        // Survey question option
        hashFieldLengths.put("strOptionLabel", new Integer(150));
        hashFieldLengths.put("strOptionValue", new Integer(150));
        
        // Study table
        hashFieldLengths.put("strStudyName", new Integer(100));
        hashFieldLengths.put("strStudyOwner", new Integer(50));
        hashFieldLengths.put("strStudyDesc" , new Integer(100));
        hashFieldLengths.put("strStudyCode", new Integer(30));
        
        // Survey patient table
        
        
        //Survey results table
        hashFieldLengths.put("strDataText", new Integer(200));
        hashFieldLengths.put("strDataChoices", new Integer(150));
        
        // Study Ethics table
        hashFieldLengths.put("strEthicsApprovedBy", new Integer(100));
        hashFieldLengths.put("strEthicsFileName", new Integer(50));
        hashFieldLengths.put("strEthicsComments", new Integer(100));

        // Person Directoy Table 
        hashFieldLengths.put("strUsername", new Integer(35));
        hashFieldLengths.put("strPassword", new Integer(64));
        hashFieldLengths.put("strPersonLastName", new Integer(15));
        hashFieldLengths.put("strPersonFirstName", new Integer(15));
        hashFieldLengths.put("strPersonEmail", new Integer(60));
	
        // Group Table 
        hashFieldLengths.put("strActivityName", new Integer(50));
        hashFieldLengths.put("strActivityDesc", new Integer(100));

        // Group Table 
        hashFieldLengths.put("strProfileName", new Integer(50));
        hashFieldLengths.put("strProfileDesc", new Integer(100));

        // Group Table 
        

        // User Table 
        
       
        // Biospecimen 
        hashFieldLengths.put("strBiospecimenID", new Integer(50));
        hashFieldLengths.put("strBiospecParentID", new Integer(50));
        hashFieldLengths.put("strBiospecSampleType", new Integer(255));
        hashFieldLengths.put("strBiospecSampleSubType", new Integer(255));
        hashFieldLengths.put("strBiospecLocation", new Integer(255));
        hashFieldLengths.put("strBiospecOtherID", new Integer(255));
        hashFieldLengths.put("strCollaborator", new Integer(255));
        hashFieldLengths.put("strComments", new Integer(255));
        hashFieldLengths.put("strEncounter", new Integer(255));	
        
        //Biotissue
        hashFieldLengths.put("strBiospecDescription", new Integer(255));
        hashFieldLengths.put("strBiospecCode", new Integer(255));
        hashFieldLengths.put("strBiospecTreatment", new Integer(255));

        //Biofluid
        

        //Biofluid - Amniocentesis
        hashFieldLengths.put("strBiospecIndicAmnio", new Integer(255));
        hashFieldLengths.put("strBiospecKaryotype", new Integer(255));
	
        //Biofluid - Cords Blood
        hashFieldLengths.put("strBiospecCordArtery", new Integer(255));
        hashFieldLengths.put("strBiospecCordVein", new Integer(255));
        hashFieldLengths.put("strBiospecArteralPH", new Integer(255));
        hashFieldLengths.put("strBiospecVenousPH", new Integer(255));
	
	
        // Survey to studys table
        
        
        // Survey Questions table
        hashFieldLengths.put("strQuestion", new Integer(200));
        hashFieldLengths.put("strQuestionScript", new Integer(250));
        
         
        // Microarray Directoy Table         
        hashFieldLengths.put("strMaProjectName",new Integer(255));
        hashFieldLengths.put("strMaProjectDescription", new Integer(255));
        hashFieldLengths.put("strMaProjectTitle", new Integer(255));
        hashFieldLengths.put("strMaResearcher", new Integer(255));
        hashFieldLengths.put("strMaStudyName",new Integer(100));                
                               
        // -- Tablename: ix_experiment
        hashFieldLengths.put("strMaExperimentName",new Integer(255));
        hashFieldLengths.put("strMaExperimentDescription",new Integer(255));
        hashFieldLengths.put("strMaAuthor", new Integer(255));

        // -- Tablename: ix_experiment_attachment
        hashFieldLengths.put("strMaExpAttachmentName",new Integer(255));
        hashFieldLengths.put("strMaExpAttachmentType",new Integer(255));
        hashFieldLengths.put("strMaExpAttachmentDescription",new Integer(255));
        hashFieldLengths.put("strMaExpAttachmentFilename",new Integer(255));
        hashFieldLengths.put("strMaExpAttachmentData", new Integer(255));
        
        // -- Table name: ix_template
        hashFieldLengths.put("strDomainName", "DOMAINNAME");
        hashFieldLengths.put("strTemplateName", new Integer(100));
        
        // -- Table name: ix_process
       
	// -- Table name: ix_workflow_template
        hashFieldLengths.put("strWorkflowTemplateStatus", new Integer(15));
        hashFieldLengths.put("strWorkflowInstanceStatus", new Integer(15));
        
        
// ------------------------ LOAD THE FIELD TYPES --------------------------------------
        
        hashDBFieldTypes.put("intInternalPatientID", INT_TYPE);
        hashDBFieldTypes.put("strPatientID", STR_TYPE);
        hashDBFieldTypes.put("strMedicareNo", STR_TYPE);
        hashDBFieldTypes.put("strOtherID", STR_TYPE);
        hashDBFieldTypes.put("strHospitalUR", STR_TYPE);
        hashDBFieldTypes.put("strTitle", STR_TYPE);
        hashDBFieldTypes.put("strSurname", STR_TYPE);
        hashDBFieldTypes.put("strFirstName", STR_TYPE);
        hashDBFieldTypes.put("dtDob", DATE_TYPE);
        hashDBFieldTypes.put("strAddressNumber", STR_TYPE);
        hashDBFieldTypes.put("strAddressLine1", STR_TYPE);
        hashDBFieldTypes.put("strAddressSuburb", STR_TYPE);
        hashDBFieldTypes.put("strAddressState", STR_TYPE);
        hashDBFieldTypes.put("strAddressOtherState", STR_TYPE);
        hashDBFieldTypes.put("strAddressCountry", STR_TYPE);
        hashDBFieldTypes.put("strAddressOtherCountry", STR_TYPE);
        hashDBFieldTypes.put("intAddressPostCode", STR_TYPE);
        hashDBFieldTypes.put("intPhoneWork", STR_TYPE);
        hashDBFieldTypes.put("intPhoneHome", STR_TYPE);
        hashDBFieldTypes.put("intPhoneMobile", STR_TYPE);
        hashDBFieldTypes.put("strEmail", STR_TYPE);
        hashDBFieldTypes.put("strStatus", STR_TYPE);
        hashDBFieldTypes.put("strHospital", STR_TYPE);
        hashDBFieldTypes.put("strSex", STR_TYPE);
        
        //attachments
        
        hashDBFieldTypes.put("strAttachmentDomain", STR_TYPE);
        hashDBFieldTypes.put("strAttachmentID", STR_TYPE);
        hashDBFieldTypes.put("strAttachmentBy", STR_TYPE);
        hashDBFieldTypes.put("strAttachmentFilename", STR_TYPE);
        hashDBFieldTypes.put("strAttachmentComments", STR_TYPE);
        hashDBFieldTypes.put("ATTACHMENTS_strAttachmentsFileName", FILE_TYPE);
        
        //flag
        
        hashDBFieldTypes.put("strFlagDomain", STR_TYPE);
        hashDBFieldTypes.put("intID", INT_TYPE);
        hashDBFieldTypes.put("struser", STR_TYPE);

        
        hashDBFieldTypes.put("intAppointmentID", INT_TYPE);
        hashDBFieldTypes.put("intAppPatientID", INT_TYPE);
        hashDBFieldTypes.put("dtAppDate", DATE_TYPE);
        hashDBFieldTypes.put("strSentStamp", STR_TYPE);
        hashDBFieldTypes.put("tmAppTime", TIME_TYPE);
        hashDBFieldTypes.put("strAppNotify", STR_TYPE);
        hashDBFieldTypes.put("dtAppAlertDate", DATE_TYPE);
        hashDBFieldTypes.put("strAppPurpose", STR_TYPE);
        
        // List of values Table
        hashDBFieldTypes.put("intLovKey", INT_TYPE);
        hashDBFieldTypes.put("strType", STR_TYPE);
        hashDBFieldTypes.put("strValue", STR_TYPE);
        hashDBFieldTypes.put("strDescription", STR_TYPE);
        hashDBFieldTypes.put("intSortOrder", INT_TYPE);
        
         hashDBFieldTypes.put("intDeleted", INT_TYPE);
        
        // Consent contact table
        hashDBFieldTypes.put("intFutureStudy", INT_TYPE);
        hashDBFieldTypes.put("intConsentID", INT_TYPE);
        hashDBFieldTypes.put("intContactOK", INT_TYPE);
        
        
        // Consent study table
        hashDBFieldTypes.put("intConsentStudyID", INT_TYPE);
        hashDBFieldTypes.put("intConsentApproved", INT_TYPE);
        hashDBFieldTypes.put("dtConsentDateApproved", DATE_TYPE);
        hashDBFieldTypes.put("strResearcher", STR_TYPE);
        hashDBFieldTypes.put("strRefDoctor", STR_TYPE);
        hashDBFieldTypes.put("intStudyApproved", INT_TYPE);
        hashDBFieldTypes.put("strConsentFileName", STR_TYPE);
        hashDBFieldTypes.put("strConsentComments", STR_TYPE);
        hashDBFieldTypes.put("flConsentFile", FILE_TYPE);
        hashDBFieldTypes.put("intStudyID", INT_TYPE);
        
        // Study biospecimen
        hashDBFieldTypes.put("intStudyBiospecimenID", INT_TYPE);
	
		
        //Survey table
        hashDBFieldTypes.put("intSurveyID", INT_TYPE);
        hashDBFieldTypes.put("strSurveyName" , STR_TYPE);
        hashDBFieldTypes.put("strSurveyDesc" , STR_TYPE);

        hashDBFieldTypes.put("strQuestixSurveyStatus" , STR_TYPE);

        hashDBFieldTypes.put("strQuestixSurveyStatus" , STR_TYPE);
        
        hashDBFieldTypes.put("intSmartformKey", INT_TYPE);
        hashDBFieldTypes.put("strSmartformName" , STR_TYPE);
        hashDBFieldTypes.put("strSmartformDesc" , STR_TYPE);
        hashDBFieldTypes.put("strDomain" , STR_TYPE);
        
/*        hashDBFieldTypes.put("intPatientSmartformKey", INT_TYPE);
        hashDBFieldTypes.put("intSmartformPatientKey" , INT_TYPE);
        hashDBFieldTypes.put("intSmartformSmartformKey" , INT_TYPE);
        hashDBFieldTypes.put("intSmartformStudyKey" , INT_TYPE);*/
   
        
        // Survey question options
        hashDBFieldTypes.put("intOptionID", INT_TYPE);
        hashDBFieldTypes.put("strOptionLabel", STR_TYPE);
        hashDBFieldTypes.put("strOptionValue", STR_TYPE);
        hashDBFieldTypes.put("intOptionOrder", INT_TYPE);
       
        
        //Survey Results table
        hashDBFieldTypes.put("intSurveyResultsID", INT_TYPE);
        hashDBFieldTypes.put("intParticipantID", INT_TYPE);
        hashDBFieldTypes.put("strDataText", STR_TYPE);
        hashDBFieldTypes.put("strDataChoices", STR_TYPE);
        hashDBFieldTypes.put("intDataNumeric", FLOAT_TYPE);
        hashDBFieldTypes.put("dtDataDate", DATE_TYPE);
        
        // Ethics consent table
        
        hashDBFieldTypes.put("intEthicsStudyID", INT_TYPE);
        hashDBFieldTypes.put("strEthicsApprovedBy", STR_TYPE);
        hashDBFieldTypes.put("intEthicsApproved", INT_TYPE);
        hashDBFieldTypes.put("dtEthicsAppDate", DATE_TYPE);
        hashDBFieldTypes.put("strEthicsFileName", STR_TYPE);
        hashDBFieldTypes.put("strEthicsComments", STR_TYPE);
        hashDBFieldTypes.put("flEthicsFile", FILE_TYPE); // Not used in database
        

// ------------------------ uPortal's Person Directory fields  
        hashDBFieldTypes.put("strUsername", STR_TYPE);
        hashDBFieldTypes.put("strPassword", STR_TYPE);     
        hashDBFieldTypes.put("strPersonLastName", STR_TYPE);    
        hashDBFieldTypes.put("strPersonFirstName", STR_TYPE);    
        hashDBFieldTypes.put("strPersonEmail", STR_TYPE); 
        hashDBFieldTypes.put("strPersonProfessionalLevel", STR_TYPE);

	// Group Table 
        hashDBFieldTypes.put("intActivityID", INT_TYPE);
        hashDBFieldTypes.put("strActivityName", STR_TYPE);
        hashDBFieldTypes.put("strActivityDesc", STR_TYPE);

	// Group Table 
        hashDBFieldTypes.put("intProfileID", INT_TYPE);
        hashDBFieldTypes.put("strProfileName", STR_TYPE);
        hashDBFieldTypes.put("strProfileDesc", STR_TYPE);

        //Group Table
	hashDBFieldTypes.put("intUserProfileID", INT_TYPE);
        hashDBFieldTypes.put("intForeignProfileID", INT_TYPE);

	// Group Table 
        hashDBFieldTypes.put("intGenixGroupID", INT_TYPE);

	// User Table 
        hashDBFieldTypes.put("intUserID", INT_TYPE);
        hashDBFieldTypes.put("intDefaultLayoutID", INT_TYPE);
        hashDBFieldTypes.put("intDefaultUserID", INT_TYPE);
        hashDBFieldTypes.put("intNextStructID", INT_TYPE);
        
        // Study table
        hashDBFieldTypes.put("intStudyID", INT_TYPE);
        hashDBFieldTypes.put("strStudyName", STR_TYPE);
        hashDBFieldTypes.put("strStudyOwner", STR_TYPE);
        hashDBFieldTypes.put("strStudyDesc" , STR_TYPE);
        hashDBFieldTypes.put("dtStudyStart", DATE_TYPE);
        hashDBFieldTypes.put("dtStudyEnd", DATE_TYPE);
        hashDBFieldTypes.put("strStudyCode", STR_TYPE);
        hashDBFieldTypes.put("intTargetPatientNo", INT_TYPE);
        hashDBFieldTypes.put("intActualPatientNo", INT_TYPE);
        
        // Survey Patients
        hashDBFieldTypes.put("intSurveyPatientsID", INT_TYPE);
        hashDBFieldTypes.put("intSurveyStatus", INT_TYPE);
               
        hashDBFieldTypes.put("intSmartformKey", INT_TYPE);
        hashDBFieldTypes.put("strSmartformName" , STR_TYPE);
        hashDBFieldTypes.put("strSmartformDesc" , STR_TYPE);
        hashDBFieldTypes.put("strDomain" , STR_TYPE);
   
        // Biospecimen 
        hashDBFieldTypes.put("intBiospecimenID", INT_TYPE);
        hashDBFieldTypes.put("strBiospecimenID", STR_TYPE);
        hashDBFieldTypes.put("intBiospecParentID", INT_TYPE);
        hashDBFieldTypes.put("strBiospecParentID", STR_TYPE);
        hashDBFieldTypes.put("strBiospecSampleType", STR_TYPE);
        hashDBFieldTypes.put("strBiospecSampleSubType", STR_TYPE);
        hashDBFieldTypes.put("strBiospecLocation", STR_TYPE);
        hashDBFieldTypes.put("dtBiospecSampleDate", DATE_TYPE);
        hashDBFieldTypes.put("strBiospecOtherID", STR_TYPE);
        hashDBFieldTypes.put("strBiospecGestAt", STR_TYPE);
        hashDBFieldTypes.put("intBiospecStudyID", INT_TYPE);
        hashDBFieldTypes.put("strBiospecGrade", STR_TYPE);
        hashDBFieldTypes.put("strCollaborator", STR_TYPE);
        hashDBFieldTypes.put("strComments", STR_TYPE); 
        hashDBFieldTypes.put("strEncounter", STR_TYPE);
        hashDBFieldTypes.put("intCellID", INT_TYPE);
//        hashDBFieldTypes.put("intPatientID", INT_TYPE);
  
        //Anita Start
        hashDBFieldTypes.put("strStoredIn", STR_TYPE);
        hashDBFieldTypes.put("tmBiospecSampleTime", TIME_TYPE);
        hashDBFieldTypes.put("tmBiospecExtractedTime", TIME_TYPE);
        //Anita End  
		
        hashDBFieldTypes.put("dtBiospecDateExtracted", DATE_TYPE);
        hashDBFieldTypes.put("dtBiospecDateDistributed", DATE_TYPE);
        hashDBFieldTypes.put("strBiospecSpecies", STR_TYPE);
        
	//Biotissue
        hashDBFieldTypes.put("strBiospecDescription", STR_TYPE);
        hashDBFieldTypes.put("strBiospecCode", STR_TYPE);
        hashDBFieldTypes.put("strBiospecTreatment", STR_TYPE);
        hashDBFieldTypes.put("intBiospecNumCollected", FLOAT_TYPE);
        hashDBFieldTypes.put("intBiospecNumRemoved", FLOAT_TYPE);

	//Biofluid
        hashDBFieldTypes.put("intBiospecVolume", FLOAT_TYPE);
        hashDBFieldTypes.put("intBiospecVolRemoved", FLOAT_TYPE);

	//Biofluid - Amniocentesis
        hashDBFieldTypes.put("strBiospecIndicAmnio", STR_TYPE);
        hashDBFieldTypes.put("strBiospecKaryotype", STR_TYPE);
	
	//Biofluid - Cords Blood
        hashDBFieldTypes.put("strBiospecCordArtery", STR_TYPE);
        hashDBFieldTypes.put("strBiospecCordVein", STR_TYPE);
	hashDBFieldTypes.put("strBiospecArteralPH", STR_TYPE);
	hashDBFieldTypes.put("strBiospecVenousPH", STR_TYPE);

        // Study to survey table
        hashDBFieldTypes.put("intStudySurveyID", INT_TYPE);
        hashDBFieldTypes.put("intStudyID", INT_TYPE);
        hashDBFieldTypes.put("intSurveyID", INT_TYPE);
 
        // Survey question table
        hashDBFieldTypes.put("intQuestionID", INT_TYPE);
        hashDBFieldTypes.put("intQuestionType", INT_TYPE);
        hashDBFieldTypes.put("strQuestion", STR_TYPE);
        hashDBFieldTypes.put("intQuestionNo", INT_TYPE);          
        hashDBFieldTypes.put("intQuestionOrder", INT_TYPE);    
        hashDBFieldTypes.put("intQuestionMaxNumeric", FLOAT_TYPE);
        hashDBFieldTypes.put("intQuestionMinNumeric", FLOAT_TYPE);
        hashDBFieldTypes.put("dtQuestionMaxDate", DATE_TYPE);
        hashDBFieldTypes.put("dtQuestionMinDate", DATE_TYPE);
        hashDBFieldTypes.put("strQuestionScript", SCRIPT_TYPE);
               
          // Microarray tables
        // -- Tablename: ix_project
        hashDBFieldTypes.put("intMaProjectKey", INT_TYPE);
	//hashDBFieldTypes.put("strMaProjectID", STR_TYPE);
	hashDBFieldTypes.put("strMaProjectName",STR_TYPE);
	hashDBFieldTypes.put("strMaProjectDescription",STR_TYPE);
	hashDBFieldTypes.put("strMaProjectTitle",STR_TYPE);
        hashDBFieldTypes.put("strMaResearcher",STR_TYPE);
        hashDBFieldTypes.put("strMaStudyName",STR_TYPE);                               
        
        // -- Tablename: ix_experiment
        hashDBFieldTypes.put("intMaExperimentKey", INT_TYPE);
	//hashDBFieldTypes.put("strMaExperimentID", STR_TYPE);
        hashDBFieldTypes.put("intMaExperimentProjectKey",INT_TYPE);
        hashDBFieldTypes.put("dtMaExperimentDate",DATE_TYPE);
	hashDBFieldTypes.put("strMaExperimentName",STR_TYPE);
	hashDBFieldTypes.put("strMaExperimentDescription",STR_TYPE);
	hashDBFieldTypes.put("strMaAuthor", STR_TYPE);

        // -- Tablename: ix_experiment_attachment
        hashDBFieldTypes.put("intMaExpAttachmentKey", INT_TYPE);
        hashDBFieldTypes.put("intMaExpAttachment_ExpKey",INT_TYPE);        
	hashDBFieldTypes.put("strMaExpAttachmentName",STR_TYPE);
        hashDBFieldTypes.put("strMaExpAttachmentType",STR_TYPE);
	hashDBFieldTypes.put("dtMaExpAttachmentCreatedDate",DATE_TYPE);
        hashDBFieldTypes.put("strMaExpAttachmentDescription",STR_TYPE);
        hashDBFieldTypes.put("strMaExpAttachmentFilename",STR_TYPE);
        hashDBFieldTypes.put("flMaExpAttachmentFilename",FILE_TYPE);
	hashDBFieldTypes.put("strMaExpAttachmentData", STR_TYPE);
        
        // -- Table name: ix_template
        hashDBFieldTypes.put("intTemplateID", INT_TYPE);
        hashDBFieldTypes.put("strDomainName", STR_TYPE);
        hashDBFieldTypes.put("strTemplateName", STR_TYPE);
        hashDBFieldTypes.put("strQuestionOrders", STR_TYPE);
        hashDBFieldTypes.put("flImportFile", FILE_TYPE);
                
        // -- Table name: ix_process
        hashDBFieldTypes.put("intProcessKey", INT_TYPE);        
        hashDBFieldTypes.put("strProcessType", STR_TYPE);
        hashDBFieldTypes.put("strProcessStatus", STR_TYPE);
        hashDBFieldTypes.put("strProcessDescription", STR_TYPE);
        hashDBFieldTypes.put("strProcessReport", STR_TYPE);
        
          // -- Table name: ix_config
        hashDBFieldTypes.put("intConfigKey", INT_TYPE);        
        hashDBFieldTypes.put("strConfigName", STR_TYPE);
        hashDBFieldTypes.put("strConfigValue", STR_TYPE);
        

       // Questix Fields
        hashDBFieldTypes.put("intPartID", INT_TYPE);
        hashDBFieldTypes.put("strPartFirstName", STR_TYPE);
        hashDBFieldTypes.put("strPartLastName", STR_TYPE);
        hashDBFieldTypes.put("strPartInitial", STR_TYPE);
        hashDBFieldTypes.put("strPartLoginName", STR_TYPE);
        hashDBFieldTypes.put("strPartPassword", STR_TYPE);
        hashDBFieldTypes.put("strPartEmail", STR_TYPE);
        hashDBFieldTypes.put("intEmailSent", INT_TYPE);
        
        hashDBFieldTypes.put("intQuestixSurveyID", INT_TYPE);
        
        hashDBFieldTypes.put("intPartRaterID", INT_TYPE);
        hashDBFieldTypes.put("strRaterFirstName", STR_TYPE);
        hashDBFieldTypes.put("strRaterLastName", STR_TYPE);
        hashDBFieldTypes.put("strRaterInitial", STR_TYPE);
        hashDBFieldTypes.put("strRaterLoginName", STR_TYPE);
        hashDBFieldTypes.put("strRaterPassword", STR_TYPE);
        hashDBFieldTypes.put("strRaterEmail", STR_TYPE);
        hashDBFieldTypes.put("strRelationship", STR_TYPE);
        hashDBFieldTypes.put("strSurveyStatus", STR_TYPE);
        hashDBFieldTypes.put("intNextQuestionOrder", INT_TYPE);
        hashDBFieldTypes.put("intFirstQuestionOrder", INT_TYPE);
        hashDBFieldTypes.put("intBackButton", INT_TYPE);
        
        hashDBFieldTypes.put("intQuestixEmailID", INT_TYPE);
        hashDBFieldTypes.put("strEmailSubject", STR_TYPE);
        hashDBFieldTypes.put("strEmailText", STR_TYPE);
        hashDBFieldTypes.put("strEmailFromName", STR_TYPE);
        hashDBFieldTypes.put("strEmailFromEmail", STR_TYPE);
        // -- Table name: ix_log
        hashDBFieldTypes.put("strLogType", STR_TYPE);
        hashDBFieldTypes.put("intLogPriority", INT_TYPE);
        hashDBFieldTypes.put("intLogResult", INT_TYPE);
        hashDBFieldTypes.put("strLogShortDesc", STR_TYPE);
        hashDBFieldTypes.put("strLogLongDesc", STR_TYPE);
        hashDBFieldTypes.put("strLogUser", STR_TYPE);
        
        // case -----------------------------------------------------------------
        
        // -- Table name: ix_case
        hashDBFieldTypes.put("intCaseID", INT_TYPE);
        hashDBFieldTypes.put("strCaseID", STR_TYPE);
        hashDBFieldTypes.put("intCaseParentID", INT_TYPE);
        hashDBFieldTypes.put("dtCaseCreatedDate", DATE_TYPE);
        hashDBFieldTypes.put("dtCaseOccurranceDate", DATE_TYPE);
        hashDBFieldTypes.put("strCaseInitiator", STR_TYPE);
        hashDBFieldTypes.put("strCaseBSB", STR_TYPE);
        hashDBFieldTypes.put("strCaseConfidentialLevel", STR_TYPE);
        hashDBFieldTypes.put("strCaseDescription", STR_TYPE);
        hashDBFieldTypes.put("intCaseActualLoss", FLOAT_TYPE);
        hashDBFieldTypes.put("intCaseActiveExposure", FLOAT_TYPE);
        hashDBFieldTypes.put("intCaseProvisionLoss", FLOAT_TYPE);
        hashDBFieldTypes.put("intCaseInsuredAmount", FLOAT_TYPE);
        hashDBFieldTypes.put("intCaseRecoveryAmount", FLOAT_TYPE);
        hashDBFieldTypes.put("intCaseTotalAllocatedLoss", FLOAT_TYPE);
        hashDBFieldTypes.put("strCaseCurrencyCode", STR_TYPE);
        hashDBFieldTypes.put("strCaseLossCategory", STR_TYPE);
        hashDBFieldTypes.put("strCaseBaselCategory", STR_TYPE);
        hashDBFieldTypes.put("strCaseStatus", STR_TYPE);
        hashDBFieldTypes.put("strCaseBU", STR_TYPE);
        hashDBFieldTypes.put("strCaseLossNotes", STR_TYPE);
        hashDBFieldTypes.put("strCaseLossAllocFinalNotes", STR_TYPE);
        hashDBFieldTypes.put("strCaseOutcomes", STR_TYPE);
        hashDBFieldTypes.put("strCaseComplexity", STR_TYPE);
        hashDBFieldTypes.put("dtCaseClosedDate", DATE_TYPE);
        hashDBFieldTypes.put("intCaseCreditRisk", INT_TYPE);
        hashDBFieldTypes.put("strCaseOutcomesCategory", STR_TYPE);
        hashDBFieldTypes.put("strCaseCluster", STR_TYPE);
                
        // -- Table name: ix_task
        hashDBFieldTypes.put("intTaskID", INT_TYPE);
        hashDBFieldTypes.put("strTaskDescription", STR_TYPE);
        hashDBFieldTypes.put("dtTaskAssignedDate", DATE_TYPE);
        hashDBFieldTypes.put("dtTaskDueDate", DATE_TYPE);
        hashDBFieldTypes.put("strTaskStatus", STR_TYPE);
        
        // -- Table name: ix_case_diary
        hashDBFieldTypes.put("intCaseDiaryID", INT_TYPE);
        hashDBFieldTypes.put("dtCaseDiaryDate",  DATE_TYPE);
        hashDBFieldTypes.put("strCaseDiaryAction", STR_TYPE);
        hashDBFieldTypes.put("strCaseDiaryStatus", STR_TYPE);
        
        // -- Table name: ix_case_appointment
        hashDBFieldTypes.put("intCaseAppointmentID", INT_TYPE);
        hashDBFieldTypes.put("dtCaseAppointmentDate",  DATE_TYPE);
        hashDBFieldTypes.put("tmCaseAppointmentTime", TIME_TYPE);
        hashDBFieldTypes.put("strCaseAppointmentPurpose", STR_TYPE);
        hashDBFieldTypes.put("dtCaseAppointmentNotifyDate",  DATE_TYPE);
        
        // -- Table name: ix_case_party
        hashDBFieldTypes.put("intCasePartyID", INT_TYPE);
        hashDBFieldTypes.put("strCasePartyID", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyType", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyFirstname", STR_TYPE);
        hashDBFieldTypes.put("strCasePartySurname", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyPhone", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyAddress", STR_TYPE);
        hashDBFieldTypes.put("strCasePartySuburb", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyState", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyCountry", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyBSB", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyNotes", STR_TYPE);
        
        // -- Table name: ix_case_account
        hashDBFieldTypes.put("intCaseAccountID", INT_TYPE);
        hashDBFieldTypes.put("strCaseAccountBSB", STR_TYPE);
        hashDBFieldTypes.put("strCaseAccountNo", STR_TYPE);
        hashDBFieldTypes.put("strCaseAccountType", STR_TYPE);
        hashDBFieldTypes.put("strCaseAccountName", STR_TYPE);
        hashDBFieldTypes.put("strCaseAccountNotes", STR_TYPE);
        
        // -- Table name: ix_case_transaction
        hashDBFieldTypes.put("intCaseTransactionID", INT_TYPE);
        hashDBFieldTypes.put("intCaseTransactionAmount", FLOAT_TYPE);
        hashDBFieldTypes.put("strCaseTransactionCurrencyCode", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionType", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionFromBSB", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionFromAccountNo", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionFromAccountName", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionFromAccountType", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionToBSB", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionToAccountNo", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionToAccountName", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionToAccountType", STR_TYPE);
        hashDBFieldTypes.put("strCaseTransactionNotes", STR_TYPE);
        
        // -- Table name: ix_case_loss_allocation
        hashDBFieldTypes.put("intCaseLossAllocID", INT_TYPE);
        hashDBFieldTypes.put("strCaseLossAllocBU", STR_TYPE);
        hashDBFieldTypes.put("intCaseLossAllocAmount", FLOAT_TYPE);
        hashDBFieldTypes.put("intCaseLossAllocPercentage", FLOAT_TYPE);
        hashDBFieldTypes.put("dtCaseLossAllocSubmitDate", DATE_TYPE);
        hashDBFieldTypes.put("strCaseLossAllocNotes", STR_TYPE);
        hashDBFieldTypes.put("strCaseLossAllocCurrencyCode", STR_TYPE);
        hashDBFieldTypes.put("strCaseLossAllocStatus", STR_TYPE);
        
        // -- Table name: ix_workflow
        hashDBFieldTypes.put("intWorkflowID", INT_TYPE);
        hashDBFieldTypes.put("strWorkflowName", STR_TYPE);
        hashDBFieldTypes.put("strWorkflowURL", STR_TYPE);


        

        

        // --- Cell inventory --------------------------------------------------

        

        // -- Table name: ix_inv_site

        hashDBFieldTypes.put("intInvSiteID", INT_TYPE);

        hashDBFieldTypes.put("strInvSiteName", STR_TYPE);

        hashDBFieldTypes.put("strInvSiteAddress", STR_TYPE);

        hashDBFieldTypes.put("strInvSiteContact", STR_TYPE);

        hashDBFieldTypes.put("strInvSitePhone", STR_TYPE);

        

        // -- Table name: ix_inv_tank

        hashDBFieldTypes.put("intInvTankID", INT_TYPE);

        hashDBFieldTypes.put("strInvTankName", STR_TYPE);

        hashDBFieldTypes.put("strInvTankDescription", STR_TYPE);

        hashDBFieldTypes.put("strInvTankLocation", STR_TYPE);

        hashDBFieldTypes.put("intInvTankCapacity", INT_TYPE);

        hashDBFieldTypes.put("intInvTankAvailable", INT_TYPE);

        hashDBFieldTypes.put("dtInvTankCommissionDate", DATE_TYPE);

        hashDBFieldTypes.put("dtInvTankDecommissionDate", DATE_TYPE);

        hashDBFieldTypes.put("dtInvTankLastServiceDate", DATE_TYPE);

        hashDBFieldTypes.put("strInvTankLastServiceNote", STR_TYPE);

        hashDBFieldTypes.put("strInvTankStatus", STR_TYPE);

        

        // -- Table name: ix_inv_box

        hashDBFieldTypes.put("intInvBoxID", INT_TYPE);

        hashDBFieldTypes.put("strInvBoxName", STR_TYPE);

        hashDBFieldTypes.put("intInvBoxCapacity", INT_TYPE);

        hashDBFieldTypes.put("intInvBoxAvailable", INT_TYPE);

        hashDBFieldTypes.put("strInvBoxDescription", STR_TYPE);

        // Questix Fields
        hashDBFieldTypes.put("intPartID", INT_TYPE);
        hashDBFieldTypes.put("strPartFirstName", STR_TYPE);
        hashDBFieldTypes.put("strPartLastName", STR_TYPE);
        hashDBFieldTypes.put("strPartInitial", STR_TYPE);
        hashDBFieldTypes.put("strPartLoginName", STR_TYPE);
        hashDBFieldTypes.put("strPartPassword", STR_TYPE);
        hashDBFieldTypes.put("strPartEmail", STR_TYPE);
        hashDBFieldTypes.put("intEmailSent", INT_TYPE);
        
        hashDBFieldTypes.put("intQuestixSurveyID", INT_TYPE);
        
        hashDBFieldTypes.put("intPartRaterID", INT_TYPE);
        hashDBFieldTypes.put("strRaterFirstName", STR_TYPE);
        hashDBFieldTypes.put("strRaterLastName", STR_TYPE);
        hashDBFieldTypes.put("strRaterInitial", STR_TYPE);
        hashDBFieldTypes.put("strRaterLoginName", STR_TYPE);
        hashDBFieldTypes.put("strRaterPassword", STR_TYPE);
        hashDBFieldTypes.put("strRaterEmail", STR_TYPE);
        hashDBFieldTypes.put("strRelationship", STR_TYPE);
        hashDBFieldTypes.put("strSurveyStatus", STR_TYPE);
        hashDBFieldTypes.put("intNextQuestionOrder", INT_TYPE);
        hashDBFieldTypes.put("intFirstQuestionOrder", INT_TYPE);
        hashDBFieldTypes.put("intBackButton", INT_TYPE);
        
        hashDBFieldTypes.put("intQuestixEmailID", INT_TYPE);
        hashDBFieldTypes.put("strEmailSubject", STR_TYPE);
        hashDBFieldTypes.put("strEmailText", STR_TYPE);
        hashDBFieldTypes.put("strEmailFromName", STR_TYPE);
        hashDBFieldTypes.put("strEmailFromEmail", STR_TYPE);
        
        // USer group membership -- ADD for Questix
       hashDBFieldTypes.put("intUserGroupID", INT_TYPE);
        hashDBFieldTypes.put("strMemberService", STR_TYPE);
        hashDBFieldTypes.put("strMemberKey", STR_TYPE);
        hashDBFieldTypes.put("strMemberIsGroup", STR_TYPE);

        // -- Table name: ix_inv_tray

        hashDBFieldTypes.put("intInvTrayID", INT_TYPE);

        hashDBFieldTypes.put("strInvTrayName", STR_TYPE);

        hashDBFieldTypes.put("intInvTrayCapacity", INT_TYPE);

        hashDBFieldTypes.put("intInvTrayAvailable", INT_TYPE);

        hashDBFieldTypes.put("intInvTrayNoOfCol", INT_TYPE);

        hashDBFieldTypes.put("intInvTrayNoOfRow", INT_TYPE);

        

        // -- Table name: ix_inv_cell

        hashDBFieldTypes.put("intInvCellID", INT_TYPE);

        hashDBFieldTypes.put("intInvCellColNo", INT_TYPE);

        hashDBFieldTypes.put("intInvCellRowNo", INT_TYPE);

        hashDBFieldTypes.put("strInvCellStatus", STR_TYPE);

        
        // USer group membership
       hashDBFieldTypes.put("intUserGroupID", INT_TYPE);
        hashDBFieldTypes.put("strMemberService", STR_TYPE);
        hashDBFieldTypes.put("strMemberKey", STR_TYPE);
        hashDBFieldTypes.put("strMemberIsGroup", STR_TYPE);

	// -- Table name: ix_workflow_template
        hashDBFieldTypes.put("strWorkflowTemplateStatus", STR_TYPE);
        hashDBFieldTypes.put("strWorkflowInstanceStatus", STR_TYPE);
        
        
//-------------------- CASE IMPORT START ----------------------------///    
        
        // -- Table name: ix_core_case
        hashDBFieldTypes.put("intCaseKey", INT_TYPE);
        hashDBFieldTypes.put("intCaseID", STR_TYPE);
        hashDBFieldTypes.put("intParentCaseKey", INT_TYPE);
        hashDBFieldTypes.put("strCategory", STR_TYPE);
        hashDBFieldTypes.put("strCaseOffence", STR_TYPE);
        hashDBFieldTypes.put("strCaseCHSID", STR_TYPE);
        hashDBFieldTypes.put("strCaseFileNo", STR_TYPE);
        hashDBFieldTypes.put("strCaseConfidential", STR_TYPE);
        hashDBFieldTypes.put("strCasePriority", STR_TYPE);
        hashDBFieldTypes.put("strCaseReferenceID", STR_TYPE);
        hashDBFieldTypes.put("strCaseSource", STR_TYPE);
        hashDBFieldTypes.put("strCaseDetection", STR_TYPE);
        hashDBFieldTypes.put("strCaseSummary", STR_TYPE);
        hashDBFieldTypes.put("strCaseStatus", STR_TYPE);
        hashDBFieldTypes.put("strSubCategory", STR_TYPE);
        hashDBFieldTypes.put("dtCaseCreatedDate", DATE_TYPE);
        hashDBFieldTypes.put("dtCaseOccurrenceDate", DATE_TYPE);
        hashDBFieldTypes.put("dtCaseReceivedDate", DATE_TYPE);
        hashDBFieldTypes.put("flCaseSavings", FLOAT_TYPE);
        hashDBFieldTypes.put("flCaseRecovery", FLOAT_TYPE);
        hashDBFieldTypes.put("flCaseLegalCost", FLOAT_TYPE);
        hashDBFieldTypes.put("flCaseRestitution", FLOAT_TYPE);
        hashDBFieldTypes.put("flCaseAwardedCost", FLOAT_TYPE);
        hashDBFieldTypes.put("flCaseFines", FLOAT_TYPE);
        hashDBFieldTypes.put("strCaseComment", STR_TYPE);
        
        
        // -- Table name: ix_related_parties
        hashDBFieldTypes.put("intCasePartyKey", INT_TYPE);
        hashDBFieldTypes.put("strCasePartyID", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyType", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyName", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyFirstname", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyLastname", STR_TYPE);
        hashDBFieldTypes.put("strCasePartySubtype", STR_TYPE);
        hashDBFieldTypes.put("dtCasePartyDOB", DATE_TYPE);
        hashDBFieldTypes.put("strCasePartySex", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyEmail", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyTitle", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyFaxAreaCode", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyFaxNumber", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyMobile", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyPhoneAreaCode", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyPhoneNumber", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyRelationship", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyAddress", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyState", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyPostcode", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyCountry", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyDescription", STR_TYPE);
        hashDBFieldTypes.put("strCasePartySuburb", STR_TYPE);
        hashDBFieldTypes.put("strCasePartyStreetNumber", STR_TYPE);
        
        // -- Table name: ix_related_case
        hashDBFieldTypes.put("intRelatedCaseKey", INT_TYPE);
        hashDBFieldTypes.put("intContactKey", INT_TYPE);
        hashDBFieldTypes.put("intRCaseKey", INT_TYPE);
        
        // -- Table name: ix_smartform_participants
        hashDBFieldTypes.put("intSmartformParticipantKey", INT_TYPE);
        hashDBFieldTypes.put("intSmartformKey", INT_TYPE);
        hashDBFieldTypes.put("intParticipantKey", INT_TYPE);
        hashDBFieldTypes.put("strSmartformParticipantDomain", STR_TYPE);
        hashDBFieldTypes.put("strSmartformParticipantStatus", STR_TYPE);
        hashDBFieldTypes.put("strSmartformParticipantUserNote", STR_TYPE);
        hashDBFieldTypes.put("strSmartformParticipantAddedBy", STR_TYPE);
        hashDBFieldTypes.put("dtAddedDate", DATE_TYPE);
        hashDBFieldTypes.put("strLastUpdatedBy", STR_TYPE);
        hashDBFieldTypes.put("dtLastUpdatedDate", DATE_TYPE);
        hashDBFieldTypes.put("intCurrentPage", INT_TYPE);
        
        // -- Table name: ix_smartform_results
        hashDBFieldTypes.put("intSmartformResultKey", INT_TYPE);
        //hashDBFieldTypes.put("intSmartformParticipantKey", INT_TYPE);
        hashDBFieldTypes.put("intSmartformDataElementKey", INT_TYPE);
        hashDBFieldTypes.put("strDataElementResult", STR_TYPE);
        
        // -- Table name: ix_dataelements
        hashDBFieldTypes.put("intDataElementKey", INT_TYPE);
        hashDBFieldTypes.put("strDataElementType", STR_TYPE);
        
//-------------------- CASE IMPORT END ----------------------------///       
        

// ------------------------ LOAD THE REQUIRED FIELDS --------------------------------------
        hashDBRequiredFields.put("strPatientID", "required");
        hashDBRequiredFields.put("strBiospecimenID", "required");
        hashDBRequiredFields.put("dtDob", "required");
        hashDBRequiredFields.put("dtAppDate", "required");
        hashDBRequiredFields.put("tmAppTime", "required");
        hashDBRequiredFields.put("strAppNotify", "required");
        hashDBRequiredFields.put("dtConsentDateApproved", "required");
        hashDBRequiredFields.put("strStudyName", "required");
        hashDBRequiredFields.put("strStudyOwner", "required");
        hashDBRequiredFields.put("strSurveyName", "required");
        hashDBRequiredFields.put("strEthicsApprovedBy", "required");
        hashDBRequiredFields.put("strUsername", "required");
        hashDBRequiredFields.put("strOldPassword", "required");
        hashDBRequiredFields.put("strNewPassword1", "required");
        hashDBRequiredFields.put("strNewPassword2", "required");
        hashDBRequiredFields.put("dtStudyEnd", "required");
        hashDBRequiredFields.put("strOptionValue", "required");
        hashDBRequiredFields.put("strOptionLabel", "required");
        hashDBRequiredFields.put("strMedicareNo", "required");
        

	// Microarray 
        hashDBRequiredFields.put("strMaProjectName", "required");
        hashDBRequiredFields.put("strMaExperimentName", "required");   
        hashDBRequiredFields.put("strMaExpAttachmentName", "required");
        
        hashDBRequiredFields.put("intMaProjectKey", "required");
        hashDBRequiredFields.put("intMaExperimentKey", "required");
        hashDBRequiredFields.put("intMaExperimentProjectKey","required");

        // ix_template
        hashDBRequiredFields.put("strDomainName","required");
        hashDBRequiredFields.put("strTemplateName","required");
        hashDBRequiredFields.put("strQuestionOrders","required");

        
        // questix
        hashDBRequiredFields.put("strRaterFirstName","required");
        hashDBRequiredFields.put("strRaterLastName","required");
        hashDBRequiredFields.put("strRaterEmail","required");
         
        
        // questix
        hashDBRequiredFields.put("strRaterFirstName","required");
        hashDBRequiredFields.put("strRaterLastName","required");
        hashDBRequiredFields.put("strRaterEmail","required");


// ------------------------ Load the List Of Values fields --------------------------------------
        
        
        /* Biospecimen List of Values */
	hashLOVSelector.put("BIOSPECCOLLECTIONTYPE","Types of Biospecimen");
	hashLOVSelector.put("BIOSPECLOCATION","Locations where Biospecimen can be stored");
	hashLOVSelector.put("BIOSPECFLUIDSUBTYPES","Fluid Subtypes");
	hashLOVSelector.put("BIOSPECAMNIO","Indicative Amniocentesis");
	hashLOVSelector.put("BIOSPECKARYOTYPE","Karyotypes");
	hashLOVSelector.put("BIOSPECTISSUESUBTYPES","Tissue Subtypes");
	hashLOVSelector.put("BIOSPECTREATMENTS","Biospecimen Treatments");
        //hashLOVSelector.put("DIABETES","Diabetes");
	//hashLOVSelector.put("ETHNICITY","ETHNICITY");
	hashLOVSelector.put("EXPERIMENT_ATTACHMENT","Attachment Data Types");
	hashLOVSelector.put("HOSPITAL","Hospitals");
        hashLOVSelector.put("BIO_QUANTITY_UNITS","Units of measure");

        //Anita Start
        hashLOVSelector.put("BIOSPECSTOREDIN", "Stored in");      
        //Anita End
        
        // Patient Encounter Details
        hashLOVSelector.put("EPISODE","Episode");
        hashLOVSelector.put("CONSENTVERSION","Consent Version");
        
	//hashLOVSelector.put("KARYOTYPE","KARYOTYPE");
	//hashLOVSelector.put("PROTEINURIA","Protenuria");
        
        
	/*  Risk Centre List of Values
        hashLOVSelector.put("TRAN_TYPE","Bank Transaction Types");
	hashLOVSelector.put("FRAUD_TYPE","Fraud Types");
        hashLOVSelector.put("BU","Business Unit");
        hashLOVSelector.put("CUR","Currency Codes");
        hashLOVSelector.put("LOSS_CAT","Loss Categories");
        hashLOVSelector.put("LOSS_EVENT","Loss Events");
        //hashLOVSelector.put("CASE_TYPE","Case Types");
        hashLOVSelector.put("CASE_STATUS","Case Statuses");
        hashLOVSelector.put("TASK_STATUS","Task Statuses");
        */

        /* Generic List of Values */
        hashLOVSelector.put("CONTACTPLACE","Contact Place");
	hashLOVSelector.put("COUNTRY","Country");
	hashLOVSelector.put("SEX","Sex");
	hashLOVSelector.put("STATE","State");
	hashLOVSelector.put("STATUS","Status");
	hashLOVSelector.put("TITLE","Title");
        hashLOVSelector.put("BIOSPECGRADE","Grade");
	hashLOVSelector.put("COLLABORATOR","Collaborator");
	hashLOVSelector.put("RESEARCHER","Researcher");
  
	hashLOVSelector.put("WORKFLOW_TEMPLATE_STATUS","Workflow Template Status");
	hashLOVSelector.put("WORKFLOW_INSTANCE_STATUS","Workflow Instance Status");
        

// ------------------------ LOAD THE OPERATORS FOR THE DAO ----------------------------------------
         hashDBOperators.put("EQUALS_OPERATOR", "=");
         hashDBOperators.put("NOT_EQUALS_OPERATOR", "<>");
         hashDBOperators.put("LESSTHAN_OPERATOR", "<");
         hashDBOperators.put("LESSTHAN_AND_EQUAL_OPERATOR", "<=");
         hashDBOperators.put("GREATERTHAN_OPERATOR", ">");
         hashDBOperators.put("GREATERTHAN_AND_EQUAL_OPERATOR", ">=");
         hashDBOperators.put("LIKE_OPERATOR", "LIKE");
         hashDBOperators.put("IN_OPERATOR", "IN");
         hashDBOperators.put("NOT_IN_OPERATOR", "NOT IN");
         
         
// ------------------------ LOAD THE CONNECTORS FOR THE DAO ----------------------------------------
         hashDBConnectors.put("AND_CONNECTOR", "AND");
         hashDBConnectors.put("OR_CONNECTOR", "OR");
         hashDBConnectors.put("INNER_JOIN", "INNER JOIN");
         hashDBConnectors.put("LEFT_JOIN", "LEFT JOIN");
         hashDBConnectors.put("UNION_CONNECTOR", "UNION");
         hashDBConnectors.put("INTERSECT_CONNECTOR", "INTERSECT");
         hashDBConnectors.put("EXCEPT_CONNECTOR", "EXCEPT");
         
         
// ------------------------ List of unique keys ---------------------------------------
       //  hashUniqueKeys.put("intInternalPatientID", INT_TYPE);
       // hashUniqueKeys.put("strPatientID", STR_TYPE);
         
         // Do a basic check to see if the Fields specified have all types
         if(hashDBFields.size() == hashDBFieldTypes.size())
         {
            blLoaded = true;
         }
         else
         {
            throw new DAODBSchema("The number of fields do not match the number of field types in the DBSchema");
         }
    }
}
