SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `lims` ;
CREATE SCHEMA IF NOT EXISTS `lims` DEFAULT CHARACTER SET latin1 ;
DROP SCHEMA IF EXISTS `study` ;
CREATE SCHEMA IF NOT EXISTS `study` ;
USE `lims` ;

-- -----------------------------------------------------
-- Table `lims`.`appointments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`appointments` ;

CREATE  TABLE IF NOT EXISTS `lims`.`appointments` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `PURPOSE` TEXT NULL DEFAULT NULL ,
  `NOTIFY` VARCHAR(100) NULL DEFAULT NULL ,
  `PATIENT_ID` INT(11) NOT NULL ,
  `TIME` DATETIME NULL DEFAULT NULL ,
  `DATE` DATETIME NOT NULL ,
  `ALERT_DATE` DATETIME NULL DEFAULT NULL ,
  `SURVEY_ID` INT(11) NULL DEFAULT NULL ,
  `SENT_TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`attachment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`attachment` ;

CREATE  TABLE IF NOT EXISTS `lims`.`attachment` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `ATTACHEDBY` TEXT NOT NULL ,
  `FILE_NAME` VARCHAR(50) NULL DEFAULT NULL ,
  `COMMENTS` TEXT NULL DEFAULT NULL ,
  `DOMAIN` VARCHAR(50) NOT NULL ,
  `NA` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`barcodeformat`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`barcodeformat` ;

CREATE  TABLE IF NOT EXISTS `lims`.`barcodeformat` (
  `ID` INT(11) NOT NULL DEFAULT '0' ,
  `STUDY_ID` INT(11) NULL DEFAULT NULL ,
  `FIELDNAME` VARCHAR(100) NULL DEFAULT NULL ,
  `CONST` VARCHAR(100) NULL DEFAULT NULL ,
  `TYPE` INT(11) NULL DEFAULT NULL ,
  `LENGTH` INT(11) NULL DEFAULT NULL ,
  `FORMAT` VARCHAR(50) NULL DEFAULT NULL ,
  `ORDER` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_barcode_format_idx` ON `lims`.`barcodeformat` (`STUDY_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`barcodeid_engine`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`barcodeid_engine` ;

CREATE  TABLE IF NOT EXISTS `lims`.`barcodeid_engine` (
  `ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `CLASS` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_barcodeid_engine_study_idx` ON `lims`.`barcodeid_engine` (`STUDY_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`bio_sampletype`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`bio_sampletype` ;

CREATE  TABLE IF NOT EXISTS `lims`.`bio_sampletype` (
  `ID` INT(11) NOT NULL ,
  `SAMPLETYPE` VARCHAR(255) NOT NULL ,
  `SAMPLESUBTYPE` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`collection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`collection` ;

CREATE  TABLE IF NOT EXISTS `lims`.`collection` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `LINK_SUBJECT_STUDY_ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `COLLECTIONDATE` DATETIME NULL DEFAULT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `COMMENTS` TEXT NULL DEFAULT NULL ,
  `HOSPITAL` VARCHAR(50) NULL DEFAULT NULL ,
  `SURGERYDATE` DATETIME NULL DEFAULT NULL ,
  `DIAG_CATEGORY` VARCHAR(50) NULL DEFAULT NULL ,
  `REF_DOCTOR` VARCHAR(50) NULL DEFAULT NULL ,
  `PATIENTAGE` INT(11) NULL DEFAULT NULL ,
  `DISCHARGEDATE` DATETIME NULL DEFAULT NULL ,
  `HOSPITAL_UR` VARCHAR(50) NULL DEFAULT NULL ,
  `DIAG_DATE` DATETIME NULL DEFAULT NULL ,
  `COLLECTIONGROUP_ID` INT(11) NULL DEFAULT NULL ,
  `EPISODE_NUM` VARCHAR(50) NULL DEFAULT NULL ,
  `EPISODE_DESC` VARCHAR(50) NULL DEFAULT NULL ,
  `COLLECTIONGROUP` VARCHAR(50) NULL DEFAULT NULL ,
  `TISSUETYPE` VARCHAR(50) NULL DEFAULT NULL ,
  `TISSUECLASS` VARCHAR(50) NULL DEFAULT NULL ,
  `PATHLABNO` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_collection_link_subject_study`
    FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
    REFERENCES `study`.`study` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study`
    FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
    REFERENCES `study`.`link_subject_study` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_collection_idx` ON `lims`.`collection` (`NAME` ASC) ;

CREATE INDEX `fk_collection_name_idx` ON `lims`.`collection` (`NAME` ASC) ;

CREATE INDEX `fk_collection_study` ON `lims`.`collection` (`LINK_SUBJECT_STUDY_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`inv_site`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`inv_site` ;

CREATE  TABLE IF NOT EXISTS `lims`.`inv_site` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `CONTACT` VARCHAR(50) NULL DEFAULT NULL ,
  `ADDRESS` TEXT NULL DEFAULT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `PHONE` VARCHAR(50) NULL DEFAULT NULL ,
  `LDAP_GROUP` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`inv_tank`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`inv_tank` ;

CREATE  TABLE IF NOT EXISTS `lims`.`inv_tank` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `LOCATION` TEXT NULL DEFAULT NULL ,
  `STATUS` VARCHAR(50) NULL DEFAULT NULL ,
  `SITE_ID` INT(11) NOT NULL ,
  `CAPACITY` INT(11) NULL DEFAULT NULL ,
  `LASTSERVICENOTE` TEXT NULL DEFAULT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `AVAILABLE` INT(11) NULL DEFAULT NULL ,
  `DECOMMISSIONDATE` DATETIME NULL DEFAULT NULL ,
  `COMMISSIONDATE` DATETIME NULL DEFAULT NULL ,
  `LASTSERVICEDATE` DATETIME NULL DEFAULT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_inv_tank_site`
    FOREIGN KEY (`SITE_ID` )
    REFERENCES `lims`.`inv_site` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_inv_tank_site` ON `lims`.`inv_tank` (`SITE_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`inv_tray`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`inv_tray` ;

CREATE  TABLE IF NOT EXISTS `lims`.`inv_tray` (
  `ID` INT(11) NOT NULL ,
  `TANK_ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `AVAILABLE` INT(11) NULL DEFAULT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  `CAPACITY` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_inv_box_tank`
    FOREIGN KEY (`TANK_ID` )
    REFERENCES `lims`.`inv_tank` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_inv_box_tank_idx` ON `lims`.`inv_tray` (`TANK_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`inv_box`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`inv_box` ;

CREATE  TABLE IF NOT EXISTS `lims`.`inv_box` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `NOOFCOL` INT(11) NOT NULL ,
  `COLNOTYPE` VARCHAR(15) NOT NULL ,
  `CAPACITY` INT(11) NULL DEFAULT NULL ,
  `NAME` VARCHAR(50) NULL DEFAULT NULL ,
  `TRAY_ID` INT(11) NOT NULL ,
  `AVAILABLE` INT(11) NULL DEFAULT NULL ,
  `NOOFROW` INT(11) NOT NULL ,
  `ROWNOTYPE` VARCHAR(15) NOT NULL ,
  `TRANSFER_ID` INT(11) NULL DEFAULT NULL ,
  `TYPE` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_inv_tray_box`
    FOREIGN KEY (`TRAY_ID` )
    REFERENCES `lims`.`inv_tray` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_inv_tray_box_idx` ON `lims`.`inv_box` (`TRAY_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`inv_cell`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`inv_cell` ;

CREATE  TABLE IF NOT EXISTS `lims`.`inv_cell` (
  `ID` INT(11) NOT NULL ,
  `BOX_ID` INT(11) NOT NULL ,
  `PATIENT_ID` INT(11) NULL DEFAULT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `ROWNO` INT(11) NULL DEFAULT NULL ,
  `COLNO` INT(11) NULL DEFAULT NULL ,
  `STATUS` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_inv_cell_tray`
    FOREIGN KEY (`BOX_ID` )
    REFERENCES `lims`.`inv_box` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_inv_cell_tray_idx` ON `lims`.`inv_cell` (`BOX_ID` ASC) ;

CREATE INDEX `fk_inv_box_patient_idx` ON `lims`.`inv_cell` (`PATIENT_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`biospecimen`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biospecimen` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biospecimen` (
  `ID` INT(11) NOT NULL ,
  `BIOSPECIMEN_ID` VARCHAR(50) NOT NULL ,
  `STUDY_ID` INT(11) NULL DEFAULT NULL ,
  `LINK_SUBJECT_STUDY_ID` INT(11) NULL DEFAULT NULL ,
  `COLLECTION_ID` INT(11) NULL DEFAULT NULL ,
  `SUBSTUDY_ID` INT(11) NULL DEFAULT NULL ,
  `PARENT_ID` INT(11) NULL DEFAULT NULL ,
  `PARENTID` VARCHAR(50) NULL DEFAULT NULL ,
  `OLD_ID` INT(11) NULL DEFAULT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `OTHERID` VARCHAR(50) NULL DEFAULT NULL ,
  `STORED_IN` VARCHAR(50) NULL DEFAULT NULL ,
  `SAMPLE_TIME` DATETIME NULL DEFAULT NULL ,
  `GRADE` VARCHAR(255) NULL DEFAULT NULL ,
  `CELL_ID` INT(11) NOT NULL ,
  `DEPTH` INT(11) NULL DEFAULT NULL ,
  `SAMPLEDATE` DATETIME NULL DEFAULT NULL ,
  `EXTRACTED_TIME` DATETIME NULL DEFAULT NULL ,
  `LOCATION` VARCHAR(255) NULL DEFAULT NULL ,
  `SAMPLETYPE_ID` INT(11) NOT NULL ,
  `SAMPLETYPE` VARCHAR(255) NOT NULL ,
  `SAMPLESUBTYPE` VARCHAR(255) NULL DEFAULT NULL ,
  `SUBTYPEDESC` VARCHAR(255) NULL DEFAULT NULL ,
  `SPECIES` VARCHAR(255) NULL DEFAULT NULL ,
  `QTY_COLLECTED` DOUBLE NULL DEFAULT NULL ,
  `DATEEXTRACTED` DATETIME NULL DEFAULT NULL ,
  `QTY_REMOVED` DOUBLE NULL DEFAULT NULL ,
  `GESTAT` DOUBLE NULL DEFAULT NULL ,
  `COMMENTS` TEXT NULL DEFAULT NULL ,
  `DATEDISTRIBUTED` DATETIME NULL DEFAULT NULL ,
  `COLLABORATOR` VARCHAR(100) NULL DEFAULT NULL ,
  `DNACONC` DOUBLE NULL DEFAULT NULL ,
  `PURITY` DOUBLE NULL DEFAULT NULL ,
  `ANTICOAG` VARCHAR(100) NULL DEFAULT NULL ,
  `PROTOCOL` VARCHAR(100) NULL DEFAULT NULL ,
  `DNA_BANK` INT(11) NULL DEFAULT NULL ,
  `QUANTITY` INT(11) NULL DEFAULT NULL ,
  `UNITS` VARCHAR(10) NULL DEFAULT NULL ,
  `QUALITY` VARCHAR(100) NULL DEFAULT NULL ,
  `WITHDRAWN` INT(11) NULL DEFAULT NULL ,
  `STATUS` VARCHAR(20) NULL DEFAULT NULL ,
  `TREATMENT` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_biospecimen_study`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_collection`
    FOREIGN KEY (`COLLECTION_ID` )
    REFERENCES `lims`.`collection` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_inv_cell`
    FOREIGN KEY (`CELL_ID` )
    REFERENCES `lims`.`inv_cell` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_biospecimen_collection` ON `lims`.`biospecimen` (`COLLECTION_ID` ASC) ;

CREATE INDEX `fk_biospecimen_biospecimen_idx` ON `lims`.`biospecimen` (`BIOSPECIMEN_ID` ASC) ;

CREATE INDEX `fk_biospecimen_study` ON `lims`.`biospecimen` (`STUDY_ID` ASC) ;

CREATE INDEX `fk_biospecimen_inv_cell` ON `lims`.`biospecimen` (`CELL_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`bio_transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`bio_transaction` ;

CREATE  TABLE IF NOT EXISTS `lims`.`bio_transaction` (
  `ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NULL DEFAULT NULL ,
  `BIOSPECIMEN_ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `TREATMENT` VARCHAR(255) NULL DEFAULT NULL ,
  `SOURCESTUDY_ID` INT(11) NULL DEFAULT NULL ,
  `UNIT` VARCHAR(50) NULL DEFAULT NULL ,
  `DELIVERYDATE` DATETIME NULL DEFAULT NULL ,
  `FIXATIONTIME` VARCHAR(50) NULL DEFAULT NULL ,
  `TRANSACTIONDATE` DATETIME NOT NULL ,
  `QUANTITY` DOUBLE NOT NULL ,
  `OWNER` VARCHAR(255) NULL DEFAULT NULL ,
  `REASON` TEXT NULL DEFAULT NULL ,
  `STATUS` VARCHAR(50) NULL DEFAULT NULL ,
  `STUDY` VARCHAR(50) NULL DEFAULT NULL ,
  `COLLABORATOR` VARCHAR(255) NULL DEFAULT NULL ,
  `RECORDER` VARCHAR(255) NULL DEFAULT NULL ,
  `DESTINATION` VARCHAR(255) NULL DEFAULT NULL ,
  `ACTION` VARCHAR(50) NULL DEFAULT NULL ,
  `TYPE` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_bio_transactions_biospecimen`
    FOREIGN KEY (`BIOSPECIMEN_ID` )
    REFERENCES `lims`.`biospecimen` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_bio_transactions_biospecimen_idx` ON `lims`.`bio_transaction` (`BIOSPECIMEN_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`biodata_field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_field` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_field` (
  `ID` INT(11) NOT NULL ,
  `TYPE_ID` INT(11) NOT NULL ,
  `FORMAT` VARCHAR(20) NULL DEFAULT NULL ,
  `COLUMNNAME` VARCHAR(50) NOT NULL ,
  `UNIT_ID` INT(11) NULL DEFAULT NULL ,
  `LOVTYPE` VARCHAR(20) NULL DEFAULT NULL ,
  `DOMAIN` VARCHAR(50) NULL DEFAULT NULL ,
  `FIELDNAME` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`biodata`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata` (
  `ID` INT(11) NOT NULL ,
  `DOMAIN_ID` INT(11) NULL DEFAULT NULL ,
  `FIELD_ID` INT(11) NOT NULL ,
  `DATE_COLLECTED` DATETIME NOT NULL ,
  `STRING_VALUE` TEXT NULL DEFAULT NULL ,
  `NUMBER_VALUE` INT(11) NULL DEFAULT NULL ,
  `DATE_VALUE` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_biodata_field`
    FOREIGN KEY (`FIELD_ID` )
    REFERENCES `lims`.`biodata_field` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_biodata_domain_idx` ON `lims`.`biodata` (`DOMAIN_ID` ASC) ;

CREATE INDEX `fk_biodata_field_idx` ON `lims`.`biodata` (`FIELD_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`biodata_criteria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_criteria` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_criteria` (
  `ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NULL DEFAULT NULL ,
  `DOMAIN` VARCHAR(50) NULL DEFAULT NULL ,
  `FIELD` VARCHAR(50) NULL DEFAULT NULL ,
  `VALUE` VARCHAR(255) NULL DEFAULT NULL ,
  `DESCRIPTION` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`biodata_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_group` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_group` (
  `ID` INT(11) NOT NULL ,
  `GROUP_NAME` VARCHAR(100) NOT NULL ,
  `DOMAIN` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`biodata_field_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_field_group` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_field_group` (
  `ID` INT(11) NOT NULL ,
  `FIELD_ID` INT(11) NOT NULL ,
  `GROUP_ID` INT(11) NOT NULL ,
  `POSITION` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_biodata_field_group_field`
    FOREIGN KEY (`FIELD_ID` )
    REFERENCES `lims`.`biodata_field` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_biodata_field_group_group`
    FOREIGN KEY (`GROUP_ID` )
    REFERENCES `lims`.`biodata_group` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_biodata_field_group_field_idx` ON `lims`.`biodata_field_group` (`FIELD_ID` ASC) ;

CREATE INDEX `fk_biodata_field_group_group_idx` ON `lims`.`biodata_field_group` (`GROUP_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`biodata_field_lov`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_field_lov` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_field_lov` (
  `ID` INT(11) NOT NULL ,
  `LIST_ID` INT(11) NOT NULL ,
  `VALUE` VARCHAR(50) NOT NULL ,
  `ORDER` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`biodata_group_criteria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_group_criteria` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_group_criteria` (
  `ID` INT(11) NOT NULL ,
  `CRITERIA_ID` INT(11) NOT NULL ,
  `GROUP_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`) ,
  CONSTRAINT `fk_biodata_group_criteria`
    FOREIGN KEY (`CRITERIA_ID` )
    REFERENCES `lims`.`biodata_criteria` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_biodata_group_group`
    FOREIGN KEY (`GROUP_ID` )
    REFERENCES `lims`.`biodata_group` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `fk_biodata_group_criteria_criteria_idx` ON `lims`.`biodata_group_criteria` (`CRITERIA_ID` ASC) ;

CREATE INDEX `fk_biodata_group_criteria_group_idx` ON `lims`.`biodata_group_criteria` (`GROUP_ID` ASC) ;


-- -----------------------------------------------------
-- Table `lims`.`biodata_lov_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_lov_list` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_lov_list` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `DESCRIPTION` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`biodata_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_type` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `DESCRIPTION` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`biodata_unit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`biodata_unit` ;

CREATE  TABLE IF NOT EXISTS `lims`.`biodata_unit` (
  `ID` INT(11) NOT NULL ,
  `UNITNAME` VARCHAR(50) NOT NULL ,
  `DESCRIPTION` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`flag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`flag` ;

CREATE  TABLE IF NOT EXISTS `lims`.`flag` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `DOMAIN` VARCHAR(50) NOT NULL ,
  `REFERENCE_ID` INT(11) NOT NULL ,
  `USER` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`group` ;

CREATE  TABLE IF NOT EXISTS `lims`.`group` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `GROUP_ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(100) NOT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  `ACTIVITY_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`listofvalues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`listofvalues` ;

CREATE  TABLE IF NOT EXISTS `lims`.`listofvalues` (
  `ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NULL DEFAULT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `TYPE` VARCHAR(100) NULL DEFAULT NULL ,
  `VALUE` VARCHAR(100) NULL DEFAULT NULL ,
  `SORTORDER` INT(11) NULL DEFAULT NULL ,
  `GROUP_ID` INT(11) NULL DEFAULT NULL ,
  `DESCRIPTION` VARCHAR(100) NULL DEFAULT NULL ,
  `PARENTTYPE` TEXT NULL DEFAULT NULL ,
  `PARENTVALUE` TEXT NULL DEFAULT NULL ,
  `ISEDITABLE` INT(11) NOT NULL ,
  `LANGUAGE` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`listofvalues_description`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`listofvalues_description` ;

CREATE  TABLE IF NOT EXISTS `lims`.`listofvalues_description` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `TYPE` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` TEXT NOT NULL ,
  `DESC_ID` VARCHAR(55) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`note`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`note` ;

CREATE  TABLE IF NOT EXISTS `lims`.`note` (
  `ID` INT(11) NOT NULL ,
  `DELETED` INT(11) NULL DEFAULT NULL ,
  `TIMESTAMP` VARCHAR(55) NULL DEFAULT NULL ,
  `NAME` VARCHAR(100) NULL DEFAULT NULL ,
  `ELEMENT_ID` INT(11) NOT NULL ,
  `TYPE` VARCHAR(50) NULL DEFAULT NULL ,
  `FILENAME` VARCHAR(50) NULL DEFAULT NULL ,
  `DOMAIN` VARCHAR(50) NULL DEFAULT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  `DATE` DATETIME NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `lims`.`samplecode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lims`.`samplecode` ;

CREATE  TABLE IF NOT EXISTS `lims`.`samplecode` (
  `ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `SAMPLETYPE` VARCHAR(100) NOT NULL ,
  `SAMPLESUBTYPE` VARCHAR(50) NULL DEFAULT NULL ,
  `SAMPLETYPE_ID` INT(11) NULL DEFAULT NULL ,
  `CODE` VARCHAR(4) NULL DEFAULT NULL ,
  `ORDER` INT(11) NULL DEFAULT NULL ,
  `CHILDCODE` VARCHAR(4) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

USE `study` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
