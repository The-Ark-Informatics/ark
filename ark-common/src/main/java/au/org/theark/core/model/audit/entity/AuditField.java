package au.org.theark.core.model.audit.entity;

import java.io.Serializable;

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
@Table(name = "AUDIT_FIELD", schema = Constants.AUDIT_SCHEMA)
public class AuditField implements Serializable, Comparable<AuditField> {

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private AuditEntity auditEntity;
	private String fieldName;
	
	public AuditField(){
	}
	
	@Id
	@SequenceGenerator(name = "audit_field_generator", sequenceName = "AUDIT_FIELD_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "audit_field_generator")
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
	@JoinColumn(name = "ENTITY_ID")
	public AuditEntity getAuditEntity() {
		return auditEntity;
	}

	public void setAuditEntity(AuditEntity auditEntity) {
		this.auditEntity = auditEntity;
	}

	@Column(name = "FIELD_NAME")
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public int compareTo(AuditField o) {
		return -o.getName().compareTo(this.getName());
	}
}
