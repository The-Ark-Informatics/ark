use study;
ALTER TABLE `study`.`subject_custom_field_data` ADD COLUMN `CUSTOM_FIELD_DISPLAY_ID` INT NOT NULL  AFTER `LINK_SUBJECT_STUDY_ID` , CHANGE COLUMN `CUSTOM_FIELD_VALUE` `CUSTOM_FIELD_VALUE` TEXT NULL DEFAULT NULL  , 
  ADD CONSTRAINT `FK_CFD_LINK_SUBJECT_STUDY_ID`
  FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `FK_CFD_CUSTOM_FIELD_DISPLAY_ID`
  FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID` )
  REFERENCES `study`.`custom_field_display` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_CFD_LINK_SUBJECT_STUDY_ID` (`LINK_SUBJECT_STUDY_ID` ASC) 
, ADD INDEX `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID` ASC) ;

ALTER TABLE `study`.`subject_custom_field_data` CHANGE COLUMN `CUSTOM_FIELD_VALUE` `DATA_VALUE` TEXT NULL DEFAULT NULL  ;
