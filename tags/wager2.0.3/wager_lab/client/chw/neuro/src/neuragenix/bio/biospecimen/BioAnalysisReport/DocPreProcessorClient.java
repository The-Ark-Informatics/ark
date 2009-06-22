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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;
import org.jasig.portal.security.IPerson;

import neuragenix.common.NGXRuntimeProperties;
import neuragenix.genix.smartform.ISmartformEntity;
import neuragenix.genix.smartform.ISmartformDE;
import neuragenix.utils.Document;
import neuragenix.common.Utilities;
import neuragenix.security.exception.SecurityException;


public class DocPreProcessorClient implements IDocPreProcessor {
    
    Patient patObj = null;
    Biospecimen bioObj = null;
    DocPPFactory dppfObj = null;
    Vector vtdocObj = null;
    Vector vtReportList = null;
    StringBuffer strbfrXML = null;
    String strStylesheet = null;
    NGXRuntimeProperties rp = null;
    String strexternalName = null;
    static String AUTO_DOC_COMMENTS_DIR = "";
    static {
        
        try {
            AUTO_DOC_COMMENTS_DIR = PropertiesManager.getProperty("neuragenix.genix.admin.AutoDocCommentsDir");
        }
        catch(Exception e) {
            System.out.println("[CGenixAdmin (CRITICAL)] Property neuragenix.genix.admin.AutoDocCommentsDir not present.");
        }
    }
    /** Creates a new instance of DocPreProcessor */
    public DocPreProcessorClient() {
    }
    public DocPreProcessorClient(NGXRuntimeProperties rp) {
        this.rp = rp;
    }
    public void setNGXProperties(NGXRuntimeProperties rp) {
        this.rp = rp;
    }
    public ChannelRuntimeData processRuntimeData(ChannelRuntimeData rd) {
        String action = rd.getParameter("action");
        String strStartOrder = rd.getParameter("StartOrder");
        String strEndOrder = rd.getParameter("EndOrder");
        if(action.equals("prepare")) {
            if(dppfObj == null) {
                dppfObj = ReportFactory.getDocPPFactoryInstance();
            }
            if(this.bioObj == null) {
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
        else if(action.equals("next")) {
            ChannelRuntimeData rntimeData = this.createRuntimeData(rd);
            rp.clearXML();
            StringBuffer strbfrXML = new StringBuffer("<AutoDocs><Biospecimen><IDInternal>");
            strbfrXML.append(this.bioObj.intBiospecimenID);
            strbfrXML.append("</IDInternal></Biospecimen>");
            
            
            //anton start
            try{
                if (rp.getAuthToken().hasActivity("bioanalysis_view_report_comments")){
                    strbfrXML.append("<bioanalysis_view_report_comments/>");
                    //System.out.print("XXXXXXXXXXXXX user has the permission: bioanalysis_view_report_comments");
                }
                //else
                //    System.out.print("XXXXXXXXXXXXX user doesn't have the permission: bioanalysis_view_report_comments");
            }catch(SecurityException sex){
                LogService.instance().log(LogService.ERROR, "Error when checking the permisison bioanalysis_view_report_comments in DocPreProcessorClient - " + sex.toString(), sex);
            }
            strbfrXML.append("</AutoDocs>");
            //anton end
            
            rp.addXML(strbfrXML.toString());
            rp.setStylesheet("set_comments");
            return rntimeData;
        }
        else if(action.equals("save")) {
            //            ChannelRuntimeData rntimeData = null;
            try {
                this.patObj = dppfObj.getPatient(bioObj.getPatientID());
                ChannelRuntimeData rdpatient = patObj.toChannelRuntimeData();
                for(java.util.Enumeration epat=rdpatient.getParameterNames();epat.hasMoreElements();) {
                    String strkey = (String)epat.nextElement();
                    rd.setParameter(strkey,(String)rdpatient.getParameter(strkey));
                }
                rd = this.saveComments(rd);
                rp.clearXML();
                //rebuild the comments and the selection
                StringBuffer strbfrXML = new StringBuffer("<AutoDocs><Biospecimen><IDInternal>");
                strbfrXML.append(this.bioObj.intBiospecimenID);
                strbfrXML.append("</IDInternal></Biospecimen><SavedValues>");
                for(java.util.Enumeration evals=rd.getParameterNames();evals.hasMoreElements();) {
                    String strkey = (String)evals.nextElement();
                    if(strkey.startsWith("txtarea_")) {
                        String strvalue = Utilities.cleanForXSL(rd.getParameter(strkey));
                        strbfrXML.append("<value id=\'" + strkey + "\'>" + strvalue + "</value>");
                    }
                }
                strbfrXML.append("</SavedValues>");
                
                //anton start
                try{
                    if (rp.getAuthToken().hasActivity("bioanalysis_view_report_comments")){
                        strbfrXML.append("<bioanalysis_view_report_comments/>");
                        //System.out.print("XXXXXXXXXXXXX user has the permission: bioanalysis_view_report_comments");
                    }
                }catch(SecurityException sex){
                    LogService.instance().log(LogService.ERROR, "Error when checking the permisison bioanalysis_view_report_comments in DocPreProcessorClient - " + sex.toString(), sex);
                }
                //anton end
                
                strbfrXML.append("<report_selected>"+this.dppfObj.getReportFileName(this.vtReportList, this.strexternalName)+"</report_selected>");
                strbfrXML.append("</AutoDocs>");
                rp.addXML(strbfrXML.toString());
            }
            catch(Exception e) {
                LogService.instance().log(LogService.ERROR, "Unknown error in getting the save action in DocPreProcessor - " + e.toString(), e);
            }
        }
        return rd;
    }

    /*
     *  The following function is used to count the amount of times a particular
     *  question appears from the smartform data as the system scans through
     *
     */
    
    public void incrementAnswerCount(Hashtable htCountTable, String strQuestionName)
    {
       String value = (String) htCountTable.get(strQuestionName);
       if (value == null) value = "0";
       
       int count = 0;
       try
       {
          count = Integer.parseInt(value);
       }
       catch (NumberFormatException e)
       {
          // I really don't give two shits about what the result is
       }
       
       // who loves an increment? oh, i do i do!
       count++;
       
       // System.out.println ("Count for Q: " + strQuestionName + ", \t\t inced to :" + count);
       
       htCountTable.put(strQuestionName, count + "");
       
       // wow 7 lines of code, in three months.  I rock.
    }
    
    
    /**
     * Return a run time data that contains all the current rd parameters + new parameters to represents the questions of the SF participants selected from the previous page
     * This method is called from the report section of biospecimen channel when the user selects the template and SF entries to be included then clicks next
     */
    public ChannelRuntimeData createRuntimeData(ChannelRuntimeData rd) {
        this.strexternalName = rd.getParameter("report_name");
        Date dtToday = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strdate = sdf.format(dtToday);
        rd.setParameter("Today_date",strdate);
        
        Hashtable htRespectiveCount = new Hashtable();
        
        if(this.bioObj != null) {
            try {
                ChannelRuntimeData rdbio = this.bioObj.toChannelRuntimeData();
                for(java.util.Enumeration erdbio= rdbio.getParameterNames();erdbio.hasMoreElements();) {
                    String strkey = (String)erdbio.nextElement();
                    rd.setParameter(strkey,(String)rdbio.getParameter(strkey));
                }
            }
            catch(Exception e) {
                LogService.instance().log(LogService.ERROR, "Unknown error in getting the createruntimeData action in DocPreProcessor - " + e.toString(), e);
            }
            Vector vtSE = this.bioObj.getSmartformEntities();
            
            for(java.util.Enumeration eSE=vtSE.elements();eSE.hasMoreElements();) {//go through entities (SF participants)
            
                ISmartformEntity ise = (ISmartformEntity)eSE.nextElement();
                
                if(rd.getParameter(ise.getSmartformParticipantKey())!= null) { //making sure the key has been added to the rd ???!!!!!!
                    //status to indicate if the resutl is normal, abnormal or unknown (the question Normal doesn't exist so doesn't apply to this smartform). represented by 1,2 and -1 respectivly
                    short status = -1;
                    ISmartformDE SfDeNormal = ise.getDE("Normal");                    
                    if (SfDeNormal != null){
                        if (SfDeNormal.getResult().equalsIgnoreCase("normal")){                            
                            status = 1;
                        }
                        else {                            
                            status = 2;
                        }
                    }
                    
                    Vector vtDE = new Vector(ise.getDE());                    
                    for(java.util.Enumeration eDE = vtDE.elements();eDE.hasMoreElements();) {//go through data elements (questiongs)
                        ISmartformDE isDE = (ISmartformDE)eDE.nextElement();
                        String strquestion = ise.getSmartformName()+"_question_"+isDE.getDENo();
                        String strquestion_comma = ise.getSmartformName()+"_question_"+isDE.getDENo()+ "_comma";//anton
                        
                        String strquestion_normal = ise.getSmartformName()+"_question_"+isDE.getDENo()+ "_normal";//anton
                        String strquestion_normal_comma = ise.getSmartformName()+"_question_"+isDE.getDENo()+ "_normal_comma";//anton
                        String strquestion_abnormal = ise.getSmartformName()+"_question_"+isDE.getDENo()+ "_abnormal";//anton
                        String strquestion_abnormal_comma = ise.getSmartformName()+"_question_"+isDE.getDENo()+ "_abnormal_comma";//anton
                        
                        StringBuffer strbfrresult = new StringBuffer();
                        StringBuffer strbfrresult_comma = new StringBuffer();//anton
                        
                        StringBuffer strbfrresult_normal = new StringBuffer();//anton
                        StringBuffer strbfrresult_normal_comma = new StringBuffer();//anton
                        StringBuffer strbfrresult_abnormal = new StringBuffer();//anton
                        StringBuffer strbfrresult_abnormal_comma = new StringBuffer();//anton
                        
                        //this variable will be used to manipulate the comma delimited values (replace the last ',' with 'and')
                        //the approach is to append with 'and' as a delimiter. the next time you append, you replace the last ' and' with ','
                        String param_comma=null;
                        
                        //**************** SECTION: append \n or and to resutls which represents a values for the same questions of a smartform                        
                        if(rd.getParameter(strquestion) != null) { // we check if ther were more SF participants, through checking the rd. If so, replace the last and with , if possible then append and 
                            strbfrresult.append(rd.getParameter(strquestion)+"\n");
                            param_comma = rd.getParameter(strquestion_comma);
                            param_comma = replaceLast(" and" , "," , param_comma);                        
                            strbfrresult_comma.append(param_comma + " and ");//anton
                            
                            // incrementAnswerCount(htRespectiveCount, strquestion);
                            
                        }
                        if (status==1){//if normal, chech if we need to append \n or ,
                            if(rd.getParameter(strquestion_normal) != null) { // we check if ther were SF participants (this is not the first one), through checking the rd. If so, we append \n or ,
                                strbfrresult_normal.append(rd.getParameter(strquestion_normal) + "\n");
                                param_comma = rd.getParameter(strquestion_normal_comma);
                                param_comma = replaceLast(" and" , "," , param_comma);
                                strbfrresult_normal_comma.append(param_comma + " and ");//anton
                                
                                //incrementAnswerCount(htRespectiveCount, strquestion_normal);
                                //incrementAnswerCount(htRespectiveCount, strquestion_normal_comma);
                            }
                        }else if (status==2){//abnormal
                            if(rd.getParameter(strquestion_abnormal) != null) { // we check if ther were more SF participants, through checking the rd. If so, we append \n or ,
                                strbfrresult_abnormal.append(rd.getParameter(strquestion_abnormal)+"\n");
                                param_comma = rd.getParameter(strquestion_abnormal_comma);
                                param_comma = replaceLast(" and" , "," , param_comma);
                                strbfrresult_abnormal_comma.append(param_comma + " and ");//anton
                                //incrementAnswerCount(htRespectiveCount, strquestion_abnormal);
                                //incrementAnswerCount(htRespectiveCount, strquestion_abnormal_comma);
                            }
                        }
                        
                        //**************** SECTION: append result if not null
                        String result = isDE.getResult();

                        if(result != null) {// ?! when is it null
                            //map the special characters in protien name to greek chars, eg #a to alpha character (protien name is the first question always
                            if ((strquestion.indexOf("question_1") != -1)) {
                                isDE.setResult(DocPPFactory.mapProteinDisplayName(isDE.getResult()));
                                result = isDE.getResult();
                                
                                
                                // put upper case/lower case code here for proteins
                                rd.setParameter(strquestion_comma + "_lc",strbfrresult_comma.toString().toLowerCase());//anton
                                // repeat this action for the rest
                            }
                            // append all results
                            strbfrresult.append(result);
                            strbfrresult_comma.append(result);//anton
                            //append normal results
                            if (status==1){//if normal, chech if we need to append \n or ,
                                strbfrresult_normal.append(result);
                                strbfrresult_normal_comma.append(result);//anton
                                //append abnormal results
                            }else if (status==2){//abnormal
                                strbfrresult_abnormal.append(result);
                                strbfrresult_abnormal_comma.append(result);//anton
                            }
                        }
                        
                        // DAN: more code needs to go here
                        
                        
                        //update the rd with questions results
                        rd.setParameter(strquestion,strbfrresult.toString());
                        rd.setParameter(strquestion_comma,strbfrresult_comma.toString());//anton
                        rd.setParameter(strquestion_comma + "_lc",strbfrresult_comma.toString().toLowerCase());//anton
                        
                        incrementAnswerCount(htRespectiveCount, strquestion);
                        incrementAnswerCount(htRespectiveCount, strquestion_comma);
                        
                        if (status==1){
                            rd.setParameter(strquestion_normal, strbfrresult_normal.toString());//anton
                            rd.setParameter(strquestion_normal_comma, strbfrresult_normal_comma.toString());//anton
                            rd.setParameter(strquestion_normal_comma + "_lc", strbfrresult_normal_comma.toString().toLowerCase());//anton
                            incrementAnswerCount(htRespectiveCount, strquestion_normal);
                            incrementAnswerCount(htRespectiveCount, strquestion_normal_comma);
                        
                        }else if (status==2){
                            rd.setParameter(strquestion_abnormal, strbfrresult_abnormal.toString());//anton
                            rd.setParameter(strquestion_abnormal_comma, strbfrresult_abnormal_comma.toString());//anton
                            rd.setParameter(strquestion_abnormal_comma + "_lc", strbfrresult_abnormal_comma.toString().toLowerCase());//anton
                            incrementAnswerCount(htRespectiveCount, strquestion_abnormal);
                            incrementAnswerCount(htRespectiveCount, strquestion_abnormal_comma);
                            
                        }
                        
                                           
                        //-----------SECTION: Desease and Gene linking to protien
                        if(isDE.getName().equalsIgnoreCase("protein")) {
                            //Desease section
                            StringBuffer strbfrDisease = new StringBuffer();
                            StringBuffer strbfrDisease_comma = new StringBuffer();
                            StringBuffer strbfrDisease_normal = new StringBuffer();
                            StringBuffer strbfrDisease_normal_comma = new StringBuffer();
                            StringBuffer strbfrDisease_abnormal = new StringBuffer();
                            StringBuffer strbfrDisease_abnormal_comma = new StringBuffer();
                            
                            
                            String strDisease = ise.getSmartformName()+"_Disease";
                            String strDisease_comma = ise.getSmartformName()+"_Disease_comma";
                            String strDisease_normal = ise.getSmartformName()+"_Disease_normal";
                            String strDisease_normal_comma = ise.getSmartformName()+"_Disease_normal_comma";
                            String strDisease_abnormal = ise.getSmartformName()+"_Disease_abnormal";
                            String strDisease_abnormal_comma = ise.getSmartformName()+"_Disease_abnormal_comma";
                            
                            
                            //all results (normal + abnormal)
                            if(rd.getParameter(strDisease) != null) {
                                strbfrDisease.append(rd.getParameter(strDisease)+ "\n");
                                param_comma = rd.getParameter(strDisease_comma);
                                param_comma = replaceLast(" and" , "," , param_comma);
                                strbfrDisease_comma.append(param_comma + " and ");
                            }
                            if(isDE.getParameter("strDisease") != null) {
                                strbfrDisease.append(isDE.getParameter("strDisease"));
                                strbfrDisease_comma.append(isDE.getParameter("strDisease"));
                            }
                            rd.setParameter(strDisease,strbfrDisease.toString());
                            rd.setParameter(strDisease_comma,strbfrDisease_comma.toString());
                            rd.setParameter(strDisease_comma + "_lc",strbfrDisease_comma.toString().toLowerCase());
                            
                            //Normal case
                            if (status==1){
                                if(rd.getParameter(strDisease_normal) !=null) {
                                    strbfrDisease_normal.append(rd.getParameter(strDisease_normal)+ "\n");
                                    param_comma = rd.getParameter(strDisease_normal_comma);
                                    param_comma = replaceLast(" and" , "," , param_comma);
                                    strbfrDisease_normal_comma.append(param_comma + " and ");
                                }
                                if(isDE.getParameter("strDisease") != null) {
                                    strbfrDisease_normal.append(isDE.getParameter("strDisease"));
                                    strbfrDisease_normal_comma.append(isDE.getParameter("strDisease"));
                                }
                                rd.setParameter(strDisease_normal,strbfrDisease_normal.toString());
                                rd.setParameter(strDisease_normal_comma,strbfrDisease_normal_comma.toString());
                                
                                // Extra tag for lower case of disease names
                                rd.setParameter(strDisease_normal_comma + "_lc", strbfrDisease_normal_comma.toString().toLowerCase());
                            }
                            //abnormal case
                            else if (status==2){
                                if(rd.getParameter(strDisease_abnormal) !=null) {
                                    strbfrDisease_abnormal.append(rd.getParameter(strDisease_abnormal)+ "\n");
                                    param_comma = rd.getParameter(strDisease_abnormal_comma);
                                    param_comma = replaceLast(" and" , "," , param_comma);
                                    strbfrDisease_abnormal_comma.append( param_comma + " and ");
                                }
                                if(isDE.getParameter("strDisease") != null) {
                                    strbfrDisease_abnormal.append(isDE.getParameter("strDisease"));
                                    strbfrDisease_abnormal_comma.append(isDE.getParameter("strDisease"));
                                }
                                rd.setParameter(strDisease_abnormal,strbfrDisease_abnormal.toString());
                                rd.setParameter(strDisease_abnormal_comma,strbfrDisease_abnormal_comma.toString());
                                
                                // Extra tag for lower case of disease names
                                rd.setParameter(strDisease_abnormal_comma + "_lc",strbfrDisease_abnormal_comma.toString().toLowerCase());
                            }                           
                            
                            
                            
                            
                            // Gene section
                            StringBuffer strbfrGene = new StringBuffer();
                            StringBuffer strbfrGene_comma = new StringBuffer();
                            StringBuffer strbfrGene_normal = new StringBuffer();
                            StringBuffer strbfrGene_normal_comma = new StringBuffer();
                            StringBuffer strbfrGene_abnormal = new StringBuffer();
                            StringBuffer strbfrGene_abnormal_comma = new StringBuffer();
                            
                            String strGene = ise.getSmartformName()+"_Gene";
                            String strGene_comma = ise.getSmartformName()+"_Gene_comma";                            
                            String strGene_normal = ise.getSmartformName()+"_Gene_normal";
                            String strGene_normal_comma = ise.getSmartformName()+"_Gene_normal_comma";
                            String strGene_abnormal = ise.getSmartformName()+"_Gene_abnormal";
                            String strGene_abnormal_comma = ise.getSmartformName()+"_Gene_abnormal_comma";
                            
                            //all results (normal  + abnormal)
                            if(rd.getParameter(strGene) != null) {
                                strbfrGene.append(rd.getParameter(strGene)+ "\n");
                                param_comma = rd.getParameter(strGene_comma);
                                param_comma = replaceLast(" and" , "," , param_comma);
                                strbfrGene_comma.append(param_comma + " and ");
                            }                            
                            if(isDE.getParameter("strGene") != null) {
                                strbfrGene.append(isDE.getParameter("strGene"));
                                strbfrGene_comma.append(isDE.getParameter("strGene"));
                            }                            
                            rd.setParameter(strGene,strbfrGene.toString());
                            rd.setParameter(strGene_comma,strbfrGene_comma.toString());                           
                            
                            
                            //Normal case
                            if (status==1){
                                if(rd.getParameter(strGene_normal) !=null) {
                                    strbfrGene_normal.append(rd.getParameter(strGene_normal)+ "\n");
                                    param_comma = rd.getParameter(strGene_normal_comma);
                                    param_comma = replaceLast(" and" , "," , param_comma);
                                    strbfrGene_normal_comma.append(param_comma + " and ");
                                }
                                if(isDE.getParameter("strGene") != null) {
                                    strbfrGene_normal.append(isDE.getParameter("strGene"));
                                    strbfrGene_normal_comma.append(isDE.getParameter("strGene"));
                                }
                                rd.setParameter(strGene_normal,strbfrGene_normal.toString());
                                rd.setParameter(strGene_normal_comma,strbfrGene_normal_comma.toString());
                            }
                            //abnormal case
                            else if (status==2){
                                if(rd.getParameter(strGene_abnormal) !=null) {
                                    strbfrGene_abnormal.append(rd.getParameter(strGene_abnormal)+ "\n");
                                    param_comma = rd.getParameter(strGene_abnormal_comma);
                                    param_comma = replaceLast(" and" , "," , param_comma);
                                    strbfrGene_abnormal_comma.append(param_comma + " and ");
                                }
                                if(isDE.getParameter("strGene") != null) {
                                    strbfrGene_abnormal.append(isDE.getParameter("strGene"));
                                    strbfrGene_abnormal_comma.append(isDE.getParameter("strGene"));
                                }
                                rd.setParameter(strGene_abnormal,strbfrGene_abnormal.toString());
                                rd.setParameter(strGene_abnormal_comma,strbfrGene_abnormal_comma.toString());
                                
                            }         
                            
                            
                        }
                        
                    }
                }
            }
        }
        

        
        //to get the document details
        if(this.vtReportList != null && this.vtReportList.size() > 0) {
            for(java.util.Enumeration ereport = this.vtReportList.elements();ereport.hasMoreElements();) {
                neuragenix.utils.Document doc = (Document)ereport.nextElement();
                if(doc.getExternalName().equalsIgnoreCase(this.strexternalName)) {
                    rd.setParameter("intDocTemplID",new String(doc.getID()).trim());
                    rd.setParameter("intDocTemplateID",new String(doc.getID()).trim());
                    rd.setParameter("strDocExternalName",new String(doc.getExternalName()).trim());
                }
            }
        }
        if(this.vtdocObj != null && this.vtdocObj.size() > 0 && rd.getParameter("doctor_names") != null) {
            String[] strDoctors =rd.getParameterValues("doctor_names");
            StringBuffer strbfrDoc = new StringBuffer();
            StringBuffer strbfrDoc_comm = new StringBuffer();//anton
            for(int i=0;i<strDoctors.length;i++) {//for all doctor_names rd parameter
                String[] strid = strDoctors[i].split("_");
                //the id of the docotr is sent by the browser not the name
                //so we need to find the name from vtdocObj
                for(java.util.Enumeration edoctr = this.vtdocObj.elements();edoctr.hasMoreElements();) {
                    Doctor doctrObj = (Doctor)edoctr.nextElement();
                    if(doctrObj.getID().equals(strid[1])) {
                        if (strbfrDoc.length()>0)
                            strbfrDoc.append("\n");
                        strbfrDoc.append(doctrObj.getFullName());
                        if (strbfrDoc_comm.length()>0){
                            strbfrDoc_comm = new StringBuffer( replaceLast(" and" , "," , strbfrDoc_comm.toString() ) );
                            strbfrDoc_comm.append(" and ");
                        }
                        strbfrDoc_comm.append(doctrObj.getFullName());//anton
                    }
                }
            }
            rd.setParameter("Ref_Doctor",strbfrDoc.toString());
            rd.setParameter("Ref_Doctor_comma",strbfrDoc_comm.toString());//anton
        }
        
        
        /*
         * Below lies the code for handling "respectively" or whatever bloody thing you want to have there
         *
         */
        
        Enumeration respectiveQuestions = htRespectiveCount.keys();
        while(respectiveQuestions.hasMoreElements())
        {
           String questionReference = (String) respectiveQuestions.nextElement();
           String value = (String) htRespectiveCount.get(questionReference);
           
           int answerCount = 0;
           
           try
           {
              answerCount = Integer.parseInt(value);
           }
           catch (NumberFormatException e)
           {
             // Throw away
           }
           if (answerCount > 1)
           {
              rd.setParameter(questionReference + "_respectively", ", respectively");
           }
           else
           {
              // rd.setParameter(questionReference = "_respectively", "");
           }
        }
        
        // comments&conclusion for (normal or abnormal)
        String strFilename = this.strexternalName.replaceAll(" ","_") + ".xml";
        Hashtable hshComments = this.dppfObj.getComment(this.AUTO_DOC_COMMENTS_DIR,strFilename,rd.getParameter("normal_abnormal"));
        
        for(java.util.Enumeration ecomments = hshComments.keys();ecomments.hasMoreElements();) {
            String commentType = (String)ecomments.nextElement();
            String comment = (String)hshComments.get(commentType);
            comment = this.dppfObj.replaceTags(rd,comment, "startTag","String endTag");//the last 2 parameters don't make sence; left overs
            //not sure if that is necessary. it is usefull in case there are some #<ascii_char> in the comment body - specified explicitly
            if (comment != null){
                comment = DocPPFactory.mapToGreekChars(comment);//Anton: map the #<cahr> to the greek char. The method developed initially
            }
            rd.setParameter(commentType,comment);
            //System.out.println("ZZZZZ comments: " + commentType + " = " + comment);
        }
        
        return rd;
    }
    public ChannelRuntimeData saveComments(ChannelRuntimeData rd) {
        for(java.util.Enumeration evals=rd.getParameterNames();evals.hasMoreElements();) {
            String strkey = (String)evals.nextElement();
            if(strkey.startsWith("txtarea_")) {
                String value = rd.getParameter(strkey);
                //the general comments
                value = DocPPFactory.mapToGreekChars(value);//Anton: map the #<cahr> to the greek char. The method developed initially
                
                //System.out.println("strkey: " + strkey + " value:" + value);
                String temp = strkey.substring(8);
                //String[] temp = strkey.split("_");
                //rd.setParameter(temp[1],value);
                rd.setParameter(temp,value);
            }
        }
        return rd;
    }
    
    /** Try to replace the last delimiter with new delimiter (eg. replace last 'and' with ',' coz we want to append another values )
     * Initial case: prot1, prot2 and prot3 -> prot1, prot2, prot3    - param ' and' , ','
     */
    private String replaceLast(String oldStr, String newStr, String str){
        String result = null;
        int index = str.lastIndexOf(oldStr);
        if (index==-1) return str;
        result = str.substring(0,index) + newStr ;
        if (str.length() > (index+oldStr.length()) ) // there are more caracters after oldStr in str (replaced string is not the last)
            //append the rest of the string (originally after oldStr)
            result += str.substring(index + oldStr.length());
        return result;
    }
    
    
    
}
