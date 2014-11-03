ALTER TABLE `study`.`link_subject_pedigree` 
ADD INDEX `fk_link_subject_pedigree_subject_in_context_fk_idx` (`LINK_SUBJECT_STUDY_ID` ASC),
ADD INDEX `fk_link_subject_pedigree_parent_relative_fk_idx` (`RELATIVE_ID` ASC),
ADD INDEX `fk_link_subject_pedigree_relationship_fk_idx` (`RELATIONSHIP_ID` ASC);
ALTER TABLE `study`.`link_subject_pedigree` 
ADD CONSTRAINT `fk_link_subject_pedigree_subject_in_context_fk`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`)
  REFERENCES `study`.`link_subject_study` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_link_subject_pedigree_parent_relative_fk`
  FOREIGN KEY (`RELATIVE_ID`)
  REFERENCES `study`.`link_subject_study` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_link_subject_pedigree_relationship_fk`
  FOREIGN KEY (`RELATIONSHIP_ID`)
  REFERENCES `study`.`relationship` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

