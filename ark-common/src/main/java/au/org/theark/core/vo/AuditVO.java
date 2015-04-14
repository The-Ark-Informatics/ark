package au.org.theark.core.vo;

import au.org.theark.core.model.audit.entity.AuditPackage;
import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;

public class AuditVO extends BaseVO {
	
	AuditPackage auditPackage;
	AuditEntity auditEntity;
	AuditField auditField;
	
	public AuditVO() {
	}

	public AuditPackage getAuditPackage() {
		return auditPackage;
	}

	public void setAuditPackage(AuditPackage auditPackage) {
		this.auditPackage = auditPackage;
	}

	public AuditEntity getAuditEntity() {
		return auditEntity;
	}

	public void setAuditEntity(AuditEntity auditEntity) {
		this.auditEntity = auditEntity;
	}

	public AuditField getAuditField() {
		return auditField;
	}

	public void setAuditField(AuditField auditField) {
		this.auditField = auditField;
	}	
}
