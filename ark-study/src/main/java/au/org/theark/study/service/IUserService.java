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

import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkUserVO;

public interface IUserService {

	/**
	 * Interface to update a person's details including their password. It does not at present modify or update the Groups and roles. The modification
	 * will only apply to Person attributes.
	 * 
	 * @param ArkUserVO
	 *           etaUserVO
	 * @throws InvalidNameException
	 */
	public void updateLdapUser(ArkUserVO etaUserVO) throws ArkSystemException;

	/**
	 * User this method to lookup a user in LDAP. The user object acts as a filter condition that is applied for the search.
	 * 
	 * @param ArkUserVO
	 * @return List<ArkUserVO>
	 * @throws InvalidNameException
	 */
	public List<ArkUserVO> searchUser(ArkUserVO user) throws ArkSystemException;

	/**
	 * 
	 * @param username
	 * @return
	 * @throws ArkSystemException
	 */
	public ArkUserVO getCurrentUser(String username) throws ArkSystemException;

	/**
	 * 
	 * @param arkUserVO
	 * @throws UserNameExistsException
	 * @throws ArkSystemException
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException;

	/**
	 * 
	 * @param arkUserVO
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void updateArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException;

	/**
	 * Lookup a user from the Ark Database system, with a study in cointext
	 * @param arkLdapUserName
	 * @param study
	 * @return
	 * @throws ArkSystemException
	 */
	public ArkUserVO lookupArkUser(String arkLdapUserName, Study study) throws ArkSystemException;

	/**
	 * Remove the user from the Ark Database system.
	 * 
	 * @param arkUserVO
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException;
	
	/**
	 * Lookup a user from the Ark Database system.
	 * @param arkLdapUserName
	 * @return
	 * @throws ArkSystemException
	 */
	public ArkUserVO lookupArkUser(String arkLdapUserName) throws ArkSystemException;

}
