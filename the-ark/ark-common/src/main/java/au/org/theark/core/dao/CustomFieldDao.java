package au.org.theark.core.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;

@Repository("customFieldDao")
public class CustomFieldDao extends HibernateSessionDao implements ICustomFieldDao {

	static Logger		log	= LoggerFactory.getLogger(CustomFieldDao.class);

	public CustomField getFieldByNameAndStudyAndFunction(String fieldName, Study study, ArkFunction arkFunction) throws EntityNotFoundException {
		CustomField field = null;
		Criteria criteria = getSession().createCriteria(CustomField.class);
		if (fieldName != null && study != null && arkFunction != null) {
			criteria.add(Restrictions.eq("name", fieldName));
			criteria.add(Restrictions.eq("study", study));
			criteria.add(Restrictions.eq("arkFunction", arkFunction));
		}
		
		field = (CustomField) criteria.uniqueResult();	// should not have more than on field called the same name
		if (field == null) {
			throw new EntityNotFoundException();
		}

		return field;
	}

	public FieldType getFieldTypeByName(String typeName) throws EntityNotFoundException {
		FieldType fieldType = null;
		Criteria criteria = getSession().createCriteria(FieldType.class);
		criteria.add(Restrictions.eq("name", typeName));

		fieldType = (FieldType) criteria.uniqueResult();	// should not have more than on field called the same name
		if (fieldType == null) {
			log.error("Field Type Table maybe out of synch. Please check if it has an entry for " + typeName + " status");
			log.error("Cannot locate a field type with " + typeName + " in the database");
			throw new EntityNotFoundException();
		}
		return fieldType;
	}

}
