USE lims;
ALTER TABLE `lims`.`biospecimenuid_template` 
DROP INDEX `fk_study_study` 
, ADD UNIQUE INDEX `fk_study_study` USING BTREE (`STUDY_ID` ASC) ;
