package au.org.theark.gdmi.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * MetaDataField entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "META_DATA_FIELD", schema = "GDMI")
public class MetaDataField implements java.io.Serializable {

	// Fields

	private long id;
	private MetaDataType metaDataType;
	private long studyId;
	private String name;
	private String description;
	private String units;
	private long seqNum;
	private String minValue;
	private String maxValue;
	private String discreteValues;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;
	private Set<MetaData> metaDatas = new HashSet<MetaData>(0);

	// Constructors

	/** default constructor */
	public MetaDataField() {
	}

	/** minimal constructor */
	public MetaDataField(long id, MetaDataType metaDataType, long studyId, String name,
			String userId, String insertTime) {
		this.id = id;
		this.metaDataType = metaDataType;
		this.studyId = studyId;
		this.name = name;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public MetaDataField(long id, MetaDataType metaDataType, long studyId, String name,
			String description, String units, long seqNum, String minValue,
			String maxValue, String discreteValues, String userId,
			String insertTime, String updateUserId, String updateTime,
			Set<MetaData> metaDatas) {
		this.id = id;
		this.metaDataType = metaDataType;
		this.studyId = studyId;
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
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
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

	@Column(name = "STUDY_ID", nullable = false, precision = 22, scale = 0)
	public long getStudyId() {
		return this.studyId;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
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
	public long getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(long seqNum) {
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metaDataField")
	public Set<MetaData> getMetaDatas() {
		return this.metaDatas;
	}

	public void setMetaDatas(Set<MetaData> metaDatas) {
		this.metaDatas = metaDatas;
	}

}