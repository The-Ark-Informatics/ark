package org.wager.biogenix;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.IChannel;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.xml.sax.ContentHandler;
import java.io.*;
import org.apache.commons.csv.*;
import org.hibernate.cfg.*;
import org.hibernate.*;
import java.util.*;
import java.math.*;
import java.text.*;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.wager.biogenix.types.*;


import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.security.*;
import neuragenix.security.exception.*;
import org.wager.biogenix.exceptions.*;
import neuragenix.dao.*;



import org.jasig.portal.utils.XSLT;

public class CNanoDrop implements IChannel {
	
	String [][] importData;
	  private ChannelStaticData staticData;
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
	  
	  private static final int IDX_BIOSPECID = 0;
	  private static final int IDX_SAMPLEDATE = 1;
	  private static final int IDX_DNACONC = 3;
	  private static final int IDX_DNAVOL = 4;
	  private static final int IDX_TEVOL = 5;
	  
	  private String strStylesheet;
	  private StringBuffer strXML;	
	  AuthToken authToken;
	  SessionFactory sf;
	  List biospecsForUpdate;
	  LinkedList validDataRows;
	   HashMap bioMapping;
	   Session hib_session;
	    
	  
	  
	  public CNanoDrop() {
		  sf = new Configuration().configure().buildSessionFactory();
	   }
	  
	  
	 public void saveUploadFile( String filename, ChannelRuntimeData runtimeData, int mode ) throws Exception
	   {
		 org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter(filename);
        BufferedReader br = new BufferedReader( new InputStreamReader(fileToSave.getInputStream()));
        if (mode == MODE_NANODROP) {
 		for (int i=0; i < 5; i++) { // Burn off five lines
 		br.readLine();
 		}
        }
        else 
        	br.readLine(); //Burn off the first header line.
        CSVStrategy strat;
        if (mode == MODE_NANODROP)
        	strat = CSVStrategy.DEFAULT_STRATEGY;
        else
        	strat = CSVStrategy.DEFAULT_STRATEGY;
        
		importData = (new CSVParser(br,strat)).getAllValues();
		int i = 1;
		
	   }

	public String [] findDuplicates(String [] s) {
		TreeSet set = new TreeSet();
		
		Arrays.sort(s);
		
		for (int j=0; j < s.length-1; j++) {
			if (s[j].equals(s[j+1])) {
				set.add(s[j]);
				
			}
		}
		Object [] oa = set.toArray();
		String [] sa = new String[oa.length];
		for (int i =0 ; i < oa.length; i++) {
			sa[i] = (String) oa[i];
		}
		return sa;
	}
	  

	public ChannelRuntimeProperties getRuntimeProperties() {
		// TODO Auto-generated method stub
		return new ChannelRuntimeProperties();
	}

	public void receiveEvent(PortalEvent arg0) {
		// TODO Auto-generated method stub

	}

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
	
	private String[] getUploadedList(String ids[]) {
		StringBuffer sb;
		if (ids == null) {
			return null;
		}
		else {
			int ll;
			int ul;
			int il = ids.length;
			int n = il/500;
			if (il % 500 != 0) 
				n++;
				
			String result[] = new String[n];
			for (int i = 0; i < n; i++) {
				ll = i*500;
				sb = new StringBuffer();
				sb.append(" (");
				if (i == n-1) ul=il; else ul= (i+1)*500;
				for (int j = ll; j < ul; j++){
					sb.append("'"+ids[j]+"'");
					if (j < ul-1) {
						sb.append(",");
					}
				}
				
				sb.append(")");
				result[i] = sb.toString();
			}
			return result;
		}
			
	}
	
	private int initialiseUpdateList(String ids[]) {
		hib_session = sf.openSession();
		biospecsForUpdate = new ArrayList();
		String inclauses[] = getUploadedList(ids);
		for (int i=0; i< inclauses.length; i++)
		biospecsForUpdate.addAll(hib_session.createQuery(
	    "from Biospecimen bio where bio.deleted = 0 and bio.biospecimenid in "+ inclauses[i]).list());
		
		
		
		return  biospecsForUpdate.size();
	}
	
