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
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.model.vo.LimsVO;

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

	public int getBiospecimenCount(LimsVO limsVo) {
		Criteria criteria = buildBiospecimenCriteria(limsVo);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}
	
	protected Criteria buildBiospecimenCriteria(LimsVO limsVo) {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		Biospecimen biospecimen = limsVo.getBiospecimen();
		
		// If study chosen, restrict otherwise restrict on users' studyList
		if(limsVo.getStudy() != null && limsVo.getStudy().getId() != null) {
			criteria.add(Restrictions.eq("study", limsVo.getStudy()));
		}
		else {
			criteria.add(Restrictions.in("study", limsVo.getStudyList()));	
		}
		
		// Restrict on linkSubjectStudy in the LimsVO (or biospecimen)
		if (limsVo.getLinkSubjectStudy() != null && limsVo.getLinkSubjectStudy().getId() != null) {
			criteria.add(Restrictions.eq("linkSubjectStudy", limsVo.getLinkSubjectStudy()));
		}
		else if (biospecimen.getLinkSubjectStudy() != null) {
			criteria.add(Restrictions.eq("linkSubjectStudy", biospecimen.getLinkSubjectStudy()));
		}
		
		if (biospecimen.getId() != null) {
			criteria.add(Restrictions.eq("id", biospecimen.getId()));
		}

		if (biospecimen.getBiospecimenUid() != null) {
			criteria.add(Restrictions.eq("biospecimenUid", biospecimen.getBiospecimenUid()));
		}

		if (biospecimen.getSampleType() != null) {
			criteria.add(Restrictions.eq("sampleType", biospecimen.getSampleType()));
		}
		
		if (biospecimen.getSampleDate() != null) {
			criteria.add(Restrictions.eq("sampleDate", biospecimen.getSampleDate()));
		}
		
		if (biospecimen.getQtyCollected() != null) {
			criteria.add(Restrictions.eq("qtyCollected", biospecimen.getQtyCollected()));
		}

		return criteria;
	}

	public List<Biospecimen> searchPageableBiospecimens(LimsVO limsVo, int first, int count) {
		Criteria criteria = buildBiospecimenCriteria(limsVo);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<Biospecimen> list = criteria.list();

		return list;
	}

	public int getBiospecimenCustomFieldDataCount(Biospecimen biospecimenCriteria, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.createAlias("customField", "cfield");
		criteria.add(Restrictions.eq("cfield.study", biospecimenCriteria.getStudy()));
		criteria.add(Restrictions.eq("cfield.arkFunction", arkFunction));
		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.uniqueResult();
		return count.intValue();
	}

	public List<BiospecimenCustomFieldData> getBiospecimenCustomFieldDataList(Biospecimen biospecimenCriteria, ArkFunction arkFunction, int first, int count) {
		List<BiospecimenCustomFieldData> biospecimenCustomFieldDataList = new ArrayList<BiospecimenCustomFieldData>();
		
		StringBuffer sb = new StringBuffer();
		sb.append(  " FROM  CustomFieldDisplay AS cfd ");
		sb.append("LEFT JOIN cfd.biospecimenCustomFieldData as fieldList ");
		sb.append(" with fieldList.biospecimen.id = :biospecimenId ");
		sb.append( "  where cfd.customField.study.id = :studyId" );
		sb.append(" and cfd.customField.arkFunction.id = :functionId");
		sb.append(" order by cfd.sequence");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("biospecimenId", biospecimenCriteria.getId());
		query.setParameter("studyId", biospecimenCriteria.getStudy().getId());
		query.setParameter("functionId", arkFunction.getId());
		query.setFirstResult(first);
		query.setMaxResults(count);
		
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			CustomFieldDisplay cfd = new CustomFieldDisplay();
			BiospecimenCustomFieldData bscfd = new BiospecimenCustomFieldData();
			if(objects.length > 0 && objects.length >= 1){
				
					cfd = (CustomFieldDisplay)objects[0];
					if(objects[1] != null){
						bscfd = (BiospecimenCustomFieldData)objects[1];
					}else{
						bscfd.setCustomFieldDisplay(cfd);
					}
					biospecimenCustomFieldDataList.add(bscfd);	
			}
		}
		return biospecimenCustomFieldDataList;
	}

	public void createBiospecimenCustomFieldData(BiospecimenCustomFieldData biospecimanCFData) {
		getSession().save(biospecimanCFData);	
	}
	
	public void updateBiospecimenCustomFieldData(BiospecimenCustomFieldData biospecimanCFData) {
		getSession().update(biospecimanCFData);		
	}

	public void deleteBiospecimenCustomFieldData(BiospecimenCustomFieldData biospecimanCFData) {
		getSession().delete(biospecimanCFData);		
	}

	public Long isCustomFieldUsed(BiospecimenCustomFieldData biospecimanCFData) {
		Long count = new Long("0");
		CustomField customField = biospecimanCFData.getCustomFieldDisplay().getCustomField();
		
		try {
			
			Long id  = biospecimanCFData.getBiospecimen().getId();
			Biospecimen biospecimen = getBiospecimen(id);
			Study subjectStudy = biospecimen.getStudy();
			ArkFunction arkFunction = customField.getArkFunction();

			StringBuffer stringBuffer = new StringBuffer();
			
			stringBuffer.append(" SELECT COUNT(*) FROM BiospecimenCustomFieldData AS bscfd WHERE EXISTS ");
			stringBuffer.append(" ( ");               
			stringBuffer.append(" SELECT cfd.id FROM  CustomFieldDisplay AS cfd  WHERE cfd.customField.study.id = :studyId");
			stringBuffer.append(" AND cfd.customField.arkFunction.id = :functionId AND bscfd.customFieldDisplay.id = :customFieldDisplayId");
			stringBuffer.append(" )");
			
			String theHQLQuery = stringBuffer.toString();
			
			Query query = getSession().createQuery(theHQLQuery);
			query.setParameter("studyId", subjectStudy.getId());
			query.setParameter("functionId", arkFunction.getId());
			query.setParameter("customFieldDisplayId", biospecimanCFData.getCustomFieldDisplay().getId());
			count = (Long) query.uniqueResult();
			
		} catch (EntityNotFoundException e) {
			//The given Biospecimen is not available, this should not happen since the person is editing custom fields for the LIMS biospecimen
			e.printStackTrace();
		}
			
		return count;
	}
}