/*
 *   BatchQuantity
 *
 *   Biospecimen Batch Allocations
 *   Author : Daniel Murley
 *   email : dmurley@neuragenix.com
 *
 *   Purpose : Allows the user to quickly search for available specimen
 *   quantities, and then allocate them to thier study.
 *
 *   $Id $
 *
 */

package neuragenix.bio.biospecimen;
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
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.services.LogService;

/**
 *
 * @author  Daniel Murley
 */


public class BatchQuantityManager {
    
    private ChannelStaticData sd = null;
    private QuantityAllocator bpQtyAllocator = null;
    private AuthToken authToken = null;
    
    private BiospecimenGenerator bpBioGenerator = null;
    private NGXRuntimeProperties rp = null;
    
    public static final String PERMISSION_BATCH_QUANTITY = "batch_quantity";
    
    /** Creates a new instance of BatchQuantityManager */
    public BatchQuantityManager(AuthToken authToken, ChannelStaticData sd) 
    {
        this.authToken = authToken;
        this.sd = sd;
    }
    
    
    public boolean isModuleRunning()
    {
        if (bpQtyAllocator == null)
            return false;
        else
            return true;
        
          
    }
    
    public String doISearch(ChannelRuntimeData rd)
    {
        if (bpQtyAllocator != null)
        {
            return bpQtyAllocator.doISearch(rd);
        }
        return "";
    }
    
    public String getISearchStylesheet()
    {
        return bpQtyAllocator.getISearchStylesheet();
    }
    
    public void setISearchFlags(ChannelRuntimeData rd)
    {
        bpQtyAllocator.setISearchFlags(rd);
    }
    
