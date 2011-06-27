package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "ARK_USER", schema = Constants.STUDY_SCHEMA)
public class ArkUser implements Serializable{

	private Long id;
	private String ldapUserName;
	
	
	public ArkUser(){
	}

	@Id
	@SequenceGenerator(name="ark_user_generator", sequenceName="ARK_USER_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "ark_user_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "LDAP_USER_NAME")
	public String getLdapUserName() {
		return ldapUserName;
	}

	public void setLdapUserName(String ldapUserName) {
		this.ldapUserName = ldapUserName;
	}

}
