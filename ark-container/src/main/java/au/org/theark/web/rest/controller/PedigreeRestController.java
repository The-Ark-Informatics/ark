package au.org.theark.web.rest.controller;

import java.io.File;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.RelationWrapperVo;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.vo.SubjectWrapperVO;
import au.org.theark.core.vo.TwinWrapperVO;
import au.org.theark.web.rest.service.IPedigreeWebServiceRest;

@RestController
public class PedigreeRestController {
	
	public static final Logger logger = LoggerFactory.getLogger(PedigreeRestController.class);

	@Autowired
	IPedigreeWebServiceRest iPedWebSerRest;

	//1-Create subject.
	@RequestMapping(value = "/subject/", method = RequestMethod.POST)
	public ResponseEntity<Void> createSubject(@RequestBody SubjectWrapperVO subjectWrapperVO, UriComponentsBuilder ucBuilder) {
		 SubjectVO subjectvo=subjectWrapperVO.getSubjectVO();
			if(authenticate(subjectWrapperVO.getArkUserVO())){
				System.out.println("Creating the subject: " + subjectvo.getSubjectFullName());
				if (!iPedWebSerRest.isStudyExist(subjectvo.getLinkSubjectStudy().getStudy())) {
					System.out.println("No study specified for subject.");
					return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
				} else if (!iPedWebSerRest.isSubjectUIDExist(subjectvo.getLinkSubjectStudy().getStudy(),
						subjectvo.getLinkSubjectStudy().getSubjectUID())) {
					System.out.println("A Subjectr with name " + subjectvo.getSubjectFullName() + " already exist");
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				} else {
					iPedWebSerRest.createSubject(subjectvo);
					HttpHeaders headers = new HttpHeaders();
					headers.setLocation(ucBuilder.path("/subject/{id}").buildAndExpand(subjectvo.getLinkSubjectStudy().getId()).toUri());
					return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
				}
			}else{
					return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}
	}
	
	//2-Get Subject
	/*@RequestMapping(value = "/getsubject/{subjectUid}/{studyid}", method = RequestMethod.GET)
    public ResponseEntity<Long> getLinkSubjectStudy(@PathVariable String subjectUid,@PathVariable Long studyid) {
	//public ResponseEntity<LinkSubjectStudy> getLinkSubjectStudy(@RequestParam(value="subjectUid") String subjectUid, @RequestParam(value="studyId") Long studyid) {
        logger.info("Fetching LinkSubjectStudy with SubjectUid {}", subjectUid);
        LinkSubjectStudy linkSubjectStudy = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(subjectUid, iPedWebSerRest.getStudyByID(studyid));
        if (linkSubjectStudy== null) {
            logger.error("LinkSubjectStudy with SubjectUid {} not found.", subjectUid);
            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Long>(linkSubjectStudy.getId(), HttpStatus.OK);
    }*/
	
