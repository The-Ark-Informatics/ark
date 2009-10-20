/*
 * NGXRuntimeProperties.java
 *
 * Created on 22 March 2005, 13:42
 */

package neuragenix.common;

/**
 *
 * @author  dmurley
 */

import neuragenix.security.AuthToken;
import java.util.Hashtable;
import java.util.Vector;

import org.jasig.portal.security.IPerson;

public class NGXRuntimeProperties 
{
    private Hashtable htReturnParameters = new Hashtable();
    private StringBuffer strXML = new StringBuffer();
    private StringBuffer strErrorMessage = new StringBuffer();
    private StringBuffer strSecurityMessage = new StringBuffer();
    
    private String strCurrentStylesheet = "";
    private AuthToken authToken = null;
    
    private boolean blSecurityError = false;
    private boolean blRawXML = false;

    private IPerson ip;
    
    /** Creates a new instance of NGXRuntimeProperties */
    public NGXRuntimeProperties() {
    }

    public NGXRuntimeProperties(AuthToken authToken)
    {
        this.authToken = authToken;
    }
    
    public void setAuthToken(AuthToken authToken)
    {
        this.authToken = authToken;
    }
    
    
    public AuthToken getAuthToken()
    {
        return authToken;
    }
    
    public void clearXML()
    {
       this.blSecurityError = false;
       strXML.delete(0, strXML.length());    
    }
    
        
    
    public void addNewParameter(String paramName, String paramValue)
    {
       htReturnParameters.put(paramName, paramValue);
    }

    public Hashtable getParameters()
    {
        return htReturnParameters;
    }
    
    public void addXML (String strXML)
    {
        this.strXML.append(strXML);
    }
    
    public void addXML (StringBuffer sbXML)
    {
        this.strXML.append(sbXML);
    }
    
    public String getXML ()
    {
        return strXML.toString();
    }
    
    public void setIPerson (IPerson ipPerson)
    {
        this.ip = ipPerson;
    }
    
    public IPerson getIPerson ()
    {
        return ip;
    }

    
    public void setRawXMLResponse(boolean rawXML) {
    	this.blRawXML = rawXML;
    }
    
    public boolean isRawXMLResponse() {
    	return this.blRawXML;
    }
    	
    public void setStylesheet(String strStylesheet)
    {
        this.strCurrentStylesheet = strStylesheet;
        this.blRawXML = false;
    }
    
    public String getStylesheet()
    {
        return strCurrentStylesheet;
    }
    
    public void setErrorMessage(String strErrorMessage)
    {
        this.strErrorMessage = new StringBuffer();
        this.strErrorMessage.append(strErrorMessage);
        //System.out.println ("NEW ERROR MESSAGE : " + strErrorMessage);
        
    }
    
    public String getErrorMessage()
    {
        return strErrorMessage.toString();
    }
    
    public String getErrorMessage(boolean blClearAfterGet)
    {
        String strReturnMessage = strErrorMessage.toString();
        if (blClearAfterGet == true)
        {
           strErrorMessage = new StringBuffer();
        }

        return strReturnMessage;
    }

    
    public void isSecurityError(boolean securityError)
    {
        this.blSecurityError = true;
    }
    
}
