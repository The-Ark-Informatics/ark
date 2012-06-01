# one data row per CFD per subject
ALTER TABLE `study`.`subject_custom_field_data` 
ADD UNIQUE INDEX `UQ_SCFD_CFD_LSS` USING BTREE (`LINK_SUBJECT_STUDY_ID` ASC, `CUSTOM_FIELD_DISPLAY_ID` ASC) ;
