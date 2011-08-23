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
public class UserServiceImpl implements IUserService {

	static final Logger			log	= LoggerFactory.getLogger(UserServiceImpl.class);

	private IStudyService		studyService;															// To gain access to Study Schema
	private IArkCommonService	arkCommonService;

	private IArkAuthorisation	arkAuthorisationService;

	/* DAO to access database */
	private IUserDao				userDAO;

	/* A DAO to access the LDAP */
	private ILdapUserDao			iLdapUserDao;

	public ILdapUserDao getiLdapPersonDao() {
		return iLdapUserDao;
	}

	@Autowired
	public void setiLdapPersonDao(ILdapUserDao iLdapPersonDao) {
		this.iLdapUserDao = iLdapPersonDao;
	}

	@Autowired
	public void setUserDAO(IUserDao userDAO) {
		this.userDAO = userDAO;
	}

	public IUserDao getUserDAO() {
		return userDAO;
	}

	public IArkCommonService getArkCommonService() {
		return arkCommonService;
	}

	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}

	@Autowired
	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
	}

	public IArkAuthorisation getArkAuthorisationService() {
		return arkAuthorisationService;
	}

	@Autowired
	public void setArkAuthorisationService(IArkAuthorisation arkAuthorisationService) {
		this.arkAuthorisationService = arkAuthorisationService;
	}

	public Person createPerson(Person personEntity) {

		return userDAO.createPerson(personEntity);
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
		arkCommonService.createAuditHistory(ah);
	}

	public ArkUserVO getCurrentUser(String username) throws ArkSystemException {
		return iLdapUserDao.getUser(username);
	}

	public boolean isArkUserPresent(String userName) {
		return userDAO.isArkUserPresent(userName);
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
			arkUserVO.setStudy(arkCommonService.getStudy(sessionStudyId));
			arkUserVO.getArkUserEntity().setLdapUserName(arkUserVO.getUserName());
			// Create the user in Ark Database as well and persist and update and roles user was assigned
			arkAuthorisationService.createArkUser(arkUserVO);

			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created User (in LDAP) " + arkUserVO.getUserName());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
			arkCommonService.createAuditHistory(ah);
		}
		catch (UserNameExistsException personExistsException) {
			throw personExistsException;
		}

	}

	public void updateArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException {

		iLdapUserDao.updateArkUser(arkUserVO);// Update the LDAP entry
		arkAuthorisationService.updateArkUser(arkUserVO);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Ark User (in LDAP) " + arkUserVO.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		arkCommonService.createAuditHistory(ah);
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
			ArkUser arkUserEntity = arkAuthorisationService.getArkUser(arkLdapUserName);
			arkUserVO.setArkUserEntity(arkUserEntity);
			arkUserRoleList = arkAuthorisationService.getArkUserLinkedModuleAndRoles(arkUserVO);
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
		arkAuthorisationService.deleteArkUser(arkUserVO);

	}

}
