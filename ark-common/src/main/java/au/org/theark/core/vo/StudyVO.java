package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.List;

public class StudyVO extends BaseVO{

	private String studyName;
	private List<ModuleVO> modules;
	private List<RoleVO> roles;//From a user' perspective the list of roles for a study in context. E.g. study_admin or ordinary_user

	/**
	 * Constructor
	 */
	public StudyVO(){
		super();
		this.roles = new ArrayList<RoleVO>();
	}
	
	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}
	
	public List<ModuleVO> getModules() {
		return modules;
	}

	public void setModules(List<ModuleVO> modules) {
		this.modules = modules;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

}
