/*

 * StudyFormFields.java

 *

 * Created on November 13, 2002, 11:11 AM

 */



package neuragenix.bio.study;

import java.util.Hashtable;

import neuragenix.dao.DBSchema;

import neuragenix.common.ValidateFieldFunctions;



/**

 *

 * @author  Administrator

 */

public class StudyFormFields {

    

    private static boolean blLoaded = false;

    private static Hashtable hashAddStudyFormFields = new Hashtable ( 20 );

    private static Hashtable hashSurveySearchFormFields = new Hashtable ( 20 );

    private static Hashtable hashSurveySearchFormOperators = new Hashtable ( 20 );

    private static Hashtable hashAddSurveyFormFields = new Hashtable ( 5 );

    private static Hashtable hashSurveyViewFormFields = new Hashtable ( 5 );  

    private static Hashtable hashSurveyResultsFormFields = new Hashtable ( 20 );  

    private static Hashtable hashStudyResultsFormFields = new Hashtable ( 20 );

    private static Hashtable hashStudySurveyFormFieldsCutSize = new Hashtable ( 5 );

    

     

     private static Hashtable hashStudySurveyStudyFormFields = new Hashtable ( 6 );

     private static Hashtable hashStudySurveysFormFields = new Hashtable ( 20 );

     private static Hashtable hashStudySmartformFormFields = new Hashtable ( 20 );         

     private static Hashtable hashStudyResultsFormFieldsDropDown = new Hashtable ( 2 );

     private static Hashtable hashStudySearchFormFieldsDropDown = new Hashtable ( 5 );

     private static Hashtable hashViewStudyFormFields = new Hashtable ( 5 );

     private static Hashtable hashStudySearchFormFields = new Hashtable ( 5 );

     private static Hashtable hashStudySearchFormOperators = new Hashtable ( 5 );

     private static Hashtable hashStudySurveyFormFieldsTableDropDown = new Hashtable ( 5 );
     
     private static Hashtable hashStudySmartformFormFieldsTableDropDown = new Hashtable ( 5 );     

     private static Hashtable hashSurveyQuestionsFormFields = new Hashtable ( 5 );

     private static Hashtable hashSurveyCompleteFormFields = new Hashtable ( 5 );

     private static Hashtable hashSurveyQuestionOptionsFormFields = new Hashtable(5);

     private static Hashtable hashViewStudyEthicsFormFields = new Hashtable(5);

     private static Hashtable hashStudyPatientCountFormFields = new Hashtable(1);

     

     

     

     //public static final String SELECT = "SELECT";

     public static final String STUDY_DOMAIN = "STUDY";

     public static final String SURVEY_DOMAIN = "SURVEY";

     public static final String SURVEY_OPTIONS_DOMAIN = "SURVEY_OPTIONS";

     public static final String STUDY_SURVEY_DOMAIN = "STUDY_SURVEY";
     
     public static final String SMARTFORM_DOMAIN = "SMARTFORM";

     public static final String CONSENT_CONTACT = "CONSENT_CONTACT";

     public static final String SURVEY_PATIENTS_DOMAIN = "SURVEY_PATIENTS";

     public static final String CONSENT_STUDY = "CONSENT_STUDY";

     public static final String APPOINTMENTS_DOMAIN = "APPOINTMENTS";

     public static final String SURVEY_QUESTIONS_DOMAIN = "SURVEY_QUESTIONS";

     public static final String SURVEY_RESULTS_DOMAIN = "SURVEY_RESULTS";

     public static final String TEMPLATE_DOMAIN = "TEMPLATE";

     public static final String ETHICS_CONSENT_DOMAIN = "ETHICS_CONSENT";

     public static final String PATIENT_DOMAIN = "PATIENT";

  //   public static final String CONSENT_CONTACT = "CONSENT_CONTACT";

  //   public static final String CONSENT_STUDY = "CONSENT_STUDY";

  //   public static final String STUDY_DOMAIN = "STUDY";

     public static final String INTERNAL_QUESTION_ID = "intQuestionID";

     public static final String QUESTION_ORDER = "intQuestionOrder";

     public static final String QUESTION_NUMBER = "intQuestionNo";

     public static final String QUESTION_TYPE = "intQuestionType";

     public static final String QUESTION_NUMERIC_MAX = "intQuestionMaxNumeric";

     public static final String QUESTION_NUMERIC_MIN = "intQuestionMinNumeric";

     public static final String QUESTION_DATE_MAX = "dtQuestionMaxDate";

     public static final String QUESTION_DATE_MIN = "dtQuestionMinDate";

     public static final String QUESTION = "strQuestion";

