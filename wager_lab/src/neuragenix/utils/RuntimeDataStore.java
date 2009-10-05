/*
 * RuntimeDataStore.java
 ** @author  renny
 * Created on March 2, 2004, 12:01 PM
 */

package neuragenix.utils;

import java.util.Hashtable;
import org.jasig.portal.ChannelRuntimeData;
/** This class is used as  a datastore to store the runtimedata against a
 * functionname and sessionid for the different channel
 */
public class RuntimeDataStore {
    /** This is the hashtable that store all the runtimedata details */    
    public Hashtable hshtblid = null;
    private Hashtable hshtblrntimedata = null;
    String function_name = null;
    String session_id = null;
    /** Creates a new instance of RuntimeDataStore */
    public RuntimeDataStore() 
    {
        if(hshtblid == null)
        {
            hshtblid = new Hashtable();
        }
    }
    /** sets the function name of the channel
     * @param functionname The function name of the channel for which the runtimedata is to be stored or
     * retrieved
     */    
    public void setChannelFunctionName(String functionname)
    {
        this.function_name = functionname;
    }
    /** sets the session id
     * @param sessionid The session id against which the functionname and the hashtable with the
     * runtimedata are stored
     */    
    public void setSessionID(String sessionid)
    {
        this.session_id = sessionid;
    }
    /** creates 2 hashtables. One with the session id as the key and the reference to
     * the other hastable as the value and the other hashtable has the function name
     * and the runtimedata
     * @param rntimedata The runtime data which is to be stored in the hashtable against a sessionid and
     * a function name
     */    
    public void setRuntimeData(ChannelRuntimeData rntimedata)
    {
        if(hshtblid.get(session_id) == null)
        {
            hshtblid.put(session_id, new Hashtable());
        }
        hshtblrntimedata = (Hashtable)hshtblid.get(session_id);
        hshtblrntimedata.put(function_name, rntimedata);
    }
    /** Returns the runtime data based on the session id and the function name
     * @return returns the runtimedata against the function name and the session id of the
     * object
     */    
    public Object getRuntimeData()
    {
        hshtblrntimedata = (Hashtable)hshtblid.get(session_id);
        return hshtblrntimedata.get(function_name);
    }
}
