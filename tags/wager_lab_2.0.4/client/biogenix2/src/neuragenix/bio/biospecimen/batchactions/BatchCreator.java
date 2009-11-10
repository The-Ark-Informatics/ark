package neuragenix.bio.biospecimen.batchactions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import java.util.SortedMap;
import java.util.Collections;
import java.util.Iterator;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.PropertiesManager;

import neuragenix.bio.biospecimen.BiospecimenCore;
import neuragenix.bio.biospecimen.GenerationDetails;
import neuragenix.bio.biospecimen.InventoryDetails;
import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.bio.utilities.InventoryUtilities;
import neuragenix.common.QueryChannel;
import neuragenix.common.Utilities;
import neuragenix.dao.DatabaseSchema;
import neuragenix.security.AuthToken;

public class BatchCreator {
	boolean blAutoGenerateID = false;
	//this list stores groups of option that entered by user
	List generationGroups = new ArrayList();
	
	//list contains all the cell IDs occupied by this allocation
	List allocatedCellIDs = new ArrayList();
	
	//Map of all internal Bios IDs this creator created
	Map allocatedIDs = new HashMap();
	
	//list of all new ID typed in by users
	
	// each batch creation should have a parent key 
	int intParentBiospecimenKey;
	
	
	// patient key of parent biospecimen
	int intPatientKey;
	
	//Channel Runtime Data of the parent biospecimen
	ChannelRuntimeData parentBiosRd;
	
	
	public BatchCreator(int parentBiospecimenKey){
		this.intParentBiospecimenKey = parentBiospecimenKey;
		this.parentBiosRd = BiospecimenUtilities.buildBiospecimenRuntimeData(parentBiospecimenKey);

        try {
            blAutoGenerateID = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.AutoGenerateBiospecimenID");
        }
        catch (Exception e) {
            System.err.println("[BiospecimenCore] : Unable to retreive property - neuragenix.bio.AutoGenerateBiospecimenID");
            e.printStackTrace();
        }
		
	}

	
	public int getIntParentBiospecimenKey() {
		return intParentBiospecimenKey;
	}

	public void setIntParentBiospecimenKey(int intParentBiospecimenKey) {
		this.intParentBiospecimenKey = intParentBiospecimenKey;
	}
	
	
	public void addNewGenerationDetails(ChannelRuntimeData usrRd){
		//number of biospecimen for this generaion detail
		int intAllocationAmount =  Integer.parseInt(usrRd.getParameter("GENERATION_DETAILS_intNumber")); 
		
		//		creat a new generation details object 
        GenerationDetails gd;
        //search for every available inventory
        int intSiteID = -1000;
        int intTankID = -1000;
        int intBoxID = -1000;
        int intTrayID = -1000;
        //default to fill blank
        int intAllocationMode = 1;
        if (usrRd.getParameter("GENERATION_DETAILS_blNewTray")!= null){
        		if (usrRd.getParameter("GENERATION_DETAILS_blNewTray").equalsIgnoreCase("true"))
        			//change allocation mode to new tray
        			intAllocationMode = 2;
        	
        }
        
        gd = new GenerationDetails(intSiteID, intTankID, intBoxID, intTrayID, intAllocationMode, intAllocationAmount);
        //add this option to generation groups
        this.generationGroups.add(gd);
        
        
        
        
        //clone the rd of the parent biospecimen
        ChannelRuntimeData newRd = (ChannelRuntimeData) this.parentBiosRd.clone();
        
        //update the new RD with information that user entered
        if (usrRd.getParameter("BIOSPECIMEN_strSampleType") != null)
        {
        		newRd.setParameter("BIOSPECIMEN_strSampleType", usrRd.getParameter("BIOSPECIMEN_strSampleType"));
        	
        }
        
        if (usrRd.getParameter("BIOSPECIMEN_strSampleSubType") != null)
        {
        		newRd.setParameter("BIOSPECIMEN_strSampleSubType", usrRd.getParameter("BIOSPECIMEN_strSampleSubType"));
        	
        }
        
        if (usrRd.getParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity") != null)
        {
        		newRd.setParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity", usrRd.getParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity"));
        	
        }
        
