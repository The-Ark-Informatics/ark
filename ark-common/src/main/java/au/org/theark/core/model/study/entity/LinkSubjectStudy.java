package au.org.theark.core.model.study.entity;

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
	private Long amdrifId;
	private Date studyApproachDate;
	private Long yearOfFirstMamogram;
	private Long yearOfRecentMamogram;
	private Long totalNumberOfMamograms;
	private String siteAddress;
	private Country country;
	private CountryState state;
	private String city;
	private String postCode;
	
	// Property accessors
	@Id
	@SequenceGenerator(name="link_subject_study_generator", sequenceName="LINK_SUBJECT_STUDY_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "link_subject_study_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "AMDRIF_ID", precision = 22, scale = 0)
	public Long getAmdrifId() {
		return amdrifId;
	}

	public void setAmdrifId(Long amdrifId) {
		this.amdrifId = amdrifId;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "STUDY_APPROACH_DATE", length = 7)
	public Date getStudyApproachDate() {
		return studyApproachDate;
	}

	public void setStudyApproachDate(Date studyApproachDate) {
		this.studyApproachDate = studyApproachDate;
	}
	
	@Column(name = "YEAR_OF_FIRST_MAMOGRAM", precision = 22, scale = 0)
	public Long getYearOfFirstMamogram() {
		return yearOfFirstMamogram;
	}
		
	
	public void setYearOfFirstMamogram(Long yearOfFirstMamogram) {
		this.yearOfFirstMamogram = yearOfFirstMamogram;
	}

	@Column(name = "YEAR_OF_RECENT_MAMOGRAM", precision = 22, scale = 0)
	public Long getYearOfRecentMamogram() {
		return yearOfRecentMamogram;
	}

	public void setYearOfRecentMamogram(Long yearOfRecentMamogram) {
		this.yearOfRecentMamogram = yearOfRecentMamogram;
	}
	
	@Column(name = "TOTAL_MAMOGRAMS", precision = 22, scale = 0)
	public Long getTotalNumberOfMamograms() {
		return totalNumberOfMamograms;
	}

	public void setTotalNumberOfMamograms(Long totalNumberOfMamograms) {
		this.totalNumberOfMamograms = totalNumberOfMamograms;
	}

	@Column(name = "SITE_STREET_ADDRESS", length = 255)
	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "COUNTRY_ID")
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID")
	public CountryState getState() {
		return state;
	}

	public void setState(CountryState state) {
		this.state = state;
	}

	@Column(name = "SITE_CITY", length = 255)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "SITE_POST", length = 45)
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}


	
	private Set<SubjectCustFldDat> subjectCustFldDats = new HashSet<SubjectCustFldDat>(
			0);

	// Constructors

	/** default constructor */
	public LinkSubjectStudy() {
		person = new Person();
		
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