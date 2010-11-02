package au.org.theark.core.vo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModuleVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String module;
	private List<RoleVO> role;
	private List<StudyVO> studies;
	

	public List<StudyVO> getStudies() {
		return studies;
	}

	public void setStudies(List<StudyVO> studies) {
		this.studies = studies;
	}

	public ModuleVO(){
		super();
		this.role = new ArrayList<RoleVO>();
		this.studies = new ArrayList<StudyVO>();
	}
	
	public ModuleVO(int id, String module, String description, List<RoleVO> role) {
		super();
		this.module = module;
		this.role = role;
	}

	public List<RoleVO> getRole() {
		return role;
	}

	public void setRole(List<RoleVO> role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((module == null) ? 0 : module.hashCode());
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
		ModuleVO other = (ModuleVO) obj;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equalsIgnoreCase((other.module)))
			return false;
		return true;
	}

	public String getModule() {
		return module;
	}
	
	public void setModule(String module) {
		this.module = module;
	}
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\nModule Name");
		sb.append(module);
		sb.append("\n Roles;");
		sb.append(getRole());
		return sb.toString();
	}
	
}
