ALTER TABLE `study`.`study_pedigree_config` 
ADD COLUMN `status_allowed` TINYINT(4) NULL AFTER `dob_allowed`,
ADD COLUMN `age_allowed` TINYINT(4) NULL AFTER `status_allowed`;

