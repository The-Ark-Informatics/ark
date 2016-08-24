
ALTER TABLE `study`.`subject_file` 
CHANGE COLUMN `STUDY_COMP_ID` `STUDY_COMP_ID` INT(11) NOT NULL ;
ALTER TABLE `study`.`subject_file` 
ADD CONSTRAINT `fk_subject_file_study_component`
  FOREIGN KEY (`STUDY_COMP_ID`)
  REFERENCES `study`.`study_comp` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `study`.`subject_file` 
ADD UNIQUE INDEX `uk_link_subject_study_comp` (`LINK_SUBJECT_STUDY_ID` ASC, `STUDY_COMP_ID` ASC);

