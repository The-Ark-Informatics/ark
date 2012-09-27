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
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenAnticoagulant;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenGrade;
import au.org.theark.core.model.lims.entity.BiospecimenQuality;
import au.org.theark.core.model.lims.entity.BiospecimenStatus;
import au.org.theark.core.model.lims.entity.BiospecimenStorage;
import au.org.theark.core.model.lims.entity.BiospecimenUidSequence;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.util.UniqueIdGenerator;
import au.org.theark.lims.web.Constants;

@SuppressWarnings("unchecked")
@Repository("biospecimenDao")
public class BiospecimenDao extends HibernateSessionDao implements IBiospecimenDao {
	private static Logger		log	= LoggerFactory.getLogger(BiospecimenDao.class);
	
	private BiospecimenUidGenerator		biospecimenUidGenerator;

	/**
	 * @param biospecimenUidGenerator the biospecimenUidGenerator to set
	 */
	@Autowired
	public void setBiospecimenUidGenerator(BiospecimenUidGenerator biospecimenUidGenerator) {
		this.biospecimenUidGenerator = biospecimenUidGenerator;
	}

	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException {	
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("id", id));

		Biospecimen biospecimen = (Biospecimen) criteria.uniqueResult();
		if (biospecimen.getId() == null) {
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}

