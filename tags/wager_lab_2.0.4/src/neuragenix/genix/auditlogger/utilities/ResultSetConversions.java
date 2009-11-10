/*
 * ResultSetConversions.java
 *
 * Created on 19 December 2005, 13:51
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.auditlogger.utilities;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import neuragenix.dao.*;

/**
 *
 * @author dmurley
 */
public class ResultSetConversions
{
   
   /** Creates a new instance of ResultSetConversions */
   protected ResultSetConversions()
   {
   }
   
   public static List convertResultSetToFormFields(ResultSetMetaData metaData)
   {
      ArrayList fieldList = new ArrayList();
      int columnCount = 0;
      try
      {
         columnCount = metaData.getColumnCount();
      }
      catch (java.sql.SQLException sqle)
      {
         System.out.println ("SQL EC: " + sqle.getErrorCode());
         sqle.printStackTrace();
      }
      
      for (int i = 1; i < columnCount; i++)
      {
         String columnName = "";
         try
         {
            columnName = metaData.getColumnName(i);
         }
         catch (java.sql.SQLException sqle)
         {
            System.out.println ("SQL EC: " + sqle.getErrorCode());
            sqle.printStackTrace();
         }
            
         //  convert back to long name
         
         try
         {
            //DBField field = (DBField) DatabaseSchema.getShortFields().get(columnName);
            
            //String fullName = field.getInternalName();
            
            //System.out.println ("Converting : " + columnName + " to " + fullName);
            
            // String fullName = (String) DatabaseSchema.getShortFields().get(columnName);
            fieldList.add(columnName);   
         }
         catch (ClassCastException cce)
         {
            System.out.println ("There was a major failure when getting short field name");
            
            cce.printStackTrace();
         }
      }
      return fieldList;
   }
   
   public static Map convertResultSetToMap(ResultSet rs)
   {
      ResultSetMetaData metaData = null;
      try{
         metaData = rs.getMetaData();
      }
      catch (java.sql.SQLException sqlex)
      {
         sqlex.printStackTrace();
         return new HashMap();
      }
      HashMap resultData = new HashMap();
      List fieldList = convertResultSetToFormFields(metaData);
      Iterator fieldListIterator = fieldList.iterator();
      
      boolean movedFirst = false;
      
      try{
         movedFirst = rs.first();
      }
      catch (java.sql.SQLException ex)
      {
         ex.printStackTrace();
      }
      if (movedFirst)
      {
         while (fieldListIterator.hasNext())
         {
            String fieldName = (String) fieldListIterator.next();
            try
            {
               resultData.put(fieldName, rs.getString(fieldName));
            }
            catch (java.sql.SQLException sqle)
            {
               sqle.printStackTrace();
            }
         }
      }
      else
      {
         System.out.println("[ResultSetConversions] Unable to move back to first or there were no results");
      }
      return resultData;
   }
}
