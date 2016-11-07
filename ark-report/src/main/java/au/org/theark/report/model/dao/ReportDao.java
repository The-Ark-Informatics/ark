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
package au.org.theark.report.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.worktracking.entity.BillableItem;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.report.model.vo.BiospecimenDetailsReportVO;
import au.org.theark.report.model.vo.BiospecimenSummaryReportVO;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.CustomFieldDetailsReportVO;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.model.vo.PhenoDataSetFieldDetailsReportVO;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.model.vo.StudyComponentReportVO;
import au.org.theark.report.model.vo.report.BiospecimenDetailsDataRow;
import au.org.theark.report.model.vo.report.BiospecimenSummaryDataRow;
import au.org.theark.report.model.vo.report.ConsentDetailsDataRow;
import au.org.theark.report.model.vo.report.CustomFieldDetailsDataRow;
import au.org.theark.report.model.vo.report.FieldDetailsDataRow;
import au.org.theark.report.model.vo.report.PhenoDataSetFieldDetailsDataRow;
import au.org.theark.report.model.vo.report.ResearcherCostDataRow;
import au.org.theark.report.model.vo.report.ResearcherDetailCostDataRow;
import au.org.theark.report.model.vo.report.StudyComponentDetailsDataRow;
import au.org.theark.report.model.vo.report.StudyUserRolePermissionsDataRow;
import au.org.theark.report.service.Constants;


/**
 * Provide the backend Data Access Object for Reporting
 * 
 * @author elam
 * 
 */
@Repository("reportDao")
public class ReportDao extends HibernateSessionDao implements IReportDao {

	private static Logger	log	= LoggerFactory.getLogger(ReportDao.class);

	private IArkCommonService<Void>	iArkCommonService;
	@Autowired
	public void setiArkCommonService(IArkCommonService<Void> iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}

	public long getTotalSubjectCount(Study study) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	public Map<String, Long> getSubjectStatusCounts(Study study) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("subjectStatus", "subjectStatusAlias");
		projectionList.add(Projections.groupProperty("subjectStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		Map<String, Long> statusMap = new HashMap<String, Long>();
		for (Object r : results) {
			Object[] obj = (Object[]) r;
			String statusName = (String) obj[0];
			statusMap.put(statusName, (Long) obj[1]);
		}
		return statusMap;
	}

	public Map<String, Long> getStudyConsentCounts(Study study) {
		Map<String, Long> statusMap = new HashMap<String, Long>();

		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("consentStatus", "consentStatusAlias");
		projectionList.add(Projections.groupProperty("consentStatusAlias.name"));
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		List results = criteria.list();
		for (Object r : results) {
			Object[] obj = (Object[]) r;
			String statusName = (String) obj[0];
			statusMap.put(statusName, (Long) obj[1]);
		}

		// Tack on count of when consentStatus = undefined (NULL)
		criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.isNull("consentStatus"));
		projectionList = Projections.projectionList();
		projectionList.add(Projections.rowCount());
		criteria.setProjection(projectionList);
		Long undefCount = (Long) criteria.uniqueResult();
		String statusName = Constants.NOT_CONSENTED;
		statusMap.put(statusName,undefCount);

		return statusMap;
	}

