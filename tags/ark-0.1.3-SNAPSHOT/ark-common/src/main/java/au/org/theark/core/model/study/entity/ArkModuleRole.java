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
@Table(name = "ARK_MODULE_ROLE", schema = Constants.STUDY_SCHEMA)
public class ArkModuleRole implements Serializable{
	
	private Long id;
	private ArkModule arkModule;
	private ArkRole arkRole;
	
	public ArkModuleRole(){
		
	}
	
	@Id
	@SequenceGenerator(name="ark_module_role_generator", sequenceName="ARK_MODULE_ROLE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "ark_module_role_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_MODULE_ID")	
	public ArkModule getArkModule() {
		return arkModule;
	}
	public void setArkModule(ArkModule arkModule) {
		this.arkModule = arkModule;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARK_ROLE_ID")	
	public ArkRole getArkRole() {
		return arkRole;
	}
	public void setArkRole(ArkRole arkRole) {
		this.arkRole = arkRole;
	}
	

}
