package au.org.theark.study.service;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.dao.ILdapUserDao;
import au.org.theark.study.model.dao.IStudyDao;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.web.Constants;

@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService{
	
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

	
	public List<Study> getStudy(Study study){
		return studyDao.getStudy(study);
	}
	
	public List<StudyStatus> getListOfStudyStatus(){
		return studyDao.getListOfStudyStatus();
	}

	public void createStudy(Study studyEntity, Set<String> selectedApplications) throws ArkSystemException, EntityExistsException, UnAuthorizedOperation{
		try{
			//Create the study group in the LDAP for the selected applications and also add the roles to each of the application.
			SecurityManager securityManager =  ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();
			
			if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN)){
				studyDao.create(studyEntity);
				iLdapUserDao.createStudy(studyEntity.getName(), selectedApplications, au.org.theark.study.service.Constants.ARK_SYSTEM_USER);
			}else{
			 throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");//Throw an exception
			}
		}catch(ArkSystemException arkSystemException){
			throw arkSystemException;
		}
		
	}
	
	public void updateStudy(Study studyEntity,Set<String> selectedApplications) throws EntityExistsException,EntityCannotBeRemoved, UnAuthorizedOperation, ArkSystemException{

		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN)){
			studyDao.updateStudy(studyEntity);
			iLdapUserDao.updateStudyApplication(studyEntity.getName(), selectedApplications,au.org.theark.study.service.Constants.ARK_SYSTEM_USER);
		}else{
		 throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");//Throw an exception
		}
	}
	
	
	private boolean validateDeleteStudy(Study studyEntity, StringBuffer messageBuffer){
		boolean isDeletable = false;
		/*Check if there are study components linked to the study */
		if(studyEntity.getLinkStudyStudycomps().size() > 0){
			isDeletable = true;
			messageBuffer.append("\nThe study is linked to Study Components.");
		}
		
		/* Check if the study has Sites linked to the study in LDAP or Backend*/
		if(studyEntity.getLinkStudyStudysites().size() > 0 ){
			isDeletable = true;
			messageBuffer.append("\n The study is linked to Study Sites.");
		}
		
		/*Check if the study has subjects linked via the backend database */
		if(studyEntity.getLinkSubjectStudies().size() > 0){
			isDeletable = true;
			messageBuffer.append("\n There are Subjects linked to the Study.");
		}
		
		/* Check if the study is linked with Subjects via Contacts */
		if(studyEntity.getLinkSubjectContacts().size() > 0){
			isDeletable = true;
			messageBuffer.append("\n There are Contacts  linked to the Study via Subjects.");
		}
		
		/* Check if the study has Sub-Study linked to the study*/
		if(studyEntity.getLinkStudySubstudiesForStudyKey().size() > 0){
			isDeletable = true;
			messageBuffer.append("\n There are sub-studies refering to this Registry study.");
		}
		
		return isDeletable;
	}
	
	/**
	 * Removes a Study from the LDAP and the database if it passes the validations.
	 */
	public void deleteStudy(Study studyEntity) throws EntityCannotBeRemoved,UnAuthorizedOperation,ArkSystemException{
		
		StringBuffer messageBuffer = new StringBuffer();
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN)){
			
			if(!validateDeleteStudy(studyEntity, messageBuffer)){
				throw new EntityCannotBeRemoved(messageBuffer.toString());
			}
			else{
				studyDao.delete(studyEntity);//Will remove the study object from the database
				Set<String> modulesToDelinkFrom = iLdapUserDao.getModulesLinkedToStudy(studyEntity.getName(),true);
				iLdapUserDao.removeStudy(studyEntity.getName(), modulesToDelinkFrom);
			}
			
		}else{
		 throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");//Throw an exception
		}
	}
	
	public Study getStudy(Long id){
		return studyDao.getStudy(id);
	}
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException{
		
		return iLdapUserDao.getModulesLinkedToStudy(studyNameCN);
	}
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException{
		return iLdapUserDao.getModulesLinkedToStudy(studyNameCN, isForDisplay);
	}
}
