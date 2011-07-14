package au.org.theark.core.model.pheno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * MarkerType entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FIELD_TYPE", schema = Constants.PHENO_TABLE_SCHEMA)
public class FieldType implements java.io.Serializable
{

	// Fields
	private Long	id;
	private String	name;

	// Constructors

	/** default constructor */
	public FieldType()
	{
	}

	/** minimal constructor */
	public FieldType(Long id)
	{
		this.id = id;
	}

	/** full constructor */
	public FieldType(Long id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public FieldType(String name)
	{
		this.name = name;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}