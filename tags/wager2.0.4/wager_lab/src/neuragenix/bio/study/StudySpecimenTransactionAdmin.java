/**
 * StudySpecimenTransactionAdmin.java
 * Study Specimen Transactions Administration
 * Copyright (C) Neuragenix Pty Ltd, 2004-2005
 *
 *
 * Created on 30 December 2004, 10:50
 *
 * @author Daniel Murley
 * email : dmurley@neuragenix.com
 * Purpose : Provides a user interface to the Transaction Governer to Administrate
 *           transactions
 *
 *
 * <B>Dependant Properties : </B>
 *         neuragenix.bio.Study.useTransactionGoverner
 *             - No property will result in a default of false
 *             - true/false based on if the Transaction Governer is used in this build
 *         neuragenix.bio.Study.DefaultTransactionGovernerNoPermission
 *             - No property will result in a default of true
 *             - true will prevent any biospecimen transaction for which permissions have
 *               not been set on a particular study to be disabled.
 */
package neuragenix.bio.study;


import org.jasig.portal.ChannelRuntimeData;
import neuragenix.security.AuthToken;
import java.lang.StringBuffer;
import java.util.Hashtable;
import java.util.Enumeration;
import neuragenix.dao.*;
import java.sql.ResultSet;


public class StudySpecimenTransactionAdmin {
    
    
    private static final String XSL_CHANNEL_PERM_IN = "STUDY_PERMISSION_IN_";
    private static final String XSL_CHANNEL_PERM_OUT = "STUDY_PERMISSION_OUT_";
    private static final String XSL_CHANNEL_PERM_SAVED = "STUDY_PERMISSION_SAVED_";
    private static final String XSL_CHANNEL_PERM_ID = "STUDY_PERMISSION_ID_";
    
    StudySpecimenTransactionGoverner stgGoverner;
    AuthToken authToken;
    
    int currentStudyKey;
    
    /** Creates a new instance of StudySpecimenTransactionAdmin */
    public StudySpecimenTransactionAdmin(AuthToken authToken) 
    {
       stgGoverner = new StudySpecimenTransactionGoverner(authToken);
       
    }
    
   
    
    
    public int getCurrentStudyKey()
    {
        return currentStudyKey;
    }
    
    public void setCurrentStudyKey(int currentStudyKey)
    {
        this.currentStudyKey = currentStudyKey;
    }
    
    public String getStudyName(int intStudyKey)
    {
        try
        {
            DALSecurityQuery query = new DALSecurityQuery();
            ResultSet rs;
            
            query.setDomain("STUDY", null, null, null);
            query.setField("STUDY_strStudyName", null);
            query.setWhere(null, 0, "STUDY_intStudyID", "=", intStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            
            rs = query.executeSelect();
            
            if (rs.first())
            {
                return rs.getString("STUDY_strStudyName");
            }
            else
            {
                return null;
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public int updateMatrix(ChannelRuntimeData rd)
    {
       
        Enumeration enumRuntimeData = rd.getParameterNames();
        
        String parameterName;
        String action;
        String externalStudyKey;
        while (enumRuntimeData.hasMoreElements())
        {
            parameterName = (String) enumRuntimeData.nextElement();
            
            if (parameterName.startsWith("STUDY_PERMISSION_ID_"))
            {
                
                externalStudyKey = new String(parameterName);
                externalStudyKey = externalStudyKey.substring(XSL_CHANNEL_PERM_ID.length());
                
                if (rd.getParameter(XSL_CHANNEL_PERM_IN + externalStudyKey) != null)
                {
                   stgGoverner.setPermission(getCurrentStudyKey(), Integer.parseInt(externalStudyKey), stgGoverner.IN_PERMISSION, true);  
                }
                else
                {
                   if (Integer.parseInt(rd.getParameter(XSL_CHANNEL_PERM_SAVED + externalStudyKey)) ==  -1)
                   {
                      stgGoverner.setPermission(getCurrentStudyKey(), Integer.parseInt(externalStudyKey), stgGoverner.IN_PERMISSION, false);
                   }
                }
            
                if (rd.getParameter(XSL_CHANNEL_PERM_OUT + externalStudyKey) != null)
                {
                    stgGoverner.setPermission(getCurrentStudyKey(), Integer.parseInt(externalStudyKey), stgGoverner.OUT_PERMISSION, true);     
                }
                else
                {
                    if (Integer.parseInt(rd.getParameter(XSL_CHANNEL_PERM_SAVED + externalStudyKey)) == -1)
                       stgGoverner.setPermission(getCurrentStudyKey(), Integer.parseInt(externalStudyKey), stgGoverner.OUT_PERMISSION, false);
                }
            }
       }
       return 0;   
    }
    
    
    
    /** 
     *  Returns XML of the current state of the permission matrix 
     *
     *
     *  Important Tags : 
     *    <studyItem>
     *     <notfound> true </notfound> 
     *
     *  Where <notfound> referes to the lack of a saved permission setting within the
     *  database for this form of transaction.  
     *
     *
     */
    public String translateToXML()
    {
        StringBuffer strXML = new StringBuffer();
        // get the permissions matrix
        int matrix[][] = stgGoverner.getAllPermissions(getCurrentStudyKey());
        
        // query the database for all of the names of the studies
        
        
        for (int i = 0; i < matrix.length; i++)
        {
            if (matrix[i][StudySpecimenTransactionGoverner.PERMISSION_MATRIX_EXTERNALSTUDYKEY] != 0)
            {
                strXML.append("<StudyPermissions>");
                strXML.append("<ExternalStudyKey>" + matrix[i][StudySpecimenTransactionGoverner.PERMISSION_MATRIX_EXTERNALSTUDYKEY] + "</ExternalStudyKey>");
                strXML.append("<ExternalStudyName>" + getStudyName(matrix[i][StudySpecimenTransactionGoverner.PERMISSION_MATRIX_EXTERNALSTUDYKEY]) + "</ExternalStudyName>");
                strXML.append("<InSelected>" + matrix[i][StudySpecimenTransactionGoverner.PERMISSION_MATRIX_IN] + "</InSelected>");
                strXML.append("<OutSelected>" + matrix[i][StudySpecimenTransactionGoverner.PERMISSION_MATRIX_OUT] + "</OutSelected>");
                strXML.append("<NotFound>" + matrix[i][StudySpecimenTransactionGoverner.PERMISSION_MATRIX_INCOMPLETE] + "</NotFound>");
                strXML.append("</StudyPermissions>");
            }
        }
       
        // output data matched against study name / study key
        
        // all data that doesnt have a match in the permissions matrix gets flagged as not found
        
        return strXML.toString();    
    }
    
}
