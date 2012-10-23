UPDATE `reporting`.`report_template` SET `NAME`='Biospecimen Summary Report' WHERE `Name`='Biospecimen summary Report';

INSERT INTO `reporting`.`report_template` (`NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) VALUES ('Biospecimen Detail Report', 'This report lists the biospecimen study details for the selected study', 'BiospecimenDetailReport.jrxml',
(select id from study.ark_module where name = 'LIMS'), 
(SELECT id FROM study.ark_function where name = 'BIOSPECIMEN'));

