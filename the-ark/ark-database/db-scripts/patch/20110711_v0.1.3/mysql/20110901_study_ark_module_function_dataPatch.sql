-- Remove Collection function from LIMS module
DELETE FROM `study`.`ark_module_function` WHERE ID =18;  
  
-- Update order of tabs in LIMS
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=2 WHERE `ID`='20';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='21';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='32';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=6 WHERE `ID`='34';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=7 WHERE `ID`='35';