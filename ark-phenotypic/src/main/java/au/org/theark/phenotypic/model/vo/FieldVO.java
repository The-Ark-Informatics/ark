/**
 * 
 */
package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.phenotypic.model.entity.Field;

/**
 * @author cellis
 *
 */
public class FieldVO implements Serializable
{
	private Field field;
	private java.util.Collection<Field> fieldCollection;
	
	public FieldVO(){
		field = new Field();
		fieldCollection = new ArrayList<Field>();
	}

	public Field getField()
	{
		return field;
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public java.util.Collection<Field> getFieldCollection()
	{
		return fieldCollection;
	}

	public void setFieldCollection(java.util.Collection<Field> fieldCollection)
	{
		this.fieldCollection = fieldCollection;
	}
}
