package au.org.theark.core.model.report.entity;

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
@Table(name = "demographic_field", schema = Constants.REPORT_SCHEMA)
public class DemographicField {
	//the alternative being to hit the mysql system tables somehow with a defined list of which fields in certain tables
	
	private Long id;
	//I almost wonder if I should state what the BASE entity is we start from, ALSO? as some start from LSS some start from PERSON
	private String entity;					//eg; person.genderType
	private String fieldName;				//eg; name     the 'name' field of the gender type table 
//	private String additionalHQLConstraint; //eg; "address.addressType = 'Residential'"
	//could potentially store table name and table field name too.  But first attempt will be at using hql and entities.
	private String publicFieldName;			//eg; Gender
	private FieldType fieldType;			//eg; String (note the field type of the output/final field)
	//private boolean hasDropDown
	//private hqlForDropDown/Multichoice
	


	@Id
	@SequenceGenerator(name = "demographic_field_generator", sequenceName = "DEMOGRAPHIC_FIELD_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "demographic_field_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ENTITY", length = 255)
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}


	@Column(name = "FIELD_NAME", length = 255)
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "PUBLIC_FIELD_NAME", length = 255)
	public String getPublicFieldName() {
		return publicFieldName;
	}
	public void setPublicFieldName(String publicFieldName) {
		this.publicFieldName = publicFieldName;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_TYPE_ID")
	public FieldType getFieldType() {
		return fieldType;
	}
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
	
}
