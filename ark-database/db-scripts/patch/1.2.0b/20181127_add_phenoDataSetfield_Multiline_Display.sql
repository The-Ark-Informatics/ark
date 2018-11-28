
-- check box
ALTER TABLE `pheno`.`pheno_dataset_field` 
ADD COLUMN `MULTI_LINE_DISPLAY` TINYINT(1) NOT NULL DEFAULT 0 AFTER `ALLOW_MULTIPLE_SELECTION`;


-- audit check box
ALTER TABLE `audit`.`aud_pheno_dataset_field` 
ADD COLUMN `MULTI_LINE_DISPLAY` TINYINT(20) NOT NULL DEFAULT 0 AFTER `ALLOW_MULTIPLE_SELECTION`;


-- increased the text length of audit table for pheno.
ALTER TABLE `audit`.`aud_pheno_dataset_data` 
CHANGE COLUMN `TEXT_DATA_VALUE` `TEXT_DATA_VALUE` TEXT NULL DEFAULT NULL ;






