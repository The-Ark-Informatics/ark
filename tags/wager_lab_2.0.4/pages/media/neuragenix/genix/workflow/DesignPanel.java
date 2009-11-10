/**
 * DesignPanel.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 09/02/2004
 */

package media.neuragenix.genix.workflow;

/**
 * This class creates panel that let users can 
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.font.LineMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.print.*;
import java.awt.event.ActionListener;

import java.awt.*;
import java.awt.Graphics.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;


import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

// javax packages
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JColorChooser;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

// jGraph packages
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

// neuragenix packages
import neuragenix.genix.workflow.*;
import media.neuragenix.genix.workflow.dialog.*;
import media.neuragenix.genix.workflow.org.jgraph.pad.*;

public class DesignPanel extends JPanel	implements GraphSelectionListener, KeyListener
{
    private static final String VIOLATE_ACTIVE_WORKFLOW = "Unable to update this workflow since itself or one of its sub-workflows is in \"Active\" status.";
    private static final String VIOLATE_INSTANCE_WORKFLOW = "Unable to update this workflow since it or one of its sub-workflows and parent workflow has an instance that is in \"In progress\" status.";
    
    private final int MAXIMUM_DISPLAY_NAME_LENGTH = 15;
    
    private static final ImageIcon personIcon = new ImageIcon(DesignPanel.class.getResource("images/person.gif"));
    
    private static final ImageIcon machineIcon = new ImageIcon(DesignPanel.class.getResource("images/machine.gif"));
    
    protected static final String UNDO_TOOLTIP = "Undo";
    
    protected static final String REDO_TOOLTIP = "Redo";
    
    protected static final String REMOVE_TOOLTIP = "Delete";
    
    protected static final String COPY_TOOLTIP = "Copy";
    
    protected static final String PASTE_TOOLTIP = "Paste";
    
    protected static final String CUT_TOOLTIP = "Cut";
    
    protected static final String PRINTOUT_TOOLTIP = "Print";
    
    protected static final String GROUP_TOOLTIP = "Group";
    
    protected static final String UNGROUP_TOOLTIP = "Ungroup";
    
    protected static final String SAVE_TOOLTIP = "Save";
    
    protected static final String SAVEAS_TOOLTIP = "Save As";
    
    protected static final String NEW_TOOLTIP = "New";
    
    protected static final String TOBACK_TOOLTIP = "To Back";
    
    protected static final String TOFRONT_TOOLTIP = "To Front";
    
    protected static final String ZOOM_TOOLTIP = "Normal Size";
    
    protected static final String ZOOMIN_TOOLTIP = "Zoom In";
    
    protected static final String ZOOMOUT_TOOLTIP = "Zoom Out";
    
    protected static final Dimension DEFAULT_CELL_SIZE = new Dimension(100, 50);
    
    protected static final Dimension DEFAULT_START_CELL_SIZE = new Dimension(50, 50);
    
    protected static final Color DEFAULT_CELL_BORDER_COLOR = Color.blue;
    
    protected static final Color DEFAULT_CELL_BACKGROUND_COLOR = Color.lightGray;
    
    protected static final Color DEFAULT_START_CELL_BORDER_COLOR = Color.black;
    
    protected static final Color DEFAULT_START_CELL_BACKGROUND_COLOR = new Color(0, 255, 102);
    
    protected static final Color DEFAULT_STOP_CELL_BORDER_COLOR = Color.black;
    
    protected static final Color DEFAULT_STOP_CELL_BACKGROUND_COLOR = Color.red;
    
    private static final String DEFAULT_END_CELL_ID_PREFIX = "Stop";

    // JGraph instance
    // Switched to use the subclassed JGraph to allow for printing
    //protected JGraph graph;
       
    protected MyGraph graph;
    
    // Undo Manager
    protected GraphUndoManager undoManager;

    // Actions which Change State
    protected Action undo,
            redo,
            remove,
            group,
            ungroup,
            tofront,
            toback,
            cut,
            copy,
            paste,
            save,
            saveAs,
            saveToFile,
            print_out,
            create,
            zoom,
            zoomin,
            zoomout, done;

    // Holds applet that contains this object
    protected WFDesigner designer;
	
    
    protected Vector savedGraphs = new Vector(10, 5);
    
    
    // Buttons
    protected JToggleButton btLine, btCurve, btQuadratic, btOrtho;
    
    // Indicate if this is a new workflow template
    private boolean isNew = true;
    
    // print setup variables
    private PageFormat printPageFormat;
    //private PrintSetup printSetup;
        
    // keep current package id
    private String strPackageId = null;
    private String strPackageName = null;
    private String strWorkflowId = null;
    private String strWorkflowName = null;
    
    // keep current workflow template
    private WorkflowProcess currentWorkflowProcess = null;
    private neuragenix.genix.workflow.Package currentPackage = null;
    private Hashtable hashTriggers = null;
    private Hashtable hashParameters = null;
	
    // the generated ID for all activities and transitions
    private static final String DEFAULT_PREFIX_GEN_ID   = "";
    private static final int    DEFAULT_STARTING_GEN_ID = 1;    
    private static int genId = DEFAULT_STARTING_GEN_ID;
        
    // Construct an DesignPanel
    public DesignPanel(WFDesigner aDesigner)
    {
        designer = aDesigner;
        
        printPageFormat = new PageFormat();
        //printSetup = new PrintSetup(true, PageFormat.PORTRAIT);
        
        //workflowProcess = aWorkflow;
        
        // Use Border Layout
        setLayout(new BorderLayout());
        // Construct the Graph
        graph = new MyGraph(new MyModel());
        graph.setEditable(false);
        graph.setPortsVisible(false);
        
        //translateWorkflowProcessToGraph(workflowProcess);
        
        //insert(new Point(50, 200), new StartCell("Start"));
        //insert(new Point(500, 200), new EndCell("End"));

        // Construct Command History
        //
        // Create a GraphUndoManager which also Updates the ToolBar
        undoManager = new GraphUndoManager() 
        {
            // Override Superclass
            public void undoableEditHappened(UndoableEditEvent e) 
            {
                // First Invoke Superclass
                super.undoableEditHappened(e);
                // Then Update Undo/Redo Buttons
                updateHistoryButtons();
                
            }
        };

        
        
        
        // Add Listeners to Graph
        //
        // Register UndoManager with the Model
        graph.getModel().addUndoableEditListener(undoManager);
        // Update ToolBar based on Selection Changes
        graph.getSelectionModel().addGraphSelectionListener(this);
        // Listen for Delete Keystroke when the Graph has Focus
        graph.addKeyListener(this);

        // Construct Panel
        //
        // Add a ToolBar
        add(createToolBar(), BorderLayout.NORTH);
        // Add the Graph as Center Component
        add(new JScrollPane(graph), BorderLayout.CENTER);
        
    }
    
    public static void resetGenId()
    {
        genId = DEFAULT_STARTING_GEN_ID;
    }

    public static String getNextGenId()
    {
        return DEFAULT_PREFIX_GEN_ID + genId++;
    }
    
    /**
     * Return graph object
     *
     * Modified 14.07.04 to return subclassed graph object
     *
     *
     */
    public MyGraph getGraph()
    {
        return graph;
    }    
    
    public String getGraphPackageName()
    {
        return graph.getPackageName();
        
    }
    
    public WorkflowProcess getCurrentWFProcess()
    {
        return currentWorkflowProcess;    
    }
    
    public neuragenix.genix.workflow.Package getCurrentPackage()
    {
        return currentPackage;
    }
    
    
    
    public String getGraphPackageId()
    {
        return graph.getPackageId();
        
    }
    
    
    public String getGraphId()
    {
        return graph.getGraphId();
        
    }
    
    
    public String getGraphName()
    {
        return graph.getGraphName();   
    }   
    
    /**
     * Return designer object
     */
    public WFDesigner getWFDesigner()
    {
        return designer;
    }
    
    public void setTriggers(Hashtable hashTriggers)
    {
        this.hashTriggers = hashTriggers;
    }
    
    public void setParameters(Hashtable hashParams)
    {
        this.hashParameters = hashParams;
    }
    
    public void newWorkflow()
    {
        isNew = true;        
        Object[] cells = graph.getRoots();
        cells = graph.getDescendants(cells);
        graph.getModel().remove(cells);

        Point pt = new Point(50, 200);
        DefaultGraphCell cell = new StartCell("Start");                
        insert(pt, DEFAULT_START_CELL_SIZE, cell);

        // Create a new temp Activity for this Start Cell so that 
        // an attribute will be associated - this is needed
        // when a transition is connecting to this start cell                    
        Hashtable hashExtendedAttributes = new Hashtable(10);
        hashExtendedAttributes.put("strType", "Start");
        hashExtendedAttributes.put("intPosX", new Long(Math.round(pt.getX())));
        hashExtendedAttributes.put("intPosY", new Long(Math.round(pt.getY())));
        hashExtendedAttributes.put("intWidth", new Long(Math.round(DEFAULT_START_CELL_SIZE.getWidth())));
        hashExtendedAttributes.put("intHeight", new Long(Math.round(DEFAULT_START_CELL_SIZE.getHeight())));
        hashExtendedAttributes.put("strBorderColor", DEFAULT_START_CELL_BORDER_COLOR);
        hashExtendedAttributes.put("strBackgroundColor", DEFAULT_START_CELL_BACKGROUND_COLOR);

        Activity activity = new Activity("Start",
                                         "Start",
                                         "",
                                         "",
                                         "",
                                         "",
                                         new Hashtable(10),
                                         hashExtendedAttributes);

        // getting cell attributes
        Hashtable cell_attrs = (Hashtable) cell.getAttributes();
        // add the activity as an attribute
        cell_attrs.put("value", activity);
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(cell, cell_attrs);
        // Insert the Vertex and its Attributes (can also use model)
        graph.getGraphLayoutCache().edit(attributes, null, null, null);                     

        // reset the sys gen IDs
        resetGenId();
    }
    
    // Insert a new Vertex at point
    public void insert(Point point, Dimension size, DefaultGraphCell vertex) 
    {     
        // Add one Floating Port
        vertex.add(new DefaultPort("Center"));
        
        // Snap the Point to the Grid
        point = graph.snap(new Point(point));
        
        // Create a Map that holds the attributes for the Vertex
        Map map = GraphConstants.createMap();
        
        // Make Vertex Opaque
        GraphConstants.setOpaque(map, true);
                
        if (vertex instanceof StartCell)
        {
            // Ensure the size has been specified.  Otherwise, use default.
            size = (size == null ? DEFAULT_START_CELL_SIZE : size);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DEFAULT_START_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_START_CELL_BACKGROUND_COLOR);
        }
        else if (vertex instanceof EndCell)
        {
            // Ensure the size has been specified.  Otherwise, use default.
            size = (size == null ? DEFAULT_START_CELL_SIZE : size);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DEFAULT_STOP_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_STOP_CELL_BACKGROUND_COLOR);
        }
        else
        {
            // Ensure the size has been specified.  Otherwise, use default.
            size = (size == null ? DEFAULT_CELL_SIZE : size);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DEFAULT_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_CELL_BACKGROUND_COLOR);
            
            //GraphConstants.setAutoSize(map, true);
        }

        // Add a Bounds Attribute to the Map
        GraphConstants.setBounds(map, new Rectangle(point, size));
        
        // Set Font
        GraphConstants.setFont(map, new Font("Arial", Font.BOLD, 10));
        
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(vertex, map);

        // Insert the Vertex and its Attributes (can also use model)
        graph.getGraphLayoutCache().insert(new Object[] { vertex }, attributes, null, null, null);      
    }

    public boolean validateConnectionPorts(Port source, Port target, boolean showError)
    {
        String strErrorMessage = getConnectionPortsError(source, target);
        boolean isValid = (strErrorMessage.equals("") ? true : false);
        
        // display error if required
        if (!isValid && showError)
            JOptionPane.showMessageDialog(this, strErrorMessage, "Invalid Connection", JOptionPane.ERROR_MESSAGE);
        
        return isValid;
    }
    
    public String getConnectionPortsError(Port source, Port target)
    {
        
        String strErrorMessage = "";
        
        if (graph.getModel().getParent(target) instanceof StartCell)
            strErrorMessage += "You are not allowed to connect to a start point";
        else if (graph.getModel().getParent(source) instanceof EndCell)
            strErrorMessage += "You are not allowed to connect from the stop point";
        else if (graph.getModel().getParent(source) instanceof StartCell)
        {
            // search for an exisiting connection from start
            Object [] cells = graph.getRoots();
            for (int i=0; i < cells.length; i++)
            {
                if (cells[i] instanceof DefaultEdge)
                {
                    DefaultEdge edgeTemp = (DefaultEdge) cells[i];
                    DefaultPort from_port = (DefaultPort) edgeTemp.getSource();
                    DefaultGraphCell from_cell = (DefaultGraphCell) graph.getModel().getParent(from_port);
                    if (from_cell instanceof StartCell)
                    {
                        // there is already a connection coming out of Start
                        strErrorMessage += "There is already a connection from the start. \n" +
                            "Only one connection is allowed";
                        break;
                    }
                }
            }                        
        }
        
        return strErrorMessage;
     }
    
    // Insert a new Edge between source and target
    public void connect(Port source, Port target) 
    {
        // do a validation
        if (!validateConnectionPorts(source, target, true))
            return;
       
        // Connections that will be inserted into the Model
        ConnectionSet cs = new ConnectionSet();
        // Construct Edge with no label (Transition)
        DefaultEdge edge = new DefaultEdge();

        // if connection from the start and is valid, a Transition object 
        // needs to be created to retain the Default Edge
        if ( graph.getModel().getParent(source) instanceof StartCell )
        {
            DefaultGraphCell to_cell = (DefaultGraphCell) graph.getModel().getParent(target);
            Activity to_activity = (Activity) to_cell.getUserObject();

            String strXCoordinator = "";
            String strYCoordinator = "";

            // changing cell attributes
            Hashtable cell_attrs = (Hashtable) edge.getAttributes();
            ArrayList points = (ArrayList) cell_attrs.get("points");
            for (int i=0; i < points.size(); i++)
            {
                Point point = (Point) points.get(i);
                strXCoordinator += point.x + " ";
                strYCoordinator += point.y + " ";
            }            
            
            Transition transition = 
                new Transition("Start transition",
                               "Start",
                               to_activity.getId(),
                               "",
                               "",
                               "Normal",
                               strXCoordinator.trim(), 
                               strYCoordinator.trim());

            // set as the user object
            edge.setUserObject(transition);                      
        }                
        
        // Create Connection between source and target using edge
        cs.connect(edge, source, target);
        // Create a Map thath holds the attributes for the edge
        Map map = GraphConstants.createMap();
        // set routing
        //GraphConstants.setRouting(map, GraphConstants.ROUTING_SIMPLE);
        
        // Add a Line End Attribute
        GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
        
        //GraphConstants.setDisconnectable(map, true);
        
        // Set Font
        GraphConstants.setFont(map, new Font("Arial", Font.BOLD, 10));
        
        // Set label position
        GraphConstants.setLabelPosition(map, new Point(500, 500));
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Edge with its Attributes
        attributes.put(edge, map);
        // Insert the Edge and its Attributes
        graph.getGraphLayoutCache().insert(new Object[] { edge }, attributes, cs, null, null);
    }

    // Create a Group that Contains the Cells
    public void group(Object[] cells) 
    {
        // Order Cells by View Layering
        cells = graph.getGraphLayoutCache().order(cells);
        // If Any Cells in View
        if (cells != null && cells.length > 0) 
        {
            // Create Group Cell
            int count = getCellCount(graph);
            DefaultGraphCell group = new DefaultGraphCell(new Integer(count - 1));
            // Create Change Information
            ParentMap map = new ParentMap();
            // Insert Child Parent Entries
            for (int i = 0; i < cells.length; i++)
                map.addEntry(cells[i], group);
            // Insert into model
            graph.getGraphLayoutCache().insert(new Object[] { group }, null, null, map, null);
        }
    }

    // Returns the total number of cells in a graph
    protected int getCellCount(JGraph graph) {
            Object[] cells = graph.getDescendants(graph.getRoots());
            return cells.length;
    }

    // Ungroup the Groups in Cells and Select the Children
    public void ungroup(Object[] cells) 
    {
        // If any Cells
        if (cells != null && cells.length > 0) 
        {
            // List that Holds the Groups
            ArrayList groups = new ArrayList();
            // List that Holds the Children
            ArrayList children = new ArrayList();
            // Loop Cells
            for (int i = 0; i < cells.length; i++) 
            {
                // If Cell is a Group
                if (isGroup(cells[i])) 
                {
                    // Add to List of Groups
                    groups.add(cells[i]);
                    // Loop Children of Cell
                    for (int j = 0; j < graph.getModel().getChildCount(cells[i]); j++) 
                    {
                        // Get Child from Model
                        Object child = graph.getModel().getChild(cells[i], j);
                        // If Not Port
                        if (!(child instanceof Port))
                            // Add to Children List
                            children.add(child);
                    }
                }
            }
            // Remove Groups from Model (Without Children)
            graph.getGraphLayoutCache().remove(groups.toArray());
            // Select Children
            graph.setSelectionCells(children.toArray());
        }
    }

    // Determines if a Cell is a Group
    public boolean isGroup(Object cell) {
        // Map the Cell to its View
        CellView view = graph.getGraphLayoutCache().getMapping(cell, false);
        if (view != null)
            return !view.isLeaf();
        return false;
    }

    // Brings the Specified Cells to Front
    public void toFront(Object[] c) {
        graph.getGraphLayoutCache().toFront(c);
    }

    // Sends the Specified Cells to Back
    public void toBack(Object[] c) {
        graph.getGraphLayoutCache().toBack(c);
    }

    // Undo the last Change to the Model or the View
    public void undo() {
        try {
            undoManager.undo(graph.getGraphLayoutCache());
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            updateHistoryButtons();
        }
    }

    // Redo the last Change to the Model or the View
    public void redo() {
        try {
            undoManager.redo(graph.getGraphLayoutCache());
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            updateHistoryButtons();
        }
    }

    // Update Undo/Redo Button State based on Undo Manager
    protected void updateHistoryButtons() {
        // The View Argument Defines the Context
        undo.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
        redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
    }

    //
    // Listeners
    //

    // From GraphSelectionListener Interface
    public void valueChanged(GraphSelectionEvent e) {
        // Group Button only Enabled if more than One Cell Selected
        group.setEnabled(graph.getSelectionCount() > 1);
        // Update Button States based on Current Selection
        boolean enabled = !graph.isSelectionEmpty();
        remove.setEnabled(enabled);
        ungroup.setEnabled(enabled);
        tofront.setEnabled(enabled);
        toback.setEnabled(enabled);
        copy.setEnabled(enabled);
        cut.setEnabled(enabled);
        paste.setEnabled(enabled);
    }

    //
    // KeyListener for Delete KeyStroke
    //
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        // Listen for Delete Key Press
        if (e.getKeyCode() == KeyEvent.VK_DELETE)
            // Execute Remove Action on Delete Key Press
            remove.actionPerformed(null);
    }

    
    public class modWorkFlow
    {
        MyGraph graph = null;
        Object[] roots = null;
        neuragenix.genix.workflow.Package wfPackage = null;
        WorkflowProcess wfProcess = null;
        
        
        public modWorkFlow(MyGraph graph, neuragenix.genix.workflow.Package wfPackage, WorkflowProcess wfProcess, Object[] roots)
        {
           this.graph = graph;
           this.wfProcess = wfProcess;
           this.wfPackage = wfPackage;
           this.roots = roots; 
            
        }
         
        public Object[] getRoots()
        {
            return roots;
        }
        
        public MyGraph getGraph()
        {
            return graph;
        }
        
        public neuragenix.genix.workflow.Package getPackage()
        {
            return wfPackage;
        }
        
        public WorkflowProcess getProcess()
        {
            return wfProcess;   
            
        }
        
        
        
    }
    
    
    
    //
    // Custom Graph
    //

    // Defines a Graph that uses the Shift-Button (Instead of the Right
    // Mouse Button, which is Default) to add/remove point to/from an edge.
    
    public class MyGraph extends JGraph implements Printable {

        private String strGraphName = "";
        private String strPackageName = "";
        private String strPackageID = "";
        private String strGraphID = "";
        
        public void setGraphName(String graphName)
        {
            strGraphName = graphName;
        }
        
        public void setGraphId(String graphID)
        {
            strGraphID = graphID;
        }
        
        public void setPackageId(String packageID)
        {
            strPackageID = packageID;
        }
        
        public String getPackageId()
        {
            return strPackageID;
        }
        
        public String getGraphId()
        {
            return strGraphID;
        }
        
        public void setPackageName(String packageName)
        {
            strPackageName = packageName;
        }
        
        public String getPackageName()
        {
            return strPackageName;
        }
        
        public String getGraphName()
        {
            return strGraphName;
        }
        
        
        // Construct the Graph using the Model as its Data Source
        public MyGraph(GraphModel model) {
            super(model);
            // Use a Custom Marquee Handler
            setMarqueeHandler(new MyMarqueeHandler());
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
        
        
        
        
        
        /** Sets up the graph to be printed.
         * Will scale the graph so that it only consumes one page.  This will also
         * scale depending on paper and margins.
         *
         * Also prints out the Graph name, its package, and a time/date stamp
         */        
        public int print(Graphics g, PageFormat pF, int page)
        {
            final Font mFooterFont = new Font("Serif", Font.ITALIC, 10);
            final float mFooterHeight = (float) (0.25 * 72);
            final DateFormat mDateFormat = new SimpleDateFormat();
            String mDateStr = mDateFormat.format(new Date()).toString();

            Graphics2D g2D = (Graphics2D) g.create();
            
            double imageableHeight = pF.getImageableHeight() - mFooterHeight;
        
            if (imageableHeight < 0) 
               imageableHeight = 0;
                      
            g2D.setPaint(Color.black);
            g2D.setFont(mFooterFont);
            LineMetrics metrics = mFooterFont.getLineMetrics(mDateStr, g2D.getFontRenderContext());            
            
            float y = (float) (pF.getImageableY() + pF.getImageableHeight()- metrics.getDescent() - metrics.getLeading());
           
            // add details to the bottom of the graph
            g2D.drawString(getGraphName() + " (" + getPackageName() + ")" + " - " + mDateStr, (int) pF.getImageableX(), (int)y);
            
            
            try 
            {
               setDoubleBuffered(false);  // create a smoother image for printing
                          
               //Rectangle componentBounds = this.getBounds(null);
               Rectangle componentBounds = this.getBounds();
              
               // scalings modified so that default printer margins won't clip the image
               double scaleX = ( pF.getImageableWidth() / componentBounds.width);
               double scaleY = ( pF.getImageableHeight() / componentBounds.height);
               double translateX = pF.getImageableX();
               double translateY = pF.getImageableY();
              
               // uncomment logic if using custom print dialog
               // if ( printSetup.isPrintScaled() && (scaleX < 1 || scaleY < 1) )
               if (scaleX < 1 || scaleY < 1)
               {
                  // use the smaller scale to fit onto the page
                  if (scaleX < scaleY)
                  {
                     scaleY = scaleX;
                  }
                  else
                  {
                     scaleX = scaleY;
                  }
                  
                  // scale the image
                  g2D.scale(scaleX, scaleY);
                  
                  // need to adjust the x and y coordinates when translating as it will be
                  // adjusted by based on the scale
                  translateX /= scaleX;
                  translateY /= scaleY;
               }
              
              
               if (page > 0)  // no need to print multiple pages as workflows should be scaled
               {
                  return NO_SUCH_PAGE;
               }

               g2D.translate(translateX, translateY);
               
               // paint the graphics based on scale and page format 
               this.paint(g2D);             
            }
            catch(Exception e)
            {
                System.err.println("Exception Caught : " + e.getMessage());
            }
           
            finally 
            {
               //turn double buffering back on
               setDoubleBuffered(true);
            }
            return PAGE_EXISTS;
        }

     }

    //
    // Custom Model
    //

    // A Custom Model that does not allow Self-References
    public class MyModel extends DefaultGraphModel {
        // Override Superclass Method
        public boolean acceptsSource(Object edge, Object port) {
            // Source only Valid if not Equal Target
            return (((Edge) edge).getTarget() != port);
        }
        // Override Superclass Method
        public boolean acceptsTarget(Object edge, Object port) {
            // Target only Valid if not Equal Source
            return (((Edge) edge).getSource() != port);
        }
    }
 
    //
    // Custom MarqueeHandler

    // MarqueeHandler that Connects Vertices and Displays PopupMenus
    public class MyMarqueeHandler extends BasicMarqueeHandler {

            // Holds the Start and the Current Point
            protected Point start, current;

            // Holds the First and the Current Port
            protected PortView port, firstPort;
            

            // Override to Gain Control (for PopupMenu and ConnectMode)
            public boolean isForceMarqueeEvent(MouseEvent e) {
                    // If Right Mouse Button we want to Display the PopupMenu
                    
                    if (SwingUtilities.isRightMouseButton(e))
                            // Return Immediately
                            return true;
                    
                    if (e.getClickCount() == 2)
                        return true;
                    // Find and Remember Port
                    port = getSourcePortAt(e.getPoint());
                    // If Port Found and in ConnectMode (=Ports Visible)
                    if (port != null /*&& graph.isPortsVisible()*/)
                            return true;
                    // Else Call Superclass
                    return super.isForceMarqueeEvent(e);
            }

            // Display PopupMenu or Remember Start Location and First Port
            public void mousePressed(final MouseEvent e) {
                    
                     if (e.getClickCount() == 2) 
                     {
                          Point loc = graph.fromScreen(e.getPoint());
                            
                          double pX = loc.x * graph.getScale();
                          double pY = loc.y * graph.getScale();
                            
                            
                          // Find Cell in Model Coordinates
                          //Object cell = graph.getFirstCellForLocation(loc.x, loc.y);
                            
                            
                          Object cell = graph.getFirstCellForLocation((int)pX, (int)pY); // adjusted to allow for zooming
                          displayDialog(e.getPoint(), cell);
                     
                     }
                
                
                
                     // If Right Mouse Button
                    
                     if (SwingUtilities.isRightMouseButton(e)) {
                            // Scale From Screen to Model
                            Point loc = graph.fromScreen(e.getPoint());
                            
                            double pX = loc.x * graph.getScale();
                            double pY = loc.y * graph.getScale();
                            
                            
                            // Find Cell in Model Coordinates
                            //Object cell = graph.getFirstCellForLocation(loc.x, loc.y);
                            
                            
                            Object cell = graph.getFirstCellForLocation((int)pX, (int)pY); // adjusted to allow for zooming
                            
                            // Create PopupMenu for the Cell
                            JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
                            // Display PopupMenu
                            menu.show(graph, e.getX(), e.getY());

                            // Else if in ConnectMode and Remembered Port is Valid
                    } else if (
                            port != null && !e.isConsumed()/* && graph.isPortsVisible()*/) {
                            // Remember Start Location
                            start = graph.toScreen(port.getLocation(null));
                            // Remember First Port
                            firstPort = port;
                            // Consume Event
                            e.consume();
                    } else
                            // Call Superclass
                            super.mousePressed(e);
            }

            // Find Port under Mouse and Repaint Connector
            public void mouseDragged(MouseEvent e) {
                    // If remembered Start Point is Valid
                    if (start != null && !e.isConsumed()) {
                            // Fetch Graphics from Graph
                            Graphics g = graph.getGraphics();
                            // Xor-Paint the old Connector (Hide old Connector)
                            paintConnector(Color.black, graph.getBackground(), g);
                            // Reset Remembered Port
                            port = getTargetPortAt(e.getPoint());
                            
                            // If Port was found then Point to Port Location
                            if (port != null)
                                    current = graph.toScreen(port.getLocation(null));
                            // Else If no Port was found then Point to Mouse Location
                            else
                                    current = graph.snap(e.getPoint());
                            // Xor-Paint the new Connector
                            paintConnector(graph.getBackground(), Color.black, g);
                            // Consume Event
                            e.consume();
                    }
                    Object[] edges = DefaultGraphModel.getEdges(graph.getModel(), graph.getSelectionCells()).toArray();
                    CellView[] edgesViews = graph.getGraphLayoutCache().getMapping(edges);
                    graph.getGraphLayoutCache().update(edgesViews);

                    // Call Superclass
                    super.mouseDragged(e);
            }

            public PortView getSourcePortAt(Point point) {
                    // Scale from Screen to Model
                    Point tmp = graph.fromScreen(new Point(point));
                    // Find a Port View in Model Coordinates and Remember
                    return graph.getPortViewAt(tmp.x, tmp.y);
            }

            // Find a Cell at point and Return its first Port as a PortView
            protected PortView getTargetPortAt(Point point) {
                    // Find Cell at point (No scaling needed here)
                    Object cell = graph.getFirstCellForLocation(point.x, point.y);
                    // Loop Children to find PortView
                    for (int i = 0; i < graph.getModel().getChildCount(cell); i++) {
                            // Get Child from Model
                            Object tmp = graph.getModel().getChild(cell, i);
                            // Get View for Child using the Graph's View as a Cell Mapper
                            tmp = graph.getGraphLayoutCache().getMapping(tmp, false);
                            // If Child View is a Port View and not equal to First Port
                            if (tmp instanceof PortView && tmp != firstPort)
                            {
                                   // Return as PortView
                                    return (PortView) tmp;
                            }
                    }
                    // No Port View found
                    return getSourcePortAt(point);
            }

            // Connect the First Port and the Current Port in the Graph or Repaint
            public void mouseReleased(MouseEvent e) {
                    // If Valid Event, Current and First Port
                    if ( (e != null)         && 
                         !e.isConsumed()     && 
                         (port != null)      && 
                         (firstPort != null) && 
                         (firstPort != port) && 
                         isFirstConnect() ) {
                            
                        // Then Establish Connection
                        Port source = (Port) firstPort.getCell();
                        Port target = (Port) port.getCell();
                        connect(source, target);                        
                        // Consume Event
                        e.consume();

                    }
                    // Repaint the Graph    
                    graph.repaint();
                    // Reset Global Vars
                    firstPort = port = null;
                    start = current = null;
                    // Call Superclass
                    super.mouseReleased(e);
            }

            // check if this is the first edge
            private boolean isFirstConnect()
            {
                Object[] cells = graph.getRoots();
                for (int i=0; i < cells.length; i++)
                {
                    if (cells[i] instanceof Edge)
                    {
                        Edge edge = (Edge) cells[i];
                                                
                        if (edge.getSource().equals(firstPort.getCell()) && edge.getTarget().equals(port.getCell()))
                            return false;
                    }
                }
                
                return true;
            }
            
            // Show Special Cursor if Over Port
            public void mouseMoved(MouseEvent e) {
                    // Check Mode and Find Port
                    if (e != null
                            && getSourcePortAt(e.getPoint()) != null
                            && !e.isConsumed()
                            /*&& graph.isPortsVisible()*/) {
                            // Set Cusor on Graph (Automatically Reset)
                            graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            // Consume Event
                            e.consume();
                    }
                    // Call Superclass
                    super.mouseReleased(e);
            }

            // Use Xor-Mode on Graphics to Paint Connector
            protected void paintConnector(Color fg, Color bg, Graphics g) {
                    // Set Foreground
                    g.setColor(fg);
                    // Set Xor-Mode Color
                    g.setXORMode(bg);
                    // Highlight the Current Port
                    paintPort(graph.getGraphics());
                    // If Valid First Port, Start and Current Point
                    if (firstPort != null && start != null && current != null)
                            // Then Draw A Line From Start to Current Point
                            g.drawLine(start.x, start.y, current.x, current.y);
            }

            // Use the Preview Flag to Draw a Highlighted Port
            protected void paintPort(Graphics g) {
                    // If Current Port is ValidDefaultGraphCell
                    if (port != null) {
                            // If Not Floating Port...
                            boolean o =
                                    (GraphConstants.getOffset(port.getAttributes()) != null);
                            // ...Then use Parent's Bounds
                            Rectangle r =
                                    (o) ? port.getBounds() : port.getParentView().getBounds();
                            // Scale from Model to Screen
                            r = graph.toScreen(new Rectangle(r));
                            // Add Space For the Highlight Border
                            r.setBounds(r.x - 3, r.y - 3, r.width + 6, r.height + 6);
                            // Paint Port in Preview (=Highlight) Mode
                            graph.getUI().paintCell(g, port, r, true);
                    }
            }

    } // End of Editor.MyMarqueeHandler

    //
    //
    //

    //
    // PopupMenu and ToolBar
    //

    //
    //
    //


    public void displayDialog(final Point pt, final Object cell)
    {
       JDialog dialog = null;
       if ( !(cell instanceof DefaultEdge))
        {
            if (cell instanceof RoundedRectangleCell)
                dialog = new ActivityDetailsDialog(DesignPanel.this, cell, DialogConstants.ACTIVITY_TYPE_OPTIONS[4], pt);
            else if ((cell instanceof EllipseCell) && !(cell instanceof EndCell))
                dialog = new ActivityDetailsDialog(DesignPanel.this, cell, DialogConstants.ACTIVITY_TYPE_OPTIONS[1], pt);
            else if ((cell instanceof DefaultGraphCell) && !(cell instanceof StartCell) && !(cell instanceof EndCell))
                dialog = new ActivityDetailsDialog(DesignPanel.this, cell, DialogConstants.ACTIVITY_TYPE_OPTIONS[0], pt);
            else if (cell instanceof StartCell)
                dialog = new TriggerDetailsDialog(DesignPanel.this, cell, hashTriggers, hashParameters);
        }
        else
        {

            DefaultPort from_port = (DefaultPort) ((DefaultEdge) cell).getSource();
            DefaultPort to_port = (DefaultPort) ((DefaultEdge) cell).getTarget();
            DefaultGraphCell from_cell = (DefaultGraphCell) graph.getModel().getParent(from_port);
            DefaultGraphCell to_cell = (DefaultGraphCell) graph.getModel().getParent(to_port);
  
            if (!(from_cell instanceof StartCell))
                dialog = new TransitionDetailsDialog(cell, graph);
        }

        if (dialog != null)
        {
            // show dialog in the center
            Dimension screensize = getToolkit().getScreenSize();
            dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);

            dialog.setVisible(true);
        }   
        
    }
    
    
    //
    // PopupMenu
    //
    public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
        JPopupMenu menu = new JPopupMenu();
        if (cell != null) {
            // Edit
            menu.add(new AbstractAction("Edit") {
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = null;
                    
                    // create appropriate dialog type
                    if ( !(cell instanceof DefaultEdge))
                    {
                        if (cell instanceof RoundedRectangleCell)
                            dialog = new ActivityDetailsDialog(DesignPanel.this, cell, DialogConstants.ACTIVITY_TYPE_OPTIONS[4], pt);
                        else if ((cell instanceof EllipseCell) && !(cell instanceof EndCell))
                            dialog = new ActivityDetailsDialog(DesignPanel.this, cell, DialogConstants.ACTIVITY_TYPE_OPTIONS[1], pt);
                        else if ((cell instanceof DefaultGraphCell) && !(cell instanceof StartCell) && !(cell instanceof EndCell))
                            dialog = new ActivityDetailsDialog(DesignPanel.this, cell, DialogConstants.ACTIVITY_TYPE_OPTIONS[0], pt);
                        else if (cell instanceof StartCell)
                            dialog = new TriggerDetailsDialog(DesignPanel.this, cell, hashTriggers, hashParameters);
                    }
                    else
                    {
                        
                        DefaultPort from_port = (DefaultPort) ((DefaultEdge) cell).getSource();
                        DefaultPort to_port = (DefaultPort) ((DefaultEdge) cell).getTarget();
                        DefaultGraphCell from_cell = (DefaultGraphCell) graph.getModel().getParent(from_port);
                        DefaultGraphCell to_cell = (DefaultGraphCell) graph.getModel().getParent(to_port);
                        
                        if (!(from_cell instanceof StartCell))
                            dialog = new TransitionDetailsDialog(cell, graph);
                    }
                    
                    if (dialog != null)
                    {
                        // show dialog in the center
                        Dimension screensize = getToolkit().getScreenSize();
                        dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);

                        dialog.setVisible(true);
                    }
                }
            });
            
            // Remove
            if (!graph.isSelectionEmpty()) {
                menu.addSeparator();
                menu.add(new AbstractAction("Remove") {
                    public void actionPerformed(ActionEvent e) {
                            remove.actionPerformed(e);
                    }
                });
            }
            
            
            // Format
            if ( !(cell instanceof DefaultEdge)) {
                menu.addSeparator();
                
                JMenu submenu = new JMenu("Format");
                menu.add(submenu);
                // Edit background color
                submenu.add(new AbstractAction("Background color") {
                    public void actionPerformed(ActionEvent e) {
                        JDialog dialog = new ColorSelectionDialog(cell, graph, ColorSelectionDialog.BACKGROUND);
                        
                        // show dialog in the center
                        Dimension screensize = getToolkit().getScreenSize();
                        dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);

                        dialog.setVisible(true);
                    }
                });
                
                // Edit border color
                submenu.add(new AbstractAction("Border color") {
                    public void actionPerformed(ActionEvent e) {
                        JDialog dialog = new ColorSelectionDialog(cell, graph, ColorSelectionDialog.BORDER);
                        
                        // show dialog in the center
                        Dimension screensize = getToolkit().getScreenSize();
                        dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);

                        dialog.setVisible(true);
                    }
                });
            }
        }
        else
        {
              
            // New activity
            JMenu submenu = new JMenu("New activity");
            // Human activity
            submenu.add(new AbstractAction(DialogConstants.ACTIVITY_TYPE_OPTIONS[0]) {
                public void actionPerformed(ActionEvent ev) {

                    JDialog dialog = new ActivityDetailsDialog(DesignPanel.this, null, DialogConstants.ACTIVITY_TYPE_OPTIONS[0], pt);

                    // show dialog in the center
                    Dimension screensize = getToolkit().getScreenSize();
                    dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);

                    dialog.setVisible(true);
                }
            });
            // System activity
            submenu.add(new AbstractAction(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]) {
                public void actionPerformed(ActionEvent ev) {

                    JDialog dialog = new ActivityDetailsDialog(DesignPanel.this, null, DialogConstants.ACTIVITY_TYPE_OPTIONS[1], pt);

                    // show dialog in the center
                    Dimension screensize = getToolkit().getScreenSize();
                    dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);

                    dialog.setVisible(true);
                }
            });
            // Sub-workflow activity
            submenu.add(new AbstractAction(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]) {
                public void actionPerformed(ActionEvent ev) {

                    JDialog dialog = new ActivityDetailsDialog(DesignPanel.this, null, DialogConstants.ACTIVITY_TYPE_OPTIONS[4], pt);

                    // show dialog in the center
                    Dimension screensize = getToolkit().getScreenSize();
                    dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);

                    dialog.setVisible(true);
                }
            });
            // End activity
            submenu.add(new AbstractAction(DialogConstants.ACTIVITY_TYPE_OPTIONS[3]) {
                public void actionPerformed(ActionEvent ev) {
                 
                    DefaultGraphCell cell = new EndCell("Stop");
                    insert(pt, DEFAULT_START_CELL_SIZE, cell);
                    
                    // Create a new temp Activity for this Stop Cell so that 
                    // an attribute will be associated - this is needed
                    // when a transition is connecting to this end cell                    
                    Hashtable hashExtendedAttributes = new Hashtable(10);
                    hashExtendedAttributes.put("strType", "Stop");
                    hashExtendedAttributes.put("intPosX", new Long(Math.round(pt.getX())));
                    hashExtendedAttributes.put("intPosY", new Long(Math.round(pt.getY())));
                    hashExtendedAttributes.put("intWidth", new Long(Math.round(DEFAULT_START_CELL_SIZE.getWidth())));
                    hashExtendedAttributes.put("intHeight", new Long(Math.round(DEFAULT_START_CELL_SIZE.getHeight())));
                    hashExtendedAttributes.put("strBorderColor", DEFAULT_STOP_CELL_BORDER_COLOR);
                    hashExtendedAttributes.put("strBackgroundColor", DEFAULT_STOP_CELL_BACKGROUND_COLOR);
                    
                    Activity activity = new Activity(getNextEndCellId(),
                                                     "Stop",
                                                     "",
                                                     "",
                                                     "",
                                                     "",
                                                     new Hashtable(10),
                                                     hashExtendedAttributes);
                    
                    // getting cell attributes
                    Hashtable cell_attrs = (Hashtable) cell.getAttributes();
                    // add the activity as an attribute
                    cell_attrs.put("value", activity);
                    // Construct a Map from cells to Maps (for insert)
                    Hashtable attributes = new Hashtable();
                    // Associate the Vertex with its Attributes
                    attributes.put(cell, cell_attrs);
                    // Insert the Vertex and its Attributes (can also use model)
                    graph.getGraphLayoutCache().edit(attributes, null, null, null);                   
                }
            });
            menu.add(submenu);
            
            // Remove
            if (!graph.isSelectionEmpty()) {
                menu.addSeparator();
                menu.add(new AbstractAction("Remove") {
                    public void actionPerformed(ActionEvent e) {
                            remove.actionPerformed(e);
                    }
                });
            }
        }
        
        return menu;
    }

    //
    // ToolBar
    //
    public JToolBar createToolBar() 
    {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        // New workflow
        URL newUrl = DesignPanel.class.getResource("images/new.gif");
        ImageIcon newIcon = new ImageIcon(newUrl);
        create = new AbstractAction("", newIcon) {
            public void actionPerformed(ActionEvent e) {
                newWorkflow();
            }
        };
        create.putValue(Action.SHORT_DESCRIPTION, NEW_TOOLTIP);
        toolbar.add(create);
        
            
        // Save
        URL saveUrl = DesignPanel.class.getResource("images/save.gif");
        ImageIcon saveIcon = new ImageIcon(saveUrl);
        save = new AbstractAction("", saveIcon) {
            public void actionPerformed(ActionEvent e) {
                if (isNew)
                {
                    JDialog dialog = new WorkflowDetailsDialog(DesignPanel.this);

                    // show dialog in the center
                    Dimension screensize = getToolkit().getScreenSize();
                    dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);
                    dialog.setVisible(true);
                }
                else
                    updateWorkflow(currentPackage.getId(), currentPackage.getName(), currentWorkflowProcess.getId(), currentWorkflowProcess.getName());
                
                //String strXPDL = toXPDL();
                //designer.interactWithServlet(strXPDL);
            }
        };
        save.putValue(Action.SHORT_DESCRIPTION, SAVE_TOOLTIP);
        toolbar.add(save);

        
        // -------------
        // Save As
        URL saveAsUrl = DesignPanel.class.getResource("images/saveas.gif");
        ImageIcon saveAsIcon = new ImageIcon(saveAsUrl);
        saveAs = new AbstractAction("", saveAsIcon) {
            public void actionPerformed(ActionEvent e) {
                    JDialog dialog = 
                        new WorkflowDetailsDialog( DesignPanel.this, true, 
                        (currentWorkflowProcess == null ? "" : currentWorkflowProcess.getName()), 
                        (currentWorkflowProcess == null ? "" : currentWorkflowProcess.getId()), 
                        (currentPackage         == null ? "" : currentPackage.getName()), 
                        (currentPackage         == null ? "" : currentPackage.getId()) );

                    // show dialog in the center
                    Dimension screensize = getToolkit().getScreenSize();
                    dialog.setLocation(screensize.width/2 - dialog.getWidth()/2, screensize.height/2 - dialog.getHeight()/2);
                    dialog.setVisible(true);
                
                                        
                //String strXPDL = toXPDL();
                //designer.interactWithServlet(strXPDL);
            }
        };
        saveAs.putValue(Action.SHORT_DESCRIPTION, SAVEAS_TOOLTIP);
        toolbar.add(saveAs);
        
        
        // print
        URL print_outUrl = DesignPanel.class.getResource("images/print3.gif");
        ImageIcon print_outIcon = new ImageIcon(print_outUrl);
        print_out = new AbstractAction("", print_outIcon) {
            public void actionPerformed(ActionEvent e) {
                                
                try
                {
                   PrinterJob prnJob = PrinterJob.getPrinterJob();
                   // PageFormat newPf = new PageFormat();
                   // Paper newPaper = new Paper();

                   /*                       
                      This is code used to create custom print setup dialog
                      however we decided to use the native page format
                      dialog so that margins can be adjusted as well.
                                                                           
                   // Show printer setup dialog
                   displayPrintSetup();
                   
                   // check for user cancel
                   if (!prnJob.printDialog())
                     return;
                   
                   // set the image to print and page margin/format
                   prnJob.setPrintable(graph, printSetup.getPageFormat());
                   */
                   
                   // display native margins page
                   PageFormat newFormat = prnJob.pageDialog(printPageFormat);
                   
                   // check for user cancel in the Page Setup dialog
                   if (newFormat == printPageFormat)
                       return;
                   // save the new print page settings
                   printPageFormat = newFormat;
                   
                   // check for user cancel in the Print dialog
                   if (!prnJob.printDialog() )
                       return;
                   
                   // set the image to print and page margin/format
                   prnJob.setPrintable(graph, printPageFormat);
                   
                   prnJob.print();                   
                }
                catch (PrinterException prnEx)
                {
                    prnEx.printStackTrace();
                    System.err.println ("Printing Error : " + prnEx.toString());
                    JOptionPane.showMessageDialog(null, "Printing Error Occured : \n\n " + prnEx.getMessage(), "Error Printing", JOptionPane.ERROR_MESSAGE);
                }
   
                //String strXPDL = toXPDL();
                //designer.interactWithServlet(strXPDL);
            }
        };
        print_out.putValue(Action.SHORT_DESCRIPTION, PRINTOUT_TOOLTIP);
        toolbar.add(print_out);
        
        // save to file
        // can be put back in should a resolution to security manager issues be found
        /*
        URL saveToFileUrl = DesignPanel.class.getResource("images/save.gif");
        ImageIcon saveToFileIcon = new ImageIcon(saveToFileUrl);
        saveToFile = new AbstractAction("", saveToFileIcon) {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Save to file button", "Save To File...", JOptionPane.INFORMATION_MESSAGE);
                createWFImage(graph, "/home/daniel/test.jpg");
                
                
                
                //String strXPDL = toXPDL();
                //designer.interactWithServlet(strXPDL);
            }
        };
        saveToFile.putValue(Action.SHORT_DESCRIPTION, PRINTOUT_TOOLTIP);
        toolbar.add(saveToFile);
        */
        
        
        
        
        
        
        /*// Insert
        URL insertUrl = DesignPanel.class.getResource("images/insert.gif");
        ImageIcon insertIcon = new ImageIcon(insertUrl);
        toolbar.add(new AbstractAction("", insertIcon) {
                public void actionPerformed(ActionEvent e) {
                        insert(new Point(10, 10));
                }
        });

        // Toggle Connect Mode
        URL connectUrl ActivityDetailsDialog dialog = new ActivityDetailsDialog();
                dialog.show();= DesignPanel.class.getResource("images/connecton.gif");
        ImageIcon connectIcon = new ImageIcon(connectUrl);
        toolbar.add(new AbstractAction("", connectIcon) {
                public void actionPerformed(ActionEvent e) {
                        graph.setPortsVisible(!graph.isPortsVisible());
                        URL connectUrl;
                        if (graph.isPortsVisible())
                                connectUrl =
                                        DesignPanel.class.getResource("images/connecton.gif");
                        else
                                connectUrl =
                                        DesignPanel.class.getResource("images/connectoff.gif");
                        ImageIcon connectIcon = new ImageIcon(connectUrl);
                        putValue(SMALL_ICON, connectIcon);
                }
        });*/

        // Undo
        toolbar.addSeparator();
        URL undoUrl = DesignPanel.class.getResource("images/undo.gif");
        ImageIcon undoIcon = new ImageIcon(undoUrl);
        undo = new AbstractAction("", undoIcon) {
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        };
        undo.setEnabled(false);
        undo.putValue(AbstractAction.SHORT_DESCRIPTION, UNDO_TOOLTIP);
        toolbar.add(undo);

        // Redo
        URL redoUrl = DesignPanel.class.getResource("images/redo.gif");
        ImageIcon redoIcon = new ImageIcon(redoUrl);
        redo = new AbstractAction("", redoIcon) {
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        };
        redo.setEnabled(false);
        redo.putValue(AbstractAction.SHORT_DESCRIPTION, REDO_TOOLTIP);
        toolbar.add(redo);

        //
        // Edit Block
        //
        toolbar.addSeparator();
        Action action;
        URL url;

        // Copy
        action = graph.getTransferHandler().getCopyAction();
        url = DesignPanel.class.getResource("images/copy.gif");
        action.putValue(Action.SMALL_ICON, new ImageIcon(url));
        copy = new EventRedirector(action);
        copy.putValue(Action.SHORT_DESCRIPTION, COPY_TOOLTIP);
        toolbar.add(copy);

        // Paste
        action = graph.getTransferHandler().getPasteAction();
        url = DesignPanel.class.getResource("images/paste.gif");
        action.putValue(Action.SMALL_ICON, new ImageIcon(url));
        paste = new EventRedirector(action);
        paste.putValue(Action.SHORT_DESCRIPTION, PASTE_TOOLTIP);
        toolbar.add(paste);

        // Cut
        action = graph.getTransferHandler().getCutAction();
        url = DesignPanel.class.getResource("images/cut.gif");
        action.putValue(Action.SMALL_ICON, new ImageIcon(url));
        cut = new EventRedirector(action);
        cut.putValue(Action.SHORT_DESCRIPTION, CUT_TOOLTIP);
        toolbar.add(cut);

        // Remove
        URL removeUrl = DesignPanel.class.getResource("images/delete.gif");
        ImageIcon removeIcon = new ImageIcon(removeUrl);
        remove = new AbstractAction("", removeIcon) {
            public void actionPerformed(ActionEvent e) {
                if (!graph.isSelectionEmpty()) {
                    Object[] cells = graph.getSelectionCells();
                    
                    // cannot remove start or end cells
                    for (int i=0; i < cells.length; i++)
                        if (cells[i] instanceof StartCell)
                            cells[i] = null;
                    
                    cells = graph.getDescendants(cells);
                    graph.getModel().remove(cells);
                }
            }
        };
        remove.setEnabled(false);
        remove.putValue(Action.SHORT_DESCRIPTION, REMOVE_TOOLTIP);
        toolbar.add(remove);

        // Zoom Std
        toolbar.addSeparator();
        URL zoomUrl = DesignPanel.class.getResource("images/zoom.gif");
        ImageIcon zoomIcon = new ImageIcon(zoomUrl);
        zoom = new AbstractAction("", zoomIcon) {
            public void actionPerformed(ActionEvent e) {
                graph.setScale(1.0);
            }
        };
        zoom.putValue(Action.SHORT_DESCRIPTION, ZOOM_TOOLTIP);
        toolbar.add(zoom);
        
        // Zoom In
        URL zoomInUrl = DesignPanel.class.getResource("images/zoomin.gif");
        ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
        zoomin = new AbstractAction("", zoomInIcon) {
            public void actionPerformed(ActionEvent e) {
                graph.setScale(2 * graph.getScale());
            }
        };
        zoomin.putValue(Action.SHORT_DESCRIPTION, ZOOMIN_TOOLTIP);
        toolbar.add(zoomin);
        
        // Zoom Out
        URL zoomOutUrl = DesignPanel.class.getResource("images/zoomout.gif");
        ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
        zoomout = new AbstractAction("", zoomOutIcon) {
            public void actionPerformed(ActionEvent e) {
                graph.setScale(graph.getScale() / 2);
            }
        };
        zoomout.putValue(Action.SHORT_DESCRIPTION, ZOOMOUT_TOOLTIP);
        toolbar.add(zoomout);
        
        // Group
        toolbar.addSeparator();
        URL groupUrl = DesignPanel.class.getResource("images/group.gif");
        ImageIcon groupIcon = new ImageIcon(groupUrl);
        group = new AbstractAction("", groupIcon) {
            public void actionPerformed(ActionEvent e) {
                group(graph.getSelectionCells());
            }
        };
        group.setEnabled(false);
        group.putValue(Action.SHORT_DESCRIPTION, GROUP_TOOLTIP);
        toolbar.add(group);

        // Ungroup
        URL ungroupUrl = DesignPanel.class.getResource("images/ungroup.gif");
        ImageIcon ungroupIcon = new ImageIcon(ungroupUrl);
        ungroup = new AbstractAction("", ungroupIcon) {
            public void actionPerformed(ActionEvent e) {
                ungroup(graph.getSelectionCells());
            }
        };
        ungroup.setEnabled(false);
        ungroup.putValue(Action.SHORT_DESCRIPTION, UNGROUP_TOOLTIP);
        toolbar.add(ungroup);

        // To Front
        toolbar.addSeparator();
        URL toFrontUrl = DesignPanel.class.getResource("images/tofront.gif");
        ImageIcon toFrontIcon = new ImageIcon(toFrontUrl);
        tofront = new AbstractAction("", toFrontIcon) {
            public void actionPerformed(ActionEvent e) {
                if (!graph.isSelectionEmpty())
                    toFront(graph.getSelectionCells());
            }
        };
        tofront.setEnabled(false);
        tofront.putValue(Action.SHORT_DESCRIPTION, TOFRONT_TOOLTIP);
        toolbar.add(tofront);

        // To Back
        URL toBackUrl = DesignPanel.class.getResource("images/toback.gif");
        ImageIcon toBackIcon = new ImageIcon(toBackUrl);
        toback = new AbstractAction("", toBackIcon) {
            public void actionPerformed(ActionEvent e) {
                if (!graph.isSelectionEmpty())
                    toBack(graph.getSelectionCells());
            }
        };
        toback.setEnabled(false);
        toback.putValue(Action.SHORT_DESCRIPTION, TOBACK_TOOLTIP);
        toolbar.add(toback);
        
        // edge types
        /*toolbar.addSeparator();
        btLine = new JToggleButton("Line");
        btLine.setSelected(true);
        btCurve = new JToggleButton("Curve");
        btQuadratic = new JToggleButton("Quadratic");
        btOrtho = new JToggleButton("Ortho");
        ButtonGroup btGroup = new ButtonGroup();
        btGroup.add(btLine);
        btGroup.add(btCurve);
        btGroup.add(btQuadratic);
        btGroup.add(btOrtho);
        toolbar.add(btLine);
        toolbar.add(btCurve);
        toolbar.add(btQuadratic);
        toolbar.add(btOrtho);*/
        
        /*done = new AbstractAction("Done") {
            public void actionPerformed(ActionEvent e) {
                getWorkflowInstance("1");
            }
        };
        
        toolbar.add(done);*/

        return toolbar;
    }
 
 /*
  * Uncomment code to use custom print setup dialog
  *   
    public void displayPrintSetup() {

        JRadioButton portrait = new JRadioButton("Portrait");
        JRadioButton landscape = new JRadioButton("Landscape");        
        // group buttons
        ButtonGroup types = new ButtonGroup();
        types.add(portrait);
        types.add(landscape);
        
        // set selected radio button based on PrintSetup
        if (printSetup.getPageFormat().getOrientation() == PageFormat.PORTRAIT)
            portrait.setSelected(true);
        else
            landscape.setSelected(true);               
        
        JPanel orientationPanel = new JPanel(new BorderLayout()); 
        orientationPanel.add(new JLabel("Orientation"), BorderLayout.NORTH);
        orientationPanel.add(portrait, BorderLayout.WEST);
        orientationPanel.add(landscape, BorderLayout.EAST);
        
        JCheckBox scaleToFit = 
            new JCheckBox("Scale to fit one page", printSetup.isPrintScaled());
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(orientationPanel, BorderLayout.NORTH);
        contentPanel.add(scaleToFit, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(this, contentPanel, "Print Setup", JOptionPane.OK_OPTION);
        
        printSetup.setOrientation(portrait.isSelected() ? PageFormat.PORTRAIT : PageFormat.LANDSCAPE);
        printSetup.setPrintScaled(scaleToFit.isSelected());
    }
    
    protected class PrintSetup {       
        // scale by default
        private boolean isPrintScaled = true;       
        private PageFormat pageFormat = null;
        
        protected PrintSetup(boolean isPrintScaled, int orientation) {
            this.isPrintScaled = isPrintScaled;
            pageFormat = new PageFormat();
            pageFormat.setOrientation(orientation);
        }
        public void setPrintScaled(boolean isPrintScaled)
        {
            this.isPrintScaled = isPrintScaled;
        }
        public void setOrientation(int orientation)
        {
            pageFormat.setOrientation(orientation);
        }
        public boolean isPrintScaled()
        {
            return isPrintScaled;
        }
        public PageFormat getPageFormat()
        {
            return pageFormat;   
        }        
    }
*/
    // This will change the source of the actionevent to graph.
    protected class EventRedirector extends AbstractAction {

        protected Action action;

        // Construct the "Wrapper" Action
        public EventRedirector(Action a) {
            super("", (ImageIcon) a.getValue(Action.SMALL_ICON));
            this.action = a;
        }

        // Redirect the Actionevent
        public void actionPerformed(ActionEvent e) {
            e = new ActionEvent(
                            graph,
                            e.getID(),
                            e.getActionCommand(),
                            e.getModifiers());
            action.actionPerformed(e);
        }
    }

    /**
     * Transform the workflow into XPDL format
     */
    protected String toXPDL(String packageID, String packageName, String wfID, String wfName)
    {
        //graph.updateUI();
        String initActivity = "";
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        int intStopCellCount = 0;
        Hashtable hashStopCells = new Hashtable(10);
        
        // create Package & Package Header
        StringBuffer strHeader = new StringBuffer();
        strHeader.append("<?xml version='1.0' encoding='utf-8'?><Package Id=\"");
        strHeader.append(packageID);
        strHeader.append("\" Name=\"");
        strHeader.append(packageName);
        strHeader.append("\">");
        strHeader.append("<PackageHeader><XPDLVersion>1.0</XPDLVersion><Vendor>Neuragenix</Vendor>");
        strHeader.append("<Created>" + fm.format(new java.util.Date()) + "</Created>");
        strHeader.append("<Description></Description></PackageHeader>");
        
        // Participants
        StringBuffer strParticipants = new StringBuffer();
        strParticipants.append("<Participants></Participants>");
        
        // Workflow template
        StringBuffer strWorkflows = new StringBuffer();
        strWorkflows.append("<WorkflowProcesses><WorkflowProcess Id=\"");
        strWorkflows.append(wfID);
        strWorkflows.append("\" Name=\"");
        strWorkflows.append(wfName);
        strWorkflows.append("\" InitialActivityId=\"");

        // workflow parameters
        StringBuffer strWorkflowParams = new StringBuffer();
        strWorkflowParams.append("<Parameters>");
        if (hashParameters != null)
        {
            Enumeration enumParams = hashParameters.elements();
            while (enumParams.hasMoreElements())
            {
                Parameter currentParam = (Parameter) enumParams.nextElement(); 
                strWorkflowParams.append("<Parameter Name=\"");
                strWorkflowParams.append(currentParam.getName());
                strWorkflowParams.append("\" Type=\"");
                strWorkflowParams.append(currentParam.getType());
                strWorkflowParams.append("\" Desc=\"");
                strWorkflowParams.append(currentParam.getDescription());
                strWorkflowParams.append("\" />");
            }
        }
        strWorkflowParams.append("</Parameters>");
        
        // workflow triggers
        StringBuffer strWorkflowTriggers = new StringBuffer();
        strWorkflowTriggers.append("<Triggers>");
        if (hashTriggers != null)
        {
            Enumeration enumTriggers = hashTriggers.elements();
            while (enumTriggers.hasMoreElements())
            {
                Trigger currentTrigger = (Trigger) enumTriggers.nextElement(); 
                strWorkflowTriggers.append("<Trigger Id=\"");
                strWorkflowTriggers.append(currentTrigger.getId());
                strWorkflowTriggers.append("\" Name=\"");
                strWorkflowTriggers.append(currentTrigger.getName());
                strWorkflowTriggers.append("\" Action=\"");
                strWorkflowTriggers.append(currentTrigger.getAction());
                strWorkflowTriggers.append("\">");
                strWorkflowTriggers.append("<Domain>" + currentTrigger.getDomain() + "</Domain>");
                strWorkflowTriggers.append("<Field>" + currentTrigger.getField() + "</Field>");
                strWorkflowTriggers.append("<Type>" + currentTrigger.getType() + "</Type>");
                strWorkflowTriggers.append("<Connection1>" + (currentTrigger.getConnection1() == null || currentTrigger.getConnection1().equals("null") ? "" : currentTrigger.getConnection1()) + "</Connection1>");
                
                if (currentTrigger.getOperator1().equals("<"))
                    strWorkflowTriggers.append("<Operator1>&lt;</Operator1>");
                
                else if (currentTrigger.getOperator1().equals(">"))
                    strWorkflowTriggers.append("<Operator1>&gt;</Operator1>");
                
                else if (currentTrigger.getOperator1().equals(">="))
                    strWorkflowTriggers.append("<Operator1>&gt;=</Operator1>");
                
                else if (currentTrigger.getOperator1().equals("<="))
                    strWorkflowTriggers.append("<Operator1>&lt;=</Operator1>");
                
                else
                    strWorkflowTriggers.append("<Operator1>" + (currentTrigger.getOperator1() == null || currentTrigger.getOperator1().equals("null") ? "" : currentTrigger.getOperator1()) + "</Operator1>");
                
                strWorkflowTriggers.append("<Value1>" + (currentTrigger.getValue1() == null || currentTrigger.getValue1().equals("null") ? "" : currentTrigger.getValue1()) + "</Value1>");
                strWorkflowTriggers.append("<Connection2>" + (currentTrigger.getConnection2() == null || currentTrigger.getConnection2().equals("null") ? "" : currentTrigger.getConnection2()) + "</Connection2>");
                
                if (currentTrigger.getOperator1().equals("<"))
                   strWorkflowTriggers.append("<Operator2>&lt;</Operator2>");
                else if (currentTrigger.getOperator1().equals(">"))
                   strWorkflowTriggers.append("<Operator2>&gt;</Operator2>");
                else
                   strWorkflowTriggers.append("<Operator2>" + (currentTrigger.getOperator2() == null || currentTrigger.getOperator2().equals("null") ? "" : currentTrigger.getOperator2()) + "</Operator2>");
                
                strWorkflowTriggers.append("<Value2>" + (currentTrigger.getValue2() == null || currentTrigger.getValue2().equals("null") ? "" : currentTrigger.getValue2()) + "</Value2>");
                strWorkflowTriggers.append("</Trigger>");
            }
        }
        strWorkflowTriggers.append("</Triggers>");
        
        // Activities
        StringBuffer strActivities = new StringBuffer();
        strActivities.append("<Activities>");
        
        StringBuffer strTransitions = new StringBuffer();
        strTransitions.append("<Transitions>");
        
        Object[] cells = graph.getRoots();
        //boolean firstActivity = true;
        for (int i=0; i < cells.length; i++)
        {
            // if cell is an activity
            if ( !(cells[i] instanceof DefaultEdge))
            {
                
                
                // get activity attributes
                DefaultGraphCell current_cell = (DefaultGraphCell) cells[i];
                Hashtable cell_attrs = (Hashtable) current_cell.getAttributes();
                
                // the current cell is a Start cell
                if (current_cell instanceof StartCell)
                {
                    // add activity opening tag
                    strActivities.append("<Activity Id=\"Start\" Name=\"Start\">");
                    // description
                    strActivities.append("<Description></Description>");
                    // add performer
                    strActivities.append("<Performer></Performer>");
                    // add performer
                    strActivities.append("<PerformerType></PerformerType>");
                    // add priority
                    strActivities.append("<Priority></Priority>");
                    // add params
                    strActivities.append("<Params></Params>");
                    
                    Rectangle cell_bounds = (Rectangle) cell_attrs.get("bounds");
                    // extended attributes
                    strActivities.append("<ExtendedAttributes>");
                    strActivities.append("<ExtendedAttribute Name=\"strType\" Value=\"Start\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intPosX\" Value=\"" + Math.round(cell_bounds.getX()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intPosY\" Value=\"" + Math.round(cell_bounds.getY()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intWidth\" Value=\"" + Math.round(cell_bounds.getWidth()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intHeight\" Value=\"" + Math.round(cell_bounds.getHeight()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strBorderColor\" Value=\"Black\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strBackgroundColor\" Value=\"White\"/>");
                    strActivities.append("</ExtendedAttributes>");
                    strActivities.append("</Activity>");
                }
                
                // the current cell is a End cell
                else if (current_cell instanceof EndCell)
                {
                    intStopCellCount++;
                    hashStopCells.put(current_cell, new Integer(intStopCellCount));
                    
                    // add activity opening tag
                    strActivities.append("<Activity Id=\"Stop" + intStopCellCount + "\" Name=\"Stop\">");
                    // description
                    strActivities.append("<Description></Description>");
                    // add performer
                    strActivities.append("<Performer></Performer>");
                    // add performer
                    strActivities.append("<PerformerType></PerformerType>");
                    // add priority
                    strActivities.append("<Priority></Priority>");
                    // add params
                    strActivities.append("<Params></Params>");
                    
                    Rectangle cell_bounds = (Rectangle) cell_attrs.get("bounds");
                    // extended attributes
                    strActivities.append("<ExtendedAttributes>");
                    strActivities.append("<ExtendedAttribute Name=\"strType\" Value=\"Stop\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intPosX\" Value=\"" + Math.round(cell_bounds.getX()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intPosY\" Value=\"" + Math.round(cell_bounds.getY()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intWidth\" Value=\"" + Math.round(cell_bounds.getWidth()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intHeight\" Value=\"" + Math.round(cell_bounds.getHeight()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strBorderColor\" Value=\"Black\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strBackgroundColor\" Value=\"White\"/>");
                    strActivities.append("</ExtendedAttributes>");
                    strActivities.append("</Activity>");
                }
                
                // adding attributes to XPDL string
                else if (cell_attrs.get("value") != null && (cell_attrs.get("value") instanceof Activity))
                {
                    // add activity opening tag
                    strActivities.append("<Activity Id=\"");
                    
                    
                    Activity currentActivity = (Activity) cell_attrs.get("value");
                    Rectangle cell_bounds = (Rectangle) cell_attrs.get("bounds");
                    
                    // add activity Id
                    strActivities.append(currentActivity.getId() + "\" Name=\"");
                    // add activity name
                    strActivities.append(cleanForXSL(currentActivity.getName()) + "\">");
                    // add description
                    strActivities.append("<Description>" + cleanForXSL(currentActivity.getDescription()) + "</Description>");
                    // add performer
                    strActivities.append("<Performer>" + cleanForXSL(currentActivity.getPerformer()) + "</Performer>");
                    // add performer
                    strActivities.append("<PerformerType>" + cleanForXSL(currentActivity.getPerformerType()) + "</PerformerType>");
                    // add priority
                    strActivities.append("<Priority>" + cleanForXSL(currentActivity.getPriority()) + "</Priority>");
                    
                    
                    // add parameters
                    strActivities.append("<Params>");
                    Hashtable hashParams = currentActivity.getParameters();
                    Enumeration enum = hashParams.elements();
                    while (enum.hasMoreElements())
                    {
                        Parameter param = (Parameter) enum.nextElement();
                        strActivities.append("<Param Name=\"" + cleanForXSL(param.getName()) + "\" Type=\"" + cleanForXSL(param.getType()) + "\" Desc=\"" + cleanForXSL(param.getDescription()) + "\" />");
                    }
                    strActivities.append("</Params>");
                    
                    // add extended attributes
                    strActivities.append("<ExtendedAttributes>");
                    Hashtable hashExtendedAttrs = currentActivity.getExtendedAttributes();
                    strActivities.append("<ExtendedAttribute Name=\"strReassignable\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strReassignable")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strSignoff\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strSignoff")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strInstruction\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strInstruction")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intFunctionKey\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("intFunctionKey")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strIfFail\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strIfFail")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"flRetryDelayValue\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("flRetryDelayValue")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strRetryDelayUnit\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strRetryDelayUnit")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intRetryCounter\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("intRetryCounter")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strIfFailAfterRetry\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strIfFailAfterRetry")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"flTriggerValue\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("flTriggerValue")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strTriggerUnit\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strTriggerUnit")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"flFinishedValue\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("flFinishedValue")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strFinishedUnit\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strFinishedUnit")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strIntervalValue\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strIntervalValue")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strIntervalUnit\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strIntervalUnit")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intCompletedTask\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("intCompletedTask")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strMultiAction\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strMultiAction")) + "\"/>");
                    
                    strActivities.append("<ExtendedAttribute Name=\"intWorkingDays\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("intWorkingDays")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strSendTriggerAlertToPerformer\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strSendTriggerAlertToPerformer")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intSendTriggerAlertToOtherKey\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("intSendTriggerAlertToOtherKey")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strSendTriggerAlertToOtherType\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strSendTriggerAlertToOtherType")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strRecurringTriggerAlert\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strRecurringTriggerAlert")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strRecurringTriggerAlertUnit\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strRecurringTriggerAlertUnit")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"flRecurringTriggerAlertValue\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("flRecurringTriggerAlertValue")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strSendFinishAlertToPerformer\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strSendFinishAlertToPerformer")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intSendFinishAlertToOtherKey\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("intSendFinishAlertToOtherKey")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strSendFinishAlertToOtherType\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strSendFinishAlertToOtherType")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strRecurringFinishAlert\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strRecurringFinishAlert")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strRecurringFinishAlertUnit\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strRecurringFinishAlertUnit")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"flRecurringFinishAlertValue\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("flRecurringFinishAlertValue")) + "\"/>");
                    
                    // sub workflows
                    strActivities.append("<ExtendedAttribute Name=\"strSubWorkflowPackageXPDLId\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strSubWorkflowPackageXPDLId")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strSubWorkflowTemplateXPDLId\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strSubWorkflowTemplateXPDLId")) + "\"/>");
                    
                    strActivities.append("<ExtendedAttribute Name=\"strType\" Value=\"" + cleanForXSL((String) hashExtendedAttrs.get("strType")) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intPosX\" Value=\"" + Math.round(cell_bounds.getX()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intPosY\" Value=\"" + Math.round(cell_bounds.getY()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intWidth\" Value=\"" + Math.round(cell_bounds.getWidth()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"intHeight\" Value=\"" + Math.round(cell_bounds.getHeight()) + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strBorderColor\" Value=\"" + hashExtendedAttrs.get("strBorderColor") + "\"/>");
                    strActivities.append("<ExtendedAttribute Name=\"strBackgroundColor\" Value=\"" + hashExtendedAttrs.get("strBackgroundColor") + "\"/>");
                    strActivities.append("</ExtendedAttributes>");
                    
                    
                
                    // add activity closing tag
                    strActivities.append("</Activity>");
                }
                
            }
            
        }
        
        // edges
        for (int i=0; i < cells.length; i++)
        {
            // if cell is a transition
            if (cells[i] instanceof DefaultEdge)
            {
                String strXCoordinator = "";
                String strYCoordinator = "";
                
                // get transition attributes
                DefaultEdge current_edge = (DefaultEdge) cells[i];
                Hashtable edge_attrs = (Hashtable) current_edge.getAttributes();
                
                //ArrayList points = (ArrayList) edge_attrs.get("points");
                if (edge_attrs.get("points") instanceof ArrayList)
                {
                    ArrayList points = (ArrayList) edge_attrs.get("points");
                    for (int j=0; j < points.size(); j++)
                    {
                        Point point = (Point) points.get(j);
                        strXCoordinator += point.x + " ";
                        strYCoordinator += point.y + " ";
                    }
                    strXCoordinator.trim();
                    strYCoordinator.trim();
                }
                
                
                // getting source and target activities
                DefaultPort from_port = (DefaultPort) current_edge.getSource();
                DefaultPort to_port = (DefaultPort) current_edge.getTarget();
                DefaultGraphCell from_cell = (DefaultGraphCell) graph.getModel().getParent(from_port);
                DefaultGraphCell to_cell = (DefaultGraphCell) graph.getModel().getParent(to_port);

                if (from_cell instanceof StartCell)
                {
                    Activity to_activity = (Activity) to_cell.getUserObject();

                    // add activity opening tag
                    strTransitions.append("<Transition Id=\"");

                    //Transition currentTransition = (Transition) edge_attrs.get("value");

                    // add transition Id
                    strTransitions.append("Start transition" + "\" From=\"");                    
                    // add transition From
                    strTransitions.append("Start" + "\" To=\"");
                    // add transition To
                    strTransitions.append(cleanForXSL(to_activity.getId()) + "\" Name=\"");
                    // add transition name
                    strTransitions.append("" + "\" Condition=\"");
                    // add transition condition
                    strTransitions.append("\" Type=\"Normal\" XCoord=\"" + strXCoordinator + "\" YCoord=\"" + strYCoordinator + "\">");

                    // add transition closing tag
                    strTransitions.append("</Transition>");                    
                    
                    initActivity = cleanForXSL(to_activity.getId());        
                }
                else if (to_cell instanceof EndCell)
                {
                    Object objCurrentTransition = edge_attrs.get("value");
                    
                    String strStopCellID = "Stop" + hashStopCells.get(to_cell);
                    Activity from_activity = (Activity) from_cell.getUserObject();

                    // add activity opening tag
                    strTransitions.append("<Transition Id=\"");

                    if (objCurrentTransition instanceof Transition)
                    {
                        Transition currentTransition = (Transition) objCurrentTransition;
                        
                        // add transition Id
                        strTransitions.append(cleanForXSL(currentTransition.getId()) + "\" From=\"");                   
                        // add transition From
                        strTransitions.append(cleanForXSL(from_activity.getId()) + "\" To=\"");
                        // add transition To
                        strTransitions.append(strStopCellID + "\" Name=\"");
                        // add transition name
                        strTransitions.append(cleanForXSL(currentTransition.getName()) + "\" Condition=\"");
                        // add transition condition
                        strTransitions.append(cleanForXSL(currentTransition.getCondition()) + "\" Type=\"");

                        strTransitions.append(currentTransition.getType() + "\" XCoord=\"" + strXCoordinator + "\" YCoord=\"" + strYCoordinator + "\">");
                    }
                    else
                    {
                        // add transition Id
                        strTransitions.append(strStopCellID + "\" From=\"");                    
                        // add transition From
                        strTransitions.append(cleanForXSL(from_activity.getId()) + "\" To=\"");
                        // add transition To
                        strTransitions.append(strStopCellID + "\" Name=\"");
                        // add transition name
                        strTransitions.append("" + "\" Condition=\"");
                        // add transition condition
                        strTransitions.append("\" Type=\"Normal\" XCoord=\"" + strXCoordinator + "\" YCoord=\"" + strYCoordinator + "\">");
                    }

                    // add transition closing tag
                    strTransitions.append("</Transition>");
                }
                else
                {

                    Activity from_activity = (Activity) from_cell.getUserObject();
                    Activity to_activity = (Activity) to_cell.getUserObject();

                    // add activity opening tag
                    strTransitions.append("<Transition Id=\"");

                    Transition currentTransition = (Transition) edge_attrs.get("value");

                    // add transition Id
                    strTransitions.append(cleanForXSL(currentTransition.getId()) + "\" From=\"");                    
                    // add transition From
                    strTransitions.append(cleanForXSL(from_activity.getId()) + "\" To=\"");
                    // add transition To
                    strTransitions.append(cleanForXSL(to_activity.getId()) + "\" Name=\"");
                    // add transition name
                    strTransitions.append(cleanForXSL(currentTransition.getName()) + "\" Condition=\"");
                    // add transition condition
                    strTransitions.append(cleanForXSL(currentTransition.getCondition()) + "\" Type=\"");

                    strTransitions.append(currentTransition.getType() + "\" XCoord=\"" + strXCoordinator + "\" YCoord=\"" + strYCoordinator + "\">");
                    // add transition closing tag
                    strTransitions.append("</Transition>");
                    
                }
                    
                
            }
        }

        // Workflow template continue...
        strWorkflows.append(initActivity);        
        strWorkflows.append("\"><ProcessHeader>");
        strWorkflows.append("<Created>" + fm.format(new java.util.Date()) + "</Created>");
        strWorkflows.append("<Description></Description></ProcessHeader><Triggers></Triggers>");        
        
        strActivities.append("</Activities>");
        strTransitions.append("</Transitions>");
        
        return strHeader.toString() + strWorkflows.toString() + strWorkflowParams.toString() + strWorkflowTriggers.toString() +strActivities.toString() + strTransitions.toString() + "</WorkflowProcess></WorkflowProcesses></Package>";
        
    }
    
 
    
    
    private void createWFImage(MyGraph theGraph, String fileName)
    {
        MyGraph tempGraph = theGraph;
        
        
        try
        {
            Object[] cells = tempGraph.getRoots();

            if (cells.length > 0) 
            {
                tempGraph.setVisible(true);
                JFrame frame = new JFrame();
                frame.getContentPane().add(tempGraph);
                frame.pack();

                Rectangle bounds = tempGraph.getCellBounds(cells).getBounds();
                graph.toScreen(bounds);

                // Create a Buffered Image
                Dimension d = bounds.getSize();
                BufferedImage img =
                    new BufferedImage(
                        d.width + 10,
                        d.height + 10,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setColor(tempGraph.getBackground());
                graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

                Object[] selection = tempGraph.getSelectionCells();
                boolean gridVisible = tempGraph.isGridVisible();
                tempGraph.setGridVisible(false);
                tempGraph.clearSelection();

                tempGraph.paint(graphics);

                tempGraph.setSelectionCells(selection);
                tempGraph.setGridVisible(gridVisible);

                ImageIO.write(img, "jpg", new File("testimg.jpg"));
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error occured whilst saving image file : " + e.getMessage());
            
        }
        
        return;     
        
    }
    
    
    
    
    
    
    /** Checks for a complete workflow.  Will also display a dialog to the user
     * explaining any changes they may need to make to the workflow before it can
     * be considred correct and as such be saved.
     * @return Returns a boolean based on the success of the function.
     */
    private boolean validateWorkFlow()
    {
        Object[] cells = graph.getRoots();
        boolean isFlowComplete = true; // function wide switch to determine if workflow is valid
        //boolean isStarted = false;
        //boolean isEnded = false;
        
        String strErrorMessage = "The workflow is unable to be saved due to : \n";
        
        // validate activities
        for (int i = 0; i < cells.length; i++)
        {
            DefaultGraphCell current_cell = (DefaultGraphCell) cells[i];
            Hashtable cell_attrs = (Hashtable) current_cell.getAttributes();
            
            
            /*
             * This is not needed as this will be checked when validating edges
             *
            if (cells[i] instanceof StartCell) // Ensure wf has starting and ending point
                isStarted = true;
            if (cells[i] instanceof EndCell)
               isEnded = true;
            */
                        
            // find activities
            if (cell_attrs.get("value") instanceof Activity)
            {
               boolean isConnected = false; // reset
               
               Activity currentActivity = (Activity) cell_attrs.get("value");
               
               // do not check if its the Start Acitivity
               if (currentActivity.getId().equals("Start"))
                   continue;
               
               for (int j=0; !isConnected && (j < cells.length); j++)
               {                   
                   if (cells[j] instanceof DefaultEdge) // found an edge
                   {
                       DefaultEdge edgeTemp = (DefaultEdge) cells[j];
                       DefaultPort from_port = (DefaultPort) edgeTemp.getSource();
                       DefaultPort to_port = (DefaultPort) edgeTemp.getTarget();
                       DefaultGraphCell from_cell = (DefaultGraphCell) graph.getModel().getParent(from_port);
                       DefaultGraphCell to_cell = (DefaultGraphCell) graph.getModel().getParent(to_port);
                   
                       if (to_cell == current_cell) // is connected
                           isConnected = true;                           
                   }
               }

               // append error message if not connected 
               if ( !isConnected )  
               {                       
                   strErrorMessage += " - The Activity " + shortenActivityName(currentActivity.getName()) + " is not correctly connected to the workflow \n";
                   isFlowComplete = false;
               }
            }
        }
      
        // validate edges
        String extErrorMessage = getTransitionsError();
        if (!extErrorMessage.equals(""))
        {
            strErrorMessage += extErrorMessage; // append the errors from transtion validations
            isFlowComplete = false;
        }
            
        // display error message if flow is not complete!
        if (!isFlowComplete)
        {
           strErrorMessage += "\n Please review the workflow and try again";
           JOptionPane.showMessageDialog(this, strErrorMessage, "Workflow Connection Missing", JOptionPane.ERROR_MESSAGE);
           return false;
        }
        
        return true; // Workflow is correct and complete                
    }
    
    private String shortenActivityName(String activityName)
    {
       String strActName = "";
       
       if (activityName.length() > MAXIMUM_DISPLAY_NAME_LENGTH)
       {
           strActName = activityName.substring(0, MAXIMUM_DISPLAY_NAME_LENGTH) + "...";
           return strActName;
       }
       else
           return activityName;
       
    }
    
    /** Saves the current workflow
     */
    public void saveWorkflow(String packageID, String packageName, String wfID, String wfName)
    {
        /*strPackageId = packageID;
        strPackageName = packageName;
        strWorkflowId = wfID;
        strWorkflowName = wfName;
        isNew = false;*/
                
        if (validateWorkFlow() == false) 
            return;
        if (validateActivityIDs() == false)
            return;
                
        String strXPDL = toXPDL(packageID, packageName, wfID, wfName);
        strXPDL = "SAVE NEW WORKFLOW\n" + packageID + "\n" + strXPDL;
        
        //System.err.println(strXPDL);
        designer.saveWorkflowTemplate(strXPDL, packageID, wfID);
        
        //designer.addTreeNode(packageName, wfName);
        
    }
    
    /** Updates the current workflow
     */
    public void updateWorkflow(String packageID, String packageName, String wfID, String wfName)
    {
        
        if (validateWorkFlow() == false)
            return;
        if (validateActivityIDs() == false)
            return;
        
        
        
        String strXPDL = toXPDL(packageID, packageName, wfID, wfName);
        strXPDL = "UPDATE WORKFLOW\n" + packageID + "\n" + strXPDL;
        //System.err.println(strXPDL);
        String strUpdateResult = designer.saveWorkflowTemplate(strXPDL, packageID, wfID);
        //System.err.println(strUpdateResult);
        
       
        
        if (!strUpdateResult.equals("SUCCESS"))
        {
            if (strUpdateResult.indexOf("ACTIVE_TEMPLATE") >= 0)
                JOptionPane.showMessageDialog(designer, VIOLATE_ACTIVE_WORKFLOW, "Error message", JOptionPane.ERROR_MESSAGE); 
            else if (strUpdateResult.indexOf("ACTIVE_INSTANTIATIONS") >= 0 ||
                     strUpdateResult.indexOf("PARENT_ACTIVE_INSTANTIATIONS") >= 0)
                JOptionPane.showMessageDialog(designer, VIOLATE_INSTANCE_WORKFLOW, "Error message", JOptionPane.ERROR_MESSAGE); 
        }
        //designer.addTreeNode(packageName, wfName);
    }
    
    public boolean validateTransitions(boolean displayError)
    {
        String strErrorMessage = getTransitionsError();
        boolean isValid = (strErrorMessage.equals("") ? true : false);
        
         // finally, display error if required
        if (!isValid && displayError)
           JOptionPane.showMessageDialog(null, "Unable to switch workflows as : \n" + strErrorMessage + "\n\nPlease correct this and try again.", "Workflow Error", JOptionPane.ERROR_MESSAGE);
       
        return isValid;        
    }
    
    // not having an ID for a transition when attempting to show a cached graph
    // will result in a null pointer error -- as such, we do a quick check here
    // to make sure won't be any issues
    public String getTransitionsError()
    {
        String strErrorMessage = ""; // empty string if no errors exists
        
        // condition type validations defaults
        boolean isMissingStart     = true;
        boolean isMissingEnd       = true;
        boolean isMissingLink      = false;
        boolean isMissingDetails   = false;
        boolean isInvalidStartLead = false;
        boolean isInvalidEndLead   = false;
 
        int intLinksFromStartCell = 0;
        
        Object[] cells = graph.getRoots();
        
        // check if new or there are no objects on screen
        if ( (cells.length == 0) ||
             ( (cells.length == 1) && (cells[0] instanceof StartCell) ) )
            return "";
       
        for (int j = 0; j < cells.length; j++)
        {
           if (cells[j] instanceof DefaultEdge) // found an edge
           {
                DefaultEdge edgeTemp = (DefaultEdge) cells[j];
                Hashtable edge_attrs = (Hashtable) edgeTemp.getAttributes();
                DefaultPort from_port = (DefaultPort) edgeTemp.getSource();
                DefaultPort to_port = (DefaultPort) edgeTemp.getTarget();
                DefaultGraphCell from_cell = (DefaultGraphCell) graph.getModel().getParent(from_port);
                DefaultGraphCell to_cell = (DefaultGraphCell) graph.getModel().getParent(to_port);

                // check for links first
  
                if (from_cell == null) // dead link found
                {
                   Activity tempActivity = (Activity) to_cell.getUserObject();
                   strErrorMessage += " - A path is not connected to an activity \n";
                   strErrorMessage += "      (Target is " + shortenActivityName(tempActivity.getName()) + ", Source Missing) \n";
                   isMissingLink = true;
                }

                if (to_cell == null) // dead link found
                {
                   Activity tempActivity = (Activity) from_cell.getUserObject();
                   strErrorMessage += " - A path is not connected to an activity \n";
                   strErrorMessage += "      (Source is " + shortenActivityName(tempActivity.getName()) + ", Target Missing) \n";
                   isMissingLink = true;
                }

                if (to_cell instanceof StartCell) // workflow leads back to start
                {                   
                    isInvalidStartLead = true;
                }

                if (to_cell instanceof EndCell)  // workflow correctly ends
                {
                    isMissingEnd = false;                           
                }

                if (from_cell instanceof StartCell)  // workflow correctly starts
                {
                    intLinksFromStartCell++;
                    isMissingStart = false;
                }

                if (from_cell instanceof EndCell)  // edge connected away from the stop point
                {          
                    isInvalidEndLead = true;
                }
                
                // check for transition details
                Transition currTransition = (Transition) edge_attrs.get("value");
                
                if ( currTransition == null )
                {
                    // get details between activities
                    if ( (!(to_cell instanceof EndCell)) && (!(to_cell instanceof StartCell)) && (!(from_cell instanceof StartCell)) && (!(from_cell instanceof EndCell)))
                    {
                        isMissingDetails = true;
                        Activity tempActFrom = (Activity) from_cell.getUserObject();                        
                        Activity tempActTo = (Activity) to_cell.getUserObject();
                        strErrorMessage += " - Details have not been entered into the transition between " + shortenActivityName(tempActFrom.getName()) + " and " + shortenActivityName(tempActTo.getName()) + "\n";
                    }
                    // get details between an Activity and an End Cell
                    else if ( (!(from_cell instanceof StartCell)) && (!(from_cell instanceof EndCell)) && (to_cell instanceof EndCell) )     
                    {
                        isMissingDetails = true;
                        Activity tempActFrom = (Activity) from_cell.getUserObject();
                        strErrorMessage += " - Details have not been entered into the transition between " + shortenActivityName(tempActFrom.getName()) + " and Stop point\n";
                    }
                }                    
            }
        }

        // display once off error type messages here
        if (intLinksFromStartCell > 1)
            strErrorMessage += " - Only one path is allowed to be connected to the start point \n";
        
        if (isInvalidStartLead)
            strErrorMessage += " - A path in the workflow leads back to the start point \n";
        
        if (isInvalidEndLead)
            strErrorMessage += " - A path in the workflow leads away from the stop point \n";
 
        if (isMissingStart)
            strErrorMessage += " - Workflow is not connected to a start point \n";

        if (isMissingEnd)
            strErrorMessage += " - Workflow is not connected to a stop point \n";                
                
        return strErrorMessage;
    }    
    
    
    public boolean isGraphCached(String graphIDToCheck)
    {
       modWorkFlow tempWF = null;
       for (int i = 0; i < savedGraphs.size(); i++)
       {
           tempWF = (modWorkFlow) savedGraphs.get(i);
           
           if (tempWF.getProcess().getId().equals(graphIDToCheck))
              return true;
           
       }
       return false;
        
        
    }
    
    public modWorkFlow getCachedGraph(String graphIDToGet)
    {
       modWorkFlow tempWF = null; 
       for (int i = 0; i < savedGraphs.size(); i++)
       {
           tempWF = (modWorkFlow) savedGraphs.get(i);
           
           if (tempWF.getProcess().getId().equals(graphIDToGet))
              return tempWF;
           
       }
       return null;
        
        
    }
    
    private boolean removeCachedGraph(String graphIDToRemove)
    {
        modWorkFlow tempWF = null;
    
        // check if the current graph is cached
        for (int i = 0; i < savedGraphs.size(); i++)
        {
            tempWF = (modWorkFlow) savedGraphs.get(i);
            if (tempWF.getProcess().getId().equals(getGraphId()))
            {
                // found a saved copy so remove this older copy
                savedGraphs.remove(i);
                return true;
            }
        }   
        
        return false;
    }
    
    private String getNextEndCellId()
    {
        boolean idFound; 
        int counter = 1;
                
        Object[] cells = graph.getRoots();
        
        do
        {
            idFound = true; // assume it's found  
            
            for (int i=0; i < cells.length; i++)
            {
                DefaultGraphCell current_cell = (DefaultGraphCell) cells[i];
                if (current_cell.getUserObject() instanceof Activity)
                {
                    Activity activity = (Activity) current_cell.getUserObject();
                    // check for for matching Acitivty Id
                    if (activity.getId().equals(DEFAULT_END_CELL_ID_PREFIX + counter))
                    {
                        // if matching, start again with comparisons
                        counter++;
                        idFound = false; // reset search
                        break;
                    }
                }
            }
        } while (!idFound);
        
        return new String(DEFAULT_END_CELL_ID_PREFIX + counter);
    }        
    
    private boolean validateActivityIDs()
    {
       String strErrorMessage = "The following activities have identical IDs : \n";
       boolean isValid = true;
       
       Object cells[] = graph.getRoots();
       Object cells2[] = graph.getRoots();
       
       
       Activity actCurrent, actChecking = null;
       DefaultGraphCell cellCurrent, cellChecking = null;
       
             
       for (int i = 0; i < cells.length; i++)
       {
           if (cells[i] instanceof DefaultGraphCell) 
           {
              cellCurrent = (DefaultGraphCell) cells[i];
              
              if (cellCurrent.getUserObject() instanceof Activity)
              {
                  
                  actCurrent = (Activity) cellCurrent.getUserObject();
                  
                  for (int j = i + 1; j < cells2.length; j++)
                  {
                     if (cells[j] instanceof DefaultGraphCell) 
                     {
                        cellChecking = (DefaultGraphCell) cells[j];
                        if (cellChecking.getUserObject() instanceof Activity)
                        {
                           actChecking = (Activity) cellChecking.getUserObject();
                           
                           // validat for stops
                           
                                                      
                           if (!actChecking.getId().equals("Stop"))
                           {
                           
                               if (actCurrent.getId().equals(actChecking.getId()))
                               {
                                  strErrorMessage = strErrorMessage + " - " + actCurrent.getName() + " is identical to " + actChecking.getName() + "\n";
                                  isValid = false;

                               }
                           }
                        }
                     }
                  }
              }
           }
       }
       
       if (isValid == false)
           JOptionPane.showMessageDialog(null, strErrorMessage, "Activity ID duplicate", JOptionPane.ERROR_MESSAGE);
       return isValid;
        
    }
    
    
    
    
    public void translateCachedWorkflowToGraph(neuragenix.genix.workflow.Package currentWorkflowPackage, WorkflowProcess currentWorkflowProcess, modWorkFlow tempWF)
    {       
        graph = tempWF.getGraph();
        graph.setModel(tempWF.getGraph().getModel());
        graph.setGraphName(currentWorkflowProcess.getName());
        graph.setGraphId(currentWorkflowProcess.getId());
        graph.setPackageId(currentWorkflowProcess.getId());
        graph.setPackageName(currentWorkflowProcess.getName());

        Object[] cells1 = tempWF.getRoots();

        for (int i = 0; i < cells1.length; i++)
        {
            if (!(cells1[i] instanceof DefaultEdge) && (cells1[i] instanceof DefaultGraphCell))
            {   
                Rectangle cell_bounds = (Rectangle) ((DefaultGraphCell) cells1[i]).getAttributes().get("bounds");
                Point newPoint = new Point((int) cell_bounds.getX(), (int) cell_bounds.getY());
                Dimension newSize = new Dimension((int) cell_bounds.getWidth(), (int) cell_bounds.getHeight());
                insert(newPoint, newSize, (DefaultGraphCell) cells1[i]);
            }
        }

            
            
        for (int i = 0; i < cells1.length; i++)
        {
            if (cells1[i] instanceof DefaultEdge)
            {
                Transition aTransition = (Transition) (((DefaultEdge) cells1[i]).getAttributes().get("value"));

                if (aTransition == null)
                    continue;
                
                String from = aTransition.getFrom();
                String to = aTransition.getTo();
                Port source = null;
                Port target = null;

                for (int j=0; j < cells1.length; j++)
                {
                    if (!(cells1[j] instanceof DefaultEdge) && (cells1[j] instanceof DefaultGraphCell))
                    {
                        DefaultGraphCell currentCell = (DefaultGraphCell) cells1[j];

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
        hashTriggers = currentWorkflowProcess.getTriggers();
        hashParameters = currentWorkflowProcess.getParameters();
        return;
    }
    
    // This method will determine if WF is using system gen IDs
    public boolean isGenIdAssignedToWorkflow(MyGraph myGraph)
    {
        boolean isGenIdAssigned = true; // assume OK
        
        Object [] objects = myGraph.getRoots();
        String currentId  = null;
        int    latestId   = 0; // keep a mark on the latest gen ID
        
        if ( (objects == null) || (objects.length == 0) )
            return false;

        for (int i=0; (objects != null) && i < objects.length; i++)
        {
            currentId = null; // reset
            
            if ( !(objects[i] instanceof StartCell)       &&
                 !(objects[i] instanceof EndCell)         &&
                 (objects[i] instanceof DefaultGraphCell) )
            {
                if (((DefaultGraphCell)objects[i]).getUserObject() instanceof Activity)
                {
                    Activity activity = (Activity) ((DefaultGraphCell)objects[i]).getUserObject();
                    currentId = activity.getId();
                }
                else if (((DefaultGraphCell)objects[i]).getUserObject() instanceof Transition)
                {
                    Transition transition = (Transition) ((DefaultEdge)objects[i]).getUserObject();
                    currentId = transition.getId();
                    
                    // ignore connecting start transition
                    if (currentId.equals("Start transition"))
                        continue;
                }
            }
            
            if (currentId != null)
            {
                // by removing the default prefix from the Id, this should
                // leave the ID remaining with just an int (gen ID)
                // otherwise, it's not a default sys gen id
                currentId.replaceFirst(DEFAULT_PREFIX_GEN_ID, "");

                try
                {
                    //int tempId  = Integer.parseInt(strTkn.nextToken());
                    int tempId = Integer.parseInt(currentId);
                    if (tempId > latestId)
                        latestId = tempId; // new genId assigned!
                } 
                catch (NumberFormatException nfe)
                {
                    // not a system generated ID!
                    isGenIdAssigned = false;
                    break;
                }
            }
        }
        
        // if all is OK, update the gen Id to the latest one as well
        if (isGenIdAssigned)
            genId = latestId + 1;
        
        return isGenIdAssigned;
    }
    
    // This method will override all the IDs of transitions and activities 
    // except Start and End activities and starting transition
    public void assignGenIdToWorkflow(MyGraph myGraph)
    {
        // reset the ID, since we're starting from scratch
        resetGenId();        
        
        // if the objects are already in the correct format, there's
        // no need to re-assign gen IDs as this will just increase their
        // value unnecessarily;
        if (isGenIdAssignedToWorkflow(myGraph))
            return;
        
        Object [] objects = myGraph.getRoots();
         
        // iterate activities and transitions, and change the current ID 
        // to system generated ID
        for (int i=0; (objects != null) && (i < objects.length); i++)
        {
            if ( !(objects[i] instanceof DefaultEdge)     && 
                 !(objects[i] instanceof StartCell)       &&
                 !(objects[i] instanceof EndCell)         &&
                 (objects[i] instanceof DefaultGraphCell) &&
                 (((DefaultGraphCell)objects[i]).getUserObject() instanceof Activity) )
            {
                // changing activity
                Activity activity = (Activity) ((DefaultGraphCell)objects[i]).getUserObject();
                String oldId  = activity.getId();

                // assign a new ID
                String newId = getNextGenId();
                activity.setId(newId);
 
                // iterate through the list of transitions and update the
                // activity IDs based on the associated connections
                for (int j=0; j < objects.length; j++)
                {
                    if ( (objects[j] instanceof DefaultEdge) &&
                         (((DefaultEdge)objects[j]).getUserObject() instanceof Transition) )
                    {
                        Transition transition = (Transition) ((DefaultEdge)objects[j]).getUserObject();
                        
                        if (transition.getFrom().equals(oldId))
                            transition.setFrom(newId);
                        if (transition.getTo().equals(oldId))
                            transition.setTo(newId);
                    }
                }
            }
            else if ( (objects[i] instanceof DefaultEdge) &&
                      (((DefaultEdge)objects[i]).getUserObject() instanceof Transition) )
            {
                // changing transition
                Transition transition = (Transition) ((DefaultEdge)objects[i]).getUserObject();
                
                // ignore connecting start transition
                if (transition.getId().equals("Start transition"))
                    continue;
                
                transition.setId(getNextGenId());
            }
        }
    }
    
    // if the graph requested is already cached, it will display the cached graph instead of a new one
    public void translateWorkflowProcessToGraph(neuragenix.genix.workflow.Package wfPackage, WorkflowProcess wfProcess)
    {      
        isNew = false;
        boolean isSaved = false;
     
        // remove previous version from the cache
        removeCachedGraph(getGraphId());
        
        // add this version to the cache
        if (!(currentPackage == null || currentWorkflowProcess == null))
           savedGraphs.add(new modWorkFlow(getGraph(), currentPackage, currentWorkflowProcess, graph.getRoots()));
             
        // check if newly requested graph is in the cache
        modWorkFlow cachedGraph = getCachedGraph(wfProcess.getId());
        if (cachedGraph != null)
        {
           currentPackage = cachedGraph.getPackage();
           currentWorkflowProcess = cachedGraph.getProcess();    
           isSaved = true;    
        }
        
        // clear the graph in design frame
        Object[] cells = graph.getRoots();        
        cells = graph.getDescendants(cells);
        graph.getModel().remove(cells);
        
        graph.setGraphName("");
        graph.setPackageName("");
        graph.setGraphId("");
        graph.setPackageId("");
        
        // show the cached version of the graph instead
        if(isSaved == true)
        {
            translateCachedWorkflowToGraph(wfPackage, wfProcess, cachedGraph);
            
            // assign the new system generated IDs automatically
            assignGenIdToWorkflow(cachedGraph.getGraph());
            
            return;
        }
        
        // setup appropriately
        if (isSaved == false)
        {
           currentPackage = wfPackage;
           currentWorkflowProcess = wfProcess;
        }
        
        
        
        
        // hashTriggers = wfProcess.getTriggers();
        hashTriggers = currentWorkflowProcess.getTriggers();
        
        
        //hashTriggers.put("Triggers 1", new Trigger("01", "Trigger 1", "patient_add", "PATIENT", "PATIENT_dtDOB", "Date", "", "=", "01/01/1970", "OR", ">", "01/01/2000"));
        //hashTriggers.put("Triggers 2", new Trigger("02", "Trigger 2", "patient_add", "PATIENT", "PATIENT_strFirstName", "String", "AND", "=", "Huy", "OR", "=", "Linh"));
        
        //hashParameters = wfProcess.getParameters();
        hashParameters = currentWorkflowProcess.getParameters();
        
        
        Hashtable hashActivities = currentWorkflowProcess.getActivities();
        Enumeration enumActivities = hashActivities.elements();
        
        while (enumActivities.hasMoreElements())
        {
            Activity aActivity = (Activity) enumActivities.nextElement();
            insertActivity(aActivity);
        }
        
        // display the transitions
        Hashtable hashTransitions = currentWorkflowProcess.getTransitions();
        Enumeration enumTransitions = hashTransitions.elements();
        
        Object[] activities = null;
        
        // this condition will never be true based on the condition above
        /*if (isSaved == true)
           activities = ((MyGraph)cachedGraph.getGraph()).getRoots();
        else*/
        activities = graph.getRoots();
        
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
        
        graph.setGraphName(wfProcess.getName());
        graph.setGraphId(wfProcess.getId());
        graph.setPackageId(wfPackage.getId());
        graph.setPackageName(wfPackage.getName());
        
        // assign the new system generated IDs automatically
        assignGenIdToWorkflow(graph);
    }
    
    private void insertActivity(Activity aActivity)
    {
        // getting activity attributes
        Hashtable hashExtendedAttributes = aActivity.getExtendedAttributes();
        String strActivityType = (String) hashExtendedAttributes.get("strType");
        int intPosX = Integer.parseInt((String) hashExtendedAttributes.get("intPosX"));
        int intPosY = Integer.parseInt((String) hashExtendedAttributes.get("intPosY"));
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
            GraphConstants.setBorderColor(map, DEFAULT_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_CELL_BACKGROUND_COLOR);
        }
        // System activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[1]))
        {
            vertex = new EllipseCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DEFAULT_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_CELL_BACKGROUND_COLOR);
        }
        // start activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[2]))
        {
            vertex = new StartCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DEFAULT_START_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_START_CELL_BACKGROUND_COLOR);
        }
        // stop activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[3]))
        {
            vertex = new EndCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DEFAULT_STOP_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_STOP_CELL_BACKGROUND_COLOR);
        }
        // sub-workflow activity
        else if (strActivityType.equals(DialogConstants.ACTIVITY_TYPE_OPTIONS[4]))
        {
            vertex = new RoundedRectangleCell(aActivity);
            // Add a Border Color Attribute to the Map
            GraphConstants.setBorderColor(map, DEFAULT_CELL_BORDER_COLOR);
            // Add a White Background
            GraphConstants.setBackground(map, DEFAULT_CELL_BACKGROUND_COLOR);
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
        
        //GraphConstants.setDisconnectable(map, true);
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
    
    /** Replace all special characters for XML format
     *  @param a dirty string
     *  @return a clean string
     */
    public String cleanForXSL(String strDirtyXSL)
    {
        if (strDirtyXSL == null)
            return "";

        strDirtyXSL = strDirtyXSL.replaceAll("&", "&amp;");
        strDirtyXSL = strDirtyXSL.replaceAll("<", "&lt;");
        strDirtyXSL = strDirtyXSL.replaceAll(">", "&gt;");
        strDirtyXSL = strDirtyXSL.replaceAll("'", "&apos;");
        strDirtyXSL = strDirtyXSL.replaceAll("\"", "&quot;");

        return strDirtyXSL;
    }
}
