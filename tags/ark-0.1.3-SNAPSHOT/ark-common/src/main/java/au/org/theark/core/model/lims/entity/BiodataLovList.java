package au.org.theark.core.model.lims.entity;

// Generated 15/06/2011 1:22:58 PM by Hibernate Tools 3.3.0.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * BiodataLovList generated by hbm2java
 */
@Entity
@Table(name = "biodata_lov_list", schema = Constants.LIMS_TABLE_SCHEMA)
public class BiodataLovList implements java.io.Serializable
{

	private Long		id;
	private String	name;
	private String	description;

	public BiodataLovList()
	{
	}

	public BiodataLovList(Long id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public BiodataLovList(Long id, String name, String description)
	{
		this.id = id;
		this.name = name;
		this.description = description;
	}

	@Id
	@SequenceGenerator(name = "biodatalovlist_generator", sequenceName = "BIODATALOVLIST_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biodatalovlist_generator")
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}