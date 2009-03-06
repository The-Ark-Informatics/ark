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

public class BatchSFResultsManager {
    
    private AuthToken authToken = null;    
    private IPerson ip = null;  
    private NGXRuntimeProperties rp = null;
    private SmartformBatchGenerationInfo smartformBatchGenerationInfo = null;
    
    private ChannelRuntimeData runtimeData;
    
    private static final String GENERATION_MODE = "generation_mode";
    private static final String SPECIMEN_SELECTION = "specimen_selection";
    private static final String GENERATE_SF_RESULTS = "GenerateSFResults";
    private static final String SELECT_SF_FIELDS = "SelectSFFields";
    private static final String SUBMIT_CLONEABLE_FIELDS = "submit_cloneable_fields";
    private static final String SUBMIT_CLONEABLE_FIELDS_DATA = "submit_cloneable_fields_data";
    private static final String SUBMIT_STANDALONE_FIELDS_DATA = "submit_standalone_fields_data";
    private static final String UPDATE_FORM_LINK_DATA = "update_form_link_data";    
    public  static final String PERMISSION_BATCH_SFRESULTS = "generate_batch_sfresults";
    private static final String XSL_CREATION_TYPE = "creation_type";
    private static final String XSL_SPECIMEN_SELECTION = "specimen_selection";
    private static final String XSL_GENERATION_REPORT = "generation_report"; 
    private static final String XSL_SELECT_FIELDS = "select_sf_fields";
    private static final String XSL_CLONE_FIELDS_DATA = "clone_fields_data";
    private static final String XSL_STANDALONE_FIELDS_DATA = "standalone_fields_data";    
    private static final String ERROR_INVALID_ID = "Invalid Biospecimen ID selected.";
    private Vector vtSmartformDE = new Vector ();
    private Hashtable hashCloneFieldData = new Hashtable ();
    private Hashtable hashDEType = new Hashtable ();
    private Hashtable hashCloneFilesSaved = new Hashtable ();
    private Hashtable hashStandaloneFilesSaved = new Hashtable ();
    private Vector vtStandaloneData = new Vector ();
    private String strBackLocation = "";
    private String strFromBiospecimenKey = "";
    
    /** Creates a new instance of BatchCloneManager */
    public BatchSFResultsManager(AuthToken authToken) 
    {
        this.authToken = authToken;
    }
    
    public BatchSFResultsManager(NGXRuntimeProperties rp)
    {
        this.rp = rp;
        this.authToken = rp.getAuthToken();
        this.ip = rp.getIPerson();
    }
   
