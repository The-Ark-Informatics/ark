/**
 * TriggerField.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 19/04/2004
 */

package neuragenix.genix.workflow;

/**
 * Class to model a trigger field object
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

import java.io.*;

public class TriggerField implements Serializable {
    
    // Field's name 
    private String strName;
    
    // Display name
    private String strDisplayName;
    
    // Data type
    private String strType;
    
    /** Creates a new instance of TriggerField 
     * @param name
     * @param display name
     */
    public TriggerField(String name, String displayname, String type) 
    {
        strName = name;
        strDisplayName = displayname;
        strType = type;
    }
    
    /** Get field's name
     * @return name
     */
    public String getName()
    {
        return strName;
    }
    
    /** Get field's display name
     * @return display name
     */
    public String getDisplayName()
    {
        return strDisplayName;
    }
    
    /** Get field's type
     * @return type
     */
    public String getType()
    {
        return strType;
    }
    
    /** Set field's name
     * @param name
     */
    public void setName(String name)
    {
        strName = name;
    }
    
    /** Set field's display name
     * @param display name
     */
    public void setDisplayName(String displayname)
    {
        strDisplayName = displayname;
    }
    
    /** Set field's type
     * @param type
     */
    public void setType(String type)
    {
        strType = type;
    }
    
    public String toString()
    {
        return strDisplayName;
    }
    
    /*
    // Serialization support.  
    public void writeObject(ObjectOutputStream s) throws IOException 
    {
        Object[] values = new Object[3];

	s.defaultWriteObject();
	// Save the userObject, if its Serializable.
        values[0] = (strName        == null ? "" : strName);
        values[1] = (strDisplayName == null ? "" : strDisplayName);
        values[2] = (strType        == null ? "" : strType);
        
	s.writeObject(values);
    }
    
    public void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException 
    {
        Object[] values = new Object[3];

	s.defaultReadObject();

	values = (String[])s.readObject();
        
        strName        = (String) (values[0] == null ? "" : values[0]);
        strDisplayName = (String) (values[1] == null ? "" : values[1]);
        strType        = (String) (values[2] == null ? "" : values[2]);
    }    */
}