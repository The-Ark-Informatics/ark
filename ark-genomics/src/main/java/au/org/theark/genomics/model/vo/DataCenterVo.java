package au.org.theark.genomics.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.MicroService;

public class DataCenterVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MicroService microService;
	
	private String name;
	
	private String directory;
	
	private String fileName;
	
	private String status;
	
	private String individualId;
	
	private String outputType;
	
	private DataSource dataSource;
	
	private List<DataSourceVo> dataSourceList;	

	private List<DataSource> dataSourceEntityList;	

	public DataCenterVo() {
		this.dataSourceList = new ArrayList<DataSourceVo>();
		this.dataSourceEntityList = new ArrayList<DataSource>();
		this.dataSource = new DataSource();
	}

	public MicroService getMicroService() {
		return microService;
	}

	public void setMicroService(MicroService microService) {
		this.microService = microService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<DataSourceVo> getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(List<DataSourceVo> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public List<DataSource> getDataSourceEntityList() {
		return dataSourceEntityList;
	}

	public void setDataSourceEntityList(List<DataSource> dataSourceEntityList) {
		this.dataSourceEntityList = dataSourceEntityList;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