        if (usrRd.getParameter("BIOSPECIMEN_TRANSACTIONS_strTreatment") != null)
        {
        		newRd.setParameter("BIOSPECIMEN_TRANSACTIONS_strTreatment", usrRd.getParameter("BIOSPECIMEN_TRANSACTIONS_strTreatment"));
        	
        }
        
        if (usrRd.getParameter("BIOSPECIMEN_TRANSACTIONS_strUnit") != null)
        {
        		newRd.setParameter("BIOSPECIMEN_TRANSACTIONS_strUnit", usrRd.getParameter("BIOSPECIMEN_TRANSACTIONS_strUnit"));
        	
        }
        
        // I hate this so much, why do I need to care about the date here, grrrr
        //set the transaction date required for quantity saving
        // default to current date
        Calendar calendar = Calendar.getInstance();
        
        SimpleDateFormat formmatter = new SimpleDateFormat("dd/MM/yyyy");
        newRd.setParameter("BIOSPECIMEN_TRANSACTIONS_dtTransactionDate", formmatter.format(calendar.getTime()));
        
        //Utilities.printRuntimeData(newRd);   
        gd.setRdDataToSave(newRd);
        
	}
	
	
	public void removeGenerationDetails(int internalIDToRemove){
		this.generationGroups.remove(internalIDToRemove);
	}

	public List getGenerationGroups() {
		return generationGroups;
	}

	public void setGenerationGroups(List generationGroups) {
		this.generationGroups = generationGroups;
	}

	public int getIntPatientKey() {
		return intPatientKey;
	}

	public void setIntPatientKey(int intPatientKey) {
		this.intPatientKey = intPatientKey;
	}
	
	/**
	 * This method saves the new runtime data to the generation details
	 * @param newRd
	 * @param internalGDKey
	 */
	public void saveFullDetails(ChannelRuntimeData newRd, int internalGDKey){
		
		GenerationDetails gdItem = (GenerationDetails) this.generationGroups.get(internalGDKey);
		//fix the data and time format
		Vector vtParentBiospecDetail = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen");
		QueryChannel.updateDateValuesInRuntimeData(vtParentBiospecDetail, newRd);
		QueryChannel.updateTimeValuesInRuntimeData(vtParentBiospecDetail, newRd);
		
		//fix in quantity form
		Vector vtAddQuantity = DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_transaction");
        QueryChannel.updateDateValuesInRuntimeData(vtAddQuantity, newRd);;
		gdItem.setRdDataToSave(newRd);
		
	}
	
	
	
	/** Cycle through the vector and get all the available allocations
     * it returns a vector of generation details with list of available options
     *
     *
     */
    public void generateAvailableAllocations()
    {
        //Vector vtAvailableAllocations = new Vector(10, 10);
        GenerationDetails tempHandle = null;
        for (int i = 0; i < this.generationGroups.size(); i++)
        {
             
            tempHandle = (GenerationDetails) this.generationGroups.get(i);
            //only look for the ones that not allocated. save some CPU cycle 
            if ( tempHandle.getGenerationStatus() != GenerationDetails.GENERATION_SUCCESS) {
               Vector locations = tempHandle.getAvailableLocations(null);
               if (locations != null)
               {
                  tempHandle.setInventoryDetails(locations);
               
               }
            }
            
        }
        
    }

	public List getAllocatedCellIDs() {
		return allocatedCellIDs;
	}
	
	
	public void setAllocatedCellIDs(List allocatedCellIDs) {
		this.allocatedCellIDs = allocatedCellIDs;
	}
	/**Allocate Cells to A Generation Details
	 * 
	 * @param intTrayKey
	 * @param cellIDsList
	 * @param internalGDKey
	 */
	
	public void setAllocateCellsToGenerationDetails(int intTrayKey, List cellIDsList, int internalGDKey){
		
		GenerationDetails gdItem = (GenerationDetails)this.generationGroups.get(internalGDKey);
		gdItem.setCellIDsList(cellIDsList);
		gdItem.setIntTrayKey(intTrayKey);
		//set a inventory seletion
		gdItem.setIntInventoryAllocationID(intTrayKey, gdItem.getAllocationType());
		InventoryDetails inventory = (InventoryDetails) gdItem.getInventoryDetails().get(gdItem.getIntInventoryAllocationID());
		//rest the site and tank, tray, box
		if (inventory != null){
			
			gdItem.setSiteName(Utilities.cleanForXSL(inventory.getSiteName()));
			gdItem.setTankName(Utilities.cleanForXSL(inventory.getTankName()));
			gdItem.setBoxName(Utilities.cleanForXSL(inventory.getBoxName()));
			gdItem.setTrayName(Utilities.cleanForXSL(inventory.getTrayName()));
		}
		gdItem.setGenerationStatus(GenerationDetails.GENERATION_SUCCESS);
		//gdItem.set
		//add those to the list to prevent duplicates
		this.allocatedCellIDs.addAll(cellIDsList);
		
	}
	
	
	public void saveBatchCreationInformation(AuthToken authToken)
        {
            //assuming all the CellIDs, and BiosID are in the GenerationDetails
            ChannelRuntimeData rdToSave = null;
            BiospecimenCore bcore = new BiospecimenCore(authToken);
            Iterator iter = this.getGenerationGroups().iterator();
            int counter = 0;
            
            while ( iter.hasNext())
            {
                counter++;
                GenerationDetails gdItem = (GenerationDetails) iter.next();
                rdToSave = gdItem.getRdDataToSave();
                List internalIDsForThisGD = new ArrayList();
                //for each biospecimen needed to be saved
                for (int i = 0; i < gdItem.getAllocationAmount(); i++)
                {
                    //if the biospecimen ID is not null
                    
                    //remove the other ids
                    if(rdToSave.getParameter("BIOSPECIMEN_strOtherID") != null)
                    {
                        rdToSave.setParameter("BIOSPECIMEN_strOtherID",(String) null);
                    }
                    //do this to bypass the required checking
                    rdToSave.setParameter("BIOSPECIMEN_strBiospecimenID","tempString");
                    
                    if (!blAutoGenerateID)
                    {
                        rdToSave.setParameter("BIOSPECIMEN_strBiospecimenID", gdItem.getBiosIDsList().get(i)+"");
                    }
                    
                    //Utilities.printRuntimeData(rdToSave);
                    String error = bcore.saveBiospecimen(rdToSave);
                    if (error != null){
                        System.out.println("Error when saving bio" + error);
                    }
                    int lastInsertedID = bcore.getLastBiospecimenAddedID();
                    
                    rdToSave.setParameter("BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", lastInsertedID+"");
                    internalIDsForThisGD.add(lastInsertedID+"");
                    //this.allocatedIDs.add(lastInsertedID+"");
                    //set ID back to ID Generation for reporting later
                    if (blAutoGenerateID)
                    {
                        if (gdItem.getBiosIDsList() == null )
                        {
                            gdItem.setBiosIDsList(new ArrayList() );
                        }
                        gdItem.getBiosIDsList().add(i, BiospecimenUtilities.getUserBiospecimenID(lastInsertedID));
                    }
                    
                    
                    bcore.doSaveQuantity(lastInsertedID+"", rdToSave);
                    if (gdItem.getCellIDsList() != null  && i < gdItem.getCellIDsList().size())
                    {
                        InventoryUtilities.saveBiospecimenLocation(lastInsertedID,Integer.parseInt((String)gdItem.getCellIDsList().get(i)), authToken);
                    }
                }
                this.allocatedIDs.put(counter+"", internalIDsForThisGD);
                
            }
        }


	public boolean isBlAutoGenerateID() {
		return blAutoGenerateID;
	}


	public void setBlAutoGenerateID(boolean blAutoGenerateID) {
		this.blAutoGenerateID = blAutoGenerateID;
	}
	
	/**This function check for duplicate IDs that user entered. 
	 * 
	 * @param dataFromRD a map that converted using rd.getParameters from ChannelRuntimeData
	 * @param returnedIDsMap this map is passed by caller, and upon return, it should contains all the index (to display) mapping to IDs entered by user  
	 * @param returnMessagesMap similar to returnIDsMap, but contains error message if applicable
	 * @return
	 */
	public boolean checkIDsEnteredDuplicates(Map dataFromRD, Map returnedIDsMap, Map returnMessagesMap){
		Iterator keyIterator = dataFromRD.keySet().iterator();
		boolean returnResult = false;
		String key;
		String value;
		while (keyIterator.hasNext()){
			key = (String)keyIterator.next();
			
			if (key.indexOf("enteredID_") >= 0){
				value = (String) dataFromRD.get(key);
				//check with already entered keys
				if (returnedIDsMap.containsValue(value)){
					//set an message to this id
					returnMessagesMap.put(key, "ID duplicated within entered data");
					returnResult = true;
				}
				returnedIDsMap.put(key, value);
			}
				
		}
		
		
		return returnResult;
	}
	
	
	
	public boolean checkIDsForDuplicateInDataBase(Map IdsMap, Map returnMessageMap){
		boolean returnedResult=false; //initially no duplicates
	
		Iterator entryIterator = IdsMap.entrySet().iterator();
		while (entryIterator.hasNext()){
			Map.Entry entry = (Map.Entry) entryIterator.next();
			//check for duplicates for this ID, we seek null for no duplicates
			if (BiospecimenUtilities.checkForDuplicateIDs((String) entry.getValue(),BiospecimenUtilities.BIOSPECIMENID) != null){
				returnedResult = true;
				returnMessageMap.put((String) entry.getKey(),"ID already in use");
			}
			
		}
		
		
		return returnedResult;
	}
	
	/** This function assumes that the duplicate checking already succeeded
	 * 
	 * @param idsMap
	 */
	public void setEnteredIdsToGenerationDetails(Map idsMap){
		//convert the keys of the map to a List so that we can sort
                ArrayList keyList = new ArrayList (idsMap.keySet());
                Collections.sort(keyList);
                //convert it to an iterator
                Iterator entryIterator = keyList.iterator();
		String marker = "enteredID_";
		String[] indexes;
		int gdIndex;
		int biosIndex;
		
		//String key;
		while (entryIterator.hasNext()){
                        //get the map entry of the current key
                        String key = new String ((String)entryIterator.next());
			String value = (String) idsMap.get(key);
			
			//need to remove the "enteredID_"
			if (key.indexOf(marker) >= 0){
				
				key = new StringBuffer(key).delete(0,marker.length()).toString();
			}
			indexes = key.split("_");
			gdIndex = Integer.parseInt(indexes[0]);
			biosIndex = Integer.parseInt(indexes[1]);
			
			
			//get the corresponding Generation Details
			GenerationDetails gdItem = (GenerationDetails) this.getGenerationGroups().get(gdIndex);
                        
                        
			List biosIDListOfGD = gdItem.getBiosIDsList();
			if (biosIDListOfGD == null){
				gdItem.setBiosIDsList(new ArrayList(gdItem.getAllocationAmount()));
			}
			//add the ID stored in entry to the list
			gdItem.getBiosIDsList().add(value);
			
		}
		
	}


	public Map getAllocatedIDs() {
		return allocatedIDs;
	}
}
		

	
	


