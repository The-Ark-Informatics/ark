package au.org.theark.admin.model.vo;

import java.io.Serializable;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;

public class ArkRoleModuleFunctionVO implements Serializable {
	/**
	 * 
	 */
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
