package org.wager.barcode;

import java.io.InputStream;

import neuragenix.security.AuthToken;

import org.jasig.portal.ChannelRuntimeData;

public class BarcodeManager {
	static final int SINGLE_BIOSPECIMEN = 1;
	static final int BULK_BIOSPECIMEN = 2;
	static final int SINGLE_ADMISSION = 3;
	static final int SINGLE_PLATE = 4;

	public static InputStream generateBarcode(AuthToken authToken,
			ChannelRuntimeData runtimeData) {
		int mode = -1;
		String domain = runtimeData.getParameter("domain").toUpperCase();
		int patientkey = -1;
		int biospecimenkey = -1;
		if (domain.equals("BIOSPECIMEN")) {
			String str_patientkey = runtimeData
			.getParameter("PATIENT_intInternalPatientID");
			String str_biospecimenkey = runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
			if (!str_patientkey.equals(null)) {
				try {
					patientkey = Integer.parseInt(str_patientkey);
					mode = BULK_BIOSPECIMEN;

				} catch (NumberFormatException ne) {
				}
			}
			else if (!str_biospecimenkey.equals(null)) {
				try {
					biospecimenkey = Integer.parseInt(str_biospecimenkey);
					mode = SINGLE_BIOSPECIMEN;

				} catch (NumberFormatException ne) {
				}
			}
			

		} else if (domain.equals("ADMISSION")) {

		} else if (domain.equals("INVENTORY")) {

		}

		ClassLoader cl = ClassLoader.getSystemClassLoader();
		try {
			BarcodeEngine e = (BarcodeEngine) Class.forName(
					"org.wager.barcode.WAFSSBio").newInstance();

			return e.getBarcode(runtimeData, authToken);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
