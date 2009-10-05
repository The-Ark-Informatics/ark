/**
 * MDXQuery.java
 * Copyright © 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 18/11/2003
 */

package neuragenix.dao;

/**
 * Class to model a MDX query
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.Vector;
import java.util.Hashtable;

public class MDXQuery {
    public static final int DIMENSION = 0;
    public static final int MEASURE = 1;
    
    private String strCube;
    private Vector vtColumn = new Vector(3, 2);
    private Vector vtRow = new Vector(3, 2);
    private Hashtable hashMeasures = new Hashtable(5);
    
    /** Creates a new instance of MDXQuery 
     */
    public MDXQuery()
    {
    }
    
    /** Set Cube for a MDX query 
     */
    public void setCube(String strACube)
    {
        strCube = strACube;
    }
    
    public void setColumnElement(String strAElement, int intElementType)
    {
        vtColumn.add(strAElement);
        
        if (intElementType == MEASURE)
            hashMeasures.put(strAElement, "");
    }
    
    public void setRowElement(String strAElement, int intElementType)
    {
        vtRow.add(strAElement);
        
        if (intElementType == MEASURE)
            hashMeasures.put(strAElement, "");
    }
    
    public String convertToString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT {");
        
        // adds column dimensions
        for (int i=0; i < vtColumn.size(); i++)
        {
            sb.append("[");
            
            String strElement = (String) vtColumn.get(i);
            if (hashMeasures.containsKey(strElement))
                sb.append("Measure].[");
                
            sb.append(strElement);
            sb.append("]");
            
            if (i != vtColumn.size() - 1)
                sb.append(", ");
        }
        
        if (vtRow.size() > 1)
            sb.append("} ON COLUMNS, {(");
        else
            sb.append("} ON COLUMNS, {");
        
        // adds row dimensions
        for (int i=0; i < vtRow.size(); i++)
        {
            sb.append("[");
            
            String strElement = (String) vtRow.get(i);
            if (hashMeasures.containsKey(strElement))
                sb.append("Measures].[");
                
            sb.append(strElement);
            sb.append("]");
            
            if (i != vtRow.size() - 1)
                sb.append(", ");
        }
        
        if (vtRow.size() > 1)
            sb.append(")} ON ROWS FROM ");
        else
            sb.append("} ON ROWS FROM ");
        
        // add cube
        sb.append(strCube);
        
        return sb.toString();
    }
}
