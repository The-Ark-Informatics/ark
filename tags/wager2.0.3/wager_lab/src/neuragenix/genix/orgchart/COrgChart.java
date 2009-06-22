/**
 *
 * Copyright (c) 2003 Neuragenix. All Rights Reserved.
 *
 * COrgChart.java
 *
 * Created on 11 February 2003, 00:00
 *
 * @author  Long Tran, email: ltran@neuragenix.com
 *
 *
 * Description:
 *
 * Class for managing users and groups in an BUSINESS organization
 *
 *
 *
 **/


package neuragenix.genix.orgchart;

// portal classes
import org.jasig.portal.IChannel;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XSLT;
import org.xml.sax.ContentHandler;
import org.jasig.portal.security.*;
import org.jasig.portal.services.LogService;
import neuragenix.security.*;

// java classes
import java.util.*;
import java.io.*;
import javax.swing.tree.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

// neuragenix classes
import neuragenix.dao.*;
import neuragenix.common.*;

/** Class for managing users and groups
 * @author Long Tran {@link mailto:ltran@neuragenix.com}
 * @version 1.0
 */

public class COrgChart implements IChannel {
    // tree actions
    private static final String EXPAND_ACTION ="expand";
    private static final String COLLAPSE_ACTION = "collapse";
    
    
    // error msg
    private static final String LOCK_ERROR = "The record you are trying to update or delete is being viewed by others. Please try again later.";
    private static final String SECURITY_ERROR = "security_error";
    
    // group action types
    private static final String GROUP_ACTION="group";
    private static final String VIEW_GROUP="view_group";
    private static final String ADD_GROUP="add_group";
    private static final String DELETE_GROUP="delete_group";
    private static final String SAVE_GROUP="save_group";
    private static final String SEARCH_GROUP="search_group";
    
    // user action types
    private static final String VIEW_USER="view_user";
    private static final String ADD_USER="add_user";
    private static final String SAVE_USER="save_user";
    private static final String SEARCH_USER="search_user";
    private static final String USER_ACTION="user";
    private static final String DELETE_USER="delete_user";
    private static final String EMPLOYEE_ADDED="Employee added. Enter data to add another employee.";
    
    // other constants
    private static final String ADD_BUTTON="add_button";
    private static final String SEARCH_BUTTON="search_button";
    private static final String SAVE_BUTTON="save_button";
    private static final String SEARCH_ACTION="search_action";
    private static final String DEFAULT_VIEW = "display_menu";
    
    // permission constants
    private static final String ORG_CHART_VIEW="org_chart_view";
    private static final String ORG_CHART_EDIT="org_chart_edit";
    
    // data for rendering last page
    private StringBuffer strLastXML = null;
    private String strLastStylesheet;
    
    // employees' picture location
    private String pictureLocation = PropertiesManager.getProperty("neuragenix.genix.orgchart.PictureLocation");
    
    // lock
    private LockRequest lock;
    
    // variable to check the current group & user for locking
    private String currentGroup;
    private String currentUser;
    
    // boolean for user add proccess
    boolean success=false;
    
    // authToken
    private AuthToken authToken ;
    
    // tree expand information
    Hashtable hsExpanded = new Hashtable();
    
    // At the very least, you'll need to keep track of the static data and the runtime data that the portal sends you.
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    
    
    
    private StringBuffer strXML; // used to create the xml document with the starting XML header
    private String strStylesheet; // Used to specify the stylesheet to use for the portal
    
    
    
    /**
     *
     *  Contructs the UserAdmin Channel
     *
     */
    
    public COrgChart() {
        
        
    }
    
    
    
    /**
     *
     *  Returns channel runtime properties.
     *
     *  Satisfies implementation of Channel Interface.
     *
     *
     *
     *  @return handle to runtime properties
     *
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
                
                lock = null;
                
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
        
        InputStream file = COrgChart.class.getResourceAsStream("OrgChartSchema.xml");
        DatabaseSchema.loadDomains(file,"OrgChartSchema.xml");
        
        hsExpanded.put("-1","");
        
        
        
    }
    
    /**
     *  Receive channel runtime data from the portal.
     *
     *  Satisfies implementation of IChannel Interface.
     * @param rd rd handle to channel runtime data
     */
    
