/*
 * DocManipulation.java
 *  @author rennypv
 * Created on February 26, 2004, 11:35 AM
 */

package neuragenix.utils;


import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XText;
import com.sun.star.text.XTextRange;
import com.sun.star.util.XSearchable;
import com.sun.star.util.XReplaceable;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XCloseable;
import com.sun.star.container.XIndexAccess;
import com.sun.star.text.XTextCursor;


import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.PropertiesManager;

import java.util.Date;
import java.util.Hashtable;

import neuragenix.utils.DocDetails;
/** This class is to read a .doc file and remove the tagged keys with the values and
 * save the document
 *To use this application the openoffice needs to be running in the listening mode
 *To start that...run
 *<OpenOfficePath>/programs/soffice "-accept=socket,port=8100;urp;"
 *
 *The details of where the templates are and where the document is to be stored is set in the
 *portal.properties file in properties

 */
public class DocManipulation
{
    
    String sConnectionString;
    XComponentLoader xcmpldr = null;
    XComponent xcmp = null;
    Hashtable hshvarbles;
    ChannelRuntimeData rntimedata = null;
    DocDetails docdet;
//    XComponent xcmp = null;
    
    /** Creates a new instance of DocManipln */
    public DocManipulation()
    {
          sConnectionString = "uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager";
          docdet = new DocDetails();
    }
    /** This method is the method first called by an outside object. This does the
     * connection to the document,loading the document,finds the key and replaces and
     * stores the documents
     * @param runtimeData runtimeData which is required to edit the document
     * @return the details of the document saved
     */    
    public DocDetails saveTemplate(ChannelRuntimeData runtimeData)
    {
        rntimedata = runtimeData;
        xcmpldr = getConnection();
        if(xcmpldr == null)
        {
            return null;
        }
        String template_url = null;
/*        if(runtimeData.getParameter("link") == null)
        {
            template_url = PropertiesManager.getProperty("neuragenix.genix.templates.TemplateLocation") + "/" +runtimeData.getParameter("templateName");
        }
        else
        {
            template_url = PropertiesManager.getProperty("neuragenix.genix.templates.StoreDocumentLocation") + "/" + runtimeData.getParameter("link");
        }*/
         template_url = PropertiesManager.getProperty("neuragenix.genix.templates.TemplateLocation") + "/" +runtimeData.getParameter("templateName");
//        System.err.println("Template url" +template_url);
        String store_url = getStoreUrl(runtimeData.getParameter("templateName"));
//        System.err.println("------------storeurl---------" + store_url);
        xcmp = loadDocument(template_url);
        XTextDocument xtxtdoc = getTextDocument(xcmp);
//        System.out.println("Text in the doc is: " + xtxtdoc.getText().getString());
       xtxtdoc = FindNReplace(xtxtdoc, "<","/>");
//        System.err.println("The text after editing is :" + xtxtdoc.getText().getString());    
       docdet.setFlag(storeDocComponent(xtxtdoc,store_url));
             com.sun.star.frame.XModel xModel = 
             (com.sun.star.frame.XModel)UnoRuntime.queryInterface(com.sun.star.frame.XModel.class,xtxtdoc);
             if(xModel!=null)
            {
                try
                {
                     com.sun.star.util.XCloseable xCloseable = (com.sun.star.util.XCloseable)UnoRuntime.queryInterface(com.sun.star.util.XCloseable.class,xModel);
//                     System.err.println("-----------in XModel != null------------");
                    if(xCloseable!=null)
                    {
    //                   try
                       {
                           xCloseable.close(true);
//                           System.err.println("---------------in close----------------");
                       }
    /*                   catch(com.sun.star.util.CloseVetoException exCloseVeto)
                       {
                            exCloseVeto.printStackTrace();
                       }*/
                    }
                   else
                    {
    //                 try
                        {
//                            System.err.println("-----------in disposeable---------");
                         com.sun.star.lang.XComponent xDisposeable =
                           (com.sun.star.lang.XComponent)UnoRuntime.queryInterface(
                            com.sun.star.lang.XComponent.class,xModel);      
                            xDisposeable.dispose();
                          }
    /*                      catch(com.sun.star.beans.PropertyVetoException exModifyVeto)
                          {
                              exModifyVeto.printStackTrace();
                          }*/
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
             }
        return docdet;
    }
    /** This code is used to manipulate the store_url string to add the date stamp
     * to th end of the filename
     * @param name template name
     * @return the url String as required by the openoffice libraries
     */    
    protected String getStoreUrl(String name)
    {
//        System.err.println("The name of the file is 000000000000" + name);
        String url = null;
        String base_url = PropertiesManager.getProperty("neuragenix.genix.templates.StoreDocumentLocation") + "/";
        if(rntimedata.getParameter("link") == null)
        {
            int indx = name.indexOf('.');
//            System.err.println("index of dot is:" + indx);
            String temp_url = name.substring(0,indx);
            temp_url = rntimedata.getParameter("sessionid")+temp_url+name.substring(indx);
            temp_url = temp_url.replaceAll(" ","");
            temp_url = temp_url.replaceAll(":","");
            docdet.setLink(temp_url);            
            url = base_url + temp_url;            
        }
        else
        {
            url = base_url + rntimedata.getParameter("link");
        }

//       System.out.println("The store string is  :" + url);
//       docdet.setLink(name);
       return url;
    }
    /** This is the standard method in openOffice to get a connection to the openoffice
     * @return XComponentLoader as in the openoffice ex
     */    
    protected XComponentLoader getConnection()
    {
        try
        {
              XComponentContext xcomponentcontext =
          com.sun.star.comp.helper.Bootstrap.createInitialComponentContext( null );

          /* Gets the service manager instance to be used (or null). This method has
             been added for convenience, because the service manager is a often used
             object. */
          XMultiComponentFactory xmulticomponentfactory =
          xcomponentcontext.getServiceManager();

          /* Creates an instance of the component UnoUrlResolver which
             supports the services specified by the factory. */
          Object objectUrlResolver =
          xmulticomponentfactory.createInstanceWithContext(
          "com.sun.star.bridge.UnoUrlResolver", xcomponentcontext );

          // Create a new url resolver
          XUnoUrlResolver xurlresolver = ( XUnoUrlResolver )
          UnoRuntime.queryInterface( XUnoUrlResolver.class,
          objectUrlResolver );

          // Resolves an object that is specified as follow:
          // uno:<connection description>;<protocol description>;<initial object name>
          Object objectInitial = xurlresolver.resolve( sConnectionString );

          // Create a service manager from the initial object
          xmulticomponentfactory = ( XMultiComponentFactory )
          UnoRuntime.queryInterface( XMultiComponentFactory.class, objectInitial );

          // Query for the XPropertySet interface.
          XPropertySet xpropertysetMultiComponentFactory = ( XPropertySet )
          UnoRuntime.queryInterface( XPropertySet.class, xmulticomponentfactory );

          // Get the default context from the office server.
          Object objectDefaultContext =
          xpropertysetMultiComponentFactory.getPropertyValue( "DefaultContext" );

          // Query for the interface XComponentContext.
          xcomponentcontext = ( XComponentContext ) UnoRuntime.queryInterface(
          XComponentContext.class, objectDefaultContext );

          /* A desktop environment contains tasks with one or more
             frames in which components can be loaded. Desktop is the
             environment for components which can instanciate within
             frames. */
          return ( XComponentLoader )UnoRuntime.queryInterface( XComponentLoader.class,xmulticomponentfactory.createInstanceWithContext(
                "com.sun.star.frame.Desktop", xcomponentcontext ) );
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
    /** A textdocument object is obtained and returned
     * @param xcomp The component which contains the textdocument
     * @return text document
     */    
    protected XTextDocument getTextDocument(XComponent xcomp)
    {
        return (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class,xcomp );
 /*       XText xtxt = xtxtdoc.getText();
        return xtxt.getString(); */
    }
    /** This method is usd to get the file component. It converts the url to the requred
     * format. it then defines the properties.
     * The properties defined here are
     * Overite - This is used since the file already exists and we are trying to edit
     * the file
     * Hidden - this is because we do not want to document to open up with openoffice
     * application
     * @param url the location of the file template
     * @return the component containing the document
     */    
    protected XComponent loadDocument(String url)
    {
        try
        {
          if ( url.indexOf("private:") != 0) 
          {
              java.io.File sourceFile = new java.io.File(url);
              StringBuffer sTmp = new StringBuffer("file:///");
              sTmp.append(sourceFile.getCanonicalPath().replace('\\', '/'));
              url = sTmp.toString();
          }  
          PropertyValue[] loadProps = new PropertyValue[2];
          loadProps[0] = new PropertyValue();
          loadProps[0].Name = "Overwrite";
          loadProps[0].Value = new Boolean(true); 
          loadProps[1] = new PropertyValue();
          loadProps[1].Name = "Hidden";
          loadProps[1].Value = new Boolean(true);
          // Load a Writer document, which will be automaticly displayed
          return xcmpldr.loadComponentFromURL(url, "_blank", 0,loadProps);
  
        }
        catch( Exception exception ) 
        {
//          System.err.println( exception );
          return null;
        }
    }
    /** This method...reads the whole of the document into a StringBuffer and the
     * stringbuffer is searched for the tags and the keys are retrieved. then teh
     * replace all method is called to replace all the keys with values.
     * @param xtxtdoc the document object
     * @param strtstrng the starting tag in the search string
     * @param endstrng the ending tag required for the search of the document
     * @return the text document with all the tags replaced
     */    
    protected XTextDocument FindNReplace(XTextDocument xtxtdoc,String strtstrng,String endstrng)
    {
        try
        {
                StringBuffer stbuf = new StringBuffer(xtxtdoc.getText().getString());
//               System.out.println("The string before editing is :" +stbuf);
                int lstindx = stbuf.lastIndexOf(endstrng);
//                System.out.println("lastindx" + lstindx);
                int startindx = stbuf.indexOf(strtstrng);
//               System.out.println("starting indx:" + startindx);
                int endindx;
                do
                {
                    startindx=stbuf.indexOf(strtstrng,startindx);
//                    System.out.println("startindx" + startindx);                    
                    endindx = stbuf.indexOf(endstrng,startindx);
//                    System.out.println("Start and end indx is:" + startindx + " " + endindx);
                    String reqstring;
                    if(startindx >= 0)
                    {
                        if(startindx == 0)
                        {
                            reqstring = stbuf.substring(0,endindx);
                        }
                        else
                        {
                            reqstring = stbuf.substring((startindx-1),endindx);
                        }
                        reqstring = reqstring.substring(2);
//                        System.out.println("Required String" + reqstring);
                        String value = getValue(reqstring);
    //                    System.out.println("The value is :" + value);
                        xtxtdoc = replaceAll(xtxtdoc,reqstring,value);
                        startindx = endindx;
                    }
                    else
                    {
                        return xtxtdoc;
                    }
                }while(startindx < lstindx);
//                 System.out.println("The text after editing is :" + xtxtdoc.getText().getString()); 
                
                 return xtxtdoc;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /** This is the standard method to store teh document file.
     * It corrects the url and calls the XStorable class which store all teh data.
     *
     * the properties set here are
     * Filter name......this determines whether the file is to besaved in MS Word
     * 97/2000 and so on
     * @param xDoc the text Document to be saved
     * @param storurl the url where the file is to be saved
     * @return true if the document is saved or false if the document is not successfully
     * saved
     */    
    protected boolean storeDocComponent(XTextDocument xDoc, String storurl) 
    {
        try
        {
             java.io.File destfile = new java.io.File(storurl);
              StringBuffer stortmp = new StringBuffer("file:///");
              stortmp.append(destfile.getCanonicalPath().replace('\\', '/'));
              storurl = stortmp.toString();
//              System.err.println("the storurl is : " + storurl);
             XStorable xStorable = (XStorable)UnoRuntime.queryInterface(XStorable.class, xDoc);
             PropertyValue[] storeProps = new PropertyValue[1];
             storeProps[0] = new PropertyValue();
             storeProps[0].Name = "FilterName";
             storeProps[0].Value = PropertiesManager.getProperty("neuragenix.genix.templates.FileTypes"); 
             xStorable.storeAsURL(storurl, storeProps);
/*             int indx = storurl.indexOf("/chw");
             String tmp_storurl = storurl.substring(indx);
             docdet.setLink(tmp_storurl); */
             return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
/*        finally
        {
             com.sun.star.frame.XModel xModel = 
             (com.sun.star.frame.XModel)UnoRuntime.queryInterface(com.sun.star.frame.XModel.class,xDoc);
             if(xModel!=null)
            {
                 com.sun.star.util.XCloseable xCloseable = (com.sun.star.util.XCloseable)UnoRuntime.queryInterface(com.sun.star.util.XCloseable.class,xModel);
                if(xCloseable!=null)
                {
                   try
                   {
                       xCloseable.close(true);
                   }
                   catch(com.sun.star.util.CloseVetoException exCloseVeto)
                   {
                        exCloseVeto.printStackTrace();
                   }
                }
            }
        }*/
     } 
    
    private XTextDocument replaceAll(XTextDocument xtxtdoc,String srchstrng,String rplcstrng)
    {
         XReplaceable xrplcable = ( XReplaceable )UnoRuntime.queryInterface(com.sun.star.util.XReplaceable.class, xtxtdoc);
         XReplaceDescriptor xrplcdesc = xrplcable.createReplaceDescriptor();
         xrplcdesc.setSearchString("<"+srchstrng+"/>");
         xrplcdesc.setReplaceString(rplcstrng);
         xrplcable.replaceAll(xrplcdesc);
        return xtxtdoc;
    }
    private String getValue(String key)
    {
        String value = rntimedata.getParameter(key);
//        System.err.println("TTTTTTTTTTTTTTTT key and valueTTTTTTTTTT" + key + " " + value);
        if(value == null)
        {
            value = rntimedata.getParameter("param_" + key);
            if(value == null)
            {
//                value = "<"+key+"/>";
                value = "";
            }
        }
        if(!rntimedata.containsKey(key))
        {
            this.docdet.populateVector(key);
        }
            return value;

    }
    /** 
     * Method to return a number of occurrences of a string in a document.
     *
     * @param strDocument file name (with full path).
     * @param strText the text to search for
     * @return number occurrrances or 0 if non is found.
     **/
    public static int getOccurrences( String strDocument, String strText ){
        int intOcc = 0;
    
        DocManipulation doc = new DocManipulation();
        doc.xcmpldr = doc.getConnection();
        doc.xcmp = doc.loadDocument(strDocument);
        
        XTextDocument xtxtdoc = doc.getTextDocument(doc.xcmp);
        StringBuffer strDocText = new StringBuffer(xtxtdoc.getText().getString());
        
        // simple algorithm to count the number occurrence
        int i = 0;
        while( i > -1 ){
            i = strDocText.indexOf( strText, i );
            if( i > -1 ){
                intOcc++;
                i++;
            }
        }
        
        return intOcc;
    }
    /** Main method to test application
     * @param args the main method to test
     */    
    public static void main(String args[])
    {
        DocManipulation doc = new DocManipulation();
        doc.hshvarbles = new Hashtable();
        doc.hshvarbles.put("name", "renny");
        doc.hshvarbles.put("address", "392,Springvale rd");
        doc.xcmpldr = doc.getConnection();
        String templ_url = "/home/renny/development/templates/testpage.doc";
        doc.xcmp = doc.loadDocument(templ_url);
        XTextDocument xtxtdoc = doc.getTextDocument(doc.xcmp);
//        System.out.println("Text in the doc is: " + xtxtdoc.getText().getString());
       xtxtdoc = doc.FindNReplace(xtxtdoc, "<!","/>");
//        System.out.println("The text after editing is :" + xtxtdoc.getText().getString());                       
        doc.storeDocComponent(xtxtdoc, templ_url);
    }
}

