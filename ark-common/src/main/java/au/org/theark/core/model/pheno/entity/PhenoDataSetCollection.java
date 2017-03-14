package au.org.theark.core.model.pheno.entity;

import java.io.Serializable;
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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.audit.annotations.ArkAuditDisplay;
import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * @author nivedann
 *
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "PHENO_DATASET_COLLECTION", schema = Constants.PHENO_TABLE_SCHEMA)
public class PhenoDataSetCollection implements Serializable{

	private static final long	serialVersionUID	= 1L;

	private Long id;
	private String description;
	private LinkSubjectStudy linkSubjectStudy;
	private Date recordDate;
	private Set<PhenoDataSetData> phenoDataSetData = new HashSet<PhenoDataSetData>();
	private Date reviewedDate;
	private ArkUser reviewedBy;
	private PhenoDataSetGroup questionnaire;;
	private QuestionnaireStatus status;
	
	public  PhenoDataSetCollection(){		
	}
	
	@Id																			
	@SequenceGenerator(name = "phenotypic_collection_generator", sequenceName = "PHENOTYPIC_COLLECTION_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "phenotypic_collection_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "DESCRIPTION", length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECORD_DATE", nullable = false)
	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVIEWED_DATE")
	public Date getReviewedDate() {
		return reviewedDate;
	}

	public void setReviewedDate(Date reviewedDate) {
		this.reviewedDate = reviewedDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REVIEWED_BY_ID")
	public ArkUser getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(ArkUser reviewedBy) {
		this.reviewedBy = reviewedBy;
	}
	
	@ArkAuditDisplay
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_DATASET_FIELD_GROUP_ID")
	public PhenoDataSetGroup getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(PhenoDataSetGroup questionnaire) {
		this.questionnaire = questionnaire;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUESTIONNAIRE_STATUS_ID")
	public QuestionnaireStatus getStatus() {
		return status;
	}

	public void setStatus(QuestionnaireStatus status) {
		this.status = status;
	}

	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "phenoDataSetCollection")
	public Set<PhenoDataSetData> getPhenoDataSetData() {
		return phenoDataSetData;
	}
	public void setPhenoDataSetData(Set<PhenoDataSetData> phenoDataSetData) {
		this.phenoDataSetData = phenoDataSetData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		PhenoDataSetCollection other = (PhenoDataSetCollection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
