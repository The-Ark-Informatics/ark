/*
 * Created on 29-Jul-2005
 * 
 * Last modified 29-Jul-2005
 *
 * @author Chris Williams
 */
package org.wager ;
import java.io.IOException;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WAFSSBarcode extends HttpServlet
{	
	/*The string that holds the barcode value*/
	private String barcode;
	private String labelType;	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//get the barcode value from the request
		barcode = (String) request.getParameter("barcode");
		labelType = (String) request.getParameter("labelType");		

		String dob = (String) request.getParameter("dob");
		if (dob != null) 
		{
			if (barcode != null) 
			{

				//set the MIME type of the response
				response.setContentType("text/prn");
				//response.setContentType("application/pdf");
				PrintWriter out = response.getWriter();
				out.println("");
				out.println("D15");
				out.println("N");
				// On rectangle label
				out.println("b192,15,D,h3,\""+ barcode + "\"");
				out.println("A252,15,0,1,1,2,N,\"" + barcode + "\"");
                                out.println("A252,45,0,1,1,2,N,\"" + dob + "\"");

				/*
				0196SCZ00001H1
				01234567891111
				          0123

				Collection #  : barcode.substring(0, 2);
				Collection yr : barcode.substring(2, 4);
				Study code    : barcode.substring(4, 7);
				Patient Id    : barcode.substring(7, 12);
				Sample Type   : barcode.substring(12,  barcode.length());

				*/
				// On top circle/dot label
				// Collection # and year
				out.println("A110,10,0,2,1,1,N,\"" + barcode.substring(0, 4) + "\"");

				// Study Code
				out.println("A110,25,0,1,1,1,N,\"" + barcode.substring(4, 7) + "\"");

				// Patient ID
				out.println("A100,40,0,2,1,1,N,\"" + barcode.substring(7, 12) +"\"");

				// Sample Type
				out.println("A115,55,0,1,1,1,N,\"" + barcode.substring(12, barcode.length()) +"\"");

				//out.println("A260,15,0,1,1,2,N,\"" + barcode + "\"");
				//out.println("A260,45,0,1,1,2,N,\"" + dob + "\"");
				out.println("P1");
			}
		}
		
		
	}




}
