package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "STUDY_PEDIGREE_CONFIG", schema = Constants.STUDY_SCHEMA)
public class StudyPedigreeConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Study study;
	private CustomField customField;
	private Boolean dobAllowed;

	private Boolean statusAllowed;
	private Boolean ageAllowed;
	
	private Boolean inbreedAllowed;

	@Id
	@SequenceGenerator(name = "study_pedigree_config_generator", sequenceName = "STUDYPEDIGREECONFIG_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "study_pedigree_config_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID",
				referencedColumnName="ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_ID")
	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

	@Column(name = "DOB_ALLOWED")
	public Boolean isDobAllowed() {
		return dobAllowed;
	}

	public void setDobAllowed(Boolean dobAllowed) {
		this.dobAllowed = dobAllowed;
	}

	@Column(name = "STATUS_ALLOWED")
	public Boolean isStatusAllowed() {
		return statusAllowed;
	}

	public void setStatusAllowed(Boolean statusAllowed) {
		this.statusAllowed = statusAllowed;
	}

	@Column(name = "AGE_ALLOWED")
	public Boolean isAgeAllowed() {
		return ageAllowed;
	}

	public void setAgeAllowed(Boolean ageAllowed) {
		this.ageAllowed = ageAllowed;
	}

	@Column(name = "INBREED_ALLOWED")
	public Boolean getInbreedAllowed() {
		return inbreedAllowed;
	}

	public void setInbreedAllowed(Boolean inbreedAllowed) {
		this.inbreedAllowed = inbreedAllowed;
	}
	
}
