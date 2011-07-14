/**
 * 
 */
package au.org.theark.core.model.pheno.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;

/**
 * @author cellis
 *
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "FIELD_GROUP", schema = Constants.PHENO_TABLE_SCHEMA)
public class FieldGroup
{
	// Fields
	private Long id;
	private String name;
	private String description;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;
	
	// Constructors
	/** default constructor */
	public FieldGroup() {
	}

	/** minimal constructor */
	public FieldGroup(Long id, String name, String userId, Date insertTime) {
		this.id = id;
		this.name = name;
		this.userId = userId;
		this.insertTime = insertTime;
	}
	
	/** full constructor */
	public FieldGroup(Long id, String name, String description, String userId, Date insertTime, String updateUserId, Date updateTime) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}
	
	// Property accessors
	@Id
	@SequenceGenerator(name="Field_Group_PK_Seq",sequenceName="PHENOTYPIC.FIELD_GROUP_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="Field_Group_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
   @Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
