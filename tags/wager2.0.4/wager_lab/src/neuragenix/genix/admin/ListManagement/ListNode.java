package neuragenix.genix.admin.ListManagement;
/*
 *  ListNode
 *   Generic node for use with ListManager
 *
 *  Author : Daniel Murley
 *  $ID: $
 *
 */
import java.lang.*;
import java.util.*;

/**
 * <p>
 * 
 * </p>
 */
public class ListNode {

    
    public final String NODETYPE = "DEFAULT";
  ///////////////////////////////////////
  // attributes

    private int internalID = -1;
/**
 * <p>
 * Key of this node in the database
 * </p>
 */
    protected int dbKey = -1; 

/**
 * <p>
 * parent key of this node (if applicable)
 * </p>
 */
    protected int dbParentKey = -1; 

    protected String dbParentCategory = null;
    
    protected String dbParentValueCategory = null;
    
    protected String dbParentValue = null;
/**
 * <p>
 * value of this node
 * </p>
 */
    protected String dbValue = null; 
    
    protected String dbDescription = null;

    protected String sortOrder = null;
/**
 * <p>
 * is this node Editable
 * </p>
 */
    protected boolean editable = false;
    protected boolean inEditMode = false;
    protected boolean edited = false;
    protected boolean isNewNode = false;
    protected boolean deleted = false;
    
/**
 * <p>
 * detected children of this node.
 * </p>
 */
    protected boolean baron; 



  ///////////////////////////////////////
  // operations


/**
 * <p>
 * Represents ...
 * </p>
 * 
 * 
 * @return 
 */
    public int getDbKey() {        
        // your code here
        return dbKey;
    } // end getDbKey        

    
    public void setDbKey(int dbKey) {        
        this.dbKey = dbKey;
                
    } // end getDbKey 
    
    
    public String getNodeType()
    {
        return NODETYPE;
    }
    
     public int getInternalID() {        
        // your code here
        return internalID;
    } // end getDbParentKey        

    
    public void setInternalID(int internalID) {        
        
        this.internalID = internalID;
    } 
    

    public String getDbParentCategory()
    {
        return dbParentCategory;
        
    }
    
    
    public void setDbParentCategory(String strParentCategory)
    {
        this.dbParentCategory = strParentCategory;
        
    }
    
    public String getDbParentValue()
    {
        return this.dbParentValue;
        
    }
    
    
    public void setDbParentValue(String strParentValue)
    {
        this.dbParentValue = strParentValue;
        
    }
    
    public String getDbParentValueCategory()
    {
        return this.dbParentValueCategory;
    }
    
    
    public void setDbParentValueCategory(String strParentValueCategory)
    {
        this.dbParentValueCategory = strParentValueCategory;
        
    }
    
    public int getDbParentKey() {        
        // your code here
        return dbParentKey;
    } // end getDbParentKey        

    
    public void setDbParentKey(int dbParentKey) {        
        
        this.dbParentKey = dbParentKey;
    } 
    
    public String getDbDescription()
    {
        return dbDescription;
        
    }
    
    
    public void setDbDescription(String dbDescription)
    {
        this.dbDescription = dbDescription;
        
    }
    
    
    
    public String getDbValue() {        
        // your code here
        return dbValue;
    } // end getDbValue        

    public void setDbValue(String dbValue) {        
        // your code here
        this.dbValue = dbValue;
    } 
    
    
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 */
    public boolean isEditable() {        
        // your code here
        return editable;
    } // end isEditable        

    protected void setEditable(boolean editable) 
    {
        this.editable = editable;
    }
    
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 */
    public boolean isBaron() {        
        // your code here
        return baron;
    } // end isBaron        

    public void setBaron(boolean baron)
    {
        this.baron = baron;
        
    }
    
    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }
    
    public boolean getDeleted ()
    {
        return deleted;
        
    }
    
    public void setSortOrder (String sortOrder)
    {
        this.sortOrder = sortOrder;
        
    }

    public String getSortOrder ()
    {
        return sortOrder;
    }
    
    
    public void setEditMode (boolean editMode)
    {
        inEditMode = editMode;
    }

    public boolean getEditMode ()
    {
        return inEditMode;
    }
    
    
 } // end ListNode




