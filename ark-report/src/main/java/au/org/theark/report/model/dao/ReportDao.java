package au.org.theark.report.model.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.dao.StudyDao;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.report.model.entity.ReportOutputFormat;
import au.org.theark.report.model.entity.ReportSecurity;
import au.org.theark.report.model.entity.ReportTemplate;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.service.Constants;
import au.org.theark.report.web.component.viewReport.consentDetails.ConsentDetailsDataRow;

@Repository("reportDao")
public class ReportDao extends HibernateSessionDao implements IReportDao {


	private static Logger log = LoggerFactory.getLogger(ReportDao.class);
	private Subject	currentUser;
	private Date		dateNow;
	
	public Study getStudy(Long id){
		StudyDao studyDao;
		return null;
	}
	
	public List<ReportSecurity> getReportSecurity(Study study, ArkUser arkUser) {
		Criteria criteria = getSession().createCriteria(ReportSecurity.class);
		criteria.createAlias("linkStudyReportTemplate", "linkReportTemplateAlias");
		criteria.add(Restrictions.or(Restrictions.eq("linkReportTemplateAlias.study", study), Restrictions.isNull("linkReportTemplateAlias.study")));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		List<ReportSecurity> reportsAvailListing = criteria.list();

		return reportsAvailListing;
	}

	public Integer getTotalSubjectCount(Study study) {
		Integer totalCount = 0;
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		totalCount = (Integer) criteria.uniqueResult();

		return totalCount;
	}

	public Map<String, Integer> getSubjectStatusCounts(Study study) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("subjectStatus", "subjectStatusAlias");
		projectionList.add(Projections.groupProperty("subjectStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		Map<String, Integer> statusMap = new HashMap<String, Integer>();
		for(Object r: results) {
			Object[] obj = (Object[]) r;
			String statusName = (String)obj[0];
			statusMap.put(statusName, (Integer)obj[1]);
		}
		return statusMap;
	}

	public Map<String, Integer> getStudyConsentCounts(Study study) {
		Map<String, Integer> statusMap = new HashMap<String, Integer>();

		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("consentStatus", "consentStatusAlias");
		projectionList.add(Projections.groupProperty("consentStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		for(Object r: results) {
			Object[] obj = (Object[]) r;
			String statusName = (String)obj[0];
			statusMap.put(statusName, (Integer)obj[1]);
		}
		
		// Tack on count of when consentStatus = undefined (NULL) 
		criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.isNull("consentStatus"));
		projectionList = Projections.projectionList();
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		Integer undefCount = (Integer) criteria.uniqueResult();
		String statusName = "Not Yet Defined";
		statusMap.put(statusName, undefCount);

		return statusMap;
	}

	public Map<String, Integer> getStudyCompConsentCounts(Study study,
			StudyComp studyComp) {
		Map<String, Integer> statusMap = new HashMap<String, Integer>();

		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("studyComp", studyComp));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("consentStatus", "consentStatusAlias");
		projectionList.add(Projections.groupProperty("consentStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		if ((results != null) && (results.size() > 0)) {
			for(Object r: results) {
				Object[] obj = (Object[]) r;
				String statusName = (String)obj[0];
				statusMap.put(statusName, (Integer)obj[1]);
			}
		}
		else {
			statusMap.put("(none found)", new Integer(0));
		}
		return statusMap;
	}
	
	public Long getWithoutStudyCompCount(Study study) {

		/* The following HQL implements this MySQL query:
		 SELECT COUNT(*) FROM study.link_subject_study AS lss
			LEFT JOIN study.consent AS c
			ON lss.id = c.subject_id		-- this line is implicit from annotations on the entity classes
			WHERE lss.study_id = 2
			AND c.id IS NULL;
		*/
		String hqlString = "SELECT COUNT(*) FROM LinkSubjectStudy AS lss \n"
							+ "LEFT JOIN lss.consents AS c \n"
							+ "WHERE lss.study = :study \n"
							+ "AND c.id IS NULL";
		Query q = getSession().createQuery(hqlString);
//		if (hqlString.contains(":study_id")) {
//			q.setParameter("study_id", study.getId());
//		}
//		if (hqlString.contains(":study")) {
			q.setParameter("study", study);
//		}
		Long undefCount = (Long) q.uniqueResult();

		return undefCount;
	}
	
	public List<ReportTemplate> getReportsForUser(ArkUser arkUser) {
		Criteria criteria = getSession().createCriteria(ReportTemplate.class);
//		TODO : Add security here
//		criteria.add(Restrictions.eq("arkUser", arkUser));
		List<ReportTemplate> reportsAvailListing = criteria.list();

		return reportsAvailListing;
	}

	public List<ReportOutputFormat> getOutputFormats() {
		Criteria criteria = getSession().createCriteria(ReportOutputFormat.class);
		List<ReportOutputFormat> outputFormats = criteria.list();

		return outputFormats;
	}

	public List<ConsentDetailsDataRow> getConsentDetailsList(
			ConsentDetailsReportVO cdrVO, boolean onlyStudyLevelConsent) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		if (cdrVO.getLinkSubjectStudy() != null) {
			if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
				criteria.add(Restrictions.ilike(Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID()));
			}
			if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
				criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
			}
		}
		if (!onlyStudyLevelConsent && cdrVO.getStudyComp() != null) {
			criteria.createAlias(Constants.LINKSUBJECTSTUDY_CONSENT, "c");
			criteria.add(Restrictions.eq("c." + Constants.CONSENT_STUDYCOMP, cdrVO.getStudyComp()));
			if (cdrVO.getConsentStatus() != null) {
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
			if (cdrVO.getConsentDate() != null) {
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTDATE, cdrVO.getConsentDate()));
			}
		}
		else {
			// we are dealing with study-level consent
			if (cdrVO.getConsentStatus() != null) {
				criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
			if (cdrVO.getConsentDate() != null) {
				criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTDATE, cdrVO.getConsentDate()));
			}
		}
//		if (onlyStudyLevelConsent) {
//			criteria.setProjection(Projections.projectionList()
//					.add(Property.forName("subjectUID").as("SubjectUID"))
//					.add(Property.forName("consentStatus").as("ConsentStatus"))	//study-level
//					.add(Property.forName("subjectStatus").as("SubjectStatus"))
//					.add(Property.forName("person.titleType.name").as("Title"))
//					.add(Property.forName("person.firstName").as("FirstName"))
//					.add(Property.forName("person.lastName").as("LastName"))
//					.add(Property.forName("consents.name").as("Consent"))
//					.add(Property.forName("add"))
//					);	
//			criteria.addOrder(Order.asc("subjectUID"));
//		}
//		else {
//			criteria.setProjection(Projections.projectionList()
//					.add(Property.forName("subjectUID").as("SubjectUID"))
//					.add(Property.forName("consents.name").as("ConsentStatus"))
//					.add(Property.forName("subjectStatus").as("SubjectStatus"))
//					.add(Property.forName("person.titleType.name").as("Title"))
//					.add(Property.forName("person.firstName").as("FirstName"))
//					.add(Property.forName("person.lastName").as("LastName"))
//					);
//			criteria.addOrder(Order.asc("subjectUID"));
//		}
		List<ConsentDetailsDataRow> consentDetailsList = criteria.list();

		return consentDetailsList;
	}
}
