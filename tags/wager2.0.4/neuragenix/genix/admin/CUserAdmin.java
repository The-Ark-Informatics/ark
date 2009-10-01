/**
 *
 * Copyright (c) 2003 Neuragenix. All Rights Reserved.
 *
 * CUserAdmin.java
 *
 * Created on 11 December 2003, 00:00
 *
 * @author  Long Tran, email: ltran@neuragenix.com
 *
 *
 * Description:
 *
 * Class for managing users and groups in an organization
 *
 *
 *
 **/

 
package neuragenix.genix.admin;

// portal classes

import org.jasig.portal.IChannel;

import org.jasig.portal.ChannelStaticData;

import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.ChannelRuntimeProperties;

import org.jasig.portal.PortalEvent;

import org.jasig.portal.PortalException;

import org.jasig.portal.utils.XSLT;

import org.xml.sax.ContentHandler;

import org.jasig.portal.security.*;

import org.jasig.portal.services.LogService;


// java classes
import java.util.*;
import java.io.*;
import javax.swing.tree.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;

// neuragenix classes
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.utils.Password;
import neuragenix.security.*;
import neuragenix.security.exception.*;

/** Class for managing users and groups
 * @author Long Tran {@link mailto:ltran@neuragenix.com}
 * @version 1.0
 */

public class CUserAdmin implements IChannel {
    // channel name for tree
    private static final String CHANNEL_NAME = "admin";
    
    private static final String FIELD_SECURITY = "Field security";
    
    // error msg
    private static final String LOCK_ERROR = "The record you are trying to update or delete is being viewed by others. Please try again later.";
    
    
    // constant for user group tree proccess
    private static final String ROOT_NAME = "All users";
    private static final String USERGROUP = "USERGROUP";
    private static final String USER = "USER";
    private static final int ROOT_ID = 0;
    
    private static final String SEARCH_ACTION = "search";
    
    // group actions
    private static final String GROUP_ACTION = "proccess_group";
    private static final String SAVE_GROUP_ACTION="save_group_details";
    private static final String ADD_SUB_GROUP_ACTION="add_sub_group";
    private static final String ADD_NEW_SUB_GROUP_ACTION="add_new_sub_group";
    private static final String DELETE_GROUP_ACTION="delete_group";
    private static final String SET_PERMISSION_ACTION="set_permission";
    private static final String SAVE_PERMISSION_ACTION="save_permission";
    
    // user actions
    private static final String USER_ACTION = "proccess_user";
    private static final String SAVE_USER_ACTION = "save_user_details";
    private static final String SAVE_USER_GROUP_ACTION = "save_user_group_details";
    private static final String SAVE_USER_PASSWORD_ACTION = "save_user_password";
    private static final String SET_PASSWORD_ACTION = "change_password";
    private static final String DELETE_USER_ACTION = "delete_user";
    private static final String ADD_USER_ACTION = "add_user";
    private static final String CHOOSE_USER_ACTION = "choose_user";
    private static final String ADD_NEW_USER_ACTION = "add_new_user";
    private static final String SET_USER_PASSWORD_ACTION = "set_user_password";
    
    // tree action
    private static final String EXPAND_ACTION = "expand";
    private static final String COLLAPSE_ACTION = "collapse";
    
    // security
    private static final String SECURITY_ERROR = "security_error";
    private static final String USERGROUP_ADMIN = "usergroup_admin";
    
    // data for rendering last page
    private StringBuffer strLastXML = null;
    private String strLastStylesheet;
    
    // lock

    Hashtable lockedUsers = new Hashtable();
    Hashtable lockedGroups = new Hashtable();
    private LockRequest userLock;
    private LockRequest lock;
    
    // variable to check the current grou for locking
    private String currentGroup;
    
    // boolean for user add proccess
    boolean success=false;
    // authToken
    private AuthToken authToken ; 
    
    // tree expand information
    Hashtable hsExpanded = new Hashtable();
    
    // At the very least, you'll need to keep track of the static data and the runtime data that the portal sends you.
    
    private ChannelStaticData staticData;
    
    private ChannelRuntimeData runtimeData;
    
    
    
    private String strCurrent; // This is used to know what the current page the user is on
    
    private static StringBuffer strXML; // used to create the xml document with the starting XML header
    
    private String strStylesheet; // Used to specify the stylesheet to use for the portal
    
    private String cloneuserID;
    
    private String cloneuser;
    
    boolean exists = false;
    
    
    
    /**
   
     *  Contructs the UserAdmin Channel
   
     */
    
    public CUserAdmin() {
        
        this.strStylesheet = "group_view"; // The default stylesheet
        
    }
    
    
    
    /**
  
     *  Returns channel runtime properties.
  
     *  Satisfies implementation of Channel Interface.
  
     *
  
     *  @return handle to runtime properties
  
     */
    
    public ChannelRuntimeProperties getRuntimeProperties() {
        
        return new ChannelRuntimeProperties();
        
    }
    
    /**
     *  Process layout-level events coming from the portal.
     *
     *  Satisfies implementation of IChannel Interface.
     * @param ev ev a portal layout event
     */
    
    public void receiveEvent(PortalEvent ev) {
        // If the user logout, destroy lock request object
        if (ev.getEventNumber() == PortalEvent.SESSION_DONE){
            try{
                
                if(lock!=null) lock.unlock();
                if(userLock!=null) userLock.unlock();
              
                lock = null;
                userLock = null;
                
            }catch(Exception e){
                
                 e.printStackTrace();
            
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                
            }
            
        }
    }
    
    /**
     *
     *  Receive static channel data from the portal.
     *
     *  Satisfies implementation of IChannel Interface.
     * @param sd sd static channel data
     */
    
    public void setStaticData(ChannelStaticData sd) {
        
        this.staticData = sd;
        
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
        
        InputStream file = CUserAdmin.class.getResourceAsStream("UserGroupTree.xml");
        TreeSchema.loadDomains(file,"UserGroupTree.xml");
        
        
        
       
        
    }
    
    /**
     *  Receive channel runtime data from the portal.
     *
     *  Satisfies implementation of IChannel Interface.
     * @param rd rd handle to channel runtime data
     */
    
