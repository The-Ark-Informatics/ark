
package neuragenix.genix.auditlogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import neuragenix.dao.DALQuery;
import neuragenix.dao.DatabaseSchema;
import java.sql.ResultSet;
import neuragenix.genix.auditlogger.exception.FailedToPersistException;
import neuragenix.dao.exception.DAOQueryInvalidDomain;
import neuragenix.common.Utilities;
/**
 * <p></p>
 *
 */
abstract class AuditLogPersistance
{
   
   public void persistLog(AuditLogItem log) throws FailedToPersistException
   {
      
      if (log == null)
         throw new FailedToPersistException("Log item was null");
      
      boolean insertResult = false;
      
      DALQuery query = new DALQuery();
      try
      {
         query.setDomain("AUDITLOG", null, null, null);
         query.setField("AUDITLOG_strUser", log.getUser());
         query.setField("AUDITLOG_strReason", log.getReason());
         query.setField("AUDITLOG_dtDate", Utilities.convertDateForDAL(new java.util.Date(System.currentTimeMillis())));
         //query.setField("AUDITLOG_strDomain", log.getDomain());
         insertResult = query.executeInsert();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      if (insertResult == false)
         throw new FailedToPersistException("Unable to update database");
      
      
      int auditLogKey = query.getInsertedRecordKey();
      
      // now we need to add all the audit log ITEMS into the databae
      
      List itemsToAdd = log.getFieldData();
      
      Iterator items = itemsToAdd.iterator();
      
      query.reset();
      try
      {
         query.setDomain("AUDITLOGITEM", null, null, null);
      }
      catch (neuragenix.dao.exception.DAOQueryInvalidDomain idE)
      {
         idE.printStackTrace();
      }
      
      while (items.hasNext())
      {
         AuditLogFieldData logItem = (AuditLogFieldData) items.next();
         try
         {
            query.clearFields();
            query.setField("AUDITLOGITEM_strDomain", log.getDomain());
            query.setField("AUDITLOGITEM_strField", logItem.getField());
            query.setField("AUDITLOGITEM_strOriginalValue", logItem.getOriginalValue());
            query.setField("AUDITLOGITEM_strNewValue", logItem.getNewValue());
            query.setField("AUDITLOGITEM_intRowKey", logItem.getRowKey() + "");
            query.setField("AUDITLOGITEM_intAuditLogKey", auditLogKey + "");
            query.executeInsert();
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      
   }
   
   public neuragenix.genix.auditlogger.AuditLogItem retreiveLog(int logKey)
   {
      DALQuery query = new DALQuery();
      Vector vtFormFields = null;
      
      vtFormFields = DatabaseSchema.getFormFields("auditlog_view_log");
      
      List auditLogItems = retrieveAuditLogItems(logKey);
      AuditLogItem logItem = AuditLogManager.createAuditLogItemInstance();
      
      try
      {
         query.setDomain("AUDITLOG", null, null, null);
         query.setFields(vtFormFields, null);
         query.setWhere(null, 0, "AUDITLOG_intAuditLogKey", "=", logKey + "", 0, DALQuery.WHERE_HAS_VALUE);



         ResultSet rs = query.executeSelect();

         if (rs.first())
         {
            // found the log with the matching key
            logItem.setUser(rs.getString("AUDITLOG_strUser"));
            logItem.setReason(rs.getString("AUDITLOG_strReason"));
            logItem.setDate(rs.getDate("AUDITLOG_dtDate"));
            logItem.setFieldData(auditLogItems);


         }

         rs.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return logItem;
   }
   
   private List retrieveAuditLogItems(int auditLogKey)
   {
      DALQuery query = new DALQuery();
      List auditLogItemList = new ArrayList();
      Vector vtFormFields = null;
      
      try
      {


         vtFormFields = DatabaseSchema.getFormFields("auditlog_view_items");
         query.setDomain("AUDITLOGITEM", null, null, null);
         query.setFields(vtFormFields, null);
         query.setWhere(null, 0, "AUDITLOGITEM_intAuditLogKey", "=", auditLogKey + "", 0, DALQuery.WHERE_HAS_VALUE);

         ResultSet rs = query.executeSelect();

         String domain = null;

         while (rs.next())
         {
            AuditLogFieldData tempData = new AuditLogFieldData();
            tempData.setField(rs.getString("AUDITLOGITEM_strField"));
            tempData.setNewValue(rs.getString("AUDITLOGITEM_strNewValue"));
            tempData.setOriginalValue(rs.getString("AUDITLOGITEM_strOriginalValue"));
            tempData.setRowKey(rs.getInt("AUDITLOGITEM_intRowKey"));
            domain = rs.getString("AUDITLOGITEM_strDomain");
            auditLogItemList.add(tempData);
         }

         rs.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      return auditLogItemList;
   }
   
   
   
   public java.util.List retrieveLog(String activity, int dataKey)
   {
      // your code here
      return null;
   }
}
