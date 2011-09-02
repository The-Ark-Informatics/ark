/*
 * Emergency data patch to fix functionality in ark-0.1.4-SNAPSHOT.war
 * (i.e. these data patches do not require any code changes)
 */

USE study;

-- Fix because LIMS Subject Detail tab was deleted by accident (ark_function_id=17, but this row had id=18)
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES ('18', '5', '17', '1');

-- Delete the correct record to remove the LIMS Collection tab (i.e. what should be been removed was: ark_function_id=18)
DELETE FROM `study`.`ark_module_function` WHERE `ARK_FUNCTION_ID` = 18;

/*
The instances where this incorrect DELETE occur were from patches:
- 20110901_lims_inventory_schemaPatch.sql; and
- 20110901_study_ark_module_function_dataPatch.sql
i.e. incorrectly deleted the wrong row due to this statement (disabled): 
-- DELETE FROM `study`.`ark_module_function` WHERE ID =18;  
*/

-- Verify table was adjusted correctly
SELECT * FROM ark_module_function amf, ark_function af
WHERE af.id = amf.ark_function_id;
