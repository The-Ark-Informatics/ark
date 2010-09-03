package au.org.theark.study.model.dao;

import java.util.List;

import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;

public interface IStudyDao {

	public void create(Study study);
	
	public void delete(Study study);
	
	public List<Study> getStudy(Study study);
	
	/**
	 * Interface to get a list of Study Status reference data from the backend.
	 * These study status' are no associated with a study as such but can be used for
	 * displaying a list of options for a particular study.
	 * @return
	 */
	public List<StudyStatus> getListOfStudyStatus();
	
	public Study getStudy(Long id);
	
	public void updateStudy(Study study);
}
