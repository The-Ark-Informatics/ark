package au.org.theark.core.model.spark.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "ANALYSIS", schema = Constants.SPARK_SCHEMA)
public class Analysis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String status;
	
	private MicroService microService;
	
	private DataSource dataSource;
	
	private Computation computation;
	
	private String parameters;
	
	private String result;
	
	private String jobId;
	
	private String checksum;
	
	private String scriptName;

	private String scriptId;

	@Id
	@SequenceGenerator(name = "analysis_generator", sequenceName = "ANALYSIS_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "analysis_generator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MICRO_SERVICE_ID")
	public MicroService getMicroService() {
		return microService;
	}

	public void setMicroService(MicroService microService) {
		this.microService = microService;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DATA_SOURCE_ID")
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPUTATION_ID")
	public Computation getComputation() {
		return computation;
	}

	public void setComputation(Computation computation) {
		this.computation = computation;
	}

	@Column(name = "PARAMETERS")
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Column(name = "RESULT")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "JOB_ID")
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Column(name = "CHECKSUM")
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name = "SCRIPT_NAME")
	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	@Column(name = "SCRIPT_ID")
	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}
	
}
