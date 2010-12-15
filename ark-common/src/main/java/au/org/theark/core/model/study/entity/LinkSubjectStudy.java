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

import au.org.theark.core.Constants;

/**
 * LinkSubjectStudy entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_STUDY", schema = Constants.STUDY_SCHEMA)
public class LinkSubjectStudy implements java.io.Serializable {

	// Fields

	private Long id;
	private Study study;
	private SubjectStatus subjectStatus;
	private Person person;
	
	private String subjectUID;
	
	private Set<SubjectCustFldDat> subjectCustFldDats = new HashSet<SubjectCustFldDat>(
			0);

	// Constructors

	/** default constructor */
	public LinkSubjectStudy() {
	}

	/** minimal constructor */
	public LinkSubjectStudy(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkSubjectStudy(Long id, Study study,
			SubjectStatus subjectStatus, Person person,
			Set<SubjectCustFldDat> subjectCustFldDats) {
		this.id = id;
		this.study = study;
		this.subjectStatus = subjectStatus;
		this.person = person;
		this.subjectCustFldDats = subjectCustFldDats;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="link_subject_study_generator", sequenceName="LINK_SUBJECT_STUDY_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "link_subject_study_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getid() {
		return this.id;
	}

	public void setid(Long id) {
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
	@JoinColumn(name = "SUBJECT_STATUS_ID")
	public SubjectStatus getSubjectStatus() {
		return this.subjectStatus;
	}

	public void setSubjectStatus(SubjectStatus subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSubjectStudy")
	public Set<SubjectCustFldDat> getSubjectCustFldDats() {
		return this.subjectCustFldDats;
	}

	public void setSubjectCustFldDats(Set<SubjectCustFldDat> subjectCustFldDats) {
		this.subjectCustFldDats = subjectCustFldDats;
	}

	@Column(name = "SUBJECT_UID", length = 50)
	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

}