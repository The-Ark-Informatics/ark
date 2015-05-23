package au.org.spark.service;

import java.util.List;

import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;

public interface SshService {
	
	public List<String> listFiles(String directory) throws Exception;
	
	public List<DataSourceVo> listFilesAndDirectories(String directory, String fileName) throws Exception;
	
	public void readFile(String directory, String fileName) throws Exception;
	
	public void processMapFile(DataSourceVo dataSource) throws Exception;
	
	public void processPlinkDataSource(DataCenterVo dataSource) throws Exception;
	
}
