USE study;

/* :Proposed Change:
 * Add new columns to the customFieldGroup so that we query it without having any customFields defined
 * Wait for Niv to provide feedback...
ALTER TABLE `study`.`custom_field_group` ADD COLUMN `STUDY_ID` INT AFTER `DESCRIPTION` , ADD COLUMN `ARK_FUNCTION_ID` INT NOT NULL  AFTER `STUDY_ID` , 
  ADD CONSTRAINT `FK_CFGROUP_ARK_FUNCTION_ID`
  FOREIGN KEY (`ARK_FUNCTION_ID` )
  REFERENCES `study`.`ark_function` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_CFGROUP_STUDY_ID`
  FOREIGN KEY (`STUDY_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_CFGROUP_ARK_MODULE_ID` (`ARK_MODULE_ID` ASC) 
, ADD INDEX `FK_CFGROUP_STUDY_ID` (`STUDY_ID` ASC) ;
*/

-- Make customFields associate with a primary arkFunction (e.g. Subject function) rather than the module.
-- Allows support for CFs on any function and was required for LIMS (biocollection vs biospecimen).
ALTER TABLE `study`.`custom_field` DROP FOREIGN KEY `FK_CUSTOM_FIELD_ARK_MODULE_ID` ;
ALTER TABLE `study`.`custom_field` CHANGE COLUMN `ARK_MODULE_ID` `ARK_FUNCTION_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `FK_CUSTOMFIELD_ARK_FUNCTION_ID`
  FOREIGN KEY (`ARK_FUNCTION_ID` )
  REFERENCES `study`.`ark_function` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `NAME` 
, ADD UNIQUE INDEX `NAME` (`NAME` ASC, `STUDY_ID` ASC, `ARK_FUNCTION_ID` ASC) 
, ADD INDEX `FK_CUSTOMFIELD_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID` ASC) 
, DROP INDEX `FK_ARK_MODULE_ID` ;

-- Upgrade existing Subject customFields
UPDATE `study`.`custom_field`
  SET ARK_FUNCTION_ID = 5   -- 5 is the Subject function
WHERE ARK_FUNCTION_ID = 2;	-- 2 used to mean the Subject module


-- Make unitType also be associated with a primary arkFunction (rather than module)
ALTER TABLE `study`.`unit_type` DROP FOREIGN KEY `FK_UNIT_TYPE_ARK_MODULE_ID` ;
ALTER TABLE `study`.`unit_type` CHANGE COLUMN `ARK_MODULE_ID` `ARK_FUNCTION_ID` INT(11) NULL DEFAULT NULL  , 
  ADD CONSTRAINT `FK_UNIT_TYPE_ARK_FUNCTION_ID`
  FOREIGN KEY (`ARK_FUNCTION_ID` )
  REFERENCES `study`.`ark_function` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `NAME_MODULE_UNIQUE` 
, ADD UNIQUE INDEX `NAME_ARK_FUNCTION_UNIQUE` (`NAME` ASC, `ARK_FUNCTION_ID` ASC) 
, ADD INDEX `FK_UNIT_TYPE_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID` ASC) 
, DROP INDEX `FK_UNIT_TYPE_ARK_MODULE_ID` ;

-- Upgrade existing Subject unitType (there probably aren't many if any)
UPDATE `study`.`unit_type`
  SET ARK_FUNCTION_ID = 5   -- 5 is the Subject function
WHERE ARK_FUNCTION_ID = 2;	-- 2 used to mean the Subject module

