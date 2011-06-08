package au.org.theark.core.model.geno.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.Study;

/**
 * MetaDataField entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.MetaDataField")
@Table(name = "META_DATA_FIELD", schema = Constants.GENO_TABLE_SCHEMA)
public class MetaDataField implements java.io.Serializable {

	// Fields

	private Long id;
	private MetaDataType metaDataType;
	private Study study;
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
	private Set<MetaData> metaDatas = new HashSet<MetaData>(0);

	// Constructors

	/** default constructor */
	public MetaDataField() {
	}

	/** minimal constructor */
	public MetaDataField(Long id, MetaDataType metaDataType, Study study, String name,
			String userId, Date insertTime) {
		this.id = id;
		this.metaDataType = metaDataType;
		this.study = study;
		this.name = name;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public MetaDataField(Long id, MetaDataType metaDataType, Study study, String name,
			String description, String units, Long seqNum, String minValue,
			String maxValue, String discreteValues, String userId,
			Date insertTime, String updateUserId, Date updateTime,
			Set<MetaData> metaDatas) {
		this.id = id;
		this.metaDataType = metaDataType;
		this.study = study;
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
		this.metaDatas = metaDatas;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="MetaDataField_PK_Seq",sequenceName=Constants.META_DATA_FIELD_PK_SEQ)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="MetaDataField_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "META_DATA_TYPE_ID", nullable = false)
	public MetaDataType getMetaDataType() {
		return this.metaDataType;
	}

	public void setMetaDataType(MetaDataType metaDataType) {
		this.metaDataType = metaDataType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID", nullable = false)
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
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

	@Column(name = "USER_ID", nullable = false)
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

	@Column(name = "UPDATE_USER_ID")
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metaDataField")
	public Set<MetaData> getMetaDatas() {
		return this.metaDatas;
	}

	public void setMetaDatas(Set<MetaData> metaDatas) {
		this.metaDatas = metaDatas;
	}

}