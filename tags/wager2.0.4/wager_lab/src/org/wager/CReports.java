package org.wager;
import org.jasig.portal.IChannel;
import org.jasig.portal.IMimeResponse;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.UPFileSpec;
import org.jasig.portal.utils.XSLT;
import org.w3c.dom.*;
import neuragenix.security.*;
import neuragenix.bio.utilities.*;

import org.w3c.dom.Node;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import java.sql.*;
import oracle.xml.sql.query.*;
import java.util.*;
import java.io.*;
import java.text.*;
import  net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
import neuragenix.security.exception.SecurityException;



/** 
 *  Customized Reporting Module for Biogenix v4.5. 
 *  @author Chris Williams (chrisjw@cyllene.uwa.edu.au)
 */
public class CReports implements IChannel, IMimeResponse{


  private ChannelRuntimeData runtimeData;
  
  private String name_prev; // the name that was previously submitted, to go
  private boolean submitting;                          // in the text box by default.
  private String report_num; 
  private Connection conn;
  private String error;
  private String filename;
  private byte[] bytes;	
  private Map parameters;
  AuthToken authToken;
  ChannelStaticData staticData;
/** 
   *  Construct0r. 
   */ 
  public CReports() { 
    this.name_prev = ""; // start with the text box empty
    this.report_num = new String(); // start with the first report  
    this.error = new String();
    this.submitting = false;
    this.parameters = new HashMap();
    this.filename = "";
    this.bytes=null;
	try { 
	 Class.forName("com.codestudio.sql.PoolMan").newInstance();
    } catch (Exception ex) {
            System.out.println("Could Not Find the PoolMan Driver. Is poolman.jar in your CLASSPATH?");
    }
        this.conn = null;
    try {
        // establish a Connection to the database with
        // <dbname>testdb</dbname>
        //in the poolman.xml file

	}
	catch (Exception e) {
	e.printStackTrace();
        }
}
  
  //
  //  Implementing the IChannel Interface
  //

  /** 
   *  Returns channel runtime properties.
   *  Satisfies implementation of Channel Interface.
   *
   *  @return handle to runtime properties 
   */ 
  public ChannelRuntimeProperties getRuntimeProperties() { 
    return new ChannelRuntimeProperties();
  }
  public String getContentType() {
   return new String("application/xls");
  } 
	
  public InputStream getInputStream() {
	try {
	this.conn = DriverManager.getConnection("jdbc:poolman://oracle");
	JasperPrint jasperPrint = JasperFillManager.fillReport(PropertiesManager.getProperty("org.wager.report.ReportLocation")+"/"+filename,
         parameters, conn);
	ByteArrayOutputStream byteArrayOutputStream
    = new ByteArrayOutputStream();
       // this.bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        JRCsvExporter exporterCSV = new JRCsvExporter();
        exporterCSV.setParameter(JRXlsExporterParameter.JASPER_PRINT,
                jasperPrint);
        exporterCSV.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
                byteArrayOutputStream);
        exporterCSV.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, new Boolean(true));
        exporterCSV.exportReport();

