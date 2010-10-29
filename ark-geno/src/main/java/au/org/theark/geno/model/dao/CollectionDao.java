package au.org.theark.geno.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.geno.model.entity.Collection;
import au.org.theark.geno.model.entity.CollectionImport;
import au.org.theark.geno.model.entity.MetaData;
import au.org.theark.geno.model.entity.MetaDataField;
import au.org.theark.geno.model.entity.MetaDataType;
import au.org.theark.geno.model.entity.Status;

// TODO: Replace all hardcoded userIds with actual code from SecurityManager
// See SearchStudyFrom.java in ark-common
//Subject currentUser = SecurityUtils.getSubject();

@Repository("collectionDao")
public class CollectionDao extends HibernateSessionDao implements ICollectionDao {

	static Logger log = LoggerFactory.getLogger(CollectionDao.class);

    @SuppressWarnings("unchecked")
	public List<Collection> getCollectionMatches(Collection colExample)
	{	
		Criteria colCriteria = getSession().createCriteria(Collection.class);
		
		if(colExample.getId() != null) {
			colCriteria.add(Restrictions.eq("id",colExample.getId()));	
		}
		
		if(colExample.getName() != null) {
			colCriteria.add(Restrictions.ilike("name", colExample.getName(), MatchMode.ANYWHERE));	
		}
		
		if(colExample.getStudyId() != null) {
			colCriteria.add(Restrictions.eq("studyId", colExample.getStudyId()));
		}
		
		if(colExample.getInsertTime() != null) {
			colCriteria.add(Restrictions.eq("insertTime", colExample.getInsertTime()));
		}
		
		if(colExample.getUserId() != null) {
			colCriteria.add(Restrictions.ilike("userId", colExample.getUserId(), MatchMode.ANYWHERE));
		}

		if(colExample.getUpdateTime() != null) {
			colCriteria.add(Restrictions.eq("updateTime", colExample.getUpdateTime()));
		}

		if(colExample.getUpdateUserId() != null) {
			colCriteria.add(Restrictions.ilike("updateUserId", colExample.getUpdateUserId(), MatchMode.ANYWHERE));
		}
		
		if(colExample.getStatus() != null) {
			colCriteria.add(Restrictions.eq("status", colExample.getStatus()));
		}

		colCriteria.addOrder(Order.asc("name"));
		List<Collection> colList  = colCriteria.list();
		return colList;
	}

	public void createCollection(Collection col) {
		getSession().save(col);
	}

	
	public Collection getCollection(Long id) {
		
		Collection col = (Collection)getSession().get(Collection.class, id);
		return col;
	}
	
	public void updateCollection(Collection colEntity) {
		getSession().update(colEntity);
	}
	
	public void createMetaDataField(MetaDataField mdf)
	{
		getSession().save(mdf);
	}
	
	public void updateMetaDataField(MetaDataField mdf)
	{
		getSession().update(mdf);
	}
	
	//Testing the create of records
	//Each FK reference for MetaData must exist (i.e. be saved) prior to use
    public void createMetaData(MetaData metaData)
    {
        getSession().save(metaData);
    }
    
	public void updateMetaData(MetaData mdEntity) {
		getSession().update(mdEntity);
	}
	
    @SuppressWarnings("unchecked")
	public Status getStatusByName(String statusName) {
    	Criteria crit = getSession().createCriteria(Status.class);
    	crit.add(Restrictions.eq("name", statusName));
    	crit.addOrder(Order.asc("id"));
		List<Status> statusList = crit.list();
		if (statusList.size() > 0) {			
			if (statusList.size() > 1) {
				log.error("Backend database has non-unique Status names, returned the first one");
			}
			return (statusList.get(0));
		}
        else
        	return null;
    }

    @SuppressWarnings("unchecked")
    //TODO: Patch the Long studyId with a true study object?
    //(it should already be in context)
    public MetaDataField getMetaDataFieldByName(Long studyId, String mdfName) {
    	Criteria crit = getSession().createCriteria(MetaDataField.class);
    	crit.add(Restrictions.eq("name", mdfName));
    	crit.add(Restrictions.eq("studyId", studyId));
    	crit.addOrder(Order.asc("id"));
		List<MetaDataField> mdfList = crit.list();
		if (mdfList.size() > 0) {			
			if (mdfList.size() > 1) {
				log.error("Backend database has non-unique MetaDataField names, returned the first one");
			}
			return (mdfList.get(0));
		}
        else
        	return null;
    }

    @SuppressWarnings("unchecked")
    //TODO: Patch the Long studyId with a true study object?
    //(it should already be in context)
    public MetaDataField getMetaDataField(Long metaDataFieldId) {
    	MetaDataField mdf = (MetaDataField)getSession().get(MetaDataField.class, metaDataFieldId);
    	return mdf;
    }
    
    @SuppressWarnings("unchecked")
	public MetaDataType getMetaDataTypeByName(String typeName)
    {
        Criteria crit = getSession().createCriteria(MetaDataType.class);
    	crit.add(Restrictions.eq("name", typeName));
        crit.addOrder(Order.asc("id"));
        //@SuppressWarnings("unchecked")
        List<MetaDataType> mdtList = crit.list();
		if (mdtList.size() > 0) {
			if (mdtList.size() > 1) {
				log.error("Backend database has non-unique MetaDataType names, returned the first one");
			}
			return (mdtList.get(0));
		}
        else
        	return null;

// Using Example to define criteria and then return records
// *but* returns the transient (example) object if none found
//        MetaDataType mdt = new MetaDataType();
//        mdt.setName("Number");
//        Example ex = Example.create(mdt);
//        Criteria crit = getSession().createCriteria(MetaDataType.class).add(ex);
//        if (mdtList != null && mdtList.size() > 0)
//        {
//            mdt = mdtList.get(0);
//        }
//        return mdt;

// Using HQL to return records
//        String hql = "from MetaDataType where name=:dataType";
//        Query q = getSession().createQuery(hql);
//        q.setString("dataType", dataType);
//        q.setMaxResults(1);
//        @SuppressWarnings("unchecked")
//        List<MetaDataType> results = q.list();
//        if (results.size() > 0)
//        {
//            return (results.get(0));
//        }
//        else
//            return null;
    }

	public void createCollectionImport(CollectionImport colImport) {
		getSession().save(colImport);
	}

}
