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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.LinkSubjectTwin;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.web.rest.model.RelationShipRequest;
import au.org.theark.web.rest.model.SubjectRequest;
import au.org.theark.web.rest.model.TwinRequest;
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
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/subject/", method = RequestMethod.POST,produces = "text/plain")
	public ResponseEntity<String> createSubject(@PathVariable("id") Long studyId,@RequestBody SubjectRequest subjectRequest,@RequestHeader HttpHeaders httpHeaders , UriComponentsBuilder ucBuilder) {
	
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
				ValidationType validationType=iPedWebSerRest.validateEntireSubjectRequest(subjectRequest,Constants.ACTION_INSERT);
				if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					SubjectVO subjectvo=iPedWebSerRest.mapSubjectRequestToBusinessSubjectVO(subjectRequest);
					iPedWebSerRest.createSubject(subjectvo);
					HttpHeaders headers = new HttpHeaders();
					headers.setLocation(ucBuilder.path("/study/{id}/subject/{id}").buildAndExpand(studyId,subjectvo.getLinkSubjectStudy().getId()).toUri());
					message="Subject created successfuly.";
					return new ResponseEntity<String>(message,headers, HttpStatus.CREATED);
				}else{
					return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
				}
			}else{
					message="Unauthorised user.Please check the credentials for the study.";
					return new ResponseEntity<String>(message,HttpStatus.UNAUTHORIZED);
			}
	}

	//2-Update subject
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/subject/{lid}", method = RequestMethod.PUT,produces = "text/plain")
	public ResponseEntity<String> updateSubject(@PathVariable("id") Long studyId,@PathVariable("lid") Long id,@RequestBody SubjectRequest subjectRequest,@RequestHeader HttpHeaders httpHeaders , UriComponentsBuilder ucBuilder) {
	
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
				subjectRequest.setId(id);
				ValidationType validationType=iPedWebSerRest.validateEntireSubjectRequest(subjectRequest,Constants.ACTION_UPDATE);
				if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					SubjectVO subjectvo=iPedWebSerRest.mapUpdateSubjectRequestToBusinessSubjectVO(subjectRequest);
					iPedWebSerRest.updateSubject(subjectvo);
					HttpHeaders headers = new HttpHeaders();
					headers.setLocation(ucBuilder.path("/study/{id}/subject/{lid}").buildAndExpand(studyId,subjectvo.getLinkSubjectStudy().getId()).toUri());
					message="Subject updated successfuly.";
					return new ResponseEntity<String>(message,headers, HttpStatus.CREATED);
				}else{
					return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
				}
			}else{
					message="Unauthorised user.Please check the credentials for the study.";
					return new ResponseEntity<String>(message,HttpStatus.UNAUTHORIZED);
			}
	}

	//3-Get Subject
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/subject/{uid}", method = RequestMethod.GET)
    public ResponseEntity<SubjectRequest> getLinkSubjectStudy(@PathVariable("id") Long studyId,@PathVariable("uid") String subjectUid,@RequestHeader HttpHeaders httpHeaders) {
		HttpHeaders headers = new HttpHeaders();
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
			 SubjectRequest createSubjectRequest;
			 ValidationType validationType=iPedWebSerRest.validateForStudy(studyId);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			        LinkSubjectStudy linkSubjectStudy = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(subjectUid, iPedWebSerRest.getStudyByID(studyId));
			        if (linkSubjectStudy== null || linkSubjectStudy.getId()==null) {
			        	message="Can not find a subject for this subjectuid.";
			            return new ResponseEntity<SubjectRequest>(HttpStatus.NOT_FOUND);
			        }else{
			          createSubjectRequest=	iPedWebSerRest.mapLinkSubjectStudyToCreateSubjectRequests(linkSubjectStudy);
			        }
			        return new ResponseEntity<SubjectRequest>(createSubjectRequest, HttpStatus.OK);
			 }else{
				 	headers.set("validation", validationType.getName());
					return new ResponseEntity<SubjectRequest>(headers,getResponseEntityForValidationCode(validationType)); 
			 }   
		 }else{
			 	message="Unauthorised user.Please check the credentials for the study.";
				return new ResponseEntity<SubjectRequest>(HttpStatus.UNAUTHORIZED);
		}
    }
	
	//4-Get List of subjects for a study.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/subject", method = RequestMethod.GET)
    public ResponseEntity<List<SubjectRequest>> getSubjectListForStudy(@PathVariable("id") Long studyId,@RequestHeader HttpHeaders httpHeaders) {
		HttpHeaders headers = new HttpHeaders();
		List<SubjectRequest> subjectRequests = null;
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
			 ValidationType validationType=iPedWebSerRest.validateForStudy(studyId);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			     List<LinkSubjectStudy> linkSubjectStudies = iPedWebSerRest.getListofLinkSubjectStudiesForStudy(iPedWebSerRest.getStudyByID(studyId));
			        if (linkSubjectStudies!= null && !linkSubjectStudies.isEmpty()) {
			        	 subjectRequests=iPedWebSerRest.mapListOfLinkSubjectStudiesToListOfSubjectRequests(linkSubjectStudies);
			            return new ResponseEntity<List<SubjectRequest>>(subjectRequests,HttpStatus.OK);
			        }
			        message="Can not find any subject for this study.";
			        headers.set("validation", message);
			        return new ResponseEntity<List<SubjectRequest>>(headers, HttpStatus.NOT_FOUND);
			 }else{
				 headers.set("validation", validationType.getName());
				 return new ResponseEntity<List<SubjectRequest>>(headers,getResponseEntityForValidationCode(validationType)); 
			 }   
		 }else{
				return new ResponseEntity<List<SubjectRequest>>(HttpStatus.UNAUTHORIZED);
		}
    }
	
	//5-Set Relationship (Mother or Father) 
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/relationship/", method = RequestMethod.POST,produces = "text/plain")
	public ResponseEntity<String> createRelationShip(@PathVariable("id") Long studyId,@RequestBody RelationShipRequest relationShipRequest,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			 ValidationType validationType=iPedWebSerRest.validateRelationShipForStudy(relationShipRequest);
			 Study study=iPedWebSerRest.getStudyByID(studyId);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 LinkSubjectStudy subject = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(relationShipRequest.getSubjectUID(), study);
				 SubjectVO subjectVO=new SubjectVO();
				 subjectVO.setLinkSubjectStudy(subject);
				 LinkSubjectStudy relative = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(relationShipRequest.getRelativeUID(), study);
				 SubjectVO relativeVO=new SubjectVO();
				 relativeVO.setLinkSubjectStudy(relative);
				 if(iPedWebSerRest.isCircularValidationSuccessful(subjectVO,relativeVO)){
					Relationship relationship=iPedWebSerRest.getRelationShipByname(relationShipRequest.getParentType());
					iPedWebSerRest.createRelationShip(subjectVO,relativeVO,relationship);
				 }else{
						return new ResponseEntity<String>(ValidationType.CIRCULAR_VALIDATION_UNSUCCESSFUL.getName(),HttpStatus.CONFLICT); 
				}	
					headers.setLocation(ucBuilder.path("/study/{id}/relationship/{id}").buildAndExpand(studyId,subject.getId()).toUri());
					return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			 	}else{
					return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
			 }   
			}else{
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
	}
	
	//6-Delete Relationships.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{sid}/relationship/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteRelationShip(@PathVariable("sid") Long studyId,@PathVariable("id") Long relationShipId,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			 ValidationType validationType=iPedWebSerRest.validateParentRelationShip(relationShipId);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 iPedWebSerRest.deleteRelationShip(relationShipId);	
				 headers.setLocation(ucBuilder.path("/study/{id}/relationship/{id}").buildAndExpand(studyId,relationShipId).toUri());
				 return new ResponseEntity<String>(headers, HttpStatus.ACCEPTED);
			 	}else{
			 		return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
			 }   
			}else{
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
	}
	
	//7-Get List of relationships for a study.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/relationship", method = RequestMethod.GET)
	public ResponseEntity<List<RelationShipRequest>> getParentRelationshipListForStudy(@PathVariable("id") Long studyId,@RequestHeader HttpHeaders httpHeaders) {
			HttpHeaders headers = new HttpHeaders();
			List<RelationShipRequest> relationshipRequests = null;
			 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
				 ValidationType validationType=iPedWebSerRest.validateForStudy(studyId);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				     List<LinkSubjectPedigree> linkSubjectPedigrees = iPedWebSerRest.getListofLinkSubjectPedigreeForStudy(iPedWebSerRest.getStudyByID(studyId));
				        if (linkSubjectPedigrees!= null && !linkSubjectPedigrees.isEmpty()) {
				        	 relationshipRequests=iPedWebSerRest.mapListOfLinkSubjectPedigreesToListOfRelationShipRequests(linkSubjectPedigrees);
				            return new ResponseEntity<List<RelationShipRequest>>(relationshipRequests,HttpStatus.OK);
				        }
				        message="Can not find any parent relations for this study.";
				        headers.set("validation", message);
				        return new ResponseEntity<List<RelationShipRequest>>(headers, HttpStatus.NOT_FOUND);
				 }else{
					 headers.set("validation", validationType.getName());
					 return new ResponseEntity<List<RelationShipRequest>>(headers,getResponseEntityForValidationCode(validationType)); 
				 }   
			 }else{
					return new ResponseEntity<List<RelationShipRequest>>(HttpStatus.UNAUTHORIZED);
			}
	    }
	
	//8-Set Twins
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/twintype/", method = RequestMethod.POST,produces = "text/plain")
	public ResponseEntity<String> createTwin(@PathVariable("id") Long studyId,@RequestBody TwinRequest twinRequest,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			 ValidationType validationType=iPedWebSerRest.validateTwinTypeForStudy(twinRequest);
			 Study study=iPedWebSerRest.getStudyByID(studyId);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 LinkSubjectStudy subject = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(twinRequest.getSubjectUID(), study);
				 SubjectVO subjectVO=new SubjectVO();
				 subjectVO.setLinkSubjectStudy(subject);
				 LinkSubjectStudy relative = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(twinRequest.getRelativeUID(), study);
				 SubjectVO relativeVO=new SubjectVO();
				 relativeVO.setLinkSubjectStudy(relative);
				 if(iPedWebSerRest.isRelativeASibling(subjectVO, relativeVO)){
					TwinType twinType=iPedWebSerRest.getTwinTypeByname(twinRequest.getTwinType());
					iPedWebSerRest.createTwin(subjectVO, relativeVO, twinType);
				 }else{
					 return new ResponseEntity<String>(ValidationType.NOT_A_SIBLING.getName(),HttpStatus.FORBIDDEN);
				 }
					headers.setLocation(ucBuilder.path("/study/{id}/twintype/").buildAndExpand(studyId).toUri());
					return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			 	}else{
			 		return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
			 }   
		}else{
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	//9-Delete Twins.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{sid}/twintype/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTwin(@PathVariable("sid") Long studyId,@PathVariable("id") Long twinTypeId,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
			HttpHeaders headers = new HttpHeaders();
			if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
				 ValidationType validationType=iPedWebSerRest.validateTwinRelationShip(twinTypeId);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					 iPedWebSerRest.deleteTwin(twinTypeId);	
					 headers.setLocation(ucBuilder.path("/study/{id}/twintype/{id}").buildAndExpand(studyId,twinTypeId).toUri());
					 return new ResponseEntity<String>(headers, HttpStatus.ACCEPTED);
				 	}else{
				 		return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
				 }   
				}else{
					return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
				}
		}
	
	//10-Get List of twins for a study.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/twintype", method = RequestMethod.GET)
	public ResponseEntity<List<TwinRequest>> getTwinListForStudy(@PathVariable("id") Long studyId,@RequestHeader HttpHeaders httpHeaders) {
				HttpHeaders headers = new HttpHeaders();
				List<TwinRequest> twinRequests = null;
				 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
					 ValidationType validationType=iPedWebSerRest.validateForStudy(studyId);
					 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					     List<LinkSubjectTwin> linkSubjectTwins = iPedWebSerRest.getListofLinkSubjectTwinForStudy(iPedWebSerRest.getStudyByID(studyId));
					        if (linkSubjectTwins!= null && !linkSubjectTwins.isEmpty()) {
					        	 twinRequests=iPedWebSerRest.mapListOfLinkSubjectTwinsToListOfTwinRequests(linkSubjectTwins);
					            return new ResponseEntity<List<TwinRequest>>(twinRequests,HttpStatus.OK);
					        }
					        message="Can not find any twins for this study.";
					        headers.set("validation", message);
					        return new ResponseEntity<List<TwinRequest>>(headers, HttpStatus.NOT_FOUND);
					 }else{
						 headers.set("validation", validationType.getName());
						 return new ResponseEntity<List<TwinRequest>>(headers,getResponseEntityForValidationCode(validationType)); 
					 }   
				 }else{
						return new ResponseEntity<List<TwinRequest>>(HttpStatus.UNAUTHORIZED);
				}
		    }

	//11-Get Pedigree view as a file
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/pedigree/{uid}", method = RequestMethod.GET)
	public ResponseEntity<String> getPedigreeView(@PathVariable("id") Long studyId,@PathVariable("uid") String uid,@RequestHeader HttpHeaders httpHeaders) {
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			 ValidationType validationType=iPedWebSerRest.validateForSubjectUIDForStudy(studyId,uid);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			 String pedigreeFile=iPedWebSerRest.getPedigreeView(uid,studyId);
	         if (pedigreeFile== null) {
		           return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		      }
	         	return new ResponseEntity<String>(pedigreeFile, HttpStatus.CREATED);
			 }else{
				 return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
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
		private HttpStatus getResponseEntityForValidationCode(ValidationType validationType){
			HttpStatus httpStatus;
			switch (validationType) {
			
            case INVALID_STUDY_ID:   
            					httpStatus=HttpStatus.NOT_FOUND;	
            					break;
            case SUBJECT_UID_ALREADY_EXISTS:   
            					httpStatus=HttpStatus.CONFLICT;
            					break;
            case NOT_EXSISTING_STUDY:   
								httpStatus=HttpStatus.NOT_FOUND;	
								break;
            case NO_GENDERTYPE:   
								httpStatus=HttpStatus.NOT_FOUND;	
								break;
            case INVALID_GENDER_TYPE:   
								httpStatus=HttpStatus.NOT_FOUND;	
								break;
            case NO_VITALTYPE:   
								httpStatus=HttpStatus.NOT_FOUND;	
								break;
            case INVALID_VITAL_TYPE:   
								httpStatus=HttpStatus.NOT_FOUND;	
								break;	
            case NO_SUBJECT_STATUS:   
								httpStatus=HttpStatus.NOT_FOUND;	
								break;
            case INVALID_SUBJECT_STATUS:   
            					httpStatus=HttpStatus.NOT_FOUND;	
            					break;	
            case SUBJECT_UID_NOT_EXISTS:
            					httpStatus=HttpStatus.CONFLICT;
            					break;
            case RELATIVE_SUBJECT_UID_NOT_EXISTS:
            					httpStatus=HttpStatus.CONFLICT;	
            					break;
            case NO_PARENT_TYPE:
            					httpStatus=HttpStatus.NOT_FOUND;	
            					break;
            case INVALID_PARENT_TYPE:
            					httpStatus=HttpStatus.NOT_FOUND;	
            					break;
            case NO_TWIN_TYPE:
				            	httpStatus=HttpStatus.NOT_FOUND;	
								break;
            case INVALID_TWIN_TYPE:
            					httpStatus=HttpStatus.NOT_FOUND;	
            					break;
            case CIRCULAR_VALIDATION_UNSUCCESSFUL:
            					httpStatus=HttpStatus.FORBIDDEN;	
            					break;
            case NOT_A_SIBLING:
            					httpStatus=HttpStatus.NOT_FOUND;	
            					break;
            case PARENT_RELATION_SHIP_ALREADY_EXISTS:
            					httpStatus=HttpStatus.CONFLICT;	
            					break;
            case TWIN_RELATION_SHIP_ALREADY_EXISTS:
            					httpStatus=HttpStatus.CONFLICT;	
            					break;
            case SUCCESSFULLY_VALIDATED:	
				            	httpStatus=HttpStatus.OK;	
								break;
			default:
				httpStatus=HttpStatus.BAD_REQUEST;
				break;	
			}
			return httpStatus;
		}

}
