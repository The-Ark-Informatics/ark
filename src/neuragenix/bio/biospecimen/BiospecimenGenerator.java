/*
 * BatchAllocator.java
 *
 * Created on 20 December 2004, 16:51
 *
 * Copyright (C) Neuragenix Pty Ltd, 2004
 * Designed and coded by Daniel Murley
 * email : dmurley@neuragenix.com
 *
 */

/*****************************************************************
 *
 *  DEVELOPERS PLEASE NOTE :
 *   If you've been asked to fix a bug in this module,
 *   please consult with Daniel first, as this code is somewhat
 *   complex and follows some strict guidelines, of which
 *   I can fill you in on.
 *
 *   Some documentation on the flow of this class is available.
 *
 *****************************************************************/

package neuragenix.bio.biospecimen;

/**
 *
 * @author  dmurley
 */

import java.text.SimpleDateFormat;
import java.util.*;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.security.*;
import neuragenix.bio.utilities.*;
import java.sql.ResultSet;

// uportal class..
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.PropertiesManager;



public class BiospecimenGenerator {
    
    
    public final static int SINGLE_TRAY_CONTINOUS = 0;
    public final static int SINGLE_TRAY_FILL_GAPS = 1;
    public final static int NEW_TRAY = 2;
    public final static int NO_ALLOCATION = 3;
    // Implement later
    public final static int MULIT_TRAY_FILL_GAPS = 2;
    
    public final int NUMBER_TO_GENERATE = 0;
    public final int PARENT_ID = 1;
    
    public static final int NAMING_AUTO = 0;
    public static final int NAMING_PREFIX = 1;
    public static final int NAMING_MANUAL = 2;
    
    public static final int SPECIMEN_NOT_FOUND = -1;
    public static final int NOT_CELL_LEVEL = -1;
    
    public static final int GENSTATUS_NOTSET = -1;
    public static final int GENSTATUS_READY_TO_GENERATE = 1;
    public static final int GENSTATUS_NO_SPACE = 2;
    public static final int GENSTATUS_GENERATION_FAILED = 3;
    
    
    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private int[] biospecimensToGenerate = new int[2];
    
    private int intAllocationMode = -1;
    private int intNamingConvention = -1;
    
    
    public static final int WILDCARD = -1000;
    
    
    
    public static final String XSL_DISPLAY_LOCATIONS = "bp_clone_displayLocations";
    public static final String XSL_DISPLAY_CELLLOCATIONS = "bp_clone_displayCellLocations";
    public static final String XSL_DISPLAY_RECEIPT = "bp_clone_displayReceipt";
    public static final String XSL_DISPLAY_GETOPTIONS = "bp_clone_getOptions";
    public static final String XSL_DISPLAY_CONFIRMLOCATION = "bp_clone_confirmLocation";
    public static final String XSL_DISPLAY_GETIDS = "bp_clone_getIDs";
    public static final String XSL_DISPLAY_FOUNDALLOCATIONS = "bp_clone_foundAllocations";
    public static final String XSL_DISPLAY_GENERATIONREPORT = "bp_clone_generationReport";
    
    private Vector vtGenerationList = new Vector (10, 10);
    
    private String strParentSpecimenForGenerationID = null;
    private int intParentSpecimenForGenerationID = -1;
    
    
    private int intIDGenerationMode = -1;
    
