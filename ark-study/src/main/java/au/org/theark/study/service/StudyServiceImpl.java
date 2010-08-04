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
	
	public void updateStudy(Study studyEntity,Set<String> selectedApplications) throws EntityExistsException,UnAuthorizedOperation, ArkSystemException{

		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN)){
			studyDao.updateStudy(studyEntity);
			//TODO update the Study association in LDAP 
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
