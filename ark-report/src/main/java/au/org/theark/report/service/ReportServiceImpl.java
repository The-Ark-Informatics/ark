package au.org.theark.report.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.report.model.dao.IReportDao;
import au.org.theark.report.model.entity.LinkStudyReportTemplate;
import au.org.theark.report.model.entity.ReportSecurity;


@Transactional
@Service(Constants.REPORT_SERVICE)
public class ReportServiceImpl implements IReportService {
	
	private static Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	private IArkCommonService arkCommonService;
	private IStudyDao studyDao;
	private IReportDao reportDao;
	
	private Long studyId;
	private Study study;
	
	public IReportDao getReportDao() {
		return reportDao;
	}

	@Autowired
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}

	/*To access Hibernate Study Dao */
	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public IArkCommonService getArkCommonService() {
		return arkCommonService;
	}

	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}

	/* Service methods */
	public List<LinkStudyReportTemplate> getReportsAvailableList(Study study,
			ArkUser arkUser) {
		Collection<ReportSecurity> tmpResult = reportDao.getReportSecurity(study, arkUser);
		ArrayList<LinkStudyReportTemplate> result = new ArrayList<LinkStudyReportTemplate>();
		for (ReportSecurity item : tmpResult) {
			result.add(item.getLinkStudyReportTemplate());
		}
		return result;
	}

}