package au.org.theark.lims.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioTransaction;

@SuppressWarnings("unchecked")
@Repository("bioTransactionDao")
public class BioTransactionDao extends HibernateSessionDao implements IBioTransactionDao
{
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException
	{
		BioTransaction bioTransaction = null;
		Criteria criteria = getSession().createCriteria(BioTransaction.class);
		criteria.add(Restrictions.eq("id", id));
		
		List<BioTransaction> list = criteria.list();
		if (list != null && list.size() > 0)
		{
			bioTransaction = list.get(0);
		}
		else
		{
			throw new EntityNotFoundException("The entity with id" + id.toString() + " cannot be found.");
		}
		
		return bioTransaction;
	}

	public List<BioTransaction> searchBioTransaction(BioTransaction bioTransaction) throws ArkSystemException
	{
		Criteria criteria = getSession().createCriteria(BioTransaction.class);
		
		if(bioTransaction.getId() != null)
			criteria.add(Restrictions.eq("id", bioTransaction.getId()));
		
		if(bioTransaction.getStudy() != null)
			criteria.add(Restrictions.eq("study", bioTransaction.getStudy()));
		
		if(bioTransaction.getBiospecimen() != null)
			criteria.add(Restrictions.eq("biospecimen", bioTransaction.getBiospecimen()));
		
		List<BioTransaction> list = criteria.list();
		return list;
	}

	public void createBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction)
	{
		getSession().save(bioTransaction);	
	}

	public void deleteBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction)
	{
		getSession().delete(bioTransaction);
	}

	public void updateBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction biospecimen)
	{
		getSession().update(biospecimen);
	}
}
