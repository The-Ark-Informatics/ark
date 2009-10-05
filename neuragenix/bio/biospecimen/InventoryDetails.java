package neuragenix.bio.biospecimen;

import java.util.Hashtable;
import neuragenix.dao.*;
import neuragenix.bio.utilities.*;
import java.sql.ResultSet;
import neuragenix.common.*;
/** This class is used by Generation Details 
 * 
 * 
 */
public class InventoryDetails
{
    private int intSiteID;
    private String strSiteName;
    private int intTankID;
    private String strTankName;
    private int intBoxID;
    private String strBoxName;
    private int intTrayID;
    private String strTrayName;
    private int intMode = -1;
    private int[] intAvailableCells = null;
    
    
    
    public final int CONTINUOUS = BiospecimenGenerator.SINGLE_TRAY_CONTINOUS;
    public final int FILLGAPS = BiospecimenGenerator.SINGLE_TRAY_FILL_GAPS;
    public final int MANUAL_SELECTION = GenerationDetails.MANUAL_SELECTION;
    
    public InventoryDetails(int intSiteID, int intTankID, int intBoxID, int intTrayID)
    {
        this.intSiteID = intSiteID;
        this.intTankID = intTankID;
        this.intBoxID = intBoxID;
        this.intTrayID = intTrayID;
	this.intMode = FILLGAPS;
    }
    
    public InventoryDetails(int intSiteID, String strSiteName, int intTankID, String strTankName, int intBoxID, String strBoxName, int intTrayID, String strTrayName)
    {
        this.intSiteID = intSiteID;
        this.intTankID = intTankID;
        this.intBoxID = intBoxID;
        this.intTrayID = intTrayID;
        this.strSiteName = strSiteName;
        this.strTankName = strTankName;
        this.strBoxName = strBoxName;
        this.strTrayName = strTrayName;
        this.intMode = FILLGAPS;
        
    }
    
    public InventoryDetails (int trayID, Object intAvailableCells)
    {
        System.err.println ("[BiospecimenGenerator : Warning] Deprecated Constructor is being used");
    }
    
    public InventoryDetails (Hashtable htTrayDetails, int trayID, Object intAvailableCells)
    {
 
        String strTrayDetails[] = (String[]) htTrayDetails.get(trayID + "");
        
        if (strTrayDetails != null)
        {
            this.intSiteID = Integer.parseInt(strTrayDetails[BGUtilities.SITEID]);
            this.strSiteName = Utilities.cleanForXSL(strTrayDetails[BGUtilities.SITENAME]);
            this.intBoxID = Integer.parseInt(strTrayDetails[BGUtilities.BOXID]);
            this.strBoxName = Utilities.cleanForXSL(strTrayDetails[BGUtilities.BOXNAME]);
            this.intTankID = Integer.parseInt(strTrayDetails[BGUtilities.TANKID]);
            this.strTankName = Utilities.cleanForXSL(strTrayDetails[BGUtilities.TANKNAME]);
            this.intTrayID = Integer.parseInt(strTrayDetails[BGUtilities.TRAYID]);
            this.strTrayName = Utilities.cleanForXSL(strTrayDetails[BGUtilities.TRAYNAME]);
            
            if (intAvailableCells == null)
               this.intMode = FILLGAPS;
            else
            {
                this.intAvailableCells = (int[]) intAvailableCells;
                this.intMode = CONTINUOUS;
            }
            
            
        }
        else
        {
            System.err.println ("[BiospecimenGenerator] Warning : Tray Details Were Null");
        }
       
    }
    
    
    public int[] getAvailableCells()
    {
        // if (this.intMode == CONTINUOUS)
            return intAvailableCells;
        // else
        //    return null;
    }

    
    
    public int[] getAvailableCells(int intCellCount)
    {
        int availableCells[] = new int[intCellCount];
        if (this.intMode == FILLGAPS)
        {
            // hunt down a set of available cells for the amount required.
            DALQuery query = new DALQuery();
            ResultSet rs = null;
            try
            {
                query.setDomain("CELL", null, null, null);
                query.setField("CELL_intCellID", null);
                query.setWhere(null, 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "CELL_intTrayID", "=", getTrayID() + "", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "CELL_intBiospecimenID", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "CELL_intPatientID", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("CELL_intRowNo", "ASC");
                query.setOrderBy("CELL_intColNo", "ASC");
                rs = query.executeSelect();
                
                int counter = -1;
                for (counter = 0; (counter < intCellCount) && (rs.next()); counter++)
                {
                    availableCells[counter] = rs.getInt("CELL_intCellID");
                }
                
                if (counter != intCellCount)
                {
                    // there was not enough cells... major error
                    System.err.println ("[InventoryDetails :: getAvailableCells]  Request [" + intCellCount + "]" + " -- Not enough cells were found in inventory");
                    return null;
                }
                intAvailableCells = availableCells;
                
                rs.close();
                
                return availableCells;
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
            
        }
        return null;
        
    }
    
    
    /**Get available cells using a connection
     */
    public int[] getAvailableCells(int intCellCount, DALSecurityQuery query)
    {
        int availableCells[] = new int[intCellCount];
        if (this.intMode == FILLGAPS)
        {
            // hunt down a set of available cells for the amount required.
            
            ResultSet rs = null;
            try
            {
                query.reset();
                query.setDomain("CELL", null, null, null);
                query.setField("CELL_intCellID", null);
                query.setWhere(null, 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "CELL_intTrayID", "=", getTrayID() + "", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "CELL_intBiospecimenID", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "CELL_intPatientID", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("CELL_intRowNo", "ASC");
                query.setOrderBy("CELL_intColNo", "ASC");
                rs = query.executeSelect();
                
                int counter = -1;
                for (counter = 0; (counter < intCellCount) && (rs.next()); counter++)
                {
                    availableCells[counter] = rs.getInt("CELL_intCellID");
                }
                
                if (counter != intCellCount)
                {
                    // there was not enough cells... major error
                    System.err.println ("[InventoryDetails :: getAvailableCells]  Request [" + intCellCount + "]" + " -- Not enough cells were found in inventory");
                    return null;
                }
                intAvailableCells = availableCells;
                
                rs.close();
               
                
                return availableCells;
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //query.cancelTransaction();
                return null;
            }
            
        }
        return null;
        
    }
    
    public int getMode()
    {
        return intMode;
    }
    
    public int getSiteID()
    {
        return intSiteID;
    }
    
    
    public String getSiteName()
    {
        return strSiteName;
    }
    
    public int getTankID()
    {
        return intTankID;
    }
    
    public String getTankName()
    {
        return strTankName;
    }
    
    
    public int getBoxID()
    {
        return intBoxID;
    }
    
    public String getBoxName()
    {
        return strBoxName;
    }
    
    
    public int getTrayID()
    {
        return intTrayID;    
    }
    
    public String getTrayName()
    {
        return strTrayName;
    }
    
    
    public void addManualCellID(int cellID) throws Exception{
    		//to avoid duplicates, check before adding in
    		boolean blDuplicate = false;
    		for (int i = intAvailableCells.length-1; i >= 0 ; i--){
    			if (intAvailableCells[i] == cellID)
    				throw new Exception("ID already in the set");
    		}
    		intAvailableCells[intAvailableCells.length] = cellID;
    		
    }
   
    
}
