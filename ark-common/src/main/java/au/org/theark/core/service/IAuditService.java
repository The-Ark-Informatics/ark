package au.org.theark.core.service;

import java.lang.reflect.Method;
import java.util.List;

import org.hibernate.envers.AuditReader;

import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;
import au.org.theark.core.vo.AuditVO;

public interface IAuditService {
 
	public AuditReader getAuditReader();
	
	public List<AuditEntity> getAuditEntityList();

	public List<AuditPackage> getAuditPackageList();

	public List<AuditField> getAuditFieldList();

	public Long getAllEntitiesCountForClass(AuditVO auditVO);
	
	public List getAllEntitiesForClass(AuditVO auditVO, long first, long count);

	public Method getEntityDisplayMethod(Class entityClass);
	
	public Class<?> getFieldReadMethodReturnType(String field, Class<?> cls);
	
	public Method getFieldReadMethod(String field, Class<?> cls);
	
	public String getEntityValue(Object entity, Method fieldDisplayMethod, Method fieldReadMethod);

	public String getEntityValue(Object entity);
	
	public Long getEntityPrimaryKey(Object entity);

	public Object getPreviousEntity(Long id, Object entity, Number currentRevision);

	public boolean isAudited(Class<?> type);

	public String getFieldName(Class<?> cls, String field);
}
