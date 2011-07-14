ALTER TABLE `study`.`consent` DROP FOREIGN KEY `fk_study_comp_status` ;
ALTER TABLE `study`.`consent` CHANGE COLUMN `STUDY_COMP_STATUS_ID` `STUDY_COMP_STATUS_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_study_comp_status`
  FOREIGN KEY (`STUDY_COMP_STATUS_ID` )
  REFERENCES `study`.`study_comp_status` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
