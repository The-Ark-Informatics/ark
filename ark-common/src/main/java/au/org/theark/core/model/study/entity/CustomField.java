package au.org.theark.core.model.study.entity;

import java.io.Serializable;
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

import au.org.theark.core.model.Constants;




/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "FIELD", schema = Constants.STUDY_SCHEMA)
public class CustomField implements Serializable {
	

	private Long id;
	private Study study;
	private FieldType fieldType;
	private String	name;
	private String	description;
	private String	units;
	private Long	seqNum;
	private String	minValue;
	private String	maxValue;
	private String	encodedValues;
	private String	missingValue;
	
	/**
	 * Constructor
	 */
	public CustomField(){
		
	}

	@Id
	@SequenceGenerator(name = "custom_field_seq_gen", sequenceName = "CUSTOM_FIELD_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_TYPE_ID", nullable = false)
	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	@Column(name = "NAME", nullable = false, length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	@Column(name = "DESCRIPTION", nullable = false, length = 1024)
	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name = "UNITS", length = 50)
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	@Column(name = "SEQ_NUM", precision = 22, scale = 0)
	public Long getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
	}

	@Column(name = "MIN_VALUE", length = 100)
	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	@Column(name = "MAX_VALUE", length = 100)
	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "ENCODED_VALUES")
	public String getEncodedValues() {
		return encodedValues;
	}

	public void setEncodedValues(String encodedValues) {
		this.encodedValues = encodedValues;
	}

	@Column(name = "MISSING_VALUE")
	public String getMissingValue() {
		return missingValue;
	}

	public void setMissingValue(String missingValue) {
		this.missingValue = missingValue;
	}


}
