package neuragenix.genix.docmanipulation; 


 import org.jasig.portal.IChannel;
 import org.jasig.portal.ChannelStaticData;
 import org.jasig.portal.ChannelRuntimeData;
 import org.jasig.portal.ChannelRuntimeProperties;
 import org.jasig.portal.PortalEvent; 
 import org.jasig.portal.PortalException;
 import org.jasig.portal.utils.XSLT; 
import org.xml.sax.ContentHandler;
import org.jasig.portal.security.IPerson;
 import org.jasig.portal.services.LogService;
 
 import neuragenix.security.AuthToken;
 import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import neuragenix.dao.SessionManager;
import neuragenix.utils.RuntimeDataStore;

 public class CDocManipulationEx implements IChannel
 {
     private static final int NORMAL_MODE = 42;
 // two of my 
    private static final int ABOUT_MODE = 31337; // favorite numbers
     private int mode; // whether we're in NORMAL_MODE or ABOUT_MODE 
    private ChannelStaticData staticData;
     private ChannelRuntimeData runtimeData; 
    private String name; // the name to say hello to 
    private String name_prev; // the name that was previously submitted, to go // in the text box by default.
    private String address;
    private String address_prev;
    private String strSessionUniqueID;
    private AuthToken authToken ;
    private IPerson ip;
    
     /** * Construct0r. */ 
    public CDocManipulationEx() 
    {
         this.name = "World";
         // default to "Hello World!" 
        this.name_prev = ""; 
        this.address = "";
        this.address_prev="";
        // start with the text box empty 
        this.mode = NORMAL_MODE; 
        // start in normal mode
     }
     // // Implementing the IChannel Interface //
     /** * Returns channel runtime properties. * Satisfies implementation of Channel Interface. * * @return handle to runtime properties */ 
        public ChannelRuntimeProperties getRuntimeProperties() 
    {
        return new ChannelRuntimeProperties(); 
    } /** * Process layout-level events coming from the portal. * Satisfies implementation of IChannel Interface. * * @param PortalEvent ev a portal layout event */
    public void receiveEvent(PortalEvent ev) 
    {
        if (ev.getEventNumber() == PortalEvent.ABOUT_BUTTON_EVENT) 
        {
            mode = ABOUT_MODE; 
        }
    }
    /** * Receive static channel data from the portal. * Satisfies implementation of IChannel Interface. * * @param ChannelStaticData sd static channel data */
    public void setStaticData(ChannelStaticData sd) 
    {
        this.staticData = sd; 
/*
 *@author rennypv
 *The IPerson object is created to be able to store the runtimedat in the IPerson object 
 *This avoids the use of static data 
 */        
        this.ip = sd.getPerson();
        this.authToken = (AuthToken)ip.getAttribute("AuthToken");
/*
    This bit of code is to get the channel-id's of different channels....required to interact with other channels
 */        
            Context globalIDContext = null;
            try
            {
                // Get the context that holds the global IDs for this user
                globalIDContext = (Context)staticData.getJNDIContext().lookup("/channel-ids");
            }
            catch (NotContextException nce)
            {
                LogService.log(LogService.ERROR, "Could not find subcontext /channel-ids in JNDI");
            }
            catch (NamingException e)
            {
                LogService.log(LogService.ERROR, e);
            }
/*
    This bit is to save the channel id of the TemplateUpdate channel to be able to connect to the TemplateUpdate channel from where the
 *document is created with the runtime data of this channel
 */            
           try 
            {
                strSessionUniqueID = authToken.getSessionUniqueID();
//                System.err.println("The sessionuniqueID is -----------" + strSessionUniqueID);
//               System.err.println("The globalIDContext----------" + (String) globalIDContext.lookup("TemplateUpdate"));                
                SessionManager.addSession(strSessionUniqueID);
                SessionManager.addChannelID(strSessionUniqueID, "TemplateUpdate",(String) globalIDContext.lookup("TemplateUpdate"));
 
//                System.err.println("The ChannelID is --------" + SessionManager.getChannelID(strSessionUniqueID,"TemplateUpdate"));
            } 
            catch (NotContextException nce)
            {
                LogService.log(LogService.ERROR, "Could not find channel ID for fname=Template Update");
            } 
            catch (NamingException e) 
            {
                LogService.log(LogService.ERROR, e);
            }

    }
    /** Receive channel runtime data from the portal. * Satisfies implementation of IChannel Interface. * * @param ChannelRuntimeData rd handle to channel runtime data
     *
     * calls runtimedata store and saves all the runtime data
     */ 
    public void setRuntimeData(ChannelRuntimeData rd) 
    {
        // Most of the processing is usually done here. 
        this.runtimeData = rd; 
        // process the form submissions
        if (runtimeData.getParameter("submit") != null) 
        {
            name = runtimeData.getParameter("name");
            address = runtimeData.getParameter("address");
            name_prev = name;
            address_prev = address;
            /*This bit of code is used for the template creation....to store the data in the hashtable sothat it can be available from the template channel
            The class required for this is neuragenix.utils.RuntimeDataStore
             *These lines of code are required to save the function name and session id and the runtime data to 
             */
            System.err.println("--------The runtime data store is updated---------");
            String sessionid = authToken.getSessionUniqueID();
            RuntimeDataStore rntmdtstore; //= new RuntimeDataStore();
/*
 *The IPerson being a session variable is used to save the runtimedata through a runtimedatastore object
 *It first checks for if the iperson already has an object of this kind and creates it only if it does not exists
 */            
            if(ip.getAttribute("rntimedtstore") == null)
            {
                ip.setAttribute("rntimedtstore",new RuntimeDataStore());
            }
            rntmdtstore = (RuntimeDataStore)ip.getAttribute("rntimedtstore");
            rntmdtstore.setChannelFunctionName("HelloWorld");
            rntmdtstore.setSessionID(sessionid);
            rntmdtstore.setRuntimeData(runtimeData);
        }
        else if (runtimeData.getParameter("clear") != null)
        {
            name_prev = ""; 
            address_prev = "";
        }
        else if (runtimeData.getParameter("back") != null) 
        {
            mode = NORMAL_MODE; 
        }
    }
    /** Output channel content to the portal * @param out a sax document handler */

  public void renderXML(ContentHandler out) throws PortalException {
    String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    String stylesheet = "normal";
    if (mode == NORMAL_MODE) {
        xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<helloworld><name>"+name+"</name>" + "<address>" + address + "</address></helloworld>";
        stylesheet = "normal";
    } else if (mode == ABOUT_MODE) {
        xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<about channel=\"Hello World\">This channel was created "
            + "for demonstrative purposes, by Owen Gunden <nog7g@"
            + "virginia.edu></about>";
        stylesheet = "about";
    }

    // Create a new XSLT styling engine
    XSLT xslt = new XSLT(this);

    org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL());
/*
 * This sets the channel URL of the template to the variable templateUpdateChannelURL which is available in the stylesheet
 */
    upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "TemplateUpdate"));
//    System.err.println( "node ID: --------------: " + SessionManager.getChannelID(strSessionUniqueID, "TemplateUpdate"));
    xslt.setStylesheetParameter("templateUpdateChannelURL", upfTmp.getUPFile());
//    System.err.println("templateUpdateChannelURL :--------" + upfTmp.getUPFile());
    // pass the result XML to the styling engine.
//    System.err.println("The xml is :................" + xml);
    xslt.setXML(xml);

    // specify the stylesheet selector
    xslt.setXSL("CDocManipulationEx.ssl", stylesheet, runtimeData.getBrowserInfo());

    // set parameters that the stylesheet needs.
    xslt.setStylesheetParameter("baseActionURL",
                                    runtimeData.getBaseActionURL());
    xslt.setStylesheetParameter("name_prev", name_prev);
    xslt.setStylesheetParameter("address_prev", address_prev);

    // set the output Handler for the output.
    xslt.setTarget(out);

    // do the deed
    xslt.transform();
  }

 }