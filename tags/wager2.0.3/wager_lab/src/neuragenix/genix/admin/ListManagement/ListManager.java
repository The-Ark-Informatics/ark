package neuragenix.genix.admin.ListManagement;

/*
 *  ListManager
 *  Interface for use directly or through a module manager for managing lists
 *
 *  Author : Daniel Murley
 *  $ID: $
 *
 */
import java.lang.*;
import java.util.*;

public interface ListManager {

    
    
    public String processRequest(String request, Hashtable parameters);
    
    
    public boolean writeOutCache();
    
    public void finalise();
    
    public void setEditFlag(int itemID);
    
    
    
    
    
    
/**
 * <p>Returns the top level of categories in a multi-heirarchy list 
 *  Needs to be returned as a hashtable so keys will be known in the list</p>
 * 
 * 
 * 
 * @return 
 */
    public Hashtable getTopLevel();
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param category 
 */
    public Hashtable getSubCategory(String category);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @param categoryID 
 */
    public void hasSubCategory(int categoryID);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param itemValue 
 */
    public boolean addToTopLevel(String itemValue);
    
    
    
    public boolean editItem (Integer itemId, Hashtable parameters);
    
    
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param parentCategoryID 
 * @param childCategoryName 
 */
    public boolean addSubCategory(int parentCategoryID, String childCategoryName);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 */
    public boolean isTopLevelEditable();
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param categoryID 
 */
    public boolean isCategoryEditable(int categoryID);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param categoryID 
 * @param itemValue 
 */
    public int addItem(Integer categoryID, String itemValue);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @param return 
 * @param itemID 
 */
   // public  editItem(int return, int itemID);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param itemID 
 */
    public boolean deleteItem(int itemID);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param itemID 
 */
    public String getItem(int itemID);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 */
    public boolean destroy();
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 */
    public String translateTreeToXML();
/**
 * <p>
 * Represents ...
 * </p>
 * 
 * 
 * @return 
 */
    public String getListName();
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @param parentNode 
 * @return 
 */
  //  public Hashtable getSubCategory(Tree.DefaultMutableTreeNode parentNode);
/**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return 
 * @param itemID 
 */
    public int getChildCount(int itemID);

} // end ListManager






