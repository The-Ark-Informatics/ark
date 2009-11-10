/**
 * WorkflowServlet.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 06/02/2004
 */

package neuragenix.genix.workflow;

/**
 * WorkflowServlet is a servlet that creates a communication-channel
 * between the WFDesigner applet and the server database
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

// uPortal packages
import org.jasig.portal.PropertiesManager;

// neuragenix packages
import neuragenix.common.OrgChart;
import neuragenix.security.AuthToken;
import neuragenix.dao.DALQuery;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.genix.workflow.util.XPDLReader;
//import neuragenix.genix.workflow.*;

public class WorkflowServlet extends HttpServlet 
{

    /** Directory to store XPDL files
     */
    private static final String XPDL_DIRECTORY = PropertiesManager.getProperty("neuragenix.genix.workflow.SaveWorkflowXPDLLocation");
    
    /** Authorisation token
     */
    private static final String SAVE_NEW = "SAVE NEW WORKFLOW";
    private static final String UPDATE = "UPDATE WORKFLOW";
    private static final String GET_WORKFLOW_INSTANCE = "GET WORKFLOW INSTANCE";
    private static final String GET_PACKAGES = "GET PACKAGES";
    private static final String GET_ORGCHART = "GET ORGCHART";
    private static final String GET_FUNCTIONS = "GET FUNCTIONS";
    private static final String GET_ACTIONS = "GET ACTIONS";
    private static final String GET_TRIGGER_TYPES = "GET TRIGGER TYPES";
    
    /**
     * Method to handle GET queries.
     **/

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
    {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.println("Error: this servlet does not support the GET method!");
        out.close();
    }


    /** 
     * Method to handle POST queries.
     **/

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException 
    {

        // Read in the message from the servlet
        StringBuffer msgBuf = new StringBuffer();
        BufferedReader fromApplet = req.getReader();
        String line = fromApplet.readLine();
        if (line != null && line.equals(SAVE_NEW))
        {
            saveNewWorkflowTemplate(fromApplet, resp);
        }
        else if (line != null && line.equals(UPDATE))
        {
            updateWorkflowTemplate(fromApplet, resp);
        }
        else if (line != null && line.equals(GET_WORKFLOW_INSTANCE))
        {
            sendWorkflowInstance(fromApplet, resp);
        }
        else if (line != null && line.equals(GET_PACKAGES))
        {
            sendPackages(resp);
        }
        else if (line != null && line.equals(GET_ORGCHART))
        {
            sendOrgChart(resp);
        }
        else if (line != null && line.equals(GET_FUNCTIONS))
        {
            sendFunctions(resp);
        }
        else if (line != null && line.equals(GET_ACTIONS))
        {
            sendActions(resp);
        }
        else if (line != null && line.equals(GET_TRIGGER_TYPES))
        {
            sendTriggerTypes(resp);
        }
    }
    
    /**
     * Save new workflow template
     */
    private void saveNewWorkflowTemplate(BufferedReader fromApplet, HttpServletResponse resp)
    {
        
        StringBuffer msgBuf = new StringBuffer();
        String line = null;
        
        try
        {
            String strFileName = fromApplet.readLine();
            while ((line=fromApplet.readLine())!=null) 
            {
                if (msgBuf.length()>0) msgBuf.append('\n');
                msgBuf.append(line);
            }
            fromApplet.close();

            // Write message to XPDL file
            //strFileName = authToken.getSessionUniqueID() + "exportfile.csv";
            BufferedWriter xpdlFile = new BufferedWriter(new FileWriter(new File(XPDL_DIRECTORY + "/" + strFileName + ".xml")));
            xpdlFile.write(msgBuf.toString());
            xpdlFile.close();

                // Save workflow into DB
            InputStream file = new FileInputStream(new File(XPDL_DIRECTORY + "/" + strFileName + ".xml"));
            XPDLReader.parseXML(file);
            Package neuragenixPackage = XPDLReader.getWorkflowPackage();

            DALSecurityQuery query = new DALSecurityQuery("", (AuthToken) null);

            //saving
            WorkflowEngine.save(neuragenixPackage, query);

            // Write the message back to the applet
            resp.setContentType("text/plain");
            PrintWriter toApplet = resp.getWriter();
            toApplet.println("I (the servlet) received a message \"" + msgBuf.toString() + "\"");
            toApplet.println("at " + (new java.util.Date()).toString());
            toApplet.println("and sent this text in response.   Hope you enjoy it!");
            toApplet.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }    
    }
    
    
    /**
     * Update workflow template
     */
    private void updateWorkflowTemplate(BufferedReader fromApplet, HttpServletResponse resp)
    {
        
        StringBuffer msgBuf = new StringBuffer();
        StringBuffer oldFileContent = new StringBuffer();
        String line = null;
        
        try
        {
            String strFileName = fromApplet.readLine();
            while ((line=fromApplet.readLine())!=null) 
            {
                if (msgBuf.length()>0) msgBuf.append('\n');
                msgBuf.append(line);
            }
            fromApplet.close();

            // read the file content
            BufferedReader oldFile = new BufferedReader(new FileReader(new File(XPDL_DIRECTORY + "/" + strFileName + ".xml")));
            while ((line = oldFile.readLine()) != null)
            {
                if (oldFileContent.length() > 0) 
                    oldFileContent.append('\n');
                oldFileContent.append(line);
            }
            oldFile.close();
            
            // Write message to XPDL file
            //strFileName = authToken.getSessionUniqueID() + "exportfile.csv";
            BufferedWriter xpdlFile = new BufferedWriter(new FileWriter(new File(XPDL_DIRECTORY + "/" + strFileName + ".xml")));
            xpdlFile.write(msgBuf.toString());
            xpdlFile.close();

                // Save workflow into DB
            InputStream file = new FileInputStream(new File(XPDL_DIRECTORY + "/" + strFileName + ".xml"));
            XPDLReader.parseXML(file);
            Package neuragenixPackage = XPDLReader.getWorkflowPackage();

            DALSecurityQuery query = new DALSecurityQuery("", (AuthToken) null);

            //saving
            String strUpdateResult = WorkflowEngine.update(neuragenixPackage, query);
            // if we can update the template
            if (strUpdateResult == null)
            {
                strUpdateResult = "SUCCESS";
                System.err.println("SUCCESS");
            }
            // if not, write the old XPDL content back
            else
            {
                xpdlFile = new BufferedWriter(new FileWriter(new File(XPDL_DIRECTORY + "/" + strFileName + ".xml")));
                xpdlFile.write(oldFileContent.toString());
                xpdlFile.close();
            }

            // Write the message back to the applet
            resp.setContentType("text/plain");
            PrintWriter toApplet = resp.getWriter();
            
            toApplet.println(strUpdateResult);
            toApplet.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
            
    }
    
    /**
     * Get workflow instance object
     */
    private void sendWorkflowInstance(BufferedReader fromApplet, HttpServletResponse resp)
    {
        ObjectOutputStream output;
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery("", (AuthToken) null);
            String strWorkflowInstanceKey = fromApplet.readLine();
            //System.err.println(strWorkflowInstanceKey);
            output = new ObjectOutputStream(resp.getOutputStream());
            output.writeObject(WorkflowManager.getWorkflowInstance(strWorkflowInstanceKey, query));
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Get workflow package list object
     */
    private void sendPackages(HttpServletResponse resp)
    {
        ObjectOutputStream output;
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery("", (AuthToken) null);
            output = new ObjectOutputStream(resp.getOutputStream());
            output.writeObject(WorkflowManager.getPackages(query));
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Get organisation chart
     */
    private void sendOrgChart(HttpServletResponse resp)
    {
        ObjectOutputStream output;
        
        try
        {
            output = new ObjectOutputStream(resp.getOutputStream());
            output.writeObject(OrgChart.buildOrgTree(null, "Group", "User"));
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
        }
    }
    
    /**
     * Get all system functions
     */
    private void sendFunctions(HttpServletResponse resp)
    {
        ObjectOutputStream output;
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery("", (AuthToken) null);
            output = new ObjectOutputStream(resp.getOutputStream());
            output.writeObject(WorkflowManager.getFunctions(query));
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Get all system actions
     */
    private void sendActions(HttpServletResponse resp)
    {
        ObjectOutputStream output;
        
        try
        {
            Vector vtActions = new Vector(20, 10);
            DALQuery query = new DALQuery();
            query.setDomain("ACTIVITY", null, null, null);
            query.setField("ACTIVITY_strActivityName", null);
            query.setField("ACTIVITY_strActivityDesc", null);
            query.setWhere(null, 0, "ACTIVITY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("ACTIVITY_strActivityName", "ASC");
            ResultSet rsResult = query.executeSelect();
            while (rsResult.next())
            {
                vtActions.add(rsResult.getString("ACTIVITY_strActivityName"));
            }
            
            output = new ObjectOutputStream(resp.getOutputStream());
            output.writeObject(vtActions);
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Get all system trigger types per domain
     */    
    private void sendTriggerTypes(HttpServletResponse resp)
    {
        ObjectOutputStream output;
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery("", (AuthToken) null);
            output = new ObjectOutputStream(resp.getOutputStream());
            output.writeObject(WorkflowManager.getTriggerTypes(query));            
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}