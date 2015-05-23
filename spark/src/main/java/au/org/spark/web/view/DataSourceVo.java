package au.org.spark.web.view;

import java.io.Serializable;

public class DataSourceVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;

	private String directory;

	private String path;

	private String status;
	
	private String dataCenterName;
	
	private String directoryName;
	
	public DataSourceVo(){
		
	}

	public DataSourceVo(String fileName, String directory, String path, String status) {
		this.fileName = fileName;
		this.directory = directory;
		this.path = path;
		this.status = status;
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

	public String getDataCenterName() {
		return dataCenterName;
	}

	public void setDataCenterName(String dataCenterName) {
		this.dataCenterName = dataCenterName;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	

}
