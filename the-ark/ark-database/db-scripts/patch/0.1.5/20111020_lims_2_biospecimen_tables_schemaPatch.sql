USE LIMS;
CREATE TABLE `biospecimen_storage` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `SIZE` double NOT NULL,
  `UNIT_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biospecimen_storage_unit` (`UNIT_ID`),
  CONSTRAINT `fk_biospecimen_storage_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE  TABLE `lims`.`biospecimen_grade` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

CREATE  TABLE `lims`.`biospecimen_quality` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

CREATE  TABLE `lims`.`biospecimen_status` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

CREATE  TABLE `lims`.`biospecimen_species` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

CREATE  TABLE `lims`.`biospecimen_protocol` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

CREATE  TABLE `lims`.`biospecimen_anticoagulant` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

ALTER TABLE `lims`.`biospecimen_storage` DROP FOREIGN KEY `fk_biospecimen_storage_unit` ;
ALTER TABLE `lims`.`biospecimen_storage` CHANGE COLUMN `UNIT_ID` `UNIT_ID` INT(11) NULL  , 
  ADD CONSTRAINT `fk_biospecimen_storage_unit`
  FOREIGN KEY (`UNIT_ID` )
  REFERENCES `lims`.`unit` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
ALTER TABLE `lims`.`biospecimen_storage` CHANGE COLUMN `SIZE` `SIZE` DOUBLE NULL  ;

