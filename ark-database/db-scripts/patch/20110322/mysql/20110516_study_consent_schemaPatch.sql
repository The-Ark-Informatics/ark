USE study;

ALTER TABLE `study`.`consent` DROP FOREIGN KEY `fk_subject` ;
ALTER TABLE `study`.`consent` 
  ADD CONSTRAINT `fk_subject`
  FOREIGN KEY (`SUBJECT_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
