package neuragenix.bio.biospecimen;

import org.jasig.portal.IChannel;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XSLT;
import org.xml.sax.ContentHandler;
import org.jasig.portal.security.*;
import org.jasig.portal.services.LogService;

import org.jasig.portal.PropertiesManager;


import java.lang.StringBuffer;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.LinkedList;

import neuragenix.security.*;
import neuragenix.dao.*;
import neuragenix.security.exception.*;
import neuragenix.common.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

import neuragenix.bio.utilities.*;


public class VialCalculationManager{
   private NGXRuntimeProperties rp = null; 
   private VialCalculator vcVialCalculator= null;
   private AuthToken authToken = null;

   //XSL styleheets
   private final static String XSL_VIAL_CALCULATION = "vial_calculation";
   private final static String XSL_AVAILABLE_TRAY = "available_tray_list";
   private final static String XSL_VIAL_CALCULATION_HISTORY = "vial_history";
   private final static String XSL_CONFIRM_VIAL_CALCULATION = "confirm_vial_calculation";


   public VialCalculationManager(NGXRuntimeProperties rp, AuthToken authToken ){
      this.authToken = authToken;
      this.rp = rp;
      this.vcVialCalculator = new VialCalculator(authToken);
      

   }

   public void processRuntimeData(ChannelRuntimeData runtimeData)
   {
       
       Vector formFields = DatabaseSchema.getFormFields("cinventory_get_display");
      // System.out.println("module Vial Caculation");
      String strErrors = null;
      String strSubAction = null;
      String strAction = null;

      try { 
         authToken.hasActivity("do_vial_calculation");
      }
      catch (Exception exc)
      {
         rp.setErrorMessage("You don't have required permission to do this action");
         rp.setStylesheet("biospecimen_action_denied");
         return;
      }

      if ( runtimeData.getParameter("action") != null )
         strAction = runtimeData.getParameter("action");

      if ( runtimeData.getParameter("subaction") != null){
         strSubAction = runtimeData.getParameter("subaction");
      }


      ChannelRuntimeData tempRuntimeData = runtimeData; 
      if (strAction.equalsIgnoreCase("back")){
         tempRuntimeData = vcVialCalculator.getPreviousRuntimeData();
         //restore the runtime data if we cant archieve from the list
         if ( tempRuntimeData == null) 
            tempRuntimeData = runtimeData;

      }

      if ( tempRuntimeData.getParameter("action") != null )
         strAction = tempRuntimeData.getParameter("action");



      if (strAction.equalsIgnoreCase("add_vial_calculation"))
      {
          //create new instance of vial calc
          vcVialCalculator = new VialCalculator(authToken);
          
          String strBioKey = tempRuntimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
          if (vcVialCalculator.previouslySaved(strBioKey))
          {
              vcVialCalculator.updateSavedData(tempRuntimeData);
          }
          
         // build the first screen of adding vial calculation
         rp.clearXML();
         rp.addXML("<biospecimen>" + vcVialCalculator.buildVialCalculationForm(tempRuntimeData)
               + "</biospecimen>");
         rp.setStylesheet(XSL_VIAL_CALCULATION);



      }

      else if (strAction.equalsIgnoreCase("perform_calculation"))
      {
         // performing calculation
         tempRuntimeData.setParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis", QueryChannel.makeDateFromForm("VIAL_CALCULATION_dtDateOfFirstDiagnosis", tempRuntimeData));
         strErrors = vcVialCalculator.performVialCalculation(tempRuntimeData);
         if ( strErrors != null)
         {
            rp.setErrorMessage(strErrors);
            // System.out.println(strErrors);
         }
         else {
            rp.setErrorMessage("Calculation performed");
            //only add if it is not yet in the list
            if (!vcVialCalculator.hasRuntimeData(tempRuntimeData)){
               vcVialCalculator.addRuntimeData(tempRuntimeData);
            }  
         }
         rp.clearXML();
         rp.addXML("<biospecimen>" + vcVialCalculator.buildVialCalculationForm(tempRuntimeData)
               + "</biospecimen>");
         rp.setStylesheet(XSL_VIAL_CALCULATION);
         //System.out.println(rp.getXML());
      }
      else if (strAction.equalsIgnoreCase("confirm_vial_calculation")){

         tempRuntimeData.setParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis", QueryChannel.makeDateFromForm("VIAL_CALCULATION_dtDateOfFirstDiagnosis", tempRuntimeData));
         strErrors = vcVialCalculator.performVialCalculation(tempRuntimeData);
         if ( strErrors != null)
         {
            rp.setErrorMessage(strErrors);
            // System.out.println(strErrors);
         }
         else {
            rp.setErrorMessage("Calculation corfirmed");
            //only add if it is not yet in the list
            if (!vcVialCalculator.hasRuntimeData(tempRuntimeData)){
               vcVialCalculator.addRuntimeData(tempRuntimeData);
            }  
         }
         rp.clearXML();
         rp.addXML("<biospecimen>" + vcVialCalculator.buildVialCalculationForm(tempRuntimeData)
               + "</biospecimen>");

         rp.setStylesheet(XSL_CONFIRM_VIAL_CALCULATION);


      }
      else if (strAction.equalsIgnoreCase("save_vial_calculation"))
      {

         tempRuntimeData.setParameter("VIAL_CALCULATION_dtDateOfFirstDiagnosis", QueryChannel.makeDateFromForm("VIAL_CALCULATION_dtDateOfFirstDiagnosis", tempRuntimeData));
         strErrors = vcVialCalculator.saveVialCalculation(tempRuntimeData);
         if ( strErrors != null)
         {
            rp.setErrorMessage(strErrors);
            // System.out.println(strErrors);
         }
         else {
            rp.setErrorMessage("Calculations saved");
            //only add if it is not yet in the list
            if (!vcVialCalculator.hasRuntimeData(tempRuntimeData)){
               vcVialCalculator.addRuntimeData(tempRuntimeData);
            }  
         }
         rp.clearXML();
         rp.addXML("<biospecimen>" + vcVialCalculator.buildVialCalculationForm(tempRuntimeData)
               + "</biospecimen>");

         rp.setStylesheet(XSL_VIAL_CALCULATION);


      }
      else if (strAction.equalsIgnoreCase("get_allocation_option")){
          
          //if nothing to be created
          if ( tempRuntimeData.getParameter("VIAL_CALCULATION_intVialStored") != null &&
          tempRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash")  != null &&
          tempRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA") != null )
          {
              
              int intVialStored  = Integer.parseInt(tempRuntimeData.getParameter("VIAL_CALCULATION_intVialStored"));
              int intWash = Integer.parseInt(tempRuntimeData.getParameter("VIAL_CALCULATION_intMRDWash"));
              int intMRDRNA = Integer.parseInt(tempRuntimeData.getParameter("VIAL_CALCULATION_intMRDRNA"));
              
              // if no need to allocate biospecimen to inventory
              if ( intVialStored == intWash + intMRDRNA  || intVialStored == 0)
              {
                  strErrors = vcVialCalculator.doFinishVialCalculation(tempRuntimeData);
                  if (strErrors != null)
                  {
                      rp.setErrorMessage(strErrors);
                      rp.addXML("<biospecimen>ERROR:" +strErrors+ "</biospecimen>");
                  }
                  else
                  {
                      //return to the BIOSPECIMEN
                      BiospecimenCore bcBiospecimen  = new BiospecimenCore(authToken);
                      int intTempBiospecimenID = Integer.parseInt( tempRuntimeData.getParameter("BIOSPECIMEN_intBiospecimenID"));
                      rp.addXML("<biospecimen>"+ bcBiospecimen.buildViewBiospecimenXML( intTempBiospecimenID , tempRuntimeData)
                      + FlagManager.getFlaggedRecordsXML(FlagManager.DOMAIN_BIOSPECIMEN, null)
                      +"</biospecimen>");
                      rp.setErrorMessage("Vial Calculation created");
                      rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);
                      
                      return;
                  }
              }
          }
         boolean blRuntimeAdded= true;

         //adding or deleting group of allocations
         if ( strSubAction != null){
            if (strSubAction.equalsIgnoreCase("add_option")){
               blRuntimeAdded = false;
               //creat a new generation details object 
               GenerationDetails gd;

               int intSiteID = -1;
               int intTankID = -1;
               int intBoxID = -1;
               int intTrayID = -1;
               //default to fill blank
               int intAllocationMode = 1;
               int intAllocationAmount = -1;

               try 
               {
                  intSiteID = Integer.parseInt(runtimeData.getParameter("SITE_intSiteID") + "");
                  intTankID = Integer.parseInt(runtimeData.getParameter("TANK_intTankID") + "");
                  intBoxID = Integer.parseInt(runtimeData.getParameter("BOX_intBoxID"));
                  intTrayID = Integer.parseInt(runtimeData.getParameter("TRAY_intTrayID"));
                  //intAllocationMode = Integer.parseInt(runtimeData.getParameter("intAllocationMode"));
                  intAllocationAmount = Integer.parseInt(runtimeData.getParameter("intAllocationAmount"));

               }
               catch (java.lang.NumberFormatException e)
               {
                  System.err.println ("[Vial Calculator Manager] :: Parsing Exception occured when reading in data from portal");
                  e.printStackTrace();
               }


               if (intSiteID != -1 && intTankID != -1 && intBoxID != -1 && intTrayID != -1)
               {
                  gd = new GenerationDetails(intSiteID, intTankID, intBoxID, intTrayID, intAllocationMode, intAllocationAmount);
                  vcVialCalculator.addNewAllocationSelection(gd);
               }
               else
               {
                  rp.addXML("<vial_calculation><strError>Unable to add allocation</strError></vial_calculation>");
               }


            }
            //if delete option
            else if (strSubAction.equalsIgnoreCase("delete_option")){
                int intAllocationID = -1;
                    //rp.clearXML();
                    blRuntimeAdded = false;
                    try 
                    {
                       intAllocationID = Integer.parseInt(runtimeData.getParameter("intAllocationID"));
                    } 
                    catch (java.lang.NumberFormatException e)
                    {
                       rp.addXML("<strError>Unable to remove allocation - internal allocation ID is not valid</strError>");
                       System.err.println ("[VialCalManger] :: Number format exception");
                       e.printStackTrace();
                    }

                    boolean result = false;

                    if (intAllocationID != -1)
                    {
                       result = vcVialCalculator.removeAllocation(intAllocationID);
                    }

                    if (result != true)
                    {
                       System.err.println ("[Vial Calculation Manager] :: Unable to remove allocation");
                       rp.addXML("<strError>Unable to remove allocation - internal allocation ID was not found</strError>");
                    }          
            }
         }

         rp.addXML("<vial_calculation>" + QueryChannel.buildFormLabelXMLFile(formFields));
         rp.addXML( InventoryUtilities.getInventoryList());
         rp.addXML(vcVialCalculator.getAllocationSelectionXML());
         rp.addXML("<VIAL_CALCULATION_intNumberOfAllocatedBios>" + vcVialCalculator.getNumberOfAllocatedBios() + "</VIAL_CALCULATION_intNumberOfAllocatedBios>"); 
         rp.addXML("<enoughBiospecimen>" + vcVialCalculator.isEnoughSpecimen()+"</enoughBiospecimen>");
         rp.addXML("</vial_calculation>");
         rp.setStylesheet("get_allocation_option");
         
       
         
         //only add if it is not yet in the list of runtime data
         if (!vcVialCalculator.hasRuntimeData(tempRuntimeData) && blRuntimeAdded){
            vcVialCalculator.addRuntimeData(tempRuntimeData);
         }

      }
      else if (strAction.equalsIgnoreCase("confirm_allocation_option")){
          
          rp.clearXML();
          rp.addXML("<vial_calculation>");
          boolean blRuntimeAdded = true; 
          if (strSubAction != null && strSubAction.equalsIgnoreCase("allocate_option")){
              
                String strAllocationID = runtimeData.getParameter("intAllocationID"); // represents the id that the user has picked
                 String strLocationID = runtimeData.getParameter("locationID");  // where the user wishes to put it... either a tray key or continuous key

                 /*if (strLocationID != null && strLocationID.equals(""))
                    strLocationID = null;
                  */      
                 if (strLocationID != null) 
                 {
                    //mark the Generation ID as allocated
                    vcVialCalculator.setAllocationID(Integer.parseInt(strAllocationID), Integer.parseInt(strLocationID));
                    
                 }
                 
          }
          
          // check the available locations for everything the user has selected
          Vector posResults = vcVialCalculator.generateAvailableAllocations();
          
          
          rp.addXML( vcVialCalculator.getAllocationSelectionXML()
          + QueryChannel.buildFormLabelXMLFile(formFields)
          + "<finishButton>" + vcVialCalculator.isFinishable()+"</finishButton>"
          
          + "</vial_calculation>");
         //only add if it is not yet in the list of runtime data
         if (!vcVialCalculator.hasRuntimeData(tempRuntimeData) ){
            vcVialCalculator.addRuntimeData(tempRuntimeData);
         }
          
          rp.setStylesheet("confirm_allocation_option");
      }
      
      //preview a tray
      else if (strAction.equalsIgnoreCase("preview_tray_selection")){
         
              
                String strAllocationID = runtimeData.getParameter("intAllocationID"); // represents the id that the user has picked
                String strLocationID = runtimeData.getParameter("locationID");  // where the user wishes to put it... either a tray key or continuous key

                
                 if (strLocationID != null && strLocationID.equals(""))
                    strLocationID = null;
                 
                 if (strLocationID != null) 
                 {
                        //mark the Generation ID as allocated
                        
           
                        rp.addXML("<vial_calculation>" 
                        + vcVialCalculator.previewTrayAllocation(Integer.parseInt(strAllocationID), Integer.parseInt(strLocationID))
                        + "<intAllocationID>" + strAllocationID +"</intAllocationID>"
                        + "<locationID>" + strLocationID +"</locationID>"
                        + "</vial_calculation>");
           
                        //only add if it is not yet in the list of runtime data
                        if (!vcVialCalculator.hasRuntimeData(tempRuntimeData) )
                        {
                            vcVialCalculator.addRuntimeData(tempRuntimeData);
                        }
                       rp.setStylesheet("create_sub_specimen");
                 }
      
      }
    
      else if (strAction.equalsIgnoreCase("finish_vial_calculation"))
      {
         strErrors = vcVialCalculator.doFinishVialCalculation(tempRuntimeData);
         if (strErrors != null)
         {
            rp.setErrorMessage(strErrors);
            rp.addXML("<biospecimen>ERROR:" +strErrors+ "</biospecimen>");
         }
         else
         {
            rp.setErrorMessage("Vial Calculation created");
            String strInternalVialCalKey = BiospecimenUtilities.getVialCalculationKey(vcVialCalculator.getIntBiospecimenID());
            tempRuntimeData.setParameter("BIOSPECIMEN_intBiospecimenID", vcVialCalculator.getIntBiospecimenID()+"");
            tempRuntimeData.setParameter("BIOSPECIMEN_strBiospecimenID",BiospecimenUtilities.getUserBiospecimenID(vcVialCalculator.getIntBiospecimenID()));
            tempRuntimeData.setParameter("VIAL_CALCULATION_intVialCalculationKey", strInternalVialCalKey);
            rp.clearXML();
            rp.addXML("<biospecimen>" + vcVialCalculator.buildVialHistoryForm(tempRuntimeData)+ "</biospecimen>");
            rp.setStylesheet(XSL_VIAL_CALCULATION_HISTORY);

         }


      }
      else if (strAction.equalsIgnoreCase("vial_calculation_history"))
      {

         rp.clearXML();
         rp.addXML("<biospecimen>" + vcVialCalculator.buildVialHistoryForm(tempRuntimeData)+ "</biospecimen>");
         rp.setStylesheet(XSL_VIAL_CALCULATION_HISTORY);

      }
   }
   
   
 

}

