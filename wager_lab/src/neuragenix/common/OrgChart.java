/*
 * OrgChart.java
 *
 * Created on February 26, 2004, 12:36 PM
 */
package neuragenix.common;

// java classes
import java.util.*;
import java.io.*;
import javax.swing.tree.*;
import java.sql.ResultSet;

import java.sql.ResultSetMetaData;
import org.jasig.portal.services.LogService;

// neuragenix classes
import neuragenix.dao.*;




/**
 *
 * @author  longtran
 */
public class OrgChart {
    
    static{
        InputStream file = OrgChart.class.getResourceAsStream("OrgTree.xml");
        DatabaseSchema.loadDomains(file,"OrgTree.xml");
    }

    
    /** Creates a new instance of OrgChart */
    public OrgChart() {
        
        
      
    }
    
     /**
     *    Method to buid the usergroup tree
     * @return DetaultMutableTreeNode tree structure
     * @param expanded hashtable contains expanded info
      * @param groupAction String action when user clicks on group
      * @param userAction String action when user clicks on user
      *
     */
    public static DefaultMutableTreeNode buildOrgTree(Hashtable expanded, String groupAction, String userAction ){
        
        
        try{
            
            
            
            
            Vector vtFormField = new Vector();
            vtFormField = DatabaseSchema.getFormFields( "orggrouptree_view");
            //		DALSecurityQuery sQuery = new DALSecurityQuery ( ORG_CHART_VIEW, authToken );
            DALQuery sQuery = new DALQuery();
            sQuery.setDomain( "ORGGROUPTREE", null, null, null );
            sQuery.setFields( vtFormField , null );
            sQuery.setWhere( null, 0, "ORGGROUPTREE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            sQuery.setOrderBy( "ORGGROUPTREE_intOrgGroupKey", "ASC" );
            ResultSet rs = sQuery.executeSelect();
                        
            // root node info
            Hashtable hNode = new Hashtable();
            hNode.put( "ORGGROUPTREE_intParentGroupKey", "-2" );
            hNode.put( "ORGGROUPTREE_intOrgGroupKey" , "-1" );
            hNode.put( "ORGGROUPTREE_strOrgGroupName" , "Root" );
            hNode.put( "url_action" , groupAction );
            
            // create a root node            
            DefaultMutableTreeNode myTree = new DefaultMutableTreeNode( hNode );
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) myTree.getRoot();
            
            // get information of each record to build the tree
            while ( rs.next() ) {
                  
                boolean added = false;
                Enumeration tree_enum = myTree.breadthFirstEnumeration();
                
                DefaultMutableTreeNode cNode = new DefaultMutableTreeNode();
                DefaultMutableTreeNode nNode = new DefaultMutableTreeNode( makeHash(rs));
               
                // get the records attributes
                String recGroupKey  = rs.getString("ORGGROUPTREE_intOrgGroupKey");
                String recParentKey = rs.getString("ORGGROUPTREE_intParentGroupKey");

                //System.err.println("RS: g=" + recGroupKey + ", p=" + recParentKey);                
                
                // go in to each node and assign all the child nodes first
                while( tree_enum.hasMoreElements() ){
                    
                    cNode = (DefaultMutableTreeNode) tree_enum.nextElement();                    
                    hNode = (Hashtable) cNode.getUserObject();
                  
                    // check if the current record is the PARENT of some node.
                    if ( recGroupKey.equals(hNode.get("ORGGROUPTREE_intParentGroupKey")) ) {
                        // create Node
                        // make the new node the child of the cNode's parrent
                        // then add the cNode to the new node
                        cNode.removeFromParent(); // remove child node from its parent
                        nNode.add( cNode );       // make the current record the parent
                    }
                }

                // go in to each node again and assign the parent nodes.  Also,
                // check for child nodes in cause any has been missed!
                tree_enum = myTree.breadthFirstEnumeration();
                while( tree_enum.hasMoreElements() ){

                    cNode = (DefaultMutableTreeNode) tree_enum.nextElement();                    
                    hNode = (Hashtable) cNode.getUserObject();

                    String childGroupKey  = hNode.get("ORGGROUPTREE_intOrgGroupKey").toString();
                    String childParentKey = hNode.get("ORGGROUPTREE_intParentGroupKey").toString();

                    //System.err.println(" H: g=" + childGroupKey + " p=" + childParentKey);
                                        
                    // check if the current record is the CHILD of some node.
                    if( recParentKey.equals(childGroupKey)  ){
                        
                        cNode.add( nNode );      // current record is added to the tree!
                        added = true;            // mark flag as added
  
                        //System.err.println(childGroupKey + "->" + recGroupKey);
                    
                    // check if the current record is the PARENT of some node.
                    } else if ( recGroupKey.equals(childParentKey) ) {
                        
                        // create Node
                        // make the new node the child of the cNode's parrent
                        // then add the cNode to the new node
                        cNode.removeFromParent(); // remove child node from its parent
                        nNode.add( cNode );       // make the current record the parent
                        
                        //System.err.println(recGroupKey + "->" + childGroupKey);
                   }

                }

                // if current record hasn't been added to the tree yet, add it!
                if (!added)                   
                    rootNode.add( nNode );
            }            
            
            Enumeration tree_enum = myTree.breadthFirstEnumeration();
            
            // skip the root
            tree_enum.nextElement();
            
            // assign action url to groups
            while( tree_enum.hasMoreElements() ){
                
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) tree_enum.nextElement();
                hNode = (Hashtable) cNode.getUserObject();
                hNode.put("url_action",groupAction);
                
            }
            
            // append users
            DALQuery query = new DALQuery();
            vtFormField = DatabaseSchema.getFormFields("orgusertree_view");
            
            // start from root group again
            tree_enum = myTree.breadthFirstEnumeration();
            tree_enum.nextElement();
            
            
            // add users to group
            try{
                // go into every node
                while (tree_enum.hasMoreElements()){
                    
                    // set up the current node
                    DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) tree_enum.nextElement();
                    hNode = (Hashtable) cNode.getUserObject();
                    
                    
                    
                    // look for users of EXPANDED groups

                    if( (hNode.get("ORGGROUPTREE_intOrgGroupKey") != null) && ( expanded == null || expanded.containsKey( hNode.get("ORGGROUPTREE_intOrgGroupKey") ) )){

                        
                        query.reset();
                        query.setDomain( "ORGUSERGROUPTREE",null,null,null );
                        query.setDomain( "ORGUSERTREE","ORGUSERGROUPTREE_intOrgUserKey","ORGUSERTREE_intOrgUserKey","INNER JOIN");
                        query.setFields( vtFormField,null );
                        
                        // set up criteria for select query
                        query.setWhere( null,0,"ORGUSERGROUPTREE_intOrgGroupKey","=",hNode.get("ORGGROUPTREE_intOrgGroupKey")
                        ,0,DALQuery.WHERE_HAS_VALUE );
                        query.setWhere( "AND",0,"ORGUSERGROUPTREE_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE );
                        
                        rs = null;
                        rs = query.executeSelect();
                        
                        // add record to the tree
                        Vector vtChild = QueryChannel.lookupRecord( rs, vtFormField );
                        rs.beforeFirst();
                        
                        for( int i = 0; i < vtChild.size(); i++ ){
                            
                            rs.next();
                            DefaultMutableTreeNode nNode = new DefaultMutableTreeNode( makeHash( rs ) );
                            
                            
                            ((Hashtable) nNode.getUserObject()).put( "url_action", userAction );
                            ((Hashtable) nNode.getUserObject()).put( "ORGGROUPTREE_intParentGroupKey", hNode.get("ORGGROUPTREE_intOrgGroupKey") );
                            cNode.add( nNode  );
                            
                        
                        }
                        
                        
                    }
                    
                    
                }// end while
                
            }catch(Exception e){
                
                LogService.instance().log(LogService.ERROR,
                "Unknown error in COrgChart - " + e.toString(), e);
            }
            
            return myTree;
            
        }catch(Exception e){
            
            LogService.instance().log(LogService.ERROR,
            "Unknown error in COrgChart - " + e.toString(), e);
        }
        
