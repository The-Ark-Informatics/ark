INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('ANALYSIS', 'Initiate the analysis based of available computational and data sources', '1', 'tab.module.genomics.analysis');

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` ='GENOMICS'), (SELECT `ID` FROM `STUDY`.`ARK_FUNCTION` WHERE `NAME` ='ANALYSIS'), '4');
