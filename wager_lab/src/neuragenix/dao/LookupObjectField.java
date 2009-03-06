/**
 * LookupObjectField.java
 * Copyright © 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 01/04/2004
 */

package neuragenix.dao;

/**
 * Class to model a system lookup field
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

public class LookupObjectField 
{
    
    /** Field's name
     */
    private String strName;
    
    /** Field's display name
     */
    private String strDisplayName;
    
    /** Indicate if the field can appear in WHERE condition
     */
    private boolean blInWhere;
    
    /** Indicate if the field can appear in the field list
     */
    private boolean blInFieldList;
    
    
    /** Creates a new instance of DBField 
     */
    public LookupObjectField() {
        this(null, null, false, false);
    }
    
    public LookupObjectField(String strAName,  String strADisplayName, boolean blAInWhere, boolean blAInFieldList)
    {
        strName = strAName;
        strDisplayName = strADisplayName;
        blInWhere = blAInWhere;
        blInFieldList = blAInFieldList;
    }
    
    public String getName() {
        return strName;
    }
    
    public String getDisplayName() {
        return strDisplayName;
    }
    
    public boolean isInWhere() {
        return blInWhere;
    }
    
    public boolean isInFieldList() {
        return blInFieldList;
    }
    
    public void setName(String strAName) {
        strName = strAName;
    }
    
    public void setDisplayName(String strADisplayName) {
        strDisplayName = strADisplayName;
    }
    
    public void setInWhere(boolean blAInWhere) {
        blInWhere = blAInWhere;
    }
    
    public void setInFieldList(boolean blAInFieldList) {
        blInFieldList = blAInFieldList;
    }
    
}
