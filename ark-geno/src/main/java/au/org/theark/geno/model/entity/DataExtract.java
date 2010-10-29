package au.org.theark.geno.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.geno.service.Constants;

/**
 * DataExtract entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.DataExtract")
@Table(name = "DATA_EXTRACT", schema = Constants.GENO_TABLE_SCHEMA)
public class DataExtract implements java.io.Serializable {

	// Fields

	private Long id;
	private FileFormat fileFormat;
	private DataSet dataSet;
	private Long buildRev;
	private Long progress;
	private String filename;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;

	// Constructors

	/** default constructor */
	public DataExtract() {
	}

	/** minimal constructor */
	public DataExtract(Long id, FileFormat fileFormat, DataSet dataSet,
			Long buildRev, Long progress, String filename, String userId,
			String insertTime) {
		this.id = id;
		this.fileFormat = fileFormat;
		this.dataSet = dataSet;
		this.buildRev = buildRev;
		this.progress = progress;
		this.filename = filename;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public DataExtract(Long id, FileFormat fileFormat, DataSet dataSet,
			Long buildRev, Long progress, String filename, String userId,
			String insertTime, String updateUserId, String updateTime) {
		this.id = id;
		this.fileFormat = fileFormat;
		this.dataSet = dataSet;
		this.buildRev = buildRev;
		this.progress = progress;
		this.filename = filename;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID", nullable = false)
	public FileFormat getFileFormat() {
		return this.fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DATA_SET_ID", nullable = false)
	public DataSet getDataSet() {
		return this.dataSet;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	@Column(name = "BUILD_REV", nullable = false, precision = 22, scale = 0)
	public Long getBuildRev() {
		return this.buildRev;
	}

	public void setBuildRev(Long buildRev) {
		this.buildRev = buildRev;
	}

	@Column(name = "PROGRESS", nullable = false, precision = 22, scale = 0)
	public Long getProgress() {
		return this.progress;
	}

	public void setProgress(Long progress) {
		this.progress = progress;
	}

	@Column(name = "FILENAME", nullable = false)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "INSERT_TIME", nullable = false)
	public String getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "UPDATE_TIME")
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}