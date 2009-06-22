/*

 * BiospecimenFields.java

 *

 * Created on November 13, 2002, 11:11 AM

 */



package neuragenix.bio.biospecimen;

import java.util.Hashtable;

import neuragenix.dao.DBSchema;

import neuragenix.common.ValidateFieldFunctions;



/**

 *

 * @author  Administrator

 */

public class BiospecimenFormFields {

    

     private static boolean blLoaded = false;



     private static Hashtable hashBiospecimenFormFields = new Hashtable ( 20 );

     private static Hashtable hashBiospecimenFormFieldsDropDown = new Hashtable ( 20 );

     private static Hashtable hashBiospecimenResultsFormFields = new Hashtable ( 20 );

     private static Hashtable hashBiospecimenViewFormFields = new Hashtable ( 20 );

     private static Hashtable hashBiospecimenSearchFormFields = new Hashtable ( 20 );
     
     private static Hashtable hashBiospecimenFlaggedFormFields = new Hashtable ( 20 );

     private static Hashtable hashBiospecimenSearchFormOperators = new Hashtable ( 20 );

     private static Hashtable hashBiospecimenAddViewValidateFields = new Hashtable ( 20 );

     private static Hashtable hashLookupFields = new Hashtable (5);

    private static Hashtable hashFlagFormFields = new Hashtable(5);

     private static Hashtable hashBiospecimenOtherData = new Hashtable(20);
     
   private static Hashtable hashStudySmartformFormFields = new Hashtable ( 20 );   

     

     private static Hashtable hashCellUpdateForm = new Hashtable();

     private static Hashtable hashTrayUpdateForm = new Hashtable();

     private static Hashtable hashBoxUpdateForm = new Hashtable();

     private static Hashtable hashTankUpdateForm = new Hashtable();

     

     

     private static Hashtable hashTrayIDForm = new Hashtable();

     private static Hashtable hashBoxIDForm = new Hashtable();

     private static Hashtable hashTankIDForm = new Hashtable();

     

     private static Hashtable hashInventoryIDForm = new Hashtable();

     private static Hashtable hashSurveyStudyFormFields = new Hashtable();

     public static final String SELECT = "SELECT";

     public static final String BIOSPECIMEN_DOMAIN = "BIOSPECIMEN";
     
     public static final String STUDY_DOMAIN = "STUDY";

     public static final String STUDY_SURVEY_DOMAIN = "STUDY_SURVEY";

    
     public static final String SMARTFORM_DOMAIN = "SMARTFORM";
     
     
     public static final String FLAG_DOMAIN = "FLAG";

     public static final String INTERNAL_BIOSPECIMEN_ID = "intBiospecimenID";

     public static final String EXTERNAL_BIOSPECIMEN_ID = "strBiospecimenID";
     
     public static final String ATTACHMENT_FILENAME = "ATTACHMENTS_strAttachmentsFileName";
     
          public static final String INTERNAL_STUDY_ID = "intStudyID";
     
      public static final String INTERNAL_PATIENT_ID = "intInternalPatientID";     

    /** Creates a new instance of BiospecimenFields */

     public BiospecimenFormFields() {

    }

     public static void unloadFormFields()

    {

        blLoaded = false;

     }

