package au.org.theark.web.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.web.rest.model.CreateRelationShipRequest;
import au.org.theark.web.rest.model.CreateSubjectRequest;
import au.org.theark.web.rest.model.GetSubjectRequest;
import au.org.theark.web.rest.model.ValidationType;
import au.org.theark.web.rest.service.ILoginWebServiceRest;
import au.org.theark.web.rest.service.IPedigreeWebServiceRest;
import au.org.theark.web.rest.util.Constants;


@RestController
public class PedigreeRestController {

	

	public static final Logger logger = LoggerFactory.getLogger(PedigreeRestController.class);

	@Autowired
	IPedigreeWebServiceRest iPedWebSerRest;
	
	@Autowired
	ILoginWebServiceRest iLoginWebServiceRest;
	
	private String message;

	//1-Create subject.
	@RequestMapping(value = "/subject/", method = RequestMethod.POST)
	public ResponseEntity<String> createSubject(@RequestBody CreateSubjectRequest createSubjectRequest, UriComponentsBuilder ucBuilder) {
		
			if(isLoginAndCreatePermissionAllow(createArkUserinsideUntilFindItFromHeader())){
				ValidationType validateCode=iPedWebSerRest.validateEntireSubjectRequest(createSubjectRequest);
				if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					SubjectVO subjectvo=iPedWebSerRest.mapSubjectRequestToBusinessSubjectVO(createSubjectRequest);
					iPedWebSerRest.createSubject(subjectvo);
					HttpHeaders headers = new HttpHeaders();
					headers.setLocation(ucBuilder.path("/subject/{id}").buildAndExpand(subjectvo.getLinkSubjectStudy().getId()).toUri());
					message="Subject created successfuly.";
					return new ResponseEntity<String>(message,headers, HttpStatus.CREATED);
				}else{
					return getResponseEntityForValidationCode(validateCode);
				}
			}else{
					message="Unauthorised user.Please check the credentials.";
					return new ResponseEntity<String>(message,HttpStatus.UNAUTHORIZED);
			}
	}
	
	//2-Get Subject
	@RequestMapping(value = "/getsubject/", method = RequestMethod.POST)
    public ResponseEntity<String> getLinkSubjectStudy(@RequestBody  GetSubjectRequest getSubjectRequest) {
		
		 if(isLoginAndCreatePermissionAllow(createArkUserinsideUntilFindItFromHeader())){ 
			 ValidationType validateCode=iPedWebSerRest.validateForSubjectUIDForStudy(getSubjectRequest);
			 if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			        LinkSubjectStudy linkSubjectStudy = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(getSubjectRequest.getSubjectUID(), iPedWebSerRest.getStudyByID(getSubjectRequest.getStudyId()));
			        if (linkSubjectStudy== null || linkSubjectStudy.getId()==null) {
			        	message="Can not find a subject for this subjectuid.";
			            return new ResponseEntity<String>(message,HttpStatus.NOT_FOUND);
			        }
			        return new ResponseEntity<String>(linkSubjectStudy.getId().toString(), HttpStatus.OK);
			 }else{
				 return getResponseEntityForValidationCode(validateCode);
			 }   
		 }else{
			 	message="Unauthorised user.Please check the credentials.";
				return new ResponseEntity<String>(message,HttpStatus.UNAUTHORIZED);
		}
    }
	
	//3-Set Mother or Father or Twins
	@RequestMapping(value = "/createRelationShip/", method = RequestMethod.POST)
	public ResponseEntity<String> createRelationShip(@RequestBody CreateRelationShipRequest createRelationShipRequest,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isLoginAndCreatePermissionAllow(createArkUserinsideUntilFindItFromHeader())){
			 ValidationType validateCode=iPedWebSerRest.validateRelationShipForStudy(createRelationShipRequest);
			 Study study=iPedWebSerRest.getStudyByID(createRelationShipRequest.getStudyId());
			 if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 LinkSubjectStudy subject = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(createRelationShipRequest.getSubjectUID(), study);
				 SubjectVO subjectVO=new SubjectVO();
				 subjectVO.setLinkSubjectStudy(subject);
				 LinkSubjectStudy relative = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(createRelationShipRequest.getRelativeUID(), study);
				 SubjectVO relativeVO=new SubjectVO();
				 relativeVO.setLinkSubjectStudy(relative);
					 if(createRelationShipRequest.getRelationType().equals(Constants.PARENT)){	
						 	if(iPedWebSerRest.isCircularValidationSuccessful(subjectVO,relativeVO)){
								Relationship relationship=iPedWebSerRest.getRelationShipByname(createRelationShipRequest.getRelationShip());
								iPedWebSerRest.createRelationShip(subjectVO,relativeVO,relationship);
						 	}else{
						 		return getResponseEntityForValidationCode(ValidationType.CIRCULAR_VALIDATION_UNSUCCESSFUL);	
						 	}	
					}else if(createRelationShipRequest.getRelationType().equals(Constants.TWIN)){
							TwinType twinType=iPedWebSerRest.getTwinTypeByname(createRelationShipRequest.getRelationShip());
							iPedWebSerRest.createTwin(subjectVO, relativeVO, twinType);
					}	
							headers.setLocation(ucBuilder.path("/createRelationShip/{id}").buildAndExpand(subject.getId()).toUri());
							return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			 	}else{
				 return getResponseEntityForValidationCode(validateCode);
			 }   
		}else{
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
	}
		
	//7-Get Pedigree view as a file
		@RequestMapping(value = "/getPedigreeView/", method = RequestMethod.POST)
	    public ResponseEntity<StringBuffer> getPedigreeView(@RequestBody GetSubjectRequest getSubjectRequest) {
			 if(isLoginAndCreatePermissionAllow(createArkUserinsideUntilFindItFromHeader())){
				 ValidationType validateCode=iPedWebSerRest.validateForSubjectUIDForStudy(getSubjectRequest);
				 if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 StringBuffer pedigreeFile=iPedWebSerRest.getPedigreeView(getSubjectRequest.getSubjectUID(),getSubjectRequest.getStudyId());
		         if (pedigreeFile== null) {
			           return new ResponseEntity<StringBuffer>(HttpStatus.NOT_FOUND);
			      }
		         	return new ResponseEntity<StringBuffer>(pedigreeFile, HttpStatus.CREATED);
				 }else{
					 return getResponseEntityFileForValidationCode(validateCode);
				 }	
			 }else{
					return new ResponseEntity<StringBuffer>(HttpStatus.UNAUTHORIZED);
			}
	    }
		/**
		 * 
		 * @param arkUserVO
		 * @return
		 */
		private boolean isLoginAndCreatePermissionAllow(ArkUserVO arkUserVO){
			return iLoginWebServiceRest.authenticate(arkUserVO) && iLoginWebServiceRest.hasRightSimilarToSubjectAdministrator();
			
		}
		/**
		 * Delete this method once finalized the way to get it from the http header authentication.
		 * @return
		 */
		private ArkUserVO createArkUserinsideUntilFindItFromHeader(){
			ArkUserVO arkUserVO=new ArkUserVO();
			arkUserVO.setUserName("arksuperuser@ark.org.au");
			arkUserVO.setPassword("sanjaya123");
			return arkUserVO;
			
		}
		/**
		 * 
		 * @param validationCode
		 * @return
		 */
		private ResponseEntity<String> getResponseEntityForValidationCode(ValidationType validationType){
			String message = validationType.getName();
			HttpStatus httpStatus = null;
		
			switch (validationType) {
            case INVALID_STUDY_ID:   
            					httpStatus=HttpStatus.OK;	
            					break;
            case SUBJECT_UID_ALREADY_EXISTS:   
            					httpStatus=HttpStatus.OK;
            					break;
            case NOT_EXSISTING_STUDY:   
								httpStatus=HttpStatus.OK;	
								break;
            case NO_GENDERTYPE:   
								httpStatus=HttpStatus.OK;	
								break;
            case INVALID_GENDER_TYPE:   
								httpStatus=HttpStatus.OK;	
								break;
            case NO_VITALTYPE:   
								httpStatus=HttpStatus.OK;	
								break;
            case INVALID_VITAL_TYPE:   
								httpStatus=HttpStatus.OK;	
								break;	
            case SUCCESSFULLY_VALIDATED:	
				            	httpStatus=HttpStatus.OK;	
								break;
			default:
				break;	
			}
			return new ResponseEntity<String>(message,httpStatus);
		}
		
		private ResponseEntity<StringBuffer> getResponseEntityFileForValidationCode(ValidationType validationType){
			StringBuffer stringBuffer=new StringBuffer();
			HttpStatus httpStatus = null;
		
			switch (validationType) {
            case INVALID_STUDY_ID:   
            					httpStatus=HttpStatus.OK;	
            					break;
            case SUBJECT_UID_ALREADY_EXISTS:   
            					httpStatus=HttpStatus.OK;
            					break;
            case NOT_EXSISTING_STUDY:   
								httpStatus=HttpStatus.OK;	
								break;
            case SUCCESSFULLY_VALIDATED:	
				            	httpStatus=HttpStatus.OK;	
								break;
			default:
				break;	
			}
			return new ResponseEntity<StringBuffer>(stringBuffer,httpStatus);
		}

}
