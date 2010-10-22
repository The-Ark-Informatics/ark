package au.org.theark.phenotypic.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.Status;

// TODO: Replace all hardcoded userIds with actual code from SecurityManager
// See SearchStudyFrom.java in ark-common
//Subject currentUser = SecurityUtils.getSubject();
@SuppressWarnings("unchecked")
@Repository("phenotypicDao")
public class PhenotypicDao extends HibernateSessionDao implements IPhenotypicDao
{

	static Logger	log	= LoggerFactory.getLogger(PhenotypicDao.class);

	public List<Collection> getCollectionMatches(Collection colExample)
	{
		Criteria colCriteria = getSession().createCriteria(Collection.class);

		if (colExample.getId() != null)
		{
			colCriteria.add(Restrictions.eq("id", colExample.getId()));
		}

		if (colExample.getName() != null)
		{
			colCriteria.add(Restrictions.ilike("name", colExample.getName(), MatchMode.ANYWHERE));
		}

		if (colExample.getStudyId() != null)
		{
			colCriteria.add(Restrictions.eq("studyId", colExample.getStudyId()));
		}

		if (colExample.getInsertTime() != null)
		{
			colCriteria.add(Restrictions.eq("insertTime", colExample.getInsertTime()));
		}

		if (colExample.getUserId() != null)
		{
			colCriteria.add(Restrictions.ilike("userId", colExample.getUserId(), MatchMode.ANYWHERE));
		}

		if (colExample.getUpdateTime() != null)
		{
			colCriteria.add(Restrictions.eq("updateTime", colExample.getUpdateTime()));
		}

		if (colExample.getUpdateUserId() != null)
		{
			colCriteria.add(Restrictions.ilike("updateUserId", colExample.getUpdateUserId(), MatchMode.ANYWHERE));
		}

		if (colExample.getStatus() != null)
		{
			colCriteria.add(Restrictions.eq("status", colExample.getStatus()));
		}

		colCriteria.addOrder(Order.asc("name"));
		List<Collection> colList = colCriteria.list();
		return colList;
	}

	public void createCollection(Collection col)
	{
		getSession().save(col);
	}

	public Collection getCollection(Long id)
	{

		Collection col = (Collection) getSession().get(Collection.class, id);
		return col;
	}

	public void updateCollection(Collection colEntity)
	{
		getSession().update(colEntity);
	}

	public void createField(Field f)
	{
		getSession().save(f);
	}

	public void updateField(Field f)
	{
		getSession().update(f);
	}

	public Status getStatusByName(String statusName)
	{
		Criteria crit = getSession().createCriteria(Status.class);
		crit.add(Restrictions.eq("name", statusName));
		crit.addOrder(Order.asc("id"));
		List<Status> statusList = crit.list();
		if (statusList.size() > 0)
		{
			if (statusList.size() > 1)
			{
				log.error("Backend database has non-unique Status names, returned the first one");
			}
			return (statusList.get(0));
		}
		else
			return null;
	}

	// TODO: Patch the Long studyId with a true study object?
	// (it should already be in context)
	public Field getFieldByName(Long studyId, String fieldName)
	{
		Criteria crit = getSession().createCriteria(Field.class);
		crit.add(Restrictions.eq("name", fieldName));
		crit.add(Restrictions.eq("studyId", studyId));
		crit.addOrder(Order.asc("id"));
		List<Field> fieldList = crit.list();
		if (fieldList.size() > 0)
		{
			if (fieldList.size() > 1)
			{
				log.error("Backend database has non-unique field names, returned the first one");
			}
			return (fieldList.get(0));
		}
		else
			return null;
	}

	// TODO: Patch the Long studyId with a true study object?
	// (it should already be in context)
	public Field getField(Long fieldId)
	{
		Field f = (Field) getSession().get(Field.class, fieldId);
		return f;
	}

	public FieldType getMetaDataTypeByName(String typeName)
	{
		Criteria crit = getSession().createCriteria(FieldType.class);
		crit.add(Restrictions.eq("name", typeName));
		crit.addOrder(Order.asc("id"));
		
		List<FieldType> fieldTypeList = crit.list();
		if (fieldTypeList.size() > 0)
		{
			if (fieldTypeList.size() > 1)
			{
				log.error("Backend database has non-unique field type names, returned the first one");
			}
			return (fieldTypeList.get(0));
		}
		else
			return null;
	}

	public void createCollectionImport(CollectionImport colImport)
	{
		getSession().save(colImport);
	}

	public FieldType getFieldTypeByName(String studyId, String fieldTypeName)
	{
		Criteria crit = getSession().createCriteria(FieldType.class);
		crit.add(Restrictions.eq("name", fieldTypeName));
		crit.add(Restrictions.eq("studyId", studyId));
		crit.addOrder(Order.asc("id"));
		List<FieldType> fieldList = crit.list();
		if (fieldList.size() > 0)
		{
			if (fieldList.size() > 1)
			{
				log.error("Backend database has non-unique field names, returned the first one");
			}
			return (fieldList.get(0));
		}
		else
			return null;
	}

	public void updateFieldData(FieldData fieldData)
	{
		getSession().update(fieldData);
	}

	public void createFieldData(FieldData fieldData)
	{
		getSession().save(fieldData);
	}

	public void createFieldType(FieldType fieldType)
	{
		// TODO Auto-generated method stub
		
	}

	public FieldData getFieldDataByName(String field)
	{
		// TODO Auto-generated method stub
		return null;
	}	

	public void updateFieldType(FieldType fieldType)
	{
		// TODO Auto-generated method stub
	}

	public Field getFieldByName(String fieldName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public FieldType getFieldTypeByName(String fieldTypeName)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
