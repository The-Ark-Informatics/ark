USE study;
ALTER TABLE `study`.`consent` DROP FOREIGN KEY `fk_subject` ;
ALTER TABLE `study`.`consent` CHANGE COLUMN `SUBJECT_ID` `LINK_SUBJECT_STUDY_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_link_subject_study`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `fk_subject` 
, ADD INDEX `fk_subject` USING BTREE (`LINK_SUBJECT_STUDY_ID` ASC) ;