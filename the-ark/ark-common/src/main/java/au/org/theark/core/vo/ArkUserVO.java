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
package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.Study;

public class ArkUserVO extends BaseVO {

	private static final long	serialVersionUID	= 1L;

	private String					userName;
	private String					firstName;
	private String					lastName;
	private String					email;
	private String					password;
	private String					confirmPassword;
	private String					oldPassword;
	private String					phoneNumber;
	private List<String>			userRoleList;
	private List<String>			userModuleList;
	private List<ModuleVO>		modules;							// Represents the user associated modules and their roles
	private List<ModuleVO>		availableModules;
	private boolean				changePassword;
	private String					availableRolesLMC;
	private String					addAllBtn;
	private String					selectedRolesLMC;
	private StudyVO				studyVO;
	private List<ArkUserVO>		userList;
	private String 				captcha;

	/* Database Entity */
	private ArkUser				arkUserEntity;
	private List<ArkUserRole>	arkUserRoleList;					// A List that will contain the current user's List Modules and Roles he is linked with
																				// for the study in context
	private Study					study;
	private boolean				isArkUserPresentInDatabase;	// Specifies if the user was found in database. if this field is false then changePassword
																				// must be set to true.

	public StudyVO getStudyVO() {
		return studyVO;
	}

	public void setStudyVO(StudyVO studyVO) {
		this.studyVO = studyVO;
	}

	public String getAvailableRolesLMC() {
		return availableRolesLMC;
	}

	public void setAvailableRolesLMC(String availableRolesLMC) {
		this.availableRolesLMC = availableRolesLMC;
	}

	public String getSelectedRolesLMC() {
		return selectedRolesLMC;
	}

	public void setSelectedRolesLMC(String selectedRolesLMC) {
		this.selectedRolesLMC = selectedRolesLMC;
	}

	public String getAddAllBtn() {
		return addAllBtn;
	}

	public void setAddAllBtn(String addAllBtn) {
		this.addAllBtn = addAllBtn;
	}

	/* A field added to support a control inside the accordion */
	private String	txtField;

	public String getTxtField() {
		return txtField;
	}

	public void setTxtField(String txtField) {
		this.txtField = txtField;
	}

	private int	mode;

	public ArkUserVO() {
		super();
		this.arkUserEntity = new ArkUser();
		this.studyVO = new StudyVO();
		this.modules = new ArrayList<ModuleVO>();
		this.userRoleList = new ArrayList<String>();
		this.arkUserRoleList = new ArrayList<ArkUserRole>();
		this.study = new Study();
		this.userName = new String();
	}

	public ArkUserVO(String userName, String firstName, String lastName, String email, String password, List<ModuleVO> modules, String phoneNumber, String confirmPassword, int mode,
			String oldPassword, boolean changePassword) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.modules = modules;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.mode = mode;
		this.oldPassword = oldPassword;
		this.changePassword = changePassword;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<String> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<String> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public List<String> getUserModuleList() {
		return userModuleList;
	}

	public void setUserModuleList(List<String> userModuleList) {
		this.userModuleList = userModuleList;
	}

	public List<ModuleVO> getModules() {
		return modules;
	}

	public void setModules(List<ModuleVO> modules) {
		this.modules = modules;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nuserName or cn: ");
		sb.append(userName);
		sb.append("\nfirstName: ");
		sb.append(firstName);
		sb.append("\nemail: ");
		sb.append("\nlastName: ");
		sb.append(lastName);
		sb.append(email);
		sb.append("\n Module");
		sb.append(getModules());
		sb.append("\n Roles");
		sb.append("Phone");
		sb.append(getPhoneNumber());
		sb.append("\n Mode:");
		sb.append(mode);

		return sb.toString();
	}

	public List<ArkUserVO> getUserList() {
		return userList;
	}

	public void setUserList(List<ArkUserVO> userList) {
		this.userList = userList;
	}

	public List<ModuleVO> getAvailableModules() {
		return availableModules;
	}

	public void setAvailableModules(List<ModuleVO> availableModules) {
		this.availableModules = availableModules;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (changePassword ? 1231 : 1237);
		result = prime * result + ((confirmPassword == null) ? 0 : confirmPassword.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((oldPassword == null) ? 0 : oldPassword.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArkUserVO other = (ArkUserVO) obj;
		if (changePassword != other.changePassword)
			return false;
		if (confirmPassword == null) {
			if (other.confirmPassword != null)
				return false;
		}
		else if (!confirmPassword.equals(other.confirmPassword))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		}
		else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		}
		else if (!lastName.equals(other.lastName))
			return false;
		if (oldPassword == null) {
			if (other.oldPassword != null)
				return false;
		}
		else if (!oldPassword.equals(other.oldPassword))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		}
		else if (!password.equals(other.password))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		}
		else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		}
		else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public ArkUser getArkUserEntity() {
		return arkUserEntity;
	}

	public void setArkUserEntity(ArkUser arkUserEntity) {
		this.arkUserEntity = arkUserEntity;
	}

	public List<ArkUserRole> getArkUserRoleList() {
		return arkUserRoleList;
	}

	public void setArkUserRoleList(List<ArkUserRole> arkUserRoleList) {
		this.arkUserRoleList = arkUserRoleList;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public boolean isArkUserPresentInDatabase() {
		return isArkUserPresentInDatabase;
	}

	public void setArkUserPresentInDatabase(boolean isArkUserPresentInDatabase) {
		this.isArkUserPresentInDatabase = isArkUserPresentInDatabase;
	}

	/**
	 * @param captcha the captcha to set
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	/**
	 * @return the captcha
	 */
	public String getCaptcha() {
		return captcha;
	}
}
