package au.org.theark.phenotypic.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.PhenoCollection;

/**
 * @author cellis
 *
 */
@SuppressWarnings("serial")
public class PhenoCollectionVO implements Serializable
{
	protected PhenoCollection phenoCollection;
	protected Field field;
	protected FieldType fieldType;
	protected FieldData fieldData;
	protected Person person;
	protected Study study;
	protected int mode;
	
	/** A Collection of collections for the study in context*/
	protected java.util.Collection<PhenoCollection> phenoCollectionCollection;

	/** A Collection of fields linked to this phenotypic collection*/
	protected java.util.Collection<Field> fieldCollection;
	
	/** A Colleciton of persons linked to this phenotypic collection*/
	protected java.util.Collection<Person> personCollection;
	
	/** A Collection of fieldData linked to this phenotypic collection*/
	protected java.util.Collection<FieldData> fieldDataCollection;
	
	public PhenoCollectionVO(){
		this.phenoCollection = new PhenoCollection();
		this.phenoCollectionCollection = new ArrayList<PhenoCollection>();
		this.field = new Field();
		this.person = new Person();
		this.study = new Study();
	}
	
	public PhenoCollection getPhenoCollection()
	{
		return phenoCollection;
	}

	public void setPhenoCollection(PhenoCollection phenoCollection)
	{
		this.phenoCollection = phenoCollection;
	}

	public Field getField()
	{
		return field;
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public FieldType getFieldType()
	{
		return fieldType;
	}

	public void setFieldType(FieldType fieldType)
	{
		this.fieldType = fieldType;
	}

	public FieldData getFieldData()
	{
		return fieldData;
	}

	public void setFieldData(FieldData fieldData)
	{
		this.fieldData = fieldData;
	}

	public Person getPerson()
	{
		return person;
	}

	public void setPerson(Person person)
	{
		this.person = person;
	}

	public Study getStudy()
	{
		return study;
	}

	public void setStudy(Study study)
	{
		this.study = study;
	}

	public int getMode()
	{
		return mode;
	}

	public void setMode(int mode)
	{
		this.mode = mode;
	}

	public java.util.Collection<Field> getFieldCollection()
	{
		return fieldCollection;
	}

	public void setFieldCollection(java.util.Collection<Field> fieldCollection)
	{
		this.fieldCollection = fieldCollection;
	}

	public java.util.Collection<Person> getPersonCollection()
	{
		return personCollection;
	}

	public void setPersonCollection(java.util.Collection<Person> personCollection)
	{
		this.personCollection = personCollection;
	}

	public java.util.Collection<FieldData> getFieldDataCollection()
	{
		return fieldDataCollection;
	}

	public void setFieldDataCollection(java.util.Collection<FieldData> fieldDataCollection)
	{
		this.fieldDataCollection = fieldDataCollection;
	}
	
	public java.util.Collection<PhenoCollection> getPhenoCollectionCollection()
	{
		return phenoCollectionCollection;
	}

	public void setPhenoCollectionCollection(java.util.Collection<PhenoCollection> phenoCollection)
	{
		this.phenoCollectionCollection = phenoCollection;
	}
}