	private int checkPermissions() {
		Vector intersect = new Vector();
		//Get list of studies where we can update.
		try {

			Vector updateStudies = authToken.getGroupsForActivity("biospecimen_update");
			Vector addTrans = authToken.getGroupsForActivity("biospecimen_add_transactions");
			Vector biospecAdd = authToken.getGroupsForActivity("biospecimen_add");
			Vector bioUpdate = authToken.getGroupsForActivity("biospecimen_update");
			intersect = new Vector(CollectionUtils.intersection(updateStudies, addTrans));
			intersect =  new Vector(CollectionUtils.intersection(intersect,biospecAdd));
			intersect =  new Vector(CollectionUtils.intersection(intersect,bioUpdate));


		}
		catch (neuragenix.security.exception.SecurityException se)
		{
			;
			
		}
		
		for (int i = 0 ; i< biospecsForUpdate.size(); i++) {
			Biospecimen b = (Biospecimen) biospecsForUpdate.get(i);
			long studykey = b.getStudykey();
			if (!intersect.contains(new Integer(""+studykey))) {
				strXML.append("<error>"+ "You do not have permissions to modify " + b.getBiospecimenid() +"</error>");
			}
			
			
			
		}
		return 0;
	}
	
	
	private int verifyData(int mode) {
		validDataRows = new LinkedList();
		for (int i=0; i< importData.length; i++) {
			try {
				
				if (importData[i][0] == null || importData[i][0].equals(""))
					throw new InvalidBiospecimenException();
			
				if (mode == MODE_NANODROP) {
					double conc = Double.valueOf(importData[i][4]).doubleValue();
					if (conc == 0 || Double.valueOf(importData[i][4]).isNaN()) throw new NumberFormatException();
					double purity = Double.valueOf(importData[i][7]).doubleValue();
					if (purity == 0 || Double.valueOf(importData[i][7]).isNaN()) throw new NumberFormatException();
				}
				else {
					double dnaconc = Double.valueOf(importData[i][3]).doubleValue();
					if (dnaconc == 0 || Double.valueOf(importData[i][3]).isNaN()) throw new NumberFormatException();
					double dnavol = Double.valueOf(importData[i][4]).doubleValue();
					if (dnavol == 0 || Double.valueOf(importData[i][4]).isNaN()) throw new InvalidDNAVolException();
					double tebuffervol = Double.valueOf(importData[i][5]).doubleValue();
					if (tebuffervol == 0 || Double.valueOf(importData[i][5]).isNaN()) throw new TEBufferVolException();
					
					
				}
				
				
			
			validDataRows.add(new Integer(i));
			
			} catch(NumberFormatException e) {
				System.err.println("[Nanodrop] Encountered an incorrect value for line: "+i);
				strXML.append("<error>" + "(line "+i+") " + importData[i][0] + " has an invalid value</error>" );
			}
			catch(InvalidBiospecimenException ibe) {
				System.err.println("[Nanodrop] Encountered an incorrect value for line: "+i);
				strXML.append("<error>" + "(line "+i+") " + importData[i][0] + " is not a valid biospecimen</error>" );
			}
		 catch(InvalidDNAVolException e) {
			System.err.println("[Nanodrop] Encountered an incorrect DNA volume value for line: "+i);
			strXML.append("<error>" + "(line "+i+") " + importData[i][0] + " has an invalid DNA volume value (" + importData[i][4]+")</error>" );
		}
	 catch(TEBufferVolException e) {
		System.err.println("[Nanodrop] Encountered an incorrect TE volume value for line: "+i);
		strXML.append("<error>" + "(line "+i+") " + importData[i][0] + " has an invalid TE volume value  (" + importData[i][5]+")</error>" );
	}
		}
		return validDataRows.size();
	}
	

	
	private String doUpload(ChannelRuntimeData rd, int mode) {
		this.strStylesheet=VERIFY;
		StringBuffer strXML = new StringBuffer();
		System.err.println("Starting upload....");

		try{
			if (mode == MODE_NANODROP)
				saveUploadFile("NANODROP_strFilename",rd,MODE_NANODROP);
			else
				saveUploadFile("ALIQUOT_strFilename",rd,MODE_ALIQUOT);
		
		
		int validRowNo = verifyData(mode);
		String ids[] = new String[validRowNo];
		this.bioMapping = new HashMap(validRowNo);
		for (int i =0; i< validRowNo; i++) {
			Integer ix = (Integer) validDataRows.get(i);
			bioMapping.put(importData[ix.intValue()][0], ix);
			ids[i] = importData[ix.intValue()][0];
		}
		String dupes [] = findDuplicates(ids);
		
		int count = initialiseUpdateList(ids);
		
		int x = checkPermissions();
		if (mode == MODE_ALIQUOT) {
			for (int i = 0 ; i< biospecsForUpdate.size(); i++) {
				Biospecimen b = (Biospecimen) biospecsForUpdate.get(i);
			    int rowval = ((Integer)bioMapping.get(b.getBiospecimenid())).intValue();
			    double dnavol = Double.valueOf(importData[rowval][4]).doubleValue();
			    double biodnavol = b.getQtyCollected().doubleValue() + b.getQtyRemoved().doubleValue();
			    if (dnavol > biodnavol) {
			    	strXML.append("<error>"+ b.getBiospecimenid() + " has insufficient DNA volume.</error>");
			    }
			}
		}
			
				strXML.append("<biocount>"+count+"</biocount>");
		if (dupes.length > 0) {
			for (int i=0; i< dupes.length; i++) {
				strXML.append("<error>"+ dupes[i] + " has duplicate entries</error>");
			}
		}
		else {
			

		if (count > 0) {
			for (int i = 0 ; i< biospecsForUpdate.size(); i++) {
				Biospecimen b = (Biospecimen) biospecsForUpdate.get(i);
				if (mode == MODE_NANODROP) {
				String purity = b.getPurity()==null ? "0" : "1";
				String dnaConc = b.getDnaconc()==null ? "0" : "1";
				strXML.append("<biospecimen " + "id=\""+b.getBiospecimenid() +"\" " +  "><conc>" + dnaConc + "</conc>"	+  "<purity>" + purity + "</purity>"
						+ "<rowid>" + bioMapping.get(b.getBiospecimenid()) + "</rowid>" +
				"</biospecimen>")	;
				}
				else {
					strXML.append("<biospecimen " + "id=\""+b.getBiospecimenid() +"\" " +  ">"
							+ "<rowid>" + bioMapping.get(b.getBiospecimenid()) + "</rowid>" +
					"</biospecimen>")	;
				}
				
				
			}
			
		}
		
		
		}
		/*for (int i=0; i< importData.length; i++) {
			try {
				double conc = Double.valueOf(importData[i][4]).doubleValue();
				if (conc == 0 || Double.valueOf(importData[i][4]).isNaN()) throw new NumberFormatException();
			}
			catch(NumberFormatException ne) {
				strXML.append("<error>"+ importData[i][0] + " has an invalid concentration value ("+importData[i][4]+ ")</error>");
			}
			
			try {
				double purity = Double.valueOf(importData[i][7]).doubleValue();
				if (purity == 0 || Double.valueOf(importData[i][7]).isNaN()) throw new NumberFormatException();
			}
			catch(NumberFormatException ne) {
				strXML.append("<error>"+ importData[i][0] + " has an invalid purity value ("+importData[i][7]+ ")</error>");
			}	
		}*/
		
		
		
		
		} catch (Exception e) {System.err.println("Houston we have a problem");
		e.printStackTrace();}
		
		
		return strXML.toString();
	}
	