	//2-Get Subject
	@RequestMapping(value = "/getsubject/", method = RequestMethod.POST)
    public ResponseEntity<Long> getLinkSubjectStudy(@RequestBody SubjectWrapperVO subjectWrapperVO) {
		 String subjectUid=subjectWrapperVO.getSubjectVO().getLinkSubjectStudy().getSubjectUID();
		 if(authenticate(subjectWrapperVO.getArkUserVO())){ 
	        logger.info("Fetching LinkSubjectStudy with SubjectUid {}", subjectUid);
	        LinkSubjectStudy linkSubjectStudy = iPedWebSerRest.getLinkSubjectStudyBySubjectUidAndStudy(subjectUid, iPedWebSerRest.getStudyByID(subjectWrapperVO.getSubjectVO().getLinkSubjectStudy().getStudy().getId()));
	        if (linkSubjectStudy== null) {
	            logger.error("LinkSubjectStudy with SubjectUid {} not found.", subjectUid);
	            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<Long>(linkSubjectStudy.getId(), HttpStatus.OK);
		 }else{
				return new ResponseEntity<Long>(HttpStatus.UNAUTHORIZED);
		}
    }
	
	//3-Set Mother or Father
	@RequestMapping(value = "/createRelationShip/", method = RequestMethod.POST)
	public ResponseEntity<Void> createRelationShip(@RequestBody RelationWrapperVo relationWrapperVo,UriComponentsBuilder ucBuilder) {
		Boolean result;
		HttpHeaders headers = new HttpHeaders();
		if(authenticate(relationWrapperVo.getArkUserVO())){
			result=iPedWebSerRest.isCircularValidationSuccessful(relationWrapperVo.getSubjectVO(),relationWrapperVo.getRelativeVO());
			if(result){
				iPedWebSerRest.createRelationShip(relationWrapperVo.getSubjectVO(),relationWrapperVo.getRelativeVO(),relationWrapperVo.getRelationship());
				headers.setLocation(ucBuilder.path("/createRelationShip/{id}").buildAndExpand(relationWrapperVo.getSubjectVO().getLinkSubjectStudy().getId()).toUri());
				return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
			}else{
				return new ResponseEntity<Void>(headers, HttpStatus.CONFLICT);
			}
		}else{
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	//4-Create twins
	@RequestMapping(value = "/createTwin/", method = RequestMethod.POST)
	public ResponseEntity<Void> createTwin(@RequestBody TwinWrapperVO twinWrapperVO,UriComponentsBuilder ucBuilder) {
		Boolean result;
		HttpHeaders headers = new HttpHeaders();
		if(authenticate(twinWrapperVO.getArkUserVO())){
			result=iPedWebSerRest.createTwin(twinWrapperVO.getSubjectVO(),twinWrapperVO.getRelativeVO(),twinWrapperVO.getTwinType());
			if(result){
				headers.setLocation(ucBuilder.path("/createTwin/{id}").buildAndExpand(twinWrapperVO.getSubjectVO().getLinkSubjectStudy().getId()).toUri());
				return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
			}else{
				return new ResponseEntity<Void>(headers, HttpStatus.BAD_REQUEST);
			}
		}else{
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}
	
/*	//5-Generate Pedigree-No need this time.
	@RequestMapping(value = "/generatePedigree/", method = RequestMethod.POST)
	public ResponseEntity<Void> generateRelationships(@RequestBody SubjectWrapperVO subjectWrapperVO,UriComponentsBuilder ucBuilder) {
		Boolean result;
		HttpHeaders headers = new HttpHeaders();
		SubjectVO subjectvo=subjectWrapperVO.getSubjectVO();
		if(authenticate(subjectWrapperVO.getArkUserVO())){ 
			result=iPedWebSerRest.generatePedigree(subjectvo);
			if(result){
				headers.setLocation(ucBuilder.path("/generatePedigree/{id}").buildAndExpand(subjectvo.getLinkSubjectStudy().getId()).toUri());
				return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
			}else{
				return new ResponseEntity<Void>(headers, HttpStatus.EXPECTATION_FAILED);
			}
		}else{
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}*/
	
	//7-Get Pedigree view as a file
		/*@RequestMapping(value = "/getPedigreeView/{subjectUid}/{studyid}", method = RequestMethod.GET)
	    public ResponseEntity<File> getPedigreeView(@PathVariable String subjectUid,@PathVariable Long studyid) {
			File pedigreeFile=iPedWebSerRest.getPedigreeView(subjectUid, studyid);
	        if (pedigreeFile== null) {
	            return new ResponseEntity<File>(HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<File>(pedigreeFile, HttpStatus.OK);
	    }*/
		
	//7-Get Pedigree view as a file
		@RequestMapping(value = "/getPedigreeView/", method = RequestMethod.POST)
	    public ResponseEntity<File> getPedigreeView(@RequestBody SubjectWrapperVO subjectWrapperVO) {
			 String subjectUid=subjectWrapperVO.getSubjectVO().getLinkSubjectStudy().getSubjectUID();
			 if(authenticate(subjectWrapperVO.getArkUserVO())){ 
				 File pedigreeFile=iPedWebSerRest.getPedigreeView(subjectUid,subjectWrapperVO.getSubjectVO().getLinkSubjectStudy().getStudy().getId());
		         if (pedigreeFile== null) {
			           return new ResponseEntity<File>(HttpStatus.NOT_FOUND);
			      }
		         	return new ResponseEntity<File>(pedigreeFile, HttpStatus.OK);
			 }else{
					return new ResponseEntity<File>(HttpStatus.UNAUTHORIZED);
			}
	    }

	/**
	 * 
	 * @param user
	 * @return
	 */
	private final boolean authenticate(ArkUserVO user) {
		Subject subject = SecurityUtils.getSubject();
		// Disable Remember me
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword(),	false);
		// This will propagate to the Realm
		try {
			subject.login(usernamePasswordToken);
			return true;
		} catch (IncorrectCredentialsException e) {
			logger.error("Password is incorrect.");

		} catch (UnknownAccountException e) {
			logger.error("User account not found.");
			
		} catch (AuthenticationException e) {
			logger.error("Invalid username and/or password.");
			
		} catch (Exception e) {
			logger.error("Login Failed.");
		}
		return false;
	}

}
