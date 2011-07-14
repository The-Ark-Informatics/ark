package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "ENTITY_TYPE", schema = Constants.STUDY_SCHEMA)
public class EntityType implements Serializable {
	
	
	private Long id;
	private String name;
	
	public EntityType(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
