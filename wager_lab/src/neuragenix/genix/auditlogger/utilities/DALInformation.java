/*
 * DALInformation.java
 *
 * Created on 19 December 2005, 15:51
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.auditlogger.utilities;

import neuragenix.dao.*;

/**
 *
 * @author dmurley
 */
public class DALInformation
{
   
   /**
    * Creates a new instance of DALInformation 
    */
   public DALInformation()
   {
   }
   
   public static String convertShortNameToLongName(String shortName)
   {
      try
      {
         DBField field = (DBField) DatabaseSchema.getShortFields().get(shortName);
            
         String fullName = field.getInternalName();
            
         return fullName;
      }
      catch (Exception e)
      {
         System.out.println("Was unable to convert the field");
         e.printStackTrace();
      }
      
      return "";
   }
   
   public static String getDomainName(String shortName)
   {
      try
      {
         DBField field = (DBField) DatabaseSchema.getShortFields().get(shortName);
            
         String domainName = field.getDomain();
            
         return domainName;
      }
      catch (Exception e)
      {
         System.out.println("Was unable to convert the field");
         e.printStackTrace();
      }
      
      return "";
   }
   
   
}
