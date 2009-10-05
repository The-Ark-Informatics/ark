/**
 * OLAPSchema.java
 * Copyright © 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 19/11/2003
 */

package neuragenix.dao;

/**
 * Class to model an OLAP schema
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

import java.util.Vector;
import java.util.Hashtable;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
 
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

public class OLAPSchema {
    
    /** Indicate if the schema loaded
     */
    private static boolean blLoaded = false;
    
    /** Stores cubes and dimensions
     */
    private static Hashtable hashCube_Dimension = new Hashtable(20);
    
    /** Stores cubes and measures
     */
    private static Hashtable hashCube_Measure = new Hashtable(20);
    
    /** Creates a new instance of OLAPSchema
     */
    public OLAPSchema()
    {
    }
    
    /** Load the database schema
     */
    public static void loadOLAPSchema(File file)
    {
        // loads the schema if it's not loaded yet
        if (!blLoaded)
        {
            // create a document factory instance
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document;
            
            try
            {
                // parse the XML file
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(file);

                // add cubes
                NodeList cubeNodes = document.getElementsByTagName("Cube");
                for (int index=0; index < cubeNodes.getLength(); index++)
                {
                    Node currentCube = cubeNodes.item(index);
                    NamedNodeMap cubeAttributes = currentCube.getAttributes();
                    String strCubeName = cubeAttributes.getNamedItem("name").getNodeValue();
                    
                    // add dimensions & measures for the current cube
                    NodeList dimensionNodes = currentCube.getChildNodes();
                    Vector vtDimensions = new Vector(10, 5);
                    Vector vtMeasures = new Vector(10, 5);
                    for (int index1=0; index1 < dimensionNodes.getLength(); index1++)
                    {
                        Node currentDimension = dimensionNodes.item(index1);
                        if (currentDimension.getNodeType() == Node.ELEMENT_NODE)
                        {
                            // a Dimension node
                            if (currentDimension.getNodeName().equals("Dimension"))
                            {
                                NamedNodeMap dimensionAttributes = currentDimension.getAttributes();
                                vtDimensions.add(dimensionAttributes.getNamedItem("name").getNodeValue());
                            }
                            // a Measure node
                            else if (currentDimension.getNodeName().equals("Measure"))
                            {
                                NamedNodeMap measureAttributes = currentDimension.getAttributes();
                                vtMeasures.add(measureAttributes.getNamedItem("name").getNodeValue());
                            }
                        }
                    }
                     
                    // add this cube
                    hashCube_Dimension.put(strCubeName, vtDimensions);
                    hashCube_Dimension.put(strCubeName, vtMeasures);
                }
                
                // set loaded status to true
                blLoaded = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
