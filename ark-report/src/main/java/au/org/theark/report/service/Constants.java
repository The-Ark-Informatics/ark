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

public class Constants {

	// Service-level definitions
	public static final String	REPORT_SERVICE							= "reportService";

	// Constants to match database lookup tables
	public static final String	STUDY_SUMMARY_REPORT_NAME			= "Study Summary Report";
	public static final String	STUDY_COMP_CONSENT_REPORT_NAME	= "Study Component Consent Details Report";
	public static final String	STUDY_LEVEL_CONSENT_REPORT_NAME	= "Study-level Consent Details Report";
	public static final String	PHENO_FIELD_DETAILS_REPORT_NAME	= "Datasets Field Details Report (Data Dictionary)";
	public static final String	STUDY_USER_ROLE_PERMISSIONS	= "Study User Role Permissions Report";
	public static final String	WORK_RESEARCHER_COST_REPORT_NAME			= "Work Researcher Cost Report";
	public static final String	WORK_RESEARCHER_DETAIL_COST_REPORT_NAME		= "Work Researcher Detail Cost Report";
	public static final String	WORK_STUDY_DETAIL_COST_REPORT_NAME			= "Work Study Detail Cost Report";
	public static final String	LIMS_BIOSPECIMEN_SUMMARY_REPORT_NAME		= "Biospecimen Summary Report";
	public static final String	LIMS_BIOSPECIMEN_DETAIL_REPORT_NAME			= "Biospecimen Detail Report";

	public static final String	PDF_REPORT_FORMAT						= "PDF";
	public static final Object	CSV_REPORT_FORMAT						= "CSV";

	public static final String	NOT_CONSENTED							= "Not Consented";

	public static final String	LINKSUBJECTSTUDY_STUDY				= "study";
	public static final String	LINKSUBJECTSTUDY_SUBJECTUID		= "subjectUID";
	public static final String	LINKSUBJECTSTUDY_SUBJECTSTATUS	= "subjectStatus";
	public static final String	LINKSUBJECTSTUDY_CONSENT			= "consents";
	public static final String	LINKSUBJECTSTUDY_CONSENTSTATUS	= "consentStatus";
	public static final String	LINKSUBJECTSTUDY_CONSENTDATE		= "consentDate";
	public static final String	LINKSUBJECTSTUDY_PERSON				= "person";

	public static final String	CONSENT_STUDYCOMP						= "studyComp";
	public static final String	CONSENT_CONSENTSTATUS				= "consentStatus";
	public static final String	CONSENT_CONSENTDATE					= "consentDate";
	public static final String	CONSENT_LINKSUBJECTSTUDY			= "linkSubjectStudy";

	

}
