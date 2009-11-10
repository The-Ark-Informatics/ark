/**
 * Parameter.java
 * Copyright © 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 20/02/2004
 */

package media.neuragenix.genix.workflow.model;

/**
 * Class to model workflow parameter objects
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

public class Parameter 
{
    /** Parameter name
     */
    private String name;
    
    /** Parameter type
     */
    private String type;
    
    /** Parameter description
     */
    private String description;
    
    
    
    /** Default constructor 
     */
    public Parameter() 
    {
        this(null, null, null);
    }
    
    /** Constructor
     */
    public Parameter(String strName, String strType, String strDesc)
    {
        name = strName;
        type = strType;
        description = strDesc;
    }
    
    /** Get the parameter name
     */
    public String getName()
    {
        return name;
    }
    
    /** Get the parameter type
     */
    public String getType()
    {
        return type;
    }
        
    /** Get the parameter description
     */
    public String getDescription()
    {
        return description;
    }
    
    /** Set the parameter name
     */
    public void setName(String strName)
    {
        name = strName;
    }
    
    /** Set the parameter type
     */
    public void setType(String strType)
    {
        type = strType;
    }
    
    /** Set the parameter description
     */
    public void setDescription(String strDesc)
    {
        description = strDesc;
    }
    
    /** Convert parameter object to string
     */
    public String toString()
    {
        return getName();
    }
}
