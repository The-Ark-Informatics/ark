package au.org.theark.report.model.entity;

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

import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.service.Constants;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "LINK_STUDY_REPORT_TEMPLATE", schema = Constants.REPORT_TABLE_SCHEMA)
public class LinkStudyReportTemplate implements java.io.Serializable
{

	// Fields
	private Long						id;
	private Study						study;
	private ReportTemplate				reportTemplate;

	// Constructors

	/** default constructor */
	public LinkStudyReportTemplate()
	{
	}

	/** minimal constructor */
	public LinkStudyReportTemplate(Long id, ReportTemplate reportTemplate)
	{
		this.id = id;
		this.reportTemplate = reportTemplate;
	}

	/**
	 * full constructor
	 * 
	 * @param decodeMasks
	 */
	public LinkStudyReportTemplate(Long id, Study study, ReportTemplate reportTemplate)
	{
		this.id = id;
		this.study = study;
		this.reportTemplate = reportTemplate;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "LinkStudyReportTemplate_PK_Seq", sequenceName = "REPORTING.LINK_STUDY_REPORT_TEMPLATE_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "LinkStudyReportTemplate_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORT_TEMPLATE_ID", nullable = false)
	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

}
