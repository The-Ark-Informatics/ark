package au.org.theark.web.rest.service;

import java.util.List;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.LinkSubjectTwin;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.web.rest.model.ConfigRequest;
import au.org.theark.web.rest.model.MadelineObject;
import au.org.theark.web.rest.model.MembershipResponse;
import au.org.theark.web.rest.model.RelationShipRequest;
import au.org.theark.web.rest.model.SubjectRequest;
import au.org.theark.web.rest.model.TwinRequest;
import au.org.theark.web.rest.model.ValidationType;

public interface IPedigreeWebServiceRest {
	
	public Boolean createSubject(SubjectVO subjectVO);
	
	public Boolean updateSubject(SubjectVO subjectVO);
	
	public LinkSubjectStudy getLinkSubjectStudyBySubjectUidAndStudy(String subjectUid,Study study);
	
	public Boolean isCircularValidationSuccessful(SubjectVO subjectVO,SubjectVO relativeVO);
	
	public void createRelationShip(SubjectVO subjectVO,SubjectVO relativeVO,Relationship relationship);
	
	public Boolean createTwin(SubjectVO subjectVO,SubjectVO relativeVO,TwinType twinType);

	public Study getStudyByID(Long id);
	
	public String getPedigreeView(String subjectUid,Long studyId);
	
	public  SubjectVO mapSubjectRequestToBusinessSubjectVO(SubjectRequest subjectRequest);
	
	public SubjectVO mapUpdateSubjectRequestToBusinessSubjectVO(SubjectRequest subjectRequest);
	
	public Relationship getRelationShipByname(String name);
	
	public TwinType getTwinTypeByname(String name);

	public List<LinkSubjectStudy> getListofLinkSubjectStudiesForStudy(Study study);
	
	//validation methods
	public ValidationType validateForStudy(Long studyId);
	
	public ValidationType validateEntireSubjectRequest(SubjectRequest subjectRequest,String action);
	
	public ValidationType validateRelationShipForStudy(RelationShipRequest createRelationShipRequest);
	
	public ValidationType validateTwinTypeForStudy(TwinRequest createTwinRequest);
	
	public ValidationType validateForSubjectUIDForStudy(Long studyId,String uid);
	
	public List<SubjectRequest> mapListOfLinkSubjectStudiesToListOfSubjectRequests(List<LinkSubjectStudy> linkSubjectStudies);
	
	public Boolean isRelativeASibling(SubjectVO subjectVO,SubjectVO relativeVO);
	
	public void deleteRelationShip(Long id);
	
	public void deleteTwin(Long id);
	
	public ValidationType validateParentRelationShip(Long studyId,Long id);
	
	public ValidationType validateTwinRelationShip(Long studyId,Long id);
	
	public SubjectRequest mapLinkSubjectStudyToCreateSubjectRequests(LinkSubjectStudy linkSubjectStudy);
	
	public List<LinkSubjectPedigree> getListofLinkSubjectPedigreeForStudy(Study study);
	
	public List<LinkSubjectTwin> getListofLinkSubjectTwinForStudy(Study study);
	
	public List<RelationShipRequest> mapListOfLinkSubjectPedigreesToListOfRelationShipRequests(List<LinkSubjectPedigree> linkSubjectPedigrees);
	
	public List<TwinRequest> mapListOfLinkSubjectTwinsToListOfTwinRequests(List<LinkSubjectTwin> linkSubjectTwins);
	
	public StudyPedigreeConfiguration getStudyPedigreeConfiguration(Long studyId);
	
	public ConfigRequest mapStudyPedigreeConfigurationToConfigRequest(StudyPedigreeConfiguration studyPedigreeConfiguration);
	
	public StudyPedigreeConfiguration mapConfigRequestToStudyPedigreeConfiguration(ConfigRequest configRequest,String action);
	
	public ValidationType validateConfigRequest(ConfigRequest configRequest,String action);
	
	public void saveOrUpdateStudyPedigreeConfiguration(StudyPedigreeConfiguration config);
	
	public List<RelationshipVo> generateSubjectPedigreeRelativeList(String subjectUID, Long studyId);
	
	public List<MembershipResponse> mapListOfRelationshipVoToListofMembershipresponse(List<RelationshipVo> relationshipVos);
	
	public String getPedigreeViewFromCsv(MadelineObject[] madelineObjects,Long studyID);
	
	public ValidationType validateCSVStringToDrawPedigree(MadelineObject[] madelineObjects,Long studyID);
	
	public List<RelationshipVo> getMySiblings(String subjectUID,Long studyId);
	
	public List<String> mapListOfRelativeVoToListOfSubjectUids(List<RelationshipVo> relationshipVos);
	
	public List<CustomField> getBinaryCustomFields(Long studyId);
	
	public List<String> mapListOfCustomFieldsToListOfCustomfieldNames(List<CustomField> customFields);
	
}
