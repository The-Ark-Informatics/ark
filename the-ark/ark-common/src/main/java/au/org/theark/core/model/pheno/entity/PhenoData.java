package au.org.theark.core.model.pheno.entity;

import java.io.Serializable;
import java.util.Date;

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

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.ICustomFieldData;

/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "PHENO_DATA", schema = Constants.PHENO_TABLE_SCHEMA)
public class PhenoData implements Serializable, ICustomFieldData {
	
	private Long id;
	private CustomFieldDisplay customFieldDisplay;
	private PhenotypicCollection phenotypicCollection;
	private String textDataValue;
	private Date dateDataValue;
	private Double numberDataValue;
	private String errorDataValue;
	
	/**
	 * Constructor
	 */
	public PhenoData(){
		
	}
	
	
	@Id																			
	@SequenceGenerator(name = "pheno_data_generator", sequenceName = "PHENO_DATA_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pheno_data_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_DISPLAY_ID")
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}


	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}


	@Column(name = "TEXT_DATA_VALUE")
	public String getTextDataValue() {
		return textDataValue;
	}


	public void setTextDataValue(String textDataValue) {
		this.textDataValue = textDataValue;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_DATA_VALUE")
	public Date getDateDataValue() {
		return dateDataValue;
	}


	public void setDateDataValue(Date dateDataValue) {
		this.dateDataValue = dateDataValue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_COLLECTION_ID")
	public PhenotypicCollection getPhenotypicCollection() {
		return phenotypicCollection;
	}


	public void setPhenotypicCollection(PhenotypicCollection phenotypicCollection) {
		this.phenotypicCollection = phenotypicCollection;
	}
	
	@Column(name="NUMBER_DATA_VALUE")
	public Double getNumberDataValue() {
		return numberDataValue;
	}

	public void setNumberDataValue(Double numberDataValue) {
		this.numberDataValue = numberDataValue;
	}

	@Column(name="ERROR_DATA_VALUE")
	public String getErrorDataValue() {
		return errorDataValue;
	}


	public void setErrorDataValue(String errorDataValue) {
		this.errorDataValue = errorDataValue;
	}

}
