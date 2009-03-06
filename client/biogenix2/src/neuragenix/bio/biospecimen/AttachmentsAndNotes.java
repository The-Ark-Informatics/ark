/*
 * AttachmentsAndNotes.java
 *
 * Module to handle attachments and notes for biospecimens
 *
 *
 *
 * Created on 31 March 2005, 00:42
 *
 *  Author ; Daniel Murley
 *  Email : dmurley@neuragenix.com
 *
 * Copyright (C) Neuragenix Pty Ltd 2005
 * $Id: AttachmentsAndNotes.java,v 1.7 2005/10/10 02:19:45 ple Exp $
 *
 */



package neuragenix.bio.biospecimen;


import org.jasig.portal.IChannel;

import org.jasig.portal.PropertiesManager;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XSLT;
import org.xml.sax.ContentHandler;

import org.jasig.portal.security.*;
import org.jasig.portal.services.LogService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;


// java classes
//import java.util.*;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

// neuragenix classes
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.security.*;
import neuragenix.utils.*;
/**
 *
 * @author  dmurley
 */

/*
 *TODO: Remove any irrelevent runtimeData passing
 *
 *
 *
 *
 */
public class AttachmentsAndNotes {
    private AuthToken authToken = null;
    
    private LockRequest lock;
    
    private static final String BIOSPECIMEN_ATTACHMENTS = "attachments";
    private static final String FILE_SIZE_ERROR = "The file you are trying to upload has invalid size. Please try another file.";
    private static final String LOCK_ERROR = "The record you are trying to update or delete is being viewed by others. Please try again later.";
    
    public static final String PERMISSION_BIOSPECIMEN_ATTACHMENTS_VIEW = "biospecimen_attachments";
    public static final String PERMISSION_BIOSPECIMEN_ATTACHMENTS_ADD = "biospecimen_attachments_add";
    public static final String PERMISSION_BIOSPECIMEN_ATTACHMENTS_DELETE = "biospecimen_attachments_delete";
    
    public static final String PERMISSION_BIOSPECIMEN_NOTES_VIEW = "biospecimen_notes";
    public static final String PERMISSION_BIOSPECIMEN_NOTES_ADD = "biospecimen_notes_add";
    public static final String PERMISSION_BIOSPECIMEN_NOTES_DELETE = "biospecimen_notes_delete";
    
    
    private static String strStylesheet;
    
    /** Creates a new instance of AttachmentsAndNotes */
    public AttachmentsAndNotes(AuthToken authToken) {
        this.authToken = authToken;
    }
    
    public static String buildViewNotesXML(ChannelRuntimeData runtimeData){
    		StringBuffer strXML = new StringBuffer();
    		String strInternalBiospecimenID = null;
            strInternalBiospecimenID =runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
            if (strInternalBiospecimenID == null){
                System.err.println("Invalid Internal Biospecimen ID");
                return null;
            }
            String strError = null;
            
            try {
                
                if( strInternalBiospecimenID != null ){
                    
                    Vector vtViewBiospecimenNotesFormFields = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen_notes");
                    DALSecurityQuery query = new DALSecurityQuery();
                    query.setCaseSensitive(false);    
                    
                    //get the values and form fields vector
                    query.setDomain("BIOSPECIMEN", null, null, null);
                    query.setDomain("NOTES","BIOSPECIMEN_intBiospecimenID","NOTES_intID","LEFT JOIN");
                    query.setFields(vtViewBiospecimenNotesFormFields, null);
                    
                    query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"NOTES_strDomain","=","BIOSPECIMEN",0,DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND",0,"NOTES_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
                    
                    ResultSet rsResultSet = query.executeSelect();
                    
                    
                    strXML.append( QueryChannel.buildSearchXMLFile("notes", rsResultSet, vtViewBiospecimenNotesFormFields));
                    
                    rsResultSet.close();
                    
                    strXML.append(QueryChannel.buildFormLabelXMLFile(vtViewBiospecimenNotesFormFields));
                    
                    if (strError != null)
                        strXML.append( "<error>" + strError + "</error>" );
                    
                    return "<biospecimen_notes>" + strXML.toString() + "</biospecimen_notes>";
                  
                }
                
            }
            catch (Exception e) {
                LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
                e.printStackTrace();
            }
            
            return "<biospecimen_notes>" + strXML.toString() + "</biospecimen_notes>";
    }
    
    
    
    
    
    
    /** Return the XML values for viewing attachment
     *
     */
    public String buildViewAttachmentsXML(ChannelRuntimeData runtimeData){
        
        StringBuffer strXML = new StringBuffer();
        String strInternalBiospecimenID = null;
        strInternalBiospecimenID =runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
        if (strInternalBiospecimenID == null){
            System.err.println("Invalid Internal Biospecimen ID");
            return null;
        }
        String strError = null;
      
        try {
            
            if( strInternalBiospecimenID != null ){
                
                //TODO: seciruty and locking here
                
                //System.out.println("Getting into build view XML");
                //resetLock();
                
                Vector vtViewBiospecimenAttachmentFormFields = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen_attachments");
                DALSecurityQuery query = new DALSecurityQuery("biospecimen_attachments", authToken);
                query.setCaseSensitive(false);    
                if (query == null){
                    System.err.println("Query is NULL in buildViewXML");
                }
                //get the values and form fields vector
                query.setDomain("BIOSPECIMEN", null, null, null);
                query.setDomain("ATTACHMENTS","BIOSPECIMEN_intBiospecimenID","ATTACHMENTS_strID","LEFT JOIN");
                query.setFields(vtViewBiospecimenAttachmentFormFields, null);
                
                query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"ATTACHMENTS_domainName","=","BIOSPECIMEN",0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"ATTACHMENTS_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
                
                ResultSet rsResultSet = query.executeSelect();
                
                
                strXML.append( QueryChannel.buildSearchXMLFile("attachments", rsResultSet, vtViewBiospecimenAttachmentFormFields));
                
                rsResultSet.close();
                
                strXML.append(QueryChannel.buildFormLabelXMLFile(vtViewBiospecimenAttachmentFormFields));
                
                
                
                if (strError != null)
                    strXML.append( "<error>" + strError + "</error>" );
               
                return strXML.toString();
                  
            }
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
            e.printStackTrace();
        }
        
        return "<biospecimen>" + strXML.toString() + "</biospecimen>";
        
        
        
    }
    
    