    public static final int ID_GENERATION_AUTO = 1;
    public static final int ID_GENERATION_MANUAL = 2;
    
    
    private AuthToken authToken;
    
    
    /** Creates a new instance of BiospecimenAllocator */
    public BiospecimenGenerator(AuthToken token)
    {
        this.authToken = token;
        
        // System.out.println ("Warning :: ID Generation has been hardcoded to manual");
        
        String idGenMode = null;
        
        try
        {
            idGenMode = PropertiesManager.getProperty("neuragenix.bio.AutoGenerateBiospecimenID");
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
            
        
        if (idGenMode == null)
        {
            System.err.println ("[BiospecimenGenerator :: Constructor] - Property \'neuragenix.bio.AutoGenerateBiospecimenID\' not set.  Defaulting to manual IDs");
            setIDGenerationMode(ID_GENERATION_MANUAL);
        }
        else if (idGenMode.equalsIgnoreCase("true"))
        {
            setIDGenerationMode(ID_GENERATION_AUTO);
        }
        else if (idGenMode.equalsIgnoreCase("false"))
        {
            setIDGenerationMode(ID_GENERATION_MANUAL);
        }
        else
        {
            System.err.println ("[BiospecimenGenerator :: Constructor] - Value for \'neuragenix.bio.AutoGenerateBiospecimenID\' unknown");
            setIDGenerationMode(ID_GENERATION_MANUAL);
        }
        
        
        
    }
 
    
    
    
    public void setAllocationMode (int intAllocationMode)
    {
        this.intAllocationMode = intAllocationMode;
    }
    
    public void setBiospecimenID(String internalBiospecimenID)
    {
        strParentSpecimenForGenerationID = internalBiospecimenID;
    }
    
    public void setBiospecimenID(int internalBiospecimenID)
    {
        intParentSpecimenForGenerationID = internalBiospecimenID;
    }
    
    public int getBiospecimenID()
    {
        return intParentSpecimenForGenerationID;
    }
    
    public String getFriendlyBiospecimenID()
    {
        // get the biospecimen id that we all know love and trust
        return getFriendlyBiospecimenID(getBiospecimenID());
        
    }
    
    public void setNamingConvention (int intNamingConvention)
    {
        this.intNamingConvention = intNamingConvention;
    }
    
    public int getNamingConvention ()
    {
        return intNamingConvention;
    }
    
    public int getAllocationMode ()
    {
        return intAllocationMode;
    }
    
    
    
    // cycle through the vector and get all the available allocations
    public Vector generateAvailableAllocations()
    {
        //Vector vtAvailableAllocations = new Vector(10, 10);
        Vector tempHandle = null;
        for (int i = 0; i < vtGenerationList.size(); i++)
        {
            tempHandle = getAvailableLocations((GenerationDetails) vtGenerationList.get(i));
            if (tempHandle != null)
            {
                ((GenerationDetails) vtGenerationList.get(i)).setInventoryDetails(tempHandle);
                
                // vtAvailableAllocations.add(i, tempHandle);
            }
            
        }
        return vtGenerationList;
    }
    
    
    
    
    public void addNewAllocationSelection(int intSiteID, int intTankID, int intBoxID, int intTrayID, int intAllocationType, int intAllocationAmount)
    {
        GenerationDetails gd = new GenerationDetails(intSiteID, intTankID, intBoxID, intTrayID, intAllocationType, intAllocationAmount);
        addNewAllocationSelection(gd);
        
    }
    
    public void addNewAllocationSelection (GenerationDetails gd)
    {
        vtGenerationList.add(gd);
    }
    
    
    
    
    public String getAllocationSelectionXML()
    {
        StringBuffer strXML = new StringBuffer();
        
        BGUtilities bgUtil = new BGUtilities();
        
        for (int i = 0; i < vtGenerationList.size(); i++)
        {
            strXML.append(((GenerationDetails) vtGenerationList.get(i)).translateToXML(bgUtil.getCellDetails(), i));
        }
        
        return strXML.toString();
    }
    
    public void addCloneIdentifier(CloneIdentifiers ci, int generationID)
    {
        ((GenerationDetails) getGDObject(generationID)).addNewCloneIdentifiers(ci);
    }
    
    
    /** try to avoid using this function and let the biospecimen generator handle
     *  everything
     *
     */
    
    private GenerationDetails getGDObject(int intGDId)
    {
        try
        {
           if (vtGenerationList.get(intGDId) != null)
           {
               return (GenerationDetails) vtGenerationList.get(intGDId);
           }
        }
        catch(Exception e)
        {
            return null;
        }
       
        return null;
    }
   
    
    public int getAmountToGenerate (int intGDId)
    {
        GenerationDetails gdHandle = getGDObject(intGDId);
        
        if (gdHandle != null)
        {
            return gdHandle.getAllocationAmount();
        }
        System.err.println ("[BiospecimenGenerator] Warning : Unable to get handle on GD - ID Was : " + intGDId);
        return -1;
    }
    
    
    
    
    public boolean removeAllocation(int allocationID)
    {
        if (allocationID < vtGenerationList.size())
        {
            vtGenerationList.remove(allocationID);
            return true;
        }
        return false;
    }
    
    public String buildXMLForGenerationDetails(int generationDetailsID)
    {
        GenerationDetails tempHandle = null;
        
        try
        {
            tempHandle = (GenerationDetails) vtGenerationList.get(generationDetailsID);    
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        
        if (tempHandle == null)
            return "";
        
        return tempHandle.translateToXML(generationDetailsID);
        
    }
    
    
    /** Generates a soft (non database written list) 
     
        Needs to be re-written for new interface
     
     
     */
    public void softGenerateSubBiospecimens(int intNumberToGenerate, int intParentBiospecimenInternalID)
    {
        if (intNumberToGenerate > 0 && intParentBiospecimenInternalID > 0)
        {
            biospecimensToGenerate[NUMBER_TO_GENERATE] = intNumberToGenerate;
            biospecimensToGenerate[PARENT_ID] = intParentBiospecimenInternalID;
 
        }
        
    }
    
    public int doGeneration(int generationDetailsID)
    {
        
        return doGeneration(getGDObject(generationDetailsID));
        
    }
    
    
    /**
     *
     *  DoGeneration Error Codes (Currently magic numbers)
     *
     *  Code 1000 - Unable to acquire locks on inventory
     *  Code 1001 - Unable to acquire read lock on parent specimen
     *  Code 1002 - Unable to find specimen details
     *  code 1003 - Unable to find generation details (eek, major)
     *  code 1004 - unable to find inventory details
     *  code 1005 - unable to match inventory details to inventory
     *  code 1006 - amount of specimen identifiers doesnt match amount to allocate
     *  code 1007 - update to inventory failed
     *  code 1008 - update to biospecimen table failed
     *  code 1100 - Generation Completed
     *  code 
     */
    
    
    public static final int UNABLE_TO_FIND_PARENT = 1002;
    public static final int NOT_ENOUGH_IDS = 1006;
    public static final int UNKNOWN_EXCEPTION = 9000;
    public static final int INVENTORY_UPDATE_FAILED = 1007;
    public static final int BIOSPECIMEN_UPDATE_FAILED = 1008;
    public static final int GENERATION_SUCCESS = 1100;
    public static final int INVENTORY_UPDATE_INVALID_SIZE = 1017;
    public static final int INVENTORY_UPDATE_TRAY_ADDED = 1027;
    
    
    public int doGeneration(GenerationDetails gd)
    {
        // TODO: validate all the locks are still available on inventory
        
        // set up the query to get details of the orignal biospecimen
        
        DALQuery dalBioDetails = new DALQuery();
        ResultSet rs = null;
        Hashtable htFieldData = new Hashtable();
        Vector vtCloneFields = DatabaseSchema.getFormFields("cbiospecimen_clone_biospecimen");
        
        try {
            //get details of parent bios
            dalBioDetails.setDomain("BIOSPECIMEN", null, null, null);
            dalBioDetails.setFields(vtCloneFields, null);
            dalBioDetails.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            dalBioDetails.setWhere("AND", 0, "BIOSPECIMEN_intBiospecimenID", "=", this.getBiospecimenID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            rs = dalBioDetails.executeSelect();
        
            if (rs.first())
            {
               for (int i = 0; i < vtCloneFields.size(); i++)
               {
                  //put data when cloning
                  String strCloneFieldName = (String) vtCloneFields.get(i);
                  String strCloneFieldValue = rs.getString((String) vtCloneFields.get(i));

                  if (strCloneFieldValue != null)
                  {

                     if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE){
                        DBField field = null;
                        field = (DBField) DatabaseSchema.getFields().get(strCloneFieldName);
                        java.sql.Date dtObject = null;
                        //need to convert into conventional format
                        if (field.getDataType() == DBMSTypes.DATE_TYPE ){
                           dtObject = rs.getDate(strCloneFieldName);
                           if (dtObject != null) {
                              SimpleDateFormat dfFormatter = new SimpleDateFormat("dd/MM/yyyy");
                              htFieldData.put(strCloneFieldName, dfFormatter.format(dtObject));
                           }
                        }
                        else if (field.getDataType() == DBMSTypes.TIME_TYPE ){
                           dtObject = rs.getDate(strCloneFieldName);
                           if (dtObject != null) {
                              SimpleDateFormat dfFormatter = new SimpleDateFormat("hh:mm a");
                              htFieldData.put(strCloneFieldName, dfFormatter.format(dtObject));
                           }
                        }
                        else
                           htFieldData.put(strCloneFieldName, strCloneFieldValue);
                     }
                     else // if ratherthan ORACLE add as normal 
                        htFieldData.put(strCloneFieldName, strCloneFieldValue);
                  }
               }  
               rs.close(); // close the resultset, keep hayden happy
               dalBioDetails = null;
            }
            else // if details can not be found -- return fatal error
            {
               System.err.println ("[BiospecimenGenerator :: DoGeneration() :: Fatal] - Unable to find parent specimen details (Internal ID : " + this.getBiospecimenID() + ")");
               rs.close();
               return UNABLE_TO_FIND_PARENT;
            }
            rs.close();
        }
        catch (Exception e)
        {
           e.printStackTrace();
           return UNKNOWN_EXCEPTION;
        }


        // TODO: send the generation details to the generation logger to store the details

        DALQuery dalInsertQuery = new DALQuery();
        dalInsertQuery.setManualCommit(true); // manual commit block for total rollback
        
        
        
        String strBiospecimenPatientKey = getPatientKey(getBiospecimenID() + "");
        
        try 
        {
            // get user entered data concerning the cloning
            Vector vtUserSpecifiedIDs = gd.getCloneIdentifiers();
            
            Vector vtGeneratedIDs = new Vector (30, 10);
            
            // get the details that we'll be using to do the allocation
            InventoryDetails idDetails = null;
            
            if (gd.getAllocationType() != NO_ALLOCATION)
            {
                idDetails = (InventoryDetails) gd.getInventoryDetails().get(gd.getIntInventoryAllocationID());
            }
            
            // retrieve the inventory details
            
            if (getIDGenerationMode() == ID_GENERATION_MANUAL)
            {
                if (vtUserSpecifiedIDs.size() != gd.getAllocationAmount())
                {
                    System.err.println("[BiospecimenGenerator] Not enough IDs specified by user");
                    // System.out.println ("User IDS Size : " + vtUserSpecifiedIDs.size());
                    // System.out.println ("Allocation Amount : " + gd.getAllocationAmount());
                    return NOT_ENOUGH_IDS;
                }

            }

            
            
            
            
            int intAllocationCellIDs[] = null;
            
            if (gd.getAllocationType() != NO_ALLOCATION)
            {
                if (gd.getAllocationType() == SINGLE_TRAY_FILL_GAPS || gd.getAllocationType() == NEW_TRAY)
                   intAllocationCellIDs = idDetails.getAvailableCells(gd.getAllocationAmount());
                else
                    intAllocationCellIDs = idDetails.getAvailableCells();

                if (intAllocationCellIDs == null)
                    System.err.println ("[BiospecimenGenerator] Error - Allocation IDs are null");
            }
            
            for (int i = 0; i < gd.getAllocationAmount(); i++)
            {
                // TODO: update the biospecimen with the cell key
             
                htFieldData.remove("BIOSPECIMEN_strBiospecimenID");         // Users ID
                htFieldData.remove("BIOSPECIMEN_intStudyKey");              // Remove Study
                htFieldData.remove("BIOSPECIMEN_intCellID"); // remove the cell id
                
                CloneIdentifiers ciHandle = (CloneIdentifiers) vtUserSpecifiedIDs.get(i);
                if (getIDGenerationMode() == ID_GENERATION_MANUAL)
                {
                    htFieldData.put("BIOSPECIMEN_strBiospecimenID", ciHandle.getBiospecimenID());
                }
                else
                {
                    htFieldData.put ("BIOSPECIMEN_strBiospecimenID", authToken.getSessionUniqueID() + "-" + i + "BCTEMP");
                }
                
                htFieldData.put("BIOSPECIMEN_strOtherID", ciHandle.getBarcodeID());

                                
                
                // study ID should always be constant
                htFieldData.put("BIOSPECIMEN_intStudyKey", ciHandle.getStudyID());
              
                
                // new set of form fields is required that doesnt include the biospecimen id (pkey)
                dalInsertQuery.reset();
                dalInsertQuery.setDomain("BIOSPECIMEN", null, null, null);
                dalInsertQuery.setFields(vtCloneFields, htFieldData);
                if (gd.getAllocationType() != NO_ALLOCATION)
                {
                   dalInsertQuery.setField("BIOSPECIMEN_intCellID", intAllocationCellIDs[i]+"", "hashtable"); 
                }
                else
                {
                   dalInsertQuery.setField("BIOSPECIMEN_intCellID", "-1" , "hashtable"); 
                }
               
                boolean queryResult = dalInsertQuery.executeInsert();
                
                if (queryResult == true) 
                {
                    
                    // get the ID
                    // with the ID, we need to 
                    //                   -- allocate to inventory
                    //                   -- update biospecimen IDs if the ID is auto generated (otherwise it'll be null)
                    //  Generated ids should also be saved somewhere as part of the audit trail
                    
                    // in the eyes of the allocation world, a new tray will be be treated as having continous allocation
                    // for that tray.
                    
                    int intNewBiospecimenID = dalInsertQuery.getInsertedRecordKey();
                    ciHandle.setDBBiospecimenID(intNewBiospecimenID);
                    // TODO: save inserted record key to generation details object
                    // TODO: auto id generation code goes here
                    
                    if (this.getIDGenerationMode() == ID_GENERATION_AUTO)
                    {
                        // get Biospecimen ID Factory
                        
                        IBiospecimenIDGenerator idGen = IDGenerationFactory.getBiospecimenIDGenerationInstance();
                        String strNewID = idGen.getBiospecimenID(ciHandle.getDBBiospecimenID(), dalInsertQuery, authToken);
                        
                        
                        
                        dalInsertQuery.reset();
                        dalInsertQuery.setDomain("BIOSPECIMEN", null, null, null);
                        dalInsertQuery.setField("BIOSPECIMEN_strBiospecimenID", strNewID);
                        dalInsertQuery.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intNewBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
                        boolean blIDUpdated = dalInsertQuery.executeUpdate();
                        
                        if (blIDUpdated == false)
                        {
                            dalInsertQuery.cancelTransaction();
                            return BIOSPECIMEN_UPDATE_FAILED;
                        }
                        else
                        {
                            ciHandle.setUserBiospecimenID(strNewID);
                        }
                        
                    }
                    
                    
                    
                    
                    // check if theres suppost to be an automatically generated ID to go along with this.
                    // IDGeneration class? modifications?
                    
                    if (gd.getAllocationType() != NO_ALLOCATION)
                    {
                        
                        // we're doing an allocation here
                        dalInsertQuery.reset();
                        Hashtable htInventoryData = new Hashtable();
                        
                        htInventoryData.put("CELL_intBiospecimenID", intNewBiospecimenID + "");
                        
                        htInventoryData.put("CELL_intPatientID", strBiospecimenPatientKey);
                        
                        dalInsertQuery.setDomain("CELL", null, null, null);
                        
                        dalInsertQuery.setFields(DatabaseSchema.getFormFields("cbiospecimen_bp_update_inventory_cell"), htInventoryData);
                        
                        dalInsertQuery.setWhere(null, 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        dalInsertQuery.setWhere("AND", 0, "CELL_intCellID", "=", intAllocationCellIDs[i] + "", 0, DALQuery.WHERE_HAS_VALUE);
                    
                        boolean blInsertedInventory = dalInsertQuery.executeUpdate();

                        if (blInsertedInventory == false)
                        {
                            dalInsertQuery.cancelTransaction();

                            //TODO: update audit logs

                            // bail out.
                            return INVENTORY_UPDATE_FAILED;

                        }
                        else
                        {
                            // TODO: update audit logs
                            if (decrementInventory(dalInsertQuery, intAllocationCellIDs[i]) == false)
                            {
                                dalInsertQuery.cancelTransaction();

                                return INVENTORY_UPDATE_FAILED;
                            }
                        }

                    } // if allocating

                } // actually managed to insert the biospecimen into the database
                else
                {
                    System.err.println ("[BiospecimenGenerator] Unable to insert the biospecimen into the database");
                    return BIOSPECIMEN_UPDATE_FAILED;
                    
                }
                
            } // generate specimen allocation loop
    
            // didn't fall out -- can be assumed that all went well.            
            // TODO: UPDATE AUDIT LOGS
            dalInsertQuery.commitTransaction();
            gd.setGenerationStatus(GENERATION_SUCCESS);
            return GENERATION_SUCCESS;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return UNKNOWN_EXCEPTION;
        }
        
        
        // user should be returned to the generation summary screen -- after which point they
        // can get the receipt for the generation.
       
    }

    public boolean decrementInventory(DALQuery query, int intCellKey)
    {
        ResultSet rs = null;
        
        
        try 
        {
            

            // get the trayid
            query.reset();
            query.setDomain("CELL", null, null, null);
            query.setField("CELL_intTrayID", null);
            query.setWhere(null, 0, "CELL_intCellID", "=", intCellKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first() == false) 
            {
                rs.close();
                return false;
            }
            
            int intTrayID = rs.getInt("CELL_intTrayID");
            
            // get the tray capacity and box ID

            rs.close();
            query.reset();
            query.setDomain("TRAY", null, null, null);
            query.setField("TRAY_intBoxID", null);
            query.setField("TRAY_intTrayAvailable", null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID + "", 0, DALQuery.WHERE_HAS_VALUE);

            rs = query.executeSelect();

            if (rs.first() == false) 
            {
                rs.close();
                return false;
            }
            
            int intBoxID = rs.getInt("TRAY_intBoxID");
            int intAvailable = rs.getInt("TRAY_intTrayAvailable");
            // update the capacity        

            query.clearFields();
            query.setField("TRAY_intTrayAvailable", (intAvailable - 1) + "");

            boolean updateResult = query.executeUpdate();

            if (updateResult == false) return false;

            // get the box capacity and the tank ID

            query.reset();
            query.setDomain("BOX", null, null, null);
            query.setField("BOX_intTankID", null);
            query.setField("BOX_intBoxAvailable", null);
            query.setWhere(null, 0, "BOX_intBoxID", "=", intBoxID + "", 0, DALQuery.WHERE_HAS_VALUE);

            rs.close();
            rs = query.executeSelect();
            
            if (rs.first() == false) 
            {
                rs.close();
                return false;
            }

            int intTankID = rs.getInt("BOX_intTankID");
            intAvailable = rs.getInt("BOX_intBoxAvailable");

            query.clearFields();
            query.setField("BOX_intBoxAvailable", (intAvailable - 1) + "");


            updateResult = query.executeUpdate();

            if (updateResult == false) return false;

            // GET the tank capacity

            query.reset();
            query.setDomain("TANK", null, null, null);
            query.setField("TANK_intTankAvailable", null);
            query.setWhere(null, 0, "TANK_intTankID", "=", intTankID + "", 0, DALQuery.WHERE_HAS_VALUE);

            rs.close();
            rs = query.executeSelect();

            if (rs.first() == false) 
            {
                rs.close();
                return false;
            }
            
            intAvailable = rs.getInt("TANK_intTankAvailable");
            query.clearFields();
            query.setField("TANK_intTankAvailable", (intAvailable - 1) + "");
         
            updateResult = query.executeUpdate();

            if (updateResult == false) return false;

            rs.close();

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public Vector getAvailableLocations (GenerationDetails gd)
    {
        Vector vtLocationGuide = null;
        StringBuffer strXML = new StringBuffer();
        int intAllocationMode = gd.getAllocationType();
        
        try
        {
            switch (intAllocationMode)
            {
                case SINGLE_TRAY_CONTINOUS:
                   
                    vtLocationGuide = discoverAvailableContinuous(gd);
                    break;
                case SINGLE_TRAY_FILL_GAPS:
                  
                    vtLocationGuide = discoverAvailableSingleTray(gd);
                    break;
            }
                      
           return vtLocationGuide;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    public String getAvailableLocations(int intSelectedTray)
    {
        StringBuffer strXML = new StringBuffer();
        
        Vector vtLocationGuide = null;
        try
        {
            switch (intAllocationMode)
            {
                case SINGLE_TRAY_CONTINOUS:
                    if (intSelectedTray > 0)
                        vtLocationGuide = discoverAvailableContinuous(biospecimensToGenerate[NUMBER_TO_GENERATE], intSelectedTray);
                    else
                        vtLocationGuide = discoverAvailableContinuous(biospecimensToGenerate[NUMBER_TO_GENERATE], NOT_CELL_LEVEL);
                    break;
                case SINGLE_TRAY_FILL_GAPS:
                    vtLocationGuide = discoverAvailableSingleTray(biospecimensToGenerate[NUMBER_TO_GENERATE], null);
                    break;
            }

            if (vtLocationGuide == null)
            {
                strXML.append("<strError>There are a no trays with enough space to allocate these biospecimens</strError>");
                return strXML.toString();
            }
            else
            {
                if (!(intSelectedTray > 0))
                {
                    DALQuery query = new DALQuery();
                    ResultSet rs;
                    Vector vtFields = DatabaseSchema.getFormFields("cbiospecimen_view_inventory_details");
                    query.setDomain("SITE", null, null, null);
                    query.setDomain("TANK", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
                    query.setDomain("BOX", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
                    query.setDomain("TRAY", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
                    query.setFields(vtFields, null);
                    query.setWhere(null, 0, "SITE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "TANK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "BOX_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

                    String currentAvailableTray = (String) vtLocationGuide.get(0);
                    String connector = "";
                    int openBracket = 0;
                    int closeBracket = 0;

                    for (int counter = 0; counter < vtLocationGuide.size(); counter++)
                    {
                        if (counter == 0)
                        {
                            connector = "AND";
                            openBracket = 1;
                            closeBracket = 0;
                        }
                        else if (counter == (vtLocationGuide.size() - 1))
                        {
                            connector = "OR";
                            openBracket = 0;
                            closeBracket = 1;
                        }
                        else
                        {
                            connector = "OR";
                            openBracket = 0;
                            closeBracket = 0;
                        }

                        currentAvailableTray = (String) vtLocationGuide.get(counter);
                        query.setWhere(connector, openBracket, "TRAY_intTrayID", "=", currentAvailableTray, closeBracket, DALQuery.WHERE_HAS_VALUE);

                    }
                    query.setOrderBy("SITE_intSiteID", "ASC");
                    query.setOrderBy("TRAY_intTrayID", "ASC");
                    rs = query.executeSelect();


                    // pass data to the query channel to build for me!
                    strXML.append(QueryChannel.buildFormLabelXMLFile(vtFields));
                    strXML.append(QueryChannel.buildSearchXMLFile("AvailableTrays", rs, vtFields));
                    rs.close();
                    
                }
                else
                {
                    Vector vtFields = DatabaseSchema.getFormFields("cbiospecimen_view_cell_tray");
                    strXML.append(QueryChannel.buildFormLabelXMLFile(vtFields));
                    
                    for (int i = 0; i < vtLocationGuide.size(); i++)
                    {
                       
                       int availableCells[] = (int[]) vtLocationGuide.get(i);
                       DALQuery cellQuery = new DALQuery();
                       ResultSet rs;
                       cellQuery.setDomain("TRAY", null, null, null);
                       cellQuery.setDomain("CELL", "CELL_intTrayID", "TRAY_intTrayID", "INNER JOIN");
                       cellQuery.setFields(vtFields, null);
                       cellQuery.setWhere(null, 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                       
                       String connector = "AND";
                       int openBrackets = 0;
                       int closeBrackets = 0;
                       
                       for (int k = 0; k < availableCells.length; k++)
                       {
                           if (k == 0)
                           {
                               connector = "AND";
                               openBrackets = 1;
                               closeBrackets = 0;
                           }
                           else if (k == (availableCells.length - 1))
                           {
                               connector = "OR";
                               openBrackets = 0;
                               closeBrackets = 1;
                           }
                           else
                           {
                               connector = "OR";
                               openBrackets = 0;
                               closeBrackets = 0;
                           }
                           cellQuery.setWhere(connector, openBrackets, "CELL_intCellID", "=", availableCells[k] + "", closeBrackets, DALQuery.WHERE_HAS_VALUE);
                        }
                      
                       rs = cellQuery.executeSelect();
                       
                       
                       strXML.append("<CellSet><SetID>" + i + "</SetID>" + QueryChannel.buildSearchXMLFile("AvailableCell", rs, vtFields) + "</CellSet>");
                       rs.close();
                       
                     }
                    
                }

                return strXML.toString();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
            
    
        
    }
    
    public String getXMLForManualID()
    {
        return null;
    }
    
    public String getFriendlyBiospecimenID(int internalBiospecimenID)
    {
        try
        {
            DALQuery query = new DALQuery();
            ResultSet rs;
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_strBiospecimenID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", internalBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first())
            {
                String strFriendlyID = rs.getString(1);
                if (strFriendlyID != null)
                {
                    rs.close();
                    return strFriendlyID;
                }
                else
                {
                    rs.close();
                    return SPECIMEN_NOT_FOUND + "";
                }
            }
            else
            {
                rs.close();
                return SPECIMEN_NOT_FOUND + "";
            }
        }
        catch (Exception e)
        {
            System.err.println ("[BiospecimenGenerator] There was an error finding the user defined biospecimen id");
            return SPECIMEN_NOT_FOUND + "";
        }
           
        
    }
    
    // added to give module biospecimen split functionality
    
    public Vector discoverAvailableContinuous (GenerationDetails gd)
    {
        Vector vtAvailableTrayKeys = new Vector (3000, 1000);
    
        // load in the inventory details.
        try {
            
        /* get a list of all the trays that actually have cells available to us
         *                  -- OR -- 
         * get all the tray for which the system has requested (where applicable)
         */
        
            
            
            
            
        // build query as appropriate
            DALSecurityQuery query = new DALSecurityQuery();
            DALQuery inventoryQuery = new DALQuery();
            DALQuery qryCell = new DALQuery();
           
            BGUtilities bgUtil = new BGUtilities();
            Hashtable trayDetails = bgUtil.getTrayDetails();
            
            
            
            
            
            ResultSet rs;
            
            query.setDomain ("SITE", null, null, null);
            query.setDomain("TANK", "SITE_intSiteID", "TANK_intSiteID", "INNER JOIN");
            query.setDomain("BOX", "TANK_intTankID", "BOX_intTankID", "INNER JOIN");
            query.setDomain("TRAY", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setField("TRAY_intTrayID", null);
            query.setField("TRAY_intNoOfRow", null);
            query.setField("TRAY_intNoOfCol", null);    
            query.setWhere(null, 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);    
            
            if (gd.getSiteID() != WILDCARD)
            {
                query.setWhere("AND", 0, "SITE_intSiteID", "=", gd.getSiteID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            if (gd.getTankID() != WILDCARD)
            {
                query.setWhere("AND", 0, "TANK_intTankID", "=", gd.getTankID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            if (gd.getBoxID() != WILDCARD)
            {
                query.setWhere("AND", 0, "BOX_intBoxID", "=", gd.getBoxID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            if (gd.getTrayID() != WILDCARD)
            {
                query.setWhere("AND", 0, "TRAY_intTrayID", "=", gd.getTrayID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            
            query.setWhere("AND", 0, "TRAY_intTrayAvailable", ">=", gd.getAllocationAmount() + "", 0, DALQuery.WHERE_HAS_VALUE);     
            
            rs = query.executeSelect();

            while (rs.next())
            {
                int trayKey = rs.getInt("TRAY_intTrayID");
                int trayCols = rs.getInt("TRAY_intNoOfCol");
                int trayRows = rs.getInt("TRAY_intNoOfRow");

                qryCell.reset();
                ResultSet rsCells;
                qryCell.setDomain("CELL", null, null, null);
                qryCell.setField("CELL_intBiospecimenID", null);
                qryCell.setField("CELL_intRowNo", null);
                qryCell.setField("CELL_intColNo", null);
                qryCell.setField("CELL_intCellID", null);
                qryCell.setWhere(null, 0, "CELL_intTrayID", "=", trayKey + "", 0, DALQuery.WHERE_HAS_VALUE);
                qryCell.setWhere("AND", 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qryCell.setOrderBy("CELL_intRowNo", "ASC");
                qryCell.setOrderBy("CELL_intColNo", "ASC");
                rsCells = qryCell.executeSelect();

                int runCount = 0;
                int runBeginPosition = -1;
                int currentCellValue = -10;
                int currentCellPosition = -1;
                int availableCells[] = new int[gd.getAllocationAmount()];
                while (rsCells.next())
                {
                    int currentCellCol = rsCells.getInt("CELL_intColNo");
                    int currentCellRow = rsCells.getInt("CELL_intRowNo");

                    currentCellValue = rsCells.getInt("CELL_intBiospecimenID");
                    currentCellPosition = (trayRows * (currentCellRow - 1)) + currentCellCol;
                    {
                        // -1 will indicate that the location is empty
                        // i'm thinking there is an infinie loop in this code.
                        if (currentCellValue == -1)
                        {
                            if (runBeginPosition == -1)
                                runBeginPosition = currentCellPosition;
                            runCount++;
                           // if (gd.getTrayID() != WILDCARD)
                           // {
                            availableCells[runCount - 1] = rsCells.getInt("CELL_intCellID");
                          
                           // }

                            if (runCount >= gd.getAllocationAmount())
                            {
                                   // System.out.print ("Available Cells Set : ");
                                   // for (int jkl = 0; jkl < availableCells.length; jkl++)
                                   // {
                                   //     System.out.print (availableCells[jkl] + ", ");
                                   // }
                                   // System.out.print("\n");
                                
                                    
                                    vtAvailableTrayKeys.add( new InventoryDetails (trayDetails, trayKey, availableCells.clone()));
                                    //System.out.println ("Above key : " + vtAvailableTrayKeys.size());
                                    availableCells = shiftArray(availableCells);
                                    runCount--;
                                    runBeginPosition++;
                            }
                        }
                        else
                        {
                            runBeginPosition = -1;
                            runCount = 0;
                            if (gd.getTrayID() != WILDCARD)
                               availableCells = new int[gd.getAllocationAmount()];
                        }
                    }

                }
                rsCells.close();
                rsCells = null;

            }
            rs.close();

            if (vtAvailableTrayKeys.size() > 0)
               return vtAvailableTrayKeys;
            else{
                return null;
               
            }
               
            }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
    
    
    
    
    
    /**
     *
     *  Returns a vector of all the available locations that contain enough continous space
     *  for these biospecimens
     *
     *  Passing in null for traysToSearch will search all trays
     *
     *
     *  Note to self -- this may need to be TRAY (singular) to search, as opposed to trayS
     *
     */
    public Vector discoverAvailableContinuous (int SpacesRequired, int traysToSearch)
    {
        Exception eDan = new Exception();
        eDan.printStackTrace();
        Vector vtAvailableTrayKeys = new Vector (10, 5);
    
        // load in the inventory details.
        try {
            
        /* get a list of all the trays that actually have cells available to us
         *                  -- OR -- 
         * get all the tray for which the system has requested (where applicable)
         */
            
        DALSecurityQuery query = new DALSecurityQuery();
        ResultSet rs;
        query.setDomain("TRAY", null, null, null);
        query.setField("TRAY_intTrayID", null);
        query.setField("TRAY_intNoOfRow", null);
        query.setField("TRAY_intNoOfCol", null);
        
        if (traysToSearch > 0)
           query.setWhere(null, 0, "TRAY_intTrayID", "=", traysToSearch + "", 0, DALQuery.WHERE_HAS_VALUE);
        else
           query.setWhere(null, 0, "TRAY_intTrayAvailable", ">=", SpacesRequired + "", 0, DALQuery.WHERE_HAS_VALUE);
        query.setWhere("AND", 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        
        rs = query.executeSelect();
        
        while (rs.next())
        {
            int trayKey = rs.getInt("TRAY_intTrayID");
            int trayCols = rs.getInt("TRAY_intNoOfCol");
            int trayRows = rs.getInt("TRAY_intNoOfRow");
            
            DALQuery qryCell = new DALQuery();
            ResultSet rsCells;
            qryCell.setDomain("CELL", null, null, null);
            qryCell.setField("CELL_intBiospecimenID", null);
            qryCell.setField("CELL_intRowNo", null);
            qryCell.setField("CELL_intColNo", null);
            qryCell.setField("CELL_intCellID", null);
            qryCell.setWhere(null, 0, "CELL_intTrayID", "=", trayKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            qryCell.setWhere("AND", 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            qryCell.setOrderBy("CELL_intRowNo", "ASC");
            qryCell.setOrderBy("CELL_intColNo", "ASC");
            rsCells = qryCell.executeSelect();
            
            int runCount = 0;
            int runBeginPosition = -1;
            int currentCellValue = -10;
            int currentCellPosition = -1;
            int availableCells[] = new int[SpacesRequired];
            while (rsCells.next())
            {
                int currentCellCol = rsCells.getInt("CELL_intColNo");
                int currentCellRow = rsCells.getInt("CELL_intRowNo");
               
                currentCellValue = rsCells.getInt("CELL_intBiospecimenID");
                currentCellPosition = (trayRows * (currentCellRow - 1)) + currentCellCol;
                {
                    if (currentCellValue == -1)
                    {
                        if (runBeginPosition == -1)
                            runBeginPosition = currentCellPosition;
                        runCount++;
            //            System.out.println ("Run count : " + runCount);
                        if (traysToSearch > 0)
                        {
                            availableCells[runCount - 1] = rsCells.getInt("CELL_intCellID");
                            
                        }
                        
                        if (runCount >= SpacesRequired)
                        {
                            if (traysToSearch > 0)
                            {
                                vtAvailableTrayKeys.add(availableCells);
                                availableCells = shiftArray(availableCells);
                                runCount--;
                                runBeginPosition++;
                            }
                            else
                            {
                               vtAvailableTrayKeys.add(trayKey + "");
                               rsCells.close();
                               break;
                            }
                        }
                    }
                    else
                    {
                        runBeginPosition = -1;
                        runCount = 0;
                        if (traysToSearch > 0)
                           availableCells = new int[SpacesRequired];
                    }
                }
                
            }
            rsCells.close();
            
            
        }
        
        rs.close();
        
        if (vtAvailableTrayKeys.size() > 0)
           return vtAvailableTrayKeys;
        else
           return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    private int[] shiftArray(int[] availableCells)
    {
        int newArray[] = new int[availableCells.length];
        for (int i = 1; i < availableCells.length; i++)
        {
            newArray[i - 1] = availableCells[i];
        }
        
        return newArray;
        
    }
    
    
    
    public Vector discoverAvailableSingleTray(GenerationDetails gd)
    {
        // do a select on the tray
        
        // get the trays with the available space
        
        Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
        Vector vtAvailableLocations = new Vector (10, 10);
        
        try {
            DALQuery query = new DALQuery();
            query.setDomain("TRAY", null, null, null);
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
            query.setFields(vtAvailableLocationForm, null);
            query.setWhere(null, 0, "TRAY_intTrayAvailable", ">=", gd.getAllocationAmount() + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            
            
            if (gd.getSiteID() != WILDCARD)
            {
                query.setWhere("AND", 0, "SITE_intSiteID", "=", gd.getSiteID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
                
                
            if (gd.getTankID() != WILDCARD)
            {
                query.setWhere("AND", 0, "TANK_intTankID", "=", gd.getTankID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
                
                
            if (gd.getBoxID() != WILDCARD)
            {
                query.setWhere("AND", 0, "BOX_intBoxID", "=", gd.getBoxID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
                
                
            if (gd.getTrayID() != WILDCARD)
            {
                query.setWhere("AND", 0, "TRAY_intTrayID", "=", gd.getTrayID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            
            
            query.setOrderBy("SITE_strSiteName", "ASC");
            query.setOrderBy("TANK_strTankName", "ASC");
            query.setOrderBy("BOX_strBoxName", "ASC");
            query.setOrderBy("TRAY_strTrayName", "ASC");
            ResultSet rsResult = query.executeSelect();
            
            int trayKey;
            
            BGUtilities bgUtil = new BGUtilities();
            Hashtable trayDetails = bgUtil.getTrayDetails();
            
            while (rsResult.next())
            {
               trayKey = rsResult.getInt("TRAY_intTrayID");
               // if (trayKey != null)
               // {
                   vtAvailableLocations.add(new InventoryDetails (trayDetails, trayKey, null));
               // }
                   
            }
            rsResult.close();
            return vtAvailableLocations;
            
        
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     *
     *  Returns a vector of all the available locations that contain enough non-continuous space
     *  within one tray
     *
     *  Passing in null for traysToSearch will search all trays
     *
     */
    public Vector discoverAvailableSingleTray(int SpacesRequired, Vector traysToSearch)
    {
        // do a select on the tray
        
        // get the trays with the available space
        
        Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
        Vector vtAvailableLocations = new Vector (10, 10);
        
        try {
            DALQuery query = new DALQuery();
            query.setDomain("TRAY", null, null, null);
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
            query.setFields(vtAvailableLocationForm, null);
            query.setWhere(null, 0, "TRAY_intTrayAvailable", ">=", SpacesRequired + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SITE_strSiteName", "ASC");
            query.setOrderBy("TANK_strTankName", "ASC");
            query.setOrderBy("BOX_strBoxName", "ASC");
            query.setOrderBy("TRAY_strTrayName", "ASC");
            ResultSet rsResult = query.executeSelect();
            
            String trayKey;
            while (rsResult.next())
            {
               trayKey = rsResult.getString("TRAY_intTrayID");
               if (trayKey != null)
               {
                   vtAvailableLocations.add(trayKey);
               }
                   
            }
            rsResult.close();
            return vtAvailableLocations;
            
        
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     *
     *  Allocates all of the biospecimens to the inventory
     *
     *  Will first attempt to lock all the positions, if a lock can not be aquired, false
     *  will be returned (maybe int might be better to match up with an appropriate error message)
     *
     *  Locations to use requires a list of the cell keys, toAllocate requires the biospecimen
     *  IDs that are going to be allocated.
     *
     *
     */
    
    
    public boolean allocateToInventory(Vector locationsToUse, Vector toAllocate)
    {
       return false;
    }
    
    private Hashtable getBiospecimenData(int intInternalBiospecimenID) 
    {
        Vector vtParentBiospecDetail = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen");
        Hashtable htBiospecimenDetails = new Hashtable();
        
        try {
            DALSecurityQuery query = new DALSecurityQuery();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(vtParentBiospecDetail, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intInternalBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            if (rsResult.next()) 
            {
                for (int i=0; i < vtParentBiospecDetail.size(); i++) {
                    String strFieldName = (String) vtParentBiospecDetail.get(i);
                    String strFieldValue = rsResult.getString(strFieldName);
                    
                    if (strFieldName.equals("BIOSPECIMEN_dtSampleDate") || strFieldName.equals("BIOSPECIMEN_dtExtractedDate")) {
                        if (strFieldValue != null && strFieldValue.length() > 0)
                            htBiospecimenDetails.put(strFieldName, Utilities.convertDateForDisplay(strFieldValue));
                    }
                    else {
                        htBiospecimenDetails.put(strFieldName, strFieldValue);
                    }
                }
                
                // set the parent ID and key for the sub-specimens
                htBiospecimenDetails.put("BIOSPECIMEN_intParentID", intInternalBiospecimenID + "");
                htBiospecimenDetails.put("BIOSPECIMEN_strParentID", getFriendlyBiospecimenID(intInternalBiospecimenID));
            }
            rsResult.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return htBiospecimenDetails;
    
    
    }
    
    
    public int getGenerationType(String strGenerationID)
    {
        return getGDObject(Integer.parseInt(strGenerationID)).getAllocationType();
    }
    
    
    /**
     * Getter for property intIDGenerationMode.
     * @return Value of property intIDGenerationMode.
     */
    public int getIDGenerationMode() {
        return intIDGenerationMode;
    }
    
    /**
     * Setter for property intIDGenerationMode.
     * @param intIDGenerationMode New value of property intIDGenerationMode.
     */
    public void setIDGenerationMode(int intIDGenerationMode) {
        this.intIDGenerationMode = intIDGenerationMode;
    }
    
    public String getPatientKey (String strInternalBiospecimenKey)
    {
        if (strInternalBiospecimenKey == null || strInternalBiospecimenKey.equals(""))
            return null;
        
        DALQuery query = new DALQuery();
        ResultSet rs = null;
        
        try 
        {
        
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField ("BIOSPECIMEN_intPatientID", null);
            query.setDistinctField("BIOSPECIMEN_intPatientID");
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalBiospecimenKey, 0, DALQuery.WHERE_HAS_VALUE);
        
            rs = query.executeSelect();
        }
        catch (Exception e)
        {
            System.err.println ("[Biospecimen Channel (Non-Critical)] Unable to get patient details for sidebar.  Unable to execute query. See portal log for details.");

        }
        
        try
        {
           String strReturnValue = null;
           if (rs.first() == true)
           {
              strReturnValue = rs.getString("BIOSPECIMEN_intPatientID");    
           }
           else
           {
               System.err.println ("[Biospecimen Channel (Non-Critical)] Unable to get patient details for sidebar - Unable to read result set.");

           }
           rs.close();
           return strReturnValue;
        }
        catch (Exception e)
        {
           System.err.println ("[Biospecimen Generator (Non-Critical)] Unable to get patient details for sidebar.");
           e.printStackTrace(System.err);
        }
        return null;
    }
    
    // check if a new tray has been added
    public boolean checkForAddedTray(String strGenerationID)
    {
        return getGDObject(Integer.parseInt(strGenerationID)).getNewTrayCreated();
    }
    
    
    
    // DAL Query is passed in here to maintain the transaction
    // all validation has been removed from this code
    //
    // pass in new data here
    
    
    
    
    public int addTray(String strGenerationID, String strColType, String strRowType, int intNumberOfCols, int intNumberOfRows, int intBoxID, String strTrayName)
    {
        
        Vector vtAddTrayFormFields = DatabaseSchema.getFormFields("cbiospecimen_add_inventory_tray");
        Vector vtViewTrayFormFields = DatabaseSchema.getFormFields("cbiospecimen_view_inventory_tray");
        Vector vtListBoxFormFields = DatabaseSchema.getFormFields("cbiospecimen_list_inventory_box");
        Vector vtAddCellFormFields = DatabaseSchema.getFormFields("cbiospecimen_add_inventory_cell");
        
        try
        {   
            int intNoOfRow = intNumberOfRows;
            int intNoOfCol = intNumberOfCols;
                    
                    // check data is in valid range
            if (intNoOfRow > 0 && intNoOfRow < 27 && intNoOfCol >0 && intNoOfCol < 27)
            {
                GenerationDetails gdHandle = getGDObject(Integer.parseInt(strGenerationID));
                if (gdHandle != null)
                {
                    if (gdHandle.getNewTrayCreated() == true)
                        return INVENTORY_UPDATE_TRAY_ADDED;   
                }
                    
                
                
                Hashtable htFieldData = new Hashtable();
                htFieldData.put("TRAY_intTrayCapacity", new Integer(intNoOfRow * intNoOfCol).toString());
                htFieldData.put("TRAY_intTrayAvailable", new Integer(intNoOfRow * intNoOfCol).toString());
                htFieldData.put("TRAY_strTrayName", strTrayName);
                htFieldData.put("TRAY_intBoxID", intBoxID + "");
                htFieldData.put("TRAY_intNoOfCol", intNumberOfCols + "");
                htFieldData.put("TRAY_intNoOfRow", intNumberOfRows + "");
                htFieldData.put("TRAY_strColNoType", strColType);
                htFieldData.put("TRAY_strRowNoType", strRowType);
                
                DALSecurityQuery query = new DALSecurityQuery("inventory_insert", authToken);
                
                
                // add new tray
                query.setDomain("TRAY", null, null, null);    
                query.setFields(vtAddTrayFormFields, htFieldData);
                query.executeInsert();           
                int intCurrentTrayID = QueryChannel.getNewestKeyAsInt(query);

                query.reset();
                query.setDomain("BOX", null, null, null);
                query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "LEFT JOIN");
                query.setField("TANK_intTankID", null);
                query.setField("TANK_intSiteID", null);
                query.setWhere(null, 0, "BOX_intBoxID", "=", intBoxID + "", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsThisBox = query.executeSelect();

                rsThisBox.close();
                
                // add cells for this tray
                ChannelRuntimeData[] rdMultiData = new ChannelRuntimeData[intNoOfRow*intNoOfCol];
                int intCounter = 0;
                for (int i=1; i < intNoOfRow + 1; i++)
                    for (int j=1; j < intNoOfCol + 1; j++)
                    {
                        ChannelRuntimeData rdData = new ChannelRuntimeData();
                        rdData.setParameter("CELL_intTrayID", new Integer(intCurrentTrayID).toString());
                        rdData.setParameter("CELL_intRowNo", new Integer(i).toString());
                        rdData.setParameter("CELL_intColNo", new Integer(j).toString());
                        rdMultiData[intCounter] = rdData;
                        intCounter++;
                    }
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setMultiInsert(vtAddCellFormFields, rdMultiData);
                query.executeMultiInsert();


                // update box capacity
                query.reset();
                query.setDomain("TRAY", null, null, null);
                query.setSumField("TRAY_intTrayCapacity");
                query.setSumField("TRAY_intTrayAvailable");
                query.setWhere(null, 0, "TRAY_intBoxID", "=", intBoxID + "", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResultSet = query.executeSelect();

                if (rsResultSet.next())
                {
                    String strBoxCapacity = rsResultSet.getString("SUM_TRAY_intTrayCapacity");
                    String strBoxAvailable = rsResultSet.getString("SUM_TRAY_intTrayAvail");

                    query.reset();
                    query.setDomain("BOX", null, null, null);
                    query.setField("BOX_intBoxCapacity", strBoxCapacity);
                    query.setField("BOX_intBoxAvailable", strBoxAvailable);
                    query.setWhere(null, 0, "BOX_intBoxID", "=", intBoxID + "", 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                }
                rsResultSet.close();
                
                // update tank capacity
                query.reset();
                query.setDomain("BOX", null, null, null);
                query.setField("BOX_intTankID", null);
                query.setWhere(null, 0, "BOX_intBoxID", "=", intBoxID + "", 0, DALQuery.WHERE_HAS_VALUE);
                rsResultSet = query.executeSelect();

                if (rsResultSet.next())
                {
                    String strTankID = rsResultSet.getString("BOX_intTankID");

                    query.reset();
                    query.setDomain("BOX", null, null, null);
                    query.setSumField("BOX_intBoxCapacity");
                    query.setSumField("BOX_intBoxAvailable");
                    query.setWhere(null, 0, "BOX_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsResultSet1 = query.executeSelect();

                    if (rsResultSet1.next())
                    {
                        String strTankCapacity = rsResultSet1.getString("SUM_BOX_intBoxCapacity");
                        String strTankAvailable = rsResultSet1.getString("SUM_BOX_intBoxAvailable");

                        query.reset();
                        query.setDomain("TANK", null, null, null);
                        query.setField("TANK_intTankCapacity", strTankCapacity);
                        query.setField("TANK_intTankAvailable", strTankAvailable);
                        query.setWhere(null, 0, "TANK_intTankID", "=", strTankID, 0, DALQuery.WHERE_HAS_VALUE);
                        query.executeUpdate();
                    }
                }

                rsResultSet.close();
                
                query.reset();
                query.setDomain("BOX", null, null, null);
                query.setFields(vtListBoxFormFields, null);
                query.setOrderBy("BOX_strBoxName", "ASC");
                ResultSet rsBoxList = query.executeSelect();
                
                // XXX: Why is this here?
                
                rsBoxList.close();
                
                if (gdHandle != null)
                {
                    gdHandle.clearInventoryDetails();
                    
                    // Inventory Details will automatically set to fill gaps
                    
                    
                    BGUtilities bgUtil = new BGUtilities();
                    Hashtable trayDetails = bgUtil.getTrayDetails();
                    
                    InventoryDetails idDetails = new InventoryDetails(trayDetails, intCurrentTrayID, null);
                    Vector tempDetails = new Vector(10, 10);
                    tempDetails.add(idDetails);
                    gdHandle.setInventoryDetails(tempDetails);
                    
                    // magic number can be used here, as there should be only one tray which
                    // can be allocated into which in this case is the new tray at position 0
                    gdHandle.setIntInventoryAllocationID(0);
                    gdHandle.setNewTrayCreated(true);
                    
                }
                else
                {
                    System.err.println ("[BiospecimenGenerator] Error - GD Handle is null");
                }
                
                
                
                return INVENTORY_UPDATE_TRAY_ADDED;
            }
            else
            {
                return INVENTORY_UPDATE_INVALID_SIZE;
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return UNKNOWN_EXCEPTION;
        }
    }
    
    public void setAllocationID(int intGenerationDetailsID, int intInventoryID)
    {
        GenerationDetails gdHandle = getGDObject(intGenerationDetailsID);
        if (gdHandle != null)
        {
            gdHandle.setIntInventoryAllocationID(intInventoryID, gdHandle.getAllocationType());
        }
    }
    
    
    public String getAllocationReportXML()
    {
        // cycle through all the generation objects
        
        // if no allocation has occured -- output as such
        
        // otherwise build XML for the tray
        
        // output xml containing all the ids for the specimens we've created
        
        
        
        // cycle through generation objects
        
        
        StringBuffer strXML = new StringBuffer();
        
        
        for (int i = 0; i < vtGenerationList.size(); i++)
        {
            GenerationDetails gdHandle = (GenerationDetails) vtGenerationList.get(i);
            strXML.append("<Allocation id=\"" + i + "\" state=\"" + gdHandle.getGenerationStatus() + "\">");
            if (gdHandle.getGenerationStatus() == GENERATION_SUCCESS)
            {
                int allocationPositions[] = null;
                if (gdHandle.getAllocationType() != NO_ALLOCATION)
                {
                    strXML.append(buildCellXMLFile(gdHandle));
                    allocationPositions = ((InventoryDetails) gdHandle.getInventoryDetails().get(gdHandle.getIntInventoryAllocationID())).getAvailableCells();
                }
                else
                {
                    strXML.append("<noAllocation>true</noAllocation>");
                }
                
                strXML.append("<GD_AllocationMode>" + gdHandle.getAllocationType() + "</GD_AllocationMode>");
                strXML.append("<strBiospecimenID>" + getFriendlyBiospecimenID() + "</strBiospecimenID>");
               
                Vector cloneDetails = gdHandle.getCloneIdentifiers();
                
                for (int k = 0; k < cloneDetails.size(); k++)
                {
                    strXML.append("<newGeneratedBiospecimen>");
                    CloneIdentifiers cdHandle = (CloneIdentifiers) cloneDetails.get(k);
                    strXML.append("<strBiospecimenID>" + cdHandle.getBiospecimenID() + "</strBiospecimenID>");
                    if (allocationPositions != null)
                    {
                        strXML.append("<strLocation>" + allocationPositions[k] + "</strLocation>");
                    }
                    strXML.append("</newGeneratedBiospecimen>");
                }
                
                
            }
            else
            {
                strXML.append("<Message>Was unable to allocate</Message>");
                // display with appropriate error message/note about the state of the generation
            }
            strXML.append("</Allocation>");
        }
        
        
        return strXML.toString();
        
        
    }
    
    
     private String buildCellXMLFile(GenerationDetails gd) {
        StringBuffer strXML = new StringBuffer();
        int intCounter = 0;
        
        int intAllocationID = gd.getIntInventoryAllocationID();
        
        
        InventoryDetails idDetails = (InventoryDetails) gd.getInventoryDetails().get(intAllocationID);

        int intAllocatedCells[] = idDetails.getAvailableCells();
                                    
        String intTrayID = idDetails.getTrayID() + "";
        
        try {
        
            DALSecurityQuery loopQuery = new DALSecurityQuery("biospecimen_add", authToken);
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_add", authToken);
            
            query.setDomain("TRAY", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_tray"), null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();
            strXML.append("<InventoryNames>");
            strXML.append("<strSiteName>" + idDetails.getSiteName() + "</strSiteName>");
            strXML.append("<strTankName>" + idDetails.getTankName() + "</strTankName>");
            strXML.append("<strBoxName>" + idDetails.getBoxName() + "</strBoxName>"); 
            strXML.append("<strTrayName>" + idDetails.getTrayName() + "</strTrayName>");
            strXML.append("</InventoryNames>");
            
            if (rsResultSet.next()) {
                String strRowType = rsResultSet.getString("TRAY_strRowNoType");
                String strColType = rsResultSet.getString("TRAY_strColNoType");
                int intNoOfRow = rsResultSet.getInt("TRAY_intNoOfRow");
                int intNoOfCol = rsResultSet.getInt("TRAY_intNoOfCol");
                
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setDomain("BIOSPECIMEN", "CELL_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "LEFT JOIN");
                query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_cell"), null);
                query.setWhere(null, 0, "CELL_intTrayID", "=", new Integer(intTrayID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("CELL_intRowNo", "ASC");
                query.setOrderBy("CELL_intColNo", "ASC");
                rsResultSet.close();
                rsResultSet = query.executeSelect();
                
                for (int i=0; i < intNoOfRow + 1; i++) {
                    strXML.append("<Row>");
                    for (int j=0; j < intNoOfCol + 1; j++) {
                        strXML.append("<Col>");
                        
                        if (i == 0 && j == 0) {
                            strXML.append("<label>0</label>");
                        }
                        else if (i == 0) {
                            if (strColType.equals("Alphabet"))
                                strXML.append("<label>" + ALPHABET.substring(j-1, j) + "</label>");
                            else
                                strXML.append("<label>" + j + "</label>");
                        }
                        else if (j == 0) {
                            if (strRowType.equals("Alphabet"))
                                strXML.append("<label>" + ALPHABET.substring(i-1, i) + "</label>");
                            else
                                strXML.append("<label>" + i + "</label>");
                        }
                        else {
                            rsResultSet.next();
                            int cellID = rsResultSet.getInt("CELL_intCellID");
                            int biospecimenID = rsResultSet.getInt("CELL_intBiospecimenID");
                            int patientID = rsResultSet.getInt("CELL_intPatientID");
                            
                            strXML.append("<label>-1</label>");
                            strXML.append("<CELL_intCellID>" + cellID + "</CELL_intCellID>");
                            strXML.append("<CELL_intPatientID>" + patientID + "</CELL_intPatientID>");
                            
                            if (biospecimenID == -1) {
                                strXML.append("<CELL_info>Cell ID: " + cellID + "&#10;&#13;");
                                String strRow = "";
                                String strCol = "";
                                
                                if (strRowType.equals("Alphabet"))
                                    strRow = ALPHABET.substring(i-1, i);
                                else
                                    strRow = "" + i;
                                
                                if (strColType.equals("Alphabet"))
                                    strCol = ALPHABET.substring(j-1, j);
                                else
                                    strCol = "" + j;
                                
                                strXML.append("Row: " + strRow + "&#10;&#13;");
                                strXML.append("Col: " + strCol + "&#10;&#13;");
                                strXML.append("Status: Empty</CELL_info>");
                                
                              
                                strXML.append("<CELL_intBiospecimenID>-1</CELL_intBiospecimenID>");
                            }
                            else {
                                String strBiospecimenID = rsResultSet.getString("BIOSPECIMEN_strBiospecimenID");
                                String strSampleType = rsResultSet.getString("BIOSPECIMEN_strSampleType");
                                String strSampleSubType = rsResultSet.getString("BIOSPECIMEN_strSampleSubType");
                               
                                
                                
                                
                                
                                strXML.append("<CELL_info>Cell ID: " + cellID + "&#10;&#13;");
                                
                                if (strRowType.equals("Alphabet"))
                                    strXML.append("Row: " + ALPHABET.substring(i-1, i) + "&#10;&#13;");
                                else
                                    strXML.append("Row: " + i + "&#10;&#13;");
                                
                                if (strColType.equals("Alphabet"))
                                    strXML.append("Col: " + ALPHABET.substring(j-1, j) + "&#10;&#13;");
                                else
                                    strXML.append("Col: " + j + "&#10;&#13;");
                                
                                strXML.append("Status: Full&#10;&#13;");
                                strXML.append("Biospecimen ID: " + strBiospecimenID + "&#10;&#13;");
                                strXML.append("Type: " + strSampleType + "&#10;&#13;");
                                strXML.append("Subtype: " + strSampleSubType + "&#10;&#13;</CELL_info>");
                                
                                boolean foundAllocatedCell = false;
                                if (intAllocatedCells != null)
                                {
                                    for (int alCells = 0; alCells < intAllocatedCells.length; alCells++)
                                    {
                                        if (intAllocatedCells[alCells] == cellID)
                                        {
                                           strXML.append("<CELL_intBiospecimenID>-2</CELL_intBiospecimenID>");    
                                           foundAllocatedCell = true;
                                           break;
                                        }
                                    }
                                }
                                else
                                {
                                    System.err.println ("[BiospecimenGenerator] : Warning Cell Set is null");
                                }
                                if (foundAllocatedCell == false)
                                {
                                   strXML.append("<CELL_intBiospecimenID>" + biospecimenID + "</CELL_intBiospecimenID>");
                                }
                            }
                        }
                        
                        strXML.append("</Col>");
                    }
                    strXML.append("</Row>");
                }
            }
            rsResultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return strXML.toString();
    }
    
    
}





/**
 *  CloneIdentifiers
 *
 *  Class used to track the details entered by the user for a particular
 *
 *
 *
 */

class CloneIdentifiers
{
    private String strBiospecimenID = null;
    private String strBarcodeID = null;
    private String strInternalStudyID = null;
    private int intDBBiospecimenID = -1;
    
    
    public CloneIdentifiers(String strBiospecimenID, String strBarcodeID, String strInternalStudyID)
    {
        this.strBiospecimenID = strBiospecimenID;
        this.strBarcodeID = strBarcodeID;
        this.strInternalStudyID = strInternalStudyID;    
       
    }
    
    public void setUserBiospecimenID(String strBiospecimenID)
    {
        this.strBiospecimenID = strBiospecimenID;
    }
    
    public void setBarcodeAndStudy(String strBarcodeID, String strInternalStudyID)
    {
        this.strBarcodeID = strBarcodeID;
        this.strInternalStudyID = strInternalStudyID;
    }
    
    public void setStudy(String strInternalStudyID)
    {
        this.strInternalStudyID = strInternalStudyID;
    }
    
    public void setDBBiospecimenID(int intBiospecimenID)
    {
        this.intDBBiospecimenID = intBiospecimenID;
    }
    
    public int getDBBiospecimenID()
    {
        return intDBBiospecimenID;
    }
    
    public String getBiospecimenID()
    {
        return strBiospecimenID;
    }
    
    public String getBarcodeID()
    {
        return strBarcodeID;
    }
   
    public String getStudyID()
    {
        return strInternalStudyID;
    }
    
}
