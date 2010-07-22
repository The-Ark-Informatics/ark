package au.org.theark.study.service;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
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
	public void createStudy(Study studyEntity, List<String> selectedApplications) throws EntityExistsException,UnAuthorizedOperation, ArkSystemException;
	
	public List<Study> getStudy(Study study);
	
	public List<StudyStatus> getListOfStudyStatus();

}
