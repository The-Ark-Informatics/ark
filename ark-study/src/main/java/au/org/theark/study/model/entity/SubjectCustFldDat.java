package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SubjectCustFldDat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECT_CUST_FLD_DAT", schema = "ETA")
public class SubjectCustFldDat implements java.io.Serializable {

	// Fields

	private Long subjectCustFldDatKey;
	private SubjectCustmFld subjectCustmFld;
	private LinkSubjectStudy linkSubjectStudy;
	private String fieldData;

	// Constructors

	/** default constructor */
	public SubjectCustFldDat() {
	}

	/** minimal constructor */
	public SubjectCustFldDat(Long subjectCustFldDatKey) {
		this.subjectCustFldDatKey = subjectCustFldDatKey;
	}

	/** full constructor */
	public SubjectCustFldDat(Long subjectCustFldDatKey,
			SubjectCustmFld subjectCustmFld, LinkSubjectStudy linkSubjectStudy,
			String fieldData) {
		this.subjectCustFldDatKey = subjectCustFldDatKey;
		this.subjectCustmFld = subjectCustmFld;
		this.linkSubjectStudy = linkSubjectStudy;
		this.fieldData = fieldData;
	}

	// Property accessors
	@Id
	@Column(name = "SUBJECT_CUST_FLD_DAT_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getSubjectCustFldDatKey() {
		return this.subjectCustFldDatKey;
	}

	public void setSubjectCustFldDatKey(Long subjectCustFldDatKey) {
		this.subjectCustFldDatKey = subjectCustFldDatKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_CUSTM_FLD_KEY")
	public SubjectCustmFld getSubjectCustmFld() {
		return this.subjectCustmFld;
	}

	public void setSubjectCustmFld(SubjectCustmFld subjectCustmFld) {
		this.subjectCustmFld = subjectCustmFld;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_KEY")
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