		return biospecimen;
	}

	public List<Biospecimen> searchBiospecimen(Biospecimen biospecimen) throws ArkSystemException {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);

		if (biospecimen.getId() != null)
			criteria.add(Restrictions.eq("id", biospecimen.getId()));

		if (biospecimen.getBiospecimenUid() != null)
			criteria.add(Restrictions.ilike("biospecimenUid", biospecimen.getBiospecimenUid(), MatchMode.ANYWHERE));

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

	public void createBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) throws ArkSystemException{
		Study studyFromBiospecimen = biospecimen.getStudy();
		Study studyToUse = null; 
		String biospecimenUid = null;
			
		if(studyFromBiospecimen.getParentStudy() != null) {
			studyToUse = studyFromBiospecimen.getParentStudy(); 
		}
		else {
			studyToUse = studyFromBiospecimen;
		}
		if(studyToUse.getAutoGenerateBiospecimenUid()){
			biospecimenUid = getNextGeneratedBiospecimenUID(biospecimen.getStudy());
		}
		else{
			if(getBiospecimenByUid(biospecimen.getBiospecimenUid(), studyToUse) == null){
				biospecimenUid = biospecimen.getBiospecimenUid();
			}
			else{
				throw new ArkSystemException("The Biospecimen UID " + biospecimen.getBiospecimenUid() + " is not unique for the given study.  Please select a unique ID");
			}
		}
		
		if(biospecimenUid == null || biospecimenUid.isEmpty()) {
			biospecimenUid = UniqueIdGenerator.generateUniqueId();
		}
		
		biospecimen.setBiospecimenUid(biospecimenUid);
		getSession().save(biospecimen);
	}

	public void deleteBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) {
		getSession().delete(biospecimen);
	}

	public void updateBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) throws ArkSystemException{
//		Biospecimen alreadyExisting = getBiospecimenByUid(biospecimen.getBiospecimenUid(), biospecimen.getStudy()) ;
		if(doesSomeoneElseHaveThisUid(biospecimen.getBiospecimenUid(), biospecimen.getStudy(), biospecimen.getId()) ){ //ie; the uid exists BUT its not the current biospecimen itself			
			throw new ArkSystemException("You are attempting to update a biospecimen to a UID (" + biospecimen.getBiospecimenUid() + ") that already exists.");
		}		
		getSession().update(biospecimen);
	}

	public long getBiospecimenCount(Biospecimen biospecimenCriteria) {
		// Handle for study not in context
		if (biospecimenCriteria.getStudy() == null) {
			return 0;
		}
		Criteria criteria = buildBiospecimenCriteria(biospecimenCriteria);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount.intValue();
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
			criteria.add(Restrictions.ilike("biospecimenUid", biospecimen.getBiospecimenUid(), MatchMode.ANYWHERE));

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
		
		if(biospecimen.getParent() != null) {
			criteria.add(Restrictions.eq("parent", biospecimen.getParent()));
		}

		return criteria;
	}

	public boolean doesSomeoneElseHaveThisUid(String biospecimenUid, final Study study, Long idToExcludeFromSearch) {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("biospecimenUid", biospecimenUid));
		criteria.add(Restrictions.ne("id", idToExcludeFromSearch));
		if(study!=null){
			criteria.add(Restrictions.eq("study", study));
		}
		return (criteria.list().size()>0);
	}

	public long getBiospecimenCount(LimsVO limsVo) {
		Criteria criteria = buildBiospecimenCriteria(limsVo);
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		return totalCount.intValue();
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
			criteria.add(Restrictions.ilike("biospecimenUid", biospecimen.getBiospecimenUid(), MatchMode.ANYWHERE));
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

	public long getBiospecimenCustomFieldDataCount(Biospecimen biospecimenCriteria, ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(CustomFieldDisplay.class);
		criteria.createAlias("customField", "cfield");
		
		// Allow child studies to inherit parent defined custom fields
		List studyList = new ArrayList();
		studyList.add(biospecimenCriteria.getStudy());
		if(biospecimenCriteria.getStudy().getParentStudy() != null && biospecimenCriteria.getStudy().getParentStudy() != biospecimenCriteria.getStudy()) {
			studyList.add(biospecimenCriteria.getStudy().getParentStudy());
		}
		
		criteria.add(Restrictions.in("cfield.study", studyList));
		criteria.add(Restrictions.eq("cfield.arkFunction", arkFunction));
		criteria.setProjection(Projections.rowCount());
		return  (Long)criteria.uniqueResult();
	}

	public List<BiospecimenCustomFieldData> getBiospecimenCustomFieldDataList(Biospecimen biospecimenCriteria, ArkFunction arkFunction, int first, int count) {
		List<BiospecimenCustomFieldData> biospecimenCustomFieldDataList = new ArrayList<BiospecimenCustomFieldData>();
		
		StringBuffer sb = new StringBuffer();
		sb.append(  " FROM  CustomFieldDisplay AS cfd ");
		sb.append("LEFT JOIN cfd.biospecimenCustomFieldData as fieldList ");
		sb.append(" with fieldList.biospecimen.id = :biospecimenId ");
		sb.append( "  where cfd.customField.study.id IN (:studyId)" );
		sb.append(" and cfd.customField.arkFunction.id = :functionId");
		sb.append(" order by cfd.sequence");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("biospecimenId", biospecimenCriteria.getId());
		
		// Allow child studies to inherit parent defined custom fields
		List studyList = new ArrayList();
		studyList.add(biospecimenCriteria.getStudy().getId());
		if(biospecimenCriteria.getStudy().getParentStudy() != null && biospecimenCriteria.getStudy().getParentStudy() != biospecimenCriteria.getStudy()) {
			studyList.add(biospecimenCriteria.getStudy().getParentStudy().getId());
		}
		query.setParameterList("studyId", studyList);
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

	public List<Unit> getUnits() {
		Criteria criteria = getSession().createCriteria(Unit.class);
		List<Unit> list = criteria.list();
		return list;
	}
	
	public Double getQuantityAvailable(Biospecimen biospecimen) {
		Criteria criteria = getSession().createCriteria(BioTransaction.class);
		criteria.add(Restrictions.eq("biospecimen", biospecimen));
		criteria.setProjection(Projections.sum("quantity"));
		Double sum = (Double) criteria.uniqueResult();
		return sum;
	}

	public List<BiospecimenAnticoagulant> getBiospecimenAnticoagulantList() {
		Criteria criteria = getSession().createCriteria(BiospecimenAnticoagulant.class);
		List<BiospecimenAnticoagulant> list = criteria.list();
		return list;
	}

	public List<BiospecimenGrade> getBiospecimenGradeList() {
		Criteria criteria = getSession().createCriteria(BiospecimenGrade.class);
		List<BiospecimenGrade> list = criteria.list();
		return list;
	}

	public List<BiospecimenQuality> getBiospecimenQualityList() {
		Criteria criteria = getSession().createCriteria(BiospecimenQuality.class);
		List<BiospecimenQuality> list = criteria.list();
		return list;
	}

	public List<BiospecimenStatus> getBiospecimenStatusList() {
		Criteria criteria = getSession().createCriteria(BiospecimenStatus.class);
		List<BiospecimenStatus> list = criteria.list();
		return list;
	}

	public List<BiospecimenStorage> getBiospecimenStorageList() {
		Criteria criteria = getSession().createCriteria(BiospecimenStorage.class);
		List<BiospecimenStorage> list = criteria.list();
		return list;
	}
	
	public BiospecimenUidTemplate getBiospecimenUidTemplate(Study study) {
		Criteria criteria = getSession().createCriteria(BiospecimenUidTemplate.class);
		criteria.add(Restrictions.eq("study", study));
		BiospecimenUidTemplate biospecimenUidTemplate = (BiospecimenUidTemplate) criteria.uniqueResult();
		return biospecimenUidTemplate;
	}
	
	public String getNextGeneratedBiospecimenUID(Study study) {
		Study studyToUse = null; 
		if(study.getParentStudy() != null) {
			studyToUse = study.getParentStudy(); 
		}
		else {
			studyToUse = study;
		}
		BiospecimenUidTemplate biospecimenUidTemplate = getBiospecimenUidTemplate(studyToUse);
		String biospecimenUidPrefix = new String("");
		String biospecimenUidToken = new String("");
		String biospecimenUidPaddedIncrementor = new String("");
		String biospecimenUidPadChar = new String("0");
		StringBuilder nextIncrementedBiospecimenUid = new StringBuilder("");
		StringBuilder biospecimenUid = new StringBuilder();

		if (biospecimenUidTemplate != null) {
			if (biospecimenUidTemplate.getBiospecimenUidPrefix() != null)
				biospecimenUidPrefix = biospecimenUidTemplate.getBiospecimenUidPrefix();

			if (biospecimenUidTemplate.getBiospecimenUidToken() != null && biospecimenUidTemplate.getBiospecimenUidToken().getName() != null) {
				biospecimenUidToken = biospecimenUidTemplate.getBiospecimenUidToken().getName();
			}

			if (biospecimenUidTemplate.getBiospecimenUidPadChar() != null && biospecimenUidTemplate.getBiospecimenUidPadChar().getName() != null) {
				biospecimenUidPadChar = biospecimenUidTemplate.getBiospecimenUidPadChar().getName().trim();
			}

			int incrementedValue = getNextUidSequence(studyToUse).intValue();
			nextIncrementedBiospecimenUid = nextIncrementedBiospecimenUid.append(incrementedValue);

			int size = Integer.parseInt(biospecimenUidPadChar);
			biospecimenUidPaddedIncrementor = StringUtils.leftPad(nextIncrementedBiospecimenUid.toString(), size, "0");
			biospecimenUid.append(biospecimenUidPrefix);
			biospecimenUid.append(biospecimenUidToken);
			biospecimenUid.append(biospecimenUidPaddedIncrementor);
		}
		else {
			biospecimenUid = null;
		}
		
		// handle for a null BiospecimenUID
		if(biospecimenUid == null || biospecimenUid.length() == 0){
			String uid = UniqueIdGenerator.generateUniqueId();
			biospecimenUid = new StringBuilder();
			biospecimenUid.append(uid);
			log.error("Biospecimen Template is not defined for the Study: " + studyToUse.getName());
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
			result = (Integer) biospecimenUidGenerator.getId(study.getName());
		}
		return result;
	}

	protected boolean getBiospecimenUidSequenceLock(Study study) {
		boolean lock;
		BiospecimenUidSequence biospecimenUidSeq = getBiospecimenUidSequence(study);
		if (biospecimenUidSeq == null) {
			lock = false; // not locked if record doesn't exist
		}
		else {
			lock = biospecimenUidSeq.getInsertLock();
		}
		return lock;
	}

	protected BiospecimenUidSequence getBiospecimenUidSequence(Study study) {
		// Stateless sessions should be used to avoid locking the record for future update
		// by getSession(), which relies on the "open session filter" mechanism
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(BiospecimenUidSequence.class);
		criteria.add(Restrictions.eq(Constants.SUBJECTUIDSEQ_STUDYNAMEID, study.getName()));
		criteria.setMaxResults(1);
		BiospecimenUidSequence result = (BiospecimenUidSequence) criteria.uniqueResult();
		session.close();
		return result;
	}

	protected void setSubjectUidSequenceLock(Study study, boolean lock) {
		// Stateless sessions should be used to avoid locking the record for future update
		// by getSession(), which relies on the "open session filter" mechanism
		StatelessSession session = getStatelessSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		BiospecimenUidSequence biospecimenUidSeq = getBiospecimenUidSequence(study);
		if (biospecimenUidSeq == null) {
			// create a new record if it doens't exist
			biospecimenUidSeq = new BiospecimenUidSequence();
			biospecimenUidSeq.setStudyNameId(study.getName());
			biospecimenUidSeq.setUidSequence(new Integer(0));
			biospecimenUidSeq.setInsertLock(lock);
			session.insert(biospecimenUidSeq);
		}
		else {
			biospecimenUidSeq.setInsertLock(lock);
			session.update(biospecimenUidSeq);
		}
		tx.commit();
		session.close();
	}
	
	public boolean studyHasBiospecimens(Study study) {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.count("id"));
		Long count = (Long) criteria.uniqueResult();
		return count>0;
	}

	public void batchInsertBiospecimens(Collection<Biospecimen> insertBiospecimens) {
		for (Biospecimen biospecimen : insertBiospecimens) {

			getSession().save(biospecimen);
			for(BioTransaction bioTransaction : biospecimen.getBioTransactions()){
				getSession().save(bioTransaction);
			}

			InvCell invCell = biospecimen.getInvCell();
			if(invCell!=null){
				getSession().refresh(biospecimen);
				invCell.setBiospecimen(biospecimen);
				invCell.setStatus("Not Empty"); // TODO constants/enum
				getSession().saveOrUpdate(invCell);
			}
		}
	}

	public void batchUpdateBiospecimens(Collection<Biospecimen> updateBiospecimens) {
		for (Biospecimen biospecimen : updateBiospecimens) {
			getSession().update(biospecimen);
		}
		
	}

	public BioSampletype getBioSampleTypeByName(String name) {
		Criteria criteria = getSession().createCriteria(BioSampletype.class);
		criteria.add(Restrictions.eq("name", name));
		return (BioSampletype) criteria.uniqueResult();
	}

	public TreatmentType getTreatmentTypeByName(String name) {
		Criteria criteria = getSession().createCriteria(TreatmentType.class);
		criteria.add(Restrictions.eq("name", name));
		return (TreatmentType) criteria.uniqueResult();
	}

	public Unit getUnitByName(String name) {
		Criteria criteria = getSession().createCriteria(Unit.class);
		criteria.add(Restrictions.eq("name", name));
		return (Unit) criteria.uniqueResult();
	}

	public List<Biospecimen> getBiospecimenByBioCollection(BioCollection bioCollection) {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("bioCollection", bioCollection));
		return criteria.list();
	}

	public Biospecimen getBiospecimenByUid(String biospecimenUid, final Study study) {
		Criteria criteria = getSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("biospecimenUid", biospecimenUid));
		if(study!=null){
			criteria.add(Restrictions.eq("study", study));
		}
		Biospecimen biospecimen = (Biospecimen) criteria.uniqueResult();
		return biospecimen;
	}

	public List<String> getAllBiospecimenUIDs(Study study) {
		String queryString = "select bio.biospecimenUid " +
		"from Biospecimen bio " +
		"where study =:study " +
		"order by biospecimenUid ";
		Query query =  getSession().createQuery(queryString);
		query.setParameter("study", study);
		return query.list();
	}

	
}