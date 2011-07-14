/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.model.study.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "VITAL_STATUS", schema = Constants.STUDY_SCHEMA)
public class VitalStatus implements Serializable{

	//Fields
	private Long id;


	private String name;
	private String description;
	private Set<Person> persons = new HashSet<Person>(0);
	
	public VitalStatus(){
		
	}
	
	public VitalStatus(Long id){
		this.id = id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	@Id
	@Column(name="ID",unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId(){
		return this.id;
	}
	
	@Column(name = "NAME", length = 25)
	public String getName() {
		return name;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "vitalStatus")
	public Set<Person> getPersons(){
		return this.persons;
	}
	
	public void setPersons(Set<Person> persons){
		this.persons = persons;
	}
	
	
}
