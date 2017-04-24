/*
 drop schema geno; 
create schema geno;
*/
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
  `PIPELINE_ID` INT NOT NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
/*  `INPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `INPUT_FILE_HASH` VARCHAR(255) NULL ,
  `INPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `INPUT_KEPT` BOOLEAN NULL ,
  `INPUT_SERVER` VARCHAR(255) NULL ,
  `OUTPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `OUTPUT_FILE_HASH` VARCHAR(255) NULL ,
  `OUTPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `OUTPUT_KEPT` BOOLEAN NULL ,
  `OUTPUT_SERVER` VARCHAR(255) NULL ,*/
  `COMMAND_ID` INT NULL COMMENT 'The command is the task/program that will perform the process/transform' ,
  `START_TIME` DATETIME NULL,
  `END_TIME` DATETIME NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;


DROP TABLE IF EXISTS `geno`.`process_input`;
CREATE  TABLE `geno`.`process_input` (
  `ID` INT NOT NULL ,
  `PROCESS_ID` INT NOT NULL ,
  `INPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `INPUT_FILE_HASH` VARCHAR(255) NULL ,
  `INPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `INPUT_KEPT` BOOLEAN NULL ,
  `INPUT_SERVER` VARCHAR(255) NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;


DROP TABLE IF EXISTS `geno`.`process_output`;
CREATE  TABLE `geno`.`process_output` (
  `ID` INT NOT NULL ,
  `PROCESS_ID` INT NOT NULL ,
  `OUTPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `OUTPUT_FILE_HASH` VARCHAR(255) NULL ,
  `OUTPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `OUTPUT_KEPT` BOOLEAN NULL ,
  `OUTPUT_SERVER` VARCHAR(255) NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;


ALTER TABLE `geno`.`process_input` 
  ADD CONSTRAINT `fk_process_input_process`
  FOREIGN KEY (`PROCESS_ID` )
  REFERENCES `geno`.`process` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_process_input_process_idx` (`PROCESS_ID` ASC) ;

ALTER TABLE `geno`.`process_output` 
  ADD CONSTRAINT `fk_process_output_process`
  FOREIGN KEY (`PROCESS_ID` )
  REFERENCES `geno`.`process` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_process_output_process_idx` (`PROCESS_ID` ASC) ;



ALTER TABLE `geno`.`process` 
  ADD CONSTRAINT `fk_process_command`
  FOREIGN KEY (`COMMAND_ID` )
  REFERENCES `geno`.`command` (`ID` )
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX `fk_process_command_idx` (`COMMAND_ID` ASC) ;



DROP TABLE IF EXISTS `geno`.`pipeline_template`;
CREATE  TABLE `geno`.`pipeline_template` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  `IS_TEMPLATE` BOOLEAN NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

/**where do I link a person**/
DROP TABLE IF EXISTS `geno`.`pipeline`;
CREATE  TABLE `geno`.`pipeline` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  `STUDY_ID` INT NOT NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

/*
DROP TABLE IF EXISTS `geno`.`pipeline_process`;
CREATE  TABLE `geno`.`pipeline_process` (
  `ID` INT NOT NULL ,
  `PIPELINE_ID` INT NOT NULL,
  `PROCESS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

ALTER TABLE `geno`.`pipeline_process` 
  ADD CONSTRAINT `fk_pipeline_process_process`
  FOREIGN KEY (`PROCESS_ID` )
  REFERENCES `geno`.`process` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `fk_pipeline_process_pipeline`
  FOREIGN KEY (`PIPELINE_ID` )
  REFERENCES `geno`.`pipeline` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_pipeline_process_process_idx` (`PROCESS_ID` ASC) 
, ADD INDEX `fk_pipeline_process_pipeline_idx` (`PIPELINE_ID` ASC) ;
*/

/*
DROP TABLE IF EXISTS `geno`.`lss_process`;
/* really could be person*
CREATE  TABLE `geno`.`lss_process` (
  `ID` INT NOT NULL ,
  `PIPELINE_ID` INT NOT NULL,
  `LSS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;
*/

/*LSS TRANSFORM INSTEAD OF PROCESS...NOT SURE WHICH IS RIGHT?*/
DROP TABLE IF EXISTS `geno`.`lss_pipeline`;
/* really could be person*/
CREATE  TABLE `geno`.`lss_pipeline` (
  `ID` INT NOT NULL ,
  `PIPELINE_ID` INT NOT NULL,
  `LSS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;


ALTER TABLE `geno`.`lss_pipeline` 
  ADD CONSTRAINT `fk_lss_pipeline_pipeline`
  FOREIGN KEY (`PIPELINE_ID` )
  REFERENCES `geno`.`pipeline` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_lss_pipeline_pipeline_idx` (`PIPELINE_ID` ASC) ;


ALTER TABLE `geno`.`lss_pipeline` 
  ADD CONSTRAINT `fk_lss_pipeline_lss`
  FOREIGN KEY (`LSS_ID` )
  REFERENCES `study`.`link_subject_study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_lss_pipeline_lss_idx` (`LSS_ID` ASC) ;

/* ALTER TABLE `geno`.`pipeline` CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT  ; */


ALTER TABLE `geno`.`process` 
  ADD CONSTRAINT `fk_process_pipeline`
  FOREIGN KEY (`PIPELINE_ID` )
  REFERENCES `geno`.`lss_pipeline` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE
, ADD INDEX `fk_process_pipeline_idx` (`PIPELINE_ID` ASC) ;


ALTER TABLE `reporting`.`search` 
   ADD COLUMN `INCLUDE_GENO` 
   TINYINT NOT NULL DEFAULT 0  AFTER `FINISHTIME` ;



