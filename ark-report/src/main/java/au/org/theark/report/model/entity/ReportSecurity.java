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
import au.org.theark.report.service.Constants;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.reporting.model.entity.ReportSecurity")
@Table(name = "REPORT_SECURITY", schema = Constants.TABLE_SCHEMA)
public class ReportSecurity implements java.io.Serializable
{

	// Fields
	private Long						id;
	private ArkUser						arkUser;
	private LinkStudyReportTemplate		linkStudyReportTemplate;

	// Constructors

	/** default constructor */
	public ReportSecurity()
	{
	}

	/** minimal constructor */
	public ReportSecurity(Long id, LinkStudyReportTemplate linkStudyReportTemplate)
	{
		this.id = id;
		this.linkStudyReportTemplate = linkStudyReportTemplate;
	}

	/**
	 * full constructor
	 * 
	 * @param decodeMasks
	 */
	public ReportSecurity(Long id, ArkUser arkUser, LinkStudyReportTemplate linkStudyReportTemplate)
	{
		this.id = id;
		this.arkUser = arkUser;
		this.linkStudyReportTemplate = linkStudyReportTemplate;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "ReportSecurity_PK_Seq", sequenceName = "REPORTING.REPORT_SECURITY_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ReportSecurity_PK_Seq")
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
	@JoinColumn(name = "ARK_USER_ID")
	public ArkUser getArkUser() {
		return arkUser;
	}

	public void setState(ArkUser arkUser) {
		this.arkUser = arkUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_STUDY_REPORT_TEMPLATE_ID", nullable = false)
	public LinkStudyReportTemplate getLinkStudyReportTemplate() {
		return linkStudyReportTemplate;
	}

	public void setLinkStudyReportTemplate(LinkStudyReportTemplate linkStudyReportTemplate) {
		this.linkStudyReportTemplate = linkStudyReportTemplate;
	}

}
