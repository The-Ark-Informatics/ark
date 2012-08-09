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

import java.util.List;
import java.util.Map;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.model.report.entity.ReportTemplate;
import au.org.theark.core.model.report.entity.ResearcherBillableItemTypeCostDataRow;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.report.model.vo.ConsentDetailsReportVO;
import au.org.theark.report.model.vo.CustomFieldDetailsReportVO;
import au.org.theark.report.model.vo.FieldDetailsReportVO;
import au.org.theark.report.model.vo.ResearcherCostResportVO;
import au.org.theark.report.model.vo.report.ConsentDetailsDataRow;
import au.org.theark.report.model.vo.report.CustomFieldDetailsDataRow;
import au.org.theark.report.model.vo.report.FieldDetailsDataRow;
import au.org.theark.report.model.vo.report.StudyUserRolePermissionsDataRow;

public interface IReportService {

	public long getTotalSubjectCount(Study study);

	public Map<String, Long> getSubjectStatusCounts(Study study);

	public Map<String, Long> getStudyConsentCounts(Study study);

	public Map<String, Long> getStudyCompConsentCounts(Study study, StudyComp studyComp);

	public Long getWithoutStudyCompCount(Study study);

	// TODO: Revise getReportsAvailableList method when migration to new Ark security done
	public List<ReportTemplate> getReportsAvailableList(ArkUser arkUser, Study study);

	public List<ReportOutputFormat> getOutputFormats();

	public List<ConsentDetailsDataRow> getStudyLevelConsentDetailsList(ConsentDetailsReportVO cdrVO);

	public List<ConsentDetailsDataRow> getStudyCompConsentDetailsList(ConsentDetailsReportVO cdrVO);

	public List<PhenoCollection> getPhenoCollectionList(Study study);

	public List<FieldDetailsDataRow> getPhenoFieldDetailsList(FieldDetailsReportVO fdrVO);

	public List<CustomFieldDetailsDataRow> getPhenoCustomFieldDetailsList(CustomFieldDetailsReportVO fdrVO);

	public List<StudyUserRolePermissionsDataRow> getStudyUserRolePermissions(Study study);

	public List<CustomFieldGroup> getQuestionnaireList(Study study);

	public List<ConsentDetailsDataRow> getStudyLevelConsentDetailsDataRowList(ConsentDetailsReportVO cdrVO);
	
	public List<ResearcherBillableItemTypeCostDataRow> getBillableItemTypeCostData(final ResearcherCostResportVO researcherCostResportVO);
	
	public List<Researcher> searchResearcherByStudyId(final Long studyId);

}
