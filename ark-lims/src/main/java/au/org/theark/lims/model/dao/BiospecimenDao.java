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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;

@SuppressWarnings("unchecked")
@Repository("biospecimenDao")
public class BiospecimenDao extends HibernateSessionDao implements IBiospecimenDao {
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException {
		Biospecimen biospecimen = null;
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("id", id));

		List<Biospecimen> list = criteria.list();
		if (list != null && list.size() > 0) {
			biospecimen = list.get(0);
		}
		else {
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}

		return biospecimen;
	}

	public List<Biospecimen> searchBiospecimen(Biospecimen biospecimen) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);

		if (biospecimen.getId() != null)
			criteria.add(Restrictions.eq("id", biospecimen.getId()));

		if (biospecimen.getBiospecimenUid() != null)
			criteria.add(Restrictions.eq("biospecimenId", biospecimen.getBiospecimenUid()));

		if (biospecimen.getLinkSubjectStudy() != null)
			criteria.add(Restrictions.eq("linkSubjectStudy", biospecimen.getLinkSubjectStudy()));

		if (biospecimen.getStudy() != null)
			criteria.add(Restrictions.eq("study", biospecimen.getStudy()));

		if (biospecimen.getSampleType() != null)
			criteria.add(Restrictions.eq("sampleType", biospecimen.getSampleType()));

		if (biospecimen.getSampleDate() != null)
			criteria.add(Restrictions.eq("sampleDate", biospecimen.getSampleDate()));

		if (biospecimen.getQtyCollected() != null)
			criteria.add(Restrictions.eq("qtyCollected", biospecimen.getQtyCollected()));

		List<Biospecimen> list = criteria.list();
		return list;
	}

	public void createBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) {
		getSession().save(biospecimen);
	}

	public void deleteBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) {
		getSession().delete(biospecimen);
	}

	public void updateBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) {
		getSession().update(biospecimen);
	}

	public int getBiospecimenCount(Biospecimen biospecimenCriteria) {
		// Handle for study not in context
		if (biospecimenCriteria.getStudy() == null) {
			return 0;
		}
		Criteria criteria = buildBiospecimenCriteria(biospecimenCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<Biospecimen> searchPageableBiospecimens(Biospecimen biospecimenCriteria, int first, int count) {
		Criteria criteria = buildBiospecimenCriteria(biospecimenCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<Biospecimen> list = criteria.list();

		return list;
	}

	protected Criteria buildBiospecimenCriteria(Biospecimen biospecimen) {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);

		if (biospecimen.getId() != null)
			criteria.add(Restrictions.eq("id", biospecimen.getId()));

		if (biospecimen.getBiospecimenUid() != null)
			criteria.add(Restrictions.eq("biospecimenId", biospecimen.getBiospecimenUid()));

		if (biospecimen.getLinkSubjectStudy() != null)
			criteria.add(Restrictions.eq("linkSubjectStudy", biospecimen.getLinkSubjectStudy()));

		if (biospecimen.getStudy() != null)
			criteria.add(Restrictions.eq("study", biospecimen.getStudy()));

		if (biospecimen.getSampleType() != null)
			criteria.add(Restrictions.eq("sampleType", biospecimen.getSampleType()));

		if (biospecimen.getSampleDate() != null)
			criteria.add(Restrictions.eq("sampleDate", biospecimen.getSampleDate()));

		if (biospecimen.getQtyCollected() != null)
			criteria.add(Restrictions.eq("qtyCollected", biospecimen.getQtyCollected()));

		return criteria;

	}

	public Biospecimen getBiospecimenByUid(String biospecimenUid) {
		Biospecimen biospecimen = null;
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("biospecimenUid", biospecimenUid));

		List<Biospecimen> list = criteria.list();
		if (list != null && list.size() > 0) {
			biospecimen = list.get(0);
		}

		return biospecimen;
	}
}
