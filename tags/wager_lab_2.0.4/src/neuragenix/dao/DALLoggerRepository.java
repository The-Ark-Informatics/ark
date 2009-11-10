/*
 * DALLoggerThread.java
 *
 * Created on March 7, 2005, 11:28 AM
 */

package neuragenix.dao;

/**
 *
 * @author  hhoang
 */

// java packages
import java.util.Vector;

// uPortal packages
import org.jasig.portal.ChannelRuntimeData;

// neuragenix packages
import neuragenix.security.AuthToken;

public class DALLoggerRepository {
    
    private static Vector vtLogQueue = new Vector(100, 50);
    private static Vector vtLogSQLStatQueue = new Vector(100, 50);
    private static Vector vtLogFormFieldQueue = new Vector(100, 50);
    private static Vector vtRuntimeDataQueue = new Vector(100, 50);
    private static Vector vtAuthTokenQueue = new Vector(100, 50);
    private static DALLoggerThread threadLogger = new DALLoggerThread();
    
    static {
        threadLogger.start();
    }
    
    /** Creates a new instance of DALLoggerThread */
    public DALLoggerRepository() {
    }
    
    public static Vector getLogQueue() {
        return vtLogQueue;
    }
    
    public static Vector getLogSQLStatQueue() {
        return vtLogSQLStatQueue;
    }
    
    public static Vector getLogFormFieldQueue() {
        return vtLogFormFieldQueue;
    }
    
    public static Vector getRuntimeDataQueue() {
        return vtRuntimeDataQueue;
    }
    
    public static Vector getAuthTokenQueue() {
        return vtAuthTokenQueue;
    }
    
    public static void addLog(String strQueryType, String strSQLStat, Vector vtFormField, ChannelRuntimeData rdRuntimeData, AuthToken authToken) {
        vtLogQueue.add(strQueryType);
        vtLogSQLStatQueue.add(strSQLStat);
        vtLogFormFieldQueue.add(vtFormField);
        vtRuntimeDataQueue.add(rdRuntimeData);
        vtAuthTokenQueue.add(authToken);
    }
}
