/**
 * GenericTree.java
 *
 * Created on November 20, 2003
 */

package neuragenix.common;


// Java libraries
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Vector;
import java.sql.ResultSet;
import org.jasig.portal.services.LogService;

// Neuragenix libraries
import neuragenix.dao.DALQuery;
import neuragenix.security.AuthToken;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.TreeSchema;


/** 
 * This class will build a generic tree
 * @author Agustian Agustian
 */

public class GenericTree
{
	public static final String ROOT = "root";
	public static final String SINGLE_DOMAIN = "singledomain"; 
	public static final String MULTI_DOMAIN = "multidomain"; 
	

    /** Constructor for the GenericTree */
    public GenericTree() 
	{
    }
	/**
	 * build the un-secure tree
	 */
    public DefaultMutableTreeNode buildTree(Hashtable hashExpanded,
			String strCurrentNode, String strTreeType, String strChannelName)
    {
        DefaultMutableTreeNode genericTree =
				new DefaultMutableTreeNode(GenericTree.ROOT);

        try
        {
			int intIndex0 = 0;
			int intIndex1 = 0;
			boolean blAdded = false;
			String strParentDomainName = TreeSchema.getParentDomainName(strChannelName);
			DALQuery query = new DALQuery();

            // unlock all records
			//if (lockRequest != null && lockRequest.isValid())
			//{
			//	lockRequest.unlock();
			//}
            
            // create new lock request
            //lockRequest = new LockRequest(authToken);
           
		    // local variable used for parent child tracking base to the 
			// definition define in the XML file
			Hashtable hashTreeStructure =
					TreeSchema.getTreeStructures(strTreeType);
			String strPreviousLocalNode = "None";
			String strCurrentLocalNode = "Root"; // set as the inital node 
			int intTreeSize = 0;

			if(strTreeType.equals(GenericTree.SINGLE_DOMAIN))
			{
		  
				intTreeSize = hashTreeStructure.size();
			}
			else
			{

				// minus 1 the size since we want to take out the ROOT
				// in multi domain tree
				intTreeSize = hashTreeStructure.size()-1;
			
			}
			// recursively construct the tree
			for(intIndex0=0; intIndex0 < intTreeSize; intIndex0++)
			{
//System.err.println("intIndex0 = " + intIndex0 + "<<<<<-----------------------");
//System.err.println("before: strCurrentLocalNode = " + strCurrentLocalNode + "<<<<<----------------------");
				// set the child node as the current node

				strCurrentLocalNode =
						getChildNode(strCurrentLocalNode, hashTreeStructure);
//System.err.println("after: strCurrentLocalNode = " + strCurrentLocalNode + "<<<<<----------------------");

				// do the query
            	query.reset();
				query.setDomain(strCurrentLocalNode.toUpperCase(), null,
								null, null);
				Vector vtFormFields = DatabaseSchema.getFormFields(
						"list_" + strParentDomainName.toLowerCase() + "_" + 
						strCurrentLocalNode.toLowerCase());

				query.setFields(vtFormFields, null);


				//agus
				if(strTreeType.equals(GenericTree.SINGLE_DOMAIN))
				{
					if(intIndex0 == 0)
					{
						query.setWhere(null, 0,
							TreeSchema.getParentKey(strCurrentLocalNode),
							"=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
					}
					else
					{
						query.setWhere(null, 0,
							TreeSchema.getParentKey(strCurrentLocalNode),
							"<>", "-1", 0, DALQuery.WHERE_HAS_VALUE);
					}
				}
				//agus

				if(strTreeType.equals(GenericTree.SINGLE_DOMAIN))
				{
					query.setWhere("AND", 0, strCurrentLocalNode.toUpperCase() +
						"_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
				}
				else
				{
					query.setWhere(null, 0, strCurrentLocalNode.toUpperCase() +
						"_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
				}

				query.setOrderBy(TreeSchema.getPrimaryKey(strCurrentLocalNode), "ASC");
				ResultSet rsResultSet = query.executeSelect();
				Vector vtResult = QueryChannel.lookupRecord(
											rsResultSet, vtFormFields);
//System.err.println("vtResult.size() = " + vtResult.size() + "<<<--------------------");
//for(int intX=0; intX < vtResult.size(); intX++)
//{
//	System.err.println("vtResult at index " + intX + " = " + vtResult.get(intX) + "<<<------------");
//}

				// for the very first level node only
				if(strPreviousLocalNode.equals("None"))
				{
					for (intIndex1=0; intIndex1 < vtResult.size(); intIndex1++)
					{
//System.err.println("about to add " + vtResult.get(intIndex1) + " to " + genericTree.getUserObject() + "<<<<<<<<-------------------");
						genericTree.add(
							new DefaultMutableTreeNode(vtResult.get(intIndex1)));
					
						// get the current local node ID
						//String intNodeID = ((Hashtable) vtResult.get(
						//					intIndex1)).get(
						//					strCurrentLocalNode.toUpperCase() +
						//					"_int" + strCurrentLocalNode +
						//					"Key").toString();
				
						// current node is the location where the user want to
						// view the data if the current node and current local
						// node are equals do read_write
						// else do read_only locking
						//if (strCurrentNode.equals(
						//	strCurrentLocalNode.toUpperCase() + "_" +
						//	intNodeID))
						//{
//System.err.println("locking " + strCurrentLocalNode.toUpperCase() + "_" + intNodeID + " with READ_WRITE lock");
						//	lockRequest.addLock(
						//		strCurrentLocalNode.toUpperCase(), intNodeID,
						//		LockRecord.READ_WRITE);
						//}
						//else
						//{
//System.err.println("locking " + strCurrentLocalNode.toUpperCase() + "_" + intNodeID + " with READ_ONLY lock");
						//	lockRequest.addLock(
						//		strCurrentLocalNode.toUpperCase(), intNodeID,
						//		LockRecord.READ_ONLY);
						//}
						
					}
				}
				else
				{
					for (intIndex1=0; intIndex1 < vtResult.size(); intIndex1++)
					{

						Hashtable hashNewNode =
								(Hashtable) vtResult.get(intIndex1);
						Enumeration tree_enum =
								genericTree.breadthFirstEnumeration();
						tree_enum.nextElement();// skip the root
						
						while (tree_enum.hasMoreElements())
						{
							DefaultMutableTreeNode currentNode =
									(DefaultMutableTreeNode) tree_enum.nextElement();
							Hashtable hashCurrentObject =
									(Hashtable) currentNode.getUserObject();

							// current node is the parent of a tree node and
							// strCurrentNode is where the user is currently
							// viewing check whether the hashCurrentObject 
							// is the direct parent of the current local node
                                                        String strPDomainName = getParentNode(strCurrentLocalNode, hashTreeStructure);
							if (strPDomainName != null && hashCurrentObject.get(TreeSchema.getPrimaryKey(strPDomainName)) != null)
							{
								// checking whether if the 
								// hashCurrentObject(parent) equals to
								// hashNewNode(child)
								if (hashCurrentObject.get(
                                                                    TreeSchema.getPrimaryKey(strPDomainName)).toString().equals(
                                                                    hashNewNode.get(TreeSchema.getParentKey(strCurrentLocalNode)).toString()))
								{
//System.err.println("about to add " + hashNewNode + " to " + currentNode.getUserObject() + "<<<<<<<<-------------------");
									currentNode.add(new DefaultMutableTreeNode(
													hashNewNode));

									//agus
									// indicates that the node is successfully added
									blAdded = true; 
									//agus
/*								    
									// check whether the parent local node of
									// the current local node is expanded	
									if (hashExpanded.containsKey(getParentNode(
										strCurrentLocalNode,
										hashTreeStructure).toUpperCase() + "_" +
										hashNewNode.get(
										strCurrentLocalNode.toUpperCase() +
										"_intParentKey").toString()))
									{
										// if the current node equals to
										// hashNewNode which is the
										// current local node then do read_write
										// else only do read_only locking
										if (strCurrentNode.equals(
											strCurrentLocalNode.toUpperCase()
											+ "_" + hashNewNode.get(
											strCurrentLocalNode.toUpperCase()
											+ "_int" + strCurrentLocalNode +
											"Key").toString()))
										{
//System.err.println("locking " + strCurrentNode + " with READ_WRITE lock");
											lockRequest.addLock(
												strCurrentLocalNode.toUpperCase(),
												hashNewNode.get(
												strCurrentLocalNode.toUpperCase() +
												"_int" + strCurrentLocalNode +
												"Key").toString(),
												LockRecord.READ_WRITE);
										}
										else
										{
//System.err.println("locking " + strCurrentLocalNode.toUpperCase() + "_" + hashNewNode.get(strCurrentLocalNode.toUpperCase() + "_int" + strCurrentLocalNode + "Key").toString() + " with READ_ONLY lock");
											lockRequest.addLock(
												strCurrentLocalNode.toUpperCase(),
												hashNewNode.get(
												strCurrentLocalNode.toUpperCase() +
												"_int" + strCurrentLocalNode +
												"Key").toString(),
												LockRecord.READ_ONLY);
										}
									} // if
*/
									
									break;
								} // if
							} // if
						} // while loop
						//agus
						if(blAdded)
						{
							// reset the blAdded
							blAdded=false;
						}
						else
						{
							// if the new node didn't get added this time
							// re-add it to the back of the vector so that
							// it will be added the next time around
							vtResult.add(hashNewNode);
						}
						//agus
					} // for loop
				} // else

				strPreviousLocalNode = strCurrentLocalNode;
			} // for loop

            //lockRequest.lockDelayWrite();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
				"Unknown error in GenericTree:buildTree - " + e.toString(), e);
        }
        
        return genericTree;
    }


	/**
	 * build the secure tree
	 */
    public DefaultMutableTreeNode buildTree(Hashtable hashExpanded,
			String strCurrentNode, String strTreeType,
			LockRequest  lockRequest, AuthToken authToken,
			DALSecurityQuery  query, String strChannelName)
    {
        DefaultMutableTreeNode genericTree =
				new DefaultMutableTreeNode(GenericTree.ROOT);

        try
        {
			int intIndex0 = 0;
			int intIndex1 = 0;
			boolean blAdded = false;
			String strParentDomainName = TreeSchema.getParentDomainName(strChannelName);

            // unlock all records
			/*if (lockRequest != null && lockRequest.isValid())
			{
				LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
			}*/
            
            // create new lock request
            //lockRequest = new LockRequest(authToken);
           
		    // local variable used for parent child tracking base to the 
			// definition define in the XML file
			Hashtable hashTreeStructure =
					TreeSchema.getTreeStructures(strTreeType);
			String strPreviousLocalNode = "None";
			String strCurrentLocalNode = "Root"; // set as the inital node 
		
			int intTreeSize = 0;
//System.err.println("hashTreeStructure = " + hashTreeStructure + "<<<<<<---------------");
			if(strTreeType.equals(GenericTree.SINGLE_DOMAIN))
			{
				intTreeSize = hashTreeStructure.size();
			}
			else
			{
				// minus 1 the size since we want to take out the ROOT
				// in multi domain tree
				intTreeSize = hashTreeStructure.size()-1;
			}

//System.err.println("intTreeSize = " + intTreeSize + "<<<<<<<<<--------------");
			// recursively construct the tree
			for(intIndex0=0; intIndex0 < intTreeSize; intIndex0++)
			{
//System.err.println("intIndex0 = " + intIndex0 + "<<<<<-----------------------");
//System.err.println("before: strCurrentLocalNode = " + strCurrentLocalNode + "<<<<<----------------------");
				// set the child node as the current node
				strCurrentLocalNode =
						getChildNode(strCurrentLocalNode, hashTreeStructure);
//System.err.println("after: strCurrentLocalNode = " + strCurrentLocalNode + "<<<<<----------------------");
//System.err.println("after: strCurrentLocalNode.toUpperCase() = " + strCurrentLocalNode.toUpperCase() + "<<<<<----------------------");

				// do the query
            	query.reset();
				query.setDomain(strCurrentLocalNode.toUpperCase(), null,
								null, null);
				Vector vtFormFields = DatabaseSchema.getFormFields(
						"list_" + strParentDomainName.toLowerCase() + "_" + 
						strCurrentLocalNode.toLowerCase());
                                
				query.setFields(vtFormFields, null);
				//agus
				if(strTreeType.equals(GenericTree.SINGLE_DOMAIN))
				{
					if(intIndex0 == 0)
					{
						query.setWhere(null, 0, TreeSchema.getParentKey(strCurrentLocalNode),
							"=", "-1", 0, DALQuery.WHERE_HAS_VALUE);
					}
					else
					{
						query.setWhere(null, 0,	TreeSchema.getParentKey(strCurrentLocalNode),
							"<>", "-1", 0, DALQuery.WHERE_HAS_VALUE);
					}
				}
				//agus
				if(strTreeType.equals(GenericTree.SINGLE_DOMAIN))
				{
					query.setWhere("AND", 0, strCurrentLocalNode.toUpperCase() +
						"_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
				}
				else
				{
					query.setWhere(null, 0, strCurrentLocalNode.toUpperCase() +
						"_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
				}

				query.setOrderBy(TreeSchema.getPrimaryKey(strCurrentLocalNode), "ASC");
				ResultSet rsResultSet = query.executeSelect();
				Vector vtResult = QueryChannel.lookupRecord(
											rsResultSet, vtFormFields);
//System.err.println("vtResult.size() = " + vtResult.size() + "<<<--------------------");
//for(int intX=0; intX < vtResult.size(); intX++)
//{
//	System.err.println("vtResult at index " + intX + " = " + vtResult.get(intX) + "<<<------------");
//}

				// for the very first level node only
				if(strPreviousLocalNode.equals("None"))
				{
					for (intIndex1=0; intIndex1 < vtResult.size(); intIndex1++)
					{
//System.err.println("about to add " + vtResult.get(intIndex1) + " to " + genericTree.getUserObject() + "<<<<<<<<-------------------");
						genericTree.add(
							new DefaultMutableTreeNode(vtResult.get(intIndex1)));
					
						// get the current local node ID
						String intNodeID = ((Hashtable) vtResult.get(intIndex1)).get(TreeSchema.getPrimaryKey(strCurrentLocalNode)).toString();
				
						// current node is the location where the user want to
						// view the data if the current node and current local
						// node are equals do read_write
						// else do read_only locking
						if (strCurrentNode.equals(
							strCurrentLocalNode.toUpperCase() + "_" +
							intNodeID))
						{
//System.err.println("locking " + strCurrentLocalNode.toUpperCase() + "_" + intNodeID + " with READ_WRITE lock");
							lockRequest.addLock(
								strCurrentLocalNode.toUpperCase(), intNodeID,
								LockRecord.READ_WRITE);
                                                        
						}
						else
						{
//System.err.println("locking " + strCurrentLocalNode.toUpperCase() + "_" + intNodeID + " with READ_ONLY lock");
							lockRequest.addLock(
								strCurrentLocalNode.toUpperCase(), intNodeID,
								LockRecord.READ_ONLY);
						}
						
					}
				}
				else
				{
					for (intIndex1=0; intIndex1 < vtResult.size(); intIndex1++)
					{

						Hashtable hashNewNode =
								(Hashtable) vtResult.get(intIndex1);
						Enumeration tree_enum =
								genericTree.breadthFirstEnumeration();
						tree_enum.nextElement();// skip the root
						
						while (tree_enum.hasMoreElements())
						{
							DefaultMutableTreeNode currentNode =
									(DefaultMutableTreeNode) tree_enum.nextElement();
							Hashtable hashCurrentObject =
									(Hashtable) currentNode.getUserObject();

							// current node is the parent of a tree node and
							// strCurrentNode is where the user is currently
							// viewing check whether the hashCurrentObject 
							// is the direct parent of the current local node
                                                        String strPDomainName = getParentNode(strCurrentLocalNode, hashTreeStructure);
							if (strPDomainName != null && hashCurrentObject.get(TreeSchema.getPrimaryKey(strPDomainName)) != null)
							{
								// checking whether if the 
								// hashCurrentObject(parent) equals to
								// hashNewNode(child)
								if (hashCurrentObject.get(
                                                                    TreeSchema.getPrimaryKey(strPDomainName)).toString().equals(
                                                                    hashNewNode.get(TreeSchema.getParentKey(strCurrentLocalNode)).toString()))
								{
//System.err.println("about to add " + hashNewNode + " to " + currentNode.getUserObject() + "<<<<<<<<-------------------");
									currentNode.add(new DefaultMutableTreeNode(
													hashNewNode));

									//agus
									// indicates that the node is successfully added
									blAdded = true; 
									//agus
								    
									// check whether the parent local node of
									// the current local node is expanded	
									if (hashExpanded.containsKey(strPDomainName.toUpperCase() + "_" +
										hashNewNode.get(TreeSchema.getParentKey(strCurrentLocalNode)).toString()))
									{
										// if the current node equals to
										// hashNewNode which is the
										// current local node then do read_write
										// else only do read_only locking
										if (strCurrentNode.equals(
											strCurrentLocalNode.toUpperCase()
											+ "_" + hashNewNode.get(
											TreeSchema.getPrimaryKey(strCurrentLocalNode)).toString()))
										{
//System.err.println("locking " + strCurrentNode + " with READ_WRITE lock");
											lockRequest.addLock(
												strCurrentLocalNode.toUpperCase(),
												hashNewNode.get(
												TreeSchema.getPrimaryKey(strCurrentLocalNode)).toString(),
												LockRecord.READ_WRITE);
										}
										else
										{
//System.err.println("locking " + strCurrentLocalNode.toUpperCase() + "_" + hashNewNode.get(strCurrentLocalNode.toUpperCase() + "_int" + strCurrentLocalNode + "Key").toString() + " with READ_ONLY lock");
											lockRequest.addLock(
												strCurrentLocalNode.toUpperCase(),
												hashNewNode.get(
												TreeSchema.getPrimaryKey(strCurrentLocalNode)).toString(),
												LockRecord.READ_ONLY);
										}
									} // if
                                                                        else
                                                                        {
                                                                                if (strCurrentNode.equals(
											strCurrentLocalNode.toUpperCase()
											+ "_" + hashNewNode.get(
											TreeSchema.getPrimaryKey(strCurrentLocalNode)).toString()))
										{
//System.err.println("locking " + strCurrentNode + " with READ_WRITE lock");
											lockRequest.addLock(
												strCurrentLocalNode.toUpperCase(),
												hashNewNode.get(
												TreeSchema.getPrimaryKey(strCurrentLocalNode)).toString(),
												LockRecord.READ_WRITE);
										}
                                                                        }
									break;
								} // if
							} // if
						} // while loop
						//agus
						if(blAdded)
						{
							// reset the blAdded
							blAdded=false;
						}
						else
						{
							// if the new node didn't get added this time
							// re-add it to the back of the vector so that
							// it will be added the next time around
							vtResult.add(hashNewNode);
						}
						//agus
					} // for loop
				} // else

				strPreviousLocalNode = strCurrentLocalNode;
			} // for loop

            lockRequest.lockDelayWrite();
  //          lockRequest.printout();
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
				"Unknown error in GenericTree:buildTree - " + e.toString(), e);
        }
        
        return genericTree;
    }


	/**
	 * get the child node
	 */
	private String getChildNode(String strCurrentNode,
										Hashtable hashTreeStructure)
	{
		return (String) hashTreeStructure.get(strCurrentNode);
	}


	/**
	 * get the parent node
	 */
	private String getParentNode(String strCurrentNode,
										Hashtable hashTreeStructure)
	{
		Enumeration enumTreeStructureKey = hashTreeStructure.keys();
		String strTreeStructureKey = null;
		while(enumTreeStructureKey.hasMoreElements())
		{
			strTreeStructureKey = (String) enumTreeStructureKey.nextElement();
			if(hashTreeStructure.get(strTreeStructureKey).equals(
															strCurrentNode))
			{
				return strTreeStructureKey; 
			}
		}

		//return strTreeStructureKey;
                return null;
    }

    /**
	 * get the XML
	 */
    public String toXML(DefaultMutableTreeNode root, String strTreeType,
						String strURL, Hashtable  hashExpanded,
						Vector vtFormFieldMappingName)
    {
		StringBuffer sbfResult = new StringBuffer();
        
        if (!root.isLeaf())
        {
			String strInitialNode = "Root";
			Enumeration tree_enum = root.breadthFirstEnumeration();
			tree_enum.nextElement();// skip the root
			Hashtable hashTreeStructure =
					TreeSchema.getTreeStructures(strTreeType);
//System.err.println("strFormFieldMappingName = " + strFormFieldMappingName + "<<<<<------------");
			Hashtable hashFormFieldMapping = new Hashtable();
			for(int intIndex=0; intIndex < vtFormFieldMappingName.size(); intIndex++)
			{
				hashFormFieldMapping.putAll(TreeSchema.getFormFieldMapping(
				  (String) vtFormFieldMappingName.get(intIndex)));
			}
//System.err.println("hashFormFieldMapping = " + hashFormFieldMapping.toString() + "<<<<<------------");

			String strConcatenatedURL = "";
			
		    sbfResult.append("<tree>");
			for(int intIndex0=1; tree_enum.hasMoreElements(); intIndex0++)
			{
				DefaultMutableTreeNode currentNode =
						(DefaultMutableTreeNode) tree_enum.nextElement();
				int intNodeLevel = currentNode.getLevel();
				
				String strCurrentDomain = 
						getDomain(intNodeLevel, hashTreeStructure);
				// parent always 1 level above the current domain level
				String strParentDomain =
						getDomain(intNodeLevel-1, hashTreeStructure);
        		Hashtable hashFormFields =
						(Hashtable) currentNode.getUserObject();
				strConcatenatedURL =
						constructURL(hashFormFields, strURL, strCurrentDomain,
										hashFormFieldMapping, intNodeLevel);

				//Constructing the XML
				sbfResult.append("<searchResult>");
				sbfResult.append("<treeId>");
				sbfResult.append(intIndex0);
				sbfResult.append("</treeId>");

				sbfResult.append("<url>");
				sbfResult.append(strConcatenatedURL);
				sbfResult.append("</url>");

				sbfResult.append("<currentDomain>");
				sbfResult.append(strCurrentDomain);
				sbfResult.append("</currentDomain>");

				sbfResult.append("<parentDomain>");
				sbfResult.append(strParentDomain);
				sbfResult.append("</parentDomain>");

				sbfResult.append("<expanded>");
				sbfResult.append(hashExpanded.containsKey(
								strCurrentDomain.toUpperCase() + "_" +
								hashFormFields.get(TreeSchema.getPrimaryKey(strCurrentDomain))));
				sbfResult.append("</expanded>");

			
				Enumeration enumFormFieldKeys = hashFormFields.keys();
				String strFormFieldKey = null;
				while(enumFormFieldKeys.hasMoreElements())
				{
					strFormFieldKey = (String) enumFormFieldKeys.nextElement();
//System.err.println("strFormFieldKey = " + strFormFieldKey + "<<<------------------");

					if(hashFormFieldMapping.containsKey(strFormFieldKey))
					{
						if(!hashFormFieldMapping.get(
							strFormFieldKey).equals("url"))		
						{
							sbfResult.append("<" + hashFormFieldMapping.get(
								strFormFieldKey) + ">");
							sbfResult.append(hashFormFields.get(
								strFormFieldKey));
							sbfResult.append("</" + hashFormFieldMapping.get(
								strFormFieldKey) + ">");
						}
					}
				}

				sbfResult.append("</searchResult>");
			}
			sbfResult.append("</tree>");
        }
        
        return sbfResult.toString();
    }

	/**
	 * get the Domain name at n level below the Root
	 */
	private String getDomain(int intLevel, Hashtable hashTreeStructure)
	{
		String strCurrentNode = "Root";

		for(int intIndex=0; intIndex < intLevel; intIndex++)
		{
	   		strCurrentNode = (String) hashTreeStructure.get(strCurrentNode);
		}

		return strCurrentNode;
	}

	/**
	 * construct the URL (link on the node)
	 */
	private String constructURL(Hashtable hashNodeInfo, String strURL,
								String strCurrentDomain,
								Hashtable hashFormFieldMapping,
								int intNodeLevel)
	{
		boolean blFirstIteration = true;
		String strAmp = "&amp;";
		String strQMark = "?";
		StringBuffer sbfURL = new StringBuffer();
		StringBuffer sbfTempURL = new StringBuffer();

		Enumeration enumFormFieldKeys = hashNodeInfo.keys();
		String strFormFieldKey = null;
		while(enumFormFieldKeys.hasMoreElements())
		{
			strFormFieldKey = (String) enumFormFieldKeys.nextElement();
//System.err.println("strFormFieldKey = " + strFormFieldKey + "<<<------------------");

			if(hashFormFieldMapping.containsKey(strFormFieldKey))
			{
				if(hashFormFieldMapping.get(
						strFormFieldKey).equals("internalId") ||
					hashFormFieldMapping.get(
						strFormFieldKey).equals("nodeName") ||
					hashFormFieldMapping.get(
						strFormFieldKey).equals("parentId"))
				{
					if(!blFirstIteration)
					{
						sbfTempURL.append(strAmp);
					}
					blFirstIteration = false;
					sbfTempURL.append(hashFormFieldMapping.get(
							strFormFieldKey));
					sbfTempURL.append("=");
					sbfTempURL.append(hashNodeInfo.get(strFormFieldKey));
					sbfTempURL.append(strAmp); 
					sbfTempURL.append(strFormFieldKey);
					sbfTempURL.append("=");
					sbfTempURL.append(hashNodeInfo.get(strFormFieldKey));
				}
				else if(hashFormFieldMapping.get(strFormFieldKey).equals("url"))
				{
					if(!blFirstIteration)
					{
						sbfTempURL.append(strAmp);
					}
					blFirstIteration = false;
					sbfTempURL.append(strFormFieldKey);
					sbfTempURL.append("=");
					sbfTempURL.append(hashNodeInfo.get(strFormFieldKey));
				}
			}
		}
		
		if(strURL.indexOf("?") > -1)
		{
			sbfURL.append(strURL);
			sbfURL.append(strAmp);
			sbfURL.append("domain=");
			sbfURL.append(strCurrentDomain);
			sbfURL.append(strAmp);
			sbfURL.append("level=");
			sbfURL.append(intNodeLevel);
			sbfURL.append(strAmp);
			sbfURL.append(sbfTempURL);
		
		}
		else
		{
			sbfURL.append(strURL);
			sbfURL.append(strAmp);
			sbfURL.append("domain=");
			sbfURL.append(strCurrentDomain);
			sbfURL.append(strAmp);
			sbfURL.append("level=");
			sbfURL.append(intNodeLevel);
			sbfURL.append(strAmp);
			sbfURL.append(sbfTempURL);
		}

		return sbfURL.toString();
	}
}
