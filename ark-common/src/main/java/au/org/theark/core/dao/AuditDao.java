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
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

import org.springframework.orm.hibernate4.SessionFactoryUtils;

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
}