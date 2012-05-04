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
package au.org.theark.admin.model.vo;

import java.io.Serializable;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;

public class ArkRoleModuleFunctionVO implements Serializable {

	private static final long	serialVersionUID	= 3593036411764379918L;
	private ArkRole				arkRole;
	private ArkModule				arkModule;
	private ArkFunction			arkFunction;
	private Boolean				arkCreatePermission;
	private Boolean				arkReadPermission;
	private Boolean				arkUpdatePermission;
	private Boolean				arkDeletePermission;

	public ArkRoleModuleFunctionVO() {
	}

	/**
	 * @return the arkRole
	 */
	public ArkRole getArkRole() {
		return arkRole;
	}

	/**
	 * @param arkRole
	 *           the arkRole to set
	 */
	public void setArkRole(ArkRole arkRole) {
		this.arkRole = arkRole;
	}

	/**
	 * @return the arkModule
	 */
	public ArkModule getArkModule() {
		return arkModule;
	}

	/**
	 * @param arkModule
	 *           the arkModule to set
	 */
	public void setArkModule(ArkModule arkModule) {
		this.arkModule = arkModule;
	}

	/**
	 * @return the arkFunction
	 */
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	/**
	 * @param arkFunction
	 *           the arkFunction to set
	 */
	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	/**
	 * @return the arkCreatePermission
	 */
	public Boolean getArkCreatePermission() {
		return arkCreatePermission;
	}

	/**
	 * @param arkCreatePermission the arkCreatePermission to set
	 */
	public void setArkCreatePermission(Boolean arkCreatePermission) {
		this.arkCreatePermission = arkCreatePermission;
	}

	/**
	 * @return the arkReadPermission
	 */
	public Boolean getArkReadPermission() {
		return arkReadPermission;
	}

	/**
	 * @param arkReadPermission the arkReadPermission to set
	 */
	public void setArkReadPermission(Boolean arkReadPermission) {
		this.arkReadPermission = arkReadPermission;
	}

	/**
	 * @return the arkUpdatePermission
	 */
	public Boolean getArkUpdatePermission() {
		return arkUpdatePermission;
	}

	/**
	 * @param arkUpdatePermission the arkUpdatePermission to set
	 */
	public void setArkUpdatePermission(Boolean arkUpdatePermission) {
		this.arkUpdatePermission = arkUpdatePermission;
	}

	/**
	 * @return the arkDeletePermission
	 */
	public Boolean getArkDeletePermission() {
		return arkDeletePermission;
	}

	/**
	 * @param arkDeletePermission the arkDeletePermission to set
	 */
	public void setArkDeletePermission(Boolean arkDeletePermission) {
		this.arkDeletePermission = arkDeletePermission;
	}
}
