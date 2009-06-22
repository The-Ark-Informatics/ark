/*
 * AuditLogStandardPersistance.java
 *
 * Created on 12 December 2005, 16:37
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.auditlogger;

/**
 *
 * @author dmurley
 */
public class AuditLogStandardPersistance extends AuditLogPersistance
{
   
   /** Creates a new instance of AuditLogStandardPersistance */
   public AuditLogStandardPersistance()
   {
   }

   public java.util.List retrieveLog(String activity, int dataKey)
   {

      java.util.List retValue;
      
      retValue = super.retrieveLog(activity, dataKey);
      return retValue;
   }

   public AuditLogItem retreiveLog(int logKey)
   {

      AuditLogItem retValue;
      
      retValue = super.retreiveLog(logKey);
      return retValue;
   }

   public void persistLog(AuditLogItem log) throws neuragenix.genix.auditlogger.exception.FailedToPersistException
   {

      super.persistLog(log);
   }
   
}
