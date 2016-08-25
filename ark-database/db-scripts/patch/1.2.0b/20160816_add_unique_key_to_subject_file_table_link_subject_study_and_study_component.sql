ALTER TABLE `study`.`subject_file` 
CHANGE COLUMN `STUDY_COMP_ID` `STUDY_COMP_ID` INT(11) NOT NULL ,
DROP INDEX `fk_subject_file_study_comp_idx`,
ADD INDEX `fk_subject_file_study_comp_idx` (`STUDY_COMP_ID` ASC),
DROP INDEX `uk_link_subject_study_comp`,
ADD UNIQUE INDEX `uk_link_subject_study_comp` (`LINK_SUBJECT_STUDY_ID` ASC, `STUDY_COMP_ID` ASC);

