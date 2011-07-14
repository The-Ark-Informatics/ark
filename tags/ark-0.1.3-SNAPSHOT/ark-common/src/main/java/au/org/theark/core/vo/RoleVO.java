package au.org.theark.core.vo;

import java.io.Serializable;

public class RoleVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String role;
	
	public RoleVO(){
		super();
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		RoleVO other = (RoleVO) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Role");
		sb.append(role);
		return sb.toString();
		
	}

}
