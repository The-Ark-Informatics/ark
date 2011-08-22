use study;
ALTER TABLE `study`.`custom_field` DROP FOREIGN KEY `FK_CUSTOM_FIELD_DATA_TYPE_ID` ;
ALTER TABLE `study`.`custom_field` DROP COLUMN `DATA_TYPE_ID` 
, DROP INDEX `FK_DATA_TYPE_ID` ;


ALTER TABLE `study`.`custom_field` 
DROP INDEX `MODULE_ID_UNIQUE` 
, DROP INDEX `STUDY_ID_UNIQUE` 
, DROP INDEX `NAME_UNIQUE` ;


alter table custom_field add unique(name,study_id,ark_module_id);

ALTER TABLE `study`.`custom_field` CHANGE COLUMN `FIELD_TYPE` `FIELD_TYPE_ID` INT(11) NOT NULL  ;

