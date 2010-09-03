package au.org.theark.study.service;

import java.util.List;
import java.util.Set;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;

public interface IStudyService {
	/**
	 * This interface must be accessible to only super administator role
	 * @param studyEntity
	 * @param selectedApplications
	 * @throws ArkSystemException
	 */
	public void createStudy(Study studyEntity, Set<String> selectedApplications) throws EntityExistsException,UnAuthorizedOperation, ArkSystemException;
	
	public List<Study> getStudy(Study study);
	
	public List<StudyStatus> getListOfStudyStatus();
	
	public Study getStudy(Long id);
	
	public void updateStudy(Study studyEntity,Set<String> selectedApplications) throws EntityExistsException,EntityCannotBeRemoved,UnAuthorizedOperation, ArkSystemException;
	
	/**
	 * Fetch the list of applications/modules the study is currently associated with from LDAP.
	 * @param studyNameCN
	 * @return
	 * @throws ArkSystemException
	 */
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException;
	
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException;
	
	public void deleteStudy(Study studyEntity) throws EntityCannotBeRemoved,UnAuthorizedOperation,ArkSystemException;

}
