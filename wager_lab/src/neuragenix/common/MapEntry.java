/*
 * MapEntry.java
 *
 * Created on 15 September 2004, 14:33
 */

package neuragenix.common;


import java.util.*;
/**
 *
 * @author  renny
 */
public class MapEntry implements Map.Entry
{
    Object key;
    Object value;
    /** Creates a new instance of MapEntry */
    public MapEntry()
    {
        
    }
    public MapEntry(Object key,Object value)
    {
        this.key = key;
        this.value = value;
    }
    public Object getKey()
    {
        return key;
    }
    public Object getValue()
    {
        return value;
    }
    public Object setValue(Object value)
    {
        Object old_value = value;
        this.value = value;
        return old_value;
    }
    
}
