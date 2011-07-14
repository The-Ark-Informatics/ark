package au.org.theark.report.model.vo;

import java.util.Date;

import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.StudyComp;

public class ConsentDetailsReportVO extends GenericReportViewVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected LinkSubjectStudy linkSubjectStudy;
	protected ConsentStatus consentStatus;
	protected Date consentDate;
	protected StudyComp studyComp;
	
	public ConsentDetailsReportVO() {
		this.linkSubjectStudy = new LinkSubjectStudy();
	}
	
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}
	
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}
	
	public ConsentStatus getConsentStatus() {
		return consentStatus;
	}
	
	public void setConsentStatus(ConsentStatus consentStatus) {
		this.consentStatus = consentStatus;
	}
	
	public Date getConsentDate() {
		return consentDate;
	}
	
	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}
	
	public StudyComp getStudyComp() {
		return studyComp;
	}
	
	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
	}
	
}
