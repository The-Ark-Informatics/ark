package au.org.spark.util;

import java.util.ArrayList;
import java.util.List;

import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;

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
	
	public static final String dataSourceTableName(DataSourceVo dataSourceVo){
		return (dataSourceVo.getDataCenterName()+dataSourceVo.getPath().replaceAll("[^A-Za-z0-9]", "_")).toUpperCase();
	}
	
	public static final String dataCenterTablePrefix(DataCenterVo dataCenterVo){
		return (dataCenterVo.getName()+"_"+dataCenterVo.getDirectory().replaceAll("[^A-Za-z0-9]", "_")).toUpperCase();
	}
	
//	public static void main(String[] args) {
//		String path = "/plink-bin/100SAMPLE.ped";
////		System.out.println("ABC_"+path.replaceAll("[./-]", "_"));
//		System.out.println("ABC"+path.replaceAll("[^A-Za-z0-9]", "_"));
//	}
	
}
