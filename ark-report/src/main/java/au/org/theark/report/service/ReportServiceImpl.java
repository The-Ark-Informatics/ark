/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.report.model.dao.IReportDao;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.CustomFieldDetailsReportVO;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.model.vo.report.ConsentDetailsDataRow;
import au.org.theark.report.model.vo.report.CustomFieldDetailsDataRow;
import au.org.theark.report.model.vo.report.FieldDetailsDataRow;
import au.org.theark.report.model.vo.report.StudyUserRolePermissionsDataRow;

@Transactional
@Service(Constants.REPORT_SERVICE)
public class ReportServiceImpl implements IReportService {

	private static Logger		log	= LoggerFactory.getLogger(ReportServiceImpl.class);

	private IArkCommonService	arkCommonService;
	private IStudyDao				studyDao;
	private IReportDao			reportDao;

	public IReportDao getReportDao() {
		return reportDao;
	}

	@Autowired
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}

	/* To access Hibernate Study Dao */
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
	public List<ReportTemplate> getReportsAvailableList(ArkUser arkUser, Study study) {
		List<ReportTemplate> result = reportDao.getReportsForUser(arkUser, study);
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

	public Map<String, Integer> getStudyCompConsentCounts(Study study, StudyComp studyComp) {
		return reportDao.getStudyCompConsentCounts(study, studyComp);
	}

	public Long getWithoutStudyCompCount(Study study) {
		return reportDao.getWithoutStudyCompCount(study);
	}

	public List<ReportOutputFormat> getOutputFormats() {
		return reportDao.getOutputFormats();
	}

	public List<ConsentDetailsDataRow> getStudyLevelConsentDetailsList(ConsentDetailsReportVO cdrVO) {

		List<ConsentDetailsDataRow> consentDetailsList = new ArrayList<ConsentDetailsDataRow>();

		// Perform translation to report data source here...
		List<LinkSubjectStudy> tmpResults = reportDao.getStudyLevelConsentDetailsList(cdrVO);
		for (LinkSubjectStudy subject : tmpResults) {
			String subjectUID = subject.getSubjectUID();
			String consentStatus = Constants.NOT_CONSENTED;
			ConsentStatus studyConsent = subject.getConsentStatus();
			if (studyConsent != null) {
				consentStatus = studyConsent.getName();
			}
			String subjectStatus = subject.getSubjectStatus().getName();
			Person p = subject.getPerson();
			String title = p.getTitleType().getName();
			String firstName = p.getFirstName();
			String lastName = p.getLastName();
			// TODO: Fix this so that there are not subsequent queries after getStudyLevelConsentDetailsList(..)
			Address a = reportDao.getBestAddress(subject);
			String streetAddress = "-NA-";
			String suburb = "-NA-";
			String state = "-NA-";
			String postcode = "-NA-";
			String country = "-NA-";
			if (a != null) {
				streetAddress = a.getStreetAddress();
				suburb = a.getCity();
				if (a.getOtherState() != null) {
					state = a.getOtherState();
				}
				else if (a.getCountryState() != null) {
					state = a.getCountryState().getState();
				}
				postcode = a.getPostCode();
				country = a.getCountry().getCountryCode();
			}
			String workPhone = "-NA-";
			String homePhone = "-NA-";
			// TODO: Fix this so that there are not subsequent queries after getStudyLevelConsentDetailsList(..)
			Phone aPhone = reportDao.getWorkPhone(subject);
			if (aPhone != null) {
				workPhone = aPhone.getAreaCode() + " " + aPhone.getPhoneNumber();
			}
			// TODO: Fix this so that there are not subsequent queries after getStudyLevelConsentDetailsList(..)
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
			consentDetailsList.add(new ConsentDetailsDataRow(subjectUID, consentStatus, subjectStatus, title, firstName, lastName, streetAddress, suburb, state, postcode, country, workPhone, homePhone,
					email, sex, consentDate));
		}

		return consentDetailsList;
	}

	public List<ConsentDetailsDataRow> getStudyCompConsentDetailsList(ConsentDetailsReportVO cdrVO) {
		// LinkedHashMap maintains insertion order
		HashMap<Long, List<ConsentDetailsDataRow>> consentDetailsMap;
		List<ConsentDetailsDataRow> results = new ArrayList<ConsentDetailsDataRow>();

		// override the default initial capacity and make the loadFactor 1.0
		consentDetailsMap = new HashMap<Long, List<ConsentDetailsDataRow>>(studyDao.getConsentStatus().size(), (float) 1.0);

		boolean noConsentDateCriteria = (cdrVO.getConsentDate() == null);
		boolean noConsentStatusCriteria = (cdrVO.getConsentStatus() == null);
		if (noConsentDateCriteria && noConsentStatusCriteria) {
			// This means that we can't do a query with LinkSubjectStudy inner join Consent
			// - so better to just iterate through the subjects that match the initial subject criteria
			Study study = cdrVO.getLinkSubjectStudy().getStudy();
			List<LinkSubjectStudy> subjectList = reportDao.getSubjects(cdrVO);

			for (LinkSubjectStudy subject : subjectList) {
				Consent consentCriteria = new Consent();
				consentCriteria.setStudy(study);
				consentCriteria.setLinkSubjectStudy(subject);
				consentCriteria.setStudyComp(cdrVO.getStudyComp());
				consentCriteria.setConsentDate(cdrVO.getConsentDate());
				consentCriteria.setConsentStatus(cdrVO.getConsentStatus());
				// reportDao.getStudyCompConsent(..) ignores consentDate and consentStatus
				Consent consentResult = reportDao.getStudyCompConsent(consentCriteria);

				ConsentDetailsDataRow cddr = new ConsentDetailsDataRow();
				if (consentResult == null) {
					populateConsentDetailsDataRow(cddr, study, subject, null);
					Long key = null;
					if (!consentDetailsMap.containsKey(key)) {
						consentDetailsMap.put(key, new ArrayList<ConsentDetailsDataRow>());
					}
					consentDetailsMap.get(key).add(cddr);
				}
				else {
					populateConsentDetailsDataRow(cddr, study, subject, consentResult);
					Long key = consentResult.getConsentStatus().getId();
					if (!consentDetailsMap.containsKey(key)) {
						consentDetailsMap.put(key, new ArrayList<ConsentDetailsDataRow>());
					}
					consentDetailsMap.get(key).add(cddr);
				}
			}
			for (Long key : consentDetailsMap.keySet()) {
				results.addAll(consentDetailsMap.get(key));
			}
		}
		else {
			// Perform a consentStatus and/or consentDate constrained lookup
			// based on LinkSubjectStudy inner join Consent (this is because if either
			// of these constraints are applied, there will be no such Consent record
			// for the "Not Consented" state)
			List<ConsentDetailsDataRow> consents = reportDao.getStudyCompConsentList(cdrVO);
			if (consents != null) {
				for (ConsentDetailsDataRow cddr : consents) {
					Study study = cdrVO.getLinkSubjectStudy().getStudy();
					populateConsentDetailsDataRow(cddr, study, null, null);
					results.add(cddr);
				}
			}
		}

		return results;
	}

	protected void populateConsentDetailsDataRow(ConsentDetailsDataRow consentRow, Study study, LinkSubjectStudy subject, Consent consent) {
		String consentStatus = Constants.NOT_CONSENTED;
		if (consent != null && consent.getConsentStatus() != null) {
			consentStatus = consent.getConsentStatus().getName();
			consentRow.setConsentStatus(consentStatus); // set ConsentStatus with override from Consent arg
			consentRow.setConsentDate(consent.getConsentDate()); // set ConsentDate with override from Consent arg
		}
		else if (consentRow.getConsentStatus() == null || consentRow.getConsentStatus().isEmpty()) {
			consentRow.setConsentStatus(consentStatus); // set ConsentStatus to Not Consented if not set
		}

		String streetAddress = "-NA-";
		String suburb = "-NA-";
		String state = "-NA-";
		String postcode = "-NA-";
		String country = "-NA-";
		String workPhone = "-NA-";
		String homePhone = "-NA-";
		String email = "-NA-";

		try {
			if (subject == null) {
				// no subject was passed in, retrieve from DB via subjectUID
				if(consent == null) {
					subject = studyDao.getSubjectByUID(consentRow.getSubjectUID(), study);
				}
				else {
					subject = studyDao.getSubjectByUID(consentRow.getSubjectUID(), consent.getStudy());
				}
			}
			else {
				// set subjectUID if subject passed in
				consentRow.setSubjectUID(subject.getSubjectUID());
			}
			String subjectStatus = subject.getSubjectStatus().getName();
			consentRow.setSubjectStatus(subjectStatus); // set SubjectStatus
			Person p = subject.getPerson();
			String title = p.getTitleType().getName();
			consentRow.setTitle(title); // set Title
			String firstName = p.getFirstName();
			consentRow.setFirstName(firstName); // set FirstName
			String lastName = p.getLastName();
			consentRow.setLastName(lastName); // set LastName
			Address a = reportDao.getBestAddress(subject);

			if (p.getPreferredEmail() != null) {
				email = p.getPreferredEmail();
			}
			String sex = p.getGenderType().getName().substring(0, 1);
			consentRow.setSex(sex); // set Sex

			if (a != null) {
				streetAddress = a.getStreetAddress();
				suburb = a.getCity();
				if (a.getOtherState() != null) {
					state = a.getOtherState();
				}
				else if (a.getCountryState() != null) {
					state = a.getCountryState().getState();
				}
				postcode = a.getPostCode();
				country = a.getCountry().getCountryCode();
			}

			Phone aPhone = reportDao.getWorkPhone(subject);
			if (aPhone != null) {
				workPhone = aPhone.getAreaCode() + " " + aPhone.getPhoneNumber();
			}
			aPhone = reportDao.getHomePhone(subject);
			if (aPhone != null) {
				homePhone = aPhone.getAreaCode() + " " + aPhone.getPhoneNumber();
			}
		}
		catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			consentRow.setStreetAddress(streetAddress);
			consentRow.setSuburb(suburb);
			consentRow.setCountry(country);
			consentRow.setState(state);
			consentRow.setPostcode(postcode);
			consentRow.setWorkPhone(workPhone);
			consentRow.setHomePhone(homePhone);
			consentRow.setEmail(email);
		}
	}

	public List<FieldDetailsDataRow> getPhenoFieldDetailsList(FieldDetailsReportVO fdrVO) {
		return reportDao.getPhenoFieldDetailsList(fdrVO);
	}

	public List<CustomFieldDetailsDataRow> getPhenoCustomFieldDetailsList(CustomFieldDetailsReportVO fdrVO) {
		return reportDao.getPhenoCustomFieldDetailsList(fdrVO);
	}

	public List<PhenoCollection> getPhenoCollectionList(Study study) {
		return reportDao.getPhenoCollectionList(study);
	}

	public List<StudyUserRolePermissionsDataRow> getStudyUserRolePermissions(Study study) {
		return reportDao.getStudyUserRolePermissions(study);
	}

	public List<CustomFieldGroup> getQuestionnaireList(Study study) {
		return reportDao.getQuestionnaireList(study);
	}
	
}
