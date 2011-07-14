package au.org.theark.core.model.pheno.entity;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;


/**
 * MetaData entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FIELD_DATA_LOG", schema = Constants.PHENO_TABLE_SCHEMA)
public class FieldDataLog implements java.io.Serializable {

	// Fields
	private Long id;
	private FieldData fieldData;
	private String comment;
	private String value;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public FieldDataLog() {
	}

	/** minimal constructor */
	public FieldDataLog(Long id, FieldData fieldData,	String comment, String userId, Date insertTime) {
		this.id = id;
		this.fieldData = fieldData;
		this.comment = comment;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public FieldDataLog(Long id,FieldData fieldData, String comment, String value, String userId, Date insertTime, String updateUserId, Date updateTime) {
		this.id = id;
		this.fieldData = fieldData;
		this.comment = comment;
		this.value = value;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="FieldDataLog_PK_Seq",sequenceName="PHENOTYPIC.FIELD_DATA_LOG_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="FieldDataLog_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_DATA_ID", nullable = false)
	public FieldData getFieldData() {
		return this.fieldData;
	}

	public void setFieldData(FieldData fieldData) {
		this.fieldData = fieldData;
	}

	@Column(name = "COMMENT", nullable = false)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name = "VALUE", length = 2000)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}