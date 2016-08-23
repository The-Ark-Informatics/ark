package au.org.theark.core.model.config.entity;

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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.FieldType;

@Entity
@Table(name = "CONFIG_FIELDS", schema = Constants.CONFIG_SCHEMA)
public class ConfigField implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private FieldType type;
	private String default_value;
	
	public ConfigField() {
	}
	
	public ConfigField(Long id) {
		this.id = id;
	}
	
	public ConfigField(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	@Id
	@SequenceGenerator(name = "config_fields_generator", sequenceName = "CONFIG_FIELDS_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "config_fields_generator")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@JoinColumn(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JoinColumn(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TYPE", nullable = false)
	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}
	
	@Column(name = "DEFAULT_VALUE")
	public String getDefaultValue() {
		return default_value;
	}
	
	public void setDefaultValue(String default_value) {
		this.default_value = default_value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ConfigField other = (ConfigField) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConfigField [id=" + id + ", name=" + name + ", description="
				+ description + ", type=" + type + ", default_value=" + default_value + "]";
	}
	
}