        return null;
        
    }
    /**
     *    Method to generate the usergroup tree XML String
     * @param myTree DefaultMutableTreeNode tree stucture
     * @param expandedTable HashTable tree expand details
     * @return XML string of the tree
     */
    
    public static String toXML(DefaultMutableTreeNode myTree, Hashtable expandedTable){
        
        StringBuffer strXML = new StringBuffer();
        
        // traverse through tree elements
        Enumeration tree_enum = myTree.breadthFirstEnumeration();
        tree_enum.nextElement();
        
        strXML.append("<orgTree>");
        
        // go in to each node
        while (tree_enum.hasMoreElements()){
            
            Hashtable hNode = (Hashtable) (
            ((DefaultMutableTreeNode) tree_enum.nextElement()).getUserObject());
            
            strXML.append("<treeNode>");
            
            Enumeration enuma = hNode.keys();
            
            // build elements
            while( enuma.hasMoreElements() ){
                
                String cKey = enuma.nextElement().toString();
                
                strXML.append( "<" + cKey + ">");
                strXML.append( hNode.get( cKey ) );
                strXML.append( "</" + cKey + ">" );
                
            }
            
            // expanded detail
            if( ( hNode.get("ORGGROUPTREE_intOrgGroupKey") != null ) && (expandedTable.containsKey( hNode.get("ORGGROUPTREE_intOrgGroupKey") ))){
                strXML.append("<expanded>").append("true").append("</expanded>");
            }else{
                strXML.append("<expanded>").append("false").append("</expanded>");
            }
            
            strXML.append("</treeNode>");
            
        }
        
        strXML.append("</orgTree>");
        
        return strXML.toString();
    }
    
      /**
     *	Method to convert a ResultSet to a Hashtable
     *	@param rs ResultSet to be converted
     * 	@return Hastable after converting
     *
     **/
    
    private static Hashtable makeHash( ResultSet rs ){
        
        Hashtable hs = new Hashtable();
        
        try{
            
    //        System.err.println("\nMaking hashtable:");
            
            // get record information
            ResultSetMetaData rsmd = rs.getMetaData();
            for( int i=1; i <= rsmd.getColumnCount(); i++ ){
                
   //             System.err.println("[" + i + "]" + "column=" + rsmd.getColumnName(i) + ", value=" + (rs.getString(i) == null ? "" : rs.getString(i)));
                
                if( rs.getString( i ) != null )
                    hs.put( rsmd.getColumnName( i ), rs.getString( i ) );
                else
                    hs.put( rsmd.getColumnName( i ), "" );
            }
            
    //        System.err.println("");
            
        }catch(Exception e){
            
            LogService.instance().log(LogService.ERROR,
            "Unknown error in COrgChart - " + e.toString(), e);
        }
        return hs;
        
        
    }
}
