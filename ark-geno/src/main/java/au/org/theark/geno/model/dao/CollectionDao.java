package au.org.theark.geno.model.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.geno.model.entity.GenoCollection;
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
	public List<GenoCollection> getCollectionMatches(GenoCollection colExample)
	{	
		Criteria colCriteria = getSession().createCriteria(GenoCollection.class);
		
		if(colExample.getId() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENO_COLLECTION_ID, colExample.getId()));	
		}
		
		if(colExample.getName() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENO_COLLECTION_NAME, colExample.getName(), MatchMode.ANYWHERE));	
		}
		
		if(colExample.getDescription() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENO_COLLECTION_DESCRIPTION, colExample.getDescription(), MatchMode.ANYWHERE));	
		}
		
		if(colExample.getStudy() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENO_COLLECTION_STUDY, colExample.getStudy()));
		}
		
		if(colExample.getInsertTime() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENO_COLLECTION_INSERT_TIME, colExample.getInsertTime()));
		}
		
		if(colExample.getUserId() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENO_COLLECTION_USER_ID, colExample.getUserId(), MatchMode.ANYWHERE));
		}

		if(colExample.getUpdateTime() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENO_COLLECTION_UPDATE_TIME, colExample.getUpdateTime()));
		}

		if(colExample.getUpdateUserId() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENO_COLLECTION_UPDATE_USER_ID, colExample.getUpdateUserId(), MatchMode.ANYWHERE));
		}
		
		if(colExample.getStatus() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENO_COLLECTION_STATUS, colExample.getStatus()));
		}

		colCriteria.addOrder(Order.asc("name"));
		List<GenoCollection> colList  = colCriteria.list();
		return colList;
	}

	public void createCollection(GenoCollection col) {
		getSession().save(col);
	}

	
	public GenoCollection getCollection(Long id) {
		
		GenoCollection col = (GenoCollection)getSession().get(GenoCollection.class, id);
		return col;
	}
	
	public void updateCollection(GenoCollection colEntity) {
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

	public Collection<Status> getStatus() {
		Criteria crit = getSession().createCriteria(Status.class);
		java.util.Collection<Status> statusCollection = crit.list();
		return (statusCollection);
	}

}
