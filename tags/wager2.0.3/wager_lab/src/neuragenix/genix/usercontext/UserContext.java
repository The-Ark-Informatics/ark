/*
 * UserContext.java
 *
 * Created on 13 September 2005, 00:18
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.genix.usercontext;

import neuragenix.security.AuthToken;

/**
 *
 * @author dmurley
 */
public class UserContext
{
   
   private AuthToken authToken = null;
   
   /** Singleton - no instantiation */
   protected UserContext()
   {
   }
   
   protected UserContext(AuthToken authToken)
   {
      this.authToken = authToken;
   }
   
   public AuthToken getAuthToken()
   {
      return authToken;
   }
}
