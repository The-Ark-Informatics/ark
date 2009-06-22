/*
 * BatchAllocation.java
 *
 * Created on 21 Dec 2006
 */

package neuragenix.bio.biospecimen;

/**
 *
 * @author  Chris Williams
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

public class BatchAllocation {
    
    private AuthToken authToken = null;
    private BiospecimenGenerator bpBioGenerator = null;
    
    private NGXRuntimeProperties rp = null;
    
    
	    private static final int ALLOC_STATUS_SELECT_BOX = 1;
	    private static final int ALLOC_STATUS_SCAN = 2;
 
    private static final String MISSING_ID_ERROR = "Either Barcode or Biospecimen ID is missing";
    private static final String DUPLICATE_ID_ERROR = "ID is already in use by the system";
    private static final String DUPLICATE_BAR_CODE = "Barcode is already in use by the system";
    
    public static final String PERMISSION_BATCH_CLONE = "batch_clone";
    
    /** Creates a new instance of BatchCloneManager */
    public BatchAllocation(AuthToken authToken) 
    {
        this.authToken = authToken;
    }
    
    public BatchAllocation(NGXRuntimeProperties rp)
    {
        this.rp = rp;
        this.authToken = rp.getAuthToken();
    }
   
    public NGXRuntimeProperties processRuntimeData(ChannelRuntimeData runtimeData)
    {
       // The below is too much functionality, and as such, needs to be moved to another class
       
                   
       Vector formFields = DatabaseSchema.getFormFields("cinventory_get_display");
        if (rp == null)
        {
            rp = new NGXRuntimeProperties();
            rp.setAuthToken(authToken);
        }
        // Get where we are at
        String bpCloneStage = runtimeData.getParameter("stage");
	if (bpCloneStage != null) 
	{
	  if (bpCloneStage.equals("BACK"))
           {
              rp.setStylesheet("batch_allocate");
              rp.clearXML();
              rp.addXML("<BATCH_ALLOCATE>"
                    + "</BATCH_ALLOCATE>");
           }
  	   else if (bpCloneStage.equals("BEGIN")) 
	{
	rp.setStylesheet("batch_allocate");
	rp.clearXML();			
	rp.addXML("<BATCH_ALLOCATE>"
	+  "<BATCH_SELECT_BOX>"
	+ "</BATCH_SELECT_BOX>"
                    + "</BATCH_ALLOCATE>");
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
