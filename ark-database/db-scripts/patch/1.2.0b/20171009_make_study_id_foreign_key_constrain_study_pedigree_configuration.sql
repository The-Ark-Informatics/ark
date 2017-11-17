ALTER TABLE `study`.`study_pedigree_config` 
CHANGE COLUMN `study_id` `study_id` INT(11) NOT NULL ,
ADD INDEX `fk_study_pedigree_config_study_idx` (`study_id` ASC);
ALTER TABLE `study`.`study_pedigree_config` 
ADD CONSTRAINT `fk_study_pedigree_config_study`
  FOREIGN KEY (`study_id`)
  REFERENCES `study`.`study` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


