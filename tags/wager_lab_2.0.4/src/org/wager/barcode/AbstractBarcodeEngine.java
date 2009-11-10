package org.wager.barcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;




public abstract class AbstractBarcodeEngine implements BarcodeEngine {

	public static final String BIOSPECIMEN_ID = "BIOSPECIMENID";
	public static final String DATE_OF_BIRTH = "DOB";
	public static final String SAMPLE_TYPE = "SAMPLETYPE";
	
	public Vector<BarcodeData> executeBiospecimenSelect(String strDomain, int domainkey) throws SQLException
	{
		/*
		 * This method should be edited to provide JDBC connection details for the study
		 */
		
		//Java Collection to store the Strings to be printed ordered by value ascending.
		Vector<BarcodeData> ts= new Vector<BarcodeData>();
		
	    //O//racleDataSource ods = new OracleDataSource();
	    //ods.setURL("jdbc:oracle:thin:wagerlab/veTum1n@172.16.1.100:1521:prod");
	    Connection conn = DriverManager.getConnection("jdbc:poolman://oracle");

	    Statement stmt = conn.createStatement();
	/*    System.out.println("select biospecimenid,sampletype, to_char(p.dob,'dd/mm/yyyy') from ix_biospecimen b, ix_patientv2 p " +
					"where p.patientkey = b.patientkey " +
					"and p.patientkey = " + patientkey +
					" and b.deleted=0 " +
        			"order by biospecimenid");*/
	    ResultSet rset = null;
	    if (strDomain.equals("SINGLE_BIOSPECIMEN")) {
	    	rset = stmt.executeQuery("select biospecimenid, sampletype,to_char(sampledate,'dd/mm/yyyy') sampledate, to_char(p.dob,'dd/mm/yyyy') dob, p.otherid,p.familyid from ix_biospecimen b, ix_patientv2 p " +
					"where p.patientkey = b.patientkey " +
					"and b.biospecimenkey = " + domainkey +
					" and b.deleted=0 " +
        			"order by biospecimenid");
	    }
	    else 
	     rset = stmt.executeQuery("select biospecimenid, sampletype,to_char(sampledate,'dd/mm/yyyy') sampledate, to_char(p.dob,'dd/mm/yyyy') dob , p.otherid,p.familyid from ix_biospecimen b, ix_patientv2 p " +
					"where p.patientkey = b.patientkey " +
					"and p.patientkey = " + domainkey +
					" and b.deleted=0 " +
        			"order by biospecimenid");
	    				
    while (rset.next())
    	
    {
    	
    	try{
    		String biospecimenid = rset.getString("BIOSPECIMENID");
		String thisDob = rset.getString("DOB");
		String sampleType = rset.getString("SAMPLETYPE");
		String sampleDate = rset.getString("SAMPLEDATE");
		String asrbno = rset.getString("OTHERID");
		String familyid = rset.getString("FAMILYID");
		BarcodeData b = new BarcodeData(biospecimenid,thisDob);
		b.setValue(SAMPLE_TYPE, sampleType);
		b.setValue("SAMPLEDATE",sampleDate);
		b.setValue("ASRBNO",asrbno);
		b.setValue("FAMILYID",familyid);
		
    		ts.add(b);
    		
    		
    	}catch(ClassCastException ce){
		System.err.println("We got a class cast error");
		ce.printStackTrace(System.err);
    	}
    }
    
    
    rset.close();
    stmt.close();
    conn.close();
    
	return ts;
				
	}//end executeSelect()
	
	public abstract void printBarcode(BarcodeData b, StringBuffer sb);
	

	public Vector<BarcodeData> executeTraySelect(int intTrayKey) throws ClassNotFoundException,SQLException
	{
		/*
		 * This method should be edited to provide JDBC connection details for the study
		 */
		
		//Java Collection to store the Strings to be printed ordered by value ascending.
		Vector<BarcodeData> ts= new Vector<BarcodeData>();
		//this.conn =
	   // OracleDataSource ods = new OracleDataSource();
	   // ods.setURL("jdbc:oracle:thin:@172.16.1.100:1521:prod");
	    Connection conn =  DriverManager.getConnection("jdbc:poolman://oracle");

	    Statement stmt = conn.createStatement();
	    ResultSet rset = stmt.executeQuery("select biospecimenid, to_char(p.dob,'dd/mm/yyyy') from ix_inv_cell cell,ix_biospecimen b, ix_patientv2 p " +
				        "where cell.biospecimenkey = b.biospecimenkey " +
					"and p.patientkey (+) = b.patientkey " +
        				"and  cell.biospecimenkey != -1 " +
        				"and  cell.deleted = 0 " +
					"and b.cellkey = cell.cellkey " +
        				"and cell.traykey =" + intTrayKey+ " order by rowno, colno");
	    				
    while (rset.next())
    	
    {
    	
    	try{
    		String biospecimenid = rset.getString(1);
		String thisDob = rset.getString(2);
		BarcodeData b = new BarcodeData(biospecimenid,thisDob);
    		ts.add(b);
    		
    	}catch(ClassCastException ce){
		System.err.println("We got a class cast error");
		ce.printStackTrace(System.err);
    	}
    }
    
    
    rset.close();
    stmt.close();
    conn.close();
    
	return ts;
				
	}//end executeSelect()
	class BarcodeData {

		Hashtable<String,String> data;
	
		
		public BarcodeData() {
			data = new Hashtable<String,String>();
		}
		public BarcodeData(String biospecimenid,String Dob) {
			data = new Hashtable<String,String>();
			if (biospecimenid != null)
			data.put(BIOSPECIMEN_ID, biospecimenid);
			if( Dob != null)
			data.put(DATE_OF_BIRTH,Dob);
			else
				data.put(DATE_OF_BIRTH,"");
		}
		public void setValue(String param, String value){
			String notNullValue = value;
			if(value == null)
				notNullValue = "";
			data.put(param, notNullValue);
		}	
		
		public String getValue(String param) {
			return data.get(param);
		}
	}
}
