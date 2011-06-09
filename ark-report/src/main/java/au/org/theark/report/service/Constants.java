package au.org.theark.report.service;

public class Constants {

	// Service-level definitions
	public static final String REPORT_SERVICE = "reportService";
	
	// Constants to match database lookup tables
	public static final String STUDY_SUMMARY_REPORT_NAME = "Study Summary Report";
	public static final String STUDY_COMP_CONSENT_REPORT_NAME = "Study Component Consent Details Report";
	public static final String STUDY_LEVEL_CONSENT_REPORT_NAME = "Study-level Consent Details Report";
	public static final String PHENO_FIELD_DETAILS_REPORT_NAME = "Phenotypic Field Details Report (Data Dictionary)";

	public static final String PDF_REPORT_FORMAT = "PDF";
	public static final Object CSV_REPORT_FORMAT = "CSV";

	public static final String NOT_CONSENTED = "Not Consented";

	public static final String LINKSUBJECTSTUDY_STUDY = "study";
	public static final String LINKSUBJECTSTUDY_SUBJECTUID = "subjectUID";
	public static final String LINKSUBJECTSTUDY_SUBJECTSTATUS = "subjectStatus";
	public static final String LINKSUBJECTSTUDY_CONSENT = "consents";
	public static final String LINKSUBJECTSTUDY_CONSENTSTATUS = "consentStatus";
	public static final String LINKSUBJECTSTUDY_CONSENTDATE = "consentDate";
	public static final String LINKSUBJECTSTUDY_PERSON = "person";

	public static final String CONSENT_STUDYCOMP = "studyComp";
	public static final String CONSENT_CONSENTSTATUS = "consentStatus";
	public static final String CONSENT_CONSENTDATE = "consentDate";
	public static final String CONSENT_LINKSUBJECTSTUDY = "linkSubjectStudy";


}
