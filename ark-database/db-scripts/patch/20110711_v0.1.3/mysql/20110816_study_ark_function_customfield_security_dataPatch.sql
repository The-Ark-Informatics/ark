USE study;

UPDATE `study`.`ark_function` SET `NAME`='SUBJECT_CUSTOM_FIELD', `RESOURCE_KEY`='tab.module.subject.subjectcustomfield' WHERE `ID`='11';
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (34, 'SUBJECT_CUSTOM_DATA', 'Data entry for Subject Custom Fields.', 1, 'tab.module.subject.subjectcustomdata');
