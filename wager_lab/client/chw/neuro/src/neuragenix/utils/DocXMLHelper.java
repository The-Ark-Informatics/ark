package neuragenix.utils;
/*
 * DocXMLHelper.java
 *
 * Created on November 17, 2005, 5:26 AM
 * to be used by DocManipulation for parsing table tag in tamplates
 */

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;


/**
 *
 * @author  akattan
 */
public class DocXMLHelper {
    
    // for general purpose xml processing
    private org.w3c.dom.Document cachedXMLDoc = null;
    private boolean success = true;
    
    
    public String[] getTableColumnsRef(){
        if (!isSuccess()) return null;
        Vector col_list = new Vector();
        Node tableNode = extractNode("table");
        Node [] columns = getChildNodes(tableNode, "column");
        for (int i=0; i< columns.length; i++) {
            String ref= getAttribute(columns[i],"ref");
            if (ref !=null)
                col_list.add(ref);
            else
                col_list.add("column_ref_not_found");//doubt this is going to be used
        }
        Object [] objs= col_list.toArray();
        String [] result = new String[objs.length];
        for (int j=0; j < objs.length; j++){
            //System.out.println("ref: " + objs[j]);
            result[j] = objs[j].toString();
        }
        return result;
    }
    
    public short[] getTableColumnsWidths(){
        if (!isSuccess()) return null;
        Vector col_list = new Vector();
        Node tableNode = extractNode("table");
        Node [] columns = getChildNodes(tableNode, "column");
        for (int i=0; i< columns.length; i++) {
            String width= getAttribute(columns[i],"width");
            if (width !=null)
                col_list.add(width);
            else
                col_list.add("-1");//doubt this is going to be used
        }
        Object [] objs= col_list.toArray();
        short [] result = new short[objs.length];
        for (int j=0; j < objs.length; j++){
            //System.out.println("ref: " + objs[j]);
            try{
            result[j] = (short)Integer.parseInt((String)objs[j].toString());
            }catch(Exception e){
                System.out.println("Invalid column witdh getTableColumnsWidths()");
                result[j]=-2;
            }
        }
        return result;
    }
    
    public String[] getTableColumnsHeader(){
        if (!isSuccess()) return null;
        Vector col_list = new Vector();
        Node tableNode = extractNode("table");
        Node [] columns = getChildNodes(tableNode, "column");
        for (int i=0; i< columns.length; i++) {
            String header= getAttribute(columns[i],"header");
            if (header !=null)
                col_list.add(header);
            else
                col_list.add("column_header_not_found");
        }
        Object [] objs= col_list.toArray();
        String [] result = new String[objs.length];
        for (int j=0; j < objs.length; j++){
            //System.out.println("header: " + objs[j]);
            result[j] = objs[j].toString();
        }
        return result;
    }
    
    public String getTableAttribute(String attribute){
        if (!isSuccess()) return null;
        Vector col_list = new Vector();
        Node tableNode = extractNode("table");
        return getAttribute(tableNode, attribute);
    }
    /** Creates a new instance of DocXMLHelper */
    public DocXMLHelper() {
    }
    
    //==============================================================================
    /**
     * Read the xml file and cache it as DOM document
     * @param filename String
     * @return boolean
     */
    public boolean cacheXMLFile(String filename) {
        File file = new File(filename);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            cachedXMLDoc = db.parse(file);
            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }
    
    
    //==============================================================================
    /**
     * For general pourpose XML processing. cache the xml string and then get values of it.
     * @param xml String
     * @return boolean
     */
    public boolean cacheXMLString(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            byte[] buff = xml.getBytes("US-ASCII");
            InputStream is = new ByteArrayInputStream(buff);
            cachedXMLDoc = db.parse(is);
            success = true;
        } catch (Exception ex) {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }
    
    
    //==============================================================================
    /**
     * Checkes wheter loadind and parsing the xml configurations files was successful
     * @return boolean
     */
    public boolean isSuccess() {
        return success;
    }
    
    
    //==============================================================================
    
    
    
    //==============================================================================
    //==============================================================================
    //============= PRIVATE METHODS - HELPING METHODS ==============================
    //==============================================================================
    /**
     * Extract Node from a node denoted by the uri from the cached xml - not reliable. it just uses string matching
     * @param uri String
     * @return Node
     */
    private Node extractNode(String uri) {
        if (cachedXMLDoc == null) {
            return null;
        }
        return extractNode(cachedXMLDoc, uri);
    }
    
    
    /**
     * Extract text from a node denoted by the uri from the cached xml - not reliable. it just uses string matching
     * @param xml String
     * @param uri String
     * @return String
     */
    private String extractText(String uri) {
        if (cachedXMLDoc == null) {
            return null;
        }
        return extractText(cachedXMLDoc, uri);
    }
    
    
    //==============================================================================
    //==============================================================================
    //==============================================================================
    //==============================================================================
    //================ UTIL METHODS - GENERAL static XML PROCESSING ================
    //==============================================================================
    //==============================================================================
    //==============================================================================
    //==============================================================================
    /**
     * similar to {@link private static Node extractNode(Node node, String uri)} but return a text leafe node contents
     * @param node Node
     * @param uri String
     * @return String
     */
    private static String extractText(Node node, String uri) {
        Node temp = extractNode(node, uri);
        if (temp != null) {
            temp = temp.getFirstChild();
            if ( (temp != null) && (temp.getNodeType() == temp.TEXT_NODE))
                return temp.getNodeValue();
            else
                return null;
        } else {
            return null;
        }
    }
    
    
    //==============================================================================
    
