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

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.springframework.ldap.core.DirContextOperations;

public class LdapUtility {

	public static String buildPersonDN(String userName, String baseDC) throws InvalidNameException {
		LdapName ldapName = new LdapName(baseDC);
		ldapName.add(new Rdn("ou", "people"));
		ldapName.add(new Rdn("cn", userName));
		return ldapName.toString();
	}

	public static void mapToGroupContext(DirContextOperations dirContext, String groupOrRoleName, String[] members) {
		dirContext.setAttributeValues("objectClass", new String[] { "groupOfNames", "top" });
		dirContext.setAttributeValue("cn", groupOrRoleName);
		dirContext.setAttributeValues("member", members);
	}

}
