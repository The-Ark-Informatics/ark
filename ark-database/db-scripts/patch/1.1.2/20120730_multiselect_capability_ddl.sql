/**

SO...we want to allow the reuse of the same encoded stuff but in some cases make it multi and some cases not.  OK...this needs to go at custom_field display  instead...

ALTER TABLE `study`.`custom_field` ADD COLUMN `ALLOW_MULTIPLE_SELECTION` VARCHAR(45) NULL DEFAULT 0  AFTER `CUSTOM_FIELD_LABEL` ;

ALTER TABLE `study`.`custom_field` DROP COLUMN `ALLOW_MULTIPLE_SELECTION`;

*****/

ALTER TABLE `study`.`custom_field_display` ADD COLUMN `ALLOW_MULTIPLE_SELECTION` TINYINT(1)  NULL DEFAULT 0  AFTER `REQUIRED_MESSAGE` ;

