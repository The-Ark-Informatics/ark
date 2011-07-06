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
import javax.persistence.JoinTable;
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
	private String otherState;
	private Long amdrfId;
	private Date studyApproachDate;
	private Long yearOfFirstMamogram;
	private Long yearOfRecentMamogram;
	private Long totalNumberOfMamograms;
	private String siteAddress;
	private Country country;
	private CountryState state;
	private String city;
	private String postCode;
	private YesNo consentToActiveContact;
	private YesNo consentToPassiveDataGathering;
	private YesNo consentToUseData;
	
	private ConsentStatus consentStatus;
	private ConsentType consentType;
	private Date consentDate;
	
	private String heardAboutStudy;
	private String comment;
	private YesNo consentDownloaded;
	
	private Set<Consent> consents = new HashSet<Consent>();	
	private Set<SubjectCustFldDat> subjectCustFldDats = new HashSet<SubjectCustFldDat>(
			0);

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TO_PASSIVE_DATA_GATHERING_ID")
	public YesNo getConsentToPassiveDataGathering() {
		return consentToPassiveDataGathering;
	}

	public void setConsentToPassiveDataGathering(YesNo consentToPassiveDataGathering) {
		this.consentToPassiveDataGathering = consentToPassiveDataGathering;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TO_USE_DATA_ID")
	public YesNo getConsentToUseData() {
		return consentToUseData;
	}

	public void setConsentToUseData(YesNo consentToUseData) {
		this.consentToUseData = consentToUseData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TO_ACTIVE_CONTACT_ID")
	public YesNo getConsentToActiveContact() {
		return consentToActiveContact;
	}

	public void setConsentToActiveContact(YesNo consentToActiveContact) {
		this.consentToActiveContact = consentToActiveContact;
	}

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

	@ManyToOne(fetch = FetchType.EAGER)
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_STATUS_ID")
	public ConsentStatus getConsentStatus() {
		return consentStatus;
	}
	public void setConsentStatus(ConsentStatus consentStatus) {
		this.consentStatus = consentStatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TYPE_ID")
	public ConsentType getConsentType() {
		return consentType;
	}
	public void setConsentType(ConsentType consentType) {
		this.consentType = consentType;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CONSENT_DATE", length = 7)
	public Date getConsentDate() {
		return consentDate;
	}
	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}

	@Column(name = "OTHER_STATE", length = 255)
	public String getOtherState() {
		return otherState;
	}

	public void setOtherState(String otherState) {
		this.otherState = otherState;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="linkSubjectStudy")
    public Set<Consent> getConsents() {
		return consents;
	}

	public void setConsents(Set<Consent> consents) {
		this.consents = consents;
	}

	@Column(name = "AMDRF_ID", precision = 22, scale = 0)
	public Long getAmdrfId() {
		return amdrfId;
	}

	public void setAmdrfId(Long amdrfId) {
		this.amdrfId = amdrfId;
	}

	@Column(name = "HEARD_ABOUT_STUDY", length = 1000)
	public String getHeardAboutStudy() {
		return heardAboutStudy;
	}

	public void setHeardAboutStudy(String heardAboutStudy) {
		this.heardAboutStudy = heardAboutStudy;
	}

	@Column(name = "COMMENTS", length = 1000)
	public String getComment() {
		return comment;
	}
	
	
	public void setComment(String comment) {
		this.comment = comment;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_DOWNLOADED")
	public YesNo getConsentDownloaded() {
		return consentDownloaded;
	}

	public void setConsentDownloaded(YesNo consentDownloaded) {
		this.consentDownloaded = consentDownloaded;
	}

}