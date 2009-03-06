/*

 * BiospecimenBaseChannel.java

 *

 * Created on May 7, 2003, 12:12 PM

 */



package neuragenix.bio.biospecimen;



/**

 *

 * @author  hh

 */



// sun packages

import java.util.Hashtable;

import java.util.Vector;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import java.sql.*;

import org.jasig.portal.services.LogService;


// neuragenix packages

import neuragenix.common.BaseChannel;

import neuragenix.common.Tree;

import neuragenix.common.Utilities;

import neuragenix.security.AuthToken;

import neuragenix.common.exception.*;

import neuragenix.dao.*;

import neuragenix.bio.patient.PatientFormFields;

import neuragenix.bio.biospecimen.BiospecimenFormFields;

/** calls the super class BaseChannel */
public class BiospecimenBaseChannel extends BaseChannel{

    private int intStartRecord;
    private int intRecordPerPage;
    private int intRecordSetSize;
    private Vector vtRecordSet;
//by rennypv...created to store the tree created from the records    
    DefaultMutableTreeNode biospecimenTree;
    DefaultMutableTreeNode parentNode;
    Vector pNodes;    
    /** calls it super class */    
    public BiospecimenBaseChannel()

    {

        super();

    }

    

    /** calls its super class
     * @param authToken checks for the permissions
     */    
    public BiospecimenBaseChannel(AuthToken authToken)

    {

        super(authToken);

    }

//To draw the side tree----rennypv
    /** Used to draw the side tree . Creates a static DefaultMutableTreeNode to save all
     * the records. If a biospecimen view then the variable passed in is
     * intBiospecimenID and if it is from any other source then the parameter passed
     * in is the strBiospecimenID. The loop is used to find the parent of the
     * strBiospecimenID that is clicked
     * @param strBiospecimenID is a intBiospecimenID if from the view and strBiospecimenID from other pages
     * @param strCurrent to distinguish whether the page source is a view or any other page
     * @return returns the tree starting from the parent of the biospecimen selected
     */    
    public DefaultMutableTreeNode buildBiospecimenTree(String intBiospecimenID)
    {
        DefaultMutableTreeNode biospecimen_sideTree = new DefaultMutableTreeNode("root");
        try
        {
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);
            Vector formFields = DatabaseSchema.getFormFields("cbiospecimen_tree_biospecimen");
            query.clearDomains();
            query.clearFields();
            query.clearWhere();            
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(formFields, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID.toString(), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE); 
            
            ResultSet rs = query.executeSelect();
            
            if(rs.next())
            {
                Hashtable hshbio = new Hashtable();
                
                for(int i = 0;i<formFields.size();i++)
                {
                    String key = (String) formFields.get(i);
                    String keyValue = rs.getString(key);
                    hshbio.put(key, keyValue);
                    
                }
                if(rs.getString("BIOSPECIMEN_strParentID").equals("N/A"))
                {
                    biospecimen_sideTree = getChildren(new DefaultMutableTreeNode(hshbio));
                }
                else
                {
                    biospecimen_sideTree = getParent(rs.getString("BIOSPECIMEN_intBiospecimenID"),hshbio);
                    biospecimen_sideTree = getChildren(biospecimen_sideTree);
                }
            }
            return biospecimen_sideTree;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return biospecimen_sideTree;
        }
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
            query.setWhere(null, 0, "BIOSPECIMEN_strParentID", "=",hshparent.get("BIOSPECIMEN_strBiospecimenID") , 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE); 
            ResultSet rs = query.executeSelect();

            
            // if(rs.next()) // used to be get fetch size
            // {
                int j = 0;
                while(rs.next())
                {
                    hshchild = new Hashtable();
                    for(int i = 0; i < vtFormFields.size(); i++)
                    {
                        String key = (String) vtFormFields.get(i);
                        String keyValue = rs.getString(key);
                        
                        hshchild.put(key, rs.getString(key));
                    }
                    Tnparent.add(getChildren(new DefaultMutableTreeNode(hshchild)));
                }
           //  }
            
