package au.org.spark.util;

import java.util.ArrayList;
import java.util.List;

public final class Constants {

	public static enum DATA_CENTERS {
		SSH_TEST, SSH_LOCAL, FTP_TEST
	};

	public static final List<String> listDataCenters() {
		ArrayList<String> list = new ArrayList<String>();
		for (DATA_CENTERS center : DATA_CENTERS.values()) {
			list.add(center.toString());
		}
		return list;
	}
}
