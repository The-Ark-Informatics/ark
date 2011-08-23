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
import au.org.theark.core.model.geno.entity.CollectionImport;
import au.org.theark.core.model.geno.entity.DelimiterType;
import au.org.theark.core.model.geno.entity.FileFormat;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.MetaData;
import au.org.theark.core.model.geno.entity.MetaDataField;
import au.org.theark.core.model.geno.entity.MetaDataType;
import au.org.theark.core.model.geno.entity.Status;
import au.org.theark.core.model.geno.entity.Upload;
import au.org.theark.core.model.geno.entity.UploadCollection;

// TODO: Replace all hardcoded userIds with actual code from SecurityManager
// See SearchStudyFrom.java in ark-common
//Subject currentUser = SecurityUtils.getSubject();

@Repository("collectionDao")
public class CollectionDao extends HibernateSessionDao implements ICollectionDao {

	static Logger log = LoggerFactory.getLogger(CollectionDao.class);

    // Create
	public void createMetaDataField(MetaDataField mdf)
	{
		getSession().save(mdf);
	}
	
	//Testing the create of records
	//Each FK reference for MetaData must exist (i.e. be saved) prior to use
    public void createMetaData(MetaData metaData)
    {
        getSession().save(metaData);
    }

	public void createCollection(GenoCollection col) {
		getSession().save(col);
	}

	public void createCollectionImport(CollectionImport colImport) {
		getSession().save(colImport);
	}

	public void createUploadCollection(UploadCollection uploadCollection) {
		if (uploadCollection.getUpload().getId() == null) {
			getSession().save(uploadCollection.getUpload());
		}
		else {
			log.debug("createUploadCollection(..) - Upload should not already exist!");
		}
		getSession().save(uploadCollection);
	}
	
	// Read
    @SuppressWarnings("unchecked")
    public List<GenoCollection> getCollectionMatches(GenoCollection genoCollectionCriteria)
	{	
		Criteria colCriteria = getSession().createCriteria(GenoCollection.class);
		
		if(genoCollectionCriteria.getId() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENOCOLLECTION_ID, genoCollectionCriteria.getId()));	
		}
		
		if(genoCollectionCriteria.getName() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENOCOLLECTION_NAME, genoCollectionCriteria.getName(), MatchMode.ANYWHERE));	
		}
		
		if(genoCollectionCriteria.getDescription() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENOCOLLECTION_DESCRIPTION, genoCollectionCriteria.getDescription(), MatchMode.ANYWHERE));	
		}
		
		if(genoCollectionCriteria.getStudy() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENOCOLLECTION_STUDY, genoCollectionCriteria.getStudy()));
		}
		
		if(genoCollectionCriteria.getInsertTime() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENOCOLLECTION_INSERTTIME, genoCollectionCriteria.getInsertTime()));
		}
		
		if(genoCollectionCriteria.getUserId() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENOCOLLECTION_USERID, genoCollectionCriteria.getUserId(), MatchMode.ANYWHERE));
		}

		if(genoCollectionCriteria.getUpdateTime() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENOCOLLECTION_UPDATETIME, genoCollectionCriteria.getUpdateTime()));
		}

		if(genoCollectionCriteria.getUpdateUserId() != null) {
			colCriteria.add(Restrictions.ilike(au.org.theark.geno.service.Constants.GENOCOLLECTION_UPDATEUSERID, genoCollectionCriteria.getUpdateUserId(), MatchMode.ANYWHERE));
		}
		
		if(genoCollectionCriteria.getStatus() != null) {
			colCriteria.add(Restrictions.eq(au.org.theark.geno.service.Constants.GENOCOLLECTION_STATUS, genoCollectionCriteria.getStatus()));
		}

		colCriteria.addOrder(Order.asc("name"));
		List<GenoCollection> colList  = colCriteria.list();
		return colList;
	}

	public GenoCollection getCollection(Long id) {
		
		GenoCollection col = (GenoCollection)getSession().get(GenoCollection.class, id);
		return col;
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

	public Collection<Status> getStatusCollection() {
		Criteria crit = getSession().createCriteria(Status.class);
		java.util.Collection<Status> statusCollection = crit.list();
		return (statusCollection);
	}

	public Collection<FileFormat> getFileFormatCollection() {
		Criteria crit = getSession().createCriteria(FileFormat.class);
		java.util.Collection<FileFormat> fileFormatCollection = crit.list();
		return (fileFormatCollection);
	}

	public Collection<DelimiterType> getDelimiterTypeCollection() {
		Criteria crit = getSession().createCriteria(DelimiterType.class);
		java.util.Collection<DelimiterType> fileFormatCollection = crit.list();
		return (fileFormatCollection);
	}	

	public Collection<UploadCollection> getFileUploadMatches(
			UploadCollection uploadCollectionCriteria) {
		
		Upload uploadCriteria;
    	Criteria crit = getSession().createCriteria(UploadCollection.class);
    	
    	// Have to alias the child for the ability to add criteria/order on child fields
		crit.createAlias(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD, au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD); 

		if(uploadCollectionCriteria.getId() != null) {
			crit.add(Restrictions.eq(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_ID, uploadCollectionCriteria.getId()));	
		}
		
		if(uploadCollectionCriteria.getCollection() != null) {
			crit.add(Restrictions.eq(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_GENOCOLLECTION, uploadCollectionCriteria.getCollection()));	
		}
		
		if((uploadCriteria = uploadCollectionCriteria.getUpload()) != null) { 
			if (uploadCriteria.getFilename() != null) {
				crit.add(Restrictions.ilike(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD_FILENAME, uploadCriteria.getFilename(), MatchMode.ANYWHERE));
			}
			
			if(uploadCriteria.getFileFormat() != null) {
				crit.add(Restrictions.eq(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD_FILEFORMAT, uploadCriteria.getFileFormat()));
			}

			if(uploadCriteria.getDelimiterType() != null) {
				crit.add(Restrictions.eq(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD_DELIMITERTYPE, uploadCriteria.getDelimiterType()));
			}
		}
		
		if(uploadCollectionCriteria.getUserId() != null) {
			crit.add(Restrictions.ilike(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_USERID, uploadCollectionCriteria.getUserId(), MatchMode.ANYWHERE));
		}

		if(uploadCollectionCriteria.getInsertTime() != null) {
			crit.add(Restrictions.eq(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_INSERTTIME, uploadCollectionCriteria.getInsertTime()));
		}
		
		if(uploadCollectionCriteria.getUpdateTime() != null) {
			crit.add(Restrictions.eq(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPDATETIME, uploadCollectionCriteria.getUpdateTime()));
		}

		if(uploadCollectionCriteria.getUpdateUserId() != null) {
			crit.add(Restrictions.ilike(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPDATEUSERID, uploadCollectionCriteria.getUpdateUserId(), MatchMode.ANYWHERE));
		}

		crit.addOrder(Order.asc(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD_FILENAME));
		crit.addOrder(Order.asc(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_UPLOAD_FILEFORMAT));
		List<UploadCollection> colList  = crit.list();
		return colList;
	}

	// Update
	public void updateCollection(GenoCollection colEntity) {
		getSession().update(colEntity);
	}
	
	public void updateMetaData(MetaData mdEntity) {
		getSession().update(mdEntity);
	}

	public void updateMetaDataField(MetaDataField mdf) {
		getSession().update(mdf);
	}

	// Delete
	public void deleteCollection(GenoCollection col) {
		getSession().delete(col);
	}

	public void deleteUploadCollection(UploadCollection uploadCollection) {
		getSession().delete(uploadCollection);
	}

}
