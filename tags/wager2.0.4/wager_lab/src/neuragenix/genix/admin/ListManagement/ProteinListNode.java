package neuragenix.genix.admin.ListManagement;
/*
 *  ProteinListNode
 *   extention of Listnode for use with CHW-Neurogenetics
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
public class ProteinListNode extends ListNode {

  public final String NODETYPE = "PROTEIN";

/**
 * <p>
 * Key of this node in the database
 * </p>
 */
    private int dbKey = -1; 
    private int internalID = -1;
/**
 * <p>
 * parent key of this node (if applicable)
 * </p>
 */
    private int dbParentKey = -1; 

/**
 * <p>
 * value of this node
 * </p>
 */
    private String dbValue = null; 
    private String geneValue = null;
    private String localisationValue = null;
    private String diseaseValue = null;
    private String reportDisplayValue = null;
/**
 * <p>
 * is this node Editable
 * </p>
 */
    private boolean editable = false; 

/**
 * <p>
 * detected children of this node.
 * </p>
 */
    private boolean baron = true; 



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
    
/**
 * <p>
 * Represents ...
 * </p>
 * 
 * 
 * @return 
 */
    public int getDbParentKey() {        
        // your code here
        return dbParentKey;
    } // end getDbParentKey        

    
    public void setDbParentKey(int dbParentKey) {        
        
        this.dbParentKey = dbParentKey;
    } 
    
    public int getInternalID() {        
        // your code here
        return internalID;
    } // end getDbParentKey        

    
    public void setInternalID(int internalID) {        
        
        this.internalID = internalID;
    } 
    
    
    
    
    
    public String getDbValue() {        
        // your code here
        return dbValue;
    } // end getDbValue        

    public void setDbValue(String dbValue) {        
        // your code here
        this.dbValue = dbValue;
    } 
    
    
    public String getLocalisationValue() {        
        // your code here
        if (localisationValue == null)
            return "";
        else
            return localisationValue;
    } // end getDbValue        

    public void setLocalisationValue(String localisationValue) {        
        // your code here
        this.localisationValue = localisationValue;
    } 
    
    public String getGeneValue ()
    {
        if (geneValue == null)
            return "";
        else
            return geneValue;
    }
    
    
    
    public void setGeneValue (String geneValue)
    {
        this.geneValue = geneValue;
        
        
    }
    
    public String getDiseaseValue ()
    {
        if (diseaseValue == null)
            return "";
        else
            return diseaseValue;
    }
    
    
    
    public void setDiseaseValue (String diseaseValue)
    {
        this.diseaseValue = diseaseValue;
        
        
    }    
    
    
    public String getReportDisplayValue ()
    {
        if (reportDisplayValue == null)
            return "";
        else
            return reportDisplayValue;
    }
    
    
    
    public void setReportDisplayValue (String reportDisplayValue)
    {
        this.reportDisplayValue = reportDisplayValue;
        
        
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
 } // end ListNode




