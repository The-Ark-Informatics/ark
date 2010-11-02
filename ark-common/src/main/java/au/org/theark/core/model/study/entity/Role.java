package au.org.theark.core.model.study.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CollectionOfElements;


@Entity
@Table(name="ROLE")
public class Role implements Serializable{
	
	private Long id;
	private String name;
	private String description;
	private Set<UserRoles> userRoleses = new HashSet<UserRoles>(0);
	/**
	 * Mandatory for hibernate a no arg constructor
	 */
	protected Role() {
 
	}
	 
	 
	public Role(String name) {
	        this.name = name;
	}


	@Id
	@Column(name="id", unique=true, nullable=false,precision=22,scale=0)
	public long getId(){
		return id;
	}
	
	@Column(name="name",length=100)
	public String getName() {
		return name;
	}
	
	@Column(name="description",length=255 )
	public String getDescription() {
		return description;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
	public Set<UserRoles> getUserRoleses() {
		return this.userRoleses;
	}

	public void setUserRoleses(Set<UserRoles> userRoleses) {
		this.userRoleses = userRoleses;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
