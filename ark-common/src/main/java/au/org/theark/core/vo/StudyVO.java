package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.List;

public class StudyVO extends BaseVO{

	private static final long serialVersionUID = 1L;
	
	private String studyName;
	private List<RoleVO> roles;//From a user' perspective the list of roles for a study in context. E.g. study_admin or ordinary_user
	
	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

	private List<ModuleVO> modules;
	
	public List<ModuleVO> getModules() {
		return modules;
	}

	public void setModules(List<ModuleVO> modules) {
		this.modules = modules;
	}

	public StudyVO(){
		super();
		this.roles = new ArrayList<RoleVO>();
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

}
