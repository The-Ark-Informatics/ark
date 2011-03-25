package au.org.theark.study.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.model.dao.ILdapUserDao;
import au.org.theark.study.model.dao.IStudyDao;
import au.org.theark.study.web.Constants;


@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService{
	
	private static Logger log = LoggerFactory.getLogger(StudyServiceImpl.class);
	
	private IArkCommonService arkCommonService;
	private IStudyDao studyDao;
	private ILdapUserDao iLdapUserDao;

	public ILdapUserDao getiLdapUserDao() {
		return iLdapUserDao;
	}

	@Autowired
	public void setiLdapUserDao(ILdapUserDao iLdapUserDao) {
		this.iLdapUserDao = iLdapUserDao;
	}

	/*To access Hibernate Study Dao */
	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}

	public IArkCommonService getArkCommonService() {
		return arkCommonService;
	}

	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}
	
	public List<StudyStatus> getListOfStudyStatus(){
		return studyDao.getListOfStudyStatus();
	}
	
	public void createSite(SiteVO siteVo) throws EntityExistsException,ArkSystemException{
		iLdapUserDao.createSite(siteVo);
	}
	
	public List<SiteVO> getSite(SiteVO siteVo){
		
		return iLdapUserDao.getSite(siteVo);
		
	}

	public void createStudy(Study studyEntity, Set<String> selectedApplications) throws ArkSystemException, EntityExistsException, UnAuthorizedOperation{
		try{
			//Create the study group in the LDAP for the selected applications and also add the roles to each of the application.
			SecurityManager securityManager =  ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();
			
			if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN)){
				studyDao.create(studyEntity);
				iLdapUserDao.createStudy(studyEntity.getName(), selectedApplications, au.org.theark.study.service.Constants.ARK_SYSTEM_USER);
				
				AuditHistory ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
				ah.setComment("Created Study " + studyEntity.getName());
				ah.setEntityId(studyEntity.getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
				arkCommonService.createAuditHistory(ah);
				
			}else{
			 throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");//Throw an exception
			}
		}catch(ArkSystemException arkSystemException){
			throw arkSystemException;
		}
		
	}
	
	public void updateStudy(Study studyEntity,Set<String> selectedApplications) throws EntityCannotBeRemoved, UnAuthorizedOperation, ArkSystemException, EntityExistsException{
		
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN)){
			studyDao.updateStudy(studyEntity);
			iLdapUserDao.updateStudyApplication(studyEntity.getName(), selectedApplications,au.org.theark.study.service.Constants.ARK_SYSTEM_USER);
			
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("Updated Study " + studyEntity.getName());
			ah.setEntityId(studyEntity.getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
			arkCommonService.createAuditHistory(ah);
		}else{
		 throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");//Throw an exception
		}
	}
	
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException{
		
		return iLdapUserDao.getModulesLinkedToStudy(studyNameCN);
	}
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException{
		return iLdapUserDao.getModulesLinkedToStudy(studyNameCN, isForDisplay);
	}
	/**
	 * This will mark the study as archived.
	 */
	public void archiveStudy(Study studyEntity) throws UnAuthorizedOperation, StatusNotAvailableException, ArkSystemException
	{
		//For archive, set the status to Archived and then issue an update
		StudyStatus status = studyDao.getStudyStatus(au.org.theark.study.service.Constants.STUDY_STATUS_ARCHIVE);
		studyEntity.setStudyStatus(status);
		studyDao.updateStudy(studyEntity);
		
		Subject currentUser = SecurityUtils.getSubject();
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Archived Study " + studyEntity.getName());
		ah.setEntityId(studyEntity.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		arkCommonService.createAuditHistory(ah);
	}
	
	public void updateSite(SiteVO siteVo)throws ArkSystemException{
		iLdapUserDao.updateSite(siteVo);
		
		Subject currentUser = SecurityUtils.getSubject();
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Site " + siteVo.getSiteName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY);
		arkCommonService.createAuditHistory(ah);
	}
	
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) throws ArkSystemException{
		return studyDao.searchStudyComp(studyCompCriteria);
	}
	
	public void create(StudyComp studyComponent) throws UnAuthorizedOperation,ArkSystemException{
		
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		if(
				(!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN)) &&
		        (!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)) ){
			log.warn("Unauthorised request to create study component by " + currentUser .getPrincipal());			
			throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");
		}

		studyDao.create(studyComponent);
	
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		
		ah.setComment("Created Study Component " + studyComponent.getName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
		ah.setStudyStatus(studyComponent.getStudy().getStudyStatus());
		ah.setEntityId(studyComponent.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void update(StudyComp studyComponent) throws UnAuthorizedOperation, ArkSystemException{
		
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		if( (!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN))&&
		    (!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN))){
			log.warn("Unauthorised request to update study component by " + currentUser .getPrincipal());			
			throw new UnAuthorizedOperation("The logged in user does not have the permission to update this Study Component.");
		}
		studyDao.update(studyComponent);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		
		ah.setComment("Updated Study Component " + studyComponent.getName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_STUDY_COMPONENT);
		ah.setStudyStatus(studyComponent.getStudy().getStudyStatus());
		ah.setEntityId(studyComponent.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void create(Phone phone) throws ArkSystemException{
		studyDao.create(phone);
		
		Subject currentUser = SecurityUtils.getSubject();
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		
		ah.setComment("Created Phone " + phone.getPhoneNumber());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHONE);
		ah.setEntityId(phone.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void update(Phone phone) throws ArkSystemException{
		studyDao.update(phone);
		
		Subject currentUser = SecurityUtils.getSubject();
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		
		ah.setComment("Updated Phone " + phone.getPhoneNumber());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHONE);
		ah.setEntityId(phone.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void delete(Phone phone) throws ArkSystemException {
		studyDao.delete(phone);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Phone " + phone.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHONE);
		ah.setEntityId(phone.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void createSubject(SubjectVO subjectVO)  throws ArkUniqueException{
		
		studyDao.createSubject(subjectVO);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Subject " + subjectVO.getSubjectStudy().getSubjectUID());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT);
		ah.setEntityId(subjectVO.getSubjectStudy().getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException{
		
		studyDao.updateSubject(subjectVO);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Subject " + subjectVO.getSubjectStudy().getSubjectUID());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT);
		ah.setEntityId(subjectVO.getSubjectStudy().getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can represent
	 * a subject or contact.
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException{
		return studyDao.getPerson(personId);
	}
	
	/**
	 * Look up the phones connected with the person(subject or contact)
	 * @param personId
	 * @return List<Phone>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId) throws EntityNotFoundException, ArkSystemException{
		return studyDao.getPersonPhoneList(personId);
	}
	
	
	/**
	 * Looks up the phones linked to a person and applies any filter supplied with the phone object.Used in Search Phone functionality.
	 * One can look up base don area code, phone type, phone number
	 * @param personId
	 * @param phone
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId,Phone phone) throws EntityNotFoundException,ArkSystemException{
		return studyDao.getPersonPhoneList(personId, phone);
	}
	
	/**
	 * Looks up the addresses linked to a person and applies any filter supplied with the address object.Used in Search Address functionality.
	 * @param personId
	 * @param address
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Address> getPersonAddressList(Long personId, Address address) throws EntityNotFoundException,ArkSystemException{
		return studyDao.getPersonAddressList(personId,address);
	}
	
	public void create(Address address) throws ArkSystemException{
		studyDao.create(address);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Address " + address.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void update(Address address) throws ArkSystemException{
		studyDao.update(address);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Address " + address.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void delete(Address address) throws ArkSystemException{
		//Add business rules to check if this address is in use/active and referred elsewhere
		studyDao.delete(address);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Address " + address.getStreetAddress());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_ADDRESS);
		ah.setEntityId(address.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void create(Consent consent) throws ArkSystemException{
		studyDao.create(consent);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException,ArkSystemException{
		return studyDao.searchConsent(consent);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.study.service.IStudyService#update(au.org.theark.core.model.study.entity.Consent)
	 */
	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException {
		studyDao.update(consent);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException,ArkSystemException{
		return studyDao.searchConsent(consentVO);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.study.service.IStudyService#delete(au.org.theark.core.model.study.entity.Consent)
	 */
	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException {
		studyDao.delete(consent);
		 
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Consent " + consent.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT);
		ah.setEntityId(consent.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public Consent getConsent(Long id) throws ArkSystemException {
		 return studyDao.getConsent(id);
	}
	
	public List<SubjectCustmFld> searchStudyFields(SubjectCustmFld subjectCustmFld){
		return studyDao.searchStudyFields(subjectCustmFld);
	}
	
	/*** correspondence service functions ***/
	public void create(Correspondences correspondence) throws ArkSystemException {
		studyDao.create(correspondence);
	}
	
	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		studyDao.update(correspondence);
	}
		
	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException {
		studyDao.delete(correspondence);
	}
	
	public List<Correspondences> getPersonCorrespondenceList(Long personId, Correspondences correspondence) throws EntityNotFoundException, ArkSystemException {
		return studyDao.getPersonCorrespondenceList(personId, correspondence);
	}

	public List<CorrespondenceStatusType> getCorrespondenceStatusTypes() {
		return studyDao.getCorrespondenceStatusTypes();
	}
	
	public List<CorrespondenceModeType> getCorrespondenceModeTypes() {
		return studyDao.getCorrespondenceModeTypes();
	}

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes() {
		return studyDao.getCorrespondenceDirectionTypes();
	}

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes() {
		return studyDao.getCorrespondenceOutcomeTypes();
	}

	
	public void create(ConsentFile consentFile) throws ArkSystemException {
		studyDao.create(consentFile);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void update(ConsentFile consentFile) throws ArkSystemException,
			EntityNotFoundException {
		studyDao.update(consentFile);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void delete(ConsentFile consentFile) throws ArkSystemException,
			EntityNotFoundException {
		studyDao.delete(consentFile);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted ConsentFile " + consentFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CONSENT_FILE);
		ah.setEntityId(consentFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<ConsentFile> searchConsentFile(ConsentFile consentFile) throws EntityNotFoundException, ArkSystemException {
		return studyDao.searchConsentFile(consentFile);
	}

	public void createPersonLastnameHistory(Person person)
	{
		studyDao.createPersonLastnameHistory(person);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PersonLastnameHistory " + person.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY);
		ah.setEntityId(person.getId());
		arkCommonService.createAuditHistory(ah);
	}
	
	public void updatePersonLastnameHistory(Person person)
	{
		studyDao.updatePersonLastnameHistory(person);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Updated PersonLastnameHistory " + person.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PERSON_LASTNAME_HISTORY);
		ah.setEntityId(person.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<PersonLastnameHistory> getLastnameHistory(Person person)
	{
		return studyDao.getLastnameHistory(person);
	}

	public String getPreviousLastname(Person person)
	{
		return studyDao.getPreviousLastname(person);
	}
	
	public String getCurrentLastname(Person person)
	{
		return studyDao.getCurrentLastname(person);
	}

	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory)
	{
		return studyDao.getPreviousSurnameHistory(personSurnameHistory);
	}
	
	public boolean personHasPreferredMailingAddress(Person person,Long currentAddressId){
		return studyDao.personHasPreferredMailingAddress(person,currentAddressId);
	}
	
	public void create(SubjectFile subjectFile) throws ArkSystemException {
		studyDao.create(subjectFile);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void update(SubjectFile subjectFile) throws ArkSystemException,
			EntityNotFoundException {
		studyDao.update(subjectFile);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public void delete(SubjectFile subjectFile) throws ArkSystemException,
			EntityNotFoundException {
		studyDao.delete(subjectFile);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted subjectFile " + subjectFile.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_SUBJECT_FILE);
		ah.setEntityId(subjectFile.getId());
		arkCommonService.createAuditHistory(ah);
	}

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException {
		return studyDao.searchSubjectFile(subjectFile);
	}

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved, UnAuthorizedOperation{
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if(	(!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN)) && 
		    (!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)) ){
			log.warn("Unauthorised request to create study component by " + currentUser .getPrincipal());			
			throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");
		}
		
		studyDao.delete(studyComp);
	}
}