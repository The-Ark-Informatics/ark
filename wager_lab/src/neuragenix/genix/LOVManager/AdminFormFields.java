/*  

 * Copyright (c) 2002 Neuragenix, Inc. All Rights Reserved.

 * AdminFormField.java

 * Created on 25 November 2002, 00:00

 * @author  Shendon Ewans

 * 

 * Description: 

 * Class for populating fields in the admin channel.

 *

 */

package neuragenix.genix.LOVManager;

import java.util.Hashtable;

import neuragenix.dao.DBSchema;

//import neuragenix.common.ValidateFieldFunctions;



/**

 *

 * @author  Shendon Ewans

 */

public class AdminFormFields { 

    

    private static boolean blLoaded = false;

    private static Hashtable hashChangePasswordFormFields = new Hashtable ( 20 );

    private static Hashtable hashUserChangePasswordFormFields = new Hashtable ( 20 );

    private static Hashtable hashUpdatePasswordFields = new Hashtable ( 2 );

    private static Hashtable hashValidatePasswordFormFields = new Hashtable ( 4 );

    private static Hashtable hashUserEditFields = new Hashtable();

    private static Hashtable hashAddActivityFormFields = new Hashtable();

    private static Hashtable hashUserProfileFormFields = new Hashtable();

    private static Hashtable hashUserProfileAddFormFields = new Hashtable();

    private static Hashtable hashProfileActivityFormFields = new Hashtable();

    private static Hashtable hashProfileActivityLOVFormFields = new Hashtable();

    private static Hashtable hashProfileSearchFormOperators = new Hashtable();

    private static Hashtable hashProfileSearchFormFields = new Hashtable();

    private static Hashtable hashProfileLOVFormFields = new Hashtable();

    private static Hashtable hashProfileFormFields = new Hashtable();

    private static Hashtable hashProfileAddFormFields = new Hashtable();

    

    private static Hashtable hashProcessListFormOperators = new Hashtable();

    private static Hashtable hashProcessServiceFormFields = new Hashtable();





    public static final String PERSON_DIR = "PERSON_DIR";

    public static final String PROFILE_DOMAIN = "PROFILE";

    public static final String ACTIVITY_DOMAIN = "ACTIVITY";

    public static final String PROFILE_AUTHORIZATION_DOMAIN = "PROFILEAUTH";

    public static final String INTERNAL_PROFILE_ID = "intProfileID";

    public static final String INTERNAL_ACTIVITY_ID = "intActivityID";

    public static final String UPDATE = "UPDATE";

    public static final String PROCESS = "PROCESS";

    

    public static final String INTERNAL_USER_ID = "strUsername";

    //public static final String EXTERNAL_USER_ID = "strUsername";

     

    /** Creates a new instance of PatientFields */

     private AdminFormFields() {

    }

         public static Hashtable getProcessListFormOperators()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProcessListFormOperators;

        

    }  

    public static Hashtable getProcessServiceFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProcessServiceFormFields;

        

    }

     public static Hashtable getUserChangePasswordFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashUserChangePasswordFormFields;

        

    }

     public static Hashtable getChangePasswordFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashChangePasswordFormFields;

        

    }

     public static Hashtable getAddActivityFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashAddActivityFormFields;

        

    }

     public static Hashtable getUserProfileFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashUserProfileFormFields;

        

    }

     public static Hashtable getUserProfileAddFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashUserProfileAddFormFields;

        

    }

     public static Hashtable getProfileActivityFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProfileActivityFormFields;

        

    }

     public static Hashtable getProfileActivityLOVFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProfileActivityLOVFormFields;

        

    }

     public static Hashtable getProfileSearchFormOperators()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProfileSearchFormOperators;

        

    }

     public static Hashtable getProfileSearchFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProfileSearchFormFields;

        

    }

     public static Hashtable getProfileLOVFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProfileLOVFormFields;

        

    }

     public static Hashtable getProfileFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProfileFormFields;

        

    }

     public static Hashtable getProfileAddFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashProfileAddFormFields; 

        

    }

     public static Hashtable getUserEditFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashUserEditFields;

        

    }

     public static Hashtable getUpdatePasswordFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashUpdatePasswordFields;

        

    }

     private static void loadFormFields() 

    {   

        

	// -- User edit / add

	hashUserEditFields.put("strUsername","User Name");

	hashUserEditFields.put("strPersonFirstName","First Name");

	hashUserEditFields.put("strPersonLastName","Last Name");

	hashUserEditFields.put("strPersonEmail","Email Address");



        hashProcessServiceFormFields.put("intProcessKey", "Process Key");        

        hashProcessServiceFormFields.put("strProcessType", "Type");

        hashProcessServiceFormFields.put("strProcessStatus", "Status");

        hashProcessServiceFormFields.put("strProcessDescription", "Description");

        

	hashAddActivityFormFields.put("intActivityID","NA");

	hashAddActivityFormFields.put("intProfileID","NA");

	

	hashProfileActivityFormFields.put("intActivityID","NA");

	hashProfileActivityFormFields.put("strActivityDesc","Activity");



	hashProfileActivityLOVFormFields.put("intActivityID", ACTIVITY_DOMAIN);

	hashProfileActivityLOVFormFields.put("strActivityDesc", ACTIVITY_DOMAIN);



	hashProfileSearchFormOperators.put("strProfileName",DBSchema.EQUALS_OPERATOR);



	hashUserProfileFormFields.put("intUserProfileID","NA");

	hashUserProfileFormFields.put("intProfileID","NA");

	hashUserProfileFormFields.put("strProfileName","Profile Name");

	hashUserProfileFormFields.put("strProfileDesc","Profile Description");

	

	hashUserProfileAddFormFields.put("intProfileID","NA");

	hashUserProfileAddFormFields.put("strUsername","NA");



	hashProfileSearchFormFields.put("strProfileName","Profile Name");

	hashProfileSearchFormFields.put("strProfileDesc","Profile Description");



	hashProfileLOVFormFields.put("intProfileID","PROFILE");

	hashProfileLOVFormFields.put("strProfileName","PROFILE");

	hashProfileLOVFormFields.put("strProfileDesc","PROFILE");



	hashProfileFormFields.put("intProfileID","NA");

	hashProfileFormFields.put("strProfileName","Profile Name");

	hashProfileFormFields.put("strProfileDesc","Profile Description");





	hashProfileAddFormFields.put("strProfileName","Profile Name");

	hashProfileAddFormFields.put("strProfileDesc","Profile Description");

	 

        // Screen field *******

        //---- Load add fields for Change Password --------------------



        hashChangePasswordFormFields.put("strUsername", "Username");

//        hashChangePasswordFormFields.put("strOldPassword", "Old Password");

        hashChangePasswordFormFields.put("strNewPassword1", "New Password");

        hashChangePasswordFormFields.put("strNewPassword2", "Confirm New Password");

        

        hashUserChangePasswordFormFields.put("strOldPassword", "Old Password");

        hashUserChangePasswordFormFields.put("strNewPassword1", "New Password");

        hashUserChangePasswordFormFields.put("strNewPassword2", "Confirm New Password");

        

        

        // Database fields ************

        //---- Load update db fields for Change Password --------------------



        hashUpdatePasswordFields.put("strUsername", "Username");

        hashUpdatePasswordFields.put("strPassword", "New Password");

                      

        hashProcessListFormOperators.put("strProcessKey", DBSchema.EQUALS_OPERATOR);

     

        blLoaded = true;

        

     }

     

    

}

