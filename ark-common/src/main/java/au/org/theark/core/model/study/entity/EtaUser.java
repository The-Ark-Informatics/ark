package au.org.theark.core.model.study.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import au.org.theark.core.Constants;



@Entity
@Table(name="ETA_USER", schema=Constants.STUDY_SCHEMA)
public class EtaUser implements Serializable{
	
	private Long id;
	private String userName;
	private String userPassword;
	private Set<UserRoles> userRoles = new HashSet<UserRoles>();
	
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	@Column(name="USER_NAME", length=100)
	public String getUserName() {
		return userName;
	}
	
	@Column(name="USER_PASSWORD", length=250)
	public String getUserPassword() {
		return userPassword;
	}

    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "etaUser")
	public Set<UserRoles> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}
   

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	/**
	 * Default constructor
	 */
	public EtaUser(){
		
	}	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Name ");
		sb.append(this.getUserName());
		sb.append("Id");
		sb.append(this.getId());
		return sb.toString();
		
	}

}
