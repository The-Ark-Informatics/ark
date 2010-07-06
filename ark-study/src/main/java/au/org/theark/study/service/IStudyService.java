package au.org.theark.study.service;

import java.util.List;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;

public interface IStudyService {
	
	public void createStudy(Study studyEntity);
	
	public List<Study> getStudy(Study study);
	
	public List<StudyStatus> getListOfStudyStatus();

}
