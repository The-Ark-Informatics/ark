/*
 * XMLHandler.java
 *
 * Created on 22 March 2005, 13:42
 */

package neuragenix.common;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jasig.portal.services.LogService;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author  rennypv
 */


public class XMLHandler 
{
   
    /** Creates a new instance of NGXRuntimeProperties */
    public XMLHandler() {
    }

    public Document getDocument(String strLocation,String strFilename)
    {
        Document document = null;
    	try
    	{
	    	File file = new File(strLocation, strFilename);
	    	
	    	 // create a document factory instance 
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	        // parse the comments XML file into Document 
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        document = builder.parse(file);
    	}
    	catch(ParserConfigurationException pce)
    	{
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the save action in XMLHandler - " + pce.toString(), pce);
    	}
    	catch(SAXException saxe)
    	{
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the save action in XMLHandler - " + saxe.toString(), saxe);
    	}
    	catch(Exception e)
    	{
            LogService.instance().log(LogService.ERROR, "Unknown error in getting the save action in XMLHandler - " + e.toString(), e);
    	}
        return document;
    }
    
    public Vector getNodes(String strLocation,String strFilename)
    {
    	Document doc = this.getDocument(strLocation,strFilename);
    	NodeList ndList = doc.getChildNodes();
    	return this.getNodeListAsVector(ndList);
    }
    public Hashtable getAttributesValues(Vector vtNodes,String strType)
    {
    	//returning a hashtable so that we have an idea which attribute value is for which node
    	Hashtable hshAttrValues = new Hashtable();
    	for(Enumeration eNodes = vtNodes.elements();eNodes.hasMoreElements();)
    	{
    		Node ndObj = (Node)eNodes.nextElement();
    		String attributeval = this.getAttributesValue(ndObj,strType);
    		if(attributeval != null)
    		{
    			hshAttrValues.put(ndObj,attributeval);
    		}
    	}
    	return hshAttrValues;
    }
    public String getAttributesValue(Node nd,String strAttributeName)
    {
    	NamedNodeMap nnm = nd.getAttributes();
    	if(nnm != null)
    	{
    		return nnm.getNamedItem(strAttributeName).getNodeValue();
    	}
    	return null;
    }
    public Vector getChildNodes(Node nd)
    {
    	NodeList ndLst = nd.getChildNodes();
    	if(ndLst != null)
    	{
    		return this.getNodeListAsVector(ndLst);
    	}
    	return new Vector();
    }
    public Vector getNodeListAsVector(NodeList nodelst)
    {
    	Vector vtNodes = new Vector();
    	for(int i =0;i<nodelst.getLength();i++)
    	{
    		vtNodes.add(nodelst.item(i));
    	}
    	return vtNodes;
    }
    public String toString(Node node) throws Exception 
    {
	  StringWriter w = new StringWriter(1000);
	  SAXParserFactory spFac = SAXParserFactory.newInstance();
	  SAXParser spar = spFac.newSAXParser();
	  TransformerFactory tf = TransformerFactory.newInstance();
	  Transformer t = tf.newTransformer();
	  Source source = new DOMSource(node);
	  Result result = new StreamResult(w);
	  t.transform(source, result);
	  return w.toString();
}

