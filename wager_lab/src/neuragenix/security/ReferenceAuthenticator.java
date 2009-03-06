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


public class ReferenceAuthenticator implements Authenticator

{



    /** AuthToken to pass to other channels */    
	private AuthToken authToken;
        
        private static String FIELD_SECURITY = "Field security";
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


    			DALQuery query = new DALQuery();                                  
                        
                        Vector formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_usergroup");
 
                     
                        // get the user's current groups
                        query.setDomain("USERUSERGROUP",null,null,null);
			query.setFields(formFields,null);
			query.setWhere(null,0,"USERUSERGROUP_strUserName","=",username,
				0,DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND",0,"USERUSERGROUP_intDeleted","=","0",
				0,DALQuery.WHERE_HAS_VALUE);
			

			ResultSet rs = query.executeSelect();
 
			// store it in a vector
                        while(rs.next())

			{

				vecProfiles.add(rs.getString("USERUSERGROUP_intUsergroupKey"));


			}

                        
			query.reset();
			rs.close();


                        // get the mapping of usergroup and parent
			Hashtable hashUserGroup = new Hashtable();
			
			formFields = DatabaseSchema.getFormFields("cuseradmin_view_usergroup");
                        query.setDomain("USERGROUP",null,null,null);
			query.setFields(formFields,null);
                        query.setWhere(null,0,"USERGROUP_intDeleted", "=", "0",0, DALQuery.WHERE_HAS_VALUE);
			rs = query.executeSelect();

			// store in a hash table
                        while(rs.next()){
			                            
			  hashUserGroup.put(rs.getString("USERGROUP_intUsergroupKey"),
					rs.getString("USERGROUP_intParentKey"));
			
			}
                       

			
			query.reset();
			rs.close();

        
                        // add children group to user's group
			
                        int i, vtSize;
                        vtSize = vecProfiles.size();
                        
                        for(i = 0;  i < vtSize; i++){
			
                                // if current group has children
                                if(hashUserGroup.containsValue(vecProfiles.get(i))){
                                        
                                        // get all the children's keys
                                        Enumeration e = hashUserGroup.keys();
					while(e.hasMoreElements()){
						
                                                String temp = (String)e.nextElement();
						
                                                // check for a single child
                                                if(hashUserGroup.get(temp).equals(vecProfiles.get(i).toString())){
							boolean flag=false;
							
                                                        // check if it has been added
                                                        for(int j=0; j < vecProfiles.size(); j++)
  							  if(vecProfiles.get(j).toString().equals(temp)) flag=true;
							
							// not added, add now
                                                        if(!flag) vecProfiles.add(temp);
                                                      

						}
					}
				}
                                
                                // get the new size 
                                vtSize = vecProfiles.size();
                                
			}

                        


// get permissions for user


			  formFields = DatabaseSchema.getFormFields("cuseradmin_view_permission_activity");			
	  	  	  query.setDomain("PERMISSION",null,null,null);
			  query.setDomain("ACTIVITY","PERMISSION_intActivityKey","ACTIVITY_intActivityKey",
					"INNER JOIN");
			  query.setFields(formFields,null);
                          
                          
                          // get permission for single group
                          for(i = 0 ; i < vecProfiles.size() ; i++ ){   

                                query.clearWhere();
                                query.setWhere(null,0,"ACTIVITY_intDeleted","=","0",
                                    0,DALQuery.WHERE_HAS_VALUE);
                                query.setWhere("AND",0,"PERMISSION_intDeleted","=","0",
                                    0,DALQuery.WHERE_HAS_VALUE);
                                query.setWhere("AND",0,"PERMISSION_intUsergroupKey","=",vecProfiles.get(i).toString(),
                                    0,DALQuery.WHERE_HAS_VALUE);


	
                                rs = query.executeSelect();
                                // add in activity vector
                                while(rs.next()){
                                    String strActivityDesc = rs.getString("ACTIVITY_strActivityDesc");
                                    String strActivityName = rs.getString("ACTIVITY_strActivityName");
                                    String strPermissionGroupKey = rs.getString("PERMISSION_intGroupKey");
                                    
                                    if (strActivityDesc != null)
                                    {
                                        if(strActivityDesc.startsWith(FIELD_SECURITY))
                                        {
                                            atmp.addDenyActivity(strActivityName, 
                                                  strPermissionGroupKey);
                                        }else{
                                            atmp.addActivity(strActivityName, 
                                                  Integer.parseInt((strPermissionGroupKey)));
                                        }
                                    }
				}

				rs.close();

			}

                        // update it
			authToken = atmp;

			return true;

		}

		catch(Exception e)

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

