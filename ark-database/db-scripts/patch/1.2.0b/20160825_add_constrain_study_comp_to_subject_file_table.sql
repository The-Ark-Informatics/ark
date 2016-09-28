ALTER TABLE `study`.`subject_file` 
DROP FOREIGN KEY `fk_subject_file_study_comp`;
ALTER TABLE `study`.`subject_file` 
ADD CONSTRAINT `fk_subject_file_study_comp`
  FOREIGN KEY (`STUDY_COMP_ID`)
  REFERENCES `study`.`study_comp` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

