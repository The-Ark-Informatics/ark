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

package neuragenix.genix.matrix;

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

    private static Hashtable hashUpdatePasswordFields = new Hashtable ( 2 );

    private static Hashtable hashValidatePasswordFormFields = new Hashtable ( 4 );

    

    public static final String PERSON_DIR = "PERSON_DIR";

    public static final String UPDATE = "UPDATE";



    public static final String INTERNAL_USER_ID = "strUsername";

    //public static final String EXTERNAL_USER_ID = "strUsername";

     

    /** Creates a new instance of PatientFields */

     public AdminFormFields() {

    }

     public static Hashtable getChangePasswordFormFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashChangePasswordFormFields;

        

    }

     public static Hashtable getUpdatePasswordFields()

    {

            // If the class is not loaded, load it to add all the hashtable data.

            if(!blLoaded){loadFormFields();}

            return hashUpdatePasswordFields;

        

    }

     private static void loadFormFields() 

    {   

        

        

        // Screen field *******

        //---- Load add fields for Change Password --------------------



        hashChangePasswordFormFields.put("strUsername", "Username");

        hashChangePasswordFormFields.put("strOldPassword", "Old Password");

        hashChangePasswordFormFields.put("strNewPassword1", "New Password");

        hashChangePasswordFormFields.put("strNewPassword2", "Confirm New Password");

        

        

        

        // Database fields ************

        //---- Load update db fields for Change Password --------------------



        hashUpdatePasswordFields.put("strUsername", "Username");

        hashUpdatePasswordFields.put("strPassword", "New Password");

      

        

      

        

        blLoaded = true;

        

     }

     

    

}

