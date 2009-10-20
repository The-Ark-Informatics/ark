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

public class BradyBarcode extends AbstractBarcodeEngine {
	static private final String REAL_NUMBER = "^[-+]?\\d+(\\.\\d+)?$";

	@Override
	public InputStream getBarcode(String strDomain, int domainKey,
			AuthToken authtoken) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("m m\n");	// Initialise units as millimetres.
		if (strDomain.equals("SINGLE_BIOSPECIMEN")
				|| strDomain.equals("MULTIPLE_BIOSPECIMEN")) {
			try {
				Vector<BarcodeData> ts = executeBiospecimenSelect(strDomain, domainKey);
				for (Iterator<BarcodeData> i = ts.iterator(); i.hasNext();) {
					BarcodeData b = (BarcodeData) i.next();
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

		sb.append("J\n");
		sb.append("Sl1;0.0,0.0,9.5,9.5,42.5,42.5,1\n");
		sb.append("B15.2,2,0,DATAMATRIX,0.41;"+aBarcode+"\n");
		sb.append("T22.4,4,0,5,2,q100;"+aBarcode+"\n");
		sb.append("T3.3,3.0,0,3,2.08,q100;"+ aBarcode.substring(0, indexOfnum)+secondLine.substring(5)+"\n");
		sb.append("T3.7,5.8,0,3,2.08,q100;"+secondLine.substring(0,5 )+"\n");
		sb.append("T6.1,8.1,0,3,2.08,q100;"+lastLineOfCircle+"\n");
		sb.append("T22.7,5.7,0,3,2.08,q100;"+dateOfBirth+"\n");
		sb.append("A 1\n");

	}// end printBarcode()


}
