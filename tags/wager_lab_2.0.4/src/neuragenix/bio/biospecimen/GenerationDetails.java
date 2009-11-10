package neuragenix.bio.biospecimen;

import java.util.*;
import java.sql.ResultSet;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;

import neuragenix.common.*;
import neuragenix.dao.*;

public class GenerationDetails
{
    public final static int SINGLE_TRAY_CONTINOUS = 0;
    public final static int SINGLE_TRAY_FILL_GAPS = 1;
    public final static int NEW_TRAY = 2;
    public final static int NO_ALLOCATION = 3;
    //User manually click and select cells to allocate
    public final static int MANUAL_SELECTION = 4;
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
    
    public static final int GENERATION_SUCCESS = 1100;
    
    public static final int WILDCARD = -1000;
    
    
    
   
    
    private int intSiteID;
    private int intTankID;
    private int intBoxID;
    private int intTrayID;
    private int intAllocationAmount;
    private int intAllocationType;
    private int intInventoryAllocationID;
    
    //the data this generation need to save
    private ChannelRuntimeData rdDataToSave;
    
    //List of cellIDs for this Generation Details
    private List cellIDsList = null; 
    private List biosIDsList = null;
    //tray that stores the new bios
    private int intTrayKey;
    
    private boolean blNewTrayCreated = false;
    
    private Vector vtInventoryDetails;
    
    
    private String strSiteName;
    private String strTankName;
    private String strBoxName;
    private String strTrayName;
    
    private Vector vtCloneIdentifiers = new Vector(30, 30);
    
    private final int SITE = 0;
    private final int TANK = 1;
    private final int BOX = 2;
    private final int TRAY = 3;
    
    private int intGenerationStatus = -1;    
    
    
    public GenerationDetails(int intSiteID, int intTankID, int intBoxID, int intTrayID, int intAllocationType, int intAllocationAmount)
    {
        this.intSiteID = intSiteID;
        strSiteName = getInventoryName(SITE, intSiteID);
        this.intTankID = intTankID;
        strTankName = getInventoryName (TANK, intTankID);
        this.intBoxID = intBoxID;
        strBoxName = getInventoryName(BOX, intBoxID);
        this.intTrayID = intTrayID;
        strTrayName = getInventoryName(TRAY, intTrayID);
        
        this.intAllocationType = intAllocationType;
        this.intAllocationAmount = intAllocationAmount;
        
    }
    
    
    public void clearInventoryDetails()
    {
        if (vtInventoryDetails != null)
        {
            this.vtInventoryDetails.clear();
        }
    }
    
    public void setNewTrayCreated(boolean created)
    {
        this.blNewTrayCreated = created;
    }
    
    public boolean getNewTrayCreated()
    {
        return blNewTrayCreated;
    }
    
    
    
    public int getAllocationType()
    {
        return intAllocationType;
    }
    
    public int getAllocationAmount()
    {
        return intAllocationAmount;
    }
    
    public void setInventoryDetails(Vector vtInvDetails)
    {
        this.vtInventoryDetails = vtInvDetails;
    }
    
    public Vector getInventoryDetails()
    {
        return vtInventoryDetails;
    }
    
    public String getSiteName()
    {
        return strSiteName;
    }
    public void setSiteName(String strSiteName){
      this.strSiteName = strSiteName;
    }
    
    public int getSiteID()
    {
        return intSiteID;
    }
    
    public String getTankName()
    {
        return strTankName;
    }

    public void setTankName(String strTankName){
       this.strTankName = strTankName;  
    }


    
    public int getTankID()
    {
        return intTankID;
    }
    
    public String getBoxName()
    {
        return strBoxName;
    }

    public void setBoxName(String strBoxName){
       this.strBoxName = strBoxName;
    }
    
    public int getBoxID()
    {
        return intBoxID;
    }
    
    public String getTrayName()
    {
        return strTrayName;
    }


    public void setTrayName(String strTrayName){
      this.strTrayName = strTrayName;
    }
    
    public int getTrayID()
    {
        return intTrayID;
    }
    
