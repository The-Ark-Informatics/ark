USE study;

-- New function 23 for Study Summary Report 
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES (23, 'REPORT_STUDYSUMARY', 'Study Summary Report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>', 2);
-- Link new function to module 1 (Study)
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (23, 1, 23);
-- Link new function to role 2 (Study Admin)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (92, 2, 1, 23, 2);
-- Link new function to role 3 (Study RO)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (93, 3, 1, 23, 2);
-- Update the FK from report_template to match the function ID
UPDATE `reporting`.`report_template` SET `FUNCTION_ID`=23 WHERE `ID`='1';

-- New function 24 for Study-level Consent Details Report 
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES (24, 'REPORT_STUDYLEVELCONSENT', 'Study-level Consent Details Report lists detailed subject information for a particular study based on their consent status at the study-level.', 2);
-- Link new function to module 2 (Subject)
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (24, 2, 24);
-- Link new function to role 4 (Subject Admin)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (94, 4, 2, 24, 2);
-- Link new function to role 5 (Subject Data Mgr)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (95, 5, 2, 24, 2);
-- Link new function to role 6 (Subject RO)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (96, 6, 2, 24, 2);
-- Update the FK from report_template to match the function ID
UPDATE `reporting`.`report_template` SET `FUNCTION_ID`=24 WHERE `ID`='2';

-- New function 25 for Study Component Consent details Report 
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES (25, 'REPORT_STUDYCOMPCONSENT', 'Study Component Consent Details Report lists detailed subject information for a particular study based on their consent status for a specific study component.', 2);
-- Link new function to module 2 (Subject)
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (25, 2, 25);
-- Link new function to role 4 (Subject Admin)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (97, 4, 2, 25, 2);
-- Link new function to role 5 (Subject Data Mgr)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (98, 5, 2, 25, 2);
-- Link new function to role 6 (Subject RO)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (99, 6, 2, 25, 2);
-- Update the FK from report_template to match the function ID
UPDATE `reporting`.`report_template` SET `FUNCTION_ID`=25 WHERE `ID`='3';

-- New function 26 for 'Phenotypic Field Details Report (Data Dictionary)
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES (26, 'REPORT_PHENOFIELDDETAILS', 'Phenotypic Field Details Report (Data Dictionary) lists detailed field information for a particular study based on their associated phenotypic collection.', 2);
-- Link new function to module 3 (Phenotypic)
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (26, 3, 26);
-- Link new function to role 7 (Pheno RO)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (100, 7, 3, 26, 2);
-- Link new function to role 8 (Pheno Data Mgr)
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (101, 8, 3, 26, 2);
-- Update the FK from report_template to match the function ID
UPDATE `reporting`.`report_template` SET `FUNCTION_ID`=26 WHERE `ID`='4';

