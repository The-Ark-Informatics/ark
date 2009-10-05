/**
 * WFTemplateViewer.java
 * Copyright © 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 21/04/2004
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
import java.util.ArrayList;
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


public class WFTemplateViewer extends JApplet {
    
    JGraph graph;
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() 
    {
        //System.err.println(getParameter("strPackageID"));
        //System.err.println(getParameter("strWorkflowID"));
        
        buildViewWorkflowTemplate(getParameter("strPackageID"), getParameter("strWorkflowID"));
        JScrollPane sp = new JScrollPane(graph);
        getContentPane().add(sp);
    }
    
    
    /** Build the workflow template view 
     */
    private void buildViewWorkflowTemplate(String strPackageID, String strWorkflowID)
    {
        // get all workflow packages
        Vector vtPackages = getPackages("GET PACKAGES");
        if (vtPackages != null)
        {
            for (int i=0; i < vtPackages.size(); i++)
            {
                neuragenix.genix.workflow.Package currentPackage = (neuragenix.genix.workflow.Package) vtPackages.get(i);
                if (currentPackage.getId().equals(strPackageID))
                {
                    Hashtable hashWorkflowProcesses = currentPackage.getWorkflowProcesses();
                    WorkflowProcess currentWorkflowProcess = (WorkflowProcess) hashWorkflowProcesses.get(strWorkflowID);
                    translateWorkflowProcessToGraph(currentWorkflowProcess);
                }
            }
        }
    }
    
    public void translateWorkflowProcessToGraph(WorkflowProcess wfProcess)
    {
        graph = new MyGraph(new DefaultGraphModel());
        graph.setMoveable(false);
        graph.setEditable(false);
        graph.setPortsVisible(false);
        
        // display the workflow activities
        Hashtable hashActivities = wfProcess.getActivities();
        Enumeration enumActivities = hashActivities.elements();
        
        while (enumActivities.hasMoreElements())
        {
            Activity aActivity = (Activity) enumActivities.nextElement();
            insertActivity(aActivity);
        }
        
        // display the transitions
        Hashtable hashTransitions = wfProcess.getTransitions();
        Enumeration enumTransitions = hashTransitions.elements();
        Object[] activities = graph.getRoots();
        
        while (enumTransitions.hasMoreElements())
        {
            Transition aTransition = (Transition) enumTransitions.nextElement();
            String from = aTransition.getFrom();
            String to = aTransition.getTo();
            Port source = null;
            Port target = null;
            
            for (int i=0; i < activities.length; i++)
            {
                if (!(activities[i] instanceof DefaultEdge) && (activities[i] instanceof DefaultGraphCell))
                {
                    DefaultGraphCell currentCell = (DefaultGraphCell) activities[i];
                    
                    if (currentCell.getUserObject() instanceof Activity)
                    {
                        Activity currentActivity = (Activity) currentCell.getUserObject();

                        // if the task is from task of the transition
                        if (from.equals(currentActivity.getId()))
                            source = (Port) currentCell.getChildAt(0);
                        else if (to.equals(currentActivity.getId()))
                            target = (Port) currentCell.getChildAt(0);
                    }
                }
            }
            
            insertTransition(source, target, aTransition);
        }
    }
    
    private void insertActivity(Activity aActivity)
    {
        // getting activity attributes
        Hashtable hashExtendedAttributes = aActivity.getExtendedAttributes();
        String strActivityType = (String) hashExtendedAttributes.get("strType");
        int intPosX = Integer.parseInt((String) hashExtendedAttributes.get("intPosX"));
        //System.err.println(intPosX);
        int intPosY = Integer.parseInt((String) hashExtendedAttributes.get("intPosY"));
        //System.err.println(intPosY);
        int intWidth = Integer.parseInt((String) hashExtendedAttributes.get("intWidth"));
        int intHeight = Integer.parseInt((String) hashExtendedAttributes.get("intHeight"));
        
        DefaultGraphCell vertex = null;
        
        // Create a Map that holds the attributes for the Vertex
        Map map = GraphConstants.createMap();
        
        // Human activity
        if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]))
        {
            vertex = new DefaultGraphCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DesignPanel.DEFAULT_CELL_BACKGROUND_COLOR);
        }
        // System activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
        {
            vertex = new EllipseCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DesignPanel.DEFAULT_CELL_BACKGROUND_COLOR);
        }
        // start activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[2]))
        {
            vertex = new StartCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_START_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DesignPanel.DEFAULT_START_CELL_BACKGROUND_COLOR);
        }
        // stop activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[3]))
        {
            vertex = new EndCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_STOP_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DesignPanel.DEFAULT_STOP_CELL_BACKGROUND_COLOR);
        }
        // sub-workflow activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
        {
            vertex = new RoundedRectangleCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DesignPanel.DEFAULT_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DesignPanel.DEFAULT_CELL_BACKGROUND_COLOR);
        }
        
        
        // Add one Floating Port
        vertex.add(new DefaultPort("Center"));
        
        // Snap the Point to the Grid
        Point point = new Point(intPosX, intPosY);
        point = graph.snap(new Point(point));
        
        // Make Vertex Opaque
        GraphConstants.setOpaque(map, true);
        
        // Set Font
        GraphConstants.setFont(map, new Font("Arial", Font.BOLD, 10));
        
        // Set bounds
        GraphConstants.setBounds(map, new Rectangle(point, new Dimension(intWidth, intHeight)));
        
        // Set value
        GraphConstants.setValue(map, aActivity);
        
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(vertex, map);

        // Insert the Vertex and its Attributes (can also use model)
        graph.getGraphLayoutCache().insert(new Object[] { vertex }, attributes, null, null, null);
    }
    
    /**
     * Insert a new Edge between source and target
     */
    private void insertTransition(Port source, Port target, Transition aTransition) 
    {
        String strXCoordinate = aTransition.getXCoordinate();
        String strYCoordinate = aTransition.getYCoordinate();
        StringTokenizer xToken = new StringTokenizer(strXCoordinate);
        StringTokenizer yToken = new StringTokenizer(strYCoordinate);
        ArrayList arrPoints = new ArrayList();
        while (xToken.hasMoreElements())
        {
            int xCoordinate = Integer.parseInt(xToken.nextToken());
            int yCoordinate = Integer.parseInt(yToken.nextToken());
            arrPoints.add(new Point(xCoordinate, yCoordinate));
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
        
        // set routing
        //GraphConstants.setRouting(map, GraphConstants.ROUTING_SIMPLE);
        // Add a Line End Attribute
        GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
        
        // Set Font
        GraphConstants.setFont(map, new Font("Arial", Font.BOLD, 10));
        
        // Set label position
        GraphConstants.setLabelPosition(map, new Point(500, 500));
        
        // set points
        if (arrPoints.size() > 0)
            GraphConstants.setPoints(map, arrPoints);
        
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Edge with its Attributes
        attributes.put(edge, map);
        // Insert the Edge and its Attributes
        graph.getGraphLayoutCache().insert(new Object[] { edge }, attributes, cs, null, null);
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