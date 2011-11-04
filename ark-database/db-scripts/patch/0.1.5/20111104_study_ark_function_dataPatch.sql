use study;
INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BIOSPECIMENUID_TEMPLATE', 'Manage BiospecimenUid templates for the study,', 1, 'tab.module.lims.biospecimenuidtemplate');
INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BIOSPECIMENUID_TEMPLATE'), 10);

INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BARCODE_LABEL', 'Manage barcode label definitions the study,', 1, 'tab.module.lims.barcodelabel');
INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BARCODE_LABEL'), 11);

INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('BARCODE_PRINTER', 'Manage barcode printers for the study,', 1, 'tab.module.lims.barcodeprinter');
INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (5, (SELECT ID FROM `study`.`ark_function` WHERE name = 'BARCODE_PRINTER'), 12);



