package org.wager.barcode;

import java.io.*;
import java.util.Hashtable;

import org.jasig.portal.RDBMServices;
import java.sql.*;

import neuragenix.dao.*;

public class WAFSSBio implements BarcodeEngine {

	public InputStream getBarcode(Hashtable params) {
		 StringBuffer sb = new StringBuffer();
		 
		 String patientkey = (String) params.get("PATIENT_intInternalPatientID");
         String admissionid = (String) params.get("BIOSPECIMEN_strEncounter");
         try{
         Connection con = RDBMServices.getConnection();
         Statement stmt = con.createStatement();
         ResultSet rset = stmt.executeQuery(
                 "select patientid, to_char(dob, 'dd/mm/yyyy'), to_char(admissiondate, 'dd/mm/yyyy'), familyid, otherid, ref_doctor, sex " +
                 "from ix_patientv2 p,ix_admissions adm " +
                 "where p.patientkey = adm.patientkey " +
                 "and adm.admissionid = " + admissionid +
                 "and adm.deleted = 0" +
                 "and p.patientkey = " + patientkey);
         
         String patientid = rset.getString(1);
         String dob = rset.getString(2);
         String admissiondate = rset.getString(3);
         String familyid = rset.getString(4);
         String asrbno = rset.getString(5);
         String researcher = rset.getString(6);
         String sex = rset.getString(7);

        
         
         sb.append("\n");
         sb.append("D15\n");
         sb.append("N\n");
         sb.append("A240,10,1,2,1,1,N,\"ID: " + patientid + " Family ID:" + familyid + "\"" + "\n");
         sb.append("A220,10,1,2,1,1,N,\"ASRB No:" + asrbno + "\"" + "\n");
         //sb.append("A180,10,1,2,1,1,N,\"ASRB No:" + asrbno + +"\"" + "\n");
         //sb.append("A160,10,1,2,1,1,N,\"Collection Date: " + admissiondate + +"\"" + "\n");
         sb.append("A200,10,1,2,1,1,N,\"Collection Date: " + admissiondate + "\"" + "\n");
         sb.append("A180,10,1,2,1,1,N,\"Researcher: " + researcher + "\"" + "\n");
         sb.append("A160,10,1,2,1,1,N,\"DOB: " + dob + " " + "Sex: " + sex + "\"" + "\n");
         //sb.append("A120,10,1,2,1,1,N,\"Sex: " + sex + +"\"" + "\n");
         sb.append("P1\n");

         }catch(SQLException se) {
        	 se.printStackTrace(System.err);
         }
		// TODO Auto-generated method stub
		return new ByteArrayInputStream(sb.toString().getBytes());
		
	}

}
