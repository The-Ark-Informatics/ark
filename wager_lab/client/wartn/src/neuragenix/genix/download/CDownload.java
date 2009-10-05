/*
 * CDownload.java
 *
 * Created on January 28, 2004, 4:16 PM
 */
package neuragenix.genix.download;



import java.util.Map;
import java.util.Hashtable;
import java.util.*;
import java.io.*;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.IMultithreadedMimeResponse;
import org.jasig.portal.PortalException;
import org.jasig.portal.UPFileSpec;
import org.jasig.portal.security.IPerson;
import org.jasig.portal.services.PersonDirectory;
import org.jasig.portal.utils.DocumentFactory;
import org.jasig.portal.utils.XSLT;
import org.jasig.portal.PropertiesManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;
import javax.activation.MimetypesFileTypeMap;


import neuragenix.security.*;
import neuragenix.dao.*;
import java.sql.*;
/**
 *
 * @author  longtran
 */
public class CDownload extends org.jasig.portal.channels.BaseMultithreadedChannel implements IMultithreadedMimeResponse {
    
    ChannelStaticData staticData;
    ChannelRuntimeData runtimeData;
    String strStylesheet = null;
    StringBuffer strXML;
    
    String domain;
    String primaryField;
    String primaryValue;
    String propertyName;
	String fileNameField; 

    String fileName = "temp.doc";
    String activity;
    
    /** Creates a new instance of CDownload */
    public CDownload() {
        
          
    }
    //private static final String sslLocation = "CPersonAttributes/CPersonAttributes.ssl";

  public void renderXML (ContentHandler out, String uid) throws PortalException {

		strXML = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
      
      	ChannelState channelState = (ChannelState)channelStateMap.get(uid);
      	staticData = channelState.getStaticData();
      	runtimeData = channelState.getRuntimeData();
//        System.err.println("---------in renderXML CDownload-------------");
/*        for(Enumeration e = runtimeData.keys();e.hasMoreElements();)
        {
            String key = (String)e.nextElement();
            System.err.println("----------key n value------" + key +" : " + runtimeData.getParameter(key));
        }*/
        AuthToken authToken = (AuthToken)staticData.getPerson().getAttribute("AuthToken");
     	
		// reconstruct the parameter

		String param = runtimeData.getBaseWorkerURL(UPFileSpec.FILE_DOWNLOAD_WORKER,true) + "?";
		if( runtimeData.getParameter( "domain" ) != null )
			param += "domain="+ runtimeData.getParameter( "domain" );		
		if( runtimeData.getParameter( "primary_field" ) != null )
            param += "&amp;primary_field=" + runtimeData.getParameter( "primary_field" );
		if( runtimeData.getParameter( "primary_value" ) != null )
            param += "&amp;primary_value="+ runtimeData.getParameter( "primary_value" );
		if( runtimeData.getParameter( "property_name" ) != null )
            param += "&amp;property_name="+ runtimeData.getParameter( "property_name" );
		if( runtimeData.getParameter( "file_name_field" ) != null )
            param += "&amp;file_name_field="+ runtimeData.getParameter( "file_name_field" );
		if( runtimeData.getParameter( "file_name" ) != null )
            param += "&amp;file_name="+ runtimeData.getParameter( "file_name" );
		if( runtimeData.getParameter( "activity_required" ) != null )
            param += "&amp;activity_required="+ runtimeData.getParameter( "activity_required" );
//            System.err.println("-------param----------" + param);

				strXML.append("<info><downloadURL>" + param + "</downloadURL><detail>" + authToken.getSessionUniqueID() + "</detail></info>"); 
    
       	
        XSLT xslt = new XSLT(this);
        
        // pass the result xml to the styling engine.
        
//    	System.err.println("STRING XML: " + strXML.toString()); 
        xslt.setXML(strXML.toString());
        
        // specify the stylesheet selector
        
        xslt.setXSL("CDownload.ssl", strStylesheet, runtimeData.getBrowserInfo());
        
        // set parameters that the stylesheet needs.
        
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());
        
