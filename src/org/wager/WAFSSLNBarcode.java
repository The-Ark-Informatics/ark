/*
 * Created on 11-May-2006
 * 
 * Last modified 11-May-2006
 *
 * @author Declan Lynch
 *
 * COMPILATION NOTES:
 * javac -cp ./servlet.jar:./ojdbc14.jar Barcode.java
 * 
 * in other words servlet.jar and ojbc14.jar should be on the classpath
 * you can copy them to same folder as Barcode.java and compile as above
 * 
 * Yes, there is a compilation warning.  I think it's a Java5 thing.
 * You can construct a TreeSet as follows in Java5 to restrict it's elements to a type
 * 
 * TreeSet<String>=new TreeSet<String>();
 * 
 * They're called "parameterized generics" or something....
 * I've left the element type open as per Java4.
 * 
 * BIOGENIX NOTES:
 * Barcode.java decides what to do with HTTP request arguments based on VALUE.
 * 
 * Supplying the parameter "barcode" causes the programme to assume it is a single
 * value to be printed directly.  In fact, that it's the value of strBiospecimenID.
 * 
 * Supplying the parameter "trayid" causes the programme to assume that it's a batch
 * print job and that the value of trayid is the value of TRAY_intTrayID.  The code then
 * fetches via jdbc all the occupied cells in the tray with that id (ix_inv_tray.traykey).
 * 
 * Supplying both/neither is untested!!
 * 
 * Add something like the following to view_tray.xsl to create the batch print button.
 * 
 * <tr>
 *  <td>Print a barcode label for each occupied biospecimen location in this tray.</td>
 * </tr>
 * <tr>
 * 	<td>
 *   <input type="button" class="uportal-button" onclick="document.location.href='/Barcode.prn?trayid={$TRAY_intTrayID}'" value="Print Barcodes"></input>
 *  </td>
 * </tr>
 * 
 * You don't need to re-edit web.xml if you've already done so for the Barcode servlet.
 */

package org.wager;
import java.io.IOException;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.*;

//import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;


public class WAFSSLNBarcode extends HttpServlet
{
	/**
	 * 
	 */
	

	static private final String REAL_NUMBER = "^[-+]?\\d+(\\.\\d+)?$";
	private static final long serialVersionUID = 1L;
	// The string that holds the TRAY_intTrayID value for the BATCH print job
	private String trayid;
	// The string that holds the BIOSPECIMEN_strBiospecimenID value for the SINGLE print job
	private String barcode;
	private String dateOfSample;
	private String patientkeyStr;
	private int patientkey;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/* Get the value of "barcode" from the request.
		 * This is the value of strBiospecimenID for the document.
		 * This will be the only barcode to print.
		 */
		barcode = (String)request.getParameter("barcode");
		patientkeyStr = (String)request.getParameter("patientKey");
		patientkey = Integer.parseInt(patientkeyStr);
		/* Get the value of "trayid" from the request
		 * This is the value of Tray_intTrayID for the document.
		 */
		trayid = (String)request.getParameter("trayid");
		dateOfSample = (String) request.getParameter("dateOfSample");
		
		//set the MIME type of the response				
		response.setContentType("text/prn");
		//response.setContentType("application/pdf");
		PrintWriter out = response.getWriter();


			if (barcode != null)
			/* 
			* If barcode is not null then we are printing only one barcode
			*/
			{
				String asrbno = "";
				try {
				 asrbno = getASRBNo(patientkey);
				}
				 catch(SQLException sqle){
                                        sqle.printStackTrace(System.err);
                                }
				printBarcode(barcode,dateOfSample,asrbno,out);

			}
			
			if (trayid != null)
			/*
			* If trayid is not null then we are printing a barcode for each biospecimen
			* in the tray.
			*/
			{

				try{
					Vector ts = executeSelect();
					for (Iterator i = ts.iterator(); i.hasNext();) {
					BarcodeData b = (BarcodeData) i.next();
					printBarcode(b.getId(), b.getDob(),null,out);
					}
				}catch (ClassNotFoundException cnfe){
				}
				
				catch(SQLException sqle){
					sqle.printStackTrace(System.err);
				}
			}
			
	}//end doGet()
	
	public void printBarcode(String aBarcode, String dateOfSample, String asrbno, PrintWriter out)
	/*
	 * This method should be edited to suit the particular printing requirements
	 * for a study.
	 */
	{
	String lastLineOfCircle = "";
	String secondLineOfCircle ="";
	//Ok first check and see if last character is a number.
	String lastChar = aBarcode.substring(aBarcode.length()-1);


		out.println("");
		out.println("D14");
		out.println("N");
		out.println("b200,15,D,h3,\""+ aBarcode + "\"");
		out.println("A260,15,0,1,1,2,N,\"" + aBarcode + "\"");
		out.println("A260,45,0,1,1,2,N,\"" + dateOfSample + "\"");
		out.println("A260,75,0,1,1,2,N,\"" + asrbno + "\"");
		out.println("P1");
	}// end printBarcode()
	

	private String getASRBNo(int patientkey) throws SQLException {

	 OracleDataSource ods = new OracleDataSource();
            ods.setURL("jdbc:oracle:thin:wagerlab/veTum1n@172.16.1.100:1521:prod");
            Connection conn = ods.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select z.asrbno from zeus.subject z where  z.subjectkey = " + patientkey );
	
	while (rset.next())
    {
        try{
                String bsnid = rset.getString(1);
		return bsnid;
        }catch(ClassCastException ce){
                System.err.println("We got a class cast error");
                ce.printStackTrace(System.err);
        }
    }

	return "";

	}


	public Vector executeSelect() throws ClassNotFoundException,SQLException
	{
		/*
		 * This method should be edited to provide JDBC connection details for the study
		 */
		
		//Java Collection to store the Strings to be printed ordered by value ascending.
		Vector ts= new Vector();
		
	    OracleDataSource ods = new OracleDataSource();
	    ods.setURL("jdbc:oracle:thin:wagerlab/veTum1n@172.16.1.100:1521:prod");
	    Connection conn = ods.getConnection();

	    Statement stmt = conn.createStatement();
	    ResultSet rset = stmt.executeQuery("select biospecimenid, to_char(p.dob,'dd/mm/yyyy') from ix_inv_cell cell,ix_biospecimen b, ix_patientv2 p " +
				        "where cell.biospecimenkey = b.biospecimenkey " +
					"and p.patientkey (+) = b.patientkey " +
        				"and  cell.biospecimenkey != -1 " +
        				"and  cell.deleted = 0 " +
					"and b.cellkey = cell.cellkey " +
        				"and cell.traykey =" + trayid+ " order by rowno, colno");
	    				
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

private String id;
private String dob;

public BarcodeData(String id, String dob) {
	this.id = id;
	this.dob = dob;
}

public String getId () {
	return this.id;
}

public String getDob() {
	return this.dob;
}

}

	
}//end Barcode
