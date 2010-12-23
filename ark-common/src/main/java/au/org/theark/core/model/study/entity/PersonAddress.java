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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * Provides a mechanism to associate a person with more than one address(home,work etc).
 * @author nivedann
 *
 */
@Entity
@Table(name = "PERSON_ADDRESS", schema = Constants.STUDY_SCHEMA)
public class PersonAddress implements Serializable {
	
	private Long id;
	private Address address;
	private Person person;
	
	/**
	 * Constructor 
	 */
	public PersonAddress(){
		
	}
	
	/**
	 * Constructor
	 * @param id
	 * @param person
	 * @param address
	 */
	public PersonAddress(Long id, Person person, Address address){
		this.id = id;
		this.address = address;
		this.person = person;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDRESS_ID")
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	

}
