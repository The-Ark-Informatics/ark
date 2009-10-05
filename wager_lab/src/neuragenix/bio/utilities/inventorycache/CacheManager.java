/*
 * InventoryCache.java
 * 
 * Created on 19 April 2005, 18:02
 *
 * Description : Class with static methods for keeping a cache on inventory
 */

package neuragenix.bio.utilities.inventorycache;

import java.util.*;
import neuragenix.dao.*;
import java.sql.ResultSet;

/**
 *
 * @author  Daniel "If it breaks, I didn't code it" Murley
 */
public class CacheManager 
{
    // semaphore to lock out reads during rebuild
    private static boolean blCacheViewable = true;
    
    private static int intViewers = 0;
    
    
    private static final long CACHE_TIMEOUT = 10000;
    private static final int DEFAULT_VECTOR_SIZE = 50;
    private static final int DEFAULT_VECTOR_INCREMENT = 100;
    
    private static Vector vtSites;
    private static Vector vtTanks;
    private static Vector vtBoxes;
    private static Vector vtTrays;
    
    public static final int INVENTORY_NAME = 2;
    public static final int INVENTORY_ID = 0;
    public static final int INVENTORY_PARENTID = 1;
    public static final int DETAILS_SIZE = 3;
    
    
    /** Creates a new instance of InventoryCache */
    public CacheManager() {
    }
    
    static
    {
        System.out.println ("And we're off and running with the cache manager");
        System.err.println ("[Inventory Cache Manager] Starting up cache manager...");
        System.err.println ("[Inventory Cache Manager] Building Initial Cache...");
        
        rebuildCache();
        
    }
    
