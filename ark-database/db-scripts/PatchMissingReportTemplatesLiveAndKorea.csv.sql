/*
-- Query: select * from reporting.report_template
LIMIT 0, 10000

-- Date: 2014-12-22 14:11
*/
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (1,'Study Summary Report','This report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>','StudySummaryReport.jrxml',1,23);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (2,'Study-level Consent Details Report','This report lists detailed subject information for a particular study based on their consent status at the study-level.','ConsentDetailsReport.jrxml',2,24);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (3,'Study Component Consent Details Report','This report lists detailed subject information for a particular study based on their consent status for a specific study component.','ConsentDetailsReport.jrxml',2,25);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (4,'Datasets Field Details Report (Data Dictionary)','This report lists detailed field information for a particular study based on their associated phenotypic collection.','DataDictionaryReport.jrxml',3,26);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (5,'Study User Role Permissions Report','This report lists all user role and permissions for the study in context.','StudyUserRolePermissions.jrxml',1,33);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (6,'Work Researcher Cost Report','This report lists the invoiced total billable item type costs related to a researcher','ResearcherCostReport.jrxml',8,62);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (7,'Work Researcher Detail Cost Report','This report lists the invoiced individual billable item costs group by the billable item type related to a researcher','ResearcherDetailCostReport.jrxml',8,62);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (8,'Work Study Detail Cost Report','This report lists the invoiced individual billable item costs group by the billable item type related to context study','StudyDetailCostReport.jrxml',8,62);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (9,'Biospecimen Summary Report','This report lists the biospecimen summaries for the selected study','BiospecimenSummaryReport.jrxml',5,19);
INSERT INTO `report_template` (`ID`,`NAME`,`DESCRIPTION`,`TEMPLATE_PATH`,`MODULE_ID`,`FUNCTION_ID`) VALUES (10,'Biospecimen Detail Report','This report lists the biospecimen study details for the selected study - includes location information','BiospecimenDetailReport.jrxml',5,19);
