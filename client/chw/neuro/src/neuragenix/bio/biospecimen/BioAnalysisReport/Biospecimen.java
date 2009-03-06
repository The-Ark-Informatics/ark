/*
 * Biospecimen.java
 *
 * Created on August 25, 2005, 9:39 PM
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
import java.util.Hashtable;
import java.util.Enumeration;

import org.jasig.portal.ChannelRuntimeData;

import neuragenix.genix.smartform.ISmartformEntity;
import neuragenix.genix.smartform.ISmartformDE;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DBField;
import neuragenix.common.Utilities;
public class Biospecimen 
{
 String strBiospecimenID = null;
 String intBiospecimenID = null;
 String intPatientID = null;
 Vector vtSmartformEntities = null;
  Vector vtFieldNames = null;
    /** Creates a new instance of Biospecimen */
    public Biospecimen() 
    {
        this.vtFieldNames = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen");
    }
    public Biospecimen(String str_id)
    {
        this();
        this.intBiospecimenID = str_id;
    }
    public String getID()
    {
        return this.strBiospecimenID;
    }
    public void setID(String str_ID)
    {
        this.strBiospecimenID = str_ID;
    }
    public String getInternalID()
    {
        return this.intBiospecimenID;
    }
    public void setInternalID(String str_ID)
    {
        this.intBiospecimenID = str_ID;
    }
    public String getPatientID()
    {
        return this.intPatientID;
    }
    public void setPatientID(String str_ID)
    {
        this.intPatientID = str_ID;
    }
    public Vector getFieldNames()
    {
        return this.vtFieldNames;
    }
    public void setFieldNames(Vector vt_Fields)
    {
        this.vtFieldNames = vt_Fields;
    }
    public Vector getSmartformEntities()
    {
        return this.vtSmartformEntities;
    }
    public void setSmartformEntities(Vector vt_smrtfrmE)
    {
        this.vtSmartformEntities = vt_smrtfrmE;
    }
    public String toXML()
    {
        StringBuffer strbfrXML = new StringBuffer();
        Hashtable hshSmartformXML = null;
        Hashtable hshSmartformQ = null;
        if(this.strBiospecimenID != null && this.strBiospecimenID.length() > 0)
        {
            strbfrXML.append("<Biospecimen>");
            String strlabel = ((DBField) DatabaseSchema.getFields().get(this.getDBSchemaName("strBiospecimenID"))).getLabelInForm();
            strbfrXML.append("<ID label=\'" + strlabel + "\'>" + this.strBiospecimenID + "</ID>");
            strlabel = ((DBField) DatabaseSchema.getFields().get(this.getDBSchemaName("intBiospecimenID"))).getLabelInForm();
            strbfrXML.append("<IDInternal label=\'" + strlabel + "\'>" + this.intBiospecimenID + "</IDInternal>");
            if(this.intPatientID != null && this.intPatientID.length() > 0)
            {
                strlabel = ((DBField) DatabaseSchema.getFields().get(this.getDBSchemaName("intPatientID"))).getLabelInForm();
                strbfrXML.append("<PatientID label=\'" + strlabel +"\'>"+this.intPatientID+"</PatientID>");
            }
            if(this.vtSmartformEntities.size() > 0)
            {
                hshSmartformXML = new Hashtable();
                hshSmartformQ = new Hashtable();
                for(java.util.Enumeration eSE = this.vtSmartformEntities.elements();eSE.hasMoreElements();)
                {
                    ISmartformEntity iSE = (ISmartformEntity)eSE.nextElement();
                    String smartformName = iSE.getSmartformName();
                    String strTemp = new String();
                    Vector vtQ = null;
                    if(hshSmartformXML.containsKey(smartformName))
                    {
                        strTemp = (String)hshSmartformXML.get(smartformName);
                    }
                    strTemp += iSE.toXML();
                    hshSmartformXML.put(smartformName, strTemp);
                }
            }
            if(hshSmartformXML != null && hshSmartformXML.size() > 0)
            {
                for(java.util.Enumeration ehsh =hshSmartformXML.keys();ehsh.hasMoreElements();)
                {
                    String key = (String)ehsh.nextElement();
                    strbfrXML.append("<Smartforms>");
                        strbfrXML.append("<Name>" + key + "</Name>");
                        strbfrXML.append((String)hshSmartformXML.get(key));
                    strbfrXML.append("</Smartforms>");
                }
            }
            strbfrXML.append("</Biospecimen>");
        }
        return strbfrXML.toString();
    }
    public String toXML(String strStartOrder,String strEndOrder)
    {
        StringBuffer strbfrXML = new StringBuffer();
        Hashtable hshSmartformXML = null;
        Hashtable hshSmartformQ = null;
        if(this.strBiospecimenID != null && this.strBiospecimenID.length() > 0)
        {
            strbfrXML.append("<Biospecimen>");
            String strlabel = ((DBField) DatabaseSchema.getFields().get(this.getDBSchemaName("strBiospecimenID"))).getLabelInForm();
            strbfrXML.append("<ID label=\'" + strlabel + "\'>" + this.strBiospecimenID + "</ID>");
            strlabel = ((DBField) DatabaseSchema.getFields().get(this.getDBSchemaName("intBiospecimenID"))).getLabelInForm();
            strbfrXML.append("<IDInternal label=\'" + strlabel + "\'>" + this.intBiospecimenID + "</IDInternal>");
            if(this.intPatientID != null && this.intPatientID.length() > 0)
            {
                strlabel = ((DBField) DatabaseSchema.getFields().get(this.getDBSchemaName("intPatientID"))).getLabelInForm();
                strbfrXML.append("<PatientID label=\'" + strlabel +"\'>"+this.intPatientID+"</PatientID>");
            }
            if(this.vtSmartformEntities.size() > 0)
            {
                hshSmartformXML = new Hashtable();
                hshSmartformQ = new Hashtable();
                for(java.util.Enumeration eSE = this.vtSmartformEntities.elements();eSE.hasMoreElements();)
                {
                    ISmartformEntity iSE = (ISmartformEntity)eSE.nextElement();
                    String smartformName = iSE.getSmartformName();
                    StringBuffer strbfrTemp = new StringBuffer();
                    StringBuffer strbfrQ = new StringBuffer();
                    if(hshSmartformXML.containsKey(smartformName))
                    {
                        strbfrTemp.append((StringBuffer)hshSmartformXML.get(smartformName));
                    }
                    strbfrTemp.append("<Smartform id=\'"+iSE.getSmartformParticipantKey()+"\'>");
                    Vector vtDE = new Vector(iSE.getDE());
                    Vector vtQ = null;
                    for(java.util.Enumeration eDE= vtDE.elements();eDE.hasMoreElements();)
                    {
                        ISmartformDE iSDE = (ISmartformDE)eDE.nextElement();
                        if(iSDE.getDENo() != null && new Integer(iSDE.getDENo()).intValue() >=  new Integer(strStartOrder).intValue() && new Integer(iSDE.getDENo()).intValue() <=  new Integer(strEndOrder).intValue())
                        {
                            if(iSDE.getResult() != null)
                            {
                                strbfrTemp.append("<Result>"+ Utilities.cleanForXSL(iSDE.getResult()) + "</Result>");;
                            }
                            else
                            {
                                strbfrTemp.append("<Result> </Result>");
                            }
                            if(hshSmartformQ.containsKey(smartformName))
                            {
                                vtQ = (Vector)hshSmartformQ.get(smartformName);
                            }
                            else
                            {
                                vtQ = new Vector();
                            }
                            if(!vtQ.contains(iSDE.getName()))
                            {
                                vtQ.add(iSDE.getName());
                            }
                            hshSmartformQ.put(smartformName, vtQ);                            
                        }
                        if(iSDE.getName().equalsIgnoreCase("protein"))
                        {
                        	strbfrTemp.append("<Other>"+ Utilities.cleanForXSL(iSDE.getOther()) + "</Other>");;
                        }
                    }
                    strbfrTemp.append("</Smartform>");
                    hshSmartformXML.put(smartformName, strbfrTemp);
                }
            }
            if(hshSmartformXML != null && hshSmartformXML.size() > 0)
            {
                for(java.util.Enumeration ehsh =hshSmartformXML.keys();ehsh.hasMoreElements();)
                {
                    String key = (String)ehsh.nextElement();
                    strbfrXML.append("<Smartforms>");
                        strbfrXML.append("<Name>" + Utilities.cleanForXSL(key) + "</Name>");
                        strbfrXML.append((StringBuffer)hshSmartformXML.get(key));
                        if(hshSmartformQ != null && hshSmartformQ.size() > 0)
                        {
                            strbfrXML.append("<Questions>");
                            Vector vtQ = (Vector)hshSmartformQ.get(key);
                            if(vtQ != null && vtQ.size() > 0)
                            {
	                            for(Enumeration eQ = vtQ.elements();eQ.hasMoreElements();)
	                            {
	                                String strQ = (String)eQ.nextElement();
	                                strbfrXML.append("<Question>" + strQ +"</Question>");
	                            }
                            }
                            strbfrXML.append("</Questions>");
                        }
                    strbfrXML.append("</Smartforms>");
                }
            }
            strbfrXML.append("</Biospecimen>");
        }
        return strbfrXML.toString();
    }
    public String getDBSchemaName(String str_param)
    {
        String strName = null;
        for(java.util.Enumeration eparam = this.vtFieldNames.elements();eparam.hasMoreElements();)
        {
            strName = (String)eparam.nextElement();
            String[] strReqname = strName.split("_");
            if(str_param.equalsIgnoreCase(strReqname[1]))
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
}
