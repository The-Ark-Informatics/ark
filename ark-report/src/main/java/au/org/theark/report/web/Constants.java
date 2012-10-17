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
package au.org.theark.report.web;

public class Constants {

	public static final String	REPORT_MAIN_TAB							= "Reporting";
	public static final String	REPORT_DETAIL							= "Reports";
	public static final String	TAB_MODULE_REPORT_DETAIL				= "tab.module.report.detail";
	public static final String	REPORT_SUBMENU							= "ReportSubMenus";
	public static final String	DATA_EXTRACTION 						= "Data Extraction";
	public static final String	TAB_MODULE_DATA_EXTRACTION				= "tab.module.report.advanced";
	
	public static final String	LOAD_BUTTON								= "load";
	public static final String	GENERATE_BUTTON							= "generate";
	public static final String	REPORT_DROP_DOWN_CHOICE					= "report.reportChoice";
	public static final String	REPORT_OUTPUT_DROP_DOWN_CHOICE			= "report.outputFormatChoice";

	public static final String	REPORT_OUTPUT_NAME						= "name";

	public static final String	LINKSUBJECTSTUDY_SUBJECTUID				= "linkSubjectStudy.subjectUID";
	public static final String	LINKSUBJECTSTUDY_SUBJECTSTATUS			= "linkSubjectStudy.subjectStatus";
	public static final String	CONSENT_DATE							= "consentDate";
	public static final String	CONSENT_STATUS							= "consentStatus";
	public static final String	STUDY_COMP								= "studyComp";

	public static final String	PHENO_COLLECTION						= "phenoCollection";
	public static final String	FIELD_DATA_AVAILABLE					= "fieldDataAvailable";

	// TODO :: May become irrelevant
	public static final String	REPORT_NAME								= "reportTemplate.name";
	public static final String	LINK_STUDY_REPORT_TEMPLATE_KEY			= "id";
	
	public static final String	RESEARCHER_COST_REPORT_RESEARCHE	 	= "researcher";
	public static final String	RESEARCHER_COST_REPORT_YEAR 			= "year";
	public static final String	RESEARCHER_COST_REPORT_YEAR_PATTERN 	= "\\d{4}";
	
	public static final String	RESEARCHER_COST_REPORT_FROM_DATE 		= "fromDate";
	public static final String	RESEARCHER_COST_REPORT_TO_DATE 			= "toDate";
	
	public static final String	ERROR_RESEARCHER_COST_REPORT_RESEARCHER_REQUIRED 	= "error.researchercostreport.researcher.required";
	public static final String	RESEARCHER_COST_REPORT_RESEARCHER_TAG 				= "Researcher";
	
	public static final String	ERROR_RESEARCHER_COST_REPORT_FROM_DATE_REQUIRED 	= "error.researchercostreport.fromdate.required";
	public static final String	RESEARCHER_COST_REPORT_FROM_DATE_TAG 				= "Period Start Date";
	
	public static final String	ERROR_RESEARCHER_COST_REPORT_TO_DATE_REQUIRED 		= "error.researchercostreport.todate.required";
	public static final String	RESEARCHER_COST_REPORT_TO_DATE_TAG 					= "Period End Date";
	
	public static final String	ERROR_RESEARCHER_COST_REPORT_YEAR_REQUIRED 			= "error.researchercostreport.year.required";
	public static final String	ERROR_RESEARCHER_COST_REPORT_YEAR_INVALID_FORMAT 	= "error.researchercostreport.year.invalid.format";
	public static final String	RESEARCHER_COST_REPORT_YEAR_TAG 					= "Year";
	
	public static final String	BIOSPECIMEN_SUMMARY_REPORT_STUDY					= "study";
	public static final String	BIOSPECIMEN_SUMMARY_REPORT_SUBJECT_UID				= "subjectUID";
	
	public static final String	ERROR_BIOSPECIMEN_SUMMARY_REPORT_STUDY_REQUIRED 	= "error.biospecimensummaryreport.study.required";
	public static final String	BIOSPECIMEN_SUMMARY_REPORT_STUDY_TAG 				= "Study";
	
}
