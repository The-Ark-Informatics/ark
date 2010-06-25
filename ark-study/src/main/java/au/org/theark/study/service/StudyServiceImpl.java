package au.org.theark.study.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.study.model.dao.StudyDao;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.web.Constants;

@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService{
	
	private StudyDao studyDao;

	@Autowired
	public void setStudyDao(StudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public StudyDao getStudyDao() {
		return studyDao;
	}

	public void createStudy(Study studyEntity) {
		studyDao.create(studyEntity);
	}
	
	public List<Study> getStudy(Study study){
		return studyDao.getStudy(study);
	}
}
