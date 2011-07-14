package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * PersonContactMethod entity. 
 * @author cellis
 */
@Entity
@Table(name = "PERSON_CONTACT_METHOD", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class PersonContactMethod implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5632425806775287317L;
	// Fields
	private Long id;
	private String name;

	// Constructors

	/** default constructor */
	public PersonContactMethod() {
	}

	/** minimal constructor */
	public PersonContactMethod(Long id) {
		this.id = id;
	}

	/** full constructor */
	public PersonContactMethod(Long id, String name) {
		this.id = id;
		this.name = name;
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
	
	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}