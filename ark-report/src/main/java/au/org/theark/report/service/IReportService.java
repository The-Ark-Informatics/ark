package au.org.theark.report.service;

import java.util.List;
import java.util.Map;

import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.entity.LinkStudyReportTemplate;
import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportTemplate;


public interface IReportService {

	// TODO: Remove this first method when migration to new Ark security done
	public List<LinkStudyReportTemplate> getReportsAvailableList(Study study, ArkUser arkUser);
	
	public Integer getTotalSubjectCount(Study study);
	public Map<String, Integer> getSubjectStatusCounts(Study study);
	public Map<String, Integer> getStudyConsentCounts(Study study);
	public Map<String, Integer> getStudyCompConsentCounts(Study study, StudyComp studyComp);
	public Long getWithoutStudyCompCount(Study study);
	public List<ReportTemplate> getReportsAvailableList(ArkUser arkUser);
	public List<ReportOutputFormat> getOutputFormats();


}
