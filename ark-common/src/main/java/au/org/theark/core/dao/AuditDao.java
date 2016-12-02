/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;
import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * Provides CRUD and accessor methods to Audit entities
 * 
 * @author cellis
 */
@Repository("auditDao")
public class AuditDao extends HibernateSessionDao implements IAuditDao {
	@SuppressWarnings("unchecked")
	public List<LssConsentHistory> getLssConsentHistoryList(LinkSubjectStudy linkSubjectStudy) {
		Criteria criteria = getSession().createCriteria(LssConsentHistory.class);
		criteria.add(Restrictions.eq("linkSubjectStudy", linkSubjectStudy));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<ConsentHistory> getConsentHistoryList(Consent consent) {
		Criteria criteria = getSession().createCriteria(ConsentHistory.class);
		criteria.add(Restrictions.eq("linkSubjectStudy", consent.getLinkSubjectStudy()));
		
		if(consent.getStudyComp() != null) {
			criteria.add(Restrictions.eq("studyComp", consent.getStudyComp()));
		}
		else {
			criteria.add(Restrictions.isNull("studyComp"));
		}
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	public void createConsentHistory(ConsentHistory consentHistory) {
		getSession().save(consentHistory);
		getSession().refresh(consentHistory);
	}

	public void createLssConsentHistory(LssConsentHistory lssConsentHistory) {
		getSession().save(lssConsentHistory);
		getSession().refresh(lssConsentHistory);
	}
	
	public AuditReader getAuditReader() {
		return AuditReaderFactory.get(getSession());
	}

	@Override
	public List<AuditEntity> getAuditEntityList() {
		Criteria criteria = getSession().createCriteria(AuditEntity.class);
		return criteria.list();
	}

	@Override
	public List<AuditPackage> getAuditPackageList() { 
		Criteria criteria = getSession().createCriteria(AuditPackage.class);
//		criteria.setProjection(Projections.distinct(Projections.id()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<AuditField> getAuditFieldList() {
		Criteria criteria = getSession().createCriteria(AuditField.class);
		return criteria.list();
	}

	@Override
	public boolean isAudited(Class<?> type) {
		Criteria criteria = getSession().createCriteria(AuditEntity.class);
		criteria.add(Restrictions.eq("classIdentifier", type.getName()));
		criteria.setProjection(Projections.rowCount());
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount != 0;
	}

	@Override
	public String getFieldName(Class<?> cls, String field) {
		Criteria criteria = getSession().createCriteria(AuditField.class);
		criteria.add(Restrictions.eq("fieldName", field));
		criteria.createAlias("auditEntity", "ae");
		criteria.add(Restrictions.eq("ae.classIdentifier", cls.getCanonicalName()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);	
		AuditField auditField=(AuditField) criteria.uniqueResult();
		if(auditField!=null){
			return auditField.getName();
		}else{
			return null;
		}
	}
}