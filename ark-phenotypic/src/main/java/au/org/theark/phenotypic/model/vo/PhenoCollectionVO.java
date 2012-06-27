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

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class PhenoCollectionVO implements Serializable {
/*	protected PhenoCollection								phenoCollection;
	protected Field											field;
	protected FieldType										fieldType;
	protected FieldData										fieldData;
	protected Person											person;
	protected Study											study;
	protected PhenoUpload									upload;
	protected Collection<Field>							fieldsAvailable;
	protected Collection<Field>							fieldsSelected;
	protected Collection<PhenoUpload>					uploadCollection;

	protected int												mode;

	/** A Collection of collections for the study in context *
	protected java.util.Collection<PhenoCollection>	phenoCollectionCollection;

	/** A Collection of fields linked to this phenotypic collection *
	protected java.util.Collection<Field>				fieldCollection;

	/** A Colleciton of persons linked to this phenotypic collection *
	protected java.util.Collection<Person>				personCollection;

	/** A Collection of fieldData linked to this phenotypic collection *
	protected java.util.Collection<FieldData>			fieldDataCollection;

	public PhenoCollectionVO() {
		this.phenoCollection = new PhenoCollection();
		this.phenoCollectionCollection = new ArrayList<PhenoCollection>();
		this.field = new Field();
		this.person = new Person();
		this.study = new Study();
		this.fieldData = new FieldData();
		this.fieldsAvailable = new ArrayList<Field>();
		this.fieldsSelected = new ArrayList<Field>();
		this.upload = new PhenoUpload();
		this.uploadCollection = new ArrayList<PhenoUpload>();
	}

	public PhenoCollection getPhenoCollection() {
		return phenoCollection;
	}

	public void setPhenoCollection(PhenoCollection phenoCollection) {
		this.phenoCollection = phenoCollection;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public FieldData getFieldData() {
		return fieldData;
	}

	public void setFieldData(FieldData fieldData) {
		this.fieldData = fieldData;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public java.util.Collection<Field> getFieldCollection() {
		return fieldCollection;
	}

	public void setFieldCollection(java.util.Collection<Field> fieldCollection) {
		this.fieldCollection = fieldCollection;
	}

	public java.util.Collection<Person> getPersonCollection() {
		return personCollection;
	}

	public void setPersonCollection(java.util.Collection<Person> personCollection) {
		this.personCollection = personCollection;
	}

	public java.util.Collection<FieldData> getFieldDataCollection() {
		return fieldDataCollection;
	}

	public void setFieldDataCollection(java.util.Collection<FieldData> fieldDataCollection) {
		this.fieldDataCollection = fieldDataCollection;
	}

	public java.util.Collection<PhenoCollection> getPhenoCollectionCollection() {
		return phenoCollectionCollection;
	}

	public void setPhenoCollectionCollection(java.util.Collection<PhenoCollection> phenoCollection) {
		this.phenoCollectionCollection = phenoCollection;
	}

	/**
	 * @param fieldsAvailable
	 *           the fieldsAvailable to set
	 *
	public void setFieldsAvailable(Collection<Field> fieldsAvailable) {
		this.fieldsAvailable = fieldsAvailable;
	}

	/**
	 * @return the fieldsAvailable
	 *
	public Collection<Field> getFieldsAvailable() {
		return fieldsAvailable;
	}

	/**
	 * @param fieldsSelected
	 *           the fieldsSelected to set
	 *
	public void setFieldsSelected(Collection<Field> fieldsSelected) {
		this.fieldsSelected = fieldsSelected;
	}

	/**
	 * @return the fieldsSelected
	 *
	public Collection<Field> getFieldsSelected() {
		return fieldsSelected;
	}

	/**
	 * @param uploadCol
	 *           the upload collection to set
	 *
	public void setUploadCollection(Collection<PhenoUpload> uploadCollection) {
		this.uploadCollection = uploadCollection;
	}

	/**
	 * @return the upload collection
	 *
	public Collection<PhenoUpload> getUploadCollection() {
		return uploadCollection;
	}

	/**
	 * @return the upload
	 *
	public PhenoUpload getUpload() {
		return upload;
	}

	/**
	 * @param upload
	 *           the upload to set
	 *
	public void setUpload(PhenoUpload upload) {
		this.upload = upload;
	}
	*/
}
