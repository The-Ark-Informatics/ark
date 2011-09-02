SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `study` ;
CREATE SCHEMA IF NOT EXISTS `study` DEFAULT CHARACTER SET latin1 ;
SHOW WARNINGS;
USE `study` ;

-- -----------------------------------------------------
-- Table `study`.`action_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`action_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`action_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`address_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`address_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`address_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`registration_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`registration_status` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`registration_status` (
  `ID` INT(11) NOT NULL ,
  `REGISTRATION_STATUS` VARCHAR(50) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`gender_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`gender_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`gender_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`marital_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`marital_status` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`marital_status` (
  `ID` INT(11) NOT NULL ,
  `MARTIAL_STATUS` VARCHAR(50) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`title_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`title_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`title_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`vital_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`vital_status` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`vital_status` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`person` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`person` (
  `ID` INT(11) NOT NULL ,
  `FIRST_NAME` VARCHAR(50) NULL DEFAULT NULL ,
  `MIDDLE_NAME` VARCHAR(50) NULL DEFAULT NULL ,
  `LAST_NAME` VARCHAR(50) NULL DEFAULT NULL ,
  `PREFERRED_NAME` VARCHAR(150) NULL DEFAULT NULL ,
  `GENDER_TYPE_ID` INT(11) NOT NULL ,
  `DATE_OF_BIRTH` DATE NULL DEFAULT NULL ,
  `DATE_OF_DEATH` DATE NULL DEFAULT NULL ,
  `REGISTRATION_DATE` DATE NULL DEFAULT NULL ,
  `CAUSE_OF_DEATH` VARCHAR(255) NULL DEFAULT NULL ,
  `MARITAL_STATUS_ID` INT(11) NOT NULL ,
  `VITAL_STATUS_ID` INT(11) NOT NULL ,
  `TITLE_TYPE_ID` INT(11) NOT NULL ,
  `REGISTRATION_STATUS_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `VITAL_STATUS_ID`, `TITLE_TYPE_ID`, `REGISTRATION_STATUS_ID`, `MARITAL_STATUS_ID`, `GENDER_TYPE_ID`) ,
  CONSTRAINT `PERSON_REGISTR_STATUS_FK`
    FOREIGN KEY (`REGISTRATION_STATUS_ID` )
    REFERENCES `study`.`registration_status` (`ID` ),
  CONSTRAINT `PERSON_GENDER_TYPE_FK`
    FOREIGN KEY (`GENDER_TYPE_ID` )
    REFERENCES `study`.`gender_type` (`ID` ),
  CONSTRAINT `PERSON_MARITAL_STATUS_FK`
    FOREIGN KEY (`MARITAL_STATUS_ID` )
    REFERENCES `study`.`marital_status` (`ID` ),
  CONSTRAINT `PERSON_TITLE_TYPE_FK`
    FOREIGN KEY (`TITLE_TYPE_ID` )
    REFERENCES `study`.`title_type` (`ID` ),
  CONSTRAINT `PERSON_VITAL_STATUS_FK`
    FOREIGN KEY (`VITAL_STATUS_ID` )
    REFERENCES `study`.`vital_status` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `PERSON_GENDER_TYPE_FK` ON `study`.`person` (`GENDER_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `PERSON_MARITAL_STATUS_FK` ON `study`.`person` (`MARITAL_STATUS_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `PERSON_VITAL_STATUS_FK` ON `study`.`person` (`VITAL_STATUS_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `PERSON_TITLE_TYPE_FK` ON `study`.`person` (`TITLE_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `PERSON_REGISTR_STATUS_FK` ON `study`.`person` (`REGISTRATION_STATUS_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`address` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`address` (
  `ID` INT(11) NOT NULL ,
  `STREET_ADDRESS` VARCHAR(255) NULL DEFAULT NULL ,
  `SUBURB` VARCHAR(50) NULL DEFAULT NULL ,
  `POST_CODE` VARCHAR(10) NULL DEFAULT NULL ,
  `CITY` VARCHAR(30) NULL DEFAULT NULL ,
  `STATE` VARCHAR(20) NULL DEFAULT NULL ,
  `COUNTRY` VARCHAR(50) NULL DEFAULT NULL ,
  `PERSON_ID` INT(11) NOT NULL ,
  `ADDRESS_STATUS` INT(11) NULL DEFAULT NULL ,
  `ADDRESS_TYPE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `ADDRESS_TYPE_ID`, `PERSON_ID`) ,
  CONSTRAINT `ADDRESS_ADDRESS_TYPE_FK`
    FOREIGN KEY (`ADDRESS_TYPE_ID` )
    REFERENCES `study`.`address_type` (`ID` ),
  CONSTRAINT `ADDRESS_PERSON_FK`
    FOREIGN KEY (`PERSON_ID` )
    REFERENCES `study`.`person` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `ADDRESS_PERSON_FK` ON `study`.`address` (`PERSON_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `ADDRESS_ADDRESS_TYPE_FK` ON `study`.`address` (`ADDRESS_TYPE_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`study_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`study_status` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`study_status` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(25) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`entity_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`entity_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`entity_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`audit_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`audit_history` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`audit_history` (
  `ID` INT(11) NOT NULL ,
  `STUDY_STATUS_ID` INT(11) NOT NULL ,
  `DATE_TIME` DATE NULL DEFAULT NULL ,
  `ACTION_TYPE_ID` INT(11) NOT NULL ,
  `ARK_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `COMMENT` VARCHAR(255) NULL DEFAULT NULL ,
  `ENTITY_TYPE_ID` INT(11) NOT NULL ,
  `ENTITY_ID` INT(11) NULL ,
  PRIMARY KEY (`ID`, `STUDY_STATUS_ID`, `ACTION_TYPE_ID`, `ENTITY_TYPE_ID`) ,
  CONSTRAINT `AUDIT_HISTORY_STUDY_STATUS_FK`
    FOREIGN KEY (`STUDY_STATUS_ID` )
    REFERENCES `study`.`study_status` (`ID` ),
  CONSTRAINT `AUDIT_HISTORY_ACTION_TYPE_FK`
    FOREIGN KEY (`ACTION_TYPE_ID` )
    REFERENCES `study`.`action_type` (`ID` ),
  CONSTRAINT `AUDIT_HISTORY_ENTITY_TYPE_FK`
    FOREIGN KEY (`ENTITY_TYPE_ID` )
    REFERENCES `study`.`entity_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `AUDIT_HISTORY_ACTION_TYPE_FK` ON `study`.`audit_history` (`ACTION_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `AUDIT_HISTORY_STUDY_STATUS_FK` ON `study`.`audit_history` (`STUDY_STATUS_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `AUDIT_HISTORY_ENTITY_TYPE_FK` ON `study`.`audit_history` (`ENTITY_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `AUDIT_HISTORY_ENTITY_ID` ON `study`.`audit_history` (`ENTITY_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`correspondence_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`correspondence_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`correspondence_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  `TYPE_DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`correspondence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`correspondence` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`correspondence` (
  `ID` INT(11) NOT NULL ,
  `DATE_OF_CORRESPONDENCE` DATE NULL DEFAULT NULL ,
  `SUMMARY` VARCHAR(255) NULL DEFAULT NULL ,
  `CORRESPONDENCE_TYPE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `CORRESPONDENCE_TYPE_ID`) ,
  CONSTRAINT `CORRESPONDENCE_CO_TYPE_FK`
    FOREIGN KEY (`CORRESPONDENCE_TYPE_ID` )
    REFERENCES `study`.`correspondence_type` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `CORRESPONDENCE_CO_TYPE_FK` ON `study`.`correspondence` (`CORRESPONDENCE_TYPE_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`data_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`data_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`data_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`study`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`study` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`study` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(150) NULL DEFAULT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `DATE_OF_APPLICATION` DATE NULL DEFAULT NULL ,
  `ESTIMATED_YEAR_OF_COMPLETION` INT(11) NULL DEFAULT NULL ,
  `CHIEF_INVESTIGATOR` VARCHAR(50) NULL DEFAULT NULL ,
  `CO_INVESTIGATOR` VARCHAR(50) NULL DEFAULT NULL ,
  `AUTO_GENERATE_SUBJECT_UID` INT(11) NOT NULL ,
  `SUBJECT_UID_START` INT(11) NULL DEFAULT NULL ,
  `STUDY_STATUS_ID` INT(11) NOT NULL ,
  `SUBJECT_KEY_PREFIX` VARCHAR(20) NULL DEFAULT NULL ,
  `CONTACT_PERSON` VARCHAR(50) NULL DEFAULT NULL ,
  `CONTACT_PERSON_PHONE` VARCHAR(20) NULL DEFAULT NULL ,
  `LDAP_GROUP_NAME` VARCHAR(100) NULL DEFAULT NULL ,
  `AUTO_CONSENT` INT(11) NULL DEFAULT NULL ,
  `SUB_STUDY_BIOSPECIMEN_PREFIX` VARCHAR(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `STUDY_STATUS_ID`) ,
  CONSTRAINT `STUDY_STUDY_STATUS_FK1`
    FOREIGN KEY (`STUDY_STATUS_ID` )
    REFERENCES `study`.`study_status` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `STUDY_STUDY_STATUS_FK1` ON `study`.`study` (`STUDY_STATUS_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`study_comp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`study_comp` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`study_comp` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(100) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`) ,
  CONSTRAINT `STUDY_COMP_STUDY_FK`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `STUDY_COMP_STUDY_FK` ON `study`.`study_comp` (`STUDY_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`document`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`document` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`document` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(100) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `DOCUMENT_CONTENT` BLOB NOT NULL ,
  `STUDY_COMP_ID` INT(11) NOT NULL ,
  `CORRESPONDENCE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `STUDY_COMP_ID`, `CORRESPONDENCE_ID`) ,
  CONSTRAINT `DOCUMENT_STUDY_COMP_FK`
    FOREIGN KEY (`STUDY_COMP_ID` )
    REFERENCES `study`.`study_comp` (`ID` ),
  CONSTRAINT `DOCUMENT_CORRESPONDENCE_FK`
    FOREIGN KEY (`CORRESPONDENCE_ID` )
    REFERENCES `study`.`correspondence` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `DOCUMENT_CORRESPONDENCE_FK` ON `study`.`document` (`CORRESPONDENCE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `DOCUMENT_STUDY_COMP_FK` ON `study`.`document` (`STUDY_COMP_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`domain_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`domain_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`domain_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`email_account_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`email_account_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`email_account_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(50) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`email_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`email_account` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`email_account` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(255) NOT NULL ,
  `PRIMARY_ACCOUNT` INT(11) NULL DEFAULT NULL ,
  `PERSON_ID` INT(11) NOT NULL ,
  `EMAIL_ACCOUNT_TYPE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `PERSON_ID`, `EMAIL_ACCOUNT_TYPE_ID`) ,
  CONSTRAINT `EMAIL_ACCOUNT_EMA_FK1`
    FOREIGN KEY (`EMAIL_ACCOUNT_TYPE_ID` )
    REFERENCES `study`.`email_account_type` (`ID` ),
  CONSTRAINT `EMAIL_ACCOUNT_PER_FK1`
    FOREIGN KEY (`PERSON_ID` )
    REFERENCES `study`.`person` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `EMAIL_ACCOUNT_PER_FK1` ON `study`.`email_account` (`PERSON_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `EMAIL_ACCOUNT_EMA_FK1` ON `study`.`email_account` (`EMAIL_ACCOUNT_TYPE_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`study_site`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`study_site` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`study_site` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `ADDRESS_ID` INT(11) NOT NULL ,
  `DOMAIN_TYPE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `ADDRESS_ID`, `DOMAIN_TYPE_ID`) ,
  CONSTRAINT `STUDY_SITE_DOMAIN_TYPE_FK`
    FOREIGN KEY (`DOMAIN_TYPE_ID` )
    REFERENCES `study`.`domain_type` (`ID` ),
  CONSTRAINT `STUDY_SITE_ADDRES_FK1`
    FOREIGN KEY (`ADDRESS_ID` )
    REFERENCES `study`.`address` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `STUDY_SITE_ADDRES_FK1` ON `study`.`study_site` (`ADDRESS_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `STUDY_SITE_DOMAIN_TYPE_FK` ON `study`.`study_site` (`DOMAIN_TYPE_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`link_site_contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`link_site_contact` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`link_site_contact` (
  `ID` INT(11) NOT NULL ,
  `PERSON_ID` INT(11) NOT NULL ,
  `STUDY_SITE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `PERSON_ID`, `STUDY_SITE_ID`) ,
  CONSTRAINT `LINK_SITE_CONTACT_STUDY_SITE_FK`
    FOREIGN KEY (`STUDY_SITE_ID` )
    REFERENCES `study`.`study_site` (`ID` ),
  CONSTRAINT `LINK_SITE_CONTACT_FK1`
    FOREIGN KEY (`PERSON_ID` )
    REFERENCES `study`.`person` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `LINK_SITE_CONTACT_FK1` ON `study`.`link_site_contact` (`PERSON_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SITE_CONTACT_STUDY_SITE_FK` ON `study`.`link_site_contact` (`STUDY_SITE_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`study_comp_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`study_comp_status` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`study_comp_status` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`link_study_studycomp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`link_study_studycomp` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`link_study_studycomp` (
  `ID` INT(11) NOT NULL ,
  `STUDY_COMP_ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `STUDY_COMP_STATUS_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`, `STUDY_COMP_STATUS_ID`, `STUDY_COMP_ID`) ,
  CONSTRAINT `LSSC_STUDY_COMP_STATUS_FK`
    FOREIGN KEY (`STUDY_COMP_STATUS_ID` )
    REFERENCES `study`.`study_comp_status` (`ID` ),
  CONSTRAINT `LSSC_STUDY_FK`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ),
  CONSTRAINT `LSSC_STUDY_COMP_FK`
    FOREIGN KEY (`STUDY_COMP_ID` )
    REFERENCES `study`.`study_comp` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `LSSC_STUDY_FK` ON `study`.`link_study_studycomp` (`STUDY_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LSSC_STUDY_COMP_FK` ON `study`.`link_study_studycomp` (`STUDY_COMP_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LSSC_STUDY_COMP_STATUS_FK` ON `study`.`link_study_studycomp` (`STUDY_COMP_STATUS_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`link_study_studysite`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`link_study_studysite` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`link_study_studysite` (
  `ID` INT(11) NOT NULL ,
  `STUDY_SITE_ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `STUDY_SITE_ID`, `STUDY_ID`) ,
  CONSTRAINT `LINK_STUDY_STUDYSITE_STUDY_FK`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ),
  CONSTRAINT `LINK_STUDY_STUDYSITE_STUDY_SITE_FK`
    FOREIGN KEY (`STUDY_SITE_ID` )
    REFERENCES `study`.`study_site` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `LINK_STUDY_STUDYSITE_STUDY_SITE_FK` ON `study`.`link_study_studysite` (`STUDY_SITE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_STUDY_STUDYSITE_STUDY_FK` ON `study`.`link_study_studysite` (`STUDY_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`link_study_substudy`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`link_study_substudy` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`link_study_substudy` (
  `ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `SUB_STUDY_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`, `SUB_STUDY_ID`) ,
  CONSTRAINT `LINK_STUDY_SUBSTUDY_SUB_STUDY_FK`
    FOREIGN KEY (`SUB_STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ),
  CONSTRAINT `LINK_STUDY_SUBSTUDY_STUDY_FK`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `LINK_STUDY_SUBSTUDY_STUDY_FK` ON `study`.`link_study_substudy` (`STUDY_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_STUDY_SUBSTUDY_SUB_STUDY_FK` ON `study`.`link_study_substudy` (`SUB_STUDY_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`relationship`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`relationship` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`relationship` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`link_subject_contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`link_subject_contact` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`link_subject_contact` (
  `ID` INT(11) NOT NULL ,
  `PERSON_CONTACT_ID` INT(11) NULL DEFAULT NULL ,
  `PERSON_SUBJECT_ID` INT(11) NULL DEFAULT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `RELATIONSHIP_ID` INT(11) NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`) ,
  CONSTRAINT `LINK_SUBJECT_CONTACT_RELATIONSHIP_FK`
    FOREIGN KEY (`RELATIONSHIP_ID` )
    REFERENCES `study`.`relationship` (`ID` ),
  CONSTRAINT `LINK_SUBJECT_CONTACT_PERSON_FK`
    FOREIGN KEY (`PERSON_CONTACT_ID` )
    REFERENCES `study`.`person` (`ID` ),
  CONSTRAINT `LINK_SUBJECT_CONTACT_PERSON_SUBJECT_FK`
    FOREIGN KEY (`PERSON_SUBJECT_ID` )
    REFERENCES `study`.`person` (`ID` ),
  CONSTRAINT `LINK_SUBJECT_CONTACT_STUDY_FK`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_CONTACT_PERSON_FK` ON `study`.`link_subject_contact` (`PERSON_CONTACT_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_CONTACT_PERSON_SUBJECT_FK` ON `study`.`link_subject_contact` (`PERSON_SUBJECT_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_CONTACT_STUDY_FK` ON `study`.`link_subject_contact` (`STUDY_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_CONTACT_RELATIONSHIP_FK` ON `study`.`link_subject_contact` (`RELATIONSHIP_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`subject_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`subject_status` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`subject_status` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`link_subject_study`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`link_subject_study` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`link_subject_study` (
  `ID` INT(11) NOT NULL ,
  `PERSON_ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `SUBJECT_STATUS_ID` INT(11) NOT NULL ,
  `SUBJECT_UID` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`, `PERSON_ID`, `SUBJECT_STATUS_ID`) ,
  CONSTRAINT `LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK`
    FOREIGN KEY (`SUBJECT_STATUS_ID` )
    REFERENCES `study`.`subject_status` (`ID` ),
  CONSTRAINT `LINK_SUBJECT_STUDY_PERSON_FK`
    FOREIGN KEY (`PERSON_ID` )
    REFERENCES `study`.`person` (`ID` ),
  CONSTRAINT `LINK_SUBJECT_STUDY_STUDY_FK`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_STUDY_PERSON_FK` ON `study`.`link_subject_study` (`PERSON_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_STUDY_STUDY_FK` ON `study`.`link_subject_study` (`STUDY_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` ON `study`.`link_subject_study` (`SUBJECT_STATUS_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_STUDY_SUBJECT_UID` ON `study`.`link_subject_study` (`SUBJECT_UID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`link_subject_studycomp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`link_subject_studycomp` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`link_subject_studycomp` (
  `ID` INT(11) NOT NULL ,
  `PERSON_SUBJECT_ID` INT(11) NOT NULL ,
  `STUDY_COMP_ID` INT(11) NOT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  `STUDY_COMP_STATUS_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `PERSON_SUBJECT_ID`, `STUDY_COMP_ID`, `STUDY_ID`, `STUDY_COMP_STATUS_ID`) ,
  CONSTRAINT `LINK_SSC_STUDY_COMP_STATUS_FK`
    FOREIGN KEY (`STUDY_COMP_STATUS_ID` )
    REFERENCES `study`.`study_comp_status` (`ID` ),
  CONSTRAINT `LINK_SSC_PERSON_FK`
    FOREIGN KEY (`PERSON_SUBJECT_ID` )
    REFERENCES `study`.`person` (`ID` ),
  CONSTRAINT `LINK_SSC_STUDY_COMP_FK`
    FOREIGN KEY (`STUDY_COMP_ID` )
    REFERENCES `study`.`study_comp` (`ID` ),
  CONSTRAINT `LINK_SUBJECT_STUDYCOM_FK3`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `LINK_SSC_PERSON_FK` ON `study`.`link_subject_studycomp` (`PERSON_SUBJECT_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SSC_STUDY_COMP_FK` ON `study`.`link_subject_studycomp` (`STUDY_COMP_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SUBJECT_STUDYCOM_FK3` ON `study`.`link_subject_studycomp` (`STUDY_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `LINK_SSC_STUDY_COMP_STATUS_FK` ON `study`.`link_subject_studycomp` (`STUDY_COMP_STATUS_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`phone_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`phone_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`phone_type` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`phone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`phone` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`phone` (
  `ID` INT(11) NOT NULL ,
  `PHONE_INT` INT(11) NOT NULL ,
  `PERSON_ID` INT(11) NOT NULL ,
  `PHONE_TYPE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `PHONE_INT`, `PERSON_ID`, `PHONE_TYPE_ID`) ,
  CONSTRAINT `PHONE_PHONE_TYPE_FK`
    FOREIGN KEY (`PHONE_TYPE_ID` )
    REFERENCES `study`.`phone_type` (`ID` ),
  CONSTRAINT `PHONE_PERSON_FK`
    FOREIGN KEY (`PERSON_ID` )
    REFERENCES `study`.`person` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `PHONE_PHONE_TYPE_FK` ON `study`.`phone` (`PHONE_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `PHONE_PERSON_FK` ON `study`.`phone` (`PERSON_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`subject_custm_fld`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`subject_custm_fld` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`subject_custm_fld` (
  `ID` INT(11) NOT NULL ,
  `NAME` VARCHAR(20) NOT NULL ,
  `DATA_TYPE_ID` INT(11) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  `STUDY_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `DATA_TYPE_ID`, `STUDY_ID`) ,
  CONSTRAINT `SUBJECT_CUSTM_FLD_STUDY_FK`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` ),
  CONSTRAINT `SUBJECT_CUSTM_FLD_DATA_TYPE_FK`
    FOREIGN KEY (`DATA_TYPE_ID` )
    REFERENCES `study`.`data_type` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `SUBJECT_CUSTM_FLD_DATA_TYPE_FK` ON `study`.`subject_custm_fld` (`DATA_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `SUBJECT_CUSTM_FLD_STUDY_FK` ON `study`.`subject_custm_fld` (`STUDY_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `study`.`subject_cust_fld_dat`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `study`.`subject_cust_fld_dat` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `study`.`subject_cust_fld_dat` (
  `ID` INT(11) NOT NULL ,
  `FIELD_DATA` VARCHAR(4000) NULL DEFAULT NULL ,
  `LINK_SUBJECT_STUDY_ID` INT(11) NOT NULL ,
  `SUBJECT_CUSTM_FLD_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`, `LINK_SUBJECT_STUDY_ID`, `SUBJECT_CUSTM_FLD_ID`) ,
  CONSTRAINT `SCFD_SUBJECT_CUSTOM_FIELD_FK`
    FOREIGN KEY (`SUBJECT_CUSTM_FLD_ID` )
    REFERENCES `study`.`subject_custm_fld` (`ID` ),
  CONSTRAINT `SCFD_LINK_SUBJECT_STUDY_FK`
    FOREIGN KEY (`LINK_SUBJECT_STUDY_ID` )
    REFERENCES `study`.`link_subject_study` (`ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `SCFD_LINK_SUBJECT_STUDY_FK` ON `study`.`subject_cust_fld_dat` (`LINK_SUBJECT_STUDY_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `SCFD_SUBJECT_CUSTOM_FIELD_FK` ON `study`.`subject_cust_fld_dat` (`SUBJECT_CUSTM_FLD_ID` ASC) ;

SHOW WARNINGS;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
