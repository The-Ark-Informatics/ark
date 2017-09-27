package au.org.theark.web.rest.controller;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import au.org.theark.web.rest.model.CreateTwinRequest;
import au.org.theark.web.rest.model.GetSubjectRequest;
import au.org.theark.web.rest.model.ValidationType;
import au.org.theark.web.rest.service.ILoginWebServiceRest;
import au.org.theark.web.rest.service.IPedigreeWebServiceRest;


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
	public ResponseEntity<String> createSubject(@RequestBody CreateSubjectRequest createSubjectRequest,@RequestHeader HttpHeaders httpHeaders , UriComponentsBuilder ucBuilder) {
	
		if(isAuthenticateSuccessfulForStudy(httpHeaders,createSubjectRequest.getStudyId())){
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
					message="Unauthorised user.Please check the credentials for the study.";
					return new ResponseEntity<String>(message,HttpStatus.UNAUTHORIZED);
			}
	}

	//2-Get Subject
	@RequestMapping(value = "/getsubject/", method = RequestMethod.POST)
    public ResponseEntity<String> getLinkSubjectStudy(@RequestBody  GetSubjectRequest getSubjectRequest,@RequestHeader HttpHeaders httpHeaders) {
		
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,getSubjectRequest.getStudyId())){ 
			 ValidationType validateCode=iPedWebSerRest.validateForStudy(getSubjectRequest.getStudyId());
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
			 	message="Unauthorised user.Please check the credentials for the study.";
				return new ResponseEntity<String>(message,HttpStatus.UNAUTHORIZED);
		}
    }
	
	//Get List of subjects for a study.
	@RequestMapping(value = "/getsubjects/", method = RequestMethod.POST)
    public ResponseEntity<List<CreateSubjectRequest>> getSubjectListForStudy(@RequestBody  Long studyId,@RequestHeader HttpHeaders httpHeaders) {
		HttpHeaders headers = new HttpHeaders();
		List<CreateSubjectRequest> createSubjectRequests = null;
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
			 ValidationType validateCode=iPedWebSerRest.validateForStudy(studyId);
			 if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			     List<LinkSubjectStudy> linkSubjectStudies = iPedWebSerRest.getListofLinkSubjectStudiesForStudy(iPedWebSerRest.getStudyByID(studyId));
			        if (linkSubjectStudies!= null && !linkSubjectStudies.isEmpty()) {
			        	 createSubjectRequests=iPedWebSerRest.mapListOfLinkSubjectStudiesToListOfCreateSubjectRequests(linkSubjectStudies);
			            return new ResponseEntity<List<CreateSubjectRequest>>(createSubjectRequests,HttpStatus.OK);
			        }
			        message="Can not find any subject for this study.";
			        headers.set("validation", message);
			        return new ResponseEntity<List<CreateSubjectRequest>>(headers, HttpStatus.NOT_FOUND);
			 }else{
				 return getResponseEntityOnHeaderValidationCode(validateCode);
			 }   
		 }else{
				return new ResponseEntity<List<CreateSubjectRequest>>(HttpStatus.UNAUTHORIZED);
		}
    }
	
	//3-Set Mother or Father 
	@RequestMapping(value = "/createRelationShip/", method = RequestMethod.POST)
	public ResponseEntity<String> createRelationShip(@RequestBody CreateRelationShipRequest createRelationShipRequest,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,createRelationShipRequest.getStudyId())){
			 ValidationType validateCode=iPedWebSerRest.validateRelationShipForStudy(createRelationShipRequest);
			 Study study=iPedWebSerRest.getStudyByID(createRelationShipRequest.getStudyId());
			 if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 LinkSubjectStudy subject = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(createRelationShipRequest.getSubjectUID(), study);
				 SubjectVO subjectVO=new SubjectVO();
				 subjectVO.setLinkSubjectStudy(subject);
				 LinkSubjectStudy relative = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(createRelationShipRequest.getRelativeUID(), study);
				 SubjectVO relativeVO=new SubjectVO();
				 relativeVO.setLinkSubjectStudy(relative);
				 if(iPedWebSerRest.isCircularValidationSuccessful(subjectVO,relativeVO)){
					Relationship relationship=iPedWebSerRest.getRelationShipByname(createRelationShipRequest.getParentType());
					iPedWebSerRest.createRelationShip(subjectVO,relativeVO,relationship);
				 }else{
					return getResponseEntityForValidationCode(ValidationType.CIRCULAR_VALIDATION_UNSUCCESSFUL);	
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
	
	//4-Set Mother or Father or Twins
	@RequestMapping(value = "/createTwin/", method = RequestMethod.POST)
	public ResponseEntity<String> createTwin(@RequestBody CreateTwinRequest createTwinRequest,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,createTwinRequest.getStudyId())){
			 ValidationType validateCode=iPedWebSerRest.validateTwinTypeForStudy(createTwinRequest);
			 Study study=iPedWebSerRest.getStudyByID(createTwinRequest.getStudyId());
			 if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 LinkSubjectStudy subject = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(createTwinRequest.getSubjectUID(), study);
				 SubjectVO subjectVO=new SubjectVO();
				 subjectVO.setLinkSubjectStudy(subject);
				 LinkSubjectStudy relative = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(createTwinRequest.getRelativeUID(), study);
				 SubjectVO relativeVO=new SubjectVO();
				 relativeVO.setLinkSubjectStudy(relative);
				 if(iPedWebSerRest.isRelativeASibling(subjectVO, relativeVO)){
					TwinType twinType=iPedWebSerRest.getTwinTypeByname(createTwinRequest.getTwinType());
					iPedWebSerRest.createTwin(subjectVO, relativeVO, twinType);
				 }else{
					 return getResponseEntityForValidationCode(ValidationType.NOT_A_SIBLING);	
				 }
					headers.setLocation(ucBuilder.path("/createTwin/{id}").buildAndExpand(subject.getId()).toUri());
					return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			 	}else{
				 return getResponseEntityForValidationCode(validateCode);
			 }   
		}else{
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
	}	
	//5-Get Pedigree view as a file
	@RequestMapping(value = "/getPedigreeView/", method = RequestMethod.POST)
	public ResponseEntity<String> getPedigreeView(@RequestBody GetSubjectRequest getSubjectRequest,@RequestHeader HttpHeaders httpHeaders) {
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,getSubjectRequest.getStudyId())){
			 ValidationType validateCode=iPedWebSerRest.validateForSubjectUIDForStudy(getSubjectRequest);
			 if(validateCode.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			 String pedigreeFile=iPedWebSerRest.getPedigreeView(getSubjectRequest.getSubjectUID(),getSubjectRequest.getStudyId());
	         if (pedigreeFile== null) {
		           return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		      }
	         	return new ResponseEntity<String>(pedigreeFile, HttpStatus.CREATED);
			 }else{
				 return getResponseEntityForValidationCode(validateCode);
			 }	
		 }else{
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
	 }
		/**
		 * 
		 * @param arkUserVO
		 * @return
		 */
		private boolean isAuthenticateSuccessfulForStudy(HttpHeaders httpHeaders,Long studyId){
			final String authorization = httpHeaders.getFirst("Authorization");
		    if (authorization != null && authorization.startsWith("Basic")) {
		        // Authorization: Basic base64credentials
		        String base64Credentials = authorization.substring("Basic".length()).trim();
		        String credentials = new String(Base64.decodeBase64(base64Credentials.getBytes(Charset.forName("US-ASCII"))));
		        // credentials = username:password
		        final String[] values = credentials.split(":",2);
		        ArkUserVO arkUserVO=new ArkUserVO();
				arkUserVO.setUserName(values[0]);
				arkUserVO.setPassword(values[1]);
				return iLoginWebServiceRest.authenticate(arkUserVO) && iLoginWebServiceRest.hasPermissionForStudy(studyId);
		    }else{
		    	return false;
		    }
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
		/**
		 * 
		 * @param validationType
		 * @return
		 */
		private ResponseEntity<List<CreateSubjectRequest>> getResponseEntityOnHeaderValidationCode(ValidationType validationType){
			//String message = validationType.getName();
			HttpHeaders httpHeaders=new HttpHeaders();
			httpHeaders.set("validation",validationType.getName());
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
			return new ResponseEntity<List<CreateSubjectRequest>>(httpHeaders,httpStatus);
		}

}
