/*

 * PatientFields.java

 *

 * Created on November 13, 2002, 11:11 AM

 */



package neuragenix.bio.patient;

import java.util.Hashtable;

import neuragenix.dao.DBSchema;

import neuragenix.common.ValidateFieldFunctions;



/**

 *

 * @author  Hayden Molnar

 */

public class PatientFormFields {

    

    private static boolean blLoaded = false;

     private static Hashtable hashAddPatientFormFields = new Hashtable ( 20 );

     private static Hashtable hashAddPatientFormFieldsDropDown = new Hashtable ( 20 );

     private static Hashtable hashPatientResultsFormFields = new Hashtable ( 20 );

     private static Hashtable hashPatientAppointmentsFormFields = new Hashtable ( 20 );

     private static Hashtable hashPatientAppointmentsPatientFormFields = new Hashtable(10);

     private static Hashtable hashPatientResultsFormFieldsDropDown = new Hashtable ( 2 );

     private static Hashtable hashPatientSearchFormFieldsDropDown = new Hashtable ( 20 );

     private static Hashtable hashPatientViewFormFields = new Hashtable ( 20 );

     private static Hashtable hashPatientSearchFormFields = new Hashtable ( 20 );

     private static Hashtable hashPatientSearchFormOperators = new Hashtable ( 20 );

     private static Hashtable hashPatientAddViewValidateFields = new Hashtable ( 20 );

     private static Hashtable hashPatientConsentContactFormFields = new Hashtable (10);

     private static Hashtable hashPatientConsentStudyFormFields = new Hashtable (10);
     
     //Anita Start
     private static Hashtable hashPatientConsentStudyEditFormFields = new Hashtable (10);
     //Anita End

     private static Hashtable hashPatientConsentDownloadFormFields = new Hashtable (10);

     private static Hashtable hashPatientConsentDownloadFileFormFields = new Hashtable (10);

     private static Hashtable hashPatientConsentContactAddFormFields = new Hashtable(1);

     private static Hashtable hashPatientAppFormFieldsTableDropDown = new Hashtable(1);

     private static Hashtable hashPatientAppointmentFormFieldsTableDropDown = new Hashtable(1);

     private static Hashtable hashPatientConsentFormFieldsTableDropDown = new Hashtable(1);

     private static Hashtable hashPatientConsentFormFieldsDropDown = new Hashtable(1);

     private static Hashtable hashPatientFormFieldsCutSize = new Hashtable(5);

     private static Hashtable hashPatientSurveyFormFields = new Hashtable(5);

     private static Hashtable hashPatientStudySurveyFormFields = new Hashtable(5);

     private static Hashtable hashPatientAppForm2FieldsTableDropDown = new Hashtable(5);

     private static Hashtable hashPatientConsentValidateFields = new Hashtable(2);

     private static Hashtable hashPatientHideFields = new Hashtable(2);

     private static Hashtable hashBiospecimenStudyFormFields = new Hashtable (5);
     
     private static Hashtable hashSurveyStudyFormFields = new Hashtable (5);
     
     

     public static final String SELECT = "SELECT";

     public static final String PATIENT_DOMAIN = "PATIENT";

     public static final String CONSENT_CONTACT = "CONSENT_CONTACT";

     public static final String CONSENT_STUDY = "CONSENT_STUDY";

     public static final String SURVEY_DOMAIN = "SURVEY";

     public static final String STUDY_DOMAIN = "STUDY";

     public static final String STUDY_SURVEY_DOMAIN = "STUDY_SURVEY";

     public static final String SURVEY_PATIENTS_DOMAIN = "SURVEY_PATIENTS";

     public static final String SURVEY_RESULTS_DOMAIN = "SURVEY_RESULTS";

     public static final String APPOINTMENTS_DOMAIN = "APPOINTMENTS";

     public static final String BIOSPECIMEN_DOMAIN = "BIOSPECIMEN";

     public static final String INTERNAL_PATIENT_ID = "intInternalPatientID";

