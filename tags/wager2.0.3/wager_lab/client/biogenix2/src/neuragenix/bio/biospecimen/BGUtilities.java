/*
 * BGUtilities.java
 *
 * Created on 17 February 2005, 20:47
 */

package neuragenix.bio.biospecimen;

/**
 *
 * @author  dmurley
 */

import java.util.Hashtable;
import java.util.Vector;
import neuragenix.dao.*;
import neuragenix.common.*;
import java.sql.*;

public class BGUtilities {
    
    public static final int SITEID = 0;
    public static final int SITENAME = 1;
    public static final int BOXID = 2;
    public static final int BOXNAME = 3;
    public static final int TANKID = 4;
    public static final int TANKNAME = 5;
    public static final int TRAYID = 6;
    public static final int TRAYNAME = 7;
    
    
    /** Creates a new instance of BGUtilities */
    public BGUtilities() {
    }
    
    
    
    public Hashtable getCellDetails()
    {
        Hashtable cellDetails = new Hashtable();
        DALQuery query = new DALQuery();  // Define a common query object for data access    
        ResultSet rs = null;
        
        try 
        {
            query.setDomain("CELL", null, null, null);
            query.setField("CELL_intColNo", null);
            query.setField("CELL_intRowNo", null);
            query.setField("CELL_intCellID", null);
            query.setWhere(null, 0, "CELL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            rs = query.executeSelect();
            
            rs.beforeFirst();
            
            while (rs.next())
            {
                String colno = rs.getString("CELL_intColNo");
                String rowno = rs.getString("CELL_intRowNo");

                cellDetails.put(rs.getString("CELL_intCellID"), "COL : " + colno + " ROW : " + rowno);
            }
            
            rs.close();
            return cellDetails;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public Hashtable getTrayDetails()
    {
        Hashtable htTrayDetails = new Hashtable();
        String[] strATrayDetails = null;
        
        
        Vector vtTrayFields = DatabaseSchema.getFormFields("cbiospecimen_view_inventory_details_for_html");
        DALQuery qryTrayDetails = new DALQuery();
        try
        {
            qryTrayDetails.setDomain("SITE", null, null, null);
            qryTrayDetails.setDomain("TANK", "SITE_intSiteID", "TANK_intSiteID", "INNER JOIN");
            qryTrayDetails.setDomain("BOX", "TANK_intTankID", "BOX_intTankID", "INNER JOIN");
            qryTrayDetails.setDomain("TRAY", "TRAY_intBoxID", "BOX_intBoxID", "INNER JOIN");
            qryTrayDetails.setFields(vtTrayFields, null);
            qryTrayDetails.setWhere (null, 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsTrayDetails = qryTrayDetails.executeSelect();

            while (rsTrayDetails.next())
            {
               strATrayDetails = new String[8];
               strATrayDetails[SITEID] = rsTrayDetails.getString("SITE_intSiteID");
               strATrayDetails[SITENAME] = Utilities.cleanForXSL(rsTrayDetails.getString("SITE_strSiteName"));
               strATrayDetails[BOXID] = rsTrayDetails.getString("BOX_intBoxID");
               strATrayDetails[BOXNAME] = Utilities.cleanForXSL(rsTrayDetails.getString("BOX_strBoxName"));
               strATrayDetails[TANKID] = rsTrayDetails.getString("TANK_intTankID");
               strATrayDetails[TANKNAME] = Utilities.cleanForXSL(rsTrayDetails.getString("TANK_strTankName"));
               strATrayDetails[TRAYID] = rsTrayDetails.getString("TRAY_intTrayID");
               strATrayDetails[TRAYNAME] = Utilities.cleanForXSL(rsTrayDetails.getString("TRAY_strTrayName"));
               
               htTrayDetails.put(strATrayDetails[TRAYID], strATrayDetails.clone());
            }

            rsTrayDetails.close();
            return htTrayDetails;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    
    
}

