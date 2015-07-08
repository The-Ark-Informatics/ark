ALTER TABLE `study`.`study_pedigree_config` 
ADD COLUMN `INBREED_ALLOWED` TINYINT(4) NULL AFTER `family_id`;

ALTER TABLE `audit`.`aud_study_pedigree_config` 
ADD COLUMN `INBREED_ALLOWED` TINYINT(4) NULL AFTER `FAMILY_ID`;
