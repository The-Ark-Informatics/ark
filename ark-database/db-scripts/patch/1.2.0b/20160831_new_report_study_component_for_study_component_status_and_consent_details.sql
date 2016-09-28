set @function_id = 0;
set @module_id = 0;

SELECT @module_id := id from `study`.`ark_module` where name='Subject';
SELECT @function_id := id from `study`.`ark_function` where name='REPORT_STUDYCOMPCONSENT';

INSERT INTO `reporting`.`report_template` (`NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) VALUES ('Study Component', 'Detailed subject information with respect to study component status and consent.', 'StudyComponentStatusAndConsentDetailsReport.jrxml', @module_id, @function_id);
