package au.org.theark.web.rest.service;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;

@Service("loginService")
@Transactional
public class LoginWebServiceRest implements ILoginWebServiceRest {
	
	public static final Logger logger = LoggerFactory.getLogger(LoginWebServiceRest.class);
	
	@Autowired
	private IArkCommonService iArkCommonService;
	

	@Override
	public boolean authenticate(ArkUserVO user) {
		Subject subject = SecurityUtils.getSubject();
		// Disable Remember me
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword(),	false);
		// This will propagate to the Realm
		try {
			subject.login(usernamePasswordToken);

			// Place a default module into session
			ArkModule arkModule = iArkCommonService.getArkModuleByName(au.org.theark.core.Constants.ARK_MODULE_STUDY);
			// Place a default function into session
			ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_STUDY);

			// Set session attributes
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_USERID, user.getUserName());
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY, arkModule.getId());
			SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY, arkFunction.getId());
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

	@Override
	public boolean hasPermissionForStudy(Long studyId) {
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		ArkUser arkUser = null;
		try {
			arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		ArkUserVO arkUserVo = new ArkUserVO();
		arkUserVo.setArkUserEntity(arkUser);
		arkUserVo.setStudy(iArkCommonService.getStudy(studyId));
		List<Study> studyListForUser= iArkCommonService.getStudyListForUser(arkUserVo);
		return (studyListForUser.size() > 0) && hasPermissionToCreateSubjectsForStudy(securityManager, currentUser);
	}
	/**
	 * 
	 * @param securityManager
	 * @param currentUser
	 * @return
	 */
	private boolean hasPermissionToCreateSubjectsForStudy(SecurityManager securityManager,Subject currentUser){
		if(securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)||
				securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_STUDY_ADMINISTATOR)||
				securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUBJECT_ADMINISTATOR)){
				return true; 
		}else{
			return false;
		}
	}

}
