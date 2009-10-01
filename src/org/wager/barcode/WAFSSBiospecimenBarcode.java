/*
 * Created on 29-Jul-2005
 * 
 * Last modified 29-Jul-2005
 *
 * @author Chris Williams
 */
package org.wager.barcode;




public class WAFSSBiospecimenBarcode extends DNABankBarcode {
	/* The string that holds the barcode value */

	
	@Override
	/*
	 * 0196SCZ00001H1 01234567891111 0123
	 * 
	 * Collection # : barcode.substring(0, 2); Collection yr :
	 * barcode.substring(2, 4); Study code : barcode.substring(4,
	 * 7); Patient Id : barcode.substring(7, 12); Sample Type :
	 * barcode.substring(12, barcode.length());
	 */
	// On top circle/dot label
	// Collection # and year
	public void printBarcode(BarcodeData b, StringBuffer sb) {
		String dob = b.getValue("DOB");
		String barcode = b.getValue("BIOSPECIMENID");
		String sampleType = b.getValue(SAMPLE_TYPE).trim();
		System.out.println("**"+sampleType+"**");
		if (sampleType.equalsIgnoreCase("Frozen Lymphocytes (F)") || sampleType.equalsIgnoreCase("Transformed lymphoblasts (T)")) {
			printLNBarcode(b,sb);
		}
		else {
		if (dob != null) {
			if (barcode != null) {

				// set the MIME type of the response

				// response.setContentType("application/pdf");

				sb.append("\n");
				sb.append("q457\n");
				sb.append("D15\n");
				sb.append("N\n");
				// On rectangle label
				sb.append("b192,15,D,h3,\"" + barcode + "\"\n");
				sb.append("A252,15,0,1,1,2,N,\"" + barcode + "\"\n");
				sb.append("A252,45,0,1,1,2,N,\"" + dob + "\"\n");

				
				sb.append("A110,10,0,2,1,1,N,\"" + barcode.substring(0, 4)
						+ "\"\n");

				// Study Code
				sb.append("A110,25,0,1,1,1,N,\"" + barcode.substring(4, 7)
						+ "\"\n");

				// Patient ID
				sb.append("A100,40,0,2,1,1,N,\"" + barcode.substring(7, 12)
						+ "\"\n");

				// Sample Type
				sb.append("A115,55,0,1,1,1,N,\""
						+ barcode.substring(12, barcode.length()) + "\"\n");

				// sb.append("A260,15,0,1,1,2,N,\"" + barcode + "\"");
				// sb.append("A260,45,0,1,1,2,N,\"" + dob + "\"");
				sb.append("P1\n");
			}

		}
		}

	}
	
	
	public void printLNBarcode(BarcodeData b, StringBuffer sb)
	/*
	 * This method should be edited to suit the particular printing requirements
	 * for a study.
	 */
	{
		
		String aBarcode = b.getValue(BIOSPECIMEN_ID);
		String dateOfSample = b.getValue("SAMPLEDATE");
		String asrbno = b.getValue("ASRBNO");
		String familyid = b.getValue("FAMILYID");
		String result="";
		if (asrbno == null) asrbno = "";
		if (familyid == null) familyid = "";
		if (asrbno.equals("") && !familyid.equals("")) {
			result="F: "+familyid; 
		}
		else if (!asrbno.equals(""))
			result="A: "+ asrbno;
		

		sb.append("\n");
		sb.append("R125,5\n");
		sb.append("D14\n");
		sb.append("N\n");
		sb.append("b200,15,D,h3,\"" + aBarcode + "\"\n");
		sb.append("A260,15,0,1,1,2,N,\"" + aBarcode + "\"\n");
		sb.append("A260,45,0,1,1,2,N,\"" + dateOfSample + "\"\n");
		sb.append("A260,75,0,1,1,2,N,\"" + result + "\"\n");
		sb.append("P1\n");
	}// end printBarcode()

}
