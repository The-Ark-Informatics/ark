package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * SubjectCustFldDat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECT_CUST_FLD_DAT", schema = Constants.STUDY_SCHEMA)
public class SubjectCustFldDat implements java.io.Serializable {

	// Fields

	private Long id;
	private SubjectCustmFld subjectCustmFld;
	private LinkSubjectStudy linkSubjectStudy;
	private String fieldData;

	// Constructors

	/** default constructor */
	public SubjectCustFldDat() {
	}

	/** minimal constructor */
	public SubjectCustFldDat(Long id) {
		this.id = id;
	}

	/** full constructor */
	public SubjectCustFldDat(Long id,
			SubjectCustmFld subjectCustmFld, LinkSubjectStudy linkSubjectStudy,
			String fieldData) {
		this.id = id;
		this.subjectCustmFld = subjectCustmFld;
		this.linkSubjectStudy = linkSubjectStudy;
		this.fieldData = fieldData;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_CUSTM_FLD_ID")
	public SubjectCustmFld getSubjectCustmFld() {
		return this.subjectCustmFld;
	}

	public void setSubjectCustmFld(SubjectCustmFld subjectCustmFld) {
		this.subjectCustmFld = subjectCustmFld;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return this.linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@Column(name = "FIELD_DATA", length = 4000)
	public String getFieldData() {
		return this.fieldData;
	}

	public void setFieldData(String fieldData) {
		this.fieldData = fieldData;
	}

}