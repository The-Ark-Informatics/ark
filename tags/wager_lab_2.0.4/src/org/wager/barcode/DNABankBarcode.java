package org.wager.barcode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wager.barcode.AbstractBarcodeEngine.BarcodeData;

import neuragenix.security.AuthToken;

public class DNABankBarcode extends AbstractBarcodeEngine {
	static private final String REAL_NUMBER = "^[-+]?\\d+(\\.\\d+)?$";

	@Override
	public InputStream getBarcode(String strDomain, int domainKey,
			AuthToken authtoken) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();

		if (strDomain.equals("SINGLE_BIOSPECIMEN")
				|| strDomain.equals("MULTIPLE_BIOSPECIMEN")) {
			try {
				Vector<BarcodeData> ts = executeBiospecimenSelect(strDomain, domainKey);
				for (Iterator<BarcodeData> i = ts.iterator(); i.hasNext();) {
					BarcodeData b = (BarcodeData) i.next();
					if (b.getValue(SAMPLE_TYPE).equals("Straw")) {
						printStrawBarcode(b,sb);
					}
					else
						printBarcode(b, sb);

				}
			} catch (SQLException sqle) {
				sqle.printStackTrace(System.err);
			}
		} else if (strDomain.equals("INVENTORY"))

		{

			try {
				Vector<BarcodeData> ts = executeTraySelect(domainKey);
				for (Iterator<BarcodeData> i = ts.iterator(); i.hasNext();) {
					BarcodeData b = (BarcodeData) i.next();
					if (b.getValue(SAMPLE_TYPE).equals("Straw")) {
						printStrawBarcode(b,sb);
					}
					else
						printBarcode(b, sb);
				}
			} catch (ClassNotFoundException cnfe) {
			}

			catch (SQLException sqle) {
				sqle.printStackTrace(System.err);
			}
		} else {
			return null;
		}

		return new ByteArrayInputStream(sb.toString().getBytes());

	}// end doGet()

	public void printBarcode(BarcodeData b, StringBuffer sb)
	/*
	 * This method should be edited to suit the particular printing requirements
	 * for a study.
	 */
	{
		String aBarcode = b.getValue(BIOSPECIMEN_ID);
		String dateOfBirth = b.getValue(DATE_OF_BIRTH);
		String lastLineOfCircle = "";
		String secondLineOfCircle = "";
		// Ok first check and see if last character is a number.
		String lastChar = aBarcode.substring(aBarcode.length() - 1);
		// Need to find out where the study code ends
		// Trim off the year component
		String withoutYear = aBarcode.substring(2);
		Pattern pat = Pattern.compile("\\d");
		Matcher matcher = pat.matcher(withoutYear);
		boolean gotMatch = matcher.find();
		System.out.print(gotMatch);
		int indexOfnum = -1;
		if (gotMatch) {
			indexOfnum = matcher.start() + 2;
		}
		System.out.print(indexOfnum);
		if (lastChar.matches(REAL_NUMBER)) {
			// We have a clone, therefore go one backwards.
			lastLineOfCircle = aBarcode.substring(aBarcode.length() - 2);
			secondLineOfCircle = aBarcode.substring(indexOfnum, aBarcode
					.length() - 2);
		} else {
			lastLineOfCircle = lastChar;
			secondLineOfCircle = aBarcode.substring(indexOfnum, aBarcode
					.length() - 1);
		}
		String secondLine = secondLineOfCircle;

		try {
			secondLine = new Integer(secondLineOfCircle).toString();
		} catch (NumberFormatException ne) {
		}

		sb.append("\n");
		sb.append("D14\n");
		sb.append("q457\n");
		sb.append("N\n");
		sb.append("b200,15,D,h3,\"" + aBarcode + "\"\n");
		sb.append("A100,20,0,2,1,1,N,\"" + aBarcode.substring(0, indexOfnum)
				+ "\"\n");
		sb.append("A100,40,0,2,1,1,N,\"" + secondLine + "\"\n");
		sb.append("A115,60,0,2,1,1,N,\"" + lastLineOfCircle + "\"\n");
		sb.append("A260,15,0,1,1,2,N,\"" + aBarcode + "\"\n");
		if (dateOfBirth != null)
			sb.append("A260,45,0,1,1,2,N,\"" + dateOfBirth + "\"\n");
		sb.append("P1\n");
	}// end printBarcode()

	
	public void printStrawBarcode(BarcodeData b, StringBuffer sb) {
			// TODO Auto-generated method stub
			sb.append("q401\n");
			sb.append("D14\n");
			sb.append("N\n");
			sb.append("B30,0,0,1,1,2,23,N,\"" + b.getValue(BIOSPECIMEN_ID) + "\"\n");
			sb.append("A230,0,0,1,1,1,N,\"" + b.getValue(BIOSPECIMEN_ID)+"\"\n");
			sb.append("A230,15,0,1,1,1,N,\"" + b.getValue(DATE_OF_BIRTH)+"\"\n");
			sb.append("P1\n");

		
	}
	
}// end Barcode
