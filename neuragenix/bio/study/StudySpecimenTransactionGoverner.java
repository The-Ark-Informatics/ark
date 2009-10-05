/*
 * StudySpecimenTransactionGoverner.java
 *
 * Created on 23 December 2004, 17:29
 *
 * Purpose : Class to manage the the matrix
 *
 */

package neuragenix.bio.study;


import neuragenix.dao.*;
import neuragenix.security.AuthToken;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.ResultSet;
import java.io.InputStream;
import org.jasig.portal.PropertiesManager;

public class StudySpecimenTransactionGoverner {
    
    public static final String ADMIN_UPDATE_ACTIVITY = "specimen_governer_admin_update";
    public static final String ADMIN_VIEW_ACTIVITY = "specimen_governer_admin_view";
    public static final String ALLOCATOR_ACTIVITY = "specimen_governer_allocator";
    
    public static final String PROPERTY_USEGOVERNER = "neuragenix.bio.study.useGoverner";
    public static final String PROPERTY_DEFPERMISSION = "neuragenix.bio.study.DefaultNoGovernerPermissions";
    public static final String PROPERTY_DEFSTUDYNAME = "neuragenix.bio.study.DefaultStudyName";
    
    private static final String DATABASE_SCHEMA_FILE = "StudyTransactionsDBSchema.xml";
    
    private static final String FORMFIELDS_PERMISSION_CHECK = "cstudy_governer_permission_check";
    private static final String FORMFIELDS_PERMISSION_SET = "cstudy_governer_permission_set_both";
    private static final String FORMFIELDS_PERMISSION_SET_IN = "cstudy_governer_permission_set_in";
    private static final String FORMFIELDS_PERMISSION_SET_OUT = "cstudy_governer_permission_set_out";
    private static final String FORMFIELDS_PERMISSION_VIEW = "cstudy_governer_permission_view";
    
    
    public static final int IN_PERMISSION = 1001;
    public static final int OUT_PERMISSION = 1002;
    public static final int IN_AND_OUT_PERMISSION = 1010;
    
    public static final int NO_PLATFORM_PERMISSION = 5000;
    public static final int TRUE = 0;
    public static final int FALSE = -1;

    
    public static final int PERMISSION_MATRIX_EXTERNALSTUDYKEY = 0;
    public static final int PERMISSION_MATRIX_IN = 1;
    public static final int PERMISSION_MATRIX_OUT = 2;
    public static final int PERMISSION_MATRIX_INCOMPLETE = 3;
    // public static final int PERMISSION_MATRIX_INTERNALKEY = 3;
    
    private static final int PERMISSION_MATRIX_COLUMNS = 4;
    
    
    
    private AuthToken authToken;
    
  /*
    public StudySpecimenTransactionGoverner() 
    {
        loadDomains();
        System.out.println ("No auth token passed");
    } */
    
    /** Creates a new instance of StudyAllocationMatrix */  
    public StudySpecimenTransactionGoverner(AuthToken authToken)
    {
       this.authToken = authToken;
       loadDomains();
       
    }
    
    private void loadDomains()
    {
         InputStream file = StudySpecimenTransactionGoverner.class.getResourceAsStream(DATABASE_SCHEMA_FILE);
         DatabaseSchema.loadDomains(file, DATABASE_SCHEMA_FILE); 
    }
    
    
    
