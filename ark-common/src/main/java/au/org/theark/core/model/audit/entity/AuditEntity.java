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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "AUDIT_ENTITY", schema = Constants.AUDIT_SCHEMA)
public class AuditEntity implements Serializable, Comparable<AuditEntity>{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private AuditPackage auditPackage;
	private String classIdentifier;
	
	private Set<AuditField> auditFields;
	
	public AuditEntity() {
	}

	@Id
	@SequenceGenerator(name = "audit_entity_generator", sequenceName = "AUDIT_ENTITY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "audit_entity_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PACKAGE_ID")
	public AuditPackage getAuditPackage() {
		return auditPackage;
	}

	public void setAuditPackage(AuditPackage auditPackage) {
		this.auditPackage = auditPackage;
	}

	@Column(name = "CLASS_IDENTIFIER")
	public String getClassIdentifier() {
		return classIdentifier;
	}

	public void setClassIdentifier(String classIdentifier) {
		this.classIdentifier = classIdentifier;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "auditEntity")
	public Set<AuditField> getAuditFields() {
		return auditFields;
	}

	public void setAuditFields(Set<AuditField> auditFields) {
		this.auditFields = auditFields;
	}

	@Override
	public String toString() {
		return "AuditEntity [id=" + id + ", name=" + name
				+ ", classIdentifier=" + classIdentifier + "]";
	}

	@Override
	public int compareTo(AuditEntity o) {
		return -o.getName().compareTo(this.getName());
	}
}
