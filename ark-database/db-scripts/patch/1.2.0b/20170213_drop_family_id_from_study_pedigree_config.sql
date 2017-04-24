ALTER TABLE `study`.`study_pedigree_config` 
DROP FOREIGN KEY `fk_study_pedigree_config_family_id`;
ALTER TABLE `study`.`study_pedigree_config` 
DROP COLUMN `family_id`,
DROP INDEX `fk_study_pedigree_config_family_id_idx` ;

ALTER TABLE `audit`.`aud_study_pedigree_config` 
DROP COLUMN `FAMILY_ID`;