    public void setRuntimeData(ChannelRuntimeData rd) {
        
        // set current user and group invalid values for proper settings in appropriate methods
        currentGroup = "-2";
        currentUser = "-2";
        
        try{
            this.runtimeData = rd;
            
            // clear the lock
            if( lock == null ) 
                lock = new LockRequest( authToken );
            
            strXML = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            
            // check for VIEW permission
            if ( !authToken.hasActivity( ORG_CHART_VIEW  ) ){
                
                strXML.append( "<body>" + doSecurityError() );
                this.strStylesheet = SECURITY_ERROR;
                return;
            }
            
            
            // catch tree actions
            if(rd.getParameter("treeAction")!=null){
                
                if(rd.getParameter("treeAction").equals(EXPAND_ACTION)){
                    
                    hsExpanded.put(rd.getParameter("ORGGROUP_intOrgGroupKey"),"0");
                }else if(rd.getParameter("treeAction").equals(COLLAPSE_ACTION)){
                    
                    hsExpanded.remove(rd.getParameter("ORGGROUP_intOrgGroupKey"));
                }
            }
            
            
            // generating body
            strXML.append("<body>");
            
            // process actions
            
            // check for first entry of channel
            if(rd.getParameter("action")!=null){
                
                // catch user actions
                if(rd.getParameter("action").equals(USER_ACTION)){
                    
                    // save user details
                    if( rd.getParameter( SAVE_BUTTON ) != null ){
                        
                        // check for permission to edit
                        if ( !authToken.hasActivity( ORG_CHART_EDIT  ) ){
                            
                            strXML.append( doSecurityError() );
                            this.strStylesheet = SECURITY_ERROR;
                            return;
                        }
                        
                        doSaveUserAction();
                        strXML.append( doViewUserAction() );
                        this.strStylesheet = VIEW_USER;
                        
                        // delete a user
                    }else if( rd.getParameter( DELETE_USER ) != null ){
                        
                        // check for permission to edit
                        if ( !authToken.hasActivity( ORG_CHART_EDIT  ) ){
                            
                            strXML.append( doSecurityError() );
                            this.strStylesheet = SECURITY_ERROR;
                            return;
                        }
                        
                        strXML.append( doDeleteUserAction() );
                        this.strStylesheet = DELETE_USER;
                        
                        // add a user
                    }else if(rd.getParameter(ADD_USER) != null){
                        
                        // check for permission to edit
                        if ( !authToken.hasActivity( ORG_CHART_EDIT  ) ){
                            
                            strXML.append( doSecurityError() );
                            this.strStylesheet = SECURITY_ERROR;
                            return;
                        }
                        
                        
                        strXML.append( doAddUserAction() ) ;
                        this.strStylesheet = ADD_USER;
                        
                        // search for users and group
                    }else if(rd.getParameter(SEARCH_BUTTON) != null){
                        
                        strXML.append( doSearchAction() );
                        this.strStylesheet = SEARCH_ACTION;
                        
                        // no specific actions caught, which means display a user's details
                    }else {
                        
                        strXML.append( doViewUserAction() );
                        this.strStylesheet=VIEW_USER;
                        
                    }
                    
                    // catch actions on groups
                }else if(rd.getParameter("action").equals(GROUP_ACTION)){
                    
                    // save group details
                    if(rd.getParameter(SAVE_BUTTON)!=null){
                        
                        // check for permission to edit
                        if ( !authToken.hasActivity( ORG_CHART_EDIT  ) ){
                            
                            strXML.append( doSecurityError() );
                            this.strStylesheet = SECURITY_ERROR;
                            return;
                        }
                        
                        
                        doSaveGroupAction();
                        strXML.append( doViewGroupAction() );
                        this.strStylesheet = VIEW_GROUP;
                        
                        // save group details
                    }else if(rd.getParameter(ADD_BUTTON)!=null){
                        
                        // check for permission to edit
                        if ( !authToken.hasActivity( ORG_CHART_EDIT  ) ){
                            
                            strXML.append( doSecurityError() );
                            this.strStylesheet = SECURITY_ERROR;
                            return;
                        }
                        
                        
                        strXML.append( doAddGroupAction() );
                        this.strStylesheet = VIEW_GROUP;
                        
                        // add sub group
                    }else if(rd.getParameter(ADD_GROUP)!=null){
                        
                        // check for permission to edit
                        if ( !authToken.hasActivity( ORG_CHART_EDIT  ) ){
                            
                            strXML.append( doSecurityError() );
                            this.strStylesheet = SECURITY_ERROR;
                            return;
                        }
                        
                        strXML.append( doAddGroupAction() );
                        this.strStylesheet = ADD_GROUP;
                        
                        // add group
                    }else if(rd.getParameter(DELETE_GROUP)!=null){
                        
                        if ( !authToken.hasActivity( ORG_CHART_EDIT  ) ){
                            
                            strXML.append( doSecurityError() );
                            this.strStylesheet = SECURITY_ERROR;
                            return;
                        }
                        
                        strXML.append( doDeleteGroupAction() );
                        this.strStylesheet = DELETE_GROUP;
                        
                        // search for user and groups
                    }else if(rd.getParameter(SEARCH_BUTTON) != null){
                        
                        strXML.append( doSearchAction() );
                        this.strStylesheet = SEARCH_ACTION;
                        
                        // no actions, display group details
                    }else{
                        
                        strXML.append( doViewGroupAction() );
                        this.strStylesheet = VIEW_GROUP;
                        
                    }
                    
                    // invalid action perform a error message, NOT YET IMPLEMENTED
                }else{
                    
                    
                }
                
                // save current information for future use
                strLastXML = new StringBuffer(strXML.toString());
                strLastStylesheet = strStylesheet;
                
                // enter channel for the firt time or just do a tree action
            }else{
                
                // first time entry
                if(strLastXML==null){
                    
                    // display a so-called DEFAULT view:
                    strStylesheet = SEARCH_ACTION;
                    strXML.append( doSearchAction() );
                    
                    // doing a tree action
                }else{
                    
                    // get the last page info and display
                    strXML = new StringBuffer(strLastXML.toString());
                    strStylesheet = strLastStylesheet;
                }
                
                
            } // nothing else
            
            // display tree, always
            DefaultMutableTreeNode myTree = OrgChart.buildOrgTree ( hsExpanded, GROUP_ACTION, USER_ACTION );
            strXML.append( OrgChart.toXML( myTree, hsExpanded ) );
            
            
                // clear the lock to re-lock
            if( lock != null && lock.isValid() )
                lock.unlock();
            lock = null;
                  
            
            lockTree(myTree,  hsExpanded);
            lock.lockDelayWrite();
            
            
            strXML.append("</body>");
            
            // end of XML
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
            
        }
        
    }
    
    /**
     *	Method to display security error message
     * @return XML String contais details
     *
     **/
    
