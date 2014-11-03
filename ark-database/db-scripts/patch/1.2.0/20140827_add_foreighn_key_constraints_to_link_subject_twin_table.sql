ALTER TABLE `study`.`link_subject_twin` 
ADD INDEX `fk_link_subject_twin_first_subject_fk_idx` (`FIRST_SUBJECT` ASC),
ADD INDEX `fk_link_subject_twin_second_subject_fk_idx` (`SECOND_SUBJECT` ASC),
ADD INDEX `fk_link_subject_twin_type_fk_idx` (`TWIN_TYPE_ID` ASC);
ALTER TABLE `study`.`link_subject_twin` 
ADD CONSTRAINT `fk_link_subject_twin_first_subject_fk`
  FOREIGN KEY (`FIRST_SUBJECT`)
  REFERENCES `study`.`link_subject_study` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_link_subject_twin_second_subject_fk`
  FOREIGN KEY (`SECOND_SUBJECT`)
  REFERENCES `study`.`link_subject_study` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_link_subject_twin_type_fk`
  FOREIGN KEY (`TWIN_TYPE_ID`)
  REFERENCES `study`.`twin_type` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
