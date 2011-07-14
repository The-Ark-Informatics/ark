package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * ActionType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACTION_TYPE", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class ActionType implements java.io.Serializable {

	// Fields
	private Long id;
	private String name;
	private String description;

	// Constructors

	/** default constructor */
	public ActionType() {
	}

	/** minimal constructor */
	public ActionType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ActionType(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	// Property accessors
	@Column(name = "NAME", unique = true, nullable = false, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}