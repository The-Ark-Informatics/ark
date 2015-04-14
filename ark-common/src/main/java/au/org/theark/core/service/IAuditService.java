package au.org.theark.core.service;

import java.util.List;

import org.hibernate.envers.AuditReader;

import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;

public interface IAuditService {
 
	public AuditReader getAuditReader();
	
	public List<AuditEntity> getAuditEntityList();

	public List<AuditPackage> getAuditPackageList();

	public List<AuditField> getAuditFieldList();
	
	public List getAllEntitiesForClass(Class cls);
	
	public Long getAllEntitiesCountForClass(Class cls);
	
	public List getAllEntitiesForClass(Class cls, int first, int count);

}
