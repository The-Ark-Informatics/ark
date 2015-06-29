CREATE TABLE `study`.`family_custom_field_data` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `FAMILY_ID` VARCHAR(8) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` INT(11) NOT NULL,
  `STUDY_ID` INT(11) NOT NULL,
  `TEXT_DATA_VALUE` TEXT NULL,
  `DATE_DATA_VALUE` DATETIME NULL,
  `ERROR_DATA_VALUE` TEXT NULL,
  `NUMBER_DATA_VALUE` DOUBLE NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

ALTER TABLE `study`.`family_custom_field_data` 
ADD INDEX `fk_family_custom_field_custom_field_display_idx` (`CUSTOM_FIELD_DISPLAY_ID` ASC),
ADD INDEX `fk_family_custom_field_data_study_id_idx` (`STUDY_ID` ASC);
ALTER TABLE `study`.`family_custom_field_data` 
ADD CONSTRAINT `fk_family_custom_field_custom_field_display`
  FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`)
  REFERENCES `study`.`custom_field_display` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_family_custom_field_data_study_id`
  FOREIGN KEY (`STUDY_ID`)
  REFERENCES `study`.`study` (`ID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

CREATE TABLE `audit`.`aud_family_custom_field_data` (
  `ID` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` varchar(255) DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` varchar(255) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` bigint(20) DEFAULT NULL,
  `STUDY_ID` bigint(20) DEFAULT NULL,
  `FAMILY_ID` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`ID`,`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

