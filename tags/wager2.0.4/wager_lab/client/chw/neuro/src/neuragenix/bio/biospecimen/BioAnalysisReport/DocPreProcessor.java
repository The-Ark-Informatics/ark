/*
 * DocPreProcessor.java
 *
 * Created on August 25, 2005, 9:36 PM
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
import java.text.SimpleDateFormat;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.security.IPerson;

import neuragenix.common.NGXRuntimeProperties;
import neuragenix.genix.smartform.ISmartformEntity;
import neuragenix.genix.smartform.ISmartformDE;
import neuragenix.utils.Document;
import neuragenix.common.Utilities;

public class DocPreProcessor implements IDocPreProcessor 
{
    Patient patObj = null;
    Biospecimen bioObj = null;
    DocPPFactory dppfObj = null;
    Vector vtdocObj = null;
    Vector vtReportList = null;
    StringBuffer strbfrXML = null;
    String strStylesheet = null;
    NGXRuntimeProperties rp = null;
    String strexternalName = null;
    /** Creates a new instance of DocPreProcessor */
    public DocPreProcessor() 
    {
    }
    public DocPreProcessor(NGXRuntimeProperties rp)
    {
        this.rp = rp;
    }
    public void setNGXProperties(NGXRuntimeProperties rp)
    {
    	this.rp = rp;
    }
    public ChannelRuntimeData processRuntimeData(ChannelRuntimeData rd)
    {
        String action = rd.getParameter("action");
        String strStartOrder = rd.getParameter("StartOrder");
        String strEndOrder = rd.getParameter("EndOrder");
        if(action.equals("prepare"))
        {
            if(dppfObj == null)
            {
                dppfObj = ReportFactory.getDocPPFactoryInstance();
            }
            if(this.bioObj == null)
            {
                this.bioObj = dppfObj.getBiospecimen(rd.getParameter("BIOSPECIMEN_intBiospecimenID"));
                this.bioObj = dppfObj.getSmartformResults(this.bioObj, rd.getParameter("strDomain"));
                this.vtdocObj = dppfObj.getDoctrList();
            }
            StringBuffer strbfrXML = new StringBuffer("<AutoDocs>");
            strbfrXML.append(dppfObj.getBiospecimenXML(this.bioObj,strStartOrder,strEndOrder));
            this.vtReportList = dppfObj.getReportList();
            strbfrXML.append(dppfObj.getReportXML(this.vtReportList));
            strbfrXML.append(dppfObj.getDoctorListXML(this.vtdocObj));
            strbfrXML.append("</AutoDocs>");
            rp.addXML(strbfrXML.toString());
            rp.setStylesheet("view_report_options");
        }
        else if(action.equals("next"))
        {
            ChannelRuntimeData rntimeData = this.createRuntimeData(rd);
            rp.clearXML();
            StringBuffer strbfrXML = new StringBuffer("<AutoDocs><Biospecimen><IDInternal>");
            strbfrXML.append(this.bioObj.intBiospecimenID);
            strbfrXML.append("</IDInternal></Biospecimen></AutoDocs>");
            rp.addXML(strbfrXML.toString());
            rp.setStylesheet("set_comments");
            return rntimeData;
        }
        else if(action.equals("save"))
        {
//            ChannelRuntimeData rntimeData = null;
            try
            {
                this.patObj = dppfObj.getPatient(bioObj.getPatientID());
                ChannelRuntimeData rdpatient = patObj.toChannelRuntimeData();
                for(java.util.Enumeration epat=rdpatient.getParameterNames();epat.hasMoreElements();)
                {
                    String strkey = (String)epat.nextElement();
                    rd.setParameter(strkey,(String)rdpatient.getParameter(strkey));
                }
                rd = this.saveComments(rd);
                rp.clearXML();
                StringBuffer strbfrXML = new StringBuffer("<AutoDocs><Biospecimen><IDInternal>");
                strbfrXML.append(this.bioObj.intBiospecimenID);
                strbfrXML.append("</IDInternal></Biospecimen><SavedValues>");
                for(java.util.Enumeration evals=rd.getParameterNames();evals.hasMoreElements();)
                {
                    String strkey = (String)evals.nextElement();
                    if(strkey.startsWith("txtarea_"))
                    {
                        String strvalue = rd.getParameter(strkey);
                        strbfrXML.append("<value id=\'" + strkey + "\'>" + strvalue + "</value>");
                    }
                }
                strbfrXML.append("</SavedValues>");
                strbfrXML.append("<report_selected>"+this.dppfObj.getReportFileName(this.vtReportList, this.strexternalName)+"</report_selected>");
                strbfrXML.append("</AutoDocs>");
                rp.addXML(strbfrXML.toString());
            }
            catch(Exception e)
            {
                LogService.instance().log(LogService.ERROR, "Unknown error in getting the save action in DocPreProcessor - " + e.toString(), e);
            }
        }
        return rd;
    }
    public ChannelRuntimeData createRuntimeData(ChannelRuntimeData rd)
    {
        this.strexternalName = rd.getParameter("report_name");
        Date dtToday = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strdate = sdf.format(dtToday);
        rd.setParameter("Today_date",strdate);
        if(this.bioObj != null)
        {
            try
            {
                ChannelRuntimeData rdbio = this.bioObj.toChannelRuntimeData();
                for(java.util.Enumeration erdbio= rdbio.getParameterNames();erdbio.hasMoreElements();)
                {
                    String strkey = (String)erdbio.nextElement();
                    rd.setParameter(strkey,(String)rdbio.getParameter(strkey));
                }
            }
            catch(Exception e)
            {
              LogService.instance().log(LogService.ERROR, "Unknown error in getting the createruntimeData action in DocPreProcessor - " + e.toString(), e);  
            }
            Vector vtSE = this.bioObj.getSmartformEntities();
            for(java.util.Enumeration eSE=vtSE.elements();eSE.hasMoreElements();)
            {
                ISmartformEntity ise = (ISmartformEntity)eSE.nextElement();
                if(rd.getParameter(ise.getSmartformParticipantKey())!= null)
                {
                    Vector vtDE = new Vector(ise.getDE());
                    for(java.util.Enumeration eDE = vtDE.elements();eDE.hasMoreElements();)
                    {
                        ISmartformDE isDE = (ISmartformDE)eDE.nextElement();
                        String strquestion = ise.getSmartformName()+"_question_"+isDE.getDENo();
                        StringBuffer strbfrresult = new StringBuffer();
                        if(rd.getParameter(strquestion) != null)
                        {
                            strbfrresult.append(rd.getParameter(strquestion)+"\n");
                        }
                        if(isDE.getResult() != null)
                        {
                            strbfrresult.append(isDE.getResult());
                        }
                        else
                        {
                            strbfrresult.append("");
                        }
                        rd.setParameter(strquestion,strbfrresult.toString());
                    }
                }
            }
        }
        if(this.vtReportList != null && this.vtReportList.size() > 0)
        {
            for(java.util.Enumeration ereport = this.vtReportList.elements();ereport.hasMoreElements();)
            {
                neuragenix.utils.Document doc = (Document)ereport.nextElement();
                if(doc.getExternalName().equalsIgnoreCase(this.strexternalName))
                {
                    rd.setParameter("intDocTemplID",new String(doc.getID()).trim());
                    rd.setParameter("intDocTemplateID",new String(doc.getID()).trim());
                    rd.setParameter("strDocExternalName",new String(doc.getExternalName()).trim());
                }
            }
        }
        if(this.vtdocObj != null && this.vtdocObj.size() > 0 && rd.getParameter("doctor_names") != null)
        {
            String[] strDoctors =rd.getParameterValues("doctor_names");
            StringBuffer strbfrDoc = new StringBuffer();
            for(int i=0;i<strDoctors.length;i++)
            {
                String[] strid = strDoctors[i].split("_");
                for(java.util.Enumeration edoctr = this.vtdocObj.elements();edoctr.hasMoreElements();)
                {
                    Doctor doctrObj = (Doctor)edoctr.nextElement();
                    if(doctrObj.getID().equals(strid[1]))
                    {
                        strbfrDoc.append(doctrObj.getFullName() + "\n");
                    }
                }
            }
            rd.setParameter("Ref_Doctor",strbfrDoc.toString());
        }
        return rd;
    }
    public ChannelRuntimeData saveComments(ChannelRuntimeData rd)
    {
         for(java.util.Enumeration evals=rd.getParameterNames();evals.hasMoreElements();)
        {
            String strkey = (String)evals.nextElement();
            if(strkey.startsWith("txtarea_"))
            {
                String value = rd.getParameter(strkey);
                String[] temp = strkey.split("_");
                rd.setParameter(temp[1],value);
            }
        }
         return rd;
    }
}