    public static Hashtable getBiospecimenResultsFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenResultsFormFields;     

    }

    public static Hashtable getBiospecimenAddViewValidateFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenAddViewValidateFields;     

    }

     public static Hashtable getBiospecimenLookupFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashLookupFields;

        

    }

     public static Hashtable getBiospecimenFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenFormFields;

        

    }
     
          public static Hashtable getBiospecimenFlaggedFormFields()
     {
            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenFlaggedFormFields;

     }
     
     public static Hashtable getFlagFormFields()
    {
            if(!blLoaded){loadFormFields();}

            return hashFlagFormFields;

    }

     public static Hashtable getExtraFormFields(String datafields)

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

		if (hashBiospecimenOtherData.containsKey(datafields + " FIELDS" ))

		{

			return (Hashtable) hashBiospecimenOtherData.get(datafields + " FIELDS") ;

		}

		else

		{

			return new Hashtable();

        	}

    }

     public static Hashtable getExtraFormFieldDropDowns(String datafields)

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

		if (hashBiospecimenOtherData.containsKey(datafields + " DROPDOWNS" ))

		{

			return (Hashtable) hashBiospecimenOtherData.get(datafields + " DROPDOWNS") ;

		}

		else

		{

			return new Hashtable();

        	}

    }

     public static Hashtable getBiospecimenFormFieldDropDowns()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenFormFieldsDropDown;

      

    }

     public static Hashtable getBiospecimenSearchFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenSearchFormFields;

        

    }

     public static Hashtable getBiospecimenSearchFormOperators()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBiospecimenSearchFormOperators;

        

    }

     

     public static Hashtable getCellUpdateForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashCellUpdateForm;

        

    }

     public static Hashtable getTrayUpdateForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashTrayUpdateForm;

        

    }

     public static Hashtable getBoxUpdateForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBoxUpdateForm;

        

    }

     public static Hashtable getTankUpdateForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashTankUpdateForm;

        

    }

     public static Hashtable getInventoryIDForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashInventoryIDForm;

        

    }

     

     public static Hashtable getTrayIDForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashTrayIDForm;

        

    }

     public static Hashtable getBoxIDForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashBoxIDForm;

        

    }

     public static Hashtable getTankIDForm()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashTankIDForm;

        

    }
     
      public static Hashtable getStudySmartformFormFields()
    {
            if(!blLoaded){loadFormFields();}

            return hashStudySmartformFormFields;
    
    }
     public static Hashtable getSurveyStudyFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashSurveyStudyFormFields;

        

    }

     private static void loadFormFields() 

    {

//------------------------ Load biospecimen -----------------------------------------

        hashBiospecimenFormFields.put("strBiospecimenID", "Tissue Bank Number");

        hashBiospecimenFormFields.put("strBiospecSampleType", "Type");

        //hashBiospecimenFormFields.put("strBiospecLocation", "Location");

        hashBiospecimenFormFields.put("dtBiospecSampleDate", "Sample Date & Time");

        hashBiospecimenFormFields.put("strBiospecOtherID", "Barcode");

	hashBiospecimenFormFields.put("intBiospecParentID", "NA");

	hashBiospecimenFormFields.put("strBiospecParentID", "Hierarchy ID");

	hashBiospecimenFormFields.put("intInternalPatientID", "Patient ID");

	hashBiospecimenFormFields.put("intBiospecStudyID", "Study");

        hashBiospecimenFormFields.put("intInvCellID", "Location");

	//hashBiospecimenFormFields.put("intBiospecVolume", "Available quantity");
        
        hashBiospecimenFormFields.put("strBiospecGrade", "Grade");
        
        hashBiospecimenFormFields.put("strBiospecSpecies", "Species");
        
        hashBiospecimenFormFields.put("dtBiospecDateExtracted", "Extracted Date & Time");
        
        hashBiospecimenFormFields.put("dtBiospecDateDistributed", "Date distributed");

        hashBiospecimenFormFields.put("strComments", "Comments");
        
        hashBiospecimenFormFields.put("strEncounter", "Collection No");     
        
        //Anita Start
        hashBiospecimenFormFields.put("strStoredIn", "Stored in");
        hashBiospecimenFormFields.put("tmBiospecSampleTime", "Time of sample");
        hashBiospecimenFormFields.put("tmBiospecExtractedTime", "Time extracted");
        //Anita End

        hashBiospecimenFormFieldsDropDown.put("strBiospecSampleType", "BIOSPECCOLLECTIONTYPE");

        //hashBiospecimenFormFieldsDropDown.put("strBiospecLocation", "BIOSPECLOCATION");

        hashBiospecimenFormFieldsDropDown.put("strDay", "DAY");

        hashBiospecimenFormFieldsDropDown.put("strMonth", "MONTH");

        hashBiospecimenFormFieldsDropDown.put("strBiospecGrade", "BIOSPECGRADE");
        
        hashBiospecimenFormFieldsDropDown.put("strBiospecSpecies", "BIOSPECIES");

        //Anita Start
        hashBiospecimenFormFieldsDropDown.put("strHour", "HOUR");
        hashBiospecimenFormFieldsDropDown.put("strMinute", "MINUTE");
        hashBiospecimenFormFieldsDropDown.put("strTime", "TIME");
        hashBiospecimenFormFieldsDropDown.put("strStoredIn", "BIOSPECSTOREDIN");        
        //Anita End
        
// ------------ ADD THE SEARCH RESULTS FIELDS -------------------------------

        hashBiospecimenResultsFormFields.put("intBiospecimenID", "NA");

        hashBiospecimenResultsFormFields.put("strBiospecimenID", "Tissue Bank Number");

        hashBiospecimenResultsFormFields.put("strPatientID", "Patient ID");

        hashBiospecimenResultsFormFields.put("strBiospecSampleType", "Sample Type");

        hashBiospecimenResultsFormFields.put("strBiospecSampleSubType", "Sample Subtype");

        hashBiospecimenResultsFormFields.put("strBiospecParentID", "Hierarchy ID");

        hashBiospecimenResultsFormFields.put("dtBiospecSampleDate","Date of Sample");

        hashBiospecimenResultsFormFields.put("intBiospecNumCollected","Quantity");

        hashBiospecimenResultsFormFields.put("intBiospecNumRemoved","Volume Removed");
        
                //rennypv added to include inventory details
        
        hashBiospecimenResultsFormFields.put("intCellID","NA");
        
        hashBiospecimenResultsFormFields.put("intInternalPatientID","NA");

        //hashBiospecimenResultsFormFields.put("strDescription", "Description");
        
//--------------------SMARTFORM VIEW-----------------------rennypv---------------        
        
        hashStudySmartformFormFields.put("intSmartformKey", "NA");

        hashStudySmartformFormFields.put("intStudyID", "NA");
        
        hashStudySmartformFormFields.put("intStudySurveyID", "NA");
       

//------------ LOAD BIOSPECIMEN LOOKUP FIELDS ------------------

	hashLookupFields.put("intInternalPatientID", "");

	hashLookupFields.put("strBiospecSampleType", "");

	hashLookupFields.put("strBiospecSampleSubType", "");

	//Add the flag form fields



        
        hashFlagFormFields.put("strFlagDomain", "Flag Domain");
        
        hashFlagFormFields.put("intID", "ID");
        
        hashBiospecimenFlaggedFormFields.put("intBiospecimenID", "N/A");
        
        hashBiospecimenFlaggedFormFields.put("strBiospecimenID", "Tissue Bank Number");


 

// --------- LOAD THE BIOSPECIMEN SEARCH SCREEN -----------------------

        

        hashBiospecimenSearchFormFields.put("strBiospecimenID", "Tissue Bank Number");

        hashBiospecimenSearchFormFields.put("strPatientID", "Patient ID");

        hashBiospecimenSearchFormFields.put("intInternalPatientID", "NA");

        hashBiospecimenSearchFormFields.put("intBiospecStudyID", "NA");
        
        hashBiospecimenSearchFormFields.put("strBiospecOtherID", "Barcode");

        // Add the search operators

        hashBiospecimenSearchFormOperators.put("strBiospecimenID", DBSchema.LIKE_OPERATOR);

        hashBiospecimenSearchFormOperators.put("strPatientID", DBSchema.LIKE_OPERATOR);

        hashBiospecimenSearchFormOperators.put("intInternalPatientID", DBSchema.EQUALS_OPERATOR);

        hashBiospecimenSearchFormOperators.put("intBiospecStudyID", DBSchema.EQUALS_OPERATOR);
        
        hashBiospecimenSearchFormOperators.put("strBiospecOtherID", DBSchema.LIKE_OPERATOR);

        

// ---------------- Add the Fields that need to be validated -------------------------

        hashBiospecimenAddViewValidateFields.put("dtBiospecSampleDate", new Integer(ValidateFieldFunctions.DATE_NOT_IN_FUTURE));



	Hashtable hashTmp; 



	// -- Add Other Fields Hashtables: Fluid

	hashTmp = new Hashtable(20);

	hashTmp.put("strBiospecDescription", "Description");
        
        //hashTmp.put("intBiospecVolume", "Available quantity");

	//hashTmp.put("intBiospecVolRemoved", "Volume Removed");

	//hashTmp.put("strBiospecGestAt", "Gest. @ Sample");

	hashTmp.put("strBiospecSampleSubType", "Subtype");     

	hashBiospecimenOtherData.put("Fluid FIELDS", hashTmp);

	

	hashTmp = new Hashtable(20);

	hashTmp.put("strBiospecSampleSubType", "BIOSPECFLUIDSUBTYPES");

	hashBiospecimenOtherData.put("Fluid DROPDOWNS", hashTmp);
	





	// -- Add Other Fields Hashtables: Cords Blood

	hashTmp = new Hashtable(20);

	

	//hashTmp.put("strBiospecGestAt", "Gest. @ Delivery");

	hashTmp.put("strBiospecCordArtery", "Cord Artery (ua)");

	hashTmp.put("strBiospecCordVein", "Cord Vein (uv)");

	hashTmp.put("strBiospecArteralPH", "Arteral pH");

	hashTmp.put("strBiospecVenousPH", "Venous pH");

        

	hashBiospecimenOtherData.put("Cords Blood FIELDS", hashTmp);

	

	// -- Add Other Fields Hashtables: Amniotic Fluid

	hashTmp = new Hashtable(20);

	

	hashTmp.put("strBiospecIndicAmnio", "Indic. Amniocentsis");

        hashTmp.put("strBiospecKaryotype", "Karyotype");

	

	hashBiospecimenOtherData.put("Amniotic Fluid FIELDS", hashTmp);



	hashTmp = new Hashtable(5);

	

	hashTmp.put("strBiospecIndicAmnio", "BIOSPECAMNIO");

        hashTmp.put("strBiospecKaryotype", "BIOSPECKARYOTYPE");

	

	hashBiospecimenOtherData.put("Amniotic Fluid DROPDOWNS", hashTmp);



	// -- Add Other Fields Hashtables: Tissue

	hashTmp = new Hashtable(20);

	

	//hashTmp.put("strBiospecGestAt", "Gest. @ Sample");

	//hashTmp.put("strBiospecCode", "Code");

        hashTmp.put("strBiospecDescription", "Description");

	//hashTmp.put("strBiospecTreatment", "Treatment");

	//hashTmp.put("intBiospecVolume", "Available quantity");

	//hashTmp.put("intBiospecNumRemoved", "Number Removed");

	hashTmp.put("strBiospecSampleSubType", "Subtype");

        

	hashBiospecimenOtherData.put("Tissue FIELDS", hashTmp);

	

	hashTmp = new Hashtable(5);

	

	hashTmp.put("strBiospecSampleSubType", "BIOSPECTISSUESUBTYPES");

	//hashTmp.put("strBiospecTreatment", "BIOSPECTREATMENTS");

        

	hashBiospecimenOtherData.put("Tissue DROPDOWNS", hashTmp);


        // -- Add Other Fields Hashtables: Skeletal Muscle

	hashTmp = new Hashtable(20);

	hashTmp.put("strBiospecDescription", "Description");

	hashTmp.put("strBiospecSampleSubType", "Subtype");     

	hashBiospecimenOtherData.put("Skeletal Muscle FIELDS", hashTmp);

	

	hashTmp = new Hashtable(20);

	hashTmp.put("strBiospecSampleSubType", "Skeletal Muscle");

	hashBiospecimenOtherData.put("Skeletal Muscle DROPDOWNS", hashTmp);
        
        
        // -- Add Other Fields Hashtables: Cardiac Tissue

	hashTmp = new Hashtable(20);

	hashTmp.put("strBiospecDescription", "Description");

	hashTmp.put("strBiospecSampleSubType", "Subtype");     

	hashBiospecimenOtherData.put("Cardiac Tissue FIELDS", hashTmp);

	

	hashTmp = new Hashtable(20);

	hashTmp.put("strBiospecSampleSubType", "Cardiac Tissue");

	hashBiospecimenOtherData.put("Cardiac Tissue DROPDOWNS", hashTmp);
	

        // -- Add Other Fields Hashtables: Whole Blood

	hashTmp = new Hashtable(20);

	hashTmp.put("strBiospecDescription", "Description");

	hashTmp.put("strBiospecSampleSubType", "Subtype");     

	hashBiospecimenOtherData.put("Whole Blood FIELDS", hashTmp);
        
        
        
        // inventory part
        hashCellUpdateForm.put("strInvCellStatus", "Cell status");

        hashCellUpdateForm.put("intBiospecimenID", "Bio key");

        hashCellUpdateForm.put("intInternalPatientID", "Patient key");

        

        hashTrayUpdateForm.put("intInvTrayAvailable", "Available");

        

        hashBoxUpdateForm.put("intInvBoxAvailable", "Available");

        

        hashTankUpdateForm.put("intInvTankAvailable", "Available");

        

        hashTrayIDForm.put("intInvCellID", "Cell key");

        hashTrayIDForm.put("intInvTrayID", "Tray key");

        

        hashBoxIDForm.put("intInvTrayID", "Tray key");

        hashBoxIDForm.put("intInvBoxID", "Box key");

        

        hashTankIDForm.put("intInvBoxID", "Box key");

        hashTankIDForm.put("intInvTankID", "Box key");

        

        hashInventoryIDForm.put("intInvCellID", "Cell key");

        hashInventoryIDForm.put("intInvTrayID", "Tray key");

        hashInventoryIDForm.put("intInvBoxID", "Box key");

        hashInventoryIDForm.put("intInvTankID", "Tank key");

        // ------------ Survey study formfields -----------
        hashSurveyStudyFormFields.put("intSurveyID", "Survey key");
        hashSurveyStudyFormFields.put("strSurveyName", "Survey name");
        hashSurveyStudyFormFields.put("intStudyID", "Study key");
        //hashSurveyStudyFormFields.put("strStudyName", "Study name");
        
	blLoaded = true;

        

     }

    

}

