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
package au.org.theark.lims.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.lims.web.Constants;

@SuppressWarnings("unchecked")
@Repository("bioTransactionDao")
public class BioTransactionDao extends HibernateSessionDao implements IBioTransactionDao {
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException {
		BioTransaction bioTransaction = null;
		Criteria criteria = getSession().createCriteria(BioTransaction.class);
		criteria.add(Restrictions.eq("id", id));

		List<BioTransaction> list = criteria.list();
		if (list != null && list.size() > 0) {
			bioTransaction = list.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}

		return bioTransaction;
	}
	
	private Criteria buildTransactionCriteria(BioTransaction bioTransaction) {
		Criteria criteria = getSession().createCriteria(BioTransaction.class);
		// All transactions must operate on a given biospecimen
		criteria.add(Restrictions.eq("biospecimen", bioTransaction.getBiospecimen()));
		
		return criteria;
	}

	public int getBioTransactionCount(BioTransaction bioTransaction) {
		// Handle for biospecimen not in context
		if (bioTransaction.getBiospecimen() == null) {
			return 0;
		}
		Criteria criteria = buildTransactionCriteria(bioTransaction);
		criteria.setProjection(Projections.rowCount());
		
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<BioTransaction> searchPageableBioTransactions(BioTransaction bioTransaction, int first, int count) {
		// Handle for biospecimen not in context
		if (bioTransaction.getBiospecimen() == null) {
			return new ArrayList<BioTransaction>(0);
		}
		Criteria criteria = buildTransactionCriteria(bioTransaction);
		// sort by most recent first
		criteria.addOrder(Order.desc("transactionDate"));
		criteria.addOrder(Order.desc("id"));
		// support pageable results list
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		
		List<BioTransaction> list = criteria.list();
		return list;
	}

	public void createBioTransaction(BioTransaction bioTransaction) {
		getSession().save(bioTransaction);
	}

	public void deleteBioTransaction(BioTransaction bioTransaction) {
		getSession().delete(bioTransaction);
	}

	public void updateBioTransaction(BioTransaction biospecimen) {
		getSession().update(biospecimen);
	}

	public List<TreatmentType> getTreatmentTypes() {
		Criteria criteria = getSession().createCriteria(TreatmentType.class);
		List<TreatmentType> list = criteria.list();
		return list;
	}

	public List<BioTransactionStatus> getBioTransactionStatusChoices() {
		Criteria criteria = getSession().createCriteria(BioTransactionStatus.class);
		List<BioTransactionStatus> list = criteria.list();
		// remove Initial status choice because this is reserved
		criteria.add(Restrictions.ne("name", Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY));
		return list;
	}

	public BioTransactionStatus getBioTransactionStatusByName(String statusName) {
		Criteria criteria = getSession().createCriteria(BioTransactionStatus.class);
		criteria.add(Restrictions.eq("name", statusName));
		BioTransactionStatus result = (BioTransactionStatus) criteria.uniqueResult();
		return result;
	}

}
