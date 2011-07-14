SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

SHOW WARNINGS;
SHOW WARNINGS;
DROP SCHEMA IF EXISTS `geno` ;
CREATE SCHEMA IF NOT EXISTS `geno` DEFAULT CHARACTER SET latin1 ;
SHOW WARNINGS;
USE `geno` ;

-- -----------------------------------------------------
-- Table `geno`.`status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`status` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`status` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`collection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`collection` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`collection` (
  `ID` INT NOT NULL ,
  `STUDY_ID` INT NOT NULL ,
  `NAME` VARCHAR(50) NULL DEFAULT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  `STATUS_ID` INT NOT NULL ,
  `START_DATE` DATETIME NULL DEFAULT NULL ,
  `EXPIRY_DATE` DATETIME NULL DEFAULT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`, `STATUS_ID`) ,
  CONSTRAINT `fk_collection_status`
    FOREIGN KEY (`STATUS_ID` )
    REFERENCES `geno`.`status` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_collection_status` ON `geno`.`collection` (`STATUS_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`delimiter_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`delimiter_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`delimiter_type` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`import_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`import_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`import_type` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`marker_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`marker_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`marker_type` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`marker_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`marker_group` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`marker_group` (
  `ID` INT NOT NULL ,
  `STUDY_ID` INT NOT NULL ,
  `MARKER_TYPE_ID` INT NOT NULL ,
  `NAME` VARCHAR(100) NULL DEFAULT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  `VISIBLE` DECIMAL(1,0) NULL DEFAULT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`, `MARKER_TYPE_ID`) ,
  CONSTRAINT `fk_marker_group_marker_type`
    FOREIGN KEY (`MARKER_TYPE_ID` )
    REFERENCES `geno`.`marker_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_marker_group_marker_type` ON `geno`.`marker_group` (`MARKER_TYPE_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`collection_import`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`collection_import` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`collection_import` (
  `ID` INT NOT NULL ,
  `COLLECTION_ID` INT NOT NULL ,
  `MARKER_GROUP_ID` INT NOT NULL ,
  `IMPORT_TYPE_ID` INT NOT NULL ,
  `DELIMITER_TYPE_ID` INT NOT NULL ,
  `START_TIME` DATETIME NULL DEFAULT NULL ,
  `FINISH_TIME` DATETIME NULL DEFAULT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `DELIMITER_TYPE_ID`, `IMPORT_TYPE_ID`, `MARKER_GROUP_ID`, `COLLECTION_ID`) ,
  CONSTRAINT `fk_collection_import_delimiter_type`
    FOREIGN KEY (`DELIMITER_TYPE_ID` )
    REFERENCES `geno`.`delimiter_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_import_collection`
    FOREIGN KEY (`COLLECTION_ID` )
    REFERENCES `geno`.`collection` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_import_import_type`
    FOREIGN KEY (`IMPORT_TYPE_ID` )
    REFERENCES `geno`.`import_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_import_marker_group`
    FOREIGN KEY (`MARKER_GROUP_ID` )
    REFERENCES `geno`.`marker_group` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_collection_import_delimiter_type` ON `geno`.`collection_import` (`DELIMITER_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_collection_import_collection` ON `geno`.`collection_import` (`COLLECTION_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_collection_import_import_type` ON `geno`.`collection_import` (`IMPORT_TYPE_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_collection_import_marker_group` ON `geno`.`collection_import` (`MARKER_GROUP_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`marker`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`marker` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`marker` (
  `ID` INT NOT NULL ,
  `MARKER_GROUP_ID` INT NOT NULL ,
  `NAME` VARCHAR(100) NULL DEFAULT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  `CHROMOSOME` VARCHAR(50) NOT NULL ,
  `POSITION` DECIMAL(65,30) NULL DEFAULT NULL ,
  `GENE` VARCHAR(100) NULL DEFAULT NULL ,
  `MAJOR_ALLELE` VARCHAR(10) NULL DEFAULT NULL ,
  `MINOR_ALLELE` VARCHAR(10) NULL DEFAULT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `MARKER_GROUP_ID`) ,
  CONSTRAINT `fk_marker_marker_group`
    FOREIGN KEY (`MARKER_GROUP_ID` )
    REFERENCES `geno`.`marker_group` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_marker_marker_group` ON `geno`.`marker` (`MARKER_GROUP_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`decode_mask`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`decode_mask` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`decode_mask` (
  `ID` INT NOT NULL ,
  `BIT_POSITION` INT NOT NULL ,
  `MARKER_ID` INT NOT NULL ,
  `COLLECTION_ID` INT NOT NULL ,
  PRIMARY KEY (`ID`, `MARKER_ID`, `COLLECTION_ID`) ,
  CONSTRAINT `fk_decode_mask_marker`
    FOREIGN KEY (`MARKER_ID` )
    REFERENCES `geno`.`marker` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_decode_mask_collection`
    FOREIGN KEY (`COLLECTION_ID` )
    REFERENCES `geno`.`collection` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_decode_mask_marker` ON `geno`.`decode_mask` (`MARKER_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_decode_mask_collection` ON `geno`.`decode_mask` (`COLLECTION_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`encoded_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`encoded_data` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`encoded_data` (
  `ID` INT NOT NULL ,
  `SUBJECT_ID` INT NOT NULL ,
  `COLLECTION_ID` INT NOT NULL ,
  `ENCODED_BIT1` LONGBLOB NULL DEFAULT NULL ,
  `ENCODED_BIT2` LONGBLOB NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `SUBJECT_ID`, `COLLECTION_ID`) ,
  CONSTRAINT `fk_encoded_data_collection`
    FOREIGN KEY (`COLLECTION_ID` )
    REFERENCES `geno`.`collection` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_encoded_data_collection` ON `geno`.`encoded_data` (`COLLECTION_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`file_format`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`file_format` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`file_format` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`meta_data_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`meta_data_type` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`meta_data_type` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`meta_data_field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`meta_data_field` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`meta_data_field` (
  `ID` INT NOT NULL ,
  `STUDY_ID` INT NOT NULL ,
  `META_DATA_TYPE_ID` INT NOT NULL ,
  `NAME` VARCHAR(100) NOT NULL ,
  `DESCRIPTION` TEXT NULL DEFAULT NULL ,
  `UNITS` VARCHAR(50) NULL DEFAULT NULL ,
  `SEQ_NUM` DECIMAL(65,30) NULL DEFAULT NULL ,
  `MIN_VALUE` VARCHAR(100) NULL DEFAULT NULL ,
  `MAX_VALUE` VARCHAR(100) NULL DEFAULT NULL ,
  `DISCRETE_VALUES` VARCHAR(100) NULL DEFAULT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `STUDY_ID`, `META_DATA_TYPE_ID`) ,
  CONSTRAINT `fk_meta_data_field_md_type`
    FOREIGN KEY (`META_DATA_TYPE_ID` )
    REFERENCES `geno`.`meta_data_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_meta_data_field_md_type` ON `geno`.`meta_data_field` (`META_DATA_TYPE_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`meta_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`meta_data` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`meta_data` (
  `ID` INT NOT NULL ,
  `META_DATA_FIELD_ID` INT NOT NULL ,
  `COLLECTION_ID` INT NOT NULL ,
  `VALUE` TEXT NULL DEFAULT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `META_DATA_FIELD_ID`, `COLLECTION_ID`) ,
  CONSTRAINT `fk_meta_data_meta_data_field`
    FOREIGN KEY (`META_DATA_FIELD_ID` )
    REFERENCES `geno`.`meta_data_field` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_meta_data_collection`
    FOREIGN KEY (`COLLECTION_ID` )
    REFERENCES `geno`.`collection` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_meta_data_meta_data_field` ON `geno`.`meta_data` (`META_DATA_FIELD_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_meta_data_collection` ON `geno`.`meta_data` (`COLLECTION_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`marker_meta_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`marker_meta_data` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`marker_meta_data` (
  `ID` INT NOT NULL ,
  `META_DATA_ID` INT NOT NULL ,
  `MARKER_ID` INT NOT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `META_DATA_ID`, `MARKER_ID`) ,
  CONSTRAINT `fk_marker_meta_data_marker`
    FOREIGN KEY (`MARKER_ID` )
    REFERENCES `geno`.`marker` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_marker_meta_data_meta_data`
    FOREIGN KEY (`META_DATA_ID` )
    REFERENCES `geno`.`meta_data` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_marker_meta_data_marker` ON `geno`.`marker_meta_data` (`MARKER_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_marker_meta_data_meta_data` ON `geno`.`marker_meta_data` (`META_DATA_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`subject_marker_meta_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`subject_marker_meta_data` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`subject_marker_meta_data` (
  `ID` INT NOT NULL ,
  `SUBJECT_ID` INT NOT NULL ,
  `META_DATA_ID` INT NOT NULL ,
  `MARKER_ID` INT NOT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `SUBJECT_ID`, `META_DATA_ID`, `MARKER_ID`) ,
  CONSTRAINT `fk_subject_marker_meta_data_meta_data`
    FOREIGN KEY (`META_DATA_ID` )
    REFERENCES `geno`.`meta_data` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_marker_meta_data_marker`
    FOREIGN KEY (`MARKER_ID` )
    REFERENCES `geno`.`marker` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_subject_marker_meta_data_meta_data` ON `geno`.`subject_marker_meta_data` (`META_DATA_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_subject_marker_meta_data_marker` ON `geno`.`subject_marker_meta_data` (`MARKER_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`subject_meta_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`subject_meta_data` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`subject_meta_data` (
  `ID` INT NOT NULL ,
  `SUBJECT_ID` INT NOT NULL ,
  `META_DATA_ID` INT NOT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `SUBJECT_ID`, `META_DATA_ID`) ,
  CONSTRAINT `fk_subject_meta_data_meta_data`
    FOREIGN KEY (`META_DATA_ID` )
    REFERENCES `geno`.`meta_data` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_subject_meta_data_meta_data` ON `geno`.`subject_meta_data` (`META_DATA_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`upload`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`upload` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`upload` (
  `ID` INT NOT NULL ,
  `FILENAME` TEXT NULL DEFAULT NULL ,
  `FILE_FORMAT_ID` INT NOT NULL ,
  `PAYLOAD` LONGBLOB NULL DEFAULT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NOT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  `CHECKSUM` VARCHAR(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `FILE_FORMAT_ID`) ,
  CONSTRAINT `fk_upload_file_format`
    FOREIGN KEY (`FILE_FORMAT_ID` )
    REFERENCES `geno`.`file_format` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_upload_file_format` ON `geno`.`upload` (`FILE_FORMAT_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`upload_collection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`upload_collection` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`upload_collection` (
  `ID` INT NOT NULL ,
  `UPLOAD_ID` INT NOT NULL ,
  `COLLECTION_ID` INT NOT NULL ,
  `USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `INSERT_TIME` DATETIME NULL DEFAULT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `COLLECTION_ID`, `UPLOAD_ID`) ,
  CONSTRAINT `fk_upload_collection_collection`
    FOREIGN KEY (`COLLECTION_ID` )
    REFERENCES `geno`.`collection` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_collection_upload`
    FOREIGN KEY (`UPLOAD_ID` )
    REFERENCES `geno`.`upload` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_upload_collection_collection` ON `geno`.`upload_collection` (`COLLECTION_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_upload_collection_upload` ON `geno`.`upload_collection` (`UPLOAD_ID` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `geno`.`upload_marker_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `geno`.`upload_marker_group` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `geno`.`upload_marker_group` (
  `ID` INT NOT NULL ,
  `UPLOAD_ID` INT NOT NULL ,
  `MARKER_GROUP_ID` INT NOT NULL ,
  `USER_ID` VARCHAR(50) NOT NULL ,
  `INSERT_TIME` DATETIME NULL DEFAULT NULL ,
  `UPDATE_USER_ID` VARCHAR(50) NULL DEFAULT NULL ,
  `UPDATE_TIME` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`, `UPLOAD_ID`, `MARKER_GROUP_ID`) ,
  CONSTRAINT `fk_upload_marker_group_upload`
    FOREIGN KEY (`UPLOAD_ID` )
    REFERENCES `geno`.`upload` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_marker_group_marker_group`
    FOREIGN KEY (`MARKER_GROUP_ID` )
    REFERENCES `geno`.`marker_group` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

SHOW WARNINGS;
CREATE INDEX `fk_upload_marker_group_upload` ON `geno`.`upload_marker_group` (`UPLOAD_ID` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_upload_marker_group_marker_group` ON `geno`.`upload_marker_group` (`MARKER_GROUP_ID` ASC) ;

SHOW WARNINGS;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