    public int setPermission(int intSourceStudyKey, int intDestinationStudyKey, int permissionType, boolean value)
    {
        // check user actually has permission to be doing this
        try
        {
            if (authToken == null)
                return NO_PLATFORM_PERMISSION;
            else
                if (authToken.hasActivity(ADMIN_UPDATE_ACTIVITY) == false)
                {
                    System.err.println ("[TransactionGoverner] - User does not have update permission");
                    return NO_PLATFORM_PERMISSION;
                }

        }
        catch (neuragenix.security.exception.SecurityException se)
        {
            System.err.println ("[Transaction Governer] - Security Exception");
            se.printStackTrace();
            return NO_PLATFORM_PERMISSION;
        }

        Vector vtFormFields;
        Hashtable htPermissionData;
        try
        {
            DALQuery query = new DALQuery();
            ResultSet rs;
            vtFormFields = DatabaseSchema.getFormFields(FORMFIELDS_PERMISSION_CHECK);

            query.setDomain("STUDY_TRANSACTION_PERMISSIONS", "","","");
            query.setFields(vtFormFields, null);    
            query.setWhere(null, 0, "STUDY_TRANSACTION_PERMISSIONS_intPrimaryStudyKey", "=", intSourceStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "STUDY_TRANSACTION_PERMISSIONS_intExternalStudyKey", "=", intDestinationStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);

            rs = query.executeSelect();

            htPermissionData = new Hashtable();
            Integer outputValue;

            if (value == true)
                outputValue = new Integer(0);
            else
                outputValue = new Integer(-1);

            switch (permissionType)
            {
                case IN_PERMISSION:
                    htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy", outputValue + "");
                    vtFormFields = DatabaseSchema.getFormFields(FORMFIELDS_PERMISSION_SET_IN);
                    break;
                case OUT_PERMISSION:
                    htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy", outputValue + "");
                    vtFormFields = DatabaseSchema.getFormFields(FORMFIELDS_PERMISSION_SET_OUT);
                    break;
                case IN_AND_OUT_PERMISSION:
                    htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy", outputValue + "");
                    htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy", outputValue + "");
                    vtFormFields = DatabaseSchema.getFormFields(FORMFIELDS_PERMISSION_SET);
                    break;

                default:
                    return FALSE;

            }                

            boolean queryResult = false;

            query.clearFields();



            if (rs.first() == false) // there is no existing permission
            {
                query.clearWhere();
                htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intPrimaryStudyKey", intSourceStudyKey + "");
                htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intExternalStudyKey", intDestinationStudyKey + "");

                query.setFields(vtFormFields, htPermissionData);    

                queryResult = query.executeInsert();
            }
            else
            {
                htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intPrimaryStudyKey", intSourceStudyKey + "");
                htPermissionData.put("STUDY_TRANSACTION_PERMISSIONS_intExternalStudyKey", intDestinationStudyKey + "");

                query.setFields(vtFormFields, htPermissionData);
                queryResult = query.executeUpdate();
            }


            // rs.close();

            if (queryResult == true) return TRUE;
            else
                return FALSE;

        }
        catch (Exception e)
        {
            System.err.println("[TransactionGoverner] - Unknown Exception");
            e.printStackTrace(System.err);
            return FALSE;
        }

      
    }
    
