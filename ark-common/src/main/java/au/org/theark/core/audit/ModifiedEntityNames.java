package au.org.theark.core.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "modified_entity_names", schema = Constants.AUDIT_SCHEMA)
public class ModifiedEntityNames {
	
	private long id;
	private UsernameRevisionEntity revisionEntity;
	private String className;

	@Id
	@SequenceGenerator(name = "modified_entity_names_generator", sequenceName = "MODIFIED_ENTITY_NAMES_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "modified_entity_names_generator")
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REVISION_ENTITY_ID")
	public UsernameRevisionEntity getRevisionEntity() {
		return revisionEntity;
	}

	public void setRevisionEntity(UsernameRevisionEntity revisionEntity) {
		this.revisionEntity = revisionEntity;
	}

	@Column(name = "CLASS_NAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "ModifiedEntityNames [id=" + id + ", className=" + className
				+ "]";
	}
	
}
