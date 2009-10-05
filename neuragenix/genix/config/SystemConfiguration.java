package neuragenix.genix.config;

/*
 * SystemConfiguration.java
 *
 * Created on 15 September 2005, 14:24
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

import java.sql.ResultSet;
import neuragenix.dao.*;

/**
 *
 * @author dmurley
 */
public class SystemConfiguration
{
   
   /**
    * Creates a new instance of SystemConfiguration 
    */
   private SystemConfiguration()
   {
   }
   
   public static String getConfigValue(String nameOfConfigItem)
   {
      DALQuery query = new DALQuery();
      ResultSet rs = null;
      String configValue = null;
      try
      {
         query.setDomain("SYSCONFIG", null, null, null);
         query.setField("SYSCONFIG_strValue", null);
         query.setWhere(null, 0, "SYSCONFIG_strName", "=", nameOfConfigItem, 0, DALQuery.WHERE_HAS_VALUE);
         rs = query.executeSelect();
         if (rs.next())
         {
            configValue = rs.getString("SYSCONFIG_strValue");
         }
         rs.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
       
      return configValue;
      
      
      
   }
     
   public static void setConfigValue(String nameOfConfigItem, String value)
   {
      // check if this value exists in the database
      String existingValue = getConfigValue(nameOfConfigItem);
      
      boolean newInsert = false;
      
      if (existingValue == null)
      {
         newInsert = true;
      }
      
      DALQuery query = new DALQuery();
      
      try
      {
         query.setDomain("SYSCONFIG", null, null, null);
         query.setField("SYSCONFIG_strValue", value);

         if (newInsert == false)
         {
            query.setWhere(null, 0, "SYSCONFIG_strName", "=", nameOfConfigItem, 0, DALQuery.WHERE_HAS_VALUE);
            query.executeUpdate();
         }
         else
         {
            query.setField("SYSCONFIG_strName", nameOfConfigItem);
            query.executeInsert();
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
   }
}
