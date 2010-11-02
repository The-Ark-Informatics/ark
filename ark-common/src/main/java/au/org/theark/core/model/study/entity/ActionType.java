package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * ActionType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACTION_TYPE", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class ActionType implements java.io.Serializable {

	// Fields

	private Long actionTypeKey;
	private String name;
	private String description;

	// Constructors

	/** default constructor */
	public ActionType() {
	}

	/** minimal constructor */
	public ActionType(Long actionTypeKey) {
		this.actionTypeKey = actionTypeKey;
	}

	/** full constructor */
	public ActionType(Long actionTypeKey, String name, String description) {
		this.actionTypeKey = actionTypeKey;
		this.name = name;
		this.description = description;
	}

	// Property accessors
	@Id
	@Column(name = "ACTION_TYPE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getActionTypeKey() {
		return this.actionTypeKey;
	}

	public void setActionTypeKey(Long actionTypeKey) {
		this.actionTypeKey = actionTypeKey;
	}

	@Column(name = "NAME", unique = true, length = 20)
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

}