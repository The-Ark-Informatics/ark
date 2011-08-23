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

//import java.util.List;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.geno.entity.EncodedData;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.Marker;
import au.org.theark.core.model.geno.entity.MarkerType;

@Repository("gwasDao")
public class GwasDao extends HibernateSessionDao implements IGwasDao
{
	static Logger log = LoggerFactory.getLogger(CollectionDao.class);
	
	//Testing the create of records
	//Each FK reference for MetaData must exist (i.e. be saved) prior to use
/*
    public void createMetaData(MetaData metaData)
    {
        log.debug("Create method invoked");
        // getSession().save(metaData);
        MetaDataType mdt = getMetaDataType("Number");
        log.debug("Tried to get MetaDataType(\"Number\"): " + mdt);
        Date dateNow = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String nowStr = sdf.format(dateNow);

        Session s = getSession();
        
        MetaDataField mdf = new MetaDataField();
        mdf.setName("Mass");
        mdf.setDescription("kg");
        log.debug("Now: " + nowStr);
        mdf.setStudyId(new Long(1));
        mdf.setUserId("elam");
        mdf.setInsertTime(dateNow);
        mdf.setMetaDataType(mdt);
        s.save(mdf);
        metaData.setUserId("elam");
        metaData.setInsertTime(dateNow);
        //TODO FIX STATUS AND COLLECTION
        Status stat = getStatusByName("Active");
        Collection colEn = new Collection();
        colEn.setStudyId(100);
        colEn.setStatus(stat);
        colEn.setUserId("elam");
        colEn.setInsertTime(dateNow);
        s.save(colEn);
        metaData.setCollection(colEn);
        metaData.setMetaDataField(mdf);
        log.debug("metaData.setMetaDataField: "+ mdf);
        s.save(metaData);
        log.debug("Tried to create a MetaDataField(\"Number\", \"Mass\", \"kg\"): " + mdf);

//        Person p = new Person();
//        p.setFirstName("A");
//        p.setLastName("Name");
//        p.setMiddleName("M");
//        getSession().save(p);
//        s.delete(metaData);
//        log.debug("Tried to delete the newly created MetaDataField(\"Number\", \"Mass\", \"kg\"): " + mdf);
    }

    public void createMetaData(MetaDataField mdf, String value)
    {
        log.debug("Invoked createMetaData...");
        MetaData metaData = new MetaData();
        metaData.setMetaDataField(mdf);
        metaData.setValue(value);
        getSession().save(metaData);
        log.debug("Created new MetaData");
    }

    public void createMetaDataField(String dataType, String name,
            String description)
    {
        log.debug("Invoked createMetaDataField...");
        MetaDataField mdf = new MetaDataField();
        MetaDataType mdt = getMetaDataType(dataType);
        mdf.setMetaDataType(mdt);
        mdf.setName(name);
        mdf.setDescription(description);
        getSession().save(mdf);
        log.debug("Created new MetaDataField");
    }

   
    public Collection getCollection(String name)
    {
        String hql = "from Collection where name = :name";
        Query q = getSession().createQuery(hql);
        q.setString("name", name);
        q.setMaxResults(1);
        @SuppressWarnings("unchecked")
        List<Collection> results = q.list();
        if (results.size() > 0)
        {
            return (results.get(0));
        }
        else
            return null;
    }
*/
    
    public void createEncodedData(EncodedData ed) {
    	Session session = getSession();
    	session.save(ed);
    	session.flush();
    	session.refresh(ed);
    	//TODO: flush() appears to guarantee upon return that the Blob will be in the database
    }
    
    public EncodedData getEncodedData(Long encodedDataId) {
    	EncodedData ed = (EncodedData)getSession().get(EncodedData.class, encodedDataId);
    	return ed;
    }
    
    public MarkerType getMarkerType(String typeName) {
        Criteria crit = getSession().createCriteria(MarkerType.class);
    	crit.add(Restrictions.eq("name", typeName));
        crit.addOrder(Order.asc("id"));
        //@SuppressWarnings("unchecked")
        List<MarkerType> mdtList = crit.list();
		if (mdtList.size() > 0) {
			if (mdtList.size() > 1) {
				log.error("Backend database has non-unique MarkerType names, returned the first one");
			}
			return (mdtList.get(0));
		}
        else
        	return null;
    }

	public void createMarker(Marker marker) {
    	Session session = getSession();
    	session.save(marker);
	}

}
