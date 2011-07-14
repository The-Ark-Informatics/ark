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

@Entity
@Table(name = "ARK_ROLE_PERMISSION", schema = Constants.STUDY_SCHEMA)
public class ArkRolePermission implements Serializable{

	private Long id;
	private ArkRole arkRole;
	private ArkPermission arkPermission;
	
	public ArkRolePermission(){
		
	}

	@Id
	@SequenceGenerator(name="role_permission_generator", sequenceName="ARK_ROLE_PERMISSION_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "role_permission_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_ROLE_ID")
	public ArkRole getArkRole() {
		return arkRole;
	}

	public void setArkRole(ArkRole arkRole) {
		this.arkRole = arkRole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_PERMISSION_ID")
	public ArkPermission getArkPermission() {
		return arkPermission;
	}

	public void setArkPermission(ArkPermission arkPermission) {
		this.arkPermission = arkPermission;
	}

}