	conn.close();
	return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}
	catch(Exception e) {
	e.printStackTrace();
	return null;
	}
	}
  public String getName() {
	return new String("report.csv");
  }
  public java.util.Map getHeaders() {
	Map headers = new HashMap();
	headers.put ("Content-Disposition", "attachment; filename=\"Report.csv\"");
	return headers;
  }
  public void downloadData(OutputStream o) {
  }
  /** 
   *  Process layout-level events coming from the portal.
   *  Satisfies implementation of IChannel Interface.
   *
   *  @param <b>PortalEvent</b> ev a portal layout event
   */
  public void receiveEvent(PortalEvent ev) {
      if (ev.getEventNumber() == PortalEvent.ABOUT_BUTTON_EVENT) {
      }
  }
  
  /** 
   *  Receive static channel data from the portal.
   *  Satisfies implementation of IChannel Interface.
   *
   *  @param <b>ChannelStaticData</b> sd static channel data
   */
  public void setStaticData(ChannelStaticData sd) {
	  this.staticData = sd;
      
      this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
	  
  }
  
  /** 
   *  Receive channel runtime data from the portal.
   *  Satisfies implementation of IChannel Interface.
   *
   *  @param <b>ChannelRuntimeData</b> rd handle to channel runtime data
   */
  public void setRuntimeData(ChannelRuntimeData rd) {
    // Most of the processing is usually done here.
    this.runtimeData = rd;
    // process the form submissions
    if (runtimeData.getParameter("submit") != null) {
	Enumeration e =  runtimeData.getParameterNames();
	System.err.println("Submitting");
	try {
	if (runtimeData.getParameter("filename") != null) {
	filename = runtimeData.getParameter("filename");
	submitting=true;
	parameters = parseParameters(rd,e);
	}
	else throw new Exception();
	
	this.error = "";
	}
	catch(ParseException pe) {
	this.error = "Please enter a valid date.";
	pe.printStackTrace();
	submitting=false;
	}
	catch(Exception ex) {
		this.error = "Couldn't file Jasper Reports template file!";
		ex.printStackTrace();
	}

	
    }
	else {
	 submitting = false;
	}
  
    if (runtimeData.getParameter("report") != null) {
        report_num = runtimeData.getParameter("report");
	System.err.println("***** Got Report " + report_num); 
    }
	else report_num=new String(); 

    if (runtimeData.getParameter("clear") != null) {
        name_prev = "";
    }

    if (runtimeData.getParameter("back") != null) {
    }
  }

   Map parseParameters(ChannelRuntimeData c,Enumeration e) throws ParseException{
	String paramName;
	java.util.Date tmpdate;
	int delim;
	Map parameters = new HashMap();
	String day,month,year;
	ArrayList a = Collections.list(e);
	Object[] list = (Object[]) a.toArray();
	Arrays.sort(list);
	for (int i = 0 ; i < list.length; i++) {
		delim = list[i].toString().indexOf("_Day");
		paramName = list[i].toString();
		System.err.println("String was: " + list[i] + ", index was: " + delim + ", " + c.getParameter(paramName) );
	if (delim != -1) {
		day = c.getParameter(paramName);
		month = c.getParameter(paramName.substring(0,delim).concat("_Month"));
		year = c.getParameter(paramName.toString().substring(0,delim).concat("_Year"));
		System.err.println("*********** found date " + day + "/" + month + "/" + year);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		tmpdate = df.parse(day + "/" + month + "/" + year);
		System.err.println("Put " + paramName.toString().substring(0,delim) + " as " + day + "/" + month + "/" + year);	
		parameters.put(paramName.substring(0,delim),day + "/" + month + "/" + year);
	}
	else {
	// Check for standard parameters
	if (paramName != "error" || paramName !="report" || paramName !="submit" || paramName.indexOf("_Month") == -1 || paramName.indexOf("_Year") == -1 || paramName !="LOGO_FILENAME") 
		parameters.put(paramName,c.getParameter(paramName).toString());
	
	if (paramName.equals("LOGO_FILENAME")) {
		System.err.println(PropertiesManager.getProperty("org.wager.report.ReportLocation")+"/"+c.getParameter(paramName));
		parameters.put(paramName,PropertiesManager.getProperty("org.wager.report.ReportLocation")+"/"+c.getParameter(paramName));
	}
	
		parameters.put("REPORTROOT",PropertiesManager.getProperty("org.wager.report.ReportLocation"));

	}	
 	}
	return parameters;
   }
  
  /** Output channel content to the portal
   *  @param out a sax document handler
   */
  public void renderXML(ContentHandler out) throws PortalException {
	      try {
	
        this.conn = DriverManager.getConnection("jdbc:poolman://oracle");
     //   Document test = DBQuery(conn,"SELECT * from REPORTS","REPORTS","REPORT",null);
       // test = DBQuery(conn,"SELECT * from REPORTS_PARAM","PARAMS","PARAM",test);
    //    createParamData(conn,test);
        Document d = ReportQuery(conn,report_num);
        System.err.println("GOT HERE");
        // load the PoolMan JDBC Driver
//       	//OracleXMLQuery qu = new OracleXMLQuery (
//                  conn, "select r.* from REPORTS r");
//	qu.setRowsetTag ("REPORTS");
//	
//	qu.setRowTag ("REPORT");
//	
//    String xml = qu.getXMLString();
//	qu = new OracleXMLQuery (
//                  conn, "select * from REPORTS_PARAM where REPORTKEY="+report_num);
//        qu.setRowsetTag ("REPORTPARMS");
//        qu.setRowTag ("PARAM");
//	String xml2 =  qu.getXMLString();
//	int startfrom = xml2.indexOf(">");
//	
//	xml = xml.concat(xml2.substring(startfrom+1));
//	int headerends = xml.indexOf(">")+1;
//	String xml3 = xml.substring(0,headerends).concat("\n<REPORTANDPARAMS>\n").concat(xml.substring(headerends+1));
//	qu =  new OracleXMLQuery (
//                  conn, "select rv.* from PARAM_VALUES rv, REPORTS_PARAM rp where rv.reportparamkey = rp.reportparamkey and rp.reportkey = "+report_num);
//        qu.setRowsetTag ("PARAMVALUES");
//        qu.setRowTag ("PARAMVALUE");
//        String xml4 =  qu.getXMLString();
//        startfrom = xml4.indexOf(">");
//
//        xml4 = xml4.substring(startfrom+1);
//	xml3 = xml3.concat(xml4.concat(generateDateXML())).concat("\n</REPORTANDPARAMS>\n");
//	
//	
// Create a new XSLT styling engine
    XSLT xslt = new XSLT(this);
    String stylesheet="normal";
    // pass the result XML to the styling engine.
    xslt.setXML(d);
    // specify the stylesheet selector
    xslt.setXSL("CReports.ssl", stylesheet, runtimeData.getBrowserInfo());
    
    // set parameters that the stylesheet needs.
   	xslt.setStylesheetParameter("error",error); 
	xslt.setStylesheetParameter("baseActionURL",
                                    runtimeData.getBaseActionURL());
    xslt.setStylesheetParameter("name_prev", name_prev);
	xslt.setStylesheetParameter("report_num", report_num);
    
if (submitting)	
        xslt.setStylesheetParameter("baseWorkerURL", runtimeData.getBaseWorkerURL(UPFileSpec.FILE_DOWNLOAD_WORKER, true));
	
    // set the output Handler for the output.
    xslt.setTarget(out);
    //System.err.println(xml3);
    // do the deed
    xslt.transform();
	conn.close();
	}
	catch (Exception e) {
	e.printStackTrace();
        }
  }
 
  boolean checkPermission(AuthToken authtoken, int intStudyID, String privilege) {
	  if (intStudyID == -1 && privilege == null) return false;
	  
	  try {
	  return (authtoken.getStudyList().contains(new Integer(intStudyID)) || authToken.hasActivity(privilege));
	  }
	  catch (SecurityException se) {
		  return false;
	  }
	  
  }
  
  
  Document ReportQuery(Connection conn, String reportKey) {
	  Document document = null;
	  System.err.println("STARTING REPORTQUERY");
	  try {
		   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		   DocumentBuilder builder = factory.newDocumentBuilder();
	       document = builder.newDocument(); 
	       Statement stmt = conn.createStatement();
	       ResultSet rs;
	       Vector studyList = authToken.getStudyList();
	       String inStr = StudyUtilities.getStudyIDSQLString(studyList);
	       
	       if (reportKey == null || reportKey.equals("")) { //Only want list of reports here.
	    	    rs = stmt.executeQuery("SELECT * from REPORTS order by reportkey ");
	       }
	       else {
	    	   PreparedStatement ps = conn.prepareStatement("SELECT r.*,p.*,v.value from REPORTS r, REPORTS_PARAM p, PARAM_VALUES v where r.reportkey = p.reportkey(+) and p.reportparamkey =v.reportparamkey(+)  order by r.reportkey,p.reportparamkey");
	    	   rs = ps.executeQuery();
	       }
	       
	       
	       ResultSetMetaData rsmd = rs.getMetaData();
		   int numberOfColumns = rsmd.getColumnCount();
		   String columns[] = new String[numberOfColumns];
		   String tables[] = new String[numberOfColumns];
		   for (int i =0; i < numberOfColumns; i++) {
			   columns[i] = rsmd.getColumnName(i+1);
			   if (i < 9)
			   tables[i] = "REPORTS";
			   else if (i >= 9 && i < 14 )
				   tables[i] = "REPORTS_PARAM";
			   else
				   tables[i] = "PARAM_VALUES";
				   
			   System.err.println("Column "+columns[i]+", Table "+tables[i]);
		   }
	       Node reports =(Element) document.createElement("REPORTS");
	       document.appendChild(reports);
	       Node report = null;
	       Node param = null;
	       String currentReportKey = null;
	       String currentParamKey = null;
	       while (rs.next() ) {
	    	   int intStudyKey =  rs.getInt("STUDYKEY");
	    	   String privilege = rs.getString("PRIVILEGE");
	    	   System.err.println("Loaded report with studykey: " + intStudyKey + ", privilege: "+ privilege);
	    	   if (checkPermission(authToken,intStudyKey,privilege)) {	    	   
	    	   if (currentReportKey == null || !rs.getString("REPORTKEY").equals(currentReportKey) ) {
	    		   currentReportKey = rs.getString("REPORTKEY");
	    		   System.err.println("Got ReportKEY = "+currentReportKey);
	    		   report = (Element) document.createElement("REPORT");
	    		   reports.appendChild(report);
	    		   for (int i =0; i < columns.length; i++) {
	    			   if (tables[i].equals("REPORTS")) {
	    				   
	    				   Node column = (Element) document.createElement(columns[i]);
	    				   report.appendChild(column);
	    				   column.appendChild((Node) document.createTextNode(rs.getString(columns[i])==null ? "" : rs.getString(columns[i])));
	    			   }  			   
	    		   }
	    	   }
	    	   if (rs.getString("REPORTKEY").equals(reportKey)) {
	    		   if (!reportKey.equals("") && (currentParamKey == null || !rs.getString("REPORTPARAMKEY").equals(currentParamKey))) {
	    			   currentParamKey = rs.getString("REPORTPARAMKEY");
	    			   param = (Element) document.createElement("PARAM");
	    			   report.appendChild(param);
	    			   
	    			   for (int i =0; i < columns.length; i++) {
	    				   if (tables[i].equals("REPORTS_PARAM") && !columns[i].equals("REPORTPARAMKEY") && !columns[i].equals("REPORTKEY")) {
	    					   Node column = (Element) document.createElement(columns[i]);
	    					   param.appendChild(column);
	    					   column.appendChild((Node) document.createTextNode(rs.getString(columns[i])==null ? "" : rs.getString(columns[i])));
	    				   }  			   
	    			   }
	    			   
	    		   }
	    		   if (rs.getString("REPORTKEY").equals(reportKey)) {
	    			   //Check for reports with no parameters
	    			   if (rs.getString("PARAMTYPE") != null) {
	    			   if (rs.getString("PARAMTYPE").equals("DATE")) {
	    				   param.appendChild(generateDateXML(document,rs.getString("PARAMNAME")));
	    			   }
   				    else if (rs.getString("PARAMTYPE").equals("STUDY")) {
					   generateStudyXML(document,param,rs.getString("PARAMNAME"));
				
				   }
	    			   else {
	    				   Node column =(Element) document.createElement("VALUE");
	    				   param.appendChild(column);
	    				   if  (rs.getString("PARAMTYPE").equals("DROPDOWN")) {
	    					   String value = (String) parameters.get(rs.getString("PARAMNAME"));
	    					   if (value != null) {
	    					   if (value.equals(rs.getString("VALUE"))) {
	    						   NamedNodeMap attr = column.getAttributes();
	    						   Attr selected = document.createAttribute("selected");
	    						   selected.setValue("1");
	    						   attr.setNamedItem(selected);
	    					   }
	    					   }
	    				   }
	    				   
	    			   
	    			   
	    			   column.appendChild((Node) document.createTextNode(rs.getString("VALUE")==null ? "" : rs.getString("VALUE")));
	    			   }
	    			   }
	    		   }
	    	   }
	    	   }
	       }
	    	   //row = (Element) document.createElement(rowName);
	    	   //rowset.appendChild(row);
	       
	    	   
		   
		   
	  
	   Result result = new StreamResult(System.err);
	   Transformer xformer = TransformerFactory.newInstance().newTransformer();
	   Source source = new DOMSource(document);
	   xformer.transform(source, result);
	   return document;
	  }catch (SQLException s) {
		  
		   s.printStackTrace();
		   return null;
	   }catch (ParserConfigurationException pe) {
		  
		   pe.printStackTrace();
		   return null;
	   }catch (TransformerException te) {
		   te.printStackTrace();
		   return null;
		   
	   }
	  
  }
  
  
   /** Returns a DOM Document with the results of the DB query converted to XML.
 * @param conn Current DB Connection
 * @param doc Existing DOM document.
 * @return Updated DOM Document object.
 */
