/**
 * CISearch.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 16/08/2004
 */

package neuragenix.genix.isearch;

/**
 * Intelligent Search
 * @author <a href="mailto:abalraj@neuragenix.com"> Anita Balraj </a>
 * Description: To define the advanced, normal and simplified search
 *
 * Modified on: 
 * @author 
 * Description: 
 */

// java packages
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.*;
import java.sql.*;

// uPortal packages
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
import org.jasig.portal.PropertiesManager; 

// neuragenix packages
import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.common.Utilities;
import neuragenix.common.LockRequest;
import neuragenix.common.LockRecord;
import neuragenix.common.QueryChannel;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DBField;
import neuragenix.dao.DALQuery;

import neuragenix.security.exception.SecurityException;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.SessionManager;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.DALQuery;
import neuragenix.common.QueryChannel;
import java.io.InputStream;
import java.sql.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

public class CISearch implements IChannel
{    
    
    // pages
    private final String BUILD_ISEARCH = "build_isearch";
    
    // channel's runtime data
    private ChannelRuntimeData runtimeData;
        
    private String strXML = "";
    private String strStylesheet;

    private IntelligentSearch iSearch;
    
    /** Creates a new instance of CISearch
     */
    public CISearch()
    {
        this.strStylesheet = BUILD_ISEARCH;
    }
    
    /**
     *  Returns channel runtime properties.
     *  Satisfies implementation of Channel Interface.
     *
     *  @return handle to runtime properties
     */
    public ChannelRuntimeProperties getRuntimeProperties()
    {
        return new ChannelRuntimeProperties();
    }

    /**
     *  Process layout-level events coming from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param PortalEvent ev a portal layout event
     */
    public void receiveEvent(PortalEvent ev)
    {
        // If the user logout, destroy lock request object
        //if (ev.getEventNumber() == PortalEvent.SESSION_DONE)
            //clearLockRequest();
    }

    /**
     *  Receive static channel data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelStaticData sd static channel data
     */
   public void setStaticData(ChannelStaticData sd)
    {
        iSearch = new IntelligentSearch(sd);
    }
    
    
    /**
     *  Receive channel runtime data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelRuntimeData rd handle to channel runtime data
     */
     public void setRuntimeData(ChannelRuntimeData rd)
     {
        this.runtimeData = rd;
        iSearch.processRuntimeData(rd);
        
        
        strStylesheet = iSearch.getStylesheet();
        strXML = iSearch.getXML();
        
     
     }
     
     public void renderXML(ContentHandler out) throws PortalException
     {
        // Create a new XSLT styling engine
        XSLT xslt = new XSLT(this);

        //System.err.println("strXML: " + strXML);
        // pass the result xml to the styling engine.
        xslt.setXML(strXML);

        // specify the stylesheet selector
        xslt.setXSL("CISearch.ssl", strStylesheet, runtimeData.getBrowserInfo());

        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());        

        // Start Secure Download Implementation     
        
        org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( true ));        
        
        upfTmp.setTargetNodeId(SessionManager.getChannelID(iSearch.getSessionID(), "CDownload"));
        xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter("nodeId", SessionManager.getChannelID(iSearch.getSessionID(), "CDownload"));   

        // End Secure
        
        // set the output Handler for the output.
        xslt.setTarget(out);

        // do the deed
        xslt.transform();
         
     }
     
}         
