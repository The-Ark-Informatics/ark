package au.org.theark.core.service;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.write.DateFormat;

import org.hibernate.criterion.MatchMode;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.hibernate.envers.query.property.OriginalIdPropertyName;
import org.hibernate.proxy.HibernateProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.audit.annotations.ArkAuditDisplay;
import au.org.theark.core.dao.IAuditDao;
import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;
import au.org.theark.core.vo.AuditVO;

/**
 * @author george
 *
 */
@Transactional
@Service(Constants.ARK_AUDIT_SERVICE)
public class AuditServiceImpl implements IAuditService {

	protected transient Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);

	private IAuditDao iAuditDao;

	@Autowired
	public void setAuditDai(IAuditDao iAuditDao) {
		this.iAuditDao = iAuditDao;
	}

	public IAuditDao getAuditDao() {
		return iAuditDao;
	}

	public AuditReader getAuditReader() {
		return iAuditDao.getAuditReader();
	}

	public List<AuditEntity> getAuditEntityList() {
		return iAuditDao.getAuditEntityList();
	}

	public List<AuditPackage> getAuditPackageList() {
		return iAuditDao.getAuditPackageList();
	}

	public List<AuditField> getAuditFieldList() {
		return iAuditDao.getAuditFieldList();
	}

	private AuditQuery getAuditQuery(Class cls, AuditVO auditVO) {
		AuditQuery query = getAuditReader().createQuery()
				.forRevisionsOfEntity(cls, false, true)
				.addOrder(org.hibernate.envers.query.AuditEntity.revisionNumber().desc());

		if(auditVO.getRevisionNumber() != null) {
			query.add(org.hibernate.envers.query.AuditEntity.revisionNumber().eq(auditVO.getRevisionNumber()));
		}

		if(auditVO.getRevisionType() != null) {
			query.add(org.hibernate.envers.query.AuditEntity.revisionType().eq(auditVO.getRevisionType()));
		}

		if(auditVO.getRevisedBy() != null) {
			query.add(org.hibernate.envers.query.AuditEntity.revisionProperty("username").like(auditVO.getRevisedBy(), MatchMode.ANYWHERE));
		}
		
		if(auditVO.getRevisedDate() != null) {
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(auditVO.getRevisedDate());
			long low = cal.getTimeInMillis();
			cal.add(Calendar.DATE, 1);
			long high = cal.getTimeInMillis();
			query.add(org.hibernate.envers.query.AuditEntity.revisionProperty("timestamp").between(low, high));
		}
		
		if(auditVO.getEntityID() != null) {
			query.add(org.hibernate.envers.query.AuditEntity.id().eq(auditVO.getEntityID()));
		}

		return query;
	}

	public Long getAllEntitiesCountForClass(AuditVO auditVO) {
		try {
			Class cls = Class.forName(auditVO.getAuditEntity().getClassIdentifier());
			AuditQuery query = getAuditQuery(cls, auditVO);
			query.addProjection(new AuditProperty(new OriginalIdPropertyName("id")).count());
			return (Long) query.getSingleResult();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0L;
		}

	}

	public List getAllEntitiesForClass(AuditVO auditVO, int first, int count) {
		try {
			Class cls = Class.forName(auditVO.getAuditEntity().getClassIdentifier());
			AuditQuery query = getAuditQuery(cls, auditVO);
			query.setFirstResult(first).setMaxResults(count);
			return query.getResultList();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	public Method getEntityDisplayMethod(Class entityClass) {
		try {
			if(entityClass.getPackage().getName().contains("theark")) {
				if(hasUniqueAnnotatedMethod(entityClass, ArkAuditDisplay.class)) {
					for(Method m : entityClass.getMethods()) {
						if(m.getAnnotation(ArkAuditDisplay.class) != null) {
							return m;
						}
					}
				} 
			}
			if(hasMethod(entityClass, "getName")){
				return entityClass.getMethod("getName", null);
			}
			return entityClass.getMethod("toString", null);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Class<?> getFieldReadMethodReturnType(String field, Class<?> cls) {
		Method readMethod = getFieldReadMethod(field, cls);
		if(readMethod != null) { 
			return readMethod.getReturnType();
		} else {
			return null;
		}
	}

	public Method getFieldReadMethod(String field, Class<?> cls) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(field, cls);
			return pd.getReadMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the value of the provided entity that is described by the provided fieldReadMethod.
	 * @param entity The entity to get the value of.
	 * @param fieldDisplayMethod The method which should be used to convert the entity to a string. This is usually returned by {@link #getEntityDisplayMethod(Class) getEntityDisplayMethod} method.
	 * @param fieldReadMethod The method which should be used to get the value to be returned
	 * @return The string representation of the provided entity. If no representation could be achieved, an empty string is returned.
	 */
	public String getEntityValue(Object entity, Method fieldDisplayMethod, Method fieldReadMethod) {
		StringBuilder sb = new StringBuilder();
		try {
			Object returned = fieldReadMethod.invoke(entity, (Object[])null);
			if(returned != null) {
				if(returned instanceof Date) {
					sb.append(new DateFormat(Constants.DD_MM_YYYY).getDateFormat().format(returned));
				} else {
					sb.append(fieldDisplayMethod.invoke(returned, (Object[])null));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	/**
	 * Gets the value of the provided entity.
	 * @param entity The entity to get the value of.
	 * @return The string representation of the provided entity. If no representation could be achieved, an empty string is returned.
	 */
	public String getEntityValue(Object entity) {
		if(entity == null) {
			return "";
		}
		Method displayMethod = getEntityDisplayMethod(HibernateProxyHelper.getClassWithoutInitializingProxy(entity));
		StringBuilder sb = new StringBuilder();
		try {
			if(entity instanceof Date) {
				sb.append(new DateFormat(Constants.DD_MM_YYYY).getDateFormat().format(entity));
			} else {
				sb.append(displayMethod.invoke(entity, (Object[])null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Checks to see if the provided annotation class appears on any method in the search class.
	 * @param searchClass The class to search in
	 * @param annotationClass The annotation to count
	 * @return True if the annotation appears, false otherwise
	 */
	private boolean hasAnnotatedMethod(Class searchClass, Class<? extends Annotation> annotationClass) { 
		return annotatedMethodCount(searchClass, annotationClass) > 0;
	}

	/**
	 * Checks to see if the provided annotation class appears only once (on any method) in the search class.
	 * @param searchClass The class to search in
	 * @param annotationClass The annotation to count
	 * @return True if the annotation appears, false otherwise
	 */
	private boolean hasUniqueAnnotatedMethod(Class searchClass, Class<? extends Annotation> annotationClass) { 
		return annotatedMethodCount(searchClass, annotationClass) == 1;
	}

	/**
	 * Counts the number of methods annotated with the given annotationClass
	 * @param searchClass The class to search in
	 * @param annotationClass The annotation to count
	 * @return The number of methods having a matching annotation
	 */
	private int annotatedMethodCount(Class searchClass, Class<? extends Annotation> annotationClass) {
		int count = 0;
		for(Method m : searchClass.getMethods()) {
			if(m.isAnnotationPresent(annotationClass)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns whether the class has a method named "methodName". This ignores case.
	 * @param cls The class to check
	 * @param methodName The name of the method to look for
	 * @return True if the method exists, false otherwise.
	 */
	private boolean hasMethod(Class cls, String methodName) {
		for(Method m : Arrays.asList(cls.getMethods())) {
			if(m.getName().equalsIgnoreCase(methodName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the primary key (ID) of the provided entity
	 * @param entity The entity we wish to know the primary key of
	 * @return The primary key of the entity. Returns null if any errors.
	 */
	public Long getEntityPrimaryKey(Object entity) {
		Class entityClass = entity.getClass();
		if(hasMethod(entityClass, "getID")) {
			Long id;
			try {
				id = (Long) entityClass.getMethod("getId", null).invoke(entity, null);
				return id;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Finds and returns the entity as it was before being modified in
	 * the current revision
	 * @param id The primary key of the entity
	 * @param entity The entity that we want the previous revision of
	 * @param currentRevision The current revision number for the entity
	 * @return The entity that was modified before the current revision. Returns null if any errors.
	 */
	public Object getPreviousEntity(Long id, Object entity, Number currentRevision) {
		AuditReader reader = getAuditReader();
		List<Number> revisions = reader.getRevisions(entity.getClass(), id);
		int index = revisions.indexOf(currentRevision);
		if(index - 1 >= 0) {
			Number previousRevisionNumber = revisions.get(index-1);
			Object en = reader.find(entity.getClass(), id, previousRevisionNumber);	 
			return en;
		}
		return null;
	}

	@Override
	public boolean isAudited(Class<?> type) {
		return iAuditDao.isAudited(type);
	}

	@Override
	public String getFieldName(Class<?> cls, String field) {
		return iAuditDao.getFieldName(cls, field);
	}

}
