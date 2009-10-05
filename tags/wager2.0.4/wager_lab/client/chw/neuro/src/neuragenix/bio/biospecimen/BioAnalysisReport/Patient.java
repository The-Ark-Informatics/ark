/*
 * Patient.java
 *
 * Created on August 25, 2005, 9:38 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.bio.biospecimen.BioAnalysisReport;

/**
 *
 * @author renny
 */
import java.util.Vector;
import java.util.Date;
import java.lang.reflect.Field;

import org.jasig.portal.ChannelRuntimeData;

import neuragenix.dao.DatabaseSchema;
import neuragenix.common.Utilities;
public class Patient 
{
    protected String intInternalPatientID = null;
    protected String strPatientID = null;
    protected String strFirstName = null;
    protected String strSurname = null;
    protected Date dtDOB = null;
    protected String strSex = null;
    protected Vector vtFields = null;
    /** Creates a new instance of Patient */
    public Patient() 
    {
        vtFields =  DatabaseSchema.getFormFields("cpatient_view_patient_details");
    }
    public Patient(String str_Internalid)
    {
        this();
        this.intInternalPatientID = str_Internalid;
    }
    public String getInternalID()
    {
        return this.intInternalPatientID;
    }
    public void setInternalID(String int_InternalID)
    {
        this.intInternalPatientID = int_InternalID;
    }
    public String getID()
    {
        return this.strPatientID;
    }
    public void setID(String str_ID)
    {
        this.strPatientID = str_ID;
    }
    public String getFirstName()
    {
        return this.strFirstName;
    }
    public void setFirstName(String str_FirstName)
    {
        this.strFirstName= str_FirstName;
    }
    public String getSurname()
    {
        return this.strSurname;
    }
    public void setSurname(String str_Surname)
    {
        this.strSurname = str_Surname;
    }
    public Date getDOB()
    {
        return this.dtDOB;
    }
    public void setDOB(Date dt_DOB)
    {
        this.dtDOB = dt_DOB;
    }
    public String getSex()
    {
        return this.strSex;
    }
    public void setSex(String str_Sex)
    {
        this.strSex = str_Sex;
    }
    public Vector getFieldNames()
    {
        return this.vtFields;
    }
    public void setFieldNames(Vector vt_Fields)
    {
        this.vtFields = vt_Fields;
    }
    public String getDBSchemaName(String str_param)
    {
        String strName = null;
        for(java.util.Enumeration eparam = this.vtFields.elements();eparam.hasMoreElements();)
        {
            strName = (String)eparam.nextElement();
            String[] strreqName = strName.split("_");
            if(str_param.equalsIgnoreCase(strreqName[1]))
            {
                return strName;
            }
        }
        return strName;
    }
    public String toXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
        if(this.strPatientID != null && this.strPatientID.length() > 0)
        {
            strbfrXML.append("<Patient>");
            strbfrXML.append("<ID>"+ this.strPatientID+"</ID>");
            strbfrXML.append("<InternalID>" + this.intInternalPatientID + "</InternalID>");
            if(this.dtDOB != null)
            {
                strbfrXML.append("<DOB>"+ this.dtDOB.toString() +"</DOB>");
            }
            if(this.strFirstName != null && this.strFirstName.length() > 0)
            {
                strbfrXML.append("<FirstName>"+ this.strFirstName+"</FirstName>");
            }
            if(this.strSex != null && this.strSex.length() > 0)
            {
                strbfrXML.append("<Sex>"+ this.strSex+"</Sex>");
            }
            if(this.strSurname != null && this.strSurname.length() > 0)
            {
                strbfrXML.append("<Surname>"+ this.strSurname +"</Surname>");
            }
            strbfrXML.append("</Patient>");
        }
        return strbfrXML.toString();
    }
    public ChannelRuntimeData toChannelRuntimeData() throws IllegalAccessException
    {
       ChannelRuntimeData rd = new ChannelRuntimeData();
       Field[] fldNames =  this.getClass().getDeclaredFields();
       for(int i=0;i<fldNames.length;i++)
       {
            Field fldName = fldNames[i];
            String strName = fldName.getName();
             Object o = fldName.get(this);
             if(o != null && o instanceof String)
             {
                 rd.setParameter(this.getDBSchemaName(strName), (String)o);
             }
             if(o != null && o instanceof Date)
             {
                rd.setParameter(this.getDBSchemaName(strName), Utilities.convertDate((Date)o,"dd/MM/yyyy"));
             }
       }
       return rd;
    }
}
