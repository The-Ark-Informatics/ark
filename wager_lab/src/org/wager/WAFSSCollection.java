/*
 * Created on 10-Sep-2007
 * 
 * Last modified 10-Sep-2007
 *
 * @author Chris Ellis
 */
package org.wager ;
import java.io.IOException;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.pool.OracleDataSource;
import java.sql.*;
import java.util.*;
import oracle.jdbc.pool.OracleDataSource;


public class WAFSSCollection extends HttpServlet
{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String patientkey = (String) request.getParameter("patientkey");
		String admissionid = (String) request.getParameter("admissionid");
		String patientid = new String();
		String dob = new String();
		String admissiondate = new String();
		String familyid = new String();
		String asrbno = new String();
		String researcher = new String();
		String sex = new String();
	
		System.out.println("Inside CollectionLabel.java: patientkey:" + patientkey + " admissionid: " + admissionid);
	
		try
		{
			OracleDataSource ods = new OracleDataSource();
	            	ods.setURL("jdbc:oracle:thin:wagerlab/veTum1n@172.16.1.100:1521:PROD");
		        Connection conn = ods.getConnection();
			System.err.println("Connected to Oracle OK");
	
		       	Statement stmt = conn.createStatement();
            		ResultSet rset = stmt.executeQuery(
					"select patientid, to_char(dob, 'dd/mm/yyyy'), to_char(admissiondate, 'dd/mm/yyyy'), familyid, otherid, ref_doctor, sex " + 
					"from ix_patientv2 p,ix_admissions adm " +
                                        "where p.patientkey = adm.patientkey " +
                                        "and adm.admissionid = " + admissionid +
                                        "and adm.deleted = 0" +
                                        "and p.patientkey = " + patientkey);
			System.err.println("Executed query OK");

			while (rset.next())
    			{
			        try
				{
		                	patientid = rset.getString(1);
					dob = rset.getString(2);
					admissiondate = rset.getString(3);
					familyid = rset.getString(4);
					asrbno = rset.getString(5);
					researcher = rset.getString(6);
					sex = rset.getString(7);

					System.out.println("Inside CollectionLabel.java: " + patientid + " " + dob + " " + admissiondate + " " + familyid + " " + asrbno
								+ " " + researcher + " " + sex);
			        }
				catch(ClassCastException ce)
				{
					System.out.println("ClassCast Error in CollectionLabel.java ");
        			}
    			}
		}
		catch (SQLException sqlex)
		{
			System.out.println("SQL Error in CollectionLabel.java ");
		}


		//set the MIME type of the response
		response.setContentType("text/prn");
		//response.setContentType("application/pdf");
		PrintWriter out = response.getWriter();

		/* Labels with circle top-sticker and small rectangle
		out.println("");
		out.println("D15");
		out.println("N");
		out.println("b200,25,D,h3,\""+ patientid + "\"");
		out.println("A100,20,0,2,1,1,N,\"" + patientid + "\"");
		out.println("A95,40,0,2,1,1,N,\"" + patientid +"\"");
		out.println("A115,60,0,2,1,1,N,\"" + dob +"\"");
		out.println("A260,25,0,1,1,2,N,\"" + admissiondate + "\"");
		out.println("A260,55,0,1,1,2,N,\"" + admissionid + "\"");
		out.println("A270,55,0,1,1,2,N,\"" + familyid + "\"");
		out.println("A280,55,0,1,1,2,N,\"" + asrbno + "\"");
		out.println("A290,55,0,1,1,2,N,\"" + researcher + "\"");
		out.println("A300,55,0,1,1,2,N,\"" + sex + "\"");
		out.println("P1");
		*/

                out.println("");
                out.println("D15");
                out.println("N");
                out.println("A240,10,1,2,1,1,N,\"ID: " + patientid + " Family ID:" + familyid + "\"");
		out.println("A220,10,1,2,1,1,N,\"ASRB No:" + asrbno + "\"");
                //out.println("A180,10,1,2,1,1,N,\"ASRB No:" + asrbno + "\"");
		//out.println("A160,10,1,2,1,1,N,\"Collection Date: " + admissiondate + "\"");
                out.println("A200,10,1,2,1,1,N,\"Collection Date: " + admissiondate + "\"");
		out.println("A180,10,1,2,1,1,N,\"Researcher: " + researcher + "\"");
		out.println("A160,10,1,2,1,1,N,\"DOB: " + dob + " " + "Sex: " + sex + "\"");
                //out.println("A120,10,1,2,1,1,N,\"Sex: " + sex + "\"");
                out.println("P1");

		
	}
}
