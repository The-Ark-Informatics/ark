USE study;

-- Add Pheno SUMMARY function
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('22', 'SUMMARY', 'Phenotypic Summary.', '1', 'tab.module.phenotypic.summary');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('22', '3', '22');

-- Adde permissions
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('89', '8', '3', '22', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('90', '7', '3', '22', '2');

-- Add LIMS_DATA_MANAGER delete permissions
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('91','10','5','17','4');
