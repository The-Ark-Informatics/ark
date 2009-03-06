/**
 * ColorSelectionDialog.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 16/02/2004
 */

package media.neuragenix.genix.workflow.dialog;

/**
 * This class creates color choosing dialog to setting background & border color.
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

// javax packages
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JColorChooser;

// jGraph
import org.jgraph.*;
import org.jgraph.graph.*;

// neuragenix packages
import media.neuragenix.genix.workflow.DesignPanel;

public class ColorSelectionDialog extends JDialog
{
    // Type of the dialog
    public static final int BACKGROUND = 0;
    public static final int BORDER = 1;
    
    private static final int DIALOG_WIDTH = 350;
    private static final int DIALOG_HEIGHT = 320;
    
    private int type;
    private DefaultGraphCell cell;
    private JGraph graph;
    
    private JButton btOK, btCancel;
    private JColorChooser colorChooser;
    
    
    /** Creates a new instance of WorkflowDetailsDialog */
    public ColorSelectionDialog(Object cell, Object graph, int type)
    {
        super((JFrame) null, "Color selection", true);
        getContentPane().setLayout(new BorderLayout());
        this.cell = (DefaultGraphCell) cell;
        this.graph = (JGraph) graph;
        this.type = type;
        
        getContentPane().add(createCentralPanel());
        
        Dimension size = colorChooser.getPreferredSize();
        setSize(size.width + 50, size.height + 110);
        //setResizable(false);
    }
    
    
    private JPanel createCentralPanel()
    {
        int row_no = 0;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        // setting color chooser object
        colorChooser = new JColorChooser();
        // setting current color
        Hashtable cell_attrs = (Hashtable) cell.getAttributes();
        if (type == BACKGROUND)
            colorChooser.setColor((Color) cell_attrs.get("backgroundColor"));
        else
            colorChooser.setColor((Color) cell_attrs.get("bordercolor"));
        
        Dimension size = colorChooser.getPreferredSize();
        colorChooser.setBounds(20, 10, size.width, size.height);
        panel.add(colorChooser);
        
        
        // add control buttons
        btOK = new JButton("OK");
        btOK.setBounds((size.width + 50 - 2*DialogConstants.BUTTON_WIDTH)/2, size.height + 20,  DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColor();
            }
        });
        panel.add(btOK);
        
        btCancel = new JButton("Cancel");
        btCancel.setBounds((size.width + 50 - 2*DialogConstants.BUTTON_WIDTH)/2 + DialogConstants.BUTTON_WIDTH, size.height + 20,  DialogConstants.BUTTON_WIDTH, DialogConstants.BUTTON_HEIGHT);
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ColorSelectionDialog.this.dispose();
            }
        });
        panel.add(btCancel);
        
        return panel;
    }
    
    private void setColor()
    {
        // getting cell attributes
        Hashtable cell_attrs = (Hashtable) cell.getAttributes();
        Enumeration enum = cell_attrs.keys();
        
        while (enum.hasMoreElements())
            System.err.println(enum.nextElement());
        
        // changing color attribute
        if (type == BACKGROUND)
            cell_attrs.put("backgroundColor", colorChooser.getColor());
        else
            cell_attrs.put("bordercolor", colorChooser.getColor());
        
        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();
        // Associate the Vertex with its Attributes
        attributes.put(cell, cell_attrs);
        // Insert the Vertex and its Attributes (can also use model)
        graph.getGraphLayoutCache().edit(attributes, null, null, null);
        
        
        // close the dialog
        ColorSelectionDialog.this.dispose();
    }
}

