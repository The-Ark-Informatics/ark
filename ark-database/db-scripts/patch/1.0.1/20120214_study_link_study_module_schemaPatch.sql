/* Patch to set cascade delete for Study deletion */
USE study;
ALTER TABLE `study`.`link_study_arkmodule` DROP FOREIGN KEY `FK_LINK_STUDY_ARKMODULE_STUDY_ID` ;
ALTER TABLE `study`.`link_study_arkmodule` 
  ADD CONSTRAINT `FK_LINK_STUDY_ARKMODULE_STUDY_ID`
  FOREIGN KEY (`STUDY_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `study`.`ark_user_role` DROP FOREIGN KEY `FK_ARK_USER_ROLE_STUDY_ID` ;
ALTER TABLE `study`.`ark_user_role` 
  ADD CONSTRAINT `FK_ARK_USER_ROLE_STUDY_ID`
  FOREIGN KEY (`STUDY_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `study`.`study` DROP FOREIGN KEY `fk_study_study` ;
ALTER TABLE `study`.`study` 
  ADD CONSTRAINT `fk_study_study`
  FOREIGN KEY (`PARENT_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE SET NULL
  ON UPDATE NO ACTION;

USE lims;
ALTER TABLE `lims`.`biospecimenuid_template` DROP FOREIGN KEY `fk_study_study` ;
ALTER TABLE `lims`.`biospecimenuid_template` 
  ADD CONSTRAINT `fk_study_study`
  FOREIGN KEY (`STUDY_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `lims`.`biocollectionuid_template` 
  ADD CONSTRAINT `fk_biocollectionuid_template_study`
  FOREIGN KEY (`STUDY_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION
, ADD INDEX `fk_biocollectionuid_template_study` (`STUDY_ID` ASC) ;
