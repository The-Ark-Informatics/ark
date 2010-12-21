package au.org.theark.phenotypic.model.entity;

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

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.phenotypic.service.Constants;

/**
 * fieldField entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FIELD", schema = Constants.TABLE_SCHEMA)
public class Field implements java.io.Serializable {

	// Fields
	private Long id;
	private Study study;
	private FieldType fieldType;
	private String name;
	private String description;
	private String units;
	private Long seqNum;
	private String minValue;
	private String maxValue;
	private String discreteValues;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;
	
	// Constructors

	/** default constructor */
	public Field() {
	}
	
	/** minimal constructor */
	public Field(Long id, FieldType fieldType, Long studyId, String name, String userId, Date insertTime) {
		this.id = id;
		this.fieldType = fieldType;
		this.name = name;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public Field(Long id, FieldType fieldType, String name,
			String description, String units, Long seqNum, String minValue,
			String maxValue, String discreteValues, String userId,
			Date insertTime, String updateUserId, Date updateTime) {
		this.id = id;
		this.fieldType = fieldType;
		this.name = name;
		this.description = description;
		this.units = units;
		this.seqNum = seqNum;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.discreteValues = discreteValues;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="Field_PK_Seq",sequenceName="PHENOTYPIC.FIELD_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="Field_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_TYPE_ID", nullable = false)
	public FieldType getFieldType() {
		return this.fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "UNITS", length = 50)
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "SEQ_NUM", precision = 22, scale = 0)
	public Long getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
	}

	@Column(name = "MIN_VALUE", length = 100)
	public String getMinValue() {
		return this.minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	@Column(name = "MAX_VALUE", length = 100)
	public String getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "DISCRETE_VALUES", length = 100)
	public String getDiscreteValues() {
		return this.discreteValues;
	}

	public void setDiscreteValues(String discreteValues) {
		this.discreteValues = discreteValues;
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

	/**
	 * @param study the study to set
	 */
	public void setStudy(Study study)
	{
		this.study = study;
	}

	/**
	 * @return the study
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy()
	{
		return study;
	}
}
