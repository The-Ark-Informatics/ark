package au.org.theark.report.model.vo.report;

import java.io.Serializable;

public class StudyUserRolePermissionsDataRow implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	protected String				studyName;
	protected String				userName;
	protected String				roleName;
	protected String				moduleName;
	protected String				create;
	protected String				read;
	protected String				update;
	protected String				delete;

	public StudyUserRolePermissionsDataRow() {
	}

	/**
	 * @return the studyName
	 */
	public String getStudyName() {
		return studyName;
	}

	/**
	 * @param studyName
	 *           the studyName to set
	 */
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *           the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *           the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the create
	 */
	public String getCreate() {
		return create;
	}

	/**
	 * @param create
	 *           the create to set
	 */
	public void setCreate(String create) {
		this.create = create;
	}

	/**
	 * @return the read
	 */
	public String getRead() {
		return read;
	}

	/**
	 * @param read
	 *           the read to set
	 */
	public void setRead(String read) {
		this.read = read;
	}

	/**
	 * @return the update
	 */
	public String getUpdate() {
		return update;
	}

	/**
	 * @param update
	 *           the update to set
	 */
	public void setUpdate(String update) {
		this.update = update;
	}

	/**
	 * @return the delete
	 */
	public String getDelete() {
		return delete;
	}

	/**
	 * @param delete
	 *           the delete to set
	 */
	public void setDelete(String delete) {
		this.delete = delete;
	}

}
