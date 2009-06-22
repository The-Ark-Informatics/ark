package neuragenix.bio.biospecimen.batchactions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasig.portal.ChannelRuntimeData;

import neuragenix.bio.biospecimen.BiospecimenCore;
import neuragenix.common.NGXRuntimeProperties;

import neuragenix.common.QueryChannel;
import neuragenix.dao.DatabaseSchema;

public class BatchCreationManager{
	NGXRuntimeProperties rp;
	BatchCreator bcreator = null;
	BatchCreatorXMLBuilder bcXMLBuilder= null;
	
	//List of steps in doing batch creating
	
	
	//List of stylesheets used in BatchCreation
	private static final String STYLE_BATCH_GET_OPTIONS="batch_get_options"; 
	
	public BatchCreationManager(NGXRuntimeProperties rp){
		this.rp = rp;
		
	}
	
	
	public void processRuntimeData(ChannelRuntimeData rd){
		String strAction = rd.getParameter("action") != null ? rd.getParameter("action") : null ;
		
		if (strAction.equalsIgnoreCase("BACK")){
			//do the back buttonb
			return;
		}
					 
		if (strAction.equalsIgnoreCase("BATCH_CREATE_START")){
			//get the id of the parent, and set it to the BatchCreator
			if (rd.getParameter("intParentBiospecimenKey")!= null){
				
					bcreator = new BatchCreator(Integer.parseInt(rd.getParameter("intParentBiospecimenKey")));
					bcreator.setIntPatientKey(Integer.parseInt(rd.getParameter("intPatientKey")));
					bcXMLBuilder = new BatchCreatorXMLBuilder(new BiospecimenCore (rp.getAuthToken()));
				
			}
			//do the first screen
			//build the first screen
			rp.addXML("<batch_creation>");
			try {
				//Tom changed the next line to make the first windown of batch create have value in type and treatment
				//rp.addXML(bcXMLBuilder.buildGettingOptionsXML(bcreator.getIntParentBiospecimenKey(), null));
				rp.addXML(bcXMLBuilder.buildGettingOptionsXML(bcreator.getIntParentBiospecimenKey(), rd));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rp.addXML(bcXMLBuilder.buildExistingGenerationsXML(bcreator.getGenerationGroups()));
			rp.addXML("<parentKey>"+bcreator.getIntParentBiospecimenKey()+"</parentKey>");
			rp.addXML("</batch_creation>");
			rp.setStylesheet(STYLE_BATCH_GET_OPTIONS);
			
			return;
		}
					
		
		if (strAction.equalsIgnoreCase("add_generation_details")){
			String strSubAction = null;

			rp.addXML("<batch_creation>");
			if (!"refresh".equals(rd.getParameter("subaction")))
			
			{   //add new otion
				bcreator.addNewGenerationDetails(rd);
			}
			
			
			
			try {
				rp.addXML(bcXMLBuilder.buildGettingOptionsXML(bcreator.getIntParentBiospecimenKey(), rd));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			rp.addXML(bcXMLBuilder.buildExistingGenerationsXML(bcreator.getGenerationGroups()));
			rp.addXML("<parentKey>"+bcreator.getIntParentBiospecimenKey()+"</parentKey>");
			rp.addXML("</batch_creation>");
			rp.setStylesheet(STYLE_BATCH_GET_OPTIONS);
			
			
			return; 
		}
		
		
		if ( strAction.equalsIgnoreCase("edit_full_details")){
			int gdIDToEdit = Integer.parseInt(rd.getParameter("GD_internalKey"));
			
			rp.addXML("<batch_creation>");
			try {
				rp.addXML("<details>");
				
				if ("refresh".equalsIgnoreCase(rd.getParameter("subaction")))
				{       
					rp.addXML(bcXMLBuilder.buildEditDetailsForm(bcreator, gdIDToEdit, rd));
				}
				else 
					rp.addXML(bcXMLBuilder.buildEditDetailsForm(bcreator, gdIDToEdit, null));
				rp.addXML("</details>");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			try {
				rp.addXML(bcXMLBuilder.buildGettingOptionsXML(bcreator.getIntParentBiospecimenKey(), rd));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//well, display the full details form
			rp.addXML(bcXMLBuilder.buildExistingGenerationsXML(bcreator.getGenerationGroups()));
			rp.addXML("<parentKey>"+bcreator.getIntParentBiospecimenKey() +"</parentKey>");
			rp.addXML("</batch_creation>");
			//System.out.println(rp.getXML());
			return;
		}
		
		
		
		if ( strAction.equalsIgnoreCase("save_details")){
			
			//save the full details
			bcreator.saveFullDetails(rd,Integer.parseInt(rd.getParameter("GD_internalKey")));
			//grep the runtime properties and add it to the corresponding GenerationDetails
			
			
//			rebuild the screen
			rp.addXML("<batch_creation>");
			try {
				rp.addXML(bcXMLBuilder.buildGettingOptionsXML(bcreator.getIntParentBiospecimenKey(), null));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			rp.addXML(bcXMLBuilder.buildExistingGenerationsXML(bcreator.getGenerationGroups()));
                        rp.addXML("<parentKey>"+bcreator.getIntParentBiospecimenKey() +"</parentKey>");

			rp.addXML("</batch_creation>");
			return;
		}
		
		
		if (strAction.equalsIgnoreCase("remove_group")){
			
			//remove a Generation Details group
			if (rd.getParameter("GD_intIDToRemove") != null){
				bcreator.removeGenerationDetails(Integer.parseInt(rd.getParameter("GD_intIDToRemove")));
			}
			//rebuild the first screen (get_option_screen)
			rp.addXML("<batch_creation>");
			try {
				rp.addXML(bcXMLBuilder.buildGettingOptionsXML(bcreator.getIntParentBiospecimenKey(), null));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			rp.addXML(bcXMLBuilder.buildExistingGenerationsXML(bcreator.getGenerationGroups()));
                        rp.addXML("<parentKey>"+bcreator.getIntParentBiospecimenKey() +"</parentKey>");
			rp.addXML("</batch_creation>");

			
			return;
		}
		
		
		if (strAction.equalsIgnoreCase("start_inventory_allocation")){
			//search for available allocations 
			bcreator.generateAvailableAllocations();
			//build the inventory allocation
		
			rp.addXML("<batch_creation>");
			rp.addXML(bcXMLBuilder.buildCurrentGenerationInventory(bcreator));
			rp.addXML("</batch_creation>");
			rp.setStylesheet("allocate_inventory");
			//System.out.println(rp.getXML());
			//display the already added options
			
			return;
		}
			
		
		
		if( strAction.equalsIgnoreCase("allocate_cells")){
			//build up the tray map based on the tray user selected
			int intSelectedTrayID = Integer.parseInt(rd.getParameter("locationID"));
			int internalGDKey = Integer.parseInt(rd.getParameter("intAllocationID"));
			
			rp.addXML("<batch_creation>");
			
			rp.addXML(bcXMLBuilder.buildCurrentGenerationInventory(bcreator));
			rp.addXML(bcXMLBuilder.buildSpecificTrayXML(intSelectedTrayID, bcreator, internalGDKey));
			rp.addXML("</batch_creation>");
			
			
			
			
			return;
		}
		
		
		if ( strAction.equalsIgnoreCase("save_allocations")){
			//get GD key
			int intGDKey = Integer.parseInt(rd.getParameter("GD_internalID"));
			int  intTrayKey = Integer.parseInt(rd.getParameter("TRAY_intTrayKey"));
			
			String cellList = rd.getParameter("cellList");
			List cellIDList = Arrays.asList(cellList.split(";"));
			bcreator.setAllocateCellsToGenerationDetails(intTrayKey, cellIDList, intGDKey);
			
			
			//search for available allocations 
			bcreator.generateAvailableAllocations();
			//build the inventory allocation
		
			rp.addXML("<batch_creation>");
			rp.addXML(bcXMLBuilder.buildCurrentGenerationInventory(bcreator));
			rp.addXML("</batch_creation>");
			rp.setStylesheet("allocate_inventory");		
			return;
		}
					  
					
		
		if (strAction.equalsIgnoreCase("generate_id")){
			
			//bring up the id generation form if not auto generated
			rp.addXML("<batch_creation>");
			rp.addXML(bcXMLBuilder.buildIDInformationPage(bcreator));
			rp.addXML("</batch_creation>");
			
			rp.setStylesheet("generate_id");
			return;
		
		}
		
		
		
		if ( strAction.equalsIgnoreCase("show_summary")){
			//check the entered ids, if duplicates then return the pages with error message
			// if it is not ID generated
			if (!bcreator.isBlAutoGenerateID()){
				//check the IDs entered
				HashMap idsMap = new HashMap();
				Map messageMap = new HashMap();
				boolean isEnteredIDDuplicates = bcreator.checkIDsEnteredDuplicates(rd.getParameters(), idsMap, messageMap);
				boolean isIDsAlreadyUsed = bcreator.checkIDsForDuplicateInDataBase(idsMap,messageMap);
				//if error, build the previous page and return 
				if (isEnteredIDDuplicates || isIDsAlreadyUsed){
					rp.addXML("<batch_creation>"+ bcXMLBuilder.buildExistingGenerationsXMLWithIndividualIDs(bcreator.getGenerationGroups(), idsMap, messageMap, false));
					rp.addXML(QueryChannel.buildFormLabelXMLFile(DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen")));
                                         
					rp.addXML("</batch_creation>");
					//System.out.println(rp.getXML());
					return;
				}
				//if it goes down here, everything should be ok
				bcreator.setEnteredIdsToGenerationDetails(idsMap);
				
				//set the ID to correspoding Generation Details
				
			}
			bcreator.saveBatchCreationInformation(rp.getAuthToken());
			
			rp.addXML("<batch_creation>" + bcXMLBuilder.buildReportXML(bcreator));
			rp.addXML("<parentKey>"+bcreator.getIntParentBiospecimenKey()+"</parentKey>");
			rp.addXML("</batch_creation>") ;
			rp.setStylesheet("report");
			
			//System.out.println(rp.getXML());
			return;
		}
		
		
		if ( strAction.equalsIgnoreCase("confirm_and_save")){
			// save all the details
			//should return to the parent biospecimen view page
			
		}
		
		
	}


	public NGXRuntimeProperties getRp() {
		return rp;
	}


	public void setRp(NGXRuntimeProperties rp) {
		this.rp = rp;
	}
	
	
	
}
