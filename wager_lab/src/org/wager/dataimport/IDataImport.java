package org.wager.dataimport;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.IChannel;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.xml.sax.ContentHandler;
import org.jasig.portal.utils.XSLT;

public class IDataImport implements IChannel {
	
	 private ChannelRuntimeData runtimeData;
	  private static final String START="start";
	  private static final String UPLOAD_NANODROP="upload_nanodrop";
	  private static final String UPLOAD_BATCH="upload_batch";
	  private static final String VERIFY="verify";
	  private static final String VERIFY_BATCH="verify_batch";
	  private static final String CONFIRM="confirm";
	  private static final String CONFIRM_BATCH="confirm_batch";
	  private static final String SUCCESS="success";
	  private static final int MODE_NANODROP = 0;
	  private static final int MODE_ALIQUOT = 1;
	  private String strStylesheet;
	  private StringBuffer strXML;	
	  

	@Override
	public void receiveEvent(PortalEvent arg0) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void setStaticData(ChannelStaticData arg0) throws PortalException {
		// TODO Auto-generated method stub

	}

	public ChannelRuntimeProperties getRuntimeProperties() {
		// TODO Auto-generated method stub
		return new ChannelRuntimeProperties();
	}

	
	
	@Override
	public void renderXML(ContentHandler out) throws PortalException {
		// TODO Auto-generated method stub
		try {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<nanodrop>\n" + strXML.toString() +  "</nanodrop>";  
		System.err.println(xml);

	    // Create a new XSLT styling engine
	    XSLT xslt = new XSLT(this);
	    
	    // pass the result XML to the styling engine.
	    xslt.setXML(xml);
	    
	    // specify the stylesheet selector
	    
	    xslt.setXSL("CNanoDrop.ssl", this.strStylesheet, runtimeData.getBrowserInfo());
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
		  this.strStylesheet=START;
		  System.err.println("Action is ::::: " + action);
		  if (action == null) // Show upload screen
			  this.strXML.append(doStart());
		  else if(action.equals(UPLOAD_NANODROP)) {
			  this.strXML.append(doUpload(rd,MODE_NANODROP));
				this.strStylesheet = VERIFY;
				strXML.append("<mode>NANODROP</mode>");
		  }
		  else if(action.equals(UPLOAD_BATCH)) {
			  this.strXML.append(doUpload(rd,MODE_ALIQUOT));
				this.strStylesheet = VERIFY;
				strXML.append("<mode>BATCH</mode>");
		  }
		  else if(action.equals(VERIFY_BATCH)) {
			  doVerify(rd,MODE_ALIQUOT);
			  this.strStylesheet = CONFIRM;
			  strXML.append("<mode>BATCH</mode>");
		  }
		  else if(action.equals(VERIFY)) {
			  doVerify(rd,MODE_NANODROP);
			  this.strStylesheet = CONFIRM;
			  strXML.append("<mode>NANODROP</mode>");
		  }
		  else if(action.equals(CONFIRM)) {
			  doConfirm(rd,MODE_NANODROP);
			  strXML.append("<mode>NANODROP</mode>");
			  this.strStylesheet = SUCCESS;
		  }
		  else if(action.equals(CONFIRM_BATCH)) {
			  doConfirm(rd,MODE_ALIQUOT);
			  strXML.append("<mode>BATCH</mode>");
			  this.strStylesheet = SUCCESS;
		  }
		  
	}
	
	private String doStart() {
		this.strStylesheet=START;
		return "";
	}
	
	private String doUpload(ChannelRuntimeData rd, int mode) {
	 return null;
	}

	private void doVerify(ChannelRuntimeData rd, int mode) {

	}
	
	private void doConfirm(ChannelRuntimeData rd, int mode) {
		
	}
	
	
}
