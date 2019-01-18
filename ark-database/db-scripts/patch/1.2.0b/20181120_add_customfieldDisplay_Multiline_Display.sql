
-- check box
ALTER TABLE `study`.`custom_field_display` 
ADD COLUMN `MULTI_LINE_DISPLAY` TINYINT(1) NOT NULL DEFAULT 0 AFTER `ALLOW_MULTIPLE_SELECTION`;

-- audit check box
ALTER TABLE `audit`.`aud_custom_field_display` 
ADD COLUMN `MULTI_LINE_DISPLAY` TINYINT(1) NULL AFTER `CUSTOM_FIELD_GROUP_ID`;

-- increased the text length of audit table f0r subject.
ALTER TABLE `audit`.`aud_subject_custom_field_data` 
CHANGE COLUMN `TEXT_DATA_VALUE` `TEXT_DATA_VALUE` TEXT NULL DEFAULT NULL ;

-- increased the text length of audit table for family.
ALTER TABLE `audit`.`aud_family_custom_field_data` 
CHANGE COLUMN `TEXT_DATA_VALUE` `TEXT_DATA_VALUE` TEXT NULL DEFAULT NULL ;

-- increased the text length of audit table for affection.
ALTER TABLE `audit`.`aud_affection_custom_field_data` 
CHANGE COLUMN `TEXT_DATA_VALUE` `TEXT_DATA_VALUE` TEXT NULL DEFAULT NULL ;

-- increased the text length of audit table for biocollection.
ALTER TABLE `audit`.`aud_biocollection_custom_field_data` 
CHANGE COLUMN `TEXT_DATA_VALUE` `TEXT_DATA_VALUE` TEXT NULL DEFAULT NULL ;

-- increased the text length of audit table for biospecimen.
ALTER TABLE `audit`.`aud_biospecimen_custom_field_data` 
CHANGE COLUMN `TEXT_DATA_VALUE` `TEXT_DATA_VALUE` TEXT NULL DEFAULT NULL ;





