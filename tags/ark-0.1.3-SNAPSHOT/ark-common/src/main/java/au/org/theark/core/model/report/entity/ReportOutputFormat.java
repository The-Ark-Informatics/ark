package au.org.theark.core.model.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "REPORT_OUTPUT_FORMAT", schema = Constants.REPORT_TABLE_SCHEMA)
public class ReportOutputFormat implements java.io.Serializable
{

	// Fields
	private Long						id;
	private String						name;
	private String						description;

	// Constructors

	/** default constructor */
	public ReportOutputFormat()
	{
	}

	/** minimal constructor */
	public ReportOutputFormat(Long id, String name)
	{
		this.id = id;
		this.name = name;
	}

	/**
	 * full constructor
	 * 
	 * @param decodeMasks
	 */
	public ReportOutputFormat(Long id, String name, String description)
	{
		this.id = id;
		this.name = name;
		this.description = description;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "ReportOutputFormat_PK_Seq", sequenceName = "REPORTING.REPORT_OUTPUT_FORMAT_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ReportOutputFormat_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 45)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 250)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
