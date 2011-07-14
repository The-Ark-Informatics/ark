package au.org.theark.core.model.study.entity;

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
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * SubjectCustmFld entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECT_CUSTM_FLD", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class SubjectCustmFld implements java.io.Serializable {

	// Fields

	private Long id;
	private Study study;
	private DataType dataType;
	private String fieldTitle;
	private String name;
	private String description;
	private String minValue;
	private String maxValue;
	private String discreteValues;
	private Long fieldPostion;
	
	private Set<SubjectCustFldDat> subjectCustFldDats = new HashSet<SubjectCustFldDat>(
			0);

	// Constructors

	/** default constructor */
	public SubjectCustmFld() {
	}

	/** minimal constructor */
	public SubjectCustmFld(Long id, Study study,
			DataType dataType) {
		this.id = id;
		this.study = study;
		this.dataType = dataType;
	}

	/** full constructor 
	 * @param minValue 
	 * @param maxValue 
	 * @param discreteValues */
	public SubjectCustmFld(Long id, Study study,
			DataType dataType, String name, String description, 
			String minValue, String maxValue, String discreteValues,
			Set<SubjectCustFldDat> subjectCustFldDats) {
		this.id = id;
		this.study = study;
		this.dataType = dataType;
		this.name = name;
		this.description = description;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.discreteValues = discreteValues;
		this.subjectCustFldDats = subjectCustFldDats;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="subject_fld_gen", sequenceName="SUB_FLD_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "subject_fld_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DATA_TYPE_ID")
	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@Column(name = "NAME", unique = true, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length=255)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "MIN_VALUE")
	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	@Column(name = "MAX_VALUE")
	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "DISCRETE_VALUES")
	public String getDiscreteValues() {
		return discreteValues;
	}

	public void setDiscreteValues(String discreteValues) {
		this.discreteValues = discreteValues;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subjectCustmFld")
	public Set<SubjectCustFldDat> getSubjectCustFldDats() {
		return this.subjectCustFldDats;
	}

	public void setSubjectCustFldDats(Set<SubjectCustFldDat> subjectCustFldDats) {
		this.subjectCustFldDats = subjectCustFldDats;
	}

	@Column(name = "FIELD_POSITION",  nullable = false, precision = 22, scale = 0)
	public Long getFieldPostion() {
		return fieldPostion;
	}

	public void setFieldPostion(Long fieldPostion) {
		this.fieldPostion = fieldPostion;
	}

	@Column(name = "FIELD_TITLE", unique = true, length = 255)
	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

}