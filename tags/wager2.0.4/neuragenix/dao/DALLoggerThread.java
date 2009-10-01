/*
 * DALLoggerThread.java
 *
 * Created on March 7, 2005, 11:46 AM
 */

package neuragenix.dao;

/**
 *
 * @author  hhoang
 */
// java packages
import java.util.Vector;

// uPortal packages
import org.jasig.portal.services.LogService;
import org.jasig.portal.ChannelRuntimeData;

// neuragenix packages
import neuragenix.security.AuthToken;

public class DALLoggerThread extends Thread {
    
    /** Creates a new instance of DALLoggerThread */
    public DALLoggerThread() {
    }
    
     public void run() {
        while (true) {
            Vector vtLogQueue = DALLoggerRepository.getLogQueue();
            Vector vtLogSQLStatQueue = DALLoggerRepository.getLogSQLStatQueue();
            Vector vtLogFormFieldQueue = DALLoggerRepository.getLogFormFieldQueue();
            Vector vtRuntimeDataQueue = DALLoggerRepository.getRuntimeDataQueue();
            Vector vtAuthTokenQueue = DALLoggerRepository.getAuthTokenQueue();
            
            try {
                while (vtLogQueue.size() > 0) {
                    writeLog(vtLogQueue, vtLogSQLStatQueue, vtLogFormFieldQueue, vtRuntimeDataQueue, vtAuthTokenQueue);
                }

                Thread.sleep(1000);
            }
            catch (InterruptedException e) {        
               LogService.log(LogService.ERROR, "InterruptedException error. " + e.toString());                
            }

            catch (Exception e){            
                LogService.log(LogService.ERROR, "Unable to register a Job Process Entry.");                
            }                       
        }
    }
     
    private synchronized void writeLog(Vector vtLogQueue, Vector vtLogSQLStatQueue, Vector vtLogFormFieldQueue, Vector vtRuntimeDataQueue, Vector vtAuthTokenQueue) throws Exception {
        // getting data related to log
        String strLogShortDesc = (String) vtLogQueue.get(0);
        String strSQLStat = (String) vtLogSQLStatQueue.get(0);
        Vector vtFormField = (Vector) vtLogFormFieldQueue.get(0);
        ChannelRuntimeData rdRuntimeData = (ChannelRuntimeData) vtRuntimeDataQueue.get(0);
        AuthToken authToken = (AuthToken) vtAuthTokenQueue.get(0);
        
        // build the SQL statement
        if (rdRuntimeData != null) {
            for (int i=0; i < vtFormField.size(); i++) {
                String strFieldName = (String) vtFormField.get(i);
                String strFieldValue = rdRuntimeData.getParameter(strFieldName);
                
                try {
                    if (strFieldValue != null) {
                        //
                        strSQLStat = strSQLStat.replaceFirst("\\?", strFieldValue);
                        //strSQLStat.append(strFieldValue + ", ");
                    }
                    else {
                        strSQLStat = strSQLStat.replaceFirst("\\?", "");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    vtLogQueue.remove(0);
                    vtLogSQLStatQueue.remove(0);
                    vtLogFormFieldQueue.remove(0);
                    vtRuntimeDataQueue.remove(0);
                    vtAuthTokenQueue.remove(0);
                }
            }
        }
     
        // write to log
        if (strLogShortDesc != null && (strLogShortDesc.equals("INSERT") || strLogShortDesc.equals("UPDATE"))) {
            DALLogger.instance().log(DALLogger.ACTIVITY, DALLogger.PRIORITY_HIGH, DALLogger.RESULT_SUCCESS, strLogShortDesc, strSQLStat, authToken.getUserIdentifier());
        }
        else {
            DALLogger.instance().log(DALLogger.ACTIVITY, DALLogger.PRIORITY_NORMAL, DALLogger.RESULT_SUCCESS, strLogShortDesc, strSQLStat, authToken.getUserIdentifier());
        }
        
        // clear the request
        vtLogQueue.remove(0);
        vtLogSQLStatQueue.remove(0);
        vtLogFormFieldQueue.remove(0);
        vtRuntimeDataQueue.remove(0);
        vtAuthTokenQueue.remove(0);
        
        
    }
}