     public static final int QUESTION_TYPE_COMMENT = 101;

     public static final int QUESTION_TYPE_TITLE = 102;

     public static final int QUESTION_TYPE_PAGEBREAK = 103;

     public static final int QUESTION_TYPE_TEXTBOX = 1;

     public static final int QUESTION_TYPE_DROPDOWN = 2;

     public static final int QUESTION_TYPE_NUMERIC = 3;

     public static final int QUESTION_TYPE_DATE = 4;

     public static final int QUESTION_TYPE_SCRIPT = 5;

     public static final String INTERNAL_STUDY_ID = "intStudyID";

     public static final String SURVEY_NAME = "strSurveyName";

     public static final String STUDY_NAME = "strStudyName";
     
     public static final String INTERNAL_SMARTFORM_ID = "intSmartformKey";

     public static final String INTERNAL_SURVEY_ID = "intSurveyID";

     public static final String INTERNAL_STUDY_SURVEY_ID = "intStudySurveyID";

     public static final String INTERNAL_ETHICS_CONSENT_ID = "intEthicsStudyID";

     public static final String INTERNAL_PATIENT_ID = "intInternalPatientID";

     public static final String EXTERNAL_PATIENT_ID = "strPatientID";

     public static final String INTERNAL_APPOINTMENT_ID = "intAppointmentID";

     public static final String INTERNAL_CONSENT_ID = "intConsentID";

     public static final String INTERNAL_CONSENT_STUDY_ID = "intConsentStudyID";

     public static final String QUESTION_MOVE_UP = "QUESTION_MOVE_UP";

     public static final String QUESTION_MOVE_DOWN = "QUESTION_MOVE_DOWN";

     public static final String OPTION_MOVE_UP = "OPTION_MOVE_UP";

     public static final String OPTION_MOVE_DOWN = "OPTION_MOVE_DOWN";

     public static final String OPTION_ORDER = "intOptionOrder";

     public static final String INTERNAL_OPTION_ID = "intOptionID";

     public static final String ETHICS_FILENAME = "flEthicsFile";

     public static final String ACTUAL_PART_NUMBER = "intActualPatientNo";

     public static final String QUESTION_SCRIPT = "strQuestionScript";

     public static final String STUDY_OWNER = "strStudyOwner";

     public static final String STUDY_START = "dtStudyStart";
    
     public static final String STUDY_END = "dtStudyEnd";
     

    /** Creates a new instance of PatientFields */

     public StudyFormFields() { 

    }

     public static void unloadFormFields()

    {

        blLoaded = false;

     }

