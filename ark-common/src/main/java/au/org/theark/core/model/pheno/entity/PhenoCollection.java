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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "PHENO_COLLECTION", schema = Constants.PHENO_TABLE_SCHEMA)
public class PhenoCollection implements Serializable{

	private static final long	serialVersionUID	= 1L;

	private Long id;
	private String name;
	private String description;
	private LinkSubjectStudy linkSubjectStudy;
	private Date recordDate;
	private Set<PhenoData> phenoData = new HashSet<PhenoData>();
	private Date reviewedDate;
	private ArkUser reviewedBy;
	private CustomFieldGroup questionnaire;
	private QuestionnaireStatus status;
	
	/**
	 * Constructor
	 */
	public  PhenoCollection(){
		
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

	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REVIEWED_BY_ID")
	public ArkUser getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(ArkUser reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_GROUP_ID")
	public CustomFieldGroup getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(CustomFieldGroup questionnaire) {
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "phenoCollection")
	public Set<PhenoData> getPhenoData() {
		return phenoData;
	}
	
	public void setPhenoData(Set<PhenoData> phenoData){
		this.phenoData = phenoData;
	}
}
