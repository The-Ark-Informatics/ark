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
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleFunction;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.Study;

/**
 * @author cellis
 * 
 */
public class AdminVO implements Serializable {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -3939245546324873647L;

	private ArkRole								arkRole;
	private ArkModule								arkModule;
	private ArkFunction							arkFunction;
	private ArkModuleFunction					arkModuleFunction;
	private ArkRolePolicyTemplate				arkRolePolicyTemplate;
	private Study									study;
	private List<Study>							studyList;
	private List<ArkRolePolicyTemplate>		arkRolePolicyTemplateList;
	private List<ArkFunction>					arkFunctionList;
	private Boolean								arkCreatePermission;
	private Boolean								arkReadPermission;
	private Boolean								arkUpdatePermission;
	private Boolean								arkDeletePermission;
	private ArkRoleModuleFunctionVO			arkRoleModuleFunctionVo;
	private List<ArkRoleModuleFunctionVO>	arkRoleModuleFunctionVoList;
	private List<ArkFunction>					availableArkFunctions;
	private List<ArkFunction>					selectedArkFunctions;
	private List<ArkRole>						availableArkRoles;
	private List<ArkRole>						selectedArkRoles;

	public AdminVO() {
		this.arkRole = new ArkRole();
		this.arkModule = new ArkModule();
		this.arkFunction = new ArkFunction();
		this.setArkModuleFunction(new ArkModuleFunction());
		this.arkRolePolicyTemplate = new ArkRolePolicyTemplate();
		this.study = new Study();
		this.studyList = new ArrayList<Study>(0);
		this.arkRolePolicyTemplateList = new ArrayList<ArkRolePolicyTemplate>(0);
		this.arkFunctionList = new ArrayList<ArkFunction>(0);
		this.arkCreatePermission = new Boolean("False");
		this.arkReadPermission = new Boolean("False");
		this.arkUpdatePermission = new Boolean("False");
		this.arkDeletePermission = new Boolean("False");
		this.arkRoleModuleFunctionVo = new ArkRoleModuleFunctionVO();
		this.setArkRoleModuleFunctionVoList(new ArrayList<ArkRoleModuleFunctionVO>(0));
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
	 * @param arkModuleFunction the arkModuleFunction to set
	 */
	public void setArkModuleFunction(ArkModuleFunction arkModuleFunction) {
		this.arkModuleFunction = arkModuleFunction;
	}

	/**
	 * @return the arkModuleFunction
	 */
	public ArkModuleFunction getArkModuleFunction() {
		return arkModuleFunction;
	}

	/**
	 * @param arkRolePolicyTemplate
	 *           the arkRolePolicyTemplate to set
	 */
	public void setArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		this.arkRolePolicyTemplate = arkRolePolicyTemplate;
	}

	/**
	 * @return the arkRolePolicyTemplate
	 */
	public ArkRolePolicyTemplate getArkRolePolicyTemplate() {
		return arkRolePolicyTemplate;
	}

	/**
	 * @param study
	 *           the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}

	/**
	 * @return the study
	 */
	public Study getStudy() {
		return study;
	}

	/**
	 * @param studyList
	 *           the studyList to set
	 */
	public void setStudyList(List<Study> studyList) {
		this.studyList = studyList;
	}

	/**
	 * @return the studyList
	 */
	public List<Study> getStudyList() {
		return studyList;
	}

	/**
	 * @param arkRolePolicyTemplateList
	 *           the arkRolePolicyTemplateList to set
	 */
	public void setArkRolePolicyTemplateList(List<ArkRolePolicyTemplate> arkRolePolicyTemplateList) {
		this.arkRolePolicyTemplateList = arkRolePolicyTemplateList;
	}

	/**
	 * @return the arkRolePolicyTemplateList
	 */
	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList() {
		return arkRolePolicyTemplateList;
	}

	/**
	 * @param arkFunctionList
	 *           the arkFunctionList to set
	 */
	public void setArkFunctionList(List<ArkFunction> arkFunctionList) {
		this.arkFunctionList = arkFunctionList;
	}

	/**
	 * @return the arkFunctionList
	 */
	public List<ArkFunction> getArkFunctionList() {
		return arkFunctionList;
	}

	/**
	 * @return the arkCreatePermission
	 */
	public Boolean getArkCreatePermission() {
		return arkCreatePermission;
	}

	/**
	 * @param arkCreatePermission
	 *           the arkCreatePermission to set
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
	 * @param arkReadPermission
	 *           the arkReadPermission to set
	 */
	public void setArkReadPermission(Boolean arkReadPermission) {
		this.arkReadPermission = arkReadPermission;
	}

	/**
	 * @return the arkUpdatePermission
	 */
	public Boolean getArkUpdatePermission() {
		return this.arkUpdatePermission;
	}

	/**
	 * @param arkUpdatePermission
	 *           the arkUpdatePermission to set
	 */
	public void setArkUpdatePermission(Boolean arkUpdatePermission) {
		this.arkUpdatePermission = arkUpdatePermission;
	}

	/**
	 * @return the arkDeletePermission
	 */
	public Boolean getArkDeletePermission() {
		return this.arkDeletePermission;
	}

	/**
	 * @param arkDeletePermission
	 *           the arkDeletePermission to set
	 */
	public void setArkDeletePermission(Boolean arkDeletePermission) {
		this.arkDeletePermission = arkDeletePermission;
	}

	/**
	 * @param arkRoleModuleFunctionVo
	 *           the arkRoleModuleFunctionVO to set
	 */
	public void setArkRoleModuleFunctionVo(ArkRoleModuleFunctionVO arkRoleModuleFunctionVo) {
		this.arkRoleModuleFunctionVo = arkRoleModuleFunctionVo;
	}

	/**
	 * @return the arkRoleModuleFunctionVO
	 */
	public ArkRoleModuleFunctionVO getArkRoleModuleFunctionVo() {
		return arkRoleModuleFunctionVo;
	}

	/**
	 * @param arkRoleModuleFunctionVoList
	 *           the arkRoleModuleFunctionVoList to set
	 */
	public void setArkRoleModuleFunctionVoList(List<ArkRoleModuleFunctionVO> arkRoleModuleFunctionVoList) {
		this.arkRoleModuleFunctionVoList = arkRoleModuleFunctionVoList;
	}

	/**
	 * @return the arkRoleModuleFunctionVoList
	 */
	public List<ArkRoleModuleFunctionVO> getArkRoleModuleFunctionVoList() {
		return arkRoleModuleFunctionVoList;
	}

	public List<ArkFunction> getAvailableArkFunctions() {
		return availableArkFunctions;
	}

	public void setAvailableArkFunctions(List<ArkFunction> availableArkFunctions) {
		this.availableArkFunctions = availableArkFunctions;
	}

	public List<ArkFunction> getSelectedArkFunctions() {
		return selectedArkFunctions;
	}

	public void setSelectedArkFunctions(List<ArkFunction> selectedArkFunctions) {
		this.selectedArkFunctions = selectedArkFunctions;
	}
	
	public List<ArkRole> getSelectedArkRoles() {
		return selectedArkRoles;
	}

	public void setAvailableArkRoles(List<ArkRole> availableArkRoles) {
		this.availableArkRoles = availableArkRoles;
	}
	
	public List<ArkRole> getAvailableArkRoles() {
		return availableArkRoles;
	}

	public void setSelectedArkRoles(List<ArkRole> selectedArkRoles) {
		this.selectedArkRoles = selectedArkRoles;
	}
}