    public int[][] getAllPermissions(int intStudyKey)
    {
        // open a new DAL query object
        try
        {
            DALQuery query = new DALQuery();
            Vector vtFormFields = DatabaseSchema.getFormFields(FORMFIELDS_PERMISSION_VIEW);
            ResultSet rs;
            int totalPermissions;
            int [][] returnArray;

            
            // get the total number of studies in the system
            
            query.setDomain ("STUDY", null, null, null);
            query.setCountField("STUDY_intStudyID", false);
            query.setWhere (null, 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            
            rs = query.executeSelect();
            
            if (rs.first() == true)
            {
                totalPermissions = rs.getInt(1);
            }
            else
            {
                totalPermissions = 0;
            }
            
            
            rs.close();
            
            query = new DALQuery();
            
            
            // get all the completed permissions
            query.setDomain("STUDY_TRANSACTION_PERMISSIONS", null, null, null);
            query.setFields(vtFormFields, null);
            query.setWhere(null, 0, "STUDY_TRANSACTION_PERMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "STUDY_TRANSACTION_PERMISSIONS_intPrimaryStudyKey", "=", intStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);

            rs = query.executeSelect();

            returnArray = new int[totalPermissions][PERMISSION_MATRIX_COLUMNS];
            
            int firstDataSetSize = 0;
            if (rs.first() == true)
            {
                rs.beforeFirst();
                for (firstDataSetSize = 0; rs.next(); firstDataSetSize++)
                {
                   if (rs.getString("STUDY_TRANSACTION_PERMISSIONS_intExternalStudyKey") != null)
                       returnArray[firstDataSetSize][PERMISSION_MATRIX_EXTERNALSTUDYKEY] = rs.getInt("STUDY_TRANSACTION_PERMISSIONS_intExternalStudyKey");
                    
                    if (rs.getString("STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy") != null)
                    {   
                        returnArray[firstDataSetSize][PERMISSION_MATRIX_IN] = rs.getInt("STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy");
                    }
                    if (rs.getString("STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy") != null)
                    returnArray[firstDataSetSize][PERMISSION_MATRIX_OUT] = rs.getInt("STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy");

                    returnArray[firstDataSetSize][PERMISSION_MATRIX_INCOMPLETE] = -1;
                }
            }

            DALQuery notInQuery = new DALQuery();

            notInQuery.setDomain("STUDY_TRANSACTION_PERMISSIONS", null, null, null);
            notInQuery.setField("STUDY_TRANSACTION_PERMISSIONS_intExternalStudyKey", null);
            notInQuery.setWhere(null, 0, "STUDY_TRANSACTION_PERMISSIONS_intPrimaryStudyKey", "=", intStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);

            query = new DALQuery();
            query.setDomain("STUDY", null, null, null);

            query.setField("STUDY_intStudyID", null); 

            query.setWhere(null, 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "STUDY_intStudyID", "NOT IN", notInQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);
            query.setWhere("AND", 0, "STUDY_intStudyID", "<>", intStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);

            rs = query.executeSelect();
            if (rs.first() == true)
            {
                rs.beforeFirst();
                for (int i = firstDataSetSize; rs.next(); i++)
                {
                    returnArray[i][PERMISSION_MATRIX_EXTERNALSTUDYKEY] = rs.getInt(1);
                    returnArray[i][PERMISSION_MATRIX_IN] = -1;
                    returnArray[i][PERMISSION_MATRIX_OUT] = -1;
                    returnArray[i][PERMISSION_MATRIX_INCOMPLETE] = 0;
                }
            }

            return returnArray;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public boolean checkPermission(int intSourceStudyKey, int intDestinationStudyKey, int permissionType, boolean forAllocation)
    {
        Vector vtFormFields;
        
        try 
        {
            String governerUsage = PropertiesManager.getProperty(PROPERTY_USEGOVERNER);
            if (governerUsage.equalsIgnoreCase("false"))
            {
                return true;
            }
        }
        catch (Exception e)
        {
            return true;
        }
        
        if (forAllocation == true)
        {    
            try
            {
                if (authToken.hasActivity(ALLOCATOR_ACTIVITY))
                    return checkPermission(intSourceStudyKey, intDestinationStudyKey, permissionType, false);
                else
                {
                    System.err.println ("[StudySpecimenTransactionGoverner] A user who is not an allocator has attempted a transaction");
                    return false;
                }
            }
            catch (neuragenix.security.exception.SecurityException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery();
            ResultSet rs;
            vtFormFields = DatabaseSchema.getFormFields(FORMFIELDS_PERMISSION_CHECK);

            query.setDomain("STUDY_TRANSACTION_PERMISSIONS", "","","");
            query.setFields(vtFormFields, null);    
            query.setWhere(null, 0, "STUDY_TRANSACTION_PERMISSIONS_intPrimaryStudyKey", "=", intSourceStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "STUDY_TRANSACTION_PERMISSIONS_intExternalStudyKey", "=", intDestinationStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            
            
            switch (permissionType)
            {
                case IN_PERMISSION:
                    query.setWhere("AND", 0, "STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    break;
                case OUT_PERMISSION:
                    query.setWhere("AND", 0, "STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    break;
                case IN_AND_OUT_PERMISSION:
                    query.setWhere("AND", 0, "STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    break;
                
                default:
                    return false;
            }
            
            rs = query.executeSelect();
            
            if (rs == null)
                return false;
            
            if (rs.first() == true)
            {
                switch (permissionType)
                {
                    case IN_PERMISSION:
                        if (rs.getInt("STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy") == 0)
                           return true;
                        else
                           return false;
                        
                    case OUT_PERMISSION:
                        if (rs.getInt("STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy") == 0)
                           return true;
                        else
                           return false;

                    case IN_AND_OUT_PERMISSION:
                        if ((rs.getInt("STUDY_TRANSACTION_PERMISSIONS_intAllocateToStudy") == 0) && (rs.getInt("STUDY_TRANSACTION_PERMISSIONS_intAllocateFromStudy") == 0))
                           return true;
                        else
                           return false;

                    default:
                        return false;

                }
            }
            else
                return false;
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        
    }
    
    
    
    
    
    
}
