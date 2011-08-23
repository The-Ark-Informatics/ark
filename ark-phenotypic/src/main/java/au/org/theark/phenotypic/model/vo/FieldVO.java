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
package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.core.model.pheno.entity.Field;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class FieldVO implements Serializable {
	private Field								field;
	private java.util.Collection<Field>	fieldCollection;
	private int									mode;

	public FieldVO() {
		field = new Field();
		setFieldCollection(new ArrayList<Field>());
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @param field
	 *           the field to set
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * @param fieldCollection
	 *           the fieldCollection to set
	 */
	public void setFieldCollection(java.util.Collection<Field> fieldCollection) {
		this.fieldCollection = fieldCollection;
	}

	/**
	 * @return the fieldCollection
	 */
	public java.util.Collection<Field> getFieldCollection() {
		return fieldCollection;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
}
