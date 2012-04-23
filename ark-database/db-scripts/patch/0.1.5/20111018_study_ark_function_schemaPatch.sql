-- Add new barcode printer/label functions

-- NB: BARCODE_PRINTER function after being inserted by the following SQL should have ID = 39
INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BARCODE_PRINTER', 'LIMS Admin function to ad/edit barcode printers.', 1, 'tab.module.lims.barcodeprinter');

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (5, 39, 8);

INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 39, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 39, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 39, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 39, 4);

-- NB: BARCODE_LABEL function after being inserted by the following SQL should have ID = 40
INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BARCODE_LABEL', 'LIMS Admin function to ad/edit barcode labels.', 1, 'tab.module.lims.barcodelabel');

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (5, 40, 9);

INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 40, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 40, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 40, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (12, 5, 40, 4);

