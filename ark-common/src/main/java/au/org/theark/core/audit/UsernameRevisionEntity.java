package au.org.theark.core.audit;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.ModifiedEntityNames;
import au.org.theark.core.Constants;

@Entity
@RevisionEntity(UsernameRevisionListener.class)
@Table(name="REVINFO", schema=Constants.AUDIT_SCHEMA)
public class UsernameRevisionEntity extends DefaultRevisionEntity {

	private static final long serialVersionUID = 1L;

	private String username;

	@ElementCollection
	@JoinTable(schema = Constants.AUDIT_SCHEMA, name = "REVCHANGES", joinColumns = @JoinColumn(name = "REV"))
	@Column(name = "ENTITYNAME")
	@ModifiedEntityNames
	private Set<String> modifiedEntityNames = new HashSet<String>();
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Set<String> getModifiedEntityNames() {
		return modifiedEntityNames;
	}
	
	public void setModifiedEntityNames(Set<String> modifiedEntityNames) {
		this.modifiedEntityNames = modifiedEntityNames;
	}
	
	@Override
	public String toString() {
		return "UsernameRevisionEntity [id=" + getId() + 
				", revisionDate=" + getRevisionDate() + 
				", username=" + username + 
				", modifiedEntityNames=" + modifiedEntityNames + "]";
	}
	
	
}
