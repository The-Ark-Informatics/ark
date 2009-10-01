/*
 * AuditLogItemStandard.java
 *
 * Created on 12 December 2005, 14:02
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.auditlogger;

import java.util.Date;
import java.util.List;

/**
 *
 * @author dmurley
 */
public class AuditLogItemStandard implements AuditLogItem
{
   
   private List fieldDataItems = new java.util.ArrayList();
   private String user = "";
   private String reason = "";
   private String domain = "";
   private String activity = "";
   private Date date;
   
   /** Creates a new instance of AuditLogItemStandard */
   public AuditLogItemStandard()
   {
      
   }

   public void setFieldData(AuditLogFieldData fieldData)
   {
      fieldDataItems.add(fieldData);
   }

   public void setUser(String user)
   {
      this.user = user;
   }

   public void setReason(String reason)
   {
      this.reason = reason;
   }

   public void setDomain(String domain)
   {
      this.domain = domain;
   }

   public List getFieldData()
   {
      return fieldDataItems;
   }

   public void setFieldData(List fieldDataItems)
   {
      this.fieldDataItems = fieldDataItems;
   }

   public String getUser()
   {
      return user;
   }

   public String getReason()
   {
      return reason;
   }

   public String getDomain()
   {
      return domain;
   }

   public void setActivity(String activity)
   {
      this.activity = activity;
   }

   public String getActivity()
   {
      return activity;
   }

   public void setDate(java.util.Date date)
   {
      this.date = date;
   }

   public java.util.Date getDate()
   {
      return date;
   }
   
   
   
   
}
