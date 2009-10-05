/*
 * MapComparator.java
 *
 * Created on 19 December 2005, 13:45
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.auditlogger.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dmurley
 */
public class MapComparator
{
   
   /** Creates a new instance of MapComparator */
   protected MapComparator()
   {
   }
   
   
   /**
    *
    *  Compares two maps - returns differences
    *  Differences will be returned as "key", {"differenceA", "differenceB"}
    *
    *  Comparison occurs by checking for everything that is different in MapB compared to MapA.
    *  It DOES NOT take into account additional keys in MapB.
    *  
    */
   public static Map getMapDifferences(Map mapA, Map mapB)
   {
      Map differenceMap = new HashMap();
      Set mapAKeys = mapA.keySet();
      Iterator mapAKeysIterator = mapAKeys.iterator();
      
      while (mapAKeysIterator.hasNext())
      {
         String keyName = (String) mapAKeysIterator.next();
         if (mapB.containsKey(keyName))
         {
            String mapAValue = (String) mapA.get(keyName);
            String mapBValue = (String) mapB.get(keyName);
            
            if (mapAValue == null)
            {
               mapAValue = "";
            }
            
            if (mapBValue == null)
            {
               mapBValue = "";
            }
            
            
            if (!(mapAValue.equals(mapBValue)))
            {
               String[] differences = new String[2];
               differences[0] = mapAValue;
               differences[1] = mapBValue;
               differenceMap.put(keyName, differences);
            }
            
         }
         else
         {
            String[] differences = new String[2];
            differences[0] = (String) mapA.get(keyName);
            differences[1] = "";
            differenceMap.put(keyName, differences);
         }
         
      }
      
      return differenceMap;
      
   }
}
