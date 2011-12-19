-- Roles
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES ('7', 'Pheno Read-Only user');
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES ('8', 'Pheno Data Manager');
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES ('9', 'LIMS Read-Only user');
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES ('10', 'LIMS Data Manager');

-- Module/Roles
INSERT INTO `study`.`ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES ('6', '3', '7');
INSERT INTO `study`.`ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES ('7', '3', '8');
INSERT INTO `study`.`ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES ('8', '5', '9');
INSERT INTO `study`.`ark_module_role` (`ID`, `ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES ('9', '5', '10');

-- Functions
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('12', 'DATA_DICTIONARY', 'Phenotypic Data Dictionary use case. This is represented by the Data Dictionary tab, under the main Phenotypic Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('13', 'DATA_DICTIONARY_UPLOAD', 'Phenotypic Data Dictionary Upload use case. This is represented by the Data Dictionary Upload tab, under the main Phenotypic Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('14', 'PHENO_COLLECTION', 'Phenotypic Collection use case. This is represented by the Collection tab, under the main Phenotypic Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('15', 'FIELD_DATA', 'Phenotypic Field Data use case. This is represented by the Field Data tab, under the main Phenotypic Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('16', 'FIELD_DATA_UPLOAD', 'Phenotypic Field Data Upload use case. This is represented by the Data Upload tab, under the main Phenotypic Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('17', 'LIMS_SUBJECT', 'LIMS Subject use case. This is represented by the Subject tab, under the main LIMS Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('18', 'LIMS_COLLECTION', 'LIMS Collection use case. This is represented by the Collection tab, under the main LIMS Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('19', 'BIOSPECIMEN', 'LIMS Biospecimen use case. This is represented by the Biospecimen tab, under the main LIMS Tab.', '1');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES ('20', 'INVENTORY', 'LIMS Inventory use case. This is represented by the Inventory tab, under the main LIMS Tab.', '1');

-- Pheno policy template
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (70,8,3,12,1);
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (71,8,3,13,1);
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (72,8,3,14,1);
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (73,8,3,15,2);
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (74,8,3,16,1);

-- LIMS policy template
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (75,10,5,17,2);
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (76,10,5,18,1);
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (77,10,5,19,1);
INSERT INTO `study`.`ark_role_policy_template` (ID,ARK_ROLE_ID,ARK_MODULE_ID,ARK_FUNCTION_ID,ARK_PERMISSION_ID) VALUES (78,10,5,20,1);
