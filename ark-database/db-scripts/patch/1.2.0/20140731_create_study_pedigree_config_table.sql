CREATE TABLE `study`.`study_pedigree_config` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `study_id` INT(11) NULL,
  `custom_field_id` INT(11) NULL,
  `dob_allowed` TINYINT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_study_pedigree_config_1_idx` (`study_id` ASC),
  INDEX `fk_study_pedigree_config_custom_field_idx` (`custom_field_id` ASC),
  CONSTRAINT `fk_study_pedigree_config_study`
    FOREIGN KEY (`study_id`)
    REFERENCES `study`.`study` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_pedigree_config_custom_field`
    FOREIGN KEY (`custom_field_id`)
    REFERENCES `study`.`custom_field` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;
