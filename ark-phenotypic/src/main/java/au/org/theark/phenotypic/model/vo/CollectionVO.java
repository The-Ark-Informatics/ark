package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;

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
	
	/** A Collection of fields linked to this phenotypic collection*/
	protected java.util.Collection<Field> fieldCollection;
	
	/** A Colleciton of persons linked to this phenotypic collection*/
	protected java.util.Collection<Person> personCollection;
	
	/** A Collection of fieldData linked to this phenotypic collection*/
	protected java.util.Collection<FieldData> fieldDataCollection;
}
