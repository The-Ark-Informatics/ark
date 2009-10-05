package neuragenix.genix.admin.ListManagement;
/* 
 *  CHW-Neurogenetics Customisations
 *  Author :Daniel Murley
 *
 *  Purpose : Provides an implementation of the listmanagement interface
 *            to support proteins and genes.
 *
 *  $ID: $
 *
 */
import java.lang.*;

import java.util.*;

import java.io.*;

import java.sql.ResultSet;

import neuragenix.dao.*;

import neuragenix.common.*;

import org.jasig.portal.services.LogService;

import neuragenix.security.*;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * <p>
 * 
 * </p>
 */
public class ProteinManager implements ListManager {
   
    private final String LISTNAME = "Proteins and Antibodies"; 
    private final String LISTREFNAME = "PROTEINLIST";
    
      
    protected DefaultMutableTreeNode root;
    protected Vector indexReferences;

    
    
    /*
    ProteinManager()
    {
        System.out.println ("ProteinManager constructor fired");
        // Default constructor with no love
        InputStream file = ProteinManager.class.getResourceAsStream("ProteinManagerDBSchema.xml");
        DatabaseSchema.loadDomains(file, "ProteinManagerDBSchema.xml");
    }
    */
    public ProteinManager()
    {
        // Get the protein manager up and running... automatically create the list
        InputStream file = ProteinManager.class.getResourceAsStream("ProteinManagerDBSchema.xml");
        DatabaseSchema.loadDomains(file, "ProteinManagerDBSchema.xml");
        if (buildTopLevelTree() == false)
            LogService.instance().log(LogService.ERROR, "[ProteinManager] Constructor unable to build top level tree");
        
    }