    public static synchronized void rebuildCache()
    {
        long lnCurrentTime = System.currentTimeMillis();
        
        System.out.println ("Cache Viewable State : " + blCacheViewable);
        
        if (blCacheViewable == false)
        {
            System.out.println ("No viewey for you!");
        }
        
        
        while (blCacheViewable == false)
        {
            System.out.println ("Cache not viewable");
            if ((System.currentTimeMillis() - lnCurrentTime) > CACHE_TIMEOUT)
            {
                System.err.println("[InventoryCache - Rebuild] - Unable to rebuild, Timeout waiting for cache to unlock");
                return;
            }
        }
        
        // got access to the cache, make it exclusive
        
        blCacheViewable = false;
        
        // wait until all users are off the system
        if (isClearToModify() == false)
        {
            System.out.println ("Unable to rebuild cache");
            return;
        }
        
        // made it here, cache is user free
        
        
        // destroy the old cache
        
        vtSites = null;
        vtSites = new Vector(DEFAULT_VECTOR_SIZE, DEFAULT_VECTOR_INCREMENT);
        
        vtTanks = null;
        vtTanks = new Vector(DEFAULT_VECTOR_SIZE, DEFAULT_VECTOR_INCREMENT);
        
        vtBoxes = null;
        vtBoxes = new Vector(DEFAULT_VECTOR_SIZE, DEFAULT_VECTOR_INCREMENT);
        
        vtTrays = null;
        vtTrays = new Vector(DEFAULT_VECTOR_SIZE, DEFAULT_VECTOR_INCREMENT);
        
        
        try
        {

            // -- Site Details
            DALSecurityQuery dsqInventory = new DALSecurityQuery();
            dsqInventory.setDomain("SITE", null, null, null);
            dsqInventory.setField("SITE_strSiteName", null);
            dsqInventory.setField("SITE_intSiteID", null);
            dsqInventory.setWhere(null, 0, "SITE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rs = dsqInventory.executeSelect();

            while (rs.next())
            {
                String strSiteDetails[] = new String[DETAILS_SIZE];
                strSiteDetails[INVENTORY_NAME] = rs.getString("SITE_strSiteName");
                strSiteDetails[INVENTORY_ID] = rs.getString("SITE_intSiteID");
                strSiteDetails[INVENTORY_PARENTID] = null;
                vtSites.add(strSiteDetails);
            }

            rs.close();

            // -- Tank Details

            dsqInventory.reset();
            dsqInventory.setDomain("TANK", null, null, null);
            dsqInventory.setField("TANK_strTankName", null);
            dsqInventory.setField("TANK_intTankID", null);
            dsqInventory.setField("TANK_intSiteID", null);
            
            dsqInventory.setWhere(null, 0, "TANK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            rs = dsqInventory.executeSelect();

            while (rs.next())
            {
                String strTankDetails[] = new String[DETAILS_SIZE];
                strTankDetails[INVENTORY_NAME] = rs.getString("TANK_strTankName");
                strTankDetails[INVENTORY_ID] = rs.getString("TANK_intTankID");
                strTankDetails[INVENTORY_PARENTID] = rs.getString("TANK_intSiteID");
                vtTanks.add(strTankDetails);
            }

            rs.close();
            
            // -- Box Details

            dsqInventory.reset();
            dsqInventory.setDomain("BOX", null, null, null);
            dsqInventory.setField("BOX_strBoxName", null);
            dsqInventory.setField("BOX_intBoxID", null);
            dsqInventory.setField("BOX_intTankID", null);
            dsqInventory.setWhere(null, 0, "BOX_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            rs = dsqInventory.executeSelect();

            while (rs.next())
            {
                String strBoxDetails[] = new String[DETAILS_SIZE];
                strBoxDetails[INVENTORY_NAME] = rs.getString("BOX_strBoxName");
                strBoxDetails[INVENTORY_ID] = rs.getString("BOX_intBoxID");
                strBoxDetails[INVENTORY_PARENTID] = rs.getString("BOX_intTankID");
                vtBoxes.add(strBoxDetails);
            }

            rs.close();
            
            // -- Tray Details

            dsqInventory.reset();
            dsqInventory.setDomain("TRAY", null, null, null);
            dsqInventory.setField("TRAY_strTrayName", null);
            dsqInventory.setField("TRAY_intTrayID", null);
            dsqInventory.setField("TRAY_intBoxID", null);
            dsqInventory.setWhere(null, 0, "TRAY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            rs = dsqInventory.executeSelect();

            while (rs.next())
            {
                String strTrayDetails[] = new String[DETAILS_SIZE];
                strTrayDetails[INVENTORY_NAME] = rs.getString("TRAY_strTrayName");
                strTrayDetails[INVENTORY_ID] = rs.getString("TRAY_intTrayID");
                strTrayDetails[INVENTORY_PARENTID] = rs.getString("BOX_intBoxID");
                vtTrays.add(strTrayDetails);
            }
            
            rs.close();
            
        }
        catch (Exception e)
        {
            blCacheViewable = true;
            e.printStackTrace();
        }
        
        blCacheViewable = true;
        
        return;
        
    }
    
    private static synchronized void incrementViewers()
    {
        intViewers++;
    }
    
    private static synchronized void decrementViewers()
    {
        intViewers++;
    }
    
    private static synchronized boolean isClearToModify()
    {
        long lnCurrentTime = System.currentTimeMillis();
        
        if (blCacheViewable == true)
        {
            System.out.println ("Unable to ensure cache is user free in its current mode");
            return false;
        }
        
        
        while (intViewers > 0)
        {
            if ((System.currentTimeMillis() - lnCurrentTime) > CACHE_TIMEOUT)
            {
                System.err.println("[Inventory Cache - isCleartoModify] - Unable to rebuild, Timeout waiting for cache to unlock");
                return false;
            }
        }
        return true;
    }
    
    public static Vector getSites()
    {
        
        if (!isClearToView())
        {
            return null;
        }
        
        incrementViewers();
                
        Vector vtReturnSites = (Vector) vtSites.clone();
        
        decrementViewers();
        
        return vtReturnSites;
        
    }
    
    public static Vector getTanks()
    {
        
        if (!isClearToView())
        {
            return null;
        }
        
        incrementViewers();
                
        Vector vtReturnTanks = (Vector) vtTanks.clone();
        
        decrementViewers();
        
        return vtReturnTanks;
        
    }
    
    public static Vector getBoxes()
    {
        
        if (!isClearToView())
        {
            return null;
        }
        
        incrementViewers();
                
        Vector vtReturnBoxes = (Vector) vtBoxes.clone();
        
        decrementViewers();
        
        return vtReturnBoxes;
        
    }
    
    public static Vector getTrays()
    {
        
        if (!isClearToView())
        {
            return null;
        }
        
        incrementViewers();
                
        Vector vtReturnTrays = (Vector) vtTrays.clone();
        
        decrementViewers();
        
        return vtReturnTrays;
        
    }
    
    
    
    private static synchronized boolean isClearToView()
    {
        long lnCurrentTime = System.currentTimeMillis();
        
        while (!blCacheViewable)
        {
            if ((System.currentTimeMillis() - lnCurrentTime) > CACHE_TIMEOUT)
            {
                System.err.println("[InventoryCache - Rebuild] - Unable to rebuild, Timeout waiting for cache to unlock");
                return false;
            }
        }
        
        return true;
    }
    
    
}
