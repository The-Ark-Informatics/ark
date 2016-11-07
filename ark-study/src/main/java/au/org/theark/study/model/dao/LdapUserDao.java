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
package au.org.theark.study.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.ArkLdapContextSource;
import au.org.theark.core.dao.IArkAuthorisation;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.service.Constants;

@Repository("ldapUserDao")
public class LdapUserDao implements ILdapUserDao {

	static Logger						log	= LoggerFactory.getLogger(LdapUserDao.class);

	private IStudyDao					studyDao;
	private ArkLdapContextSource	ldapDataContextSource;

	private IArkAuthorisation		arkAuthorisationService;

	/* To access Hibernate Study Dao */
	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}

	public ArkLdapContextSource getLdapDataContextSource() {
		return ldapDataContextSource;
	}

	@Autowired
	public void setLdapDataContextSource(ArkLdapContextSource ldapDataContextSource) {
		this.ldapDataContextSource = ldapDataContextSource;
	}

	public IArkAuthorisation getArkAuthorisationService() {
		return arkAuthorisationService;
	}

	@Autowired
	public void setArkAuthorisationService(IArkAuthorisation arkAuthorisationService) {
		this.arkAuthorisationService = arkAuthorisationService;
	}


	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException {

		log.debug("Inside createArkUser");
		try {

			DirContextAdapter dirContextAdapter = new DirContextAdapter();
			// Map the Front end values into LDAP Context
			mapToContext(arkUserVO.getUserName(), arkUserVO.getFirstName(), arkUserVO.getLastName(), arkUserVO.getEmail(), arkUserVO.getPassword(), dirContextAdapter);
			LdapName ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
			ldapName.add(new Rdn(Constants.CN, arkUserVO.getUserName()));
			Name nameObj = ldapName;
			// Create the person in ArkUsers Group in LDAP
			ldapDataContextSource.getLdapTemplate().bind(nameObj, dirContextAdapter, null);

		}
		catch (org.springframework.ldap.NameAlreadyBoundException nabe) {

			log.error("The given username " + arkUserVO.getUserName() + "  exists in LDAP. " + nabe.getMessage());
			throw new UserNameExistsException("A user with the username " + arkUserVO.getUserName() + " already exists in our system. Please provide a unique username.");

		}
		catch (InvalidNameException ine) {

			log.error("A System exception occured " + ine.getMessage());
			// TODO Implement a LDAP ContextSourceTransactionManager for client side Transaction management
			// Note LDAP as such does not participate in Txn management.
			throw new ArkSystemException("A System exception occured.");

		}
		catch (Exception exception) {
			// TODO Implement a LDAP ContextSourceTransactionManager for client side Transaction management
			// Note LDAP as such does not participate in Txn management.
			log.error("A system exception has occured when trying to add roles to the groups. Need to rollback the LDAP transaction. Remove the user from LDAP People collection "
					+ exception.getMessage());
			throw new ArkSystemException("A System exception occured");
		}

	}

	public void updateArkUser(ArkUserVO userVO) throws ArkSystemException {
		try {
			// Assuming that all validation is already done update the attributes in LDAP
			LdapName ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
			ldapName.add(new Rdn("cn", userVO.getUserName()));

			BasicAttribute sn = new BasicAttribute("sn", userVO.getLastName());
			BasicAttribute givenName = new BasicAttribute("givenName", userVO.getFirstName());
			BasicAttribute email = new BasicAttribute("mail", userVO.getEmail());

			ModificationItem itemSn = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, sn);
			ModificationItem itemGivenName = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, givenName);
			ModificationItem itemEmail = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, email);

			if (userVO.getPassword() != null && userVO.getPassword().length() > 0 && userVO.isChangePassword()) {
				BasicAttribute userPassword = new BasicAttribute("userPassword", new Sha256Hash(userVO.getPassword()).toHex());
				ModificationItem itemPassword = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, userPassword);
				ldapDataContextSource.getLdapTemplate().modifyAttributes(ldapName, new ModificationItem[] { itemSn, itemGivenName, itemEmail, itemPassword });
			}
			else {
				ldapDataContextSource.getLdapTemplate().modifyAttributes(ldapName, new ModificationItem[] { itemSn, itemGivenName, itemEmail });
			}

		}
		catch (InvalidNameException ine) {
			throw new ArkSystemException("A System error has occured");
		}

	}

	protected void mapToContext(String username, String firstName, String lastName, String email, String password, DirContextOperations dirCtxOperations) {

		dirCtxOperations.setAttributeValues("objectClass", new String[] { "inetOrgPerson", "organizationalPerson", "person" });
		dirCtxOperations.setAttributeValue("cn", username);
		dirCtxOperations.setAttributeValue("sn", lastName);
		dirCtxOperations.setAttributeValue("givenName", firstName);
		dirCtxOperations.setAttributeValue("mail", email);
		dirCtxOperations.setAttributeValue("userPassword", new Sha256Hash(password).toHex());// Service will always hash it.
	}

	/**
	 * 
	 * getUserAccountInfo - userdetails, applications and roles linked to the user Returns the person details of the current user.
	 */
	public ArkUserVO getUser(String username) throws EntityNotFoundException {

		ArkUserVO etaUserVO = null;
		log.debug("\n getUser ");
		try {
			LdapName ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
			ldapName.add(new Rdn("cn", username));
			etaUserVO = (ArkUserVO) ldapDataContextSource.getLdapTemplate().lookup(ldapName, new PersonContextMapper());
			log.debug("\n etauserVO " + etaUserVO);

		}
		catch (InvalidNameException | NameNotFoundException ne) {
			log.debug("\nGiven username or user does not exist." + username);
			throw new EntityNotFoundException("Given username or user does not exist");

		}
		return etaUserVO;
	}

	/*
	 * TODO private String buildPersonDN(String userName) throws InvalidNameException { LdapName ldapName = new
	 * LdapName(ldapDataContextSource.getBaseLdapPathAsString()); ldapName.add(new Rdn("ou", "people")); ldapName.add(new Rdn("cn", userName)); return
	 * ldapName.toString(); }
	 */

	private static class PersonContextMapper implements ContextMapper {

		public Object mapFromContext(Object ctx) {
			log.debug("\n PersonContext Mapper...");
			DirContextAdapter context = (DirContextAdapter) ctx;

			ArkUserVO arkUserVO = new ArkUserVO();
			arkUserVO.setUserName(context.getStringAttribute("cn"));
			arkUserVO.setFirstName(context.getStringAttribute("givenName"));
			arkUserVO.setLastName(context.getStringAttribute("sn"));
			arkUserVO.setEmail(context.getStringAttribute("mail"));
			
			if(context.getObjectAttribute("userPassword") == null) {
				arkUserVO.setPassword(new String());
			}
			else{
				String ldapPassword = new String((byte[]) context.getObjectAttribute("userPassword"));
				arkUserVO.setPassword(ldapPassword);
			}
			return arkUserVO;
		}
	}

	/**
	 * Maps only the CN of the person and returns the vo
	 * 
	 * @author nivedan
	 * 
	 */
	public static class LitePersonContextMapper implements ContextMapper {
		public Object mapFromContext(Object ctx) {
			DirContextAdapter context = (DirContextAdapter) ctx;
			ArkUserVO arkUserVO = new ArkUserVO();
			arkUserVO.setUserName(context.getStringAttribute("cn"));
			return arkUserVO;
		}
	}

	/*
	 * TODO private static class LiteSiteContextMapper implements ContextMapper {
	 * 
	 * public Object mapFromContext(Object ctx) { log.debug("\n LiteSiteContext Mapper..."); DirContextAdapter context = (DirContextAdapter) ctx;
	 * SiteVO siteVo = new SiteVO(); siteVo.setSiteName(context.getStringAttribute("cn"));
	 * siteVo.setSiteDescription(context.getStringAttribute("description")); // If the schema has other attributes then set/add them here return
	 * siteVo; } }
	 */

	/**
	 * Use when you want to return ALL users from LDAP. Applies for a Super User and Study Admin only. The criteria is supplied in the userVO
	 * 
	 * @param userCriteriaVO
	 * @return
	 * @throws InvalidNameException
	 */
	public List<ArkUserVO> searchAllUsers(ArkUserVO userCriteriaVO) throws ArkSystemException {

		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		List<ArkUserVO> userList = new ArrayList<ArkUserVO>();

		try {
			List<ArkUserRole> adminUserNameList = arkAuthorisationService.getArkSuperAdministratorList();
			if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) && securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE)
					&& securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ)) {

				log.debug("getBaseDn() " + ldapDataContextSource.getBasePeopleDn());// ou=arkUsers or whatever is configured in the context file.
				LdapName ldapName;
				try {

					AndFilter andFilter = new AndFilter();
					andFilter.and(new EqualsFilter("objectClass", "person"));

					ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
					// if userId was specified
					/* User ID */
					if (StringUtils.hasText(userCriteriaVO.getUserName())) {
						ldapName.add(new Rdn(Constants.CN, userCriteriaVO.getUserName()));
						andFilter.and(new WhitespaceWildcardsFilter(Constants.CN, userCriteriaVO.getUserName()));
					}
					/* Given Name */
					if (StringUtils.hasText(userCriteriaVO.getFirstName())) {
						ldapName.add(new Rdn(Constants.GIVEN_NAME, userCriteriaVO.getFirstName()));
						andFilter.and(new WhitespaceWildcardsFilter(Constants.GIVEN_NAME, userCriteriaVO.getFirstName()));
					}

					/* Surname Name */
					if (StringUtils.hasText(userCriteriaVO.getLastName())) {
						ldapName.add(new Rdn(Constants.LAST_NAME, userCriteriaVO.getLastName()));
						andFilter.and(new WhitespaceWildcardsFilter(Constants.LAST_NAME, userCriteriaVO.getLastName()));
					}

					/* Email */
					if (StringUtils.hasText(userCriteriaVO.getEmail())) {
						ldapName.add(new Rdn(Constants.EMAIL, userCriteriaVO.getEmail()));
						andFilter.and(new WhitespaceWildcardsFilter(Constants.EMAIL, userCriteriaVO.getEmail()));
					}

					for (ArkUserRole superAdmin : adminUserNameList) {
						ldapName.add(new Rdn(Constants.CN, superAdmin.getArkUser().getLdapUserName()));
						Filter filter = new NotFilter(new EqualsFilter(Constants.CN, superAdmin.getArkUser().getLdapUserName()));
						andFilter.and(filter);
					}

					/* Status is not defined as yet in the schema */
					userList = ldapDataContextSource.getLdapTemplate().search(ldapDataContextSource.getBasePeopleDn(), andFilter.encode(), new PersonContextMapper());
					log.debug("Size of list " + userList.size());
				}
				catch (InvalidNameException ine) {

					log.error("Exception occured in searchAllUsers " + ine);
					throw new ArkSystemException("A system errror occured");
				}
			}

		}
		catch (EntityNotFoundException e) {

			log.error("Exception occured in searchAllUsers " + e);
			throw new ArkSystemException("A system errror occured. ");
		}

		return userList;
	}

	/*
	 * TODO: Cleanup
	 * 
	 * private static class GroupMembersContextMapper implements ContextMapper {
	 * 
	 * public GroupMembersContextMapper() { super(); }
	 * 
	 * public Object mapFromContext(Object ctx) { DirContextAdapter context = (DirContextAdapter) ctx; String[] members =
	 * context.getStringAttributes("member"); ArrayList<String> memberList = new ArrayList<String>(); for (int i = 0; i < members.length; i++) {
	 * memberList.add(members[i].substring(members[i].indexOf("cn=") + 3, members[i].indexOf(","))); } return memberList; } }
	 */

	/**
	 * Retrieves a sub-set of users from LDAP. The memberCnList List<String> contains the list of userNames or CN, and ArkUserVO acts as a criteria
	 * that will be applied when looking up the user. Not all users in the memberCnList will be returned, it also depends if the criteria matches with
	 * the sub-set of users.
	 * 
	 * @param memberCnList
	 * @param userCriteriaVO
	 * @return
	 */
	public List<ArkUserVO> getPersonsByCn(List<String> memberCnList, ArkUserVO userCriteriaVO) {

		if (memberCnList == null || memberCnList.size() < 0) {
			return new ArrayList<ArkUserVO>();
		}

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));

		if (StringUtils.hasText(userCriteriaVO.getUserName())) {
			filter.and(new EqualsFilter(Constants.CN, userCriteriaVO.getUserName()));
		}

		/* Given Name */
		if (StringUtils.hasText(userCriteriaVO.getFirstName())) {
			filter.and(new EqualsFilter(Constants.GIVEN_NAME, userCriteriaVO.getFirstName()));
		}

		/* Surname Name */
		if (StringUtils.hasText(userCriteriaVO.getLastName())) {

			filter.and(new EqualsFilter(Constants.LAST_NAME, userCriteriaVO.getLastName()));
		}

		/* Email */
		if (StringUtils.hasText(userCriteriaVO.getEmail())) {

			filter.and(new EqualsFilter(Constants.EMAIL, userCriteriaVO.getEmail()));
		}

		OrFilter orFilter = new OrFilter();
		filter.and(orFilter);
		// Build the filter that matches the cn's and then apply the criteria
		for (Iterator<String> iterator = memberCnList.iterator(); iterator.hasNext();) {
			String userDN = iterator.next();
			orFilter.or(new EqualsFilter("cn", userDN));

			if (StringUtils.hasText(userCriteriaVO.getUserName())) {
				filter.and(new EqualsFilter(Constants.CN, userCriteriaVO.getUserName()));
			}
			/* Given Name */
			if (StringUtils.hasText(userCriteriaVO.getFirstName())) {
				filter.and(new EqualsFilter(Constants.GIVEN_NAME, userCriteriaVO.getFirstName()));
			}

			/* Surname Name */
			if (StringUtils.hasText(userCriteriaVO.getLastName())) {

				filter.and(new EqualsFilter(Constants.LAST_NAME, userCriteriaVO.getLastName()));
			}

			/* Email */
			if (StringUtils.hasText(userCriteriaVO.getEmail())) {

				filter.and(new EqualsFilter(Constants.EMAIL, userCriteriaVO.getEmail()));
			}
		}
		// TODO NN User a light version of PersonContextMapper, dont need to map password details.
		return ldapDataContextSource.getLdapTemplate().search(ldapDataContextSource.getBasePeopleDn(), filter.encode(), new PersonContextMapper());
	}

	public List<ArkUserVO> searchUser(ArkUserVO userVO) throws ArkSystemException {

		// This is only when you want all the users.
		List<ArkUserVO> userList = new ArrayList<ArkUserVO>();
		/**
		 * Determine the role of the user and return results based on role
		 */
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		// Allow only a role that has Create,
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) && securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE)
				&& securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ)) {

			userList = searchAllUsers(userVO);

		}
		return userList;
	}

	/*
	 * TODO remove? // Returns the Member attribute value of a group private static class GroupContextMapper implements ContextMapper { public Object
	 * mapFromContext(Object ctx) { DirContextAdapter context = (DirContextAdapter) ctx; String[] members = context.getStringAttributes("member");
	 * return members; } }
	 */

	/**
	 * Checks if the given user exists in the LDAP System. If present it will return a boolean true.
	 * 
	 * @param username
	 * @return boolean
	 * @throws ArkSystemException
	 */
	public boolean isArkUserPresent(String username) {
		boolean isPresent = false;

		ArkUserVO arkUserVO = null;

		log.debug("\n getUser ");
		try {
			LdapName ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
			ldapName.add(new Rdn("cn", username));
			Name nameObj = (Name) ldapName;

			arkUserVO = (ArkUserVO) ldapDataContextSource.getLdapTemplate().lookup(nameObj, new LitePersonContextMapper());
			log.debug("\n etauserVO " + arkUserVO);
			if (arkUserVO.getUserName().equalsIgnoreCase(username)) {
				isPresent = true;
			}

		}
		catch (InvalidNameException ne) {
			log.debug("\n Given username or user does not exist." + username);
			// Do not throw any exception back it is not necessary here since we want the boolean isPresent to return false
		}
		return isPresent;
	}

	/**
	 * Looks up a particular user from LDAP using the username/login name for Ark System
	 * 
	 * @param arkUserName
	 * @return ArkUserVO
	 * @throws ArkSystemException
	 */
	public ArkUserVO lookupArkUser(String arkUserName) throws ArkSystemException {

		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		List<ArkUserVO> userList = new ArrayList<ArkUserVO>();

		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) && securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE)
				&& securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ)) {

			log.debug("getBaseDn() " + ldapDataContextSource.getBasePeopleDn());// ou=arkUsers or whatever is configured in the context file.
			LdapName ldapName;
			try {

				AndFilter andFilter = new AndFilter();
				andFilter.and(new EqualsFilter("objectClass", "person"));

				ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
				// if userId was specified
				/* User ID */
				if (StringUtils.hasText(arkUserName)) {
					ldapName.add(new Rdn(Constants.CN, arkUserName));
					andFilter.and(new EqualsFilter(Constants.CN, arkUserName));
				}

				userList = ldapDataContextSource.getLdapTemplate().search(ldapDataContextSource.getBasePeopleDn(), andFilter.encode(), new PersonContextMapper());
				log.debug("Size of list " + userList.size());
			}
			catch (InvalidNameException ine) {

				log.error("Exception occured in lookupArkUser(String arkUserName) " + ine);
				throw new ArkSystemException("A system errror occured");
			}
		}
		ArkUserVO arkUserVO = new ArkUserVO();
		if (userList != null && userList.size() > 0) {
			arkUserVO = userList.get(0);
		}
		return arkUserVO;
	}

	public void update(ArkUserVO userVO) throws ArkSystemException {
		log.debug("update() invoked: Updating user details in LDAP");
		try {
			// Assuming that all validation is already done update the attributes in LDAP
			LdapName ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
			ldapName.add(new Rdn("cn", userVO.getUserName()));

			BasicAttribute sn = new BasicAttribute("sn", userVO.getLastName());
			BasicAttribute givenName = new BasicAttribute("givenName", userVO.getFirstName());
			BasicAttribute email = new BasicAttribute("mail", userVO.getEmail());

			ModificationItem itemSn = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, sn);
			ModificationItem itemGivenName = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, givenName);
			ModificationItem itemEmail = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, email);

			// Check if password needs to be modified. Make sure client validation is done.
			// TODO Also enforce the validation check here.
			if (userVO.isChangePassword()) {
				BasicAttribute userPassword = new BasicAttribute("userPassword", new Sha256Hash(userVO.getPassword()).toHex());
				ModificationItem itemPassword = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, userPassword);
				ldapDataContextSource.getLdapTemplate().modifyAttributes(ldapName, new ModificationItem[] { itemSn, itemGivenName, itemEmail, itemPassword });
			}
			else {
				ldapDataContextSource.getLdapTemplate().modifyAttributes(ldapName, new ModificationItem[] { itemSn, itemGivenName, itemEmail });
			}
		}
		catch (InvalidNameException ine) {
			throw new ArkSystemException("A System error has occured");
		}
	}
	@Override
	public void deleteArkUser(ArkUserVO arkUserVO) throws  ArkSystemException {
		
		log.debug("Inside DeleteArkUser");
					LdapName ldapName;
			try {
				ldapName = new LdapName(ldapDataContextSource.getBasePeopleDn());
				ldapName.add(new Rdn(Constants.CN, arkUserVO.getUserName()));
				Name nameObj = ldapName;
				// Create the person in ArkUsers Group in LDAP
				ldapDataContextSource.getLdapTemplate().unbind(nameObj);
			} catch (InvalidNameException ine) {
				log.error("A System exception occured " + ine.getMessage());
				// TODO Implement a LDAP ContextSourceTransactionManager for client side Transaction management
				// Note LDAP as such does not participate in Txn management.
				throw new ArkSystemException("A System exception occured.");
			}
	}	

}
