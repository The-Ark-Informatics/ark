/*
 * BiospecimenSideTree.java
 *
 * Copyright (C) Neuragenix Pty Ltd, 2005
 * 
 * Description : Washed, Rinsed and Spun Dry implementation of the recursive side tree
 *
 * @author  Daniel Murley
 * @email  dmurley@neuragenix.com
 *
 * @since Biogenix v4.5
 *
 */

package neuragenix.bio.biospecimen;


import javax.swing.tree.*;
import java.util.*;
import java.sql.ResultSet;
import neuragenix.common.*;
import neuragenix.dao.*;
import neuragenix.security.*;
import java.lang.StringBuffer;

public class BiospecimenSideTree 
{
    AuthToken authToken = null;
    DefaultMutableTreeNode treeSideTree = null;
    
    
    /** Creates a new instance of BiospecimenSideTree */
    public BiospecimenSideTree(AuthToken authToken) throws Exception
    {
        if (authToken == null)
            throw new Exception("Null Authtoken passed to BiospecimenSideTree Class");
        else
            this.authToken = authToken;
    }
  
    
    public String getBiospecimenTree(int intBiospecimenID)
    {
        return null;
    }

    public void buildBiospecimenTree(String internalBiospecimenID)
    {
        DefaultMutableTreeNode biospecimen_sideTree = new DefaultMutableTreeNode("root");
        
        if (internalBiospecimenID == null || internalBiospecimenID.equals(""))
        {
            return;
        }
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);
            Vector vtFormFields = DatabaseSchema.getFormFields("cbiospecimen_tree_biospecimen");
            query.clearDomains();
            query.clearFields();
            query.clearWhere();            
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(vtFormFields, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", internalBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE); 
            
            ResultSet rs = query.executeSelect();
            
            if(rs.next())
            {
                Hashtable htBiospecimenDetails = new Hashtable();
                
                for (int i = 0; i < vtFormFields.size(); i++)
                {
                    String strFieldName = (String) vtFormFields.get(i);
                    String strValue = rs.getString(strFieldName);
	            if (strValue != null)
                    {
		        htBiospecimenDetails.put(strFieldName, strValue);
                    }
                    
                }
                
                String strInternalParentID = (String) htBiospecimenDetails.get("BIOSPECIMEN_intParentID");
                String strInternalBiospecimenID = (String) htBiospecimenDetails.get("BIOSPECIMEN_intBiospecimenID");
                                
                if(strInternalParentID.equals("-1"))
                {
                    biospecimen_sideTree = getChildren(new DefaultMutableTreeNode(htBiospecimenDetails));
                }
                else
                {
                    biospecimen_sideTree = getParent(strInternalBiospecimenID, htBiospecimenDetails);
                    biospecimen_sideTree = getChildren(biospecimen_sideTree);
                }
            }
            rs.close();
            this.treeSideTree = biospecimen_sideTree;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            this.treeSideTree = biospecimen_sideTree;
        }
    }
    
    public String buildXMLForBiospecimenSideTree(String strCurrentBiospecimenKey)
    {
        StringBuffer strXML = new StringBuffer();
       
        
        if (treeSideTree != null)
        {
            Enumeration enumTree = treeSideTree.preorderEnumeration();
            strXML.append("<biospecimenSideTree>");
            while (enumTree.hasMoreElements())
            {
                DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) enumTree.nextElement();
                Hashtable htBioDetails = (Hashtable) tempNode.getUserObject();
                String strBiospecimenID = (String) htBioDetails.get("BIOSPECIMEN_strBiospecimenID");
                String strBiospecimenKey = (String) htBioDetails.get("BIOSPECIMEN_intBiospecimenID");
                boolean blCurrentNode = false;
                boolean blHasChildren = false;
                if (strCurrentBiospecimenKey.equals(strBiospecimenKey))
                {
                    blCurrentNode = true;
                }
                
                if (tempNode.getChildCount() > 0)
                {
                    blHasChildren = true;
                }
                
                strXML.append("<node internalID=\"" + strBiospecimenKey + "\" depth=\"" + tempNode.getLevel() + "\" hasChildren=\"" + blHasChildren + "\" currentNode=\"" + blCurrentNode + "\">");
                strXML.append(strBiospecimenID);
                strXML.append("</node>");
            }
            strXML.append("</biospecimenSideTree>");
        }
        return strXML.toString();
    }
    
    private DefaultMutableTreeNode getParent(String strInternalBiospecimenID, Hashtable htBiospecimenDetails)
    {
        DefaultMutableTreeNode biospecimen_parent = new DefaultMutableTreeNode(htBiospecimenDetails);
        boolean isParent = false;
        try
        {
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);        
            Vector vtFormFields = DatabaseSchema.getFormFields("cbiospecimen_tree_biospecimen");
            do
            {
                String strInternalParentID = (String) htBiospecimenDetails.get("BIOSPECIMEN_intParentID");
                if (strInternalParentID != null)
                {
                    
                    query.clearDomains();
                    query.clearFields();
                    query.clearWhere();            
                    query.setDomain("BIOSPECIMEN", null, null, null);
                    query.setFields(vtFormFields, null);
                    query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=",  strInternalParentID, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0",0,DALQuery.WHERE_HAS_VALUE); 
                    query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC");
                    ResultSet rs = query.executeSelect();
                    
                    
                    
                    if(rs.next())
                    {
                       for (int i = 0; i < vtFormFields.size(); i++)
                       {
                           String key = (String) vtFormFields.get(i);
                           String value = rs.getString(key);
                           
                           if (value != null)
                           {
                              htBiospecimenDetails.put(key, value); 
                           }
                       }
                       
                       String strTempInternalParentID = (String) htBiospecimenDetails.get("BIOSPECIMEN_intParentID");
                       
                       
                        if(strTempInternalParentID.equals("-1"))
                        {
                            biospecimen_parent = new DefaultMutableTreeNode(htBiospecimenDetails);
                            isParent = true;
                        } 
                    }
                    rs.close();
                }
                else
                {
                    System.out.println ("Unable to read internal parent ID -- potential infinite loop");
                }
            }while(!isParent);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return biospecimen_parent;
    }
    
    private DefaultMutableTreeNode getChildren(DefaultMutableTreeNode parent)
    {
        Hashtable hshparent = null;
        Hashtable hshchild = null;
        DefaultMutableTreeNode child = null;
        DefaultMutableTreeNode Tnparent = null;
        Tnparent = parent;
        try
        {
            Vector vtFormFields = DatabaseSchema.getFormFields("cbiospecimen_tree_biospecimen");
            hshparent = (Hashtable)Tnparent.getUserObject();
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);
            
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(vtFormFields, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intParentID", "=",hshparent.get("BIOSPECIMEN_intBiospecimenID") , 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE); 
            query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC");
            ResultSet rs = query.executeSelect();
            int j = 0;
            while(rs.next())
            {
                hshchild = new Hashtable();
                for(int i = 0; i < vtFormFields.size(); i++)
                {
                    String key = (String) vtFormFields.get(i);
                    String value = rs.getString(key);
                    if (value != null)
                    {
                        hshchild.put(key, value);
                    }

                }
                Tnparent.add(getChildren(new DefaultMutableTreeNode(hshchild)));
            }

           rs.close();
           return Tnparent;  
        }
        catch(Exception e)
        {
            e.printStackTrace();
             return Tnparent;
        }
       
    }
    
    
    
    
    
    
    
    
    
    
}
