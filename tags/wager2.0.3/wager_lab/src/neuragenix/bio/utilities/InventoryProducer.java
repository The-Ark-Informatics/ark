/*
 * InventoryProducer.java
 *
 * Created on 19 April 2005, 17:27
 */

package neuragenix.bio.utilities;

import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.*;

import neuragenix.dao.*;
import neuragenix.security.*;
import neuragenix.common.*;

import org.jasig.portal.security.IPerson;
import org.jasig.portal.security.PersonManagerFactory;


/**
 *
 * @author  dmurley
 * @version
 */
public class InventoryProducer extends HttpServlet {
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        boolean isValidated = false;
        
        // detect if this is a valid uPortal session
        IPerson person = null;
        try
        {
            person = PersonManagerFactory.getPersonManagerInstance().getPerson(request);

            if (person != null && person.getSecurityContext().isAuthenticated())
            {
                isValidated = true;

            }
            else
            {
                isValidated = false;
            }
        }
        catch (Exception e)
        {
            System.err.println ("[InventoryProducer - Servlet] Non-validated user attempted to access servlet");
            isValidated = false;
        }
        
        if (isValidated == false)
        {
            out.write("<h1>You are not authorised to access this component or you have timed out.  <br />Please (re)log-in to the system</h1>");
        }
        
        
        // form will need to submit in the site, tank, box and tray values, plus an identifier for the current field
        // 
        
        String strLookupType = (String) request.getAttribute("strInventoryLookupType");
        String strKeyword = (String) request.getAttribute("strLookupKeyword");
        
        Vector vtLookupResults = null;
        
        if (strLookupType != null && strKeyword != null)
        {
            
        }
        
        
        
        
        
        
        
        
        
        out.close();
    }
    
    private Vector doInventoryLookup(String strLookupType, String strKeyword)
    {
        return null;
    }
    
    
    
    
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
