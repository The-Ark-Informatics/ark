/*
 * DALSecurityQuery.java
 *
 * Created on 24 August 2005, 19:59
 *
 * Copyright (C) 2005 Neuragenix Pty Ltd
 */

package neuragenix.dao;


import java.util.Vector;

import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;

import neuragenix.security.AuthToken;
import neuragenix.security.exception.SecurityException;

import neuragenix.dao.exception.*;
import neuragenix.genix.auditlogger.AuditLogManager;
/**
 *
 * @author dmurley
 */
public class DALSecurityQuery extends DALQuery
{
   
   /** Creates a new instance of DALSecurityQuery */
   public DALSecurityQuery()
   {
   }
   
   protected AuthToken authToken;
   
   protected String strActivityRequested;
   
   public DALSecurityQuery(String activity, AuthToken auth) throws DAONotAuthorisedException, SecurityException
   {
	   
	   strActivityRequested = activity;
	      
	      authToken = auth;
	   
	   if(authToken != null && !authToken.hasActivity(strActivityRequested)) {
		   DALLogger.instance().log(DALLogger.ALERT, DALLogger.PRIORITY_NORMAL, DALLogger.RESULT_FAILURE, "Query", authToken.getUserIdentifier() + " attempted a Query for activity " + strActivityRequested, authToken.getUserIdentifier());
	         
	         throw new DAONotAuthorisedException("Security Violation: Attempting to use a disallowed activity.") ;
		   
	   }
	  
	   
   }
   
   
   public DALSecurityQuery(String activity, int intStudyKey, AuthToken auth) throws Exception
   {
      
      strActivityRequested = activity;
      
      authToken = auth;
      
      
      
      if(authToken != null) {
    	  if (!authToken.hasActivity(strActivityRequested) || (intStudyKey != -1 && !authToken.hasActivity(strActivityRequested,intStudyKey) )) {
    		  
         DALLogger.instance().log(DALLogger.ALERT, DALLogger.PRIORITY_NORMAL, DALLogger.RESULT_FAILURE, "Query", authToken.getUserIdentifier() + " attempted a Query for activity " + strActivityRequested, authToken.getUserIdentifier());
         
         throw new DAONotAuthorisedException("Security Violation: Attempting to use a disallowed activity.") ;
    	  } 
      }
      
     
      
   }
   
   public void setDomain(String strADomain,
   		String strAField1,
   		String strAField2,
   		String strAJoinType) throws DAOQueryInvalidDomain
   		{
   	if (domainExists(strADomain))
   	{
   		//Ok, so the domain is valid; If we are adding STUDY as a domain
   		//Make sure the studies returned are limited.
   		/*if (strADomain.equals("STUDY")) {
   			try {
   			this.setWhere(null, 0, "STUDY_intStudyID", "IN", StudyUtilities.getStudyIDSQLString(authToken.getStudyList()), 0,
					DALQuery.WHERE_HAS_VALUE);
   			}catch ( DAOQueryInvalidField dif) {
   				// if this happens its author's own fault.
   				System.err.println("Crap");
   			}
   		}*/
   		if (vtDomains.contains(strADomain)) {
   		int vindex = vtDomains.indexOf(strADomain);
   		Vector<String> v = (Vector<String>) vtFirstJoinFields.get(vindex);
   		v.add(strAField1);
   		Vector v2 = (Vector) vtSecondJoinFields.get(vindex);
   		v2.add(strAField2);
   			
   		} else {
   		vtDomains.add(strADomain);
   		Vector field1 = new Vector();
   		field1.add(strAField1);
   		vtFirstJoinFields.add(field1);
   		Vector field2 = new Vector();
   		field2.add(strAField2);
   		vtSecondJoinFields.add(field2);
   		vtJoinTypes.add(strAJoinType);
   		}
   	}
   	else
   	{
   		LogService.instance().log(LogService.ERROR, "DAOQueryInvalidDomain - The domain " + strADomain + " is invalid");
   		throw new DAOQueryInvalidDomain("The domain supplied is invalid");
   	}
   		}
   
   
   public boolean executeUpdate() throws DAOException, DAOUpdateInvalidDomain, DBMSInvalidOperator
   {
	  boolean useLogger = false; 
      try
      {
    	  useLogger = PropertiesManager.getPropertyAsBoolean("neuragenix.dao.useFieldAuditLogger");
      }
      catch (Exception e)
      {
    	  e.printStackTrace();
    	  LogService.log(LogService.ERROR, "Unable to read property - neuragenix.dao.useFieldAuditLogger, defaulting to false");
      }
	  
      
      if (useLogger == true)
      {	  
	      AuditLogManager logManager = new AuditLogManager();
	      String domain = getUpdateDomain();
	      
	      int primaryKey = -1;
	      try{
	         primaryKey = Integer.parseInt(getUpdatePrimaryKey(domain));   
	      }
	      catch (Exception e)
	      {
	    	  LogService.log(LogService.DEBUG, "Unable to parse integer - most likely received incorrect value when attempting to get primary key");
	      }
	      
	      return logManager.executeAuditUpdate(this, strActivityRequested, primaryKey, authToken.getUserIdentifier());
      }
      else
    	  return super.executeUpdate();
   }
   
   public boolean executeUpdateWhenAuditing() throws DAOException, DAOUpdateInvalidDomain, DBMSInvalidOperator
   {
      return super.executeUpdate();
   }
   
   public String getUpdateDomain()
   {
      if (vtDomains.size() != 1)
         return "";
      else
         return (String) vtDomains.get(0);
      
   }
   
   private String getUpdatePrimaryKey(String domainName)
   {
      // Yuk code lies below.  1001 apologies.
      
      String pkey = "";
      pkey = DatabaseSchema.getPrimaryKey(domainName);
      DBField field = (DBField) DatabaseSchema.getFieldsByExternalName().get(domainName + "_" + pkey);
      String ipkey = field.getInternalName();
      String primaryKey = ipkey.substring(1, ipkey.length() - 1);
     LogService.log(LogService.WARN," Using primarykey = " + primaryKey); 
      if (vtWhereFields.contains(primaryKey))
      {
         LogService.log(LogService.DEBUG, "Found Primary Key For Audit Logger");
         
         int keyloc = vtWhereFields.indexOf(primaryKey);
        
         String objValue = (String) vtWhereObjects.get(keyloc);
         
         return objValue;
      }
      else
      {
         LogService.log(LogService.WARN, "[DALSecurityQuery/AuditLogger] - Unable to locate primary key for audit log");
      }
      return "-1";
   }
   
}
