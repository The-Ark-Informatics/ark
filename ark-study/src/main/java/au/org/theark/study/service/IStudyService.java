package au.org.theark.study.service;

import java.util.List;

import au.org.theark.study.model.entity.Study;

public interface IStudyService {
	
	public void createStudy(Study studyEntity);
	
	public List<Study> getStudy(Study study);

}
