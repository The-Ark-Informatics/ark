package au.org.theark.lims.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;

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
		
		if(bioCollection.getCollectionDate() != null)
			criteria.add(Restrictions.eq("collectionDate", bioCollection.getCollectionDate()));
		
		if(bioCollection.getSurgeryDate() != null)
			criteria.add(Restrictions.eq("surgeryDate", bioCollection.getSurgeryDate()));
		
		List<BioCollection> list = criteria.list();
		return list;
	}

	public void createBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection)
	{
		getSession().save(limsCollection);
	}

	public void deleteBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection)
	{
		getSession().delete(limsCollection);
	}
	
	public void updateBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection)
	{
		getSession().update(limsCollection);
	}
}
