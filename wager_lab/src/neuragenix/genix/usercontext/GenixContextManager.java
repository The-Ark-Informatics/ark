/*
 * GenixContextManager.java
 *
 * Created on 13 September 2005, 00:19
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.usercontext;

import java.util.HashMap;
import neuragenix.security.AuthToken;
/**
 *
 * @author dmurley
 */
public class GenixContextManager
{
   
   private static HashMap threads = new HashMap();
   
   
   /**
    * Creates a new instance of GenixContextManager 
    */
   private GenixContextManager()
   {
   }
   
   public static void register(AuthToken authToken)
   {
      Thread currentThread = Thread.currentThread();
      threads.put(currentThread, authToken);
   }
   
   public static UserContext getContext()
   {
      Thread currentThread = Thread.currentThread();
      AuthToken currToken = (AuthToken) threads.get(currentThread);
      UserContext newContext = new UserContext(currToken);
      return newContext;
   }

   public static void deregister()
   {
      Thread currentThread = Thread.currentThread();
      threads.remove(currentThread);
   }
}
