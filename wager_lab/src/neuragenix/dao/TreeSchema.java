/**
 * TreeSchema.java
 * Copyright © 2003 Neuragenix Pty Ltd.  All rights reserved.
 * Date: 20/11/2003
 */

package neuragenix.dao;

/**
 * Class to specify the TreeSchema
 * @author <a href="mailto:aagustian@neuragenix.com">Agustian Agustian</a>
 */

// java system libraries
import java.util.Hashtable;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;


public class TreeSchema
{
    /**
	 * tree structures
     */
    private static Hashtable hashTreeStructures = new Hashtable();
    
    /**
	 * tree primary keys
     */
    private static Hashtable hashTreePrimaryKeys = new Hashtable();
    
    /**
	 * tree parent keys
     */
    private static Hashtable hashTreeParentKeys = new Hashtable();

    /**
	 * parent domain name
     */
    private static Hashtable hashParentDomainNames = new Hashtable();

    /**
	 * fields mapping
     */
    private static Hashtable hashFormFieldMapping = new Hashtable();

    /**
	 * Indicate if the files was loaded
     */
    private static Hashtable hashLoaded = new Hashtable(10);

	/** 
	 * TreeSchema constructor
	 */
	public TreeSchema()
	{
	}

    /**
	 * Return a hashtable that represent the tree structure
     */
    public static Hashtable getTreeStructures(String strTreeStructureType)
    {
        return (Hashtable) hashTreeStructures.get(strTreeStructureType);
    }

    /**
	 * Return a the parent domain name
     */
    public static String getParentDomainName(String strChannelName)
    {
        return (String) hashParentDomainNames.get(strChannelName);
    }

    /**
	 * Return a hashtable that represent the tree structure
     */
    public static Hashtable getFormFieldMapping(String strFormFieldMappingName)
    {
        return (Hashtable) hashFormFieldMapping.get(strFormFieldMappingName);
    }
    
    /**
	 * Return primary key of a domain
     */
    public static String getPrimaryKey(String strDomainName)
    {
        return (String) hashTreePrimaryKeys.get(strDomainName);
    }
    
    /**
	 * Return parent key of a domain
     */
    public static String getParentKey(String strDomainName)
    {
        return (String) hashTreeParentKeys.get(strDomainName);
    }

    /** 
	 * Load the tree schema
     */
    public static void loadDomains(InputStream file, String strFileName)
    {
        if (!hashLoaded.containsKey(strFileName))
        {
            // create a document factory instance
            DocumentBuilderFactory factory =
					DocumentBuilderFactory.newInstance();
            Document document;

            try
            {
                // parse the XML file
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(file);

				NodeList treestructureNodes =
						document.getElementsByTagName("treestructure");
                for(int intIndex0=0;
					intIndex0 < treestructureNodes.getLength(); intIndex0++)
                {
                    Node currentTreeStructure =
							treestructureNodes.item(intIndex0);
                    NamedNodeMap treestructureAttributes =
							currentTreeStructure.getAttributes();
                    String treestructureName =
							treestructureAttributes.getNamedItem(
							"type").getNodeValue();
                    NodeList levelNodes = currentTreeStructure.getChildNodes();

					Hashtable hashLevels = new Hashtable();
                    for(int intIndex1=0; intIndex1 < levelNodes.getLength();
						intIndex1++)
                    {
                        Node currentLevel = levelNodes.item(intIndex1);
                        if (currentLevel.getNodeType() == Node.ELEMENT_NODE)
                        {
                            NamedNodeMap levelAttributes =
								currentLevel.getAttributes();
							hashLevels.put(levelAttributes.getNamedItem(
								"name").getNodeValue(),
								levelAttributes.getNamedItem(
								"child").getNodeValue());
                            hashTreePrimaryKeys.put(levelAttributes.getNamedItem(
								"name").getNodeValue(), levelAttributes.getNamedItem(
								"key").getNodeValue());
                            hashTreeParentKeys.put(levelAttributes.getNamedItem(
								"name").getNodeValue(), levelAttributes.getNamedItem(
								"parentkey").getNodeValue());
                        }
                    }
                    hashTreeStructures.put(treestructureName, hashLevels);
                }

				NodeList parentDomainNodes =
						document.getElementsByTagName("parentdomain");
				for(int intIndex=0; intIndex < treestructureNodes.getLength();
					intIndex++)
				{
					Node onlyParentDomainNode =
							parentDomainNodes.item(intIndex);
					NamedNodeMap parentDomainAttributes =
							onlyParentDomainNode.getAttributes();
					hashParentDomainNames.put(parentDomainAttributes.getNamedItem(
							"channelname").getNodeValue(), parentDomainAttributes.getNamedItem(
							"name").getNodeValue());
				}

				NodeList formFieldMappingNodes =
						document.getElementsByTagName("formfieldmapping");
                for(int intIndex0=0;
					intIndex0 < formFieldMappingNodes.getLength(); intIndex0++)
                {
                    Node formFieldMappingNode =
							formFieldMappingNodes.item(intIndex0);
                    NamedNodeMap formFieldMappingAttributes =
							formFieldMappingNode.getAttributes();
                    String formFieldMappingName =
							formFieldMappingAttributes.getNamedItem(
							"name").getNodeValue();
                    NodeList mapNodes = formFieldMappingNode.getChildNodes();

					Hashtable hashMaps = new Hashtable();
                    for(int intIndex1=0; intIndex1 < mapNodes.getLength();
						intIndex1++)
                    {
                        Node currentMap = mapNodes.item(intIndex1);
                        if (currentMap.getNodeType() == Node.ELEMENT_NODE)
                        {
                            NamedNodeMap mapAttributes =
								currentMap.getAttributes();
							hashMaps.put(mapAttributes.getNamedItem(
								"name").getNodeValue(),
								mapAttributes.getNamedItem(
								"mapped").getNodeValue());
                        }
                    }
                    hashFormFieldMapping.put(formFieldMappingName, hashMaps);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            hashLoaded.put(strFileName, "loaded");
        }
    }
}
