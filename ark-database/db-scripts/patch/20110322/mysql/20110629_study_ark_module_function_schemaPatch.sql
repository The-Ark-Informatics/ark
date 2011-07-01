USE study;

ALTER TABLE `study`.`ark_module_function` ADD COLUMN `FUNCTION_SEQUENCE` INT NULL  AFTER `ARK_FUNCTION_ID` ;


UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=1 WHERE `ID`='1';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=2 WHERE `ID`='2';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='3';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='4';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=1 WHERE `ID`='5';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=2 WHERE `ID`='6';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='7';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='8';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=5 WHERE `ID`='9';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=6 WHERE `ID`='10';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=7 WHERE `ID`='11';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=8 WHERE `ID`='12';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=1 WHERE `ID`='22';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=2 WHERE `ID`='13';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='14';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='15';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=5 WHERE `ID`='16';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=6 WHERE `ID`='17';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=1 WHERE `ID`='18';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=2 WHERE `ID`='19';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='20';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='21';



