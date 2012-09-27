USE study;
ALTER TABLE `study`.`custom_field_display` CHANGE COLUMN `ALLOW_MULTIPLE_SELECTION` `ALLOW_MULTIPLE_SELECTION` TINYINT(1) NOT NULL DEFAULT 0  ;
UPDATE study.custom_field_display SET allow_multiple_selection = 0 WHERE allow_multiple_selection is null;


