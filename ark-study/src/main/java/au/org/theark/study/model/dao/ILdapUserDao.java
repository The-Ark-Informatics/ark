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

import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.ArkUserVO;

/**
 * An interface that communicates with LDAP resource.
 * 
 * @author nivedann
 * 
 */
public interface ILdapUserDao {

	/**
	 * Interface to update a person's details including their password. IT does not at present modify or update the Groups and roles. The modification
	 * will only apply to Person attributes. bind the user to a specific group in ldap.
	 * 
	 * @param ArkUserVO
	 *           userVO
	 * @throws InvalidNameException
	 */
	public void update(ArkUserVO userVO) throws ArkSystemException;

	/**
	 * Fetches the current user's details from the LDAP.
	 * 
	 * @param username
	 * @throws EntityNotFoundException
	 * @return
	 */
	public ArkUserVO getUser(String username) throws EntityNotFoundException;

	/**
	 * Interface to return a zero or more users as a List, that match the search criteria.
	 * 
	 * @param userVO
	 * @return
	 */
	public List<ArkUserVO> searchUser(ArkUserVO userVO) throws ArkSystemException;

	/**
	 * This is a new interface that persists the user into ArkUsers group in LDAP
	 * 
	 * @param arkUserVO
	 * @throws UserNameExistsException
	 * @throws ArkSystemException
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException;

	/**
	 * 
	 * @param userVO
	 * @throws ArkSystemException
	 */
	public void updateArkUser(ArkUserVO userVO) throws ArkSystemException;

	/**
	 * 
	 * @param arkUserName
	 * @return
	 * @throws ArkSystemException
	 */
	public ArkUserVO lookupArkUser(String arkUserName) throws ArkSystemException;

}
