package au.org.theark.core.model.lims.entity;

// Generated Jun 14, 2011 3:39:29 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Biodata generated by hbm2java
 */
@Entity
@Table(name = "biodata", catalog = "lims")
public class Biodata implements java.io.Serializable {

	private int id;
	private BiodataField biodataField;
	private Integer domainId;
	private Date dateCollected;
	private String stringValue;
	private Integer numberValue;
	private Date dateValue;

	public Biodata() {
	}

	public Biodata(int id, BiodataField biodataField, Date dateCollected) {
		this.id = id;
		this.biodataField = biodataField;
		this.dateCollected = dateCollected;
	}

	public Biodata(int id, BiodataField biodataField, Integer domainId,
			Date dateCollected, String stringValue, Integer numberValue,
			Date dateValue) {
		this.id = id;
		this.biodataField = biodataField;
		this.domainId = domainId;
		this.dateCollected = dateCollected;
		this.stringValue = stringValue;
		this.numberValue = numberValue;
		this.dateValue = dateValue;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_ID", nullable = false)
	public BiodataField getBiodataField() {
		return this.biodataField;
	}

	public void setBiodataField(BiodataField biodataField) {
		this.biodataField = biodataField;
	}

	@Column(name = "DOMAIN_ID")
	public Integer getDomainId() {
		return this.domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_COLLECTED", nullable = false, length = 19)
	public Date getDateCollected() {
		return this.dateCollected;
	}

	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}

	@Column(name = "STRING_VALUE", length = 65535)
	public String getStringValue() {
		return this.stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Column(name = "NUMBER_VALUE")
	public Integer getNumberValue() {
		return this.numberValue;
	}

	public void setNumberValue(Integer numberValue) {
		this.numberValue = numberValue;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_VALUE", length = 19)
	public Date getDateValue() {
		return this.dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

}
