package au.org.theark.report.model.dao;

import java.util.List;
import java.util.Map;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.model.vo.report.ConsentDetailsDataRow;
import au.org.theark.report.model.vo.report.FieldDetailsDataRow;


public interface IReportDao {

	public Integer getTotalSubjectCount(Study study);
	public Map<String, Integer> getSubjectStatusCounts(Study study);
	public Map<String, Integer> getStudyConsentCounts(Study study);
	public Map<String, Integer> getStudyCompConsentCounts(Study study, StudyComp studyComp);
	public Long getWithoutStudyCompCount(Study study);
	public List<ReportTemplate> getReportsForUser(ArkUser arkUser, Study study);
	public List<ReportOutputFormat> getOutputFormats();
	public List<LinkSubjectStudy> getStudyLevelConsentDetailsList(
			ConsentDetailsReportVO cdrVO);
	/**
	 * Gets a list of Consents for particular Study Component constrained by
	 * Consent Status/Date, but does not handle for "Not Consented".
	 * (i.e. not to be used to return a breakdown by Consent Status because
	 * that would require special handling for "Not Consented").
	 * It will return the following fields into a ConsentDetailsDataRow bean:
	 *  + subjectUID
	 *  + consentStatus
	 *  + consentDate
	 * @param cdrVO
	 * @return
	 */
	public List<ConsentDetailsDataRow> getStudyCompConsentList(
			ConsentDetailsReportVO cdrVO);
	
	public Address getBestAddress(LinkSubjectStudy subject);
	public Phone getWorkPhone(LinkSubjectStudy subject);
	public Phone getHomePhone(LinkSubjectStudy subject);
	
	/**
	 * Gets a list of LinkSubjectStudy records for a given study and is constrained on:
     *  + subjectUID
     *  + subjectStatus
	 * @param consent
	 * @return
	 */
	public List<LinkSubjectStudy> getSubjects(ConsentDetailsReportVO cdrVO);
	
	/**
	 * Gets the consent record for a given subject and studyComp.
	 * Does NOT constrain against consentStatus or consentDate here, because we want to be able to
	 * tell if they are "Not Consented" vs "Consented" with different consentStatus or consentDate.
	 * @param consent
	 * @return
	 */
	public Consent getStudyCompConsent(Consent consent);

	
	public List<PhenoCollection> getPhenoCollectionList(Study study);
	public List<FieldDetailsDataRow> getPhenoFieldDetailsList(
			FieldDetailsReportVO fdrVO);

}
