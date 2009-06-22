
package neuragenix.genix.auditlogger;

import java.util.Date;
import java.util.List;
/**
 * <p></p>
 *
 */
public interface AuditLogItem
{
   
   public void setFieldData(List fieldDataItems);
   
   
   /**
    * <p>Does ...</p>
    *
    *
    * @param fieldData
    */
   public void setFieldData(AuditLogFieldData fieldData);
   /**
    * <p>Does ...</p>
    *
    */
   public void setUser(String user);
   /**
    * <p>Does ...</p>
    *
    */
   public void setReason(String reason);
   /**
    * <p>Does ...</p>
    *
    */
   public void setDomain(String domain);
   public String getUser();
   /**
    * <p>Does ...</p>
    *
    */
   public String getReason();
   /**
    * <p>Does ...</p>
    *
    */
   public String getDomain();
   
   public List getFieldData();

   public void setActivity(String activity);
   
   public String getActivity();
   
   public Date getDate();
   
   public void setDate(Date date);
   
}


