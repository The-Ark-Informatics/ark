package au.org.theark.lims.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

@SuppressWarnings("unchecked")
@Repository("bioCollectionDao")
public class BioCollectionDao extends HibernateSessionDao implements IBioCollectionDao
{
	public BioCollection getBioCollection(Long id) throws EntityNotFoundException, ArkSystemException
	{
		au.org.theark.core.model.lims.entity.BioCollection limsCollection = null;
		Criteria criteria = getSession().createCriteria(BioCollection.class);
		criteria.add(Restrictions.eq("id", id));
		
		List<BioCollection> list = criteria.list();
		if (list != null && list.size() > 0)
		{
			limsCollection = list.get(0);
		}
		else
		{
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}
		
		return limsCollection;
	}

	public java.util.List<BioCollection> searchBioCollection(BioCollection bioCollection) throws ArkSystemException
	{
		Criteria criteria = getSession().createCriteria(BioCollection.class);
		
		if(bioCollection.getId() != null)
			criteria.add(Restrictions.eq("id", bioCollection.getId()));
		
		if(bioCollection.getName() != null)
			criteria.add(Restrictions.eq("name", bioCollection.getName()));
		
		if(bioCollection.getLinkSubjectStudy() != null)
			criteria.add(Restrictions.eq("linkSubjectStudy", bioCollection.getLinkSubjectStudy()));
		
		if(bioCollection.getStudy() != null)
			criteria.add(Restrictions.eq("study", bioCollection.getStudy()));
		
		if(bioCollection.getCollectionDate() != null)
			criteria.add(Restrictions.eq("collectionDate", bioCollection.getCollectionDate()));
		
		if(bioCollection.getSurgeryDate() != null)
			criteria.add(Restrictions.eq("surgeryDate", bioCollection.getSurgeryDate()));
		
		List<BioCollection> list = criteria.list();
		return list;
	}

	public void createBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection)
	{
		getSession().save(bioCollection);
	}

	public void deleteBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection)
	{
		getSession().delete(bioCollection);
	}
	
	public void updateBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection)
	{
		getSession().update(bioCollection);
	}

	public List<BioSampletype> getSampleTypes()
	{
		Criteria criteria = getStatelessSession().createCriteria(BioSampletype.class);
		List<BioSampletype> list = criteria.list();
		return list;
	}

	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy)
	{
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(BioCollection.class);
		criteria.add(Restrictions.eq("linkSubjectStudy", linkSubjectStudy));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		Boolean result = ((Integer)criteria.uniqueResult()) > 0;
		session.close();
		
		return result;
	}

	public Boolean hasBiospecimens(BioCollection bioCollection)
	{
		StatelessSession session = getStatelessSession();
		Criteria criteria = getStatelessSession().createCriteria(Biospecimen.class);
		criteria.add(Restrictions.eq("bioCollection", bioCollection));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		Boolean result = ((Integer)criteria.uniqueResult()) > 0;
		session.close();

		return result;
	}

	public int getBioCollectionCount(BioCollection bioCollectionCriteria) {
		// Handle for study not in context
		if(bioCollectionCriteria.getStudy() == null)
		{
			return 0;
		}
		Criteria criteria = buildBioCollectionCriteria(bioCollectionCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer)criteria.uniqueResult();
		return totalCount;
	}

	public List<BioCollection> searchPageableBioCollections(
			BioCollection bioCollectionCriteria, int first, int count) {
		Criteria criteria = buildBioCollectionCriteria(bioCollectionCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<BioCollection> list = criteria.list();

		return list;
	}
	
	protected Criteria buildBioCollectionCriteria(BioCollection bioCollectionCriteria) {
		Criteria criteria = getSession().createCriteria(BioCollection.class);
		
		if(bioCollectionCriteria.getId() != null)
			criteria.add(Restrictions.eq("id", bioCollectionCriteria.getId()));
		
		if(bioCollectionCriteria.getName() != null)
			criteria.add(Restrictions.eq("name", bioCollectionCriteria.getName()));
		
		if(bioCollectionCriteria.getLinkSubjectStudy() != null)
			criteria.add(Restrictions.eq("linkSubjectStudy", bioCollectionCriteria.getLinkSubjectStudy()));
		
		if(bioCollectionCriteria.getStudy() != null)
			criteria.add(Restrictions.eq("study", bioCollectionCriteria.getStudy()));
		
		if(bioCollectionCriteria.getCollectionDate() != null)
			criteria.add(Restrictions.eq("collectionDate", bioCollectionCriteria.getCollectionDate()));
		
		if(bioCollectionCriteria.getSurgeryDate() != null)
			criteria.add(Restrictions.eq("surgeryDate", bioCollectionCriteria.getSurgeryDate()));
		
		return criteria;
	}
}
