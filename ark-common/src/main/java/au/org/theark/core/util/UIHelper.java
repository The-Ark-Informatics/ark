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
package au.org.theark.core.util;

import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.ListMultipleChoice;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ModuleVO;

public class UIHelper {

	/**
	 * Compares the display value sent from front-end and returns the equivalent system value.
	 * 
	 * @param roleName
	 * @return String A system roleName
	 */
	public static String getSystemRoleName(String roleName) {

		String ldapRoleName = roleName;
		if (roleName.equalsIgnoreCase(Constants.DISPLAY_ROLE_STUDY_ADMIN)) {

			ldapRoleName = Constants.ROLE_STUDY_ADMIN;

		}
		else if (roleName.equalsIgnoreCase(Constants.DISPLAY_ROLE_SUPER_ADMIN)) {

			ldapRoleName = Constants.ROLE_SUPER_ADMIN;
		}
		else if (roleName.equalsIgnoreCase(Constants.DISPLAY_ROLE_ORDINARY_USER)) {

			ldapRoleName = Constants.ROLE_ORDINARY_USER;

		}
		else if (roleName.equalsIgnoreCase(Constants.DISPLAY_ROLE_POWER_USER)) {

			ldapRoleName = Constants.ROLE_POWER_USER;

		}
		else if (roleName.equalsIgnoreCase(Constants.DISPLAY_ROLE_LAB_PERSON)) {

			ldapRoleName = Constants.ROLE_LAB_PERSON;
		}
		else if (roleName.equalsIgnoreCase(Constants.DISPLAY_ROLE_WADB_ADMIN)) {

			ldapRoleName = Constants.ROLE_WADB_ADMINISTRATOR;
		}
		else if (roleName.equalsIgnoreCase(Constants.DISPLAY_ROLE_WADB_PERSON)) {

			ldapRoleName = Constants.ROLE_WADB_PERSON;
		}
		return ldapRoleName;
	}

	public static String getDisplayRoleName(String roleName) {

		String displayName = "Role Not Available";
		if (roleName.equalsIgnoreCase(Constants.ROLE_STUDY_ADMIN)) {

			displayName = Constants.DISPLAY_ROLE_STUDY_ADMIN;

		}
		else if (roleName.equalsIgnoreCase(Constants.ROLE_ORDINARY_USER)) {

			displayName = Constants.DISPLAY_ROLE_ORDINARY_USER;

		}
		else if (roleName.equalsIgnoreCase(Constants.ROLE_POWER_USER)) {

			displayName = Constants.DISPLAY_ROLE_POWER_USER;

		}
		else if (roleName.equalsIgnoreCase(Constants.ROLE_LAB_PERSON)) {

			displayName = Constants.DISPLAY_ROLE_LAB_PERSON;
		}
		else if (roleName.equalsIgnoreCase(Constants.ROLE_WADB_ADMINISTRATOR)) {

			displayName = Constants.DISPLAY_ROLE_WADB_ADMIN;
		}
		else if (roleName.equalsIgnoreCase(Constants.ROLE_WADB_PERSON)) {

			displayName = Constants.DISPLAY_ROLE_WADB_PERSON;
		}
		else if (roleName.equalsIgnoreCase(Constants.ROLE_SUPER_ADMIN)) {

			displayName = Constants.DISPLAY_ROLE_SUPER_ADMIN;
		}

		return displayName;
	}

	public static String getDisplayModuleName(String moduleName) {

		if (moduleName.equals(Constants.MODULE_ARK)) {

			moduleName = Constants.DISP_MODULE_ARK;

		}
		else if (moduleName.equals(Constants.MODULE_STUDY_MANAGER)) {

			moduleName = Constants.DISP_MODULE_STUDY_MANAGER;

		}
		else if (moduleName.equals(Constants.MODULE_GWAS)) {

			moduleName = Constants.DISP_MODULE_GWAS;

		}
		else if (moduleName.equals(Constants.MODULE_PHENOTYPIC)) {

			moduleName = Constants.DISP_MODULE_PHENOTYPIC;

		}
		else if (moduleName.equals(Constants.MODULE_WAGER_LAB)) {

			moduleName = Constants.DISP_MODULE_WAGER_LAB;
		}
		return moduleName;
	}

	public static void getDisplayModuleName(List<ModuleVO> modules) {
		for (ModuleVO module : modules) {
			module.setModule(getDisplayModuleName(module.getModule()));
		}
	}

	public static void getDisplayModuleNameList(List<ModuleVO> moduleVoList, List<String> listOfModuleName) {
		for (ModuleVO module : moduleVoList) {
			listOfModuleName.add(getDisplayModuleName(module.getModule()));
		}
	}

	public static String getSystemModuleName(String moduleName) {

		if (moduleName.equals(Constants.DISP_MODULE_ARK)) {

			moduleName = Constants.MODULE_ARK;

		}
		else if (moduleName.equals(Constants.DISP_MODULE_STUDY_MANAGER)) {

			moduleName = Constants.MODULE_STUDY_MANAGER;

		}
		else if (moduleName.equals(Constants.DISP_MODULE_GWAS)) {

			moduleName = Constants.MODULE_GWAS;

		}
		else if (moduleName.equals(Constants.DISP_MODULE_WAGER_LAB)) {

			moduleName = Constants.MODULE_WAGER_LAB;

		}
		return moduleName;
	}

	public static void addSelectedItems(Set<String> selectedItems, ListMultipleChoice targetMLC) {
		for (String item : selectedItems) {
			if (!targetMLC.getChoices().contains(item)) {
				targetMLC.getChoices().add(item);
			}
		}
	}

	public static void removeSelectedItems(List<String> selectedItemsToRemove, ListMultipleChoice targetMLC) {
		for (String item : selectedItemsToRemove) {
			targetMLC.getChoices().remove(item);
		}
	}

}
