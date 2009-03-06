package neuragenix.bio.study;

/*
 * ConfigurationManager.java
 *
 * Created on 22 March 2005, 13:15
 */

/**
 *
 * @author  dmurley
 */

import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.bio.utilities.*;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Enumeration;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;
import neuragenix.genix.config.SystemConfiguration;

public class ConfigurationManager {
    
    private AuthToken authToken = null;
    
    private NGXRuntimeProperties rp = null;
    
    private final String CONFIGURATION_KEY = "DEFAULTSTUDY";
    
    /**
     * Creates a new instance of ConfigurationManager 
     */
    public ConfigurationManager(AuthToken authToken) 
    {
        this.authToken = authToken;
    }
    
    public ConfigurationManager(NGXRuntimeProperties rp)
    {
        this.rp = rp;
        this.authToken = rp.getAuthToken();
    }
   
    public NGXRuntimeProperties processRuntimeData(ChannelRuntimeData runtimeData)
    {
        String action = runtimeData.getParameter("action");
        rp.clearXML();
        rp.setStylesheet("defaultStudySelection");
        
        
        if (action.equalsIgnoreCase("displayScreen"))
        {
           rp.addXML(buildDisplayXML());
        }
        else if (action.equalsIgnoreCase("updateDefaultStudy"))
        {
           String newDefaultStudy = runtimeData.getParameter("defaultStudy");
           if (newDefaultStudy == null)
           {
              rp.setErrorMessage("No study has been selected");
              rp.addXML(buildDisplayXML());
           }
           else
           {
              SystemConfiguration.setConfigValue(CONFIGURATION_KEY, newDefaultStudy);
              rp.setErrorMessage("Study has been updated");
              rp.addXML(buildDisplayXML());
           }
        }
        return rp;
    }

    public StringBuffer buildDisplayXML()
    {
       StringBuffer buffer = new StringBuffer();
       buffer.append("<defaultStudy>");
       buffer.append(StudyUtilities.getListOfStudiesXML(this.authToken,true));
       String currDefault = SystemConfiguration.getConfigValue(CONFIGURATION_KEY);
       buffer.append("<selectedStudy>" + currDefault + "</selectedStudy>");
       try
       {
           // If user has permission to change the study settings
           if (authToken.hasActivity("study_settings"))               
           {
               buffer.append("<PermissionStudySettings>" + "true" + "</PermissionStudySettings>");
           }
       }
       catch (Exception e)
       {
           e.printStackTrace();

           LogService.instance().log(LogService.ERROR, "Unknown error in CStudy Channel - " + e.toString());
       }
       
       buffer.append("</defaultStudy>");
       return buffer;
    }


}
