package au.org.theark.web.rest.service;

import java.util.List;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.web.rest.model.CreateRelationShipRequest;
import au.org.theark.web.rest.model.CreateSubjectRequest;
import au.org.theark.web.rest.model.CreateTwinRequest;
import au.org.theark.web.rest.model.GetSubjectRequest;
import au.org.theark.web.rest.model.ValidationType;

public interface IPedigreeWebServiceRest {
	
	public Boolean createSubject(SubjectVO subjectVO);
	
	public LinkSubjectStudy getLinkSubjectStudyBySubjectUidAndStudy(String subjectUid,Study study);
	
	public Boolean isCircularValidationSuccessful(SubjectVO subjectVO,SubjectVO relativeVO);
	
	public void createRelationShip(SubjectVO subjectVO,SubjectVO relativeVO,Relationship relationship);
	
	public Boolean createTwin(SubjectVO subjectVO,SubjectVO relativeVO,TwinType twinType);
	
	public Boolean generatePedigree(SubjectVO subjectVO);
	
	public Study getStudyByID(Long id);
	
	public String getPedigreeView(String subjectUid,Long studyId);
	
	public  SubjectVO mapSubjectRequestToBusinessSubjectVO(CreateSubjectRequest subjectRequest);
	
	public Relationship getRelationShipByname(String name);
	
	public TwinType getTwinTypeByname(String name);

	public List<LinkSubjectStudy> getListofLinkSubjectStudiesForStudy(Study study);
	
	//validation methods
	public ValidationType validateForStudy(Long studyId);
	
	public ValidationType validateEntireSubjectRequest(CreateSubjectRequest subjectRequest);
	
	public ValidationType validateRelationShipForStudy(CreateRelationShipRequest createRelationShipRequest);
	
	public ValidationType validateTwinTypeForStudy(CreateTwinRequest createTwinRequest);
	
	public ValidationType validateForSubjectUIDForStudy(GetSubjectRequest getSubjectRequest);
	
	public List<CreateSubjectRequest> mapListOfLinkSubjectStudiesToListOfCreateSubjectRequests(List<LinkSubjectStudy> linkSubjectStudies);
	
	public Boolean isRelativeASibling(SubjectVO subjectVO,SubjectVO relativeVO);
	
}
