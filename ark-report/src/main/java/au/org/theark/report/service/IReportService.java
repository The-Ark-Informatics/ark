package au.org.theark.report.service;

import java.util.List;

import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.model.entity.LinkStudyReportTemplate;


public interface IReportService {

	public List<LinkStudyReportTemplate> getReportsAvailableList(Study study, ArkUser arkUser);
	
}
