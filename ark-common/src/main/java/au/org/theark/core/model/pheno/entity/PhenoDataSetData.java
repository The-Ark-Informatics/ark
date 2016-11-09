package au.org.theark.core.model.pheno.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.IPhenoDataSetFieldData;

/**
 * @author nivedann
 *
 */

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "PHENO_DATASET_DATA", schema = Constants.PHENO_TABLE_SCHEMA)
public class PhenoDataSetData implements Serializable, IPhenoDataSetFieldData {
	private static final long serialVersionUID = 1L;
	private Long id;
	private PhenoDataSetFieldDisplay phenoDataSetFieldDisplay;
	private PhenoDataSetCollection phenoDataSetCollection;
	private String textDataValue;
	private Date dateDataValue;
	private Double numberDataValue;
	private String errorDataValue;
	
	public PhenoDataSetData(){
		
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
	@JoinColumn(name = "PHENO_DATASET_FIELD_DISPLAY_ID")
	public PhenoDataSetFieldDisplay getPhenoDataSetFieldDisplay() {
		return phenoDataSetFieldDisplay;
	}
	public void setPhenoDataSetFieldDisplay(
			PhenoDataSetFieldDisplay phenoDataSetFieldDisplay) {
		this.phenoDataSetFieldDisplay = phenoDataSetFieldDisplay;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHENO_DATASET_COLLECTION_ID")
	@NotAudited
	public PhenoDataSetCollection getPhenoDataSetCollection() {
		return phenoDataSetCollection;
	}

	public void setPhenoDataSetCollection(
			PhenoDataSetCollection phenoDataSetCollection) {
		this.phenoDataSetCollection = phenoDataSetCollection;
	}

	@Column(name = "TEXT_DATA_VALUE")
	public String getTextDataValue() {
		return textDataValue;
	}
	public void setTextDataValue(String textDataValue) {
		this.textDataValue = textDataValue;
	}

	@Override
	public PhenoDataSetData deepCopy() {
		PhenoDataSetData clone = new PhenoDataSetData();
		clone.setId(this.getId());
		clone.setDateDataValue(this.getDateDataValue());
        clone.setTextDataValue(this.getTextDataValue());
		clone.setNumberDataValue(this.getNumberDataValue());
		clone.setErrorDataValue(this.getErrorDataValue());
		return clone;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_DATA_VALUE")
	public Date getDateDataValue() {
		return dateDataValue;
	}


	public void setDateDataValue(Date dateDataValue) {
		this.dateDataValue = dateDataValue;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PhenoDataSetData that = (PhenoDataSetData) o;
		return Objects.equals(getId(), that.getId()) &&
				Objects.equals(getTextDataValue(), that.getTextDataValue()) &&
				Objects.equals(getDateDataValue(), that.getDateDataValue()) &&
				Objects.equals(getNumberDataValue(), that.getNumberDataValue()) &&
				Objects.equals(getErrorDataValue(), that.getErrorDataValue());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getPhenoDataSetFieldDisplay(), getPhenoDataSetCollection(), getTextDataValue(), getDateDataValue(), getNumberDataValue(), getErrorDataValue());
	}
}
