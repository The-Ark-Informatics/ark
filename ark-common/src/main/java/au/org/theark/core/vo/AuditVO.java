package au.org.theark.core.vo;

import java.util.Date;

import org.hibernate.envers.RevisionType;

import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;

public class AuditVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	private AuditPackage auditPackage;
	private AuditEntity auditEntity;
	private AuditField auditField;
	
	private Integer revisionNumber;
	private RevisionType revisionType;
	private String revisedBy;
	private Date revisedDate;
	private Long entityID;
	
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

	public Integer getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(Integer revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	
	public RevisionType getRevisionType() {
		return revisionType;
	}

	public void setRevisionType(RevisionType revisionType) {
		this.revisionType = revisionType;
	}

	public String getRevisedBy() {
		return revisedBy;
	}

	public void setRevisedBy(String revisedBy) {
		this.revisedBy = revisedBy;
	}

	public Date getRevisedDate() {
		return revisedDate;
	}

	public void setRevisedDate(Date revisedDate) {
		this.revisedDate = revisedDate;
	}

	public Long getEntityID() {
		return entityID;
	}

	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}

	@Override
	public String toString() {
		return "AuditVO [auditPackage=" + auditPackage + ", auditEntity="
				+ auditEntity + ", auditField=" + auditField
				+ ", revisionNumber=" + revisionNumber + ", revisionType="
				+ revisionType + ", revisedBy=" + revisedBy + "]";
	}

}