    public NGXRuntimeProperties processRuntimeData(ChannelRuntimeData rd)
    {
        this.runtimeData = rd; 
        rp.clearXML();
        rp.addXML("<body>");
                           
        // Monitor the actions requested in runtimeData
        if (runtimeData.getParameter("action").equals(GENERATION_MODE))
        {
            if (runtimeData.getParameter("Next") != null)
            {
                // create a new smartformBatchGenerationInfo object
                smartformBatchGenerationInfo = new SmartformBatchGenerationInfo ();
                smartformBatchGenerationInfo.setAuthToken(this.authToken);
                // Reset all private data
                vtSmartformDE.clear();
                hashCloneFieldData.clear();
                hashCloneFilesSaved.clear();
                hashStandaloneFilesSaved.clear();
                vtStandaloneData.clear();
                hashDEType.clear();
                
                setGenerationMode ();
                buildSelectSpecimenFormXML ();
                rp.addXML("<fromBiospecimenKey>" + strFromBiospecimenKey + "</fromBiospecimenKey>");
            }  
            else
            {
                // Start with this page - to select the creation type
                rp.addXML("<BatchResultSelection>SingleToMultiple</BatchResultSelection>");
                rp.setStylesheet(XSL_CREATION_TYPE);
            }    
        } 
        // Add specimen to the current list of specimens for which to generate smartform data
        else if (runtimeData.getParameter("action").equals(SPECIMEN_SELECTION))
        {
            if (runtimeData.getParameter("Save") != null)
            {
                doSelectSmartformForGeneration();
                doAddSpecimenForGeneration();                
            }  
            else if (runtimeData.getParameter("selectSmartform") != null)
            {
                doSelectSmartformForGeneration();
            }    
            else if (runtimeData.getParameter("Remove") != null)
            {
                doRemoveSpecimenForGeneration();                
            }                
            
            buildSelectSpecimenFormXML();
            rp.addXML("<fromBiospecimenKey>" + strFromBiospecimenKey + "</fromBiospecimenKey>");
        }
        else if (runtimeData.getParameter("action").equals(GENERATE_SF_RESULTS))
        {
            
            // If successful generation
            if (SmartformBatchGenerator.doGeneration(smartformBatchGenerationInfo))
            {
                // build success report
                buildGenerationReportXML(true); 
            }
            else
            {
                // build error generation report
                buildGenerationReportXML(false); 
            }

            // Reset all data
            smartformBatchGenerationInfo = null;
            // Reset all private data
            vtSmartformDE.clear();
            hashCloneFieldData.clear();
            hashCloneFilesSaved.clear();
            hashStandaloneFilesSaved.clear();            
            vtStandaloneData.clear(); 
            hashDEType.clear();
            
        }   
        else if (runtimeData.getParameter("action").equals(SELECT_SF_FIELDS))
        {
            if (runtimeData.getParameter("Next") != null)
            {
                setCloneDataFields();
            }
            else
            {
                buildSFFieldListXML(smartformBatchGenerationInfo.getSmartformTemplate());
            }   
        }     
        else if (runtimeData.getParameter("action").equals(SUBMIT_CLONEABLE_FIELDS_DATA))
        {
            if (runtimeData.getParameter("Next") != null)
            {
                updateCloneDataFields();
                buildStandaloneFieldsDataXML();
            }
            else if (runtimeData.getParameter("UploadAttachment") != null || runtimeData.getParameter("DeleteAttachment") != null)
            {
                updateCloneDataFields();
                buildCloneFieldsDataXML ();
            }
            // Previous button pressed from standalone field data entry
            else if (runtimeData.getParameter("Back") != null)
            {
                updateStandaloneDataFields();
                buildCloneFieldsDataXML();
            }
            else
            {
                buildCloneFieldsDataXML ();
            }   
        }  
        else if (runtimeData.getParameter("action").equals(SUBMIT_STANDALONE_FIELDS_DATA))
        {
            if (runtimeData.getParameter("Next") != null)
            {
                updateStandaloneDataFields();
                // Set the clone field and standalone fields data
                smartformBatchGenerationInfo.setCloneFieldData(hashCloneFieldData);
                smartformBatchGenerationInfo.setStandaloneFieldData(vtStandaloneData);
                
                // If successful generation
                if (SmartformBatchGenerator.doGeneration(smartformBatchGenerationInfo))
                {
                    // build success report
                    buildGenerationReportXML(true); 
                }
                else
                {
                    // build error generation report
                    buildGenerationReportXML(false); 
                } 
                
                // Reset all data
                smartformBatchGenerationInfo = null;
                // Reset all private data
                vtSmartformDE.clear();
                hashCloneFieldData.clear();
                hashCloneFilesSaved.clear();
                hashStandaloneFilesSaved.clear();                
                vtStandaloneData.clear(); 
                hashDEType.clear();
                
            }
            else if (runtimeData.getParameter("UploadAttachment") != null || runtimeData.getParameter("DeleteAttachment") != null)
            {
                updateStandaloneDataFields();
                buildStandaloneFieldsDataXML ();
            }            
            else
            {
                buildStandaloneFieldsDataXML ();
            }   
        }
        else if (runtimeData.getParameter("action").equals(UPDATE_FORM_LINK_DATA))
        {
            if (runtimeData.getParameter("cloneDataFormLink") != null)
            {
                updateCloneDataFields();
                buildCloneFieldsDataXML ();
            }
            else if (runtimeData.getParameter("standaloneDataFormLink") != null)
            {
                updateStandaloneDataFields();
                buildStandaloneFieldsDataXML ();
            }        
        }
        else
        {
            // Start with this page - to select the creation type
            // Record the Biospecimen key that we came from
            if (runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") != null)
            {
                strFromBiospecimenKey = runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
                strBackLocation = "?module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID=" + runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
            }    
            else
            {
                strFromBiospecimenKey = "";
                strBackLocation = "?current=biospecimen_search";
            }    
            
            rp.addXML("<BatchResultSelection>SingleToMultiple</BatchResultSelection>");
            rp.setStylesheet(XSL_CREATION_TYPE);
        }    
        
        rp.addXML("<backLocation>" + strBackLocation + "</backLocation>");
        rp.addXML("</body>");
        
        // Set the BatchGenerationInfo object as an IPerson attribute so that be accessed 
        // in the smartform channel
        if (smartformBatchGenerationInfo != null)
        {    
            ip.setAttribute("SFBatchGenInfo", smartformBatchGenerationInfo);
        }

        return rp;
    }

    // Sets the selected generation mode to the SmartformBatchGenerationInfo object
    private void setGenerationMode ()
    {
        if (runtimeData.getParameter("rdBatchResultsSelect") != null)
        {    
            if (runtimeData.getParameter("rdBatchResultsSelect").equals("SingleToMultiple"))
            {
                smartformBatchGenerationInfo.setGenerationMode(smartformBatchGenerationInfo.GEN_MODE_MULTIPARTICIPANT);
            }    
            else if (runtimeData.getParameter("rdBatchResultsSelect").equals("MultipleToSingle"))
            {
                smartformBatchGenerationInfo.setGenerationMode(smartformBatchGenerationInfo.GEN_MODE_SINGLEPARTICIPANT);
            }
        }
    }
    
    // Set the selected smartform for batch results generation 
    private void doSelectSmartformForGeneration ()
    {
        if (runtimeData.getParameter("Smartform") != null)
        {           
            int intSmartformKey = Integer.parseInt(runtimeData.getParameter("Smartform").toString());
            smartformBatchGenerationInfo.setSmartformTemplate(intSmartformKey);  
            smartformBatchGenerationInfo.setDomain("Bioanalysis"); 
                        
            // Reset the standalone and clone field data
            smartformBatchGenerationInfo.clearCloneFields();
            smartformBatchGenerationInfo.clearStandaloneFields();
            vtSmartformDE.clear();
            hashCloneFieldData.clear();
            hashCloneFilesSaved.clear();
            hashStandaloneFilesSaved.clear();            
            vtStandaloneData.clear();
            hashDEType.clear();
        }    
    }    
    
    // Gets the user selected ID and if it exists in the database add as a participant to 
    // do the generation for
    private void doAddSpecimenForGeneration ()
    {
        try
        {
            if (runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID") != null)
            {            
                DALQuery query = new DALQuery ();
                query.setDomain("BIOSPECIMEN", null, null, null);
                query.setField ("BIOSPECIMEN_intBiospecimenID", null);
                query.setWhere(null, 0, "BIOSPECIMEN_strBiospecimenID", "=", runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID"), 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rs = query.executeSelect();

                // If the record exists, add the key as a particpant in the SmartformBatchGenerationInfo object
                if (rs.next())
                {
                    Vector vtCurrentSpecimens = smartformBatchGenerationInfo.getParticipants();
                    String strNewParticipant = rs.getString("BIOSPECIMEN_intBiospecimenID");
                    // Add as a participant only if it has not already been added as a praticipant
                    if (!vtCurrentSpecimens.contains(strNewParticipant))
                    {    
                        smartformBatchGenerationInfo.addParticipant (strNewParticipant);
                    }    
                }            
                else
                {
                    rp.addXML("<ErrorMsg>" + ERROR_INVALID_ID + "</ErrorMsg>");
                }
                
                // Close the result set
                rs.close();
            }
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
    }
    

    // Gets the user selected ID and if it exists in the database add as a participant to 
    // do the generation for
    private void doRemoveSpecimenForGeneration ()
    {
        if (runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") != null)
        {            
            Vector vtCurrentSpecimens = smartformBatchGenerationInfo.getParticipants();
            String strBiospecimenKey = runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
            // Add as a participant only if it has not already been added as a praticipant
            if (vtCurrentSpecimens.contains(strBiospecimenKey))
            {    
                smartformBatchGenerationInfo.removeParticipant (strBiospecimenKey);
            }    
        }
    }
    
    private void buildSelectSpecimenFormXML ()    
    {
        // Display the list of smartforms to select from
        rp.addXML(getListofSmartformsXML ("Bioanalysis"));
        
        // Display the list of specimens already selected for batch creation of smartform results
        rp.addXML(getListofSelectedSpecimensXML());
        
        // Add the selected generation mode
        if (smartformBatchGenerationInfo.getGenerationMode() == smartformBatchGenerationInfo.GEN_MODE_SINGLEPARTICIPANT)
        {    
            rp.addXML("<singlePartGeneration>true</singlePartGeneration>");
            // If one participant has already been added do not add the addition of more participants
            if (smartformBatchGenerationInfo.getParticipants().size() > 0)
            {
                rp.addXML("<DisableAddParticipant>true</DisableAddParticipant>");
            }    
        }
        
        // Set the stylesheet
        rp.setStylesheet(XSL_SPECIMEN_SELECTION);
    }
    
    
    // Creates an XML string of the list of smartforms
    private String getListofSmartformsXML (String strDomain)    
    {
        String strSmartformList = "";
        Vector vtSmartformFields = DatabaseSchema.getFormFields("csmartform_smartform_view");
        
        // Build the selected smartform ID and Name
        if (smartformBatchGenerationInfo.getSmartformTemplate() != 0)
        {
            rp.addXML("<selectedSmartformID>" + Integer.toString(smartformBatchGenerationInfo.getSmartformTemplate()) + "</selectedSmartformID>");                 
            rp.addXML("<selectedSmartformName>" + getSmartformName(smartformBatchGenerationInfo.getSmartformTemplate()) + "</selectedSmartformName>");                 
        }    
        
        try
        {
            DALQuery query = new DALQuery ();
            query.setDomain("SMARTFORM", null, null, null);  
            query.setFields (vtSmartformFields, null);
            query.setWhere( null, 0, "SMARTFORM_strDomain", "=", strDomain, 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            ResultSet rs = query.executeSelect ();
            strSmartformList = QueryChannel.buildSearchXMLFile("smartform_list", rs, vtSmartformFields);
            
            // Close the result set
            rs.close();
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        return strSmartformList;
        
    }
    

    // Creates an XML string of the list of smartforms
    private String getListofSelectedSpecimensXML ()    
    {
        String strBiospecimenList = "";
        
        try
        {
            Vector vtSpecimens = smartformBatchGenerationInfo.getParticipants();
            
            // If we have any participants
            if (vtSpecimens.size() > 0)
            {    
                Vector vtFields = new Vector ();
                vtFields.add ("BIOSPECIMEN_intBiospecimenID");
                vtFields.add ("BIOSPECIMEN_strBiospecimenID");
                vtFields.add ("BIOSPECIMEN_strSampleType");
                vtFields.add ("BIOSPECIMEN_strSampleSubType");

                // Display the list of Biospecimen IDs already selected
                // Get list of biospecimens already added
                String strSelectedBiospecimens = "(";
                for (int i=0; i<vtSpecimens.size(); i++)
                {
                    strSelectedBiospecimens += vtSpecimens.get(i).toString();
                    // Don't put comma for last element in the list
                    if (i != (vtSpecimens.size()-1))
                    {
                        strSelectedBiospecimens += ", ";
                    }    
                } 
                strSelectedBiospecimens += ")";            

                // Get the required details for display from the list of selected biospecimens
                DALQuery query = new DALQuery ();
                query.setDomain("BIOSPECIMEN", null, null, null);  
                query.setFields (vtFields, null);
                query.setWhere( null, 0, "BIOSPECIMEN_intBiospecimenID", "IN", strSelectedBiospecimens, 0, DALQuery.WHERE_HAS_VALUE );
                ResultSet rs = query.executeSelect ();
                strBiospecimenList = QueryChannel.buildSearchXMLFile("biospecimen_list", rs, vtFields);
                
                // Close the result set
                rs.close();
            }
            
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        return strBiospecimenList;
        
    }
    
    /**
     * <p>Does ...</p>
     * 
     * 
     * @return 
     */
    private void buildGenerationReportXML(boolean blSuccess) 
    {
        String strReportMessage = "";
        String strParticipants = "";
        
        // Get the list of participants
        Vector vtParticipants = smartformBatchGenerationInfo.getParticipants();
        for (int i=0; i<vtParticipants.size(); i++)
        {
           strParticipants += "<participant>" + getParticipantID(vtParticipants.get(i).toString()) + "</participant>";
        }    
        
        if (blSuccess == true)
        {
            strReportMessage = "<ReportMessage>" + "The Smartform " + getSmartformName(smartformBatchGenerationInfo.getSmartformTemplate()) 
                               + " has been successfully added ";
            
            if (smartformBatchGenerationInfo.getGenerationMode() == SmartformBatchGenerationInfo.GEN_MODE_SINGLEPARTICIPANT)
            {    
                strReportMessage += Integer.toString(smartformBatchGenerationInfo.getNumbertoClone()) + " times ";
            }
            strReportMessage += "to the following" +"</ReportMessage>" + "<numSpecimens>" + Integer.toString (vtParticipants.size()) + "</numSpecimens>";
        }    
        else
        {
            strReportMessage = "<ReportMessage>" + "Error adding the Smartform " + getSmartformName(smartformBatchGenerationInfo.getSmartformTemplate()) + " ";
            if (smartformBatchGenerationInfo.getGenerationMode() == SmartformBatchGenerationInfo.GEN_MODE_SINGLEPARTICIPANT)
            {    
                strReportMessage += Integer.toString(smartformBatchGenerationInfo.getNumbertoClone()) + " times ";
            }
            strReportMessage += "to the following" +"</ReportMessage>" + "<error>" + "Error"+ "</error>" +  "<numSpecimens>" + "1" + "</numSpecimens>";
        }    
        
        
        // your code here
        rp.addXML(strReportMessage + strParticipants);
        rp.setStylesheet(XSL_GENERATION_REPORT);
    }
    
    
    // Build the smartform field list given the smartform key
    private void buildSFFieldListXML (int intSmartformKey) 
    {
        String strFieldList = "";
        
        try
        {
            vtSmartformDE.clear();
            DALQuery query = new DALQuery();    
            query.setDomain("SMARTFORM", null, null, null);
            query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
            query.setDomain("DATAELEMENTS","SMARTFORMTODATAELEMENTS_intDataElementID","DATAELEMENTS_intDataElementID","INNER JOIN");
            query.setField("DATAELEMENTS_intDataElementID", null);
            query.setField("DATAELEMENTS_strDataElementName", null);  
            query.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder", null);
            query.setWhere(null, 0, "SMARTFORM_intSmartformID", "=", Integer.toString(intSmartformKey), 0, DALQuery.WHERE_HAS_VALUE);            
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "<>", "PAGEBREAK", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");            

            ResultSet rs = query.executeSelect();
            
            while (rs.next())
            {
                strFieldList += "<Field>";
                strFieldList += "<DEID>" + rs.getString("DATAELEMENTS_intDataElementID") + "</DEID>";
                // Keep track of the dataelements for this smartform
                vtSmartformDE.add(rs.getString("DATAELEMENTS_intDataElementID"));
                strFieldList += "<DEName>" + rs.getString("DATAELEMENTS_strDataElementName") + "</DEName>";
                strFieldList += "</Field>";
            } 
            
            // Close the result set
            rs.close();            
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        rp.addXML(strFieldList);
        rp.setStylesheet(XSL_SELECT_FIELDS);
    }
    
    // Set the fields selected to be cloned 
    private void setCloneDataFields ()
    {
        
        try
        {
            hashCloneFieldData = new Hashtable ();
            vtStandaloneData = new Vector ();
            Hashtable hashStandaloneFieldData = new Hashtable ();
            
            for (int i=vtSmartformDE.size()-1; i>=0; i--)
            {
                if (runtimeData.getParameter(vtSmartformDE.get(i).toString()) != null)
                {
                    smartformBatchGenerationInfo.addCloneField((vtSmartformDE.get(i).toString()));
                    hashCloneFieldData.put (vtSmartformDE.get(i).toString(), "");
                }
                else
                {
                    smartformBatchGenerationInfo.addStandaloneField((vtSmartformDE.get(i).toString()));
                    hashStandaloneFieldData.put (vtSmartformDE.get(i).toString(), "");                    
                }    
                
                // for each DE get the type
                Hashtable hashDEDetail = getDEDetails (vtSmartformDE.get(i).toString());
                hashDEType.put (vtSmartformDE.get(i).toString(), hashDEDetail.get("DATAELEMENTS_intDataElementType"));
               
            }  

            // Set the number of clones to generate
            if (runtimeData.getParameter("NumberToClone") != null)
            {
                int intNumClones = Integer.parseInt(runtimeData.getParameter("NumberToClone"));
                smartformBatchGenerationInfo.setNumbertoClone (intNumClones);
                // build a vector of hashtable entries for each SF to be generated
                for (int i=0; i<intNumClones; i++)
                {
                    Hashtable htData = (Hashtable) hashStandaloneFieldData.clone();
                    vtStandaloneData.add (htData);
                }    
            }
            
            buildCloneFieldsDataXML();
            
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
                
    }
    
    // Builds the form where the user entered data to be cloned in the generated smartforms
    private void buildCloneFieldsDataXML ()
    {
        String strCloneFieldData = "";
                
        // Loop through the vector so that the clone fields are built in the correct order
        for (int i=0; i<vtSmartformDE.size(); i++)            
        {
            String strDEID = vtSmartformDE.get(i).toString();
            if (hashCloneFieldData.containsKey(strDEID))
            {           
                String strDEResult = hashCloneFieldData.get(strDEID).toString();
                Hashtable htDEDetails = getDEDetails (strDEID);

                strCloneFieldData += "<CloneField>";
                strCloneFieldData += "<ID>" + strDEID + "</ID>";
                strCloneFieldData += "<Name>" + htDEDetails.get("DATAELEMENTS_strDataElementName") + "</Name>";
                strCloneFieldData += "<Type>" + htDEDetails.get("DATAELEMENTS_intDataElementType") + "</Type>";
                if (strDEResult != null && !strDEResult.equals(""))
                {    
                    strCloneFieldData += "<Result>" + Utilities.cleanForXSL(strDEResult) + "</Result>";
                }  

                // get additional info to build the dataelement based on its type
                if (htDEDetails.get("DATAELEMENTS_intDataElementType").equals("DROPDOWN") || htDEDetails.get("DATAELEMENTS_intDataElementType").equals("DATE"))
                {    
                    strCloneFieldData += buildDataElementType (strDEID, htDEDetails.get("DATAELEMENTS_intDataElementType").toString(), strDEResult);
                }


                if (htDEDetails.get("DATAELEMENTS_intDataElementType").equals("FORM LINK")) 
                {
                    strCloneFieldData += "<DATAELEMENTS_strChannelFunctionalName>" + htDEDetails.get("DATAELEMENTS_strChannelFunctionalName") + "</DATAELEMENTS_strChannelFunctionalName>";
                    strCloneFieldData += "<DATAELEMENTS_strPageName>" + htDEDetails.get("DATAELEMENTS_strPageName") + "</DATAELEMENTS_strPageName>";                
                }

                if (htDEDetails.get("DATAELEMENTS_intDataElementType").equals("ATTACHMENT")) 
                {
                    if (hashCloneFilesSaved.get(strDEID) != null)
                    {
                        strCloneFieldData += "<AttachedFileName>" + hashCloneFilesSaved.get(strDEID) + "</AttachedFileName>";                 
                    }    
                }    

                strCloneFieldData += "</CloneField>"; 
            }
        }                    

        rp.addXML(strCloneFieldData);
        rp.setStylesheet(XSL_CLONE_FIELDS_DATA);        
    }


    // Update the data fields to be cloned with the data entered by the user
    private void updateCloneDataFields ()
    {

        try
        {
            Enumeration enum = hashCloneFieldData.keys();        

            while (enum.hasMoreElements())
            {
                String strDEID = enum.nextElement().toString();

                if (hashDEType.get(strDEID).equals("DATE"))
                {
                    if (runtimeData.getParameter(strDEID + "_Year") != null
                            && runtimeData.getParameter(strDEID + "_Month") != null
                            && runtimeData.getParameter(strDEID + "_Day") != null )
                    {    
                        String strDate = runtimeData.getParameter(strDEID + "_Year").trim() + "-" +
                                          runtimeData.getParameter(strDEID + "_Month").trim() + "-" +
                                          runtimeData.getParameter(strDEID  + "_Day").trim();
                        hashCloneFieldData.put(strDEID,strDate);
                    }
                }
                else if (hashDEType.get(strDEID).equals("ATTACHMENT"))
                {
                    if (runtimeData.getParameter("UploadAttachment_" + strDEID) != null)
                    {    
                        if (runtimeData.getObjectParameter(strDEID) != null)
                        {    
                            // Location where the attachment will get saved
                            String attachmentLocation = PropertiesManager.getProperty("neuragenix.genix.smartform.AttachmentLocation");     

                            String strFileName = QueryChannel.saveUploadFile(strDEID, runtimeData, attachmentLocation);   

                            if (strFileName != null)
                            {    
                                hashCloneFieldData.put (strDEID, strFileName);
                            }
                            org.jasig.portal.MultipartDataSource fileSaved = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter(strDEID);
                            hashCloneFilesSaved.put (strDEID, fileSaved.getName() );
                        }
                    }
                    else if (runtimeData.getParameter("DeleteAttachment_" + strDEID) != null)
                    {
                        hashCloneFieldData.put (strDEID, "");
                        hashCloneFilesSaved.remove(strDEID);
                    }    
                }
                else
                {    
                    if (runtimeData.getParameter(strDEID) != null)
                    {    
                        hashCloneFieldData.put (strDEID, runtimeData.getParameter(strDEID));
                    }

                    if (runtimeData.getParameter("cloneDataFormLink") != null)
                    {    
                        if (runtimeData.getParameter("d" + strDEID) != null)
                        {    
                            hashCloneFieldData.put (strDEID, runtimeData.getParameter("d" + strDEID));
                        }
                    }
                }
            }   
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
                
    }
    
    // Builds the form where the user entered data to be cloned in the generated smartforms
    private void buildStandaloneFieldsDataXML ()
    {
        String strStandaloneFieldData = "";
        Hashtable hashStandaloneData;
        Vector vtParticipants = smartformBatchGenerationInfo.getParticipants();
        
        for (int i=0; i<vtStandaloneData.size(); i++)
        {    
            strStandaloneFieldData += "<SF>";
            strStandaloneFieldData += "<SFNum>" + i + "</SFNum>";
            strStandaloneFieldData += "<ParticipantID>" + getParticipantID(vtParticipants.get(0).toString()) + "</ParticipantID>";
            hashStandaloneData = (Hashtable) vtStandaloneData.get(i);
            for (int j=0; j<vtSmartformDE.size(); j++)
            {
                String strDEID = vtSmartformDE.get(j).toString();
                if (hashStandaloneData.containsKey(strDEID))
                {
                    String strDEResult = hashStandaloneData.get(strDEID).toString();
                    Hashtable htDEDetails = getDEDetails (strDEID);

                    strStandaloneFieldData += "<StandaloneField>";
                    strStandaloneFieldData += "<ID>" + strDEID + "</ID>";
                    strStandaloneFieldData += "<Name>" + htDEDetails.get("DATAELEMENTS_strDataElementName") + "</Name>";
                    strStandaloneFieldData += "<Type>" + htDEDetails.get("DATAELEMENTS_intDataElementType") + "</Type>";

                    if (strDEResult != null && !strDEResult.equals(""))
                    {    
                        strStandaloneFieldData += "<Result>" + Utilities.cleanForXSL(strDEResult) + "</Result>";
                    }  

                    // get additional info to build the dataelement based on its type
                    if (htDEDetails.get("DATAELEMENTS_intDataElementType").equals("DROPDOWN") || htDEDetails.get("DATAELEMENTS_intDataElementType").equals("DATE"))
                    {    
                        strStandaloneFieldData += buildDataElementType (strDEID, htDEDetails.get("DATAELEMENTS_intDataElementType").toString(), strDEResult);
                    }

                    if (htDEDetails.get("DATAELEMENTS_intDataElementType").equals("FORM LINK")) 
                    {
                        strStandaloneFieldData += "<DATAELEMENTS_strChannelFunctionalName>" + htDEDetails.get("DATAELEMENTS_strChannelFunctionalName") + "</DATAELEMENTS_strChannelFunctionalName>";
                        strStandaloneFieldData += "<DATAELEMENTS_strPageName>" + htDEDetails.get("DATAELEMENTS_strPageName") + "</DATAELEMENTS_strPageName>";                
                    }

                    if (htDEDetails.get("DATAELEMENTS_intDataElementType").equals("ATTACHMENT")) 
                    {
                        if (hashStandaloneFilesSaved.get(Integer.toString(i) + "_" + strDEID) != null)
                        {
                            strStandaloneFieldData += "<AttachedFileName>" + hashStandaloneFilesSaved.get(Integer.toString(i) + "_" + strDEID) + "</AttachedFileName>";                 
                        }    
                    }    

                    strStandaloneFieldData += "</StandaloneField>";
                }

            }
            strStandaloneFieldData += "</SF>";
        }
            
        rp.addXML(strStandaloneFieldData);
        rp.setStylesheet(XSL_STANDALONE_FIELDS_DATA);        
    }
        
    // Update the standalone data fields with the data entered by the user
    private void updateStandaloneDataFields ()
    {

        try{
            for (int i=0; i<vtStandaloneData.size(); i++)
            {
                Hashtable hashStandaloneData = (Hashtable) vtStandaloneData.get(i);            
                Enumeration enum = hashStandaloneData.keys();

                while (enum.hasMoreElements())
                {
                    String strDEID = enum.nextElement().toString();

                    if (hashDEType.get(strDEID).equals("DATE"))
                    {
                        if (runtimeData.getParameter(Integer.toString(i) + "_" + strDEID + "_Year") != null
                            && runtimeData.getParameter(Integer.toString(i) + "_" + strDEID + "_Month") != null
                            && runtimeData.getParameter(Integer.toString(i) + "_" + strDEID + "_Day") != null )
                        {    
                            String strDate = runtimeData.getParameter(Integer.toString(i) + "_" + strDEID + "_Year").trim() + "-" +
                                              runtimeData.getParameter(Integer.toString(i) + "_" + strDEID + "_Month").trim() + "-" +
                                              runtimeData.getParameter(Integer.toString(i) + "_" + strDEID  + "_Day").trim();
                            hashStandaloneData.put(strDEID,strDate);
                        }
                    }
                    else if (hashDEType.get(strDEID).equals("ATTACHMENT"))
                    {
                        if (runtimeData.getParameter("UploadAttachment_" + Integer.toString(i) + "_" + strDEID) != null)
                        {    
                            if (runtimeData.getObjectParameter(Integer.toString(i) + "_" + strDEID) != null)
                            {    
                                // Location where the attachment will get saved
                                String attachmentLocation = PropertiesManager.getProperty("neuragenix.genix.smartform.AttachmentLocation");     

                                String strFileName = QueryChannel.saveUploadFile(Integer.toString(i) + "_" + strDEID, runtimeData, attachmentLocation);   

                                if (strFileName != null)
                                {    
                                    hashStandaloneData.put (strDEID, strFileName);
                                }
                                org.jasig.portal.MultipartDataSource fileSaved = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter(Integer.toString(i) + "_" + strDEID);
                                hashStandaloneFilesSaved.put (Integer.toString(i) + "_" + strDEID, fileSaved.getName());
                            }
                        }
                        else if (runtimeData.getParameter("DeleteAttachment_" + Integer.toString(i) + "_" + strDEID) != null)
                        {
                            hashStandaloneData.put (strDEID, "");
                            hashStandaloneFilesSaved.remove(Integer.toString(i) + "_" + strDEID);
                        }
                    }                
                    else
                    {    
                        if (runtimeData.getParameter(Integer.toString(i) + "_" + strDEID) != null)
                        {
                            hashStandaloneData.put (strDEID, runtimeData.getParameter(Integer.toString(i) + "_" + strDEID));
                        }  


                        // Handles Form Link types
                        if (runtimeData.getParameter("standaloneDataFormLink") != null)
                        {
                            if (runtimeData.getParameter("standaloneSFNum") != null)
                            {
                                if (i == Integer.parseInt(runtimeData.getParameter("standaloneSFNum")))
                                {
                                    if (runtimeData.getParameter("d" + strDEID) != null)
                                    {
                                        hashStandaloneData.put (strDEID, runtimeData.getParameter("d" + strDEID));
                                    }  
                                }    
                            }    
                        }    

                    }                                
                }    

            }    
        }   
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }                
    }
    
    
    /**
     * <p>Build additional Dataelement info based on the dataelement type</p>
     * 
     * 
     * @param String: Dataelement Key 
     * @param String: Dataelement Type
     * @param String: Dataelement Value
     */         
    private String buildDataElementType (String DEKey, String strType, String strValue)
    {
        String strTypeInfo = "";
        
        if (strType.equals("DROPDOWN"))
        {
            strTypeInfo += SmartformUtilities.buildDataElementOptions(DEKey, strValue);
        }    
        else if (strType.equals("DATE"))
        {
            strTypeInfo += SmartformUtilities.buildDataElementDate(Utilities.convertDateForDisplay(strValue));     
        }    
        
        return strTypeInfo;
    }
    
    /**
     * <p>Returns the Smartform Name</p>
     * 
     * 
     * @param int: Smartform Key 
     */
    private String getSmartformName (int intSmartformKey) 
    {
        String strSFName = "";
        
        try
        {
            DALQuery query = new DALQuery ();
            query.setDomain("SMARTFORM", null, null, null);
            query.setField ("SMARTFORM_strSmartformName", null);
            query.setWhere(null, 0, "SMARTFORM_intSmartformID", "=", Integer.toString(intSmartformKey), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();

            if (rs.next())
            {
                if (rs.getString("SMARTFORM_strSmartformName") != null)
                {    
                    strSFName = rs.getString("SMARTFORM_strSmartformName");
                }    
            } 
            
            // Close the result set
            rs.close();
            
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        return strSFName;
        
    }

    /**
     * <p>Returns the Participant ID</p>
     * 
     * 
     * @param String: participant Key 
     */    
    private String getParticipantID (String strParticipantKey) 
    {
        String strParticipantID = "";
        
        try
        {
            DALQuery query = new DALQuery ();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField ("BIOSPECIMEN_strBiospecimenID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strParticipantKey, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();

            if (rs.next())
            {
                if (rs.getString("BIOSPECIMEN_strBiospecimenID") != null)
                {    
                    strParticipantID = rs.getString("BIOSPECIMEN_strBiospecimenID");
                }    
            }  
            
            // Close the result set
            rs.close();            
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        return strParticipantID;
        
    }    
    
    /**
     * <p>Returns hashtable containing the dataelement Name and Type</p>
     * 
     * 
     * @param String: Dataelement key
     */  
    private Hashtable getDEDetails (String strDEKey) 
    {
        Hashtable htDEDetails = new Hashtable ();
        
        try
        {
            DALQuery query = new DALQuery ();
            query.setDomain("DATAELEMENTS", null, null, null);
            query.setField ("DATAELEMENTS_strDataElementName", null);
            query.setField ("DATAELEMENTS_intDataElementType", null);
            query.setField ("DATAELEMENTS_strChannelFunctionalName", null);
            query.setField ("DATAELEMENTS_strPageName", null);            
            query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", strDEKey, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();

            if (rs.next())
            {
                if (rs.getString("DATAELEMENTS_strDataElementName") != null)
                {    
                    htDEDetails.put("DATAELEMENTS_strDataElementName", rs.getString("DATAELEMENTS_strDataElementName"));
                }    
                
                if (rs.getString("DATAELEMENTS_intDataElementType") != null)
                {    
                    htDEDetails.put("DATAELEMENTS_intDataElementType", rs.getString("DATAELEMENTS_intDataElementType"));
                }    

                if (rs.getString("DATAELEMENTS_strChannelFunctionalName") != null)
                {    
                    htDEDetails.put("DATAELEMENTS_strChannelFunctionalName", rs.getString("DATAELEMENTS_strChannelFunctionalName"));
                }    
                
                if (rs.getString("DATAELEMENTS_strPageName") != null)
                {    
                    htDEDetails.put("DATAELEMENTS_strPageName", rs.getString("DATAELEMENTS_strPageName"));
                }    

            }       
            
            // Close the result set
            rs.close();            
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        return htDEDetails;
        
    }     
    
}
