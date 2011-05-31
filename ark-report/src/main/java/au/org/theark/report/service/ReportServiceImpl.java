package au.org.theark.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.report.model.dao.IReportDao;
import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportTemplate;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.web.component.viewReport.consentDetails.ConsentDetailsDataRow;


@Transactional
@Service(Constants.REPORT_SERVICE)
public class ReportServiceImpl implements IReportService {
	
	private static Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	private IArkCommonService arkCommonService;
	private IStudyDao studyDao;
	private IReportDao reportDao;
	
	public IReportDao getReportDao() {
		return reportDao;
	}

	@Autowired
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}

	/*To access Hibernate Study Dao */
	public IStudyDao getStudyDao() {
		return studyDao;
	}

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
	public List<ReportTemplate> getReportsAvailableList(ArkUser arkUser) {
		List<ReportTemplate> result = reportDao.getReportsForUser(arkUser);
		return result;
	}
	
	public Integer getTotalSubjectCount(Study study) {
		return reportDao.getTotalSubjectCount(study);
	}
	
	public Map<String, Integer> getSubjectStatusCounts(Study study) {
		return reportDao.getSubjectStatusCounts(study);
	}

	public Map<String, Integer> getStudyConsentCounts(Study study) {
		return reportDao.getStudyConsentCounts(study);
	}

	public Map<String, Integer> getStudyCompConsentCounts(Study study,
			StudyComp studyComp) {
		return reportDao.getStudyCompConsentCounts(study, studyComp);
	}
	
	public Long getWithoutStudyCompCount(Study study) {
		return reportDao.getWithoutStudyCompCount(study);
	}

	public List<ReportOutputFormat> getOutputFormats() {
		return reportDao.getOutputFormats();
	}

	public List<ConsentDetailsDataRow> getConsentDetailsList(
			ConsentDetailsReportVO cdrVO, boolean onlyStudyLevelConsent) {
		
		List<ConsentDetailsDataRow> consentDetailsList = new ArrayList<ConsentDetailsDataRow>();
		
		// Perform translation to report data source here...
		List<LinkSubjectStudy> tmpResults = reportDao.getConsentDetailsList(cdrVO, onlyStudyLevelConsent);
		for (LinkSubjectStudy subject : tmpResults) {
			String subjectUID = subject.getSubjectUID();
			String consentStatus = "Not Consented";
			ConsentStatus studyConsent = subject.getConsentStatus();
			if (studyConsent != null) {
				consentStatus = studyConsent.getName();
			}
			String subjectStatus = subject.getSubjectStatus().getName();
			Person p = subject.getPerson();
			String title = p.getTitleType().getName();
			String firstName = p.getFirstName();
			String lastName = p.getLastName();
			Address a = reportDao.getBestAddress(subject);
			String streetAddress = "-NA-";
			String suburb = "-NA-";
			String state = "-NA-";
			String postcode = "-NA-";
			String country = "-NA-";
			if (a != null) {
				streetAddress = a.getStreetAddress();
				suburb = a.getCity();
				if (a.getCountryState() != null) {
					state = a.getCountryState().getState();
				}
				else if (a.getOtherState() != null) {
					state = a.getOtherState();
				}
				postcode = a.getPostCode();
				country = a.getCountry().getCountryCode();
			}
			String workPhone = "-NA-";
			String homePhone = "-NA-";
			Phone aPhone = reportDao.getWorkPhone(subject);
			if (aPhone != null) {
				workPhone = aPhone.getAreaCode() + " " + aPhone.getPhoneNumber();
			}
			aPhone = reportDao.getHomePhone(subject);
			if (aPhone != null) {
				homePhone = aPhone.getAreaCode() + " " + aPhone.getPhoneNumber();
			}
			String email = "-NA-";
			if (p.getPreferredEmail() != null) {
				email = p.getPreferredEmail();
			}
			String sex = p.getGenderType().getName().substring(0, 1);
			Date consentDate = subject.getConsentDate();
			consentDetailsList.add(new ConsentDetailsDataRow(subjectUID, consentStatus, subjectStatus, 
																title, firstName, lastName, 
																streetAddress, suburb, state, postcode, country, 
																workPhone, homePhone, email, 
																sex, consentDate));
		}

		return consentDetailsList;
	}
}