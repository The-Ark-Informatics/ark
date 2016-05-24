INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('COMPUTATION', 'Declare the computational resources for analysis', '1', 'tab.module.genomics.computation');

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` ='GENOMICS'), (SELECT `ID` FROM `STUDY`.`ARK_FUNCTION` WHERE `NAME` ='COMPUTATION'), '3');