	public Map<String, Long> getStudyCompConsentCounts(Study study, StudyComp studyComp) {
		Map<String, Long> statusMap = new HashMap<String, Long>();

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
			for (Object r : results) {
				Object[] obj = (Object[]) r;
				String statusName = (String) obj[0];
				statusMap.put(statusName, (Long) obj[1]);
			}
		}
		else {
			statusMap.put("(none found)", new Long(0));
		}
		return statusMap;
	}

	public Long getWithoutStudyCompCount(Study study) {

		/*
		 * The following HQL implements this MySQL query: SELECT COUNT(*) FROM study.link_subject_study AS lss LEFT JOIN study.consent AS c ON lss.id =
		 * c.subject_id -- this line is implicit from annotations on the entity classes WHERE lss.study_id = 2 AND c.id IS NULL;
		 */
		String hqlString = "SELECT COUNT(*) FROM LinkSubjectStudy AS lss \n" + "LEFT JOIN lss.consents AS c \n" + "WHERE lss.study = :study \n" + "AND c.id IS NULL";
		Query q = getSession().createQuery(hqlString);
		// if (hqlString.contains(":study_id")) {
		// q.setParameter("study_id", study.getId());
		// }
		// if (hqlString.contains(":study")) {
		q.setParameter("study", study);
		// }
		Long undefCount = (Long) q.uniqueResult();

		return undefCount;
	}

	public List<ReportTemplate> getReportsForUser(ArkUser arkUser, Study study) {
		Criteria criteria = getSession().createCriteria(ReportTemplate.class, "rt");
		Collection<ArkModule> modules = iArkCommonService.getArkModulesLinkedWithStudy(study);
		/*
		 * TODO : Filter reports based on security criteria For now we will implement security upon the selection of a report
		 * 
		 * // The following is not yet designed to work with super admins // criteria.add(Restrictions.eq("arkUser", arkUser)); DetachedCriteria
		 * functionCriteria = DetachedCriteria.forClass(ArkRolePolicyTemplate.class, "arpt"); // Join FieldPhenoCollection and FieldData on ID FK
		 * functionCriteria.add(Property.forName("rt.module").eqProperty("arpt." + "arkModule"));
		 * functionCriteria.add(Property.forName("rt.function").eqProperty("arpt." + "arkFunction")); criteria.createAlias("arpt." + "arkFunction",
		 * "aFn"); ArkFunction reportArkFnType = getArkFunctionByName(RoleConstants.REPORT_FUNCTION_TYPE);
		 * functionCriteria.add(Restrictions.eq("aFn.arkFunctionType", reportArkFnType));
		 * criteria.add(Subqueries.exists(functionCriteria.setProjection(Projections.property("arpt.id"))));
		 */
		if(modules.size() >0)
			criteria.add(Restrictions.in("module", modules));
		
		List<ReportTemplate> reportsAvailListing = criteria.list();

		return reportsAvailListing;
	}

	public List<ReportOutputFormat> getOutputFormats() {
		Criteria criteria = getSession().createCriteria(ReportOutputFormat.class);
		List<ReportOutputFormat> outputFormats = criteria.list();

		return outputFormats;
	}

	public List<LinkSubjectStudy> getStudyLevelConsentDetailsList(ConsentDetailsReportVO cdrVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);

		// Add study in context to criteria first (linkSubjectStudy on the VO should never be null)
		criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike(Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}

		// we are dealing with study-level consent
		if (cdrVO.getConsentStatus() != null) {
			if (cdrVO.getConsentStatus().getName().equals(Constants.NOT_CONSENTED)) {
				// Special-case: Treat the null FK for consentStatus as "Not Consented"
				criteria.add(Restrictions.or(Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()), Restrictions.isNull(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS)));
			}
			else {
				criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
		}
		if (cdrVO.getConsentDate() != null) {
			criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_CONSENTDATE, cdrVO.getConsentDate()));
		}
		criteria.addOrder(Order.asc("consentStatus")); // although MySQL causes NULLs to come first
		criteria.addOrder(Order.asc("subjectUID"));

		return (List<LinkSubjectStudy>) criteria.list();
	}
	
	public List<ConsentDetailsDataRow> getStudyLevelConsentDetailsDataRowList(ConsentDetailsReportVO cdrVO) {
		List<ConsentDetailsDataRow> resultList = new ArrayList<ConsentDetailsDataRow>(0);

		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class, "lss");
		// Add study in context to criteria first (linkSubjectStudy on the VO should never be null)
		criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}

		// we are dealing with study-level consent
		if (cdrVO.getConsentStatus() != null) {
			if (cdrVO.getConsentStatus().getName().equals(Constants.NOT_CONSENTED)) {
				// Special-case: Treat the null FK for consentStatus as "Not Consented"
				criteria.add(Restrictions.or(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()), Restrictions.isNull(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS)));
			}
			else {
				criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
		}
		if (cdrVO.getConsentDate() != null) {
			criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_CONSENTDATE, cdrVO.getConsentDate()));
		}
		
		criteria.createAlias("lss.consentStatus", "cs", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.subjectStatus", "ss");
		criteria.createAlias("lss.person", "p");
		criteria.createAlias("lss.person.genderType", "genderType");
		
		// Restrict any addresses to the preferred mailing address
		//Criteria addressCriteria = 
				criteria.createAlias("lss.person.addresses", "a", JoinType.LEFT_OUTER_JOIN);
//		addressCriteria.setMaxResults(1);
//		addressCriteria.add(Restrictions.or(Restrictions.or(Restrictions.eq("a.preferredMailingAddress", true), Restrictions.isNull("a.preferredMailingAddress"),Restrictions.eq("a.preferredMailingAddress", false))));
		
		criteria.createAlias("lss.person.addresses.country", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.person.addresses.state", "state", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.person.phones", "phone", JoinType.LEFT_OUTER_JOIN);
		
		//TODO: Get work phone returned as well
		//Criteria phoneCriteria = 
		criteria.createAlias("lss.person.phones.phoneType", "phoneType", JoinType.LEFT_OUTER_JOIN);/*.add(
				Restrictions.or(Restrictions.eq("phoneType.name", "Home"),
									(
										Restrictions.or(Restrictions.or(Restrictions.eq("phoneType.name", "Home"),
														Restrictions.isNull("phoneType.name"),Restrictions.eq("phoneType.name", "Mobile")))
								
									)
								)
							));*/
		//phoneCriteria.setMaxResults(1);
		criteria.createAlias("lss.person.titleType", "title");
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("lss.subjectUID"), "subjectUID");
		projectionList.add(Projections.property("cs.name"), "consentStatus");
		projectionList.add(Projections.property("ss.name"), "subjectStatus");
		projectionList.add(Projections.property("title.name"), "title");
		projectionList.add(Projections.property("p.firstName"), "firstName");
		projectionList.add(Projections.property("p.lastName"), "lastName");
		projectionList.add(Projections.property("a.streetAddress"), "streetAddress");
		projectionList.add(Projections.property("a.city"), "suburb");
		projectionList.add(Projections.property("a.postCode"), "postcode");
		projectionList.add(Projections.property("state.name"), "state");
		projectionList.add(Projections.property("c.name"), "country");
		projectionList.add(Projections.property("phone.phoneNumber"), "homePhone");
		projectionList.add(Projections.property("p.preferredEmail"), "email");
		projectionList.add(Projections.property("genderType.name"), "sex");
		projectionList.add(Projections.property("lss.consentDate"), "consentDate");
		
		criteria.setProjection(projectionList); // only return fields required for report

		criteria.addOrder(Order.asc("lss.consentStatus")); // although MySQL causes NULLs to come first
		criteria.addOrder(Order.asc("lss.subjectUID"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(ConsentDetailsDataRow.class));
		resultList = (criteria.list());
		
		return resultList;
	}
	
	public List<ConsentDetailsDataRow> getStudyLevelConsentOtherIDDetailsDataRowList(ConsentDetailsReportVO cdrVO) {
		List<ConsentDetailsDataRow> resultList = new ArrayList<ConsentDetailsDataRow>(0);

		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class, "lss");
		// Add study in context to criteria first (linkSubjectStudy on the VO should never be null)
		criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}

		// we are dealing with study-level consent
		if (cdrVO.getConsentStatus() != null) {
			if (cdrVO.getConsentStatus().getName().equals(Constants.NOT_CONSENTED)) {
				// Special-case: Treat the null FK for consentStatus as "Not Consented"
				criteria.add(Restrictions.or(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()), Restrictions.isNull(Constants.LINKSUBJECTSTUDY_CONSENTSTATUS)));
			}
			else {
				criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
		}
		if (cdrVO.getConsentDate() != null) {
			criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_CONSENTDATE, cdrVO.getConsentDate()));
		}
		
		criteria.createAlias("lss.consentStatus", "cs", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.subjectStatus", "ss");
		criteria.createAlias("lss.person", "p");
		criteria.createAlias("lss.person.genderType", "genderType");
		
		// Restrict any addresses to the preferred mailing address
		//Criteria addressCriteria = 
				criteria.createAlias("lss.person.addresses", "a", JoinType.LEFT_OUTER_JOIN);
//		addressCriteria.setMaxResults(1);
//		addressCriteria.add(Restrictions.or(Restrictions.or(Restrictions.eq("a.preferredMailingAddress", true), Restrictions.isNull("a.preferredMailingAddress"),Restrictions.eq("a.preferredMailingAddress", false))));
		
		criteria.createAlias("lss.person.addresses.country", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.person.addresses.state", "state", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.person.phones", "phone", JoinType.LEFT_OUTER_JOIN);
		
		//TODO: Get work phone returned as well
		//Criteria phoneCriteria = 
		criteria.createAlias("lss.person.phones.phoneType", "phoneType", JoinType.LEFT_OUTER_JOIN);/*.add(
				Restrictions.or(Restrictions.eq("phoneType.name", "Home"),
									(
										Restrictions.or(Restrictions.or(Restrictions.eq("phoneType.name", "Home"),
														Restrictions.isNull("phoneType.name"),Restrictions.eq("phoneType.name", "Mobile")))
								
									)
								)
							));*/
		//phoneCriteria.setMaxResults(1);
		criteria.createAlias("lss.person.titleType", "title");
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("lss.subjectUID"), "subjectUID");
		projectionList.add(Projections.property("cs.name"), "consentStatus");
		projectionList.add(Projections.property("ss.name"), "subjectStatus");
		projectionList.add(Projections.property("title.name"), "title");
		projectionList.add(Projections.property("p.firstName"), "firstName");
		projectionList.add(Projections.property("p.lastName"), "lastName");
		projectionList.add(Projections.property("a.streetAddress"), "streetAddress");
		projectionList.add(Projections.property("a.city"), "suburb");
		projectionList.add(Projections.property("a.postCode"), "postcode");
		projectionList.add(Projections.property("state.name"), "state");
		projectionList.add(Projections.property("c.name"), "country");
		projectionList.add(Projections.property("phone.phoneNumber"), "homePhone");
		projectionList.add(Projections.property("p.preferredEmail"), "email");
		projectionList.add(Projections.property("genderType.name"), "sex");
		projectionList.add(Projections.property("lss.consentDate"), "consentDate");
		
		criteria.setProjection(projectionList); // only return fields required for report

		criteria.addOrder(Order.asc("lss.consentStatus")); // although MySQL causes NULLs to come first
		criteria.addOrder(Order.asc("lss.subjectUID"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(ConsentDetailsDataRow.class));
		resultList = (criteria.list());
		
		//removing duplicate entries from resultList
		
		List<ConsentDetailsDataRow>	uniqueResults = new ArrayList();

		Iterator<ConsentDetailsDataRow> iterator = resultList.iterator();

		while (iterator.hasNext())
		{
			ConsentDetailsDataRow o = iterator.next();
			if(!uniqueResults.contains(o)) uniqueResults.add(o);
		}
		
		resultList.clear();
		resultList.addAll(uniqueResults);
		
		for(int j = 0; j < resultList.size(); j++) {
			ConsentDetailsDataRow c = resultList.get(j);
			if(c.getOtherID() == null && c.getOtherIDSource() == null) {
				List<OtherID> otherIDs = null;
				try {
					otherIDs = iArkCommonService.getOtherIDs(iArkCommonService.getSubjectByUID(c.getSubjectUID(), cdrVO.getLinkSubjectStudy().getStudy()).getPerson());
					if(otherIDs.size() >= 1) {
						c.setOtherID(otherIDs.get(0).getOtherID());
						c.setOtherIDSource(otherIDs.get(0).getOtherID_Source());
					}
					if(otherIDs.size() > 1) { 
						for(int i = 1; i <otherIDs.size(); i++) {
							OtherID o = otherIDs.get(i);
							ConsentDetailsDataRow clone = new ConsentDetailsDataRow(c.getSubjectUID(), o.getOtherID_Source(), o.getOtherID(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
							resultList.add(clone);
						}	
					}
				} catch (EntityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}	
	
	public List<ConsentDetailsDataRow> getStudyCompConsentList(ConsentDetailsReportVO cdrVO) {
		// NB: There should only ever be one Consent record for a particular Subject for a particular StudyComp

		List<ConsentDetailsDataRow> results = new ArrayList<ConsentDetailsDataRow>();
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class, "lss");
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("lss." + "subjectUID"), "subjectUID");

		criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq("lss." + Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}

		if (cdrVO.getConsentDate() != null) {
			// NB: constraint on consentDate or consentStatus automatically removes "Not Consented" state
			// So LinkSubjectStudy inner join to Consent ok for populated consentDate
			criteria.createAlias("lss." + Constants.LINKSUBJECTSTUDY_CONSENT, "c");
			criteria.createAlias("c." + Constants.CONSENT_CONSENTSTATUS, "cs");
			// constrain on studyComp
			criteria.add(Restrictions.eq("c." + Constants.CONSENT_STUDYCOMP, cdrVO.getStudyComp()));
			// constrain on consentDate
			criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTDATE, cdrVO.getConsentDate()));
			// ConsentStatus is optional for this query...
			if (cdrVO.getConsentStatus() != null) {
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTSTATUS, cdrVO.getConsentStatus()));
			}
			projectionList.add(Projections.property("cs.name"), "consentStatus");
			projectionList.add(Projections.property("c." + Constants.CONSENT_CONSENTDATE), "consentDate");

		}
		else if (cdrVO.getConsentStatus() != null) {
			if (cdrVO.getConsentStatus().getName().equals(Constants.NOT_CONSENTED)) {
				// Need to handle "Not Consented" status differently (since it doesn't have a Consent record)
				// Helpful website: http://www.cereslogic.com/pages/2008/09/22/hibernate-criteria-subqueries-exists/

				// Build subquery to find all Consent records for a Study Comp
				DetachedCriteria consentCriteria = DetachedCriteria.forClass(Consent.class, "c");
				// Constrain on StudyComponent
				consentCriteria.add(Restrictions.eq("c." + Constants.CONSENT_STUDYCOMP, cdrVO.getStudyComp()));
				// Just in case "Not Consented" is erroneously entered into a row in the Consent table
				// consentCriteria.add(Restrictions.ne("c." + Constants.CONSENT_CONSENTSTATUS, cdrVO.getConsentStatus()));
				// Join LinkSubjectStudy and Consent on ID FK
				consentCriteria.add(Property.forName("c.linkSubjectStudy.id").eqProperty("lss." + "id"));
				criteria.add(Subqueries.notExists(consentCriteria.setProjection(Projections.property("c.id"))));

				// If Consent records follows design for "Not Consented", then:
				// - consentStatus and consentDate are not populated
			}
			else {
				// NB: constraint on consentDate or consentStatus automatically removes "Not Consented" state
				// So LinkSubjectStudy inner join to Consent ok for all recordable consentStatus
				criteria.createAlias("lss." + Constants.LINKSUBJECTSTUDY_CONSENT, "c");
				criteria.createAlias("c." + Constants.CONSENT_CONSENTSTATUS, "cs");
				// Constrain on StudyComponent
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_STUDYCOMP, cdrVO.getStudyComp()));
				// ConsentStatus is NOT optional for this query!
				criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTSTATUS, cdrVO.getConsentStatus()));
				if (cdrVO.getConsentDate() != null) {
					criteria.add(Restrictions.eq("c." + Constants.CONSENT_CONSENTDATE, cdrVO.getConsentDate()));
				}
				projectionList.add(Projections.property("cs.name"), "consentStatus");
				projectionList.add(Projections.property("c." + Constants.CONSENT_CONSENTDATE), "consentDate");
			}
		}
		else {
			// Should not attempt to run this query with no consentDate nor consentStatus criteria provided
			log.error("reportDao.getStudyCompConsentList(..) is missing consentDate or consentStatus parameters in the VO");
			return null;
		}

		criteria.addOrder(Order.asc("lss." + "subjectUID"));
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(ConsentDetailsDataRow.class));
		// This gives a list of subjects matching the specific studyComp and consentStatus
		results = criteria.list();

		return results;
	}

	/**
	 * obviously you need it attached and preferably prefetched or you may take an exponential hit to the DB
	 * 
	 * @param lss
	 * @return
	 */
	public Address getBestAddressWithOutNewQueries(LinkSubjectStudy lss){
		Address goodEnoughIfWeCantFindBetter = null;
		for(Address a : lss.getPerson().getAddresses()){
			if(a!=null && a.getPreferredMailingAddress()!=null && a.getPreferredMailingAddress()){
				return a;
			}
			else if(goodEnoughIfWeCantFindBetter == null){
				goodEnoughIfWeCantFindBetter = a;
			}
		}
		return goodEnoughIfWeCantFindBetter;
	}
	
	public Address getBestAddress(LinkSubjectStudy subject) {
		Address result = null;
		// Attempt to get the preferred address first
		Criteria criteria = getSession().createCriteria(Address.class);
		criteria.add(Restrictions.eq("person", subject.getPerson()));
		criteria.add(Restrictions.eq("preferredMailingAddress", true));
		criteria.setMaxResults(1);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("streetAddress"), "streetAddress");
		projectionList.add(Projections.property("city"), "city");
		projectionList.add(Projections.property("country"), "country");
		projectionList.add(Projections.property("state"), "state");
		projectionList.add(Projections.property("otherState"), "otherState");
		projectionList.add(Projections.property("postCode"), "postCode");
		criteria.setProjection(projectionList); // only return fields required for report
		criteria.setResultTransformer(Transformers.aliasToBean(Address.class));

		if (criteria.uniqueResult() != null) {
			result = (Address) criteria.uniqueResult();
		}
		else {
			// Get any address
			criteria = getSession().createCriteria(Address.class);
			criteria.add(Restrictions.eq("person", subject.getPerson()));
			criteria.setMaxResults(1);
			criteria.setProjection(projectionList); // only return fields required for report
			criteria.setResultTransformer(Transformers.aliasToBean(Address.class));

			result = (Address) criteria.uniqueResult();
		}
		return result;
	}


	/**
	 * obviously you need it attached an preferably prefetched or you may take an exponential hit to the DB
	 * 
	 * @param lss
	 * @return
	 */
	public Phone getWorkPhoneWithoutExponentialQueries(LinkSubjectStudy lss){
		Phone goodEnoughIfWeCantFindBetter = null;
		for(Phone phone : lss.getPerson().getPhones()){
			if(phone!=null && phone.getPhoneType()!=null && phone.getPhoneType().getName().equalsIgnoreCase("Work")){
				return phone;
			}
			else if(goodEnoughIfWeCantFindBetter == null){
				goodEnoughIfWeCantFindBetter = phone;
			}
		}
		return goodEnoughIfWeCantFindBetter;
	}
	
	public Phone getWorkPhone(LinkSubjectStudy subject) {
		Phone result = null;
		Criteria criteria = getSession().createCriteria(Phone.class);
		criteria.add(Restrictions.eq("person", subject.getPerson()));
		criteria.createAlias("phoneType", "pt");
		criteria.add(Restrictions.eq("pt.name", "Work"));
		criteria.setMaxResults(1);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("areaCode"), "areaCode");
		projectionList.add(Projections.property("phoneNumber"), "phoneNumber");
		criteria.setProjection(projectionList); // only return fields required for report
		criteria.setResultTransformer(Transformers.aliasToBean(Phone.class));

		if (criteria.uniqueResult() != null) {
			result = (Phone) criteria.uniqueResult();
		}
		return result;
	}
	
	/**
	 * obviously you need it attached an preferably prefetched or you may take an exponential hit to the DB
	 * 
	 * @param lss
	 * @return
	 */
	public Phone getHomePhoneWithoutExponentialQueries(LinkSubjectStudy lss){
		Phone goodEnoughIfWeCantFindBetter = null;
		for(Phone phone : lss.getPerson().getPhones()){
			if(phone!=null && phone.getPhoneType()!=null && phone.getPhoneType().getName().equalsIgnoreCase("Home")){
				return phone;
			}
			else if(goodEnoughIfWeCantFindBetter == null){
				goodEnoughIfWeCantFindBetter = phone;
			}
		}
		return goodEnoughIfWeCantFindBetter;
	}
	
	public Phone getHomePhone(LinkSubjectStudy subject) {
		Phone result = null;
		Criteria criteria = getSession().createCriteria(Phone.class);
		criteria.add(Restrictions.eq("person", subject.getPerson()));
		criteria.createAlias("phoneType", "pt");
		criteria.add(Restrictions.eq("pt.name", "Home"));
		criteria.setMaxResults(1);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("areaCode"), "areaCode");
		projectionList.add(Projections.property("phoneNumber"), "phoneNumber");
		criteria.setProjection(projectionList); // only return fields required for report
		criteria.setResultTransformer(Transformers.aliasToBean(Phone.class));

		if (criteria.uniqueResult() != null) {
			result = (Phone) criteria.uniqueResult();
		}
		return result;
	}
	


	public List<LinkSubjectStudy> getSubjectsMatchingComponentConsent(ConsentDetailsReportVO cdrVO){
		
		String qs = " select lss from LinkSubjectStudy lss " +
				"  left join fetch lss.person p " +
				"  left join fetch p.addresses a " +
				"  left join fetch p.phones ps " +
				"  left join fetch lss.consents c " +
				" where " +
				" lss.study =:study ";

		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			qs = qs + " and lss.id =:id ";
					
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			qs = qs + " and lss.subjectStatus=:subjectStatus " ;
		}
		

		Query query = getSession().createQuery((qs + "order by lss.id "));
		query.setParameter("study", cdrVO.getLinkSubjectStudy().getStudy());
		
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			query.setParameter("id", cdrVO.getLinkSubjectStudy().getId());
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			query.setParameter("subjectStatus", cdrVO.getLinkSubjectStudy().getSubjectStatus());
		}
