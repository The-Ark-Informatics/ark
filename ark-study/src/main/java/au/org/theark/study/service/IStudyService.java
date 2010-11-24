package au.org.theark.study.service;

import java.util.List;
import java.util.Set;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.vo.SubjectVO;




public interface IStudyService {
	/**
	 * This interface must be accessible to only super administator role
	 * @param studyEntity
	 * @param selectedApplications
	 * @throws ArkSystemException
	 */
	public void createStudy(Study studyEntity, Set<String> selectedApplications) throws EntityExistsException,UnAuthorizedOperation, ArkSystemException;
	
	public void updateStudy(Study studyEntity,Set<String> selectedApplications) throws EntityCannotBeRemoved,EntityExistsException,UnAuthorizedOperation, ArkSystemException;
	
	/**
	 * Fetch the list of applications/modules the study is currently associated with from LDAP.
	 * @param studyNameCN
	 * @return
	 * @throws ArkSystemException
	 */
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException;
	
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException;
	
	public void archiveStudy(Study studyEntity) throws UnAuthorizedOperation,StatusNotAvailableException, ArkSystemException;
	
	public void createSite(SiteVO siteVo) throws EntityExistsException,ArkSystemException; 
	
	public List<SiteVO> getSite(SiteVO siteVo);
	
	public void updateSite(SiteVO siteVo) throws ArkSystemException;
	
	/**
	 * Search for Study components with a certain criteria.
	 * @param studyCompCriteria
	 * @return
	 */
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) throws ArkSystemException;
	
	
	public void create(StudyComp sc) throws UnAuthorizedOperation, ArkSystemException;
	
	public void update(StudyComp studyComponent)throws UnAuthorizedOperation, ArkSystemException;
	
	public void create(Phone phone) throws ArkSystemException;
	
	/**
	 * A method to create a Subject.
	 * @param subjectVO
	 */
	public void createSubject(SubjectVO subjectVO);
	
	public void updateSubject(SubjectVO subjectVO);

}
