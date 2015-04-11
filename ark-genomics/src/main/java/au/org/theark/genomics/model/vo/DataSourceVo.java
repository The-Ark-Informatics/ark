package au.org.theark.genomics.model.vo;

import java.io.Serializable;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.genomics.util.Constants;

public class DataSourceVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;

	private String directory;

	private String path;

	private String status;
	
	private String dataCenter;
	
	private MicroService microService;
	
	private DataSource dataSource;
	
	private int mode;
	
	public DataSourceVo(){
		this.dataSource = new DataSource();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MicroService getMicroService() {
		return microService;
	}

	public void setMicroService(MicroService microService) {
		this.microService = microService;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public String getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void pupulateDataSource(){
		this.dataSource = new DataSource();
		this.dataSource.setMicroService(microService);
		this.dataSource.setPath(path);;
		this.dataSource.setDataCenter(dataCenter);;
		this.dataSource.setName(fileName);
		this.mode = Constants.MODE_NEW;
	}

}