    public NGXRuntimeProperties processRuntimeData (ChannelRuntimeData runtimeData)
    {

        NGXRuntimeProperties rp = new NGXRuntimeProperties(authToken);
        String strCurrent = runtimeData.getParameter("current");
        
        System.out.println ("BatchQty : current : " + strCurrent);
        
        
        
        if (strCurrent.equals("bp_allocate"))
        {
            String bpAllocateStage = runtimeData.getParameter("stage");
            System.out.println ("stage : " + bpAllocateStage);
            
            if (bpAllocateStage.equalsIgnoreCase("BEGIN"))
            {
                // out with the old ... in with the new
                bpQtyAllocator = new QuantityAllocator(this.sd);

                rp.addXML("<QuantityAllocation></QuantityAllocation>");
                rp.setStylesheet(QuantityAllocator.XSL_DISPLAY_ALLOCMODE_SELECTION);

            }

            else if (bpAllocateStage.equals("GETMODE"))
            {
                // get the mode for the allocation
                String strAllocMode = runtimeData.getParameter("strAllocMode");

                if (strAllocMode != null)
                {
                    if (strAllocMode.equalsIgnoreCase("allocate"))
                    {
                        bpQtyAllocator.setAllocationType(QuantityAllocator.CHANGE_TO_ALLOCATED);
                    }
                    else if (strAllocMode.equalsIgnoreCase("deliver"))
                    {
                        bpQtyAllocator.setAllocationType(QuantityAllocator.CHANGE_TO_DELIVERED);
                    }

                }

                // should get moved to search at this point
                runtimeData.remove("current");
                bpQtyAllocator.setISearchCriteria();
                rp.addXML(bpQtyAllocator.doISearch(runtimeData));
                rp.setStylesheet(bpQtyAllocator.getISearchStylesheet());
            }

            else if (bpAllocateStage.equals("ISEARCH_FINISH"))
            {
                // instruct the allocator to get the data it needs from isearch

                String tempXMLFromAllocator = bpQtyAllocator.getSelectedAllocationXML();

                rp.addXML("<QuantityAllocation>");
                rp.addXML(tempXMLFromAllocator);
                rp.addXML(StudyUtilities.getListOfCurrentStudiesXML());  // output study data
                try
                {
                    rp.addXML(QueryChannel.buildLOVXMLFile("BIOSPECIMEN_TRANSACTIONS_strTreatment", null));    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                rp.addXML("<AllocationType>" + bpQtyAllocator.getAllocationType() + "</AllocationType>");

                if (bpQtyAllocator.getAllocationType() == QuantityAllocator.CHANGE_TO_ALLOCATED)
                {
                    // output xml for this allocation type

                }
                else if (bpQtyAllocator.getAllocationType() == QuantityAllocator.CHANGE_TO_DELIVERED)
                {

                }

                // output allocation mode so the stylesheet can decide what to display

                rp.addXML("</QuantityAllocation>");

                // display a confirmation


                // request delivery date if required

                // set the stylesheet
                rp.setStylesheet(QuantityAllocator.XSL_DISPLAY_ALLOC_CONFIRMATION);
            }

            else if (bpAllocateStage.equals("DOALLOCATION"))
            {

                String strStudyKey = null;
                String strDeliveryDate = null;
                int intAllocationResult = -1;

                switch (bpQtyAllocator.getAllocationType())
                {
                    case QuantityAllocator.CHANGE_TO_ALLOCATED:
                       String strFixationTime = runtimeData.getParameter("strFixationTime");
                       String strTreatment = runtimeData.getParameter("strTreatment");
                       strStudyKey = runtimeData.getParameter("STUDY_intStudyID");


                       Enumeration rdEnum = runtimeData.getParameterNames();

                       if ((strFixationTime != null) && (strTreatment != null))
                       {
                           Hashtable htTempData = new Hashtable();
                           htTempData.put("BIOSPECIMEN_TRANSACTIONS_strFixationTime", strFixationTime);
                           htTempData.put("BIOSPECIMEN_TRANSACTIONS_strTreatment", strTreatment);

                           htTempData.put("BIOSPECIMEN_TRANSACTIONS_intStudyKey", strStudyKey);
                           bpQtyAllocator.setDataFields(htTempData);
                           intAllocationResult = bpQtyAllocator.doAllocation();
                       }


                    break;


                    case QuantityAllocator.CHANGE_TO_DELIVERED:
                        strDeliveryDate = runtimeData.getParameter("strDeliveryDate");


                       if (strDeliveryDate != null)
                       {
                           Hashtable htTempData = new Hashtable();
                           htTempData.put("BIOSPECIMEN_TRANSACTIONS_dtDeliveryDate", strDeliveryDate);
                           bpQtyAllocator.setDataFields(htTempData);
                           intAllocationResult = bpQtyAllocator.doAllocation();
                       }

                    break;

                }

                // display confirmation again... with completed notice this timne.
                rp.addXML("<QuantityAllocation>");
                rp.addXML(bpQtyAllocator.getSelectedAllocationXML());

                // TODO: failure handling needs to go here
                if (intAllocationResult == QuantityAllocator.ALLOCATION_SUCCESS)
                {
                    if (strStudyKey != null)
                    {
                        rp.addXML("<STUDY_strStudyName>" + StudyUtilities.getStudyName(Integer.parseInt(strStudyKey)) + "</STUDY_strStudyName>");
                    }
                    
                    rp.addXML("<AllocationType>" + bpQtyAllocator.getAllocationType() + "</AllocationType>");

                    if (strDeliveryDate != null)
                    {
                        rp.addXML("<strDeliveryDate>" + strDeliveryDate + "</strDeliveryDate>");
                    }
                }
                rp.addXML("</QuantityAllocation>");

                rp.setStylesheet(QuantityAllocator.XSL_DISPLAY_ALLOC_FINISH);


            }
            else if (bpAllocateStage.equals("CANCELALLOCATION"))
            {
                // clear the locks
                // return the user to search or something
            }


            // XXX: maybe something might be needed here for going BACK to iSearch



        }
        
        return rp;    
    }
    
    
    
}
