/**
 * ReportChannelsManager.java
 * Copyright © 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 12/12/2003
 */

package neuragenix.genix.report;

/**
 * Report channel
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */


// java packages
import java.util.Hashtable;

public class ReportChannelsManager {
    
    static Hashtable hashReportChannelObjects = new Hashtable(10);
    
    /** Creates a new instance of ReportChannelsManager */
    public ReportChannelsManager() {
    }
    
    public static void registerReportChannelObject(String strSessionID, Object objChannel)
    {
        if (!hashReportChannelObjects.containsKey(strSessionID))
            hashReportChannelObjects.put(strSessionID, objChannel);
    }
    
    public static void unregisterReportChannelObject(String strSessionID)
    {
        hashReportChannelObjects.remove(strSessionID);
    }
    
    public static Object getReportChannelObject(String strSessionID)
    {
        return hashReportChannelObjects.get(strSessionID);
    }
}
