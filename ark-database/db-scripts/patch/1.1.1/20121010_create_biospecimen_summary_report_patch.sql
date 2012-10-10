INSERT INTO `reporting`.`report_template` (`NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) 
VALUES ('Biospecimen summary Report', 'This report lists the biospecimen summaries for the selected study', 'BiospecimenSummaryReport.jrxml', 
(select id from study.ark_module where name = 'LIMS'), 
(SELECT id FROM study.ark_function where name = 'BIOSPECIMEN'));