    private boolean buildTopLevelTree ()
    {
        final String domain = "PROTEIN";
        int nodeCounter = -1;
        
        
        // Set everything up
        try {
            DALQuery query = new DALQuery();
            root = new DefaultMutableTreeNode();
            ProteinListNode proteinNode = null;
            DefaultMutableTreeNode tempNode = null;
            Vector formfields = DatabaseSchema.getFormFields("view_protein");

            query.setDomain(domain, null, null, null);
            query.setFields( formfields, null );
            query.setWhere(null, 0, "PROTEIN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setCaseSensitive(false);
            query.setOrderBy("PROTEIN_strName", "ASC");
            ResultSet rs = query.executeSelect();
            try {
                while (rs.next())
                {
                    
                    proteinNode = new ProteinListNode();
                    proteinNode.setInternalID(++nodeCounter);
                    proteinNode.setDbKey(rs.getInt("PROTEIN_intProteinKey"));
                    proteinNode.setDbValue(rs.getString("PROTEIN_strName"));
                    proteinNode.setGeneValue(rs.getString("PROTEIN_strGene"));
                    proteinNode.setLocalisationValue(rs.getString("PROTEIN_strLocalisation"));
                    proteinNode.setDiseaseValue(rs.getString("PROTEIN_strDisease"));
                    proteinNode.setReportDisplayValue(rs.getString("PROTEIN_strDisplayName"));
                    tempNode = new DefaultMutableTreeNode(proteinNode);
                    root.add (tempNode);
                }
            } 
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR, "[ProteinManager] : Failed to load data into tree");
                return false;
            }
            
            
            
            rebuildIndex();
            return true;
        }
        catch (neuragenix.dao.exception.DAOQueryInvalidDomain e)
        {
           LogService.instance().log(LogService.ERROR, "[ProteinManager] DAOQueryInvalidDomain - The domain passed in is invalid");
           return false;
        }
        catch (neuragenix.dao.exception.DAOQueryInvalidField e)
        {
           LogService.instance().log(LogService.ERROR, "[ProteinManager] A field has been detected as being invalid");
           return false;

        }
        catch(neuragenix.dao.exception.DAOException e)
        {
           LogService.instance().log(LogService.ERROR, "[ProteinManager] A generic DAOException has occured");
           return false;
            
        }
       
        
        
    }
    
    
    public void debug_displayList()
    {
        LogService.instance().log(LogService.ERROR, "[ProteinManager] : Warning - debug_displayList called.");
        
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
            
            
        } */
        
        
    }
    
    
    public boolean buildSubLevel(int levelToBuild)
    {
        // get the root node
        
       // DefaultMutableTreeNode headNode = (DefaultMutableTreeNode) root.getChildAt(levelToBuild);
        DefaultMutableTreeNode headNode = (DefaultMutableTreeNode) getTreeNodeFromIndex(levelToBuild);
        
        if (headNode.getChildCount() > 0)
        {
            LogService.instance().log(LogService.ERROR, "[ProteinManager] : Attempted to expand a node that already contains children");
            return false;
        }
        
        DefaultMutableTreeNode tempNode = null;
        
        
        Object nodeData = null;
        
        nodeData = headNode.getUserObject();
        // tempNode = null;
        ProteinListNode tempProteinNode = null;
        int nodeCounter = -1;
        
        
        // TODO: More checking here
        if (nodeData != null)
        {
           tempProteinNode = (ProteinListNode) nodeData;                
            
        }
        else 
            return false;
        
        if (tempProteinNode.getDbKey() == -1)
            return false;
        
        try {
            DALQuery query = new DALQuery();
            AntibodyListNode antiBodyNode = null;
            
            Vector formfields = DatabaseSchema.getFormFields("view_antibody");

            query.setDomain("ANTIBODY", null, null, null);
            query.setFields( formfields, null );
            query.setWhere(null,  0, "ANTIBODY_intProteinKey", "=", tempProteinNode.getDbKey() + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "ANTIBODY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("ANTIBODY_strName", "ASC");
            ResultSet rs = query.executeSelect();
            try {
                while (rs.next())
                {
                    
                    antiBodyNode = new AntibodyListNode();
                    antiBodyNode.setInternalID(++nodeCounter);
                    antiBodyNode.setDbKey(rs.getInt("ANTIBODY_intAntibodyKey"));
                    antiBodyNode.setDbParentKey(rs.getInt("ANTIBODY_intProteinKey"));
                    antiBodyNode.setDbValue(rs.getString("ANTIBODY_strName"));
                    
                    headNode.add(new DefaultMutableTreeNode(antiBodyNode));
                    ((ListNode)headNode.getUserObject()).setBaron(false);
                    
                }
            } 
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR, "[ProteinManager] : There was an error building the subtree");
                e.printStackTrace();
                return false;
            }
            
            
            
        rebuildIndex();   
        return true;
        }
        
        catch (neuragenix.dao.exception.DAOQueryInvalidDomain e)
        {
           LogService.instance().log(LogService.ERROR, "[ProteinManager] : DAO Invalid Domain Exception");
           e.printStackTrace();
           return false;
        }
        catch (neuragenix.dao.exception.DAOQueryInvalidField e)
        {
           LogService.instance().log(LogService.ERROR, "[ProteinManager] : DAO Invalid Field Expation");
           e.printStackTrace();
           return false;

        }
        catch(neuragenix.dao.exception.DAOException e)
        {
           LogService.instance().log(LogService.ERROR, "[ProteinManager] : Generic DAO Exception");
           e.printStackTrace();
           return false;
            
        }
        
        
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
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
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
        
        ListNode newNode = null;
        
        
        // if parent ID = -1, we're adding a top level (protein)
        // if parent ID > 0 we're adding an antibody
        
        if (parentID.intValue() == -1)
        {
            ProteinListNode tempNode = new ProteinListNode();
            tempNode.setDbValue("");
            tempNode.setLocalisationValue("");
            tempNode.setGeneValue("");
            tempNode.setDiseaseValue("");
            tempNode.setReportDisplayValue("");
            newNode = tempNode;
            
        }
        else
        {
            AntibodyListNode tempNode = new AntibodyListNode();
            tempNode.setDbValue("");
            newNode = tempNode;
        }
        
        newNode.edited = true;
        newNode.setEditMode(true);
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
    
    
    
    public int addItem(Integer parentID, String name, String localisation, String gene, String disease, String reportDisplay) {
        
        ListNode newNode = null;
        
        if (parentID.intValue() == -1)
        {
            ProteinListNode tempNode = new ProteinListNode();
            tempNode.setDbValue(name);
            tempNode.setLocalisationValue(localisation);
            tempNode.setGeneValue(gene);
            tempNode.setDiseaseValue(disease);
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
    
     public int updateItem(Integer itemID, String name, String localisation, String gene, String disease, String reportDisplay) {
        
         ListNode updateNode = null;
        
        
        // if parent ID = -1, we're adding a top level (protein)
        // if parent ID > 0 we're adding an antibody
        
        updateNode = getNodeFromIndex(itemID.intValue());
        
        if (updateNode.getNodeType().equals("PROTEIN"))
        {
            updateNode.setDbValue(name);
            ((ProteinListNode) updateNode).setLocalisationValue(localisation);
            ((ProteinListNode) updateNode).setGeneValue(gene);
            ((ProteinListNode) updateNode).setDiseaseValue(disease);
            ((ProteinListNode) updateNode).setReportDisplayValue(reportDisplay);
            
        }
        else
        {
            updateNode.setDbValue(name);
        }
        
        updateNode.edited = true;
        updateNode.setEditMode(false);
        
        rebuildIndex();
        
        return updateNode.getInternalID();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Not used in this implementation
    public boolean addSubCategory(int parentCategoryID, String childCategoryName) 
    {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    
    // Not used in this implementation
    public boolean addToTopLevel(String itemValue) {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    public boolean deleteItem(int itemID) {
    
        ListNode tempNode = getNodeFromIndex(itemID);
        
        tempNode.setDeleted(true);
        
        return true;
    }
    
    // Not currently used, but should be completed in a later version
    public boolean destroy() {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    
    // Not used in this implementation
    public int getChildCount(int itemID) {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return 0;
    }
    
    
    // not used in this implementation
    public String getItem(int itemID) {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return null;
    }
    
    
    
    private DefaultMutableTreeNode getTreeNodeFromIndex (int index)
    {
        DefaultMutableTreeNode treeNode = null;
        
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
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return null;
    }
    
  //  public Hashtable getSubCategory(Tree.DefaultMutableTreeNode parentNode) {
  //  }
    
    public Hashtable getSubCategory(String category) {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return null;
    }
    
    public Hashtable getTopLevel() {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return null;
    }
    
    public void hasSubCategory(int categoryID) {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return;
    }
    
    public boolean isCategoryEditable(int categoryID) {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    public boolean isTopLevelEditable() {
        System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
        return false;
    }
    
    public String translateTreeToXML() {
        StringBuffer toXML = new StringBuffer();
        toXML.append("<Proteins>");
        toXML.append(translateTreeToXML(root));
        toXML.append("</Proteins>");
        return toXML.toString();
        
        
    }
    
     public String translateTreeToXML(DefaultMutableTreeNode node) {
        StringBuffer toXML = new StringBuffer();
        Enumeration treePreOrder = node.preorderEnumeration();
        DefaultMutableTreeNode tempPosition = null;
        ListNode tempListNode = null;
        ProteinListNode tempProteinNode = null;
        AntibodyListNode tempAntibodyNode = null;
        boolean topLevel = false;
        
        if (node != root)
            treePreOrder.nextElement();
        
        
        
        while (treePreOrder.hasMoreElements())
        {
            tempPosition = (DefaultMutableTreeNode)treePreOrder.nextElement();
            tempListNode = (ListNode)tempPosition.getUserObject();
       
            if ((tempListNode != null) && (tempListNode.getDeleted() == false))
            {    
                
                if (tempListNode.getNodeType().equals("PROTEIN"))
                {
                    topLevel = true;
                    tempProteinNode = (ProteinListNode) tempListNode;

                    toXML.append("<Protein>\n");
                    toXML.append("\t<InternalID>\n" + tempProteinNode.getInternalID() + "</InternalID>");
                    toXML.append("\t<Name>\n" + tempProteinNode.getDbValue() + "</Name>");
                    toXML.append("\t<Gene>\n" + tempProteinNode.getGeneValue() + "</Gene>");
                    toXML.append("\t<Localisation>\n" + tempProteinNode.getLocalisationValue() + "</Localisation>");
                    toXML.append("\t<Disease>\n" + tempProteinNode.getDiseaseValue() + "</Disease>");
                    toXML.append("\t<ReportDisplay>\n" + tempProteinNode.getReportDisplayValue() + "</ReportDisplay>");
                    toXML.append("\t<EditMode>" + tempProteinNode.getEditMode() + "</EditMode>");
                    if (tempProteinNode.isBaron() == false)
                    {
                        toXML.append("<Antibodies>");
                        toXML.append(translateTreeToXML((DefaultMutableTreeNode) tempPosition));
                        toXML.append("</Antibodies>");
                        
                    }
                    toXML.append("</Protein>\n");

                }
                else if (tempListNode.getNodeType().equals("ANTIBODY") && (topLevel == false))
                {
                        tempAntibodyNode = (AntibodyListNode) tempListNode;

                        toXML.append("\t<Antibody>\n");
                        toXML.append("\t<InternalID>\n" + tempAntibodyNode.getInternalID() + "</InternalID>");
                        toXML.append("\t<Name>\n" + tempAntibodyNode.getDbValue() + "</Name>");
                        toXML.append("\t<EditMode>" + tempAntibodyNode.getEditMode() + "</EditMode>");
                        toXML.append("\t</Antibody>\n");
        
                }
            }
        }
        
        
        
        
        return toXML.toString();
    }
    
     
     public String processRequest(String request, Hashtable parameters) 
     {
         System.err.println ("[ProteinManager] : A function has been called that is not part of this implementation");
         return null;
     }     
    
     
     public void setEditFlag(int itemID)
     {
         ListNode tempNode = getNodeFromIndex(itemID);
         
         if (tempNode != null)
             tempNode.setEditMode(true);
         else
             LogService.instance().log(LogService.ERROR, "[ProteinManager] : Unable to set edit flag for ID " + itemID);
         
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
               if (tempListNode.isNewNode == true)
               {
                   insertNodeToDatabase(tempListNode, false);
               }
               else if ((tempListNode.edited == true) || (tempListNode.getDeleted() == true))
               {
                   insertNodeToDatabase(tempListNode, true);
               }
               
            }
            
                
        }
        
        root = null; // destroy the old 
        if (buildTopLevelTree() == false)
            LogService.instance().log(LogService.ERROR, "[ProteinManager] : There was an error rebuilding tree");
        
        
        return true;
         
         
         
         
         
     }     
    
     
     
     // Warning : Uses updates to DALQuery for returning correct primary key
     //           NEW DALQUERY OBJECT MUST BE USED OR THIS FUNCTION WILL FAIL.
     //             ==> getInsertedRecordKey()
     
     public boolean insertNodeToDatabase(ListNode node, boolean update)
     {
        
         String domain = null;
         Hashtable valuesToAdd = new Hashtable();
         boolean queryResult = false;
         
         
         try {
            DALQuery query = new DALQuery();
          
            DefaultMutableTreeNode tempNode = null;
            Vector formfields = null;
            formfields = DatabaseSchema.getFormFields("view_" + node.getNodeType().toLowerCase());
            domain = node.getNodeType();
            
        
            valuesToAdd.put(domain + "_strName", node.getDbValue());
            
            if (node.getNodeType().equals("PROTEIN"))
            {
                // valuestoAdd.put (domain + "_strGene", ((ProteinListNode) node).getGeneValue());
                valuesToAdd.put (domain + "_strGene", ((ProteinListNode) node).getGeneValue());
                valuesToAdd.put (domain + "_strLocalisation", ((ProteinListNode) node).getLocalisationValue());
                valuesToAdd.put (domain + "_strDisease", ((ProteinListNode) node).getDiseaseValue());
                valuesToAdd.put (domain + "_strDisplayName", ((ProteinListNode) node).getReportDisplayValue());
            }
            if (node.getNodeType().equals("ANTIBODY"))
            {
                valuesToAdd.put (domain + "_intProteinKey", String.valueOf(node.getDbParentKey()));
            }
            if (node.getDeleted() == true)
                valuesToAdd.put (domain + "_intDeleted", "-1");
            else
                valuesToAdd.put (domain + "_intDeleted", "0");
            
            query.setDomain(domain, null, null, null);
            
            query.setFields( formfields, valuesToAdd );
                        
            if (update == true)
            {
                
                String keyName = String.valueOf(domain.charAt(0));
                keyName = keyName + domain.substring(1).toLowerCase();
                
                valuesToAdd.put ((domain + "_int" + keyName + "Key"), String.valueOf(node.getDbKey()));
                
                // valuesToAdd.put(domain + "_int" + keyName + "Key", node.getDbValue());
                query.setWhere(null, 0, domain + "_int" + keyName + "Key", "=", String.valueOf(node.getDbKey()), 0, DALQuery.WHERE_HAS_VALUE);
                
                
                queryResult = query.executeUpdate();
                if (queryResult == true)
                {
                    node.edited = false;
                    node.isNewNode = false;
                }
                return queryResult;
            }
            
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
             LogService.instance().log(LogService.ERROR, "[ProteinManager] : There was an error while writing out nodes");
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

             if (parameters.get("PROTEIN_strReportDisplay") != null)
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




