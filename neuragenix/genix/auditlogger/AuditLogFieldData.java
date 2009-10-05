
package neuragenix.genix.auditlogger;

import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DBField;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * <p></p>
 *
 */
public class AuditLogFieldData
{
   private String field;
   private String originalValue;
   private String newValue;
   private int rowKey;
   
   public AuditLogFieldData()
   {}
   
   
   public AuditLogFieldData(String field, String originalValue, String newValue, int rowKey)
   {
      this.field = field;
      this.originalValue = originalValue;
      this.newValue = newValue;
      this.rowKey = rowKey;
   }
   
   
   public String getField()
   {
      return field;
   }

   public String getLongFieldName()
   {
       String tempField = "";
       DBField databaseField = null;
       
       tempField = getField().substring(1, getField().length() - 1);
       databaseField = (DBField) DatabaseSchema.getFields().get(tempField);
       
       if (databaseField != null)
       {
          return databaseField.getLabelInForm();
       }
       else
       {
          return field;
       }
   }
     
   public void setField(String field)
   {
      this.field = field;
   }

   public String getOriginalValue()
   {
      return originalValue;
   }

   public void setOriginalValue(String originalValue)
   {
      this.originalValue = originalValue;
   }

   public String getNewValue()
   {
      return newValue;
   }

   public void setNewValue(String newValue)
   {
      this.newValue = newValue;
   }

   public int getRowKey()
   {
      return rowKey;
   }

   public void setRowKey(int rowKey)
   {
      this.rowKey = rowKey;
   }



}
