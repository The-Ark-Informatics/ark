/*
 * VialCalculator.java
 *
 * Created on 21 March 2005, 11:43
 */

package neuragenix.bio.biospecimen;

import java.util.*;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import neuragenix.bio.utilities.*;
import neuragenix.utils.IDGeneration;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.security.AuthToken;


import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;

/**
 *
 * @author  dmurley
 */

// TODO: Remove all instances of checkPermissions

public class VialCalculator
{
    
    AuthToken authToken = null;
    Vector vtNewBiospecimenIDs = null;
    Vector vtNewLocations = null;
      
    
    private final String VIAL_CALCULATION = "vial_calculation";
    
    // These are the page names that the value current (current page) can contain
    private static final String BIOSPECIMEN_ADD = "biospecimen_add"; //used
    private static final String BIOSPECIMEN_CLONE = "biospecimen_clone";
    private static final String BIOSPECIMEN_UPDATE = "biospecimen_update";
    private static final String BIOSPECIMEN_DELETE = "biospecimen_delete";
    private static final String BIOSPECIMEN_SEARCH = "biospecimen_search";
    private static final String BIOSPECIMEN_RESULTS = "biospecimen_results";
    private static final String BIOSPECIMEN_VIEW = "biospecimen_view";
    private static final String SECURITY_ERROR = "securityerror";
    private static final String BIOSPECIMEN_STUDY = "biospecimen_study";
    private static final String BIOSPECIMEN_TREE_VIEW = "biospecimen_tree_view";
    private final String BIOSPECIMEN_ATTACHMENTS = "biospecimen_attachments";
    private final String VIAL_HISTORY = "vial_history";
    private final String AVAILABLE_TRAY_LIST = "available_tray_list";
    private final String CREATE_SUB_SPECIMEN = "create_sub_specimen";
    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 

    ChannelRuntimeData vialCalRuntimeData = null;

    //used to store multiple stages when doing vial cal
    private LinkedList listDatas = new LinkedList();

    //current position in the listDatas
    private int intCurrentPosition = -1;

    // Keep generated Biospecimen IDs for plasma & GDNA
   // private Vector vtNewBiospecimenIDsForPlasmaGDNA;

    // Keep generated Biospecimen keys
    private Vector vtNewBiospecimenKeys;
   

    //number of requested biospecimen
    private int intVialStored;

    //total of created biospecimen including Plasma & GDNA
    private int intTotalBios; 
    
    // Keep location keys of new generated biospecimen
    private Vector vtNewLocationKeys = new Vector();

    //number of vial about to be allocated to inventory
    private int intAllocatedBios;

    int intBiospecimenID = 0;
    
    private Vector vtGenerationDetails = new Vector(); 
    
    private Vector vtTrayIDs = new Vector();
       
    private Vector vtTempNewLocationKeys = new Vector ();
    /** Creates a new instance of VialCalculator */
    public VialCalculator(AuthToken authToken) 
    {
       this.authToken = authToken;
    }

    //use for the whole vial cal as a transaction;
    private DALSecurityQuery query = new DALSecurityQuery();
    
   
    
   
    
    
    /** Build XML string for new generated Biospecimen IDs
    */

    public String buildNewGeneratedBiospecimenIDXMLFile(int intTotal, Vector vtNewLocations) {
       StringBuffer strResult = new StringBuffer();

          for (int i=0; i < intTotal ; i++) 
          {
             strResult.append("<newGeneratedBiospecimen>");
             strResult.append("<strBiospecimenID>System Generated</strBiospecimenID>");
             strResult.append("<strLocation>" + (i < vtNewLocations.size() ? vtNewLocations.get(i) : "NA") + "</strLocation>");
             strResult.append("</newGeneratedBiospecimen>");
            }
        
        return strResult.toString();
    }
    
    
    
    /** Perform vial calculation
     *
     *  @param runtimeData channel runtime data for all the form data
     */
    
