package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "FIELD_TYPE", schema = Constants.STUDY_SCHEMA)
public class FieldType implements Serializable{
	
	private Long	id;
	private String	name;
	
	/**
	 * Constructor
	 */
	public FieldType(){
		
	}
	
	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
