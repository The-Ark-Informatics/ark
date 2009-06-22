
package neuragenix.genix.auditlogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jasig.portal.services.LogService;

import neuragenix.dao.DALQuery;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.genix.auditlogger.utilities.*;

/**
 * <p></p>
 *
 */
public class AuditLogManager
{
   
   public AuditLogManager()
   {
      // System.out.println ("AuditLogManager has been instantiated");
   }
   
   /**
    * <p>Does ...</p>
    *
    */
   public void createInstance()
   {
      // your code here
   }
   
   /**
    * <p>Send a particular audit log item to the database</p>
    *
    *
    * @param logItem
    */
   public void logItem(neuragenix.genix.auditlogger.AuditLogItem logItem)
   {
      // your code here
   }
   
   /**
    * <p>Executes a database update for a
    *  DALSecurityQuery object, however,
    *  also conducts the audit logging</p>
    *
    * @deprecated Should only be used during testing
    * @param query
    */
   public boolean executeAuditUpdate(DALSecurityQuery query)
   {
      return executeAuditUpdate(query, "N/A", -1, "Unknown");
   }
   
   /**
    * <p>As above however allows for the specification of the activity and the
    *  primary key value</p>
    *
    *
    * @param query
    */
   
   public boolean executeAuditUpdate(DALSecurityQuery query, String activity, int primaryKey, String userName)
   {
      // get the post update data and register it
      
      ResultSet rs = null;
      boolean updateResult = false;
      try
      {
         rs = query.executeSelect();
      }
      catch (Exception e)
      {
         System.out.println(AuditLogManager.class.getName() + " - A failure occured when reading result set");
         try
         {
            System.out.println("Query : " + query.convertSelectQueryToString());
         }
         catch (Exception e1)
         {}
         e.printStackTrace();
         return false;
      }
      
      int rowCount = 0;
      try
      {
         while (rs.next())
         {
            rowCount++;
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      Map originalData = null;
      Map newData = null;
      
      if (rowCount > 1)
      {
         System.out.println("Query not definitive enough to log - please ref. to ix log");
      }
      else
      {
         originalData = ResultSetConversions.convertResultSetToMap(rs);
      }
      
      
      try
      {
         updateResult = query.executeUpdateWhenAuditing();
         if (updateResult == false)
         {
            LogService.log(LogService.WARN, this.getClass().getName() + " - Update to data has potentially failed");
            return false;
         }
         else
         {
            
            ResultSet rs1 = query.executeSelect();
            int rowCount1 = 0;
            while (rs1.next())
            {
               rowCount1++;
            }
            
            if (rowCount1 > 1)
            {
               System.out.println("Update was too large to log - please ref to ix log");
            }
            else
            {
               // work out the differences
               newData = ResultSetConversions.convertResultSetToMap(rs1);
               Map differences = MapComparator.getMapDifferences(originalData, newData);
               
               // DEBUG
               
               DebugUtilities.displayStringBasedMapContents(differences);
               
               // END DEBUG
               
               // Map back to database
               String domain = query.getUpdateDomain();
               
               AuditLogItem logItem = createAuditLogItem(differences, primaryKey, userName, "", domain);
               
               AuditLogPersistance persistance = new AuditLogStandardPersistance();
               persistance.persistLog(logItem);
               
               // and we're done
               return updateResult;
            }
            
            
            
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return false; // and hey, why not?
   }
   
   private static AuditLogPersistance getPersistance()
   {
      return new AuditLogStandardPersistance();
   }
   
   public static AuditLogItem createAuditLogItemInstance()
   {
      AuditLogItem logItem = new AuditLogItemStandard();
      return logItem;
   }
   
   private AuditLogItem createAuditLogItem(Map changedFields, int rowKey, String userName)
   {
      return createAuditLogItem(changedFields, rowKey, userName, "None specified or required", "NS");
      
   }
   
   
   public static AuditLogItem getAuditLog(int auditLogKey)
   {
      AuditLogPersistance persistance = getPersistance();
      System.out.println ("attempting to retrieve : " + auditLogKey);
      return persistance.retreiveLog(auditLogKey);
   }
   
   public static List getAuditLogsByDomainAndKey(String domain, int recordKey)
   {
      List logKeyList = getLogKeysByDomainAndKey(domain, recordKey);
      ArrayList auditLogsList = new ArrayList();
      
      System.out.println ("log key list : " + logKeyList.size());
      
      Iterator logKeyIterator = logKeyList.iterator();
      while (logKeyIterator.hasNext())
      {
         int key = Integer.parseInt((String) logKeyIterator.next());
         
         auditLogsList.add(getAuditLog(key));
      }
      
      return auditLogsList;
      
   }
   
   
   
   /**
    *
    * Retrieves all the stuff that is good and proper and that we're interested in.
    *
    */
   
   private static synchronized List getLogKeysByDomainAndKey(String domain, int rowKey)
   {
      DALQuery query = new DALQuery();
      HashMap logKeys = new HashMap();
      ArrayList logKeyList = new ArrayList();
      try
      {
         query.setDomain("AUDITLOGITEM", null, null, null);
         query.setField("AUDITLOGITEM_intAuditLogKey", null);
         query.setWhere(null, 0, "AUDITLOGITEM_intRowKey", "=", rowKey + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "AUDITLOGITEM_strDomain", "=", domain, 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rs = query.executeSelect();
         while (rs.next())
         {
            int logKey = rs.getInt("AUDITLOGITEM_intAuditLogKey");
            logKeys.put(logKey + "", logKey + "");
         }
         rs.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      Iterator logKeysIter = logKeys.keySet().iterator();
      
      while (logKeysIter.hasNext())
      {
         String key = (String) logKeysIter.next();
         logKeyList.add(key);
      }
      return logKeyList;
      
   }
   
   
   
   
   private AuditLogItem createAuditLogItem(Map changedFields, int rowKey, String userName, String reason, String domain)
   {
      
      final int ORIGINALVALUE = 0;
      final int NEWVALUE = 1;
      Set changedFieldKeys = changedFields.keySet();
      Iterator changedFieldKeysIter = changedFieldKeys.iterator();
      
      AuditLogItem tempItem = createAuditLogItemInstance();
      tempItem.setUser(userName);
      tempItem.setReason(reason);
      tempItem.setActivity("tempActivity");
      tempItem.setDomain(domain);
      
      while (changedFieldKeysIter.hasNext())
      {
         String fieldKey = (String) changedFieldKeysIter.next();
         String[] changedData = null;
         if (changedFields.get(fieldKey) instanceof String[])
         {
            changedData = (String[]) changedFields.get(fieldKey);
            String longFieldName = DALInformation.convertShortNameToLongName(fieldKey);
            tempItem.setFieldData(new AuditLogFieldData(longFieldName, changedData[ORIGINALVALUE], changedData[NEWVALUE], rowKey));
         }
         else
         {
            System.out.println("Unknown data type detected!");
         }
         
      }
      return tempItem;
   }
}
