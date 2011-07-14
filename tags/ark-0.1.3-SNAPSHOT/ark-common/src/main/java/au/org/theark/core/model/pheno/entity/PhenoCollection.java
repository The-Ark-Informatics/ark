package au.org.theark.core.model.pheno.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.Constants;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.phenotypic.model.entity.Collection")
@Table(name = "COLLECTION", schema = Constants.PHENO_TABLE_SCHEMA)
public class PhenoCollection implements java.io.Serializable
{
	// Fields
	private Long						id;
	private Status						status;
	private Study						study;
	private String						name;
	private String						description;
	private Date						startDate;
	private Date						endDate;
	private String						userId;
	private Date						insertTime;
	private String						updateUserId;
	private Date						updateTime;
	
	private Set<PhenoCollectionUpload> phenoCollectionUploads = new HashSet<PhenoCollectionUpload>(0);

	// Constructors

	/** default constructor */
	public PhenoCollection()
	{
	}

	/** minimal constructor */
	public PhenoCollection(Long id, Status status, Study study, String userId, Date insertTime)
	{
		this.id = id;
		this.status = status;
		this.study = study;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/**
	 * full constructor
	 * 
	 * @param decodeMasks
	 */
	public PhenoCollection(Long id, Status status, Study study, String name, String description, Date startDate, Date expiryDate, String userId, Date insertTime, String updateUserId, Date updateTime,
			Set<PhenoCollectionUpload> phenoCollectionUploads)
	{
		this.id = id;
		this.status = status;
		this.study = study;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = expiryDate;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.phenoCollectionUploads = phenoCollectionUploads;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "Collection_PK_Seq", sequenceName = "PHENOTYPIC.COLLECTION_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Collection_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	public Status getStatus()
	{
		return this.status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	/**
	 * @return the study
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy()
	{
		return study;
	}

	public void setStudy(Study study)
	{
		this.study = study;
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

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate()
	{
		return this.startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate()
	{
		return this.endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime()
	{
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime)
	{
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId()
	{
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId)
	{
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime()
	{
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	/**
	 * @param phenoCollectionUploads the phenoCollectionUploads to set
	 */
	public void setPhenoCollectionUploads(Set<PhenoCollectionUpload> phenoCollectionUploads)
	{
		this.phenoCollectionUploads = phenoCollectionUploads;
	}

	/**
	 * @return the phenoCollectionUploads
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<PhenoCollectionUpload> getPhenoCollectionUploads()
	{
		return phenoCollectionUploads;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhenoCollection other = (PhenoCollection) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
