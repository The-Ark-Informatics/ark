/*
 * BatchResultsManager.java
 * Description: Generate smartform results in batches
 *              option 1: same smartform result for multiple specimens
 *              option 2: multiple smartform results for a single specimen 
 * Created on 22 August 2005, 14:09
 * Author: Seena Parappat
 *
 */

package neuragenix.bio.biospecimen;

/**
 *
 * @author  sparappat
 */

import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.genix.smartform.SmartformBatchGenerationInfo;
import neuragenix.genix.smartform.SmartformBatchGenerator;
import neuragenix.genix.smartform.SmartformUtilities;
import neuragenix.bio.utilities.*;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Enumeration;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.security.IPerson;

public class PickerManager {
    
    private AuthToken authToken = null;    
    private NGXRuntimeProperties rp = null;
    private ChannelRuntimeData runtimeData;
    
    private static final String SEARCH_PROTEIN = "search_protein";
    private static final String SEARCH_ANTIBODY = "search_antibody";
    
    private String sortBy = null;
    private String order = "ASC";
    private String returnValue = "";
    private String returnURL = "";
    private String lastAnalysis = "";
    private String lastAction = "";  
    private String lastBiospecimen = "";
    
    private DALSecurityQuery proteinQuery;

    
    /** Creates a new instance of BatchCloneManager */
    public PickerManager(AuthToken authToken) 
    {
        this.authToken = authToken;
    }
    
    public PickerManager(NGXRuntimeProperties rp)
    {
        this.rp = rp;
        this.authToken = rp.getAuthToken();
    }
   
    public NGXRuntimeProperties processRuntimeData(ChannelRuntimeData rd)
    {
        this.runtimeData = rd;
        rp.clearXML();
        
        if (((runtimeData.getParameter("action") != null) && (!runtimeData.getParameter("action").equals(""))) && SEARCH_PROTEIN.equals(runtimeData.getParameter("action")))
        {
            rp.addXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><body>");
            rp.addXML("<proteinMode>" + "SEARCH_PROTEIN" + "</proteinMode>");
            rp.addXML(doSearchProtein() + "</body>");
        }

        else if (((runtimeData.getParameter("action") != null) && (!runtimeData.getParameter("action").equals(""))) && SEARCH_ANTIBODY.equals(runtimeData.getParameter("action")))
        {
            rp.addXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><body>");
            rp.addXML("<proteinMode>" + "SEARCH_ANTIBODY" + "</proteinMode>");
            rp.addXML(doSearchProtein() + "</body>");
        }

        else if (((runtimeData.getParameter("current") != null) && (!runtimeData.getParameter("current").equals(""))) && SEARCH_PROTEIN.equals(runtimeData.getParameter("current")))
        {
            rp.addXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><body>");
            rp.addXML("<proteinMode>" + "SEARCH_ANTIBODY" + "</proteinMode>");
            rp.addXML(doSearchProtein() + "</body>");
        }   
        
        return rp;
    }
    
