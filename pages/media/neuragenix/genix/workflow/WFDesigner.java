/**
 * WFDesigner.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 30/01/2004
 */

package media.neuragenix.genix.workflow;

/**
 * Workflow designer class
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

// javax packages
import javax.swing.event.*;
import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;


// JGraph packages
import org.jgraph.*;
import org.jgraph.graph.*;


// Neuragenix packages
import neuragenix.genix.workflow.*;


public class WFDesigner extends JApplet {
    
    JTree workflowTree;
    DefaultMutableTreeNode root, orgchart;
    DefaultTreeModel treeModel;
    Hashtable hashTriggerTypes;
    
    JScrollPane leftScrollPane, rightScrollPane;
    JSplitPane sp;
    DesignPanel designPanel = null;
    Vector vtPackages = null;
    Vector vtFunctions = null;
    WorkflowProcess wfProcess;
    
    Vector loadedPanels = new Vector(10, 5);
    
    /** Get all the packages
     */
    
    public void clearLoadedPanels()
    {
        loadedPanels.removeAllElements();
    }
    
    
    public Vector getPackages()
    {
        return vtPackages;
    }
    
    /** Get all the system functions
     */
    public Vector getFunctions()
    {
        return vtFunctions;
    }
    
    /** Get the Organisational chart
     */
    public DefaultMutableTreeNode getOrgChart()
    {
        return orgchart;
    }
    
    /** Get the list of Trigger Types
     */
    public Hashtable getTriggerTypes()
    {
        return hashTriggerTypes;
    }
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() 
    {
        createMainPanel();
    }
    
    private void createMainPanel() 
    {
        // build org chart
        orgchart = getOrgChart("GET ORGCHART");
        
        // build the trigger types
        hashTriggerTypes = getTriggerTypes("GET TRIGGER TYPES");
        
        // get functions
        vtFunctions = getFunctions("GET FUNCTIONS");
        
        // create design pane
        designPanel = new DesignPanel(this);
        
        // build workflow tree
        workflowTree = new JTree();
        workflowTree.addTreeSelectionListener(new MyTreeSelectionListener());
        
        // For Mozilla-based browsers, the Applet retains the space character
        // code (%20) of the URL, thus chanage it back to space
        String strPackageID  = getParameter("strPackageID").replaceAll("%20", " ");
        String strWorkflowID = getParameter("strWorkflowID").replaceAll("%20", " ");
        createWorkflowTree(strPackageID, strWorkflowID);
        
        /*Enumeration enum = orgchart.breadthFirstEnumeration();
        while (enum.hasMoreElements())
            System.err.println(enum.nextElement());*/
        
        leftScrollPane = new JScrollPane(workflowTree);
        leftScrollPane.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.BLACK), "Workflow list"));
        
        
        rightScrollPane = new JScrollPane(designPanel);
        rightScrollPane.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.BLACK), "Design frame"));
        
        sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        sp.setDividerLocation(getWidth()/4);
        getContentPane().add(sp);
        //repaint();import javax.swing.tree.TreeSelectionModel;

    }
    
    public void createWorkflowTree(String strPackageID, String strWorkflowID) 
    {
        root = new DefaultMutableTreeNode("Neuragenix Workflows");
        createNodes();
        treeModel = new DefaultTreeModel(root);
        treeModel.addTreeModelListener(new MyTreeModelListener());

        workflowTree.setModel(treeModel);
        setCurrentWorkflowNode(strPackageID, strWorkflowID);
        //workflowTree.addTreeSelectionListener(new MyTreeSelectionListener());
        //workflowTree = new JTree(root);
    }
    
    /** Build workflow tree nodes
     */
    private void createNodes()  {
        vtPackages = getPackages("GET PACKAGES");

        if (vtPackages != null)
        {
            for (int i=0; i < vtPackages.size(); i++)
            {
                // add a package node
                neuragenix.genix.workflow.Package currentPackage = (neuragenix.genix.workflow.Package) vtPackages.get(i);
                DefaultMutableTreeNode currentPackageNode = new DefaultMutableTreeNode(currentPackage);
                root.add(currentPackageNode);
                
                // add workflow template nodes
                Hashtable hashWorkflowTemplates = currentPackage.getWorkflowProcesses();
                Enumeration enum = hashWorkflowTemplates.elements();
                while (enum.hasMoreElements())
                {
                    WorkflowProcess currentWorkflow = (WorkflowProcess) enum.nextElement();
                    DefaultMutableTreeNode currentWorkflowNode = new DefaultMutableTreeNode(currentWorkflow);
                    currentPackageNode.add(currentWorkflowNode);
                }
            }
        }  
        
    }
    
    private void setCurrentWorkflowNode(String strPackageID, String strWorkflowID)
    {
        // flag for whether the node is selected
        boolean wfSet = false;
        
        if (strPackageID != null && !strPackageID.equals("") && strWorkflowID != null && !strWorkflowID.equals(""))
        {
            Enumeration enum = root.breadthFirstEnumeration();
            //DefaultMutableTreeNode new_node = null, current_node = null;

            while (enum.hasMoreElements() && !wfSet)
            {
                DefaultMutableTreeNode current_node = (DefaultMutableTreeNode) enum.nextElement();                
                if (current_node.getUserObject() instanceof neuragenix.genix.workflow.Package)
                {
                    neuragenix.genix.workflow.Package currentPackage = (neuragenix.genix.workflow.Package) current_node.getUserObject();
                    if (currentPackage.getId().equals(strPackageID))
                    {
                        Enumeration enumChildren = current_node.breadthFirstEnumeration();
                        while (enumChildren.hasMoreElements() && !wfSet)
                        {                       
                            DefaultMutableTreeNode current_child_node = (DefaultMutableTreeNode) enumChildren.nextElement();
                            if (current_child_node.getUserObject() instanceof WorkflowProcess)
                            {
                                WorkflowProcess currentWorkflow = (WorkflowProcess) current_child_node.getUserObject();
                                if (currentWorkflow.getId().equals(strWorkflowID))
                                {                        
                                    workflowTree.setSelectionPath(new TreePath(current_child_node.getPath()));
                                    designPanel.translateWorkflowProcessToGraph(currentPackage, currentWorkflow);
                                    wfSet = true;                                   
                                }
                            }
                        }
                    }
                }
            }
        }     

        // if there is no associated WF process and package, 
        // then show a default new WF design view
        if (!wfSet)
            designPanel.newWorkflow();           
    }
    
    public void addTreeNode(String parentNode, String newNode)
    {
        Enumeration enum = root.breadthFirstEnumeration();
        DefaultMutableTreeNode new_node = null, current_node = null;
        
        while (enum.hasMoreElements())
        {
            current_node = (DefaultMutableTreeNode) enum.nextElement();
            if (current_node.getUserObject().toString().equals(parentNode))
            {
                new_node = new DefaultMutableTreeNode(newNode);
                break;
            }
        }
        
        if (new_node != null)
        {
            // refresh GUI
            treeModel.insertNodeInto(new_node, current_node, current_node.getChildCount());
            workflowTree.setSelectionPath(new TreePath(new_node.getPath()));
        }
    }
    
    /**
     * Save a new workflow template
     **/

    public String saveWorkflowTemplate(String strMessageToSend, String strPackageID, String strWorkflowID) 
    {
        StringBuffer response = new StringBuffer();
        ObjectInputStream inputFromServlet;
        try 
        {

            String strServletURL = "";
            
            if (getCodeBase().getPort() > 0 && getCodeBase().getPort() < 65536)
                // Create an object we can use to communicate with the servlet
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + ":" +
                                       getCodeBase().getPort() + getCodeBase().getPath() + "workflow";
            else
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + getCodeBase().getPath() + "workflow";
            
            URL servletURL = new URL(strServletURL);
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoOutput(true);  // to allow us to write to the URL
            servletConnection.setUseCaches(false);  // to ensure that we do contact
                                                  // the servlet and don't get
                                                  // anything from the browser's
                                                  // cache

            // Write the message to the servlet
            PrintStream out = new PrintStream(servletConnection.getOutputStream());
            out.println(strMessageToSend);
            out.close();

            
            // Now read in the response
            InputStream in = servletConnection.getInputStream();
            
            int chr;
            while ((chr=in.read())!=-1) 
            {
                response.append((char) chr);
            }
            in.close();
            
            
            
            createWorkflowTree(strPackageID, strWorkflowID);
            //leftScrollPane.add(workflowTree);
            //System.err.println(response.toString());
             
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + e.toString());
        }
        
        return response.toString();
    }
    
    
    /**
     * Get workflow packages list
     **/

    public Vector getPackages(String strMessageToSend) 
    {
        ObjectInputStream inputFromServlet;
        try 
        {

            String strServletURL = "";
            
            if (getCodeBase().getPort() > 0 && getCodeBase().getPort() < 65536)
                // Create an object we can use to communicate with the servlet
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + ":" +
                                       getCodeBase().getPort() + getCodeBase().getPath() + "workflow";
            else
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + getCodeBase().getPath() + "workflow";
            
            URL servletURL = new URL(strServletURL);
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoOutput(true);  // to allow us to write to the URL
            servletConnection.setUseCaches(false);  // to ensure that we do contact
                                                  // the servlet and don't get
                                                  // anything from the browser's
                                                  // cache

            // Write the message to the servlet
            PrintStream out = new PrintStream(servletConnection.getOutputStream());
            out.println(strMessageToSend);
            out.close();

            // Read the response
            inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
            return (Vector) inputFromServlet.readObject();
            
        }
        catch (ClassNotFoundException ex) 
        {
            ex.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + ex.toString());
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + e.toString());
        }
        return null;
    }
    
    
    /**
     * Get the trigger types
     */    
    public Hashtable getTriggerTypes(String strMessageToSend)
    {
        ObjectInputStream inputFromServlet;
        try 
        {
            String strServletURL = "";
            
            if (getCodeBase().getPort() > 0 && getCodeBase().getPort() < 65536)
                // Create an object we can use to communicate with the servlet
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + ":" +
                                       getCodeBase().getPort() + getCodeBase().getPath() + "workflow";
            else
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + getCodeBase().getPath() + "workflow";
            
            URL servletURL = new URL(strServletURL);
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoOutput(true);  // to allow us to write to the URL
            servletConnection.setUseCaches(false);  // to ensure that we do contact
                                                  // the servlet and don't get
                                                  // anything from the browser's
                                                  // cache

            // Write the message to the servlet
            PrintStream out = new PrintStream(servletConnection.getOutputStream());
            out.println(strMessageToSend);
            out.close();

            // Read the response
            inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
            Hashtable hashTriggerTypesResp = (Hashtable) inputFromServlet.readObject();
            
            return hashTriggerTypesResp;
        }
        catch (ClassNotFoundException ex) 
        {
            ex.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + ex.toString());
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + e.toString());
        }
        
        return null;
    }
    
    
    /**
     * Get org chart
     */
    public DefaultMutableTreeNode getOrgChart(String strMessageToSend)
    {
        ObjectInputStream inputFromServlet;
        try 
        {

            String strServletURL = "";
            
            if (getCodeBase().getPort() > 0 && getCodeBase().getPort() < 65536)
                // Create an object we can use to communicate with the servlet
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + ":" +
                                       getCodeBase().getPort() + getCodeBase().getPath() + "workflow";
            else
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + getCodeBase().getPath() + "workflow";
            
            URL servletURL = new URL(strServletURL);
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoOutput(true);  // to allow us to write to the URL
            servletConnection.setUseCaches(false);  // to ensure that we do contact
                                                  // the servlet and don't get
                                                  // anything from the browser's
                                                  // cache

            // Write the message to the servlet
            PrintStream out = new PrintStream(servletConnection.getOutputStream());
            out.println(strMessageToSend);
            out.close();

            // Read the response
            inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
            DefaultMutableTreeNode orgchartTree = (DefaultMutableTreeNode) inputFromServlet.readObject();
            Enumeration enum = orgchartTree.breadthFirstEnumeration();
            boolean isRootNode = true;
            while (enum.hasMoreElements())
            {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) enum.nextElement();
                Hashtable orguser = (Hashtable) currentNode.getUserObject();
                String orgusertype = (String) orguser.get("url_action");
                
                if (orgusertype.equals("Group"))
                {
                    OrgChartUser newuser = null;
                    if (isRootNode)
                    {
                        isRootNode = false;
                        newuser = new OrgChartUser((String) orguser.get("ORGGROUPTREE_intOrgGroupKey"),
                                                                "Initiator",
                                                                orgusertype);
                    }
                    else
                        newuser = new OrgChartUser((String) orguser.get("ORGGROUPTREE_intOrgGroupKey"),
                                                                (String) orguser.get("ORGGROUPTREE_strOrgGroupName"),
                                                                orgusertype);
                    currentNode.setUserObject(newuser);
                    
                }
                else
                {
                    OrgChartUser newuser = new OrgChartUser((String) orguser.get("ORGUSERTREE_intOrgUserKey"),
                                                            (String) orguser.get("ORGUSERTREE_strFirstName") + " " + 
                                                            (String) orguser.get("ORGUSERTREE_strLastName"),
                                                            orgusertype);
                    currentNode.setUserObject(newuser);
                }
            }
            
            return orgchartTree;
            
        }
        catch (ClassNotFoundException ex) 
        {
            ex.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + ex.toString());
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + e.toString());
        }
        return null;
    }
    
    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)
                     (e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                       (node.getChildAt(index));
            } catch (NullPointerException exc) {}

            //System.out.println("The user has finished editing the node.");
            //System.out.println("New value: " + node.getUserObject());
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
    
    class MyTreeSelectionListener implements TreeSelectionListener
    {
        private String strCurrentSelectedNode = "";
        private TreePath oldTreePath = null;
        private boolean isDoubleCall = false;
        
        public void valueChanged(TreeSelectionEvent e) 
        {
                        
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) workflowTree.getLastSelectedPathComponent();
            
            if (node == null) return;

            Object nodeInfo = node.getUserObject();
            //System.err.println(node);
            if (nodeInfo instanceof WorkflowProcess)
            {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                Object parentNodeInfo = parentNode.getUserObject();
                             
                if (!(designPanel.getGraphId().equals("")))  // going to lose details if not new graph
                {
                   if (!(workflowTree.getSelectionPath() == oldTreePath))
                   {
                       if (!designPanel.validateTransitions(true)) 
                       {
                            workflowTree.setSelectionPath(oldTreePath);  // reset selection
                            isDoubleCall = true;
                       }
                       else
                       {
                          strCurrentSelectedNode = ((WorkflowProcess)nodeInfo).getId();
                          oldTreePath = workflowTree.getSelectionPath();
                          designPanel.translateWorkflowProcessToGraph((neuragenix.genix.workflow.Package) parentNodeInfo, (WorkflowProcess) nodeInfo);   
                       }
                   }
                
                }
                else
                {
                   strCurrentSelectedNode = ((WorkflowProcess)nodeInfo).getId();
                   oldTreePath = workflowTree.getSelectionPath();
                   designPanel.translateWorkflowProcessToGraph((neuragenix.genix.workflow.Package) parentNodeInfo, (WorkflowProcess) nodeInfo); 
                }
                    
                
            }
                
            
        }

    }

    /**
     * Get system functions
     */
    public Vector getFunctions(String strMessageToSend)
    {
        ObjectInputStream inputFromServlet;
        try 
        {

            String strServletURL = "";
     
            if (getCodeBase().getPort() > 0 && getCodeBase().getPort() < 65536)
                // Create an object we can use to communicate with the servlet
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + ":" +
                                       getCodeBase().getPort() + getCodeBase().getPath() + "workflow";
            else
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + getCodeBase().getPath() + "workflow";
            
            URL servletURL = new URL(strServletURL);
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoOutput(true);  // to allow us to write to the URL
            servletConnection.setUseCaches(false);  // to ensure that we do contact
                                                  // the servlet and don't get
                                                  // anything from the browser's
                                                  // cache

            // Write the message to the servlet
            PrintStream out = new PrintStream(servletConnection.getOutputStream());
            out.println(strMessageToSend);
            out.close();

            // Read the response
            inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
            Vector vtSystemFunctions = (Vector) inputFromServlet.readObject();
            
            return vtSystemFunctions;
            
        }
        catch (ClassNotFoundException ex) 
        {
            ex.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + ex.toString());
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + e.toString());
        }
        return null;
    }
    
    /**
     * Get system actions
     */
    public Vector getActions(String strMessageToSend)
    {
        ObjectInputStream inputFromServlet;
        try 
        {

            String strServletURL = "";
            
            if (getCodeBase().getPort() > 0 && getCodeBase().getPort() < 65536)
                // Create an object we can use to communicate with the servlet
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + ":" +
                                       getCodeBase().getPort() + getCodeBase().getPath() + "workflow";
            else
                strServletURL = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + getCodeBase().getPath() + "workflow";
            
            URL servletURL = new URL(strServletURL);
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoOutput(true);  // to allow us to write to the URL
            servletConnection.setUseCaches(false);  // to ensure that we do contact
                                                  // the servlet and don't get
                                                  // anything from the browser's
                                                  // cache

            // Write the message to the servlet
            PrintStream out = new PrintStream(servletConnection.getOutputStream());
            out.println(strMessageToSend);
            out.close();

            // Read the response
            inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
            Vector vtSystemActions = (Vector) inputFromServlet.readObject();
            
            return vtSystemActions;
            
        }
        catch (ClassNotFoundException ex) 
        {
            ex.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + ex.toString());
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            //responseField.setText("An error occurred: " + e.toString());
            System.err.println("An error occurred: " + e.toString());
        }
        return null;
    }
}
