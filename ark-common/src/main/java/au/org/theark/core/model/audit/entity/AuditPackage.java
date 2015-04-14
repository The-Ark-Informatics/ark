package au.org.theark.core.model.audit.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import au.org.theark.core.Constants;

@Entity
@Table(name = "AUDIT_PACKAGE", schema = Constants.AUDIT_SCHEMA)
public class AuditPackage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private Set<AuditEntity> auditEntities;
	
	public AuditPackage() {
	}

	@Id
	@SequenceGenerator(name = "audit_package_generator", sequenceName = "AUDIT_PACKAGE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "audit_package_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "auditPackage")
	public Set<AuditEntity> getAuditEntities() {
		return auditEntities;
	}
	
	public void setAuditEntities(Set<AuditEntity> auditEntities) {
		this.auditEntities = auditEntities;
	}

	@Override
	public String toString() {
		return "AuditPackage [id=" + id + ", name=" + name + "]";
	}
}
