package au.org.theark.core.model.pheno.entity;

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
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.audit.annotations.ArkAuditDisplay;
import au.org.theark.core.model.Constants;

@Entity
@Table(name = "PHENO_DATASET_FIELD_DISPLAY", schema = Constants.PHENO_TABLE_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PhenoDataSetFieldDisplay {
	
	
	private Long id;
	private PhenoDataSetField phenoDataSetField;
	private PhenoDataSetGroup phenoDataSetGroup;
	private Boolean required;
	private String requiredMessage;
	private Boolean allowMultiselect = Boolean.FALSE;
	private Long sequence;
	private Set<PhenoData> phenoData = new HashSet<PhenoData>();
	protected String descriptiveNameIncludingCFGName;

	public PhenoDataSetFieldDisplay() {

	}
	
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_DATASET_FIELD_ID", nullable = false)
	public PhenoDataSetField getPhenoDataSetField() {
		return phenoDataSetField;
	}

	public void setPhenoDataSetField(PhenoDataSetField phenoDataSetField) {
		this.phenoDataSetField = phenoDataSetField;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_DATASET_FIELD_GROUP_ID")
	public PhenoDataSetGroup getPhenoDataSetGroup() {
		return phenoDataSetGroup;
	}

	public void setPhenoDataSetGroup(PhenoDataSetGroup phenoDataSetGroup) {
		this.phenoDataSetGroup = phenoDataSetGroup;
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

	@Column(name = "SEQUENCE", precision = 22, scale = 0)
	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	//TODO: Remove NotAudited when pheno auditing is done
	@NotAudited
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "phenoDataSetFieldDisplay")customFieldDisplay
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customFieldDisplay")
	public Set<PhenoData> getPhenoData() {
		return phenoData;
	}

	public void setPhenoData(Set<PhenoData> phenoData) {
		this.phenoData = phenoData;
	}

	@ArkAuditDisplay
	@Transient
	public String getDescriptiveNameIncludingCFGName() {
		return descriptiveNameIncludingCFGName;
	}

	public void setDescriptiveNameIncludingCFGName(
			String descriptiveNameIncludingCFGName) {
		this.descriptiveNameIncludingCFGName = descriptiveNameIncludingCFGName;
	}

	

	
}
