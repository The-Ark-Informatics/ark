package au.org.spark.web.view;

import java.io.Serializable;

public class QueryVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String microServiceId;
	
	private String dataCentre;
	
	private String directory;
	
	private String dataSourceId;
	
	private String parentDataSourceId;
	
	private String individualId;
	
	private String familyId;
	
	private String sex;
	
	private String affected;
	
	private String output;
	
	private String[] idValues;
	

	public String getMicroServiceId() {
		return microServiceId;
	}

	public void setMicroServiceId(String microServiceId) {
		this.microServiceId = microServiceId;
	}

	public String getDataCentre() {
		return dataCentre;
	}

	public void setDataCentre(String dataCentre) {
		this.dataCentre = dataCentre;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAffected() {
		return affected;
	}

	public void setAffected(String affected) {
		this.affected = affected;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String[] getIdValues() {
		return idValues;
	}

	public void setIdValues(String[] idValues) {
		this.idValues = idValues;
	}

	public String getParentDataSourceId() {
		return parentDataSourceId;
	}

	public void setParentDataSourceId(String parentDataSourceId) {
		this.parentDataSourceId = parentDataSourceId;
	}

}
