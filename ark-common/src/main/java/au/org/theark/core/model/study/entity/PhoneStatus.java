package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "PHONE_STATUS",  schema = Constants.STUDY_SCHEMA)
public class PhoneStatus implements Serializable{
	
	
	private Long id;
	private String name;
	private String description;
	
	public PhoneStatus(){
		
	}
	
	@Id
	@SequenceGenerator(name="phone_status_generator", sequenceName="PHONE_STATUS_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "phone_status_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME", nullable = false, length = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DESCRIPTION", nullable = false, length = 500)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
