package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.List;

import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.Subject;

/**
 * @author cellis
 *
 */
@SuppressWarnings("serial")
public class CollectionVO implements Serializable
{
	protected Collection collection;
	protected Field field;
	
	//TODO: Utilise au.org.theark.core.Subject;
	protected Subject subject;
	
	/** A List of fields linked to this phenotypic collection*/
	protected List<Field> fieldList;
	
	/** A List of subjects linked to this phenotypic collection*/
	protected List<Subject> subjectList;
	
}