     public static final String INTERNAL_STUDY_ID = "intStudyID";

     public static final String INTERNAL_SURVEY_ID = "intSurveyID";

     public static final String EXTERNAL_PATIENT_ID = "strPatientID";

     public static final String INTERNAL_APPOINTMENT_ID = "intAppointmentID";

     public static final String INTERNAL_CONSENT_ID = "intConsentID";

     public static final String INTERNAL_CONSENT_STUDY_ID = "intConsentStudyID";
     
     public static final String INT_APPROVED = "intConsentApproved";

     public static final String INTERNAL_PARTICIPANT_ID = "intParticipantID";

     public static final String STUDY_END_DATE = "dtStudyEnd";

     public static final String SURVEY_NAME = "strSurveyName";

     public static final String APP_DATE = "dtAppDate";

     public static final String CONSENT_DATE = "dtConsentDateApproved";

     public static final String PATIENT_SURNAME = "strSurname"; 

     public static final String PATIENT_FIRST_NAME = "strFirstName"; 

     public static final String PATIENT_DOB = "dtDob"; 
     
     //rennypv for otherID
     
     public static final String PATIENT_OTHERID = "strOtherID"; 
     
     //rennypv
     public static final String NOT_AVAILABLE = "######"; 
     
     public static final String ATTACHMENT_FILENAME = "ATTACHMENTS_strAttachmentsFileName";


    /** Creates a new instance of PatientFields */

     public PatientFormFields() {

    }

     public static void unloadFormFields()

    {

        blLoaded = false;

     }