    /**
     * Method to display a list of protein and allow user to choose
     *
     * @return xml string
     **/
    private String doSearchProtein(){
        
        StringBuffer strXML = new StringBuffer();
        
        try
        {
            String proteinID = runtimeData.getParameter( "PROTEIN_intProteinKey" );
            returnURL = runtimeData.getParameter( "returnURL");
            returnValue = runtimeData.getParameter( "returnValue" );
       
            
            // search for protein
            if( proteinID == null )
            {
                
                // build protein list
                Vector formfields = DatabaseSchema.getFormFields( "cbiospecimen_view_protein" );
                
                if( (proteinQuery == null) || (runtimeData.getParameter( "newSearch" ) == null )){
                    
                    proteinQuery = new DALSecurityQuery( "biospecimen_analysis", authToken );
                    proteinQuery.setDomain( "PROTEIN", null, null, null );
                    proteinQuery.setWhere( null, 0, "PROTEIN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    proteinQuery.setFields( formfields, null );
                    
                }
                String noOfRecords = PropertiesManager.getProperty("neuragenix.platformgenix.RecordPerPage");
                
                if( (runtimeData.getParameter( "noOfRecords") != null ) &&
                isNumeric( runtimeData.getParameter( "noOfRecords"))){
                    noOfRecords = runtimeData.getParameter( "noOfRecords");
                }
                
                int intNoOfRecords = Integer.parseInt( noOfRecords );
                int noOfPages = 0;
                int currentPage = 1; // is this right?
                
                if( runtimeData.getParameter( "orderBy" ) != null ){
                    if( runtimeData.getParameter( "orderBy" ).equals( sortBy ) ){
                        if( order.equals( "ASC" ) )
                            order = "DESC";
                        else
                            order = "ASC";
                        
                    }
                    sortBy = runtimeData.getParameter( "orderBy" );
                    
                }
                if( sortBy != null ){
                    proteinQuery.clearOrderBy();
                    
                    if( "PROTEIN_strName".equals(sortBy )){
                        proteinQuery.setOrderBy( sortBy, order );
                        proteinQuery.setOrderBy( "PROTEIN_strLocalisation", order );
                    }else{
                        proteinQuery.setOrderBy( sortBy, order );
                        proteinQuery.setOrderBy( "PROTEIN_strName", order );
                    }
                }else{
                    
                    proteinQuery.setOrderBy( "PROTEIN_strName", order );
                    proteinQuery.setOrderBy( "PROTEIN_strLocalisation", order );
                    
                }
                
                if( ( runtimeData.getParameter( "currentPage") != null ) &&
                isNumeric( runtimeData.getParameter( "currentPage"))){
                    currentPage = Integer.parseInt( runtimeData.getParameter( "currentPage") );
                }
                
                proteinQuery.clearLimitOffset();
                proteinQuery.setCaseSensitive(false);
                
                ResultSet tRS = proteinQuery.executeSelect();
                tRS.last();
                int allRecords = tRS.getRow();
                tRS = null;
                
                noOfPages = ( (allRecords - 1) / intNoOfRecords ) + 1;
                
                if( currentPage < 1 ) currentPage =1;
                if( currentPage > noOfPages ) currentPage = noOfPages;
                // check this line here.
                proteinQuery.setLimitOffset( intNoOfRecords, ((currentPage - 1) * intNoOfRecords) );
                
                
                
                
                strXML.append( "<currentPage>" + currentPage + "</currentPage>" );
                strXML.append( "<noOfPages>" + noOfPages + "</noOfPages>" );
                strXML.append( "<noOfRecords>" + noOfRecords + "</noOfRecords>" );
                
                if (runtimeData.getParameter("currentPage") != null)
                {
                    runtimeData.remove("currentPage");
                }
                
                if (runtimeData.getParameter("orderBy") != null)
                    runtimeData.remove("orderBy");
                
                if (runtimeData.getParameter("change_next") != null)
                    runtimeData.remove("change_next");
                ResultSet rs = proteinQuery.executeSelect();
                //TO-DO: lock
                strXML.append( QueryChannel.buildSearchXMLFileAndLockRecord( "protein", rs, formfields,
                "PROTEIN", "PROTEIN_intProteinKey", new LockRequest(authToken), LockRecord.READ_ONLY ));
                strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                
                runtimeData.remove( "current" );
                
                strXML.append( "<currentField>" + runtimeData.getParameter( "currentField" )
                    + "</currentField>" );
                strXML.append( "<nextField>" + runtimeData.getParameter( "nextField" )
                    + "</nextField>" );
                
                
                // DM - 12-Oct-2004
                // Under the assumption that the next field will always be 
                // the field that directly follows the previous
                
                String previousField = "";
                if ((runtimeData.getParameter("currentField") != null) && (!runtimeData.getParameter("currentField").equals("")))
                {
                    previousField = runtimeData.getParameter("currentField");
                    
                    int newField = Integer.parseInt(previousField.substring(1));
                    newField++;
                    runtimeData.setParameter("nextField", "d" + newField);
                    
                }
                
                if ((runtimeData.getParameter("nextField") != null) && (!runtimeData.getParameter("nextField").equals("")))
                  runtimeData.remove( runtimeData.getParameter( "nextField" ) );
                
                
                runtimeData.getParameter("currentField");
                
                if ((runtimeData.getParameter("currentField") != null) && (!runtimeData.getParameter("currentField").equals("")))
                   runtimeData.remove( runtimeData.getParameter( "currentField" ) );
                
                Enumeration myEnum = runtimeData.keys();
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" + param + "</name><value>" + Utilities.cleanForXSL(runtimeData.getParameter( param )) +
                        "</value></parameter>" );
                    
                }
                
                rp.setStylesheet(SEARCH_PROTEIN);
                if( runtimeData.getParameter( "proteinOnly") != null ){
                    //strXML.append( "<lastAction>" + lastAction + "</lastAction>" );
                    strXML.append( "<lastAction>true</lastAction>" );
                    strXML.append( "<BIOANALYSIS_strAnalysis>" + lastAnalysis + "</BIOANALYSIS_strAnalysis>" );

                    
                }
                if( runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") != null ){
                    strXML.append( "<BIOSPECIMEN_intBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID")
                        + "</BIOSPECIMEN_intBiospecimenID>" );
                }
                if( runtimeData.getParameter("PATIENT_intInternalPatientID") != null ){
                    strXML.append( "<PATIENT_intInternalPatientID>" + runtimeData.getParameter("PATIENT_intInternalPatientID")
                        + "</PATIENT_intInternalPatientID>" );
                }

                // search for antibody
            }
            else
            {
                
                Vector formfields = DatabaseSchema.getFormFields( "cbiospecimen_view_antibody" );
                DALSecurityQuery query = new DALSecurityQuery( "biospecimen_analysis", authToken );
                query.setDomain( "ANTIBODY", null, null, null );
                query.setWhere( null, 0, "ANTIBODY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "ANTIBODY_intProteinKey", "=", proteinID, 0, DALQuery.WHERE_HAS_VALUE );
                query.setFields( formfields, null );
                query.setOrderBy( "ANTIBODY_strName", "ASC" );
                
                ResultSet rs = query.executeSelect();
                //TO-DO: lock
                strXML.append( QueryChannel.buildSearchXMLFileAndLockRecord( "antibody", rs, formfields,
                "ANTIBODY", "ANTIBODY_intAntibodyKey", new LockRequest(authToken), LockRecord.READ_ONLY ));
                strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                
                strXML.append( "<returnURL>" + returnURL + "</returnURL>" );
                strXML.append( "<return>" + returnValue + "</return>" );
                strXML.append( "<lastAction>" + lastAction + "</lastAction>" );
                strXML.append( "<BIOANALYSIS_strAnalysis>" + lastAnalysis + "</BIOANALYSIS_strAnalysis>" );
                strXML.append( "<BIOSPECIMEN_intBiospecimenID>" + lastBiospecimen + "</BIOSPECIMEN_intBiospecimenID>" );
                strXML.append( "<PROTEIN_intProteinKey>" + runtimeData.getParameter("PROTEIN_intProteinKey") + "</PROTEIN_intProteinKey>" );
                strXML.append( "<PROTEIN_strName>" + runtimeData.getParameter("PROTEIN_strName") + "</PROTEIN_strName>" );
                strXML.append( "<PROTEIN_strLocalisation>" + runtimeData.getParameter("PROTEIN_strLocalisation") + "</PROTEIN_strLocalisation>" );
                
                
                runtimeData.remove( "current" );
                strXML.append( "<currentField>" + runtimeData.getParameter( "currentField" )
                    + "</currentField>" );
                strXML.append( "<nextField>" + runtimeData.getParameter( "nextField" )
                    + "</nextField>" );
                
                runtimeData.remove( runtimeData.getParameter( "nextField" ) );
                runtimeData.remove( runtimeData.getParameter( "currentField" ) );
                runtimeData.remove( "PROTEIN_intProteinKey" );
                
                Enumeration myEnum = runtimeData.keys();
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" + param + "</name><value>" + Utilities.cleanForXSL(runtimeData.getParameter( param )) +
                        "</value></parameter>" );
                    
                }
                                    
