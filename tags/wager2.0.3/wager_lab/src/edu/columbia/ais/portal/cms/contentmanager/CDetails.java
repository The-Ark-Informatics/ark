/*
 * CDetails.java
 *
 * Created on 30 September 2004, 14:41
 */

package edu.columbia.ais.portal.cms.contentmanager;

/**
 *
 * @author  renny
 */
import org.jasig.portal.IChannel;
import org.jasig.portal.security.IPerson;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XSLT;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;

import org.xml.sax.ContentHandler;

import neuragenix.security.AuthToken;
import neuragenix.dao.DALQuery;
import neuragenix.common.QueryChannel;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.SessionManager;


import java.io.InputStream;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;


public class CDetails implements IChannel
{
    IPerson ip;
    AuthToken authToken;
    ChannelStaticData staticData;
    ChannelRuntimeData runtimeData;
    private String strStylesheet;
    private String casekey;
    String strXML ="";
    private String strSessionUniqueID="";
    
    /** Creates a new instance of CDetails */
    public CDetails()
    {
    }
    
    public ChannelRuntimeProperties getRuntimeProperties()
    {
        return new ChannelRuntimeProperties();
    }
    
    public void setStaticData(ChannelStaticData sd) 
    {
        ip = sd.getPerson();
        this.authToken = (AuthToken)ip.getAttribute("AuthToken");
        this.staticData = sd;
        
         InputStream file = CDetails.class.getResourceAsStream("ContentManagerDBSchema.xml");
         DatabaseSchema.loadDomains(file, "ContentManagerDBSchema.xml");
         
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
                    
                    SessionManager.addChannelID(strSessionUniqueID, "CCase",
                            (String) globalIDContext.lookup("CCase"));
                } 
                catch (NotContextException nce)
                {
                    LogService.log(LogService.ERROR, "Could not find channel ID for fname=Case Management");
                }catch (NamingException e)
                {
                    LogService.log(LogService.ERROR, e);
                }    
    }
    
    public void setRuntimeData(ChannelRuntimeData rd) 
    {
        this.runtimeData = rd;
        this.strStylesheet = "details";
        casekey = null;
        strXML = "";
        if(ip.getAttribute("CORECASE_intCoreCaseKey") != null)
        {
            try
            {
                casekey = (String)ip.getAttribute("CORECASE_intCoreCaseKey");
                Vector formfields = DatabaseSchema.getFormFields("doc_case_details");
                strXML += QueryChannel.buildFormLabelXMLFile(formfields);
                strXML += QueryChannel.buildViewFromKeyXMLFile(formfields,"CORECASE_intCoreCaseKey",casekey);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
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
        String xml="";
        XSLT xslt = new XSLT(this);
        
        org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL());

        upfTmp.setTargetNodeId(SessionManager.getChannelID(authToken.getSessionUniqueID(), "CCase"));
                
        xml += "<?xml version=\"1.0\" encoding=\"utf-8\"?><details>"+strXML+"<caseURL>" + upfTmp.getUPFile() + "</caseURL>" +
                    "<caseTab>" + SessionManager.getTabOrder( authToken, "CCase") + "</caseTab>" +"</details>";
        
        
        xslt.setXML(xml);
        
        xslt.setXSL("CDetails.ssl", strStylesheet, runtimeData.getBrowserInfo());
        
         // set the output Handler for the output.
        xslt.setTarget(out);

        // do the deed
        xslt.transform();
    }
}
