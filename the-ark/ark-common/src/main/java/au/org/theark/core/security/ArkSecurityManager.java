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
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

/**
 * A wrapper for Shiro Security Manager
 * 
 * @author nivedann
 * 
 */
public class ArkSecurityManager {

	private static ArkSecurityManager	arkManager;

	public static ArkSecurityManager getInstance() {

		if (arkManager == null) {
			arkManager = new ArkSecurityManager();
		}
		return arkManager;
	}

	private SecurityManager getShiroSecurityManager() {
		return ThreadContext.getSecurityManager();

	}

	public boolean subjectHasRole(String roleName) {

		Subject currentUser = SecurityUtils.getSubject();
		return getShiroSecurityManager().hasRole(currentUser.getPrincipals(), roleName);
	}

}