        // set the output Handler for the output.
        
        xslt.setTarget(out);
 
        // do the deed
        
        xslt.transform();
    
  
  }

  
  
    /**
     * Returns the MIME type of the content.  
     */
    public java.lang.String getContentType (String uid) {

        ChannelState channelState = (ChannelState)channelStateMap.get(uid);
        
        ChannelRuntimeData runtimeData = channelState.getRuntimeData();
        
        
        /*fileName = runtimeData.getParameter( "file_name" );*/
        
//	System.err.println( "int getContentType, filename is: " + fileName );
	if( fileName != null ){
            
            MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();
            // adding type pdf.
            mimeType.addMimeTypes("application/x-pdf pdf");
        
            
            return mimeType.getContentType( fileName );
            
        }else{
         
            return null;
        }

    }

    /**
     * Returns the MIME content in the form of an input stream.
     * Returns null if the code needs the OutputStream object
     */
    public java.io.InputStream getInputStream (String uid) throws IOException {
    
        // create domains & formfields for this channel
 
        ChannelState channelState = (ChannelState)channelStateMap.get(uid);
        ChannelStaticData staticData = channelState.getStaticData();
        //ChannelRuntimeData runtimeData = channelState.getRuntimeData();
        
        AuthToken authToken = (AuthToken)staticData.getPerson().getAttribute("AuthToken");
        
        domain = runtimeData.getParameter( "domain" );
        primaryField = runtimeData.getParameter( "primary_field" );
        primaryValue = runtimeData.getParameter( "primary_value" );
        propertyName = runtimeData.getParameter( "property_name" );
    	fileNameField = runtimeData.getParameter( "file_name_field" );    
  
	fileName = runtimeData.getParameter( "file_name" );
        activity = runtimeData.getParameter( "activity_required" );
       
        
        
        try{
        
            if(( authToken != null )) {
                
                // download export file
                if ( (domain == null) ){
            
                    
                    String fileLoc = PropertiesManager.getProperty( propertyName );
                    if( fileName.startsWith( authToken.getSessionUniqueID() ) || activity.equals("do_isearch")  ){

                        java.io.InputStream is;
                        is = (java.io.InputStream) new java.io.FileInputStream( fileLoc + "/" + fileName );

                        return is;
                        
                    }
                    
                    return null;
                
                    //download uploaded file
                }else{
                    
		
                    if( (primaryField != null) && (primaryValue != null) && (propertyName != null) && ( fileNameField != null) ){
                        
                        //create a query to get the file name
                        DALSecurityQuery sQuery = new DALSecurityQuery( activity, authToken );

                        sQuery.setDomain( domain, null, null, null ); 
                        sQuery.setField( fileNameField, null );
                        sQuery.setWhere( null, 0, primaryField, "=", primaryValue, 0, DALQuery.WHERE_HAS_VALUE );
						
						// if the is result
                        ResultSet rs = sQuery.executeSelect();
                        if ( rs.next() ){
                            fileName = rs.getString( fileNameField );
		
                            if( fileName != null ){ 
                       
                                String fileLoc = PropertiesManager.getProperty( propertyName );
                                java.io.InputStream is;
                                is = (java.io.InputStream) new java.io.FileInputStream( fileLoc + "/" + fileName );
                
                                return is;
                            }
                        }

         
                        
                    }
                    return null;
                    
  			                     
                    
                }
        
            }else{
                
                String error = "Access denied";
                
                return (java.io.InputStream) new java.io.StringBufferInputStream( error );
            
            }
            
        }catch( Exception e ){
            
           e.printStackTrace();
            
           LogService.instance().log(LogService.ERROR, "Error occurred when trying to authenticate user for accessing download files - " + e.toString()); 
           
        }
        return null;
    }

    /**
     * Pass the OutputStream object to the download code if it needs special handling
     * (like outputting a Zip file).  Unimplemented.
     */
    public void downloadData (OutputStream out, String uid) throws IOException {
    }

    /**
     * Returns the name of the MIME file.
     */
    public java.lang.String getName (String uid) {
        // As noted above the only attribute we support right now is "image/jpeg" for
        // the jpegPhoto attribute.
        
        //ChannelState channelState = (ChannelState)channelStateMap.get(uid);
        //ChannelStaticData staticData = channelState.getStaticData();
        //ChannelRuntimeData runtimeData = channelState.getRuntimeData();
        //String payloadName;
        //if ("jpegPhoto".equals(runtimeData.getParameter("attribute")))
        //    payloadName = "image.jpg";  
        //else    
         //   payloadName = "unknown";
        //return payloadName;
        return "download.file";
    }

    /**
     * Returns a list of header values that can be set in the HttpResponse.
     * Returns null if no headers need to be set.
     */
    public Map getHeaders (String uid) {
        //content-disposition, inline;filename=....;
        ChannelState channelState = (ChannelState)channelStateMap.get(uid);
        ChannelStaticData staticData = channelState.getStaticData();
        //ChannelRuntimeData runtimeData = channelState.getRuntimeData();
        for(Enumeration e= runtimeData.keys();e.hasMoreElements();)
        {
            String key= (String)e.nextElement();
//            System.err.println("------------key and value----------" + key + ": " + runtimeData.getParameter(key));
        }
        AuthToken authToken = (AuthToken)staticData.getPerson().getAttribute("AuthToken");
        
        domain = runtimeData.getParameter( "domain" );
        primaryField = runtimeData.getParameter( "primary_field" );
        primaryValue = runtimeData.getParameter( "primary_value" );
        propertyName = runtimeData.getParameter( "property_name" );
    	fileNameField = runtimeData.getParameter( "file_name_field" );    
        
        fileName = runtimeData.getParameter( "file_name" );
        
        
//       System.err.println( "WHAT I SEE: " + runtimeData.getParameter( "property_name" ) + "===" +
        runtimeData.getParameter( "file_name_field" );
        
        
        try{
        
            if(( authToken != null )) {
                
                // download export file
                if ( (domain != null) )
                {
//                    System.err.println("------------in domain != null-------------");
                    if( (primaryField != null) && (primaryValue != null) && (propertyName != null) && ( fileNameField != null) ){
                        
						//create a query to get the file name
                        DALQuery sQuery = new DALQuery();

                        sQuery.setDomain( domain, null, null, null ); 
                        sQuery.setField( fileNameField, null );
                        sQuery.setWhere( null, 0, primaryField, "=", primaryValue, 0, DALQuery.WHERE_HAS_VALUE );
						
						// if the is result
                        ResultSet rs = sQuery.executeSelect();
                        if ( rs.next() ){
                            fileName = rs.getString( fileNameField );
		
                        }
                     
                    }
                }
        
            }
        }catch( Exception e ){
            
           e.printStackTrace();
            
           LogService.instance().log(LogService.ERROR, "Error occurred when trying to authenticate user for accessing download files - " + e.toString()); 
           
        }
        
        MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();
            // adding type pdf.
        mimeType.addMimeTypes("application/pdf pdf");
        
        Hashtable hd = new Hashtable();
//        System.err.println("-------------filename in getHeaders---------" + fileName);
	if( (runtimeData.getParameter( "custom_filename" ) != null) &&  (runtimeData.getParameter( "custom_filename" ).length() > 0 ) ){
		int li = fileName.lastIndexOf('.');
		if( li > 0 ){
			fileName =  runtimeData.getParameter( "custom_filename" ) + fileName.substring( li, fileName.length() );
		}
	}
	fileName = fileName.replaceAll( " ", "_" );
	
        hd.put( "Content-type", mimeType.getContentType( fileName ) );
        hd.put( "Content-Disposition", "attachment; filename=" + fileName );
        //hd.put( "Content-Disposition", "attachment; filename=" + fileName );
        
        
        return hd;
    }

    
    
}
