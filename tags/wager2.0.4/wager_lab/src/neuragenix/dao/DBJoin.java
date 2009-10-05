/**
 * DBJoin.java
 * Copyright © 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 14/10/2003
 */

package neuragenix.dao;

/**
 * Class to specify join conditions between domains
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

public class DBJoin {
    
    /** Type of the join (eg INNER JOIN, LEFT JOIN ...)
     */
    private String type;
    
    /** First field of the join
     */
    private String firstField;
    
    /** Second field of the join
     */
    private String secondField;
    
    
    /** Creates a new instance of DBJoin 
     */
    public DBJoin(String strType, String strFirstField, String strSecondField)
    {
        type = strType;
        firstField = strFirstField;
        secondField = strSecondField;
    }   
    
    /** Return join type
     */
    public String getType()
    {
        return type;
    }
    
    /** Return first field
     */
    public String getFirstField()
    {
        return firstField;
    }
    
    /** Return second field
     */
    public String getSecondField()
    {
        return secondField;
    }
}
