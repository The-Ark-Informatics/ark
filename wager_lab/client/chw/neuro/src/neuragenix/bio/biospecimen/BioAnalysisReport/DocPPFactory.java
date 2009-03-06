/*
 * DocPPFactory.java
 *
 * Created on August 25, 2005, 9:37 PM
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
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jasig.portal.services.LogService;
import org.jasig.portal.ChannelRuntimeData;
import org.w3c.dom.Node;

import neuragenix.dao.DALQuery;
import neuragenix.genix.smartform.SmartformFactory;
import neuragenix.genix.smartform.ISmartformManager;
import neuragenix.utils.Document;
import neuragenix.dao.DatabaseSchema;
import neuragenix.common.Utilities;
import neuragenix.common.XMLHandler;

import java.io.File;

public class DocPPFactory {
    
    /** Creates a new instance of DocPPFactory */
    public DocPPFactory() {
    }
    public Patient getPatient(String str_PatientID) {
        Patient ptObj = new Patient(str_PatientID);
        try {
            DALQuery qry_patient = new DALQuery();
            qry_patient.setDomain("PATIENT",null , null, null);
            qry_patient.setFields(ptObj.getFieldNames(),null);
            qry_patient.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", str_PatientID, 0, DALQuery.WHERE_HAS_VALUE);
            qry_patient.setWhere("AND", 0, "PATIENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs_patient = qry_patient.executeSelect();
            if(rs_patient.next()) {
                Field[] fldNames = ptObj.getClass().getDeclaredFields();
                for(int i = 0;i<fldNames.length;i++) {
                    if(fldNames[i].getType().getName().equalsIgnoreCase("java.lang.String")) {
                        String strName = fldNames[i].getName();
                        String strreqName = ptObj.getDBSchemaName(strName);
                        fldNames[i].set(ptObj, rs_patient.getString(strreqName));
                    }
                    else if(fldNames[i].getType().getName().equalsIgnoreCase("java.util.Date")) {
                        String strName = fldNames[i].getName();
                        String strreqName = ptObj.getDBSchemaName(strName);
                        fldNames[i].set(ptObj, rs_patient.getDate(strreqName));
                    }
                }
            }
            rs_patient.close();
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the Patient in DocPPFactory - " + e.toString(), e);
        }
        return ptObj;
    }
    public Biospecimen getBiospecimen(String str_BiospecimenID) {
        Biospecimen bio = new Biospecimen(str_BiospecimenID);
        try {
            DALQuery qry_biospcmen = new DALQuery();
            qry_biospcmen.setDomain("BIOSPECIMEN",null , null, null);
            qry_biospcmen.setFields(bio.getFieldNames(),null);
            qry_biospcmen.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", str_BiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
            qry_biospcmen.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs_biospcmen = qry_biospcmen.executeSelect();
            if(rs_biospcmen.next()) {
                Field[] fldNames = bio.getClass().getDeclaredFields();
                for(int i = 0;i<fldNames.length;i++) {
                    if(fldNames[i].getType().getName().equalsIgnoreCase("java.lang.String")) {
                        String strName = fldNames[i].getName();
                        String strreqName = bio.getDBSchemaName(strName);
                        fldNames[i].set(bio, rs_biospcmen.getString(strreqName));
                    }
                }
            }
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the Biospecimen in DocPPFactory - " + e.toString(), e);
        }
        return bio;
    }
    public Biospecimen getSmartformResults(Biospecimen bio,String strdomain) {
        ISmartformManager ismObj = SmartformFactory.getSmartformManagerInstance();
        List lstSMR = ismObj.getSmartformResults(strdomain, bio.getInternalID());
        bio.setSmartformEntities(new Vector(lstSMR));
        return bio;
    }
    
    public Vector getReportList() {
        Vector vtReport = new Vector();
        try {
            DALQuery qry_report = new DALQuery();
            qry_report.setDomain("DOCUMENTTEMPLATE",null , null, null);
            qry_report.setField("DOCUMENTTEMPLATE_intDocTemplateID",null);
            qry_report.setField("DOCUMENTTEMPLATE_strExternalName",null);
            qry_report.setField("DOCUMENTTEMPLATE_strFileName",null);
            qry_report.setWhere(null, 0, "DOCUMENTTEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs_report = qry_report.executeSelect();
            while(rs_report.next()) {
                Document doc = new Document();
                doc.setID(rs_report.getString("DOCUMENTTEMPLATE_intDocTemplateID"));
                doc.setExternalName(rs_report.getString("DOCUMENTTEMPLATE_strExternalName"));
                doc.setFileName(rs_report.getString("DOCUMENTTEMPLATE_strFileName"));
                vtReport.add(doc);
            }
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the Reports List in DocPPFactory - " + e.toString(), e);
        }
        return vtReport;
    }
    public String getReportFileName(Vector vtList,String strExternalName) {
        String strfName = null;
        for(java.util.Enumeration elst=vtList.elements();elst.hasMoreElements();) {
            Document doc = (Document)elst.nextElement();
            if(doc.getExternalName().equalsIgnoreCase(strExternalName)) {
                strfName = doc.getFileName();
            }
        }
        return strfName;
    }
    
    public Vector getDoctrList() {
        Vector vtDocLst = new Vector();
        try {
            DALQuery qry_doclst = new DALQuery();
            qry_doclst.setDomain("DOCTOR",null , null, null);
            qry_doclst.setField("DOCTOR_strID",null);
            qry_doclst.setField("DOCTOR_strTitle",null);
            qry_doclst.setField("DOCTOR_strFirstName",null);
            qry_doclst.setField("DOCTOR_strSurname",null);
            qry_doclst.setOrderBy("DOCTOR_strSurname", "ASC");
            qry_doclst.setWhere(null, 0, "DOCTOR_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs_doclst = qry_doclst.executeSelect();
            while(rs_doclst.next()) {
                Doctor doctrObj = new Doctor(rs_doclst.getString("DOCTOR_strID"));
                doctrObj.setTitle(rs_doclst.getString("DOCTOR_strTitle"));
                doctrObj.setFirstName(rs_doclst.getString("DOCTOR_strFirstName"));
                doctrObj.setSurname(rs_doclst.getString("DOCTOR_strSurname"));
                vtDocLst.add(doctrObj);
            }
            rs_doclst.close();
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the Reports List in DocPPFactory - " + e.toString(), e);
        }
        return vtDocLst;
    }
    public Vector getDoctors() {
        Vector vtDocLst = new Vector();
        try {
            DALQuery qry_doclst = new DALQuery();
            qry_doclst.setDomain("DOCTOR",null , null, null);
            qry_doclst.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_doctor_details"),null);
            qry_doclst.setOrderBy("DOCTOR_strSurname", "ASC");
            qry_doclst.setWhere(null, 0, "DOCTOR_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs_doc = qry_doclst.executeSelect();
            while(rs_doc.next()) {
                Doctor doctr = new Doctor();
                Field[] fldNames = doctr.getClass().getDeclaredFields();
                for(int i = 0;i<fldNames.length;i++) {
                    if(fldNames[i].getType().getName().equalsIgnoreCase("java.lang.String")) {
                        String strName = fldNames[i].getName();
                        String strreqName = doctr.getDBSchemaName(strName);
                        try {
                            fldNames[i].set(doctr, rs_doc.getString(strreqName));
                        }
                        catch(IllegalAccessException iae) {
                            //don't do anything....this is because of a field that is final in the class
                            //just continue
                        }
                    }
                }
                vtDocLst.add(doctr);
            }
            rs_doc.close();
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the Doctors in DocPPFactory - " + e.toString(), e);
        }
        return vtDocLst;
        
    }
    public boolean saveDoctor(ChannelRuntimeData rd) {
        boolean blsaved = false;
        try {
            DALQuery query_doc = new DALQuery();
            query_doc.setDomain("DOCTOR", null, null, null);
            query_doc.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_doctor_details"),rd);
            if(rd.getParameter("DOCTOR_strID") != null && rd.getParameter("DOCTOR_strID").trim().length() > 0) {
                query_doc.setWhere(null, 0, "DOCTOR_strID", "=", rd.getParameter("DOCTOR_strID"), 0, DALQuery.WHERE_HAS_VALUE);
                query_doc.executeUpdate();
            }
            else {
                query_doc.executeInsert();
            }
            blsaved = true;
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in saving Doctors in DocPPFactory - " + e.toString(), e);
            blsaved = false;
        }
        return blsaved;
    }
    public boolean deleteDoctor(String strID) {
        boolean bldeleted = false;
        try {
            DALQuery query_doc = new DALQuery();
            query_doc.setDomain("DOCTOR", null, null, null);
            query_doc.setField("DOCTOR_intDeleted","-1");
            query_doc.setWhere(null, 0, "DOCTOR_strID", "=", strID, 0, DALQuery.WHERE_HAS_VALUE);
            bldeleted = query_doc.executeUpdate();
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in saving Doctors in DocPPFactory - " + e.toString(), e);
            bldeleted = false;
        }
        return bldeleted;
    }
    public String getDoctorListXML(Vector vtDocList) {
        StringBuffer strbfrXML = new StringBuffer();
        if(vtDocList != null && vtDocList.size() > 0) {
            strbfrXML.append("<Doctors>");
            for(java.util.Enumeration edoctr = vtDocList.elements();edoctr.hasMoreElements();) {
                strbfrXML.append("<Doctor>");
                Doctor docObj = (Doctor)edoctr.nextElement();
                strbfrXML.append("<ID>"+docObj.getID()+"</ID>");
                strbfrXML.append("<Name>"+docObj.getFullName()+"</Name>");
                strbfrXML.append("</Doctor>");
            }
            strbfrXML.append("</Doctors>");
        }
        return strbfrXML.toString();
    }
    public String getDoctorXML(Vector vtDocList) {
        StringBuffer strbfrXML = new StringBuffer();
        if(vtDocList != null && vtDocList.size() > 0) {
            strbfrXML.append("<Doctors>");
            for(java.util.Enumeration edoctr = vtDocList.elements();edoctr.hasMoreElements();) {
                Doctor doctrObj = (Doctor)edoctr.nextElement();
                strbfrXML.append(doctrObj.toXML());
            }
            strbfrXML.append("</Doctors>");
        }
        return strbfrXML.toString();
    }
    public String getReportXML(Vector vtReport) {
        StringBuffer strbfrXML = new StringBuffer();
        if(vtReport != null && vtReport.size() > 0) {
            strbfrXML.append("<Documents>");
            for(java.util.Enumeration ereport = vtReport.elements();ereport.hasMoreElements();) {
                Document doc = (Document)ereport.nextElement();
                strbfrXML.append(doc.toXML());
            }
            strbfrXML.append("</Documents>");
        }
        return strbfrXML.toString();
    }
    public String getBiospecimenXML(Biospecimen bio) {
        StringBuffer strbfrXML = new StringBuffer();
        if(bio != null) {
            strbfrXML.append(bio.toXML());
        }
        return strbfrXML.toString();
    }
    public String getBiospecimenXML(Biospecimen bio,String strStartOrder,String strEndOrder) {
        StringBuffer strbfrXML = new StringBuffer();
        if(bio != null) {
            strbfrXML.append(bio.toXML(strStartOrder,strEndOrder));
        }
        return strbfrXML.toString();
    }
    public Hashtable getComment(String strLocation,String strFileName,String strResultType) {
        Hashtable hshComment = new Hashtable();
        //check if the comments xml file exist - it might not exist when uploading a template
        //for the first time or when changing the external name of existing template from the gui
        try {
            File commentFile = new File(strLocation,strFileName);
            if (!commentFile.exists()) return hshComment;
        }catch (Exception ex){
            ex.printStackTrace();
            return hshComment;
        }
        
        XMLHandler xmlhandler = new XMLHandler();
        Vector vtNodes = xmlhandler.getNodes(strLocation,strFileName);
        Hashtable hshAttrValues = xmlhandler.getAttributesValues(vtNodes,"type");
        String[] strTemplate = strFileName.split(".x");
        for(Enumeration eAttr = hshAttrValues.keys();eAttr.hasMoreElements();) {
            Node nd = (Node)eAttr.nextElement();
            String strValue = (String)hshAttrValues.get(nd);
            if(strValue.equalsIgnoreCase(strTemplate[0])) {
                vtNodes = new Vector(xmlhandler.getChildNodes(nd));
                Hashtable hshAttrResult = xmlhandler.getAttributesValues(vtNodes,"type");
                for(Enumeration eAttrLl = hshAttrResult.keys();eAttrLl.hasMoreElements();) {
                    Node ndKey = (Node)eAttrLl.nextElement();
                    String strValueLl = (String)hshAttrResult.get(ndKey);
                    vtNodes = new Vector(xmlhandler.getChildNodes(ndKey));
                    Hashtable hshAttrResultLl = xmlhandler.getAttributesValues(vtNodes,"type");
                    for(Enumeration eAttrL2 =hshAttrResultLl.keys();eAttrL2.hasMoreElements(); ) {
                        Node childNode = (Node)eAttrL2.nextElement();
                        String strValL3 = (String)hshAttrResultLl.get(childNode);
                        if(strValL3.equalsIgnoreCase(strResultType)) {
                            vtNodes = new Vector(xmlhandler.getChildNodes(childNode));
                            for(Enumeration eNode = vtNodes.elements();eNode.hasMoreElements();) {
                                Node valNode = (Node)eNode.nextElement();
                                StringBuffer strbfr = new StringBuffer();
                                String strNode = null;
                                if(hshComment.containsKey(strValueLl)) {
                                    strbfr.append((String)hshComment.get(strValueLl));
                                }
                                if(valNode.getFirstChild() != null) {
                                    strbfr.append(valNode.getFirstChild().getNodeValue());
                                    hshComment.put(strValueLl,strbfr.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        return hshComment;
    }
    /** Replace the tags withing 'strToReplace' with the values from the runtime data. This was initially developed to replace
     *the tags from results & conclusion comments (which comes from an xml file for a particular report)
     */
    public String replaceTags(ChannelRuntimeData rd,String strToReplace,String startTag,String endTag) {
        if (strToReplace==null) return null;
        //System.out.println("XXXXXXXXX strToReplace: " + strToReplace);
        StringBuffer strbfr = new StringBuffer(strToReplace);
        int indx_start=0;
        int indx_end=0;
        String strtag = null;
        String strTagValue = null;
        do{
            indx_start= strbfr.indexOf("<", indx_end);
            indx_end = strbfr.indexOf("/>",indx_start);
            if (indx_end > indx_start && indx_start >0){
                strtag = strbfr.substring(indx_start+1,indx_end).trim();
                strTagValue=rd.getParameter(strtag);
                
                // System.out.println("XXXXXXXXX tag, tagvalue: " + strtag + ", "  + strTagValue);
                if (strTagValue==null){
                    strbfr.replace(indx_start,indx_end+2, "");
                    indx_end = indx_start;//start of new section to search
                }else{
                    strbfr.replace(indx_start,indx_end+2, strTagValue );
                    indx_end = indx_start + strTagValue.length();//start of new section to search
                }
            }
        }while (indx_start>0);
        //System.out.println("XXXXXXXXX strToReplace: " + strbfr.toString());
        return strbfr.toString();
    }
    
    
    
    /**
     * Returns the protein name to be written to the report. ie. mapps special characters to the unicode equivilant.
     * eg. #a is converted to greek alpha character.
     * #b is converted to greek belta
     * @return The unicode string (char) after replacing the special character (#a , # b)
     * @param proteinName The name of the protien with the sepcial characters eg. #a
     */
    public static String mapProteinDisplayName(String proteinName) {
        
        if (proteinName == null || proteinName.length() == 0)
            return proteinName;
        
        String proteinDisplayName = proteinName;
        
        try {
            
            DALQuery query = new DALQuery();
            query.setDomain("PROTEIN", null, null, null);
            query.setField("PROTEIN_strDisplayName", null);
            query.setWhere(null, 0, "PROTEIN_strName", "=", proteinName, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "PROTEIN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();
            
            if (rs.next()) {
                if (rs.getString("PROTEIN_strDisplayName") != null &&  !rs.getString("PROTEIN_strDisplayName").equals("")) {
                    proteinDisplayName = rs.getString("PROTEIN_strDisplayName");
                    // replace with greek alpha unicode mapping
                    proteinDisplayName = proteinDisplayName.replaceAll("#a", "\u03b1");
                    // replace with greek beta unicode mapping
                    proteinDisplayName = proteinDisplayName.replaceAll("#b", "\u03b2");
                    // replace with greek gamma unicode mapping
                    proteinDisplayName = proteinDisplayName.replaceAll("#g", "\u03b3");
                    // replace with greek delta unicode mapping
                    proteinDisplayName = proteinDisplayName.replaceAll("#d", "\u03b4");
                }
            }
            
            rs.close();
            
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in obtaining the proteins display name " + e.toString(), e);
        }
        
        //System.out.println ("proteinDisplayName " + proteinDisplayName);
        
        return proteinDisplayName;
        
    }
    
    /** Replace all #<ascii_char> in throws string  'escapedString' with the equivelent greek char. (eg. replace #a with alpha)
     */
    public static String mapToGreekChars(String escapedString){
        if (escapedString==null) return null;
        String greekString = escapedString;
        // replace with greek alpha unicode mapping
        greekString = greekString.replaceAll("#a", "\u03b1");
        // replace with greek beta unicode mapping
        greekString = greekString.replaceAll("#b", "\u03b2");
        // replace with greek gamma unicode mapping
        greekString = greekString.replaceAll("#g", "\u03b3");
        // replace with greek delta unicode mapping
        greekString = greekString.replaceAll("#d", "\u03b4");
        return greekString;
    }
}

