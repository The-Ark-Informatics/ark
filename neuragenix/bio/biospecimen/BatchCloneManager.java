/*
 * BatchCloneManager.java
 *
 * Created on 22 March 2005, 13:15
 */

package neuragenix.bio.biospecimen;

/**
 *
 * @author  dmurley
 */

import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.bio.utilities.*;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Enumeration;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

public class BatchCloneManager {
    
    private AuthToken authToken = null;
    private BiospecimenGenerator bpBioGenerator = null;
    
    private NGXRuntimeProperties rp = null;
    
    
    
    private static final String MISSING_ID_ERROR = "Either Barcode or Biospecimen ID is missing";
    private static final String DUPLICATE_ID_ERROR = "ID is already in use by the system";
    private static final String DUPLICATE_BAR_CODE = "Barcode is already in use by the system";
    
    public static final String PERMISSION_BATCH_CLONE = "batch_clone";
    
    /** Creates a new instance of BatchCloneManager */
    public BatchCloneManager(AuthToken authToken) 
    {
        this.authToken = authToken;
    }
    
    public BatchCloneManager(NGXRuntimeProperties rp)
    {
        this.rp = rp;
        this.authToken = rp.getAuthToken();
    }
   
    public NGXRuntimeProperties processRuntimeData(ChannelRuntimeData runtimeData)
    {
       // The below is too much functionality, and as such, needs to be moved to another class
       
       // get the property for checking empty barcodes
       boolean barcodeRequired = false;
       try
       {
          barcodeRequired = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.BatchClone.BarcodeRequired");
       }
       catch (Exception e)
       {
          System.err.println("[BatchCloneManager]: Unable to retreive property - neuragenix.bio.Biospecimen.BatchClone.BarcodeRequired");
          e.printStackTrace();
       }
                   
       Vector formFields = DatabaseSchema.getFormFields("cinventory_get_display");
        if (rp == null)
        {
            rp = new NGXRuntimeProperties();
            rp.setAuthToken(authToken);
        }
        
        String bpCloneStage = runtimeData.getParameter("stage");

        if (bpBioGenerator == null)
        {
            bpBioGenerator = new BiospecimenGenerator(rp.getAuthToken());
            String internalBiospecimenID = runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
            
            if (internalBiospecimenID != null)
            {
                bpBioGenerator.setBiospecimenID(Integer.parseInt(internalBiospecimenID));
            }
        }



        if (bpCloneStage != null)
        {
           if (bpCloneStage.equals("BACK"))
           {
              rp.setStylesheet(BiospecimenGenerator.XSL_DISPLAY_GETOPTIONS);
              rp.clearXML();
              rp.addXML("<BATCH_PROCESSING>"
                    + "<BIOSPECIMEN_strBiospecimenID>" 
                    + bpBioGenerator.getFriendlyBiospecimenID() 
                    + "</BIOSPECIMEN_strBiospecimenID>"
                    + "<BIOSPECIMEN_intBiospecimenID>" 
                    + bpBioGenerator.getBiospecimenID() 
                    + "</BIOSPECIMEN_intBiospecimenID>"
                    + InventoryUtilities.getInventoryList()
                    +  QueryChannel.buildFormLabelXMLFile(formFields)   
                    + bpBioGenerator.getAllocationSelectionXML() 
                    + "</BATCH_PROCESSING>");
           }
           else if (bpCloneStage.equals("BEGIN"))
           {
              rp.setStylesheet(BiospecimenGenerator.XSL_DISPLAY_GETOPTIONS);
              bpBioGenerator = new BiospecimenGenerator(authToken);

              String internalBiospecimenID = runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
              if (internalBiospecimenID != null)
              {
                 bpBioGenerator.setBiospecimenID(Integer.parseInt(internalBiospecimenID));
              }

              rp.clearXML();
              rp.addXML("<BATCH_PROCESSING>" 
                    + "<BIOSPECIMEN_strBiospecimenID>" 
                    + bpBioGenerator.getFriendlyBiospecimenID() 
                    + "</BIOSPECIMEN_strBiospecimenID>");
              if ( internalBiospecimenID != null){ 
                 rp.addXML("<BIOSPECIMEN_intBiospecimenID>"
                       +  internalBiospecimenID
                       +"</BIOSPECIMEN_intBiospecimenID>");
              }

              rp.addXML(QueryChannel.buildFormLabelXMLFile(formFields));

              rp.addXML( InventoryUtilities.getInventoryList()

                    + "</BATCH_PROCESSING>");
           }
           else if (bpCloneStage.equals("OPTIONSET"))
           {
              // Got the cloning stage, now get the mode/state its in

              String strCloneMode = runtimeData.getParameter("mode");

              if (strCloneMode != null)
              {
                 if (strCloneMode.equalsIgnoreCase("addnewclone"))    
                 {

                    rp.clearXML();

                    GenerationDetails gd;

                    int intSiteID = -1;
                    int intTankID = -1;
                    int intBoxID = -1;
                    int intTrayID = -1;
                    int intAllocationMode = -1;
                    int intAllocationAmount = -1;

                    try 
                    {
                       intSiteID = Integer.parseInt(runtimeData.getParameter("SITE_intSiteID") + "");
                       intTankID = Integer.parseInt(runtimeData.getParameter("TANK_intTankID") + "");
                       intBoxID = Integer.parseInt(runtimeData.getParameter("BOX_intBoxID"));
                       intTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayID"));
                       intAllocationMode = Integer.parseInt(runtimeData.getParameter("intAllocationMode"));
                       intAllocationAmount = Integer.parseInt(runtimeData.getParameter("intAllocationAmount"));

                    }
                    catch (java.lang.NumberFormatException e)
                    {
                       System.err.println ("[BiospecimenGenerator] :: Parsing Exception occured when reading in data from portal");
                       e.printStackTrace();
                    }


                    if (intSiteID != -1 && intTankID != -1 && intBoxID != -1 && intTrayID != -1)
                    {
                       gd = new GenerationDetails(intSiteID, intTankID, intBoxID, intTrayID, intAllocationMode, intAllocationAmount);
                       bpBioGenerator.addNewAllocationSelection(gd);
                    }
                    else
                    {
                       rp.addXML("<BATCH_PROCESSING><strError>Unable to add allocation</strError></BATCH_PROCESSING>");
                    }

                    rp.setStylesheet(BiospecimenGenerator.XSL_DISPLAY_GETOPTIONS);

                    rp.addXML("<BATCH_PROCESSING>" 
                          + "<BIOSPECIMEN_strBiospecimenID>" 
                          + bpBioGenerator.getFriendlyBiospecimenID() 
                          + "</BIOSPECIMEN_strBiospecimenID>" 
                          + InventoryUtilities.getInventoryList() 
                          + QueryChannel.buildFormLabelXMLFile(formFields)
                          + bpBioGenerator.getAllocationSelectionXML() 
                          + "</BATCH_PROCESSING>");


                 }
                 else if (strCloneMode.equalsIgnoreCase("deleteallocation"))
                 {
                    int intAllocationID = -1;
                    rp.clearXML();
                    try 
                    {
                       intAllocationID = Integer.parseInt(runtimeData.getParameter("intAllocationID"));
                    } 
                    catch (java.lang.NumberFormatException e)
                    {
                       rp.addXML("<strError>Unable to remove allocation - internal allocation ID is not valid</strError>");
                       System.err.println ("[BiospecimenGenerator] :: Number format exception");
                       e.printStackTrace();
                    }

                    boolean result = false;

                    if (intAllocationID != -1)
                    {
                       result = bpBioGenerator.removeAllocation(intAllocationID);
                    }

                    if (result != true)
                    {
                       System.err.println ("[BiospecimenGenerator] :: Unable to remove allocation");
                       rp.addXML("<strError>Unable to remove allocation - internal allocation ID was not found</strError>");
                    }



                    rp.setStylesheet(BiospecimenGenerator.XSL_DISPLAY_GETOPTIONS);
                    rp.addXML("<BATCH_PROCESSING>" 
                          + "<BIOSPECIMEN_intBiospecimenID>" 
                          + bpBioGenerator.getFriendlyBiospecimenID() 
                          + "</BIOSPECIMEN_intBiospecimenID>" 
                          + InventoryUtilities.getInventoryList() 
                          + bpBioGenerator.getAllocationSelectionXML() 
                          + QueryChannel.buildFormLabelXMLFile(formFields)
                          + "</BATCH_PROCESSING>");

                 }
                 else if (strCloneMode.equalsIgnoreCase("cancelclone"))
                 {

                    // TODO: Fix this
                    // Not Entirely sure how this is going to be handled
                    // Should probably call the core, and set the RP

                    // viewBiospecimen(bpBioGenerator.getBiospecimenID()+"");
                 }
                 else if (strCloneMode.equalsIgnoreCase("VALIDATION"))
                 {
                    // check the available locations for everything the user has selected
                    Vector posResults = bpBioGenerator.generateAvailableAllocations();
                    
                    rp.clearXML();

                    rp.addXML("<BiospecimenGeneration>" 
                          + bpBioGenerator.getAllocationSelectionXML() 
                          + QueryChannel.buildFormLabelXMLFile(formFields) 
                          + "</BiospecimenGeneration>");
                    rp.setStylesheet(BiospecimenGenerator.XSL_DISPLAY_FOUNDALLOCATIONS);
                    
                    
                 }

              }  // if clone mode not null
           } // check for option set

           else if (bpCloneStage.equalsIgnoreCase("ALLOCATOR"))
           {
              String strCloneMode = runtimeData.getParameter("mode");
              if (strCloneMode.equalsIgnoreCase("DOALLOCATION"))
              {

                 rp.clearXML();

                 // do the specified allocation that the user has requested.
                 // parameters : current=bp_clone  stage=ALLOCATOR  mode=DOALLOCATION   intAllocationID={GD_internalID}"
                 // TODO: need to check at this point for auto generation of IDS;
                 // todo: if IDS are being auto generated, pass a parameter to the XSL
                
                 String strStage = runtimeData.getParameter("stage");
                 String strAllocationID = runtimeData.getParameter("intAllocationID"); // represents the id that the user has picked
                 String strLocationID = runtimeData.getParameter("locationID");  // where the user wishes to put it... either a tray key or continuous key

                 if (strLocationID != null && strLocationID.equals(""))
                    strLocationID = null;

                 if (strLocationID != null) // Biospecimen Generator will hopefully pick up on this
                 {
                    bpBioGenerator.setAllocationID(Integer.parseInt(strAllocationID), Integer.parseInt(strLocationID));
                 }

                 // first stage -- get the ids for the biospecimens

                 // may need to also output the biospecimen id at this point so they remember what they are cloning

                 rp.addXML("<BiospecimenGeneration>");
                 if (strAllocationID != null)
                 {
                    rp.addXML(bpBioGenerator.buildXMLForGenerationDetails(Integer.parseInt(strAllocationID)));
                    // output a list of studies
                }

                 Vector vtAddTrayFormFields = DatabaseSchema.getFormFields("cbiospecimen_add_inventory_tray");
                 Vector vtListBoxFormFields = DatabaseSchema.getFormFields("cbiospecimen_list_inventory_box");

                 try
                 {
                    DALSecurityQuery query = new DALSecurityQuery("inventory_view", authToken);
                    query.setDomain("BOX", null, null, null);
                    query.setFields(vtListBoxFormFields, null);
                    query.setOrderBy("BOX_strBoxName", "ASC");
                    ResultSet rsBoxList = query.executeSelect();

                    rp.addXML(QueryChannel.buildSearchXMLFile("search_box", rsBoxList, vtListBoxFormFields) +
                          QueryChannel.buildFormLabelXMLFile(vtAddTrayFormFields) +
                          QueryChannel.buildAddFormXMLFile(vtAddTrayFormFields));

                    rsBoxList.close();
                 }
                 catch (Exception e)
                 {
                    LogService.instance().log(LogService.ERROR, "Unknown error in Inventory Channel - " + e.toString(), e);
                 }


                 if (bpBioGenerator.getIDGenerationMode() == BiospecimenGenerator.ID_GENERATION_AUTO)
                 {
                    rp.addXML("<AutoGenerateBiospecimenID>true</AutoGenerateBiospecimenID>");
                 }
                 else
                    rp.addXML("<AutoGenerateBiospecimenID>false</AutoGenerateBiospecimenID>");

                 // specify the inital prefix
                 IBiospecimenIDGenerator idgen = IDGenerationFactory.getBiospecimenIDGenerationInstance();
                 rp.addXML("<prefixBioID>" + idgen.getBiospecimenIDPrefix() + "</prefixBioID>");
                 
                 rp.addXML(StudyUtilities.getListOfCurrentStudiesXML(authToken) + "</BiospecimenGeneration>");


                 rp.setStylesheet(bpBioGenerator.XSL_DISPLAY_GETIDS);    

              }
              else if(strCloneMode.equalsIgnoreCase("VALIDATE"))
              {
                 // get the number of ids...
                 boolean validated = true;

                 String strValidData = null; 
                 int addTrayResult;
                 int returnCode = 0;


                 int intAllocationID = -1;
                 if (runtimeData.getParameter("intAllocationID") != null)
                 {
                    intAllocationID = Integer.parseInt(runtimeData.getParameter("intAllocationID"));
                 }

                 int intAmountToGenerate = bpBioGenerator.getAmountToGenerate(intAllocationID);

                 int intEmptyFieldKeys[] = new int[intAmountToGenerate];

                 int intNextDataValue = 0;

                 for (int i = 0; i < intEmptyFieldKeys.length; i++)
                 {
                    intEmptyFieldKeys[i] = -1;
                 }

                 int intEmptyKeysNextValue = 0;

                 // BIO ID VALIDATION

                 // check that all the items have been entered

                 Enumeration enumRD = runtimeData.getParameterNames();

                 boolean blAutoGenerateID = false;

                 if (bpBioGenerator.getIDGenerationMode() == BiospecimenGenerator.ID_GENERATION_AUTO)
                 {
                    blAutoGenerateID = true;
                 }




                 //validate data 
                 while (enumRD.hasMoreElements())
                 {
                    String strParamName = (String) enumRD.nextElement();
                    
                    if (strParamName.startsWith("barcode_") && barcodeRequired)
                        barcodeRequired = true;


                    if ((strParamName.startsWith("id_") && blAutoGenerateID == false) || barcodeRequired)
                    {
                       String paramValue = (String) runtimeData.getParameter(strParamName);
                       if (paramValue != null)
                       {
                          if (paramValue.trim().equals(""))
                          {
                             validated = false;

                             boolean foundKey = false;
                             int currentKey = Integer.parseInt(strParamName.split("_")[1]);
                             for (int i = 0; i < intEmptyFieldKeys.length; i++)
                             {
                                if (intEmptyFieldKeys[i] == currentKey)
                                {
                                   foundKey = true;
                                   break;
                                }
                             }

                             if (foundKey == false)
                             {
                                intEmptyFieldKeys[intEmptyKeysNextValue++] = currentKey;
                             }

                          }
                       }
                    }
                 }

                 if (validated == false)
                 {

                    // failure
                    // output xml of all the failures for the style sheet to pick up

                    // also re-generate the XML.

                    // make sure the original data also goes back into the styleshhet

                    Vector vtFieldsForView = new Vector(10, 2);

                    for (int i = 1; i <= intAmountToGenerate; i++)
                    {
                       vtFieldsForView.add("id_" + i);
                       vtFieldsForView.add("barcode_" + i);
                       vtFieldsForView.add("study_" + i);
                    }

                    rp.clearXML();

                    String strMissingIDErrorXML = "";
                    if (intEmptyFieldKeys != null)
                    {
                       for (int i = 0; i < intEmptyFieldKeys.length; i++)
                       {
                          strMissingIDErrorXML = "<MissingBiospecimenID error=\"" + MISSING_ID_ERROR + "\">" + intEmptyFieldKeys[i] + "</MissingBiospecimenID>";    
                       }
                    }

                    rp.addXML("<BiospecimenGeneration>" + strMissingIDErrorXML);

                    rp.addXML(QueryChannel.buildXMLFromRuntimeData(vtFieldsForView, runtimeData));
                    if (intAllocationID != -1)
                    {
                       rp.addXML(bpBioGenerator.buildXMLForGenerationDetails(intAllocationID));
                    }
                    if (bpBioGenerator.getIDGenerationMode() == BiospecimenGenerator.ID_GENERATION_AUTO)
                    {
                       rp.addXML("<AutoGenerateBiospecimenID>true</AutoGenerateBiospecimenID>");
                    }
                    else
                       rp.addXML("<AutoGenerateBiospecimenID>false</AutoGenerateBiospecimenID>");

                    rp.addXML(StudyUtilities.getListOfCurrentStudiesXML(authToken) + "</BiospecimenGeneration>");

                    // stylesheet remains the same as previous stylesheet

                 }
                 else
                 { 
                    // validate all the keys against the database to make sure they don't upset the 
                    // business logic

                    // get all the biospecimen IDS

                    String strBiospecimenIDs[] = new String[intAmountToGenerate];

                    for (int i = 0; i < intAmountToGenerate; i++)
                    {
                       strBiospecimenIDs[i] = runtimeData.getParameter("id_" + (i + 1));
                       //System.out.println("strBio["+i+"]: " + strBiospecimenIDs[i]);
                    }

                    String strBarcodeIDs[] = new String[intAmountToGenerate];

                    for (int i = 0; i < intAmountToGenerate; i++)
                    {
                       strBarcodeIDs[i] = runtimeData.getParameter("barcode_" + (i + 1));
                    }

                    String dupBioIDs[] = null;
                    String dupBarIDs[] = null;

                    //System.out.println("AutoGenerateID: "+ blAutoGenerateID);
                    if (blAutoGenerateID == false)
                    {
                       dupBioIDs = BiospecimenUtilities.checkForDuplicateIDs(strBiospecimenIDs, BiospecimenUtilities.BIOSPECIMENID);
                    }
                    
                    // Remove empty barcodes
                    String tmpBarcodeIDs[] = BiospecimenUtilities.removeEmptyBarcodes(barcodeRequired, strBarcodeIDs);
                    // if there are barcodes to check for duplicates
                    if ((tmpBarcodeIDs!=null)&&(tmpBarcodeIDs.length>0))
                    {
                        dupBarIDs = BiospecimenUtilities.checkForDuplicateIDs(tmpBarcodeIDs, BiospecimenUtilities.BARCODE);
                    }
                    
                    String strGenerationID = runtimeData.getParameter("intAllocationID");
                    if (dupBioIDs == null && dupBarIDs == null)
                    { //there is no duplicates bar and IDs

                       if (strGenerationID != null)
                       {
                          // put all the data into the object

                          CloneIdentifiers ci = null;

                          for (int i = 0; i < intAmountToGenerate; i++)
                          {

                             String strUserBiospecimenID = null;
                             //remember the IDs and Barcodes
                             if (bpBioGenerator.getIDGenerationMode() == BiospecimenGenerator.ID_GENERATION_MANUAL)
                             {
                                strUserBiospecimenID = runtimeData.getParameter("id_" + (i + 1));
                             }
                             String strUserBarcodeID = runtimeData.getParameter("barcode_" + (i + 1));
                             String strUserStudyID = runtimeData.getParameter("study_" + (i + 1));

                             ci = new CloneIdentifiers(strUserBiospecimenID, strUserBarcodeID, strUserStudyID);
                             bpBioGenerator.addCloneIdentifier(ci, Integer.parseInt(strGenerationID));
                          } 

                          // check if new tray
                          // TODO: Inventory details for this particular generation object will

                          // most likely need to be updated

                          if (bpBioGenerator.getGenerationType(strGenerationID) == BiospecimenGenerator.NEW_TRAY)
                          {

                             if (bpBioGenerator.checkForAddedTray(strGenerationID) == false)
                             {
                                Vector vtAddTrayFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_tray");

                                // add new tray after checking for valid data
                                if  ( ((strValidData = QueryChannel.validateData(vtAddTrayFormFields, runtimeData)) == null)
                                      &&  ((strValidData = QueryChannel.checkRequiredFields(vtAddTrayFormFields, runtimeData)) == null)){
                                   String strColType = runtimeData.getParameter("TRAY_strColNoType");
                                   String strRowType = runtimeData.getParameter("TRAY_strRowNoType");

                                   int intNumberOfCols = Integer.parseInt(runtimeData.getParameter("TRAY_intNoOfCol"));
                                   int intNumberOfRows = Integer.parseInt(runtimeData.getParameter("TRAY_intNoOfRow"));
                                   String strTrayName = runtimeData.getParameter("TRAY_strTrayName");
                                   int intBoxID = Integer.parseInt(runtimeData.getParameter("TRAY_intBoxID"));


                                   addTrayResult = bpBioGenerator.addTray(strGenerationID, strColType, strRowType, intNumberOfCols, intNumberOfRows, intBoxID, strTrayName);
                                   //only generate when succesfully added a new tray
                                   if (addTrayResult == BiospecimenGenerator.INVENTORY_UPDATE_TRAY_ADDED)
                                      returnCode = bpBioGenerator.doGeneration(Integer.parseInt(strGenerationID)); 

                                      }
                                else {
                                   //need to set error message here
                                   addTrayResult = 0; // put dummy in addTrayResult
                                   Vector vtFieldsForView = new Vector(10, 2);

                                   for (int i = 1; i <= intAmountToGenerate; i++)
                                   {
                                      vtFieldsForView.add("id_" + i);
                                      vtFieldsForView.add("barcode_" + i);
                                      vtFieldsForView.add("study_" + i);
                                   }

                                   rp.clearXML();
                                   //if error when adding new tray
                                   if (strValidData != null){
                                      rp.setErrorMessage(strValidData);
                                   }

                                   rp.addXML("<BiospecimenGeneration>");
                                   //put stuff back to rp
                                   rp.addXML(QueryChannel.buildXMLFromRuntimeData(vtFieldsForView, runtimeData));
                                   if (intAllocationID != -1)
                                   {
                                      rp.addXML(bpBioGenerator.buildXMLForGenerationDetails(intAllocationID));
                                   }
                                   if (bpBioGenerator.getIDGenerationMode() == BiospecimenGenerator.ID_GENERATION_AUTO)
                                   {
                                      rp.addXML("<AutoGenerateBiospecimenID>true</AutoGenerateBiospecimenID>");
                                   }
                                   else
                                      rp.addXML("<AutoGenerateBiospecimenID>false</AutoGenerateBiospecimenID>");



                                   rp.addXML(displayAddTrayForm());
                                   rp.addXML(StudyUtilities.getListOfCurrentStudiesXML(authToken) + "</BiospecimenGeneration>");


                                   //stylesheet not set as the previous one should suffice

                                }

                             }
                          }
                          // if not adding a new tray, generata stuff
                          else returnCode = bpBioGenerator.doGeneration(Integer.parseInt(strGenerationID));

                          if (returnCode == BiospecimenGenerator.GENERATION_SUCCESS)
                          {
                             rp.clearXML();
                             rp.addXML("<BiospecimenGeneration>");
                             rp.addXML("<strMessage>Generation Successful</strMessage>");
                             rp.addXML(bpBioGenerator.getAllocationSelectionXML() + "</BiospecimenGeneration>");
                             rp.setStylesheet(BiospecimenGenerator.XSL_DISPLAY_FOUNDALLOCATIONS);
                          }

                       }
                       else
                          System.err.println ("[BiospecimenGenerator] :: Generation id was null");
                       // sucess, no duplicates, and all fields have values
                       // study is a given as the user is forced to select a value
                       // and as such -- we wont check.

                       // put all the data into the biospecimen geneerator

                       // do the allocation (if works -- status should be updated)

                       // record all the details of the allocation into an audit log
                       // also generate a receipt for this split, save to the database and
                       // save the id into the generation details

                    }
                    else
                    {

                       // failure
                       // output xml of all the failures for the style sheet to pick up

                       // also re-generate the XML.

                       // make sure the original data also goes back into the styleshhet
                       Vector vtFieldsForView = new Vector(10, 2);

                       for (int i = 1; i <= intAmountToGenerate; i++)
                       {
                          vtFieldsForView.add("id_" + i);
                          vtFieldsForView.add("barcode_" + i);
                          vtFieldsForView.add("study_" + i);
                       }

                       rp.clearXML();
                       //if error when adding new tray
                       if (strValidData != null){
                          rp.setErrorMessage(strValidData);
                       }

                       rp.addXML("<BiospecimenGeneration>");

                       if (dupBioIDs != null)
                       {
                          for (int i = 0; i < dupBioIDs.length; i++)
                          {
                             rp.addXML("<InvalidBiospecimenID error=\"" + DUPLICATE_ID_ERROR + "\">" + dupBioIDs[i] + "</InvalidBiospecimenID>");    
                          }
                       }

                       if (dupBarIDs != null)
                       {
                          for (int i = 0; i < dupBarIDs.length; i++)
                          {
                             rp.addXML("<InvalidBarcodeID error=\"" + DUPLICATE_BAR_CODE + "\">" + dupBarIDs[i] +  "</InvalidBarcodeID>");    
                          }
                       }

                       if (bpBioGenerator.getGenerationType(strGenerationID) == BiospecimenGenerator.NEW_TRAY)
                       {
                          rp.addXML(displayAddTrayForm());
                       }

                       rp.addXML(QueryChannel.buildXMLFromRuntimeData(vtFieldsForView, runtimeData));
                       if (intAllocationID != -1)
                       {
                          rp.addXML(bpBioGenerator.buildXMLForGenerationDetails(intAllocationID));
                       }
                       if (bpBioGenerator.getIDGenerationMode() == BiospecimenGenerator.ID_GENERATION_AUTO)
                       {
                          rp.addXML("<AutoGenerateBiospecimenID>true</AutoGenerateBiospecimenID>");
                       }
                       else
                          rp.addXML("<AutoGenerateBiospecimenID>false</AutoGenerateBiospecimenID>");
                       rp.addXML(StudyUtilities.getListOfCurrentStudiesXML(authToken) + "</BiospecimenGeneration>");
                       // stylesheet not set as the previous one should suffice
                       // back to the channel men!

                    }

                 }


                 // if any of these fail, build error message - and return -- build a xml error object
                 // make sure that all the data the user originally posted is returned

                 // otherwise put all the data into the generation details object.
                 // -- array of strings [genNumber][String bioID, String barID, int StudyID]



              }

              // current=bp_clone&stage=ALLOCATOR&mode=VALIDATE&intAllocationID=0

              // validate all the ids and barcodes


              // if valid - display confirmation and allocate

              // save audit trail.

              // allocate a unique id to this set of allocations at the beginning of the process



           }

           else if (bpCloneStage.equalsIgnoreCase("DISPLAYREPORT"))
           {
              rp.clearXML();
              rp.addXML("<BiospecimenGeneration>" + bpBioGenerator.getAllocationReportXML() + "</BiospecimenGeneration>");
              rp.setStylesheet(BiospecimenGenerator.XSL_DISPLAY_GENERATIONREPORT);
           }

        }
        else
        {
           System.err.println ("[CBiospecimen - Batch Processing] Did not receive stage parameter.  Unable to proceed");
        }

        return rp;
    }

    private String displayAddTrayForm()
    {                       
       StringBuffer strXML = new StringBuffer();
       Vector vtAddTrayFormFields = DatabaseSchema.getFormFields("cinventory_add_inventory_tray");
       Vector vtListBoxFormFields = DatabaseSchema.getFormFields("cinventory_list_inventory_box");
       try
       {
          DALSecurityQuery query = new DALSecurityQuery("inventory_view", authToken);
          query.setDomain("BOX", null, null, null);
          query.setFields(vtListBoxFormFields, null);
          query.setOrderBy("BOX_strBoxName", "ASC");
          ResultSet rsBoxList = query.executeSelect();
          strXML.append(  QueryChannel.buildSearchXMLFile("search_box", rsBoxList, vtListBoxFormFields) +
                QueryChannel.buildFormLabelXMLFile(vtAddTrayFormFields) +
                QueryChannel.buildAddFormXMLFile(vtAddTrayFormFields));
          rsBoxList.close();
       }
       catch (Exception e)
       {
          e.printStackTrace();
          LogService.instance().log(LogService.ERROR, "Unknown error in Batch Clone Manager- " + e.toString(), e);
       }
       return strXML.toString();
    }



}
