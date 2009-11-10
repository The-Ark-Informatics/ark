package neuragenix.genix.admin.ListManagement;
/* 
 *  Biogenix v4.0.7 - LOV Manager
 *  Author :Daniel Murley
 *
 *  Purpose : Provides an implementation of the listmanagement interface
 *            for the modification of LOV values based on the new heirarchial
 *            LOV.  Ensure you have the new columns in LOV before attempting to use this
 *  $Id: LOVManager.java,v 1.6 2005/11/18 04:46:29 sparappat Exp $
 *
 */
import java.lang.*;

import java.util.*;

import java.io.*;

import java.sql.ResultSet;

import neuragenix.dao.*;

import neuragenix.common.*;

import org.jasig.portal.services.LogService;

import org.jasig.portal.PropertiesManager;

import neuragenix.security.*;

import javax.swing.tree.DefaultMutableTreeNode;

/**
* <p>
 * 
 * </p>
 */
public class LOVManager implements ListManager {
   
    private final String LISTNAME = "List of Values"; 
    private final String LISTREFNAME = "LOVLIST";
    
    private final int VIRTUAL_DATA = -100;
      
    protected DefaultMutableTreeNode root;
    protected Vector indexReferences;

    protected static String strReplacement = "-";
    
    private boolean blSeeHidden = false;
    static
    {
        try
        {
            strReplacement = PropertiesManager.getProperty("neuragenix.admin.LOVManager.replaceblankValues");
        }
        catch (Exception e)
        {
            System.err.println ("[LOVManager - Unable to read property : neuragenix.admin.LOVManager.replaceblankValues");
            strReplacement = "-";
        }
    }
    public LOVManager()
    {
       // Get the protein manager up and running... automatically create the list
        // InputStream file = LOVManager.class.getResourceAsStream("LOVManagerDBSchema.xml");
        // DatabaseSchema.loadDomains(file, "LOVManagerDBSchema.xml");
        if (buildTopLevelTree() == false)
            LogService.instance().log(LogService.ERROR, "[LOVManager] Constructor unable to build top level tree");
        
    }

