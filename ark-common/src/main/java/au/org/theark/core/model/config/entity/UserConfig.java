package au.org.theark.core.model.config.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.ArkUser;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "USER_CONFIG", schema = Constants.CONFIG_SCHEMA)
public class UserConfig implements Serializable{
	
	private static final long	serialVersionUID	= 1L;
	private Long					id;
	private ArkUser 				arkUser;
	private ConfigField				configField;
	private String					value;

	
	public UserConfig() {
	}

	public UserConfig(Long id) {
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "user_config_generator", sequenceName = "USER_CONFIG_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_config_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public ArkUser getArkUser() {
		return arkUser;
	}

	public void setArkUser(ArkUser arkUser) {
		this.arkUser = arkUser;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FIELD_ID")
	public ConfigField getConfigField() {
		return configField;
	}

	public void setConfigField(ConfigField configField) {
		this.configField = configField;
	}

	@JoinColumn(name = "VALUE", nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Transient
	public int getIntValue() throws NumberFormatException {
		return new Integer(value).intValue();
	}

	@Override
	public String toString() {
		return "UserConfig [id=" + id + ", arkUser=" + arkUser
				+ ", configField=" + configField + ", value=" + value + "]";
	}	
	
	
}
