/*
 * Doctor.java
 *
 * Created on September 21, 2005, 4:14 PM
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
import java.lang.reflect.Field;

import neuragenix.dao.DatabaseSchema;
import neuragenix.common.Utilities;

import org.jasig.portal.ChannelRuntimeData;


public class Doctor 
{
    String strID = null;
    String strFirstName = null;
    String strSurname = null;
    String strDepartment = null;
    String strHospital = null;
    String strAddress = null;
    String strPhone = null;
    String strFax = null;
    String strEmail = null;
    String strTitle = "Dr";
    Vector vtFields = null;
    
    /** Creates a new instance of Doctor */
    public Doctor() 
    {
        vtFields = DatabaseSchema.getFormFields("cbiospecimen_view_doctor_details");
    }
    public Doctor(String str_ID)
    {
        this();
        this.strID = str_ID;
    }
    public String getID()
    {
        return this.strID;
    }
    public void setID(String str_ID)
    {
        this.strID = str_ID;
    }
    public String getTitle()
    {
    	return this.strTitle;
    }
    public void setTitle(String str_title)
    {
    	this.strTitle = str_title;
    }
    public String getFirstName()
    {
        return this.strFirstName;
    }
    public void setFirstName(String str_FirstName)
    {
        this.strFirstName = str_FirstName;
    }
    public String getSurname()
    {
        return this.strSurname;
    }
    public void setSurname(String str_Surname)
    {
        this.strSurname = str_Surname;
    }
    public String getFullName()
    {
    	StringBuffer strFN = new StringBuffer();
    	if(this.strTitle != null)
    	{
    		strFN.append(this.strTitle + " ");
    	}
    	if(this.strFirstName != null)
    	{
    		strFN.append(this.strFirstName + " ");
    	}
    	if(this.strSurname != null)
    	{
    		strFN.append(this.strSurname);
    	}
    		return strFN.toString();
    }
    public String getDepartment()
    {
        return this.strDepartment;
    }
    public void setDepartment(String str_Department)
    {
        this.strDepartment = str_Department;
    }
    public String getHospital()
    {
        return this.strHospital;
    }
    public void setHospital(String str_Hospital)
    {
        this.strHospital = str_Hospital;
    }
    public String getAddress()
    {
        return this.strAddress;
    }
    public void setAddress(String str_Address)
    {
        this.strAddress = str_Address;
    }
    public String getPhone()
    {
        return this.strPhone;
    }
    public void setPhone(String str_Phone)
    {
        this.strPhone = str_Phone;
    }
    public String getFax()
    {
        return this.strFax;
    }
    public void setFax(String str_Fax)
    {
        this.strFax = str_Fax;
    }
    public String getEmail()
    {
        return this.strEmail;
    }
    public void setEmail(String str_Email)
    {
        this.strEmail = str_Email;
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
       }
       return rd;
    }
    
    public String toXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
        if(this.strID != null && this.strID.length() > 0)
        {
            strbfrXML.append("<Doctor>");
            strbfrXML.append("<ID>"+ this.strID+"</ID>");
            strbfrXML.append("<Title>"+ this.strTitle+"</Title>");
            if(this.strFirstName != null && this.strFirstName.length() > 0)
            {
                strbfrXML.append("<FirstName>"+ Utilities.cleanForXSL(this.strFirstName)+"</FirstName>");
            }
            if(this.strSurname != null && this.strSurname.length() > 0)
            {
                strbfrXML.append("<Surname>"+ Utilities.cleanForXSL(this.strSurname)+"</Surname>");
            }
            if(this.strDepartment != null && this.strDepartment.length() > 0)
            {
                strbfrXML.append("<Department>"+ Utilities.cleanForXSL(this.strDepartment)+"</Department>");
            }
            if(this.strHospital != null && this.strHospital.length() > 0)
            {
                strbfrXML.append("<Hospital>"+ Utilities.cleanForXSL(this.strHospital)+"</Hospital>");
            }
            if(this.strAddress != null && this.strAddress.length() > 0)
            {
                strbfrXML.append("<Address>"+ Utilities.cleanForXSL(this.strAddress)+"</Address>");
            }
            if(this.strPhone != null && this.strPhone.length() > 0)
            {
                strbfrXML.append("<Phone>"+ Utilities.cleanForXSL(this.strPhone)+"</Phone>");
            }
            if(this.strFax != null && this.strFax.length() > 0)
            {
                strbfrXML.append("<Fax>"+ Utilities.cleanForXSL(this.strFax)+"</Fax>");
            }
            if(this.strEmail != null && this.strEmail.length() > 0)
            {
                strbfrXML.append("<Email>"+ Utilities.cleanForXSL(this.strEmail)+"</Email>");
            }
            strbfrXML.append("</Doctor>");
        }
        return strbfrXML.toString();
    }
}
