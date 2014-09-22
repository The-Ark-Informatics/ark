package au.org.theark.core.model.disease.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.ICustomFieldData;

@Entity
@Table(name = "AFFECTION_CUSTOM_FIELD_DATA", schema = Constants.DISEASE_SCHEMA)
public class AffectionCustomFieldData implements Serializable, ICustomFieldData {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Affection affection;
	private CustomFieldDisplay customFieldDisplay;
	private String textDataValue;
	private Date dateDataValue;
	private String errorDataValue;
	private Double numberDataValue;

	public AffectionCustomFieldData() {

	}

	@Id
	@SequenceGenerator(name = "affection_custom_field_data_generator", sequenceName = "AFFECTION_CUSTOM_FIELD_DATA_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "affection_custom_field_data_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AFFECTION_ID")
	public Affection getAffection() {
		return this.affection;
	}

	public void setAffection(Affection affection) {
		this.affection = affection;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CUSTOM_FIELD_DISPLAY_ID")
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_DATA_VALUE")
	public Date getDateDataValue() {
		return dateDataValue;
	}

	public void setDateDataValue(Date dateDataValue) {
		this.dateDataValue = dateDataValue;
	}

	@Column(name = "ERROR_DATA_VALUE")
	public String getErrorDataValue() {
		return errorDataValue;
	}

	public void setErrorDataValue(String errorDataValue) {
		this.errorDataValue = errorDataValue;
	}

	@Column(name = "NUMBER_DATA_VALUE")
	public Double getNumberDataValue() {
		return numberDataValue;
	}

	public void setNumberDataValue(Double numberDataValue) {
		this.numberDataValue = numberDataValue;
	}

	@Column(name = "TEXT_DATA_VALUE")
	public String getTextDataValue() {
		return textDataValue;
	}

	public void setTextDataValue(String textDataValue) {
		this.textDataValue = textDataValue;
	}

	@Override
	public String toString() {
		return "AffectionCustomFieldData [id=" + id + ", affection="
				+ affection.getId() + ", customFieldDisplay=" + customFieldDisplay
				+ ", textDataValue=" + textDataValue + ", dateDataValue="
				+ dateDataValue + ", errorDataValue=" + errorDataValue
				+ ", numberDataValue=" + numberDataValue + "]";
	}
	
	
}
