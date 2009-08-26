package org.wager.barcode;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import neuragenix.security.AuthToken;

import org.jasig.portal.ChannelRuntimeData;

public class BarcodeManager {
	static final int SINGLE_BIOSPECIMEN = 1;
	static final int BULK_BIOSPECIMEN = 2;
	static final int SINGLE_ADMISSION = 3;
	static final int SINGLE_PLATE = 4;

	public static String barcodeSupported(Vector<Integer> siteList, String domain,
			int studyKey) {
		Connection conn;
		
		try {
			
			conn = DriverManager.getConnection("jdbc:poolman://oracle");
			PreparedStatement ps = conn
					.prepareStatement("SELECT CLASS from IX_BARCODEENGINE where  SITEKEY in " + getSiteIDSQLString(siteList) + " and DOMAIN=? and (STUDYKEY=? or STUDYKEY=0) order by STUDYKEY DESC, SITEKEY DESC");
			
			ps.setString(1, domain.toUpperCase());
			ps.setInt(2, studyKey);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return null;// Grab the first.
			String className = rs.getString("CLASS");
			return className;
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	 public static String getSiteIDSQLString (Vector<Integer> sitekeys){
		  // System.err.println("Studykeys size: " + sitekeys.size());
	       StringBuffer in_expr = new StringBuffer();
	       in_expr.append("(");
	       try {
	       if (sitekeys.size() != 0) {
	       for (int i=0; i < sitekeys.size(); i++) {
	    	   Integer j = sitekeys.get(i);
	       	in_expr.append(j.toString());
	       if (i < sitekeys.size() - 1)
	       	in_expr.append(",");
	       }
	       }
	       else 
	       	in_expr.append("null");
	       
	       in_expr.append(")");
	       } catch (ClassCastException nfe) {
	    	  in_expr = new StringBuffer("(null)");
	       }
	   	return in_expr.toString();
	   	
	   }

	public static InputStream generateBarcode(AuthToken authToken,
			ChannelRuntimeData runtimeData) {
		String domain = runtimeData.getParameter("domain");
		String studyKeystr = runtimeData.getParameter("studykey");
		String domainKeyStr = runtimeData.getParameter("domainkey");
		if (domain == null || studyKeystr == null || domainKeyStr == null)
			 return null;
		int studyKey = Integer.parseInt(studyKeystr);
		int domainKey = Integer.parseInt(domainKeyStr);
		Vector<Integer> siteList = authToken.getSiteList();
		if (!siteList.contains(new Integer(0))) {
			siteList.add(new Integer(0));
		}
		String classStr = barcodeSupported(siteList,domain,studyKey);
		if (classStr == null ) {
			return null;
		}
		System.out.println("Loading class: " + classStr );
		//ClassLoader cl = ClassLoader.getSystemClassLoader();
		try {
			Class<?> c =  Class.forName(
					classStr);
			
			BarcodeEngine e = (BarcodeEngine) c.newInstance();

			return e.getBarcode(domain,domainKey, authToken);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
