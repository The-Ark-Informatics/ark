package au.org.theark.report.service;

import java.util.List;
import java.util.Map;

import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportTemplate;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.web.component.viewReport.consentDetails.ConsentDetailsDataRow;


public interface IReportService {

	public Integer getTotalSubjectCount(Study study);
	public Map<String, Integer> getSubjectStatusCounts(Study study);
	public Map<String, Integer> getStudyConsentCounts(Study study);
	public Map<String, Integer> getStudyCompConsentCounts(Study study, StudyComp studyComp);
	public Long getWithoutStudyCompCount(Study study);
	// TODO: Revise getReportsAvailableList method when migration to new Ark security done
	public List<ReportTemplate> getReportsAvailableList(ArkUser arkUser);
	public List<ReportOutputFormat> getOutputFormats();
	public List<ConsentDetailsDataRow> getConsentDetailsList(ConsentDetailsReportVO cdrVO, boolean onlyStudyLevelConsent);

}
