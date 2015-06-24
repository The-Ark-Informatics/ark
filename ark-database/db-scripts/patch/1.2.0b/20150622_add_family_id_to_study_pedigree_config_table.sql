ALTER TABLE `study`.`study_pedigree_config` 
ADD COLUMN `family_id` INT(11) NULL AFTER `age_allowed`,
ADD INDEX `fk_study_pedigree_config_family_id_idx` (`family_id` ASC);
ALTER TABLE `study`.`study_pedigree_config` 
ADD CONSTRAINT `fk_study_pedigree_config_family_id`
  FOREIGN KEY (`family_id`)
  REFERENCES `study`.`custom_field` (`ID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `audit`.`aud_study_pedigree_config` 
ADD COLUMN `FAMILY_ID` BIGINT(20) NULL AFTER `STUDY_ID`;
