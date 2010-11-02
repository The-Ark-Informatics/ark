package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.List;

import au.org.theark.core.model.study.entity.Person;
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;

/**
 * @author cellis
 *
 */
@SuppressWarnings("serial")
public class CollectionVO implements Serializable
{
	protected Collection collection;
	protected Field field;
	protected FieldType fieldType;
	protected FieldData fieldData;
	protected Person Person;
	
	/** A List of fields linked to this phenotypic collection*/
	protected List<Field> fieldList;
	
	/** A List of persons linked to this phenotypic collection*/
	protected List<Person> personList;
	
	/** A List of fieldData linked to this phenotypic collection*/
	protected List<FieldData> fieldDataList;
}
