/*
 * Data patch to add CRUD permissions for Barcode Printer and Barcode Label functions for the LIMS Administrator
 */
USE study;
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 42, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 42, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 42, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 42, 4);

INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 43, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 43, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 43, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 43, 4);
