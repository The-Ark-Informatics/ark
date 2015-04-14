package au.org.theark.core.service;

import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.hibernate.envers.query.property.OriginalIdPropertyName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.IAuditDao;
import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;

@Transactional
@Service(Constants.ARK_AUDIT_SERVICE)
public class AuditServiceImpl implements IAuditService {

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

	public List getAllEntitiesForClass(Class cls) {
		return getAuditReader().createQuery()
				.forRevisionsOfEntity(cls, false, true)
				.getResultList();
	}
	
	public Long getAllEntitiesCountForClass(Class cls) {
		return (Long) getAuditReader().createQuery()
				.forRevisionsOfEntity(cls, false, true)
				.addProjection(new AuditProperty(new OriginalIdPropertyName("id")).countDistinct())
				.getSingleResult();
	}
	
	public List getAllEntitiesForClass(Class cls, int first, int count) {
		return getAuditReader().createQuery()
				.forRevisionsOfEntity(cls, false, true)
				.setFirstResult(first)
				.setMaxResults(count)
				.getResultList();
	}
}
