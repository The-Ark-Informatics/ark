UPDATE `reporting`.`report_template` SET `FUNCTION_ID`=(select id from study.ark_function where name='BILLABLE_ITEM') WHERE `name`='Work Researcher Cost Report';
UPDATE `reporting`.`report_template` SET `FUNCTION_ID`=(select id from study.ark_function where name='BILLABLE_ITEM') WHERE `name`='Work Researcher Detail Cost Report';
UPDATE `reporting`.`report_template` SET `FUNCTION_ID`=(select id from study.ark_function where name='BILLABLE_ITEM') WHERE `name`='Work Study Detail Cost Report';


UPDATE `reporting`.`report_template` SET `TEMPLATE_PATH`='ResearcherDetailCostReport.jrxml' WHERE `name`='Work Researcher Detail Cost Report';