    public void setRuntimeData(ChannelRuntimeData rd) {
        
        // try to catch all exception
        
        currentGroup = "-2";
        Utilities.printRuntimeData(rd);
       
        try  {
            
            this.runtimeData = rd;
            
            this.exists = false;
            
            strXML = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            if (!authToken.hasActivity(USERGROUP_ADMIN)){
                
                strXML.append(doSetUserPasswordAction());
                
                this.strStylesheet = SET_USER_PASSWORD_ACTION;
          
                return;
            }
            
            
          if(checkPermissions(USERGROUP_ADMIN, "manage usergroup")){
            // catch tree actions
            if(rd.getParameter("treeAction")!=null){
                if(rd.getParameter("treeAction").equals(EXPAND_ACTION)){
                    
                    
                    hsExpanded.put(rd.getParameter("id"),"0");
                }else if(rd.getParameter("treeAction").equals(COLLAPSE_ACTION)){
                    
                    hsExpanded.remove(rd.getParameter("id"));
                }
                
                
            }
            
            
            // generating body
            
            
            strXML.append("<body>");
            
            // process actions
            
            // check for first entry of channel
            if(rd.getParameter("action")!=null){
                
                // catch user actions
                if(rd.getParameter("action").equals(USER_ACTION)){
                    
                    // save user details caught
                    if(rd.getParameter(SAVE_USER_ACTION) != null){
                        
                        doUserSaveAction(); // perform needed tasks
                        
                        strXML.append(doUserClickAction()); // trigger function to display result and details
                        
                        this.strStylesheet=USER_ACTION;
                        
                        // set password for user caught
                    }else if(rd.getParameter(SET_PASSWORD_ACTION) != null){
                        
                        strXML.append( doSetPasswordAction() );
                        
                        this.strStylesheet = SET_PASSWORD_ACTION;
                        
                        // delete user caught, need to be re-considered with up_user_* tables
                    //}else if(rd.getParameter(DELETE_USER_ACTION) != null){
                        
                      //  strXML.append( doDeleteUserAction() );
                        
                      //  this.strStylesheet = DELETE_USER_ACTION;
                        
                        // no specific actions caught, which means display a user's details
                    
                    }else if(rd.getParameter(SEARCH_ACTION) != null){
                    
                        strXML.append( doSearchAction() );
                        
                        this.strStylesheet = SEARCH_ACTION;
                        
                    }else {
                        
                        strXML.append( doUserClickAction() );
                        
                        this.strStylesheet=USER_ACTION;
                        
                    }
                    
                    // catch actions on groups
                }
                else if(rd.getParameter("action").equals(GROUP_ACTION))
                {
                    
                    // save group details
                    if(rd.getParameter(SAVE_GROUP_ACTION)!=null)
                    {
                        
                        doGroupSaveAction();
                        
                        strXML.append( doGroupClickAction() );
                        
                        this.strStylesheet = GROUP_ACTION;
                        
                        // add sub group
                    }
                    else if(rd.getParameter(ADD_SUB_GROUP_ACTION)!=null)
                    {
                        
                        strXML.append( doAddSubGroupAction() );
                        
                        this.strStylesheet = ADD_SUB_GROUP_ACTION;
                        
                        // add new user, but not to group, just a dummy user
                    }
                    else if(rd.getParameter("create_user") != null)
                    {
                        String strTemp;
                        strTemp = doAddUserAction();
//                        System.err.println("-----------in create user------------" + rd.getParameter("newuser"));
                        if(rd.getParameter("newuser") != null)
                        {
                             if(rd.getParameter("newuser").equals("cloneuser"))
                            {
//                                System.err.println("---------user to be cloned---in cloneuser-----" + rd.getParameter("user"));
    //                            System.err.println("---------groupkey--------" + rd.getParameter("UsergroupKey"));
//                                strXML.append("<clone>"+ rd.getParameter("user") +"</clone>");
                            }

                                strXML.append( strTemp );
    //                            System.err.println("--------strXML--------" + strXML);
                                this.strStylesheet = ADD_USER_ACTION;
                        }
                        else
                        {
//                            System.err.println("---------no option was selected-------" + strTemp);
                            strXML.append( strTemp );
                            this.strStylesheet = CHOOSE_USER_ACTION;
                        }
                    }
                    
                    else if(rd.getParameter(ADD_USER_ACTION)!=null)
                    {
                        String strTemp;
                        strTemp = doAddUserAction();
                        
                        if(success)
                        {
//                            System.err.println("----------in success----------");
//                            System.err.println("---------user to be cloned--------" + rd.getParameter("USERID_strUserName"));
//                            System.err.println("---------------username----------" + rd.getParameter("USERDETAILS_strUserName"));
//                            System.err.println("groupkey in add user action-------------" + rd.getParameter("UsergroupKey"));
//                            strXML.append("<clone>"+ rd.getParameter("USERID_strUserName") +"</clone>");
                            if(runtimeData.getParameter("USERID_strUserName") != null && runtimeData.getParameter("USERID_strUserName").equals("null"))
                            {
//                                System.err.println("------------UserID null----------409-----");
                                runtimeData.remove("USERID_strUserName");
                            }
                            if(runtimeData.getParameter("USERID_strUserName") != null)
                            {
//                                System.err.println("---------user to be cloned----before getUserDetails----" + rd.getParameter("USERID_strUserName"));                                
                                getUserDetails();
                                runtimeData.setParameter("USERID_strUserName",rd.getParameter("USERDETAILS_strUserName"));
/*                                Date today = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");                                
                                System.err.println("Today's date-------------" + sdf.format(today));
                                runtimeData.setParameter("USERID_strAbsTimestamp",sdf.format(today));*/
                                cloneUser();
                            }
                            
                            strXML.append( doSetPasswordAction() );
                        
                            this.strStylesheet = SET_PASSWORD_ACTION;
                            
                        }
                        else
                        {
                        
/*                            strXML.append( strTemp );
                        
                            this.strStylesheet = ADD_USER_ACTION;*/
                             if(runtimeData.getParameter("UsergroupKey") == null & runtimeData.getParameter("USERGROUP_intUsergroupKey") == null)
                            {
//                                System.err.println("------------UsergroupKey==null-----------");
                                 strXML.append( strTemp );
        //                            System.err.println("--------strXML--------" + strXML);
                                    this.strStylesheet = ADD_USER_ACTION;
                            }
                             else if(exists)
                             {
//                                System.err.println("-----------in the else if exists------------" + strTemp);
                                strXML.append( strTemp );
                               //this.strStylesheet = CHOOSE_USER_ACTION;
                                this.strStylesheet = ADD_USER_ACTION;
                             
                             
                             }
                            else
                            {
//                            System.err.println("-----------in the else------------" + strTemp);
                                strXML.append( strTemp );
                               this.strStylesheet = CHOOSE_USER_ACTION;
                                //this.strStylesheet = ADD_USER_ACTION;
                            }
                        }
                        
                        success=false;
                        // delete a group
                    }else if(rd.getParameter(DELETE_GROUP_ACTION)!=null && rd.getParameter(DELETE_GROUP_ACTION).equals("true")){
                        
                        strXML.append( doDeleteGroupAction() );

                        this.strStylesheet = DELETE_GROUP_ACTION;
                        
                        // save user - user group details
                    }else if(rd.getParameter(SAVE_USER_GROUP_ACTION)!=null){
                        
                        doSaveUserGroupAction();
                        
                        strXML.append( doGroupClickAction() );
                        
                        this.strStylesheet = GROUP_ACTION;
                        
                    }else if(rd.getParameter(SET_PERMISSION_ACTION)!=null){
                        
                        strXML.append(doSetPermissionAction());
                        
                        this.strStylesheet = SET_PERMISSION_ACTION;
                        
                        // no actions, display group details
                    }else{
                        
                        strXML.append( doGroupClickAction() );
                        
                        this.strStylesheet = GROUP_ACTION;
                        
                    }
                    // invalid action perform a error message
                }else{
                    
                    
                    
                }
                
                // save current information for future use
                strLastXML = new StringBuffer(strXML.toString());
                strLastStylesheet = strStylesheet;
                
                // enter channel for the firt time or just do a tree action
            }
            else if(runtimeData.getParameter("cancel") != null)
            {
                if(runtimeData.getParameter("UsergroupKey")!= null)
                {
//                    System.err.println("UserGroupKey != null----------in cancel515");
                        String strTemp;
                        strTemp = doAddUserAction();
                        strXML.append( strTemp );
//                        System.err.println("---------strTemp--------" + strTemp);
                        this.strStylesheet = CHOOSE_USER_ACTION;
                }
/*                if(runtimeData.getParameter("clone")!= null)
                {
                    System.err.println("UserGroupKey != null----------in cancel515");
                        String strTemp;
                        strTemp = doAddUserAction();
                        strXML.append( strTemp );
                        this.strStylesheet = CHOOSE_USER_ACTION;
                }*/
                else if(strLastXML==null)
                {
                    
                    // display a so-called DEFAULT view: ADD USER
                    strStylesheet = ADD_USER_ACTION;
                    strXML.append(doAddUserAction());
                    
                    // doing a tree action
                }else{
                    
                    // get the last page info and display
                    strXML = new StringBuffer(strLastXML.toString());
                    
                    strStylesheet = strLastStylesheet;
                }
            }
            else
            {
                
                // first time entry
                if(strLastXML==null){
                    
                    // display a so-called DEFAULT view: ADD USER
                    strStylesheet = ADD_USER_ACTION;
                    strXML.append(doAddUserAction());
                    
                    // doing a tree action
                }else{
                    
                    // get the last page info and display
                    strXML = new StringBuffer(strLastXML.toString());
                    
                    strStylesheet = strLastStylesheet;
                }
                
                
            } // nothing else
            
            // display tree, always
            DefaultMutableTreeNode myTree = buildUsergroupTree(hsExpanded);
            strXML.append(toXML(myTree, hsExpanded));
            
            lockedUsers = null;
            lockedGroups = new Hashtable();
            
            lockedUsers = null;
            lockedGroups = new Hashtable();
            
            lockTree(myTree,  hsExpanded);
            lock.lockDelayWrite();
            
            
            strXML.append("</body>");
            
            // end of XML
         
          }
        }
        
        catch (Exception e)
        
        {
            
            e.printStackTrace();
            
            LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
            
        }
        
    }
    //rennypv
    