                rp.setStylesheet(SEARCH_ANTIBODY);
                
            }
            
            // Indicate if the Picker was selected from Batch Analysis Results generation
            if (runtimeData.getParameter("cloneDataFormLink") != null || runtimeData.getParameter("standaloneDataFormLink") != null) 
            {
                strXML.append( "<fromBatchAnalysis>" + "true" + "</fromBatchAnalysis>");
            }    
            
            
        }catch( Exception e){
            
            LogService.instance().log(LogService.ERROR, "Unknown error in Biospecimen Channel - " + e.toString(), e);
        }
        
        strXML = new StringBuffer ( "<protein>" + strXML.toString() + "</protein>" );
        
        strXML.append( "<biospecimen>" );
        
        if( runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") != null ){
            strXML.append( "<biospecimen_details>" + BiospecimenUtilities.getBiospecimenDetails( runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID"), authToken)
                + "</biospecimen_details>");
        
            strXML.append( "<patient_details>" + PatientUtilities.getPatientDetailsXML( Integer.toString(PatientUtilities.getPatientKey(Integer.parseInt(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID")))), authToken)
                + "</patient_details>" );
        }

        strXML.append( "</biospecimen>" );
        return strXML.toString();
    } 
    
    private boolean isNumeric(String s){

        String validChars = "0123456789";
        boolean isNumber = true;
        for (int i = 0; i < s.length() && isNumber; i++){
            char c = s.charAt(i);
            if (validChars.indexOf(c) == -1){
                return false;
            }
        }
        return true;
    }    
    
}