Document DBQuery(Connection conn, String query, String rowsetName, String rowName) {
	   System.err.println("Starting DBQUERY **************");
	   Document document = null;
	   String columnval;
	   String columns[];
	   String tables[];
	   try {
	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	   DocumentBuilder builder = factory.newDocumentBuilder();
       document = builder.newDocument(); 
	   Statement stmt = conn.createStatement();
	   ResultSet rs = stmt.executeQuery(query);
	   // get Result Set metadata
	   ResultSetMetaData rsmd = rs.getMetaData();
	   int numberOfColumns = rsmd.getColumnCount();
	   columns = new String[numberOfColumns];
	   tables = new String[numberOfColumns];
	   for (int i =0; i < numberOfColumns; i++) {
		   columns[i] = rsmd.getColumnName(i+1);
		   tables[i] = rsmd.getTableName(i+1);
	   }
	   // Set the first element of the document
	   Element rowset = (Element) document.createElement(rowsetName);
	   document.appendChild(rowset);
	   Element row;
	   Element rowcol;
	   
	   String currenttab;
	   while (rs.next() ) {
	   row = (Element) document.createElement(rowName);
	   rowset.appendChild(row);
	   if (columns.length > 0) {
		   rowcol=(Element) document.createElement(columns[0]);
		   currenttab = tables[0]; 
		   row.appendChild(rowcol);
		   for (int j=1; j < columns.length; j++) {
			   if (!tables[j].equals(currenttab))
				   rowcol.appendChild((Element) document.createElement(tables[j]));
			   
			   rowcol = (Element) document.createElement(columns[j]);
			   row.appendChild(rowcol);
			   columnval = rs.getString(j+1);
			   rowcol.appendChild((Node) document.createTextNode(columnval == null ? "" : columnval));
		   }
	   
	   }
	   
	   
	   
	   }
	   Result result = new StreamResult(System.err);
	   Transformer xformer = TransformerFactory.newInstance().newTransformer();
	   Source source = new DOMSource(document);
	   xformer.transform(source, result);
	   
	   }catch (SQLException s) {
		  
		   s.printStackTrace();
	   }catch (ParserConfigurationException pe) {
		  
		   pe.printStackTrace();
	   }catch (TransformerException te) {
		   te.printStackTrace();
		   
	   }
	   
	   return document;
   }

