package au.org.theark.core.model.pheno.entity;

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

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;

@Entity
@Table(name = "PHENO_DATASET_FIELD", schema = Constants.PHENO_TABLE_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PhenoDataSetField implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private FieldType fieldType;
	private Study study;
	private ArkFunction arkFunction;
	private UnitType unitType;
	private String minValue;
	private String maxValue;
	private String encodedValues;
	private String missingValue;
	private Boolean phenoFieldHasData;
	private String fieldLabel;
	private String defaultValue;
	//Add unit type as String
	private String unitTypeInText;
	private Boolean required;
	private Boolean allowMultiselect = Boolean.FALSE;

	//private Set<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplay = new HashSet<PhenoDataSetFieldDisplay>();

	public PhenoDataSetField(){
		
	}
	@Id
	@SequenceGenerator(name = "pheno_dataset_field_seq_gen", sequenceName = "PHENO_DATASET_FIELD_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pheno_dataset_field_seq_gen")
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
	@JoinColumn(name = "ARK_FUNCTION_ID", nullable = false)
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	@Column(name = "NAME", nullable = false, length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_TYPE_ID", nullable = false)
	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIT_TYPE_ID")
	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
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

	@Column(name = "HAS_DATA", precision = 1, scale = 0)
	public Boolean getPhenoFieldHasData() {
		return phenoFieldHasData;
	}

	public void setPhenoFieldHasData(Boolean customFieldHasData) {
		this.phenoFieldHasData = customFieldHasData;
	}

	@Column(name = "PHENO_FIELD_LABEL", length = 255)
	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	@Column(name = "DEFAULT_VALUE")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name="UNIT_TYPE_IN_TEXT")
	public String getUnitTypeInText() {
		return unitTypeInText;
	}

	public void setUnitTypeInText(String unitTypeInText) {
		this.unitTypeInText = unitTypeInText;
	}

	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "phenoDataSetField")
	public Set<PhenoDataSetFieldDisplay> getPhenoDataSetFieldDisplay() {
		return phenoDataSetFieldDisplay;
	}

	public void setPhenoDataSetFieldDisplay(Set<PhenoDataSetFieldDisplay> phenoDataSetFieldDisplay) {
		this.phenoDataSetFieldDisplay = phenoDataSetFieldDisplay;
	}*/
	@Column(name = "REQUIRED")
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	@Column(name = "ALLOW_MULTIPLE_SELECTION", precision = 1, scale = 0)
	public Boolean getAllowMultiselect() {
		return allowMultiselect;
	}
	public void setAllowMultiselect(Boolean allowMultiselect) {
		this.allowMultiselect = allowMultiselect;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhenoDataSetField other = (PhenoDataSetField) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
