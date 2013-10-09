-- drop schema geno;
-- create schema geno;

DROP TABLE IF EXISTS `geno`.`command`;
CREATE  TABLE `geno`.`command` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NULL ,
  `LOCATION` VARCHAR(255) NULL ,
  `SERVER_URL` VARCHAR(255) NULL ,
  `INPUT_FILE_FORMAT` VARCHAR(255) NULL ,
  `OUTPUT_FILE_FORMAT` VARCHAR(255) NULL COMMENT 'may need error output too i guess or alt output' ,
  PRIMARY KEY (`ID`) )  ENGINE=InnoDB ;

DROP TABLE IF EXISTS `geno`.`process`;
CREATE  TABLE `geno`.`process` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  `INPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `INPUT_FILE_HASH` VARCHAR(255) NULL ,
  `INPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `INPUT_KEPT` BOOLEAN NULL ,
  `INPUT_SERVER` VARCHAR(255) NULL ,
  `OUTPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `OUTPUT_FILE_HASH` VARCHAR(255) NULL ,
  `OUTPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `OUTPUT_KEPT` BOOLEAN NULL ,
  `OUTPUT_SERVER` VARCHAR(255) NULL ,
  `COMMAND_ID` INT NULL COMMENT 'The command is the task/program that will perform the process/transform' ,
  `START_TIME` DATETIME NULL,
  `END_TIME` DATETIME NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;


ALTER TABLE `geno`.`process` 
  ADD CONSTRAINT `fk_process_command`
  FOREIGN KEY (`COMMAND_ID` )
  REFERENCES `geno`.`command` (`ID` )
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX `fk_process_command_idx` (`COMMAND_ID` ASC) ;



DROP TABLE IF EXISTS `geno`.`transformation_template`;
CREATE  TABLE `geno`.`transformation_template` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

/**where do I link a person**/
DROP TABLE IF EXISTS `geno`.`transformation`;
CREATE  TABLE `geno`.`transformation` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

DROP TABLE IF EXISTS `geno`.`transformation_process`;
CREATE  TABLE `geno`.`transformation_process` (
  `ID` INT NOT NULL ,
  `TRANSFORMATION_ID` INT NOT NULL,
  `PROCESS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

ALTER TABLE `geno`.`transformation_process` 
  ADD CONSTRAINT `fk_transformation_process_process`
  FOREIGN KEY (`PROCESS_ID` )
  REFERENCES `geno`.`process` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `fk_transformation_process_transformation`
  FOREIGN KEY (`TRANSFORMATION_ID` )
  REFERENCES `geno`.`transformation` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_transformation_process_process_idx` (`PROCESS_ID` ASC) 
, ADD INDEX `fk_transformation_process_transformation_idx` (`TRANSFORMATION_ID` ASC) ;

/*
DROP TABLE IF EXISTS `geno`.`lss_process`;
/* really could be person*
CREATE  TABLE `geno`.`lss_process` (
  `ID` INT NOT NULL ,
  `TRANSFORMATION_ID` INT NOT NULL,
  `LSS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;
*/

/*L;SS TRANSFORM INSTEAD OF PROCESS...NOT SURE WHICH IS RIGHT?*/
DROP TABLE IF EXISTS `geno`.`lss_transformation`;
/* really could be person*/
CREATE  TABLE `geno`.`lss_transformation` (
  `ID` INT NOT NULL ,
  `TRANSFORMATION_ID` INT NOT NULL,
  `LSS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;


ALTER TABLE `geno`.`lss_transformation` 
  ADD CONSTRAINT `fk_lss_transformation_transformation`
  FOREIGN KEY (`TRANSFORMATION_ID` )
  REFERENCES `geno`.`transformation` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_lss_transformation_transformation_idx` (`TRANSFORMATION_ID` ASC) ;


ALTER TABLE `geno`.`lss_transformation` 
  ADD CONSTRAINT `fk_lss_transformation_lss`
  FOREIGN KEY (`LSS_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_lss_transformation_lss_idx` (`LSS_ID` ASC) ;

/*** NOW DO THE SAME FOR LSS  !!!!!!!!!!!!!!      */




