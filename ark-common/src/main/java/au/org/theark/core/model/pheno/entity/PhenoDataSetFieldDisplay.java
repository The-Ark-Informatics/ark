package au.org.theark.core.model.pheno.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.audit.annotations.ArkAuditDisplay;
import au.org.theark.core.model.Constants;

@Entity
@Table(name = "PHENO_DATASET_FIELD_DISPLAY", schema = Constants.PHENO_TABLE_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PhenoDataSetFieldDisplay implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private PhenoDataSetGroup phenoDataSetGroup;
	private PhenoDataSetCategory phenoDataSetCategory;
	private PhenoDataSetCategory parentPhenoDataSetCategory;
	private Long phenoDataSetCategoryOrderNumber;
	private PhenoDataSetField phenoDataSetField;
	private Long phenoDataSetFiledOrderNumber;
	private Boolean required;
	private String requiredMessage;
	private Boolean allowMultiselect = Boolean.FALSE;
	private Set<PhenoDataSetData> phenoDataSetData = new HashSet<PhenoDataSetData>();
	protected String descriptiveNameIncludingCFGName;

	
	
	@Id
	@SequenceGenerator(name = "pheno_dataset_field_display_seq_gen", sequenceName = "PHENO_DATASET_FIELD_DISPLAY_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pheno_dataset_field_display_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PHENO_DATASET_FIELD_GROUP_ID")
	public PhenoDataSetGroup getPhenoDataSetGroup() {
		return phenoDataSetGroup;
	}

	public void setPhenoDataSetGroup(PhenoDataSetGroup phenoDataSetGroup) {
		this.phenoDataSetGroup = phenoDataSetGroup;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PHENO_DATASET_CATEGORY_ID", nullable = false)
	public PhenoDataSetCategory getPhenoDataSetCategory() {
		return phenoDataSetCategory;
	}
	public void setPhenoDataSetCategory(PhenoDataSetCategory phenoDataSetCategory) {
		this.phenoDataSetCategory = phenoDataSetCategory;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_PHENO_DATASET_CATEGORY_ID")
	public PhenoDataSetCategory getParentPhenoDataSetCategory() {
		return parentPhenoDataSetCategory;
	}
	public void setParentPhenoDataSetCategory(
			PhenoDataSetCategory parentPhenoDataSetCategory) {
		this.parentPhenoDataSetCategory = parentPhenoDataSetCategory;
	}
	@Column(name = "PHENO_DATASET_CATEGORY_ORDER_NUMBER", nullable = false )
	public Long getPhenoDataSetCategoryOrderNumber() {
		return phenoDataSetCategoryOrderNumber;
	}
	public void setPhenoDataSetCategoryOrderNumber(
			Long phenoDataSetCategoryOrderNumber) {
		this.phenoDataSetCategoryOrderNumber = phenoDataSetCategoryOrderNumber;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_DATASET_FIELD_ID", nullable = false)
	public PhenoDataSetField getPhenoDataSetField() {
		return phenoDataSetField;
	}
	public void setPhenoDataSetField(PhenoDataSetField phenoDataSetField) {
		this.phenoDataSetField = phenoDataSetField;
	}
	@Column(name = "PHENO_DATASET_FIELD_ORDER_NUMBER", nullable = true )
	public Long getPhenoDataSetFiledOrderNumber() {
		return phenoDataSetFiledOrderNumber;
	}
	public void setPhenoDataSetFiledOrderNumber(Long phenoDataSetFiledOrderNumber) {
		this.phenoDataSetFiledOrderNumber = phenoDataSetFiledOrderNumber;
	}
	
	@Column(name = "REQUIRED")
	public Boolean getRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Column(name = "REQUIRED_MESSAGE")
	public String getRequiredMessage() {
		return requiredMessage;
	}

	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}

	@Column(name = "ALLOW_MULTIPLE_SELECTION", precision = 1, scale = 0)
	public Boolean getAllowMultiselect() {
		return allowMultiselect;
	}

	public void setAllowMultiselect(Boolean allowMultiselect) {
		this.allowMultiselect = allowMultiselect;
	}
	//TODO: Remove NotAudited when pheno auditing is done
	//@NotAudited
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "phenoDataSetFieldDisplay")customFieldDisplay
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	/*public Set<PhenoDataSetData> getPhenoData() {
		return phenoData;
	}

	public void setPhenoData(Set<PhenoDataSetData> phenoData) {
		this.phenoData = phenoData;
	}*/
	
	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "phenoDataSetFieldDisplay")
	public Set<PhenoDataSetData> getPhenoDataSetData() {
		return phenoDataSetData;
	}
	public void setPhenoDataSetData(Set<PhenoDataSetData> phenoDataSetData) {
		this.phenoDataSetData = phenoDataSetData;
	}

	@ArkAuditDisplay
	@Transient
	public String getDescriptiveNameIncludingCFGName() {
		StringBuilder displayExpression = new StringBuilder();
		displayExpression.append(phenoDataSetGroup==null?"UNKNOWN":phenoDataSetGroup.getName());
		displayExpression.append(" > ");
		displayExpression.append(phenoDataSetField==null?"UNKNOWN":phenoDataSetField.getName());
		return displayExpression.toString();
	}
	
	public void setDescriptiveNameIncludingCFGName(
			String descriptiveNameIncludingCFGName) {
		this.descriptiveNameIncludingCFGName = descriptiveNameIncludingCFGName;
	}

	

	
}
