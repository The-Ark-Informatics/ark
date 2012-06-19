USE study;
INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('SUBJECT_CUSTOM_FIELD_UPLOAD', 'Uploader for Subject Custom Fields', 1, 'tab.module.subject.subjectCustomFieldUpload');
INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BIOCOLLECTION_CUSTOM_FIELD_UPLOAD', 'Uploader for BioCollection Custom Fields', 1, 'tab.module.lims.bioCollectionCustomFieldUpload');
INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BIOSPECIMEN_CUSTOM_FIELD_UPLOAD', 'Uploader for Biospecimen Custom Fields', 1, 'tab.module.lims.biospecimenCustomFieldUpload');

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (2, (SELECT ID FROM `study`.`ark_function` WHERE name = 'SUBJECT_CUSTOM_FIELD_UPLOAD'), 50);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (4, 2, (SELECT ID FROM `study`.`ark_function` WHERE name = 'SUBJECT_CUSTOM_FIELD_UPLOAD'), 1);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (4, 2, (SELECT ID FROM `study`.`ark_function` WHERE name = 'SUBJECT_CUSTOM_FIELD_UPLOAD'), 2);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (4, 2, (SELECT ID FROM `study`.`ark_function` WHERE name = 'SUBJECT_CUSTOM_FIELD_UPLOAD'), 3);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (4, 2, (SELECT ID FROM `study`.`ark_function` WHERE name = 'SUBJECT_CUSTOM_FIELD_UPLOAD'), 4);

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOCOLLECTION_CUSTOM_FIELD_UPLOAD'), 50);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOCOLLECTION_CUSTOM_FIELD_UPLOAD'), 1);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOCOLLECTION_CUSTOM_FIELD_UPLOAD'), 2);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOCOLLECTION_CUSTOM_FIELD_UPLOAD'), 3);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOCOLLECTION_CUSTOM_FIELD_UPLOAD'), 4);

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOSPECIMEN_CUSTOM_FIELD_UPLOAD'), 50);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOSPECIMEN_CUSTOM_FIELD_UPLOAD'), 1);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOSPECIMEN_CUSTOM_FIELD_UPLOAD'), 2);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOSPECIMEN_CUSTOM_FIELD_UPLOAD'), 3);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOSPECIMEN_CUSTOM_FIELD_UPLOAD'), 4);
