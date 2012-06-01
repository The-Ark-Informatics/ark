/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings("unchecked")
@Component
public class ArkLdapRealm extends AuthorizingRealm {
	static final Logger				log					= LoggerFactory.getLogger(ArkLdapRealm.class);
	private static final String	UNKNOWN_ACCOUNT	= "The user is not registered with the Ark Application. Please see your administrator";
	/* Interface to Core */
	protected IArkCommonService	iArkCommonService;

	@Autowired
	public void setiArkCommonService(IArkCommonService iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	public ArkLdapRealm() {
		setName("arkLdapRealm"); // This name must match the name in the User class's getPrincipals() method
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
		setCredentialsMatcher(hashedCredentialsMatcher);
	}
	
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo simpleAuthInfo = new SimpleAuthorizationInfo();

		// Get the logged in user name from Shiro Session
		String ldapUserName = (String) principals.getPrimaryPrincipal();

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionFunctionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		try {

			if (sessionModuleId != null && sessionFunctionId != null && sessionStudyId == null) {
				// Load the role for the given module and use case
				ArkFunction arkFunction = iArkCommonService.getArkFunctionById(sessionFunctionId);
				ArkModule arkModule = iArkCommonService.getArkModuleById(sessionModuleId);

				String role = iArkCommonService.getUserRole(ldapUserName, arkFunction, arkModule, null);
				simpleAuthInfo.addRole(role);

				/* Check if the logged in user is a Super Administrator */
				if (iArkCommonService.isSuperAdministator(ldapUserName, arkFunction, arkModule)) {

					java.util.Collection<String> userRolePermission = iArkCommonService.getArkRolePermission(role);
					simpleAuthInfo.addStringPermissions(userRolePermission);
				}
				else {
					if (role != null) {
						java.util.Collection<String> userRolePermission = iArkCommonService.getArkRolePermission(arkFunction, role, arkModule);
						simpleAuthInfo.addStringPermissions(userRolePermission);
					}
				}
			}
			else if (sessionModuleId != null && sessionFunctionId != null && sessionStudyId != null) {
				// Get the roles for the study in context
				Study study = iArkCommonService.getStudy(sessionStudyId);
				ArkFunction arkFunction = iArkCommonService.getArkFunctionById(sessionFunctionId);
				ArkModule arkModule = iArkCommonService.getArkModuleById(sessionModuleId);
				String role = iArkCommonService.getUserRole(ldapUserName, arkFunction, arkModule, study);
				simpleAuthInfo.addRole(role);

				if (iArkCommonService.isSuperAdministator(ldapUserName, arkFunction, arkModule)) {
					java.util.Collection<String> userRolePermission = iArkCommonService.getArkRolePermission(role);
					simpleAuthInfo.addStringPermissions(userRolePermission);
				}
				else {
					if (role != null) {
						java.util.Collection<String> userRolePermission = iArkCommonService.getArkRolePermission(arkFunction, role, arkModule);
						simpleAuthInfo.addStringPermissions(userRolePermission);
					}
				}
			}

		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}

		return simpleAuthInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		SimpleAuthenticationInfo sai = null;
		ArkUserVO userVO = null;
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
		try {
			userVO = iArkCommonService.getUser(token.getUsername().trim());// Example to use core services to get user
			if (userVO != null) {
				// Check if the user is in the Ark Database
				ArkUser arkUser = iArkCommonService.getArkUser(token.getUsername().trim());
				// Also check if the Ark User is linked with any study and has roles.
				// If no roles found, stop the user from logging in until an administrator has set it up
				if (!iArkCommonService.isArkUserLinkedToStudies(arkUser)) {
					throw new UnknownAccountException(UNKNOWN_ACCOUNT);
				}

				sai = new SimpleAuthenticationInfo(userVO.getUserName(), userVO.getPassword(), getName());
			}
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		catch (EntityNotFoundException e) {
			throw new UnknownAccountException(UNKNOWN_ACCOUNT);
		}
		return sai;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}
}
