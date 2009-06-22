/**
 * WFViewer.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 17/03/2004
 */

package media.neuragenix.genix.workflow;

/**
 * Workflow Instance Viewer class
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

// javax packages
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
import media.neuragenix.genix.workflow.dialog.*;
import media.neuragenix.genix.workflow.org.jgraph.pad.*;


public class WFViewer extends JApplet {
    
    protected static final Color DEFAULT_COMPLETED_TASK_BACKGROUND_COLOR = new Color(0, 255, 102);
    
    protected static final Color DEFAULT_CURRENT_TASK_BACKGROUND_COLOR = Color.ORANGE;
    
    protected static final Color DEFAULT_INACTIVE_TASK_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    
    protected static final Color DEFAULT_FOLLOWED_TRANSITION = new Color(43, 181, 52);
    
    // graph object to present a workflow instance
    JGraph graph;
    
    // organisation users
    Hashtable hashOrgUsers;
    
    //private Vector vtStopKeys = new Vector(10, 5);
    
    WorkflowInstance savedWFInstance = null;
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() 
    {
        // System.err.println("Parameter : " + getParameter("WorkflowInstanceKey"));
        
        buildViewWorkflowInstance(getParameter("WorkflowInstanceKey"));
        JScrollPane sp = new JScrollPane(graph);
        getContentPane().add(sp);
    }
    
    /**
     * Get org chart users
     */
    public Hashtable getOrgChart(String strMessageToSend)
    {
        ObjectInputStream inputFromServlet;
        Hashtable hashOrgUser = new Hashtable(10);
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
            while (enum.hasMoreElements())
            {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) enum.nextElement();
                Hashtable orguser = (Hashtable) currentNode.getUserObject();
                String orgusertype = (String) orguser.get("url_action");
                if (orgusertype.equals("User"))
                {
                    OrgChartUser newuser = new OrgChartUser((String) orguser.get("ORGUSERTREE_intOrgUserKey"),
                                                            (String) orguser.get("ORGUSERTREE_strFirstName") + " " + 
                                                            (String) orguser.get("ORGUSERTREE_strLastName"),
                                                            orgusertype);
                    hashOrgUser.put(orguser.get("ORGUSERTREE_intOrgUserKey"), (String) orguser.get("ORGUSERTREE_strFirstName") + " " + 
                                                            (String) orguser.get("ORGUSERTREE_strLastName"));
                }
            }
            
            
            
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
        return hashOrgUser;
    }
    
    /**
     * Build workflow instance view
     **/

    public void buildViewWorkflowInstance(String strWorkflowInstanceKey) 
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
            out.println("GET WORKFLOW INSTANCE\n" + strWorkflowInstanceKey);
            out.close();


            // Read the response
            inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
            WorkflowInstance wfInstance = (WorkflowInstance) inputFromServlet.readObject();
            //System.err.println(wfInstance.getName());
        
            // System.err.println("get task size : " + vtTasks.size());
            
                      
            
            //System.err.println(((Task) vtTasks.get(0)).getName());
            
            
            savedWFInstance = wfInstance;
            graph = translateWorkflowInstanceToGraph(wfInstance);
             
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
    }
    
    
    private JGraph translateWorkflowInstanceToGraph(WorkflowInstance wfInstance)
    {
        // get org users
        hashOrgUsers = getOrgChart("GET ORGCHART");
        
        JGraph aGraph = new MyGraph(new DefaultGraphModel());
        aGraph.setMoveable(false);
        aGraph.setEditable(false);
        aGraph.setPortsVisible(false);
        // display the workflow instance's tasks
        Vector vtTasks = wfInstance.getTasks();
       
        
        for (int i=0; i < vtTasks.size(); i++)
        {
            Task aTask = (Task) vtTasks.get(i);
            insertTask(aGraph, aTask);  
        }
        
        // display the workflow instance's transitions
        Object[] tasks = aGraph.getRoots();
        Vector vtTaskTransitions = wfInstance.getTaskTransitions();
        for (int i=0; i < vtTaskTransitions.size(); i++)
        {
            TaskTransition aTransition = (TaskTransition) vtTaskTransitions.get(i);
            // non graph format here
            String from = aTransition.getFrom();
            String to = aTransition.getTo();
            Port source = null;
            Port target = null;
            
            
            for (int j=0; j < tasks.length; j++)  // cycle through all the objects in the workflow diagram
            {
                // not an edge, but definately a cell
                if (!(tasks[j] instanceof DefaultEdge) && (tasks[j] instanceof DefaultGraphCell))
                {
                    DefaultGraphCell currentCell = (DefaultGraphCell) tasks[j];
                    
                    if (currentCell.getUserObject() instanceof Task)
                    {
                        Task currentTask = (Task) currentCell.getUserObject();

                        // if the task is from task of the transition
                        if (from.equals(currentTask.getKey()))
                            source = (Port) currentCell.getChildAt(0);
                        else if (to.equals(currentTask.getKey()))
                            target = (Port) currentCell.getChildAt(0);
                    }
                }
            }
            
            insertTaskTransition(aGraph, source, target, aTransition);
        }
        
        // display task's status label
        for (int i=0; i < vtTasks.size(); i++)
        {
            Task aTask = (Task) vtTasks.get(i);
            
            if (aTask != null && aTask.getPerformer() != null && aTask.getStatus().equals("Completed"))
                insertTaskLabel(aGraph, aTask);  
                
                
        }
        
        
        return aGraph;
    }

    
    
    /**
     * Insert a task to model so we can show it on an applet
     */
    private void insertTask(JGraph aGraph, Task aTask)
    {
        DefaultGraphCell vertex = null;
        if (aTask.getType().equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]))
            vertex = new DefaultGraphCell(aTask);
        else if (aTask.getType().equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
            vertex = new EllipseCell(aTask);
        else if (aTask.getType().equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[2]))
            vertex = new StartCell(aTask);
        else if (aTask.getType().equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[3]))
            vertex = new EndCell(aTask);
        else if (aTask.getType().equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
            vertex = new RoundedRectangleCell(aTask);
            
        // Add one Floating Port
        vertex.add(new DefaultPort("Center"));
        
        // Snap the Point to the Grid
        Point point = new Point(Integer.parseInt(aTask.getPositionX()), Integer.parseInt(aTask.getPositionY()));
        point = aGraph.snap(new Point(point));
        
        // Create a Map that holds the attributes for the Vertex
        Map map = GraphConstants.createMap();
        
        // Make Vertex Opaque
        GraphConstants.setOpaque(map, true);
        
        // Set Font
        GraphConstants.setFont(map, new Font("Arial", Font.BOLD, 10));
        
        // Set bounds
        GraphConstants.setBounds(map, new Rectangle(point, new Dimension(Integer.parseInt(aTask.getWidth()), Integer.parseInt(aTask.getHeight()))));
        
               
        if (!(vertex instanceof StartCell) && !(vertex instanceof EndCell))
        {
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_CELL_BORDER_COLOR);
            // Set value
            GraphConstants.setValue(map, aTask);
        
            if (aTask.getStatus().equals("Completed"))
                GraphConstants.setBackground(map, DEFAULT_COMPLETED_TASK_BACKGROUND_COLOR);
            else if (aTask.getStatus().equals("Active") || aTask.getStatus().equals("Started"))
                GraphConstants.setBackground(map, DEFAULT_CURRENT_TASK_BACKGROUND_COLOR);
            else if (aTask.getStatus().equals("Not active"))
                GraphConstants.setBackground(map, DEFAULT_INACTIVE_TASK_BACKGROUND_COLOR);
        }
        else if (vertex instanceof StartCell)
        {
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_START_CELL_BORDER_COLOR);
            GraphConstants.setBackground(map, DesignPanel.DEFAULT_START_CELL_BACKGROUND_COLOR);
        }
        else if (vertex instanceof EndCell)
        {
            //vtStopKeys.add(aTask.getKey());
            
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_STOP_CELL_BORDER_COLOR);
            GraphConstants.setBackground(map, DesignPanel.DEFAULT_STOP_CELL_BACKGROUND_COLOR);
        }
        
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(vertex, map);

        // Insert the Vertex and its Attributes (can also use model)
        aGraph.getGraphLayoutCache().insert(new Object[] { vertex }, attributes, null, null, null);
    }
    
    /**
     * Insert a task to model so we can show it on an applet
     */
    private void insertTaskLabel(JGraph aGraph, Task aTask)
    {
        DefaultGraphCell vertex = new DefaultGraphCell(buildTaskLabel(aTask));
        
        // Snap the Point to the Grid
        Point point = new Point(Integer.parseInt(aTask.getPositionX()), Integer.parseInt(aTask.getPositionY()) + Integer.parseInt(aTask.getHeight()) + 10);
        point = aGraph.snap(new Point(point));
        
        // Create a Map that holds the attributes for the Vertex
        Map map = GraphConstants.createMap();
        
        // Make Vertex Opaque
        GraphConstants.setOpaque(map, false);
        
        // Set Font
        GraphConstants.setFont(map, new Font("Arial", Font.BOLD, 10));
        
        // Set bounds
        GraphConstants.setBounds(map, new Rectangle(point, new Dimension(Integer.parseInt(aTask.getWidth()), Integer.parseInt(aTask.getHeight()))));
        
        // Add a Border Color Attribute to the Map
        GraphConstants.setBorderColor(map, Color.WHITE);
        // Add Background
        GraphConstants.setBackground(map, Color.WHITE);
        
        // set auto size
        GraphConstants.setAutoSize(map, true);
        
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(vertex, map);

        // Insert the Vertex and its Attributes (can also use model)
        aGraph.getGraphLayoutCache().insert(new Object[] { vertex }, attributes, null, null, null);
    }
    
   
    // Returns true for anything that is 
    private boolean isTargetProgressed(TaskTransition aTransition)
    {
        boolean from = false;
        boolean to   = false;
        
        Vector vtTasks = savedWFInstance.getTasks();
        
        for (int i=0; (!to || !from) && (i < vtTasks.size()); i++)
        {
            Task aTask = (Task) vtTasks.get(i);
            
            // check that both the to and from task that this transition is
            // connected to has a valid status in order to show the
            // progressed target.            
            if (aTask.getKey().equals(aTransition.getTo()))
            {
                if ( aTask.getName().equals("Stop") ) // ignore status of end task
                    to = true;                
                else if ( aTask.getStatus().equals("Completed") || aTask.getStatus().equals("Started") || aTask.getStatus().equals("Active") )
                    to = true;
            }           
            else if ( aTask.getKey().equals(aTransition.getFrom()) )
            {
                if ( aTask.getName().equals("Start") ) // ignore status of start task
                    from = true;
                else if (aTask.getStatus().equals("Completed") || aTask.getStatus().equals("Started") || aTask.getStatus().equals("Active"))
                    if ((aTask.getAction() != null) && aTask.getAction().equals(aTransition.getName()) )
                        from = true;                    
            }
        }
        
        return to && from ? true : false;
    }
    
    
    /**
     * Insert a new Edge between source and target
     *
     */
    
    private void insertTaskTransition(JGraph aGraph, Port source, Port target, TaskTransition aTransition) 
    {
        String strXCoordinate = aTransition.getXCoordinate();
        String strYCoordinate = aTransition.getYCoordinate();
        StringTokenizer xToken = new StringTokenizer(strXCoordinate);
        StringTokenizer yToken = new StringTokenizer(strYCoordinate);
        Vector vtPoints = new Vector(3, 2);
        Vector vtTasks = savedWFInstance.getTasks();
        
        while (xToken.hasMoreElements())
        {
            int xCoordinate = Integer.parseInt(xToken.nextToken());
            int yCoordinate = Integer.parseInt(yToken.nextToken());
            vtPoints.add(new Point(xCoordinate, yCoordinate));
        }
        
        
               
        
        
        
        // Connections that will be inserted into the Model
        ConnectionSet cs = new ConnectionSet();
        
        // Construct Edge with no label
        DefaultEdge edge = new DefaultEdge(aTransition);
        
        // Create Connection between source and target using edge
        cs.connect(edge, source, target);
        
        // Create a Map thath holds the attributes for the edge
        Map map = GraphConstants.createMap();
        
        // Set value
        GraphConstants.setValue(map, aTransition);
        
        
        // prevent highlighting of reject paths
        
        if (!(aTransition.getType().equals("Reject")))
        {
            if (isTargetProgressed(aTransition) == true)
            {
                Color c = DEFAULT_FOLLOWED_TRANSITION;
                GraphConstants.setLineColor(map, c);
            }
            /*
            // check if we need to highlight the final path
            if (savedWFInstance.getStatus().equals("Completed"))
            {
                for (int i=0; i < vtStopKeys.size(); i++)
                {
                    String stopKey = (String) vtStopKeys.get(i);
                    if (aTransition.getTo().equals(stopKey))
                    {
                        Color c = DEFAULT_FOLLOWED_TRANSITION;
                        GraphConstants.setLineColor(map, c);
                        break;
                    }
                }
            }
            */
        }
        // Add a Line End Attribute
        GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
        
        // Set Font
        GraphConstants.setFont(map, new Font("Arial", Font.BOLD, 10));
        
        // Set label position
        GraphConstants.setLabelPosition(map, new Point(500, 500));
        
        // set points
        GraphConstants.setPoints(map, vtPoints);
        
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        
        // Associate the Edge with its Attributes
        attributes.put(edge, map);
        
        // Insert the Edge and its Attributes
        aGraph.getGraphLayoutCache().insert(new Object[] { edge }, attributes, cs, null, null);
    }
    
    private String buildTaskLabel(Task aTask)
    {
        
        StringBuffer strResult = new StringBuffer();
        
        if (aTask != null && aTask.getPerformer() != null && hashOrgUsers != null)
        {
            strResult.append("<html><p style=\"text-align:left\">");
            strResult.append("Done by: " + hashOrgUsers.get(aTask.getPerformer()) + "<br>");
            strResult.append("Action: " + aTask.getAction() + "<br>");
            strResult.append("Date: " + aTask.getDateCompleted() + "<br>");
            strResult.append("Time: " + aTask.getTimeCompleted() + "<br>");
            strResult.append("</p></html>");
        }
        
        return strResult.toString();
    }
    
    public class MyGraph extends JGraph {

        // Construct the Graph using the Model as its Data Source
        public MyGraph(GraphModel model) {
            super(model);
            // Use a Custom Marquee Handler
            //setMarqueeHandler(new MyMarqueeHandler());
            // Tell the Graph to Select new Cells upon Insertion
            setSelectNewCells(true);
            // Make Ports Visible by Default
            setPortsVisible(true);
            // Use the Grid (but don't make it Visible)
            setGridEnabled(true);
            // Set the Grid Size to 10 Pixel
            setGridSize(1);
            // Set the Tolerance to 2 Pixel
            setTolerance(5);
        }

        // Override Superclass Method to Return Custom EdgeView
        protected EdgeView createEdgeView(Edge e, CellMapper cm) {
            // Return Custom EdgeView
            return new EdgeView(e, this, cm) {
                // Override Superclass Method
                public boolean isAddPointEvent(MouseEvent event) {
                    // Points are Added using Shift-Click
                    return event.isShiftDown();
                }
                // Override Superclass Method
                public boolean isRemovePointEvent(MouseEvent event) {
                    // Points are Removed using Shift-Click
                    return event.isShiftDown();
                }
            };
        }
        
        // Override super-class method to return custom cell view
        protected VertexView createVertexView(Object v, CellMapper cm) {
            if (v instanceof EllipseCell)
                return new EllipseView(v, this, cm);
            else if (v instanceof RoundedRectangleCell)
                return new RoundedRectangleView(v, this, cm);
            
            return super.createVertexView(v, cm);
	}

    }
}