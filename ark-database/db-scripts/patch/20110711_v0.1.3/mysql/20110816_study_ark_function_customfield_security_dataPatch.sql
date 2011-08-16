USE study;

-- Adjust the original SUBJECT_CUSTOM to SUBJECT_CUSTOM_FIELD (management of the CFs available for all subjects)
UPDATE `study`.`ark_function` SET `NAME`='SUBJECT_CUSTOM_FIELD', `RESOURCE_KEY`='tab.module.subject.subjectcustomfield' WHERE `ID`='11';
-- Add a new function SUBJECT_CUSTOM_DATA (data entry for subject-specific CF data)
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (34, 'SUBJECT_CUSTOM_DATA', 'Data entry for Subject Custom Fields.', 1, 'tab.module.subject.subjectcustomdata');

-- Add the new function to the Subject module and also re-arrange the sequence of the sub-menu tabs
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (31, 2, 34, 2);
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=9 WHERE `ID`='11';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=8 WHERE `ID`='10';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=6 WHERE `ID`='9';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=5 WHERE `ID`='8';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='7';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='6';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=7 WHERE `ID`='12';
