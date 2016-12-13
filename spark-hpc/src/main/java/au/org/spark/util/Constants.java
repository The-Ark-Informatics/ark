package au.org.spark.util;

import java.util.ArrayList;
import java.util.List;

import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;

public final class Constants {

	public static enum DATA_CENTERS {
//		SSH, SSH_LOCAL, FTP_TEST
		SSH_HPC
	};
	
	public static final String STATUS_PROCESSED="Processed";
	
	public static final String STATUS_PROCESSING="Processing";
	
	public static final String STATUS_UNPROCESSED="Unprocessed";
	
	public static final String STATUS_JOB_SUBMIT="Submitted";

	public static final String STATUS_JOB_SUBMIT_FAILED="Submission Failed";
	
	public static final String SPARK_INFO_MAKETYPE="maketype";	

	public static final String SPARK_INFO_MAKEPARAMETERS="makeparameter";	
	
	public static final String SPARK_INFO_MODE="mode";

	public static final String SPARK_INFO_AUTOMATIC_MODE="automatic";	

	public static final String SPARK_OUTPUT="output";	

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
		return ("ms"+dataCenterVo.getMicroserviceId() +"_"+ dataCenterVo.getName()+"_"+dataCenterVo.getDirectory().replaceAll("[^A-Za-z0-9]", "_")).toUpperCase();
	}
	
}
