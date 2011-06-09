package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;


/**
 * DelimiterType entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.common.model.study.entity.DelimiterType")
@Table(name = "DELIMITER_TYPE", schema = Constants.STUDY_SCHEMA)
public class DelimiterType implements java.io.Serializable
{

	// Fields
	private Long			id;
	private String			name;
	private char delimiterCharacter;

	// Constructors
	/** default constructor */
	public DelimiterType()
	{
	}

	/** minimal constructor */
	public DelimiterType(Long id)
	{
		this.id = id;
	}

	/** full constructor */
	public DelimiterType(Long id, String name)
	{
		this.id = id;
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
	
	@Column(name = "DELIMITER_CHARACTER")
	public char getDelimiterCharacter()
	{
		return delimiterCharacter;
	}

	public void setDelimiterCharacter(char delimiterCharacter)
	{
		this.delimiterCharacter = delimiterCharacter;
	}
}