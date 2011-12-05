USE study;


INSERT INTO `study`.`ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('MODULE_FUNCTION', 'Allows CRUD operations on the ark_module_function table for the Ark application', 1,
 'tab.module.admin.modulefunction');

UPDATE `study`.`ark_function` SET `ID`=44, `NAME`='MODULE_FUNCTION', `DESCRIPTION`='Allows CRUD operations on the ark_module_function table for the Ark application' WHERE `ID`='45';

INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`) VALUES (45, 'ROLE', 'Allows CRUD operations on user roles.');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`) VALUES (46, 'MODULE_ROLE', 'Allows CRUD operations on ark_module_role table.');

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (7, 44, 3);

UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=1 WHERE `ID`='28';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=2 WHERE `ID`='29';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='30';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='44';
