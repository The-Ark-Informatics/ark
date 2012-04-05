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
package au.org.theark.study.service;

/**
 * Add Copyright header
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IArkAuthorisation;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.model.dao.ILdapUserDao;
import au.org.theark.study.model.dao.IUserDao;

@Transactional
@Service("userService")
@SuppressWarnings("unchecked")
public class UserServiceImpl implements IUserService {

	static final Logger			log	= LoggerFactory.getLogger(UserServiceImpl.class);

	private IArkCommonService	iArkCommonService;
	private IArkAuthorisation	iArkAuthorisationService;

	/* DAO to access database */
	private IUserDao				iUserDao;

	/* A DAO to access the LDAP */
	private ILdapUserDao			iLdapUserDao;

	public IArkCommonService getArkCommonService() {
		return iArkCommonService;
	}

	@Autowired
	public void setArkCommonService(IArkCommonService iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	public IArkAuthorisation getArkAuthorisationService() {
		return iArkAuthorisationService;
	}

	@Autowired
	public void setArkAuthorisationService(IArkAuthorisation iArkAuthorisationService) {
		this.iArkAuthorisationService = iArkAuthorisationService;
	}

	@Autowired
	public void setUserDAO(IUserDao iUserDao) {
		this.iUserDao = iUserDao;
	}

	public IUserDao getUserDAO() {
		return iUserDao;
	}

	public ILdapUserDao getiLdapUserDao() {
		return iLdapUserDao;
	}

	@Autowired
	public void setiLdapUserDao(ILdapUserDao iLdapUserDao) {
		this.iLdapUserDao = iLdapUserDao;
	}

	public Person createPerson(Person personEntity) {

		return iUserDao.createPerson(personEntity);
	}

	public List<ArkUserVO> searchUser(ArkUserVO user) throws ArkSystemException {
		return iLdapUserDao.searchUser(user);
	}

	public void updateLdapUser(ArkUserVO userVO) throws ArkSystemException {
		iLdapUserDao.update(userVO);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated User (in LDAP) " + userVO.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		iArkCommonService.createAuditHistory(ah);
	}

	public ArkUserVO getCurrentUser(String username) throws EntityNotFoundException {
		return iLdapUserDao.getUser(username);
	}

	public boolean isArkUserPresent(String userName) {
		return iUserDao.isArkUserPresent(userName);
	}

	/**
	 * This is a new interface that persists the user into ArkUsers group in LDAP
	 * 
	 * @param arkUserVO
	 * @throws UserNameExistsException
	 * @throws ArkSystemException
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException {

		// Create user in LDAP
		try {
			iLdapUserDao.createArkUser(arkUserVO);
			// Setting the Username and Study on the ArkUser Hibernate entity from the Value Object that would reflect what it is on LDAP

			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			arkUserVO.setStudy(iArkCommonService.getStudy(sessionStudyId));
			arkUserVO.getArkUserEntity().setLdapUserName(arkUserVO.getUserName());
			// Create the user in Ark Database as well and persist and update and roles user was assigned
			iArkAuthorisationService.createArkUser(arkUserVO);

			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created User (in LDAP) " + arkUserVO.getUserName());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
			iArkCommonService.createAuditHistory(ah);
		}
		catch (UserNameExistsException personExistsException) {
			throw personExistsException;
		}

	}

	public void updateArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException {

		iLdapUserDao.updateArkUser(arkUserVO);// Update the LDAP entry
		iArkAuthorisationService.updateArkUser(arkUserVO);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Ark User (in LDAP) " + arkUserVO.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		iArkCommonService.createAuditHistory(ah);
	}

	/**
	 * 
	 * @param arkUserVO
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public ArkUserVO lookupArkUser(String arkLdapUserName, Study study) throws ArkSystemException {
		// Fetch the Ark User details from LDAP
		ArkUserVO arkUserVO = iLdapUserDao.lookupArkUser(arkLdapUserName);
		arkUserVO.setStudy(study);

		// Fetch ArkUserRole and ArkUser Details from Backend using the arkLdapUserName
		List<ArkUserRole> arkUserRoleList = new ArrayList<ArkUserRole>();
		try {
			ArkUser arkUserEntity = iArkAuthorisationService.getArkUser(arkLdapUserName);
			arkUserVO.setArkUserEntity(arkUserEntity);
			arkUserRoleList = iArkAuthorisationService.getArkUserLinkedModuleAndRoles(arkUserVO);
			arkUserVO.setArkUserPresentInDatabase(true);
			arkUserVO.setArkUserRoleList(arkUserRoleList);
		}
		catch (EntityNotFoundException e) {
			log.debug("The specified User is not in the Ark Database");
		}

		return arkUserVO;
	}

	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException {
		// Note: Only Remove the Ark User from database not in LDAP
		iArkAuthorisationService.deleteArkUser(arkUserVO);

	}

	/**
	 * 
	 * @param arkUserVO
	 * @throws ArkSystemException
	 */
	public ArkUserVO lookupArkUser(String arkLdapUserName) throws ArkSystemException {
		// Fetch the Ark User details from LDAP
		ArkUserVO arkUserVo = iLdapUserDao.lookupArkUser(arkLdapUserName);

		try {
			ArkUser arkUserEntity = iArkAuthorisationService.getArkUser(arkLdapUserName);
			arkUserVo.setArkUserEntity(arkUserEntity);
		}
		catch (EntityNotFoundException e) {
			log.debug("The specified User is not in the Ark Database");
		}

		return arkUserVo;
	}

	public ArkUser getArkUser(String arkLdapUserName) throws EntityNotFoundException {
		ArkUser arkUserEntity = iArkAuthorisationService.getArkUser(arkLdapUserName);
		return arkUserEntity;
	}

	public void resetArkUserPassword(ArkUserVO arkUserVO) throws ArkSystemException {
		try {
			// Deny rest of Super administrator password!
			if (!iArkAuthorisationService.isSuperAdministrator(arkUserVO.getUserName())) {
				iLdapUserDao.updateArkUser(arkUserVO);// Update the LDAP entry
				AuditHistory ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
				ah.setComment("Reset ArkUser password (in LDAP) " + arkUserVO.getUserName());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
				iArkCommonService.createAuditHistory(ah);
			}
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	public void deleteArkUserRole(ArkUserRole arkUserRole) {
		iArkAuthorisationService.deleteArkUserRole(arkUserRole);
	}

	public void deleteArkUserRolesForStudy(Study study, ArkUser arkUser) {
		iArkAuthorisationService.deleteArkUserRolesForStudy(study, arkUser);
	}

	public void createArkUserForChildStudy(ArkUserVO arkUserVo) {
		iArkAuthorisationService.createArkUser(arkUserVo);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created User (in LDAP) " + arkUserVo.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		iArkCommonService.createAuditHistory(ah);
	}

	public void createArkUserRole(ArkUserRole arkUserRole) {
		iArkAuthorisationService.createArkUserRole(arkUserRole);

		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created ArkUserRole for " + arkUserRole.getArkUser().getLdapUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		iArkCommonService.createAuditHistory(ah);
	}
}