    public String saveNote(ChannelRuntimeData runtimeData) {
		String strError = null;
		// TOD: Catch exception
		// if( lock.isValid() && lock.lockWrites() ){

		String strInternalBiospecimenID = null;
		strInternalBiospecimenID = runtimeData
				.getParameter("BIOSPECIMEN_intBiospecimenID");
		
		
		if (strInternalBiospecimenID == null) {
			System.err
					.println("Error when trying to save an attachment for empty BiospecimenID");
			return "Empty BiospecimenID provided when saving attachments";
		}

		try {
				runtimeData.setParameter("NOTES_intID",
						strInternalBiospecimenID);
				runtimeData.setParameter("NOTES_strDomain",
						"Biospecimen");
			
				java.util.Calendar currentTime = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				runtimeData.setParameter("NOTES_dtDate", formatter.format(currentTime.getTime()));
				
				DALSecurityQuery query = new DALSecurityQuery();
				query.setDomain("NOTES", null, null, null);
                                runtimeData.setParameter("NOTES_strAddedBy", authToken.getUserIdentifier());
                                
				query.setFields(	DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_notes"),runtimeData);
				query.executeInsert();
		}

		catch (Exception e) {
			System.err.println("Error when Saving new Note to biospecimen");
			e.printStackTrace();
		}

		return null;
	}

    
    public String deleteNote(ChannelRuntimeData runtimeData)
    {
    	
    	 	String strError = null;
         
    	 	try{
             
             DALSecurityQuery query = new DALSecurityQuery("biospecimen_attachments", authToken);
             query.setDomain("NOTES", null, null, null);
             query.setField("NOTES_intDeleted", "-1");
             
             query.setWhere( null, 0, "NOTES_intNotesKey", "=", runtimeData.getParameter("NOTES_intNotesKey"), 0,
             DALQuery.WHERE_HAS_VALUE );
             
             
           //  if( lock.isValid() && lock.lockWrites() ){
             query.executeUpdate();
           //  lock.unlockWrites();
             
             strError = "Note deleted.";
             
            //  }else{
            // strError = LOCK_ERROR;
            // }
         }
         catch(Exception e){
             System.err.println("Error when deleting files to server");
             e.printStackTrace();
         }
         return strError;
         
    		
    	
    }
    
    
    
    public String saveAttachment(ChannelRuntimeData runtimeData){
        
        String strError = null;
        //TOD: Catch exception
        //if( lock.isValid() && lock.lockWrites() ){
        
        String strInternalBiospecimenID = null;
        strInternalBiospecimenID =runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
        if (strInternalBiospecimenID == null){
            System.err.println("Error when trying to save an attachment for empty BiospecimenID");
            return "Empty BiospecimenID provided when saving attachments";
        }
        
        
        
        
        
        
        
        
        try {
            
            String fileName =QueryChannel.saveUploadFile( "ATTACHMENTS_strAttachmentsFileName",
            runtimeData, PropertiesManager.getProperty( "neuragenix.bio.patient.SaveAttachmentsLocation" ));
            
            if( fileName == null )
                
                strError = FILE_SIZE_ERROR;
            
            else {
                
                //strError = "File uploaded.";
                runtimeData.setParameter("ATTACHMENTS_strAttachedBy", authToken.getUserIdentifier() );
                runtimeData.setParameter("ATTACHMENTS_strAttachmentsFileName",fileName);
                runtimeData.setParameter("ATTACHMENTS_strID", strInternalBiospecimenID);
                runtimeData.setParameter("ATTACHMENTS_domainName","Biospecimen");
                DALSecurityQuery query = new DALSecurityQuery("patient_attachments", authToken);
                query.setDomain("ATTACHMENTS", null, null, null);
                query.setFields(DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_attachments"), runtimeData);
                query.executeInsert();
                
                
            }
        }
        
        catch (Exception e){
            System.err.println("Error when uploading files to server");
            e.printStackTrace();
        }
        
        return strError;
    }
    
    
    
    
    /** delete an attachment
     *
     */
    
    
    
    public String  deleteAttachment(ChannelRuntimeData runtimeData){
        
        String strError = null;
        try{
            
            DALSecurityQuery query = new DALSecurityQuery("biospecimen_attachments", authToken);
            query.setDomain("ATTACHMENTS", null, null, null);
            query.setField("ATTACHMENTS_intDeleted", "-1");
            
            query.setWhere( null, 0, "ATTACHMENTS_attachmentkey", "=", runtimeData.getParameter("ATTACHMENTS_intAttachmentKey"), 0,
            DALQuery.WHERE_HAS_VALUE );
            
            
          //  if( lock.isValid() && lock.lockWrites() ){
            query.executeUpdate();
          //  lock.unlockWrites();
            
            strError = "File deleted.";
            
           //  }else{
           // strError = LOCK_ERROR;
           // }
        }
        catch(Exception e){
            System.err.println("Error when deleting files to server");
            e.printStackTrace();
        }
        return strError;
        
        
    }
    
    
   
    
   
}
