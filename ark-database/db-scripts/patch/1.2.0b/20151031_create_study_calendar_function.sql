INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('CALENDAR', 'Initiate the study calendars', '1', 'tab.module.study.calendar');

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` ='STUDY'), (SELECT `ID` FROM `STUDY`.`ARK_FUNCTION` WHERE `NAME` ='CALENDAR'), '9');
