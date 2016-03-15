package au.org.spark.web.view;

import java.io.Serializable;

public class AnalysisVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer analysisId;
	private String programId;
	private String programName;
	private String sourcePath;
	private String sourceDataCenter;
	private Boolean sourceDir;
	private String parameters;
	private String result;
	private String scriptName;
	private String jobId;


	public Integer getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(Integer analysisId) {
		this.analysisId = analysisId;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getSourceDataCenter() {
		return sourceDataCenter;
	}

	public void setSourceDataCenter(String sourceDataCenter) {
		this.sourceDataCenter = sourceDataCenter;
	}

	public Boolean isSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(Boolean sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
