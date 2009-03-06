/*
 * DebugUtilities.java
 *
 * Created on 19 December 2005, 13:57
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.auditlogger.utilities;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dmurley
 */
public class DebugUtilities
{
 
   
   /** Creates a new instance of DebugUtilities */
   public DebugUtilities()
   {
   }
   
   public static void displayStringBasedMapContents(Map mapToDisplay)
   {
      if (mapToDisplay == null)
      {
         logData ("System was sent a null map - unable to display contents");
      }
      else
      {
         logData("Displaying Contents of Map");
         logData("---------------------------");
      }
      Set keySet = mapToDisplay.keySet();
      Iterator keyIterator = keySet.iterator();
      
      while (keyIterator.hasNext())
      {
         String key = (String) keyIterator.next();
         Object objData = mapToDisplay.get(key);
         
         if (objData instanceof String)
         {
            String data = (String) objData;
            logData("Key : " + key + "\t\t" + "Data : " + data);
         }
         else if (objData instanceof String[])
         {
            String commaValues = "";
            String[] data = (String[]) objData;
            for (int i = 0; i < data.length; i++)
            {
               commaValues += data[i];
               if (i < data.length - 1)
               {
                  commaValues += ", ";
               }
            }
            logData("Key : " + key + "\t\t" + "Data : " + commaValues);
         }
         else
         {
            logData("Key : " + key + "\t\t" + "Data : indeterminable type");
         }
         
      }
   }
   
   private synchronized static void logData(String dataToLog)
   {
      // System.out.println("[Debug utilities] - " + dataToLog);
   }
      
}
