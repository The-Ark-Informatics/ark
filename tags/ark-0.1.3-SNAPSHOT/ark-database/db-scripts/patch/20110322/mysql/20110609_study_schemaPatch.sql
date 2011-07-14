USE study;

/* Update table definition */
ALTER TABLE `study`.`person` DROP FOREIGN KEY `fk_person_gender_type` , DROP FOREIGN KEY `fk_person_vital_status` ;
ALTER TABLE `study`.`person` CHANGE COLUMN `GENDER_TYPE_ID` `GENDER_TYPE_ID` INT(11) NOT NULL DEFAULT '0',
					CHANGE COLUMN `VITAL_STATUS_ID` `VITAL_STATUS_ID` INT(11) NOT NULL DEFAULT '0'  , 
  ADD CONSTRAINT `fk_person_gender_type` FOREIGN KEY (`GENDER_TYPE_ID` ) REFERENCES `study`.`gender_type` (`ID` )  ON UPDATE CASCADE, 
  ADD CONSTRAINT `fk_person_vital_status` FOREIGN KEY (`VITAL_STATUS_ID` ) REFERENCES `study`.`vital_status` (`ID` ) ON UPDATE CASCADE;

/* Update table definition */
ALTER TABLE `study`.`link_subject_study` DROP FOREIGN KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` ;
ALTER TABLE `study`.`link_subject_study` CHANGE COLUMN `SUBJECT_STATUS_ID` `SUBJECT_STATUS_ID` INT(11) NOT NULL DEFAULT '0'  , 
  ADD CONSTRAINT `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK`
  FOREIGN KEY (`SUBJECT_STATUS_ID` )
  REFERENCES `study`.`subject_status` (`ID` );


/* Update lookup table data */
UPDATE `study`.`vital_status` SET id=0 WHERE name = 'Unknown';
UPDATE `study`.`gender_type` SET id=0 WHERE name = 'Unknown';