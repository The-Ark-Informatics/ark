/*
 * DataMapping.java
 *
 * Created on 13 January 2005, 09:49
 * Author Long Tran
 *
 */

package neuragenix.genix.polling_agent;

// Import packages for parsing the xml file
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
 
import org.w3c.dom.*;
import org.apache.xml.serialize.*;

import java.io.InputStream;
import java.util.Hashtable;
/**
 * NOT NEEDED 
 *
 * @author ltran
 */
public class DataMapping {

    //private static Hashtable hsDomainHash = null;
    /** Creates a new instance of DataMapping */
    public DataMapping() {
    }
    
    public static Hashtable getMappingInfo(  ) throws Exception{



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        Hashtable hashMap = new Hashtable( 5,5 );

        // parse the XML file
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream file = DataMapping.class.getResourceAsStream("DataMapping.xml");
        org.w3c.dom.Document mappingDoc = builder.parse(file);
        mappingDoc.normalize();

        NodeList casegenixNodes = mappingDoc.getElementsByTagName("casegenix");
        for( int i = 0; i < casegenixNodes.getLength(); i++ ){
            Node casegenixNode = casegenixNodes.item(i);

            NodeList entryNodes = ((Element) (casegenixNode)).getElementsByTagName( "entry" );

            for( int e = 0; e < entryNodes.getLength(); e++ ){

                Element entryNode = (Element) entryNodes.item(e);

                String strEntry = entryNode.getAttribute( "name" );

                String strValue = ((Text) (entryNode.getFirstChild())).getNodeValue();

                hashMap.put( strEntry, strValue );
            }
            /*
            NodeList domainNodes = ((Element) (casegenixNode)).getElementsByTagName( "domain" );

            for( int e = 0; e < domainNodes.getLength(); e++ ){

                Node domainNode = domainNodes.item( e );
                
                String strValue = ((Text) (domainNode.getFirstChild())).getNodeValue();

                hashMap.put( strValue, "" );
            }*/

        }
                
        return hashMap;
    }
    /*
    public static Hashtable getDomainHash(){
        if( hsDomainHash == null ){
            getMappingInfo();
        }
        return hsDomainHash;
    }
    */
    public static Document map( Document doc ) throws Exception{
                 
        replaceNodeName( doc, getMappingInfo(), doc );
        return doc;
     
    }
    
    public static void replaceNodeName( Node node, Hashtable map, Document doc ){
        
        // map document
        
        if( node.getNodeType() == Node.ELEMENT_NODE ){
            Element el = (Element) node;
            
            
            if( map.containsKey( el.getNodeName() )){
                NodeList nodes = el.getChildNodes();

                Node pNode = el.getParentNode();
                                
                // create new element with new name
                Element nNode;
                //doc.insertBefore( nNode, root );
                nNode = doc.createElement( (String) map.get(el.getNodeName() ));
                
                //doc.removeChild( nNode );
                for( int i=0; i < nodes.getLength(); i++ ){

                    nNode.appendChild( nodes.item(i).cloneNode (true) );

                }
                    
                
                pNode.replaceChild( nNode, el );
                //pNode.removeChild( el );
                node = nNode;

            }
        }        
        
        
        if (node.hasChildNodes()) {
          Node firstChild = node.getFirstChild();
          replaceNodeName(firstChild, map, doc);
        }
        Node nextNode = node.getNextSibling();
        if (nextNode != null) replaceNodeName(nextNode, map, doc);


    }
}