	private void updateBiospecimen(Biospecimen b, int rowval) {
		Transaction tx = null;
		try {
		    tx = hib_session.beginTransaction();
		    b.setDnaconc(new Double(importData[rowval][4]));
		    b.setPurity(new Double(importData[rowval][7])); 
		    hib_session.update(b);
		    tx.commit();
		}
		catch (RuntimeException e) {
		    if (tx != null) tx.rollback();
		    throw e; // or display error message
		}
		
	}
	
	private void doVerify(ChannelRuntimeData rd, int mode) {
			this.strStylesheet = CONFIRM;
			for (int i = 0; i < biospecsForUpdate.size(); i++) {
				Biospecimen b = (Biospecimen) biospecsForUpdate.get(i);
				String strNewBiospecimenID = "";
				try{
					int studykey = (int) b.getStudykey();
				strNewBiospecimenID = BiospecimenUtilities
				.getNewSubBiospecimenStringID(
						new DALSecurityQuery("biospecimen_add",authToken),b.getSampletype(), b.getBiospecimenid(),true, studykey);
				} catch (Exception e) {}
				
				int rowval = ((Integer) bioMapping.get( b.getBiospecimenid())).intValue();
				
				if (mode == MODE_NANODROP) {
				strXML.append("<biospecimen id=\"" + b.getBiospecimenid() + "\" >" 
						+ "<oldconc>"  + b.getDnaconc() + "</oldconc>"
					     + "<oldpurity>" + b.getPurity() + "</oldpurity>"
					     + "<newconc>" + importData[rowval][4] + "</newconc>"
					     + "<newpurity>" + importData[rowval][7] + "</newpurity>"
					     + "</biospecimen>");
				}
				else
				{
					double stockVol = new Double(importData[rowval][IDX_DNAVOL]).doubleValue();
				    double stockConv = new Double(importData[rowval][IDX_DNACONC]).doubleValue();
				    double teVol = new Double(importData[rowval][IDX_TEVOL]).doubleValue();
				    double newConc = stockVol * stockConv / (stockVol + teVol);
					strXML.append("<biospecimen id=\"" + b.getBiospecimenid() + "\" >" 
							+ "<aliquotid>" + strNewBiospecimenID + "</aliquotid>"
							+ "<stockvol>"  +  stockVol + "</stockvol>"
						     + "<aliquotvol>" + (stockVol + teVol) + "</aliquotvol>"
						     + "<aliquotconc>" + newConc + "</aliquotconc>"
						     + "</biospecimen>");
					
				}
			}
			
	}
	
