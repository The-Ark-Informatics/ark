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
@Table(name = "ARK_USER_ROLE", schema = Constants.STUDY_SCHEMA)
public class ArkUserRole implements Serializable{
	
	private Long id;
	private ArkRole arkRole;
	private ArkModule arkModule;
	private ArkUser arkUser;
	private Study study;
	
	public ArkUserRole(){
		
	}

	@Id
	@SequenceGenerator(name="user_role_generator", sequenceName="USER_ROLE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "user_role_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_ROLE_ID")
	public ArkRole getArkRole() {
		return arkRole;
	}

	public void setArkRole(ArkRole arkRole) {
		this.arkRole = arkRole;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_MODULE_ID")
	public ArkModule getArkModule() {
		return arkModule;
	}

	public void setArkModule(ArkModule arkModule) {
		this.arkModule = arkModule;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_USER_ID")
	public ArkUser getArkUser() {
		return arkUser;
	}

	public void setArkUser(ArkUser arkUser) {
		this.arkUser = arkUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arkModule == null) ? 0 : arkModule.hashCode());
		result = prime * result + ((arkRole == null) ? 0 : arkRole.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArkUserRole other = (ArkUserRole) obj;
		if (arkModule == null) {
			if (other.arkModule != null)
				return false;
		} else if (!arkModule.equals(other.arkModule))
			return false;
		if (arkRole == null) {
			if (other.arkRole != null)
				return false;
		} else if (!arkRole.equals(other.arkRole))
			return false;
		return true;
	}

}
