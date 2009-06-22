/*
 * DocManipulation.java
 *  @author rennypv
 * Created on February 26, 2004, 11:35 AM
 */

package neuragenix.utils;

import com.sun.star.text.*;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.XTextCursor;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.table.XCell;

//import com.sun.star.c



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
import com.sun.star.table.XTableColumns;


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
 *
 */
public class DocManipulation {
    /** Store tmp info about the table we are bilding from xml */
    class TableModel{
        //font colors
        private int ColorHeader = 0;
        private int Color1 = 0;
        private int Color2 = 0;
        //cells bg colors
        private int BGColorHeader = 16777215; // color for header bg
        private int BGColor1 = 16777215; //white; color for odd rows (header excluded)
        private int BGColor2 = 16777215;//white; color for even rows (header excluded)
        
        private short[] col_widths = null;
        private Object[] columns = null;
        private int n_col =0;
        private int n_row = 0;
        public String getCell(int row, int col){//to be implemented
            return null;
        }
    }
    
    String sConnectionString;
    XComponentLoader xcmpldr = null;
    XComponent xcmp = null;
    Hashtable hshvarbles;
    ChannelRuntimeData rntimedata = null;
    DocDetails docdet;
    //    XComponent xcmp = null;
    
    
    /** specific to neuro talbe replacement
     */
    private static final String TAG_TABLE="<table";
    private static final String TAG_TABLE_END="</table>";
    /*
    private int BGColorHeader = 16777215; // color for header bg
    private int BGColor1 = 16777215; //white; color for odd rows (header excluded)
    private int BGColor2 = 13421823;//violet; color for even rows (header excluded)
    int[] col_widths = null;
    TableModel cur_table = new TableModel(); // properties of the current table we are trying to build from xml
     */
    /** Creates a new instance of DocManipln */
    public DocManipulation() {
        //System.out.println("DocManipulation");
        sConnectionString = "uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager";
        docdet = new DocDetails();
    }
    /** This method is the method first called by an outside object. This does the
     * connection to the document,loading the document,finds the key and replaces and
     * stores the documents
     * @param runtimeData runtimeData which is required to edit the document
     * @return the details of the document saved
     */
    public DocDetails saveTemplate(ChannelRuntimeData runtimeData) {
        //System.out.println("saveTemplate");
        rntimedata = runtimeData;
        xcmpldr = getConnection();
        if(xcmpldr == null) {
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
        xtxtdoc = findNReplaceTables(xtxtdoc);
        xtxtdoc = FindNReplace(xtxtdoc, "<","/>");
        //        System.err.println("The text after editing is :" + xtxtdoc.getText().getString());
        docdet.setFlag(storeDocComponent(xtxtdoc,store_url));
        com.sun.star.frame.XModel xModel =
        (com.sun.star.frame.XModel)UnoRuntime.queryInterface(com.sun.star.frame.XModel.class,xtxtdoc);
        if(xModel!=null) {
            try {
                com.sun.star.util.XCloseable xCloseable = (com.sun.star.util.XCloseable)UnoRuntime.queryInterface(com.sun.star.util.XCloseable.class,xModel);
                //                     System.err.println("-----------in XModel != null------------");
                if(xCloseable!=null) {
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
                else {
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
            catch(Exception e) {
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
    protected String getStoreUrl(String name) {
        //System.out.println("getStoreUrl");
        //        System.err.println("The name of the file is 000000000000" + name);
        String url = null;
        String base_url = PropertiesManager.getProperty("neuragenix.genix.templates.StoreDocumentLocation") + "/";
        if(rntimedata.getParameter("link") == null) {
            int indx = name.indexOf('.');
            //            System.err.println("index of dot is:" + indx);
            String temp_url = name.substring(0,indx);
            temp_url = rntimedata.getParameter("sessionid")+temp_url+name.substring(indx);
            temp_url = temp_url.replaceAll(" ","");
            temp_url = temp_url.replaceAll(":","");
            docdet.setLink(temp_url);
            url = base_url + temp_url;
        }
        else {
            url = base_url + rntimedata.getParameter("link");
        }
        
        //       System.out.println("The store string is  :" + url);
        //       docdet.setLink(name);
        return url;
    }
    /** This is the standard method in openOffice to get a connection to the openoffice
     * @return XComponentLoader as in the openoffice ex
     */
    protected XComponentLoader getConnection() {
        //System.out.println("getConnection");
        try {
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
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    /** A textdocument object is obtained and returned
     * @param xcomp The component which contains the textdocument
     * @return text document
     */
    protected XTextDocument getTextDocument(XComponent xcomp) {
        //System.out.println("getTextDocument");
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
    protected XComponent loadDocument(String url) {
        //System.out.println("loadDocument");
        try {
            if ( url.indexOf("private:") != 0) {
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
        catch( Exception exception ) {
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
    protected XTextDocument FindNReplace(XTextDocument xtxtdoc,String strtstrng,String endstrng) {
        //System.out.println("FindNReplace");
        try {
            StringBuffer stbuf = new StringBuffer(xtxtdoc.getText().getString());
            //               System.out.println("The string before editing is :" +stbuf);
            int lstindx = stbuf.lastIndexOf(endstrng);
            //                System.out.println("lastindx" + lstindx);
            int startindx = stbuf.indexOf(strtstrng);
            //               System.out.println("starting indx:" + startindx);
            int endindx;
            do {
                startindx=stbuf.indexOf(strtstrng,startindx);
                //                    System.out.println("startindx" + startindx);
                endindx = stbuf.indexOf(endstrng,startindx);
                //                    System.out.println("Start and end indx is:" + startindx + " " + endindx);
                String reqstring;
                if(startindx >= 0) {
                    if(startindx == 0) {
                        reqstring = stbuf.substring(0,endindx);
                    }
                    else {
                        reqstring = stbuf.substring((startindx-1),endindx);
                    }
                    reqstring = reqstring.substring(2);
                    //                        System.out.println("Required String" + reqstring);
                    String value = getValue(reqstring);
                    //                    System.out.println("The value is :" + value);
                    xtxtdoc = replaceAll(xtxtdoc,reqstring,value);
                    startindx = endindx;
                }
                else {
                    return xtxtdoc;
                }
            }while(startindx < lstindx);
            //                 System.out.println("The text after editing is :" + xtxtdoc.getText().getString());
            
            return xtxtdoc;
        }
        catch(Exception e) {
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
    protected boolean storeDocComponent(XTextDocument xDoc, String storurl) {
        //System.out.println("storeDocComponent");
        try {
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
        catch(Exception e) {
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
    
    private XTextDocument replaceAll(XTextDocument xtxtdoc,String srchstrng,String rplcstrng) {
        //System.out.println("replaceAll");
        XReplaceable xrplcable = ( XReplaceable )UnoRuntime.queryInterface(com.sun.star.util.XReplaceable.class, xtxtdoc);
        XReplaceDescriptor xrplcdesc = xrplcable.createReplaceDescriptor();
        xrplcdesc.setSearchString("<"+srchstrng+"/>");
        xrplcdesc.setReplaceString(rplcstrng);
        xrplcable.replaceAll(xrplcdesc);
        return xtxtdoc;
    }
    private String getValue(String key) {
        //System.out.println("getValue");
        String value = rntimedata.getParameter(key);
        //        System.err.println("TTTTTTTTTTTTTTTT key and valueTTTTTTTTTT" + key + " " + value);
        if(value == null) {
            value = rntimedata.getParameter("param_" + key);
            if(value == null) {
                //                value = "<"+key+"/>";
                value = "";
            }
        }
        if(!rntimedata.containsKey(key)) {
            this.docdet.populateVector(key);
        }
        return value;
        
    }
    
     /**  !! this method was added by long at the source level and i had to add it hear as other source level classed uses it - won't compile without it !!
      *
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
    public static void main(String args[]) {
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
    
    
    
    
    //===============================================================================================================
    //===============================================================================================================
    //===============================================================================================================
    //== NEURO SPECIFIC HACK - TO BE CLEANED AND STANDARDISED ==================================-====================
    //===============================================================================================================
    /** THIS METHOD IS SALLCED BEFORE FindNReplace() TO REPLACE THE TABLES TAGS <TABLE > ... </TABLE>
     * goes through the whole doc searching for table xml tags and calls replaceTable
     */
    protected XTextDocument findNReplaceTables(XTextDocument xtxtdoc) {
        //System.out.println("FindNReplaceTABLES");
        try {
            StringBuffer stbuf = new StringBuffer(xtxtdoc.getText().getString());
            
            int lstindx = stbuf.lastIndexOf(TAG_TABLE_END);//constraint the start and end of search (from first start tab to last end tag)
            int startindx = stbuf.indexOf(TAG_TABLE);
            
            int endindx;
            do {
                startindx=stbuf.indexOf(TAG_TABLE,startindx);
                endindx = stbuf.indexOf(TAG_TABLE_END,startindx);//look for end tag starting search from start tag. This solved a problem where the document had greater than or less than char
                String table_xml;
                if(startindx >= 0) {
                    table_xml = stbuf.substring(startindx,(endindx + TAG_TABLE_END.length() ));
                    xtxtdoc = replaceTable(xtxtdoc, table_xml);
                    startindx = endindx;
                }
                else {
                    return xtxtdoc;
                }
            }while(startindx < lstindx);
            return xtxtdoc;
        }
        catch(Exception e) {
            e.printStackTrace();
            return xtxtdoc;
        }
        
    }
    
    
    /** Create a table structure tha represents the xml segment (using the runtime environment) an insert it in the text document (replacing the original xml).
     *returns text document after inserting the table
     */
    private XTextDocument replaceTable(XTextDocument xtxtdoc, String xml){
        //System.out.println("replaceTable");
        TableModel cur_table = parseTable(xml);
        
        //creating table
        try{
            //XTextDocument xtxtdoc = getTextDocument(xcmp);
            XMultiServiceFactory mxDocFactory = (XMultiServiceFactory) UnoRuntime.queryInterface( XMultiServiceFactory.class, xtxtdoc );
            XTextTable xTable = (XTextTable)UnoRuntime.queryInterface(XTextTable.class, mxDocFactory.createInstance("com.sun.star.text.TextTable"));
            xTable.initialize(cur_table.n_row, cur_table.n_col);
            //System.out.println("xTable.initialize(" + cur_table.n_row + ","+ cur_table.n_col+ ")");
            //System.out.println("Table XML:\n[" + xml + "]");
            
            xtxtdoc = insertTable(xTable, xtxtdoc, xml);//...into document. necessary to be able to add cells
            
            //todo: set widths
            //xTable.
            //TableColumnSeparator[] tableColumnSeparators = (XTextTable)UnoRuntime.queryInterface(TableColumnSeparator.class, xTable);
            
            try{
                /*
                //XIndexAccess xTableColumns = (XTableColumns)UnoRuntime.queryInterface(XTableColumns.class, xTable.getColumns());
                XTableColumns xTableColumns = (XTableColumns)UnoRuntime.queryInterface(XTableColumns.class, xTable.getColumns());
                System.out.println( "Type: " + xTableColumns.getElementType());
                Class clas = xTableColumns.getByIndex(1).getClass();
                System.out.println("Class: " + clas.getName());
                System.out.println("Superclass: " + clas.getSuperclass());
                Class[] classes = clas.getInterfaces();
                System.out.println("Interfaces:");
                for (int i=0; i< classes.length; i++){
                    System.out.println(classes[i].getName());
                }
                System.out.println("xTableColumns.hasElements(): " + xTableColumns.hasElements() + "  count: " + xTableColumns.getCount() ) ;
                //XPropertySet textColumnProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTableColumns.getByIndex(1) );
                com.sun.star.table.TableColumn tableColumn = (com.sun.star.table.TableColumn) UnoRuntime.queryInterface(com.sun.star.table.TableColumn.class, xTableColumns.getByIndex(1) );
                System.out.println("tableColumn: " + tableColumn);
                xTableColumn textColumnProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTableColumns.getByIndex(1) );
                System.out.println("textColumnProps: " + textColumnProps);
                 */
                
                
                
                XPropertySet xTableProps = (XPropertySet)UnoRuntime.queryInterface( XPropertySet.class, xTable );
                Object colSepsObject = xTableProps.getPropertyValue("TableColumnSeparators" );
                TableColumnSeparator[] colSeps = (TableColumnSeparator[]) colSepsObject;
                //System.out.println("colSeps.length: " + colSeps.length);
                for (int i=0; i< cur_table.n_col-1 ; i++){//last column won't be taken into consideration
                    if (cur_table.col_widths[i]>0){
                        colSeps[i].Position = (short) (cur_table.col_widths[i] * 100);
                        //colSeps[i].Position = cur_table.col_widths[i] ;
                        //System.out.println("Col width(" + i + "): " + cur_table.col_widths[i] );
                    }
                }
                xTableProps.setPropertyValue("TableColumnSeparators", colSeps);
                
                
                
                //TextColumn textColumn = (TextColumn)xTableColumns.getByIndex(1);
                //XPropertySet textColumnProps = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, textColumn );
                //textColumnProps.setPropertyValue("Width",new Integer(50));
                
            }catch(Exception ex){
                System.out.println("Error setting width of columns 1");
                ex.printStackTrace();
            }
            
            
            //populate cells of tables. Names of cells similar to xls , row1: A1, A2, A3,... row 2: B1, B2, B3,...
            char col_lable = 'A';//index of column name
            //char end = 'z';
            for (int col=0; col<cur_table.n_col; col++){ //columns
            	Object o = cur_table.columns[col];
            	if (o!=null){
            		String[] column = (String [])o ;
            		//int row_n = column.length;// # of rows in this columns
            		String value = null;
            		for (int row=0; row< cur_table.n_row; row++){//rows in a columns
            			//only get the values from the column if it is long enough to avoid index out of bound exception
            			//a particular columns might be shorter that the #rows in a table (a column is longer than others)
            			if (row<column.length) 
            				value = column[row];            				
            			else
            				value = "";
            			if (value==null)//handle null values
            				value = "";
            			String cellName = "" + col_lable + (row+1);
            			// BGColor1:BGColor2 except the the header BGColorHeader
            			if (row==0) // header row
            				insertIntoCell( cellName, value, xTable, cur_table.ColorHeader, cur_table.BGColorHeader);
            			else if (row%2!=0) //odd rows (header not counted)
            				insertIntoCell( cellName, value, xTable, cur_table.Color1, cur_table.BGColor1);
            			else //even rows (header not counted)
            				insertIntoCell( cellName, value, xTable, cur_table.Color2, cur_table.BGColor2);
            			
            		}
            	}
            	col_lable += 1; //increment col A,B,C ...
            }
            return xtxtdoc;
        }catch(Exception ex){
            ex.printStackTrace();
            return xtxtdoc;
        }
    }
    
    
    /**  Parse the xml and populated a TableModel with all values and properties of that table. It uses the runtime environment to get values.
     * Values are stored in the model as an Array of Array of Strings. The array represents the columns (array of string).
     */
    private TableModel parseTable(String xml){
        //System.out.println("parseTable");
        TableModel cur_table = new TableModel();
        cur_table.n_row = 1;// minimum length
        DocXMLHelper helper = new DocXMLHelper();
        helper.cacheXMLString(xml);
        String[] col_refs = helper.getTableColumnsRef();
        String[] col_headers = helper.getTableColumnsHeader();
        cur_table.col_widths = helper.getTableColumnsWidths();
        cur_table.n_col = col_refs.length;//find # columns
        java.util.Vector col_list = new java.util.Vector(cur_table.n_col);
        //retrieve table properties (attributes) and store them in the TableModel
        
        try{//header row font color
            if (helper.getTableAttribute("ColorHeader") != null)
                cur_table.ColorHeader = getHex2Dec(helper.getTableAttribute("ColorHeader"));
        }
        catch(Exception ex){
            System.out.println("invalied ColorHeader for talbe");
            cur_table.ColorHeader = 16777215; //white
        }
        try{//header bg color
            if (helper.getTableAttribute("BGColorHeader") != null)
                cur_table.BGColorHeader = getHex2Dec(helper.getTableAttribute("BGColorHeader"));
        }
        catch(Exception ex){
            System.out.println("invalied BGColorHeader for talbe");
            cur_table.BGColorHeader = 16777215; //white
        }
        try{//font color for odd rows
            if (helper.getTableAttribute("Color1") != null)
                cur_table.Color1 = getHex2Dec(helper.getTableAttribute("Color1"));
        }
        catch(Exception ex){
            System.out.println("invalied Color1 for talbe");
            cur_table.Color1 = 0; //white
        }
        try{//font color for even rows
            if (helper.getTableAttribute("Color2") != null)
                cur_table.Color2 = getHex2Dec(helper.getTableAttribute("Color2"));
        }
        catch(Exception ex){
            System.out.println("invalied Color2 for talbe");
            cur_table.Color2 = 0; //white
        } 
        try{// bg color for odd rows
            if (helper.getTableAttribute("BGColor1") != null)
                cur_table.BGColor1 = getHex2Dec(helper.getTableAttribute("BGColor1"));
        }
        catch(Exception ex){
            System.out.println("invalied BGColor1 for talbe");
            cur_table.BGColor1 = 16777215; //white
        }
        try{// bg color for even rows
            if (helper.getTableAttribute("BGColor2") != null)
                cur_table.BGColor2 = getHex2Dec(helper.getTableAttribute("BGColor2"));
        }
        catch(Exception ex){
            System.out.println("invalied BGColor2 for talbe");
            cur_table.BGColor2 = 16777215; //white
        }
        
        //create an array of array of string (array of columns, each columns is array of string - rows)
        for (int i=0; i<col_refs.length; i++ ){ // for each column (new line speratated) make String[]
            String strColumn = rntimedata.getParameter(col_refs[i]);
            if (strColumn != null) { //if parameter found in runtime environment, create an array of entries
                String [] column = strColumn.split("\n");
                //add header to top of array
                String [] column_w_header = new String[column.length+1];
                column_w_header[0]=col_headers[i];
                System.arraycopy(column, 0, column_w_header, 1, column.length);
                if(column_w_header.length > cur_table.n_row)//get the max # row
                    cur_table.n_row = column_w_header.length;
                col_list.add(column_w_header);//add the columns to list of columns
            }else {//ref not found in runtime, create an empty column
                if (col_headers[i] == null) //if there is no header for that column, add empty array of strings to the list of columns
                    col_list.add(new String[0]);
                else{//if there is a header lable then add a column with the only one value (the header lables) to the list of columns
                    String [] tmp = new String[1];
                    tmp[0] = col_headers[i];
                    col_list.add(tmp);
                }
            }
        }
        //convert the vector of columns(array of strings) to array of columns (aray of strings)
        cur_table.columns = col_list.toArray();
        return cur_table;
        //return col_list.toArray();
    }
    
    /** Replace the xml segment (text) with the passed XTextTable. !!! This is necessary before populating the table !!!
     */
    protected XTextDocument insertTable(XTextTable xTable, XTextDocument xtxtdoc, String xml){
        //System.out.println("insertTable");
        try{
            XText xTest = xtxtdoc.getText();
            XTextCursor mxDocCursor = xTest.createTextCursor();
            XSentenceCursor xSentenceCursor = (XSentenceCursor) UnoRuntime.queryInterface(XSentenceCursor.class, mxDocCursor );// not sure if necessary
            xSentenceCursor.gotoStart(false); // not sure if necessary
            XWordCursor xWordCursor = (XWordCursor) UnoRuntime.queryInterface(XWordCursor.class, mxDocCursor );//to select what we want to delete
            //find starting and ending point of xml (text) to be replaced
            int start = xTest.getString().indexOf(xml);
            int end = xml.length();
            //select the xml (text) to be replaced
            xWordCursor.goRight((short)start, false);//go to the starting position without selecting
            xWordCursor.goRight((short)(end) , true);// got to the end of tag while selecting
            //insert the table with override set to true. replace the selected contents
            xTest.insertTextContent(xWordCursor, xTable, true);
            return xtxtdoc;//return the text document after inserting the table (replacing the xml text)
        }catch(Exception e){
            System.out.println("insertTable exception");
            e.printStackTrace();
            return xtxtdoc;
        }
    }
    
    /** pupulate a cell (ref) of a table with text using a particular BG color
     */
    protected static void insertIntoCell(String sCellName, String sText, XTextTable xTable, int color, int bgColor) {
        //System.out.println("insertIntoCell(" + sCellName + "," + sText + ",XTextTable," + bgColor +  "," + color +") " );
        try { 
            XCell cell = xTable.getCellByName( sCellName );            
            XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, cell );
            
            
            //set the font color
            try{
                XTextCursor xCellCursor = xCellText.createTextCursor();
                XPropertySet xCellCursorProps = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, xCellCursor );
                xCellCursorProps.setPropertyValue( "CharColor", new Integer( color ) );
            }catch(Exception fex){
                System.out.println("Cant format font. insertIntoCell(" + sCellName + "," + sText + ",XTextTable," + bgColor + "," + color + ") " );
                fex.printStackTrace();
            }
            
            
            //set the bg color
            try{
                XPropertySet xCellProps = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, cell );
                xCellProps.setPropertyValue( "BackColor", new Integer( bgColor ) );//documentation mentioned CellBackColor
            }catch(Exception fex){
                System.out.println("Cant format cell. insertIntoCell(" + sCellName + "," + sText + ",XTextTable," + bgColor + "," + color + ") " );
                fex.printStackTrace();
            }
            
            
            // Set the text of the cell
            xCellText.setString( sText );
        }
        catch ( Exception e) {
            System.out.println("Cant insert cell. insertIntoCell(" + sCellName + "," + sText + ",XTextTable," + bgColor +  "," + color + ") " );
            e.printStackTrace();
        }
    }
    /** converts a hex string to a decimal. initially developed to convert rgb color to decimal to be used to color rows. (eg. FFCC00);
     */
    protected static int getHex2Dec(String hex) throws NumberFormatException{
        if (hex==null) return -1;
        Integer result = null;        
        result = Integer.valueOf(hex,16);
        return result.intValue();        
    }
    //===============================================================================================================
    
    
    
}

