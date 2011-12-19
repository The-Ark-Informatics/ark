use study;
ALTER TABLE `study`.`custom_field` ADD COLUMN `CUSTOM_FIELD_LABEL` VARCHAR(255) NULL  AFTER `HAS_DATA` ;

ALTER TABLE `study`.`custom_field_display` DROP COLUMN `CUSTOM_FIELD_LABEL` ;


