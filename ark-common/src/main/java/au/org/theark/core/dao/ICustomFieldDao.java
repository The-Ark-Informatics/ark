package au.org.theark.core.dao;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;

public interface ICustomFieldDao {

	CustomField getFieldByNameAndStudyAndFunction(String fieldName, Study study, ArkFunction arkFunction) throws EntityNotFoundException;

	FieldType getFieldTypeByName(String typeName) throws EntityNotFoundException;

}
