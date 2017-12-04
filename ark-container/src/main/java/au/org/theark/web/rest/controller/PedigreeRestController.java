package au.org.theark.web.rest.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
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
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.LinkSubjectPedigree;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.LinkSubjectTwin;
import au.org.theark.core.model.study.entity.Relationship;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.model.study.entity.TwinType;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.web.rest.model.ApiError;
import au.org.theark.web.rest.model.BinaryCustomFieldsResponse;
import au.org.theark.web.rest.model.ConfigRequest;
import au.org.theark.web.rest.model.MadelineObject;
import au.org.theark.web.rest.model.MembershipResponse;
import au.org.theark.web.rest.model.PedigreeResponse;
import au.org.theark.web.rest.model.RelationShipRequest;
import au.org.theark.web.rest.model.SiblingsResponse;
import au.org.theark.web.rest.model.SubjectRequest;
import au.org.theark.web.rest.model.TwinRequest;
import au.org.theark.web.rest.model.ValidationType;
import au.org.theark.web.rest.service.ILoginWebServiceRest;
import au.org.theark.web.rest.service.IPedigreeWebServiceRest;


@RestController
public class PedigreeRestController {

	private static final String SUBJECT = "Subject";
	
	private static final String RELATIONSHIP = "Relationship";
	
	private static final String TWINTYPE = "Twintype";
	
	private static final String CONFIGURATION = "Configuration";
	
	public static final Logger logger = LoggerFactory.getLogger(PedigreeRestController.class);

	@Autowired
	IPedigreeWebServiceRest iPedWebSerRest;
	
