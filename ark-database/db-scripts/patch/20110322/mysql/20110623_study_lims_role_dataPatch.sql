USE study;
-- Add "LIMS Read-Only user" permissions
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('79', '9', '5', '17', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('80', '9', '5', '18', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('81', '9', '5', '19', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('82', '9', '5', '20', '2');

-- Add "Pheno Read-Only user" permissions
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('83', '7', '3', '12', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('84', '7', '3', '13', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('85', '7', '3', '14', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('86', '7', '3', '15', '2');
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('87', '7', '3', '16', '2');

-- Add Pheno module/functions
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('13', '3', '12');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('14', '3', '13');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('15', '3', '14');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('16', '3', '15');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('17', '3', '16');

-- Add LIMS module/functions
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('18', '5', '17');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('19', '5', '18');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('20', '5', '19');
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES ('21', '5', '20');

-- Modify read/update only permission for BioCollection and Biospecimen
-- UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`='2' WHERE `ID`='76';
-- UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`='2' WHERE `ID`='77';
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`='3' WHERE `ID`='76';
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`='3' WHERE `ID`='77';

-- Add SUBJECT_UPOLOAD create permission to Subject Data Manager
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES ('88', '5', '2', '10', '1');

-- Allow UPDATE for LIMS Data Manager
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`='3' WHERE `ID`='75';
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`='3' WHERE `ID`='78';


