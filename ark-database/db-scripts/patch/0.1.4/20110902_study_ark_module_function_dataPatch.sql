USE study;

-- Fix because LIMS Subject Detail tab was deleted by accident (ark_function_id=17, but this row had id=18)
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `SEQUENCE`) VALUES ('18', '5', '17', '1');

-- Delete the correct record to remove the LIMS Coolection tab (i.e. what should be been removed was: ark_function_id=18)
DELETE FROM `study`.`ark_module_function` WHERE `ARK_FUNCTION_ID` = 18;

/*
The instances where this incorrect DELETE occur were from patches:
- 20110901_lims_inventory_schemaPatch.sql; and
- 20110901_study_ark_module_function_dataPatch.sql
i.e. incorrectly deleted the wrong row due to this statement (disabled): 
-- DELETE FROM `study`.`ark_module_function` WHERE ID =18;  
*/