    private String doSecurityError(){
        
        return "<dummy></dummy></body>";
        
    }
    
    /**
     *    Method to save group details
     * 	  error message will be passed through channel runtime data, param "error"
     */
    
    private void doSaveGroupAction(){
        
        // get the group key
        String groupId=runtimeData.getParameter("ORGGROUP_intOrgGroupKey");
        
        
        
        // continue processing if a valid group is enter
        if( groupId != null ){
            
            try{
                // error string, if caught
                String strErr;
                
                // setup all query basis
                Vector formFields = DatabaseSchema.getFormFields("orggroup_view");
                DALQuery query = new DALQuery();
                
                query.setDomain( "ORGGROUP",null,null,null );
                query.setFields( formFields,runtimeData );
                
                // ****** this will update group details, not the USER - GROUP relations *****
                // validating data
                if( (strErr=QueryChannel.validateFields(formFields,runtimeData) ) == null ){
                    
                    // validating data succeeded
                    query.setWhere(null,0,"ORGGROUP_intOrgGroupKey","=",  groupId ,0,DALQuery.WHERE_HAS_VALUE);
                    
                     if( lock != null ) lock.unlock();
                    lock = null;
                    lock = new LockRequest( authToken );
                    lock.addLock( "ORGGROUP", groupId, LockRecord.READ_WRITE );
                    lock.lockDelayWrite();
             
                    
                    // check for locking error
                    if(lock.isValid() && lock.lockWrites()){
                        if(query.executeUpdate()){
                            
                            runtimeData.setParameter("error", "Details updated.");
                            
                        }else{
                            // catch an unexpected database error
                            runtimeData.setParameter("error", "An unknown error has occured. Please try again later.");
                            
                        }
                        // unlock when done
                        lock.unlockWrites();
                        
                    }else{
                        // it's locked
                        runtimeData.setParameter("error",LOCK_ERROR);
                    }
                    
                }else{
                    
                    runtimeData.setParameter("error", strErr);
                }
                
                // ***** if updating details succeeded, update USER - GROUP relations *****
                
                if( runtimeData.getParameter("error").equals("Details updated.") ){
                    
                    String[] groupUsers = runtimeData.getParameterValues("groupUsers");
                    String[] allUsers = runtimeData.getParameterValues("allUsers");
                    int i;
                    
                    
                    // setup all the query basis
                    query.reset();
                    DALQuery sQuery = new DALQuery();
                    
                    // first, "remove" all users in group
                    query.setDomain("ORGUSERGROUP",null,null,null);
                    query.setField("ORGUSERGROUP_intDeleted","1");
                    query.setWhere(null, 0, "ORGUSERGROUP_intOrgGroupKey", "=", runtimeData.getParameter("ORGGROUP_intOrgGroupKey"), 0,
                    DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                    
                    // boolean to check if lock is ok to continue
                    boolean go = true;
                    
                    //check for lock is ok
                    if(lock.isValid() && lock.lockWrites()){
                        
                        query.executeUpdate();
                        // unlock when done
                        lock.unlockWrites();
                        
                        
                    }else{
                        
                        runtimeData.setParameter("error",LOCK_ERROR);
                        go=false;
                        return ;
                    }
                    
                    // check if everything is ok so far to continue
                    if(( runtimeData.getParameterValues("groupUsers")!=null ) && go ){
                        
                        // then, add each user into group
                        for( i = 0; i < groupUsers.length; i++){
                            
                            query.reset();
                            query.setDomain("ORGUSERGROUP",null,null,null);
                            query.setField("ORGUSERGROUP_intDeleted","0");
                            query.setWhere(null, 0, "ORGUSERGROUP_intOrgGroupKey", "=", groupId, 0,
                            DALQuery.WHERE_HAS_VALUE);
                            query.setWhere("AND", 0, "ORGUSERGROUP_intOrgUserKey" ,  "=",  groupUsers[i], 0, DALQuery.WHERE_HAS_VALUE);
                            
                            ResultSet rs = query.executeSelect();
                            
                            // check if it existed, do an "undelete"
                            if(rs.next()){
                                
                                query.executeUpdate();
                                
                            }else{
                                
                                // try to insert it
                                sQuery.reset();
                                sQuery.setDomain("ORGUSERGROUP",null,null,null);
                                formFields = DatabaseSchema.getFormFields("orgusergroup_view");
                                
                                
                                runtimeData.setParameter("ORGUSERGROUP_intOrgGroupKey",runtimeData.getParameter("ORGGROUP_intOrgGroupKey"));
                                runtimeData.setParameter("ORGUSERGROUP_intOrgUserKey",groupUsers[i]);
                                
                                sQuery.setFields(formFields, runtimeData);
                                
                                // any unexpected error
                                sQuery.executeInsert();
                                
                                
                            }
                            
                        }
                        runtimeData.setParameter("error", "Details updated.");
                        
                    }
                }
                
            }
            catch (Exception e) {
                
                e.printStackTrace();
                LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
                
            }
            
        }
    }
    
    
    /**
     *    Method to display user details
     * @return XML string contains all user details and errors, if any
     */
    
    private String doViewGroupAction(){
        
        StringBuffer strXML = new StringBuffer();
        
        strXML.append("<info>");
        
        // get usergroup key
        if(runtimeData.getParameter("ORGGROUP_intOrgGroupKey")!=null){
            
            String groupId= runtimeData.getParameter("ORGGROUP_intOrgGroupKey");
            
            // set current group for locking WRITE
            currentGroup = groupId;
            
            // try to catch all the exceptions
            try{
                
                // display error or message from other processes, such as saving group error
                if(runtimeData.getParameter("error")!=null){
                    
                    strXML.append("<error>" + runtimeData.getParameter("error") + "</error>");
                    
                }
                
                // setup all query basis
                Vector vtFormFields = DatabaseSchema.getFormFields("orggroup_view");
                DALQuery query = new DALQuery();
                
                // gather group details
                query.setDomain( "ORGGROUP",null,null,null );
                query.setFields( vtFormFields,null );
                
                query.setWhere( null,0,"ORGGROUP_intOrgGroupKey","=",groupId ,0,DALQuery.WHERE_HAS_VALUE );
                
                ResultSet rs = query.executeSelect();
                
                strXML.append( "<groupDetails>" );
                // display group details
                strXML.append( QueryChannel.buildSearchXMLFile( "details", rs, vtFormFields ));
                strXML.append( QueryChannel.buildFormLabelXMLFile( vtFormFields ));
                
                // set up user for displaying supervisors
                query.reset();
                vtFormFields = DatabaseSchema.getFormFields("orguser_view");
                query.setFields( vtFormFields ,null );
                query.setWhere( null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setDomain("ORGUSER",null,null,null);
                query.setOrderBy( "ORGUSER_strFirstName","ASC" );
                
                // get info from the query
                rs = null;
                rs = query.executeSelect();
                
                strXML.append("<supervisor>");
                strXML.append( QueryChannel.buildSearchXMLFile( "user", rs, vtFormFields ) );
                strXML.append("</supervisor>");
                
                
                // get users in the group
                query.reset();
                query.setDomain("ORGUSERGROUP",null,null,null);
                query.setDomain("ORGUSER","ORGUSER_intOrgUserKey","ORGUSERGROUP_intOrgUserKey","INNER JOIN");
                
                vtFormFields = DatabaseSchema.getFormFields("orguser_view");
                query.setFields(vtFormFields, null);
                query.setWhere(null,0, "ORGUSERGROUP_intOrgGroupKey", "=", groupId,0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"ORGUSERGROUP_intDeleted","=", "0" ,0,DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("ORGUSER_strFirstName","ASC");
                
                rs = null;
                rs = query.executeSelect();
                
                strXML.append( "<groupUsers>" );
                strXML.append( QueryChannel.buildSearchXMLFile( "user", rs, vtFormFields ));
                strXML.append( "</groupUsers>" );
                
                // get all other users, which are not in group
                // this is the query, which uses the previous query as a sub query (the query that search for users in group)
                // so that we can use it in the NOT IN clause
                DALQuery sQuery = new DALQuery();
                sQuery.setDomain("ORGUSER",null,null,null);
                
                sQuery.setFields(vtFormFields, null);
                sQuery.setOrderBy("ORGUSER_strFirstName","ASC");
                
                query.clearFields();
                query.setField("ORGUSER_intOrgUserKey",null);
                sQuery.setWhere( null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                sQuery.setWhere("AND", 0, "ORGUSER_intOrgUserKey", "NOT IN", query, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                
                rs = sQuery.executeSelect();
                
                strXML.append( "<otherUsers>" );
                strXML.append( QueryChannel.buildSearchXMLFile( "user", rs, vtFormFields ));
                strXML.append( "</otherUsers>" );
                
                strXML.append( "</groupDetails>" );
            }
            catch (Exception e) {
                
                e.printStackTrace();
                LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
                
            }
            
        }
        strXML.append("</info>");
        return strXML.toString();
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
        if((groupId = runtimeData.getParameter("ORGGROUP_intOrgGroupKey"))!=null){
            try{
                
                // set up query basis
                DALQuery query = new DALQuery();
                DALQuery sQuery = new DALQuery();
                query.setDomain("ORGGROUP",null,null,null);
                Vector vtChild = new Vector();
                
                vtChild.add( groupId );
                
                query.setWhere(null, 0,"ORGGROUP_intOrgGroupKey", "=", groupId, 0, DALQuery.WHERE_HAS_VALUE);
                query.setField("ORGGROUP_intDeleted","1");
                
                if( lock != null ) lock.unlock();
                lock = null;
                lock = new LockRequest( authToken );
                lock.addLock( "ORGGROUP", groupId, LockRecord.READ_WRITE );
                lock.lockDelayWrite();
                
                // check for lock
                if( lock.isValid() && lock.lockWrites() ){
                    
                    query.executeUpdate();
                    
                    // delete subgroups, recursively
                    for(int i=0; i < vtChild.size(); i++){
                        
                        groupId = (String) vtChild.get(i);
                        query.clearWhere();
                        query.setWhere(null, 0,"ORGGROUP_intParentGroupKey", "=", groupId, 0, DALQuery.WHERE_HAS_VALUE);
                        query.setField("ORGGROUP_intOrgGroupKey",null);
                        
                        ResultSet rs = query.executeSelect();
                        while( rs.next() ){
                            
                            vtChild.add( rs.getString( "ORGGROUP_intOrgGroupKey" ) );
                            
                        }
                        
                        // delete group
                        query.clearFields();
                        query.setField("ORGGROUP_intDeleted","1");
                        query.executeUpdate();
                        
                        
                        // delete users in group
                        sQuery.reset();
                        
                        sQuery.setDomain("ORGUSERGROUP",null,null,null);
                        sQuery.setWhere(null, 0,"ORGUSERGROUP_intOrgGroupKey", "=", groupId, 0, DALQuery.WHERE_HAS_VALUE);
                        sQuery.setField("ORGUSERGROUP_intDeleted","1");
                        
                        sQuery.executeUpdate();
                        
                    }
                    
                    strErr = "Group deleted.";
                    lock.unlockWrites();
                    
                }else{
                    
                    strErr = LOCK_ERROR;
                    
                }
                
                
                
            }catch(Exception e){
                
                e.printStackTrace();
                LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
            }
        }else{
            
            strErr = "No group selected. Please select a group on the left";
        }
        
        strXML.append("<info><error>" + strErr + "</error></info>");
        return strXML.toString();
        
    }
    
    /**
     *    Method to add a sub group into group
     * @return XML string to display the proccess
     */
    
    private String doAddGroupAction(){
        
        StringBuffer strXML = new StringBuffer();
        strXML.append("<info>");
        
        try{
            // check if posted from a details form
            if( runtimeData.getParameter( ADD_BUTTON )!=null ){
                
                // query basis
                Vector formFields = new Vector();
                formFields = DatabaseSchema.getFormFields("orggroup_view");
                
                String strErr;
                
                // validate data
                if((strErr=QueryChannel.validateFields(formFields, runtimeData))==null){
                    try{
                        DALQuery query=new DALQuery();
                        
                        // check if same name exists in parent group
                        query.setDomain("ORGGROUP",null,null,null);
                        query.setFields(formFields,null);
                        
                        query.setWhere(null,0,"ORGGROUP_strOrgGroupName","=", runtimeData.getParameter("ORGGROUP_strOrgGroupName"),
                        0, DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND",0,"ORGGROUP_intParentGroupKey","=", runtimeData.getParameter("ORGGROUP_intParentGroupKey"),
                        0, DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND",0,"ORGGROUP_intDeleted","=", "0",
                        0, DALQuery.WHERE_HAS_VALUE);
                        
                        ResultSet rs = query.executeSelect();
                        
                        // if not
                        if(!rs.next()){
                            
                            query.reset();
                            query.setDomain( "ORGGROUP",null,null,null );
                            query.setFields( formFields,runtimeData );
                            
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
                        
                        
                    }catch(Exception e){
                        
                        e.printStackTrace();
                        
                        LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
                    }
                    
                    // validate fails
                }else{
                    
                    strXML.append("<error>"+ strErr +"</error>");
                }
                
                // output them
                
            }else{
                
                
                String parentId;
                
                // check for group to be added in
                if((parentId = runtimeData.getParameter("ORGGROUP_intOrgGroupKey"))!=null){
                    
                    strXML.append( "<form>" );
                    // build the form
                    Vector formFields = new Vector();
                    formFields = DatabaseSchema.getFormFields("orggroup_view");
                    
                    strXML.append("<ORGGROUP_intParentGroupKey>" + parentId + "</ORGGROUP_intParentGroupKey>" );
                    strXML.append( QueryChannel.buildFormLabelXMLFile( formFields ) );
                    
                    // details part
                    strXML.append( "<details>" );
                    strXML.append( QueryChannel.buildAddFormXMLFile( formFields ) );
                    strXML.append( "</details>" );
                    
                    
                    DALQuery query = new DALQuery();
                    query.setDomain( "ORGUSER", null, null, null );
                    query.setWhere( null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    formFields = DatabaseSchema.getFormFields( "orguser_view" );
                    query.setFields( formFields, null );
                    ResultSet rs = query.executeSelect();
                    
                    // supervisor part
                    strXML.append( "<supervisor>" );
                    strXML.append( QueryChannel.buildSearchXMLFile( "user", rs, formFields ));
                    strXML.append( "</supervisor>" );
                    strXML.append( "</form>" );
                    
                }else{
                    strXML.append("<error>No group selected. Please select a group on the left.</error>");
                }
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
        }
        
        strXML.append("</info>");
        
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
        if(runtimeData.getParameter("ORGUSER_intOrgUserKey")!=null){
            try{
                
                // query basis
                DALQuery query = new DALQuery();
                
                query.setDomain("ORGUSER",null,null,null);
                query.setWhere(null, 0,"ORGUSER_intOrgUserKey", "=", runtimeData.getParameter("ORGUSER_intOrgUserKey"), 0, DALQuery.WHERE_HAS_VALUE);
                query.setField("ORGUSER_intDeleted", "1");
                
                if( lock != null ) lock.unlock();
                lock = null;
                lock = new LockRequest( authToken );
                lock.addLock( "ORGUSER", runtimeData.getParameter("ORGUSER_intOrgUserKey"), LockRecord.READ_WRITE );
                lock.lockDelayWrite();
                
                if(lock.isValid() && lock.lockWrites()){
                    
                    // delete it
                    if(query.executeUpdate()){
                        
                        query.reset();
                        
                        query.setDomain("ORGUSERGROUP",null,null,null);
                        query.setWhere(null, 0,"ORGUSERGROUP_intOrgUserKey", "=", runtimeData.getParameter("ORGUSER_intOrgUserKey"), 0, DALQuery.WHERE_HAS_VALUE);
                        query.setField("ORGUSERGROUP_intDeleted","1");
                        
                        query.executeUpdate();
                        
                        strErr = "User deleted.";
                        
                    }else{
                        
                        strErr = "An error has occured. Please try again later.";
                    }
                    
                    // unlock writes when done
                    lock.unlockWrites();
                }else{
                    
                    strErr = LOCK_ERROR;
                }
                
            }catch(Exception e){
                e.printStackTrace();
                LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
            }
            
        }else{
            
            strErr = "No user selected. Please select a user from the left.";
            
        }
        
        strXML.append("<info><error>" + strErr + "</error></info>");
        
        return strXML.toString();
    }
    
    /**
     *	Method to search for users and groups
     *	@return XML String for displaying info
     *
     **/
    
    private String doSearchAction() {
        
        StringBuffer strXML = new StringBuffer();
        
        try{
            
            strXML.append("<info><searchDetails>");
            
            // checking if something is posted
            if( (runtimeData.getParameter("search_button") != null) && (runtimeData.getParameter( "search_string") != null) ){
                
                // query basis
                DALQuery query = new DALQuery();
                Vector formFields = DatabaseSchema.getFormFields( "orguser_view" );
                ResultSet rs = null;
                
                
                // ***** search for user ***
                query.setDomain( "ORGUSER", null, null, null );
                query.setFields( formFields, null );
                
                // posted data
                String str = runtimeData.getParameter("search_string");
                String job = runtimeData.getParameter("job_title");
                String emp = runtimeData.getParameter("employment_type");
                
                if( job==null ) job = "";
                
                // pass data back to page
                strXML.append("<searchString>" + str + "</searchString>");
                strXML.append("<jobTitle>" + job + "</jobTitle>");
                
                // set where conditions
                query.setWhere(null,1,"ORGUSER_strFirstName","LIKE",str,0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("OR",0,"ORGUSER_strLastName","LIKE",str,1,DALQuery.WHERE_HAS_VALUE);
                
                // if they require job check
                if( (job!=null) && (!job.equals("")) )
                    query.setWhere("AND",0,"ORGUSER_strTitle","LIKE",job,0,DALQuery.WHERE_HAS_VALUE);
                
                // empoyment type check
                if( (emp!=null) && (!emp.equals("")) )
                    query.setWhere("AND",0,"ORGUSER_strEmploymentType","=",emp,0,DALQuery.WHERE_HAS_VALUE);
                
                // check for deleted, always
                query.setWhere("AND",0,"ORGUSER_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                rs = query.executeSelect();
                
                // build xml file
                strXML.append( QueryChannel.buildSearchXMLFile( "user", rs, formFields ));
                
                // ***** search for groups ******
                query.reset();
                query.setDomain( "ORGGROUP", null, null, null );
                formFields = DatabaseSchema.getFormFields( "orggroup_view" );
                query.setFields( formFields, null ) ;
                
                // execute select
                query.setWhere(null,0,"ORGGROUP_strOrgGroupName","LIKE",str,0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"ORGGROUP_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                
                rs = query.executeSelect();
                
                strXML.append( QueryChannel.buildSearchXMLFile( "group", rs, formFields ));
            }
            
            strXML.append( QueryChannel.buildLOVXMLFile( "ORGUSER_strEmploymentType",""));
            strXML.append( "</searchDetails></info>");
            
            
        }catch(Exception e){
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
        }
        
        return strXML.toString();
        
    }
    
    
    
    /**
     *    Method to add users
     * @return XML string to display process
     */
    
    private String doAddUserAction(){
        
        StringBuffer strXML = new StringBuffer();
        String strErr = null;
        strXML.append("<info>");
        
        try{
            // check if posted from an add user form
            if(runtimeData.getParameter( ADD_BUTTON )!=null){
                
                // query basis
                Vector formFields = new Vector();
                formFields = DatabaseSchema.getFormFields("orguser_view");
                
                success = false;
                
                // validate data
                if((strErr=QueryChannel.validateFields(formFields, runtimeData))==null){
                    try{
                        
                        DALQuery query=new DALQuery();
                        
                        query.setDomain("ORGUSER",null,null,null);
                        
                        // Save file to server first, get the name
                        String fileName = QueryChannel.saveUploadFile( "ORGUSER_strPhoto", runtimeData, pictureLocation );
                        
                        if( fileName == null )
                            runtimeData.setParameter( "ORGUSER_strPhoto", "" );
                        else
                            runtimeData.setParameter( "ORGUSER_strPhoto", fileName );
                        
                        query.setFields(formFields,runtimeData);
                        
                        if(query.executeInsert()){
                            
                            
                            strErr = EMPLOYEE_ADDED;
                            success = true;
                            
                            
                        }else{
                            
                            strErr = "An error has occured. Please try again later.";
                            
                        }
                        
                        
                        
                    }catch(Exception e){
                        
                        e.printStackTrace();
                        
                        LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
                    }
                }
                
                
            }
            
            
            
            Vector formFields = new Vector();
            formFields = DatabaseSchema.getFormFields("orguser_view");
            
            // buid the report to part
            DALQuery query = new DALQuery();
            query.setDomain( "ORGUSER", null, null, null );
            query.setFields( formFields, null );
            query.setWhere( null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query.setOrderBy( "ORGUSER_strFirstName", "ASC" );
            ResultSet rs = query.executeSelect();
            
            if( strErr != null )
                strXML.append( "<error>" + strErr + "</error>" );
            
            strXML.append( "<userDetails>" );
            strXML.append( QueryChannel.buildFormLabelXMLFile( formFields ));
            strXML.append( QueryChannel.buildSearchXMLFile( "reportTo", rs, formFields ) );
            
            
            
            // if any error occurs, retain the posted data instead of blank ones
            if( (strErr != null ) && (!strErr.equals( EMPLOYEE_ADDED ))){
                
                    // retain the data
                    strXML.append( QueryChannel.buildViewXMLFile( formFields, runtimeData ));
               
            }else{
                
                strXML.append( QueryChannel.buildAddFormXMLFile( formFields ));
            }
            
            DALQuery subQuery = new DALQuery();
            subQuery.setDomain( "ORGUSER", null, null, null );
            subQuery.setField( "ORGUSER_strSystemUser", null );
            subQuery.setWhere( null, 0, "ORGUSER_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE );
            
            formFields = new Vector();
            formFields.add( "SYSTEMUSER_strUserName" );
            query.reset();
            query.setDomain("SYSTEMUSER", null, null, null);
            query.setField( "SYSTEMUSER_strUserName", null );
            query.setWhere( null, 0, "SYSTEMUSER_strUserName", "NOT IN", subQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY );
            query.setWhere( "AND", 0, "SYSTEMUSER_strUserName", "<>", "", 0, DALQuery.WHERE_HAS_VALUE );
            query.setOrderBy( "SYSTEMUSER_strUserName", "ASC" );
            rs = query.executeSelect();
            
            strXML.append( QueryChannel.buildSearchXMLFile( "systemUser", rs, formFields));
            
            strXML.append( "</userDetails>" );
            
            
            
            
            
            
        }catch(Exception e){
            
            e.printStackTrace();
            
            LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
        }
        
        strXML.append("</info>");
        
        return strXML.toString();
    }
    
    /**
     *    Method to save user details
     * 		Error is passed through param error of channel runtime data
     */
    
    private void doSaveUserAction(){
        
        String userKey=runtimeData.getParameter("ORGUSER_intOrgUserKey");
        
        if( userKey != null ){
            
            try{
                
                // query basis
                Vector formFields = DatabaseSchema.getFormFields("orguser_view");
                DALQuery query = new DALQuery();
                String strErr;
                
                // Save file to server first, get the name
                String fileName = QueryChannel.saveUploadFile( "ORGUSER_strPhoto", runtimeData, pictureLocation );
                
                if( fileName == null )
                    
                    // Check if the user retain the image or upload a new image
                    if( runtimeData.getParameter( "ORGUSER_strCurPhoto" ) !=null )
                        runtimeData.setParameter( "ORGUSER_strPhoto", runtimeData.getParameter( "ORGUSER_strCurPhoto" ) );
                    else
                        runtimeData.setParameter( "ORGUSER_strPhoto", "" );
                
                else{
                    
                    runtimeData.setParameter( "ORGUSER_strPhoto", fileName );
                    
                }
                
                query.setDomain("ORGUSER",null,null,null);
                query.setFields( formFields, runtimeData);
                
                
                // validate data
                if((strErr=QueryChannel.validateFields(formFields,runtimeData))==null){
                    
                    query.setWhere(null,0,"ORGUSER_intOrgUserKey","=", userKey ,0,DALQuery.WHERE_HAS_VALUE);
                    
                    if( lock != null ) lock.unlock();
                    lock = null;
                    lock = new LockRequest( authToken );
                    lock.addLock( "ORGUSER", userKey, LockRecord.READ_WRITE );
                    lock.lockDelayWrite();

                    
                    
                    if( lock.isValid() && lock.lockWrites()){
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
                
                LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
                
            }
            
        }
    }
    
    /**
     *    Method to display user details
     * @return XML string to display user details and error from other process
     * on users
     */
    
    private String doViewUserAction(){
        
        StringBuffer strXML = new StringBuffer();
        
        String userKey=runtimeData.getParameter("ORGUSER_intOrgUserKey");
        
        strXML.append("<info>");
        
        if( userKey != null ){
            
            // set current user for locking WRITE
            currentUser = userKey;
            
            try{
                
                // display message from other procces
                if(runtimeData.getParameter("error")!=null){
                    
                    strXML.append("<error>" + runtimeData.getParameter("error") + "</error>");
                }
                
                
                // query bais
                
                Vector formFields = DatabaseSchema.getFormFields("orguser_view");
                
                DALQuery query = new DALQuery();
                
                query.setDomain("ORGUSER",null,null,null);
                query.setFields(formFields,null);
                query.setWhere( null, 0, "ORGUSER_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE );
                query.setOrderBy( "ORGUSER_strFirstName", "ASC" );
                
                ResultSet rs = query.executeSelect();
                
                
                // display details
                strXML.append( "<userDetails>" );
                strXML.append( QueryChannel.buildFormLabelXMLFile( formFields ) );
                
                if( runtimeData.getParameter("error") != null ){
                    
                    strXML.append( QueryChannel.buildViewXMLFile( formFields, runtimeData ) );
                    
                }else{
                    
                    strXML.append( QueryChannel.buildViewFromKeyXMLFile( formFields, "ORGUSER_intOrgUserKey", Integer.parseInt( userKey ) ) );
                }
                
                strXML.append( QueryChannel.buildSearchXMLFile( "reportTo", rs, formFields ) );
                
                DALQuery subQuery = new DALQuery();
                subQuery.setDomain( "ORGUSER", null, null, null );
                subQuery.setField( "ORGUSER_strSystemUser", null );
                subQuery.setWhere( null, 0, "ORGUSER_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE );
                subQuery.setWhere( "AND", 0, "ORGUSER_intOrgUserKey","<>", userKey ,0,DALQuery.WHERE_HAS_VALUE );

                formFields = new Vector();
                formFields.add( "SYSTEMUSER_strUserName" );
                query.reset();
                query.setDomain("SYSTEMUSER", null, null, null);
                query.setField( "SYSTEMUSER_strUserName", null );
                query.setWhere( null, 0, "SYSTEMUSER_strUserName", "NOT IN", subQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY );
                query.setWhere( "AND", 0, "SYSTEMUSER_strUserName", "<>", "", 0, DALQuery.WHERE_HAS_VALUE );
                query.setOrderBy( "SYSTEMUSER_strUserName", "ASC" );
                
                rs = query.executeSelect();
            
         //       System.err.println( "Query: " + query.convertSelectQueryToString() );
                strXML.append( QueryChannel.buildSearchXMLFile( "systemUser", rs, formFields));
                
                strXML.append( "</userDetails>" );
                
                
            }
            
            catch (Exception e)
            
            {
                
                e.printStackTrace();
                LogService.instance().log(LogService.ERROR, "Unknown error in COrgChart Channel - " + e.toString());
                
            }
            
        }else{
            
            strXML.append("<error>No user selected. Please select one user from the left.</error>");
        }
        
        strXML.append("</info>");
        
        return strXML.toString();
    }
    
    
    /**
     *    Output channel content to the portal
     * @param out a sax document handler
     * @throws PortalException Portal Exception
     */
    
    public void renderXML(ContentHandler out) throws PortalException {
        //
        
        // Create a new XSLT styling engine
        XSLT xslt = new XSLT(this);
        
        
        // pass the result xml to the styling engine.
        //System.err.println( "String XML ---------> \n" + strXML.toString() );
        xslt.setXML(strXML.toString());
        
        
        // specify the stylesheet selector
        xslt.setXSL("COrgChart.ssl", strStylesheet, runtimeData.getBrowserInfo());
        
        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());
        
        // set the output Handler for the output.
        xslt.setTarget(out);
        
        // do the deed
        xslt.transform();
        
    }
    
   
    
    /**
     *
     * Lock visible records on the tree
     * @param tree The tree that has been built
     * @param hashExpanded Hashtable contains expanding information
     *
     */
    private void lockTree(DefaultMutableTreeNode tree, Hashtable hashExpanded){
        
        
        try{
            
            Hashtable lockedUsers = new Hashtable();
            Vector visible = new Vector();
            visible.add( tree );
            
            // search for visible node only, relies on hashExpanded info
            for( int i=0; i < visible.size(); i++ ){
                
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode)visible.get(i);
                Hashtable hNode = (Hashtable) cNode.getUserObject();
                
                //check if the current group is expanded, add its children to visible vector
                if( (hNode.get( "ORGGROUPTREE_intOrgGroupKey" ) != null) && (hashExpanded.containsKey( hNode.get("ORGGROUPTREE_intOrgGroupKey") ))){
                    
                    Enumeration child_enum = cNode.children();
                    while( child_enum.hasMoreElements() )
                        visible.add( child_enum.nextElement());
                    
                }
                
                
            }
            
            
            // create the lock now
            if( lock == null )
                lock = new LockRequest( authToken );
            
            
            
            // boolean to check the case that user view record from search result, not from the tree, since the method mainly lock what's
            // on the tree only
            boolean lockedUser = false;
            boolean lockedGroup = false;
            
       //     System.err.println( "VISIBEL_ ___________> : " + visible );
            
            for( int i = 0; i < visible.size(); i++ ){
                
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) visible.get(i);
                Hashtable hNode = (Hashtable) cNode.getUserObject();
                
                // if node is user, add in ORGUSER domain
                if( hNode.get( "url_action" ).equals( USER_ACTION )){
                    
                    // check if node is currently viewed
                    //if( currentUser.equals( hNode.get("ORGUSER_intOrgUserKey") )){
                        
                        //if( !lockedUsers.containsKey( hNode.get("ORGUSER_intOrgUserKey") )){
                            //lockedUser = true;
                            //lock.addLock( "ORGUSER", hNode.get("ORGUSER_intOrgUserKey").toString(), LockRecord.READ_WRITE );
                            
                        //}
                        
                    //}else{
                        //if( !lockedUsers.containsKey( hNode.get("ORGUSER_intOrgUserKey") ))
          //                  System.err.println( "CURRENT USER NODE --- > " + hNode );
                            lock.addLock( "ORGUSER", hNode.get("ORGUSERTREE_intOrgUserKey").toString(), LockRecord.READ_ONLY );
                        
                    //}
                    
                    //lockedUsers.put( hNode.get("ORGUSER_intOrgUserKey"), "" );
                    
                }else{
                    
                    // same logic as user's
                    //if( currentGroup.equals( hNode.get("ORGGROUP_intOrgGroupKey") )){
                        
                    //    lockedGroup = true;
                    //    lock.addLock( "ORGGROUP", hNode.get("ORGGROUP_intOrgGroupKey").toString(), LockRecord.READ_WRITE );
                    //}else{
          //              System.err.println( "CURRENT NODE --- > " + hNode );
                        lock.addLock( "ORGGROUP", hNode.get("ORGGROUPTREE_intOrgGroupKey").toString(), LockRecord.READ_ONLY );
                        
                   // }
                    
                    
                }
                
            }
            
            // Lock user record if not locked on the tree
            //if( !lockedUser && !currentUser.equals("-2")){
            //    lock.addLock( "ORGUSER", currentUser, LockRecord.READ_WRITE );
             
            //}
            
            // Lock cwgroupuser record if not locked on the tree
            //if( !lockedGroup && !currentGroup.equals("-2")){
              //  lock.addLock( "ORGGROUP", currentGroup, LockRecord.READ_WRITE );
             
            //}
            
        }catch (Exception e){
            
            LogService.instance().log(
            LogService.ERROR, "Unknown error in UserAdmin Channel - " + e.toString(), e);
            
        }
        
        
        
    }
    
}