    /**
     * Traverse the uri and extract a node in the subtree - calls extractChild until it gets to the node.
     * @param node Node
     * @param uri String eg. connection/mmscurl
     * @return Node if node not found in the subtree of node it returns null
     */
    private static Node extractNode(Node node, String uri) {
        StringTokenizer st = new StringTokenizer(uri, "/");
        Node child = node;
        while (st.hasMoreElements()) {
            String childTag = (String) st.nextElement();
            //System.out.println("Child: " + childTag);
            child = extractChild(child, childTag);
        }
        if (child != null) {
            return child;
        } else {
            return null;
        }
    }
    
    
    //==============================================================================
    
    /**
     * Look for a child node with the name tag only one level down
     * @param node Node
     * @param tag String
     * @return Node
     */
    private static Node extractChild(Node node, String tag) {
        if (node == null) {
            return null;
        }
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (tag.equalsIgnoreCase(nl.item(i).getNodeName())) {
                return nl.item(i);
            }
        }
        return null;
    }
    
    
    /**
     * Return array of chile Node(s) that are actual elements and not not comments or other types.
     * Optionally, if tag is not null, will only return child nodes that have the same name as the tag
     * formaly include child node if child.getNodeName().equalsIgnoreCase(tag) is true
     * @param node Node
     * @param tag String can be null but if spdified then only return child nodes that have the same name as the tag
     * @return Node[]
     */
    private static Node[] getChildNodes(Node node, String tag) {
        if (node == null)return null;
        List resultList = new ArrayList();//store valid children temporarly
        NodeList nodelist = node.getChildNodes();
        Node tempNode = null;
        for (int i = 0; i < nodelist.getLength(); i++) {
            tempNode = nodelist.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tag == null)
                    resultList.add(tempNode);
                else if (tempNode.getNodeName().equalsIgnoreCase(tag))
                    resultList.add(tempNode);
            }
        }
        //convert temporary list of valid child nodes to array of nodes
        Node[] children = new Node[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            children[i] = (Node)resultList.get(i);
        }
        return children;
    }
    
    private static String getAttribute(Node node, String attribute){
        NamedNodeMap attr = node.getAttributes();
        if (attr == null) return null;
        Node ref_attr = attr.getNamedItem(attribute);
        if (ref_attr == null) return null;
        return ref_attr.getNodeValue();
    }
    
    //==============================================================================
    /**
     * Creates a text node with the tag as name and the value for the document doc
     * @param tag String
     * @param value String
     * @param doc Document
     * @return Node
     */
    private static Node createTxtNode(String tag, String value,  org.w3c.dom.Document doc) {
        Element e = doc.createElement(tag);
        if (value == null)return e;
        Text txt = doc.createTextNode(value);
        e.appendChild(txt);
        return e;
    }
    
    
    private static Node createChildTxtNode(String tag, String value, Node parent, org.w3c.dom.Document doc) {
        if (tag == null)return null;
        //    if (value == null) value = "null";
        Node node = createTxtNode(tag, value, doc);
        parent.appendChild(node);
        return node;
    }
    
    
    private static Node createChildNode(String tag, Node parent, org.w3c.dom.Document doc) {
        Element e = doc.createElement(tag);
        parent.appendChild(e);
        return e;
    }
    
    
    //==============================================================================
    /**
     * Converts a node to a string representation
     * @param node Node
     * @throws Exception
     * @return String
     */
    private static String toString(Node node)
    throws Exception {
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
    
    
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    
    public static void main(String[] args)
    throws Exception {
        //XMLConfig config = new XMLConfig(new File("test1.xml"));
        DocXMLHelper helper = new DocXMLHelper();
        helper.cacheXMLString("<table name=\"\"><column header=\"Disease name\" ref=\"WesternBlot_Disease\"/><column header=\"Protein\" ref=\"WesternBlot_question_1\"/><column header=\"Antibody used\" ref=\"WesternBlot_question_2\"/></table>");
        System.out.println("Success: " + helper.isSuccess());
        System.out.println("xml: " + helper);
        
    }
    
}
