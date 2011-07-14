package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudyVO extends BaseVO{

	private String studyName;
	private List<ModuleVO> modules;
	private List<RoleVO> roles;//From a user' perspective the list of roles for a study in context. E.g. study_admin or ordinary_user

	/**
	 * Constructor
	 */
	public StudyVO(){
		super();
		this.roles = new ArrayList<RoleVO>();
		studyStatusVo = new StudyStatusVo();
	}
	
	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}
	
	public List<ModuleVO> getModules() {
		return modules;
	}

	public void setModules(List<ModuleVO> modules) {
		this.modules = modules;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	
	
	private Long studyKey;
	private String name;
	private String description;
	private Date dateOfApplication;
	private Long estimatedYearOfCompletion;
	private String chiefInvestigator;
	private String coInvestigator;
	private Boolean autoGenerateSubjectKey;
	private Long subjectKeyStart;
	private String subjectIdPrefix;
	private String contactPerson;
	private String contactPersonPhone;
	private String ldapGroupName;
	private Boolean autoConsent;
	private String subStudyBiospecimenPrefix;
	private StudyStatusVo studyStatusVo;
	
	
	public Long getStudyKey() {
		return studyKey;
	}

	public void setStudyKey(Long studyKey) {
		this.studyKey = studyKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateOfApplication() {
		return dateOfApplication;
	}

	public void setDateOfApplication(Date dateOfApplication) {
		this.dateOfApplication = dateOfApplication;
	}

	public Long getEstimatedYearOfCompletion() {
		return estimatedYearOfCompletion;
	}

	public void setEstimatedYearOfCompletion(Long estimatedYearOfCompletion) {
		this.estimatedYearOfCompletion = estimatedYearOfCompletion;
	}

	public String getChiefInvestigator() {
		return chiefInvestigator;
	}

	public void setChiefInvestigator(String chiefInvestigator) {
		this.chiefInvestigator = chiefInvestigator;
	}

	public String getCoInvestigator() {
		return coInvestigator;
	}

	public void setCoInvestigator(String coInvestigator) {
		this.coInvestigator = coInvestigator;
	}

	public Boolean getAutoGenerateSubjectKey() {
		return autoGenerateSubjectKey;
	}

	public void setAutoGenerateSubjectKey(Boolean autoGenerateSubjectKey) {
		this.autoGenerateSubjectKey = autoGenerateSubjectKey;
	}

	public Long getSubjectKeyStart() {
		return subjectKeyStart;
	}

	public void setSubjectKeyStart(Long subjectKeyStart) {
		this.subjectKeyStart = subjectKeyStart;
	}

	public String getSubjectIdPrefix() {
		return subjectIdPrefix;
	}

	public void setSubjectIdPrefix(String subjectIdPrefix) {
		this.subjectIdPrefix = subjectIdPrefix;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPersonPhone() {
		return contactPersonPhone;
	}

	public void setContactPersonPhone(String contactPersonPhone) {
		this.contactPersonPhone = contactPersonPhone;
	}

	public String getLdapGroupName() {
		return ldapGroupName;
	}

	public void setLdapGroupName(String ldapGroupName) {
		this.ldapGroupName = ldapGroupName;
	}

	public Boolean getAutoConsent() {
		return autoConsent;
	}

	public void setAutoConsent(Boolean autoConsent) {
		this.autoConsent = autoConsent;
	}

	public String getSubStudyBiospecimenPrefix() {
		return subStudyBiospecimenPrefix;
	}

	public void setSubStudyBiospecimenPrefix(String subStudyBiospecimenPrefix) {
		this.subStudyBiospecimenPrefix = subStudyBiospecimenPrefix;
	}

	public StudyStatusVo getStudyStatusVo() {
		return studyStatusVo;
	}

	public void setStudyStatusVo(StudyStatusVo studyStatusVo) {
		this.studyStatusVo = studyStatusVo;
	}

}
