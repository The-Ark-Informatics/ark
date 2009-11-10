package org.wager.barcode;

import java.io.*;

import org.jasig.portal.RDBMServices;

import java.sql.*;

import neuragenix.security.AuthToken;

public class WAFSSAdmissionBarcode extends AbstractBarcodeEngine {
	
	
	@Override
	public InputStream getBarcode(String strDomain, int domainKey,
			AuthToken authtoken) {
		// TODO Auto-generated method stub
		 StringBuffer sb = new StringBuffer();
		 if (!strDomain.equals("ADMISSIONS")) 
			 return null;
		 
		 int admissionkey = domainKey;
				
         try{
         Connection con = RDBMServices.getConnection();
         Statement stmt = con.createStatement();
         ResultSet rset = stmt.executeQuery(
                 "select patientid, to_char(dob, 'dd/mm/yyyy'), to_char(admissiondate, 'dd/mm/yyyy'), familyid, otherid, ref_doctor, sex " +
                 "from ix_patientv2 p,ix_admissions adm " +
                 "where p.patientkey = adm.patientkey " +
                 "and adm.admissionkey = '" + admissionkey + "'" +
                 "and adm.deleted = 0");
         while (rset.next()) {
         String patientid = rset.getString(1);
         String dob = rset.getString(2);
         String admissiondate = rset.getString(3);
         String familyid = rset.getString(4);
         String asrbno = rset.getString(5);
         String researcher = rset.getString(6);
         String sex = rset.getString(7);
         BarcodeData b = new BarcodeData();
         b.setValue("PATIENTID", patientid);
         b.setValue(DATE_OF_BIRTH,dob);
         b.setValue("ADMISSIONDATE",admissiondate);
         b.setValue("ASBRNO",asrbno);
         b.setValue("FAMILYID",familyid);
         b.setValue("RESEARCHER",researcher);
         b.setValue("SEX",sex);
         printBarcode(b,sb);
         rset.close();
         stmt.close();
         con.close();
         }
         }catch(SQLException se) {
        	 se.printStackTrace(System.err);
         }
		// TODO Auto-generated method stub
       
         
		return new ByteArrayInputStream(sb.toString().getBytes());
		
	}

	@Override
	public void printBarcode(BarcodeData b, StringBuffer sb) {
		
		String patientid = b.getValue("PATIENTID");
		String familyid = b.getValue("FAMILYID");
		String asrbno = b.getValue("ASRBNO");
		String admissiondate = b.getValue("ADMISSIONDATE");
		String researcher = b.getValue("RESEARCHER");
		String dob = b.getValue(DATE_OF_BIRTH);
		String sex = b.getValue("SEX");
		 sb.append("\n");
         sb.append("D15\n");
         sb.append("N\n");
         sb.append("A240,10,1,2,1,1,N,\"ID: " + patientid + " Family ID:" + familyid + "\"" + "\n");
         sb.append("A220,10,1,2,1,1,N,\"ASRB No:" + asrbno + "\"" + "\n");
         sb.append("A200,10,1,2,1,1,N,\"Collection Date: " + admissiondate + "\"" + "\n");
         sb.append("A180,10,1,2,1,1,N,\"Researcher: " + researcher + "\"" + "\n");
         sb.append("A160,10,1,2,1,1,N,\"DOB: " + dob + " " + "Sex: " + sex + "\"" + "\n");
         sb.append("P1\n");
	}

	}	


