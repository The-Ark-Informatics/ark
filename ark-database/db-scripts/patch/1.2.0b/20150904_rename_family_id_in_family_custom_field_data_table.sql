ALTER TABLE `study`.`family_custom_field_data` 
CHANGE COLUMN `FAMILY_ID` `FAMILY_UID` VARCHAR(8) NOT NULL ;

ALTER TABLE `audit`.`aud_family_custom_field_data` 
CHANGE COLUMN `FAMILY_ID` `FAMILY_UID` VARCHAR(8) NULL DEFAULT NULL ;



