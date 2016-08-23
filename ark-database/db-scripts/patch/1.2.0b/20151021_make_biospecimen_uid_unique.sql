ALTER TABLE `lims`.`biospecimen` 
DROP INDEX `fk_biospecimen_biospecimen_idx` ,
ADD UNIQUE INDEX `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_UID` ASC, `STUDY_ID` ASC, `LINK_SUBJECT_STUDY_ID` ASC);

