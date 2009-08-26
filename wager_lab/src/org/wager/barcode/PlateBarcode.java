package org.wager.barcode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

import org.wager.barcode.AbstractBarcodeEngine.BarcodeData;

import neuragenix.security.AuthToken;

public class PlateBarcode extends AbstractBarcodeEngine {

	@Override
	public void printBarcode(BarcodeData b, StringBuffer sb) {
		// TODO Auto-generated method stub
		sb.append("q401\n");
		sb.append("D14\n");
		sb.append("N\n");
		sb.append("B30,0,0,1,1,2,23,N,\"" + b.getValue(BIOSPECIMEN_ID) + "\"\n");
		sb.append("A230,0,0,1,1,1,N,\"" + b.getValue(BIOSPECIMEN_ID) + " PLATE\n");
		sb.append("A230,15,0,1,1,1,N,\"" + b.getValue("CAPACITY") +"W 13/03/2008\"\n");
		sb.append("P1\n");

	}

	@Override
	public InputStream getBarcode(String strDomain, int domainKey,
			AuthToken authtoken) throws SQLException {
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();

		if (strDomain.equals("PLATE")) {
				
			try {
				Vector<BarcodeData> ts = executePlateSelect(strDomain, domainKey);
				for (Iterator<BarcodeData> i = ts.iterator(); i.hasNext();) {
					BarcodeData b = (BarcodeData) i.next();
					printBarcode(b, sb);

				}
			} catch (SQLException sqle) {
				sqle.printStackTrace(System.err);
				return null;
			}
		}
			return new ByteArrayInputStream(sb.toString().getBytes());

	}

		public Vector<BarcodeData> executePlateSelect(String strDomain,
				int domainkey) throws SQLException {

		Vector<BarcodeData> ts = new Vector<BarcodeData>();
		Connection conn = DriverManager.getConnection("jdbc:poolman://oracle");

		Statement stmt = conn.createStatement();

		ResultSet rset = null;
	
			rset = stmt
					.executeQuery("select name,capacity from ix_inv_tray where "
							+ "traykey = " + domainkey);

		
		
		while (rset.next())

		{

			String biospecimenid = rset.getString(1);
			String capacity = rset.getString(2);
			BarcodeData b = new BarcodeData();
			b.setValue(BIOSPECIMEN_ID, biospecimenid);
			b.setValue("CAPACITY", capacity);
			ts.add(b);

		}

		rset.close();
		stmt.close();
		conn.close();

		return ts;
		}
		
}