    public static Hashtable getStudyResultsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudyResultsFormFields;     

    }

    public static Hashtable getSurveyResultsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveyResultsFormFields;     

    }

     public static Hashtable getStudyAddFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashAddStudyFormFields;

        

    }

     public static Hashtable getSurveyAddFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashAddSurveyFormFields;

        

    }

     public static Hashtable getStudyViewFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashViewStudyFormFields;

        

    }

     public static Hashtable getSurveyViewFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveyViewFormFields;

        

    }

     public static Hashtable getSurveySearchFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveySearchFormFields;

        

    }

     public static Hashtable getSurveySearchFormOperators()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveySearchFormOperators;

        

    }

     public static Hashtable getStudySearchFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudySearchFormFields;

        

    }

     public static Hashtable getStudySearchFormOperators()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudySearchFormOperators;

        

    }

    public static Hashtable getStudySurveyStudyFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudySurveyStudyFormFields;

        

    }

    //smartform
    
   public static Hashtable getStudySmartformFormFields()



    {
            if(!blLoaded){loadFormFields();}

            return hashStudySmartformFormFields;
    
    }
    
    public static Hashtable getStudySurveysFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudySurveysFormFields;

    }

    public static Hashtable getStudySurveyFormFieldsTableDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudySurveyFormFieldsTableDropDown;

    }

     public static Hashtable getStudySmartformFormFieldsTableDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudySmartformFormFieldsTableDropDown;

    }
    public static Hashtable getSurveyQuestionsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveyQuestionsFormFields;

    }

    public static Hashtable getStudySurveyFormFieldsCutSize()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudySurveyFormFieldsCutSize;

    }

    public static Hashtable getSurveyCompleteFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveyCompleteFormFields;

    }

    public static Hashtable getSurveyQuestionOptionsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveyQuestionOptionsFormFields;

    }

    public static Hashtable getViewStudyEthicsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashViewStudyEthicsFormFields;

    }

    public static Hashtable getStudyPatientCountFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashStudyPatientCountFormFields;

    }

     private static void loadFormFields() 

    {

//------------------------ LOAD ADD STUDY -----------------------------------------

  

        hashAddStudyFormFields.put("strStudyName", "Study name");

        hashAddStudyFormFields.put("strStudyOwner", "Study owner");

        hashAddStudyFormFields.put("strStudyCode", "Study code");

        
        hashAddStudyFormFields.put("strStudyDesc" , "Study description");

        hashAddStudyFormFields.put("dtStudyStart", "Start date (dd/mm/yyyy)");

        hashAddStudyFormFields.put("dtStudyEnd", "End date (dd/mm/yyyy)");

        hashAddStudyFormFields.put("intTargetPatientNo", "Target patient no.");

       

        

// ----------------------- LOAD VIEW STUDY -----------------------------------------        

       

        hashViewStudyFormFields.put("intStudyID", "NA");

        hashViewStudyFormFields.put("strStudyName", "Study name");

        hashViewStudyFormFields.put("strStudyOwner", "Study owner");

        hashViewStudyFormFields.put("strStudyCode", "Study code");
   
        hashViewStudyFormFields.put("strStudyDesc" , "Study description");

        hashViewStudyFormFields.put("dtStudyStart", "Start date (dd/mm/yyyy)");

        hashViewStudyFormFields.put("dtStudyEnd", "End date (dd/mm/yyyy)");

        hashViewStudyFormFields.put("intTargetPatientNo", "Target patient no.");

       // hashViewStudyFormFields.put("intActualPatientNo", "Actual patient no.");

        

// ---------------------- LOAD THE CONSENT ETHICS FOR STUDY ----------------------

        hashViewStudyEthicsFormFields.put("intStudyID", "NA");

        hashViewStudyEthicsFormFields.put("intEthicsStudyID", "NA");

        hashViewStudyEthicsFormFields.put("intEthicsApproved", "Approved");

        hashViewStudyEthicsFormFields.put("dtEthicsAppDate", "Date");

        hashViewStudyEthicsFormFields.put("strEthicsFileName", "Ethics file name");

        hashViewStudyEthicsFormFields.put("strEthicsComments", "Comments");

        hashViewStudyEthicsFormFields.put("strEthicsApprovedBy", "Approved by");



// ------------ ADD THE SEARCH RESULTS FIELDS -------------------------------

        

        hashStudyResultsFormFields.put("intStudyID", "NA");

        hashStudyResultsFormFields.put("strStudyName", "Study name");

        hashStudyResultsFormFields.put("strStudyOwner", "Study owner");

        hashStudyResultsFormFields.put("dtStudyStart", "Start date");

        hashStudyResultsFormFields.put("dtStudyEnd", "End date");    



        

// --------- LOAD THE STUDY SEARCH SCREEN -----------------------

        

        

        hashStudySearchFormFields.put("strStudyName", "Study name");

        hashStudySearchFormFields.put("strStudyOwner", "Study owner");

        hashStudySearchFormFields.put("dtStudyStart", "Start date");

        hashStudySearchFormFields.put("dtStudyEnd", "End date");

     

        // Add the search operators

        hashStudySearchFormOperators.put("strStudyName", DBSchema.LIKE_OPERATOR);

        hashStudySearchFormOperators.put("strStudyOwner", DBSchema.LIKE_OPERATOR);

        hashStudySearchFormOperators.put("dtStudyStart", DBSchema.EQUALS_OPERATOR);

        hashStudySearchFormOperators.put("dtStudyEnd", DBSchema.EQUALS_OPERATOR);

      



// --------- LOAD THE SURVEY SEARCH SCREEN -----------------------

        

        

        hashSurveySearchFormFields.put("strSurveyName", "Survey name");



        // Add the search operators

        hashSurveySearchFormOperators.put("strSurveyName", DBSchema.LIKE_OPERATOR);

     

// --------- LOAD THE SURVEY ADD SCREEN -----------------------        

        

        hashAddSurveyFormFields.put("strSurveyName", "Survey name");

        hashAddSurveyFormFields.put("strSurveyDesc" , "Survey description");

        

// ---------- LOAD THE SURVEY VIEW SCREEN --------------------------------------

        hashSurveyViewFormFields.put("intSurveyID", "NA");

        hashSurveyViewFormFields.put("strSurveyName", "Survey name");

        hashSurveyViewFormFields.put("strSurveyDesc" , "Survey description");

        

// ---------- LOAD THE SURVEY RESULTS SCREEN -------------------------

        

        hashSurveyResultsFormFields.put("intSurveyID", "NA");

        hashSurveyResultsFormFields.put("strSurveyName", "Survey name");

        

// ---------- LOAD THE STUDY SURVEY SCREEN -----------------------------

        

        hashStudySurveyStudyFormFields.put("intStudyID", "NA");

        hashStudySurveyStudyFormFields.put("strStudyName", "Study name");

        hashStudySurveyStudyFormFields.put("strStudyOwner", "Study owner");

        hashStudySurveyStudyFormFields.put("dtStudyStart", "Start date");

        hashStudySurveyStudyFormFields.put("dtStudyEnd", "End date");

        

        hashStudySurveysFormFields.put("intSurveyID", "NA");

        hashStudySurveysFormFields.put("intStudyID", "NA");

        hashStudySurveysFormFields.put("strSurveyName", "Survey name");

        hashStudySurveysFormFields.put("intStudySurveyID", "NA");

        //smartforms
        
         hashStudySmartformFormFields.put("intSmartformKey", "NA");

        hashStudySmartformFormFields.put("intStudyID", "NA");
        
        hashStudySmartformFormFields.put("strSmartformName", "Smartform name");
        
        hashStudySmartformFormFields.put("intStudySurveyID", "NA");



         // TABLE Drop Downs

        hashStudySurveyFormFieldsTableDropDown.put("intSurveyID", "SURVEY");

        hashStudySurveyFormFieldsTableDropDown.put("strSurveyName", "SURVEY");
        
        //smartform drop downs

         hashStudySmartformFormFieldsTableDropDown.put("intSmartformKey", "SMARTFORM");

        hashStudySmartformFormFieldsTableDropDown.put("strSmartformName", "SMARTFORM");

        // TABLE Drop Down cut sizes

        hashStudySurveyFormFieldsCutSize.put("strQuestion", new Integer(40));

        hashStudySurveyFormFieldsCutSize.put("strOptionLabel", new Integer(40));

        

// --------- LOAD THE ADD QUESTIONS SCREEN --------------------------------

        

        hashSurveyQuestionsFormFields.put("intQuestionID", "Question id");

        hashSurveyQuestionsFormFields.put("intQuestionType", "Question type");

        hashSurveyQuestionsFormFields.put("strQuestion", "Question");

        hashSurveyQuestionsFormFields.put("intQuestionOrder", "NA");

        hashSurveyQuestionsFormFields.put("intQuestionNo", "NA");

        hashSurveyQuestionsFormFields.put("intSurveyID", "NA");

        hashSurveyQuestionsFormFields.put("intQuestionMaxNumeric", "Max numeric value");

        hashSurveyQuestionsFormFields.put("intQuestionMinNumeric", "Min numeric value");

        hashSurveyQuestionsFormFields.put("dtQuestionMaxDate", "Max date value");

        hashSurveyQuestionsFormFields.put("dtQuestionMinDate", "Min date value");

        hashSurveyQuestionsFormFields.put("strQuestionScript", "Script");

        

        

        

// --------- LOAD THE ADD OPTIONS FOR QUESTIONS SCREEN --------------------------------

        hashSurveyQuestionOptionsFormFields.put("intOptionID", "Option id");

        hashSurveyQuestionOptionsFormFields.put("intQuestionID", "Question id");

        hashSurveyQuestionOptionsFormFields.put("strOptionLabel", "Option label");

        hashSurveyQuestionOptionsFormFields.put("strOptionValue", "Option value");

        hashSurveyQuestionOptionsFormFields.put("intOptionOrder", "NA");

        

// --------- LOAD THE SURVEY COMPLETE SCREEN --------------------------------

        

        hashSurveyCompleteFormFields.put("intQuestionID", "Question id");

        hashSurveyCompleteFormFields.put("intQuestionType", "Question type");

        hashSurveyCompleteFormFields.put("strQuestion", "Question");

        hashSurveyCompleteFormFields.put("intQuestionOrder", "NA");

        hashSurveyCompleteFormFields.put("intQuestionNo", "NA");

        hashSurveyCompleteFormFields.put("intQuestionMaxNumeric", "NA");

        hashSurveyCompleteFormFields.put("intQuestionMinNumeric", "NA");

        hashSurveyCompleteFormFields.put("dtQuestionMaxDate", "NA");

        hashSurveyCompleteFormFields.put("dtQuestionMinDate", "NA");

        hashSurveyCompleteFormFields.put("intSurveyID", "NA");

        hashSurveyCompleteFormFields.put("strQuestionScript", "NA");



// --------- LOAD THE SURVEY COMPLETE SCREEN --------------------------------

        

        hashStudyPatientCountFormFields.put("intInternalPatientID", "NA");

      

       

                

        blLoaded = true;

        

     }

    

}

