ALTER TABLE `lims`.`biospecimen` DROP FOREIGN KEY `fk_biospecimen_study` ;
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `STUDY_ID` `STUDY_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_biospecimen_study`
  FOREIGN KEY (`STUDY_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
