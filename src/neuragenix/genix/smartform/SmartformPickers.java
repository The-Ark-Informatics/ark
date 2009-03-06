/*

 * SmartformPickers.java

 *

 * Created on January 13, 2005, 9:25 AM

 */



package neuragenix.genix.smartform;



// sun packages
import java.util.Vector;

import java.util.Enumeration;
 
import java.sql.*;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;


// neuragenix packages
import neuragenix.dao.*;
import neuragenix.common.*;


/**

 *

 * @author  Seena Parappat

 */

public class SmartformPickers 

{   

    // Implementaion of the diagnosis picker
    public static String doDiagnosis(ChannelRuntimeData rdData){
         
        StringBuffer strXML = new StringBuffer();

        try {
                                            
                //implement the diagnosis picker
                Vector formfields = DatabaseSchema.getFormFields("csmartform_view_diagnosis");

                // set the current parameter in order to return to the smartform results page
                // on cancel or submit from the diagnosis page                       
                rdData.setParameter( "current", "smartform_result_view" );

                // Obtain the list of diagnosis
                DALQuery query = new DALQuery();
                
                query.setDomain( "DIAGNOSIS", null, null, null );
                query.setFields( formfields, null );
                query.setWhere( null, 0, "DIAGNOSIS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setDistinctField( "DIAGNOSIS_intDiagnosisKey" );
                
                ResultSet rs = query.executeSelect();
                
                // Build the list of diagnosis
                strXML.append( QueryChannel.buildSearchXMLFile( "diagnosis", rs, formfields ));
                strXML.append( "<diagnosisCount>" + QueryChannel.getMaxKeyAsString("DIAGNOSIS_intDiagnosisKey")
                    + "</diagnosisCount>" );
                strXML.append( "<currentField>" + rdData.getParameter( "currentField" )
                    + "</currentField>" );
                                

                Enumeration myEnum = rdData.keys();
                
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" +  param + "</name><value>" +  Utilities.cleanForXSL(rdData.getParameter( param )) +
                        "</value></parameter>" );
                    
                }
                               
                // clean up
                query = null;
                rs = null;
                
                return "<diagnosis_picker>" + strXML.toString() + "</diagnosis_picker>";
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        return "<diagnosis_picker>" + strXML.toString() + "</diagnosis_picker>";
        
        
        
    }
    
    
    // Implementaion of the ICD codes picker
    public static String doICDPicker (ChannelRuntimeData rdData){
        
        StringBuffer strXML = new StringBuffer();

        try {
                //implement the diagnosis picker
                Vector formfields = DatabaseSchema.getFormFields("csmartform_view_icdcodes");

                // set the current parameter in order to return to the smartform results page
                // on cancel or submit from the diagnosis page                       
                rdData.setParameter( "current", "smartform_result_view" );

                // Obtain the list of ICD codes                           
                DALQuery query = new DALQuery();
                
                query.setDomain( "ICDCODES", null, null, null );
                query.setFields( formfields, null );
                query.setWhere( null, 0, "ICDCODES_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setDistinctField( "ICDCODES_intICDCodeKey" );
                
                ResultSet rs = query.executeSelect();
                
                // Build the list of ICD codes
                strXML.append( QueryChannel.buildSearchXMLFile( "icdcodes", rs, formfields ));
                strXML.append( "<icdCodesCount>" + QueryChannel.getMaxKeyAsString("ICDCODES_intICDCodeKey")
                    + "</icdCodesCount>" );
                strXML.append( "<currentField>" + rdData.getParameter( "currentField" )
                    + "</currentField>" );
                
                Enumeration myEnum = rdData.keys();
                
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" + param + "</name><value>" + Utilities.cleanForXSL(rdData.getParameter( param )) +
                        "</value></parameter>" );
                    
                }
                                                             
                // clean up
                query = null;
                rs = null;

                return "<icdcodes_picker>" + strXML.toString() + "</icdcodes_picker>";
               
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel (doICDPicker) - " + e.toString(), e);
        }
        return "<icdcodes_picker>" + strXML.toString() + "</icdcodes_picker>";
        
    }
    
    // Implementaion of the additional diagnosis picker
    public static String doSDiagnosis(ChannelRuntimeData rdData){
        
        StringBuffer strXML = new StringBuffer();
        try {
                                         
                //implement the diagnosis picker
                Vector formfields = DatabaseSchema.getFormFields("csmartform_view_sdiagnosis");

                // set the current parameter in order to return to the smartform results page
                // on cancel or submit from the diagnosis page                       
                rdData.setParameter( "current", "smartform_result_view" );

                // Obtain the list of additional diagnosis
                DALQuery query = new DALQuery();
                
                query.setDomain( "SDIAGNOSIS", null, null, null );
                query.setFields( formfields, null );
                query.setWhere( null, 0, "SDIAGNOSIS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setDistinctField( "SDIAGNOSIS_intDiagnosisKey" );
                
                ResultSet rs = query.executeSelect();
                
                // Build the additional diagnosis
                strXML.append( QueryChannel.buildSearchXMLFile( "diagnosis", rs, formfields ));
                strXML.append( "<diagnosisCount>" + QueryChannel.getMaxKeyAsString( "SDIAGNOSIS_intDiagnosisKey" )
                    + "</diagnosisCount>" );
                strXML.append( "<currentField>" + rdData.getParameter( "currentField" )
                    + "</currentField>" );
                               
                Enumeration myEnum = rdData.keys();
                
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" + param + "</name><value>" + rdData.getParameter( param ) +
                        "</value></parameter>" );
                    
                }
                               
                // clean up
                query = null;
                rs = null;

                return "<sdiagnosis_picker>" + strXML.toString() + "</sdiagnosis_picker>";
           
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        return "<sdiagnosis_picker>" + strXML.toString() + "</sdiagnosis_picker>";
        
    }

    
    // Implementaion of the ECG codes picker
    public static String doECGPicker (ChannelRuntimeData rdData){
        
        StringBuffer strXML = new StringBuffer();

        try {
                //implement the diagnosis picker
                Vector formfields = DatabaseSchema.getFormFields("csmartform_view_ecgcodes");

                // set the current parameter in order to return to the smartform results page
                // on cancel or submit from the diagnosis page                       
                rdData.setParameter( "current", "smartform_result_view" );

                // Obtain the list of ECG codes                           
                DALQuery query = new DALQuery();
                
                query.setDomain( "ECGCODES", null, null, null );
                query.setFields( formfields, null );
                query.setWhere( null, 0, "ECGCODES_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setDistinctField( "ECGCODES_intECGCodeKey" );
                
                ResultSet rs = query.executeSelect();
                
                // Build the list of ECG codes
                strXML.append( QueryChannel.buildSearchXMLFile( "ecgcodes", rs, formfields ));
                strXML.append( "<ecgCodesCount>" + QueryChannel.getMaxKeyAsString( "ECGCODES_intECGCodeKey" )
                    + "</ecgCodesCount>" );
                strXML.append( "<currentField>" + rdData.getParameter( "currentField" )
                    + "</currentField>" );
                
                Enumeration myEnum = rdData.keys();
                
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" + param + "</name><value>" + rdData.getParameter( param ) +
                        "</value></parameter>" );
                    
                }
                                                             
                // clean up
                query = null;
                rs = null;

                return "<ecgcodes_picker>" + strXML.toString() + "</ecgcodes_picker>";
               
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel (doECGPicker) - " + e.toString(), e);
        }
        return "<ecgcodes_picker>" + strXML.toString() + "</ecgcodes_picker>";
        
    }

    // Implementaion of the Syndromes picker
    public static String doSyndromes(ChannelRuntimeData rdData){
        
        StringBuffer strXML = new StringBuffer();

        try {
                //implement the diagnosis picker
                Vector formfields = DatabaseSchema.getFormFields("csmartform_view_syndromes");

                // set the current parameter in order to return to the smartform results page
                // on cancel or submit from the diagnosis page                       
                rdData.setParameter( "current", "smartform_result_view" );

                // Obtain the list of syndromes                           
                DALQuery query = new DALQuery();
                
                query.setDomain( "SYNDROMES", null, null, null );
                query.setFields( formfields, null );
                query.setWhere( null, 0, "SYNDROMES_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setDistinctField( "SYNDROMES_intSyndromesKey" );
                
                ResultSet rs = query.executeSelect();
                
                // Build the list of syndromes                
                strXML.append( QueryChannel.buildSearchXMLFile( "syndrome", rs, formfields ));
                strXML.append( "<syndromeCount>" + QueryChannel.getMaxKeyAsString( "SYNDROMES_intSyndromesKey" )
                    + "</syndromeCount>" );
                strXML.append( "<currentField>" + rdData.getParameter( "currentField" )
                    + "</currentField>" );
                
                
                Enumeration myEnum = rdData.keys();
                
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" + param + "</name><value>" + rdData.getParameter( param ) +
                        "</value></parameter>" );
                    
                }
                
                // clean up
                query = null;
                rs = null;

                return "<syndromes_picker>" + strXML.toString() + "</syndromes_picker>";
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        return "<syndromes_picker>" + strXML.toString() + "</syndromes_picker>";
        
    }
    
}
