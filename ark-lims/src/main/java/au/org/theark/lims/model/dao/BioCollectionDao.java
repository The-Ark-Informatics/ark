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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BioCollectionUidTemplate;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.util.UniqueIdGenerator;

@SuppressWarnings("unchecked")
@Repository("bioCollectionDao")
public class BioCollectionDao extends HibernateSessionDao implements IBioCollectionDao {
	private static Logger		log	= LoggerFactory.getLogger(BioCollection.class);
	private BioCollectionUidGenerator bioCollectionUidGenerator;

	@Autowired
	public void setBioCollectionUidGenerator(BioCollectionUidGenerator bioCollectionUidGenerator) {
		this.bioCollectionUidGenerator = bioCollectionUidGenerator;
	}

	public BioCollection getBioCollection(Long id) throws EntityNotFoundException {
		Criteria criteria = getSession().createCriteria(BioCollection.class);
		criteria.add(Restrictions.eq("id", id));

		BioCollection bioCollection = (BioCollection) criteria.uniqueResult();
		if (bioCollection == null) {
			throw new EntityNotFoundException("The BioCollection entity cannot be found.");
		}

		return bioCollection;
	}

	public java.util.List<BioCollection> searchBioCollection(BioCollection bioCollection) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(BioCollection.class);

		if (bioCollection.getId() != null)
			criteria.add(Restrictions.eq("id", bioCollection.getId()));

		if (bioCollection.getName() != null)
			criteria.add(Restrictions.eq("name", bioCollection.getName()));

		if (bioCollection.getLinkSubjectStudy() != null)
			criteria.add(Restrictions.eq("linkSubjectStudy", bioCollection.getLinkSubjectStudy()));

		if (bioCollection.getStudy() != null)
			criteria.add(Restrictions.eq("study", bioCollection.getStudy()));

		if (bioCollection.getCollectionDate() != null)
			criteria.add(Restrictions.eq("collectionDate", bioCollection.getCollectionDate()));

		if (bioCollection.getSurgeryDate() != null)
			criteria.add(Restrictions.eq("surgeryDate", bioCollection.getSurgeryDate()));