void createParamData(Connection conn, Document d) {
	//Document doc;
	 System.err.println("Starting CREATE_PARAM_DATA **************");
	NodeList nl = d.getElementsByTagName("PARAM");
	int numnodes = nl.getLength();
	System.err.println("Found " + numnodes + " parameters");
	Node currentNode;
	Node currentChildNode;
	String reportParamKey = new String("");
	Statement stmt;
	ResultSet rs;
	Element row;
	for (int i = 0 ; i < numnodes; i++) {
		currentNode = nl.item(i);
		System.out.println(nl.item(i).getNodeName());

		for (int j=0; j < currentNode.getChildNodes().getLength(); j++){
			currentChildNode = currentNode.getChildNodes().item(j);
			System.err.println(currentChildNode.getNodeName());
			if (currentChildNode.getNodeName().equals("REPORTPARAMKEY")) {
				reportParamKey = currentChildNode.getFirstChild().getNodeValue();
				System.err.println("Found report param key: " + reportParamKey);
			
			try{
			 stmt = conn.createStatement();
			    rs = stmt.executeQuery("SELECT VALUE from PARAM_VALUES where REPORTPARAMKEY='" + reportParamKey+"'");
			    while (rs.next() ) {
			 	   row = (Element) d.createElement("VALUE");
			 	   row.appendChild((Node) d.createTextNode(rs.getString(1)==null ? "" : rs.getString(1)));  
			 	   currentNode.appendChild(row);
			 	   }
			    
			}catch(SQLException se) {se.printStackTrace();}
			}
		}
		
		
	}
	try{
		Result result = new StreamResult(System.err);
		   Transformer xformer = TransformerFactory.newInstance().newTransformer();
		   Source source = new DOMSource(d);
		   xformer.transform(source, result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	
	
}

	void  generateStudyXML(Document d,Node n, String param_name) {
	
	// Default to today's date
	String dtstring = (String) parameters.get(param_name);
	if (dtstring != null) {
	}
	else dtstring = new String("");
	Statement stmt;
        ResultSet rs;
        
	Element fields = d.createElement(param_name);
	 try{
                         stmt = conn.createStatement();
                         Vector studyList = authToken.getStudyList();
              	       String inStr = StudyUtilities.getStudyIDSQLString(studyList);
                            rs = stmt.executeQuery("SELECT STUDYNAME from zeus.study where studykey in "+inStr);  
                            while (rs.next() ) {
					Element row = d.createElement("VALUE");
					System.err.println(rs.getString(1));	
                                   row.appendChild((Node) d.createTextNode(rs.getString(1)==null ? "" : rs.getString(1)));
					n.appendChild(row);
					if (dtstring.equals(rs.getString(1))) {
					 NamedNodeMap attr = row.getAttributes();
			                  Attr selected = d.createAttribute("selected");
                 			 selected.setValue("1");
                  			attr.setNamedItem(selected);
					}
                                   }

                        }catch(SQLException se) {se.printStackTrace();}


	}	


	Element  generateDateXML(Document d,String param_name) {
	
	// Default to today's date
	java.util.Date dt = new java.util.Date();
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(dt);
	String dtstring = (String) parameters.get(param_name);
	if (dtstring != null) {
		try {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		java.util.Date date = df.parse(dtstring);
		if (date != null){
		c.setTime(date);
		}
		}catch (ParseException pe) {
			pe.printStackTrace();
		}
	}
	 int day = (c.get(Calendar.DAY_OF_MONTH));
	int month = (c.get(Calendar.MONTH));
	int year = (c.get(Calendar.YEAR));
	
	Element fields = d.createElement("VALUE");
        for (int i = 1 ; i < 32; i++) {
        	Element days = d.createElement("DAY");
        	fields.appendChild(days);
           days.appendChild((Node) d.createTextNode(""+i));
	  if (i==day) {
		  NamedNodeMap attr = days.getAttributes();
		  Attr selected = d.createAttribute("selected");
		  selected.setValue("1");
		  attr.setNamedItem(selected);
	  }
	}
        for (int i=1 ; i< 13; i++) {
           Element months = d.createElement("MONTH");
       	fields.appendChild(months);
        months.appendChild((Node) d.createTextNode(""+i));
	  if (i==month+1) {
		  NamedNodeMap attr = months.getAttributes();
		  Attr selected = d.createAttribute("selected");
		  selected.setValue("1");
		  attr.setNamedItem(selected);
	  }
	}
        Element years = d.createElement("YEAR");
    	fields.appendChild(years);
       years.appendChild((Node) d.createTextNode(""+year));
	
	return fields;

	}

}
