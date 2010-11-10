/**
 * 
 */
package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldType;

/**
 * @author cellis
 *
 */
@SuppressWarnings("serial")
public class FieldVO implements Serializable
{
	private Field field;
	private java.util.Collection<Field> fieldCollection;
	private FieldType fieldType;
	private int mode;
	
	public FieldVO(){
		field = new Field();
		fieldCollection = new ArrayList<Field>();
	}
	
	/**
	 * @return the field
	 */
	public Field getField()
	{
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(Field field)
	{
		this.field = field;
	}

	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(FieldType fieldType)
	{
		this.fieldType = fieldType;
	}

	/**
	 * @return the fieldType
	 */
	public FieldType getFieldType()
	{
		return fieldType;
	}

	public java.util.Collection<Field> getFieldCollection()
	{
		return fieldCollection;
	}

	public void setFieldCollection(java.util.Collection<Field> fieldCollection)
	{
		this.fieldCollection = fieldCollection;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public int getMode()
	{
		return mode;
	}
}