    public static Hashtable getPatientResultsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientResultsFormFields;     

    }

    public static Hashtable getPatientAddViewValidateFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientAddViewValidateFields;     

    }

     public static Hashtable getPatientAddFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashAddPatientFormFields;

        

    }

     public static Hashtable getPatientViewFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientViewFormFields;

        

    }

     public static Hashtable getPatientAddFormFieldDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashAddPatientFormFieldsDropDown;

      

    }

     public static Hashtable getPatientResultsFormFieldDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientResultsFormFieldsDropDown;

        

    }

     public static Hashtable getPatientSearchFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientSearchFormFields;

        

    }

     public static Hashtable getPatientSearchFormFieldDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientSearchFormFieldsDropDown;

        

    }

     public static Hashtable getPatientSearchFormOperators()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientSearchFormOperators;

        

    }

    public static Hashtable getPatientAppointmentsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientAppointmentsFormFields;

        

    }

    public static Hashtable getPatientAppointmentsPatientFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientAppointmentsPatientFormFields;

        

    }

    public static Hashtable getPatientConsentContactFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentContactFormFields;    

    }

    public static Hashtable getPatientConsentContactAddFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentContactAddFormFields;    

    }

    public static Hashtable getPatientConsentStudyFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentStudyFormFields;    

    }
    
    public static Hashtable getPatientConsentStudyEditFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentStudyEditFormFields;    

    }
    
    public static Hashtable getPatientConsentDownloadFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentDownloadFormFields;    

    }

    public static Hashtable getPatientConsentDownloadFileFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentDownloadFileFormFields;    

    }

    public static Hashtable getPatientAppFormFieldsTableDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientAppFormFieldsTableDropDown;    

    }

    public static Hashtable getPatientAppointmentFormFieldsTableDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientAppointmentFormFieldsTableDropDown;    

    }

    public static Hashtable getPatientConsentFormFieldsTableDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentFormFieldsTableDropDown;    

    }

    public static Hashtable getPatientConsentFormFieldsDropDown()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentFormFieldsDropDown;    

    }

    public static Hashtable getPatientFormFieldsCutSize()

    {

        // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientFormFieldsCutSize;    

    }

    public static Hashtable getPatientStudySurveyFormFields()

    {

        // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientStudySurveyFormFields;    

    }

     public static Hashtable getPatientSurveyFormFields()

    {

        // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientSurveyFormFields;    

    }

    public static Hashtable getPatientAppForm2FieldsTableDropDown()

    {

        // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientAppForm2FieldsTableDropDown;    

    }

    public static Hashtable getPatientConsentValidateFields()

    {

        // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientConsentValidateFields;    

    }

    public static Hashtable getPatientHideFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashPatientHideFields;     

    }
    
    
    public static Hashtable getBiospecimenStudyFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenStudyFormFields;     

    }
    
    public static Hashtable getSurveyStudyFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveyStudyFormFields;     

    }

     private static void loadFormFields() 

    {

//------------------------ Load add patient -----------------------------------------

  //      hashAddPatientFormFields.put("intInternalPatientID", "NA");

        hashAddPatientFormFields.put("strPatientID", "Patient ID");

        hashAddPatientFormFields.put("strOtherID", "Other ID");

        hashAddPatientFormFields.put("strHospitalUR", "Hospital UR");

        hashAddPatientFormFields.put("strTitle", "Title");

        hashAddPatientFormFields.put("strSurname", "Surname");

        hashAddPatientFormFields.put("strFirstName", "First name");

        hashAddPatientFormFields.put("dtDob", "DOB(dd/mm/yyyy)");

        hashAddPatientFormFields.put("strAddressLine1", "Address");

        hashAddPatientFormFields.put("strAddressSuburb", "Suburb");

        hashAddPatientFormFields.put("strAddressState", "State");

        hashAddPatientFormFields.put("strAddressOtherState", "Other state");

        hashAddPatientFormFields.put("strAddressCountry", "Country");

        hashAddPatientFormFields.put("strAddressOtherCountry", "Other country");

        hashAddPatientFormFields.put("intAddressPostCode", "Post code");

        hashAddPatientFormFields.put("intPhoneWork", "Work phone");

        hashAddPatientFormFields.put("intPhoneHome", "Home phone");

        hashAddPatientFormFields.put("intPhoneMobile", "Mobile");

        hashAddPatientFormFields.put("strEmail", "Email");

        hashAddPatientFormFields.put("strStatus", "Status");

        hashAddPatientFormFields.put("strHospital", "Hospital");

        hashAddPatientFormFields.put("strSex", "Sex (M or F)"); 


        // ADD THE Add paitent form DropDowns



        hashAddPatientFormFieldsDropDown.put("strAddressState", "STATE");

        hashAddPatientFormFieldsDropDown.put("strAddressCountry", "COUNTRY");

        hashAddPatientFormFieldsDropDown.put("strStatus", "STATUS");

        hashAddPatientFormFieldsDropDown.put("strTitle", "TITLE");

        hashAddPatientFormFieldsDropDown.put("strSex", "SEX");

        hashAddPatientFormFieldsDropDown.put("strDay", "DAY");
        
	hashAddPatientFormFieldsDropDown.put("strMonth", "MONTH");



// ------------ ADD THE SEARCH RESULTS FIELDS -------------------------------

        hashPatientResultsFormFields.put("intInternalPatientID", "NA");

        hashPatientResultsFormFields.put("strPatientID", "Patient ID");

        hashPatientResultsFormFields.put("strSurname", "Surname");

        hashPatientResultsFormFields.put("strFirstName", "First name");

        hashPatientResultsFormFields.put("dtDob", "DOB");
        
        hashPatientResultsFormFields.put("strOtherID", "Other ID");



//------------------------ Load view patient -----------------------------------------

        hashPatientViewFormFields.put("intInternalPatientID", "NA");

        hashPatientViewFormFields.put("strPatientID", "Patient ID");

        hashPatientViewFormFields.put("strOtherID", "Other ID");

        hashPatientViewFormFields.put("strHospitalUR", "Hospital UR");

        hashPatientViewFormFields.put("strTitle", "Title");

        hashPatientViewFormFields.put("strSurname", "Surname");

        hashPatientViewFormFields.put("strFirstName", "First name");

        hashPatientViewFormFields.put("dtDob", "DOB(dd/mm/yyyy)");

        hashPatientViewFormFields.put("strAddressLine1", "Address");

        hashPatientViewFormFields.put("strAddressSuburb", "Suburb");

        hashPatientViewFormFields.put("strAddressState", "State");

        hashPatientViewFormFields.put("strAddressOtherState", "Other state");

        hashPatientViewFormFields.put("strAddressCountry", "Country");

        hashPatientViewFormFields.put("strAddressOtherCountry", "Other country");

        hashPatientViewFormFields.put("intAddressPostCode", "Post code");

        hashPatientViewFormFields.put("intPhoneWork", "Work phone");

        hashPatientViewFormFields.put("intPhoneHome", "Home phone");

        hashPatientViewFormFields.put("intPhoneMobile", "Mobile");

        hashPatientViewFormFields.put("strEmail", "Email");

        hashPatientViewFormFields.put("strStatus", "Status");

        hashPatientViewFormFields.put("strHospital", "Hospital");

        hashPatientViewFormFields.put("strSex", "Sex (M or F)");
 

// --------- LOAD THE PATIENT SEARCH SCREEN -----------------------

        

        hashPatientSearchFormFields.put("strPatientID", "Patient ID");

        hashPatientSearchFormFields.put("strOtherID", "Other ID");

        hashPatientSearchFormFields.put("strHospitalUR", "Hospital UR");

        hashPatientSearchFormFields.put("strTitle", "Title");

        hashPatientSearchFormFields.put("strSurname", "Surname");

        hashPatientSearchFormFields.put("strFirstName", "First name");

        hashPatientSearchFormFields.put("dtDob", "DOB");

     //   hashPatientSearchFormFields.put("intStudyID", "NA");

        // Add the search operators

        hashPatientSearchFormOperators.put("strPatientID", DBSchema.LIKE_OPERATOR);

        hashPatientSearchFormOperators.put("strOtherID",DBSchema.LIKE_OPERATOR);

        hashPatientSearchFormOperators.put("strHospitalUR", DBSchema.LIKE_OPERATOR);

        hashPatientSearchFormOperators.put("strTitle", DBSchema.EQUALS_OPERATOR);

        hashPatientSearchFormOperators.put("strSurname", DBSchema.LIKE_OPERATOR);

        hashPatientSearchFormOperators.put("strFirstName", DBSchema.LIKE_OPERATOR);

        hashPatientSearchFormOperators.put("dtDob", DBSchema.EQUALS_OPERATOR);

   //     hashPatientSearchFormOperators.put("intStudyID", DBSchema.EQUALS_OPERATOR);

        // Search Drop Down

	hashPatientSearchFormFieldsDropDown.put("strDay", "DAY");        

	hashPatientSearchFormFieldsDropDown.put("strMonth", "MONTH");        
 

// ---------- LOAD THE APPOINTMENT SCREEN -----------------------------

        // There are two because the page deals with two distinct areas, patient and appointments

       

        hashPatientAppointmentsPatientFormFields.put("intInternalPatientID", "NA");

        hashPatientAppointmentsPatientFormFields.put("strPatientID", "Patient ID");
        
        hashPatientAppointmentsPatientFormFields.put("strOtherID", "Other ID");

        hashPatientAppointmentsPatientFormFields.put("strHospitalUR", "Hospital UR");

        hashPatientAppointmentsPatientFormFields.put("strSurname", "Surname");

        hashPatientAppointmentsPatientFormFields.put("strFirstName", "First name");

        hashPatientAppointmentsPatientFormFields.put("dtDob", "DOB");



        hashPatientAppointmentsFormFields.put("intInternalPatientID", "NA");

        hashPatientAppointmentsFormFields.put("intAppointmentID", "NA");

        hashPatientAppointmentsFormFields.put("intSurveyID", "NA");

        hashPatientAppointmentsFormFields.put("dtAppDate", "Date (dd/mm/yyyy)");

        hashPatientAppointmentsFormFields.put("tmAppTime", "Time (e.g.,6:05PM)");

        hashPatientAppointmentsFormFields.put("strAppNotify", "Notify");

        hashPatientAppointmentsFormFields.put("strAppPurpose", "Purpose");

        hashPatientAppointmentsFormFields.put("dtAppAlertDate", "Alert on (dd/mm/yyyy)");

        hashPatientAppointmentsFormFields.put("strSurveyName", "Survey Name");

        

        // TABLE Drop Downs

        hashPatientAppFormFieldsTableDropDown.put("intSurveyID", "SURVEY");

        hashPatientAppFormFieldsTableDropDown.put("strSurveyName", "SURVEY");


        hashPatientAppForm2FieldsTableDropDown.put("strPersonEmail", "PERSON_DIR");


        hashPatientAppointmentFormFieldsTableDropDown.put("strDay", "DAY"); 

        hashPatientAppointmentFormFieldsTableDropDown.put("strMonth", "MONTH"); 

        hashPatientAppointmentFormFieldsTableDropDown.put("strHour", "HOUR"); 

        hashPatientAppointmentFormFieldsTableDropDown.put("strMinute", "MINUTE"); 

        hashPatientAppointmentFormFieldsTableDropDown.put("strTime", "TIME"); 

        hashPatientAppointmentFormFieldsTableDropDown.put("strAlertDay", "DAY"); 

        hashPatientAppointmentFormFieldsTableDropDown.put("strAlertMonth", "MONTH"); 

// ---------------- ADD THE CONSENT FORM ----------------------------------------------

        hashPatientConsentContactFormFields.put("intInternalPatientID", "NA");

        hashPatientConsentContactFormFields.put("intConsentID", "NA");

        hashPatientConsentContactFormFields.put("intFutureStudy", "All current and future studies");

        hashPatientConsentContactFormFields.put("intContactOK", "Patient would like to be contacted for future studies");

        

        hashPatientConsentContactAddFormFields.put("intInternalPatientID", "NA");

        

        

// ---------------- ADD THE CONSENT STUDY FORM --------------------------------------------------------------------      

        hashPatientConsentStudyFormFields.put("dtConsentDateApproved", "Consented date");

        hashPatientConsentStudyFormFields.put("strResearcher", "Researcher");

        hashPatientConsentStudyFormFields.put("strRefDoctor", "Ref doctor");

        hashPatientConsentStudyFormFields.put("strConsentFileName", "NA");

        hashPatientConsentStudyFormFields.put("intStudyApproved", "Consented");

        hashPatientConsentStudyFormFields.put("strConsentComments", "Comments");

        hashPatientConsentStudyFormFields.put("intConsentStudyID", "NA");

        hashPatientConsentStudyFormFields.put("intStudyID", "NA");

        hashPatientConsentStudyFormFields.put("intConsentID", "NA");

        hashPatientConsentStudyFormFields.put("strStudyName", "Study name");

        
// ---------------- EDIT THE CONSENT STUDY FORM --------------------------------------------------------------------      

        hashPatientConsentStudyEditFormFields.put("dtConsentDateApproved", "Consented date");

        hashPatientConsentStudyEditFormFields.put("strResearcher", "Researcher");

        hashPatientConsentStudyEditFormFields.put("strRefDoctor", "Ref doctor");

        //hashPatientConsentStudyEditFormFields.put("strConsentFileName", "NA");

        hashPatientConsentStudyEditFormFields.put("intStudyApproved", "Consented");

        hashPatientConsentStudyEditFormFields.put("strConsentComments", "Comments");

        hashPatientConsentStudyEditFormFields.put("intConsentStudyID", "NA");

        //hashPatientConsentStudyEditFormFields.put("intStudyID", "NA");

        //hashPatientConsentStudyEditFormFields.put("intConsentID", "NA");

        //hashPatientConsentStudyEditFormFields.put("strStudyName", "Study name");

        

        // TABLE Drop Downs

        hashPatientConsentFormFieldsTableDropDown.put("intStudyID", "STUDY");

        hashPatientConsentFormFieldsTableDropDown.put("strStudyName", "STUDY");

        hashPatientConsentFormFieldsDropDown.put("strDay", "DAY"); 

        hashPatientConsentFormFieldsDropDown.put("strMonth", "MONTH"); 

        hashPatientConsentFormFieldsDropDown.put("strResearcher", "RESEARCHER");
         

// ---------------- ADD THE CONSENT DOWNLOAD FORM --------------------------

      //  hashPatientConsentDownloadFormFields.put("flConsentFile", "Select file");

        hashPatientConsentDownloadFormFields.put("intConsentStudyID", "NA");

        hashPatientConsentDownloadFormFields.put("intInternalPatientID", "NA");

        hashPatientConsentDownloadFormFields.put("flConsentFile", "Select signed consent form:");

        // Different from Above !!!!!!!!

        hashPatientConsentDownloadFileFormFields.put("flConsentFile", "Select signed consent form:");



// ---------------- ADD THE STUDY PATIENT FORM--------------------------

        

        hashPatientStudySurveyFormFields.put("intStudyID", "NA");

        hashPatientStudySurveyFormFields.put("intSurveyID", "NA");

        hashPatientStudySurveyFormFields.put("strSurveyName", "Survey name");

        hashPatientStudySurveyFormFields.put("intSurveyStatus", "Survey Status");

        

        

        hashPatientSurveyFormFields.put("intInternalPatientID", "NA");

        hashPatientSurveyFormFields.put("intStudyID", "NA");

        hashPatientSurveyFormFields.put("intSurveyID", "NA");

        hashPatientSurveyFormFields.put("intSurveyStatus", "Survey Status");

        

// ---------------- Add the Fields that need to be validated -------------------------

        hashPatientAddViewValidateFields.put("dtDob", new Integer(ValidateFieldFunctions.DOB_CHECK));
       
  //      hashPatientAddViewValidateFields.put("strEmail", new Integer(ValidateFieldFunctions.EMAIL_CHECK));

        hashPatientAddViewValidateFields.put("dtAppDate", new Integer(ValidateFieldFunctions.DATE_GREATER_THAN_OR_EQUAL_TODAY));

         hashPatientAddViewValidateFields.put("strMedicareNo", new Integer(ValidateFieldFunctions.MEDICARENO_CHECK));

  // ----------- Add the consent fields needing validation ------------      

         hashPatientAddViewValidateFields.put("dtConsentDateApproved", new Integer(ValidateFieldFunctions.DATE_NOT_IN_FUTURE));

      //   hashPatientConsentValidateFields

         

        // TABLE Drop Down cut sizes

        hashPatientFormFieldsCutSize.put("strStudyName", new Integer(10));

        hashPatientFormFieldsCutSize.put("strSurveyName", new Integer(10));

  // ----------- Add the patient hide fields ------------      

	hashPatientHideFields.put("strHospitalUR", NOT_AVAILABLE);
        hashPatientHideFields.put("strOtherID", NOT_AVAILABLE);
	hashPatientHideFields.put("strFirstName", NOT_AVAILABLE);        
	hashPatientHideFields.put("strSurname", NOT_AVAILABLE);        
	hashPatientHideFields.put("dtDob", NOT_AVAILABLE);        

  // ----------- Add the biospecimen study fields ------------    
        hashBiospecimenStudyFormFields.put("intBiospecimenID", "Biospecimen key");
        hashBiospecimenStudyFormFields.put("strBiospecimenID", "Biospecimen ID");
        hashBiospecimenStudyFormFields.put("intStudyID", "Study key");
        hashBiospecimenStudyFormFields.put("strStudyName", "Study name");
        
  // ------------ Survey study formfields -----------
        hashSurveyStudyFormFields.put("intSurveyID", "Survey key");
        hashSurveyStudyFormFields.put("strSurveyName", "Survey name");
        hashSurveyStudyFormFields.put("intStudyID", "Study key");
        
        blLoaded = true;

        

     }

    

}

