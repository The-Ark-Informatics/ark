package org.wager.dataimport;

import java.io.IOException;

import neuragenix.security.AuthToken;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.IChannel;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XSLT;
import org.wager.biogenix.types.Biospecimen;
import org.xml.sax.ContentHandler;

public class CDataImport implements IChannel {

	private ChannelRuntimeData runtimeData;
	private ChannelStaticData staticData;
	private AuthToken authToken;
	  private static final String START="start";
	  private static final String UPLOAD_NANODROP="upload_nanodrop";
	  private static final String UPLOAD_BATCH="upload_batch";
	  private static final String UPLOAD="upload";
	  private static final String VERIFY="verify";
	  private static final String VERIFY_BATCH="verify_batch";
	  private static final String CONFIRM="confirm";
	  private static final String CONFIRM_BATCH="confirm_batch";
	  private static final String SUCCESS="success";
	  private static final int MODE_NANODROP = 0;
	  private static final int MODE_ALIQUOT = 1;
	  
	 
	  private static final int IDX_SAMPLEDATE = 1;
	  private static final int IDX_DNACONC = 3;
	  private static final int IDX_DNAVOL = 4;
	  private static final int IDX_TEVOL = 5;
	  IDataImportStrategy importStrategy;
	  private String strStylesheet;
	  private StringBuffer strXML;	
	  private int mode = -1;
	  public CDataImport() {
		  
	  }
	  
	@Override
	public ChannelRuntimeProperties getRuntimeProperties() {
		// TODO Auto-generated method stub
		return new ChannelRuntimeProperties();
	}

	@Override
	public void receiveEvent(PortalEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderXML(ContentHandler out) throws PortalException {
		// TODO Auto-generated method stub
		try {
			if (mode == MODE_NANODROP)
				strXML.append("<mode>NANODROP</mode>");
			else if (mode == MODE_ALIQUOT)
					strXML.append("<mode>ALIQUOT</mode>");
			
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<nanodrop>\n" + strXML.toString() +  "</nanodrop>";  
		System.err.println(xml);
		
	    // Create a new XSLT styling engine
	    XSLT xslt = new XSLT(this);
	    
	    // pass the result XML to the styling engine.
	    xslt.setXML(xml);
	    
	    // specify the stylesheet selector
	    
	    xslt.setXSL("CDataImport.ssl", this.strStylesheet, runtimeData.getBrowserInfo());
	    System.err.println("Stylesheet is "+ this.strStylesheet);
	    System.err.println("baseActionURL is "+ runtimeData.getBaseActionURL());
	    // set parameters that the stylesheet needs.
	    if (runtimeData.getBaseActionURL() != null) { 
	    xslt.setStylesheetParameter("baseActionURL",
	                                    runtimeData.getBaseActionURL());
	    }
	    
	    // set the output Handler for the output.
	    xslt.setTarget(out);
	    
	    // do the deed
	    xslt.transform();
	    System.err.println(out.toString());
	    strXML.setLength(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setRuntimeData(ChannelRuntimeData rd) throws PortalException {
		// TODO Auto-generated method stub
		  this.runtimeData = rd;
		  this.strXML = new StringBuffer();
		  String action = runtimeData.getParameter( "action" );
		  String strMode = runtimeData.getParameter("mode");
		  String separator = runtimeData.getParameter("separator");
		  this.strStylesheet=START;
		  System.err.println("Action is ::::: " + action);
		  if (action == null) // Show upload screen
			  this.strXML.append(doStart());
		  else if(action.equals("upload")) {
			  if (strMode != null && separator != null) {
			  org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter("ALIQUOT_strFilename");
			  if (strMode.equals("0"))
				  mode = MODE_NANODROP;
			  else
				  mode = MODE_ALIQUOT;
			  
				  this.importStrategy = ImportStrategyFactory.getInstance(mode);
				if (importStrategy == null){
					throw new PortalException();
					
				}
			  try {
				importStrategy.importData(fileToSave.getInputStream(),separator);
				 strXML.append(importStrategy.getOutput());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				this.strStylesheet = VERIFY;
				if (mode == MODE_NANODROP)
				strXML.append("<mode>NANODROP</mode>");
				else
					strXML.append("<mode>ALIQUOT</mode>");
			  }
		  }
		
		  else if(action.equals(VERIFY)) {
			  importStrategy.verify();
			  strXML.append(importStrategy.getOutput());
			  this.strStylesheet = CONFIRM;
		  }
		  else if(action.equals(CONFIRM)) {
			 importStrategy.process();
			 strXML.append(importStrategy.getOutput());
			  this.strStylesheet = SUCCESS;
		  }
		  
	}

	
	private String doStart() {
		this.strStylesheet=START;
		return "";
	}
	
	@Override
	public void setStaticData(ChannelStaticData sd) throws PortalException {
		// TODO Auto-generated method stub
	    this.staticData = sd;
	    this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");

	}
}
