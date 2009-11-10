/*
 * TemplateUpdate.java
 *
 * Created on February 27, 2004, 11:15 AM
 */

package neuragenix.utils;

import neuragenix.utils.DocManipulation;
import neuragenix.dao.DatabaseSchema;
import org.jasig.portal.ChannelRuntimeData;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.io.File;
import java.io.InputStream;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.Context;


import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XSLT;
import org.jasig.portal.security.IPerson;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;

import neuragenix.utils.ITemplateUpdate;
import neuragenix.utils.RuntimeDataStore;
import neuragenix.utils.DocDetails;
import neuragenix.dao.SessionManager;


import neuragenix.security.AuthToken;

import edu.columbia.ais.portal.cms.contentmanager.commands.CopyNode;

import org.xml.sax.ContentHandler;

import org.jasig.portal.IChannel;
/** Constructor
 * @author renny
 */
public class CTemplateUpdate implements ITemplateUpdate,IChannel
{
    boolean flag =false;
    String template_name ="";
    String store_url =null;
    String function_name = null;
    /** Creates a new instance of TemplateUpdate */
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    AuthToken authToken;
    private DocDetails doc_details;
    private boolean paramexists = false;
    private IPerson ip;
    private String strSessionUniqueID;
    private String copied = null;
    ChannelRuntimeData rntimedtstore;
    private String title = "";
    private String description = "";
    private String keywords = "";
    /** create an instance of the class */    
    public CTemplateUpdate() 
    {
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
  
  /** The authToken and the static data are set
   * @param sd Channel static data
   */  
  public void setStaticData(ChannelStaticData sd) 
  {
      ip = sd.getPerson();
    this.authToken = (AuthToken)ip.getAttribute("AuthToken");
    this.staticData = sd;
    
    InputStream file = CTemplateUpdate.class.getResourceAsStream("TemplateDBSchema.xml");
    DatabaseSchema.loadDomains(file, "TemplateDBSchema.xml");
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
    try 
    {
        strSessionUniqueID = authToken.getSessionUniqueID();
        SessionManager.addSession(strSessionUniqueID);
        SessionManager.addChannelID(strSessionUniqueID, "CDownload",
                                            (String) globalIDContext.lookup("CDownload"));

    }
    catch (NotContextException nce)
    {
        LogService.log(LogService.ERROR, "Could not find channel ID for fname=CDownload");
    } 
    catch (NamingException e) 
    {
        LogService.log(LogService.ERROR, e);
    }
  }
  
  /** This receives the runtime data....
   * creates a new instance of the runtimedatastore from where the runtime data of the
   * other channels are obtained.
   * All additional data in the runtime data is also saved on to the runtimedatastore
   *
   * @param rd the runtime data of the object
   */  
  public void setRuntimeData(ChannelRuntimeData rd) 
  {
    // Most of the processing is usually done here.
    this.runtimeData = rd;
    this.copied = null;
    this.title = "";
    this.description="";
    this.keywords="";
    if(ip.getAttribute("rntimedatastore") != null)
    {
        rntimedtstore = (ChannelRuntimeData)ip.getAttribute("rntimedatastore");
    }
    else
    {
        rntimedtstore = new ChannelRuntimeData();    
    }
     if(runtimeData.getParameter("templateName") != null)
    {
        function_name = runtimeData.getParameter("functionName");
        template_name = runtimeData.getParameter("templateName");
        setRuntimeDataValues(rntimedtstore);   
        this.runtimeData.setParameter("template",template_name);
        updateTemplate();
        if(doc_details != null)
        {
            this.runtimeData.setParameter("link",doc_details.getLink());
            if(doc_details.getVector() != null)
            {
                Vector vnovalues = doc_details.getVector();
                for(Enumeration etemp = vnovalues.elements();etemp.hasMoreElements();)
                {
                    String key = (String)etemp.nextElement();
                    if(runtimeData.getParameter(key) == null)
                    {
                        runtimeData.setParameter("param_"+key,"");
                    }
                }
            }
        }

        rntimedtstore = this.runtimeData; 
        ip.setAttribute("rntimedatastore",this.runtimeData);
    }
/*
   * if add button is clicked then the parameter and the value are saved in the
   * runtimedatastore
 */    
        if (runtimeData.getParameter("addvalues") != null) 
      {
            if(rntimedtstore.getParameter("templateName")!= null)
            {
                setRuntimeDataValues(rntimedtstore);                 
                if(!((String)(runtimeData.getParameter("parametername"))).equals("") & !((String)(runtimeData.getParameter("parametervalue"))).equals(""))
                {
                    setRuntimeParams((String)runtimeData.getParameter("parametername"),(String)runtimeData.getParameter("parametervalue"));
                    runtimeData.remove("addvalues");
                    ip.setAttribute("rntimedatastore",this.runtimeData);
                }
            }
            else
            {
//                System.err.println("--------------!!!!!!!!! No Function Name !!!!!!!!!----------------");
            }
      }
// To add the file to the repository
    if(runtimeData.getParameter("addfile") != null)
    {

       setRuntimeDataValues(rntimedtstore);
        String filename = PropertiesManager.getProperty("neuragenix.genix.templates.StoreDocumentLocation") + "/"+ runtimeData.getParameter("filename");        
        File f = new File(filename);
        CopyNode cpnode = new CopyNode();
//        String project = "/projects/BioHelp.xml";
//        String toPath = "/Biospecimen/*";
        String project = runtimeData.getParameter("cmsProject");
        String toPath = runtimeData.getParameter("cmsPath");
        runtimeData.setParameter("strDocExternalName",runtimeData.getParameter("newfilename"));
        copied = cpnode.copyFile(this.staticData,this.runtimeData,project,f, toPath, runtimeData.getParameter("newfilename"));
        this.runtimeData.remove("addfile");
        this.title = runtimeData.getParameter("title");
        this.description = runtimeData.getParameter("description");
        this.keywords = runtimeData.getParameter("keywords");
        ip.setAttribute("rntimedatastore",this.runtimeData);
    }
    if(runtimeData.getParameter("finished") != null)
    {
        runtimeData.clear();
    }
/*
    * if edit button is clicked then it first checks for if the parameter name is the
   * same as the original...if not then the parameter and value are removed and the
   * new ones added otherwise the parametervalue is just replaced with the new one
   *
 */    
    if(runtimeData.getParameter("editparam") != null)
    {
        String paramname = runtimeData.getParameter("paramname");
        if(rntimedtstore.getParameter("templateName")!= null)
        {
            setRuntimeDataValues(rntimedtstore);                             
            if(!paramname.equals((String)runtimeData.getParameter("change_name")))
            {
                removeRuntimeParams("param_"+ runtimeData.getParameter("paramname"));
            }
           setRuntimeParams(runtimeData.getParameter("change_name"),runtimeData.getParameter("change_value"));            
           runtimeData.remove("paramname");
           runtimeData.remove("editparam");
           runtimeData.remove("change_name");
           runtimeData.remove("change_value");           
           ip.setAttribute("rntimedatastore",this.runtimeData);
           
        }
        else
        {
//               System.err.println("--------------!!!!!!!!! No Function Name !!!!!!!!!----------------");        
        }
    }
/*
    * if delete is clicked then the parameter name and value are removed
 */    
    if(runtimeData.getParameter("deleteparam") != null)
    {
        String paramname = runtimeData.getParameter("paramname");
        this.runtimeData = prepareRuntimeData(this.runtimeData);
        if(rntimedtstore.getParameter("templateName")!= null)
        {
            setRuntimeDataValues(rntimedtstore);  
            paramname = "param_"+ paramname;
           this.runtimeData.remove(paramname);  
           this.runtimeData.remove("paramname");
           runtimeData.remove("deleteparam");
/*           runtimeData.remove("param_"+paramname);
           runtimeData.remove("value_"+ paramname);           */
           ip.setAttribute("rntimedatastore",this.runtimeData);
        }
        else
        {
//               System.err.println("--------------!!!!!!!!! No Function Name !!!!!!!!!----------------");        
        }        
    }
/*
    * if update doc is clicked then the file location is obtained from the link
   * parameter and the file is updated.
 */    
    if(runtimeData.getParameter("updatedoc") != null)
    {
        this.runtimeData = prepareRuntimeData(this.runtimeData);
        if(rntimedtstore.getParameter("templateName") != null)
        {
           setRuntimeDataValues(rntimedtstore);                            
           updateTemplate();
           this.runtimeData.remove("updatedoc");
           ip.setAttribute("rntimedatastore",this.runtimeData);           
        }
    }
      if (runtimeData.getParameter("clear") != null)
        {
            template_name = ""; 
        }
    
  }
  
  public ChannelRuntimeData prepareRuntimeData(ChannelRuntimeData rnTmDt)
  {
      ChannelRuntimeData crd = rnTmDt;
    for(Enumeration ertd = crd.getParameterNames();ertd.hasMoreElements();)
    {
        String paramname = (String)ertd.nextElement();
        if(paramname.startsWith("param_"))
        {
            int i = paramname.indexOf('_');
            String key = paramname.substring(i+1);
            String value = crd.getParameter("value_"+key);
            crd.remove(paramname);
            crd.remove("value_"+paramname);
            crd.setParameter(paramname,value);
        }
    }
    return crd;
  }
  /** sets the runtimedata with the parameter key and value...
   *
   * the key is preceded with the string "param_" to recognise the parameters
   * @param key The key as in the hashtable
   * @param value value to go against the key
   */  
  public void setRuntimeParams(String key,String value)
  {
    String tmp_key = "param_"+key;
      if(runtimeData.getParameter(tmp_key) == null)
      {
        this.runtimeData.setParameter(tmp_key,value);
      }
      else if(!(runtimeData.getParameter(tmp_key).equals(value)))
      {
          this.runtimeData.setParameter(tmp_key,value);
      }
      else
      {
        this.paramexists = true;
      }
  }
  /** removes the key value pair corresponding to the specified key
   * @param key same functionality as the hashtable
   */  
  public void removeRuntimeParams(String key)
  {
    this.runtimeData.remove(key);
  }
  /** gets the runtime data from the runtimedatastore and updates this.runtimedata with
   * the corresponding key value pairs
   * @param datastore datastore as saved in the IPerson
   */  
    public void setRuntimeDataValues(ChannelRuntimeData datastore)
  {
      try
      {
//        ChannelRuntimeData tmp_runtimeData = (ChannelRuntimeData)datastore.getRuntimeData();   
        if(datastore != null)
        {
            for(Enumeration erntimedata=datastore.getParameterNames();erntimedata.hasMoreElements();)
            {
                String tmp_rntimedataname = (String)erntimedata.nextElement();
                //& !(tmp_rntimedataname.equals("parametername")) & !(tmp_rntimedataname.equals("parametervalue")))
                     if(this.runtimeData.getParameter(tmp_rntimedataname) == null)
                    {
                        String tmp_rntimedatavalue = (String)datastore.getParameter(tmp_rntimedataname);
                        this.runtimeData.setParameter(tmp_rntimedataname,tmp_rntimedatavalue);
                    }
            }
        }
        else
        {
//            System.err.println("-----------datastore in null---------");
        }
      }
      catch(NullPointerException ne)
      {
        ne.printStackTrace();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
       
  }
  /** Part of IChannel that has to be implemented
   * @param ev same as that in uPortal
   */  
    public void receiveEvent(PortalEvent ev) 
    {
    }
    /** This is where the xml created and published
     * @param out The output
     * @throws PortalException same as the uPortal
     */    
  public void renderXML(ContentHandler out) throws PortalException 
  {
          String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
      if(runtimeData.getParameter("templateName") != null)
    {
          // Create a new XSLT styling engine
        xml += "<template>";
//            System.err.println("++++++++The xml as of now is: ++++++++++" + xml);        
        if(doc_details != null && doc_details.getLink() != null)
        {
/*
 *    The link is maintained to get a handle on the name of the document created from the template and also to be able to 
 * update the same document when the update is called
 */            
            xml +=    "<link>"+ doc_details.getLink() +"</link>";
            xml +=  "<template>"+runtimeData.getParameter("template")+"</template>";

/*
    just there for future references
 */            
            xml +=    "<author>"+ doc_details.getAuthor() +"</author>";
        }
       else if(doc_details == null)
      {
          xml += "<link>No Documents</link>";
          xml += "<error>Channel Error. Please contact administrator</error>";
      }
        else if(runtimeData.getParameter("filename")!=null)
        {
                xml +=    "<link>"+ runtimeData.getParameter("filename") +"</link>";
        }
        
         if(runtimeData.getParameter("intDocTemplID") != null)
        {
            xml +=  "<intDocTemplID>"+runtimeData.getParameter("intDocTemplID")+"</intDocTemplID>";
        }
         
/*
    This bit of code is to display an error is an attempt is made to add the same parameter twice
 */        
        if(paramexists)
        {
            xml += "<param_exists>"+"Error: Parameter exists"+"</param_exists>";
            paramexists = false;
        }
        
        if(copied != null)
        {
                xml += "<copied>"+copied+"</copied>";
        }
        if(runtimeData.getParameter("strDocExternalName") != null)
        {
            xml += "<defaultFileName>"+runtimeData.getParameter("strDocExternalName")+"</defaultFileName>";
        }
        if(title != null)
        {
            xml += "<title>"+title+"</title>";
        }
        if(keywords != null)
        {
            xml += "<keywords>"+keywords+"</keywords>";
        }
        if(description != null)
        {
            xml += "<description>"+description+"</description>";
        }
/*
 *    This is to create an xml of all the parameter that have been added to the channel other than the ones in the 
 *runtimedata
 */        
        for(Enumeration eforprms=this.runtimeData.getParameterNames();eforprms.hasMoreElements();)
        {
            String prm_name = (String)eforprms.nextElement();
           if(prm_name.startsWith("param_"))
            {
                int indx = prm_name.indexOf("_") + 1;
                String tmp_prm_name = prm_name.substring(indx);
                xml += "<params>";
                xml += "<p_name>" + tmp_prm_name + "</p_name>";
                xml += "<p_value>" + (String)runtimeData.getParameter(prm_name) + "</p_value>";
                xml += "</params>";
            }
        }
    }
/*
 *The noDocuments in between the link tag is for the initial display of the xml
 */   
    else
    {
        xml += "<template><link>No Documents</link>";
    }

    xml += "</template>";   
    XSLT xslt = new XSLT(this);
    
//     System.err.println("++++++++The xml as of now is: ++++++++++" + xml);

 /*  org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL());

    upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "Inventory"));
    xslt.setStylesheetParameter("inventoryChannelURL", upfTmp.getUPFile());*/
    // pass the result XML to the styling engine.
    xslt.setXML(xml);

    // specify the stylesheet selector
    xslt.setXSL("CTemplateUpdate.ssl", "normal", runtimeData.getBrowserInfo());

    // set parameters that the stylesheet needs.
    xslt.setStylesheetParameter("baseActionURL",
                                    runtimeData.getBaseActionURL());
    org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( true ));//org.jasig.portal.UPFileSpec.FILE_DOWNLOAD_WORKER,true));        

    upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CDownload"));
    xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
    xslt.setStylesheetParameter("nodeId", SessionManager.getChannelID(strSessionUniqueID, "CDownload")); 
  //  xslt.setStylesheetParameter("name_prev", name_prev);


    // set the output Handler for the output.
    xslt.setTarget(out);

    // do the deed
    xslt.transform();
    

  }
  
  /** 
     * Method to return a number of occurrences of a string in a document.
     *
     * @param strDocument file name.
     * @param strText the text to search for
     * @return number occurrrances or 0 if non is found.
     **/
    public static int getOccurrences( String strDocument, String strText )
    {
        int i = 0 ;
        String filename = PropertiesManager.getProperty("neuragenix.genix.templates.TemplateLocation") + "/"+ strDocument;        
        if(filename != null && strText != null)
        {
            i = DocManipulation.getOccurrences(filename, strText);
        }
        return i;
    }
  
  /** DocManipulation is called where the template is updated with the data */  
   public void updateTemplate()
   {
       try
       {
           DocManipulation doc = new DocManipulation();
           this.runtimeData.setParameter("sessionid",this.authToken.getSessionUniqueID());
           doc_details = doc.saveTemplate(this.runtimeData);
           if(doc_details != null)
           {
            doc_details.setAuthor(authToken.getUserIdentifier());
           }
       }
       catch(Exception e)
       {
            e.printStackTrace();
       }
   }
   

}
