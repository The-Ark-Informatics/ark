package au.org.theark.report.model.dao;

import java.util.Collection;
import java.util.Date;

import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.dao.StudyDao;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.report.model.entity.ReportSecurity;
import au.org.theark.report.model.entity.ReportTemplate;

@Repository("reportDao")
public class ReportDao extends HibernateSessionDao implements IReportDao {


	private static Logger log = LoggerFactory.getLogger(ReportDao.class);
	private Subject	currentUser;
	private Date		dateNow;
	
	public Study getStudy(Long id){
		StudyDao studyDao;
		return null;
	}
	
	public Collection<ReportSecurity> getReportSecurity(Study study, ArkUser arkUser) {
		Criteria criteria = getSession().createCriteria(ReportSecurity.class);
		criteria.createAlias("linkStudyReportTemplate", "linkReportTemplateAlias");
		criteria.add(Restrictions.or(Restrictions.eq("linkReportTemplateAlias.study", study), Restrictions.isNull("linkReportTemplateAlias.study")));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		java.util.Collection<ReportSecurity> reportsAvailListing = criteria.list();

		return reportsAvailListing;
	}
}
