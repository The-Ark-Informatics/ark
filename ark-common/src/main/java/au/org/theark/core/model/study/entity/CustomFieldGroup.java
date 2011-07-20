package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * @author nivedann
 *
 */


@Entity
@Table(name = "CUSTOM_FIELD_GROUP", schema = Constants.STUDY_SCHEMA)
public class CustomFieldGroup implements Serializable{
	
	private Long	id;
	private String	name;
	private String	description;
	
	/**
	 * 
	 */
	public CustomFieldGroup(){
		
	}

	@Id
	@SequenceGenerator(name = "custom_field_group_seq_gen", sequenceName = "CUSTOM_FIELD_GROUP_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_group_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "NAME", length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