           return Tnparent;  
        }
        catch(Exception e)
        {
            e.printStackTrace();
             return Tnparent;
        }
       
    }
    
    private DefaultMutableTreeNode getParent(String intBiospecimenID,Hashtable hshbio)
    {
        
        DefaultMutableTreeNode biospecimen_parent = new DefaultMutableTreeNode(hshbio);
        boolean isParent = false;
        try
        {
            Vector vtFormFields = DatabaseSchema.getFormFields("cbiospecimen_tree_biospecimen");
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_view", authToken);        
            do
            {
                query.clearDomains();
                query.clearFields();
                query.clearWhere();            
                query.setDomain("BIOSPECIMEN", null, null, null);
                query.setFields(vtFormFields, null);
                query.setWhere(null, 0, "BIOSPECIMEN_strBiospecimenID", "=", (String)hshbio.get("BIOSPECIMEN_strParentID"), 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE); 
                ResultSet rs = query.executeSelect();
                
                if(rs.next())
                {
                   for(int i = 0; i < vtFormFields.size(); i++)
                    {
                        String key = (String) vtFormFields.get(i);
                        hshbio.put(key, rs.getString(key));
                    }
                    if(rs.getString("BIOSPECIMEN_strParentID").equals("N/A"))
                    {
                        biospecimen_parent = new DefaultMutableTreeNode(hshbio);
                        isParent = true;
                    } 
                }

            }while(!isParent);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return biospecimen_parent;
    }

     /** This method builds the tree structure of all the records
      * @return returns the defaultmutabletree.. All records in the tree form
      */     
    public DefaultMutableTreeNode buildBiospecimenTree()

    {

        biospecimenTree = new DefaultMutableTreeNode("root");


        try

        {    

            vtRecordSet = lookupRecord();

            for (int i=0; i < vtRecordSet.size(); i++)

            {
                Hashtable hashNewNode = (Hashtable) vtRecordSet.get(i);

                

                

                Enumeration enum = biospecimenTree.breadthFirstEnumeration();
                
               

                enum.nextElement();// skip the root
                

                boolean isChild = false;

                boolean isParent = false;



                //DefaultMutableTreeNode currentNode = null;

                DefaultMutableTreeNode cNode = null;

                pNodes = new Vector();

                while (enum.hasMoreElements())

                {

                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) enum.nextElement();
                    

                    Hashtable hashTemp = (Hashtable) currentNode.getUserObject();



                    // new node is the parent of a tree node

                    if (hashTemp.get("strBiospecParentID").toString().equals(

                        hashNewNode.get("strBiospecimenID").toString()))

                    {
                        

                        isParent = true;
                        
                        
                        pNodes.add(currentNode);

                        //break;

                    }



                    // new node is a child of a tree node

                    if (hashTemp.get("strBiospecimenID").toString().equals(

                        hashNewNode.get("strBiospecParentID").toString()))

                    {

                        isChild = true;
                        

                        cNode = currentNode;

                        //break;

                    }

                }


                if (isParent)

                {

                    parentNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) pNodes.get(0)).getParent();

                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(hashNewNode);


                    for (int j=0; j < pNodes.size(); j++)

                    {

                        newNode.add((DefaultMutableTreeNode) pNodes.get(j));

                    }

                    

                    if (isChild) 

                    {

                        cNode.add(newNode);

                    }

                    else

                    {

                        parentNode.add(newNode);

                    }

                }

                else if (isChild)

                {

                    cNode.add(new DefaultMutableTreeNode(hashNewNode));

                }

                else

                {

                    biospecimenTree.add(new DefaultMutableTreeNode(hashNewNode));

                }


            }

        }

        catch (Exception e)

        {

            e.printStackTrace();

        }

        
        return biospecimenTree;

    }
    
    /** This creates the XML file required to display the tree
     * @param root the parent from where the xml is to be built is passedin
     */    
    public void buildXMLFileForBiospecimenTree(DefaultMutableTreeNode root)

    {
        if(strXML == null)
        {
            strXML = "";
        }
       
        if(intRecordPerPage != 0)
        {
            if(this.intRecordSetSize == 0)
            {
                this.intRecordSetSize = root.getChildCount();
            }



    
            int lastrecord = intStartRecord + intRecordPerPage;
            if(lastrecord > intRecordSetSize)
            {
                lastrecord = intRecordSetSize;
            }
            if (!root.isLeaf())

            {

    //            for (int i=0; i < root.getChildCount(); i++)
                for (int i=intStartRecord; i < lastrecord ; i++)
                    strXML += getNodeXML((DefaultMutableTreeNode) root.getChildAt(i));

            }
        }
        else
        {
            if (!root.isLeaf())

            {

                strXML += getNodeXML((DefaultMutableTreeNode)root);

            }
            else
            {

                    strXML += getNodeXML((DefaultMutableTreeNode)root);
            }
        }

        

        //System.out.println(strXML);

    }

    /** returns the record set size
     * @return returns the int of the recordsetsize
     */    
     public int getRecordSetSize() 
     {

        return intRecordSetSize;

    }
     /** the starting index of the records and the records/page is set
      * @param startRecord the starting index of the records
      * @param recordPerPage record/page
      */     
    public void setLimit(int startRecord, int recordPerPage) 
    {

        intStartRecord = startRecord;

        intRecordPerPage = recordPerPage;

    }
    
    //rennypv to add the new record in the biospecimen_Tree
    /** The super method overridden to accomodate the addition of the same biospecimen
     * into the static biospecimen_Tree
     * @param intParentID this is the ParentID of the new Biospecimen created
     */    
    public void addRecord(String intParentID)
    {
       try
       {
            super.addRecord();
       }
       catch(Exception e)
       {
            e.printStackTrace();
       }
            

    }
    
    //rennypv
    

    /** this method creates the XML for the branch and the nodes
     * @param node the node whose XML is to be created
     * @return The string XML
     */    
       public String getNodeXML(DefaultMutableTreeNode node)

    {

        String strResult = "";
        
        Hashtable hashTemp = null;
        
        if (node.getUserObject() instanceof Hashtable)
        {
            hashTemp = (Hashtable) node.getUserObject();
        }
        else
        {
            return "";
        }
        

        

        if (node.isLeaf())

        {

            strResult = "<leaf>";
            
            if(hashTemp.get("strBiospecimenID") != null)
            {

                strResult += "<strBiospecimenID>" + hashTemp.get("strBiospecimenID") + "</strBiospecimenID>";

                strResult += "<intBiospecimenID>" + hashTemp.get("intBiospecimenID") + "</intBiospecimenID>";

                strResult += "<strBiospecSampleType>" + hashTemp.get("strBiospecSampleType") + "</strBiospecSampleType>";

                strResult += "<strBiospecSampleSubType>" + hashTemp.get("strBiospecSampleSubType") + "</strBiospecSampleSubType>";

                strResult += "<dtBiospecSampleDate>" + hashTemp.get("dtBiospecSampleDate") + "</dtBiospecSampleDate>";

                strResult += "<intBiospecNumCollected>" + hashTemp.get("intBiospecNumCollected") + "</intBiospecNumCollected>";

                strResult += "<intBiospecNumRemoved>" + hashTemp.get("intBiospecNumRemoved") + "</intBiospecNumRemoved>";

                strResult += "<intCellID>" + hashTemp.get("intCellID") + "</intCellID>";

                strResult += "<intInternalPatientID>" + hashTemp.get("intInternalPatientID") + "</intInternalPatientID>";

            }
            else if(hashTemp.get("BIOSPECIMEN_strBiospecimenID") != null)
            {
                strResult += "<strBiospecimenID>" + hashTemp.get("BIOSPECIMEN_strBiospecimenID") + "</strBiospecimenID>";

                strResult += "<intBiospecimenID>" + hashTemp.get("BIOSPECIMEN_intBiospecimenID") + "</intBiospecimenID>";

                strResult += "<strBiospecSampleType>" + hashTemp.get("BIOSPECIMEN_strSampleType") + "</strBiospecSampleType>";

/*                strResult += "<strBiospecSampleSubType>" + hashTemp.get("strBiospecSampleSubType") + "</strBiospecSampleSubType>";

                strResult += "<dtBiospecSampleDate>" + hashTemp.get("dtBiospecSampleDate") + "</dtBiospecSampleDate>";

                strResult += "<intBiospecNumCollected>" + hashTemp.get("intBiospecNumCollected") + "</intBiospecNumCollected>";

                strResult += "<intBiospecNumRemoved>" + hashTemp.get("intBiospecNumRemoved") + "</intBiospecNumRemoved>";

                strResult += "<intCellID>" + hashTemp.get("intCellID") + "</intCellID>";

                strResult += "<intInternalPatientID>" + hashTemp.get("intInternalPatientID") + "</intInternalPatientID>";*/
            }
                
            strResult += "</leaf>";

        }

        else

        {
            if(hashTemp.get("strBiospecimenID") != null)
            {
                strResult += "<strBiospecimenID>" + hashTemp.get("strBiospecimenID") + "</strBiospecimenID>";

                strResult += "<intBiospecimenID>" + hashTemp.get("intBiospecimenID") + "</intBiospecimenID>";

                strResult += "<strBiospecSampleType>" + hashTemp.get("strBiospecSampleType") + "</strBiospecSampleType>";

                strResult += "<strBiospecSampleSubType>" + hashTemp.get("strBiospecSampleSubType") + "</strBiospecSampleSubType>";

                strResult += "<dtBiospecSampleDate>" + hashTemp.get("dtBiospecSampleDate") + "</dtBiospecSampleDate>";

                strResult += "<intBiospecNumCollected>" + hashTemp.get("intBiospecNumCollected") + "</intBiospecNumCollected>";

                strResult += "<intBiospecNumRemoved>" + hashTemp.get("intBiospecNumRemoved") + "</intBiospecNumRemoved>";

                strResult += "<intCellID>" + hashTemp.get("intCellID") + "</intCellID>";

                strResult += "<intInternalPatientID>" + hashTemp.get("intInternalPatientID") + "</intInternalPatientID>";
            }

             else if(hashTemp.get("BIOSPECIMEN_strBiospecimenID") != null)
            {
                strResult += "<strBiospecimenID>" + hashTemp.get("BIOSPECIMEN_strBiospecimenID") + "</strBiospecimenID>";

                strResult += "<intBiospecimenID>" + hashTemp.get("BIOSPECIMEN_intBiospecimenID") + "</intBiospecimenID>";

                strResult += "<strBiospecSampleType>" + hashTemp.get("BIOSPECIMEN_strSampleType") + "</strBiospecSampleType>";
             }

            

            for (int i=0; i < node.getChildCount(); i++)
            {
                DefaultMutableTreeNode temp = (DefaultMutableTreeNode)node.getChildAt(i);
                strResult += getNodeXML((DefaultMutableTreeNode) node.getChildAt(i));
                
            }
            if(hashTemp.get("strBiospecimenID") != null)
            {
                strResult = "<branch id=\"" + hashTemp.get("strBiospecimenID") + "\">" + strResult + "</branch>";
            }
             else if(hashTemp.get("BIOSPECIMEN_strBiospecimenID") != null)
            {
                strResult = "<branch id=\"" + hashTemp.get("BIOSPECIMEN_strBiospecimenID") + "\">" + strResult + "</branch>";
             }

        }

        

        return strResult;

    }


    /** To check whether the biospecimen has a parent or not
     * @return blReturnValue the output which indicates whether the current biospecimen has a parent or not
     * @param strActivity activity that is required to be done
     * @param strBiospecimenID the current biospecimenID
     * @param authToken the authorization token
     * @throws BaseChannelException if an exception occur
     */

    public boolean hasParent( String strBiospecimenID, AuthToken authToken,
        String strActivity ) throws BaseChannelException
    {
        boolean blReturnValue = false;

        try
        {         
            // Load a new Query object 
            Query my_query = new Query( Query.SELECT_QUERY, strActivity, authToken );
            my_query.setDeletedColumn( blDeletedColumn );
            my_query.setCaseSensitive( blTurnLowerCaseOff );

            // Set the domain
            my_query.setDomainName( "BIOSPECIMEN" );
            my_query.setField( "intBiospecParentID", "" );
            my_query.setWhere( "", "intBiospecParentID", "NOT_EQUALS_OPERATOR", "-1" );
            my_query.setWhere( DBSchema.AND_CONNECTOR, "intBiospecimenID", "EQUALS_OPERATOR", strBiospecimenID);


            boolean blExcuteOK = my_query.execute();
            ResultSet my_resultset = my_query.getResults();
	    // indicates that the query is getting a result meaning this
            // biospecimen has a parent
            if ( my_resultset.next() )
            {
                blReturnValue = true;
            }

            // Return the ResultSet so it can be closed!
            my_query.killResultSet(my_resultset);
            my_query = null;
        }
        catch( Exception e )
        {
            LogService.instance().log( LogService.ERROR,
	         "Biospecimen-BaseChannelException - Unknown error while trying to execute hasParent method - " +  e.toString(), e );

            throw new BaseChannelException( "Unknown error while trying to execute hasParent method - " + e.toString(), e );
        }

	return blReturnValue;
    }


    /** To check whether the biospecimen has a child or not
     * @param strBiospecimenID the current biospecimenID
     * @param authToken the authorization token
     * @param strActivity the activity that is going to be run
     * @return blReturnValue the output which indicates whether the current biospecimen has a child or not
     * @throws BaseChannelException if an exception occur
     */

    public boolean hasChild( String strBiospecimenID, AuthToken authToken,
        String strActivity ) throws BaseChannelException
    {
        boolean blReturnValue = false;

        try
        {         
            // Load a new Query object 
            Query my_query = new Query( Query.SELECT_QUERY, strActivity, authToken );
            my_query.setDeletedColumn( blDeletedColumn );
            my_query.setCaseSensitive( blTurnLowerCaseOff );

            // Set the domain
            my_query.setDomainName( "BIOSPECIMEN" );
            my_query.setField( "intBiospecParentID", "" );
            my_query.setWhere( "", "intBiospecParentID", "EQUALS_OPERATOR", strBiospecimenID );


            boolean blExcuteOK = my_query.execute();
            ResultSet my_resultset = my_query.getResults();
	    // indicates that the query is getting a result meaning this
            // biospecimen has a child
            if ( my_resultset.next() )
            {
                blReturnValue = true;
            }

            // Return the ResultSet so it can be closed!
            my_query.killResultSet(my_resultset);
            my_query = null;
        }
        catch( Exception e )
        {
            LogService.instance().log( LogService.ERROR,
	         "Biospecimen-BaseChannelException - Unknown error while trying to execute hasParent method - " +  e.toString(), e );

            throw new BaseChannelException( "Unknown error while trying to execute hasParent method - " + e.toString(), e );
        }

	return blReturnValue;
    }
}
