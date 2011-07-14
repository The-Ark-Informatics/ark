/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "MARITAL_STATUS", schema = Constants.STUDY_SCHEMA)
public class MaritalStatus implements Serializable {
	
	private Long id;
	private String name;
	private String description;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DESCRIPTION", length = 255)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
