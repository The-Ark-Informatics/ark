package au.org.theark.core.audit;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import au.org.theark.core.Constants;

@Entity
@RevisionEntity(UsernameRevisionListener.class)
@Table(name="REVINFO", schema=Constants.AUDIT_SCHEMA)
public class UsernameRevisionEntity extends DefaultRevisionEntity {

	private static final long serialVersionUID = 1L;

	private String username;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UsernameRevisionEntity [id=" + getId() + 
				", revisionDate=" + getRevisionDate() + 
				", username=" + username + "]";
	}
	
	
}
