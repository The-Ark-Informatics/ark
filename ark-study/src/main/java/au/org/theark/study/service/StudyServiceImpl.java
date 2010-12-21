package au.org.theark.study.service;

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
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.model.dao.ILdapUserDao;
import au.org.theark.study.model.dao.IStudyDao;
import au.org.theark.study.web.Constants;


@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService{
	
	private static Logger log = LoggerFactory.getLogger(StudyServiceImpl.class);
	
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
	}
	
	public void updateSite(SiteVO siteVo)throws ArkSystemException{
		iLdapUserDao.updateSite(siteVo);
	}
	
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) throws ArkSystemException{
		return studyDao.searchStudyComp(studyCompCriteria);
	}
	
	public void create(StudyComp studyComponent) throws UnAuthorizedOperation,ArkSystemException{
		
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		if(!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN)){
			log.warn("Unauthorised request to create study component by " + currentUser .getPrincipal());			
			throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");
		}

		studyDao.create(studyComponent);
		//Add Audit Log here
	}
	
	public void update(StudyComp studyComponent) throws UnAuthorizedOperation, ArkSystemException{
		
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		if(!securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.SUPER_ADMIN)){
			log.warn("Unauthorised request to update study component by " + currentUser .getPrincipal());			
			throw new UnAuthorizedOperation("The logged in user does not have the permission to update this Study Component.");
		}
		studyDao.update(studyComponent);
		//Add audit log
	}
	
	public void create(Phone phone) throws ArkSystemException{
		studyDao.create(phone);
	}
	
	public void update(Phone phone) throws ArkSystemException{
		studyDao.update(phone);
	}
	
	public void createSubject(SubjectVO subjectVO){
		studyDao.createSubject(subjectVO);
	}
	
	public void updateSubject(SubjectVO subjectVO){
		studyDao.updateSubject(subjectVO);
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
		
}