    private String getInventoryName(int intType, int intID)
    {
        String domain = "";
        DALQuery query;
        ResultSet rs;
        
        
        switch (intType)
        {
            case SITE:
                domain = "Site";
                
            break;
                
            case TANK:
                domain = "Tank";
            break;
            
            case BOX:
                domain = "Box";
            break;
            
            case TRAY:
                domain = "Tray";
            break;
            
            default:
                return "";
        }
        
        try
        {
            query = new DALQuery();
            query.setDomain(domain.toUpperCase(), null, null, null);
            query.setField(domain.toUpperCase() + "_str" + domain + "Name", null);
            query.setWhere(null, 0, domain.toUpperCase() + "_int" + domain + "ID", "=", intID + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, domain.toUpperCase() + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            rs = query.executeSelect();

            if (rs.first())
            {
                String name = rs.getString(1);
                if (name == null)
                {
                    rs.close();
                    return "";
                }
                else
                {
                    rs.close();
                    return name;
                }
            }
            rs.close();
        }
        catch (Exception e)
        {
            System.err.println ("[BiospecimenGenerator :: GenerationDetails] An error occured whilst communicating with the database");
            e.printStackTrace();
        }
        return "";
        
    }
    
    public String translateToXML()
    {
        return translateToXML(-1);
    }
    
    
    public void addNewCloneIdentifiers(CloneIdentifiers ciDetails)
    {
        vtCloneIdentifiers.add(ciDetails);
    }
    
    public Vector getCloneIdentifiers()
    {
        return vtCloneIdentifiers;
    }
    
    public String translateToXML(int internalID)
    {   
        BGUtilities bgUtil = new BGUtilities();
        return translateToXML(null, internalID);
        //return translateToXML(bgUtil.getCellDetails(), internalID);
    }
    
    
    public String translateToXML(Hashtable cellDetails, int internalID)
    {
        StringBuffer strXML = new StringBuffer();
        
       
        strXML.append("<GenerationDetails>");
        
        if (internalID != -1)
            strXML.append("<GD_internalID>" + internalID + "</GD_internalID>");
        
        strXML.append("<SITE_intSiteID>" + intSiteID + "</SITE_intSiteID>");
        
        strXML.append("<SITE_strSiteName>" + strSiteName + "</SITE_strSiteName>");
        
        strXML.append("<TANK_intTankID>" + intTankID + "</TANK_intTankID>");
        
        strXML.append("<TANK_strTankName>" + strTankName + "</TANK_strTankName>");
        
        strXML.append("<BOX_intBoxID>" + intBoxID + "</BOX_intBoxID>");
        
        strXML.append("<BOX_strBoxName>" + strBoxName + "</BOX_strBoxName>");
        
        strXML.append("<TRAY_intTrayID>" + intTrayID + "</TRAY_intTrayID>");
        
        strXML.append("<TRAY_strTrayName>" + strTrayName + "</TRAY_strTrayName>");
        
        strXML.append("<GD_AllocationMode>" + intAllocationType + "</GD_AllocationMode>");
        
        strXML.append("<GD_AllocationAmount>" + intAllocationAmount + "</GD_AllocationAmount>");
        
        strXML.append("<GD_AllocationStatus>" + intGenerationStatus + "</GD_AllocationStatus>");
        
        if (rdDataToSave != null)
        {
        
        		Vector formfield = DatabaseSchema.getFormFields("cbiospecimen_batch_creation_option_form");
        		try 
        		{
        			strXML.append(QueryChannel.buildViewXMLFile(formfield, rdDataToSave));
        		}
        		catch (Exception e){
        			LogService.instance().log(LogService.ERROR, "[GenerationDetails] Cant get info from RD " + e.toString(), e);
        		}
                        
        

        	
        }
        
        if (vtInventoryDetails != null)
        {
            if (vtInventoryDetails.size() == 0)
            {
                strXML.append("<GD_AvailableLocations>");
                
                strXML.append("<GD_NoAvailable>true</GD_NoAvailable>");
                strXML.append("</GD_AvailableLocations>");
            }
            else
            {
         
                for (int i = 0; i < vtInventoryDetails.size(); i++)
                {
                    InventoryDetails details = (InventoryDetails) vtInventoryDetails.get(i);
                    strXML.append("<GD_AvailableLocations>");
                    strXML.append("<AL_internalID>" + i + "</AL_internalID>");
                    strXML.append("<SITE_intSiteID>" + details.getSiteID() + "</SITE_intSiteID>");
                    strXML.append("<SITE_strSiteName>" + details.getSiteName() + "</SITE_strSiteName>");
                    strXML.append("<TANK_intTankID>" + details.getTankID() + "</TANK_intTankID>");
                    strXML.append("<TANK_strTankName>" + details.getTankName() + "</TANK_strTankName>");

                    strXML.append("<BOX_intBoxID>" + details.getBoxID() + "</BOX_intBoxID>");

                    strXML.append("<BOX_strBoxName>" + details.getBoxName() + "</BOX_strBoxName>");

                    strXML.append("<TRAY_intTrayID>" + details.getTrayID() + "</TRAY_intTrayID>");

                    strXML.append("<TRAY_strTrayName>" + details.getTrayName() + "</TRAY_strTrayName>");
                    
                    if (details.getMode() == BiospecimenGenerator.SINGLE_TRAY_CONTINOUS)
                    { 
                       int tempCells[] = details.getAvailableCells();
                       strXML.append("<AL_Continuous>");
                       strXML.append("<AL_Continuous_ID>" + i + "</AL_Continuous_ID>");
                       strXML.append("<AL_Continuous_UserData>");
                       strXML.append(getCellLocationDetails(cellDetails, tempCells[0]) + " to " + getCellLocationDetails(cellDetails, tempCells[tempCells.length - 1]));
                       strXML.append("</AL_Continuous_UserData>");
                       strXML.append("</AL_Continuous>");
                    }
                     strXML.append("</GD_AvailableLocations>");
                }
                
         

            }
        }
        else
        {
               // strXML.append("<GD_AvailableLocations>");
               // strXML.append("<GD_NoAvailable>true</GD_NoAvailable>");
               // strXML.append("</GD_AvailableLocations>");
        }
        
        
        strXML.append("</GenerationDetails>");
        
        
        // the availability details go here
        
        // strXML.append("<AvailabilityDetails>");
        
        
        return strXML.toString();
    }
    
    
    private String getCellLocationDetails(Hashtable cellDetails, int intCellID)
    {
        String cellID = intCellID + "";
        return (String) cellDetails.get(cellID);
    }
    
    public int getGenerationStatus() 
    {
        return intGenerationStatus;
    }    
    
    public void setGenerationStatus(int generationStatus) 
    {
        this.intGenerationStatus = generationStatus;
    }    
    
    /**
     * Getter for property intInventoryAllocationID.
     * @return Value of property intInventoryAllocationID.
     */
    public int getIntInventoryAllocationID() 
    {
        return intInventoryAllocationID;
    }    
       
    /**
     * Setter for property intInventoryAllocationID.
     * @param intInventoryAllocationID New value of property intInventoryAllocationID.
     */
    public void setIntInventoryAllocationID(int intInventoryAllocationID) 
    {
        this.intInventoryAllocationID = intInventoryAllocationID;
    }
    
    /** This function bind a generation details with a specific inventory details 
     *  ( a confirmation of what location has been chosen )
     *
     */
    
    public void setIntInventoryAllocationID(int intInventoryAllocationID, int intType)
    {
        if (intType == BiospecimenGenerator.SINGLE_TRAY_CONTINOUS)
            this.intInventoryAllocationID = intInventoryAllocationID;
        else  // find the tray id in the inventory details
        {
            for (int i = 0; i < vtInventoryDetails.size(); i++)
            {
                InventoryDetails idDetails = (InventoryDetails) vtInventoryDetails.get(i);
                if (idDetails.getTrayID() == intInventoryAllocationID)
                {
                    this.intInventoryAllocationID = i;
                    break;
                }
            }
        }
    }

    
    public Vector getAvailableLocations(){
        return getAvailableLocations(null);
    }
    
    public Vector getAvailableLocations(DALSecurityQuery query)
    {
        
        Vector vtLocationGuide = null;
        
        int intAllocationMode = getAllocationType();
        
        try
        {
            switch (intAllocationMode)
            {
                case SINGLE_TRAY_CONTINOUS:
                    //System.out.println("THIS BRANCHE");
                    vtLocationGuide = discoverAvailableContinuous(this);
                    break;
                case BiospecimenGenerator.SINGLE_TRAY_FILL_GAPS:
                    //System.out.println("FILL GAPS");
                    vtLocationGuide = discoverAvailableSingleTray(this, query);
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
    
    
    
    // added to give module biospecimen split functionality
    
    public Vector discoverAvailableContinuous(GenerationDetails gd)
    {
        Vector vtAvailableTrayKeys = new Vector(3000, 1000);
        
        // load in the inventory details.
        try
        {
            
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
            
            query.setDomain("SITE", null, null, null);
            query.setDomain("TANK", "SITE_intSiteID", "TANK_intSiteID", "INNER JOIN");
            query.setDomain("BOX", "TANK_intTankID", "BOX_intTankID", "INNER JOIN");
            query.setDomain("TRAY", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setField("TRAY_intTrayID", null);
            query.setField("TRAY_intNoOfRow", null);
            query.setField("TRAY_intNoOfCol", null);
            query.setWhere(null, 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            
            if (gd.getSiteID() != BiospecimenGenerator.WILDCARD)
            {
                query.setWhere("AND", 0, "SITE_intSiteID", "=", gd.getSiteID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            if (gd.getTankID() != BiospecimenGenerator.WILDCARD)
            {
                query.setWhere("AND", 0, "TANK_intTankID", "=", gd.getTankID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            if (gd.getBoxID() != BiospecimenGenerator.WILDCARD)
            {
                query.setWhere("AND", 0, "BOX_intBoxID", "=", gd.getBoxID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            if (gd.getTrayID() != BiospecimenGenerator.WILDCARD)
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
                                
                                
                                vtAvailableTrayKeys.add( new InventoryDetails(trayDetails, trayKey, availableCells.clone()));
                                //System.out.println ("Above key : " + vtAvailableTrayKeys.size());
                                availableCells = Utilities.shiftArray(availableCells);
                                runCount--;
                                runBeginPosition++;
                            }
                        }
                        else
                        {
                            runBeginPosition = -1;
                            runCount = 0;
                            if (gd.getTrayID() != BiospecimenGenerator.WILDCARD)
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
            else
            {
                return null;
                
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }

    
    public Vector discoverAvailableSingleTray(GenerationDetails gd){
        return discoverAvailableSingleTray(gd, null);
    }
    
      public Vector discoverAvailableSingleTray(GenerationDetails gd, DALSecurityQuery queryIn)
    {
        
        DALSecurityQuery query;
        
        if ( queryIn == null ){
           query = new DALSecurityQuery();
        }
        else 
            query = queryIn;
        
        Vector vtAvailableLocationForm = DatabaseSchema.getFormFields("cbiospecimen_available_locations");
        Vector vtAvailableLocations = new Vector (10, 10);
        
        try {
            query.reset();
            query.setDomain("TRAY", null, null, null);
            query.setDomain("BOX", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            query.setDomain("TANK", "BOX_intTankID", "TANK_intTankID", "INNER JOIN");
            query.setDomain("SITE", "TANK_intSiteID", "SITE_intSiteID", "INNER JOIN");
            query.setFields(vtAvailableLocationForm, null);
            query.setWhere(null, 0, "TRAY_intTrayAvailable", ">=", getAllocationAmount() + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            
            
            if (gd.getSiteID() != WILDCARD)
            {
                query.setWhere("AND", 0, "SITE_intSiteID", "=", getSiteID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
                
                
            if (gd.getTankID() != WILDCARD)
            {
                query.setWhere("AND", 0, "TANK_intTankID", "=",getTankID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
                
                
            if (gd.getBoxID() != WILDCARD)
            {
                query.setWhere("AND", 0, "BOX_intBoxID", "=",getBoxID() + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
                
                
            if (gd.getTrayID() != WILDCARD)
            {
                query.setWhere("AND", 0, "TRAY_intTrayID", "=",getTrayID() + "", 0, DALQuery.WHERE_HAS_VALUE);
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


	public ChannelRuntimeData getRdDataToSave() {
		return rdDataToSave;
	}


	public void setRdDataToSave(ChannelRuntimeData rdDataToSave) {
		this.rdDataToSave = rdDataToSave;
	}


	public List getCellIDsList() {
		return cellIDsList;
	}


	public void setCellIDsList(List cellIDsList) {
		this.cellIDsList = cellIDsList;
	}


	public List getBiosIDsList() {
		return biosIDsList;
	}


	public void setBiosIDsList(List biosIDsList) {
		this.biosIDsList = biosIDsList;
	}


	public int getIntTrayKey() {
		return intTrayKey;
	}


	public void setIntTrayKey(int intTrayKey) {
		this.intTrayKey = intTrayKey;
	}

}