    public String performVialCalculation(ChannelRuntimeData runtimeData) 
    {
       DecimalFormat format = new DecimalFormat("######0.00");  
       Vector vtVialCalculationForm = DatabaseSchema.getFormFields("cbiospecimen_vial_calculation");
       String strErrorMessage = null;
       StringBuffer strXML = new StringBuffer();
       long lngClinicalStatCalc = 0;
       double dbClinicalStatCalMonth = 0;

       intBiospecimenID = Integer.parseInt(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID"));

       try {
          // checks required field
          if ( (strErrorMessage = QueryChannel.checkRequiredFields(vtVialCalculationForm, runtimeData)) != null || 
                ( strErrorMessage = QueryChannel.validateData(vtVialCalculationForm, runtimeData)) != null )
          {
             return strErrorMessage; 
          }


          if (runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis") != null
                && !runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis").equals("")
                && runtimeData.getParameter("VIAL_CALCULATION_dtDateOfCollection") != null
                && !runtimeData.getParameter("VIAL_CALCULATION_dtDateOfCollection").equals(""))
          {

                java.util.Date dtGetFromSmartform = Utilities.convertStringToDate(runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis"), "dd/MM/yyyy");
                java.util.Date dtDateOfCollection = Utilities.convertStringToDate(runtimeData.getParameter("VIAL_CALCULATION_dtDateOfCollection"), "dd/MM/yyyy");

                //Clinical Stat Calc
                lngClinicalStatCalc = Math.round((dtDateOfCollection.getTime() - dtGetFromSmartform.getTime()) / 86400000.0);

                // Clinical Stat Calc Month 
                dbClinicalStatCalMonth = lngClinicalStatCalc/30.0;
          }
          
          // Manual cell count             
          long lngManCellCount = Long.parseLong(runtimeData.getParameter("VIAL_CALCULATION_intManualCellCount"));
           
          // Dillution Factor  
          long lngDillutionFactor = Long.parseLong(runtimeData.getParameter("VIAL_CALCULATION_intDilutionFactor"));
          
          if (runtimeData.getParameter("VIAL_CALCULATION_flSampleWeighting") != null && runtimeData.getParameter("VIAL_CALCULATION_intVialStored") != null 
                && runtimeData.getParameter("VIAL_CALCULATION_flSampleWeighting").length() >0 ){
            double dbSampleWeighting =  Double.parseDouble(runtimeData.getParameter("VIAL_CALCULATION_flSampleWeighting"));
            int intVialStored = Integer.parseInt(runtimeData.getParameter("VIAL_CALCULATION_intVialStored"));
            double dbWeightPerVial = 0;
            if (intVialStored != 0){
                dbWeightPerVial = (double) dbSampleWeighting / intVialStored;
            }
               
            DecimalFormat formatTemp = new DecimalFormat("######0.000");
            runtimeData.setParameter("VIAL_CALCULATION_flSampleWeightingPerVial",formatTemp.format(dbWeightPerVial));   
            
 
          }
          
          // mLs suspended
         long lngmLsSuspended= Long.parseLong(runtimeData.getParameter("VIAL_CALCULATION_flmLsSuspended"));

          double dbmLsCollectionTube = Double.parseDouble(runtimeData.getParameter("VIAL_CALCULATION_flmLsCollectionTube"));
          double dbmLsAnticosgulant = Double.parseDouble(runtimeData.getParameter("VIAL_CALCULATION_flmLsAnticosgulant"));
          long lngConcentration = Long.parseLong(runtimeData.getParameter("VIAL_CALCULATION_intConcentration"));

         // Cell/mL
          double dbCellmLs = (double) lngManCellCount * lngDillutionFactor / 100;
            // total cell count
          double dbTotalCellCount = (double) dbCellmLs * lngmLsSuspended;
           
          double dbmLsResuspended = (double) dbTotalCellCount * 1000000 / lngConcentration;
          
          double dbmLsBM = dbmLsCollectionTube - dbmLsAnticosgulant;          
   
        
          // set runtime data for parameters
          runtimeData.setParameter("VIAL_CALCULATION_intClinicalStatCalc", "" + lngClinicalStatCalc);

          runtimeData.setParameter("VIAL_CALCULATION_flClinicalStatCalcMonths", "" + format.format(dbClinicalStatCalMonth));
          runtimeData.setParameter("VIAL_CALCULATION_flCellmL", "" + format.format(dbCellmLs));
          runtimeData.setParameter("VIAL_CALCULATION_flTotalCellCount", "" + format.format(dbTotalCellCount));
          runtimeData.setParameter("VIAL_CALCULATION_flmLsResuspended", "" + format.format(dbmLsResuspended));
          runtimeData.setParameter("VIAL_CALCULATION_flTotalmLsBM", "" + format.format(dbmLsBM));
         
          vialCalRuntimeData = runtimeData;
          
          
          String strNumberOfVials = "";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intVialStored") != null
                && !vialCalRuntimeData.getParameter("VIAL_CALCULATION_intVialStored").trim().equals("")) {
             strNumberOfVials = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intVialStored");
                }
          else {
             strNumberOfVials = vialCalRuntimeData.getParameter("VIAL_CALCULATION_flmLsResuspended");
          }

          // checks if vial stored is a positive whole number
          if (strNumberOfVials.indexOf("-") >= 0) {
             strErrorMessage = "Please enter a valid data for the field: Vial stored.";
             return strErrorMessage;
          }


          // needs to change activity to VIAL_CALCULATION
          String intMRDWash = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash").trim().length() > 0) {
             intMRDWash = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash");
                }

          // checks if MRD\Wash is a positive whole number
          if (intMRDWash.indexOf("-") >= 0) {
             strErrorMessage = "Please enter a valid data for the field: MRD\\Wash.";
             return strErrorMessage;
          }


          String intMRDRNA = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA").trim().length() > 0) {
             intMRDRNA = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA");
          }

          // checks if MRD\Wash is a positive whole number
          if (intMRDRNA.indexOf("-") >= 0) {
             strErrorMessage = "Please enter a valid data for the field: MRD/RNA.";
             return strErrorMessage;
          }

          
          String intPlasma = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma")!= null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma").trim().length() > 0){
             intPlasma = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma"); 
                }

         
          String intGDNA = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA")!= null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA").trim().length() > 0){
             intGDNA = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA"); 
                }
            
          // checks if we have enough vial for MRD\Wash and ...
            double flNumberOfVials = Double.parseDouble(strNumberOfVials);
            int intNumberOfVials = (int) flNumberOfVials;
            int intMinVialNeeded =( Integer.parseInt(intMRDWash) + Integer.parseInt(intMRDRNA));
            intTotalBios = intNumberOfVials + Integer.parseInt(intPlasma) + Integer.parseInt(intGDNA);
            intAllocatedBios  = intNumberOfVials  - intMinVialNeeded;
            if (intNumberOfVials < intMinVialNeeded) {
                strErrorMessage = "The number of enerated vials is not enough for MRD\\Wash or MRD\\RNA. Please increase this number.";
                return strErrorMessage;
            }
 
          return null;

       }
        catch (Exception e) {
           e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
        return strErrorMessage;
    }
    
    
    
    private ChannelRuntimeData buildParentBiospecimenRuntimeData() {
        Vector vtParentBiospecDetail = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen");
        ChannelRuntimeData rdParentBioRuntimeData = new ChannelRuntimeData();
        
        try {
            //reset the query
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(vtParentBiospecDetail, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID+"", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            if (rsResult.next()) {
                for (int i=0; i < vtParentBiospecDetail.size(); i++) {
                    String strFieldName = (String) vtParentBiospecDetail.get(i);
                    String strFieldValue = rsResult.getString(strFieldName);
                    
                    if (strFieldName.equals("BIOSPECIMEN_dtSampleDate") || strFieldName.equals("BIOSPECIMEN_dtExtractedDate")) {
                        if (strFieldValue != null && strFieldValue.length() > 0)
                            rdParentBioRuntimeData.setParameter(strFieldName, Utilities.convertDateForDisplay(strFieldValue));
                    }
                    else if (strFieldName.equals("BIOSPECIMEN_tmExtractedTime") || strFieldName.equals("BIOSPECIMEN_tmSampleTime")){
                         if (strFieldValue != null && strFieldValue.length() > 0)
                            rdParentBioRuntimeData.setParameter(strFieldName, Utilities.convertTimeForDisplay(strFieldValue));
                    }
                    else {
                        rdParentBioRuntimeData.setParameter(strFieldName, strFieldValue);
                    }
                }
                
                // set the parent ID and key for the sub-specimens
                rdParentBioRuntimeData.setParameter("BIOSPECIMEN_intParentID", intBiospecimenID+"");
                rdParentBioRuntimeData.setParameter("BIOSPECIMEN_strParentID", BiospecimenUtilities.getUserBiospecimenID(intBiospecimenID));
                rdParentBioRuntimeData.setParameter("BIOSPECIMEN_intBiospecimenID", (String) null);
               
                
            }
            rsResult.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
        
        return rdParentBioRuntimeData;
    }

    
   
    
    
    
    /** Complete the vial calculation wizard
     */
       
    public String doFinishVialCalculation(ChannelRuntimeData runtimeData) {
        
        listDatas.clear();
        intCurrentPosition = -1;


        String strErrors = null;
        String strBiospecimenStringID;
        vtNewBiospecimenKeys = new Vector(10, 5);
        
        Vector vtInsertSubspecimen = DatabaseSchema.getFormFields("cbiospecimen_insert_sub_biospecimen");
        Vector vtUpdateInvCell = DatabaseSchema.getFormFields("cbiospecimen_update_inventory_cell");
        Vector vtInsertVialDetails = DatabaseSchema.getFormFields("cbiospecimen_insert_vial_details");
        Vector vtVialHistoryDetails = DatabaseSchema.getFormFields("cbiospecimen_vial_calculation_insert");
        
        Vector vtAddBioQuantity = DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_transaction");
        Vector vtUpdateQuantity = DatabaseSchema.getFormFields("cbiospecimen_update_biospecimen_quantity");
        //used for quantity
        ChannelRuntimeData  rdBioQuantityRuntimeData  = null;

         // set default strMRDWash
        String strMRDWash = "0";
        if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash").trim().length() > 0) {
            strMRDWash = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash");
        }

        String strMRDRNA = "0";
        if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA").trim().length() > 0) {
            strMRDRNA = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA");
        }

         String strPlasma= "0";
        if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma").trim().length() > 0) {
            strPlasma = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma");
        }

        String strGDNA = "0";
        if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA").trim().length() > 0) {
            strGDNA = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA");
        }

        //boolean blMRDWash = (vialCalRuntimeData.getParameter("VIAL_CALCULATION_strMRDWash").equals("Yes") ? true : false);
        //boolean blMRDRNA = (vialCalRuntimeData.getParameter("VIAL_CALCULATION_strMRDRNA").equals("Yes") ? true : false);

        int intMRDWash = Integer.parseInt(strMRDWash);
        int intMRDRNA = Integer.parseInt(strMRDRNA);
        int intPlasma = Integer.parseInt(strPlasma);
        int intGDNA = Integer.parseInt(strGDNA);

        int intTotalRemoved = intMRDWash + intMRDRNA + intPlasma + intGDNA;
        java.util.Date today = new java.util.Date();
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        ResultSet rsResult;
        // use this to save bios, quantity and update inventory
        BiospecimenCore bcBiospecimen = new BiospecimenCore(authToken); 

        // used for insert vial details
        vialCalRuntimeData.setParameter("VIAL_DETAILS_strCollectionTube", vialCalRuntimeData.getParameter("VIAL_CALCULATION_strCollectionTube"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_intMRDWash", vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_intMRDRNA", vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flTotalmLsBM", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flTotalmLsBM"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_intClinicalStatCalc", vialCalRuntimeData.getParameter("VIAL_CALCULATION_intClinicalStatCalc"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_intConcentration", vialCalRuntimeData.getParameter("VIAL_CALCULATION_intConcentration"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flTotalCellCount", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flTotalCellCount"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flmLsAnticosgulant", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flmLsAnticosgulant"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flCellmL", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flCellmL"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flmLsCollectionTube", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flmLsCollectionTube"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flmLsSuspended", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flmLsSuspended"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_intSecondLook", vialCalRuntimeData.getParameter("VIAL_CALCULATION_intSecondLook"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_intDilutionFactor", vialCalRuntimeData.getParameter("VIAL_CALCULATION_intDilutionFactor"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flLsResuspended", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flmLsResuspended"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_intManualCellCount", vialCalRuntimeData.getParameter("VIAL_CALCULATION_intManualCellCount"));
        vialCalRuntimeData.setParameter("VIAL_DETAILS_flClinicalStatCalcMonths", vialCalRuntimeData.getParameter("VIAL_CALCULATION_flClinicalStatCalcMonths"));
        
        //set for Bioquanity transaction
        rdBioQuantityRuntimeData = new ChannelRuntimeData();
        rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity", "1");
        rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strUnit", bcBiospecimen.requiredQuantity);
        rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_dtTransactionDate", fm.format(today));
        rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strRecordedBy", authToken.getUserIdentifier());
       
         
        
        //create a vector of strBiospecimenID
        vtNewBiospecimenIDs = new Vector(10,5);
        
        try {
            
            
            // create runtime data for sub-bios
            ChannelRuntimeData rdSubBioRuntimeData = buildParentBiospecimenRuntimeData();
                       
            //get parent string ID and use it as prefix
            String strParentStringID = BiospecimenUtilities.getUserBiospecimenID(intBiospecimenID);
            
            String strIDPrefix = null;
            // insert new generated sub-bios
            for (int i=0; i < intTotalBios; i++) {
                
               
                 query.reset();  
               //get a new string ID for this sub biospecimen
               //use strParentID as prefix
                strIDPrefix = strParentStringID;
                
                
               // determine the prefix based on the the type 
                if (intMRDWash > 0){
                   strIDPrefix = strIDPrefix + "-MRDG";
                }
                else if (intMRDRNA > 0){
                   strIDPrefix = strIDPrefix + "-MRDRNA";
                }
                else if (intPlasma > 0){
                   strIDPrefix = strIDPrefix + "-P";
                }
                else if (intGDNA >0 ){
                   strIDPrefix = strIDPrefix + "-G";
                 } 
                
                strBiospecimenStringID =  BiospecimenUtilities.getNewSubBiospecimenStringID(query, strIDPrefix,1);
                rdSubBioRuntimeData.setParameter("BIOSPECIMEN_strBiospecimenID",strBiospecimenStringID);
                //added to the vector to later used by the vial calc history
                vtNewBiospecimenIDs.add(strBiospecimenStringID);
                
                rdSubBioRuntimeData.setParameter("BIOSPECIMEN_strSampleType", vialCalRuntimeData.getParameter("VIAL_CALCULATION_strSampleType"));
                
                
                int intIndex;
                intIndex =  i - intTotalRemoved;
                
                
                
                //else rdSubBioRuntimeData.setParameter("BIOSPECIMEN_intCellID", "-1");
        


                rdSubBioRuntimeData.setParameter("blFromVialCalc", true+""); 
                //adding into database
                strErrors = bcBiospecimen.saveBiospecimen(rdSubBioRuntimeData);
                
                if (strErrors != null){
                  return strErrors;
                }
                             
                // get the key
                //String strNewBiospecimenKey = QueryChannel.getNewestKeyAsString(query);
                String strNewBiospecimenKey = bcBiospecimen.getLastBiospecimenAddedID()+"";
                vtNewBiospecimenKeys.add(strNewBiospecimenKey);
                
                // add a vial into bio-quantity
                // create runtime data for bio-quantity
                rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", strNewBiospecimenKey);
                rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strReason", "From vial calculation");
                rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity", "1");
                //save quantity and update in biospecimen
                strErrors  = bcBiospecimen.doSaveQuantity(strNewBiospecimenKey, rdBioQuantityRuntimeData);
                if (strErrors != null ){
                  return strErrors;
                }  
               // if MRD Wash

                //get ready for Wash, RNA , Plasma and RDNA
                rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity", "-1");
                rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strUnit", bcBiospecimen.requiredQuantity);

                if (intMRDWash > 0){ 

                   rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strReason", "For MRD\\Wash");
                   strErrors =  bcBiospecimen.doSaveQuantity(strNewBiospecimenKey, rdBioQuantityRuntimeData);
                   intMRDWash--;
                   if (strErrors != null ){
                      return strErrors;
                   }

                }
                else if (intMRDRNA > 0) {

                   rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strReason", "For MRD\\RNA");
                   strErrors = bcBiospecimen.doSaveQuantity(strNewBiospecimenKey, rdBioQuantityRuntimeData);
                   intMRDRNA--;
                   if (strErrors != null ){
                      return strErrors;
                   }

                }
                else if (intPlasma > 0) {
                   rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strReason", "For Plasma");
                   strErrors = bcBiospecimen.doSaveQuantity(strNewBiospecimenKey, rdBioQuantityRuntimeData);
                   intPlasma--;
                   if (strErrors != null ){
                      return strErrors;
                   }
                }
                else if (intGDNA > 0) {
                   rdBioQuantityRuntimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strReason", "For GDNA");
                   strErrors = bcBiospecimen.doSaveQuantity(strNewBiospecimenKey, rdBioQuantityRuntimeData);
                   if (strErrors != null ){
                      return strErrors;
                   }
                   intGDNA--;
                }




                //save vial details 
                vialCalRuntimeData.setParameter("VIAL_DETAILS_intBiospecimenID", strNewBiospecimenKey);
                query.reset();
                query.setDomain("VIAL_DETAILS", null, null, null);
                query.setFields(vtInsertVialDetails, vialCalRuntimeData);
                query.executeInsert();

                if ( intIndex >=0 && intIndex < vtNewLocationKeys.size()){  
                    int intCellID = Integer.parseInt((String) vtNewLocationKeys.get(intIndex));
                    if (intCellID > 0)
                       InventoryUtilities.saveBiospecimenLocation(bcBiospecimen.getLastBiospecimenAddedID(), intCellID, this.authToken);

                }
                               
            }
            
            // insert vial history
            // get BIOSPECIMEN ID from current runtimeData
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_intBiospecimenID",intBiospecimenID+"");
            String strTrayIDs = "";
            //add Tray IDs in 
            for (int i=0; i<vtTrayIDs.size(); i++){
                strTrayIDs += "," + (String) vtTrayIDs.get(i);
                
            }
            
            
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_strTrayID",strTrayIDs);
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_strUserName", authToken.getUserIdentifier());
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_dtDateOfCalculation", fm.format(today));
            
            String strBiospecimenKeys = "";
            String strBiospecimenIDs = "";
            String strLocations = "";
            String strLocationIDs = "";
            
            for (int i=0; i < intTotalBios; i++) {
                strBiospecimenIDs += vtNewBiospecimenIDs.get(i) + ",";
                strBiospecimenKeys += vtNewBiospecimenKeys.get(i) + ",";
                int intIndexi = i - intTotalRemoved; 
                if ( intIndexi >= 0 && intIndexi < vtNewLocationKeys.size()){
                  //strLocations +=   vtNewLocations.get(intIndexi) + "," ; 
                  strLocationIDs +=  vtNewLocationKeys.get(intIndexi) + ",";
                }
                else {
                  //strLocations +=   "NA," ; 
                  strLocationIDs +=  "NA," ;
                }
           }
            if ( strBiospecimenIDs.length() > 0)
               strBiospecimenIDs = strBiospecimenIDs.substring(0, strBiospecimenIDs.length()-1);
            if ( strBiospecimenKeys.length() > 0)
               strBiospecimenKeys = strBiospecimenKeys.substring(0, strBiospecimenKeys.length()-1);
            if (strLocations.length() > 0)
               strLocations = strLocations.substring(0, strLocations.length()-1);
            if (strLocationIDs.length()  > 0)
               strLocationIDs = strLocationIDs.substring(0, strLocationIDs.length()-1);
         
         
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_strBiospecimenKeys", strBiospecimenKeys);
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_strBiospecimenIDs", strBiospecimenIDs);
            //vialCalRuntimeData.setParameter("VIAL_CALCULATION_strLocations", strLocations);
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_strLocationIDs", strLocationIDs);
            
            vialCalRuntimeData.setParameter("VIAL_CALCULATION_intSaved", "0"); 
            // check to see if vial calc data has been saved before
            String strBioKey = String.valueOf(intBiospecimenID);
            if (previouslySaved(strBioKey)) // doing an update
            {
                query.reset();
                query.setDomain("VIAL_CALCULATION", null, null, null);
                query.setFields(vtVialHistoryDetails, vialCalRuntimeData);
                query.setWhere(null, 0, "VIAL_CALCULATION_intBiospecimenID", "=", strBioKey + "", 0, DALQuery.WHERE_HAS_VALUE);

                if (!query.executeUpdate())
                {
                    strErrors = "Finish Update: Error saving data!";
                }
                /*else
                {
                    System.out.println ("Updated vial calc details for: " + strBioKey);
                }*/
            }
            else // doing an insert
            {
                query.reset();
                query.setDomain("VIAL_CALCULATION", null, null, null);
                query.setFields(vtVialHistoryDetails, vialCalRuntimeData);
                if (!query.executeInsert())
                {
                    strErrors = "Insert Update: Error saving data!";
                }
                /*else
                {
                    System.out.println ("Inserted vial calc details");
                }*/
            }

            //upto here everything is fine
            query.commitTransaction();
                        
            // back to view the biospecimen
            return strErrors;
        }
        catch (Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
            try {
                query.cancelTransaction();
            }
            catch(Exception ex){
                LogService.instance().log(LogService.ERROR, "Cant cancel the transaction", ex);
            }
        }
        return strErrors;
    }
    
    
   
    public String buildVialHistoryForm(ChannelRuntimeData runtimeData) {
        Vector vtVialHistory = DatabaseSchema.getFormFields("cbiospecimen_vial_calculation_insert");
        Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
        String strVialHistoryKey = runtimeData.getParameter("VIAL_CALCULATION_intVialCalculationKey");
        StringBuffer strXML = new StringBuffer();
        
        try {
            DALSecurityQuery query = new DALSecurityQuery(BIOSPECIMEN_ADD, authToken);
            query.setDomain("VIAL_CALCULATION", null, null, null);
            query.setFields(vtVialHistory, null);
            query.setWhere(null, 0, "VIAL_CALCULATION_intVialCalculationKey", "=", strVialHistoryKey, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
           
            String strIDs=null;
            if (rsResult.next())
            {
                String strTrayIDs = rsResult.getString("VIAL_CALCULATION_strTrayID");
                
                //get the key
                strIDs = rsResult.getString("VIAL_CALCULATION_strBiospecimenKeys");
                Vector vtKeys = new Vector();
                StringTokenizer tok;
                if ( strIDs != null){
                   tok = new StringTokenizer(strIDs, ",");

                   //create a vector of keys
                   while (tok.hasMoreElements())
                   {
                      vtKeys.add(tok.nextToken());
                   }
                }
                
                if (strTrayIDs != null){
                   tok = new StringTokenizer(strTrayIDs,",");

                   //looping through each tray
                   while(tok.hasMoreTokens())
                   {
                      strXML.append("<Allocation>" +
                            buildCellXMLForHistory(Integer.parseInt(tok.nextToken()), vtKeys)
                            + "</Allocation>");

                   }
                }

               if (  rsResult.getString("VIAL_CALCULATION_strBiospecimenIDs") != null){
                 tok = new StringTokenizer(  rsResult.getString("VIAL_CALCULATION_strBiospecimenIDs"),","); 
                       
                 while ( tok.hasMoreElements()){
                    strXML.append("<subIDs>" + tok.nextToken() + "</subIDs>"   ); 
                 }
                 
               }
               
            }
                
            
            strXML.append( QueryChannel.buildFormLabelXMLFile(vtVialHistory) +
            QueryChannel.buildViewFromKeyXMLFile(vtVialHistory, "VIAL_CALCULATION_intVialCalculationKey", strVialHistoryKey));
            strXML.append("<VIAL_CALCULATION_strBiospecimenKeys>"+strIDs+"</VIAL_CALCULATION_strBiospecimenKeys>");
            strXML.append( "<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
            strXML.append( "<intBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") + "</intBiospecimenID>");
            strXML.append( "<intParentBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_intParentID") + "</intParentBiospecimenID>");
            strXML.append( "<strBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID") + "</strBiospecimenID>");
            rsResult.close();
            //strXML.append( "<BIOSPECIMEN_strInitialBiospecSampleType>" + runtimeData.getParameter("BIOSPECIMEN_strInitialBiospecSampleType") + "BIOSPECIMEN_</strInitialBiospecSampleType>");
            ////System.out.println("VIAL CAL" + strXML.toString());
            
            // build the biospecimenSideTree
            strXML.append(this.buildSideTree(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID")));
            // build the patientSideDetails
            strXML.append(this.buildSidePatientDetails(Integer.parseInt(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID"))));

        }
        catch (Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in Biospecimen Channel - " + e.toString(), e);
        }
        return strXML.toString();
    }
    
     /** Build the form to show created sub specimens and
     *  their location
     */
    
    public String buildCreateSubSpecimenForm(int intTrayID, int intLocationNeeded) {
        Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
        StringBuffer strXML  = new StringBuffer(); 
        String strErrorMessage = null;
        Vector vtNewLocations = new Vector();
        ChannelRuntimeData runtimeData = this.vialCalRuntimeData;
        try {
            
           if (!authToken.hasActivity("biospecimen_add")){
              return "Not allowed to add new biospecimen";
           }
            String strParentID = runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID");
           runtimeData.setParameter("TRAY_intTrayID",runtimeData.getParameter("TRAY_0"));
            
            if (runtimeData.getParameter("VIAL_CALCULATION_intVialStored")!= null){
               intVialStored = Integer.parseInt(runtimeData.getParameter("VIAL_CALCULATION_intVialStored"));
            }

            // needs to change activity to VIAL_CALCULATION
            query.reset(); 
            query.setDomain("TRAY", null, null, null);
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
            query.setFields(vtAvailableLocationForm, null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID+ "", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            //build sites details
            strXML.append(QueryChannel.buildSearchXMLFile("search_location", rsResult, vtAvailableLocationForm) +
            //build view for preview
            buildCellXMLFile(intTrayID, intLocationNeeded, vtNewLocations) +
            
            
            buildNewGeneratedBiospecimenIDXMLFile(intLocationNeeded, vtNewLocations));
            
            
           
            strXML.append("<BIOSPECIMEN_intInternalPatientID>" + runtimeData.getParameter("BIOSPECIMEN_intInternalPatientID") + "</BIOSPECIMEN_intInternalPatientID>");
            strXML.append("<BIOSPECIMEN_intBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") + "</BIOSPECIMEN_intBiospecimenID>");
            strXML.append("<BIOSPECIMEN_strBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID") + "</BIOSPECIMEN_strBiospecimenID>");
            strXML.append("<BIOSPECIMEN_strInitialBiospecSampleType>" + runtimeData.getParameter("BIOSPECIMEN_strInitialBiospecSampleType") + "</BIOSPECIMEN_strInitialBiospecSampleType>");
            strXML.append("<VIAL_CALCULATION_intVialStored>" + runtimeData.getParameter("VIAL_CALCULATION_intVialStored") + "</VIAL_CALCULATION_intVialStored>");
            strXML.append("<TRAY_intTrayID>" + runtimeData.getParameter("TRAY_intTrayID") + "</TRAY_intTrayID>");
            strXML.append("<VIAL_CALCULATION_intLocationNeeded>" + runtimeData.getParameter("VIAL_CALCULATION_intLocationNeeded") + "</VIAL_CALCULATION_intLocationNeeded>"); 
            
            // Add the DisplayNames
            DBField siteField = (DBField)DatabaseSchema.getFields().get("SITE_strSiteName");        
            strXML.append("<SITE_strSiteNameDisplay>" + siteField.getLabelInForm() + "</SITE_strSiteNameDisplay>");
            DBField tankField = (DBField)DatabaseSchema.getFields().get("TANK_strTankName");        
            strXML.append("<TANK_strTankNameDisplay>" + tankField.getLabelInForm() + "</TANK_strTankNameDisplay>");
            DBField boxField = (DBField)DatabaseSchema.getFields().get("BOX_strBoxName");        
            strXML.append("<BOX_strBoxNameDisplay>" + boxField.getLabelInForm() + "</BOX_strBoxNameDisplay>");
            DBField trayField = (DBField)DatabaseSchema.getFields().get("TRAY_strTrayName");        
            strXML.append("<TRAY_strTrayNameDisplay>" + trayField.getLabelInForm() + "</TRAY_strTrayNameDisplay>");
            
            rsResult.close();
            return strXML.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
        return "";
    }
    
     
    /** Display the list of all available trays
     *  for sub-specimens allocation, 
         @return null if every thing ok, strErrorMessage if some errors occur       

     */
    
    public  String buildAvailableTrayList(NGXRuntimeProperties rp) {
        
        
        StringBuffer strXML = new StringBuffer();
        String strErrorMessage = null;
        
        Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
        Vector vtVialCalculationForm = DatabaseSchema.getFormFields("cbiospecimen_vial_calculation");
       
        //get a copy of latest vialCalRuntimeData, gonna use it when saving this in doFinishVialCalculation() function
        //vialCalRuntimeData = vialCalRuntimeData;
        
        try {
           if (!authToken.hasActivity("biospecimen_add")){
               rp.setErrorMessage("Not Allowed to Add Biospecimen");
               return "Not allowed to add biospecimen";               
           }


           // checks required field
          if ( (strErrorMessage = QueryChannel.checkRequiredFields(vtVialCalculationForm, vialCalRuntimeData)) != null || 
                ( strErrorMessage = QueryChannel.validateData(vtVialCalculationForm, vialCalRuntimeData)) != null )
          {
             return strErrorMessage; 
          }


          String strNumberOfVials = "";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intVialStored") != null
                && !vialCalRuntimeData.getParameter("VIAL_CALCULATION_intVialStored").trim().equals("")) {
             strNumberOfVials = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intVialStored");
                }
          else {
             strNumberOfVials = vialCalRuntimeData.getParameter("VIAL_CALCULATION_flmLsResuspended");
          }

          // checks if vial stored is a positive whole number
          if (strNumberOfVials.indexOf("-") >= 0) {
             strErrorMessage = "Please enter a valid data for the field: Vial stored.";
             return strErrorMessage;
          }


          // needs to change activity to VIAL_CALCULATION
          String intMRDWash = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash").trim().length() > 0) {
             intMRDWash = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash");
                }

          // checks if MRD\Wash is a positive whole number
          if (intMRDWash.indexOf("-") >= 0) {
             strErrorMessage = "Please enter a valid data for the field: MRD\\Wash.";
             return strErrorMessage;
          }


          String intMRDRNA = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA") != null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA").trim().length() > 0) {
             intMRDRNA = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA");
           }

          // checks if MRD\Wash is a positive whole number
          if (intMRDRNA.indexOf("-") >= 0) {
             strErrorMessage = "Please enter a valid data for the field: MRD/RNA.";
             return strErrorMessage;
          }

          
          String intPlasma = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma")!= null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma").trim().length() > 0){
             intPlasma = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma"); 
                }

         
          String intGDNA = "0";
          if (vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA")!= null
                && vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA").trim().length() > 0){
             intGDNA = vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA"); 
                }
            
          // checks if we have enough vial for MRD\Wash and ...
            double flNumberOfVials = Double.parseDouble(strNumberOfVials);
            int intNumberOfVials = (int) flNumberOfVials;
            int intMinVialNeeded =( Integer.parseInt(intMRDWash) + Integer.parseInt(intMRDRNA));
            intTotalBios = intNumberOfVials + Integer.parseInt(intPlasma) + Integer.parseInt(intGDNA);

            ////System.out.println("VIAL CAL :needed " + intMinVialNeeded);
            if (intNumberOfVials < intMinVialNeeded) {
                strErrorMessage = "The number of enerated vials is not enough for MRD\\Wash or MRD\\RNA. Please increase this number.";
                return strErrorMessage;
            }

            
                

          
            //DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);
            query.reset();
            query.setDomain("TRAY", null, null, null);
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");

            query.setFields(vtAvailableLocationForm, null);
            query.setWhere(null, 0, "TRAY_intTrayAvailable", ">=", (intNumberOfVials - intMinVialNeeded) + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SITE_strSiteName", "ASC");
            query.setOrderBy("TANK_strTankName", "ASC");
            query.setOrderBy("BOX_strBoxName", "ASC");
            query.setOrderBy("TRAY_strTrayName", "ASC");
            ResultSet rsResult = query.executeSelect();

            strXML.append(QueryChannel.buildFormLabelXMLFile(vtAvailableLocationForm) +
            QueryChannel.buildSearchXMLFile("search_location", rsResult, vtAvailableLocationForm));
           
           
            strXML.append("<intInternalPatientID>" + vialCalRuntimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
            strXML.append("<BIOSPECIMEN_intBiospecimenID>" + vialCalRuntimeData.getParameter("BIOSPECIMEN_intBiospecimenID") + "</BIOSPECIMEN_intBiospecimenID>");
            strXML.append("<BIOSPECIMEN_strBiospecimenID>" + vialCalRuntimeData.getParameter("BIOSPECIMEN_strBiospecimenID") + "</BIOSPECIMEN_strBiospecimenID>");
            strXML.append("<BIOSPECIMEN_strInitialBiospecSampleType>" + vialCalRuntimeData.getParameter("BIOSPECIMEN_strInitialBiospecSampleType") + "</BIOSPECIMEN_strInitialBiospecSampleType>");
            strXML.append("<VIAL_CALCULATION_intVialStored>" + intNumberOfVials + "</VIAL_CALCULATION_intVialStored>");
            strXML.append("<VIAL_CALCULATION_intLocationNeeded>" + (intNumberOfVials - intMinVialNeeded) + "</VIAL_CALCULATION_intLocationNeeded>");
            strXML.append("<VIAL_CALCULATION_intPlasma>" + vialCalRuntimeData.getParameter("VIAL_CALCULATION_intPlasma") + "</VIAL_CALCULATION_intPlasma>");
            strXML.append("<VIAL_CALCULATION_intGDNA>" + vialCalRuntimeData.getParameter("VIAL_CALCULATION_intGDNA") + "</VIAL_CALCULATION_intGDNA>");

            rp.addXML("<biospecimen>" + strXML.toString() + "</biospecimen>");
            
            rsResult.close();
        }
        catch (Exception e) {
           e.printStackTrace();
           LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
         return strErrorMessage;
    }
    
     /** Build the XML string for displaying
     */
    
    private String buildCellXMLForHistory(int intTrayID, Vector vtBiospecimenIDs){
        
       String strXML = "";
        int intCounter = 0;
        
        Hashtable htIDLocations = new Hashtable();
        try {
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);
            query.setDomain("TRAY", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_tray"), null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID+"", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();
            
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
                
                rsResultSet = query.executeSelect();
                
                for (int i=0; i < intNoOfRow + 1; i++) {
                    strXML += "<Row>";
                    for (int j=0; j < intNoOfCol + 1; j++) {
                        strXML += "<Col>";
                        
                        if (i == 0 && j == 0) {
                            strXML += "<label>0</label>";
                        }
                        else if (i == 0) {
                            if (strColType.equals("Alphabet"))
                                strXML += "<label>" + ALPHABET.substring(j-1, j) + "</label>";
                            else
                                strXML += "<label>" + j + "</label>";
                        }
                        else if (j == 0) {
                            if (strRowType.equals("Alphabet"))
                                strXML += "<label>" + ALPHABET.substring(i-1, i) + "</label>";
                            else
                                strXML += "<label>" + i + "</label>";
                        }
                        else {
                            rsResultSet.next();
                            int cellID = rsResultSet.getInt("CELL_intCellID");
                            int biospecimenID = rsResultSet.getInt("CELL_intBiospecimenID");
                            int patientID = rsResultSet.getInt("CELL_intPatientID");
                            
                            strXML += "<label>-1</label>";
                            strXML += "<CELL_intCellID>" + cellID + "</CELL_intCellID>";
                            strXML += "<CELL_intPatientID>" + patientID + "</CELL_intPatientID>";
                            
                            // if the cell contains the key listed in allocated list
                            if (vtBiospecimenIDs.contains(biospecimenID+"")) {
                                strXML += "<CELL_info>Cell ID: " + cellID + "&#10;&#13;";
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
                                
                                strXML += "Row: " + strRow + "&#10;&#13;";
                                strXML += "Col: " + strCol + "&#10;&#13;";
                                strXML += "Status: Empty</CELL_info>";
                                
                                //put to keep track
                                htIDLocations.put(biospecimenID+"", strCol + strRow);
                                //mark as green in the map
                                strXML += "<CELL_intBiospecimenID>-2</CELL_intBiospecimenID>";

                            }
                            else {
                                String strBiospecimenID = rsResultSet.getString("BIOSPECIMEN_strBiospecimenID");
                                String strSampleType = rsResultSet.getString("BIOSPECIMEN_strSampleType");
                                String strSampleSubType = rsResultSet.getString("BIOSPECIMEN_strSampleSubType");
                                                          
                                strXML += "<CELL_intBiospecimenID>" + biospecimenID + "</CELL_intBiospecimenID>";
                            }
                        }
                        
                        strXML += "</Col>";
                    }
                    strXML += "</Row>";
                }
               Enumeration enumeration = htIDLocations.keys();
               
               
               //location details for each cells
               while (enumeration.hasMoreElements()){
                   String strKey = (String) enumeration.nextElement();
                    strXML += "<biospecimens>";
                   strXML+= "<strBiospecimenID>" +BiospecimenUtilities.getUserBiospecimenID(Integer.parseInt(strKey) )+ "</strBiospecimenID>";
                   strXML+= "<strLocation>" +htIDLocations.get(strKey) + "</strLocation>";
                   strXML += "</biospecimens>";
               }
          
               //Tray Details
               
                Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
                DALSecurityQuery query1 = new DALSecurityQuery();
                query1.setDomain("TRAY", null, null, null);
                query1.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
                
                query1.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
                query1.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
                query1.setFields(vtAvailableLocationForm, null);
                
                query1.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID+"", 0, DALQuery.WHERE_HAS_VALUE);
                
                ResultSet rsResultSet1 = query1.executeSelect();
                
                if (rsResultSet1.next()) {
                    strXML+=( "<strSiteName>" + rsResultSet1.getString("SITE_strSiteName") + "</strSiteName>");
                    strXML+= ("<strTankName>" + rsResultSet1.getString("TANK_strTankName") + "</strTankName>");
                    strXML+=( "<strBoxName>" + rsResultSet1.getString("BOX_strBoxName") + "</strBoxName>");
                    strXML+=( "<strTrayName>" + rsResultSet1.getString("TRAY_strTrayName") + "</strTrayName>");
                }
               
               
               
            }
            rsResultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in Biospecimen Channel - " + e.toString(), e);
        }
        
        return strXML;
      
    }
    
    
    private String buildCellXMLFile(int intTrayID, int intNoOfNewGeneratedBiospecimen, Vector vtNewLocations) throws java.sql.SQLException
    {
        String strXML = "";
        int intCounter = 0;
        boolean valid = false;
        ResultSet rsResultSet = null;
        String strRowType = new String();
        String strColType = new String();
        int intNoOfRow = -1;
        int intNoOfCol = -1;
        
        //vtNewLocations = new Vector(10, 5);
        //vtNewLocationKeys = new Vector(10, 5);
        
        try 
        {
            //DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);
            query.reset();
            query.setDomain("TRAY", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_tray"), null);
            query.setWhere(null, 0, "TRAY_intTrayID", "=", intTrayID+"", 0, DALQuery.WHERE_HAS_VALUE);
            rsResultSet = query.executeSelect();
            
            if (rsResultSet.next())
        	{
                strRowType = rsResultSet.getString("TRAY_strRowNoType");
                strColType = rsResultSet.getString("TRAY_strColNoType");
                intNoOfRow = rsResultSet.getInt("TRAY_intNoOfRow");
                intNoOfCol = rsResultSet.getInt("TRAY_intNoOfCol");
                valid = true;
            }
            rsResultSet.close();
            rsResultSet = null;
            if (valid)
            {
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setDomain("BIOSPECIMEN", "CELL_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "LEFT JOIN");
                query.setFields(DatabaseSchema.getFormFields("cbiospecimen_view_inventory_cell"), null);
                query.setWhere(null, 0, "CELL_intTrayID", "=", new Integer(intTrayID).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("CELL_intRowNo", "ASC");
                query.setOrderBy("CELL_intColNo", "ASC");
                
                rsResultSet = query.executeSelect();
                
                for (int i=0; i < intNoOfRow + 1; i++) {
                    strXML += "<Row>";
                    for (int j=0; j < intNoOfCol + 1; j++) {
                        strXML += "<Col>";
                        
                        if (i == 0 && j == 0) {
                            strXML += "<label>0</label>";
                        }
                        else if (i == 0) {
                            if (strColType.equals("Alphabet"))
                                strXML += "<label>" + ALPHABET.substring(j-1, j) + "</label>";
                            else
                                strXML += "<label>" + j + "</label>";
                        }
                        else if (j == 0) {
                            if (strRowType.equals("Alphabet"))
                                strXML += "<label>" + ALPHABET.substring(i-1, i) + "</label>";
                            else
                                strXML += "<label>" + i + "</label>";
                        }
                        else {
                            rsResultSet.next();
                            int cellID = rsResultSet.getInt("CELL_intCellID");
                            int biospecimenID = rsResultSet.getInt("CELL_intBiospecimenID");
                            int patientID = rsResultSet.getInt("CELL_intPatientID");
                            
                            strXML += "<label>-1</label>";
                            strXML += "<CELL_intCellID>" + cellID + "</CELL_intCellID>";
                            strXML += "<CELL_intPatientID>" + patientID + "</CELL_intPatientID>";
                            
                            if (biospecimenID == -1) {
                                strXML += "<CELL_info>Cell ID: " + cellID + "&#10;&#13;";
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
                                
                                strXML += "Row: " + strRow + "&#10;&#13;";
                                strXML += "Col: " + strCol + "&#10;&#13;";
                                strXML += "Status: Empty</CELL_info>";
                                
                                //System.out.println("KEYS" + vtNewLocationKeys);
                                if (intCounter < intNoOfNewGeneratedBiospecimen && !vtNewLocationKeys.contains(cellID+"")) {
                                    intCounter++;
                                    ;
                                    vtNewLocations.add(strCol + strRow);
                                    vtTempNewLocationKeys.add(cellID+"");
                                    //strXML += "Status: Ready to allocate</CELL_info>";
                                    strXML += "<CELL_intBiospecimenID>-2</CELL_intBiospecimenID>";
                                    
                                }
                                else{
                                    //strXML += "Status: Empty </CELL_info>";
                                    strXML += "<CELL_intBiospecimenID>-1</CELL_intBiospecimenID>";
                                    
                                }
                            }
                            else {
                                String strBiospecimenID = rsResultSet.getString("BIOSPECIMEN_strBiospecimenID");
                                String strSampleType = rsResultSet.getString("BIOSPECIMEN_strSampleType");
                                String strSampleSubType = rsResultSet.getString("BIOSPECIMEN_strSampleSubType");
                                
                                strXML += "<CELL_info>Cell ID: " + cellID + "&#10;&#13;";
                                
                                if (strRowType.equals("Alphabet"))
                                    strXML += "Row: " + ALPHABET.substring(i-1, i) + "&#10;&#13;";
                                else
                                    strXML += "Row: " + i + "&#10;&#13;";
                                
                                if (strColType.equals("Alphabet"))
                                    strXML += "Col: " + ALPHABET.substring(j-1, j) + "&#10;&#13;";
                                else
                                    strXML += "Col: " + j + "&#10;&#13;";
                                
                                strXML += "Status: Full&#10;&#13;";
                                strXML += "Biospecimen ID: " + strBiospecimenID + "&#10;&#13;";
                                strXML += "Type: " + strSampleType + "&#10;&#13;";
                                strXML += "Subtype: " + strSampleSubType + "&#10;&#13;</CELL_info>";
                                strXML += "<CELL_intBiospecimenID>" + biospecimenID + "</CELL_intBiospecimenID>";
                            }
                        }
                        
                        strXML += "</Col>";
                    }
                    strXML += "</Row>";
                }
                rsResultSet.close();
            }
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Biospecimen Channel - " + e.toString(), e);
        }
        finally
        {
            if (rsResultSet!=null)
            	rsResultSet.close();
        }
        
        return strXML;
    }
    
    
    
    
    
    
    
     /** Build the form to perform vials calculation
     */
    public String buildVialCalculationForm(ChannelRuntimeData runtimeData) 
    {
        
        StringBuffer strXML = new StringBuffer();
        Vector vtVialCalculationForm = DatabaseSchema.getFormFields("cbiospecimen_vial_calculation");
        
        try {
            
            
            query.reset();   
            query.setDomain("ADMISSIONS", null, null, null);
            query.setField("ADMISSIONS_dtDiagnosis", null);
            query.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", runtimeData.getParameter("intInternalPatientID"), 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("ADMISSIONS_dtDiagnosis", "ASC");
            ResultSet rsResult = query.executeSelect();
            
            if (rsResult.next()) {
            	String temp =null;
                String dtDiagnosis = Utilities.convertDateForDisplay(rsResult.getString("ADMISSIONS_dtDiagnosis"));
                //System.out.println("DATE:::"+dtDiagnosis);
                if (runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis_Day") != null
                	&& runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis_Month") != null
                	&& runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis_Year") != null)
                {
                	temp  = runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis_Day")+ "/"
                	+ runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis_Month") + "/"
                	+ runtimeData.getParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis_Year");
                }
                //System.out.println("DATE1:::"+temp);
                if (temp !=null && dtDiagnosis.equalsIgnoreCase(temp)==false)
                	dtDiagnosis = temp;
                runtimeData.setParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis", dtDiagnosis);
              
            }
            
            rsResult.close();
            
            strXML.append(QueryChannel.buildFormLabelXMLFile(vtVialCalculationForm)) ;

            
            //get sample type
            runtimeData.setParameter("VIAL_CALCULATION_strSampleType", BiospecimenUtilities.getBiospecimenSampleType(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID")));
            strXML.append("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
            strXML.append("<BIOSPECIMEN_intBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") + "</BIOSPECIMEN_intBiospecimenID>");
            strXML.append("<BIOSPECIMEN_strBiospecimenID>" + runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID") + "</BIOSPECIMEN_strBiospecimenID>");
            strXML.append("<BIOSPECIMEN_strInitialBiospecSampleType>" + runtimeData.getParameter("BIOSPECIMEN_strInitialBiospecSampleType") + "</BIOSPECIMEN_strInitialBiospecSampleType>");
            
           // get date of collection
            if ( runtimeData.getParameter("BIOSPECIMEN_dtSampleDate") != null )
            {
                runtimeData.setParameter("VIAL_CALCULATION_dtDateOfCollection",  runtimeData.getParameter("BIOSPECIMEN_dtSampleDate"));
            }
            
            if (runtimeData.getParameter("BIOSPECIMEN_dtExtractedDate") != null)
            {
                // get date of processing
                runtimeData.setParameter("VIAL_CALCULATION_dtDateOfProcessing", runtimeData.getParameter("BIOSPECIMEN_dtExtractedDate")); 
            }
            
            strXML.append(QueryChannel.buildViewXMLFile(vtVialCalculationForm, runtimeData));
            ////System.out.println(strXML.toString()); 
            
            // build the biospecimenSideTree
            strXML.append(this.buildSideTree(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID")));
            // build the patientSideDetails
            strXML.append(this.buildSidePatientDetails(Integer.parseInt(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID"))));


        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
        return strXML.toString();
        
    }

   public ChannelRuntimeData getPreviousRuntimeData(){
      if (intCurrentPosition > 0 && listDatas.size() >0){
         intCurrentPosition--; 
         //System.out.println("currentPost " + intCurrentPosition);
         //System.out.println("current Size " + listDatas.size() );
         return (ChannelRuntimeData) listDatas.get(intCurrentPosition);
      }
      else return null; 
   }


   public ChannelRuntimeData getNextRuntimeData(){
      if ( intCurrentPosition < listDatas.size()-1 ){
         //System.out.println("currentPost " + intCurrentPosition);
         //System.out.println("current Size " + listDatas.size() );
         return (ChannelRuntimeData) listDatas.get(++intCurrentPosition);
      }
      else return null;
   }

   public void addRuntimeData(ChannelRuntimeData rd){
      if ( rd != null) {

         //System.out.println("IN ADDING THINGS");
         //remove the stages that we have traversed back
         while (intCurrentPosition != (listDatas.size() - 1)  && listDatas.size() > 0){
            listDatas.removeLast();
            //System.out.println("REMOVING, NEW SIZE: " + listDatas.size());
            //System.out.println("REMOVING, Curent size: " + intCurrentPosition);
         }
         listDatas.addLast(rd);
         intCurrentPosition++;

         //System.out.println("currentPost " + intCurrentPosition);
         //System.out.println("current Size " + listDatas.size() );
      }
   }

   public boolean hasRuntimeData(ChannelRuntimeData rd){
   
      return listDatas.contains(rd);
   }

   public int getNumberOfAllocatedBios(){
      return this.intAllocatedBios;
   }


   
     public void addNewAllocationSelection(int intSiteID, int intTankID, int intBoxID, int intTrayID, int intAllocationType, int intAllocationAmount)
    {
        GenerationDetails gd = new GenerationDetails(intSiteID, intTankID, intBoxID, intTrayID, intAllocationType, intAllocationAmount);
        addNewAllocationSelection(gd);
        
    }
    
    public void addNewAllocationSelection (GenerationDetails gd)
    {
        vtGenerationDetails.add(gd);
    }
    
    
      public String getAllocationSelectionXML()
    {
        StringBuffer strXML = new StringBuffer();
        
        BGUtilities bgUtil = new BGUtilities();
        
        for (int i = 0; i < vtGenerationDetails.size(); i++)
        {
            strXML.append(((GenerationDetails) vtGenerationDetails.get(i)).translateToXML(bgUtil.getCellDetails(), i));
        }
        
        return strXML.toString();
    }
   
      
      public boolean removeAllocation(int allocationID)
      {
          if (allocationID < vtGenerationDetails.size())
          {
              vtGenerationDetails.remove(allocationID);
              return true;
          }
          return false;
      }
      
      
    /** Cycle through the vector and get all the available allocations
     * it returns a vector of generation details with list of available options
     *
     *
     */
    public Vector generateAvailableAllocations()
    {
        //Vector vtAvailableAllocations = new Vector(10, 10);
        GenerationDetails tempHandle = null;
        for (int i = 0; i < vtGenerationDetails.size(); i++)
        {
             
            tempHandle = (GenerationDetails) vtGenerationDetails.get(i);
            //only look for the ones that not allocated. save some CPU cycle 
            if ( tempHandle.getGenerationStatus() != GenerationDetails.GENERATION_SUCCESS) {
               Vector locations = tempHandle.getAvailableLocations(query);
               if (locations != null)
               {
                  tempHandle.setInventoryDetails(locations);
               
               }
            }
            
        }
        return vtGenerationDetails;
    }
    
   /** This function is used when user click on confirm this allocation on Tray Preview page
    *
    *
    */
    
    public void setAllocationID(int intGenerationDetailsID, int intInventoryID)
    {
        GenerationDetails gdHandle = (GenerationDetails) vtGenerationDetails.get(intGenerationDetailsID);
        
        if (gdHandle != null)
        {
            gdHandle.setIntInventoryAllocationID(intInventoryID, gdHandle.getAllocationType());
            
            //add the new allocated Cells into vector of cell
            //create one if we dont have
            if ( vtNewLocationKeys == null )
                vtNewLocationKeys = new Vector();
            
            
            //get inventory object that was chosen
            InventoryDetails inventory = (InventoryDetails) gdHandle.getInventoryDetails().get(gdHandle.getIntInventoryAllocationID());
   

            //reset the site, tank, tray, box in generation details to the ones of InventoryDetails
            
            if ( inventory != null && inventory instanceof InventoryDetails){
               gdHandle.setSiteName(inventory.getSiteName());
               gdHandle.setTankName(inventory.getTankName());
               gdHandle.setBoxName(inventory.getBoxName());
               gdHandle.setTrayName(inventory.getTrayName());
            }
            
            //grep cell keys
            //int[] arrayOfCellKeys = inventory.getAvailableCells(gdHandle.getAllocationAmount(),query);
            


            
            //add chosen keys to vtNewLocationsKeys used when saving later on
            for (int i = 0; i < vtTempNewLocationKeys.size(); i++){
                vtNewLocationKeys.add((String) vtTempNewLocationKeys.get(i));
                
            }
                        
            gdHandle.setGenerationStatus(GenerationDetails.GENERATION_SUCCESS);
            if ( !vtTrayIDs.contains(intInventoryID+""))
                vtTrayIDs.add(intInventoryID+"");
             vtTempNewLocationKeys.clear();
            
        }
    }
    
    public String previewTrayAllocation(int intGenerationDetailsID, int intTrayID ){
        
        GenerationDetails gdHandle = (GenerationDetails) vtGenerationDetails.get(intGenerationDetailsID);
        
        if (gdHandle != null)
        {
           
            //InventoryDetails inventory = (InventoryDetails) gdHandle.getInventoryDetails().get(intInventoryID);
            
            //build XML for previewing the tray
            return this.buildCreateSubSpecimenForm(intTrayID, gdHandle.getAllocationAmount());
            
        }
        return null;
        
    }
    
    
    public boolean isEnoughSpecimen(){
        int intAddedBios = 0;
        for (int i = 0; i< vtGenerationDetails.size(); i++){
            intAddedBios += ((GenerationDetails)vtGenerationDetails.get(i)).getAllocationAmount();
        }
   
        if ( intAddedBios == intAllocatedBios)
            return true;
        else  return false;
         
    }
    
    /** Check whether the vial cal is ready to be finished
     */
    public boolean isFinishable(){
        return vtNewLocationKeys.size() == intAllocatedBios;
         
                 
    }
    
      
    /**
     * Getter for property intBiospecimenID.
     * @return Value of property intBiospecimenID.
     */
    public int getIntBiospecimenID()
    {
        return intBiospecimenID;
    }    
      
    /**
     * Setter for property intBiospecimenID.
     * @param intBiospecimenID New value of property intBiospecimenID.
     */
    public void setIntBiospecimenID(int intBiospecimenID)
    {
        this.intBiospecimenID = intBiospecimenID;
    }    
      
    /**
     * Getter for property query.
     * @return Value of property query.
     */
    public DALSecurityQuery getQuery()
    {
        return query;
    }
    
    /**
     * Setter for property query.
     * @param query New value of property query.
     */
    public void setQuery(DALSecurityQuery query)
    {
        this.query = query;
        query.setManualCommit(true);
    }
    
    /**
     *  Function determines if the sample previously saved the vial calculation details.
     */
    public boolean previouslySaved(String strBioKey)
    {
        boolean savedEarlier = false;

        try
        {
            DALQuery query = new DALQuery();
            ResultSet rs = null;
            query.setDomain("VIAL_CALCULATION", null, null, null);
            query.setField("VIAL_CALCULATION_intSaved", null);
            query.setWhere(null, 0, "VIAL_CALCULATION_intBiospecimenID", "=", strBioKey, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "VIAL_CALCULATION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first())     // data retrieved from the result set
            {
                int saved = Integer.parseInt(rs.getString("VIAL_CALCULATION_intSaved"));
                if (saved == 1)
                    savedEarlier = true;
                else
                    savedEarlier = false;
            }
            else    // no data retrieved from the result set
            {
                    savedEarlier = false;
            }
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[VialCalculator::previouslySaved] Unexpected error ", e);
            e.printStackTrace();
        }
        return savedEarlier;
    }

    /**
     *  Function updates the runtimeData with the new data from the database
     */
    public void updateSavedData(ChannelRuntimeData tempRD)
    {
        String strBioKey = tempRD.getParameter("BIOSPECIMEN_intBiospecimenID");
        Vector vtVialCalculationForm = DatabaseSchema.getFormFields("cbiospecimen_vial_calculation_insert");

        try
        {
            DALQuery query = new DALQuery();
            ResultSet rs = null;
            query.setDomain("VIAL_CALCULATION", null, null, null);
            query.setFields(vtVialCalculationForm, null);
            query.setWhere(null, 0, "VIAL_CALCULATION_intBiospecimenID", "=", strBioKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "VIAL_CALCULATION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "VIAL_CALCULATION_intSaved", "=", "1", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first())     // update runtime data
            {
                tempRD.setParameter("VIAL_CALCULATION_flSampleWeighting", (String) rs.getString("VIAL_CALCULATION_flSampleWeighting"));
                tempRD.setParameter("VIAL_CALCULATION_flSampleWeightingPerVial", (String) rs.getString("VIAL_CALCULATION_flSampleWeightingPerVial"));
                tempRD.setParameter("VIAL_CALCULATION_intMRDWash", (String) rs.getString("VIAL_CALCULATION_intMRDWash"));
                tempRD.setParameter("VIAL_CALCULATION_intMRDRNA", (String) rs.getString("VIAL_CALCULATION_intMRDRNA"));
                tempRD.setParameter("VIAL_CALCULATION_flmLsCollectionTube", (String) rs.getString("VIAL_CALCULATION_flmLsCollectionTube"));
                tempRD.setParameter("VIAL_CALCULATION_flmLsAnticosgulant", (String) rs.getString("VIAL_CALCULATION_flmLsAnticosgulant"));
                tempRD.setParameter("VIAL_CALCULATION_strCollectionTube", (String) rs.getString("VIAL_CALCULATION_strCollectionTube"));
                //tempRD.setParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis", (String) rs.getString("VIAL_CALCULATION_dtDateOfFirstDiagnosis"));
                tempRD.setParameter("VIAL_CALCULATION_flTotalmLsBM", (String) rs.getString("VIAL_CALCULATION_flTotalmLsBM"));
                tempRD.setParameter("VIAL_CALCULATION_flmLsResuspended", (String) rs.getString("VIAL_CALCULATION_flmLsResuspended"));
                tempRD.setParameter("VIAL_CALCULATION_flTotalCellCount", (String) rs.getString("VIAL_CALCULATION_flTotalCellCount"));
                tempRD.setParameter("VIAL_CALCULATION_intClinicalStatCalc", (String) rs.getString("VIAL_CALCULATION_intClinicalStatCalc"));
                tempRD.setParameter("VIAL_CALCULATION_intConcentration", (String) rs.getString("VIAL_CALCULATION_intConcentration"));
                //tempRD.setParameter("VIAL_CALCULATION_dtDateOfCollection", (String) rs.getString("VIAL_CALCULATION_dtDateOfCollection"));
                tempRD.setParameter("VIAL_CALCULATION_flmLsSuspended", (String) rs.getString("VIAL_CALCULATION_flmLsSuspended"));
                tempRD.setParameter("VIAL_CALCULATION_flmLsResuspended", (String) rs.getString("VIAL_CALCULATION_flmLsResuspended"));
                tempRD.setParameter("VIAL_CALCULATION_intVialStored", (String) rs.getString("VIAL_CALCULATION_intVialStored"));
                //tempRD.setParameter("VIAL_CALCULATION_strSampleType", (String) rs.getString("VIAL_CALCULATION_strSampleType"));
                //tempRD.setParameter("VIAL_CALCULATION_dtDateOfProcessing", (String) rs.getString("VIAL_CALCULATION_dtDateOfProcessing"));
                tempRD.setParameter("VIAL_CALCULATION_intSecondLook", (String) rs.getString("VIAL_CALCULATION_intSecondLook"));
                tempRD.setParameter("VIAL_CALCULATION_intManualCellCount", (String) rs.getString("VIAL_CALCULATION_intManualCellCount"));
                tempRD.setParameter("VIAL_CALCULATION_intDilutionFactor", (String) rs.getString("VIAL_CALCULATION_intDilutionFactor"));
                tempRD.setParameter("VIAL_CALCULATION_flCellmL", (String) rs.getString("VIAL_CALCULATION_flCellmL"));
                tempRD.setParameter("VIAL_CALCULATION_flClinicalStatCalcMonths", (String) rs.getString("VIAL_CALCULATION_flClinicalStatCalcMonths"));
                tempRD.setParameter("VIAL_CALCULATION_intPlasma", (String) rs.getString("VIAL_CALCULATION_intPlasma"));
                tempRD.setParameter("VIAL_CALCULATION_intGDNA", (String) rs.getString("VIAL_CALCULATION_intGDNA"));
                tempRD.setParameter("VIAL_CALCULATION_strClinicalStatus", (String) rs.getString("VIAL_CALCULATION_strClinicalStatus"));
                tempRD.setParameter("VIAL_CALCULATION_flSurvivalPeriod", (String) rs.getString("VIAL_CALCULATION_flSurvivalPeriod"));
            }
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[VialCalculator::previouslySaved] Unexpected error ", e);
            e.printStackTrace();
        }
    }
    
    /**
     *
     */
    public String saveVialCalculation (ChannelRuntimeData tempRD)
    {
        String strBioKey = tempRD.getParameter("BIOSPECIMEN_intBiospecimenID");
        Vector vtVialCalculationForm = DatabaseSchema.getFormFields("cbiospecimen_vial_calculation_insert");
        DALQuery query = new DALQuery();
        ResultSet rs = null;
        String strErrors = new String();
        strErrors = null;
        
        try
        {
            tempRD.setParameter("VIAL_CALCULATION_intSaved", "1"); 
            tempRD.setParameter("VIAL_CALCULATION_intBiospecimenID", strBioKey);
            
            // check to see if vial calc data has been saved before
            if (previouslySaved(strBioKey)) // doing an update
            {
                query.setDomain("VIAL_CALCULATION", null, null, null);
                query.setFields(vtVialCalculationForm, tempRD);
                query.setWhere(null, 0, "VIAL_CALCULATION_intBiospecimenID", "=", strBioKey + "", 0, DALQuery.WHERE_HAS_VALUE);

                if (!query.executeUpdate())
                {
                    strErrors = "Update: Error saving data!";
                }
                /*else
                {
                    System.out.println ("Update: qry all good for key: ");
                }*/
            }
            else // doing an insert
            {
                query.setDomain("VIAL_CALCULATION", null, null, null);
                query.setFields(vtVialCalculationForm, tempRD);

                if (!query.executeInsert())
                {
                    strErrors = "Insert: Error saving data!";
                }
                /*else
                {
                    String newKey = QueryChannel.getNewestKeyAsString(query);
                    System.out.println ("Insert: qry all good for key: " + newKey);
                }*/
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[VialCalculator::saveVialCalculation] Unexpected error ", e);
            e.printStackTrace();
        }
        return strErrors;
    }
    
    public StringBuffer buildSideTree (String strBiospecimenKey)
    {
        StringBuffer sbXML = new StringBuffer();
        try
        {
            // build the XML
            BiospecimenSideTree bst = null;

            boolean blUseSideTree = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.useSideTree");

            if (blUseSideTree == true)
            {
                try
                {
                    bst = new BiospecimenSideTree(authToken);
                    bst.buildBiospecimenTree(strBiospecimenKey);
                    sbXML.append(bst.buildXMLForBiospecimenSideTree(strBiospecimenKey));
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[VialCalculator::buildSideTree] Unexpected error ", e);
            e.printStackTrace();
        }
        //System.out.println("[Vial Calculator::buildSideTree] XML = " + sbXML);
        return sbXML;
    }

    public StringBuffer buildSidePatientDetails (int intBiospecID)
    {
        StringBuffer sbXML = new StringBuffer();
        try
        {
            if (authToken.hasActivity("patient_view"))
                sbXML.append("<patient_details>" + PatientUtilities.getPatientDetailsXML(PatientUtilities.getPatientKey(intBiospecID) + "", authToken) + "</patient_details>" );
        }
        catch (Exception e)
        {
            System.err.println("[Vial Calculator::buildSidePatientDetails] Error when trying to get patient details for biospecimen");
            e.printStackTrace();
        }
        //System.out.println("[Vial Calculator::buildSidePatientDetails] XML = " + sbXML);
        return sbXML;
    }
}
