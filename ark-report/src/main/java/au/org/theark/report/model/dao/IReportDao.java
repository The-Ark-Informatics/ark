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

import java.util.List;
import java.util.Map;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.report.model.vo.BiospecimenDetailsReportVO;
import au.org.theark.report.model.vo.BiospecimenSummaryReportVO;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.CustomFieldDetailsReportVO;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.model.vo.report.BiospecimenDetailsDataRow;
import au.org.theark.report.model.vo.report.BiospecimenSummaryDataRow;
import au.org.theark.report.model.vo.report.ConsentDetailsDataRow;
import au.org.theark.report.model.vo.report.CustomFieldDetailsDataRow;
import au.org.theark.report.model.vo.report.FieldDetailsDataRow;
import au.org.theark.report.model.vo.report.ResearcherCostDataRow;
import au.org.theark.report.model.vo.report.ResearcherDetailCostDataRow;
import au.org.theark.report.model.vo.report.StudyUserRolePermissionsDataRow;

public interface IReportDao {

	public long getTotalSubjectCount(Study study);

	public Map<String, Long> getSubjectStatusCounts(Study study);

	public Map<String, Long> getStudyConsentCounts(Study study);

	public Map<String, Long> getStudyCompConsentCounts(Study study, StudyComp studyComp);

	public Long getWithoutStudyCompCount(Study study);

	public List<ReportTemplate> getReportsForUser(ArkUser arkUser, Study study);

	public List<ReportOutputFormat> getOutputFormats();

	public List<LinkSubjectStudy> getStudyLevelConsentDetailsList(ConsentDetailsReportVO cdrVO);

	/**
	 * Gets a list of Consents for particular Study Component constrained by Consent Status/Date, but does not handle for "Not Consented". (i.e. not to
	 * be used to return a breakdown by Consent Status because that would require special handling for "Not Consented"). It will return the following
	 * fields into a ConsentDetailsDataRow bean: + subjectUID + consentStatus + consentDate
	 * 
	 * @param cdrVO
	 * @return
	 */
	public List<ConsentDetailsDataRow> getStudyCompConsentList(ConsentDetailsReportVO cdrVO);

	public Address getBestAddress(LinkSubjectStudy subject);

	public Phone getWorkPhone(LinkSubjectStudy subject);

	public Phone getHomePhone(LinkSubjectStudy subject);

	/**
	 * Gets a list of LinkSubjectStudy records for a given study and is constrained on: + subjectUID + subjectStatus
	 * 
	 * @param consent
	 * @return
	 */
	public List<LinkSubjectStudy> getSubjects(ConsentDetailsReportVO cdrVO);

	/**
	 * Gets the consent record for a given subject and studyComp. Does NOT constrain against consentStatus or consentDate here, because we want to be
	 * able to tell if they are "Not Consented" vs "Consented" with different consentStatus or consentDate.
	 * 
	 * @param consent
	 * @return
	 */
	public Consent getStudyCompConsent(Consent consent);

	public List<PhenoCollection> getPhenoCollectionList(Study study);

	public List<FieldDetailsDataRow> getPhenoFieldDetailsList(FieldDetailsReportVO fdrVO);

	public List<CustomFieldDetailsDataRow> getPhenoCustomFieldDetailsList(CustomFieldDetailsReportVO fdrVO);

	public List<StudyUserRolePermissionsDataRow> getStudyUserRolePermissions(Study study);

	public List<CustomFieldGroup> getQuestionnaireList(Study study);
	
	public List<ConsentDetailsDataRow> getStudyLevelConsentDetailsDataRowList(ConsentDetailsReportVO cdrVO);
	
	
	public List<ResearcherCostDataRow> getResearcherBillableItemTypeCostData(final ResearcherCostResportVO researcherCostResportVO);
	
	public List<Researcher> searchResearcherByStudyId(final Long studyId);
	
	public List<ResearcherDetailCostDataRow> getBillableItemDetailCostData(final ResearcherCostResportVO researcherCostResportVO);
	
	public List<BiospecimenSummaryDataRow> getBiospecimenSummaryData(final BiospecimenSummaryReportVO biospecimenSummaryReportVO);
	
	public List<BiospecimenDetailsDataRow> getBiospecimenDetailsData(final BiospecimenDetailsReportVO biospecimenDetailReportVO);

}
