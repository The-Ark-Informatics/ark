package org.wager.barcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BDSBiospecimenBarcode extends DNABankBarcode {
	static private final String REAL_NUMBER = "^[-+]?\\d+(\\.\\d+)?$";

	@Override
	public Vector<BarcodeData> executeBiospecimenSelect(String strDomain,
			int domainkey) throws SQLException {

		Vector<BarcodeData> ts = new Vector<BarcodeData>();
		Connection conn = DriverManager.getConnection("jdbc:poolman://oracle");

		Statement stmt = conn.createStatement();

		ResultSet rset = null;
		if (strDomain.equals("SINGLE_BIOSPECIMEN")) {
			rset = stmt
					.executeQuery("select biospecimenid, to_char(z.dob,'dd/mm/yyyy'),zi.data_value from ix_biospecimen b, zeus.subject z, zeus.subject_info zi, zeus.subject_info_field f  "
							+ "where z.subjectkey = b.patientkey "
							+ "and b.biospecimenkey = "
							+ domainkey
							+ " and b.deleted=0 "
							+ " and z.subjectkey = zi.subjectkey "
							+ " and zi.sifieldkey = f.sifieldkey and f.field_name ='BSN_BLOOD_ID' "
							+ "order by biospecimenid");

		} else
			rset = stmt
					.executeQuery("select biospecimenid, to_char(z.dob,'dd/mm/yyyy') ,zi.data_value from ix_biospecimen b, zeus.subject z, zeus.subject_info zi, zeus.subject_info_field f  "
							+ "where z.subjectkey = b.patientkey "
							+ "and z.subjectkey = "
							+ domainkey
							+ " and b.deleted=0 "
							+ " and z.subjectkey = zi.subjectkey "
							+ "and zi.sifieldkey = f.sifieldkey and f.field_name ='BSN_BLOOD_ID' "
							+ "order by biospecimenid");

		while (rset.next())

		{

			
			String biospecimenid = rset.getString(1);
			String thisDob = rset.getString(2);
			if (thisDob == null) thisDob = new String("");
			
			String bsn_id = rset.getString(3);
			if (bsn_id == null) bsn_id= new String("");
			BarcodeData b = new BarcodeData(biospecimenid, thisDob);
			b.setValue("BSN_ID", bsn_id);
			ts.add(b);

		}

		rset.close();
		stmt.close();
		conn.close();

		return ts;

	}// end executeSelect()

	@Override
	public void printBarcode(BarcodeData b, StringBuffer sb)
	/*
	 * This method should be edited to suit the particular printing requirements
	 * for a study.
	 */
	{
		String aBarcode = b.getValue(BIOSPECIMEN_ID);
		String bsn_id = b.getValue("BSN_ID");
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
		sb.append("N\n");

		sb.append("b180,20,D,h3,\"" + aBarcode + "\"\n");
		sb.append("A80,25,0,2,1,1,N,\"" + aBarcode.substring(0, 5) + "\"\n");
		sb.append("A80,45,0,2,1,1,N,\"" + secondLine + "\"\n");
		sb.append("A95,60,0,2,1,1,N,\"" + lastLineOfCircle + "\"\n");
		sb.append("A240,20,0,1,1,2,N,\"" + aBarcode + "\"\n");

		if (bsn_id != null)
			sb.append("A240,50,0,1,1,2,N,\"" + bsn_id + "\"\n");
		sb.append("P1\n");
	}

}
