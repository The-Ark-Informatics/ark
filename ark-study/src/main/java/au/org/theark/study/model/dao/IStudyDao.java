package au.org.theark.study.model.dao;

import java.util.List;

import au.org.theark.study.model.entity.Study;

public interface IStudyDao {

	public void create(Study study);
	
	public List<Study> getStudy(Study study);
}