 /*    public Vector getNode(Node nd,String strNodeName)
    {
    	Vector vtNode = new Vector();
    	NodeList nodelst = null;
    	if(nd.hasChildNodes())
    	{
    		nodelst = this.getChildren(nd);
    		for(int i=0;i<nodelst.getLength();i++)
    		{
    			vtNode.add(this.getNode(nodelst,i));
    		}
    	}
    	else
    	{
    		vtNode.add(nd);
    	}
    	return vtNode;
    }
    public NamedNodeMap getAttibutes(Node node)
    {
    	return node.getAttributes();
    }
    public String getAttributesValue(NodeList ndList,String strAttributeName)
    {
    	Node node = this.getNode(ndList,1);
    	NamedNodeMap nnm = this.getAttibutes(node);
    	return this.getAttributesValue(nnm,strAttributeName);
    }
    public String getAttributeValue(NamedNodeMap nnmp,String strAttrName)
    {
    	return nnmp.getNamedItem(strAttrName).getNodeValue();
    }
    public NodeList getChildren(Node nd)
    {
    	return nd.getChildNodes();
    }
    /**By calling this method you can pass in a Vector of all the nodes in the order
     * of the heirarchy and get a Hashtable with Nodes as the key and a 
     * hashtable with attribute-value pair as the valuein the final element in the hierarchy for example
     * This method requires the keys in the hshNodeNames to have attributes
     * <hello_1 id="test1">
     * 	<hello_2 id = "test2">
     * 		<hello_world>Hello</hello_world>
     * 		<hello_world>World</hello_world>
     * 	</hello_2>
     * </hello_1>
     * 
     * @param hshNodeNames in the above example may have at index1 hello_1 as the key and the value to be a hashtable with
     * id as the key and test1 as the value.If it has no attribute then the value can be set to null
     * @return vector of nodes hello_world
     */
 /*   public Vector getNode(String strLocation,String strFileName,Hashtable hshNodeNames)
    {
    	Document docObj = this.getDocument(strLocation,strFileName);
    	NodeList ndLst = null;
    	Node ndObj = null;
    	Vector vtNodes = null;
    	for(Enumeration eNodes=hshNodeNames.keys();eNodes.hasMoreElements();)
    	{
    		String strNodeName= (String)eNodes.nextElement();
    		if(ndLst == null)
    		{
    			ndLst = this.getNodeList(docObj,strNodeName);
    		}
    		else if(ndObj != null)
    		{
    			ndLst = ndObj.getChildNodes();
    		}
    		Hashtable hshattibute = (Hashtable)hshNodeNames.get(strNodeName);
    		if(hshattribute != null && hshattribute.size() > 0)
    		{
    			for(Enumeration eAttributes = hshattribute.keys();eAttributes.hasMoreElements();)
    			{
    				String strattribute = (String)eAttributes.nextElement();
    				String strAttributeValue = (String)hshattibute.get(strattribute);
    				for(int i=0;i<ndLst.getLength();i++)
    				{
    					ndObj = this.getNode(ndLst,i);
    					if(this.getAttributeValue((this.getAttibutes(ndObj)),strattribute).equalsIgnoreCase(strAttributeValue))
    					{
    						break;
    					}
    				}
    			}
    		}
    	}
    	if(ndObj.hasChildNodes())
    	{
    		ndLst = ndObj.getChildNodes();
    		for(int i=0;i<ndLst.getLength();i++)
    		{
    			vtNodes.add(this.getNode(ndLst,i));
    		}
    	}
    	else
    	{
    		vtNodes.add(ndObj);
    	}
    	return vtNodes;
    }*/
    /*

                // Get a list of all the Comment Nodes
                NodeList CommentNodeList = document.getElementsByTagName("Comment");

                // Remove all child nodes from each <Comment> Node
                for (int index=0; index < CommentNodeList.getLength(); index++)
                {
                    Node currentNode = CommentNodeList.item(index);
                    // Get the Comment Nodes attributes in order to get the Comment Node's ID
                    NamedNodeMap currentNodeAttributes = currentNode.getAttributes();

                    String strCommentType = currentNodeAttributes.getNamedItem("type").getNodeValue();

                    // If the comment type is not part of the Hashtable of existing comment types
                    // add it to the Hashtable
                    if (!hashCommentTypes.containsKey(strCommentType))
                    {
                        hashCommentTypes.put(strCommentType, strCommentType);
                    }    

                    NodeList currentNodesChildren = currentNode.getChildNodes();
                    int length = currentNodesChildren.getLength();
                    int element = 0;
                    for (int index1=0; index1 < length; index1++)
                    {                                    
                        Node child = currentNodesChildren.item(index1);
                        
                        if (child.getNodeType() == Node.ELEMENT_NODE)
                        {    
                            element++;
                            // Get the report Nodes attributes in order to get the different report types
                            NamedNodeMap currentChildNodeAttributes = child.getAttributes();
                            // Get the comment ID
                            String strResultType = currentChildNodeAttributes.getNamedItem("type").getNodeValue();

                            // If the report type is not part of the Hashtable of existing report types
                            // add it to the Hashtable
                            if (!hashResultTypes.containsKey(strResultType))
                            {
                                hashResultTypes.put(strResultType, strResultType);
                            }    

                            // for the selected comment type and the result type
                            // display the result value
                            // if no result type or comment type have been selected (the first time the selected report XML has been loaded)
                            // then display the first result value in the XML file
                            if (!strSelectedCommentType.equals("") && !strSelectedResultType.equals("") )
                            {
                                if (strCommentType.equals(strSelectedCommentType) && strResultType.equals(strSelectedResultType))
                                {
                                    Node temp =  extractChild(child, "value");
                                    if (runtimeData.getParameter("UpdateComment") != null)
                                    {
                                        if ( runtimeData.getParameter("Comment") != null)
                                        {
                                            // create the child for the value tag if none exist
                                            if (temp.getFirstChild() == null)
                                            {
                                                temp.appendChild(document.createTextNode(runtimeData.getParameter("Comment")));                                                
                                            }    
                                            temp.getFirstChild().setNodeValue(runtimeData.getParameter("Comment"));
                                            saveDocumentUpdatestoXML(document, AUTO_DOC_COMMENTS_DIR + "/" + strSelectedReport);
                                        }    
                                    }
                                    
                                    if (temp.getFirstChild() != null)
                                    {    
                                        strComment = temp.getFirstChild().getNodeValue();
                                    }
                                }    
                            }    
                            else
                            {
                                if (index==FIRST_NODE && element==FIRST_ELEMENT)
                                {
                                    strSelectedCommentType = strCommentType;
                                    strSelectedResultType = strResultType;
                                    Node temp =  extractChild(child, "value");
                                    if (temp.getFirstChild() != null)
                                    {    
                                        strComment = temp.getFirstChild().getNodeValue();
                                    }
                                }    
                            }    
                            
                        }
                    }
                }


                Enumeration enum = hashCommentTypes.keys();
                while (enum.hasMoreElements())
                {
                    String strCommentType = enum.nextElement().toString();
                    if (strCommentType.equals(strSelectedCommentType))
                    {    
                        xml += "<CommentType selected='1'>" + strCommentType + "</CommentType>";
                    }    
                    else
                    {    
                        xml += "<CommentType selected='0'>" + strCommentType + "</CommentType>";
                    }    
                }    

                enum = hashResultTypes.keys();
                while (enum.hasMoreElements())
                {
                    String strResultType = enum.nextElement().toString();
                    if (strResultType.equals(strSelectedResultType))
                    {    
                        xml += "<ResultType selected='1'>" + strResultType + "</ResultType>";
                    }    
                    else
                    {    
                        xml += "<ResultType selected='0'>" + strResultType + "</ResultType>";
                    }    
                }  

                xml += "<Comment>" + strComment + "</Comment>";    */
    
    
        
}
