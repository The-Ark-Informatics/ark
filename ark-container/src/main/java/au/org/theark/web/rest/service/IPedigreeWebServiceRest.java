package au.org.theark.web.rest.service;

import java.io.File;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.vo.SubjectVO;

public interface IPedigreeWebServiceRest {
	
	public Boolean createSubject(SubjectVO subjectVO);
	//public Study getStudyByID(Long id);
	public Boolean isSubjectUIDExist(Study study,String subjectUid);
	public Boolean isStudyExist(Study study);
	public LinkSubjectStudy getLinkSubjectStudyBySubjectUidAndStudy(String subjectUid,Study study);
	public Boolean isCircularValidationSuccessful(SubjectVO subjectVO,SubjectVO relativeVO);
	public void createRelationShip(SubjectVO subjectVO,SubjectVO relativeVO,Relationship relationship);
	public Boolean createTwin(SubjectVO subjectVO,SubjectVO relativeVO,TwinType twinType);
	public Boolean generatePedigree(SubjectVO subjectVO);
	public Study getStudyByID(Long id);
	public File getPedigreeView(String subjectUid,Long studyId);
}