    public LOVManager(boolean blhidden)
    {
            this.blSeeHidden = blhidden;
            if (buildTopLevelTree() == false)
            LogService.instance().log(LogService.ERROR, "[LOVManager] Constructor unable to build top level tree");
    }
    private boolean buildTopLevelTree ()
    {
        final String domain = "LOV";
        int nodeCounter = -1;
        
        
        // Set everything up
        try {
            DALQuery query = new DALQuery();
            root = new DefaultMutableTreeNode();
            ListNode lovNode = null;
            DefaultMutableTreeNode tempNode = null;
            // change this whole function to support lov levels....
            
            // take a distinct of all the categories, and get the descriptions
            Vector formfields = DatabaseSchema.getFormFields("ccoredomains_edit_listofvalues");

            query.setDomain(domain, null, null, null);
            query.setDomain("LOVDESC", "LOV_strLOVType", "LOVDESC_strLOVType", "LEFT JOIN");
            query.setField("LOV_strLOVType", null);
            query.setField("LOVDESC_strLOVDescription", null);
            // query.setCountField("LOV_strLOVType", false);
            query.setGroupBy("LOV_strLOVType");
            query.setGroupBy("LOVDESC_strLOVDescription");
            query.setWhere(null, 0, domain + "_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 1, "LOVDESC_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("OR", 0, "LOVDESC_intDeleted","=", "IS NULL", 1, DALQuery.WHERE_HAS_NULL_VALUE);
            if(!this.blSeeHidden)
            {
                query.setWhere("AND", 0, "LOV_intIsEditable", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            }
            query.setOrderBy("LOV_strLOVType", "ASC");
            query.setCaseSensitive(false);

            ResultSet rs = query.executeSelect();

            try {
            
                while (rs.next())
                {
                    
                    lovNode = new ListNode();
                    lovNode.setInternalID(++nodeCounter);
                    // lovNode.setDbKey(rs.getInt("PROTEIN_intProteinKey"));
                    lovNode.setDbKey(VIRTUAL_DATA);
                    lovNode.setDbValue(rs.getString("LOV_strLOVType"));
                    lovNode.setDbDescription(rs.getString("LOVDESC_strLOVDescription"));
                    tempNode = new DefaultMutableTreeNode(lovNode);
                    root.add (tempNode);
                }
                rs.close();
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                LogService.instance().log(LogService.ERROR, "[LOVManager] : Failed to load data into tree");
                
                return false;
            }
            
            
            
            rebuildIndex();
            return true;
        }
        catch (neuragenix.dao.exception.DAOQueryInvalidDomain e)
        {
           e.printStackTrace(System.err);
           LogService.instance().log(LogService.ERROR, "[LOVManager] DAOQueryInvalidDomain - The domain passed in is invalid");
           return false;
        }
        catch (neuragenix.dao.exception.DAOQueryInvalidField e)
        {
           e.printStackTrace(System.err);
           LogService.instance().log(LogService.ERROR, "[LOVManager] A field has been detected as being invalid");
           return false;

        }
        catch(neuragenix.dao.exception.DAOException e)
        {
           System.err.println("[LOVManager] A generic DAOException has occured.  See log for more details");
           LogService.instance().log(LogService.ERROR, "[LOVManager] A generic DAOException has occured");
           e.printStackTrace();
           return false;
            
        }
       
        
        
    }
    
    
    public void debug_displayList()
    {
        LogService.instance().log(LogService.ERROR, "[LOVManager] : Warning - debug_displayList called.");
        System.err.println("[LOVManager] : Warning - debug_displayList called.");
        
        /*
        Enumeration enum1 = root.preorderEnumeration();
        DefaultMutableTreeNode tempNode = null;
        ListNode tempListNode = null;
        Object nodeData = null;
        String del = "";
        while (enum1.hasMoreElements())
        {
            tempNode = (DefaultMutableTreeNode)enum1.nextElement();
           // for (int i = 0; i <= tempNode.getChildCount(); i++)
           // {
                nodeData = tempNode.getUserObject();
                if (nodeData != null)
                {
                   tempListNode = (ListNode) nodeData;                
                
                   if (tempListNode.getDeleted() == true)
                       del = "DELETED";
                   else
                       del = "";
                   
                   System.out.println("Node (" + tempNode.getChildCount() + ") TYPE : " + tempListNode.getNodeType() + " : " + tempListNode.getInternalID() + "Value : " + tempListNode.getDbValue() + "  \t\t" + del);
                   
                }
        
                
           // }
            
            
        }
        */
        
    }
    
    
    public boolean buildSubLevel(int levelToBuild)
    {
        // get the root node
        
       // DefaultMutableTreeNode headNode = (DefaultMutableTreeNode) root.getChildAt(levelToBuild);
        DefaultMutableTreeNode headNode = (DefaultMutableTreeNode) getTreeNodeFromIndex(levelToBuild);
        
        String strParentType = null;
        
        if (headNode == null)
        {
            return false;
        }
        
        if (headNode.getChildCount() > 0)
        {
            LogService.instance().log(LogService.ERROR, "[LOVManager] : Attempted to expand a node that already contains children");
            return false;
        }
        
        DefaultMutableTreeNode tempNode = null;
        
        
        Object nodeData = null;
        
        nodeData = headNode.getUserObject();
        // tempNode = null;
        ListNode listNode = null;
        int nodeCounter = -1;
        
        
        // TODO: More checking here
        if (nodeData != null)
        {
           listNode = (ListNode) nodeData;                
            
        }
        else 
            return false;
        
        
        
        
        
        // check if this is a category node
        
        if (listNode.getDbKey() == VIRTUAL_DATA)
        {
            if (!(listNode.getDbValue() == null || listNode.getDbValue().equals("")))
            {
        
                strParentType = listNode.getDbValue();
                // expand this node.
        
                DALQuery dalQuery = new DALQuery();
                // ListNode listNode = null;
                ResultSet rs = null;
                try 
                {
                    
                    dalQuery.setDomain("LOV", null, null, null);
                    dalQuery.setFields(DatabaseSchema.getFormFields("ccoredomains_edit_listofvalues"), null);
                    dalQuery.setWhere(null, 0, "LOV_strLOVType", "=", listNode.getDbValue(), 0, DALQuery.WHERE_HAS_VALUE);
                    dalQuery.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    if(!this.blSeeHidden)
                    {
                        dalQuery.setWhere("AND", 0, "LOV_intIsEditable", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    }
                    dalQuery.setOrderBy("LOV_strLOVParentType", "ASC",false);
                    dalQuery.setOrderBy("LOV_strLOVParentValue","ASC",false);
                    dalQuery.setOrderBy("LOV_intLOVSortOrder", "ASC",false);
                   
                    rs = dalQuery.executeSelect();
                }
                catch (Exception e)
                {
                   e.printStackTrace();    
                }
                
                    
                    
                    
                try 
                {
                    while (rs.next())
                    {

                        listNode = new ListNode();
                        listNode.setInternalID(++nodeCounter);
                        listNode.setDbKey(rs.getInt("LOV_intLOVID"));
                        listNode.setDbValue(rs.getString("LOV_strLOVValue"));
                        listNode.setDbParentCategory(strParentType);
                        listNode.setDbParentValueCategory(rs.getString("LOV_strLOVParentType"));
                        listNode.setDbParentValue(rs.getString("LOV_strLOVParentValue"));
                        listNode.setSortOrder(rs.getString("LOV_intLOVSortOrder"));
                        headNode.add(new DefaultMutableTreeNode(listNode));
                        ((ListNode)headNode.getUserObject()).setBaron(false);

                    }
                    rs.close();
                } 
                catch (Exception e)
                {
                    LogService.instance().log(LogService.ERROR, "[LOVManager] : There was an error building the subtree");
                    e.printStackTrace();
                    return false;
                }
                
            }
            else
                LogService.instance().log(LogService.ERROR, "[LOVManager] : There was an error building the subtree");
            
            
            
            
        }
        
        rebuildIndex();   
        return true;
        
    }
    
    
    // reset indexes under node
    protected boolean rebuildIndex ()
    {
        Enumeration enum1 = root.preorderEnumeration();
        ListNode tempListNode = null;
        DefaultMutableTreeNode tempTreeNode = null;
        indexReferences = new Vector(100, 100);
        int nodeCounter = -1;
        
        while (enum1.hasMoreElements())
        {
            nodeCounter++;
            tempTreeNode = (DefaultMutableTreeNode)enum1.nextElement();
            tempListNode = (ListNode) tempTreeNode.getUserObject();
            
            if (tempListNode != null)
               tempListNode.setInternalID(nodeCounter);
            indexReferences.add(nodeCounter, tempTreeNode);
            
        }
        
        return true;
        
    }
    
    
    
    private boolean buildSubLevel (ProteinListNode nodeToBuild)
    {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    
    private boolean closeSubLevel(int levelToClose)
    {
    
        // Unused in this implementation
        DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) root.getChildAt(levelToClose);
        
        if (tempNode != null)
        {
            tempNode.removeAllChildren();
            return true;
        }
        else
            return false;
        
           
        
    }
    
    public int addItem(Integer parentID, String itemValue) {
    
        return addItem(parentID, itemValue, "", "", "", "");
        
    }
    
    public int addItem(Integer parentID) 
    {
        
        ListNode newNode = new ListNode();
        
        
        // if parent ID = -1, we're adding a top level (protein)
        // if parent ID > 0 we're adding an antibody
        
        newNode.setDbValue("");
        
        newNode.edited = true;
        newNode.setEditMode(true);
        newNode.isNewNode = true;
        
        DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(newNode);
        DefaultMutableTreeNode treeStem = null;
        
        
        // treeStem = (DefaultMutableTreeNode) root.getChildAt(parentID.intValue());
        treeStem = getTreeNodeFromIndex(parentID.intValue());
        // newNode.setDbParentKey(((ListNode) treeStem.getUserObject()).getDbKey());
        newNode.setDbParentCategory(((ListNode) treeStem.getUserObject()).getDbValue());
        newNode.setDbParentValueCategory(getParentValueCategory((ListNode)treeStem.getUserObject()));
        treeStem.add (newTreeNode);
        ((ListNode) treeStem.getUserObject()).setBaron(false);
           
        rebuildIndex();
        
        return newNode.getInternalID();    
        
        
        
    }
    
    
    
    public int addItem(Integer parentID, String name, String localisation, String gene, String disease, String reportDisplay) {
        
        ListNode newNode = null;
        
        if (parentID.intValue() == -1)
        {
            ProteinListNode tempNode = new ProteinListNode();
            tempNode.setDbValue(name);
            tempNode.setLocalisationValue(localisation);
            tempNode.setGeneValue(gene);
            tempNode.setDiseaseValue(gene);
            tempNode.setReportDisplayValue(reportDisplay);
            newNode = tempNode;
            
        }
        else
        {
            AntibodyListNode tempNode = new AntibodyListNode();
            tempNode.setDbValue(name);
            newNode = tempNode;
        }
        
        newNode.edited = true;
        newNode.isNewNode = true;
        
        DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(newNode);
        DefaultMutableTreeNode treeStem = null;
        
        if (parentID.intValue() == -1)
           root.add(newTreeNode);
        else
        {
           // treeStem = (DefaultMutableTreeNode) root.getChildAt(parentID.intValue());
           treeStem = getTreeNodeFromIndex(parentID.intValue());
           newNode.setDbParentKey(((ListNode) treeStem.getUserObject()).getDbKey());
           treeStem.add (newTreeNode);
           ((ListNode) treeStem.getUserObject()).setBaron(false);
            
        }
           
        rebuildIndex();
        
        return newNode.getInternalID();
    }
    
     public int updateItem(Integer itemID, String value, String sortOrder) {
        
         ListNode updateNode = null;
        
        
        // if parent ID = -1, we're adding a top level (protein)
        // if parent ID > 0 we're adding an antibody
        
        updateNode = getNodeFromIndex(itemID.intValue());
        
        
        updateNode.setDbValue(value);
        updateNode.setSortOrder(sortOrder);
        
        updateNode.edited = true;
        updateNode.setEditMode(false);
        
        rebuildIndex();
        
        return updateNode.getInternalID();
    }
    
 //rennypv...the method is overloaded to update the parent value for the biospecimensubtype
     public int updateItem(Integer itemID, String value, String sortOrder,String strparentValue) 
     {
        
         ListNode updateNode = null;
        
        
        // if parent ID = -1, we're adding a top level (protein)
        // if parent ID > 0 we're adding an antibody
        
        updateNode = getNodeFromIndex(itemID.intValue());
        
        
        updateNode.setDbValue(value);
        updateNode.setSortOrder(sortOrder);
        updateNode.setDbParentValue(strparentValue);
        
        updateNode.edited = true;
        updateNode.setEditMode(false);
        
        rebuildIndex();
        
        return updateNode.getInternalID();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Not used in this implementation
    public boolean addSubCategory(int parentCategoryID, String childCategoryName) 
    {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    
    // Not used in this implementation
    public boolean addToTopLevel(String itemValue) {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    public boolean deleteItem(int itemID) {
    
        ListNode tempNode = getNodeFromIndex(itemID);
        
        tempNode.setDeleted(true);
    
        // System.out.println ("Marked DELETE : " + itemID);
        
        return true;
    }
    //rennypv..this method is used to get the ParentType of the nodes
    public String getParentValueCategory(ListNode lstnode)
    {
        String strparentType = null;
        try
        {
            DALQuery dal_parent = new DALQuery();
            dal_parent.setDomain("LOV", null, null, null);
            dal_parent.setField("LOV_strLOVParentType", null);
            dal_parent.setWhere(null, 0, "LOV_strLOVType", "=", lstnode.getDbValue(), 0, DALQuery.WHERE_HAS_VALUE);
            dal_parent.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            dal_parent.setWhere("AND", 0, "LOV_intIsEditable", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs_parent = dal_parent.executeSelect();
            if(rs_parent.next())
            {
                strparentType = rs_parent.getString("LOV_strLOVParentType"); 
            }
            rs_parent.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return strparentType;
    }
    // Not currently used, but should be completed in a later version
    public boolean destroy() {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    
    // Not used in this implementation
    public int getChildCount(int itemID) {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return 0;
    }
    
    
    // not used in this implementation
    public String getItem(int itemID) 
    {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return null;
    }
    
    
    
    private DefaultMutableTreeNode getTreeNodeFromIndex (int index)
    {
        DefaultMutableTreeNode treeNode = null;
        
        
        // DM Check this
        if (indexReferences == null)
           rebuildIndex();
        
        
        if (indexReferences.size() < index)
            return null;
        else
        {
            treeNode = (DefaultMutableTreeNode) indexReferences.elementAt(index);
            return treeNode;
        }
    }
    
    
    private ListNode getNodeFromIndex(int index)
    {
        int counter = 0;
        ListNode tempNode = null;
        DefaultMutableTreeNode treeNode = null;
        
        if (indexReferences.size() < index)
            return null;
        else
        {
            treeNode = (DefaultMutableTreeNode) indexReferences.elementAt(index);
            return (ListNode) treeNode.getUserObject();
        }
    }
    
    
    // Not used in this implementation
    public String getListName() {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return null;
    }
    
  //  public Hashtable getSubCategory(Tree.DefaultMutableTreeNode parentNode) {
  //  }
    
    public Hashtable getSubCategory(String category) 
    {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return null;
    }
    //rennypv....this returns the list of the subcategories as a vector passing in the type
    public Vector getSubCategoryList(String value)
    {
        DALQuery dalQuery = new DALQuery();
                // ListNode listNode = null;
        Vector vtValues = new Vector();
                ResultSet rs = null;
                try 
                {
                    dalQuery.setDomain("LOV", null, null, null);
                    dalQuery.setField("LOV_strLOVValue",null);
                    dalQuery.setWhere(null, 0, "LOV_strLOVType", "=", value, 0, DALQuery.WHERE_HAS_VALUE);
                    dalQuery.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    dalQuery.setWhere("AND", 0, "LOV_intIsEditable", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    rs = dalQuery.executeSelect();
                    while(rs.next())
                    {
                        vtValues.add(rs.getString("LOV_strLOVValue"));
                    }
                    rs.close();
                }
                catch (Exception e)
                {
                   e.printStackTrace();    
                }
            return vtValues;
    }
    public Hashtable getTopLevel() {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return null;
    }
    
    public void hasSubCategory(int categoryID) {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return;
    }
    
    public boolean isCategoryEditable(int categoryID) {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    public boolean isTopLevelEditable() {
        System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    public String translateTreeToXML() {
        StringBuffer toXML = new StringBuffer();
        toXML.append("<Categories>");
        toXML.append(translateTreeToXML(root));
        toXML.append("</Categories>");
        return toXML.toString();
        
        
    }
    
     public String translateTreeToXML(DefaultMutableTreeNode node) {
        StringBuffer toXML = new StringBuffer();
        Enumeration treePreOrder = node.preorderEnumeration();
        DefaultMutableTreeNode tempPosition = null;
        ListNode tempListNode = null;
        
        boolean topLevel = false;
        
        if (node != root)
            treePreOrder.nextElement();
        
        
        
        while (treePreOrder.hasMoreElements())
        {
            tempPosition = (DefaultMutableTreeNode)treePreOrder.nextElement();
            tempListNode = (ListNode)tempPosition.getUserObject();
       
            if ((tempListNode != null) && (tempListNode.getDeleted() == false))
            {    
                
                if (tempListNode.getDbKey() == VIRTUAL_DATA)
                {
                    topLevel = true;
                    
                    toXML.append("<Category>\n");
                    toXML.append("\t<InternalID>\n" + tempListNode.getInternalID() + "</InternalID>");
                    toXML.append("\t<Description>\n" + tempListNode.getDbDescription() + "</Description>");
                    toXML.append("\t<Value>\n" + Utilities.cleanForXSL(tempListNode.getDbValue()) + "</Value>");
                    toXML.append("\t<EditMode>" + tempListNode.getEditMode() + "</EditMode>");
                    if (tempListNode.isBaron() == false)
                    {
                        toXML.append("<ListValues>");
                        toXML.append(translateTreeToXML((DefaultMutableTreeNode) tempPosition));
                        toXML.append("</ListValues>");
                        
                    }
                    toXML.append("</Category>\n");

                }
                else if ((tempListNode.getDbKey() != VIRTUAL_DATA) && (topLevel == false))
                {
                        
                        toXML.append("\t<Values>\n");
                        toXML.append("\t<InternalID>\n" + tempListNode.getInternalID() + "</InternalID>");
                        toXML.append("\t<Value>\n" + Utilities.cleanForXSL(tempListNode.getDbValue()) + "</Value>");
                        toXML.append("\t<SortOrder>\n" + Utilities.cleanForXSL(tempListNode.getSortOrder()) + "</SortOrder>");
                        if(tempListNode.getDbParentValueCategory() != null && tempListNode.getDbParentCategory().length() > 0)
                        {
                            if(tempListNode.getDbParentValue() != null && tempListNode.getDbParentValue().length() > 0)
                            {
                                toXML.append("\t<ParentCategory>\n" + Utilities.cleanForXSL(tempListNode.getDbParentValueCategory()) + "</ParentCategory>");
                                toXML.append("\t<ParentValue>\n" + Utilities.cleanForXSL(tempListNode.getDbParentValue()) + "</ParentValue>");
                            }
                            if(tempListNode.getEditMode())
                            {
                                toXML.append("\t<ParentdropdownValues>\n");
//                                ListNode lstNode = getItem(tempListNode.getDbParentValueCategory());
                                Vector vtvalues = this.getSubCategoryList(tempListNode.getDbParentValueCategory());
                                for(Enumeration evalues=vtvalues.elements();evalues.hasMoreElements();)
                                {
                                    String strval = (String)evalues.nextElement();
                                    toXML.append("\t<Value ");
                                    if(strval.equals(tempListNode.getDbParentValue()))
                                    {
                                        toXML.append("selected=\'1\'");
                                    }
                                    toXML.append(">\n");
                                    toXML.append(strval);
                                    toXML.append("</Value>");
                                }
                                toXML.append("</ParentdropdownValues>");
                            }
                        }
                        toXML.append("\t<EditMode>" + tempListNode.getEditMode() + "</EditMode>");
                        toXML.append("\t</Values>\n");
        
                }
            }
        }
        
        
        
        // System.out.println(toXML.toString());
        return toXML.toString();
    }
    
    public ListNode getItem(String strDbValue)
    {
        ListNode lstNode = null;
        for(Enumeration echildren=this.root.children();echildren.hasMoreElements();)
        {
            DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode)echildren.nextElement();
            lstNode = (ListNode)tempNode.getUserObject();
            if(lstNode.getDbValue() != null)
            {
                if(lstNode.getDbValue().equals(strDbValue))
                {
                    return lstNode;
                }
            }
        }
        return lstNode;
    }
     public String processRequest(String request, Hashtable parameters) 
     {
         System.err.println ("[LOVManager] : A function has been called that is not part of this implementation");
         return null;
     }     
    
     
     public void setEditFlag(int itemID)
     {
         ListNode tempNode = getNodeFromIndex(itemID);
         
         if (tempNode != null)
             tempNode.setEditMode(true);
         else
             LogService.instance().log(LogService.ERROR, "[LOVManager] : Unable to set edit flag for ID " + itemID);
         
     }
     
     
     
     public boolean writeOutCache() 
     {
         // write out all the items in the tree that are marked as edited.
         
        Enumeration enum1 = root.preorderEnumeration();
        ListNode tempListNode = null;
        DefaultMutableTreeNode tempTreeNode = null;
        int nodeCounter = -1;
        
        while (enum1.hasMoreElements())
        {
            
            tempListNode = (ListNode) ((DefaultMutableTreeNode)enum1.nextElement()).getUserObject();
            if ((tempListNode != null))
            {
                //LogService.instance().log(LogService.ERROR, "[LOVManager::writeOutCache] TempListNode contains: " + tempListNode.toString());

               if (tempListNode.isNewNode == true)
               {
                   insertNodeToDatabase(tempListNode, false);
               }
               else if ((tempListNode.edited == true) || (tempListNode.getDeleted() == true))
               {
                   insertNodeToDatabase(tempListNode, true);
               }
            }
            else
            {
                //LogService.instance().log(LogService.ERROR, "[LOVManager::writeOutCache] TempListNode is null - nothing to add ");
            }
        }
        
        root = null; // destroy the old 
        if (buildTopLevelTree() == false)
            LogService.instance().log(LogService.ERROR, "[LOVManager] : There was an error rebuilding tree");
        
        
        return true;
         
         
         
         
         
     }     
    
     
     
     // Warning : Uses updates to DALQuery for returning correct primary key
     //           NEW DALQUERY OBJECT MUST BE USED OR THIS FUNCTION WILL FAIL.
     //             ==> getInsertedRecordKey()
     
     public boolean insertNodeToDatabase(ListNode node, boolean update)
     {
        
         String domain = "LOV";
         Hashtable valuesToAdd = new Hashtable();
         boolean queryResult = false;
         
         
         try {
            DALQuery query = new DALQuery();
          
            DefaultMutableTreeNode tempNode = null;
            Vector formfields = null;
            formfields = DatabaseSchema.getFormFields("ccoredomains_update_listofvalues");
     
           
           
            valuesToAdd.put(domain + "_strLOVType", node.getDbParentCategory());
            if (node.getDbValue() == null || node.getDbValue().trim().equals(""))
            {
                node.setDbValue(this.strReplacement);
            }
            valuesToAdd.put(domain + "_strLOVValue", node.getDbValue());
            
            if (node.getSortOrder() == null) node.setSortOrder(" ");
            valuesToAdd.put(domain + "_intLOVSortOrder", node.getSortOrder());
            
            if(node.getDbParentValue() == null)
            {
                node.setDbParentValue(" ");
            }
            valuesToAdd.put(domain + "_strLOVParentValue", node.getDbParentValue());
            
            if(node.getDbParentValueCategory() == null)
            {
                node.setDbParentValueCategory(" ");
            }
            valuesToAdd.put(domain + "_strLOVParentType", node.getDbParentValueCategory());
            if (node.getDeleted() == true)
                valuesToAdd.put (domain + "_intDeleted", "-1");
            else
                valuesToAdd.put (domain + "_intDeleted", "0");
            
//            if (node.getEditMode() == true)
                valuesToAdd.put(domain + "_intIsEditable",  "0");
//            else
//                valuesToAdd.put(domain + "_intIsEditable",  "-1");
            
            query.setDomain(domain, null, null, null);

            query.setFields( formfields, valuesToAdd );
                        
            if (update == true)
            {
                
                //String keyName = String.valueOf(domain.charAt(0));
                //keyName = keyName + domain.substring(1).toLowerCase();
                
                String keyName = domain;
                
                
                // valuesToAdd.put ((domain + "_int" + keyName + "ID"), String.valueOf(node.getDbKey()));
                // valuesToAdd.put(domain + "_int" + keyName + "Key", node.getDbValue());
                query.setWhere(null, 0, domain + "_int" + keyName + "ID", "=", String.valueOf(node.getDbKey()), 0, DALQuery.WHERE_HAS_VALUE);

LogService.instance().log(LogService.ERROR, "[LOVManager::insertNodeToDb] UPDATE:" + query.convertSelectQueryToString());
                
                queryResult = query.executeUpdate();
                if (queryResult == true)
                {
                    node.edited = false;
                    node.isNewNode = false;
                }
                return queryResult;
            }
            
LogService.instance().log(LogService.ERROR, "[LOVManager::insertNodeToDb] INSERT: " + query.convertSelectQueryToString());
            queryResult = query.executeInsert();
            if (queryResult == true)
            {
                node.edited = false;
                node.deleted = false;
                node.isNewNode = false;
                node.dbKey = query.getInsertedRecordKey();
            }
         }
         catch (Exception e)
         {
             LogService.instance().log(LogService.ERROR, "[LOVManager] : There was an error while writing out nodes");
             e.printStackTrace();
         }

         return false;
     }
     
     // Not used in this implementation
     public void finalise() {
     }     
    
     public boolean editItem(Integer itemId, Hashtable parameters) 
     {
         ListNode tempNode = getNodeFromIndex(itemId.intValue());
         
         if (tempNode == null)
             return false;
         
         tempNode.edited = true;
         if (tempNode.getNodeType().equals("PROTEIN"))
         {
             ProteinListNode proteinNode = (ProteinListNode) tempNode;
             
             if (parameters.get("PROTEIN_strName") != null)
                 proteinNode.setDbValue((String) parameters.get("PROTEIN_strName"));
             
             if (parameters.get("PROTEIN_strGene") != null)
                 proteinNode.setGeneValue((String) parameters.get("PROTEIN_strGene"));
             
             if (parameters.get("PROTEIN_strLocalisation") != null)
                 proteinNode.setLocalisationValue((String) parameters.get("PROTEIN_strLocalisation"));
             
             if (parameters.get("PROTEIN_strDisease") != null)
                 proteinNode.setDiseaseValue((String) parameters.get("PROTEIN_strDisease")); 
             
             if (parameters.get("PROTEIN_strDisplayName") != null)
                 proteinNode.setReportDisplayValue((String) parameters.get("PROTEIN_strDisplayName"));
             
             return true;
             
         }
         else if (tempNode.getNodeType().equals("ANTIBODY"))
         {
             if (parameters.get("ANTIBODY_strName") != null)
                 tempNode.setDbValue((String) parameters.get("ANTIBODY_strName"));
             return true;
             
         }
         else
             return false;
             
     }     
    
 } // end ProteinManager




