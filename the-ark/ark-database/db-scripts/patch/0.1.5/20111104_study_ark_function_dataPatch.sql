use study;
DELETE FROM `study`.`ark_role_policy_template` WHERE ark_module_id = (SELECT id FROM `study`.`ark_module` WHERE name = 'LIMS') AND ark_function_id = (SELECT id FROM `study`.`ark_function` WHERE name = 'BARCODE_LABEL');
DELETE FROM `study`.`ark_role_policy_template` WHERE ark_module_id = (SELECT id FROM `study`.`ark_module` WHERE name = 'LIMS') AND ark_function_id = (SELECT id FROM `study`.`ark_function` WHERE name = 'BARCODE_PRINTER');

DELETE FROM `study`.`ark_module_function` WHERE ark_module_id = (SELECT id FROM `study`.`ark_module` WHERE name = 'LIMS') AND ark_function_id = (SELECT id FROM `study`.`ark_function` WHERE name = 'BARCODE_LABEL');
DELETE FROM `study`.`ark_module_function` WHERE ark_module_id = (SELECT id FROM `study`.`ark_module` WHERE name = 'LIMS') AND ark_function_id = (SELECT id FROM `study`.`ark_function` WHERE name = 'BARCODE_PRINTER');

INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BIOSPECIMENUID_TEMPLATE', 'Manage BiospecimenUid templates for the study,', 1, 'tab.module.lims.biospecimenuidtemplate');
INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES ((SELECT id FROM `study`.`ark_module` WHERE name = 'LIMS'), (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOSPECIMENUID_TEMPLATE'), 10);

INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BARCODE_LABEL', 'Manage barcode label definitions the study,', 1, 'tab.module.lims.barcodelabel');
INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES ((SELECT id FROM `study`.`ark_module` WHERE name = 'LIMS'), (SELECT ID FROM `study`.`ark_function` WHERE name = 'BARCODE_LABEL'), 11);

INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BARCODE_PRINTER', 'Manage barcode printers for the study,', 1, 'tab.module.lims.barcodeprinter');
INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES ((SELECT id FROM `study`.`ark_module` WHERE name = 'LIMS'), (SELECT ID FROM `study`.`ark_function` WHERE name = 'BARCODE_PRINTER'), 12);
