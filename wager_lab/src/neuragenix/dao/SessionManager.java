/**
 * SessionManager.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 09/09/2003
 */

package neuragenix.dao;

/**
 * Class to keep vital information such as channel's id of a session
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.ResultSet;
import java.io.*;

// uportal packages
import org.jasig.portal.services.LogService;

// neuragenix packages
import neuragenix.security.AuthToken;
import neuragenix.common.LockRequest;

public class SessionManager
{
    /** Default number of sessions
     */
    private static final int DEFAULT_NO_OF_SESSIONS = 100;
    
    /** Keep channel's ids of sessions. Each element of the hashtable
     *  store a hashtable of all registered channel's ids for a particular
     *  session
     */
    private static Hashtable hashSessionChannelIDs = new Hashtable(DEFAULT_NO_OF_SESSIONS);
    
    /** Keep lockrequest objects of sessions. Each element of the hashtable
     *  store a hashtable of all lockrequest objects for each channels
     */
    private static Hashtable hashSessionLockRequests = new Hashtable(DEFAULT_NO_OF_SESSIONS);
    
    private static  boolean found = false;
    
    /** Creates a new instance of SessionManager */
    public SessionManager() {
    }
    
    /** Add a new session
     *  @param strSessionUniqueID: unique ID of the session
     */
    public static void addSession(String strSessionUniqueID)
    {
        if (!hashSessionChannelIDs.containsKey(strSessionUniqueID))
        {
            hashSessionChannelIDs.put(strSessionUniqueID, new Hashtable(5));
        }
    }
    
    /** Add a new lockrequest session
     *  @param strSessionUniqueID: unique ID of the session
     */
    public static void addLockRequestSession(String strSessionUniqueID)
    {
        if (!hashSessionLockRequests.containsKey(strSessionUniqueID))
        {
            hashSessionLockRequests.put(strSessionUniqueID, new Hashtable(10));
        }
    }
    
    /** Add a new lockrequest object
     *  @param strSessionUniqueID: unique ID of the session
     *  @param strChannelFName: channel functional name
     *  @lockRequest: the lockRequest object
     */
    public static void addLockRequestObject(String strSessionUniqueID, String strChannelFName, LockRequest lockRequest)
    {
        if (!hashSessionLockRequests.containsKey(strSessionUniqueID))
        {
            hashSessionLockRequests.put(strSessionUniqueID, new Hashtable(10));
        }
        
        Hashtable hashLockRequests = (Hashtable) hashSessionLockRequests.get(strSessionUniqueID);
        if (!hashLockRequests.containsKey(strChannelFName))
        {
            hashLockRequests.put(strChannelFName, new Hashtable(5));
        }
        
        Hashtable hashChannelLockRequests = (Hashtable) hashLockRequests.get(strChannelFName);
        hashChannelLockRequests.put(lockRequest, "registered");
    }
    
    /** Unlock the lockrequest object
     *  @param strSessionUniqueID: unique ID of the session
     *  @param hashChannelsNotToClear: channels don't need to clear lock
     */
    public static void clearLockRequests(String strSessionUniqueID, Hashtable hashChannelsNotToClear)
    {
        Hashtable hashLockRequests = (Hashtable) hashSessionLockRequests.get(strSessionUniqueID);
        
        if (hashLockRequests != null)
        {
            Enumeration enumChannels = hashLockRequests.keys();

            while (enumChannels.hasMoreElements())
            {
                String strChannelFName = (String) enumChannels.nextElement();

                if (hashChannelsNotToClear != null && !hashChannelsNotToClear.containsKey(strChannelFName))
                {
                    Hashtable hashChannelLockRequests = (Hashtable) hashLockRequests.get(strChannelFName);
                    
                    if (hashChannelLockRequests != null)
                    {
                        Enumeration enumLockRequests = hashChannelLockRequests.keys();
                        while (enumLockRequests.hasMoreElements())
                        {
                            LockRequest lockRequestToClear = (LockRequest) enumLockRequests.nextElement();
                            
                            
                            try
                            {
                                if (lockRequestToClear != null)
                                    lockRequestToClear.unlock();
                            }
                            catch (Exception e)
                            {
                                LogService.instance().log(LogService.ERROR, "Unknown error in Session Manager - " + e.toString(), e);
                            }
                            finally
                            {
                                hashChannelLockRequests.remove(lockRequestToClear);
                                lockRequestToClear = null;
                            }
                        }
                    }
                }
            }
        }
    }
    
    /** Add a channel ID for a specified session
     */
    public static void addChannelID(String strSessionUniqueID, String strChannelName, String strChannelID)
    {
        if (hashSessionChannelIDs.containsKey(strSessionUniqueID))
        {
            Hashtable hashChannelIDs = (Hashtable) hashSessionChannelIDs.get(strSessionUniqueID);
            if (!hashChannelIDs.containsKey(strChannelName.toLowerCase()))
                hashChannelIDs.put(strChannelName.toLowerCase(), strChannelID);
         
        }
        else
        {
            addSession(strSessionUniqueID);
            addChannelID(strSessionUniqueID, strChannelName, strChannelID);
        }
    }
    
    /** Return the channel ID of a specified channel and session
     */
    public static String getChannelID(String strSessionUniqueID, String strChannelName)
    {
        if (hashSessionChannelIDs.containsKey(strSessionUniqueID))
        {
            Hashtable hashChannelIDs = (Hashtable) hashSessionChannelIDs.get(strSessionUniqueID);
            if( hashChannelIDs.get(strChannelName.toLowerCase()) != null )
            return (String) hashChannelIDs.get(strChannelName.toLowerCase());
        }
        
        return "";
    }
    /**
     * Method to look up the tab order which contains a specific channel
     *    
     *
     *  @param authToken user's AuthToken
     *  @param channelName name of channel to be located
     *  @return String the tab order.
     *
     **/
    
    public static String getTabOrder( AuthToken authToken, String channelName ){
        int tabOrder = 0;
        String strResult = "";
        
        try{
            
            String channelID = getChannelID( authToken.getSessionUniqueID(), channelName );
	    System.err.println("Got channel name: " + channelName);
            channelID = channelID.substring(1);
            
            String userID = null;
            DALQuery query = new DALQuery();
            Vector vtFormFields = new Vector();
            ResultSet rs;
            String currentChannel="0";
            
            // get the user ID, not NAME
            query.setDomain( "USERID", null, null, null );
            vtFormFields = DatabaseSchema.getFormFields( "view_user_id");
            query.setFields( vtFormFields, null );
            query.setWhere( null, 0, "USERID_strUserName", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE );
            
            rs = query.executeSelect();
            
            if( rs.next() ) {
                userID = rs.getString( "USERID_intUserID");
            }
            rs.close();
            
            // look for the first tab, which is after "footer" or "header"
            vtFormFields = DatabaseSchema.getFormFields( "view_layout_struct");
            
            query.reset();
            query.setDomain( "LAYOUTSTRUCT", null, null, null);
            
            query.setFields( vtFormFields, null );
            query.setWhere( null, 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "LAYOUTSTRUCT_strType","=","header",0, DALQuery.WHERE_HAS_VALUE );
            
            rs = query.executeSelect();
            
            // if it is after "header"
            if( rs.next() ){
                currentChannel = rs.getString( "LAYOUTSTRUCT_intNextStructID");
            }
            rs.close();
            
            // if it is not after "header", it must be after "footer" then
            if( !currentChannel.equals("0")){
                query.reset();
                query.setDomain( "LAYOUTSTRUCT", null, null, null);

                query.setFields( vtFormFields, null );
                query.setWhere( null, 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "LAYOUTSTRUCT_strType","=","footer",0, DALQuery.WHERE_HAS_VALUE );

                rs = query.executeSelect();

                if( rs.next() )
                    currentChannel = rs.getString( "LAYOUTSTRUCT_intNextStructID");
                
                rs.close();
                rs = null;

            }
            
            // reset the global variable "found"
            found = false;
            
            while( !found && !currentChannel.equals( "0" )){
                
                // increase the tab order
                tabOrder++;
                
                query.reset();
                query.setDomain( "LAYOUTSTRUCT", null, null, null);

                query.setFields( vtFormFields, null );
                query.setWhere( null, 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "LAYOUTSTRUCT_intStructID","=",currentChannel,0, DALQuery.WHERE_HAS_VALUE );
                
                rs = query.executeSelect();
                
                // look for the child's struct only, since at this level, we are travelling through the
                // tabs, which next struct ID will be the next tab, we don't want to check for next tab
                // at in this recursive method though
                if( rs.next() ){
                    currentChannel = rs.getString( "LAYOUTSTRUCT_intNextStructID");
                    checkStructID( userID, rs.getString( "LAYOUTSTRUCT_intChildStructID"), channelID );
                }else{
                    currentChannel = "0";
                }
                
                rs.close();
                rs = null;
            }
            
            strResult = new Integer( tabOrder ).toString();
        }catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in SessionManager - " + e.toString());
            
        }
        
        return strResult;
    }
    
    /** Method to check if the current struct holds the channel
     *
     *
     *  @param userID userID to check
     *  @param struckID structID to check
     *  @param channelID channelID to check
     *  @return boolean the result
     **/
    private static boolean checkStructID( String userID, String structID, String channelID ){
        
        DALQuery query = new DALQuery();
        Vector vtFormFields = new Vector();
        ResultSet rs;
        vtFormFields = DatabaseSchema.getFormFields( "view_layout_struct");
        try{
            
            // check for current struct
            query.setDomain( "LAYOUTSTRUCT", null, null, null);
            query.setFields( vtFormFields, null );
            query.setWhere( null, 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "LAYOUTSTRUCT_intStructID","=",structID,0, DALQuery.WHERE_HAS_VALUE );

            rs = query.executeSelect();

            if( rs.next() && !found){
                
                // if current structID = the node ID, then, we did it.
                if(rs.getString( "LAYOUTSTRUCT_intStructID").equals(channelID)){
                    found = true;
                    rs.close();
                    return true;
                }
                
                // check for the next struct ID, which can be another column
                if( !rs.getString( "LAYOUTSTRUCT_intNextStructID").equals("0"))
                    checkStructID( userID, rs.getString( "LAYOUTSTRUCT_intNextStructID"), channelID );
                // check for the child layout
                if( rs.getString( "LAYOUTSTRUCT_intChildStructID") != null)
                    checkStructID( userID, rs.getString( "LAYOUTSTRUCT_intChildStructID"), channelID );

            }
            
            rs.close();
            rs = null;
        }catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in SessionManager - " + e.toString());
            
        }
        
        
        return false;
    }
    
    /**
     * Method to look up the tab order which contains a specific channel
     *    
     *
     *  @param authToken user's AuthToken
     *  @param channelName name of channel to be located
     *  @return String the tab order.
     *
     **/
    
    public static Hashtable getSharedChannels( AuthToken authToken, String channelName ){
        int tabOrder = 0;
        Hashtable hashResult = null;
        hashResult = new Hashtable();
        try{
            
            String channelID = "";
            
            String userID = null;
            DALQuery query = new DALQuery();
            Vector vtFormFields = new Vector();
            ResultSet rs;
            String currentChannel="0";
            
            // get the user ID, not NAME
            query.reset();
            query.setDomain( "USERID", null, null, null );
            vtFormFields = DatabaseSchema.getFormFields( "view_user_id");
            query.setFields( vtFormFields, null );
            query.setWhere( null, 0, "USERID_strUserName", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE );
            
            rs = query.executeSelect();
            
            if( rs.next() )
                userID = rs.getString( "USERID_intUserID");
            
            rs.close();
            rs = null;
            
            query.reset();
            query.setField( "LAYOUTSTRUCT_intStructID" , null );
            query.setDomain( "CHANNEL", null, null, null );
            query.setDomain( "LAYOUTSTRUCT", "LAYOUTSTRUCT_intChannelID", "CHANNEL_intChannelID", "INNER JOIN" );
            query.setWhere( null, 0, "CHANNEL_strFunctionalName", "=", channelName, 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
            rs = query.executeSelect();
            
            if( rs.next() )
                channelID = rs.getString( "LAYOUTSTRUCT_intStructID");
            else
                return hashResult;
            
            rs.close();
            rs = null;
            
            Vector vtChannels = new Vector();
            // look for the struct id that has the channel, may be more than 1.
            vtFormFields = DatabaseSchema.getFormFields( "view_layout_struct");
            
            query.reset();
            query.setDomain( "LAYOUTSTRUCT", null, null, null);
            
            String nextChannelID = channelID;
            query.setFields( vtFormFields, null );
            
            while( ( nextChannelID != null ) && ( nextChannelID.length() > 0 ) && (!nextChannelID.equals( "0"))){
                
                query.clearWhere();
                query.setWhere( null, 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "LAYOUTSTRUCT_intStructID","=", nextChannelID ,0, DALQuery.WHERE_HAS_VALUE );
                
                rs = query.executeSelect();
                
                if( rs.next() ){
                    if( rs.getString( "LAYOUTSTRUCT_intChannelID" ) != null ){
                        vtChannels.add( nextChannelID );
                        nextChannelID = rs.getString( "LAYOUTSTRUCT_intNextStructID" );
                    }else{
                        nextChannelID = rs.getString( "LAYOUTSTRUCT_intChannelID" );
                    }
                }else{
                    nextChannelID = null;
                }
                
                rs.close();
                rs = null;
            }
            
            nextChannelID = channelID;
            
            while( ( nextChannelID != null ) && ( nextChannelID.length() > 0 ) && (!nextChannelID.equals( "0"))){
                
                
                query.clearWhere();
                query.setWhere( null, 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "LAYOUTSTRUCT_intNextStructID","=", nextChannelID ,0, DALQuery.WHERE_HAS_VALUE );
                
                rs = query.executeSelect();
                
                if( rs.next() ){
                    if( rs.getString( "LAYOUTSTRUCT_intChannelID" ) != null ){
                        nextChannelID = rs.getString( "LAYOUTSTRUCT_intStructID" );
                        vtChannels.add( nextChannelID );
                    }else{
                        nextChannelID = rs.getString( "LAYOUTSTRUCT_intChannelID" );
                    }
                }else{
                    nextChannelID = null;
                }
                
                rs.close();
                rs = null;
            }
            
            query.reset();
            query.setField( "CHANNEL_strFunctionalName" , null );
            query.setDomain( "CHANNEL", null, null, null );
            query.setDomain( "LAYOUTSTRUCT", "LAYOUTSTRUCT_intChannelID", "CHANNEL_intChannelID", "INNER JOIN" );
            
            for( int i = 0; i < vtChannels.size(); i ++ ){
                
                query.clearWhere();
                query.setWhere( null, 0, "LAYOUTSTRUCT_intStructID", "=", vtChannels.get( i ), 0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "LAYOUTSTRUCT_intUserID","=",userID,0, DALQuery.WHERE_HAS_VALUE );
                rs = null;

                rs = query.executeSelect();
                if( rs.next() ){
                    hashResult.put( rs.getString( "CHANNEL_strFunctionalName"), "yes");
                }
                
                rs.close();
                rs = null;
                
            }
            
            
        }catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in SessionManager - " + e.toString());
            
        }
        
        return new Hashtable( hashResult );
    }

}