		List<BioCollection> list = criteria.list();
		return list;
	}

	public void createBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection) {
		String bioCollectionUid = getNextGeneratedBiCollectionUID(bioCollection.getStudy());
		if(bioCollectionUid.isEmpty()) {
			bioCollectionUid = UniqueIdGenerator.generateUniqueId();
		}
		bioCollection.setName(bioCollectionUid);
		getSession().save(bioCollection);
	}

	private String getNextGeneratedBiCollectionUID(Study study) {
		Study studyToUse = null; 
		if(study.getParentStudy() != null) {
			studyToUse = study.getParentStudy(); 
		}
		else {
			studyToUse = study;
		}
		BioCollectionUidTemplate biocollectionUidTemplate = getBioCollectionUidTemplate(studyToUse);
		String bioCollectionUidPrefix = new String("");
		String bioCollectionUidToken = new String("");
		String bioCollectionUidPaddedIncrementor = new String("");
		String bioCollectionUidPadChar = new String("0");
		StringBuilder nextIncrementedBioCollectionUid = new StringBuilder("");
		StringBuilder biospecimenUid = new StringBuilder();

		if (biocollectionUidTemplate != null) {
			if (biocollectionUidTemplate.getBioCollectionUidPrefix() != null)
				bioCollectionUidPrefix = biocollectionUidTemplate.getBioCollectionUidPrefix();

			if (biocollectionUidTemplate.getBioCollectionUidToken() != null && biocollectionUidTemplate.getBioCollectionUidToken().getName() != null) {
				bioCollectionUidToken = biocollectionUidTemplate.getBioCollectionUidToken().getName();
			}

			if (biocollectionUidTemplate.getBioCollectionUidPadChar() != null && biocollectionUidTemplate.getBioCollectionUidPadChar().getName() != null) {
				bioCollectionUidPadChar = biocollectionUidTemplate.getBioCollectionUidPadChar().getName().trim();
			}

			int incrementedValue = getNextUidSequence(studyToUse).intValue() - 1;
			nextIncrementedBioCollectionUid = nextIncrementedBioCollectionUid.append(incrementedValue);

			int size = Integer.parseInt(bioCollectionUidPadChar);
			bioCollectionUidPaddedIncrementor = StringUtils.leftPad(nextIncrementedBioCollectionUid.toString(), size, "0");
			biospecimenUid.append(bioCollectionUidPrefix);
			biospecimenUid.append(bioCollectionUidToken);
			biospecimenUid.append(bioCollectionUidPaddedIncrementor);
		}
		else {
			biospecimenUid = null;
		}
		
		// handle for a null BiospecimenUID
		if(biospecimenUid == null || biospecimenUid.length() == 0){
			String uid = UniqueIdGenerator.generateUniqueId();
			biospecimenUid = new StringBuilder();
			biospecimenUid.append(uid);
			log.error("Biocollection Template is not defined for the Study: " + studyToUse.getName());
		}
		
		return biospecimenUid.toString();
	}
	
	public Integer getNextUidSequence(Study study) {
		Integer result = null;
		if (study == null) {
			log.error("Error in Biospecimen insertion - Study was null");
		}
		else if (study.getName() == null) {
			log.error("Error in Biospecimen insertion - Study name was null");
		}
		else {
			result = (Integer) bioCollectionUidGenerator.getId(study.getName());
		}
		return result;
	}
	
	public BioCollectionUidTemplate getBioCollectionUidTemplate(Study study) {
		Criteria criteria = getSession().createCriteria(BioCollectionUidTemplate.class);
		criteria.add(Restrictions.eq("study", study));
		BioCollectionUidTemplate biocollectionUidTemplate = (BioCollectionUidTemplate) criteria.uniqueResult();
		return biocollectionUidTemplate;
	}

	public void deleteBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection) {
		getSession().delete(bioCollection);
	}

	public void updateBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection) {
		getSession().update(bioCollection);
	}

	public List<BioSampletype> getSampleTypes() {
		Criteria criteria = getStatelessSession().createCriteria(BioSampletype.class);
		List<BioSampletype> list = criteria.list();
		return list;
	}

	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy) {
		// Use WHERE EXIST to optimise query even further
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(LinkSubjectStudy.class, "lss");
		DetachedCriteria sizeCriteria = DetachedCriteria.forClass(BioCollection.class, "bc");
		criteria.add(Restrictions.eq("lss.id", linkSubjectStudy.getId()));
		sizeCriteria.add(Property.forName("lss.id").eqProperty("bc.linkSubjectStudy.id"));
		criteria.add(Subqueries.exists(sizeCriteria.setProjection(Projections.property("bc.id"))));
		criteria.setProjection(Projections.rowCount());
		Boolean result = ((Integer) criteria.uniqueResult()) > 0;
		session.close();

		return result;
	}

	public Boolean hasBiospecimens(BioCollection bioCollection) {
		// Use WHERE EXIST to optimise query even further
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(BioCollection.class, "bc");
		DetachedCriteria sizeCriteria = DetachedCriteria.forClass(Biospecimen.class, "b");
		criteria.add(Restrictions.eq("bc.id", bioCollection.getId()));
		sizeCriteria.add(Property.forName("bc.id").eqProperty("b.bioCollection.id"));
		criteria.add(Subqueries.exists(sizeCriteria.setProjection(Projections.property("b.id"))));
		criteria.setProjection(Projections.rowCount());
		Boolean result = ((Integer) criteria.uniqueResult()) > 0;
		session.close();

		return result;
	}

	public int getBioCollectionCount(BioCollection bioCollectionCriteria) {
		// Handle for study not in context
		if (bioCollectionCriteria.getStudy() == null) {
			return 0;
		}
		Criteria criteria = buildBioCollectionCriteria(bioCollectionCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<BioCollection> searchPageableBioCollections(BioCollection bioCollectionCriteria, int first, int count) {
		Criteria criteria = buildBioCollectionCriteria(bioCollectionCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<BioCollection> list = criteria.list();

		return list;
	}

	protected Criteria buildBioCollectionCriteria(BioCollection bioCollectionCriteria) {
		Criteria criteria = getSession().createCriteria(BioCollection.class);

		if (bioCollectionCriteria.getId() != null)
			criteria.add(Restrictions.eq("id", bioCollectionCriteria.getId()));

		if (bioCollectionCriteria.getName() != null)
			criteria.add(Restrictions.eq("name", bioCollectionCriteria.getName()));

		if (bioCollectionCriteria.getLinkSubjectStudy() != null)
			criteria.add(Restrictions.eq("linkSubjectStudy", bioCollectionCriteria.getLinkSubjectStudy()));

		if (bioCollectionCriteria.getStudy() != null)
			criteria.add(Restrictions.eq("study", bioCollectionCriteria.getStudy()));

		if (bioCollectionCriteria.getCollectionDate() != null)
			criteria.add(Restrictions.eq("collectionDate", bioCollectionCriteria.getCollectionDate()));

		if (bioCollectionCriteria.getSurgeryDate() != null)
			criteria.add(Restrictions.eq("surgeryDate", bioCollectionCriteria.getSurgeryDate()));

		return criteria;
	}
	
	/**
	 * This count can be based on CustomFieldDisplay alone (i.e. does not need left join to BioCollectionCustomFieldData)
	 */
	public int getBioCollectionCustomFieldDataCount(BioCollection bioCollectionCriteria, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.createAlias("customField", "cfield");
		criteria.add(Restrictions.eq("cfield.study", bioCollectionCriteria.getStudy()));
		criteria.add(Restrictions.eq("cfield.arkFunction", arkFunction));
		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.uniqueResult();
		return count.intValue();
	}
	
	public List<BioCollectionCustomFieldData> getBioCollectionCustomFieldDataList(BioCollection bioCollectionCriteria, ArkFunction arkFunction, int first, int count) {
		List<BioCollectionCustomFieldData> bioCollectionCustomFieldDataList = new ArrayList<BioCollectionCustomFieldData>();
	
		StringBuffer sb = new StringBuffer();
		sb.append(  " FROM  CustomFieldDisplay AS cfd ");
		sb.append("LEFT JOIN cfd.bioCollectionCustomFieldData as fieldList ");
		sb.append(" with fieldList.bioCollection.id = :bioCollectionId ");
		sb.append( "  where cfd.customField.study.id = :studyId" );
		sb.append(" and cfd.customField.arkFunction.id = :functionId");
		sb.append(" order by cfd.sequence");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("bioCollectionId", bioCollectionCriteria.getId());
		query.setParameter("studyId", bioCollectionCriteria.getStudy().getId());
		query.setParameter("functionId", arkFunction.getId());
		query.setFirstResult(first);
		query.setMaxResults(count);
		
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			CustomFieldDisplay cfd = new CustomFieldDisplay();
			BioCollectionCustomFieldData bccfd = new BioCollectionCustomFieldData();
			if(objects.length > 0 && objects.length >= 1){
				
					cfd = (CustomFieldDisplay)objects[0];
					if(objects[1] != null){
						bccfd = (BioCollectionCustomFieldData)objects[1];
					}else{
						bccfd.setCustomFieldDisplay(cfd);
					}
					bioCollectionCustomFieldDataList.add(bccfd);	
			}
		}
		return bioCollectionCustomFieldDataList;
	}
	
	public BioCollectionCustomFieldData getBioCollectionCustomFieldData(BioCollection bioCollectionCriteria, ArkFunction arkFunction, String customFieldName) {
		StringBuffer sb = new StringBuffer();
		sb.append(  " FROM  CustomFieldDisplay AS cfd ");
		sb.append("LEFT JOIN cfd.bioCollectionCustomFieldData as fieldList ");
		sb.append(" WITH fieldList.bioCollection.id = :bioCollectionId ");
		sb.append( "  WHERE cfd.customField.study.id = :studyId" );
		sb.append(" AND cfd.customField.arkFunction.id = :functionId");
		sb.append(" AND cfd.customField.name = :customFieldName");
		sb.append(" ORDER BY cfd.sequence");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("bioCollectionId", bioCollectionCriteria.getId());
		query.setParameter("studyId", bioCollectionCriteria.getStudy().getId());
		query.setParameter("functionId", arkFunction.getId());
		query.setParameter("customFieldName", customFieldName);
		
		BioCollectionCustomFieldData bccfd = new BioCollectionCustomFieldData();
		List<Object[]> listOfObjects = query.list();
		for (Object[] objects : listOfObjects) {
			CustomFieldDisplay cfd = new CustomFieldDisplay();
			
			if(objects.length > 0 && objects.length >= 1){
				
					cfd = (CustomFieldDisplay)objects[0];
					if(objects[1] != null){
						bccfd = (BioCollectionCustomFieldData)objects[1];
					}else{
						bccfd.setCustomFieldDisplay(cfd);
					}
			}
		}
		return bccfd;
	}
	
	public void createBioCollectionCustomFieldData(BioCollectionCustomFieldData bioCollectionCFData) {
		getSession().save(bioCollectionCFData);	
	}

	public void updateBioCollectionCustomFieldData(BioCollectionCustomFieldData bioCollectionCFData) {
		getSession().update(bioCollectionCFData);
	}
	
	public void deleteBioCollectionCustomFieldData(BioCollectionCustomFieldData bioCollectionCFData) {
		getSession().delete(bioCollectionCFData);		
	}

	public Long isCustomFieldUsed(BioCollectionCustomFieldData bioCollectionCFData) {
		Long count = new Long("0");
		CustomField customField = bioCollectionCFData.getCustomFieldDisplay().getCustomField();

		// The Study
		try {
			Long id  = bioCollectionCFData.getBioCollection().getId();
			BioCollection bioCollection = getBioCollection(id);
			Study subjectStudy = bioCollection.getStudy();
			ArkFunction arkFunction = customField.getArkFunction();

			StringBuffer stringBuffer = new StringBuffer();
			
			stringBuffer.append(" SELECT COUNT(*) FROM BioCollectionCustomFieldData AS bccfd WHERE EXISTS ");
			stringBuffer.append(" ( ");               
			stringBuffer.append(" SELECT cfd.id FROM  CustomFieldDisplay AS cfd  WHERE cfd.customField.study.id = :studyId");
			stringBuffer.append(" AND cfd.customField.arkFunction.id = :functionId AND bccfd.customFieldDisplay.id = :customFieldDisplayId");
			stringBuffer.append(" )");
			
			String theHQLQuery = stringBuffer.toString();
			
			Query query = getSession().createQuery(theHQLQuery);
			query.setParameter("studyId", subjectStudy.getId());
			query.setParameter("functionId", arkFunction.getId());
			query.setParameter("customFieldDisplayId", bioCollectionCFData.getCustomFieldDisplay().getId());
			count = (Long) query.uniqueResult();
			
		} catch (EntityNotFoundException e) {
			//The given BioCollection is not available, this should not happen since the person is editing custom fields for the LIMS collection
			e.printStackTrace();
		}
			
		return count;
	}

	public BioCollection getBioCollectionByName(String name) {
		Criteria criteria = getSession().createCriteria(BioCollection.class);
		criteria.add(Restrictions.eq("name", name));
		BioCollection bioCollection = (BioCollection) criteria.uniqueResult();
		return bioCollection;
	}
}