			private double calcConc(double stockconc, double stockvol, double tevol) {
				return stockconc*stockvol/(stockvol + tevol);
			}
			
	private void doConfirm(ChannelRuntimeData rd, int mode) {
		this.strStylesheet = SUCCESS;
		for (int i = 0; i < biospecsForUpdate.size(); i++) {
			Biospecimen b = (Biospecimen) biospecsForUpdate.get(i);
			int rowval = ((Integer) bioMapping.get( b.getBiospecimenid())).intValue();
			if (mode==MODE_NANODROP)
			updateBiospecimen(b, rowval);
			else
				createAliquot(b,null,rowval);
		}
		hib_session.close();
	}
	
	public void setStaticData(ChannelStaticData sd) throws PortalException {
		// TODO Auto-generated method stub
	    this.staticData = sd;
	    this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");

	}

	public void createAliquot(Biospecimen b, Cell c, int rowval) {
		Transaction tx = null;
		try {
		    tx = hib_session.beginTransaction();
		    int studykey = (int) b.getStudykey();
		    String strNewBiospecimenID = BiospecimenUtilities
			.getNewSubBiospecimenStringID(
					new DALSecurityQuery("biospecimen_add",authToken),b.getSampletype(), b.getBiospecimenid(),true,studykey);
		    Biospecimen a = new Biospecimen();
		    BioTransactions parentbt = new BioTransactions();
		    BioTransactions aliquotbt = new BioTransactions();
		    a.setBiospecimenid(strNewBiospecimenID);
			a.setSampletype(b.getSampletype());
			a.setParentid(b.getBiospecimenid());
			a.setParentkey(b.getBiospecimenkey());
		    a.setDepth(b.getDepth() +1);
		    a.setPatientkey(b.getPatientkey());
		    a.setStudykey(b.getStudykey());
		    double stockVol = new Double(importData[rowval][IDX_DNAVOL]).doubleValue();
		    double stockConv = new Double(importData[rowval][IDX_DNACONC]).doubleValue();
		    double teVol = new Double(importData[rowval][IDX_TEVOL]).doubleValue();
		    double newConc = stockVol * stockConv / (stockVol + teVol);
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		    Date sampleDate = sdf.parse(importData[rowval][IDX_SAMPLEDATE]);
		    parentbt.setTransactiondate(sampleDate);
		    parentbt.setBiospecimenkey(b.getBiospecimenkey());
		    parentbt.setAction("Delivered");
		    parentbt.setReason("Sub-aliquot");
		    parentbt.setQuantity(stockVol*-1);
		    b.setQtyRemoved(new Double(b.getQtyRemoved().doubleValue() - stockVol) );
		    a.setQtyCollected(new Double(teVol + stockVol) );
		    
		    
		    
		    
		    
		    
		    a.setDnaconc(new Double(newConc));	    
		    
		    long biospecId =  ((Long) hib_session.save(a)).longValue();
		    aliquotbt.setTransactiondate(sampleDate);
		    aliquotbt.setBiospecimenkey(biospecId);
		    aliquotbt.setAction("Available");
		    aliquotbt.setReason("Initial Quantity");
		    aliquotbt.setQuantity(stockVol+teVol);
		    System.err.println("HIB: Save new biospecimen with id: "+biospecId);
		    hib_session.save(aliquotbt);
		    hib_session.save(parentbt);
		    hib_session.save(b);
		    hib_session.save(a);
		    tx.commit();
		}
		catch (RuntimeException e) {
		    if (tx != null) tx.rollback();
		    throw e; // or display error message
		}
		catch (Exception e) {
			
		}
	}
	
}
