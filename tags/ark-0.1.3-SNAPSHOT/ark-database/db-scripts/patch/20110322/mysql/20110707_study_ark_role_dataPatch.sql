-- Module/Role for Genotypic
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (27, 'GENO_COLLECTION', 'Genotypic Collection use case. This is represented by the Collection tab, under the main Genotypic Menu', 1, 'tab.module.geno.collection');
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES (11, 'Geno Read-Only User');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (27, 4, 27, 1);
INSERT INTO `study`.`ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES (10, 4, 11);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (104, 11, 4, 27, 2);

-- Read and update permissions on FieldData for Pheno Data Manager
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (105, 8, 3, 14, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (106, 8, 3, 14, 2);

-- Subject Read-Only for Correspondence
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (107, 6, 2, 21, 2);

-- Subject Data Manager for Correspondence
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (108, 5, 2, 21, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (109, 5, 2, 21, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (110, 5, 2, 21, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (111, 5, 2, 21, 4);

-- Subject Administrator for Correspondence
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (112, 4, 2, 21, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (113, 4, 2, 21, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (114, 4, 2, 21, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (115, 4, 2, 21, 4);

-- Module/Role for LIMS Administrator
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES (12, 'LIMS Administrator');
INSERT INTO `study`.`ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES (11, 5, 12);

-- Module/Role for Phenotypic Administrator
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES (13, 'Pheno Administrator');
INSERT INTO `study`.`ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES (12, 3, 13);

-- LIMS Administrator permissions
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (116,12,5,17,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (117,12,5,17,2);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (118,12,5,17,3);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (119,12,5,17,4);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (120,12,5,18,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (121,12,5,18,2);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (122,12,5,18,3);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (123,12,5,18,4);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (124,12,5,19,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (125,12,5,19,2);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (126,12,5,19,3);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (127,12,5,19,4);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (128,12,5,20,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (129,12,5,20,2);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (130,12,5,20,3);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (131,12,5,20,4);

-- Pheno Administrator permissions
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (132,13,3,12,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (133,13,3,13,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (134,13,3,14,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (135,13,3,15,3);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (136,13,3,16,1);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (137,13,3,22,4);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (138,13,3,26,2);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (139,13,3,14,3);
INSERT INTO `ark_role_policy_template` (`ID`,`ARK_ROLE_ID`,`ARK_MODULE_ID`,`ARK_FUNCTION_ID`,`ARK_PERMISSION_ID`) VALUES (140,13,3,14,2);

-- Modify Pheno Data Manager permissions (read only Collection and Data Dictionary)
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`=2 WHERE `ID`='70';
DELETE FROM `study`.`ark_role_policy_template` WHERE `ID`='105';

-- Modify Pheno Read Only user
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`=2 WHERE ark_role_id=7;
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`=3 WHERE id=90;
