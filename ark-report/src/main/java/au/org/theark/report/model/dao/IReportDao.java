package au.org.theark.report.model.dao;

import java.util.List;
import java.util.Map;

import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportSecurity;
import au.org.theark.report.model.entity.ReportTemplate;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.web.component.viewReport.consentDetails.ConsentDetailsDataRow;


public interface IReportDao {

	public Study getStudy(Long id);
	public List<ReportSecurity> getReportSecurity(Study study, ArkUser arkUser);
	public Integer getTotalSubjectCount(Study study);
	public Map<String, Integer> getSubjectStatusCounts(Study study);
	public Map<String, Integer> getStudyConsentCounts(Study study);
	public Map<String, Integer> getStudyCompConsentCounts(Study study, StudyComp studyComp);
	public Long getWithoutStudyCompCount(Study study);
	public List<ReportTemplate> getReportsForUser(ArkUser arkUser);
	public List<ReportOutputFormat> getOutputFormats();
	public List<ConsentDetailsDataRow> getConsentDetailsList(
			ConsentDetailsReportVO cdrVO, boolean onlyStudyLevelConsent);

}