	@Autowired
	ILoginWebServiceRest iLoginWebServiceRest;

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
					return new ResponseEntity<String>(SUBJECT+ValidationType.CREATED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.CREATED_SUCCESSFULLY));
				}else{
					return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
				}
			}else{
					return new ResponseEntity<String>(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName(),getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
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
					return new ResponseEntity<String>(SUBJECT+ValidationType.UPDATED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.UPDATED_SUCCESSFULLY));
				}else{
					return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
				}
			}else{
					return new ResponseEntity<String>(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName(),getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
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
			        	 headers.set("message", ValidationType.SUBJECT_UID_NOT_EXISTS.getName());
			            return new ResponseEntity<SubjectRequest>(headers,getResponseEntityForValidationCode(ValidationType.SUBJECT_UID_NOT_EXISTS));
			        }else{
			        	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());	
			          createSubjectRequest=	iPedWebSerRest.mapLinkSubjectStudyToCreateSubjectRequests(linkSubjectStudy);
			        }
			        return new ResponseEntity<SubjectRequest>(createSubjectRequest,headers,HttpStatus.OK);
			 }else{
				 	headers.set("message", validationType.getName());
					return new ResponseEntity<SubjectRequest>(headers,getResponseEntityForValidationCode(validationType)); 
			 }   
		 }else{
			 	headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName() );
				return new ResponseEntity<SubjectRequest>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
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
			        	 headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());	
			            return new ResponseEntity<List<SubjectRequest>>(subjectRequests,headers,HttpStatus.OK);
			        }
			        headers.set("message", ValidationType.NO_SUBJECT.getName());
			        return new ResponseEntity<List<SubjectRequest>>(headers, getResponseEntityForValidationCode(ValidationType.NO_SUBJECT));
			 }else{
				 headers.set("message", validationType.getName());
				 return new ResponseEntity<List<SubjectRequest>>(headers,getResponseEntityForValidationCode(validationType)); 
			 }   
		 }else{
			 headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
			 return new ResponseEntity<List<SubjectRequest>>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
		}
    }
	
	//5-Set Relationship (Mother or Father) 
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/relationship/", method = RequestMethod.POST,produces = "text/plain")
	public ResponseEntity<String> createRelationShip(@PathVariable("id") Long studyId,@RequestBody RelationShipRequest relationShipRequest,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			//Set study id for the validation if not assigned in the request.
			if(relationShipRequest.getStudyId()==null){
				relationShipRequest.setStudyId(studyId);
			}
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
						return new ResponseEntity<String>(ValidationType.CIRCULAR_VALIDATION_UNSUCCESSFUL.getName(),getResponseEntityForValidationCode(ValidationType.CIRCULAR_VALIDATION_UNSUCCESSFUL)); 
				}	
					headers.setLocation(ucBuilder.path("/study/{id}/relationship/{id}").buildAndExpand(studyId,subject.getId()).toUri());
					return new ResponseEntity<String>(RELATIONSHIP+ValidationType.CREATED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.CREATED_SUCCESSFULLY));
			 	}else{
					return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
			 }   
			}else{
				headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
				return new ResponseEntity<String>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
			}
	}
	
	//6-Delete Relationships.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{sid}/relationship/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteRelationShip(@PathVariable("sid") Long studyId,@PathVariable("id") Long relationShipId,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			 ValidationType validationType=iPedWebSerRest.validateParentRelationShip(studyId,relationShipId);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				 iPedWebSerRest.deleteRelationShip(relationShipId);	
				 headers.setLocation(ucBuilder.path("/study/{id}/relationship/{id}").buildAndExpand(studyId,relationShipId).toUri());
				 return new ResponseEntity<String>(RELATIONSHIP+ValidationType.DELETED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.DELETED_SUCCESSFULLY));
			 	}else{
			 		return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
			 }   
			}else{
				headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
				return new ResponseEntity<String>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
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
				        	 headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());	 
				            return new ResponseEntity<List<RelationShipRequest>>(relationshipRequests,headers,HttpStatus.OK);
				        }
				        headers.set("message", ValidationType.NO_PARENT_RELATIONSHIP_EXISTS_FOR_STUDY.getName());
				        return new ResponseEntity<List<RelationShipRequest>>(headers, getResponseEntityForValidationCode(ValidationType.NO_PARENT_RELATIONSHIP_EXISTS_FOR_STUDY));
				 }else{
					 headers.set("message", validationType.getName());
					 return new ResponseEntity<List<RelationShipRequest>>(headers,getResponseEntityForValidationCode(validationType)); 
				 }   
			 }else{
				 headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
				 return new ResponseEntity<List<RelationShipRequest>>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
			}
	    }
	
	//8-Set Twins
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/twintype/", method = RequestMethod.POST,produces = "text/plain")
	public ResponseEntity<String> createTwin(@PathVariable("id") Long studyId,@RequestBody TwinRequest twinRequest,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			//Set study id for the validation if not assigned in the request.
			if(twinRequest.getStudyId()==null){
				twinRequest.setStudyId(studyId);
			}
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
					 return new ResponseEntity<String>(ValidationType.NOT_A_SIBLING.getName(),getResponseEntityForValidationCode(ValidationType.NOT_A_SIBLING));
				 }
					headers.setLocation(ucBuilder.path("/study/{id}/twintype/").buildAndExpand(studyId).toUri());
					return new ResponseEntity<String>(TWINTYPE+ValidationType.CREATED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.CREATED_SUCCESSFULLY));
			 	}else{
			 		return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
			 }   
		}else{
			headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
			return new ResponseEntity<String>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
		}
	}
	
	//9-Delete Twins.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{sid}/twintype/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTwin(@PathVariable("sid") Long studyId,@PathVariable("id") Long twinTypeId,@RequestHeader HttpHeaders httpHeaders,UriComponentsBuilder ucBuilder) {
			HttpHeaders headers = new HttpHeaders();
			if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
				 ValidationType validationType=iPedWebSerRest.validateTwinRelationShip(studyId,twinTypeId);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					 iPedWebSerRest.deleteTwin(twinTypeId);	
					 headers.setLocation(ucBuilder.path("/study/{id}/twintype/{id}").buildAndExpand(studyId,twinTypeId).toUri());
					 return new ResponseEntity<String>(TWINTYPE+ValidationType.DELETED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.DELETED_SUCCESSFULLY));
				 	}else{
				 		return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
				 }   
				}else{
					headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
					return new ResponseEntity<String>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
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
					        	 headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());	 
						         return new ResponseEntity<List<TwinRequest>>(twinRequests,headers,HttpStatus.OK);
					        }
					        headers.set("message", ValidationType.NO_TWINTYPE_RELATIONSHIP_EXISTS_FOR_STUDY.getName());
					        return new ResponseEntity<List<TwinRequest>>(headers, getResponseEntityForValidationCode(ValidationType.NO_TWINTYPE_RELATIONSHIP_EXISTS_FOR_STUDY));
					 }else{
						 headers.set("message", validationType.getName());
						 return new ResponseEntity<List<TwinRequest>>(headers,getResponseEntityForValidationCode(validationType)); 
					 }   
				 }else{
					 headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
						return new ResponseEntity<List<TwinRequest>>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
				}
		    }

	//11-Get Pedigree view for the persistence subjects.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/pedigree/{uid}", method = RequestMethod.GET)
	public ResponseEntity<Object> getPedigreeView(@PathVariable("id") Long studyId,@PathVariable("uid") String uid,@RequestHeader HttpHeaders httpHeaders) {
		 HttpHeaders headers = new HttpHeaders();
		 ValidationType validationType=null;
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			 validationType=iPedWebSerRest.validateForSubjectUIDForStudy(studyId,uid);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			 String pedigreeFile=iPedWebSerRest.getPedigreeView(uid,studyId);
			 if (pedigreeFile== null || StringUtils.contains(pedigreeFile, "Pedigree Error") || StringUtils.contains(pedigreeFile, "No Pedigree History")) {
	        	 headers.set("message", ValidationType.PEDIGREE_VIEW_NOT_EXISTS.getName());
	        	 ApiError apiError =   new ApiError( getResponseEntityForValidationCode(ValidationType.PEDIGREE_VIEW_NOT_EXISTS),ValidationType.PEDIGREE_VIEW_NOT_EXISTS.getName(),ValidationType.PEDIGREE_VIEW_NOT_EXISTS.getName());
					return new ResponseEntity<Object>(apiError,apiError.getStatus()); 
			    // return new ResponseEntity<Object>(headers, getResponseEntityForValidationCode(ValidationType.PEDIGREE_VIEW_NOT_EXISTS));
		      }
	         	PedigreeResponse pedigreeResponse=new PedigreeResponse();
	         	pedigreeResponse.setSvg(pedigreeFile);
	         	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());
	         	return new ResponseEntity<Object>(pedigreeResponse,headers,HttpStatus.OK);
			 }else{
				 ApiError apiError =   new ApiError(getResponseEntityForValidationCode(validationType),validationType.getName(),validationType.getName());
				return new ResponseEntity<Object>(apiError,apiError.getStatus()); 
			 }	
		 }else{
			 	
		     	 ApiError apiError =   new ApiError(getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES),ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName(),ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
			 	headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
				return new ResponseEntity<Object>(apiError,apiError.getStatus()); 
		}
	 }
	//12-Get Study Pedigree Configuration
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/configuration/", method = RequestMethod.GET)
	public ResponseEntity<ConfigRequest> getPedigreeConfig(@PathVariable("id") Long studyId,@RequestHeader HttpHeaders httpHeaders) {
		HttpHeaders headers = new HttpHeaders();
		 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
			 ValidationType validationType=iPedWebSerRest.validateForStudy(studyId);
			 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
			 StudyPedigreeConfiguration studyPedigreeConfiguration=iPedWebSerRest.getStudyPedigreeConfiguration(studyId);
	         if (studyPedigreeConfiguration== null) {
	        	 headers.set("message", ValidationType.PEDIGREE_CONFIG_NOT_EXISTS.getName());
			     return new ResponseEntity<ConfigRequest>(headers,getResponseEntityForValidationCode(ValidationType.PEDIGREE_CONFIG_NOT_EXISTS));
		      }
	         	ConfigRequest config=iPedWebSerRest.mapStudyPedigreeConfigurationToConfigRequest(studyPedigreeConfiguration);
	         	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());
	         	return new ResponseEntity<ConfigRequest>(config,headers,HttpStatus.OK);
			 }else{
				 headers.set("message", validationType.getName());
				 return new ResponseEntity<ConfigRequest>(headers,getResponseEntityForValidationCode(validationType)); 
			 }	
		 }else{
			 headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
			 return new ResponseEntity<ConfigRequest>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
		}
	 }
	
	//13-Create Pedigree config.
	@CrossOrigin(origins = "http://localhost:8082")
	@RequestMapping(value = "/study/{id}/configuration/", method = RequestMethod.POST,produces = "text/plain")
	public ResponseEntity<String> createPedigreeConfig(@PathVariable("id") Long studyId,@RequestBody ConfigRequest configRequest,@RequestHeader HttpHeaders httpHeaders , UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
				//Set study id for the validation if not assigned in the request.
				if(configRequest.getStudyId()==null){
					configRequest.setStudyId(studyId);
				}
				ValidationType validationType=iPedWebSerRest.validateConfigRequest(configRequest,Constants.ACTION_INSERT);
				if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					StudyPedigreeConfiguration studyPedigreeConfiguration=iPedWebSerRest.mapConfigRequestToStudyPedigreeConfiguration(configRequest,Constants.ACTION_INSERT);
					iPedWebSerRest.saveOrUpdateStudyPedigreeConfiguration(studyPedigreeConfiguration);
					headers.setLocation(ucBuilder.path("/study/{id}/configuration/").buildAndExpand(studyId,studyPedigreeConfiguration.getStudy().getId()).toUri());
					return new ResponseEntity<String>(CONFIGURATION+ValidationType.CREATED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.CREATED_SUCCESSFULLY));
				}else{
					return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
				}
			}else{
				headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
				return new ResponseEntity<String>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
			}
	}
	//14-Update Pedigree config.
		@CrossOrigin(origins = "http://localhost:8082")
		@RequestMapping(value = "/study/{id}/configuration/", method = RequestMethod.PUT,produces = "text/plain")
		public ResponseEntity<String> updatePedigreeConfig(@PathVariable("id") Long studyId,@RequestBody ConfigRequest configRequest,@RequestHeader HttpHeaders httpHeaders , UriComponentsBuilder ucBuilder) {
			HttpHeaders headers = new HttpHeaders();
			if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
					//Set study id for the validation if not assigned in the request.
					if(configRequest.getStudyId()==null){
						configRequest.setStudyId(studyId);
					}
					ValidationType validationType=iPedWebSerRest.validateConfigRequest(configRequest,Constants.ACTION_UPDATE);
					if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
						StudyPedigreeConfiguration studyPedigreeConfiguration=iPedWebSerRest.mapConfigRequestToStudyPedigreeConfiguration(configRequest,Constants.ACTION_UPDATE);
						iPedWebSerRest.saveOrUpdateStudyPedigreeConfiguration(studyPedigreeConfiguration);
						headers.setLocation(ucBuilder.path("/study/{id}/configuration/").buildAndExpand(studyId,studyPedigreeConfiguration.getStudy().getId()).toUri());
						return new ResponseEntity<String>(CONFIGURATION+ValidationType.UPDATED_SUCCESSFULLY.getName(),headers,getResponseEntityForValidationCode(ValidationType.UPDATED_SUCCESSFULLY));
					}else{
						return new ResponseEntity<String>(validationType.getName(),getResponseEntityForValidationCode(validationType)); 
					}
				}else{
					headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
					return new ResponseEntity<String>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
				}
		}
		
		//15-Get Study Member ship.
		@CrossOrigin(origins = "http://localhost:8082")
		@RequestMapping(value = "/study/{id}/membership/{uid}", method = RequestMethod.GET)
		public ResponseEntity<List<MembershipResponse>> getMembership(@PathVariable("id") Long studyId,@PathVariable("uid") String subjectUid,@RequestHeader HttpHeaders httpHeaders) {
			HttpHeaders headers = new HttpHeaders();
			 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
				 ValidationType validationType=iPedWebSerRest.validateForSubjectUIDForStudy(studyId,subjectUid);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					 List<RelationshipVo> relationshipVos=iPedWebSerRest.generateSubjectPedigreeRelativeList(subjectUid, studyId);
		         if (relationshipVos==null || relationshipVos.isEmpty()) {
		        	 headers.set("message", ValidationType.PEDIGREE_MEMEBERS_CAN_NOT_FOUND.getName());
				     return new ResponseEntity<List<MembershipResponse>>(headers,getResponseEntityForValidationCode(ValidationType.PEDIGREE_MEMEBERS_CAN_NOT_FOUND));
			      }
		         List<MembershipResponse> membershipResponses=iPedWebSerRest.mapListOfRelationshipVoToListofMembershipresponse(relationshipVos);
		         	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());
		         	return new ResponseEntity<List<MembershipResponse>>(membershipResponses,headers,HttpStatus.OK);
				 }else{
					 headers.set("message", validationType.getName());
					 return new ResponseEntity<List<MembershipResponse>>(headers,getResponseEntityForValidationCode(validationType)); 
				 }	
			 }else{
				 headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
				 return new ResponseEntity<List<MembershipResponse>>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
			}
		 }
		
		//16-Get Pedigree view from ped file.
		@CrossOrigin(origins = "http://localhost:8082")
		@RequestMapping(value = "/study/{id}/visualise/", method = RequestMethod.POST)
		public ResponseEntity<Object> getPedigreeViewFromCSV(@PathVariable("id") Long studyId,@RequestBody List<MadelineObject> medlineObjects,@RequestHeader HttpHeaders httpHeaders) {
			 HttpHeaders headers = new HttpHeaders();
			 ValidationType validationType=null;
			 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){
				 MadelineObject[] madelineArr = new MadelineObject[medlineObjects.size()];
				 madelineArr = medlineObjects.toArray(madelineArr);
				  validationType=iPedWebSerRest.validateCSVStringToDrawPedigree(madelineArr, studyId);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					 String pedigreeFile = iPedWebSerRest.getPedigreeViewFromCsv(madelineArr,studyId);
		         if (pedigreeFile== null || StringUtils.contains(pedigreeFile, "Pedigree Error") || StringUtils.contains(pedigreeFile, "No Pedigree History")) {
		        	 ApiError apiError =   new ApiError( getResponseEntityForValidationCode(ValidationType.PEDIGREE_VIEW_NOT_EXISTS),ValidationType.PEDIGREE_VIEW_NOT_EXISTS.getName(),ValidationType.PEDIGREE_VIEW_NOT_EXISTS.getName());
						return new ResponseEntity<Object>(apiError,apiError.getStatus()); 
		        	// headers.set("message", ValidationType.PEDIGREE_VIEW_NOT_EXISTS.getName());
				    // return new ResponseEntity<Object>(headers, getResponseEntityForValidationCode(ValidationType.PEDIGREE_VIEW_NOT_EXISTS));
			      }
		         	PedigreeResponse pedigreeResponse=new PedigreeResponse();
		         	pedigreeResponse.setSvg(pedigreeFile);
		         	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());
		         	return new ResponseEntity<Object>(pedigreeResponse,headers,HttpStatus.OK);
				 }else{
					 ApiError apiError =   new ApiError(getResponseEntityForValidationCode(validationType),validationType.getName(),validationType.getName());
						return new ResponseEntity<Object>(apiError,apiError.getStatus());
					 //headers.set("message",validationType.getName() );
					 //return new ResponseEntity<Object>(headers,getResponseEntityForValidationCode(validationType)); 
				 }	
			 }else{
				 ApiError apiError =   new ApiError(getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES),ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName(),ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
				 	headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
					return new ResponseEntity<Object>(apiError,apiError.getStatus()); 
				 	//headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName());
					//return new ResponseEntity<Object>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
			}
		 }
		
		//17-Get Subject Siblings.
		@CrossOrigin(origins = "http://localhost:8082")
		@RequestMapping(value = "/study/{id}/siblings/{uid}", method = RequestMethod.GET)
	    public ResponseEntity<SiblingsResponse> getSibling(@PathVariable("id") Long studyId,@PathVariable("uid") String subjectUid,@RequestHeader HttpHeaders httpHeaders) {
			HttpHeaders headers = new HttpHeaders();
			SiblingsResponse siblingResponse=new SiblingsResponse();
			 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
				 List<String> subjectUids;
				 ValidationType validationType=iPedWebSerRest.validateForSubjectUIDForStudy(studyId, subjectUid);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				        List<RelationshipVo> relatinshipVos = iPedWebSerRest.getMySiblings(subjectUid,studyId);
				        if (relatinshipVos== null || relatinshipVos.isEmpty()) {
				        	 headers.set("message", ValidationType.SIBLINGS_DOES_NOT_EXISTS.getName());
				            return new ResponseEntity<SiblingsResponse>(headers,getResponseEntityForValidationCode(ValidationType.SIBLINGS_DOES_NOT_EXISTS));
				        }else{
				        	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());	
				        	subjectUids=	iPedWebSerRest.mapListOfRelativeVoToListOfSubjectUids(relatinshipVos);
				        	siblingResponse.setSubjectUids(subjectUids);
				        }
				        return new ResponseEntity<SiblingsResponse>(siblingResponse,headers,HttpStatus.OK);
				 }else{
					 	headers.set("message", validationType.getName());
						return new ResponseEntity<SiblingsResponse>(headers,getResponseEntityForValidationCode(validationType)); 
				 }   
			 }else{
				 	headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName() );
					return new ResponseEntity<SiblingsResponse>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
			}
	    }
		//18-Get Encoded customfileds for study.
		@CrossOrigin(origins = "http://localhost:8082")
		@RequestMapping(value = "/study/{id}/binarycustomfields", method = RequestMethod.GET)
		public ResponseEntity<BinaryCustomFieldsResponse> getBinaryCustomFields(@PathVariable("id") Long studyId,@RequestHeader HttpHeaders httpHeaders) {
			HttpHeaders headers = new HttpHeaders();
			BinaryCustomFieldsResponse binaryCustomFieldsResponse=new BinaryCustomFieldsResponse();
			 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
				 List<String> customFieldNames;
				 ValidationType validationType=iPedWebSerRest.validateForStudy(studyId);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
				        List<CustomField> customFields = iPedWebSerRest.getBinaryCustomFields(studyId);
				        if (customFields== null || customFields.isEmpty()) {
				        	 headers.set("message", ValidationType.BINARY_CUSTOMFIELD_DOES_NOT_EXISTS.getName());
				            return new ResponseEntity<BinaryCustomFieldsResponse>(headers,getResponseEntityForValidationCode(ValidationType.BINARY_CUSTOMFIELD_DOES_NOT_EXISTS));
				        }else{
				        	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());	
				        	customFieldNames=	iPedWebSerRest.mapListOfCustomFieldsToListOfCustomfieldNames(customFields);
				        	binaryCustomFieldsResponse.setCustomFieldNames(customFieldNames);
				        }
				        return new ResponseEntity<BinaryCustomFieldsResponse>(binaryCustomFieldsResponse,headers,HttpStatus.OK);
				 }else{
					 	headers.set("message", validationType.getName());
						return new ResponseEntity<BinaryCustomFieldsResponse>(headers,getResponseEntityForValidationCode(validationType)); 
				 }   
			 }else{
				 	headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName() );
					return new ResponseEntity<BinaryCustomFieldsResponse>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
			}
		}
		//19-Get people who has siblings for study.
		@CrossOrigin(origins = "http://localhost:8082")
		@RequestMapping(value = "/study/{id}/commonparentsubjects", method = RequestMethod.GET)
		public ResponseEntity<SiblingsResponse> getHaveSiblings(@PathVariable("id") Long studyId,@RequestHeader HttpHeaders httpHeaders) {
			logger.info("Common Parent info");
			HttpHeaders headers = new HttpHeaders();
			SiblingsResponse siblingResponse=new SiblingsResponse();
			 if(isAuthenticateSuccessfulForStudy(httpHeaders,studyId)){ 
				 List<String> subjectUids;
				 ValidationType validationType=iPedWebSerRest.validateForStudy(studyId);
				 if(validationType.equals(ValidationType.SUCCESSFULLY_VALIDATED)){
					 subjectUids = iPedWebSerRest.getSubjectUidsWhoHasSiblings(studyId);
				        if (subjectUids== null || subjectUids.isEmpty()) {
				        	 headers.set("message", ValidationType.NO_SUBJECT_UID_HAS_SIBLINGS.getName());
				            return new ResponseEntity<SiblingsResponse>(headers,getResponseEntityForValidationCode(ValidationType.NO_SUBJECT_UID_HAS_SIBLINGS));
				        }else{
				        	headers.set("message",ValidationType.FOUND_SUCCESSFULLY.getName());	
				        	siblingResponse.setSubjectUids(subjectUids);
				        }
				        return new ResponseEntity<SiblingsResponse>(siblingResponse,headers,HttpStatus.OK);
				 }else{
					 	headers.set("message", validationType.getName());
						return new ResponseEntity<SiblingsResponse>(headers,getResponseEntityForValidationCode(validationType)); 
				 }   
			 }else{
				 	headers.set("message",ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES.getName() );
					return new ResponseEntity<SiblingsResponse>(headers,getResponseEntityForValidationCode(ValidationType.USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES));
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
			
			case USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES:
								httpStatus=HttpStatus.UNAUTHORIZED;
								break;
			
	        case INVALID_STUDY_ID:   
            case NOT_EXISTING_STUDY:  
            case NO_SUBJECT:	
	        case NO_GENDERTYPE:   
	        case INVALID_GENDER_TYPE:   
	        case NO_VITAL_STATUS:   
	        case INVALID_VITAL_STATUS:   
	        case NO_SUBJECT_STATUS:   
	        case INVALID_SUBJECT_STATUS: 
	        case NO_CONSENT_STATUS:   
	        case INVALID_CONSENT_STATUS:   	
            case SUBJECT_UID_NOT_EXISTS:
            case RELATIVE_SUBJECT_UID_NOT_EXISTS:
            case NO_PARENT_TYPE:
            case INVALID_PARENT_TYPE:
            case NO_PARENT_RELATIONSHIP_EXISTS_FOR_STUDY:					
            case NO_TWIN_TYPE:
	        case INVALID_TWIN_TYPE:
	        case PEDIGREE_VIEW_NOT_EXISTS:
	        case PEDIGREE_CONFIG_NOT_EXISTS:
	        case PEDIGREE_MEMEBERS_CAN_NOT_FOUND:
	        case PEDIGREE_CONFIGURATION_CUSTOM_FIELD_NAME_NOT_FOUND:	
	        case FATHER_ID_IS_NOT_PRESENT_IN_THE_LIST:
	        case MOHTER_ID_IS_NOT_PRESENT_IN_THE_LIST:	
	        case SIBLINGS_DOES_NOT_EXISTS:	
	        case BINARY_CUSTOMFIELD_DOES_NOT_EXISTS:	
	        case NO_SUBJECT_UID_HAS_SIBLINGS:	
	        					httpStatus=HttpStatus.NOT_FOUND;	
	        					break;
	        case MISMATCH_SUBJECT_UID_WITH_SUBJECT_ID:					
            case NOT_A_SIBLING:
            case PARENT_RELATIONSHIP_ALREADY_EXISTS:
            case TWIN_RELATIONSHIP_ALREADY_EXISTS:
            case PEDIGREE_CONFIGURATION_SET_VALUE_NOT_ACCEPTED:
            case PEDIGREE_CONFIGURATION_ALREADY_EXISTS_FOR_STUDY:
            case PEDIGREE_CONFIGURATION_CAN_NOT_UPDATE_FOR_STUDY:
            case SUBJECT_UID_ALREADY_EXISTS:   
								httpStatus=HttpStatus.CONFLICT;
								break;
            case CIRCULAR_VALIDATION_UNSUCCESSFUL:
            case SUBJECT_STATUS_NOT_ALLOWED_TO_CREATE_RELATIONSHIP:
            case RELATION_STATUS_NOT_ALLOWED_TO_CREATE_RELATIONSHIP:	
								httpStatus=HttpStatus.FORBIDDEN;	
								break;
            case GENDER_FIELD_UNACCEPTED_VALUES:
            case AFFECTED_UNACCEPTED_VALUES:
            case ZYGOSITY_UNACCEPTED_VALUES:
            case DECEASED_UNACCEPTED_VALUES:
            case PROBAND_UNACCEPTED_VALUES:	
           	            		httpStatus=HttpStatus.NOT_ACCEPTABLE;	
								break;
            case INDIVIDUAL_ID_IS_MANDATORY:
            case FAMILY_ID_IS_MANDATORY:
            					httpStatus=HttpStatus.NO_CONTENT;	
            					break;	
            case SUCCESSFULLY_VALIDATED:
            case CREATED_SUCCESSFULLY:
	        case UPDATED_SUCCESSFULLY:
	        case DELETED_SUCCESSFULLY:
			case FOUND_SUCCESSFULLY:
				            	httpStatus=HttpStatus.OK;	
								break;
	        default:
								httpStatus=HttpStatus.BAD_REQUEST;
								break;	
			}
			return httpStatus;
		}

}
