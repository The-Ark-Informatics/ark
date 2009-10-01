/** 

 * Copyright (c) 2003 Neuragenix. All Rights Reserved.

 * ReferenceAuthenticator.java

 * Created on 11 December 2003, 00:00

 * @author  Long Tran, email: ltran@neuragenix.com

 * 

 * Description: 

 * Class for athenticating users

 * // fix intDeleted and recursive code

 **/



// package
package neuragenix.security;


// neuragenix classes
import neuragenix.security.exception.*;
import neuragenix.dao.*;

// java classes
import java.sql.*;
import java.util.*;
import org.jasig.portal.RDBMServices;
import org.jasig.portal.services.LogService;

/**
 *    Public class
 */


public class WAGERAuthenticator implements Authenticator

{

	private Connection con = null;

    /** AuthToken to pass to other channels */    
	private AuthToken authToken;
        
        private static String FIELD_SECURITY = "Field security";
        private static String APP_GROUP = "wager_lab";
        
        /**
         *    Method to log user in and set user permissions to AuthToken
         * @param username String username to be logged in
         * @param password String password has no use.
         * @return boolean value of status of logging in
         */        
        
	public boolean login(String username, String password)

	{

		try

		{

			WAGERAuthTokenImpl atmp = new WAGERAuthTokenImpl();

			atmp.setUserIdentifier(username);
			Vector vecProfiles = new Vector();

			 if (con == null)
	            {
	                con = RDBMServices.getConnection();
	            
	                
	                
	                
	            }
			 
			 PreparedStatement getPrivileges = con.prepareStatement("select s.studykey, w.* from table(janus.ldap_person_privileges(?,?)) w, zeus.study s where s.ldap_group = w.study_name");
			 getPrivileges.setString(1, username);
			 getPrivileges.setString(2,APP_GROUP);
			 ResultSet rs = getPrivileges.executeQuery();
			 
			 
			 int studyKey;
			 String studyName;
			 String privilege;
			 String role;
			 while (rs.next()) {
			        studyKey = rs.getInt("STUDYKEY");
			        studyName = rs.getString("STUDY_NAME");
			        privilege = rs.getString("PRIVILEGE_NAME");
			        role = rs.getString("ROLE");
			        System.err.println("**Adding privilege: " + privilege + " to study: "+studyKey);
			        atmp.addActivity(privilege, 
                            studyKey);
			        
			        
			       
			}
				rs.close();
				String siteName;
				Integer siteKey;
				PreparedStatement getSites = con.prepareStatement("select w.site_name, s.sitekey from table(janus.ldap_person_sites(?,null)) w, ix_inv_site s where s.ldap_group = w.site_name");
				 getSites.setString(1, username);
				rs = getSites.executeQuery();
				while (rs.next()) {
					siteName = rs.getString("SITE_NAME");
					siteKey = new Integer(rs.getInt("SITEKEY"));
					 System.err.println("**Adding site access: " + siteName + " = " + siteKey);
					 atmp.addSite(siteName,siteKey);
				}
				
				
				
				authToken = atmp;
				getSites.close();
				rs.close();
				return true;

		}catch(Exception e)

		{

			System.err.println("Error in ReferenceAuthenticator: " + e.toString());	

			e.printStackTrace(System.err);

			authToken = null;

			return false;

		}

	//	return false;

	}

	

	

        /**
         *    Method to return the private AuthToken
         * @return AuthToken has all permissions of the current user
         * @throws SecurityNotLoggedInException thows exception to channel
         */        
	public AuthToken getAuthToken() throws SecurityNotLoggedInException

	{

		if(authToken == null)

		{

			throw new  SecurityNotLoggedInException("Not logged in yet!");

		}

		return authToken;

	}



	/**
         * Equivalent to login(username, password); getAuthToken();
         * @param username username
         * @param password String password
         * @throws SecurityNotLoggedInException throws exception
         * @return boolean value of logging in status
         */

	public AuthToken getAuthToken(String username, String password) throws SecurityNotLoggedInException

	{

		login(username, password);

		return getAuthToken();

	}



}