     private void getUserDetails()
   {
       try
       {
           cloneuser = runtimeData.getParameter("USERID_strUserName");
            Vector formFields = DatabaseSchema.getFormFields("cuseradmin_view_user");
            DALQuery query = new DALQuery();
            query.setDomain("USERID",null,null,null);
            query.setFields(formFields,null);
            query.setWhere(null,0,"USERID_strUserName","=",  cloneuser ,0,DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();

            if(rs.next())
            {
                for (int i = 0; i < formFields.size(); i++)                
                {
                    String columnname = (String) formFields.get(i);
                    String value = rs.getString(columnname);

                    runtimeData.setParameter(columnname,value);
                }
            }
            rs.close();
            rs = null;
            
            query.clearFields();
            query.clearWhere();
            query.setMaxField("USERID_intUserID");
            ResultSet rsmax = query.executeSelect();
            if(rsmax.next())
            {
                String maxval = rsmax.getString("MAX_UID_intUserID");
                int i = new Integer(maxval).intValue();
//                System.err.println("[getUserDetails] ---------max value is----------" + Integer.toString(i+1));
                cloneuserID = runtimeData.getParameter("USERID_intUserID");
                runtimeData.setParameter("USERID_intUserID",Integer.toString(i+1));
            }
            rsmax.close();
            rsmax = null;
       }
       catch(Exception e)
       {
            e.printStackTrace();
       }
   }
   private void cloneUser()
   {
       try
       {
        Vector formFields = DatabaseSchema.getFormFields("cuseradmin_add_user_id");
        DALSecurityQuery sec_query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
        sec_query.setDomain("USERID",null,null,null);
        sec_query.setFields(formFields,runtimeData);
        sec_query.executeInsert();
 
        DALQuery query = new DALQuery();
        query.setDomain("USERID",null,null,null);
        query.setField("USERID_intUserID",null);
        query.setWhere(null,0,"USERID_strUserName","=",  runtimeData.getParameter("USERID_strUserName") ,0,DALQuery.WHERE_HAS_VALUE);
        
        ResultSet rsuid = query.executeSelect();
        String uid = null;
        if(rsuid.next())
        {
            uid = rsuid.getString("USERID_intUserID");
//            System.err.println("[cloneUser]-------cloneid--& userid---" + cloneuserID + " " + uid);            
        }

        // Close the resultset
        rsuid.close();
        rsuid = null;
       
        //        System.err.println("--------user and cloneuser----------in cloneuser-------" + runtimeData.getParameter("USERID_strUserName") + " " + cloneuser);
        
        insertclone("USERPROFILE","cuseradmin_view_user_profile",cloneuserID,uid,"cuseradmin_add_user_profile");
        
        insertclone("USERSKIN","cuseradmin_view_user_skin",cloneuserID,uid,"cuseradmin_add_user_skin");
        
        insertclone("USERLAYOUT","cuseradmin_view_user_layout",cloneuserID,uid,"cuseradmin_add_user_layout");
        
        insertclone("LAYOUTSTRUCT","cuseradmin_view_user_layout_struct",cloneuserID,uid,"cuseradmin_add_user_layout_struct");
        
        insertclone("LAYOUTPARAM","cuseradmin_view_layout_param",cloneuserID,uid,"cuseradmin_view_layout_param");
        
//       Vector formFields = DatabaseSchema.getFormFields("cuseradmin_view_group_membership");
        query.clearDomains();
        query.clearFields();
        query.clearWhere();
        query.setDomain("GROUPMEMBERSHIP",null,null,null);
        Vector vtUserGroupMembership = DatabaseSchema.getFormFields("cuseradmin_view_group_membership");
        query.setFields(vtUserGroupMembership,null);
        query.setWhere(null,0,"GROUPMEMBERSHIP_strMemberKey","=",  cloneuser ,0,DALQuery.WHERE_HAS_VALUE);
        ResultSet rsmember = query.executeSelect();

        while(rsmember.next())
        {
            for(int i=0; i < vtUserGroupMembership.size(); i++)
            {
                String columnname = (String) vtUserGroupMembership.get(i);
                String value = rsmember.getString(columnname);

                if(value != null)
                {
                    runtimeData.setParameter(columnname,value);
                }
                else
                {
                    runtimeData.setParameter(columnname,"");
                }
             }
             runtimeData.setParameter("GROUPMEMBERSHIP_strMemberKey",runtimeData.getParameter("USERID_strUserName"));
             sec_query.clearDomains();
             sec_query.clearFields();
             sec_query.clearWhere();
             sec_query.setDomain("GROUPMEMBERSHIP",null,null,null);
             sec_query.setFields(DatabaseSchema.getFormFields("cuseradmin_add_group_membership"),runtimeData);             
             sec_query.executeInsert();
        }

        // Close the resultset
        rsmember.close();
        rsmember = null;
        
        query.clearWhere();
        query.clearFields();
        query.setFields(vtUserGroupMembership,null);
        query.setWhere(null,0,"GROUPMEMBERSHIP_strMemberKey","=",  cloneuserID ,0,DALQuery.WHERE_HAS_VALUE);
        query.setWhere("AND",0,"GROUPMEMBERSHIP_strMemberIsGroup","=",  "F" ,0,DALQuery.WHERE_HAS_VALUE);
        ResultSet rsmemberkey = query.executeSelect();
        
        while(rsmemberkey.next())
        {
            for(int i = 0; i < vtUserGroupMembership.size(); i++)
            {
                String columnname = (String) vtUserGroupMembership.get(i);
                String value = rsmemberkey.getString(columnname);

                if(value != null)
                {
                    runtimeData.setParameter(columnname,value);
                }
                else
                {
                    runtimeData.setParameter(columnname,"");
                }
             }
             runtimeData.setParameter("GROUPMEMBERSHIP_strMemberKey",uid);
             sec_query.clearDomains();
             sec_query.clearFields();
             sec_query.clearWhere();
             sec_query.setDomain("GROUPMEMBERSHIP",null,null,null);
             sec_query.setFields(DatabaseSchema.getFormFields("cuseradmin_add_group_membership"),runtimeData);             
             sec_query.executeInsert();
        }
        
        // Close the resultset
        rsmemberkey.close();
        rsmemberkey = null;        
        
        query.clearDomains();
        query.clearFields();
        query.clearWhere();
        query.setDomain("USERUSERGROUP",null,null,null);
        Vector vtViewUserGroup = DatabaseSchema.getFormFields("cuseradmin_view_user_usergroup");
        query.setFields(vtViewUserGroup,null);
        query.setWhere(null,0,"USERUSERGROUP_strUserName","=",  cloneuser ,0,DALQuery.WHERE_HAS_VALUE);
        query.setWhere("AND",0,"USERUSERGROUP_intDeleted","=",  "0" ,0,DALQuery.WHERE_HAS_VALUE);
        ResultSet rsgroup = query.executeSelect();
        
        while(rsgroup.next())
        {
            for(int i = 0; i < vtViewUserGroup.size(); i++)
            {
                String columnname = (String) vtViewUserGroup.get(i);
                String value = rsgroup.getString(columnname);

                if(value != null)
                {
                    runtimeData.setParameter(columnname,value);
                }
                else
                {
                    runtimeData.setParameter(columnname,"");
                }
             }
             runtimeData.setParameter("USERUSERGROUP_strUserName",runtimeData.getParameter("USERID_strUserName"));
             sec_query.clearDomains();
             sec_query.clearFields();
             sec_query.clearWhere();
             sec_query.setDomain("USERUSERGROUP",null,null,null);
             sec_query.setFields(DatabaseSchema.getFormFields("cuseradmin_insert_user"),runtimeData);             
             sec_query.executeInsert();
        }
        rsgroup.close();
        // Close the resultset
        rsgroup = null;                
        
       }
       catch(Exception e)
       {
            e.printStackTrace();
       }
   }
   private void insertclone(String domain,String formfields,String cloneid,String uid,String toformfields)
   {
        try
        {
            DALQuery query = new DALQuery();
            DALSecurityQuery query1 = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
            query.clearDomains();
            query.clearFields();
            query.clearWhere();
            query.setDomain(domain, null, null, null);
            query1.clearDomains();
            query1.clearFields();
            query1.clearWhere();
            query1.setDomain(domain, null, null, null);
            Vector vtFormfields = DatabaseSchema.getFormFields(formfields);
            query.setFields(vtFormfields, null);
            query.setWhere(null, 0, domain +"_intUserID", "=",cloneid,0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsprofile = query.executeSelect();
//            ResultSetMetaData rsmd = rsprofile.getMetaData(); 
            while(rsprofile.next())
            {
                 for(int i=0; i<vtFormfields.size(); i++)
                {
                    String columnname = (String) vtFormfields.get(i);
                    String value = rsprofile.getString(columnname);
//                    System.err.println("name and value------------" + columnname + ":" + value);
                    if(value != null)
                    {
                        runtimeData.setParameter(columnname,value);
                    }
                    else
                    {
                        runtimeData.setParameter(columnname,"");
                    }
                 }
                 runtimeData.setParameter(domain +"_intUserID",uid);
                 query1.clearFields();
                 query1.clearWhere();
                 query1.setFields(DatabaseSchema.getFormFields(toformfields),runtimeData);             
                 query1.executeInsert();
            }
            rsprofile.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
   }
    
    
    //End rennypv
    /**
     *    Method to save group details
     */
    private void doGroupSaveAction(){
        
        // get the group key
        String groupId=runtimeData.getParameter("USERGROUP_intUsergroupKey");
        
        
        
        // continue processing if a valid group is enter
        if(groupId != null){
            
            try{
                // error string, if caught
                String strErr;
                
                // setup all query basis
                Vector formFields = DatabaseSchema.getFormFields("cuseradmin_update_admin_usergroup");
                DALSecurityQuery query = new DALSecurityQuery();
                
                query.setDomain("USERGROUP",null,null,null);
                query.setFields(formFields,runtimeData);
                
                
                // validating data
                if((strErr=QueryChannel.validateFields(formFields,runtimeData))==null){
                    
                    // validating data succeeded
                    query.setWhere(null,0,"USERGROUP_intUsergroupKey","=",  groupId ,0,DALQuery.WHERE_HAS_VALUE);
                    //lock.lockDelayWrite();
                    
                    if( lock != null ) lock.unlock();
                    lock = null;
                    lock = new LockRequest( authToken );
                    lockUsergroup( groupId, false, LockRecord.READ_WRITE );
                    lock.lockDelayWrite();
                    
                    if(lock.isValid() && lock.lockWrites()){
                        
                        if(query.executeUpdate()){
                        
                            runtimeData.setParameter("error", "Details updated.");
                        
                        }else{
                            // catch an unexpected database error
                                runtimeData.setParameter("error", "An unknown error has occured. Please try again later.");
                        
                        }
                        
                        lock.unlockWrites();
                        
                    
                    }else{
                        runtimeData.setParameter("error",LOCK_ERROR);
                    }
                    
                    
                }else{
                    
                    runtimeData.setParameter("error", strErr);
                }
                
                
            }
            
            catch (Exception e)
            
            {
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                
            }
            
        }
    }
    
    /**
     *    Method to set group permissions
     */
    
    private String doSetPermissionAction(){
        
        StringBuffer strXML = new StringBuffer();
         // error string, if caught
        String strErr = new String("");
            
        // get the group key
        String groupId=runtimeData.getParameter("USERGROUP_intUsergroupKey");
        
        strXML.append("<proccess><permissionDetails>");
        
        // continue processing if a valid group is enter
        if(groupId != null){
            
            Vector formFields ;
            
            DALQuery query = new DALQuery();
           
            ResultSet rs;
            Vector oneFieldA = new Vector();
            Vector oneField = new Vector();
            
            try{
                
                DALSecurityQuery sec_query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                
                if(runtimeData.getParameter("GROUP_intGroupKey")!=null){
                
                    // check if user save the permissions
                    if((runtimeData.getParameter(SAVE_PERMISSION_ACTION)!=null)&&(!runtimeData.getParameter("GROUP_intGroupKey").equals(""))){
                    
                        sec_query.reset();
                    
                        sec_query.setDomain("PERMISSION",null,null,null);
                        formFields = DatabaseSchema.getFormFields("cuseradmin_view_permission");
                    
                        sec_query.setField("PERMISSION_intDeleted","1");
                    
                        sec_query.setWhere(null,0,"PERMISSION_intGroupKey","=",
                        runtimeData.getParameter("GROUP_intGroupKey"), 0,DALQuery.WHERE_HAS_VALUE);
                    
                        sec_query.setWhere("AND",0,"PERMISSION_intUsergroupKey","=",
                        groupId, 0,DALQuery.WHERE_HAS_VALUE);
                    
                        //if( lock!= null ) lock.unlock();
                        //lock = null;
                        //lock = new LockRequest( authToken );
                        //lockUsergroup( groupId, false, LockRecord.READ_WRITE );
                        //lock.lockDelayWrite();
                        
                        //if(lock.isValid() && lock.lockWrites()){
                            
                            sec_query.executeUpdate();
                        //    lock.unlockWrites();
                            
                        //}else{
                            
                           // strErr = LOCK_ERROR;
                          //  
                        //}
                        String[] groupActivities = runtimeData.getParameterValues("groupActivities");
                        String[] groupDenyActivities = runtimeData.getParameterValues("groupDenyActivities");
                    
                    
                        
                        if((groupActivities!=null)&&(!strErr.equals(LOCK_ERROR))){
                            int i;
                            
                        
                            
                            for( i = 0; i < groupActivities.length; i++){
                                
                                DALSecurityQuery testQuery = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                                testQuery.setDomain("PERMISSION",null,null,null);
                                testQuery.setField("PERMISSION_intDeleted","0");
                                
                                testQuery.setWhere(null,0,"PERMISSION_intGroupKey","=",
                                    runtimeData.getParameter("GROUP_intGroupKey"), 0,DALQuery.WHERE_HAS_VALUE);
                    
                                testQuery.setWhere("AND",0,"PERMISSION_intUsergroupKey","=",
                                    groupId, 0,DALQuery.WHERE_HAS_VALUE);
                            
                                testQuery.setWhere("AND",0,"PERMISSION_intActivityKey","=",
                                groupActivities[i], 0,DALQuery.WHERE_HAS_VALUE);
                                
                                ResultSet rsQuery = testQuery.executeSelect();
                                
                                if(rsQuery.next()){
                                    
                                       rsQuery.close();
                                       testQuery.executeUpdate();
                                       
                                                                          
                                }else{
                                    
                                    
                                    rsQuery.close();  
                                    runtimeData.setParameter("PERMISSION_intActivityKey",groupActivities[i]);
                                    runtimeData.setParameter("PERMISSION_intUsergroupKey",groupId);
                                    runtimeData.setParameter("PERMISSION_intGroupKey",runtimeData.getParameter("GROUP_intGroupKey"));
                                    formFields = DatabaseSchema.getFormFields("cuseradmin_insert_permission");
                                    
                                    testQuery.clearFields();
                                    testQuery.setFields(formFields, runtimeData);
                                
                                    testQuery.executeInsert();
                                  
                                
                                }
                                                                
                                
                             }
                            
                            if( groupDenyActivities != null )
                                for( i = 0; i < groupDenyActivities.length; i++){

                                    DALSecurityQuery testQuery = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                                    testQuery.setDomain("PERMISSION",null,null,null);
                                    testQuery.setField("PERMISSION_intDeleted","0");

                                    testQuery.setWhere(null,0,"PERMISSION_intGroupKey","=",
                                        runtimeData.getParameter("GROUP_intGroupKey"), 0,DALQuery.WHERE_HAS_VALUE);

                                    testQuery.setWhere("AND",0,"PERMISSION_intUsergroupKey","=",
                                        groupId, 0,DALQuery.WHERE_HAS_VALUE);

                                    testQuery.setWhere("AND",0,"PERMISSION_intActivityKey","=",
                                    groupDenyActivities[i], 0,DALQuery.WHERE_HAS_VALUE);

                                    ResultSet rsTestQuery = testQuery.executeSelect();
                                    if(rsTestQuery.next()){

                                            rsTestQuery.close();
                                            testQuery.executeUpdate();
                                            

                                    }else{


                                        rsTestQuery.close();
                                        runtimeData.setParameter("PERMISSION_intActivityKey",groupDenyActivities[i]);
                                        runtimeData.setParameter("PERMISSION_intUsergroupKey",groupId);
                                        runtimeData.setParameter("PERMISSION_intGroupKey",runtimeData.getParameter("GROUP_intGroupKey"));
                                        formFields = DatabaseSchema.getFormFields("cuseradmin_insert_permission");

                                        testQuery.clearFields();
                                        testQuery.setFields(formFields, runtimeData);

                                        testQuery.executeInsert();


                                    }


                                 }

                             strErr = "Details updated";
                        
                        }
                    
                    
                    
                    }
                
                 
                   
                
                    // check if user change group to view permission
                    if (!runtimeData.getParameter("GROUP_intGroupKey").equals("")){
                        
                        
                        // get the current group
                        strXML.append("<currentGroup>" + runtimeData.getParameter("GROUP_intGroupKey") + "</currentGroup>");
                    
                                        
                    
                        formFields = DatabaseSchema.getFormFields("cuseradmin_view_admin_usergroup");
                        query.reset();
                    
                        // get the names of actitvities this group has
                        oneField = new Vector();
                        oneField.add("ACTIVITY_strActivityName");
                        oneField.add("ACTIVITY_intActivityKey");
                        oneField.add("ACTIVITY_strActivityDesc");                                        
                    
                        query.setDomain("PERMISSION",null,null,null);
                        query.setDomain("ACTIVITY","PERMISSION_intActivityKey","ACTIVITY_intActivityKey",
                            "INNER JOIN");
                        query.setFields(oneField,null);
                    
                        query.setWhere(null,0,"ACTIVITY_intDeleted","=","0",
                            0,DALQuery.WHERE_HAS_VALUE);
                    
                        query.setWhere("AND",0,"PERMISSION_intDeleted","=","0",
                            0,DALQuery.WHERE_HAS_VALUE);
                    
                        query.setWhere("AND",0,"PERMISSION_intUsergroupKey","=",groupId,
                            0,DALQuery.WHERE_HAS_VALUE);
                    
                        query.setWhere("AND",0,"PERMISSION_intGroupKey","=",
                            runtimeData.getParameter("GROUP_intGroupKey"), 0,DALQuery.WHERE_HAS_VALUE);
                        // Field Security implement
                        //  query.setWhere( "AND",0,"ACTIVITY_strActivityDesc","<>",
                        query.setWhere( "AND",0,"ACTIVITY_strActivityDesc","NOT LIKE",
                            FIELD_SECURITY, 0,DALQuery.WHERE_HAS_VALUE);
                    
                        
                        //query.setOrderBy("ACTIVITY_strActivityDesc",  "ASC");
                        
                        rs = query.executeSelect();
                    
                        Vector vtactivities = new Vector();
                       
                        while(rs.next())
                        {
                             strXML.append("<groupActivities>");
                                strXML.append("<ACTIVITY_strActivityName>");
                                String strTempData = rs.getString("ACTIVITY_strActivityName");
                                vtactivities.addElement(strTempData);
                                strXML.append(Utilities.cleanForXSL(strTempData));
                                strXML.append("</ACTIVITY_strActivityName>");
                                strXML.append("<ACTIVITY_intActivityKey>");
                                strTempData = rs.getString("ACTIVITY_intActivityKey");
                                strXML.append(Utilities.cleanForXSL(strTempData));
                                strXML.append("</ACTIVITY_intActivityKey>");
                                strXML.append("<ACTIVITY_strActivityDesc>");
                                strXML.append(Utilities.cleanForXSL(rs.getString("ACTIVITY_strActivityDesc")));
                                strXML.append("</ACTIVITY_strActivityDesc>");
                             strXML.append("</groupActivities>");
                        }
                        
                        //strXML.append(QueryChannel.buildSearchXMLFile("groupActivities", rs, oneField));
                        
                        // get the names of activities Not in this group
                        query.clearFields();
                        query.setField("ACTIVITY_strActivityName",null);

                        DALQuery aQuery = new DALQuery();
                        aQuery.setDomain("ACTIVITY",null,null,null);
                        aQuery.setFields(oneField,null);
                        aQuery.setWhere(null,0,"ACTIVITY_strActivityName","NOT IN",query,0,DALQuery.WHERE_HAS_SUB_QUERY);
//                        aQuery.setWhere( "AND",0,"ACTIVITY_strActivityDesc","<>",
                        aQuery.setWhere( "AND",0,"ACTIVITY_strActivityDesc","NOT LIKE",                        
                            FIELD_SECURITY, 0,DALQuery.WHERE_HAS_VALUE);
                    
                        aQuery.setOrderBy("ACTIVITY_strActivityDesc", "ASC");
                    
                        if (rs != null)
                        {
                            rs.close();
                        }
                        rs = null;
			System.err.println(aQuery.convertSelectQueryToString()); 
                        rs = aQuery.executeSelect();
                    
                        // display them
                        strXML.append(QueryChannel.buildSearchXMLFile("allActivities", rs,oneField));
                        
                        /********************************* FIEL SECURITY IMPLEMENT ************************/
                        // Fiel security implement
                                                // get the names of actitvities this group has
                        oneField = new Vector();
                        oneField.add("ACTIVITY_strActivityName");
                        oneField.add("ACTIVITY_intActivityKey");
                        oneField.add("ACTIVITY_strActivityDesc");                        
                        query.setFields( oneField, null );
                        
                        query.clearWhere();
                        query.setWhere(null,0,"ACTIVITY_intDeleted","=","0",
                            0,DALQuery.WHERE_HAS_VALUE);
                    
                        query.setWhere("AND",0,"PERMISSION_intDeleted","=","0",
                            0,DALQuery.WHERE_HAS_VALUE);
                    
                        query.setWhere("AND",0,"PERMISSION_intUsergroupKey","=",groupId,
                            0,DALQuery.WHERE_HAS_VALUE);
                    
                        query.setWhere("AND",0,"PERMISSION_intGroupKey","=",
                            runtimeData.getParameter("GROUP_intGroupKey"), 0,DALQuery.WHERE_HAS_VALUE);
                        
//                        query.setWhere( "AND",0,"ACTIVITY_strActivityDesc","=",
                            query.setWhere( "AND",0,"ACTIVITY_strActivityDesc","LIKE",
                            FIELD_SECURITY, 0,DALQuery.WHERE_HAS_VALUE);
                           //query.setOrderBy("ACTIVITY_strActivityDesc", "ASC");
                        
                        if (rs != null)
                        {
                            rs.close();
                        }
                            
                        rs = null;
                        rs = query.executeSelect();
                        
                            while(rs.next())
                            {

                                    String strTempData = rs.getString("ACTIVITY_strActivityName");
                            		
                                     if(checkForAddPermissions(strTempData,vtactivities))
                                    {
                                    	
                                        strXML.append("<groupDenyActivities>");
                                        strXML.append("<ACTIVITY_strActivityName>");
                                        strXML.append(Utilities.cleanForXSL(strTempData));
                                        strXML.append("</ACTIVITY_strActivityName>");
                                        strXML.append("<ACTIVITY_intActivityKey>");
                                        strTempData = rs.getString("ACTIVITY_intActivityKey");
                                        strXML.append(Utilities.cleanForXSL(strTempData));
                                        strXML.append("</ACTIVITY_intActivityKey>");
                                        strXML.append("<ACTIVITY_strActivityDesc>");
                                        strXML.append(cleanForDisplay(Utilities.cleanForXSL(rs.getString("ACTIVITY_strActivityDesc")),"- "));
                                        strXML.append("</ACTIVITY_strActivityDesc>");
                                         strXML.append("</groupDenyActivities>");
                                     }
                            }
                        
//                        strXML.append(QueryChannel.buildSearchXMLFile("groupDenyActivities", rs, oneField));
                        
                        query.clearFields();
                        query.setField("ACTIVITY_strActivityName",null);
                  
                        aQuery.clearWhere();
                        aQuery.setWhere(null,0,"ACTIVITY_strActivityName","NOT IN",query,0,DALQuery.WHERE_HAS_SUB_QUERY);
//                        aQuery.setWhere( "AND",0,"ACTIVITY_strActivityDesc","=",
                         aQuery.setWhere( "AND",0,"ACTIVITY_strActivityDesc","LIKE",
                            FIELD_SECURITY, 0,DALQuery.WHERE_HAS_VALUE);
                        aQuery.setOrderBy("ACTIVITY_strActivityDesc", "ASC");
                        if (rs != null)
                        {
                            rs.close();
                        }
                        rs = null;
                        rs = aQuery.executeSelect();
                        
                        
                        while(rs.next())
                        {
                                String strTempData = rs.getString("ACTIVITY_strActivityName");
                               
                                if(checkForAddPermissions(strTempData,vtactivities))
                                {
                                
                                    strXML.append("<allDenyActivities>");
                                    strXML.append("<ACTIVITY_strActivityName>");
                                    strXML.append(Utilities.cleanForXSL(strTempData));
                                    strXML.append("</ACTIVITY_strActivityName>");
                                    strXML.append("<ACTIVITY_intActivityKey>");
                                    strTempData = rs.getString("ACTIVITY_intActivityKey");
                                    strXML.append(Utilities.cleanForXSL(strTempData));
                                    strXML.append("</ACTIVITY_intActivityKey>");
                                    strXML.append("<ACTIVITY_strActivityDesc>");
                                    strXML.append(cleanForDisplay(Utilities.cleanForXSL(rs.getString("ACTIVITY_strActivityDesc")),"- "));
                                    strXML.append("</ACTIVITY_strActivityDesc>");
                                     strXML.append("</allDenyActivities>");
                                }
                        }
                        // display them
                        //strXML.append(QueryChannel.buildSearchXMLFile("allDenyActivities", rs,oneField));
                        
                    
                    }
                }    
                
                
                 if (lock != null)
                    if (lock.isValid()){
                        
                        lock.unlock();
                        lock = null;
                        
                    }
                currentGroup = groupId;
                
                
                // get user group name
                query.reset();
                query.setDomain("USERGROUP",null,null,null);
                query.setField("USERGROUP_strUsergroupName",null);
                query.setWhere(null,0,"USERGROUP_intUsergroupKey", "=", groupId, 0, DALQuery.WHERE_HAS_VALUE);
                
                
                rs = query.executeSelect();
                
                String groupName = "";
                
                if(rs.next()){
                    groupName = (rs.getString("USERGROUP_strUsergroupName"));
                }
                
                oneFieldA =  new Vector();
                oneFieldA.add("USERGROUP_strUsergroupName");
                strXML.append("<USERGROUP_strUsergroupName>" + groupName + "</USERGROUP_strUsergroupName>");
                strXML.append("<USERGROUP_intUsergroupKey>" + groupId + "</USERGROUP_intUsergroupKey>");
                strXML.append(QueryChannel.buildFormLabelXMLFile(oneFieldA ));
                
                
                
                // add group name
                query.reset();
                query.setDomain("GROUP",null,null,null);
                    
                formFields = DatabaseSchema.getFormFields("cuseradmin_view_group");
                    
                query.setFields(formFields,null);
                rs.close();
                rs = null;
                rs = query.executeSelect();
                
                
                  
                strXML.append(QueryChannel.buildSearchXMLFile("groups",rs,formFields));
                strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                    
                              
                
            }
            
            catch (Exception e)
            
            {
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                
            }
            
        }
        
        
        strXML.append("</permissionDetails>");
        
       
            strXML.append("<error>" + strErr + "</error>");
        
        strXML.append("</proccess>");
        
        return strXML.toString();
    }
    
    public String cleanForDisplay(String value,String dirtystring)
    {
        int i = value.indexOf(dirtystring);
        String cleanstring = value.substring(i+1);
        return cleanstring;
    }
    public boolean checkForAddPermissions(String fieldname,Vector vtactivities)
    {
       boolean blpermit = false;
       String[] domain = fieldname.split("_");
      String activitynameadd = domain[0].toLowerCase() + "_add";
       String activitynameview = domain[0].toLowerCase() + "_view";
       if(!vtactivities.contains(activitynameadd) && vtactivities.contains(activitynameview))
       {
            blpermit = true;
       }
       return blpermit;
    }
    
    /**
     *    Method to display user details
     * @return XML string contains all user details and errors, if any
     */
    
    private String doGroupClickAction(){
        
        StringBuffer strXML = new StringBuffer();
        
        strXML.append("<proccess>");
        
        // get usergroup key
        if(runtimeData.getParameter("USERGROUP_intUsergroupKey")!=null){
            
            String groupId= runtimeData.getParameter("USERGROUP_intUsergroupKey");
            
            
            // try to catch all the exceptions
            try{
                
                currentGroup = groupId;
                // display error or message from other processes
                if(runtimeData.getParameter("error")!=null){
                    
                    strXML.append("<error>" + runtimeData.getParameter("error") + "</error>");
                    
                }
                
                // setup all query basis
                Vector formFields = DatabaseSchema.getFormFields("cuseradmin_view_admin_usergroup");
                DALQuery query = new DALQuery();
                
                // gather group details
                query.setDomain("USERGROUP",null,null,null);
                query.setFields(formFields,null);
                
                query.setWhere(null,0,"USERGROUP_intUsergroupKey","=",groupId ,0,DALQuery.WHERE_HAS_VALUE);
                
                if (lock != null)
                    if (lock.isValid()){
                        
                        lock.unlock();
                        lock = null;
                        
                    }
                                    
                
                ResultSet rs = query.executeSelect();
                
                // display group details
                strXML.append("<groupDetails>");
                
                strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                
                strXML.append(QueryChannel.buildSearchXMLFile("details", rs, formFields));
                
                strXML.append("</groupDetails>");
                
                // get users in the group
                rs.close();
                query.reset();
                query.setDomain("USERUSERGROUP",null,null,null);
                
                formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_usergroup");
                query.setFields(formFields, null);
                query.setWhere(null,0, "USERUSERGROUP_intUsergroupKey", "=", groupId,0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"USERUSERGROUP_intDeleted","=", "0" ,0,DALQuery.WHERE_HAS_VALUE);
//Seena                query.setOrderBy("USERUSERGROUP_strUserName","ASC");
                
                
                rs = null;
                rs = query.executeSelect();
                
                // display them
                strXML.append("<groupUsers>");
                
                while(rs.next()){
                    
                    strXML.append("<user><name>").append(rs.getString("USERUSERGROUP_strUserName")).append("</name></user>");
                    
                }
                
                strXML.append("</groupUsers>");
                
                
                // get all other users, which are not in group
                // this is the sub query
                rs.close();
                DALQuery query1 = new DALQuery();
                query1.setDomain("USERDETAILS",null,null,null);
                
                formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_details");
                query1.setFields(formFields, null);
//Seena                query1.setOrderBy("USERDETAILS_strUserName","ASC");
                
                
                // this is the actual query
                query.clearFields();
                query.setField("USERUSERGROUP_strUserName",null);
                query1.setWhere(null, 0, "USERDETAILS_strUserName", "NOT IN", query, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                
                rs = null;
                rs = query1.executeSelect();
                
                // display them
                strXML.append("<allUsers>");
                
                while(rs.next()){
                    
                    strXML.append("<user><name>").append(rs.getString("USERDETAILS_strUserName")).append("</name></user>");
                    
                }
                
                strXML.append("</allUsers>");
                rs.close();
                
            }
            
            catch (Exception e)
            
            {
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                
            }
            
        }
        strXML.append("</proccess>");
        return strXML.toString();
    }
    
    /**
     *    Method to manage users in group
     */
    
     private void doSaveUserGroupAction(){
        
        // get all the posted data
        String[] groupUsers = runtimeData.getParameterValues("groupUsers");
        String[] allUsers = runtimeData.getParameterValues("allUsers");
        int i;
        
        // try to catch al the exceptions
        try{
            
            // setup all the query basis
            DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
            DALSecurityQuery query1 = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
            Vector formFields;
            
            // first, "remove" all users in group
            query.setDomain("USERUSERGROUP",null,null,null);
            query.setField("USERUSERGROUP_intDeleted","1");
            query.setWhere(null, 0, "USERUSERGROUP_intUsergroupKey", "=", runtimeData.getParameter("USERGROUP_intUsergroupKey"), 0,
            DALQuery.WHERE_HAS_VALUE);
            
            boolean go = true;
            
            //if(lock.isValid() && lock.lockWrites()){
                query.executeUpdate();
            //    lock.unlockWrites();
            //}else{
            //    runtimeData.setParameter("error",LOCK_ERROR);
            //    go=false;
             //   return ; 
            //}
            
            
            
            if((runtimeData.getParameterValues("groupUsers")!=null)&&go){
                
                // then, add each user into group
                for( i = 0; i < groupUsers.length; i++){
                    
                    
                    query.reset();
                    query.setDomain("USERUSERGROUP",null,null,null);
                    query.setField("USERUSERGROUP_intDeleted","0");
                    query.setWhere(null, 0, "USERUSERGROUP_intUsergroupKey", "=", runtimeData.getParameter("USERGROUP_intUsergroupKey"), 0,
                    DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "USERUSERGROUP_strUserName" ,  "=",  groupUsers[i], 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rs = query.executeSelect();
                    
                    // check if it existed, do an "undelete"
                    if(rs.next()){
                        
                        // need not to check this lock
                        
                            query.executeUpdate();                               
                            // not existed, insert it
                    }else{
                        
                        // try to insert it
                        query1.reset();
                        query1.setDomain("USERUSERGROUP",null,null,null);
                        formFields = DatabaseSchema.getFormFields("cuseradmin_insert_user");
                        
                        runtimeData.setParameter("USERUSERGROUP_intUsergroupKey",runtimeData.getParameter("USERGROUP_intUsergroupKey"));
                        runtimeData.setParameter("USERUSERGROUP_strUserName",groupUsers[i]);
                        
                        query1.setFields(formFields, runtimeData);
                        
                        // any unexpected error
                        query1.executeInsert();
                            
                        
                    }
                    rs.close();
                    // Close the resultset
                    rs = null;
                }
                runtimeData.setParameter("error", "Details updated.");
                
            }
            
            // display the error
            
            
        }
        
        catch (Exception e)
        
        {
            
            e.printStackTrace();
            
            LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
            
        }
        
    }
    
    /**
     *    Method to delete a group
     * @return XML string to confirm the deletion or display error, if any
     */
    
    private String doDeleteGroupAction(){
        
        StringBuffer strXML = new StringBuffer();
        
        String strErr = "";
        
        String groupId;
        
        // try if a group has been posted
        if((groupId = runtimeData.getParameter("USERGROUP_intUsergroupKey"))!=null){
            try{
                
                
                
                // set up query basis
                DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                DALSecurityQuery query1 = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                query.setDomain("USERGROUP",null,null,null);
                Vector vtChild = new Vector();
                
                vtChild.add( groupId );
                
                query.setWhere(null, 0,"USERGROUP_intUsergroupKey", "=", groupId, 0, DALQuery.WHERE_HAS_VALUE);
                query.setField("USERGROUP_intDeleted","1");
                query.executeUpdate();
                
                for(int i=0; i < vtChild.size(); i++){
                     
                    groupId = (String) vtChild.get(i);
                    query.clearWhere();
                    query.setWhere(null, 0,"USERGROUP_intParentKey", "=", groupId, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setField("USERGROUP_intUsergroupKey",null);
                    
                    ResultSet rs = query.executeSelect();
                    while( rs.next() ){
                     
                        vtChild.add( rs.getString( "USERGROUP_intUsergroupKey" ) );
                        

                    
                    
                    }
                    
                    rs.close();
                    //Close the resultset
                    rs = null;
                    
                    // delete group
                    query.clearFields();
                    query.setField("USERGROUP_intDeleted","1");
                    query.executeUpdate();
                     
                    
                    // delete users in group
                    query1.reset();
                    
                    query1.setDomain("USERUSERGROUP",null,null,null);
                    query1.setWhere(null, 0,"USERUSERGROUP_intUsergroupKey", "=", groupId, 0, DALQuery.WHERE_HAS_VALUE);
                    query1.setField("USERUSERGROUP_intDeleted","1");
                    
                    query1.executeUpdate();
                
                }
                
               
               
 
                strErr = "Group deleted.";
                    
              
                
            }catch(Exception e){
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
            }
        }else{
            
            strErr = "No group selected. Please select a group on the left";
        }
        
        strXML.append("<proccess><error>" + strErr + "</error></proccess>");
        
        return strXML.toString();
        
    }
    
    /**
     *    Method to add a sub group into group
     * @return XML string to display the proccess
     */
    
    private String doAddSubGroupAction(){
        
        StringBuffer strXML = new StringBuffer();
        
        strXML.append("<proccess>");
        
        // check if posted from a details form
        if(runtimeData.getParameter(ADD_NEW_SUB_GROUP_ACTION)!=null){
            
            // query basis
            Vector formFields = new Vector();
            formFields = DatabaseSchema.getFormFields("cuseradmin_insert_usergroup");
            
            String strErr;
            
            // validate data
            if((strErr=QueryChannel.validateFields(formFields, runtimeData))==null){
                try{
                    DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                    
                    // check if same name exists in parent group
                    query.setDomain("USERGROUP",null,null,null);
                    query.setFields(formFields,null);
                    
                    query.setWhere(null,0,"USERGROUP_strUsergroupName","=", runtimeData.getParameter("USERGROUP_strUsergroupName"),
                    0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"USERGROUP_intParentKey","=", runtimeData.getParameter("USERGROUP_intParentKey"),
                    0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"USERGROUP_intDeleted","=", "0",
                    0, DALQuery.WHERE_HAS_VALUE);
                    
                    ResultSet rs = query.executeSelect();
                    
                    // if not
                    if(!rs.next()){
                        
                        query.reset();
                        query.setDomain("USERGROUP",null,null,null);
                        query.setFields(formFields,runtimeData);
                        
                        // insert into table
                        if(query.executeInsert()){
                            strXML.append("<error>Group added.</error>");
                        }else{
                            strXML.append("<error>An error has occured. Please try again later.</error>");
                        }
                        
                        // sme name in group
                    }else{
                        strXML.append("<error>Group name existed in this group. Please try another name.</error>");
                    }
                    
                    rs.close();
                    
                    //Close the resultset
                    rs = null;
                    
                }catch(Exception e){
                    
                    e.printStackTrace();
                    
                    LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                }
                
                // validate fails
            }else{
                
                strXML.append("<error>"+ strErr +"</error>");
            }
            
            // output them
            strXML.append("<groupDetails>");
            strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
            strXML.append("<USERGROUP_strUsergroupName>" + runtimeData.getParameter("USERGROUP_strUsergroupName") + "</USERGROUP_strUsergroupName>");
            strXML.append("<USERGROUP_strDescription>" + runtimeData.getParameter("USERGROUP_strDescription") + "</USERGROUP_strDescription>");
            strXML.append("<USERGROUP_intParentKey>" + runtimeData.getParameter("USERGROUP_intParentKey") + "</USERGROUP_intParentKey>");
            strXML.append("</groupDetails>");
            
            
        }else{
            
            
            int parentId = -1;
            
            // check for group to be added in
            if((runtimeData.getParameter("USERGROUP_intUsergroupKey"))!=null){
                
                // build the form
                parentId=Integer.parseInt(runtimeData.getParameter("USERGROUP_intUsergroupKey"));
                strXML.append("<groupDetails>");
                
                Vector formFields = new Vector();
                formFields = DatabaseSchema.getFormFields("cuseradmin_insert_usergroup");
                
                strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                
                
                strXML.append("<USERGROUP_intParentKey>" + parentId + "</USERGROUP_intParentKey>");
                
                strXML.append("</groupDetails>");
                
            }else{
                strXML.append("<error>No group selected. Please select a group on the left.</error>");
            }
            
        }
        
        strXML.append("</proccess>");
        
        return strXML.toString();
    }
    
    
    /**
     * Method to display search 
     * @return XML string to display process
     *
     */
    private String doSearchAction(){
     
        StringBuffer strXML = new StringBuffer();
        String strSearch = runtimeData.getParameter("search_string");
        
        
        strXML.append("<proccess><searchDetails>");
        
        if(strSearch != null){
            
            try{
                
                strXML.append("<searchString>" + strSearch + "</searchString>");
                
                
                Vector nameField = new Vector();
                DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                ResultSet rs;
            
                query.setDomain("USERDETAILS",null,null,null);
                query.setField("USERDETAILS_strUserName",null);
                query.setWhere(null,0, "USERDETAILS_strUserName", "LIKE", "%" + strSearch + "%", 0, DALQuery.WHERE_HAS_VALUE);
                nameField.add("USERDETAILS_strUserName");
            
                rs = query.executeSelect();
            
                strXML.append(QueryChannel.buildSearchXMLFile("user", rs, nameField));
            
                nameField = new Vector();
                nameField = DatabaseSchema.getFormFields("cuseradmin_view_usergroup");
                
                query.reset();
                query.setDomain("USERGROUP",null,null,null);
                query.setFields(nameField,null);
                query.setWhere(null,0, "USERGROUP_strUsergroupName", "LIKE", "%" + strSearch + "%",0, DALQuery.WHERE_HAS_VALUE);
                
                rs.close();
                
                rs = query.executeSelect();
                
                strXML.append(QueryChannel.buildSearchXMLFile("usergroup", rs, nameField));

                rs.close();
                // Close the resultset
                rs = null;
                
            }catch(Exception e){
                
                e.printStackTrace();
            
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                
            }
            
            
            
            
        }
        
        
        
        
        strXML.append("</searchDetails></proccess>");
        return strXML.toString();        
        
        
    }
    
    
    
    /**
     *    Method to set password for users
     * @return XML string to display process
     */
    
    private String doSetPasswordAction(){
        
        String username=runtimeData.getParameter("USERDETAILS_strUserName");
        StringBuffer strXML = new StringBuffer();
        
        strXML.append("<proccess>");
        
        // try to catch all exceptions
        try{
            
            
            
            // check if username exists in the request
            if(username != null){
                
                String pass1 = runtimeData.getParameter("USERDETAILS_strPassword");
                String pass2 = runtimeData.getParameter("re-type");
                Vector formFields = new Vector();
                formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_password");
                
                // check if submitted from our form
                if( runtimeData.getParameter(SAVE_USER_PASSWORD_ACTION)!=null){
                    
                    // check if password is not blank
                    if( !pass1.equals("") ){
                        
                        //query basis
                        DALQuery query = new DALQuery();
                        
                        
                        boolean success = false;
                        
                        // 2 easy to get
                        if(pass1.equals(pass2)){
                            
                            // md5 them
                            pass1 = Password.getEncrypt(pass1);
                            
                            runtimeData.setParameter("USERDETAILS_strPassword",pass1);
                            
                            // update them
                            query.setDomain("USERDETAILS",null,null,null);
                            query.setWhere(null, 0, "USERDETAILS_strUserName", "=",  username, 0, DALQuery.WHERE_HAS_VALUE);
                            query.setFields(formFields, runtimeData);
                            
                            if (lock != null)                            
                            {
                   
                                lock.unlock();
                                lock = null;
                        
                            }
                            lockUser(username,  LockRecord.READ_WRITE);               
                            lock.lockDelayWrite();
                            
                            if((lock.isValid()) && lock.lockWrites()){
                                // catch any unexpected errors
                                if(!query.executeUpdate()){
                                
                                    strXML.append("<error>An unknown error has occured, please try again later.</error>");
                                }else{
                                
                                    strXML.append("<error>New password updated.</error>");
                                }
                                lock.unlockWrites();
                            }else{
                               
                                    strXML.append("<error>" + LOCK_ERROR + "</error>");
                            }
                            
                        }else{
                            
                            // the error explains
                            strXML.append("<error>Passwords do not match. Please try again.</error>");
                            
                        }
                        
                        
                    }else{
                        
                        strXML.append("<error>Please enter password</error>");
                        
                    }
                    
                } // end check submit
                
                // print out form here
                lockUser(username,LockRecord.READ_WRITE);
                lock.lockDelayWrite();
                
                strXML.append("<userPassword>");
                strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                strXML.append("<USERDETAILS_strUserName>" + username + "</USERDETAILS_strUserName>");
                strXML.append("</userPassword>");
                
            }else{
                
                strXML.append("<error>No user selected. Please click on a user on the left to set the password</error>");
                
            }
            
        }catch(Exception e){
            
            e.printStackTrace();
            
            LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
            
        }
        
        
        strXML.append("</proccess>");
        
        return strXML.toString();
        
    }
    /**
     *    Method to set password for users
     * @return XML string to display process
     */
    
    private String doSetUserPasswordAction(){
        
        String username=authToken.getUserIdentifier();
        
        StringBuffer strXML = new StringBuffer();
        
        strXML.append("<proccess>");
        
        // try to catch all exceptions
        try{
            
            
            
            // check if username exists in the request
            if(username != null){
                
                String pass1 = runtimeData.getParameter("USERDETAILS_strPassword");
                String pass2 = runtimeData.getParameter("re-type");
                Vector formFields = new Vector();
                formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_password");
                
                // check if submitted from our form
                if( runtimeData.getParameter(SAVE_USER_PASSWORD_ACTION)!=null){
                    
                    // check if password is not blank
                    if( !pass1.equals("") ){
                        
                        //query basis
                        DALQuery query = new DALQuery();
                        
                        
                        boolean success = false;
                        
                        // 2 easy to get
                        if(pass1.equals(pass2)){
                            
                            // md5 them
                            pass1 = Password.getEncrypt(pass1);
                            
                            runtimeData.setParameter("USERDETAILS_strPassword",pass1);
                            
                            // update them
                            query.setDomain("USERDETAILS",null,null,null);
                            query.setWhere(null, 0, "USERDETAILS_strUserName", "=",  username, 0, DALQuery.WHERE_HAS_VALUE);
                            query.setFields(formFields, runtimeData);
                            if (lock != null)
                            {
                   
                                lock.unlock();
                                lock = null;
                        
                            }
                            lockUser(username,  LockRecord.READ_WRITE);               
                            lock.lockDelayWrite();
                            
                            if((lock.isValid()) && lock.lockWrites()){
                                // catch any unexpected errors
                                if(!query.executeUpdate()){
                                
                                    strXML.append("<error>An unknown error has occured, please try again later.</error>");
                                }else{
                                
                                    strXML.append("<error>New password updated.</error>");
                                }
                                lock.unlockWrites();
                            }else{
                               
                                    strXML.append("<error>" + LOCK_ERROR + "</error>");
                            }
                            
                        }else{
                            
                            // the error explains
                            strXML.append("<error>Passwords do not match. Please try again.</error>");
                            
                        }
                        
                        
                    }else{
                        
                        strXML.append("<error>Please enter password</error>");
                        
                    }
                    
                } // end check submit
                
                // print out form here
                lockUser(username,LockRecord.READ_WRITE);
                lock.lockDelayWrite();
                
                strXML.append("<userPassword>");
                strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                strXML.append("<USERDETAILS_strUserName>" + username + "</USERDETAILS_strUserName>");
                strXML.append("</userPassword>");
                
            }else{
                
                strXML.append("<error>Not logged in.</error>");
                
            }
            
        }catch(Exception e){
            
            e.printStackTrace();
            
            LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
            
        }
        
        
        strXML.append("</proccess>");
        
        return strXML.toString();
        
    }
    
    /**
     *    Method to delete users
     * @return XML string to display process
     */
    
    private String doDeleteUserAction(){
        
        StringBuffer strXML = new StringBuffer();
        
        String strErr = "";
        
        // check if user name posted
        if(runtimeData.getParameter("USERDETAILS_strUserName")!=null){
            try{
                
                // query basis
                DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                
                query.setDomain("USERDETAILS",null,null,null);
                query.setWhere(null, 0,"USERDETAILS_strUserName", "=", runtimeData.getParameter("USERDETAILS_strUserName"), 0, DALQuery.WHERE_HAS_VALUE);
                
                // delete it
                if(query.executeDelete()){
                    query.reset();
                    
                    query.setDomain("USERUSERGROUP",null,null,null);
                    query.setWhere(null, 0,"USERUSERGROUP_strUserName", "=", runtimeData.getParameter("USERDETAILS_strUserName"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.setField("USERUSERGROUP_intDeleted","1");
                    
                    query.executeUpdate();
                    
                    strErr = "User deleted.";
                    
                }else{
                    
                    strErr = "An error has occured. Please try again later.";
                }
                
            }catch(Exception e){
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
            }
        }else{
            
            strErr = "No user selected. Please select a user from the left.";
            
        }
        
        strXML.append("<proccess><error>" + strErr + "</error></proccess>");
        
        return strXML.toString();
    }
    
    /**
     *    Method to add users
     * @return XML string to display process
     */
    
    private String doAddUserAction(){
        
        StringBuffer strXML = new StringBuffer();
        
       
        strXML.append("<proccess>");
        
        // check if posted from an add user form
        if(runtimeData.getParameter(ADD_NEW_USER_ACTION)!=null)
        {
            
            // query basis
            Vector formFields = new Vector();
            formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_details");
            
            String strErr;
            success = false;
            
            // validate data
            if((strErr=QueryChannel.validateFields(formFields, runtimeData))==null)
            {
                try{
                    
                    DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                    
                    // try to search for that user name first
                    query.setDomain("USERDETAILS",null,null,null);
                    query.setFields(formFields,null);
                    query.setWhere(null,0,"USERDETAILS_strUserName","=", runtimeData.getParameter("USERDETAILS_strUserName"),
                    0, DALQuery.WHERE_HAS_VALUE);
                    
                    ResultSet rs = query.executeSelect();
                    
                    // if username existed
                    if(!rs.next()){
                        
                        // try to insert
                        query.reset();
                        query.setDomain("USERDETAILS",null,null,null);
                        
                        query.setFields(formFields,runtimeData);
                        
                        
                        if(query.executeInsert()){
                            
                            strXML.append("<error>User added.</error>");
                            success = true;
                            
                        }else
                        {
                            
                            strXML.append("<error>An error has occured. Please try again later.</error>");
                            
                        }
                        
                    }else
                    {
                        
                        strXML.append("<error>User existed. Please try another name.</error>");
                        exists = true;
                    }
                    rs.close();
                    //Close the result set
                    rs = null;
                }catch(Exception e){
                    
                    e.printStackTrace();
                    
                    LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                }
            }else{
                
                strXML.append("<error>"+ strErr +"</error>");
            }
            
            if(!success)
            {
//                System.err.println("----------doAddUserAction--------!success------");
                // display them
                if(exists)
                {
/*rennypv       */ 
                strXML.append("<userDetails>");
                strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                strXML.append("<USERDETAILS_strFirstName>" + runtimeData.getParameter("USERDETAILS_strFirstName") + "</USERDETAILS_strFirstName>");
                strXML.append("<USERDETAILS_strLastName>" + runtimeData.getParameter("USERDETAILS_strLastName") + "</USERDETAILS_strLastName>");
                strXML.append("<USERDETAILS_strEmail>" + runtimeData.getParameter("USERDETAILS_strEmail") + "</USERDETAILS_strEmail>");
                strXML.append("<USERDETAILS_strUserName>" + runtimeData.getParameter("USERDETAILS_strUserName") + "</USERDETAILS_strUserName>");
                if(runtimeData.getParameter("USERGROUP_intUsergroupKey") != null)
                {
                    strXML.append("<userGroupKey>"+ runtimeData.getParameter("USERGROUP_intUsergroupKey") +"</userGroupKey>");
                }
                strXML.append("<clone>"+ runtimeData.getParameter("USERID_strUserName") +"</clone>");
                strXML.append("</userDetails>");
//                rennypv*/
                }
                
                else if(runtimeData.getParameter("UsergroupKey") != null)
                {
//                    System.err.println("-----------UsergroupKey != null---------");
                    try
                    {
                        strXML.append("<userDetails>");
                        DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                        query.clearDomains();
                        query.clearFields();
                        query.clearWhere();
                        query.setDomain("USERUSERGROUP", null, null, null);
                        query.setFields(DatabaseSchema.getFormFields("cuseradmin_view_user_usergroup"),null);
                        query.setWhere(null,0,"USERUSERGROUP_intUsergroupKey","=", runtimeData.getParameter("UsergroupKey"),0, DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND",0,"USERUSERGROUP_intDeleted","=", "0",0, DALQuery.WHERE_HAS_VALUE);
                        ResultSet rsusers = query.executeSelect();
                        while(rsusers.next())
                        {
                            String user = rsusers.getString("USERUSERGROUP_strUserName");
        //                    System.err.println("-----------user is----------" + user);
                            strXML.append("<user>"+user+"</user>");
                        }
                        strXML.append("<userGroupKey>"+runtimeData.getParameter("UsergroupKey")+"</userGroupKey>");                
                        strXML.append("</userDetails>"); 
                        
                        rsusers.close();
                        //Close the resultset
                        rsusers = null;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
//                    System.err.println("----------!success n else------------");
                    strXML.append("<userDetails>");
                    strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                    strXML.append("<USERDETAILS_strFirstName>" + runtimeData.getParameter("USERDETAILS_strFirstName") + "</USERDETAILS_strFirstName>");
                    strXML.append("<USERDETAILS_strLastName>" + runtimeData.getParameter("USERDETAILS_strLastName") + "</USERDETAILS_strLastName>");
                    strXML.append("<USERDETAILS_strEmail>" + runtimeData.getParameter("USERDETAILS_strEmail") + "</USERDETAILS_strEmail>");
                    strXML.append("<USERDETAILS_strUserName>" + runtimeData.getParameter("USERDETAILS_strUserName") + "</USERDETAILS_strUserName>");
    //                strXML.append("<userGroupKey>"+runtimeData.getParameter("USERGROUP_intUsergroupKey")+"</userGroupKey>");                
                    strXML.append("</userDetails>");
                }
            }
            
            
        }
        else if(runtimeData.getParameter("newuser") == null && runtimeData.getParameter("UsergroupKey") != null)
        {
//            System.err.println("----------new user == null----------");
            try
                {
                    strXML.append("<userDetails>");
                    DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                    query.clearDomains();
                    query.clearFields();
                    query.clearWhere();
                    query.setDomain("USERUSERGROUP", null, null, null);
                    query.setFields(DatabaseSchema.getFormFields("cuseradmin_view_user_usergroup"),null);
                    query.setWhere(null,0,"USERUSERGROUP_intUsergroupKey","=", runtimeData.getParameter("UsergroupKey"),0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"USERUSERGROUP_intDeleted","=", "0",0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsusers = query.executeSelect();
                    while(rsusers.next())
                    {
                        String user = rsusers.getString("USERUSERGROUP_strUserName");
//                        System.err.println("-----------user is----------" + user);
                        strXML.append("<user>"+user+"</user>");
                    }
                    strXML.append("<userGroupKey>"+runtimeData.getParameter("UsergroupKey")+"</userGroupKey>");                
                    if(runtimeData.getParameter("cancel") == null)
                    {
                        strXML.append("<error>Please select one of the options</error>");                    
                    }
                    strXML.append("</userDetails>");   

                    rsusers.close();
                    //Close the resultset
                    rsusers = null;                    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
        }
        else if(runtimeData.getParameter("USERGROUP_intUsergroupKey") != null)
        {
//            System.err.println("----------in the else------add_user_action == null---------" + runtimeData.getParameter("USERGROUP_intUsergroupKey"));
            try
            {
                strXML.append("<userDetails>");
                DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                query.clearDomains();
                query.clearFields();
                query.clearWhere();
                query.setDomain("USERUSERGROUP", null, null, null);
                query.setFields(DatabaseSchema.getFormFields("cuseradmin_view_user_usergroup"),null);
                query.setWhere(null,0,"USERUSERGROUP_intUsergroupKey","=", runtimeData.getParameter("USERGROUP_intUsergroupKey"),0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"USERUSERGROUP_intDeleted","=", "0",0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsusers = query.executeSelect();
                while(rsusers.next())
                {
                    String user = rsusers.getString("USERUSERGROUP_strUserName");
//                    System.err.println("-----------user is----------" + user);
                    strXML.append("<user>"+user+"</user>");
                }
                strXML.append("<userGroupKey>"+runtimeData.getParameter("USERGROUP_intUsergroupKey")+"</userGroupKey>");                
                strXML.append("</userDetails>");      
                
                rsusers.close();
                //Close the resultset
                rsusers = null;                
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
//rennypv        
        
        else{
//            System.err.println("-----------in the else line 2149------------");
            strXML.append("<userDetails>");
            if(runtimeData.getParameter("UsergroupKey") != null)
            {
                strXML.append("<userGroupKey>"+runtimeData.getParameter("UsergroupKey")+"</userGroupKey>");                            
            }
            Vector formFields = new Vector();
            formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_details");
            
            strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
            if(runtimeData.getParameter("newuser") != null && runtimeData.getParameter("newuser").equals("cloneuser"))
            {
//                System.err.println("----------in the else--where cloneuser-------" + runtimeData.getParameter("user"));                
                strXML.append("<clone>"+ runtimeData.getParameter("user") +"</clone>");            
            }
            strXML.append("</userDetails>");
            
        }
        
        strXML.append("</proccess>");
        
        return strXML.toString();
    }
    /**
     *    Method to save user details
     */
    
    private void doUserSaveAction(){
        
        String username=runtimeData.getParameter("USERDETAILS_strUserName");
        
        if(username != null){
            
            try{
                
                // query basis
                Vector formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_details");
                DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                String strErr;
                
                query.setDomain("USERDETAILS",null,null,null);
                query.setFields(formFields,runtimeData);
                
                
                // validate data
                if((strErr=QueryChannel.validateFields(formFields,runtimeData))==null){
                    
                    query.setWhere(null,0,"USERDETAILS_strUserName","=",  username ,0,DALQuery.WHERE_HAS_VALUE);
                    

                    if( lock != null ) lock.unlock();
                    lock = null;
                    
                    lock = new LockRequest( authToken );
                    lock.addLock( "USERDETAILS", username, LockRecord.READ_WRITE );
                    lock.lockDelayWrite();
                    
                    if(lock.isValid() && lock.lockWrites()){

                        // update details
                        if(query.executeUpdate()){
                        
                            runtimeData.setParameter("error", "Details updated.");
                        
                        }else{
                            runtimeData.setParameter("error", "An unknown error has occured. Please try again later.");
                        
                        }
                        
                        lock.unlockWrites();
                                     
                    }else{
                        
                        runtimeData.setParameter("error", LOCK_ERROR);
                    }
                }else{
                    
                    runtimeData.setParameter("error", strErr);
                }
                
            }
            
            catch (Exception e)
            
            {
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                
            }
            
        }
    }
    
    /**
     *    Method to display user details
     * @return XML string to display user details and error from other process
     * on users
     */
    
    private String doUserClickAction(){
        
        StringBuffer strXML = new StringBuffer();
        
        String username=runtimeData.getParameter("USERDETAILS_strUserName");
        
        strXML.append("<proccess>");
        
        if(username != null){
            
            try{
                
                
                // display message from other procces
                if(runtimeData.getParameter("error")!=null){
                    
                    strXML.append("<error>" + runtimeData.getParameter("error") + "</error>");
                }
                
                
                
                
                // query bais
                Vector formFields = DatabaseSchema.getFormFields("cuseradmin_view_user_details");
                DALSecurityQuery query = new DALSecurityQuery(USERGROUP_ADMIN, authToken);
                
                query.setDomain("USERDETAILS",null,null,null);
                query.setFields(formFields,null);
                
                query.setWhere(null,0,"USERDETAILS_strUserName","=",  username ,0,DALQuery.WHERE_HAS_VALUE);
                 
                ResultSet rs = query.executeSelect();
                
                
                // lock user
               
                
                if (lock != null){
                   
                        lock.unlock();
                        lock = null;
                        
                }
                lockUser(username,  LockRecord.READ_WRITE);               
                lock.lockDelayWrite();
                
                // display details
                strXML.append("<userDetails>");
                
                strXML.append(QueryChannel.buildFormLabelXMLFile(formFields));
                
                strXML.append(QueryChannel.buildSearchXMLFile("details", rs, formFields));
                
                strXML.append("</userDetails>");
                rs.close();
                // Close the resultset
                rs = null;
            }
            
            catch (Exception e)
            
            {
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CUserAdmin Channel - " + e.toString());
                
            }
            
        }else{
            
            strXML.append("<error>No user selected. Please select one user from the left.</error>");
        }
        
        strXML.append("</proccess>");
        
        return strXML.toString();
    }
    
    
    /**
     *    Output channel content to the portal
     * @param out a sax document handler
     * @throws PortalException Portal Exception
     */
    
    public void renderXML(ContentHandler out) throws PortalException {
        
        
        
        // Create a new XSLT styling engine
        
        XSLT xslt = new XSLT(this);
        
        
        
        // pass the result xml to the styling engine.
        
        //System.err.println("------------XML----------" + strXML.toString());
        
     
        xslt.setXML(strXML.toString());
        
        
        
        // specify the stylesheet selector
        
        xslt.setXSL("CUserAdmin.ssl", strStylesheet, runtimeData.getBrowserInfo());
        
        
        
        
        // set parameters that the stylesheet needs.
        
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());
        
        
        
        // set the output Handler for the output.
        
        xslt.setTarget(out);
        
        
        
        // do the deed
        
        xslt.transform();
        
    }
    /**
     *    Method to generate the usergroup tree XML String
     * @param myTree DefaultMutableTreeNode tree stucture
     * @param expandedTable HashTable tree expand details
     * @return XML string of the tree
     */
    private String toXML(DefaultMutableTreeNode myTree, Hashtable expandedTable){
        
        StringBuffer strXML = new StringBuffer();
        
        // traverse through tree elements
        Enumeration tree_enum = myTree.breadthFirstEnumeration();
        
        tree_enum.nextElement();
        
        strXML.append("<usergroupTree>");
        
        // go in to each node
        while (tree_enum.hasMoreElements()){
            
            Hashtable currentNode = (Hashtable) (
            ((DefaultMutableTreeNode) tree_enum.nextElement()).getUserObject());
            
            strXML.append("<treeNode>");
            
            // data
            strXML.append("<nodeName>").append(currentNode.get("nodeName")).append("</nodeName>");
            strXML.append("<type>").append(currentNode.get("type")).append("</type>");
            strXML.append("<id>").append(currentNode.get("id")).append("</id>");
            strXML.append("<treeId>").append(currentNode.get("treeId")).append("</treeId>");
            strXML.append("<parentId>").append(currentNode.get("parentId")).append("</parentId>");
            strXML.append("<url>").append(currentNode.get("url")).append("</url>");
            
            // expanded detail
            if(expandedTable.containsKey(currentNode.get("id"))){
                
                strXML.append("<expanded>").append("true").append("</expanded>");
                
            }else{
                
                strXML.append("<expanded>").append("false").append("</expanded>");
                
            }
            
            strXML.append("</treeNode>");
            
        }
        
        strXML.append("</usergroupTree>");
        
        return strXML.toString();
    }
    
    
    /**
     *    Method to buid the usergroup tree
     * @return DetaultMutableTreeNode tree structure
     */
    
    private DefaultMutableTreeNode buildUsergroupTree(Hashtable expanded){
        
        Vector vtFF = new Vector();
        vtFF.add("usergroup_mapping");
        
        // get the group tree
        DefaultMutableTreeNode myTree =
        new GenericTree().buildTree(new Hashtable(),"Root",GenericTree.SINGLE_DOMAIN, CHANNEL_NAME);
        
        Enumeration tree_enum = myTree.breadthFirstEnumeration();
        // skip the root
        tree_enum.nextElement();
        
        // assign unique ids to groups
        int id=ROOT_ID;
        while(tree_enum.hasMoreElements()){
            
            DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) tree_enum.nextElement();
            Hashtable currentNode = (Hashtable) cNode.getUserObject();
            
            // set up elements
            currentNode.put("nodeName",currentNode.get("USERGROUP_strUsergroupName"));
            if(id==ROOT_ID){
                currentNode.put("parentId", "-1");
            }
            currentNode.put("treeId",new Integer(id++).toString());
            currentNode.put("type",USERGROUP);
            currentNode.put("id",currentNode.get("USERGROUP_intUsergroupKey"));
            currentNode.put("url",GROUP_ACTION);
            
        }
        
        // make parentID
        tree_enum = myTree.breadthFirstEnumeration();
        tree_enum.nextElement();
        tree_enum.nextElement();
        
        while(tree_enum.hasMoreElements()){
            
            DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) tree_enum.nextElement();
            if (cNode != null && cNode.getParent() != null)
            {
                Hashtable pNode = (Hashtable) (((DefaultMutableTreeNode)cNode.getParent()).getUserObject());
                Hashtable currentNode = (Hashtable) cNode.getUserObject();
                currentNode.put("parentId",pNode.get("treeId"));
            }
        }
        
        
        DALQuery query = new DALQuery();
        vtFF = DatabaseSchema.getFormFields("cuseradmin_view_user_usergroup");
        
        // start from root group again
        Enumeration enuma = myTree.breadthFirstEnumeration();
        enuma.nextElement();
        
        
        // add users to group
        try{
            // go into every node
            while (enuma.hasMoreElements()){
                // set up the current node
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) enuma.nextElement();
                Hashtable currentNode = (Hashtable) cNode.getUserObject();
                
                
                // set up parentid for current node
                //			  currentNode.put("parentId",((Hashtable) cNode.getParent()).get("treeId"));
                
                // look for users of group
                
                if(expanded.containsKey(currentNode.get("id"))){
                
                    query.reset();
                    query.setDomain("USERUSERGROUP",null,null,null);
                    query.setFields(vtFF,null);
                
                    // set up criteria for select query
                    query.setWhere(null,0,"USERUSERGROUP_intUsergroupKey","=",currentNode.get("id")
                    ,0,DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"USERUSERGROUP_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                
                    // get the result
                    ResultSet rsResultSet = query.executeSelect();
                    Vector vtResult = QueryChannel.lookupRecord(
                    rsResultSet, vtFF);
                
                    rsResultSet.close();
                    
                    // add to current node
                    for(int i = 0; i < vtResult.size(); i++){
                    
                        // set up elements
                        Hashtable hsTemp = new Hashtable();
                        hsTemp.put("nodeName",
                        ((Hashtable) vtResult.get(i)).get("USERUSERGROUP_strUserName"));
                        hsTemp.put("type",USER);
                        hsTemp.put("id",
                        ((Hashtable) vtResult.get(i)).get("USERUSERGROUP_intUserUsergroupKey"));
                        hsTemp.put("treeId",new Integer(id++).toString());
                        hsTemp.put("parentId",
                        currentNode.get("treeId"));
                        hsTemp.put("url",USER_ACTION);
                    
                        // add one node
                        cNode.add( new DefaultMutableTreeNode(hsTemp) );
                        //			  System.err.println("Add: " + hsTemp + " To: " + cNode);
                    } // end for
                }
                
                
            }// end while
            
        }catch(Exception e){
            
            LogService.instance().log(LogService.ERROR,
            "Unknown error in CUserAdmin - " + e.toString(), e);
        }
        
        
        return myTree;
        
    }
    
/**
 *
 * Lock visible records
 *
 */
    
    private void lockTree(DefaultMutableTreeNode tree, Hashtable visible){
        

        try{
            
            
            Enumeration tree_enum = tree.breadthFirstEnumeration();
            tree_enum.nextElement();
            
            
            DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) tree_enum.nextElement();
            Hashtable currentNode = (Hashtable) cNode.getUserObject();
            
        
            //if(currentGroup.equals(currentNode.get("id").toString())){
            //    lockUsergroup(currentNode.get("id").toString(),visible.containsKey(currentNode.get("id")),LockRecord.READ_WRITE);
               
            //}else
            lockUsergroup(currentNode.get("id").toString(),visible.containsKey(currentNode.get("id")),LockRecord.READ_ONLY);
            
            
            while(tree_enum.hasMoreElements()){
                
                cNode = (DefaultMutableTreeNode) tree_enum.nextElement();
                currentNode = (Hashtable) cNode.getUserObject();
                
                DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) cNode.getParent();
                Hashtable parrentNode = (Hashtable) pNode.getUserObject();
                
                
                
                if( visible.containsKey(parrentNode.get("id"))){
                
                    
                    if(!currentNode.get("type").equals(USER)){
                        //if(currentGroup.equals(currentNode.get("id").toString())){
                         //   lockUsergroup(currentNode.get("id").toString(), visible.containsKey(currentNode.get("id")),  LockRecord.READ_WRITE);
                           
                        //}else
                            lockUsergroup(currentNode.get("id").toString(), visible.containsKey(currentNode.get("id")), LockRecord.READ_ONLY);
                    }
  
                    
                }
        
                
            }
            //System.err.println( "-------------------------------;");
        }catch (Exception e){
            
                LogService.instance().log(
                LogService.ERROR, "Unknown error in UserAdmin Channel - " + e.toString(), e);
                
        }
        
        
        
    }


    private void lockUser(String strUserName, int lockType){
    
        try{
          
            if(lock==null)
                lock = new LockRequest(authToken);
                //if( !lockedUsers.containsKey( "strUserName" ))
            lock.addLock("USERDETAILS", strUserName,  lockType);
      //      System.err.println( "Locking USER: " + strUserName );
            
            //System.err.println("Add lock: " + lock.toString());
            
            //System.err.println(lock.toString());
        
        }catch (Exception e){
            
                LogService.instance().log(
                LogService.ERROR, "Unknown error in UserAdmin Channel - " + e.toString(), e);
        }
        
    }
    
    private void lockUsergroup(String usergroupId, boolean lockChildren, int lockType){
        
        
        try{
            
            if (lock == null) lock = new LockRequest(authToken);
            
            DALQuery query = new DALQuery();
            ResultSet rs;
            
            
            
            if (lockChildren){
                
                query.setDomain("USERUSERGROUP",null,null,null);
                query.setField("USERUSERGROUP_intUserUsergroupKey",null);
                query.setWhere(null,0,"USERUSERGROUP_intUsergroupKey", "=", usergroupId, 0, DALQuery.WHERE_HAS_VALUE);
                rs = query.executeSelect();
                
            
                while(rs.next()){
                
                    lock.addLock("USERUSERGROUP",rs.getString("USERUSERGROUP_intUserUsergroupKey"),lockType );
                   
              
                }
                
                rs.close();
                query.reset();
                query.setDomain("USERGROUP",null,null,null);
                query.setField("USERGROUP_intUsergroupKey",null);
                query.setWhere(null,0,"USERGROUP_intParentKey", "=", usergroupId, 0, DALQuery.WHERE_HAS_VALUE);
                rs = query.executeSelect();
            
                
            
                while(rs.next()){
                     
                    lock.addLock("USERGROUP",rs.getString("USERGROUP_intUsergroupKey"),lockType );
      //              System.err.println( "Locking GROUP: " + rs.getString("USERGROUP_intUsergroupKey") );
               
                }
                rs.close();
            }else{
                
                lock.addLock("USERUSERGROUP",usergroupId,lockType );
            }
            
       
            
        }catch (Exception e){
            
                LogService.instance().log(
                LogService.ERROR, "Unknown error in UserAdmin Channel - " + e.toString(), e);
        }
    }
    
    
/**
 *
 *  Method to check the permission to view this channel
 *
 *  @param String strActivity Activity name
 *  @param String permission permission name
 *  @return boolean returns the result
 *
 */
	private boolean checkPermissions(String strActivity, String permissionRequested)

	{

		try

		{

			if (!authToken.hasActivity(strActivity))

			{

				String strXML;
                                strXML= "<security>" 

					+ "<errorstring>No Permission to " + permissionRequested + "</errorstring>"

					+ "<errortext>" 

					+ "The person " + staticData.getPerson().getFullName() + " is not authorised to " + permissionRequested 

					+ "</errortext>"

					+ "<errordata></errordata>"

					+ "</security>";
                                
                                this.strXML.append(strXML);

				strStylesheet = SECURITY_ERROR;

				return false;

			}

			

		}

		catch(Exception secEx)

		{

			LogService.instance().log(LogService.ERROR, "CUserAdmin: Error checking permissions for \"" 

							+ strActivity + "\" - \"" + permissionRequested + "\"", secEx);

			return false;

		}

		return true;

	}

}
