USE `audit`;
--
-- Table structure for table `aud_pheno_dataset_field_display`
--
ALTER TABLE `aud_pheno_dataset_field_display` 
ADD COLUMN `PARENT_PHENO_DATASET_CATEGORY_ID` INT(11) NULL AFTER `PHENO_DATASET_CATEGORY_ID`;

ALTER TABLE `aud_pheno_dataset_field_display` 
CHANGE COLUMN `PHENO_DATASET_FIELD_ID` `PHENO_DATASET_FIELD_ID` BIGINT(20) NULL ;