/*		next stage...just prefetch for now
  		if (cdrVO.getConsentStatus() != null){
			q.setParameter("studyId", cdrVO.getLinkSubjectStudy().getStudy());	
		}
		if (cdrVO.getStudyComp() != null){
			q.setParameter("studyId", cdrVO.getLinkSubjectStudy().getStudy());
		}*/
		
		List<LinkSubjectStudy> results = query.list();
		return results;
	}
	

	public List<LinkSubjectStudy> getSubjects(ConsentDetailsReportVO cdrVO) {
		Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_STUDY, cdrVO.getLinkSubjectStudy().getStudy()));
		if (cdrVO.getLinkSubjectStudy().getSubjectUID() != null) {
			criteria.add(Restrictions.ilike(Constants.LINKSUBJECTSTUDY_SUBJECTUID, cdrVO.getLinkSubjectStudy().getSubjectUID(), MatchMode.ANYWHERE));
		}
		if (cdrVO.getLinkSubjectStudy().getSubjectStatus() != null) {
			criteria.add(Restrictions.eq(Constants.LINKSUBJECTSTUDY_SUBJECTSTATUS, cdrVO.getLinkSubjectStudy().getSubjectStatus()));
		}
		criteria.addOrder(Order.asc("subjectUID"));
		List<LinkSubjectStudy> results = criteria.list();
		return results;
	}

	public Consent getStudyCompConsent(Consent consent) {
		// Note: Should never be possible to have more than one Consent record for a
		// given a particular subject and study component
		Criteria criteria = getSession().createCriteria(Consent.class);
		if (consent != null) {
			criteria.add(Restrictions.eq("study.id", consent.getStudy().getId()));
			// must only get consents for subject in context
			criteria.add(Restrictions.eq("linkSubjectStudy.id", consent.getLinkSubjectStudy().getId()));
			// must only get consents for specific studyComp
			criteria.add(Restrictions.eq("studyComp.id", consent.getStudyComp().getId()));
			// Do NOT constrain against consentStatus or consentDate here, because we want to be able to
			// tell if they are "Not Consented" vs "Consented" with different consentStatus or consentDate.
			// if (consent.getConsentStatus() != null)
			// {
			// criteria.add(Restrictions.eq("consentStatus.id", consent.getConsentStatus().getId()));
			// }
			//
			// if (consent.getConsentDate() != null)
			// {
			// criteria.add(Restrictions.eq("consentDate", consent.getConsentDate()));
			// }

		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("studyComp"), "studyComp");
		projectionList.add(Projections.property("consentStatus"), "consentStatus");
		projectionList.add(Projections.property("consentDate"), "consentDate");
		criteria.setProjection(projectionList);
		criteria.setMaxResults(1);
		criteria.setResultTransformer(Transformers.aliasToBean(Consent.class));
		Consent result = (Consent) criteria.uniqueResult();
		return result;
	}

	public List<PhenoDataSetCollection> getPhenoCollectionList(Study study) {
		List<PhenoDataSetCollection> results = null;
		Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class);
		criteria.add(Restrictions.eq("study", study));
		results = criteria.list();
		return results;
	}

	//TODO review
	public List<FieldDetailsDataRow> getPhenoFieldDetailsList(FieldDetailsReportVO fdrVO) {
		List<FieldDetailsDataRow> results = new ArrayList<FieldDetailsDataRow>();
		Criteria criteria = getSession().createCriteria(PhenoDataSetCollection.class, "fpc");
		criteria.createAlias("phenoCollection", "pc"); // Inner join to Field
		criteria.createAlias("field", "f"); // Inner join to Field
		criteria.createAlias("f.fieldType", "ft"); // Inner join to FieldType
		criteria.add(Restrictions.eq("study", fdrVO.getStudy()));
		if (fdrVO.getPhenoCollection() != null) {
			criteria.add(Restrictions.eq("phenoCollection", fdrVO.getPhenoCollection()));
		}
		if (fdrVO.getFieldDataAvailable()) {
			DetachedCriteria fieldDataCriteria = DetachedCriteria.forClass(PhenoDataSetData.class, "fd");
			// Join FieldPhenoCollection and FieldData on ID FK
			fieldDataCriteria.add(Property.forName("f.id").eqProperty("fd." + "field.id"));
			fieldDataCriteria.add(Property.forName("pc.id").eqProperty("fd." + "collection.id"));
			criteria.add(Subqueries.exists(fieldDataCriteria.setProjection(Projections.property("fd.id"))));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("pc.name"), "collection");
		projectionList.add(Projections.property("f.name"), "fieldName");
		projectionList.add(Projections.property("f.description"), "description");
		projectionList.add(Projections.property("f.minValue"), "minValue");
		projectionList.add(Projections.property("f.maxValue"), "maxValue");
		projectionList.add(Projections.property("f.encodedValues"), "encodedValues");
		projectionList.add(Projections.property("f.missingValue"), "missingValue");
		projectionList.add(Projections.property("f.units"), "units");
		projectionList.add(Projections.property("ft.name"), "type");
		criteria.setProjection(projectionList); // only return fields required for report
		criteria.setResultTransformer(Transformers.aliasToBean(FieldDetailsDataRow.class));
		criteria.addOrder(Order.asc("pc.id"));
		criteria.addOrder(Order.asc("f.name"));
		results = criteria.list();

		return results;
	}
	
	public List<CustomFieldDetailsDataRow> getPhenoCustomFieldDetailsList(CustomFieldDetailsReportVO fdrVO) {
		List<CustomFieldDetailsDataRow> results = new ArrayList<CustomFieldDetailsDataRow>();
		if (fdrVO.getCustomFieldDisplay() != null) {
			/*
			 * Following query returns customFields whether or not they are 
			 * associated with a customFieldGroups (via customFieldDisplay)
			 */
			Criteria criteria = getSession().createCriteria(CustomField.class, "cf");
			criteria.createAlias("customFieldDisplay", "cfd", JoinType.LEFT_OUTER_JOIN);	// Left join to CustomFieldDisplay
			criteria.createAlias("cfd.customFieldGroup", "cfg", JoinType.LEFT_OUTER_JOIN); // Left join to CustomFieldGroup
			criteria.createAlias("fieldType", "ft", JoinType.LEFT_OUTER_JOIN); // Left join to FieldType
			criteria.createAlias("unitType", "ut", JoinType.LEFT_OUTER_JOIN); // Left join to UnitType
			criteria.add(Restrictions.eq("cf.study", fdrVO.getStudy()));
			ArkFunction function = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
			criteria.add(Restrictions.eq("cf.arkFunction", function));

			if (fdrVO.getCustomFieldDisplay().getCustomFieldGroup() != null) {
				criteria.add(Restrictions.eq("cfg.id", fdrVO.getCustomFieldDisplay().getCustomFieldGroup().getId()));
			}
			if (fdrVO.getFieldDataAvailable()) {
				DetachedCriteria fieldDataCriteria = DetachedCriteria.forClass(PhenoDataSetData.class, "pd");
				// Join CustomFieldDisplay and PhenoData on ID FK
				fieldDataCriteria.add(Property.forName("cfd.id").eqProperty("pd." + "customFieldDisplay.id"));
				criteria.add(Subqueries.exists(fieldDataCriteria.setProjection(Projections.property("pd.customFieldDisplay"))));
			}
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("cfg.name"), "questionnaire");
			projectionList.add(Projections.property("cf.name"), "fieldName");
			projectionList.add(Projections.property("cf.description"), "description");
			projectionList.add(Projections.property("cf.minValue"), "minValue");
			projectionList.add(Projections.property("cf.maxValue"), "maxValue");
			projectionList.add(Projections.property("cf.encodedValues"), "encodedValues");
			projectionList.add(Projections.property("cf.missingValue"), "missingValue");
			projectionList.add(Projections.property("ut.name"), "units");
			projectionList.add(Projections.property("ft.name"), "type");

			criteria.setProjection(projectionList); // only return fields required for report
			criteria.setResultTransformer(Transformers.aliasToBean(CustomFieldDetailsDataRow.class));
			criteria.addOrder(Order.asc("cfg.id"));
			criteria.addOrder(Order.asc("cfd.sequence"));
			results = criteria.list();
		}

		return results;
	}

	public List<PhenoDataSetFieldDetailsDataRow> getPhenoDataSetFieldDetailsList(PhenoDataSetFieldDetailsReportVO reportVO) {
		List<PhenoDataSetFieldDetailsDataRow> results = new ArrayList<PhenoDataSetFieldDetailsDataRow>();
		if (reportVO.getPhenoDataSetFieldDisplay() != null) {
			/*
			 * Following query returns customFields whether or not they are
			 * associated with a customFieldGroups (via customFieldDisplay)
			 */
			Criteria criteria = getSession().createCriteria(PhenoDataSetField.class, "pf");
			/*On 2016-08-30 found out this criteria can not have a property type call phenoDatasetFieldDisplay.there is a bug here but didn't fix this 
			/*Caused by: org.hibernate.QueryException: could not resolve property: phenoDatasetFieldDisplay of: au.org.theark.core.model.pheno.entity.PhenoDataSetField*/
			criteria.createAlias("phenoDatasetFieldDisplay", "pdfd", JoinType.LEFT_OUTER_JOIN);	// Left join to CustomFieldDisplay
			criteria.createAlias("pdfd.phenoDatasetFieldGroup", "pdfg", JoinType.LEFT_OUTER_JOIN); // Left join to CustomFieldGroup
			criteria.createAlias("fieldType", "ft", JoinType.LEFT_OUTER_JOIN); // Left join to FieldType
			criteria.createAlias("unitType", "ut", JoinType.LEFT_OUTER_JOIN); // Left join to UnitType
			criteria.add(Restrictions.eq("pf.study", reportVO.getStudy()));
			ArkFunction function = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
			criteria.add(Restrictions.eq("pf.arkFunction", function));

			if (reportVO.getPhenoDataSetFieldDisplay().getPhenoDataSetGroup() != null) {
				criteria.add(Restrictions.eq("pdfg.id", reportVO.getPhenoDataSetFieldDisplay().getPhenoDataSetGroup().getId()));
			}
			if (reportVO.getFieldDataAvailable()) {
				DetachedCriteria fieldDataCriteria = DetachedCriteria.forClass(PhenoDataSetData.class, "pd");
				// Join CustomFieldDisplay and PhenoData on ID FK
				fieldDataCriteria.add(Property.forName("pdfd.id").eqProperty("pd." + "phenoDatasetFieldDisplay.id"));
				criteria.add(Subqueries.exists(fieldDataCriteria.setProjection(Projections.property("pd.phenoDatasetFieldDisplay"))));
			}

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("pdfg.name"), "questionnaire");
			projectionList.add(Projections.property("pf.name"), "fieldName");
			projectionList.add(Projections.property("pf.description"), "description");
			projectionList.add(Projections.property("pf.minValue"), "minValue");
			projectionList.add(Projections.property("pf.maxValue"), "maxValue");
			projectionList.add(Projections.property("pf.encodedValues"), "encodedValues");
			projectionList.add(Projections.property("pf.missingValue"), "missingValue");
			projectionList.add(Projections.property("ut.name"), "units");
			projectionList.add(Projections.property("ft.name"), "type");

			criteria.setProjection(projectionList); // only return fields required for report
			criteria.setResultTransformer(Transformers.aliasToBean(PhenoDataSetFieldDetailsDataRow.class));
			criteria.addOrder(Order.asc("pdfg.id"));
			criteria.addOrder(Order.asc("pdfd.sequence"));
			results = criteria.list();
		}

		return results;
	}

	protected ArkFunction getArkFunctionByName(String functionName) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("name", functionName));
		criteria.setMaxResults(1);
		ArkFunction arkFunction = (ArkFunction) criteria.uniqueResult();
		return arkFunction;
	}

	@SuppressWarnings("unchecked")
	public List<StudyUserRolePermissionsDataRow> getStudyUserRolePermissions(Study study) {
		String sqlString = "SELECT * FROM `study`.`study_user_role_permission_view` WHERE studyName = :studyName"; 
		Query q = getSession().createSQLQuery(sqlString);
		q.setParameter("studyName", study.getName());
		q.setResultTransformer(Transformers.aliasToBean(StudyUserRolePermissionsDataRow.class));
		return q.list();
	}

	public List<PhenoDataSetGroup> getQuestionnaireList(Study study) {
		List<PhenoDataSetGroup> results = null;
		Criteria criteria = getSession().createCriteria(PhenoDataSetGroup.class);
		criteria.add(Restrictions.eq("study", study));
		ArkFunction function = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		criteria.add(Restrictions.eq("arkFunction", function));
		results = criteria.list();
		return results;
	}
	
	
	public List<ResearcherCostDataRow> getResearcherBillableItemTypeCostData(final ResearcherCostResportVO researcherCostResportVO){
		List<ResearcherCostDataRow> results = new ArrayList<ResearcherCostDataRow>();
		Criteria criteria = getSession().createCriteria(BillableItem.class, "bi");
		criteria.createAlias("workRequest", "wr", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("billableItemType", "bit", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bit.billableItemTypeStatus", "bitst", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("wr.researcher", "re", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("re.id", researcherCostResportVO.getResearcherId()));
		criteria.add(Restrictions.eq("bi.studyId", researcherCostResportVO.getStudyId()));
		criteria.add(Restrictions.eq("bi.invoice", researcherCostResportVO.getInvoice()));
		criteria.add(Restrictions.le("bi.commenceDate", researcherCostResportVO.getToDate()));
		criteria.add(Restrictions.ge("bi.commenceDate", researcherCostResportVO.getFromDate()));
		criteria.add(Restrictions.eq("bitst.name", "ACTIVE"));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("bit.id")); 
		projectionList.add(Projections.property("bit.itemName"), "costType");
		projectionList.add(Projections.sum("bi.totalCost"), "totalCost");
		projectionList.add(Projections.sum("bi.totalGST"), "totalGST");
		
		criteria.setProjection(projectionList); // only return fields required for report
		criteria.setResultTransformer(Transformers.aliasToBean(ResearcherCostDataRow.class));
		
		criteria.addOrder(Order.asc("bit.itemName"));
		results=criteria.list();
		return results;
	}
	
	public List<Researcher> searchResearcherByStudyId(final Long studyId) {
		Criteria criteria = getSession().createCriteria(Researcher.class);
		criteria.add(Restrictions.eq("studyId" , studyId));
		List<Researcher> list = criteria.list();
		return list;
	}

	public List<ResearcherDetailCostDataRow> getBillableItemDetailCostData(
			ResearcherCostResportVO researcherCostResportVO) {
		List<ResearcherDetailCostDataRow> results = new ArrayList<ResearcherDetailCostDataRow>();
		Criteria criteria = getSession().createCriteria(BillableItem.class, "bi");
		criteria.createAlias("workRequest", "wr", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("billableItemType", "bit", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("wr.researcher", "re", JoinType.LEFT_OUTER_JOIN);
		
		if(researcherCostResportVO.getResearcherId() !=null){
			criteria.add(Restrictions.eq("re.id", researcherCostResportVO.getResearcherId()));
		}
		
		criteria.add(Restrictions.eq("bi.studyId", researcherCostResportVO.getStudyId()));
		criteria.add(Restrictions.eq("bi.invoice", researcherCostResportVO.getInvoice()));
		criteria.add(Restrictions.le("bi.commenceDate", researcherCostResportVO.getToDate()));
		criteria.add(Restrictions.ge("bi.commenceDate", researcherCostResportVO.getFromDate()));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("bi.description"), "description");
		projectionList.add(Projections.property("bi.commenceDate"), "commencedDate");
		projectionList.add(Projections.property("bi.invoice"), "invoice");
		projectionList.add(Projections.property("bi.quantity"), "quantity");
		projectionList.add(Projections.property("bi.totalCost"), "totalAmount");
		projectionList.add(Projections.property("bi.totalGST"), "totalGST");
		projectionList.add(Projections.property("bit.itemName"), "itemType");
		projectionList.add(Projections.property("bit.id"), "typeId");
		projectionList.add(Projections.property("bit.quantityType"), "quantityType");
		projectionList.add(Projections.property("wr.gstAllow"), "gstAllowed");
		
		projectionList.add(Projections.property("wr.name"), "requestName");

		criteria.setProjection(projectionList); // only return fields required for report
		criteria.setResultTransformer(Transformers.aliasToBean(ResearcherDetailCostDataRow.class));
		criteria.addOrder(Order.asc("bit.id"));
		criteria.addOrder(Order.asc("bi.commenceDate"));
		results=criteria.list();
		return results;
	}
	/*
	private Date toStartOfYear(int year) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, year);
	    calendar.set(Calendar.WEEK_OF_YEAR, 1);
	    calendar.set(Calendar.DAY_OF_WEEK,1);
	    return calendar.getTime();
	}

	private Date toEndOfYear(int year) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, year);
	    calendar.set(Calendar.MONTH,11);
	    calendar.set(Calendar.DAY_OF_MONTH,31);
	    return calendar.getTime();
	}
*/

	public List<BiospecimenSummaryDataRow> getBiospecimenSummaryData(BiospecimenSummaryReportVO biospecimenSummaryReportVO) {
		List<BiospecimenSummaryDataRow> results = new ArrayList<BiospecimenSummaryDataRow>();

		Criteria criteria = getSession().createCriteria(BioTransaction.class, "bt");
		
		criteria.createAlias("status","bts",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("biospecimen","bs",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.study", "st", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.linkSubjectStudy", "lss", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.sampleType", "sat", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("st.id", biospecimenSummaryReportVO.getStudy().getId()));
		if(biospecimenSummaryReportVO.getSubjectUID() !=null){
			criteria.add(Restrictions.eq("lss.subjectUID", biospecimenSummaryReportVO.getSubjectUID()));
		}
	
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.min("bt.id"));
		projectionList.add(Projections.groupProperty("bs.id"));
		
		projectionList.add(Projections.property("st.name"), "studyName");
		projectionList.add(Projections.property("lss.subjectUID"), "subjectUId");
		projectionList.add(Projections.property("bs.id"), "biospecimenId");
		projectionList.add(Projections.property("bs.biospecimenUid"), "biospecimenUid");
		projectionList.add(Projections.property("bs.parentUid"), "parentId");
		projectionList.add(Projections.property("sat.name"), "sampleType");
		projectionList.add(Projections.property("bs.quantity"), "quantity");
		projectionList.add(Projections.property("bts.name"), "initialStatus");
	
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(BiospecimenSummaryDataRow.class));
		criteria.addOrder(Order.asc("lss.subjectUID"));
		criteria.addOrder(Order.asc("bs.biospecimenUid"));
		results=criteria.list();
	
		return results;
	}

	public List<BiospecimenDetailsDataRow> getBiospecimenDetailsData(BiospecimenDetailsReportVO biospecimenDetailReportVO) {
		List<BiospecimenDetailsDataRow> results = new ArrayList<BiospecimenDetailsDataRow>();

		Criteria criteria = getSession().createCriteria(BioTransaction.class, "bt");
		
		criteria.createAlias("status","bts",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("biospecimen","bs",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.study", "st", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.bioCollection", "bc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.linkSubjectStudy", "lss", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.sampleType", "sat", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bs.invCell", "inc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("inc.invBox", "inb", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("inb.invRack", "inr", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("inr.invFreezer", "inf", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("inf.invSite", "ins", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("st.id", biospecimenDetailReportVO.getStudy().getId()));
		if(biospecimenDetailReportVO.getSubjectUID() !=null){
			criteria.add(Restrictions.eq("lss.subjectUID", biospecimenDetailReportVO.getSubjectUID()));
		}
	
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.min("bt.id"));
		projectionList.add(Projections.groupProperty("bs.id"));
		
		projectionList.add(Projections.property("st.name"), "studyName");
		projectionList.add(Projections.property("lss.subjectUID"), "subjectUId");
		projectionList.add(Projections.property("bs.id"), "biospecimenId");
		projectionList.add(Projections.property("bc.biocollectionUid") , "biocollectionUid");
		projectionList.add(Projections.property("bs.sampleDate"), "sampleDate");
		projectionList.add(Projections.property("bs.biospecimenUid"), "biospecimenUid");
		projectionList.add(Projections.property("bs.parentUid"), "parentId");
		projectionList.add(Projections.property("sat.name"), "sampleType");
		projectionList.add(Projections.property("bs.quantity"), "quantity");
		projectionList.add(Projections.property("bts.name"), "initialStatus");

		projectionList.add(Projections.property("inb.name"), "box");
		projectionList.add(Projections.property("inr.name"), "rack");
		projectionList.add(Projections.property("inf.name"), "freezer");
		projectionList.add(Projections.property("ins.name"), "site");
	
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(BiospecimenDetailsDataRow.class));
		criteria.addOrder(Order.asc("lss.subjectUID"));
		criteria.addOrder(Order.asc("bs.biospecimenUid"));
		results=criteria.list();
	
		return results;
	}

	public List<StudyComponentDetailsDataRow> getStudyComponentDataRow(StudyComponentReportVO studyComponentReportVO) {
		
		List<StudyComponentDetailsDataRow> results = new ArrayList<StudyComponentDetailsDataRow>();
		//List<Consent> results=null;
		Criteria criteria = getSession().createCriteria(Consent.class,"consent");
		criteria.createAlias("consent.linkSubjectStudy", "lss", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.subjectStatus", "subjectStatus", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("lss.person", "person", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("consent.studyComponentStatus", "scs", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("study", studyComponentReportVO.getConsent().getStudy()));
		criteria.add(Restrictions.eq("studyComp", studyComponentReportVO.getConsent().getStudyComp()));
		criteria.add(Restrictions.eq("studyComponentStatus", studyComponentReportVO.getConsent().getStudyComponentStatus()));
		if(studyComponentReportVO.getFromDate()!=null && studyComponentReportVO.getToDate()!=null){
			if(studyComponentReportVO.getConsent().getStudyComponentStatus().getName().equals(Constants.STUDY_STATUS_COMPLETED)){
				criteria.add(Restrictions.between("completedDate",studyComponentReportVO.getFromDate() , studyComponentReportVO.getToDate()));
			}else if(studyComponentReportVO.getConsent().getStudyComponentStatus().getName().equals(Constants.STUDY_STATUS_REQUESTED)){
				criteria.add(Restrictions.between("requestedDate",studyComponentReportVO.getFromDate() , studyComponentReportVO.getToDate()));
			}else if(studyComponentReportVO.getConsent().getStudyComponentStatus().getName().equals(Constants.STUDY_STATUS_RECEIVED)){
				criteria.add(Restrictions.between("receivedDate",studyComponentReportVO.getFromDate() , studyComponentReportVO.getToDate()));
			}
		}
		criteria.add(Restrictions.eq("consentStatus", studyComponentReportVO.getConsent().getConsentStatus()));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("lss.subjectUID"), "subjectUID");
		projectionList.add(Projections.property("subjectStatus.name"), "subjectStatus");
		projectionList.add(Projections.property("person.firstName"), "firstName");
		projectionList.add(Projections.property("person.lastName"), "lastName");
		projectionList.add(Projections.property("person.dateOfBirth"), "dateOfBirth");
		projectionList.add(Projections.property("completedDate"), "completedDate");
		projectionList.add(Projections.property("requestedDate"), "requestedDate");
		projectionList.add(Projections.property("receivedDate"), "receivedDate");
		projectionList.add(Projections.property("scs.name"), "componentStatus");
		projectionList.add(Projections.property("person.id"), "personId");
		criteria.setProjection(projectionList);
		criteria.addOrder(Order.asc("lss.subjectUID"));
		criteria.setResultTransformer(Transformers.aliasToBean(StudyComponentDetailsDataRow.class));
		results=(criteria.list());
		return results;
	}
	
}
