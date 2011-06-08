/**
 * 
 */
package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.core.model.pheno.entity.Field;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class FieldVO implements Serializable
{
	private Field								field;
	private java.util.Collection<Field>	fieldCollection;
	private int									mode;

	public FieldVO()
	{
		field = new Field();
		setFieldCollection(new ArrayList<Field>());
	}

	/**
	 * @return the field
	 */
	public Field getField()
	{
		return field;
	}

	/**
	 * @param field
	 *           the field to set
	 */
	public void setField(Field field)
	{
		this.field = field;
	}

	/**
	 * @param fieldCollection
	 *           the fieldCollection to set
	 */
	public void setFieldCollection(java.util.Collection<Field> fieldCollection)
	{
		this.fieldCollection = fieldCollection;
	}

	/**
	 * @return the fieldCollection
	 */
	public java.util.Collection<Field> getFieldCollection()
	{
		return fieldCollection;
	}

	/**
	 * @param mode
	 *           the mode to set
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
