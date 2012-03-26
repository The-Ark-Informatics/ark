package au.org.theark.core.model.study.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;	
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * @author travis endersby
 *
 */
@Entity
@Table(name = "MEASUREMENT_TYPE", schema = Constants.STUDY_SCHEMA)
public class MeasurementType {


	private Long			id;
	private Set<UnitType>	unitTypes	= new HashSet<UnitType>(0);
	private String			value;
	private String			description;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "VALUE", length = 45)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "DESCRIPTION", length=255)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "measurementType")
	public Set<UnitType> getUnitTypes() {
		return this.unitTypes;
	}

	public void setUnitTypes(Set<UnitType> unitTypes) {
		this.unitTypes = unitTypes;
	}
}
