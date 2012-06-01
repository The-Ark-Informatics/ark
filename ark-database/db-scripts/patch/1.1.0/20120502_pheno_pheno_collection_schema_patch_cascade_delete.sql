ALTER TABLE `pheno`.`pheno_collection` DROP FOREIGN KEY `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID` ;
ALTER TABLE `pheno`.`pheno_collection` 
  ADD CONSTRAINT `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `study`.`subject_custom_field_data` DROP FOREIGN KEY `FK_CFD_LINK_SUBJECT_STUDY_ID` ;
ALTER TABLE `study`.`subject_custom_field_data` 
  ADD CONSTRAINT `FK_CFD_LINK_SUBJECT_STUDY_ID`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `study`.`subject_file` DROP FOREIGN KEY `fk_subject_file_subject` ;
ALTER TABLE `study`.`subject_file` 
  ADD CONSTRAINT `fk_subject_file_subject`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `study`.`consent` DROP FOREIGN KEY `fk_link_subject_study` ;
ALTER TABLE `study`.`consent` 
  ADD CONSTRAINT `fk_link_subject_study`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `lims`.`biocollection` DROP FOREIGN KEY `fk_collection_link_subject_study` ;
ALTER TABLE `lims`.`biocollection` 
  ADD CONSTRAINT `fk_collection_link_subject_study`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


ALTER TABLE `lims`.`biospecimen` DROP FOREIGN KEY `fk_biospecimen_biocollection` ;
ALTER TABLE `lims`.`biospecimen` 
  ADD CONSTRAINT `fk_biospecimen_biocollection`
  FOREIGN KEY (`BIOCOLLECTION_ID` )
  REFERENCES `lims`.`biocollection` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `lims`.`biospecimen` DROP FOREIGN KEY `fk_biospecimen_biospecimen` ;
ALTER TABLE `lims`.`biospecimen` 
  ADD CONSTRAINT `fk_biospecimen_biospecimen`
  FOREIGN KEY (`PARENT_ID` )
  REFERENCES `lims`.`biospecimen` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


	


