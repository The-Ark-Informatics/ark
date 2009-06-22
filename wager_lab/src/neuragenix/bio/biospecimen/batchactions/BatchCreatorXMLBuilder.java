package neuragenix.bio.biospecimen.batchactions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import neuragenix.bio.biospecimen.BGUtilities;
import neuragenix.bio.biospecimen.BiospecimenCore;
import neuragenix.bio.biospecimen.GenerationDetails;
import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.bio.utilities.InventoryUtilities;
import neuragenix.bio.utilities.PatientUtilities;
import neuragenix.bio.utilities.StudyUtilities;
import neuragenix.common.QueryChannel;
import neuragenix.dao.DatabaseSchema;

import org.apache.axis.wsdl.toJava.GeneratedFileInfo;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.PropertiesManager;

/**This class will be in charge of all XML building for Batch Creator Module
 * 
 * @author ple
 *
 */
public class BatchCreatorXMLBuilder {
	BiospecimenCore bcore = null;
	
	//constructor, may use the BCore from the channel
	public BatchCreatorXMLBuilder(BiospecimenCore bcore){
		this.bcore = bcore;
	
	}
		

	
	
	public String buildGettingOptionsXML(int intParentBiospecimenKey, ChannelRuntimeData rd) throws Exception{
		StringBuffer strXML = new StringBuffer();
		
		//build XML for the parent details
		//strXML.append(bcore.getBiospecimenDetailsXML(intParentBiospecimenKey, rd));
		
		
		//get XML for the quantity form
		
		Vector formField = DatabaseSchema.getFormFields("cbiospecimen_batch_creation_option_form");
		//Tom changed the next line to make the first windown of batch create have value in type and treatment		
		if ( rd != null ) // && !"edit_full_details".equalsIgnoreCase(rd.getParameter("action")))
        {
			
            formField.remove("BIOSPECIMEN_strSampleSubType");
			strXML.append(QueryChannel.buildViewXMLFile(formField, rd));
			strXML.append(this.buildSubTypeXML(rd));
		}
		/* else if (rd!= null && "edit_full_details".equalsIgnoreCase(rd.getParameter("action"))){
			System.out.println("br2");
                        strXML.append(bcore.getBiospecimenDetailsXML(intParentBiospecimenKey, rd));
                 
		} */
		else 
		{
                        
			//strXML.append(bcore.getBiospecimenDetailsXML(intParentBiospecimenKey, rd));	
			strXML.append(DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_transaction"));
			strXML.append(QueryChannel.buildAddFormXMLFile(formField));
		}
		strXML.append(QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cbiospecimen_batch_creation_option_form")));
		//System.out.println(strXML.toString());
		
		
		return strXML.toString();
	}
	
	
	public String buildExistingGenerationsXML(List generationDetails){
		StringBuffer sbXML = new StringBuffer();
		Iterator iterator = generationDetails.iterator();
		int index = 0;
		while (iterator.hasNext()){
			
			GenerationDetails gdItem = (GenerationDetails)iterator.next();
                        //System.out.println("Index " + index);
			sbXML.append(gdItem.translateToXML(index));
			index++;
		}
		
		
		return sbXML.toString();
	}

	public String buildExistingGenerationsXMLWithIndividualIDs(List generationDetails, Map idMap, Map messages, boolean autoIDGeneration){
		StringBuffer sbXML = new StringBuffer();
		Iterator iterator = generationDetails.iterator();
		int index = 0;
		while (iterator.hasNext()){
			sbXML.append("<generation>");
			GenerationDetails gdItem = (GenerationDetails)iterator.next();
			sbXML.append(gdItem.translateToXML(index));
			for (int i=0; i < gdItem.getAllocationAmount(); i++){
				sbXML.append("<biospecimen autoIDGeneration=\""+autoIDGeneration+"\">");
				sbXML.append("<internalIndex>"+i+"</internalIndex>");
				sbXML.append("<id-key>"+"enteredID_"+index+"_"+i+"</id-key>");
				if (idMap != null && idMap.containsKey("enteredID_"+index+"_"+i)){
						
						sbXML.append("<id-value>"+idMap.get("enteredID_"+index+"_"+i)+"</id-value>");
				}
				
				if (messages != null && messages.containsKey("enteredID_"+index+"_"+i)){
					sbXML.append("<id-message>"+messages.get("enteredID_"+index+"_"+i)+"</id-message>");
				}
				
				sbXML.append("</biospecimen>");	
			}
			sbXML.append("</generation>");
			index++;
		}
		
		
		return sbXML.toString();
	}
	
	
	public String buildEditDetailsForm(BatchCreator bcreator, int internalGenerationID, ChannelRuntimeData rd) throws Exception{
		
		
		//get the appropriate generation details
		StringBuffer sbXML = new StringBuffer();
		
		sbXML.append("<GD_internalID>" + internalGenerationID +"</GD_internalID>");
		GenerationDetails gdItem = (GenerationDetails) bcreator.getGenerationGroups().get(internalGenerationID);
		
		//biospecimen details
		Vector formField = DatabaseSchema.getFormFields("cbiospecimen_save_biospecimen");
		
		//quantity transaction details formfields
		formField.addAll(DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_transaction"));
		
		
		
		if (rd == null){
			sbXML.append(QueryChannel.buildViewXMLFile(formField, gdItem.getRdDataToSave()));
		}
		else {
			//need to reresh
			formField.remove("BIOSPECIMEN_strSampleSubType");
			if (rd.getParameter("BIOSPECIMEN_strSampleType")!=null){
				sbXML.append(QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSampleSubType","" , rd.getParameter("BIOSPECIMEN_strSampleType")));
			}
			sbXML.append(QueryChannel.buildViewXMLFile(formField, rd));
		}
		//output the study details
		boolean blShowNonConsentedStudies = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showNonConsentedStudies");
		boolean blShowExpiredStudies = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showExpiredStudies");
		sbXML.append(StudyUtilities.getListOfStudiesXML(null, blShowExpiredStudies, blShowNonConsentedStudies,bcreator.getIntPatientKey(), StudyUtilities.DOMAIN_BIOSPECIMEN, null));
		
		
		sbXML.append(PatientUtilities.getPatientEncountersXML(bcreator.getIntPatientKey()));
		
		
		sbXML.append(QueryChannel.buildFormLabelXMLFile(formField));
                
                try
                {
                    sbXML.append(addTransactionXML());
                }
                catch (Exception e)
                {
                    System.out.println("[BatchCreateXMLBuilder::buildEditDetailsForm] Exception: " + e.toString());
                }
		return sbXML.toString();
		
	}
        
        // function is a hack
        // XML is not being generated for bioTransactions: strRecordedBy and strOwner
        private StringBuffer addTransactionXML() throws java.sql.SQLException
        {
            StringBuffer sb = new StringBuffer();
            neuragenix.dao.DALSecurityQuery query = null;
            java.sql.ResultSet rs = null;
            Vector vtSearchPersonDir = DatabaseSchema.getFormFields("ccoredomains_search_up_person_dir");
            
            // Append strRecordedBy
            sb.append("<BIOSPECIMEN_TRANSACTIONS_strRecordedBy>" + bcore.authToken.getUserIdentifier() + "</BIOSPECIMEN_TRANSACTIONS_strRecordedBy>");
            
            // Append search_user - strOwner details
            try
            {
                query = new neuragenix.dao.DALSecurityQuery(bcore.ACTION_BIOSPECIMEN_VIEW, bcore.authToken);
                query.setDomain("USERDETAILS", null, null, null);
                query.setFields(vtSearchPersonDir, null);
                query.setOrderBy("USERDETAILS_strUserName", "ASC");
                rs = query.executeSelect();
                sb.append(QueryChannel.buildSearchXMLFile("search_user", rs, vtSearchPersonDir));
                if (rs!=null)
                    rs.close();
            }
            catch (Exception e)
            {
                if (rs!=null)
                    rs.close();
                System.out.println("[BatchCreateXMLBuilder::addTransactionXML] Exception: " + e.toString());
            }
            return sb;
        }
	
	
	public String buildCurrentGenerationInventory(BatchCreator bcreator){
		StringBuffer sbXML = new StringBuffer();
		//get the XML for the inventory label
		Vector inventoryFormFields = DatabaseSchema.getFormFields("cbiospecimen_inventory_titles");
		inventoryFormFields.addAll(DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen"));
		sbXML.append(QueryChannel.buildFormLabelXMLFile(inventoryFormFields));
		sbXML.append(QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_transaction")));
		sbXML.append(getAvailableCellsXML(bcreator));
		return sbXML.toString();
	}
	
	public String getAvailableCellsXML(BatchCreator bcreator){ 
		StringBuffer strXML = new StringBuffer();
		
		BGUtilities bgUtil = new BGUtilities();
		
		for (int i = 0; i < bcreator.getGenerationGroups().size(); i++)
		{
			strXML.append(((GenerationDetails) bcreator.getGenerationGroups().get(i)).translateToXML(i));
		}
		
		return strXML.toString();
	}
	
	
	public String buildSpecificTrayXML(int intTrayKey, BatchCreator bcreator, int internalGDKey){
		StringBuffer sbXML = new StringBuffer();
		
		sbXML.append("<details>");
		sbXML.append("<numberOfCellToSelect>"+((GenerationDetails) bcreator.getGenerationGroups().get(internalGDKey)).getAllocationAmount()+"</numberOfCellToSelect>");
		sbXML.append("<GD_internalID>"+internalGDKey + "</GD_internalID>");
		sbXML.append("<TRAY_intTrayKey>"+intTrayKey+"</TRAY_intTrayKey>");
		sbXML.append(InventoryUtilities.buildTrayMapXML(intTrayKey, null, bcreator.getAllocatedCellIDs(), null));
		sbXML.append("</details>");
		return sbXML.toString();
	}
	
	
	public String buildIDInformationPage(BatchCreator bcreator){
		
		//	grab the property for autogeneration
        boolean blAutoGenerateID = false;
        try {
            blAutoGenerateID = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.AutoGenerateBiospecimenID");
        }
        catch (Exception e) {
            System.err.println("[BuildXMLBatchCreation] : Unable to retreive property - neuragenix.bio.AutoGenerateBiospecimenID");
            //e.printStackTrace();
        }
        
        StringBuffer sbXML = new StringBuffer();
        sbXML.append(this.buildExistingGenerationsXMLWithIndividualIDs(bcreator.getGenerationGroups(), null, null, blAutoGenerateID));
        sbXML.append(QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen")));
	sbXML.append("<parentID>"+BiospecimenUtilities.getUserBiospecimenID(bcreator.getIntParentBiospecimenKey())+"</parentID>");
        return sbXML.toString();

	}
	
	/** This function builds the XML for the final page, when user review the batch creation
	 * 
	 * 
	 */
	
	public String buildReportXML(BatchCreator bcreator){
		StringBuffer sbXML = new StringBuffer();
		Iterator iter = bcreator.getGenerationGroups().iterator();
		int index=0;
		while (iter.hasNext()){
			index++;
			GenerationDetails gdItem = (GenerationDetails) iter.next();
			
			sbXML.append("<generation>");
			sbXML.append(gdItem.translateToXML(index));
			
			sbXML.append(InventoryUtilities.buildTrayMapXML(gdItem.getIntTrayKey(), null, null, (List)bcreator.getAllocatedIDs().get(index+"")));
			
			
			sbXML.append("</generation>");
		}
		sbXML.append(QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cbiospecimen_inventory_titles")));
	        sbXML.append(QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cbiospecimen_batch_creation_option_form")));
	
		
		return sbXML.toString();
		
	}
	
	
	public String buildSubTypeXML(ChannelRuntimeData rd) {
		StringBuffer strXML = new StringBuffer();
		String strSampleType = rd.getParameter("BIOSPECIMEN_strSampleType");
		try {
			strXML.append(QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSampleSubType",null ,strSampleType));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return strXML.toString();
	}
}



